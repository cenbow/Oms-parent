package com.work.shop.oms.orderReturn.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderRefund;
import com.work.shop.oms.bean.OrderRefundExample;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.OrderReturnShipExample;
import com.work.shop.oms.bean.OrderSettleBill;
import com.work.shop.oms.bean.OrderSettleBillExample;
import com.work.shop.oms.bean.ReturnForward;
import com.work.shop.oms.bean.ReturnForwardExample;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.bean.SystemPaymentWithBLOBs;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.ConstantValues.ORDER_RETURN_CHECKINSTATUS;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnGoods;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ReturnOrder;
import com.work.shop.oms.common.bean.ReturnOrderGoods;
import com.work.shop.oms.common.bean.ReturnPay;
import com.work.shop.oms.config.service.OrderCustomDefineService;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsDetailMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderRefundMapper;
import com.work.shop.oms.dao.OrderReturnActionMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.OrderSettleBillMapper;
import com.work.shop.oms.dao.ReturnForwardMapper;
import com.work.shop.oms.dao.SystemMsgTemplateMapper;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderMonitorService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.param.bean.LstSfSchTaskExecOosDtls;
import com.work.shop.oms.param.bean.SfSchTaskExecOosInfo;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.service.MessageService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.ReturnOrderParam;
import com.work.shop.oms.vo.SettleBillQueue;
import com.work.shop.oms.vo.StorageGoods;


@Service("orderReturnStService")
public class OrderReturnStServiceImpl implements OrderReturnStService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OrderReturnMapper orderReturnMapper;
	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private OrderSettleService orderSettleService;
	@Resource
	private OrderReturnGoodsMapper orderReturnGoodsMapper;
	@Resource
	private OrderRefundMapper orderRefundMapper;

	@Resource
	private PayService payService;

	@Resource
	private OrderActionService orderActionService;
	@Resource
	private SystemMsgTemplateMapper systemMsgTemplateMapper;
	@Resource
	private MessageService messageService;
	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;
	@Resource(name = "orderMonitorServiceImpl")
	private OrderMonitorService orderMonitorService;
	@Resource
	private SystemPaymentMapper systemPaymentMapper;
	@Resource
	private OrderBillListMapper orderBillListMapper;
	@Resource
	private OrderSettleBillMapper orderSettleBillMapper;
	@Resource
	private OrderReturnActionMapper orderReturnActionMapper;
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	private ReturnForwardMapper returnForwardMapper;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	@Resource(name = "returnInputProviderJmsTemplate")
	private JmsTemplate jmsTemplateForReturnInput;
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	@Resource
	private OrderCustomDefineService orderCustomDefineService;
	@Resource
	private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
	@Resource
	private JmsSendQueueService jmsSendQueueService;
	
	/**
	 * 退单入库
	 * @param returnOrderParam
	 */
	@Override
	public ReturnInfo<String> returnOrderStorage(ReturnOrderParam returnOrderParam) {
		logger.info("退单入库 request:"+JSON.toJSONString(returnOrderParam));
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单入库失败");
		returnInfo.setReturnSn(returnOrderParam.getReturnSn());
		// 退单号
		String returnSn = returnOrderParam.getReturnSn();
		// 操作人
		String actionUser = returnOrderParam.getUserName();
		// 退货入库商品
		List<StorageGoods> storageGoodsList = returnOrderParam.getStorageGoods();
		// 日志说明
		String actionNote = returnOrderParam.getActionNote();
		try {
			//校验退单数据
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if (orderReturn == null) {
				throw new RuntimeException("无法获取有效退单信息");
			}
			if (orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY) {
				throw new RuntimeException("退单无效无法进行操作");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if (orderReturnShip == null){
				throw new RuntimeException("无法获取有效地退单发货信息");
			}
			if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS && StringUtils.isBlank(orderReturnShip.getReturnExpress())) {
				throw new RuntimeException("失货退货单" + returnSn + "承运商不存在");
			}
			if (orderReturnShip.getCheckinStatus().intValue() == ConstantValues.YESORNO_YES) {
				throw new RuntimeException("退单已入库");
			}
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
			if (master == null) {
				throw new RuntimeException("无法获取关联订单信息");
			}
			if (StringUtils.isEmpty(master.getUserId())) {
				throw new RuntimeException("无法获取有效的订单用户ID信息");
			}
			MasterOrderInfo exchange = null;
			if (StringUtils.isNotBlank(orderReturn.getNewOrderSn())) {
				exchange = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getNewOrderSn());
				if (exchange == null) {
					throw new RuntimeException("退单关联的换货单号不存在或不在三个月范围内");
				}
				if (exchange.getOrderType().intValue() != ConstantValues.ORDERRETURN_RELATIN_ORDER_TYPE.EXCHANGE.intValue()) {
					throw new RuntimeException("退货单所关联换货单["+orderReturn.getNewOrderSn()+"]非换货单类型");
				}
			}
			//验证退货单数据完整性：-入库、结算ERP
			if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()) {
				ReturnInfo<String> checkReturn = orderSettleService.checkReturnSettle(returnSn);
				if(checkReturn.getIsOk() < ConstantValues.YESORNO_YES){
					throw new RuntimeException("退货单入库验证失败："+checkReturn.getMessage());
				}
			}
			// 校验商品信息
			OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
			boolean lastPullInGood = true;
			
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if (CollectionUtils.isEmpty(returnGoodsList)) {
				throw new RuntimeException("退货商品列表为空");
			}
			StringBuffer buff = new StringBuffer();
			if (!returnOrderParam.isPullInAll()) {
				for (StorageGoods storageGoods : storageGoodsList) {
					buff.append(storageGoods.getId());
					buff.append(",");
					if(storageGoods.getProdScanNum() <= 0) {
						throw new RuntimeException("商品入库数量不能小于或等于0");
					}
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(storageGoods.getId());
					if (null == returnGood) {
						throw new RuntimeException("入库商品" + storageGoods.getId() + "不存在！");
					} else {
						if (returnGood.getCheckinStatus().intValue() == ConstantValues.ORDERRETURN_ORDER_TYPE.YES.intValue()) {
							throw new RuntimeException("退单：" + returnSn + "中商品goodsSn:" + returnGood.getGoodsSn() + "已经是入库状态！");
						}
						updateReturnGoods.setId(returnGood.getId());
						if (storageGoods.getProdScanNum() + returnGood.getProdscannum() >= returnGood.getGoodsReturnNumber()) {
							//商品行--商品全部入库
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
							updateReturnGoods.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.byteValue());
						} else {
							//商品行--商品部分入库
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT.byteValue());
							updateReturnGoods.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT.byteValue());
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.byteValue());
						}
						updateReturnGoods.setCheckinTime(new Date());
						updateReturnGoods.setProdscannum(BigDecimal.valueOf(storageGoods.getProdScanNum() + returnGood.getProdscannum()).shortValue());
						updateReturnGoods.setRemark(storageGoods.getRemark());
						int haveReturnCount = returnGood.getHaveReturnCount().intValue() + storageGoods.getProdScanNum();
						updateReturnGoods.setHaveReturnCount(haveReturnCount);
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				//检查是否为退单唯一一个没有入库的商品
				returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
				for (OrderReturnGoods orderReturnGoods:returnGoodsList) {
					if (orderReturnGoods.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()) {
						lastPullInGood = false ;
						break;
					}
				}
			} else {
				//所有商品入库（退单整体入库）
				for (OrderReturnGoods goods : returnGoodsList) {
					if (goods.getCheckinStatus().intValue() != ConstantValues.ORDERRETURN_ORDER_TYPE.YES.intValue()) {
						buff.append(goods.getId());
						buff.append(",");
						updateReturnGoods = new OrderReturnGoods();
						updateReturnGoods.setId(goods.getId());
						updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_ORDER_TYPE.YES);
						updateReturnGoods.setCheckinStatus(ConstantValues.ORDERRETURN_ORDER_TYPE.YES);
						updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURN_ORDER_TYPE.YES);
						//将商品件数作为扫描件数更新
						updateReturnGoods.setProdscannum(goods.getGoodsReturnNumber());
						updateReturnGoods.setCheckinTime(new Date());
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				logger.info("退单" + returnSn + "商品入库：所有商品入库状态更改成功");
				//推送erp拼接数据
				List<StorageGoods> storageList = new ArrayList<StorageGoods>();
				OrderReturnGoodsExample example = new OrderReturnGoodsExample();
				example.or().andRelatingReturnSnEqualTo(returnSn);
				List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(example);
				for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
					StorageGoods storageGoods = new StorageGoods();
					storageGoods.setId(orderReturnGoods.getId());
					storageGoods.setProdScanNum(orderReturnGoods.getGoodsReturnNumber());
					storageGoods.setCustomCode(orderReturnGoods.getCustomCode());
					storageGoods.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
					storageList.add(storageGoods);
				}
				returnOrderParam.setStorageGoods(storageList);
			}
