package com.work.shop.oms.orderop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.oms.bean.*;
import com.work.shop.oms.order.service.MasterOrderPayService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.cardAPI.api.CardCartSearchServiceApi;
import com.work.shop.cardAPI.bean.APIBackMsgBean;
import com.work.shop.cardAPI.bean.ParaUserCardStatus;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.param.bean.CreateOrderReturnGoods;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.CreateReturnVO;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.DistributeQuestionMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderRefundMapper;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.orderop.service.OrderNormalService;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.shoppay.service.ShopPayService;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.MqConfig;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单取消或者退单服务
 * @author QuYachu
 *
 */
@Service("orderCancelService")
public class OrderCancelServiceImpl implements OrderCancelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
    private MasterOrderPayService masterOrderPayService;

	@Resource
	OrderActionService orderActionService;

	@Resource
	DistributeActionService distributeActionService;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private UserPointsService userPointsService;

	@Resource(name = "orderNormalService")
	private OrderNormalService orderNormalService;

	private CardCartSearchServiceApi cardCartSearchServiceApi;

	@Resource
	private DistributeShipService distributeShipService;

	@Resource(name="orderReturnService")
	private OrderReturnService orderReturnService;

	@Resource(name = "redisClient")
	private RedisClient redisClient;

	@Resource(name = "orderStockRealeseJmsTemplate")
	private JmsTemplate orderStockRealeseJmsTemplate;

	@Resource(name = "shopPayServiceImpl")
	private ShopPayService shopPayService;

	@Resource
	private JmsSendQueueService jmsSendQueueService;

	@Resource(name = "uniteStockServiceImpl")
	private UniteStockService uniteStockService;
	
	@Resource
	private OrderRefundMapper orderRefundMapper;

	@Resource
	DistributeQuestionMapper distributeQuestionMapper;

	/**
	 * 主订单取消：
	 * 已拆单：取消主单下所有符合取消的分配单并且通知分配单供应商， 全部取消后生成退货单
	 * 为拆单：主单取消生成退款单
	 * 子订单取消：
	 * 取消子订单状态并且通知分配单供应商取消订单
	 */
	private ReturnInfo cancelOrder(MasterOrderInfo master, List<OrderDistribute> mbDistributes,
			List<OrderDistribute> distributes, OrderStatus orderStatus, String type) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null && StringUtil.isListNull(distributes)) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单取消操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单取消操作！");
			return info;
		}
		if (ConstantValues.METHOD_SOURCE_TYPE.POS.equals(orderStatus.getSource())) {
			orderStatus.setMessage("POS取消：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(orderStatus.getSource())) {
			orderStatus.setMessage("前台取消：" + orderStatus.getMessage() == null ? "" : orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(orderStatus.getSource())) {
			orderStatus.setMessage("ERP订单：" + orderStatus.getMessage());
		} else {
			orderStatus.setMessage("取消：" + orderStatus.getMessage());
		}
		if (StringUtil.isTrimEmpty(orderStatus.getCode())) {
			logger.error("指定的订单取消原因编码为空！");
			info.setMessage("指定的订单取消原因编码为空！");
			return info;
		}
		OrderCustomDefine define = orderCustomDefineMapper.selectByPrimaryKey(orderStatus.getCode());
		if (define == null) {
			logger.error("指定的订单取消原因编码[" + orderStatus.getCode() + "]不存在");
			info.setMessage("指定的订单取消原因编码[" + orderStatus.getCode() + "]不存在");
			return info;
		}
		// 获取订单扩展信息
		String masterOrderSn = master.getMasterOrderSn();
		logger.debug("订单取消：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		if (StringUtil.isListNotNull(distributes)) {
			info = cancelOrderByThirdDistribute(getOrderSns(distributes), distributes, define, orderStatus, master);
			if (info.getIsOk() == Constant.OS_NO) {
				return info;
			}
		}
		// 主订单取消
		if (Constant.order_type_master.equals(type)) {
			info = cancelOrderByMaster(masterOrderSn, master, define, orderStatus);
		}
		
		if (info.getIsOk() == Constant.OS_YES) {
			ReturnInfo tInfo = judgeMasterOrderStatus(masterOrderSn, define, (byte)Constant.OI_ORDER_STATUS_CANCLED, type);
			if (tInfo.getIsOk() == Constant.OS_YES) {
				master.setOrderStatus((byte)Constant.OI_ORDER_STATUS_CANCLED);
			}
		}
		// 处理失败
		if (info.getIsOk() == Constant.OS_NO) {
			return info;
		}
		ReturnInfo shipInfo = distributeShipService.judgeMasterShipedStatus(masterOrderSn);
		// 取消订单时创建退单
		// 判断主单订单是否全部取消 || 总订单已发货 全部取消生成退货单 
		if (shipInfo.getIsOk() == Constant.OS_YES) {
			info = createReturnOrder(masterOrderSn, orderStatus, 1);
		} else if (Constant.OI_ORDER_STATUS_CANCLED == master.getOrderStatus().intValue()) {
			// 订单取消
			info = createReturnOrder(masterOrderSn, orderStatus, 0);
		}
		return info;
	}

    /**
     * 合并订单状态
     * @param masterOrderSn 订单编码
     * @param define 取消原因
     * @param orderStatus 订单状态
     * @param type 类型 0 主订单、1交货单
     * @return ReturnInfo
     */
	private ReturnInfo judgeMasterOrderStatus(String masterOrderSn, OrderCustomDefine define, byte orderStatus, String type) {
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
				if (orderDistribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED || orderDistribute.getOrderStatus() == orderStatus) {
					count++;
					continue;
				}
			}
			if (count != distributes.size()) {
				info.setMessage("主订单[" + masterOrderSn + "] 下子订单状态不一致！");
				return info;
			}
			// 删除问题单
			orderNormalService.deleteMasterOrderQuestion(masterOrderSn);
		}

		try {
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setOrderStatus(orderStatus);
			updateMaster.setCancelCode(define.getCode());
			updateMaster.setCancelReason(define.getName());
			updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
			updateMaster.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
		} catch (Exception e) {
			info.setMessage("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage());
			logger.error("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage(), e);
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单已取消");
		return info;
	}
	
	/**
	 * 订单取消
	 * @param masterOrderSn 订单编码
	 * @param orderStatus
	 */
	@Override
	public ReturnInfo cancelOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		String redisKey = "";
		try {
			long result = -1;
			redisKey = "order_cancel_" + masterOrderSn;
			try {
				result = redisClient.setnx(redisKey, masterOrderSn);
				// redis 超时时间有5秒钟改成30秒
				redisClient.expire(redisKey, 30);
			} catch (Throwable e) {
				logger.error("订单[" + masterOrderSn + "]判断订单是否取消中,读取redis异常:" + e);
			}
			if (result == 0) {
				info.setMessage("订单正在取消中，请稍后再重试！");
				return info;
			}
			if (orderStatus == null) {
				logger.error("[orderStatus]传入参数为空，不能进行订单取消操作！");
				info.setMessage("[orderStatus]传入参数为空，不能进行订单取消操作！");
				return info;
			}
			logger.debug("订单取消：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (master == null) {
				logger.warn("订单[" + masterOrderSn + "]不存在，无法确认");
				info.setMessage("订单[" + masterOrderSn + "]不存在，无法确认");
				return info;
			}
			if (StringUtil.isEmpty(orderStatus.getSource())) {
				logger.error("订单[" + masterOrderSn + "][orderStatus.resource]取消订单来源类型空，不能进行订单取消操作！");
				info.setMessage("[orderStatus.resource]取消订单来源类型空，不能进行订单取消操作！");
				return info;
			}
			// 默认不创建
			if (StringUtil.isEmpty(orderStatus.getType())) {
				orderStatus.setType(ConstantValues.CREATE_RETURN.NO);
			}
			ReturnInfo tInfo = checkMasterConditionOfExecution(master, masterOrderSn, orderStatus.getSource());
			if (tInfo.getIsOk() == Constant.OS_NO) {
				return tInfo;
			}
			List<OrderDistribute> distributes = null;
			if (master.getSplitStatus() != (byte) 0) {
				// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
				OrderDistributeExample distributeExample = new OrderDistributeExample();
				OrderDistributeExample.Criteria criteria = distributeExample.or();
				criteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
				criteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
				distributes = this.orderDistributeMapper.selectByExample(distributeExample);
				if (StringUtil.isListNotNull(distributes)) {
					ReturnInfo<List<OrderDistribute>> checkInfo = checkConditionOfExecution(distributes, orderStatus.getSource(), master);
					if (checkInfo.getIsOk() == Constant.OS_NO) {
						return checkInfo;
					}
					distributes = checkInfo.getData();
				}
			}
            setMasterOrderReturnInfo(master, orderStatus);
			info = cancelOrder(master, null, distributes, orderStatus, Constant.order_type_master);
		} finally {
			clearRedis(redisKey);
		}
		return info;
	}

    /**
     * 设置订单取消是否创建退单
     * @param master
     * @param orderStatus
     */
	private void setMasterOrderReturnInfo(MasterOrderInfo master, OrderStatus orderStatus) {
		// 订单来源
		String orderFrom = master.getOrderFrom();
		if (!Constant.DEFAULT_SHOP.equals(orderFrom)) {
			return;
		}
		// 订单是否付款
        int payStatus = master.getPayStatus().intValue();
		if (payStatus == Constant.OI_PAY_STATUS_UNPAYED) {
		    return;
        }
		String masterOrderSn = master.getMasterOrderSn();
		// 获取订单支付信息
        List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
        if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
            return;
        }
        MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
        int payId = masterOrderPay.getPayId().intValue();
        if (payId != Constant.PAYMENT_ZHANGQI_ID && payId != Constant.PAYMENT_YINCHENG) {
            return;
        }
		// 公司类型
		MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (masterOrderInfoExtend == null) {
			return;
		}
		// 公司类型 1内部公司、2外部公司
		Integer companyType = masterOrderInfoExtend.getCompanyType();
		if (companyType != null && companyType == 1) {
			// 不创建退单
			orderStatus.setType("2");
			// 不创建退单
			orderStatus.setReturnType(7);
		}
	}

	/**
	 * 处理并发缓存key
	 * @param redisKey
	 */
	public void clearRedis(String redisKey) {
		if (StringUtil.isNotEmpty(redisKey)) {
			try {
				redisClient.del(redisKey);
			} catch (Throwable e) {
				logger.error("使用缓存KEY[" + redisKey + "]删除redis中缓存数据异常:" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 指定订单交货单取消
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo cancelOrderByOrderSn(OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单取消操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单取消操作！");
			return info;
		}
		String orderSn = orderStatus.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		logger.debug("订单取消：orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.warn("订单[" + orderSn + "]不存在，无法确认");
			info.setMessage("订单[" + orderSn + "]不存在，无法确认");
			return info;
		}
		String redisKey = "";
		try {
			long result = -1;
			redisKey = "order_cancel_" + distribute.getMasterOrderSn();
			try {
				result = redisClient.setnx(redisKey, distribute.getMasterOrderSn());
				// redis 超时时间有5秒钟改成30秒
				redisClient.expire(redisKey, 30);
			} catch (Throwable e) {
				logger.error("订单[" + distribute.getMasterOrderSn() + "]判断订单是否取消中,读取redis异常:" + e);
			}
			if (result == 0) {
				info.setMessage("订单正在取消中，请稍后再重试！");
				return info;
			}
			
			if (StringUtil.isEmpty(orderStatus.getSource())) {
				logger.error("订单[" + orderSn + "][orderStatus.resource]取消订单来源类型空，不能进行订单取消操作！");
				info.setMessage("[orderStatus.resource]取消订单来源类型空，不能进行订单取消操作！");
				return info;
			}
			MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
			if (master == null) {
				logger.error("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单取消操作！");
				info.setMessage("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单取消操作！");
				return info;
			}
            // 默认不创建
			if (StringUtil.isEmpty(orderStatus.getType())) {
				orderStatus.setType(ConstantValues.CREATE_RETURN.NO);
			}
			ReturnInfo tInfo = checkConditionOfExecution(distribute, orderSn, orderStatus.getSource(), master);
			if (tInfo.getIsOk() == Constant.OS_NO) {
				return tInfo;
			}
			List<OrderDistribute> mbDistributes = null;
			List<OrderDistribute> thDistributes = null;
			if (Constant.SUPPLIER_TYPE_MB.equals(distribute.getSupplierCode())) {
				mbDistributes = new ArrayList<OrderDistribute>();
				mbDistributes.add(distribute);
			} else {
				thDistributes = new ArrayList<OrderDistribute>();
				thDistributes.add(distribute);
			}
			// 交货单取消
			info = cancelOrder(master, mbDistributes, thDistributes, orderStatus, Constant.order_type_distribute);
		} finally {
			clearRedis(redisKey);
		}
		return info;
	}

    /**
     * 订单取消(共用方法)
     * @param orderSns 交货单列表
     * @param distributes 交货单信息
     * @param define 取消原因
     * @param orderStatus 订单状态
     * @param master 订单信息
     * @return ReturnInfo
     */
	private ReturnInfo cancelOrderByThirdDistribute(List<String> orderSns, List<OrderDistribute> distributes,
			OrderCustomDefine define, OrderStatus orderStatus, MasterOrderInfo master) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法取消");
			info.setMessage("[" + orderSns + "]订单不存在，无法取消");
			return info;
		}
		logger.debug("订单取消 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			if(distribute.getOrderStatus().equals(ConstantValues.ORDER_STATUS.CANCELED)) {
				logger.error("订单号[" + orderSn + "]的订单已经取消，请不要重复操作");
				info.setMessage("订单号[" + orderSn + "]的订单已经取消，请不要重复操作");
				continue ;
			}
			ReturnInfo tInfo = processDistribute(orderSn, distribute.getMasterOrderSn(), define, orderStatus);
			if (tInfo.getIsOk() == Constant.OS_NO) {
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单["+orderSns+"]订单取消成功");
		logger.debug("订单["+orderSns+"]订单取消成功");
		return info;
	}

	/**
	 * 处理交货单取消
	 * @param orderSn 交货单编码
	 * @param masterOrderSn 订单编码
	 * @param define 取消原因
	 * @param orderStatus 订单状态
	 * @return ReturnInfo
	 */
	private ReturnInfo processDistribute(String orderSn, String masterOrderSn, OrderCustomDefine define, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		OrderDistribute distribute = this.orderDistributeMapper.selectByPrimaryKey(orderSn);
		try {
			// 判断订单是否已经创建了采购单
			if (distribute.getLastUpdateTime() != null) {
				boolean isPush = true;
				// 已创建采购单&&是待审问题单(编码17)时不通知写入MQ
				DistributeQuestionExample example = new DistributeQuestionExample();
				example.or().andOrderSnEqualTo(orderSn).andQuestionCodeEqualTo("17");
				List<DistributeQuestion> questions = distributeQuestionMapper.selectByExample(example);
				if (CollectionUtils.isNotEmpty(questions)) {
					isPush = false;
				}
				if (isPush) {
					// 取消后通知供应商取消
					DistributeShippingBean message = new DistributeShippingBean();
					message.setOrderSn(orderSn);
					message.setActionUser(orderStatus.getAdminUser());
					jmsSendQueueService.sendQueueMessage(MqConfig.supplier_order_cancel, JSON.toJSONString(message));
				}
			}
			//删除问题单
			OrderDistribute updateDistribute = new OrderDistribute();
			if (distribute.getQuestionStatus().equals(ConstantValues.ORDERQUESTION_STATUS.QUESTION)) {
				updateDistribute.setQuestionStatus(ConstantValues.ORDERQUESTION_STATUS.NORMAL);
				updateDistribute.setQuestionTime(null);
				orderNormalService.deleteOrderQuestion(orderSn);
			}
			// 交货单信息更新保存
			updateDistribute.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CANCLED);
			updateDistribute.setCancelCode(define.getCode());
			updateDistribute.setCancelReason(define.getName());
			updateDistribute.setOrderSn(orderSn);
			updateDistribute.setUpdateTime(new Date());
			orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
			//订单操作记录
			distributeActionService.addOrderAction(orderSn, orderStatus.getMessage(), orderStatus.getAdminUser());
			logger.debug("订单[" + orderSn + "]取消成功!");
			// 业务监控
		} catch (Exception e) {
			logger.error("取消订单[" + orderSn + "]失败，错误信息：", e);
			distributeActionService.addOrderAction(orderSn, "<font style=color:red;>取消：错误信息 "+ e.getMessage() + "</font>", orderStatus.getAdminUser());
			info.setMessage("取消订单[" + orderSn + "]失败，错误信息：" + e.getMessage());
			return info;
		}
/*		String msg = "";
		//释放库存
		try {
			// 调用库存释放处理模块
			OrderStatus stockStatus = new OrderStatus();
			stockStatus.setMasterOrderSn(masterOrderSn);
			stockStatus.setOrderSn(orderSn);
			stockStatus.setType(Constant.order_type_distribute);
			orderStockRealeseJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(stockStatus)));
		} catch (Exception e) {
			msg = "取消订单,释放库存：<font style=color:red;>" + e.getMessage() + "</font>";
			logger.error("取消订单[" + orderSn + "],释放库存异常: ", e);
		}*/
		info.setIsOk(Constant.OS_YES);
		info.setMessage("处理完成");
		return info;
	}

	/**
	 * 订单取消(共用方法)
	 * @param masterOrderSn 订单编码
	 * @param master 订单信息
	 * @param define 取消原因
	 * @param orderStatus 订单状态信息
	 * @return ReturnInfo
	 */
	private ReturnInfo cancelOrderByMaster(String masterOrderSn, MasterOrderInfo master, OrderCustomDefine define, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null) {
			logger.warn("[" + masterOrderSn + "]订单不存在，无法取消");
			info.setMessage("[" + masterOrderSn + "]订单不存在，无法取消");
			return info;
		}
		logger.debug("订单取消 masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		String note = orderStatus.getMessage();
		note = (null == note ? "" : note+",") + "[取消code:" + define.getCode() + ", 取消原因:" + define.getName() + "]";
		if (master.getOrderStatus().equals(ConstantValues.ORDER_STATUS.CANCELED)) {
			logger.error("订单号[" + masterOrderSn + "]的订单已经取消，请不要重复操作");
			info.setMessage("订单号[" + masterOrderSn + "]的订单已经取消，请不要重复操作");
			return info;
		}
		try {
			// 删除问题单
			ReturnInfo tInfo = orderNormalService.deleteMasterOrderQuestion(masterOrderSn);
			if (tInfo.getIsOk() == Constant.OS_NO) {
				return tInfo;
			}

			// 订单取消，返回正常单
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CANCLED);
			updateMaster.setUpdateTime(new Date());
			updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			//订单操作记录
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, note, orderStatus.getAdminUser());
			logger.debug("订单[" + masterOrderSn + "]取消成功!");
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]取消失败，错误信息：", e);
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>取消：错误信息 "+ e.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			info.setMessage("订单[" + masterOrderSn + "]取消失败，错误信息：" + e.getMessage());
			return info;
		}
		//释放库存
		/*if (master.getSplitStatus().byteValue() == Constant.SPLIT_STATUS_UNSPLITED.byteValue()) {
			String msg = "取消订单,释放库存";
			try {
				OrderStatus stockStatus = new OrderStatus();
				stockStatus.setMasterOrderSn(masterOrderSn);
				stockStatus.setType(Constant.order_type_master);
				orderStockRealeseJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(stockStatus)));
			} catch (Exception e) {
				msg = "取消订单,释放库存：<font style=color:red;>" + e.getMessage() + "</font>";
				logger.error(masterOrderSn + msg);
				logger.error("取消订单[" + masterOrderSn + "],释放库存异常: " + e.getMessage(), e);
			} finally {
				// 订单操作日志记录
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, msg, orderStatus.getAdminUser());
			}
		}*/
		info.setIsOk(Constant.OS_YES);
		info.setMessage("[" + masterOrderSn + "]订单取消成功");
		logger.debug("[" + masterOrderSn + "]订单取消成功");
		return info;
	}
	
	/**
	 * 创建退单
	 * @param masterOrderSn 订单编码
	 * @param orderStatus
	 * @param createType 0订单取消、1创建退单
	 */
	@Override
	public ReturnInfo createReturnOrder(String masterOrderSn, OrderStatus orderStatus, int createType) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		// 全部取消订单之后，生成退货单
		// 判断主单订单是否全部取消 ，全部取消生成退货单
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		// 符合生成退单条件
		List<String> useCards = new ArrayList<String>();
		if (!OrderAttributeUtil.isPosOrder(master.getSource())) {
		    // 需要创建退单
			if (ConstantValues.CREATE_RETURN.YES.equals(orderStatus.getType())) {
			    if (master.getPayStatus() == Constant.OI_PAY_STATUS_PAYED && (master.getMoneyPaid().doubleValue() >= 0 || master.getSurplus().doubleValue() > 0)) {
			    	// 已付款订单
                    List<CreateOrderReturnGoods> returnGoodsList = processOrderReturnGoods(master, useCards);
                    ReturnInfo<List<OrderReturn>> orderReturnInfo = orderReturnService.getEffectiveReturns(masterOrderSn);
                    if (orderReturnInfo != null && orderReturnInfo.getIsOk() == Constant.OS_YES
                            && StringUtil.isListNull(orderReturnInfo.getData())) {

                        CreateReturnVO returnVO = createOrderReturnVo(master, orderStatus, returnGoodsList, createType);
                        String returnMsg = null;
                        if (createType == 0) {
                            // 整单取消
                            returnMsg = "取消时创建退单:";
                        } else {
                            // 订单退单
                            returnMsg = "发货时创建退单:";
                        }
                        // 创建退单支付单
                        ReturnInfo ri = orderReturnService.createOrderReturnPay(returnVO);
                        if (ri == null || ri.getIsOk() == Constant.OS_NO) {
                            String msg = (ri == null ? masterOrderSn + "创建退单返回结果为空!" : masterOrderSn + "创建退单返回结果:" + ri.getMessage());
                            masterOrderActionService.insertOrderActionBySn(masterOrderSn,
                                    "取消时创建退单异常:<font style=color:red;>" + msg + "</font>", orderStatus.getAdminUser());
                            logger.error(msg);
                            info.setMessage("订单取消成功," + msg);
                            return info;
                        }

                        // 处理订单取消是否自动退款
                        masterOrderActionService.insertOrderActionBySn(masterOrderSn, returnMsg + ri.getReturnSn(), orderStatus.getAdminUser());
                        logger.debug("创建删除商品退款单 end：masterOrderSn=" + masterOrderSn);
                        // 通知统一库存释放
                        uniteStockService.realese(masterOrderSn);
                    }
				} else {
					// 不需要创建退单
					List<String> orderUseCardList = getOrderGoodsCards(masterOrderSn);
					if (orderUseCardList != null && orderUseCardList.size() > 0) {
						useCards.addAll(orderUseCardList);
					}
					// 部分付款订单取消余额退还
                    processOrderCancelSurplus(master);
				}
			} else {
                Integer returnType = orderStatus.getReturnType();
                if (returnType != null && returnType == 7) {
                    // 订单取消，不创建退单
                    // 通知统一库存释放
                    uniteStockService.realese(masterOrderSn);
                } else {
                    // 未支付订单取消
                    List<String> orderUseCardList = getOrderGoodsCards(masterOrderSn);
                    if (orderUseCardList != null && orderUseCardList.size() > 0) {
                        useCards.addAll(orderUseCardList);
                    }
                    // 库存冻结取消
                    orderStatus.setStockRealese(true);
                }
			}
			
			// 取消订单
			if (createType == 0) {
			    // 处理积分退还
                processOrderIntegral(master, orderStatus);
				// 处理订单卡券退还
                processOrderCard(master, useCards, orderStatus);
			}
		}
		// 处理退单后续事项
        processReturnOrderFollow(master, orderStatus);

		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单取消成功！");
		return info;
	}

    /**
     * 处理退单后续事项
     * @param master
     */
	private void processReturnOrderFollow(MasterOrderInfo master, OrderStatus orderStatus) {
        // 交易类型 1：款到发货 2：货到付款 3：担保交易
        Byte transType = master.getTransType();
        if (transType != null && transType == Constant.OI_TRANS_TYPE_GUARANTEE) {
            // 担保交易, 不需要处理
        } else {
            // 订单取消释放商城库存
			OrderStatus stockStatus = new OrderStatus();
            stockStatus.setMasterOrderSn(master.getMasterOrderSn());
            stockStatus.setType(Constant.order_type_master);
            orderStockRealeseJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(stockStatus)));
        }
    }

    /**
     * 处理订单退单、取消商品信息
     * @param master
     * @param useCardList
     * @return List<CreateOrderReturnGoods>
     */
	private List<CreateOrderReturnGoods> processOrderReturnGoods(MasterOrderInfo master, List<String> useCardList) {
        List<CreateOrderReturnGoods> returnGoodsList = new ArrayList<CreateOrderReturnGoods>();
	    String masterOrderSn = master.getMasterOrderSn();

        if (master.getSplitStatus().intValue() == Constant.SPLIT_STATUS_UNSPLITED.intValue()) {
            // 未拆单
            processOrderReturnGoods(masterOrderSn, useCardList, returnGoodsList);
        } else {
            // 已拆单
            processOrderReturnGoods(masterOrderSn, useCardList, returnGoodsList);
        }

        return returnGoodsList;
    }

    /**
     * 创建退单
     * @param master
     * @param orderStatus
     * @param returnGoodsList
     * @param createType 0整单取消、1部分退单
     * @return CreateReturnVO
     */
	private CreateReturnVO createOrderReturnVo(MasterOrderInfo master, OrderStatus orderStatus, List<CreateOrderReturnGoods> returnGoodsList, int createType) {

	    String masterOrderSn = master.getMasterOrderSn();

        CreateReturnVO returnVO = new CreateReturnVO();
        returnVO.setMasterOrderSn(masterOrderSn);
        returnVO.setActionUser(orderStatus.getAdminUser());
        returnVO.setBvValue(master.getBvValue());
        returnVO.setBaseBvValue(master.getBaseBvValue());
        returnVO.setPoints(master.getPoints().doubleValue());

        Double returnMoney = 0.0;
        // 整单取消
        if (createType == 0) {
            returnVO.setReturnSource(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_CANCEL);
            //returnMoney = master.getMoneyPaid().add(master.getSurplus()).add(master.getPoints()).doubleValue();
            returnMoney = master.getMoneyPaid().subtract(master.getTax()).doubleValue();
            returnVO.setReturnShipping(Double.valueOf(master.getShippingTotalFee().toString()));
        } else {
            // 订单退单
            returnVO.setReturnSource(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP);
            returnMoney = Math.abs(master.getTotalPayable().doubleValue());
        }
        returnVO.setReturnMoney(returnMoney);
        returnVO.setOrderReturnGoodsList(returnGoodsList);

        return returnVO;
    }

    /**
     * 处理订单积分退还
     * @param master
     * @param orderStatus
     */
	private void processOrderIntegral(MasterOrderInfo master, OrderStatus orderStatus) {
        if (master.getIntegral().intValue() > 0) {
            String masterOrderSn = master.getMasterOrderSn();
            int integral = master.getIntegral();
            ReturnInfo pointResponse = userPointsService.giveUserPoints(master.getUserId(),integral, masterOrderSn);
            String pointsNote = null;
            if (pointResponse.getIsOk() > 0) {
                logger.info("订单[" + masterOrderSn + "]取消时CAS积分("+integral+")返还成功！masterOrderSn:"+masterOrderSn+",userId:" + master.getUserId());
                pointsNote = "订单[" + masterOrderSn + "]取消时CAS积分("+ integral +")返还成功！";
            } else {
                logger.error("订单[" + masterOrderSn + "]取消时CAS积分("+integral+")返还失败！masterOrderSn:"+masterOrderSn+",userId:" + master.getUserId());
                pointsNote = "订单[" + masterOrderSn + "]取消时CAS积分("+integral+")返还失败！错误信息:"+pointResponse.getMessage();
            }
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, orderStatus.getAdminUser());
        }
    }

    /**
     * 处理订单卡券退还
     * @param master
     * @param useCards
     * @param orderStatus
     */
    private void processOrderCard(MasterOrderInfo master, List<String> useCards, OrderStatus orderStatus) {
	    String masterOrderSn = master.getMasterOrderSn();
        MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
        String freePostCard = null;
        if (extend != null) {
            freePostCard = extend.getFreePostCard();
        }
        // 订单取消订单 退红包卷卡
        if (StringUtil.isNotNull(master.getBonusId()) || StringUtil.isListNotNull(useCards) || StringUtil.isNotEmpty(freePostCard)) {
            ParaUserCardStatus paraBean = new ParaUserCardStatus();
            if (StringUtil.isListNotNull(useCards)) {
                String cards = "";
                for (int i = 0;i < useCards.size();i++) {
                    if (useCards.size() == 1) {
                        cards = useCards.get(i);
                    } else if (useCards.size() == (i +1)) {
                        cards += useCards.get(i);
                    } else {
                        cards += useCards.get(i) + Constant.STRING_SPLIT_COMMA;
                    }
                }
                // 打折券，多个以，分割
                paraBean.setCouponNo(cards);
            }
            if (StringUtil.isNotEmpty(master.getBonusId())) {
                // 红包，多个以,分割
                paraBean.setPackageNo(master.getBonusId());
            }
            if (StringUtil.isNotEmpty(freePostCard)) {
                // 免邮券卡号
                paraBean.setFreeNo(freePostCard);
            }
            // 订单号
            paraBean.setOrderNo(masterOrderSn);
            // 状态，3作废、4使用
            paraBean.setStatus(Constant.CARD_CANCEL);
            // 下单人
            paraBean.setUserId(master.getUserId());
            APIBackMsgBean<Integer> result = null;
            try {
                String errorMSg = "订单取消 通知券卡中心返还红包券卡成功";
                logger.info("订单[" + masterOrderSn + "]取消 通知券卡中心返还红包券卡 paraBean=" + JSON.toJSONString(paraBean));
                result = cardCartSearchServiceApi.updateUserCard(paraBean);
                logger.info("订单[" + masterOrderSn + "]取消 返还红包券卡 result=" + JSON.toJSONString(result));
                if (result == null || result.getSoaStateBean() == null || "0".equals(result.getSoaStateBean().getIsOk())) {
                    errorMSg = result == null ? "通知券卡中心使用券卡异常:接口返回结果为空" : result.getSoaStateBean().getMsg();
                    errorMSg = "<font style=color:red;>" + errorMSg + "</font>";
                }
                masterOrderActionService.insertOrderActionBySn(masterOrderSn, errorMSg, orderStatus.getAdminUser());
            } catch (Exception e) {
                logger.error("订单[" + masterOrderSn + "]取消 通知券卡中心返还红包券卡异常：" + e.getMessage(), e);
                masterOrderActionService.insertOrderActionBySn(masterOrderSn,
                        "<font style=color:red;>订单取消 通知券卡中心返还红包券卡异常" + e.getMessage() + "</font>", orderStatus.getAdminUser());
            }
        }
    }

    /**
     * 处理订单取消, 余额退还
     * @param master
     */
	private void processOrderCancelSurplus(MasterOrderInfo master) {
	    // 正常订单、部分付款
        if (master.getOrderType().intValue() == Constant.OI_ORDER_TYPE_NORMAL_ORDER
                && master.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED) {
            // 使用余额支付
            if (master.getSurplus().floatValue() > 0) {
                String masterOrderSn = master.getMasterOrderSn();
                String memo = "订单[" + masterOrderSn + "]取消成功后返还余额：" + master.getSurplus();
                ReturnInfo<String> apiBack = shopPayService.backSurplus(master.getUserId(), master.getSurplus().floatValue(), masterOrderSn, memo);
                String msg = "";
                if (apiBack.getIsOk() == Constant.OS_YES) {
                    msg = "余额[" + master.getSurplus() + "]返还成功,流水号[" + apiBack.getData() + "]";
                } else {
                    msg = "余额[" + master.getSurplus() + "]返还失败,原因:" + apiBack.getMessage();
                }
                MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
                orderAction.setActionNote(msg);
                masterOrderActionService.insertOrderActionByObj(orderAction);
            }
        }
    }

    /**
     * 处理退单商品
     * @param masterOrderSn
     * @param useCardList
     * @param returnGoodsList
     */
    private void processOrderReturnGoods(String masterOrderSn, List<String> useCardList, List<CreateOrderReturnGoods> returnGoodsList) {
        MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
        goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
        for (int i = 0; i < goodsList.size(); i++) {
            MasterOrderGoods orderGoods = goodsList.get(i);
            returnGoodsList.add(buildCreateOrderReturnGoods(orderGoods));
            getOrderGoodsUseCard(orderGoods, useCardList);
        }
    }

	/**
	 * 获取订单使用的卡券信息
	 * @param masterOrderSn
	 * @return List<String>
	 */
	private List<String> getOrderGoodsCards(String masterOrderSn) {
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
		List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);

		List<String> useCards = new ArrayList<String>();
		for (int i = 0; i < goodsList.size(); i++) {
			MasterOrderGoods orderGoods = goodsList.get(i);
            getOrderGoodsUseCard(orderGoods, useCards);
		}

		return useCards;
	}

    /**
     * 获取订单商品使用卡券信息
     * @param orderGoods
     * @param useCards
     */
	private void getOrderGoodsUseCard(MasterOrderGoods orderGoods, List<String> useCards) {
        if (StringUtil.isNotEmpty(orderGoods.getUseCard())) {
            String[] arr = orderGoods.getUseCard().split(Constant.STRING_SPLIT_COLON);
            for (int j = 0;j < arr.length;j++) {
                useCards.add(arr[j]);
            }
        }
    }

	private void processOrderReturnMoney() {
        // 担保交易,不需要下发退款
        /*Byte transType = master.getTransType();
        if (transType != null && transType == 3) {
            // 判断是否联采订单
            // 退款单创建成功后通知下发
            Map<String, Object> orderCancelMap = new HashMap<String, Object>();
            orderCancelMap.put("orderSn", master.getMasterOrderSn());
            orderCancelMap.put("orderOutSn", master.getOrderOutSn());
            orderCancelMap.put("channelCode", master.getChannelCode());
            orderCancelMap.put("shopCode", master.getOrderFrom());
            jmsSendQueueService.sendQueueMessage(MqConfig.cloud_channel_orderCancel + "-" + master.getChannelCode(), JSON.toJSONString(orderCancelMap));
        } else {
            OrderReturnMoneyBean orderReturnMoneyBean = new OrderReturnMoneyBean();
            orderReturnMoneyBean.setSiteCode(master.getChannelCode());
            orderReturnMoneyBean.setOrderSn(master.getMasterOrderSn());
            orderReturnMoneyBean.setUserId(master.getUserId());
            orderReturnMoneyBean.setReturnPaySn(ri.getReturnSn());
            orderReturnMoneyBean.setReturnOrderAmount(returnMoney + "");
            doOrderReturnMoney(orderReturnMoneyBean);
        }*/
    }

	private void processPoint() {
        /*String userId = master.getUserId();
        if (master.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_UNPAYED
                || master.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_PARTPAYED) {
            if (master.getChannelCode().equals(Constant.KELTI)
                    && master.getPoints().doubleValue() > 0) {
                Double points = master.getPoints().doubleValue();
                String pointsNote = null;
                try {
                    OptionPointsBean pointsBean = new OptionPointsBean(userId, master.getTotalFee().doubleValue(),
                            masterOrderSn, points, master.getBvValue(), 1);
                    ReturnInfo<Integer> returnInfo = userPointsService.optionPoints(pointsBean);
                    if(returnInfo.getIsOk() == Constant.OS_YES){
                        logger.debug("订单取消-点数("+ points + ")冻结成功！orderSn:" + masterOrderSn +",userId:"+userId);
                        pointsNote = "订单取消-点数(" + points + ")冻结成功！";
                        masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                        pointsBean.setOrderType(4);
                        ReturnInfo<Integer> returnPointsInfo = userPointsService.optionPoints(pointsBean);
                        if(returnPointsInfo.getIsOk() == Constant.OS_YES){
                            logger.debug("订单取消-点数("+ points + ")退返成功！orderSn:" + masterOrderSn +",userId:"+userId);
                            pointsNote = "订单取消-点数("+ points + ")退返成功！";
                            masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                        } else {
                            logger.error("订单取消-点数(" + points + ")退返失败！orderSn:"+ masterOrderSn +",userId:" + userId);
                            pointsNote = "订单取消-点数(" + points + ")退返失败！错误信息:"+ returnPointsInfo.getMessage();
                            masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                            // 释放冻结点数
                            // 记录日志
                            pointsBean.setOrderType(2);
                            ReturnInfo<Integer> realesePointsInfo = userPointsService.optionPoints(pointsBean);
                            if(realesePointsInfo.getIsOk() == Constant.OS_YES){
                                logger.debug("订单取消-点数("+ points + ")释放成功！orderSn:" + masterOrderSn +",userId:"+userId);
                                pointsNote = "订单取消-点数("+ points + ")释放成功！";
                                masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                            } else {
                                logger.error("订单取消-点数(" + points + ")释放失败！orderSn:"+ masterOrderSn +",userId:" + userId);
                                pointsNote = "订单取消-点数(" + points + ")释放失败！错误信息:"+realesePointsInfo.getMessage();
                                masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                            }
                        }
                    } else {
                        logger.error("订单取消-点数(" + points + ")冻结失败！orderSn:"+ masterOrderSn +",userId:" + userId);
                        pointsNote = "订单取消-点数(" + points + ")冻结失败！返回信息:"+ returnInfo.getMessage();
                        masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
                    }
                } catch (Exception e) {
                    logger.error(masterOrderSn + "用户ID：" + userId + "订单取消-退还点数("+points+")失败" + e.getMessage(), e);
                    pointsNote = "订单取消-点数(" + points + ")退还失败！错误信息:" + e.getMessage();
                }
            }
        }*/
    }

	/**
	 * 检查订单是否可以进行取消操作
	 * 返回null表示此订单可以执行取消操作，否则表示不能执行
	 * @param master
	 * @param orderSn
     * @param sourceType
	 * @return
	 */
	private ReturnInfo checkMasterConditionOfExecution(MasterOrderInfo master, String orderSn, String sourceType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);

		if (master == null) {
			ri.setMessage("在近三个月的记录中，" + orderSn + "的订单信息没有取得!");
			return ri;
		}
		int os = master.getOrderStatus();
		if (os == Constant.OI_ORDER_STATUS_CANCLED || os == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + orderSn + "已处于完结状态，不能在进行取消操作！");
			return ri;
		}
		if (master.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage("订单" + orderSn + "的货物已发，不能在进行取消操作！");
			return ri;
		}
		ReturnInfo<List<OrderReturn>> returnInfo = this.orderReturnService.getEffectiveReturns(master.getMasterOrderSn());
		if (returnInfo != null && returnInfo.getIsOk() == Constant.OS_YES && StringUtil.isListNotNull(returnInfo.getData())) {
			ri.setMessage("订单" + orderSn + "已创建退单，不能在进行取消操作！");
			return ri;
		}
		// POS 端订单
		if (!ConstantValues.METHOD_SOURCE_TYPE.POS.equals(sourceType) && !ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(sourceType)) {
			if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(sourceType)) {
				if (master.getPayStatus() != Constant.OI_PAY_STATUS_UNPAYED) {
					ri.setMessage("订单" + orderSn + "的订单已付款不能取消操作！");
					return ri;
				}
			} else {
				// 同时处于已确定已付款
				/*if ((os == Constant.OI_ORDER_STATUS_CONFIRMED 
						&& master.getPayStatus() == Constant.OI_PAY_STATUS_PAYED)) {
					ri.setMessage("订单" + orderSn + "同时处于已确定已付款，不能在进行取消操作！");
					return ri;
				}*/
			}
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

	/**
	 * 检查订单是否可以进行取消操作
	 * 返回null表示此订单可以执行取消操作，否则表示不能执行
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ReturnInfo checkConditionOfExecution(OrderDistribute orderInfo,
			String orderSn, String sourceType, MasterOrderInfo master) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("在订单记录中，" + orderSn + "的订单信息没有取得!");
			return ri;
		}
		int os = orderInfo.getOrderStatus();
		if (os == Constant.OI_ORDER_STATUS_CANCLED || os == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + orderSn + "已处于完结状态，不能在进行取消操作！");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage("订单" + orderSn + "的货物已发，不能在进行取消操作！");
			return ri;
		}
		// POS|ERP 端订单取消 
		if (!ConstantValues.METHOD_SOURCE_TYPE.POS.equals(sourceType)&&!ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(sourceType)) {
			if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(sourceType)) {
				if (master.getPayStatus() != Constant.OI_PAY_STATUS_UNPAYED) {
					ri.setMessage("订单" + orderSn + "的订单已付款不能取消操作！");
					return ri;
				}
			} else {
				// 同时处于已确定已付款
				/*if ((os == Constant.OI_ORDER_STATUS_CONFIRMED 
						&& master.getPayStatus() == Constant.OI_PAY_STATUS_PAYED)) {
					ri.setMessage("订单" + orderSn + "同时处于已确定已付款，不能在进行取消操作！");
					return ri;
				}*/
			}
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}


    /**
     * 检查订单是否可以进行取消操作
     * @param distributes
     * @param sourceType
     * @param master
     * @return ReturnInfo
     */
	private ReturnInfo<List<OrderDistribute>> checkConditionOfExecution(List<OrderDistribute> distributes, String sourceType, MasterOrderInfo master) {
		ReturnInfo<List<OrderDistribute>> ri = new ReturnInfo<List<OrderDistribute>>(Constant.OS_NO);
		List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			/* 参数检验 */
			int os = distribute.getOrderStatus();
			if (os == Constant.OI_ORDER_STATUS_CANCLED || os == Constant.OI_ORDER_STATUS_FINISHED) {
				ri.setMessage("订单" + orderSn + "已处于完结状态，不能在进行取消操作！");
				continue;
			}
			// 备货中和备货完成，订单可以取消，只是统一库存无需返回库存
			if (distribute.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
				ri.setMessage("订单" + orderSn + "的货物已发，不能在进行取消操作！");
				continue;
			}
			// POS 端订单
			if (!ConstantValues.METHOD_SOURCE_TYPE.POS.equals(sourceType) && !ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(sourceType)) {
				if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(sourceType)) {
					if (master.getPayStatus() != Constant.OI_PAY_STATUS_UNPAYED) {
						ri.setMessage("订单" + orderSn + "的订单已付款不能取消操作！");
						continue;
					}
				} /*else {
					// 同时处于已确定已付款
					if ((os == Constant.OI_ORDER_STATUS_CONFIRMED 
							&& master.getPayStatus() == Constant.OI_PAY_STATUS_PAYED)) {
						ri.setMessage("订单" + orderSn + "同时处于已确定已付款，不能在进行取消操作！");
						continue;
					}
				}*/
			}
			/* 有关联订单时  如果是换货单并且存在换货退货单*/
			/*if (StringUtil.isNotEmpty(distribute.getRelatingReturnSn())
					&& distribute.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
				OrderReturnExample example = new OrderReturnExample();
				example.or().andReturnSnEqualTo(distribute.getRelatingReturnSn());
				List<OrderReturn> returns = orderReturnMapper.selectByExample(example);
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(distribute.getRelatingReturnSn());
				if (StringUtil.isListNull(returns) || orderReturnShip == null) {
					ri.setMessage("订单[" + orderSn + "]关联的退单信息缺失！");
					return ri;
				}
				OrderReturn orderReturn = returns.get(0);
				if (orderReturn.getReturnType() == Constant.OR_RETURN_TYPE_RETURN
						&& orderReturnShip.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()) {
					ri.setMessage("订单[" + orderSn + "]的退货单货物暂未入库，不能取消操作！请返回！");
					return ri;
				}
			}*/
			updateDistributes.add(distribute);
		}
		ri.setData(updateDistributes);
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

    /**
     * 组装订单退单商品信息
     * @param orderGoods
     * @return CreateOrderReturnGoods
     */
	private CreateOrderReturnGoods buildCreateOrderReturnGoods(MasterOrderGoods orderGoods) {
		CreateOrderReturnGoods returnGoods = new CreateOrderReturnGoods();
		returnGoods.setMasterOrderSn(orderGoods.getMasterOrderSn());
		returnGoods.setOrderSn(orderGoods.getOrderSn());
		returnGoods.setCustomCode(orderGoods.getCustomCode());
		returnGoods.setGoodsReturnNumber(orderGoods.getGoodsNumber());
		returnGoods.setGoodsBuyNumber(orderGoods.getGoodsNumber());
		returnGoods.setExtensionCode(orderGoods.getExtensionCode());
		returnGoods.setExtensionId(orderGoods.getExtensionId());
		returnGoods.setOsDepotCode(orderGoods.getDepotCode());
		returnGoods.setSettlePrice(orderGoods.getSettlementPrice().doubleValue());
		returnGoods.setMarketPrice(orderGoods.getMarketPrice().doubleValue());
		returnGoods.setGoodsPrice(orderGoods.getTransactionPrice().doubleValue());
		returnGoods.setShareBonus(orderGoods.getShareBonus().doubleValue());
		returnGoods.setSeller(orderGoods.getSupplierCode());
		returnGoods.setSalesMode(orderGoods.getSalesMode().intValue());
		returnGoods.setGoodsName(orderGoods.getGoodsName());
		returnGoods.setDiscount(Double.valueOf(orderGoods.getDiscount().toString()));
		returnGoods.setSap(orderGoods.getSap());
		returnGoods.setColorName(orderGoods.getGoodsColorName());
		returnGoods.setSizeName(orderGoods.getGoodsSizeName());
		returnGoods.setGoodsThumb(orderGoods.getGoodsThumb());
		returnGoods.setBvValue(StringUtil.isTrimEmpty(orderGoods.getBvValue()) ? 0 : Integer.valueOf(orderGoods.getBvValue()));
		returnGoods.setBaseBvValue(orderGoods.getBaseBvValue());
		returnGoods.setGoodsSn(orderGoods.getGoodsSn());
		returnGoods.setPayPoints(orderGoods.getSettlementPrice().doubleValue());
		returnGoods.setIntegralMoney(orderGoods.getIntegralMoney().doubleValue());
        // 箱规
		returnGoods.setBoxGauge(orderGoods.getBoxGauge() == null ? null : orderGoods.getBoxGauge().intValue());
		//成本价
        returnGoods.setCostPrice(orderGoods.getCostPrice().doubleValue());
		return returnGoods;
	}
	
	private List<String> getOrderSns(List<OrderDistribute> distributes) {
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		return orderSns;
	}
	
	private MasterOrderInfoBean builtMasterBean(MasterOrderInfo master, double shippingFee, double newTotalPayable) {
		MasterOrderInfoBean newMaster = new MasterOrderInfoBean();
		double shipDiff = shippingFee - master.getShippingTotalFee().doubleValue();
		newMaster.setTotalPayable(new BigDecimal(newTotalPayable));
		newMaster.setAfterChangePayable(new BigDecimal(newTotalPayable));
		newMaster.setShippingTotalFee(new BigDecimal(shippingFee));
		newMaster.setTotalFee(BigDecimal.valueOf(master.getTotalFee().doubleValue() + shipDiff));
		newMaster.setGoodsAmount(master.getGoodsAmount());
		newMaster.setGoodsCount(master.getGoodsCount());
		newMaster.setDiscount(master.getDiscount());
		newMaster.setBonus(master.getBonus());
		newMaster.setIntegralMoney(master.getIntegralMoney());
		newMaster.setPayStatus(master.getPayStatus());
		return newMaster;
	}
	
	private MasterOrderInfo editMasterOrderInfo(MasterOrderInfo master, StringBuffer msg, MasterOrderInfoBean newMaster, String orderSn) {
		MasterOrderInfo newOrderInfo = new MasterOrderInfo();
		boolean flag = false;
		StringBuffer message = new StringBuffer();
		message.append("取消交货单[" + orderSn + "]信息：");
		// 商品总金额
		if (master.getGoodsAmount().compareTo(newMaster.getGoodsAmount().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newGoodsAmount = newMaster.getGoodsAmount().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setGoodsAmount(newGoodsAmount);
			message.append("</br>商品总金额：" + master.getGoodsAmount() + " → " + newGoodsAmount);
			master.setGoodsAmount(newGoodsAmount);
		}
		// 订单配送费用
		if (master.getShippingTotalFee().compareTo(newMaster.getShippingTotalFee().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal shippingTotalFee = newMaster.getShippingTotalFee().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setShippingTotalFee(shippingTotalFee);
			message.append("</br>配送费用：" + master.getShippingTotalFee() + " → " + shippingTotalFee);
			master.setGoodsAmount(shippingTotalFee);
		}
		// 订单总金额
		BigDecimal totalFee = newMaster.getGoodsAmount().subtract(newMaster.getDiscount()).add(newMaster.getShippingTotalFee());
		if (master.getTotalFee().compareTo(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)) != 0 ) {
			flag = true;
			newOrderInfo.setTotalFee(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)); // 订单总金额
			message.append("</br>订单总金额：" + master.getTotalFee() + " → " + newOrderInfo.getTotalFee());
			master.setTotalFee(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)); // 订单总金额
		}
		// 应付款总金额
		if (master.getTotalPayable().compareTo(newMaster.getAfterChangePayable().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newTotalPayable = newMaster.getAfterChangePayable().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setTotalPayable(newTotalPayable); // 应付款总金额
			message.append("</br>应付款总金额：" + master.getTotalPayable() + " → " + newTotalPayable);
			master.setTotalPayable(newTotalPayable); // 应付款总金额
		}
		// 折让金额
		if (master.getDiscount().compareTo(newMaster.getDiscount().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newDiscountAmount = newMaster.getDiscount().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setDiscount(newDiscountAmount);
			message.append("</br>折让金额：" + master.getDiscount() + " → " + newDiscountAmount);
			master.setDiscount(newDiscountAmount);
		}
		// 商品总数量
		if (!master.getGoodsCount().equals(newMaster.getGoodsCount())) {
			flag = true;
			newOrderInfo.setGoodsCount(newMaster.getGoodsCount());
			message.append("</br>商品总数量：" + master.getGoodsCount() + " → " + newMaster.getGoodsCount());
			master.setGoodsCount(newMaster.getGoodsCount());
		}
		// 红包总金额
		if (master.getBonus().compareTo(newMaster.getBonus()) != 0) {
			flag = true;
			BigDecimal newOrderUseBonus = newMaster.getBonus().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setBonus(newOrderUseBonus);
			message.append("</br>红包总金额：" + master.getBonus() + " → " + newOrderUseBonus);
			master.setBonus(newOrderUseBonus);
		}
		// 积分总金额
		if (master.getIntegralMoney().compareTo(newMaster.getIntegralMoney()) != 0) {
			flag = true;
			BigDecimal newIntegralMoney = newMaster.getIntegralMoney().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderInfo.setIntegralMoney(newIntegralMoney);
			message.append("</br>积分总金额：" + master.getIntegralMoney() + " → " + newIntegralMoney);
			master.setBonus(newIntegralMoney);
		}
		message.append("</br>");
		if (flag) {
			// orderInfo整理 订单状态 订单金额相关
			newOrderInfo.setMasterOrderSn(master.getMasterOrderSn());
			newOrderInfo.setPayStatus(newMaster.getPayStatus());
//			newOrderInfo.setPayTotalFee(new BigDecimal(newMaster.getAfterChangePayable())); // 支付总费用
			newOrderInfo.setUpdateTime(new Date());
			msg.append(message.toString());
		} else {
			newOrderInfo = null;
		}
		return newOrderInfo;
	}

	/**
	 * 订单退款操作
	 * @param orderReturnBean
	 */
	@Override
	public void doOrderReturnMoneyByCommon(OrderReturnBean orderReturnBean) {
        // 退单编码
	    String returnSn = orderReturnBean.getReturnSn();

        OrderRefundExample orderRefundExample = new OrderRefundExample();
        orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
        List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
        if (CollectionUtils.isEmpty(orderRefundList)) {
            logger.info("通过退单编码:" + returnSn + "无法获取有效地退款单数据" + "," + JSONObject.toJSONString(orderReturnBean));
            return;
        }

        OrderRefund orderRefund = orderRefundList.get(0);
        // 退款状态：0未退，1已退
        Byte backBalance = orderRefund.getBackbalance();
        // 退款金额
        orderReturnBean.setReturnMoney(orderRefund.getReturnFee());

        // 未退
        if (backBalance == null || backBalance.intValue() == 0) {
            jmsSendQueueService.sendQueueMessage(MqConfig.CLOUD_ORDER_ACCOUNT_RETURN, JSON.toJSONString(orderReturnBean));
        } else {
            logger.info("通过退单编码:" + returnSn + "退款已退无须发起退款," + JSONObject.toJSONString(orderReturnBean));
        }

	}
	
	/**
	 * 订单退款
	 * @param orderReturnMoneyBean
	 */
	@Override
	public void doOrderReturnMoney(OrderReturnMoneyBean orderReturnMoneyBean) {
		
		// 退单编码
		String returnSn = orderReturnMoneyBean.getReturnPaySn();
		
		OrderRefundExample orderRefundExample = new OrderRefundExample();
		orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
		List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
		if (CollectionUtils.isEmpty(orderRefundList)) {
			logger.info("通过退单编码:" + returnSn + "无法获取有效地退款单数据" + "," + JSONObject.toJSONString(orderReturnMoneyBean));
			return;
		}
		
		OrderRefund orderRefund = orderRefundList.get(0);
		// 退款状态：0未退，1已退
		Byte backBalance = orderRefund.getBackbalance();
		
		// 未退
		if (backBalance == null || backBalance.intValue() == 0) {
			jmsSendQueueService.sendQueueMessage(MqConfig.cloud_center_orderRefund, JSON.toJSONString(orderReturnMoneyBean));
		} else {
			logger.info("通过退单编码:" + returnSn + "退款已退无须发起退款," + JSONObject.toJSONString(orderReturnMoneyBean));
		}
	}
}
