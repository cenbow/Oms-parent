package com.work.shop.oms.orderop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.bimonitor.service.BIMonitorService;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.erp.service.ErpInterfaceProxy;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.orderop.service.*;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.webservice.ErpWebserviceResultBean;
import com.work.shop.pca.common.ResultData;
import com.work.shop.pca.feign.BgProductService;
import com.work.shop.pca.model.BgGroupBuyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 订单确认服务
 * @author QuYachu
 */
@Service("orderConfirmService")
public class OrderConfirmServiceImpl implements OrderConfirmService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
    BgProductService bgProductService;
	@Resource
	OrderDistributeMapper orderDistributeMapper;
	@Resource
	OrderCustomDefineMapper orderCustomDefineMapper;
	@Resource
	MasterOrderQuestionMapper masterOrderQuestionMapper;
	@Resource(name="masterOrderActionServiceImpl")
	MasterOrderActionService masterOrderActionService;
	@Resource
	MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	DistributeActionService distributeActionService;
	@Resource
	BIMonitorService biMonitorService;
	@Resource
	private ErpInterfaceProxy erpInterfaceProxy;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;
	@Resource
	private OrderReturnMapper orderReturnMapper;
	@Resource
	private DistributeSupplierService distributeSupplierService;
	@Resource
	private OrderDistributeOpService orderDistributeOpService;
	@Resource(name = "orderDistributeProducerJmsTemplate")
	private JmsTemplate orderDistributeJmsTemplate;

	@Resource(name = "orderDistributeService")
	private OrderDistributeService orderDistributeService;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource(name = "orderConfirmProviderJmsTemplate")
	private JmsTemplate orderConfirmJmsTemplate;
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource(name = "orderDistributeEditServiceImpl")
	private OrderDistributeEditService orderDistributeEditService;

	@Resource
	private OrderNormalService orderNormalService;

	@Resource
	private PayService payService;

	@Resource
	private SystemPaymentMapper systemPaymentMapper;

	@Resource
	private MasterOrderPayService masterOrderPayService;

    /**
     * 订单确认
     * @param master
     * @param mbDistributes
     * @param thDistributes
     * @param orderStatus
     * @return ReturnInfo
     */
	private ReturnInfo confirmOrder(MasterOrderInfo master, List<OrderDistribute> mbDistributes, List<OrderDistribute> thDistributes, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null && mbDistributes == null && thDistributes == null) {
			logger.warn("[masterOrderInfo]或[distributes]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distributes]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		if (ConstantValues.METHOD_SOURCE_TYPE.POS.equals(orderStatus.getSource())) {
			orderStatus.setMessage("POS确认：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(orderStatus.getSource())) {
			orderStatus.setMessage("前台确认：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(orderStatus.getSource())) {
			orderStatus.setMessage("ERP订单：" + orderStatus.getMessage());
		} else {
			orderStatus.setMessage("确认：" + orderStatus.getMessage());
		}
		String masterOrderSn = master.getMasterOrderSn();
		MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		logger.debug("订单确认：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		// 有子订单时：子订单确认
		if (StringUtil.isListNotNull(mbDistributes)) {
			info = confirmOrderByMbDistribute(getOrderSns(mbDistributes), mbDistributes, orderStatus, extend.getReviveStt());
			if (Constant.OS_NO == info.getIsOk()) {
				return info;
			}
		}
		if (StringUtil.isListNotNull(thDistributes)) {
			info = confirmOrderByThirdDistribute(getOrderSns(thDistributes), thDistributes, orderStatus, extend.getReviveStt());
			if (Constant.OS_NO == info.getIsOk()) {
				return info;
			}
		}
		// 主订单确认
		if (Constant.order_type_master.equals(orderStatus.getType())) {
			info = confirmOrderByMaster(master.getMasterOrderSn(), master, orderStatus);
		}
		if(info.getIsOk() == Constant.OS_YES && "1".equals(info.getData())){
			return info;
		}
		if (info.getIsOk() == Constant.OS_YES) {
			judgeMasterOrderStatus(masterOrderSn, null, (byte)Constant.OI_ORDER_STATUS_CONFIRMED, orderStatus.getType());
		}
		return info;
	}

	/**
	 * 根据订单号确认订单
	 * @param masterOrderSn 订单号
	 * @param orderStatus 订单状态
	 */
	@Override
	public ReturnInfo confirmOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		logger.info("订单确认：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		MasterOrderInfo master = null;
		if (StringUtil.isNotEmpty(masterOrderSn)) {
			master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		}
		// 检查订单是否可以进行确认
		ReturnInfo tInfo = checkMasterOrderConfirm(master, orderStatus);
		if (tInfo.getIsOk() == Constant.OS_NO) {
			return tInfo;
		}
		List<OrderDistribute> mbDistributes = null;
		List<OrderDistribute> thDistributes = null;
		if (master.getSplitStatus() != (byte) 0) {
			// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria criteria = distributeExample.or();
			criteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			// 未取消的订单
			criteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			// 供应商编码不是MB
			criteria.andSupplierCodeNotEqualTo(Constant.SUPPLIER_TYPE_MB);
			thDistributes = this.orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNotNull(thDistributes)) {
				// 检查可以确认的子订单信息
				ReturnInfo<List<OrderDistribute>> checkInfo = checkDistributeListOrderConfirm(thDistributes);
				thDistributes = checkInfo.getData();
			}
			OrderDistributeExample mbDistributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria mbCriteria = mbDistributeExample.or();
			mbCriteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
            // 未取消的订单
			mbCriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
            // 供应商编码是MB
			mbCriteria.andSupplierCodeEqualTo(Constant.SUPPLIER_TYPE_MB);
			mbDistributes = this.orderDistributeMapper.selectByExample(mbDistributeExample);
			if (StringUtil.isListNotNull(mbDistributes)) {
				// 检查可以确认的子订单信息
				ReturnInfo<List<OrderDistribute>> checkInfo = checkDistributeListOrderConfirm(mbDistributes);
				mbDistributes = checkInfo.getData();
			}
		}
		// 0 主单
		orderStatus.setType(Constant.order_type_master);
		info = confirmOrder(master, mbDistributes, thDistributes, orderStatus);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo confirmOrderByOrderSn(String orderSn, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		logger.info("订单确认：orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);;
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		ReturnInfo tInfo = checkDistributeOrderConfirm(master, distribute, orderStatus);
		if (tInfo.getIsOk() == Constant.OS_NO) {
			return tInfo;
		}
		List<OrderDistribute> mbDistributes = null;
		List<OrderDistribute> thDistributes = null;
		if (Constant.SUPPLIER_TYPE_MB.equals(distribute.getSupplierCode())) {
			// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
			mbDistributes = new ArrayList<OrderDistribute>();
			mbDistributes.add(distribute);
		} else {
			thDistributes = new ArrayList<OrderDistribute>();
			thDistributes.add(distribute);
		}
		orderStatus.setType(Constant.order_type_distribute);
		info = confirmOrder(master, mbDistributes, thDistributes, orderStatus);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo posConfirmOrder(ConsigneeModifyInfo modifyInfo) {
		logger.info("POS订单确认：modifyInfo=" + modifyInfo);
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (modifyInfo == null) {
			info.setMessage("[modifyInfo]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		String orderSn = modifyInfo.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderSn)) {
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if(StringUtil.isBlank(modifyInfo.getPayId())){
			info.setMessage("[modifyInfo.payId]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		logger.debug("POS订单确认：orderSn=" + orderSn + ";modifyInfo=" + modifyInfo);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);;
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		if(StringUtil.isTrimEmpty(modifyInfo.getOrderOutSn())){
			logger.error("订单[" + orderSn + "]待处理外部交易号为空 ! ");
			info.setMessage("订单[" + orderSn + "]待处理外部交易号为空 ! ");
			return info;
		}
		if(distribute.getQuestionStatus().intValue() == Constant.OI_QUESTION_STATUS_QUESTION){
			logger.error("订单[" + orderSn + "]为问题单不可进行确认操作! ");
			info.setMessage("订单[" + orderSn + "]为问题单不可进行确认操作! ");
			return info;
		}
		/*if (modifyInfo.getPayTotalFee() == null || modifyInfo.getPayTotalFee() == 0D) {
			logger.error("订单[" + orderSn + "]付款金额为空! ");
			info.setMessage("订单[" + orderSn + "]付款金额为空! ");
			return info;
		}*/
		String masterOrderSn = distribute.getMasterOrderSn();
		try {
			SystemPayment payment = systemPaymentMapper.selectByPrimaryKey(Byte.valueOf(modifyInfo.getPayId()));
			if (payment == null) {
				logger.error("订单[" + orderSn + "]支付方式[" + modifyInfo.getPayId() + "]不存在!");
				info.setMessage("订单[" + orderSn + "]支付方式[" + modifyInfo.getPayId() + "]不存在!");
				return info;
			}
			MasterOrderPayExample example = new MasterOrderPayExample();
			example.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusEqualTo((byte) Constant.OP_PAY_STATUS_UNPAYED);
			List<MasterOrderPay> masterOrderPays = masterOrderPayMapper.selectByExample(example);
			if (StringUtil.isListNull(masterOrderPays)) {
				logger.error("订单[" + orderSn + "] 支付单不存在未付款支付单,不可进行支付确认操作! ");
				info.setMessage("订单[" + orderSn + "]支付单不存在未付款支付单,不可进行支付确认操作! ");
				return info;
			}
			MasterOrderPay masterOrderPay = masterOrderPays.get(0);
			/*if (Math.abs(modifyInfo.getPayTotalFee().doubleValue() - masterOrderPay.getPayTotalfee().doubleValue()) > 0.005) {
				logger.error("订单[" + orderSn + "]付款金额与支付单金额不一致! ");
				info.setMessage("订单[" + orderSn + "]付款金额与支付单金额不一致! ");
				return info;
			}*/
			// 调用支付接口
			OrderStatus orderStatus = new OrderStatus(masterOrderSn, "POS门店支付", "");
			orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
			orderStatus.setPaySn(masterOrderPay.getMasterPaySn());
			orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.POS);
			orderStatus.setMasterOrderSn(masterOrderSn);
			ReturnInfo tInfo = payService.payStCh(orderStatus);
			if (tInfo.getIsOk() == Constant.OS_NO) {
				logger.error("订单[" + orderSn + "]" + tInfo.getMessage());
				return tInfo;
			}
			OrderDistribute updateDistribute = new OrderDistribute();
			updateDistribute.setOrderSn(orderSn);
			updateDistribute.setUpdateTime(new Date());
			updateDistribute.setOrderOutSn(modifyInfo.getOrderOutSn());
			orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
			MasterOrderAddressInfo orderAddressInfo = this.masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			StringBuffer actionNote = new StringBuffer();
			MasterOrderAddressInfo updateOrderAddressInfo = 
					orderDistributeEditService.editAddressInfo(masterOrderSn, modifyInfo, orderAddressInfo, actionNote);
			updateOrderAddressInfo.setMasterOrderSn(masterOrderSn);
			updateOrderAddressInfo.setUpdateTime(new Date());
			//编辑订单地址
			masterOrderAddressInfoMapper.updateByPrimaryKeySelective(updateOrderAddressInfo);
			MasterOrderInfo masterOrderInfo = new MasterOrderInfo();
			masterOrderInfo.setMasterOrderSn(masterOrderSn);
			masterOrderInfo.setPostscript(modifyInfo.getPostscript());
			masterOrderInfo.setUpdateTime(new Date());
			masterOrderInfo.setOrderOutSn(modifyInfo.getOrderOutSn());
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
			if (StringUtil.isNotNull(modifyInfo.getInvPayee()) || StringUtil.isNotNull(modifyInfo.getInvContent())) {
				MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
				extend.setMasterOrderSn(masterOrderSn);
				extend.setInvPayee(modifyInfo.getInvPayee());
				extend.setInvContent(modifyInfo.getInvContent());
				masterOrderInfoExtendMapper.updateByPrimaryKeySelective(extend);
			}
			// 强制下发
			distributeSupplierService.executeDistribute(distribute, true);
		} catch (Exception e) {
			logger.error("订单[" + orderSn + "]支付确认异常" + e.getMessage(), e);
			DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
			orderAction.setActionUser("POS");
			orderAction.setActionNote("<font style=color:red;>POS确认：错误信息 "+ e.getMessage() + "</font>");
			distributeActionService.saveOrderAction(orderAction);
			info.setMessage("订单[" + orderSn + "]POS支付确认失败，错误信息：" + e.getMessage());
			return info;
		}
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderSn(orderSn);
		orderStatus.setAdminUser("POS");
		orderStatus.setMessage("POS确认:");
		orderStatus.setType(Constant.order_type_distribute);
		asynConfirmOrderByOrderSn(orderStatus);
		info.setIsOk(ConstantValues.YEANDNO.YES);
		info.setMessage("Pos订单确认操作成功！");
		return info;
	}
	
	@Override
	public void asynConfirmOrderByOrderSn(OrderStatus orderStatus) {
		try {
			if (orderStatus == null) {
				return ;
			}
			orderConfirmJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatus)));
		} catch (Exception e) {
			logger.error("订单[" + orderStatus.getOrderSn() + "]异步确认放入队列异常：" + e.getMessage(), e);
		}
		return ;
	}

	@SuppressWarnings("rawtypes")
	private ReturnInfo unConfirmOrder(MasterOrderInfo master,
			List<OrderDistribute> mbDistributes, List<OrderDistribute> thDistributes, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(master == null && mbDistributes == null && thDistributes == null) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		if (ConstantValues.METHOD_SOURCE_TYPE.POS.equals(orderStatus.getSource())) {
			orderStatus.setMessage("POS未确认：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(orderStatus.getSource())) {
			orderStatus.setMessage("前台未确认：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(orderStatus.getSource())) {
			orderStatus.setMessage("ERP未订单：" + orderStatus.getMessage());
		} else {
			orderStatus.setMessage("未确认：" + orderStatus.getMessage());
		}
		String masterOrderSn = master.getMasterOrderSn();
		MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		logger.debug("订单确认：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		// 有子订单时：子订单未确认
		if (StringUtil.isListNotNull(mbDistributes)) {
			info = unConfirmOrderByMbDistribute(getOrderSns(mbDistributes), mbDistributes,
					orderStatus, extend.getReviveStt());
			if (Constant.OS_NO == info.getIsOk()) {
				return info;
			}
		}
		if (StringUtil.isListNotNull(thDistributes)) {
			info = unConfirmOrderByThirdDistribute(getOrderSns(thDistributes), thDistributes, 
					orderStatus, extend.getReviveStt());
			if (Constant.OS_NO == info.getIsOk()) {
				return info;
			}
		}
		// 主订单未确认
		if (Constant.order_type_master.equals(orderStatus.getType())) {
			info = unConfirmOrderByMaster(master.getMasterOrderSn(), master, orderStatus);
		}
		if (info.getIsOk() == Constant.OS_YES) {
			judgeMasterOrderStatus(masterOrderSn, null, (byte)Constant.OI_ORDER_STATUS_UNCONFIRMED, orderStatus.getType());
		}
		return info;
	}

	@SuppressWarnings("rawtypes")
	private ReturnInfo judgeMasterOrderStatus(String masterOrderSn, MasterOrderInfo master, byte orderStatus, String type) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (Constant.order_type_distribute.equals(type)) {
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria criteria = distributeExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn);
			List<OrderDistribute> distributes = this.orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNull(distributes)) {
				info.setMessage("主订单[" + masterOrderSn + "] 下子订单列表为空！");
				return info;
			}
			int count = 0;
			for (OrderDistribute orderDistribute : distributes) {
				if (orderDistribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
					count ++;
					continue;
				}
				if (orderDistribute.getOrderStatus() == orderStatus) {
					count ++;
					continue;
				}
			}
			if (count != distributes.size()) {
				info.setMessage("主订单[" + masterOrderSn + "] 下子订单状态不一致！");
				return info;
			}
		}
		try {
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setOrderStatus(orderStatus);
			if (orderStatus == Constant.OI_ORDER_STATUS_CONFIRMED) {
				updateMaster.setConfirmTime(new Date());
			}
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
		} catch (Exception e) {
			info.setMessage("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage());
			logger.error("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage(), e);
		}
		return info;
	}

	/**
	 * 订单未确认
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo unConfirmOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		logger.debug("订单确认：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);;
		if (master == null) {
			logger.error("订单[" + masterOrderSn + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单[" + masterOrderSn + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		if (master.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage(" 订单[" + masterOrderSn + "]已经取消,订单状态变化刷新重新操作！");
			return info;
		}
		if (master.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			info.setMessage(" 订单[" + masterOrderSn + "]要处于已确定状态");
			return info;
		}
		//masterOrderSn = master.getMasterOrderSn();
		List<OrderDistribute> mbDistributes = null;
		List<OrderDistribute> thDistributes = null;
		if (master.getSplitStatus() != (byte) 0) {
			// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria criteria = distributeExample.or();
			criteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			criteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			criteria.andSupplierCodeNotEqualTo(Constant.SUPPLIER_TYPE_MB);
			thDistributes = this.orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNotNull(thDistributes)) {
				ReturnInfo<List<OrderDistribute>> chaeckInfo = checkDistributeListOrderUnConfirm(thDistributes);
				thDistributes = chaeckInfo.getData();
			}
			OrderDistributeExample mbDistributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria mbCriteria = mbDistributeExample.or();
			mbCriteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			mbCriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			mbCriteria.andSupplierCodeEqualTo(Constant.SUPPLIER_TYPE_MB);
			mbDistributes = this.orderDistributeMapper.selectByExample(mbDistributeExample);
			if (StringUtil.isListNotNull(mbDistributes)) {
				ReturnInfo<List<OrderDistribute>> chaeckInfo = checkDistributeListOrderUnConfirm(mbDistributes);
				mbDistributes = chaeckInfo.getData();
			}
		}
		orderStatus.setType(Constant.order_type_master);
		info = unConfirmOrder(master, mbDistributes, thDistributes, orderStatus);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo unConfirmOrderByOrderSn(String orderSn, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单确认操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单确认操作！");
			return info;
		}
		logger.debug("订单确认：orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);;
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单[" + orderSn + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单未确认操作！");
			info.setMessage("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单未确认操作！");
			return info;
		}
		ReturnInfo tInfo = checkDistributeOrderUnConfirm(master, distribute, orderStatus);
		if (tInfo.getIsOk() == Constant.OS_NO) {
			return tInfo;
		}
		List<OrderDistribute> mbDistributes = null;
		List<OrderDistribute> thDistributes = null;
		if (Constant.SUPPLIER_TYPE_MB.equals(distribute.getSupplierCode())) {
			// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
			mbDistributes = new ArrayList<OrderDistribute>();
			mbDistributes.add(distribute);
		} else {
			thDistributes = new ArrayList<OrderDistribute>();
			thDistributes.add(distribute);
		}
		orderStatus.setType(Constant.order_type_distribute);
		info = unConfirmOrder(master, mbDistributes, thDistributes, orderStatus);
		return info;
	}


	@Resource
	private OrderQuestionService orderQuestionService;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource(name = "groupBuyMessageSummaryJmsTemplate")
	private JmsTemplate groupBuyMessageSummaryJmsTemplate;

	/**
	 * 主订单确认(共用方法)
	 * 
	 * @param masterOrderSn 订单号
	 * @param master 订单信息
	 * @param orderStatus 订单状态对象
	 * @return ReturnInfo
	 */
	private ReturnInfo confirmOrderByMaster(String masterOrderSn, MasterOrderInfo master, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(master == null) {
			logger.warn("[" + masterOrderSn + "]订单不存在，无法确认");
			info.setMessage("[" + masterOrderSn + "]订单不存在，无法确认");
			return info;
		}
		logger.debug("订单确认 masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		try {

			//查询订单扩展，判断是否为团购订单
			MasterOrderInfoExtendExample example = new MasterOrderInfoExtendExample();
			example.createCriteria().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderInfoExtend> masterOrderInfoExtends = masterOrderInfoExtendMapper.selectByExample(example);
			MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtends.get(0);

			//预付款支付，挂团购问题单，尾款支付，挂尾款问题单
			ReturnInfo returnInfo = processGroupBuy(masterOrderInfoExtend, master);
			if (returnInfo.getIsOk() == Constant.OS_NO) {
				info.setData(returnInfo.getData());
				info.setIsOk(Constant.OS_YES);
				info.setMessage("团购问题单处理");
				return info;
			}

			MasterOrderInfo updateMaster = new MasterOrderInfo();
			// 确认
			updateMaster.setOrderStatus((byte)Constant.OI_ORDER_STATUS_CONFIRMED);
			// 确认时间
			updateMaster.setConfirmTime(new Date());
			updateMaster.setMasterOrderSn(masterOrderSn);
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);

			//订单操作记录
			// 库存占用
			if(!OrderAttributeUtil.isPosOrder(master.getSource())) {
//				orderStockJmsTemplate.send(new TextMessageCreator(masterOrderSn));
			}
			if (master.getSplitStatus() == (byte) 0) {
				// 拆单处理
				orderDistributeService.orderDistribute(masterOrderSn);
			}
			// 主订单操作日志表
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote(orderStatus.getMessage());
			masterOrderActionService.insertOrderActionByObj(orderAction);
			logger.debug("订单[" + masterOrderSn + "]确认成功!");
		} catch (Exception e) {
			logger.error("确认订单[" + masterOrderSn + "]失败，错误信息：", e);
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>确认：错误信息 "+ e.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			info.setMessage("确认订单[" + masterOrderSn + "]失败，错误信息：" + e.getMessage());
			return info;
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("[" + masterOrderSn + "]订单确认成功");
		logger.debug("[" + masterOrderSn + "]订单确认成功");
		return info;
	}

	/**
	 * 处理团购订单预付款
	 *
	 * @param masterOrderInfoExtend 订单扩展信息
	 * @param master 订单信息
	 * @return
	 * @author yaokan
	 * @date 2020-07-24 21:00
	 */
	private ReturnInfo processGroupBuy(MasterOrderInfoExtend masterOrderInfoExtend, MasterOrderInfo master) {
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo.setIsOk(Constant.OS_YES);
		if (masterOrderInfoExtend.getGroupId() == null) {
			return returnInfo;
		}
		if (masterOrderInfoExtend.getIsConfirmPay() == -1) {
			return returnInfo;
		}
		//预付款
		if (masterOrderInfoExtend.getIsConfirmPay() == 0) {
			boolean prepayments = processGroupPrepayments(masterOrderInfoExtend, master);
			if(prepayments){
				returnInfo.setData("1");
				return returnInfo;
			}
			returnInfo.setData("1");
			returnInfo.setIsOk(Constant.OS_NO);
			return returnInfo;
		}
		//处理尾款
		if (masterOrderInfoExtend.getIsConfirmPay() == 1) {
			boolean balanceAmount = processGroupBalanceAmount(masterOrderInfoExtend, master);
			if(balanceAmount){
				returnInfo.setData("2");
				return returnInfo;
			}
			returnInfo.setData("2");
			returnInfo.setIsOk(Constant.OS_NO);
			return returnInfo;
		}
		returnInfo.setIsOk(Constant.OS_NO);
		return returnInfo;
	}

	/**
	 * 团购订单处理尾款
	 * @param masterOrderInfoExtend 订单扩展信息
	 * @param master 订单信息
	 * @param null
	 * @return
	 * @author yaokan
	 * @date 2020-07-24 22:48
	 */
	private boolean processGroupBalanceAmount(MasterOrderInfoExtend masterOrderInfoExtend, MasterOrderInfo master) {
		boolean back = true;
		String masterOrderSn = master.getMasterOrderSn();
		MasterOrderPay record = new MasterOrderPay();
		record.setPayStatus(Constant.OI_PAY_STATUS_PAYED);
		record.setPayNote("团购尾款已支付");
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.createCriteria().andMasterOrderSnEqualTo(masterOrderSn);
		masterOrderPayMapper.updateByExampleSelective(record,masterOrderPayExample);

		//运营确认状态改为已确认尾款
		MasterOrderInfoExtend masterOrderInfoExtendNew = new MasterOrderInfoExtend();
		masterOrderInfoExtendNew.setMasterOrderSn(masterOrderSn);
		masterOrderInfoExtendNew.setIsOperationConfirmPay(Constant.OD_ORDER_STATUS_CONFIRMED);
		masterOrderInfoExtendMapper.updateByPrimaryKeySelective(masterOrderInfoExtendNew);

		MasterOrderInfo masterOrderInfoNew = new MasterOrderInfo();
		masterOrderInfoNew.setMasterOrderSn(masterOrderSn);
		masterOrderInfoNew.setPayStatus(Constant.OI_PAY_STATUS_PAYED);
		masterOrderInfoNew.setOrderStatus(Constant.OD_ORDER_STATUS_CONFIRMED);
		masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfoNew);

		//调用问题单改为正常单接口
		List<Integer> integers = new ArrayList<>();
		// 问题单类型、待审核问题单
		integers.add(0);
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setMasterOrderSn(masterOrderSn);
		orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
		orderStatus.setAdminUserId(Constant.OS_STRING_SYSTEM);
		orderStatus.setQuestionTypes(integers);
		orderStatus.setMessage("团购尾款问题单返回正常单");
		orderNormalService.normalOrderByMasterSn(masterOrderSn, orderStatus);

		return false;
	}

	/**
	 * 处理预付款
	 * @param masterOrderInfoExtend 订单扩展信息
	 * @param master 订单信息
	 * @return
	 * @author yaokan
	 * @date 2020-07-24 22:45
	 */
	private boolean processGroupPrepayments(MasterOrderInfoExtend masterOrderInfoExtend, MasterOrderInfo master) {
		boolean back = true;
		String masterOrderSn = master.getMasterOrderSn();
		BigDecimal prepayments = master.getPrepayments();

		//设置支付单为部分付款
		MasterOrderPay record = new MasterOrderPay();
		record.setPayStatus(Byte.valueOf("1"));
		record.setPayNote("预付款");
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.createCriteria().andMasterOrderSnEqualTo(masterOrderSn);
		masterOrderPayMapper.updateByExampleSelective(record, masterOrderPayExample);

		//运营确认状态改为已确认预付款
		MasterOrderInfoExtend masterOrderInfoExtendNew = new MasterOrderInfoExtend();
		masterOrderInfoExtendNew.setMasterOrderSn(masterOrderSn);
		masterOrderInfoExtendNew.setIsOperationConfirmPay(Byte.valueOf("0"));
		masterOrderInfoExtendMapper.updateByPrimaryKeySelective(masterOrderInfoExtendNew);

		//记录团购问题单
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setCode(Constant.QUESTION_CODE_TEN_THOUSAND);
		orderStatus.setMessage("团购问题单");
		orderQuestionService.questionOrderByMasterSn(masterOrderSn, orderStatus);

		MasterOrderInfo masterOrderInfoNew = new MasterOrderInfo();
		masterOrderInfoNew.setMasterOrderSn(masterOrderSn);
		masterOrderInfoNew.setMoneyPaid(prepayments);
		masterOrderInfoNew.setTotalPayable(master.getPayTotalFee().subtract(prepayments));
		masterOrderInfoNew.setPayStatus(Byte.valueOf("1"));
		masterOrderInfoNew.setOrderStatus(Byte.valueOf("0"));
		masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfoNew);

		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		goodsExample.createCriteria().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderGoods> orderGoods = masterOrderGoodsMapper.selectByExample(goodsExample);

		//查询参与团购方式（1：下单参与，2：付预付款参与）
		List<Integer> list = new ArrayList<>();
		list.add(masterOrderInfoExtend.getGroupId());
		ResultData<List<BgGroupBuyInfo>> groupBuyInfos = bgProductService.getGroupBuyInfoBuGroupIds(list);
		Integer type = groupBuyInfos.getData().get(0).getParticipateGroupType();
		if (type == 2) {
			int number = 0;
			for (MasterOrderGoods orderGood : orderGoods) {
				number += orderGood.getGoodsNumber();
			}
			ProductGroupBuyBean productGroupBuyBean = new ProductGroupBuyBean();
			productGroupBuyBean.setId(masterOrderInfoExtend.getGroupId());
			productGroupBuyBean.setMasterOrderSn(masterOrderSn);
			productGroupBuyBean.setOrderAmount(BigDecimal.valueOf(number));
			productGroupBuyBean.setOrderMoney(master.getGoodsAmount());
			String groupBuyOrderMQ = JSONObject.toJSONString(productGroupBuyBean);
			logger.info("团购订单汇总mq下发:" + groupBuyOrderMQ);
			groupBuyMessageSummaryJmsTemplate.send(new TextMessageCreator(groupBuyOrderMQ));
		}
		return false;
	}

	/**
	 * 订单确认(MB供应商使用)
	 * 
	 * @param orderSn
	 * @param distribute
	 * @param define
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ReturnInfo confirmOrderByMbDistribute(List<String> orderSns, List<OrderDistribute> distributes,
			OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法确认");
			info.setMessage("[" + orderSns + "]订单不存在，无法确认");
			return info;
		}
		logger.debug("订单确认 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		String note = orderStatus.getMessage();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			try {
				//同步至ERP
				if(OrderAttributeUtil.doERP(distribute, isrelive)) {
					// 同步至ERP
					ErpWebserviceResultBean erpResultBean = erpInterfaceProxy.confirmOrder(orderSn, 1, null);
					if(erpResultBean.getCode() == 4){
						logger.debug(erpResultBean.toString());
					} else if(erpResultBean.getCode() == 0 || erpResultBean.getCode() > 1){
						logger.error("同步至ERP异常：" + erpResultBean.toString());
//						throw new Exception ("订单" + orderSn + "确认同步至ERP异常：" + erpResultBean.getMessage());
						info.setMessage("订单" + orderSn + "确认同步至ERP异常：" + erpResultBean.getMessage());
						return info;
					}
					logger.info("订单确认同步至ERP成功：" + erpResultBean.toString());
				}
				OrderDistribute updateDistribute = new OrderDistribute();
				// 信息更新保存
				updateDistribute.setOrderStatus((byte)Constant.OI_ORDER_STATUS_CONFIRMED);
				updateDistribute.setConfirmTime(new Date());
				updateDistribute.setOrderSn(orderSn);
				orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
				//订单操作记录
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote(note);
				distributeActionService.saveOrderAction(orderAction);
				info.setIsOk(Constant.OS_YES);
				info.setMessage("订单" + orderSn + "确认成功！");
				// 未下发ERP时调用下发ERP方法 
				if (!OrderAttributeUtil.doERP(distribute, isrelive)) {
					distributeSupplierService.distribute(orderSn);
				} else if (OrderAttributeUtil.doERP(distribute, isrelive)
						&& distribute.getDepotStatus() == Constant.OI_DEPOT_STATUS_UNDEPOTED) {
					// 已下发未分仓订单(POS立即分配除外)，调用分配接口
					if (OrderAttributeUtil.isPosOrder(distribute.getSource())) {
						logger.debug("订单号" + distribute.getOrderSn() + "不需要系统自动异步分配");
					} else {
						orderStatus.setMessage("确认后系统自动异步分配");
						orderDistributeOpService.allocation(orderSn, orderStatus);
					}
				}
			} catch (Exception e) {
				logger.error("确认订单[" + orderSn + "]失败，错误信息：", e);
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote("<font style=color:red;>确认：错误信息 "+ e.getMessage() + "</font>");
				distributeActionService.saveOrderAction(orderAction);
				info.setMessage("确认订单[" + orderSn + "]失败，错误信息：" + e.getMessage());
				return info;
			}
			note = "";
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("["+orderSns+"]订单确认成功");
		logger.debug("["+orderSns+"]订单确认成功");
		return info;
	}

    /**
     * 订单确认(MB供应商使用)
     * @param orderSns 交货单编码列表
     * @param distributes 交货单列表
     * @param orderStatus 订单状态
     * @param isrelive 复活标示 1为复活
     * @return ReturnInfo
     */
	private ReturnInfo confirmOrderByThirdDistribute(List<String> orderSns, List<OrderDistribute> distributes,
			OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法确认");
			info.setMessage("[" + orderSns + "]订单不存在，无法确认");
			return info;
		}
		logger.debug("订单确认 orderSns=" + orderSns + ";orderStatus=" + orderStatus);

		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			if (OrderAttributeUtil.doERP(distribute, isrelive)) {
				
			}
			try {
				OrderDistribute updateDistribute = new OrderDistribute();
				// 订单确认原因保存
				updateDistribute.setOrderStatus((byte)Constant.OI_ORDER_STATUS_CONFIRMED);
				updateDistribute.setConfirmTime(new Date());
				updateDistribute.setOrderSn(orderSn);
				orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
				//订单操作记录
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote(orderStatus.getMessage());
				distributeActionService.saveOrderAction(orderAction);
				logger.debug("订单[" + orderSn + "]确认成功!");
				// 未下发供应商时调用下发方法 
				if (!OrderAttributeUtil.doERP(distribute, isrelive)) {
					distributeSupplierService.executeDistributeByMq(orderSn);
				}
			} catch (Exception e) {
				logger.error("确认订单[" + orderSns + "]失败，错误信息：", e);
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote("<font style=color:red;>确认：错误信息 "+ e.getMessage() + "</font>");
				distributeActionService.saveOrderAction(orderAction);
				info.setMessage("确认订单[" + orderSns + "]失败，错误信息：" + e.getMessage());
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单["+orderSns+"]订单确认成功");
		logger.debug("订单["+orderSns+"]订单确认成功");
		return info;
	}
	
	
	/**
	 * 订单确认(共用方法)
	 * 
	 * @param orderSn
	 * @param distribute
	 * @param define
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ReturnInfo unConfirmOrderByMaster(String masterOrderSn, MasterOrderInfo master, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null) {
			logger.warn("[" + masterOrderSn + "]订单不存在，无法未确认");
			info.setMessage("[" + masterOrderSn + "]订单不存在，无法未确认");
			return info;
		}
		logger.debug("订单确认 masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		try {
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setOrderStatus((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			//订单操作记录
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote(orderStatus.getMessage());
			masterOrderActionService.insertOrderActionByObj(orderAction);
			logger.debug("订单[" + masterOrderSn + "]未确认成功!");
		} catch (Exception e) {
			logger.error("认订单[" + masterOrderSn + "]未确失败，错误信息：", e);
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>未确认：错误信息 "+ e.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			info.setMessage("订单[" + masterOrderSn + "]未确认失败，错误信息：" + e.getMessage());
			return info;
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("[" + masterOrderSn + "]订单未确认成功");
		logger.debug("[" + masterOrderSn + "]订单未确认成功");
		return info;
	}

	/**
	 * 订单未确认(MB供应商使用)
	 * 
	 * @param orderSn
	 * @param distribute
	 * @param define
	 * @param orderStatus
	 * @return
	 */
	private ReturnInfo unConfirmOrderByMbDistribute(List<String> orderSns, List<OrderDistribute> distributes, OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法未确认");
			info.setMessage("[" + orderSns + "]订单不存在，无法未确认");
			return info;
		}
		logger.debug("订单确认 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			logger.debug("订单未确认 orderSn=" + orderSn + ";orderStatus=" + orderStatus);
			/* 执行前提检查 */
			if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
				info.setMessage(" 订单" + orderSn + "要处于已确定状态");
				return info;
			}
			if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				info.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
				return info;
			}
		}
		String note = orderStatus.getMessage();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			try {
				//同步至ERP
				if(OrderAttributeUtil.doERP(distribute, isrelive)) {
					// 同步至ERP
					ErpWebserviceResultBean erpResultBean = erpInterfaceProxy.confirmOrder(orderSn, 2, null);
					if(erpResultBean.getCode() == 4){
						logger.debug(erpResultBean.toString());
					} else if(erpResultBean.getCode() == 0 || erpResultBean.getCode() > 1){
						logger.error("同步至ERP异常：" + erpResultBean.toString());
						throw new Exception ("订单" + orderSn + "未确认同步至ERP异常：" + erpResultBean.getMessage());
					}
					logger.info("订单未确认同步至ERP成功：" + erpResultBean.toString());
				}
				OrderDistribute updateDistribute = new OrderDistribute();
				// 信息更新保存
				updateDistribute.setOrderStatus((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
				updateDistribute.setOrderSn(orderSn);
				orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
				//订单操作记录
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote(note);
				distributeActionService.saveOrderAction(orderAction);
				info.setIsOk(Constant.OS_YES);
				info.setMessage("订单" + orderSn + "未确认成功！");
			} catch (Exception e) {
				logger.error("订单[" + orderSn + "]未确认失败，错误信息：", e);
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote("<font style=color:red;>未确认：错误信息 "+ e.getMessage() + "</font>");
				distributeActionService.saveOrderAction(orderAction);
				info.setMessage("订单[" + orderSn + "]未确认失败，错误信息：" + e.getMessage());
				return info;
			}
			note = "";
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("["+orderSns+"]订单未确认成功");
		logger.debug("["+orderSns+"]订单未确认成功");
		return info;
	}

	/**
	 * 订单未确认(MB供应商使用)
	 * 
	 * @param orderSn
	 * @param distribute
	 * @param define
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ReturnInfo unConfirmOrderByThirdDistribute(List<String> orderSns, List<OrderDistribute> distributes, OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法确认");
			info.setMessage("[" + orderSns + "]订单不存在，无法确认");
			return info;
		}
		logger.debug("订单确认 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			logger.debug("订单未确认 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
			if(OrderAttributeUtil.doERP(distribute, isrelive)) {
				
			}
			try {
				OrderDistribute updateDistribute = new OrderDistribute();
				// 信息更新保存
				updateDistribute.setOrderStatus((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
				// 订单确认原因保存
				updateDistribute.setOrderSn(orderSn);
				orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
				//订单操作记录
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote(orderStatus.getMessage());
				distributeActionService.saveOrderAction(orderAction);
				logger.debug("订单[" + orderSn + "]未确认成功!");
			} catch (Exception e) {
				logger.error("订单[" + orderSns + "]未确认失败，错误信息：", e);
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(orderStatus.getAdminUser());
				orderAction.setActionNote("<font style=color:red;>未确认：错误信息 "+ e.getMessage() + "</font>");
				distributeActionService.saveOrderAction(orderAction);
				info.setMessage("订单[" + orderSns + "]未确认失败，错误信息：" + e.getMessage());
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单["+orderSns+"]未确认成功");
		logger.debug("订单["+orderSns+"]未确认成功");
		return info;
	}
	
	private List<String> getOrderSns(List<OrderDistribute> distributes) {
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		return orderSns;
	}
	
	/**
	 * 检查订单是否可以进行确认操作
	 * @param master 订单信息
	 * @param orderStatus 订单状态
	 * @return ReturnInfo
	 */
	private ReturnInfo checkMasterOrderConfirm(MasterOrderInfo master, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		String orderSn = master.getMasterOrderSn();

		if (master.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			info.setMessage(" 订单[" + orderSn + "]要处于未确定状态");
			return info;
		}
		if(orderStatus.getOrderFlag() == 0){
			if (master.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
				info.setMessage(" 订单[" + orderSn + "]要处于正常单状态");
				return info;
			}
		}
		if (master.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage(" 订单" + orderSn + "已经取消,订单状态变化刷新重新操作！");
			return info;
		}
		/*if (master.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| master.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| master.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			info.setMessage(" 订单[" + orderSn + "]要处于未发货.部分发货状态！");
			return info;
		}*/
		// 订单支付状态检查
		if (!OrderAttributeUtil.isUpdateModifyFlag((int)master.getTransType(), (int)master.getPayStatus())) {
			info.setMessage("订单[" + orderSn + "]要处于已付款状态！否则不能进行确认操作！");
			return info;
		}
		OrderReturn orderReturn = null;
		/* 有关联订单时  如果是换货单并且存在换货退货单*/
		if (StringUtil.isNotEmpty(master.getRelatingReturnSn())
				&& master.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			if (null == orderReturn) {
				OrderReturnExample example = new OrderReturnExample();
				example.or().andReturnSnEqualTo(master.getRelatingReturnSn());
				// 退单信息
				List<OrderReturn> returns = orderReturnMapper.selectByExample(example);
				// 退单扩展信息
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(master.getRelatingReturnSn());
				if (CollectionUtils.isEmpty(returns) || orderReturnShip == null) {
					info.setMessage("换货单[" + orderSn + "]关联的退单信息缺失！");
					return info;
				}
				orderReturn = returns.get(0);
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(master.getRelatingReturnSn());
			if (orderReturnShip == null) {
				info.setMessage("换货单[" + orderSn + "[关联的退单Ship信息缺失！");
				return info;
			}
			if (orderReturn.getReturnType() == Constant.OR_RETURN_TYPE_RETURN
					&& orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()) {
				info.setMessage("换货单[" + orderSn + "]的退货单货物暂未入库，不能确认！请返回");
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	@SuppressWarnings("rawtypes")
	private ReturnInfo checkDistributeOrderConfirm(MasterOrderInfo master, OrderDistribute distribute, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		String orderSn = distribute.getOrderSn();
		/* 执行前提检查 */
		if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			info.setMessage(" 订单[" + orderSn + "]要处于未确定状态");
			return info;
		}
		if (distribute.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
			info.setMessage(" 订单[" + orderSn + "]要处于正常单状态");
			return info;
		}
		if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage(" 订单" + orderSn + "已经取消,订单状态变化刷新重新操作！");
			return info;
		}
		/*if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			info.setMessage(" 订单[" + orderSn + "]要处于未发货.部分发货状态！");
			return info;
		}*/
		// 订单支付状态检查
		if (!OrderAttributeUtil.isUpdateModifyFlag((int)distribute.getTransType(), (int)master.getPayStatus())) {
			info.setMessage("订单[" + orderSn + "]要处于已付款状态！否则不能进行确认操作！");
			return info;
		}
		OrderReturn orderReturn = null;
		/* 有关联订单时  如果是换货单并且存在换货退货单*/
		if (StringUtil.isNotEmpty(distribute.getRelatingReturnSn())
				&& distribute.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			if (null == orderReturn) {
				OrderReturnExample example = new OrderReturnExample();
				example.or().andReturnSnEqualTo(distribute.getRelatingReturnSn());
				List<OrderReturn> returns = orderReturnMapper.selectByExample(example);
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(distribute.getRelatingReturnSn());
				if (CollectionUtils.isEmpty(returns) || orderReturnShip == null) {
					info.setMessage("换货单[" + orderSn + "]关联的退单信息缺失！");
					return info;
				}
				orderReturn = returns.get(0);
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(distribute.getRelatingReturnSn());
			if (orderReturnShip == null) {
				info.setMessage("换货单[" + orderSn + "[关联的退单Ship信息缺失！");
				return info;
			}
			if (orderReturn.getReturnType() == Constant.OR_RETURN_TYPE_RETURN
					&& orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()) {
				info.setMessage("换货单[" + orderSn + "]的退货单货物暂未入库，不能确认！请返回");
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	
	@SuppressWarnings("rawtypes")
	private ReturnInfo checkDistributeOrderUnConfirm(MasterOrderInfo master, OrderDistribute distribute, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		String orderSn = distribute.getOrderSn();
		logger.debug("订单未确认 orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		/* 执行前提检查 */
		if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			info.setMessage(" 订单" + orderSn + "要处于已确定状态");
			return info;
		}
		if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage(" 订单" + orderSn + "已经取消,订单状态变化刷新重新操作！");
			return info;
		}
		if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			info.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return info;
		}
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	/**
	 * 检查子订单是否可以确认
	 * @param distributes
	 * @return ReturnInfo<List<OrderDistribute>>
	 */
	private ReturnInfo<List<OrderDistribute>> checkDistributeListOrderConfirm(List<OrderDistribute> distributes) {
		ReturnInfo<List<OrderDistribute>> info = new ReturnInfo<List<OrderDistribute>>(Constant.OS_NO);

		List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();

			if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CONFIRMED) {
				info.setMessage(" 订单[" + orderSn + "]要处于未确定状态");
				continue;
			}
			if (distribute.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
				info.setMessage(" 订单[" + orderSn + "]要处于正常单状态");
				continue;
			}
			if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
				info.setMessage(" 订单" + orderSn + "已经取消,订单状态变化刷新重新操作！");
				return info;
			}
			/*if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				info.setMessage(" 订单[" + orderSn + "]要处于未发货.部分发货状态！");
				continue;
			}*/
			OrderReturn orderReturn = null;
			/* 有关联订单时  如果是换货单并且存在换货退货单*/
			if (StringUtil.isNotEmpty(distribute.getRelatingReturnSn())
					&& distribute.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
				if (null == orderReturn) {
					OrderReturnExample example = new OrderReturnExample();
					example.or().andReturnSnEqualTo(distribute.getRelatingReturnSn());
					List<OrderReturn> returns = orderReturnMapper.selectByExample(example);
					OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(distribute.getRelatingReturnSn());
					if (CollectionUtils.isEmpty(returns) || orderReturnShip == null) {
						info.setMessage("换货单[" + orderSn + "]关联的退单信息缺失！");
						continue;
					}
					orderReturn = returns.get(0);
				}
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(distribute.getRelatingReturnSn());
				if (orderReturnShip == null) {
					info.setMessage("换货单[" + orderSn + "[关联的退单Ship信息缺失！");
					continue;
				}
				if (orderReturn.getReturnType() == Constant.OR_RETURN_TYPE_RETURN
						&& orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()) {
					info.setMessage("换货单[" + orderSn + "]的退货单货物暂未入库，不能确认！请返回");
					continue;
				}
			}
			updateDistributes.add(distribute);
		}
		info.setData(updateDistributes);
		info.setIsOk(Constant.OS_YES);
		return info;
	}

	private ReturnInfo<List<OrderDistribute>> checkDistributeListOrderUnConfirm(List<OrderDistribute> distributes) {
		ReturnInfo<List<OrderDistribute>> info = new ReturnInfo<List<OrderDistribute>>(Constant.OS_NO);
		/* 执行前提检查 */
		List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			/* 执行前提检查 */
			if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
				info.setMessage(" 订单" + orderSn + "要处于已确定状态");
				continue;
			}
			if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
				info.setMessage(" 订单" + orderSn + "已经取消,订单状态变化刷新重新操作！");
				return info;
			}
			if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				info.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
				return info;
			}
			updateDistributes.add(distribute);
		}
		info.setData(updateDistributes);
		info.setIsOk(Constant.OS_YES);
		return info;
	}

	/**
	 *  订单改价确认
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 */
	@Override
	public ReturnInfo changePriceConfirmOrder(String masterOrderSn, OrderStatus orderStatus,List<MasterOrderQuestion> orderQuestionList) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单改价确认！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单改价确认！");
			return info;
		}
		logger.info("订单改价确认：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);

		//2020-07-14 发版逻辑
		MasterOrderInfo master = null;

		//可改价的问题单
		boolean priceEideFlag = false;
		if (StringUtil.isNotEmpty(masterOrderSn)) {
			master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(orderQuestionList != null && orderQuestionList.size() >0){
				for(MasterOrderQuestion orderQuestion:orderQuestionList){
					if(Constant.EDIT_PRICE_QUESTION_NO.indexOf(orderQuestion.getQuestionCode()+"_") != -1){
						priceEideFlag = true;
					}
				};
			}
		}

		//检查订单改价情况
		if( master != null && priceEideFlag && master.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2){
			//未确认
			//各单据价格验证

			// 订单支付状态检查
			if (!OrderAttributeUtil.isUpdateModifyFlag((int)master.getTransType(), (int)master.getPayStatus())) {
				//未付款订单设置付款单最后支付期限
				masterOrderPayService.updateBymasterOrderSnLastTime(master.getMasterOrderSn());
			}

			//设置平台确认
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			// 确认
			updateMaster.setPriceChangeStatus(Constant.PRICE_CHANGE_AFFIRM_2);
			updateMaster.setMasterOrderSn(masterOrderSn);
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			// 主订单操作日志表
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("价格变动确认！ "+orderStatus.getMessage() != null ? orderStatus.getMessage():"");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			//订单确认
			confirmOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "自动确认", null));
		}else{
			logger.info("订单:" + masterOrderSn + " 不符合改价确认条件 ! ");
		}

		//2020-07-14 发版之前的逻辑  由于之前只有特殊商品会进行改价   在改变逻辑之后 正常商品的盈合问题单也是可以改价的
		/*MasterOrderInfo master = null;
		if (StringUtil.isNotEmpty(masterOrderSn)) {
			master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		}

		//检查订单改价情况
		if(master.getGoodsSaleType() != null && master.getPriceChangeStatus() != null
				&& master.getGoodsSaleType() != Constant.GOODS_SALE_TYPE_STANDARD && master.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2){
			//未确认
			//各单据价格验证

			// 订单支付状态检查
			if (!OrderAttributeUtil.isUpdateModifyFlag((int)master.getTransType(), (int)master.getPayStatus())) {
				//未付款订单设置付款单最后支付期限
				masterOrderPayService.updateBymasterOrderSnLastTime(master.getMasterOrderSn());
			}

			//设置平台确认
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			// 确认
			updateMaster.setPriceChangeStatus(Constant.PRICE_CHANGE_AFFIRM_2);
			updateMaster.setMasterOrderSn(masterOrderSn);
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			// 主订单操作日志表
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("价格变动确认！ "+orderStatus.getMessage() != null ? orderStatus.getMessage():"");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			//订单确认
			confirmOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "自动确认", null));
		}else{
			logger.info("订单:" + masterOrderSn + " 不符合改价确认条件 ! ");
		}*/
		info.setIsOk(Constant.OS_YES);
		info.setMessage("[" + masterOrderSn + "]订单改价确认成功");
		return info;
	}
}