//			String goodsIds = buff.toString();//所有入库商品id字符串
			//更新退单和退款单信息
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			// updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.byteValue());//默认部分入库
			//入库的时候将入库人保存到数据库
			updateOrderReturn.setActionUser(actionUser);
			updateOrderReturn.setUpdateTime(new Date());
			
			OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
			updateOrderReturnShip.setRelatingReturnSn(orderReturn.getReturnSn());
			//默认部分入库
			updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT.byteValue());
			updateOrderReturnShip.setCheckinTime(new Date());
			
			//当为全部入库或者所有商品都已入库时，将order_return和order_return_ship入库状态修改为已入库
			if (returnOrderParam.isPullInAll() || lastPullInGood) {
				updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURN_SHIP_STATUSS.STORAG);
				updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
			}
			
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			OrderRefund updateOrderRefund = new OrderRefund();
			updateOrderRefund.setUpdateTime(new Date());
			if (returnOrderParam.isPullInAll() || lastPullInGood) {
				//只有整单已入库时才将结算状态改为待结算
				updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
				updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
			}
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
			orderRefundMapper.updateByExampleSelective(updateOrderRefund, orderRefundExample);
			//orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			// 针对退换单对应的换货单中的待确认付款单做已付款处理,同时将换货单总付款状态已付款处理
			/*if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					&& StringUtils.isNotBlank(orderReturn.getNewOrderSn()) 
					&& orderReturnShip.getCheckinStatus().intValue() ==ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()){
				MasterOrderInfo exchangeOrder = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getNewOrderSn());
				if(exchangeOrder == null){
					logger.info("退货单("+orderReturn.getReturnSn()+")对应的换货单("+orderReturn.getNewOrderSn()+")不存在,不能对换货单的付款单状态做修正处理");
				}else{
					MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
					orderPayExample.or().andMasterOrderSnEqualTo(orderReturn.getNewOrderSn()).andPayStatusEqualTo(ConstantValues.OP_ORDER_PAY_STATUS.WAIT_CONFRIM.byteValue());
					List<MasterOrderPay> orderPayList = masterOrderPayMapper.selectByExample(orderPayExample);
					if(CollectionUtils.isNotEmpty(orderPayList)){
						OrderStatus orderStatus = new OrderStatus();
						orderStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
						orderStatus.setMasterOrderSn(orderPayList.get(0).getMasterOrderSn());
						orderStatus.setPaySn(orderPayList.get(0).getMasterPaySn());
						orderStatus.setMessage("退货单["+orderReturn.getReturnSn()+"]入库更新换货单待确认付款单");
						ReturnInfo payResult = payService.orderReturnPayStCh(orderStatus);
						logger.info("退货单入库时调用换货单付款接口修改待确认付款单数据：returnSn:"+returnSn+",payStCh:"+JSON.toJSONString(payResult));
					}
				}
			}*/
			
			// 如果是换货产生的退单（退单类型：1退货单；是否是换货时产生的退货、退款单：1是）
			// 则把对应的订单更新为已确认状态，操作备注为“确认： 入库自动触发该换货单确认
			/*if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					&& StringUtils.isNotBlank(orderReturn.getNewOrderSn()) && exchange != null){
				//退货单入库时，获取关联换货单生成的取消全额退款单，做自动确认操作
				OrderReturnExample orderReturnExample = new OrderReturnExample();
				orderReturnExample.or().andRelatingOrderSnEqualTo(orderReturn.getNewOrderSn());
				List<OrderReturn> cancelPayReturnList = orderReturnMapper.selectByExample(orderReturnExample);
				if(CollectionUtils.isNotEmpty(cancelPayReturnList)){
					OrderReturn cancelPayReturn = cancelPayReturnList.get(0);
					if(cancelPayReturn != null
							&& cancelPayReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM.intValue()){
						ReturnInfo<String> payResult = this.returnOrderConfirm(cancelPayReturn.getReturnSn(), "退货单["+returnSn+"]入库时，换货单["+orderReturn.getNewOrderSn()+"]取消生成退款单自动确认操作", actionUser);
						logger.info("returnOrderStorage.退货单["+returnSn+"]入库时，换货单["+orderReturn.getNewOrderSn()+"]取消生成退款单自动确认操作,Result:"+JSON.toJSONString(payResult));
					}
				}
			}*/
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单入库成功");
		} catch (Exception e) {
			actionNote = "退单入库失败:" + e.getMessage();
			logger.info("退单[" + returnSn + "]入库失败，Msg："+e.getMessage(), e);
			returnInfo.setMessage("退单入库失败:" + e.getMessage());
		} finally {
			// 入库完结日志
			orderActionService.addOrderReturnAction(returnSn, "入库："+actionNote,actionUser);
		}
		return returnInfo;
	
	}
	
	@Override
	public ReturnInfo<String> returnStorageCancle(String returnSn,String userName,String storageTimeStamp) {
		logger.info("returnStorageCancle.begin:returnSn"+returnSn+",userName"+userName+",storageTimeStamp"+storageTimeStamp);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单撤销入库失败");
		returnInfo.setReturnSn(returnSn);
		String actionUser = userName;
		StringBuffer actionNote = new StringBuffer();
		actionNote.append("退单撤销入库：");
		try {
			if(StringUtils.isBlank(returnSn)){
				throw new RuntimeException("退单号不能为空！");
			}
			//校验退单数据
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效地退单信息");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
				throw new RuntimeException("退单无效无法进行操作");
			}
			if(orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED && orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
				throw new RuntimeException("已结算退单无法进行撤销入库操作");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效地退单发货信息");
			}
			if(orderReturnShip.getCheckinStatus().intValue() != ConstantValues.YESORNO_YES && orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.intValue()) {
				throw new RuntimeException("退单入库状态不符合要求!");
			}
			
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
			if(masterOrderInfo == null ){
				throw new RuntimeException("退单关联的主单不存在或不在三个月范围内！");
			}
			
			MasterOrderInfo exchange = null;
			if(StringUtils.isNotBlank(orderReturn.getNewOrderSn())){
				exchange = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getNewOrderSn());
				if(exchange == null){
					throw new RuntimeException("退单关联的换货单号不存在或不在三个月范围内");
				}
				if(exchange.getOrderType().intValue() != ConstantValues.ORDERRETURN_RELATIN_ORDER_TYPE.EXCHANGE.intValue()){
					throw new RuntimeException("退货单所关联换货单["+orderReturn.getNewOrderSn()+"]非换货单类型");
				}
				
				if(exchange.getShipStatus().intValue() != ConstantValues.ORDERINFO_SHIP_STATUS.UNSHIP.intValue()){
					throw new RuntimeException("换货单"+exchange.getMasterOrderSn()+"不是未发货状态!");
				}
			}
			
			//校验商品信息
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			OrderReturnGoodsExample.Criteria criteria = orderReturnGoodsExample.or();
			criteria.andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if(CollectionUtils.isEmpty(returnGoodsList)){
				throw new RuntimeException("退货商品列表为空");
			}
			
			//校验付款单信息
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			if(CollectionUtils.isEmpty(orderRefundList)){
				throw new RuntimeException("退货付款单为空");
			}
			
			criteria.andToErpGreaterThanOrEqualTo(Constant.TOERP_NEED);
			criteria.andStorageTimeStampEqualTo(storageTimeStamp);
			List<OrderReturnGoods> cancelGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			boolean cancleFlag = true;
			//回退退单商品状态
			for (OrderReturnGoods orderReturnGoods : cancelGoodsList) {
				if(StringUtils.isBlank(storageTimeStamp) && orderReturnGoods.getToErp().intValue() == Constant.TOERP_NEED){
					cancleFlag = false;
				}
				orderReturnGoods.setCheckinStatus(ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.UNSTORAGE.byteValue());
				orderReturnGoods.setProdscannum(ConstantValues.YESORNO_NO.shortValue());
				orderReturnGoods.setToErp(ConstantValues.YESORNO_NO.byteValue());
				orderReturnGoods.setStorageTimeStamp(StringUtils.EMPTY);
				orderReturnGoods.setStorageStatus(Constant.STORAGE_STATUS_UNTRATED);
				if(orderReturnGoods.getStorageTreateTime() != null){
					orderReturnGoods.setStorageTreateTime(null);
				}
				if(orderReturnGoods.getCheckinTime() != null ){
					orderReturnGoods.setCheckinTime(null);
				}
				orderReturnGoodsMapper.updateByPrimaryKey(orderReturnGoods);
			}
			
			//回退退单物流状态
			OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
			OrderReturnGoodsExample returnGoodsExample = new OrderReturnGoodsExample();
			returnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> goodsList = orderReturnGoodsMapper.selectByExample(returnGoodsExample);
			boolean inputStatusFlag = true;
			//默认回退后退单为待入库状态
			updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.WAITSTORAGE.byteValue());
			for (OrderReturnGoods orderReturnGoods : goodsList) {
				if(orderReturnGoods.getCheckinStatus().intValue() ==ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()){
					inputStatusFlag = false;
					break;
				}
			}
			if(!inputStatusFlag){
				updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.byteValue()); 
			}
			updateOrderReturnShip.setRelatingReturnSn(orderReturnShip.getRelatingReturnSn());;
			orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
			
			
			//回退退单状态
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setUpdateTime(new Date());
			updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
			updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.CONFIRMED);
			updateOrderReturn.setToErp(Constant.TOERP_NOT_NEED);//调度任务将不会推送入库数据
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//拒收入库单修改关联主单为已确认状态
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
				MasterOrderInfo updateMasterOrderInfo = new MasterOrderInfo();
				updateMasterOrderInfo.setMasterOrderSn(orderReturn.getMasterOrderSn());
				updateMasterOrderInfo.setOrderStatus(ConstantValues.ORDER_STATUS.CONFIRMED);
				masterOrderInfoMapper.updateByPrimaryKeySelective(updateMasterOrderInfo);
				masterOrderActionService.insertOrderActionBySn(orderReturn.getMasterOrderSn(), "退单撤销入库回退关联主单状态", actionUser);
			}else{
				//回退退单付款单状态
				for (OrderRefund orderRefund : orderRefundList) {
					OrderRefund updateOrderRefund = new OrderRefund();
					updateOrderRefund.setReturnPaySn(orderRefund.getReturnPaySn());
					updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERREFUND_RETURN_PAY_STATUS.UNSETTLED);
					orderRefundMapper.updateByPrimaryKeySelective(updateOrderRefund);
				}
				
			}
			
			//退单关联换单
			if(StringUtils.isNotBlank(orderReturn.getNewOrderSn()) && exchange.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CANCELED){
				//换单关联的退单转入款状态回退
				MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
				masterOrderPayExample.or().andMasterOrderSnEqualTo(exchange.getMasterOrderSn()).andPayIdEqualTo(Byte.valueOf(ConstantValues.EXCHANGE_ORDER_RETURN_2_PAY_ID));
				List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
				if(CollectionUtils.isNotEmpty(masterOrderPayList)){
					OrderStatus orderStatus = new OrderStatus();
					orderStatus.setMasterOrderSn(exchange.getMasterOrderSn());
					orderStatus.setAdminUser(userName);
					orderStatus.setPaySn(masterOrderPayList.get(0).getMasterPaySn());
					orderStatus.setMessage("退单撤销入库回退换单状态");
					ReturnInfo result = payService.orderReturnUnPayStCh(orderStatus);
					logger.info("退单撤销入库回退换单状态,result:"+JSON.toJSONString(result)+",orderStatus:"+JSON.toJSONString(orderStatus));
					masterOrderActionService.insertOrderActionBySn(exchange.getMasterOrderSn(), "退单撤销入库回退换单状态", actionUser);
				}
			}
			//退单撤销入库推送erp数据
			if(cancleFlag){
				SfSchTaskExecOosInfo inputObj = new SfSchTaskExecOosInfo();
				inputObj.SRC_DOC_TYPE ="3";
				inputObj.B2C_DOC_CODE = storageTimeStamp;
				inputObj.SRC_DOC_CODE = storageTimeStamp;
				inputObj.OS_DOC_CODE = storageTimeStamp;
				inputObj.DISP_WAREH_CODE = orderReturn.getChannelCode();
				inputObj.SHOP_CODE = orderReturnShip.getDepotCode();
				inputObj.lstSfSchTaskExecOosDtls = new ArrayList<LstSfSchTaskExecOosDtls>();//撤销入库不传商品
				
				//放入队列
				logger.info(">>>>>returnStorageCancle.returnSn:"+returnSn+",inputObj:"+JSON.toJSONString(inputObj));
				sendMessage(jmsTemplateForReturnInput, JSON.toJSONString(inputObj));
			}
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单撤销入库成功！");
			actionNote.append("退单撤销入库成功！");
		} catch (Exception e) {
			logger.error("退单撤销入库失败:"+e.getMessage(),e);
			returnInfo.setMessage("退单撤销入库失败:"+e.getMessage());
			actionNote.append("退单撤销入库失败:"+e.getMessage());
		}finally{
			orderActionService.addOrderReturnAction(returnSn, actionNote.toString(), actionUser);
		}
		return returnInfo;
	}
	
	/**
	 * 将数据放入队列中
	 * @param jms
	 * @param data
	 */
	private void sendMessage(JmsTemplate jms,final String data) {
		jms.send(new MessageCreator() {
			@Override
			public Message createMessage(Session paramSession) throws JMSException {
				TextMessage message = paramSession.createTextMessage();
				message.setText(data);
				logger.info("SettleOrder-put message to settlement Queue:"+data);
				return message;
			}
		});
	}

	private String getMobile(MasterOrderInfo master) {
		if(StringUtils.isNotBlank(master.getMasterOrderSn())){
			return masterOrderAddressInfoMapper.selectByPrimaryKey(master.getMasterOrderSn()).getMobile();
		  }else{
			  return null;
		  }
	}

	private String getUserId(OrderDistribute orderInfo) {
		if(StringUtils.isNotBlank(orderInfo.getMasterOrderSn())){
		  return masterOrderInfoMapper.selectByPrimaryKey(orderInfo.getMasterOrderSn()).getUserId();
		}else{
			return null;
		}
	}

	/**
	 * 退单结算
	 * @param returnSn 退单号
	 * @param actionNote 退单备注
	 * @param actionUser 操作人
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> returnOrderSettle(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderSettle.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(Constant.OS_NO);
		returnInfo.setMessage("退单结算失败");
		returnInfo.setReturnSn(returnSn);
		try {
			//结算校验
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if (orderReturn == null) {
				returnInfo.setMessage("无法获取有效地退单信息");
				return returnInfo;
			}
			// 退单完成、退单取消、支付状态待结算
			if (orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE.intValue()
					|| orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY.intValue()
					|| orderReturn.getPayStatus().intValue() != ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE){
				returnInfo.setMessage("退单不满足结算条件");
				return returnInfo;
			}
			//退款单、额外退款单 无须退款 不结算
			if (orderReturn.getHaveRefund().intValue() == ConstantValues.YESORNO_NO.intValue()
					&& orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY.intValue()
					&& orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()){
				returnInfo.setMessage("退单款无须退款设置无法正常结算");
				return returnInfo;
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if (orderReturnShip == null) {
				returnInfo.setMessage("无法获取有效的退单发货信息");
				return returnInfo;
			}
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			if (CollectionUtils.isEmpty(orderRefundList)) {
				returnInfo.setMessage("无法获取有效的退款单数据");
				return returnInfo;
			}

			// 需要退款
			if (orderReturn.getHaveRefund().intValue() == ConstantValues.YESORNO_YES.intValue()) {
				for (OrderRefund orderRefund : orderRefundList) {
					if (orderRefund.getBackbalance().intValue() == ConstantValues.YESORNO_NO.intValue() && orderRefund.getReturnPay().intValue() == 3) {
						returnInfo.setMessage("退单" + returnSn + "没有退平台币，请先退平台币！");
						return returnInfo;
					}
				}
			}
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			// 退货单才进行商品校验
			if (CollectionUtils.isEmpty(returnGoodsList) && orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()){
				returnInfo.setMessage("退单商品列表为空");
				return returnInfo;
			}

			OrderDistribute orderInfo = builtOrderInfo(orderReturn);
			if (orderInfo == null) {
				returnInfo.setMessage("原订单不存在三个月，请先迁移订单后结算");
				return returnInfo;
			}
			// 退款单，要求原订单必须已发货
			if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY
					&& orderInfo.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CANCELED.intValue()
					&& orderInfo.getShipStatus().intValue() < ConstantValues.ORDERINFO_SHIP_STATUS.SHIPED){
				returnInfo.setMessage("退单关联原订单处于未发货状态不可进行退单结算操作");
				return returnInfo;
			}
			//退货单所关联的换货单必须为无效或者已取消或者已发货状态方可进行结算操作
			if (StringUtils.isNotBlank(orderReturn.getNewOrderSn())
					&& orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS){
				MasterOrderInfo exchangeOrder = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getNewOrderSn());
				if (exchangeOrder == null) {
					throw new RuntimeException("所关联换货单订单["+orderReturn.getNewOrderSn()+"]不存在三个月，请先迁移订单后结算");
				}
				if(exchangeOrder.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CANCELED.intValue()
						&& exchangeOrder.getShipStatus().intValue() < ConstantValues.ORDERINFO_SHIP_STATUS.SHIPED 
						&& exchangeOrder.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.INVALIDITY.intValue()
						&& exchangeOrder.getPayStatus().intValue() != ConstantValues.ORDER_PAY_STATUS.SETTLEMENT.intValue()){//换货单已结算的退单也让其进行结算
					throw new RuntimeException("退货单所关联换货单["+orderReturn.getNewOrderSn()+"]当前不是无效、取消或发货状态，不可进行退单结算操作");
				}
			}
			//验证退货单数据完整性：-入库、结算ERP
			if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()){
				if (orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
					returnInfo.setMessage("退货单待入库状态方可进行结算操作");
					return returnInfo;
				}
				ReturnInfo<String> checkReturn = orderSettleService.checkReturnSettle(returnSn);
				if (checkReturn.getIsOk() < ConstantValues.YESORNO_YES) {
					returnInfo.setMessage("退货单结算验证失败：" + checkReturn.getMessage());
					return returnInfo;
				}
			}
			//更新退单和退款单信息
            processOrderReturnFinish(returnSn);
			//批量更新付款单-已结算
            processOrderRefundFinish(orderReturn);

			//操作日志
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(),"结算：" + actionNote, actionUser);
			returnInfo.setIsOk(Constant.OS_YES);
			returnInfo.setMessage("退单结算成功");
		} catch (Exception e) {
			logger.error(returnSn + "退单结算失败:" + e.getMessage(), e);
			returnInfo.setMessage("退单结算失败:" + e.getMessage());
		}
		return returnInfo;
	}

    /**
     * 处理退单结算完成
     * @param returnSn
     */
	private void processOrderReturnFinish(String returnSn) {
        //更新退单和退款单信息
        OrderReturn updateOrderReturn = new OrderReturn();
        updateOrderReturn.setReturnSn(returnSn);
        // 已完成
        updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.COMPLETE);
        // 已结算
        updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED);
        // 结算时间
        updateOrderReturn.setClearTime(new Date());
        orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
    }

    /**
     * 处理退单退款单结算完成
     * @param orderReturn
     */
    private void processOrderRefundFinish(OrderReturn orderReturn) {
        //批量更新付款单-已结算
        OrderRefundExample refundExample = new OrderRefundExample();
        refundExample.or().andRelatingReturnSnEqualTo(orderReturn.getReturnSn());
        OrderRefund updateOrderRefund = new OrderRefund();
        // 已结算
        updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED);
        updateOrderRefund.setUpdateTime(new Date());
        orderReturn.setClearTime(new Date());
        if (orderReturn.getInitClearTime() == null) {
            orderReturn.setInitClearTime(new Date());
        }
        orderRefundMapper.updateByExampleSelective(updateOrderRefund, refundExample);
    }

	@Override
	public ReturnInfo<String> returnOrderConfirm(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderconfirm.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单确认失败");
		returnInfo.setReturnSn(returnSn);
		try {
			//验证数据
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED){
				throw new RuntimeException("退单已处于确认状态");
			}
			if(orderReturn.getPayStatus().intValue() != ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED){
				throw new RuntimeException("退单必须处于未结算状态");
			}
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					|| orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE){
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderReturn.getReturnSn());
				if(orderReturnShip.getIsGoodReceived().intValue() == ConstantValues.YESORNO_YES){
					throw new RuntimeException("退单必须处于未收货状态");  
				}
			}
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
			if (master == null) {
				returnInfo.setMessage("退货单[" + returnSn + "]关联的原订单数据不存在！");
				return returnInfo;
			}
			OrderDistribute orderInfo = builtOrderInfo(orderReturn);
//					orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			if(orderInfo == null){
				throw new RuntimeException("原订单不存在三个月，请先迁移订单后结算");
			}
			OrderReturnShip returnGoodsShip = null;
			//如果是换货单取消生成的全额退款单，则要判定换货单所关联的退货单是否已经入库，如果未入库则该全额退款单不能够确认操作
			if(orderInfo.getOrderType().intValue() == ConstantValues.ORDERRETURN_RELATIN_ORDER_TYPE.EXCHANGE.intValue()
					&& orderReturn.getRefundType().intValue() == ConstantValues.ORDERRETURN_REFUND_TYPE.CANCEL_ORDER.intValue()
					&& orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY.intValue()){
				String returnGoodsSn = orderInfo.getRelatingReturnSn();
				if(StringUtils.isBlank(returnGoodsSn)){
					throw new RuntimeException("换货单["+orderReturn.getRelatingOrderSn()+"]所关联的退货单异常");
				}
				returnGoodsShip = orderReturnShipMapper.selectByPrimaryKey(returnGoodsSn);
				if(returnGoodsShip == null){
					throw new RuntimeException("换货单["+orderReturn.getRelatingOrderSn()+"]关联的退货单不存在");
				}
				if(returnGoodsShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()){
					throw new RuntimeException("换货单关联的退货单["+returnGoodsSn+"]未入库不可，不能进行退款单确认操作");
				}
			}
			// 退单收货单
			if (returnGoodsShip == null) {
				returnGoodsShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
				if(returnGoodsShip == null){
					returnInfo.setMessage("退货单[" + returnSn + "]的退货发货单不存在");
					return returnInfo;
				}
//				if (StringUtil.isTrimEmpty(returnGoodsShip.getReturnExpress())) {
//					returnInfo.setMessage("退货单[" + returnSn + "]关联的退货承运商不能为空！");
//					return returnInfo;
//				}
//				if (StringUtil.isTrimEmpty(returnGoodsShip.getReturnInvoiceNo())) {
//					returnInfo.setMessage("退货单[" + returnSn + "]关联的退货快递单号不能为空！");
//					return returnInfo;
//				}
				if (orderReturn.getReturnType() == Constant.OR_RETURN_TYPE_RETURN) {
					if (StringUtil.isTrimEmpty(returnGoodsShip.getDepotCode())) {
						returnInfo.setMessage("退货单[" + returnSn + "]关联的退货仓库不能为空！");
						return returnInfo;
					}
				}
			}
			
			List<ReturnOrderGoods> returnGoodsList = masterOrderGoodsDetailMapper.getXOMSOrderReturnGoods(returnSn);
			if (CollectionUtils.isEmpty(returnGoodsList)) {
				throw new RuntimeException("退货商品列表为空");
			}
			
			MasterOrderAddressInfo addressInfo = masterOrderAddressInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
			if (addressInfo == null) {
				returnInfo.setMessage("退货单[" + returnSn + "]关联的原订单收货人数据不存在！");
				return returnInfo;
			}
			
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			if (CollectionUtils.isEmpty(orderRefundList)) {
				throw new RuntimeException("退款单数据不存在");
			}
			//退款单，要求原订单必须已发货
			if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY
					&& orderInfo.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CANCELED.intValue()
					&& orderInfo.getShipStatus().intValue() < ConstantValues.ORDERINFO_SHIP_STATUS.SHIPED) {
				throw new RuntimeException("退单关联原订单处于未发货状态不可进行退单结算操作");
			}
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(orderReturn.getReturnSn());
			
			OrderRefundExample updateOrderRefundExample = new OrderRefundExample();
			updateOrderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			OrderRefund updateOrderRefund = new OrderRefund();
			updateOrderRefund.setUpdateTime(new Date());
			//退款单-红包验证
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY 
					||orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY){
				double bonus = 0D;
//				OrderDistribute refOrderInfo = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
				OrderDistribute refOrderInfo =builtOrderInfo(orderReturn);
				if(refOrderInfo == null){
					throw new RuntimeException("关联订单信息无效");
//					OrderInfoHistory refOrderInfoHistory = orderInfoHistoryMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
//					if(refOrderInfoHistory == null){
//					}else{
//						bonus = refOrderInfoHistory.getBonus().doubleValue();   
//					}
				}else{
					bonus = refOrderInfo.getBonus().doubleValue();
				}
				if(bonus > 0){
					updateOrderReturn.setBackToCs(ConstantValues.YESORNO_YES);
				}
				updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
				updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
			}
			
			//更新退单
			updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.CONFIRMED);
			updateOrderReturn.setConfirmTime(new Date());
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//退款单 - 确认是更新refund为待结算
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY 
					||orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY){
				orderRefundMapper.updateByExampleSelective(updateOrderRefund, updateOrderRefundExample);
			}
			OrderCustomDefine define = orderCustomDefineService.selectCustomDefineByCode(orderReturn.getReturnReason());
			//操作日志
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "确认："+actionNote,actionUser);
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单确认成功");
			// 下发XOMS系统
			ReturnOrder returnOrder = new ReturnOrder();
			returnOrder.setReturnSn(returnSn);
			returnOrder.setAddTime(TimeUtil.formatDate(orderReturn.getAddTime()));
			returnOrder.setHaveRefund((int) orderReturn.getHaveRefund());
			returnOrder.setProcessType((int) orderReturn.getProcessType());
			returnOrder.setOrderSn(orderReturn.getMasterOrderSn());
			returnOrder.setShipSn(orderReturn.getRelatingOrderSn());
			returnOrder.setNewOrderSn(orderReturn.getNewOrderSn());
			returnOrder.setReturnReason(define != null ? define.getName() : orderReturn.getReturnReason());
			returnOrder.setUserId(orderReturn.getUserId());
			returnOrder.setShopCode(orderReturn.getChannelCode());
			returnOrder.setBvValue(orderReturn.getBvValue());
			returnOrder.setBaseBvValue(orderReturn.getBaseBvValue());
			returnOrder.setPoints(setScale(orderReturn.getPoints()));
			/*SystemShipping systemShipping = systemShippingService.getSystemShipByShipCode(returnGoodsShip.getReturnExpress());
			if (systemShipping == null) {
				logger.error("");
			} else {
				returnOrder.setShippingCode(returnGoodsShip.getReturnExpress());
				returnOrder.setShippingName(systemShipping.getShippingName());
			}*/
			if (StringUtil.isNotEmpty(returnGoodsShip.getReturnExpress())) {
				returnOrder.setShippingName(returnGoodsShip.getReturnExpress());
			}
			returnOrder.setTotalFee(setScale(orderReturn.getReturnTotalFee()));
			returnOrder.setReturnInvoiceNo(returnGoodsShip.getReturnInvoiceNo());
			returnOrder.setDepotCode(returnGoodsShip.getDepotCode());
			returnOrder.setReturnShippingFee(setScale(orderReturn.getReturnShipping()));
			returnOrder.setPostscript(orderReturn.getReturnDesc());
			returnOrder.setGoodsAmount(setScale(orderReturn.getReturnGoodsMoney()));
			returnOrder.setGoodsCount(orderReturn.getReturnAllgoodsCount());
			
			returnOrder.setDiscount(setScale(master.getDiscount()));
			returnOrder.setBonus(setScale(orderReturn.getReturnBonusMoney()));
			returnOrder.setIntegralMoney(setScale(orderReturn.getReturnIntegralMoney()));
			returnOrder.setConsignee(addressInfo.getConsignee());
			returnOrder.setMobile(addressInfo.getMobile());
			returnOrder.setTel(addressInfo.getTel());
			returnOrder.setConsignee(addressInfo.getConsignee());
			List<SystemRegionArea> regions = getSystemRegion(addressInfo);
			returnOrder.setCountry(findRegionsNameByid(regions, addressInfo.getCountry()));
			returnOrder.setProvince(findRegionsNameByid(regions, addressInfo.getProvince()));
			returnOrder.setCity(findRegionsNameByid(regions, addressInfo.getCity()));
			returnOrder.setDistrict(findRegionsNameByid(regions, addressInfo.getDistrict()));
			returnOrder.setAddress(addressInfo.getAddress());
			returnOrder.setEmail(addressInfo.getEmail());
			returnOrder.setAreaCode(addressInfo.getAreaCode());
			returnOrder.setReasonCode(orderReturn.getReturnReason());
			returnOrder.setCreateReturnType(orderReturn.getRefundType().intValue());
			List<ReturnGoods> returnGoods = new ArrayList<ReturnGoods>();
			int baseBvValue = 0;
			for (ReturnOrderGoods orderReturnGoods : returnGoodsList) {
				ReturnGoods goods = new ReturnGoods();
				goods.setGoodsSn(orderReturnGoods.getGoodsSn());
				goods.setCustomCode(orderReturnGoods.getCustomCode());
				goods.setGoodsName(orderReturnGoods.getGoodsName());
				goods.setExtensionCode(orderReturnGoods.getExtensionCode());
				goods.setColorName(orderReturnGoods.getGoodsColorName());
				goods.setSizeName(orderReturnGoods.getGoodsSizeName());
				goods.setGoodsNumber(orderReturnGoods.getGoodsReturnNumber().intValue());
				goods.setGoodsPrice(setScale(orderReturnGoods.getMarketPrice()));
				goods.setTransactionPrice(setScale(orderReturnGoods.getGoodsPrice()));
				goods.setSettlementPrice(setScale(orderReturnGoods.getSettlementPrice()));
				goods.setShareBonus(setScale(orderReturnGoods.getShareBonus()));
				goods.setGoodsThumb(orderReturnGoods.getGoodsThumb());
				goods.setSap(orderReturnGoods.getSap());
				goods.setDiscount(setScale(orderReturnGoods.getDiscount()));
				goods.setIntegralMoney(setScale(orderReturnGoods.getIntegralMoney()));
				goods.setBvValue(StringUtil.isTrimEmpty(orderReturnGoods.getBvValue()) ? 0 : Integer.valueOf(orderReturnGoods.getBvValue()));
				goods.setBaseBvValue(orderReturnGoods.getBaseBvValue());
				goods.setDepotCode(orderReturnGoods.getOsDepotCode());
				goods.setPrIds(orderReturnGoods.getPromotionId());
				returnGoods.add(goods);
				baseBvValue += (orderReturnGoods.getBaseBvValue() == null ? 0 : orderReturnGoods.getBaseBvValue() * orderReturnGoods.getGoodsReturnNumber());
			}
			if (returnOrder.getBaseBvValue() == null || returnOrder.getBaseBvValue() == 0) {
				returnOrder.setBaseBvValue(baseBvValue);
			}
			returnOrder.setReturnGoods(returnGoods);
			List<ReturnPay> returnPays = new ArrayList<ReturnPay>();
			for (OrderRefund orderRefund : orderRefundList) {
				ReturnPay returnPay = new ReturnPay();
				SystemPayment payment = systemPaymentMapper.selectByPrimaryKey(orderRefund.getReturnPay().byteValue());
				returnPay.setPayCode(payment.getPayCode());
				returnPay.setPayName(payment.getPayName());
				returnPay.setPaySn(orderRefund.getReturnPaySn());
				returnPay.setPayTotalFee(setScale(orderRefund.getReturnFee()));
				returnPay.setCreateTime(TimeUtil.formatDate(orderRefund.getAddTime()));
				returnPays.add(returnPay);
			}
			returnOrder.setReturnPays(returnPays);
			OrderReturn updateReturn = new OrderReturn();
			//更新退单
			updateReturn.setReturnSn(returnSn);
			updateReturn.setToErp((byte)2);
			updateReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateReturn);
			//操作日志
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单确认成功");
		} catch (Exception e) {
			logger.error("returnOrderComfirm"+returnSn+" 退单确认失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("退单确认失败:"+e.getMessage());
		}
		return returnInfo;
	}

	private Double setScale(BigDecimal obj) {
		if (obj == null) {
			return 0D;
		}
		return obj.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private Double setScale(Double obj) {
		if (obj == null) {
			return 0D;
		}
		return BigDecimal.valueOf(obj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private Double setScale(Float obj) {
		if (obj == null) {
			return 0D;
		}
		return BigDecimal.valueOf(obj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public List<OrderReturnGoods> groupByOrderGoods(List<OrderReturnGoods> orderGoodsList) throws Exception {
		String order_sn = orderGoodsList.get(0).getOrderSn();
		logger.debug("订单" + order_sn + "开始合并商品");
		List<OrderReturnGoods> templist = new ArrayList<OrderReturnGoods>(orderGoodsList.size());
		Map<String, OrderReturnGoods> map = new HashMap<String, OrderReturnGoods>();
		for (OrderReturnGoods ogBean : orderGoodsList) {
			String exCode = ogBean.getExtensionCode();
			String customCode = ogBean.getCustomCode() + exCode;
			if (StringUtil.isEmpty(customCode)) {
				logger.debug("订单下发时合并商品异常.订单号：" + order_sn);
				throw new Exception("订单下发时合并商品异常.订单号：" + order_sn);
			}
			if (StringUtil.isBlank(customCode)) {
				logger.debug("订单" + order_sn + "商品customCode为空");
				continue;
			}
			if (!map.containsKey(customCode)) {
				logger.debug("订单" + order_sn + "商品" + customCode + "处理完成");
				map.put(customCode, ogBean);
			} else {
				OrderReturnGoods ogs = map.get(customCode);
				setAvgTransactionPrice(ogs, ogBean);
				setAvgSettlementPrice(ogs, ogBean);
				setAvgShareBonus(ogs, ogBean);
				setAvgIntegralMoney(ogs, ogBean);
				setAvgDiscount(ogs, ogBean);
				setGoodsNumber(ogBean, ogs);
				logger.debug("订单" + order_sn + "商品" + customCode + "合并完成");
			}
		}
		for (Iterator<OrderReturnGoods> iterator = map.values().iterator(); iterator.hasNext();) {
			templist.add(iterator.next());
		}
		if (StringUtil.isListNull(templist)) {
			logger.debug("订单下发时合并商品异常.订单号：" + order_sn);
			throw new Exception("订单下发时合并商品异常.订单号：" + order_sn);
		}
		return templist;
	}

	private void setGoodsNumber(OrderReturnGoods ogBean, OrderReturnGoods ogs) {
		ogs.setGoodsReturnNumber((short) (ogs.getGoodsReturnNumber() + ogBean.getGoodsReturnNumber()));
	}
	
	private void setAvgShareBonus(OrderReturnGoods ogs, OrderReturnGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsReturnNumber(), ogs.getShareBonus(), ogBean.getGoodsReturnNumber(), ogBean.getShareBonus());
		ogs.setShareBonus(bd);
	}
	
	private void setAvgIntegralMoney(OrderReturnGoods ogs, OrderReturnGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsReturnNumber(), ogs.getIntegralMoney(), ogBean.getGoodsReturnNumber(), ogBean.getIntegralMoney());
		ogs.setIntegralMoney(bd);
	}
	
	private void setAvgDiscount(OrderReturnGoods ogs, OrderReturnGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsReturnNumber(), BigDecimal.valueOf(ogs.getDiscount()), ogBean.getGoodsReturnNumber(), BigDecimal.valueOf(ogs.getDiscount()));
		ogs.setDiscount(bd.floatValue());
	}
	
	private void setAvgTransactionPrice(OrderReturnGoods ogs, OrderReturnGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsReturnNumber(), ogs.getGoodsPrice(), ogBean.getGoodsReturnNumber(), ogBean.getGoodsPrice());
		ogs.setGoodsPrice(bd);
	}

	private void setAvgSettlementPrice(OrderReturnGoods ogs, OrderReturnGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsReturnNumber(), ogs.getSettlementPrice(), ogBean.getGoodsReturnNumber(), ogBean.getSettlementPrice());
		ogs.setSettlementPrice(bd);
	}

	private BigDecimal setAvgPrice(Short goodsNumber, BigDecimal transactionPrice, Short goodsNumber2, BigDecimal transactionPrice2) {
		BigDecimal price1 = transactionPrice.multiply(BigDecimal.valueOf(goodsNumber)).setScale(5, BigDecimal.ROUND_HALF_UP);
		BigDecimal price2 = transactionPrice2.multiply(BigDecimal.valueOf(goodsNumber2)).setScale(5, BigDecimal.ROUND_HALF_UP);
		BigDecimal sum = price1.add(price2);
		BigDecimal result = sum.divide(BigDecimal.valueOf(goodsNumber + goodsNumber2), 5, BigDecimal.ROUND_HALF_UP);
		return result;
	}
	
	/**
	 * 根据地区ID查询地区名称
	 * 
	 * @param regions
	 *			地区数据源
	 * @param regionId
	 *			地区ID
	 * @return
	 */
	private String findRegionsNameByid(List<SystemRegionArea> regions, String regionId) {
		if (regions == null || regions.isEmpty())
			return "";
		for (int i = 0; i < regions.size(); i++) {
			SystemRegionArea sg = regions.get(i);
			if (sg == null)
				continue;
			if (sg.getRegionId() == null)
				continue;
			if (sg.getRegionId().equals(regionId)) {
				return sg.getRegionName();
			}
		}
		return "";
	}

	private List<SystemRegionArea> getSystemRegion(final MasterOrderAddressInfo address) {
		String[] arr = new String[] { address.getCountry(), address.getProvince(), address.getCity(), address.getDistrict() };
		SystemRegionAreaExample example = new SystemRegionAreaExample();
		example.or().andRegionIdIn(Arrays.asList(arr));
		return systemRegionAreaMapper.selectByExample(example);
	}

    /**
     * 根据退单创建订单信息
     * @param orderReturn
     * @return OrderDistribute
     */
	private OrderDistribute builtOrderInfo(OrderReturn orderReturn) {
		OrderDistribute orderDistribute = new OrderDistribute();
		MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
		orderDistribute.setRelatingReturnSn(orderReturn.getReturnSn());
		orderDistribute.setShipStatus(masterOrderInfo.getShipStatus());
		orderDistribute.setOrderStatus(masterOrderInfo.getOrderStatus());
		orderDistribute.setBonus(masterOrderInfo.getBonus());
		orderDistribute.setOrderType(masterOrderInfo.getOrderType());
		orderDistribute.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
		orderDistribute.setOrderFrom(masterOrderInfo.getOrderFrom());
		orderDistribute.setReferer(masterOrderInfo.getReferer());
		return orderDistribute;
	}

	@Override
	public ReturnInfo<String> returnOrderUnConfirm(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderUnConfirm.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
				throw new RuntimeException("退单无效无法进行操作");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM.intValue()){
				throw new RuntimeException("退单状态必须为已确认");
			}
			if(orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED.intValue()){
				throw new RuntimeException("退单已结算不可进行未确认操作");
			}
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			if(CollectionUtils.isEmpty(orderRefundList)){
				throw new RuntimeException("退款单数据不存在");
			}
			OrderRefund orderRefund = orderRefundList.get(0);
			
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("退单发货数据为空");
			}
			//退货单和拒收入库单，要求退单发货状态必须为未追单
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					|| orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE){
				
				if(orderReturnShip.getIsGoodReceived().intValue() == ConstantValues.YESORNO_YES){
					throw new RuntimeException("退单必须处于未未收货状态方可进行操作");
				}
			}
			orderRefund.setRelatingReturnSn(returnSn);
			orderRefund.setUpdateTime(new Date());
			orderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM);
			orderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
			orderReturn.setClearTime(null);
			orderReturn.setConfirmTime(null);
			orderReturn.setUpdateTime(new Date());
			
			//退单保存
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY){
				orderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
				orderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
			}

			orderRefundMapper.updateByPrimaryKey(orderRefund);
			orderReturnMapper.updateByPrimaryKey(orderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "未确认："+actionNote,actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderUnConfirm"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 退单收货
	 * @param returnOrderParam
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> returnOrderReceive(ReturnOrderParam returnOrderParam) {
		String returnSn = returnOrderParam.getReturnSn();
		logger.info("returnOrderReceive.begin:" + JSON.toJSONString(returnOrderParam));
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		List<StorageGoods> storageGoodsList = returnOrderParam.getStorageGoods();
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if (orderReturn == null) {
				throw new RuntimeException("退单数据不存在");
			}
			if (orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY) {
				throw new RuntimeException("退单无效无法进行操作");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if (orderReturnShip == null) {
				throw new RuntimeException("退单发货数据不存在");
			}
			if (orderReturnShip.getIsGoodReceived().intValue() == ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED.intValue()) {
				throw new RuntimeException("退单已处于已收货状态");
			}
			
			//校验商品信息
			OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
			boolean lastPullInGood = true;
			
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if (CollectionUtils.isEmpty(returnGoodsList)) {
				throw new RuntimeException("退货商品列表为空");
			}
			StringBuffer buff = new StringBuffer();
			if (!returnOrderParam.isPullInAll()) {
				for (StorageGoods storageGoods : storageGoodsList) {
					buff.append(storageGoods.getId());
					buff.append(",");
					
					if (storageGoods.getProdScanNum() <= 0) {
						throw new RuntimeException("商品质检数量不能小于或等于0");
					}
					
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(storageGoods.getId());
					if (null == returnGood) {
						throw new RuntimeException("收货商品" + storageGoods.getId() + "不存在！");
					} else {
						updateReturnGoods.setId(returnGood.getId());
						if (storageGoods.getProdScanNum() + returnGood.getProdscannum() >= returnGood.getGoodsReturnNumber()) {
							//商品行--商品全部已收货
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED.byteValue());
						} else {
							//商品行--商品部分收货
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.PARTRECEIVED.byteValue());
						}
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				//检查是否为退单唯一一个没有收货的商品
				returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
				for (OrderReturnGoods orderReturnGoods:returnGoodsList) {
					if (orderReturnGoods.getIsGoodReceived().intValue() != ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED.intValue()) {
						lastPullInGood = false ;
						break;
					}
				}
			} else {
				//所有商品入库（退单整体收货）
				returnGoodsAll(returnGoodsList);
				logger.info("退单" + returnSn + "商品收货：所有商品收货状态更改成功");
			}
			
			//退单保存
			processOrderReturnShip(orderReturn, returnOrderParam, lastPullInGood);
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderReceive"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 整单商品入库
	 * @param returnGoodsList
	 */
	private void returnGoodsAll(List<OrderReturnGoods> returnGoodsList) {
		//所有商品入库（退单整体收货）
		for (OrderReturnGoods goods : returnGoodsList) {
			if (goods.getIsGoodReceived().intValue() != ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED) {
				OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
				updateReturnGoods.setId(goods.getId());
				updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
				orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
			}
		}
	}

	/**
	 * 处理退货入库单状态
	 * @param orderReturn
	 * @param returnOrderParam
	 * @param lastPullInGood
	 */
	private void processOrderReturnShip(OrderReturn orderReturn, ReturnOrderParam returnOrderParam, boolean lastPullInGood) {
		//退单保存
		String returnSn = orderReturn.getReturnSn();
		OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
		updateOrderReturnShip.setRelatingReturnSn(returnSn);
		// 默认部分收货
		updateOrderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.PARTRECEIVED);
		updateOrderReturnShip.setReceivedTime(new Date());
		updateOrderReturnShip.setUpdateTime(new Date());

		//当为全部入库或者所有商品都收货时，将order_return_ship收货状态修改为已收货
		if (returnOrderParam.isPullInAll() || lastPullInGood){
			updateOrderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
		}
		orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
		//日志保存
		orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "已收货：" + returnOrderParam.getActionNote(), returnOrderParam.getUserName());

	}

	@Override
	public ReturnInfo<String> returnOrderUnReceive(ReturnOrderParam returnOrderParam) {
		String returnSn = returnOrderParam.getReturnSn();
//	  String goodsId = returnOrderParam.getGoodsId();
		logger.info("returnOrderUnReceive.begin:"+JSON.toJSONString(returnOrderParam));
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		List<StorageGoods> storageGoodsList = returnOrderParam.getStorageGoods();
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
				throw new RuntimeException("退单无效无法进行操作");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("退单发货数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED.intValue()
					&& orderReturnShip.getIsGoodReceived().intValue() <= ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED){
				throw new RuntimeException("退单已处于已确认未收货状态");
			}
			
			//校验商品信息
			OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
			boolean lastPullInGood = true;
			
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if(CollectionUtils.isEmpty(returnGoodsList)){
				throw new RuntimeException("退货商品列表为空");
			}
			StringBuffer buff = new StringBuffer();
			if(!returnOrderParam.isPullInAll()){
				for(StorageGoods storageGoods : storageGoodsList){
					buff.append(storageGoods.getId());
					buff.append(",");
					
					if(storageGoods.getProdScanNum() <= 0){
						throw new RuntimeException("商品质检数量不能小于或等于0");
					}
					
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(storageGoods.getId());
					if(null == returnGood){
						throw new RuntimeException("未收货商品"+storageGoods.getId()+"不存在！");
					}else{
						if(returnGood.getIsGoodReceived().intValue() == ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED.intValue()){
							throw new RuntimeException("退单："+returnSn+"中商品goodsSn:"+returnGood.getGoodsSn()+"已经是未收货状态！");
						}
						
						updateReturnGoods.setId(returnGood.getId());
						
						if(storageGoods.getProdScanNum() + returnGood.getProdscannum() >= returnGood.getGoodsReturnNumber()){
							//商品行--商品全部未收货
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED.byteValue());
						}else {
							//商品行--商品部分未收货
							updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.PARTRECEIVED.byteValue());
						}
//					  updateReturnGoods.setProdscannum(BigDecimal.valueOf(storageGoods.getProdScanNum() + returnGood.getProdscannum()).shortValue());
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				//检查是否为退单唯一一个未收货的商品
				returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
				for(OrderReturnGoods orderReturnGoods:returnGoodsList){
					if(orderReturnGoods.getIsGoodReceived().intValue() != ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED.intValue()){
						lastPullInGood = false ;
						break;
					}
				}
				
				
			}else{
				//所有商品未收货（退单整体未收货）
				for(OrderReturnGoods goods:returnGoodsList){
					if(goods.getIsGoodReceived().intValue() != ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED){
						updateReturnGoods = new OrderReturnGoods();
						updateReturnGoods.setId(goods.getId());
						updateReturnGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED);
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				logger.info("退单"+returnSn+"商品未收货：所有商品收货状态更改成功");
			}
			
			//默认部分收货
			orderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.PARTRECEIVED);
			orderReturnShip.setRelatingReturnSn(returnSn);
			orderReturnShip.setReceivedTime(null);
			orderReturnShip.setUpdateTime(new Date());
			
			//当为全部未收货或者所有商品都未收货时，将order_return_ship收货状态修改为未收货
			if(returnOrderParam.isPullInAll() || lastPullInGood){
				orderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED);
			}
			
			orderReturnShipMapper.updateByPrimaryKey(orderReturnShip);

//		  OrderReturn updateOrderReturn = new OrderReturn();
//		  updateOrderReturn.setReturnSn(returnSn);
//		  updateOrderReturn.setUpdateTime(new Date());
//		  updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURN_SHIP_STATUSS.UNRECEIVED);
//		  orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "未收货："+returnOrderParam.getActionNote(),returnOrderParam.getUserName());
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderUnReceive"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 退单质检通过
	 * @param returnOrderParam
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> returnOrderPass(ReturnOrderParam returnOrderParam) {
		String returnSn = returnOrderParam.getReturnSn();
		logger.info("returnOrderPass.begin:"+JSON.toJSONString(returnOrderParam));
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		List<StorageGoods> storageGoodsList = returnOrderParam.getStorageGoods();
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if (orderReturn == null) {
				throw new RuntimeException("退单数据不存在");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if (orderReturnShip == null) {
				throw new RuntimeException("退单发货数据不存在");
			}
			
			if (orderReturnShip.getQualityStatus() == ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue()) {
				throw new RuntimeException("退单已处于质检通过状态");
			}
			//校验商品信息
			OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
			boolean lastPullInGood = true;
			
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if (CollectionUtils.isEmpty(returnGoodsList)) {
				throw new RuntimeException("退货商品列表为空");
			}
			StringBuffer buff = new StringBuffer();
			if (!returnOrderParam.isPullInAll()) {
				for(StorageGoods storageGoods : storageGoodsList){
					buff.append(storageGoods.getId());
					buff.append(",");
					
					if (storageGoods.getProdScanNum() <= 0) {
						throw new RuntimeException("商品质检数量不能小于或等于0");
					}
					
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(storageGoods.getId());
					if (null == returnGood) {
						throw new RuntimeException("质检商品"+storageGoods.getId()+"不存在！");
					} else {
						updateReturnGoods.setId(returnGood.getId());
						if (storageGoods.getProdScanNum() + returnGood.getProdscannum() >= returnGood.getGoodsReturnNumber()) {
							//商品行--商品全部质检通过
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.byteValue());
						} else {
							//商品行--商品部分质检通过
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.byteValue());
						}
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				//检查是否为退单商品是否已全部质检通过
				returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
				for (OrderReturnGoods orderReturnGoods:returnGoodsList) {
					if(orderReturnGoods.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue()){
						lastPullInGood = false ;
						break;
					}
				}
			} else {
				//所有商品已质检（退单整体已质检）
				processReturnOrderGoodsPass(returnGoodsList);
				logger.info("退单"+returnSn+"商品已质检：所有商品质检状态更改成功");
			}
			processReturnOrderGoodsQuality(orderReturn, orderReturnShip, returnOrderParam, lastPullInGood);
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "质检通过："+returnOrderParam.getActionNote(),returnOrderParam.getUserName());
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderPass"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 处理退单商品质检通过
	 * @param returnGoodsList
	 */
	private void processReturnOrderGoodsPass(List<OrderReturnGoods> returnGoodsList) {
		for (OrderReturnGoods goods : returnGoodsList) {
			if (goods.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS) {
				OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
				updateReturnGoods.setId(goods.getId());
				updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS);
				orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
			}
		}
	}

	/**
	 * 处理退单商品质检通过
	 * @param orderReturn
	 * @param orderReturnShip
	 * @param returnOrderParam
	 * @param lastPullInGood
	 */
	private void processReturnOrderGoodsQuality(OrderReturn orderReturn, OrderReturnShip orderReturnShip, ReturnOrderParam returnOrderParam, boolean lastPullInGood) {
		//退单保存
		String returnSn = orderReturn.getReturnSn();
		OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
		updateOrderReturnShip.setRelatingReturnSn(returnSn);
		//部分质检通过
		updateOrderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.intValue());
		if (orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT) {
			updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.WAITINPUT.byteValue());
		}
		updateOrderReturnShip.setQualityTime(new Date());
		updateOrderReturnShip.setUpdateTime(new Date());

		//当为全部已质检或者所有商品都已质检时，将order_return_ship质检状态修改为已质检
		if (returnOrderParam.isPullInAll() || lastPullInGood){
			updateOrderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue());
		}

		orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
	}

	@Override
	public ReturnInfo<String> returnOrderUnPass(ReturnOrderParam returnOrderParam) {
		String returnSn = returnOrderParam.getReturnSn();
//	  String goodsId = returnOrderParam.getGoodsId();
		logger.info("returnOrderUnPass.begin..returnSn:"+returnSn+",actionNote:"+returnOrderParam.getActionNote()+",actionUser:"+returnOrderParam.getUserName());
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		List<StorageGoods> storageGoodsList = returnOrderParam.getStorageGoods();
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
				throw new RuntimeException("退单无效无法进行操作");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("退单发货数据不存在");
			}
			if(orderReturnShip.getQualityStatus() == ConstantValues.YESORNO_NO){
				throw new RuntimeException("退单已处于质检未通过状态");
			}
			
			//校验商品信息
			OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
			boolean lastPullInGood = true;
			
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if(CollectionUtils.isEmpty(returnGoodsList)){
				throw new RuntimeException("退货商品列表为空");
			}
			StringBuffer buff = new StringBuffer();
			if(!returnOrderParam.isPullInAll()){
				for(StorageGoods storageGoods : storageGoodsList){
					buff.append(storageGoods.getId());
					buff.append(",");
					
					if(storageGoods.getProdScanNum() <= 0){
						throw new RuntimeException("商品未质检数量不能小于或等于0");
					}
					
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(storageGoods.getId());
					if(null == returnGood){
						throw new RuntimeException("未质检商品"+storageGoods.getId()+"不存在！");
					}else{
						if(returnGood.getQualityStatus().intValue() == ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS.intValue()){
							throw new RuntimeException("退单："+returnSn+"中商品goodsSn:"+returnGood.getGoodsSn()+"已经是质检不通过状态！");
						}
						
						updateReturnGoods.setId(returnGood.getId());
						
						if(storageGoods.getProdScanNum() + returnGood.getProdscannum() >= returnGood.getGoodsReturnNumber()){
							//商品行--商品全部质检不通过
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS.byteValue());
						}else {
							//商品行--商品部分质检不通过
							updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.byteValue());
						}
//					  updateReturnGoods.setProdscannum(BigDecimal.valueOf(storageGoods.getProdScanNum() + returnGood.getProdscannum()).shortValue());
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				//检查是否为退单商品是否已全部质检不通过
				returnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
				for(OrderReturnGoods orderReturnGoods:returnGoodsList){
					if(orderReturnGoods.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS.intValue()){
						lastPullInGood = false ;
						break;
					}
				}
				
				
			}else{
				//所有商品未质检（退单整体未质检）
				for(OrderReturnGoods goods:returnGoodsList){
					if(goods.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS){
						updateReturnGoods = new OrderReturnGoods();
						updateReturnGoods.setId(goods.getId());
						updateReturnGoods.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS);
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateReturnGoods);
					}
				}
				logger.info("退单"+returnSn+"商品未质检：所有商品质检状态更改成功");
			}
			
			//退单更新
			orderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.intValue());//默认部分质检通过
//		  orderReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.UNINPUT.byteValue());
			orderReturnShip.setQualityTime(null);
			orderReturnShip.setUpdateTime(new Date());
			
			//当为全部未质检或者所有商品都未质检时，将order_return_ship质检状态修改为未质检
			if(returnOrderParam.isPullInAll() || lastPullInGood){
				orderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.UNPASS.intValue());
			}
			
			orderReturnShipMapper.updateByPrimaryKey(orderReturnShip);

//		  OrderReturn updateOrderReturn = new OrderReturn();
//		  updateOrderReturn.setReturnSn(returnSn);
//		  updateOrderReturn.setUpdateTime(new Date());
//		  updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURN_SHIP_STATUSS.UNPASSCHECK);
//		  orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "质检未通过："+returnOrderParam.getActionNote(),returnOrderParam.getUserName());
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderUnPass"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderInvalid(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderInvalid.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY
					|| orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE){
				throw new RuntimeException("退单已作废或已完结不可再次操作");
			}
			
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			
			if(orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()
					|| orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.intValue()
					|| orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED){
				throw new RuntimeException("退单已入库或已结算不可作废退单！");
			}
			
			//退单更新
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.INVALIDITY);
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//退货单、拒收入库单 添加监控
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()
					||orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
				orderMonitorService.monitorForReturnCancel(returnSn, actionNote);		   
			}
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "作废："+actionNote,actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderInvalid"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

    /**
     * 退单日志
     * @param returnSn 退单号
     * @param actionNote 退单日志
     * @param actionUser 操作人
     * @return
     */
	@Override
	public ReturnInfo<String> returnOrderCommunicate(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderCommunicate.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			//保存日志
            OrderReturnAction action = new OrderReturnAction();
            action.setReturnSn(orderReturn.getReturnSn());
            action.setActionNote("沟通："+actionNote);
            action.setActionUser(actionUser);
            action.setLogType((byte)1);
			orderActionService.addOrderReturnAction(action);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderCommunicate"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderBackToCs(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderBackToCs.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			
			//退单更新
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setBackToCs(orderReturn.getBackToCs() == 0 ? ConstantValues.YESORNO_YES : ConstantValues.YESORNO_NO);
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "回退客服："+actionNote,actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderBackToCs"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderRevive(String returnSn, String actionNote, String actionUser) {
		logger.info("returnOrderRevive.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
				throw new RuntimeException("退单无效无法进行操作");
			}
			//退单更新
			OrderRefund updateOrderRefund = new OrderRefund();
			updateOrderRefund.setRelatingReturnSn(returnSn);
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setUpdateTime(new Date());
			OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
			updateOrderReturnShip.setRelatingReturnSn(returnSn);
			updateOrderReturnShip.setUpdateTime(new Date());

			/*if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY
					|| orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE){
				//已完成、取消   回退为   已确认 ，质检通过，待结算，已入库
				updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.CONFIRMED);
				updateOrderReturn.setReturnOrderIspass(ConstantValues.ORDERRETURN_ISPASS_STATUS.PASS.intValue());
				updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
				updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERREFUND_RETURN_PAY_STATUS.WAITSETTLE);
			}else if(orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE.intValue()){
				//待结算			  回退为   质检通过 未结算，已收货未入库
				updateOrderReturn.setReturnOrderIspass(ConstantValues.ORDERRETURN_ISPASS_STATUS.PASS.intValue());
				updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
				updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURN_SHIP_STATUS.RECEIVED);
				updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERREFUND_RETURN_PAY_STATUS.UNSETTLED);
			}else if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED
					&& orderReturn.getShipStatus().intValue() == ConstantValues.ORDERRETURN_SHIP_STATUS.RECEIVED.intValue()){
				//已确认，已收货未入库 回退为 未收货
				updateOrderReturn.setShipStatus(ConstantValues.ORDERRETURN_SHIP_STATUS.UNRECEIVED);
				updateOrderReturnShip.setChasedOrNot(ConstantValues.YESORNO_NO);
			}else if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED.intValue()){
				//已确认  回退为 未确认
				updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM);
			}
			*/
			orderRefundMapper.updateByPrimaryKeySelective(updateOrderRefund);
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "复活："+actionNote,actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("returnOrderRevive"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderLock(String returnSn, String actionNote, String actionUser, Integer userId) {
		logger.info("returnOrderLock.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getLockStatus() != 0){
				throw new RuntimeException("退单已锁定不可再次操作");
			}
			//退单更新
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setLockStatus(userId);
			updateOrderReturn.setLockTime(new Date());
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "锁定",actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("returnOrderLock"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderUnLock(String returnSn, String actionNote, String actionUser, Integer userId) {
		logger.info("returnOrderUnLock.begin..returnSn:"+returnSn+",actionNote:"+actionNote+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		try{
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(orderReturn.getLockStatus() == 0){
				throw new RuntimeException("退单处于锁定状态不可再次操作");
			}
		    if(orderReturn.getLockStatus().intValue() != userId.intValue() && !StringUtils.equalsIgnoreCase(actionUser, "admin")){
				throw new RuntimeException("退单" + returnSn + "非当前用户锁定，无法解锁");
			}
			//退单更新
			orderReturn.setReturnSn(returnSn);
			orderReturn.setLockStatus(0);
			orderReturn.setLockTime(null);
			orderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(orderReturn);
			
			//日志保存
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), "解锁",actionUser);
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("returnOrderUnLock"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> returnOrderShareSettle(String returnSn, Integer payId, Double returnMoney, String actionUser) {
		logger.info("returnOrderShareSettle.begin..returnSn:"+returnSn+",payId:"+payId+",returnMoney:"+returnMoney+",actionUser:"+actionUser);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单退款方式结算失败");
		returnInfo.setReturnSn(returnSn);
		String actionNote = StringUtils.EMPTY;
		//验证付款方式、退单数据以及各种数据条件
		//退款方式、结算金额相等则做单条结算操作
		//退单所有退款方式都已结算，则进行整个退单的结算操作
		
		OrderRefund nowOrderRefund = null;
		try {
			if(returnMoney < 0){
				throw new RuntimeException("结算退款金额小于零无法正常操作");
			}
			//结算校验
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效地退单信息");
			}
			if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE.intValue()
					|| orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY.intValue()
					|| orderReturn.getPayStatus().intValue() != ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE){
				throw new RuntimeException("退单不满足结算条件");
			}
			//关联原订单验证
			OrderDistribute orderInfo = builtOrderInfo(orderReturn);
			if(orderInfo == null){
				throw new RuntimeException("原订单不存在三个月，请先迁移订单后结算");
			}
			//退款单，要求原订单必须已发货
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY
					&& orderInfo.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CANCELED.intValue()
					&& orderInfo.getShipStatus().intValue() < ConstantValues.ORDERINFO_SHIP_STATUS.SHIPED){
				throw new RuntimeException("退单关联原订单处于未发货状态不可进行退单结算操作");
			}
			//退款方式验证
			SystemPaymentWithBLOBs systemPayment = systemPaymentMapper.selectByPrimaryKey(payId.byteValue());
			if(systemPayment == null){
				throw new RuntimeException("当前退单结算的退款方式无效payId:"+payId);
			}
			if(payId.intValue() == 3 && orderReturn.getHaveRefund().intValue() == ConstantValues.YESORNO_YES.intValue()){
				OrderRefundExample refundExample = new OrderRefundExample();
				refundExample.or().andRelatingReturnSnEqualTo(returnSn).andReturnPayEqualTo(payId.shortValue());
				List<OrderRefund> checkRefundList = orderRefundMapper.selectByExample(refundExample);
				if(CollectionUtils.isNotEmpty(checkRefundList)){
					OrderRefund checkRefund = checkRefundList.get(0);
					if(checkRefund.getBackbalance().intValue() == ConstantValues.YESORNO_NO.intValue()){
						throw new RuntimeException("退单"+returnSn+"没有退平台币，请先退平台币！");
					}
					
				}
			}
			//发货信息验证
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效地退单发货信息");
			}
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
				throw new RuntimeException("拒收入库单不能进行结算操作");
			}else if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()){
				if(orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
					throw new RuntimeException("退货单当前不是已入库状态不能进行结算操作");
				}
			}//退款单、额外退款单
			
			//退款单验证
			OrderRefundExample orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn).andReturnPayEqualTo(payId.shortValue());
			List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			if(CollectionUtils.isEmpty(orderRefundList)){
				throw new RuntimeException("无法获取当前要结算的退款数据");
			}
			if(orderRefundList.size() > 1){
				throw new RuntimeException("退单中退款方式重复");
			}
			nowOrderRefund = orderRefundList.get(0);
			/*if(nowOrderRefund.getReturnPay().intValue() == 3 && nowOrderRefund.getBackbalance().intValue() == ConstantValues.YESORNO_NO.intValue()){
				throw new RuntimeException("请先退平台币！");
			}*/
			if(nowOrderRefund.getReturnPayStatus().intValue() != ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE.intValue()){
				returnInfo.setIsOk(ConstantValues.YESORNO_NO);
				returnInfo.setMessage("退单退款方式结算失败:退单"+systemPayment.getPayName()+"非待结算状态不可结算操作");
//			  throw new RuntimeException("退单"+systemPayment.getPayName()+"非待结算状态不可结算操作");
				return returnInfo;
			}
			//验证退货单数据完整性：-入库、结算ERP
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()){
				if(orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
					throw new RuntimeException("退货单待入库状态方可进行结算操作");
				}
				ReturnInfo<String> checkReturn = orderSettleService.checkReturnSettle(returnSn);
				if(checkReturn.getIsOk() < ConstantValues.YESORNO_YES){
					throw new RuntimeException("退货单结算验证失败："+checkReturn.getMessage());
				}
			}
			
			//退款方式、结算金额相等则做单条结算操作
			if(nowOrderRefund.getReturnFee().doubleValue() - returnMoney > 0.01
					|| nowOrderRefund.getReturnFee().doubleValue() - returnMoney < -0.01){
				throw new RuntimeException("退单 "+systemPayment.getPayName()+ "("+payId+") 退款"+nowOrderRefund.getReturnFee().doubleValue()+" 与结算金额"+returnMoney +" 不相等，请检查数据");
			}
			OrderRefund updateOrderRefund = new OrderRefund();
			updateOrderRefund.setReturnPaySn(nowOrderRefund.getReturnPaySn());
			updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED);
			updateOrderRefund.setUpdateTime(new Date());
			orderRefundMapper.updateByPrimaryKeySelective(updateOrderRefund);
			actionNote += "退单按照退款方式分别结算：退款方式"+systemPayment.getPayName()+ "("+payId+")  退款金额"+nowOrderRefund.getReturnFee().doubleValue()+" 结算成功！";
			logger.info("退单("+returnSn+")按照退款方式分别结算：退款方式"+systemPayment.getPayName()+ "("+payId+")  退款金额"+nowOrderRefund.getReturnFee().doubleValue()+" 结算成功！");
		
			//操作日志
			orderActionService.addOrderReturnAction(orderReturn.getReturnSn(), actionNote,actionUser);
			
			//检查退款单列表是否都已结算完毕
			orderRefundExample = new OrderRefundExample();
			orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn).
			andReturnPayStatusIn(Arrays.asList(new Byte[]{ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE,ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED}));
			List<OrderRefund> noSettleRefundList = orderRefundMapper.selectByExample(orderRefundExample);
			logger.info("退单整体结算之前的未结算的refund是否为空:"+CollectionUtils.isEmpty(noSettleRefundList));
			if(CollectionUtils.isEmpty(noSettleRefundList)){
				//退单整体结算
				logger.info("退单整体结算:"+returnSn);
				returnOrderSettle(returnSn, "退单按照退款方式结算成功；", actionUser);
			}
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单退款方式结算成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("returnOrderShareSettle"+returnSn+" 退单结算失败，Msg："+e.getMessage(),e);
			if(nowOrderRefund != null){
				//如果单条结算失败，则还原退款单待结算状态
				OrderRefund updateOrderRefund = new OrderRefund();
				updateOrderRefund.setReturnPaySn(nowOrderRefund.getReturnPaySn());
				updateOrderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
				updateOrderRefund.setUpdateTime(new Date());
				orderRefundMapper.updateByPrimaryKeySelective(updateOrderRefund);
			}
			
			returnInfo.setIsOk(ConstantValues.YESORNO_NO);
			returnInfo.setMessage("退单退款方式结算失败:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public void callSettleReturnSharePay(SettleBillQueue billQueue) {
		logger.info("callSettleReturnSharePay.begin..SettleBillQueue"+JSON.toJSONString(billQueue));
		
		OrderSettleBillExample orderSettleBillExample = new OrderSettleBillExample();
		orderSettleBillExample.or().andIdEqualTo(Long.valueOf(billQueue.getId()));
		//orderSettleBillExample.or().andBillNoEqualTo(billQueue.getBillNo()).andBillTypeEqualTo(billQueue.getBillType())
		//.andOrderCodeEqualTo(billQueue.getOrderCode()).andActionUserEqualTo(billQueue.getActionUser()).andResultStatusEqualTo(ConstantValues.ORDER_SETTLE_BILL_RESULT.NOPROCESS.byteValue());
		
		OrderSettleBill updateOrderSettleBill = new OrderSettleBill();
		OrderSettleBill orderSettleBill = null;
		try {
			OrderBillList orderBillList = orderBillListMapper.selectByPrimaryKey(billQueue.getBillNo());
			if(orderBillList == null){
				throw new RuntimeException("调整单据批次号数据库不存在"+billQueue.getBillNo());
			}
			if(orderBillList.getIsSync().intValue() == 2){
				throw new RuntimeException("结算单据已作废");
			}
			if(StringUtils.isBlank(billQueue.getBillNo())){
				throw new RuntimeException("待结算退单结算单据编号为空");
			}
			if(billQueue.getOrderCodeType().intValue() > 0){
				throw new RuntimeException("结算单据单据类型异常");
			}
			if(StringUtils.isBlank(billQueue.getOrderCode())){
				throw new RuntimeException("待结算退单结算单据业务单号为空");
			}
			
			if(orderBillList.getBillType().intValue() != ConstantValues.ORDER_SETTLE_BILL_TYPE.RETURN_SETTLE.intValue()){
				throw new RuntimeException("待结算退单结算单据类型异常："+orderBillList.getBillType().intValue());
			}
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(billQueue.getOrderCode());
			if(orderReturn == null){
				throw new RuntimeException("待结算退单数据不存在");
			}
			/*if(orderReturn.getHaveRefund().intValue() != ConstantValues.YESORNO_NO.intValue() && billQueue.getMoney().doubleValue() < 0.01 && billQueue.getMoney() > -0.01){
				throw new RuntimeException("待结算退单结算金额异常"+billQueue.getMoney());
			}*/
//			OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			OrderDistribute orderInfo = builtOrderInfo(orderReturn);
			if(orderInfo == null){
				throw new RuntimeException("原订单不存在三个月，请先迁移订单后结算");
			}
			List<OrderSettleBill> settleBillList = orderSettleBillMapper.selectByExample(orderSettleBillExample);
			if(CollectionUtils.isEmpty(settleBillList)){
				throw new RuntimeException("待结算订单调整单据不存在");
			}
			if(CollectionUtils.size(settleBillList) > 1){
				throw new RuntimeException("待结算订单调整单据不是唯一");
			}
			orderSettleBill = settleBillList.get(0);
			updateOrderSettleBill.setOrderType(orderInfo.getOrderType());//关联订单类型
			
			//退货单/退款单
			ReturnInfo<String> returnResult = returnOrderShareSettle(billQueue.getOrderCode(), billQueue.getReturnPay(), billQueue.getMoney(), orderBillList.getActionUser());
			//ReturnInfo returnResult = returnOrderSettle(billQueue.getOrderCode(), "批量结算操作("+orderSettleBill.getBillNo()+")", orderBillList.getActionUser());
			if(returnResult.getIsOk() > 0){
				updateOrderSettleBill.setClearTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.SUCCESS.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
				updateOrderSettleBill.setUpdateTime(new Date());
			}else{
				updateOrderSettleBill.setUpdateTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
			}
		} catch (Exception e) {
			logger.error("callSettleReturnSharePay.exception..orderCode:"+billQueue.getOrderCode(),e);
			updateOrderSettleBill.setUpdateTime(new Date());
			updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
			updateOrderSettleBill.setResultMsg("结算异常:"+e.getMessage());
		}finally{
			orderSettleBillMapper.updateByExampleSelective(updateOrderSettleBill, orderSettleBillExample);
		}
	}
	
	@Override
	public void addReturnOrderAction(String returnSn, String actionNote,
			String actionUser) {
		orderActionService.addOrderReturnAction(returnSn, actionNote, actionUser);
	}

	@Override
	public void updateDeposit(SettleBillQueue billQueue) {
	
		logger.info("OrderReturnStServiceImpl.begin..updateDeposit"+JSON.toJSONString(billQueue));
		
		OrderSettleBillExample orderSettleBillExample = new OrderSettleBillExample();
		orderSettleBillExample.or().andIdEqualTo(Long.valueOf(billQueue.getId()));
		
		OrderSettleBill updateOrderSettleBill = new OrderSettleBill();
	//  OrderSettleBill orderSettleBill = null;
		
		try{
			
			OrderBillList orderBillList = orderBillListMapper.selectByPrimaryKey(billQueue.getBillNo());
			if(orderBillList == null){
				throw new RuntimeException("调整单据批次号数据库不存在"+billQueue.getBillNo());
			}
			if(orderBillList.getIsSync().intValue() == 2){
				throw new RuntimeException("调整单据已作废！");
			}
			if(StringUtils.isBlank(billQueue.getBillNo())){
				throw new RuntimeException("调整单据编号为空！");
			}
			
			if(null == billQueue.getReturnSettlementType()){
				throw new RuntimeException("保险金状态为空！");
			}

			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(billQueue.getOrderCode());
			
			if(orderReturn == null){
				throw new RuntimeException("退单中没有此退单号,无退单数据");
			
			}
		
			ReturnInfo<String> returnResult = returnOrderDeposit(billQueue.getOrderCode(), billQueue.getReturnSettlementType());
			if(returnResult.getIsOk() > 0){
				updateOrderSettleBill.setClearTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.SUCCESS.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
				updateOrderSettleBill.setUpdateTime(new Date());
			}else{
				updateOrderSettleBill.setUpdateTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
			}

		}catch(Exception e){
			logger.error("callSettleReturnSharePay.exception..orderCode:"+billQueue.getOrderCode(),e);
			updateOrderSettleBill.setUpdateTime(new Date());
			updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
			updateOrderSettleBill.setResultMsg("结算异常:"+e.getMessage());
		}finally{
			orderSettleBillMapper.updateByExampleSelective(updateOrderSettleBill, orderSettleBillExample);
		}
		logger.info("OrderReturnStServiceImpl.end.updateDeposit");
	}
	
	/**
	 * 
	 * 修改退单退保证金或预付款状态;
	 * **/
	public ReturnInfo<String> returnOrderDeposit(String returnSn, Byte  returnSettlementType ) {
		
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单退款方式结算失败");
		returnInfo.setReturnSn(returnSn);

		try{
			
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andReturnSnEqualTo(returnSn);
			
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSettlementType(returnSettlementType);
			int count = orderReturnMapper.updateByExampleSelective(updateOrderReturn, orderReturnExample);
		
			
			if(count>0){
				returnInfo.setIsOk(ConstantValues.YESORNO_YES);
				returnInfo.setMessage("修改保证金状态成功！");
			}else{
				returnInfo.setIsOk(ConstantValues.YESORNO_NO);
				returnInfo.setMessage("修改保证金状态失败！");
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("returnOrderShareSettle"+returnSn+" 退单结算失败，Msg："+e.getMessage(),e);
		}
		
		return returnInfo;
		
		
		
	}

	/**
	 *修改订单和退单日志 
	 ***/
	public void updateOrderInfoOrOrderReturnLog(SettleBillQueue billQueue) {
		
	logger.info("OrderReturnStServiceImpl.updateOrderInfoOrOrderReturnLog  begin."+JSON.toJSONString(billQueue));
		
		OrderSettleBillExample orderSettleBillExample = new OrderSettleBillExample();
		orderSettleBillExample.or().andIdEqualTo(Long.valueOf(billQueue.getId()));
		
		OrderSettleBill updateOrderSettleBill = new OrderSettleBill();

		try{
			
			OrderBillList orderBillList = orderBillListMapper.selectByPrimaryKey(billQueue.getBillNo());
			if(orderBillList == null){
				throw new RuntimeException("调整单据批次号数据库不存在"+billQueue.getBillNo());
			}
			if(orderBillList.getIsSync().intValue() == 2){
				throw new RuntimeException("调整单据已作废！");
			}
			if(StringUtils.isBlank(billQueue.getBillNo())){
				throw new RuntimeException("调整单据编号为空！");
			}
			
			if(StringUtils.isBlank(billQueue.getMessage())){
				throw new RuntimeException("日志不能为空！");
			}
			
			if(StringUtils.isBlank(billQueue.getOrderCode())){
				throw new RuntimeException("订单号或退单号不能为空！");
			}

			if(null == billQueue.getOrderCodeType()){
				throw new RuntimeException("单号类型不能为空！");
			}

			if(ConstantValues.ORDER_CODE_ORDER_SN == billQueue.getOrderCodeType()){//订单号

//				OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(billQueue.getOrderCode());
				MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(billQueue.getOrderCode()); 
				
				if(orderInfo == null){
					throw new RuntimeException("订单中没有此订单号,无退单数据");
				
				}
				
				
			}else if(ConstantValues.ORDER_CODE_RETURN_SN == billQueue.getOrderCodeType()){//退单号
				OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(billQueue.getOrderCode());
				
				if(orderReturn == null){
					throw new RuntimeException("退单中没有此退单号,无退单数据");
				
				}
			}
		
			ReturnInfo<String> returnResult = returnOrderLog(billQueue);
			
			if(returnResult.getIsOk() > 0){
				updateOrderSettleBill.setClearTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.SUCCESS.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
				updateOrderSettleBill.setUpdateTime(new Date());
			}else{
				updateOrderSettleBill.setUpdateTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
				updateOrderSettleBill.setResultMsg(returnResult.getMessage());
			}

		}catch(Exception e){
			logger.error("callSettleReturnSharePay.exception..orderCode:"+billQueue.getOrderCode(),e);
			updateOrderSettleBill.setUpdateTime(new Date());
			updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
			updateOrderSettleBill.setResultMsg("结算异常:"+e.getMessage());
		}finally{
			orderSettleBillMapper.updateByExampleSelective(updateOrderSettleBill, orderSettleBillExample);
		}
		
		logger.info("OrderReturnStServiceImpl.updateDeposit  end");
		
	}
	
	/**
	 * 
	 * 修改退单退保证金或预付款状态;
	 * **/
	public ReturnInfo<String> returnOrderLog(SettleBillQueue billQueue) {
		

		logger.info("OrderReturnStServiceImpl.ReturnInfo  start");
		
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("订单退单调整单日志失败");
		returnInfo.setReturnSn(billQueue.getOrderCode());
		

		try{
			if(ConstantValues.ORDER_CODE_ORDER_SN == billQueue.getOrderCodeType()){//订单号
				MasterOrderAction orderAction = new MasterOrderAction();
				MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(billQueue.getOrderCode());
				if(orderInfo == null){
					throw new RuntimeException("订单中没有此订单号,无退单数据");
				}
				
				orderAction.setActionUser(billQueue.getActionUser());
				orderAction.setActionNote(billQueue.getMessage());
				orderAction.setMasterOrderSn(billQueue.getOrderCode());
				orderAction.setLogTime(new Date());
				orderAction.setOrderStatus(orderInfo.getOrderStatus()==null ? 0:orderInfo.getOrderStatus());
				orderAction.setPayStatus(orderInfo.getPayStatus()==null ? 0:orderInfo.getPayStatus());
				orderAction.setShippingStatus(orderInfo.getShipStatus() == null ? 0: orderInfo.getShipStatus());
				
				int  count = masterOrderActionMapper.insertSelective(orderAction);
				
				if(count>0){
					returnInfo.setIsOk(ConstantValues.YESORNO_YES);
					returnInfo.setMessage("订单调整单日志插入成功！");
				}else{
					returnInfo.setIsOk(ConstantValues.YESORNO_NO);
					returnInfo.setMessage("订单调整单日志插入失败！");
				}
				/*DistributeAction orderAction = new DistributeAction();
				OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(billQueue.getOrderCode());
				
				if(orderInfo == null){
					throw new RuntimeException("订单中没有此订单号,无退单数据");
				
				}
								
				orderAction.setActionUser(billQueue.getActionUser());
				orderAction.setActionNote(billQueue.getMessage());
				orderAction.setOrderSn(billQueue.getOrderCode());
				orderAction.setLogTime(new Date());
				orderAction.setQuestionStatus(orderInfo.getQuestionStatus()==null ? 0:orderInfo.getQuestionStatus().byteValue());
				orderAction.setOrderStatus(orderInfo.getOrderStatus()==null ? 0:orderInfo.getOrderStatus());
				orderAction.setPayStatus(orderInfo.getPayStatus()==null ? 0:orderInfo.getPayStatus());
				orderAction.setShippingStatus(orderInfo.getShipStatus() == null ? 0: orderInfo.getShipStatus());
				
				int  count = distributeActionMapper.insertSelective(orderAction);
				
				if(count>0){
					returnInfo.setIsOk(ConstantValues.YESORNO_YES);
					returnInfo.setMessage("订单调整单日志插入成功！");
				}else{
					returnInfo.setIsOk(ConstantValues.YESORNO_NO);
					returnInfo.setMessage("订单调整单日志插入失败！");
				}*/
				
			}else if(ConstantValues.ORDER_CODE_RETURN_SN == billQueue.getOrderCodeType()){//退单号
				
				OrderReturnAction orderReturnAction = new OrderReturnAction();
				OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(billQueue.getOrderCode());
				
				if(orderReturn == null){
					throw new RuntimeException("退单中没有此退单号,无退单数据");
					
				}
				
				
				OrderReturnShip ors = selectOrderReturnShipByOrderSn(billQueue.getOrderCode());
				orderReturnAction.setActionUser(billQueue.getActionUser());
				orderReturnAction.setActionNote(billQueue.getMessage());
				orderReturnAction.setReturnSn(billQueue.getOrderCode());
				orderReturnAction.setLogTime(new Date());
				orderReturnAction.setReturnOrderStatus(orderReturn.getReturnOrderStatus()==null ? 0 : Integer.valueOf(orderReturn.getReturnOrderStatus()));
				orderReturnAction.setReturnShippingStatus(orderReturn.getShipStatus() ==null ?0:orderReturn.getShipStatus().intValue());
				orderReturnAction.setCheckinStatus(ors.getCheckinStatus() == null ? 0 :ors.getCheckinStatus());
				orderReturnAction.setIsGoodReceived(ors.getIsGoodReceived() == null ?0:ors.getIsGoodReceived());
				
				orderReturnAction.setQualityStatus(ors.getQualityStatus() == null ?0:ors.getQualityStatus());
				orderReturnAction.setReturnPayStatus(orderReturn.getPayStatus()== null ?0:orderReturn.getPayStatus().intValue());
				
				
				int  count = orderReturnActionMapper.insertSelective(orderReturnAction);
				
				if(count > 0){
					returnInfo.setIsOk(ConstantValues.YESORNO_YES);
					returnInfo.setMessage("退单调整单日志插入成功！");
				}else{
					returnInfo.setIsOk(ConstantValues.YESORNO_NO);
					returnInfo.setMessage("退单调整单日志插入失败！");
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("returnOrderShareSettle"+billQueue.getOrderCode()+" 订单退单调整单日志插入失败，Msg："+e.getMessage(),e);
		}
		
		logger.info("OrderReturnStServiceImpl.ReturnInfo  end");
		
		return returnInfo;
		
		
	}
	
	private OrderReturnShip selectOrderReturnShipByOrderSn(String returnSn){
		
		OrderReturnShipExample orderReturnShipExample = new OrderReturnShipExample();
		orderReturnShipExample.or().andRelatingReturnSnEqualTo(returnSn);
	
		List<OrderReturnShip> list = orderReturnShipMapper.selectByExample(orderReturnShipExample);
		OrderReturnShip orderReturnShip = null;

		if(StringUtil.isNotNullForList(list)){
			orderReturnShip = list.get(0);
		}
		
		return orderReturnShip;	 
	}
	
	@Override
	public List<OrderReturnAction> getOrderReturnActionList(OrderReturnAction model) {
		return orderActionService.getOrderReturnActionList(model);
	}
	
	@Override
	public Map<String, Object> returnForward(ReturnForward returnForward, String type,String userName) {
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("success", false);
		String returnSn= returnForward.getReturnSn();
		if(StringUtil.isBlank(returnSn)){
			map.put("message", "退单号为空！");
			return map;
		}
		if(StringUtil.isBlank(type)){
			map.put("message", "类别为空！");
			return map;
		}
		StringBuffer noteBuff = new StringBuffer();
		noteBuff.append(returnSn+"退单转运：<br />");
		try {
			ReturnForwardExample returnForwardExample = new ReturnForwardExample();
			ReturnForwardExample.Criteria criteria = returnForwardExample.or();
			criteria.andReturnSnEqualTo(returnSn);
			List<ReturnForward> list = returnForwardMapper.selectByExample(returnForwardExample);
			if(StringUtil.equalsIgnoreCase("init", type)){
				if(CollectionUtils.isNotEmpty(list)){
					map.put("returnForward", list.get(0));
				}
			}else if(StringUtil.equalsIgnoreCase("edit", type)){
				if(CollectionUtils.isNotEmpty(list)){
					returnForward.setId(list.get(0).getId());
					returnForward.setUpdateTime(new Date());
					if(!StringUtils.equalsIgnoreCase(list.get(0).getExpressName(), returnForward.getExpressName())){
						noteBuff.append("承运商由 "+list.get(0).getExpressName()+" 修改为 "+returnForward.getExpressName()+"；<br />");
					}
					
					if(!StringUtils.equalsIgnoreCase(list.get(0).getInvoiceNo(), returnForward.getInvoiceNo())){
						noteBuff.append("快递单号由 "+list.get(0).getInvoiceNo()+" 修改为 "+returnForward.getInvoiceNo()+"；<br />");
					}
					returnForwardMapper.updateByPrimaryKeySelective(returnForward);
					orderActionService.addOrderReturnAction(returnSn, noteBuff.toString(), userName);
				}else{
					returnForward.setAddTime(new Date());
					returnForwardMapper.insert(returnForward);
					noteBuff.append("创建！");
					orderActionService.addOrderReturnAction(returnSn, noteBuff.toString(), userName);
				}
			}
			map.put("success", true);
		} catch (Exception e) {
			logger.error("退单转发出错！",e);
			map.put("message", "退单转发出错！"+e);
		}
		return map;
	}
}
