package com.work.shop.oms.orderop.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.exception.OrderException;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderAddressInfoExample;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderInfoExtendExample;
import com.work.shop.oms.bean.MasterOrderInfoHistory;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderDistributeHistory;
import com.work.shop.oms.bean.OrderDistributeHistoryExample;
import com.work.shop.oms.bean.SystemMsgTemplate;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoDetailMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoHistoryMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDistributeHistoryMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemMsgTemplateMapper;
import com.work.shop.oms.dao.define.HistoryOrderToOrderMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.dao.define.OrderToHistoryMapper;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.SystemOrderSnService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.OrderStatusUtils;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.SettleParamObj;
import com.work.shop.sms.bean.Message;
import com.work.shop.sms.bean.State;
import com.work.shop.sms.bean.User;
import com.work.shop.sms.send.api.SMSService;

/**
 * 订单交货单操作服务接口
 * @author QuYachu
 */
@Service("orderDistributeOpService")
public class OrderDistributeOpServiceImpl implements OrderDistributeOpService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	MasterOrderInfoHistoryMapper masterOrderInfoHistoryMapper;
	@Resource
	OrderDistributeMapper orderDistributeMapper;
	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	private SystemOrderSnService systemOrderSnService;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;

	@Resource(name = "orderDistributeService")
	private OrderDistributeService orderDistributeService;

	@Resource
	private DistributeShipService distributeShipService;
	@Resource(name="orderSettleServiceImpl")
	private OrderSettleService orderSettleService;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource(name="distributeSwdiJmsTemplate")
	private JmsTemplate distributeSwdiJmsTemplate;
	@Resource
	HistoryOrderToOrderMapper historyOrderToOrderMapper;
	@Resource
	OrderToHistoryMapper orderToHistoryMapper;
	@Resource(name = "orderDistributeProducerJmsTemplate")
	private JmsTemplate orderDistributeJmsTemplate;
	@Resource
	private OrderDistributeHistoryMapper orderDistributeHistoryMapper;
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	//@Resource
	private SMSService sMSService;
	@Resource
	private SystemMsgTemplateMapper systemMsgTemplateMapper;
	@Resource
	private MasterOrderInfoDetailMapper masterOrderInfoDetailMapper;
	@Resource
	private OrderDistributeOpService orderDistributeOpService;

	/**
	 * 订单锁定
	 * @param masterOrderSn 订单编码
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return ReturnInfo
	 * @throws OrderException
	 */
	@Override
	public ReturnInfo lockOrder(String masterOrderSn, OrderStatus orderStatus) throws OrderException {
		logger.debug("订单锁定 : masterOrderSn=" + masterOrderSn + "; orderStatus=" + orderStatus);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null  == masterOrderSn || masterOrderSn.trim().isEmpty()) {
			logger.error("传入的订单编号参数为空！");
			ri.setMessage("传入的订单编号参数为空！");
			return ri;
		}
		if (orderStatus == null) {
			logger.error(masterOrderSn + "传入的参数[orderStatus]为空！");
			ri.setMessage(masterOrderSn + "传入的参数[orderStatus]为空！");
			return ri;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn.trim());
		// 检查订单信息订单状态
		try {
			MasterOrderInfo newOrderInfo = new MasterOrderInfo();
			newOrderInfo.setMasterOrderSn(masterOrderSn);
			if (orderStatus.getUserId() != 0) {
				newOrderInfo.setLockStatus(orderStatus.getUserId());
			} else {
				// 为锁定
				newOrderInfo.setLockStatus(Constant.OI_LOCK_STATUS_LOCKED);
			}
			newOrderInfo.setLockTime(new Date());
			newOrderInfo.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(newOrderInfo);
			MasterOrderAction action = masterOrderActionService.createOrderAction(master);
			action.setActionUser(orderStatus.getAdminUser());
			action.setActionNote(orderStatus.getMessage());
			masterOrderActionService.insertOrderActionByObj(action);
			ri.setMessage("锁定操作成功：订单" + masterOrderSn + "锁定成功！");
			ri.setIsOk(Constant.OS_YES);
			return ri;
		} catch (Exception e) {
			logger.error("订单锁定操作失败！" , e);
			ri.setMessage("订单锁定失败！ 订单：" + masterOrderSn + "发生异常！" + e.getMessage());
		}
		return ri;
	}

	/**
	 * 订单解锁
	 * @param masterOrderSn 订单编码
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return ReturnInfo
	 * @throws OrderException
	 */
	@Override
	public ReturnInfo unLockOrder(String masterOrderSn, OrderStatus orderStatus) throws OrderException {
		logger.debug("订单解锁 : masterOrderSn=" + masterOrderSn + "; orderStatus=" + orderStatus);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("传入的订单编号参数为空！");
			ri.setMessage("传入的订单编号参数为空！");
			return ri;
		}
		if (orderStatus == null) {
			logger.error(masterOrderSn + "传入的参数[orderStatus]为空！");
			ri.setMessage(masterOrderSn + "传入的参数[orderStatus]为空！");
			return ri;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn.trim());
		// 检查订单信息订单状态
		if (master.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		// 判断是否被非admin的当前人锁定
		if (orderStatus.getAdminUser() != null && !orderStatus.getAdminUser().trim().equals("admin")
				&& !orderStatus.getAdminUser().trim().equals(Constant.OS_STRING_SYSTEM)) {

		}
		try {
			MasterOrderInfo newOrderInfo = new MasterOrderInfo();
			newOrderInfo.setMasterOrderSn(masterOrderSn);
			// 未锁定
			newOrderInfo.setLockStatus(Constant.OI_LOCK_STATUS_UNLOCKED);
			newOrderInfo.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(newOrderInfo);
			MasterOrderAction action = masterOrderActionService.createOrderAction(master);
			action.setActionUser(orderStatus.getAdminUser());
			action.setActionNote(orderStatus.getMessage());
			masterOrderActionService.insertOrderActionByObj(action);
			ri.setMessage("解锁操作成功：订单" + masterOrderSn + "解锁成功！");
			ri.setIsOk(Constant.OS_YES);
			return ri;
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单解锁操作失败！" + e.getMessage(), e);
			ri.setMessage("订单解锁失败！ 订单：" + masterOrderSn + "发生异常！" + e.getMessage());
		}
		return ri;
	}

	@Override
	public ReturnInfo noticeReceivables(String masterOrderSn, OrderStatus orderStatus)
			throws Exception {
		logger.debug("通知收款noticeStCh service begin: masterOrderSn=" + masterOrderSn
				+ ";orderStatus=" + orderStatus);
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		//执行条件判断
		if(StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]订单编号不能为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("订单[" + masterOrderSn + "]传入的参数[orderStatus]为空！");
			info.setMessage("订单[" + masterOrderSn + "]传入的参数[orderStatus]为空！");
			return info;
		}
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn.trim());
			// 检查订单信息订单状态
			ReturnInfo tRi = OrderStatusUtils.notice(master, masterOrderSn, orderStatus.getAdminUser(), orderStatus.getUserId());
			if (tRi != null && tRi.getIsOk() == Constant.OS_NO) {
				logger.error(tRi.getMessage());
				return tRi;
			}
			//更新订单信息
			MasterOrderInfo newOrderInfo = new MasterOrderInfo();
			newOrderInfo.setMasterOrderSn(masterOrderSn);
			newOrderInfo.setNoticeStatus(Constant.OI_NOTICE_STATUS_NOTICED);
			newOrderInfo.setNoticeTime(new Date());
			newOrderInfo.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(newOrderInfo);
			MasterOrderAction action = masterOrderActionService.createOrderAction(master);
			action.setActionUser(orderStatus.getAdminUser());
			action.setActionNote(orderStatus.getMessage());
			masterOrderActionService.insertOrderActionByObj(action);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("订单通知收款操作成功！");
		} catch(Exception e) {
			// TODO 异常日志
			logger.error("订单[" + masterOrderSn + "]通知收款失败！ 异常:" + e.getMessage(), e);
			info.setMessage("订单[" + masterOrderSn + "]通知收款失败！ 异常:" + e.getMessage());
		} finally {
			logger.debug("noticeStCh service end!");
		}
		return info;
	}

	@Override
	public ReturnInfo reviveOrder(String masterOrderSn, OrderStatus orderStatus)
			throws Exception {
		logger.debug("订单复活 begin: masterOrderSn=" + masterOrderSn + ";OrderStatus="+orderStatus);
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		info.setOrderSn(masterOrderSn);
		//执行条件判断
		if(StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("订单编号不能为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error(masterOrderSn + "传入的参数[orderStatus]为空！");
			info.setMessage(masterOrderSn + "传入的参数[orderStatus]为空！");
			return info;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn.trim());
		try {
			// 检查订单信息订单状态
			ReturnInfo tRi = OrderStatusUtils.relive(master, masterOrderSn, orderStatus.getAdminUser(), orderStatus.getUserId());
			if (tRi != null && tRi.getIsOk() == Constant.OS_NO) {
				logger.error(tRi.getMessage());
				return tRi;
			}
//			OrderShip orderShip = null;
			MasterOrderPay orderPay = null;
			MasterOrderInfo newOrderInfo = new MasterOrderInfo();
			newOrderInfo.setLockStatus(Constant.OI_LOCK_STATUS_UNLOCKED);
			newOrderInfo.setMasterOrderSn(masterOrderSn);
			newOrderInfo.setShipStatus(master.getShipStatus());
			newOrderInfo.setOrderStatus(master.getOrderStatus());
			newOrderInfo.setPayStatus(master.getPayStatus());
			if (master.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED) {
				// 订单总状态已完成 回退为 已确认
				newOrderInfo.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CONFIRMED);
			} else if (master.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
				// 支付总状态 将已结算 回退为  已付款
				newOrderInfo.setPayStatus(Constant.OI_PAY_STATUS_PAYED);
				orderPay = new MasterOrderPay();
				orderPay.setPayStatus(Constant.OI_PAY_STATUS_PAYED);
			} else if (master.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| master.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				// 配送总状态 将已发货.客户已收货 回退为未发货
				newOrderInfo.setShipStatus((byte) Constant.OI_SHIP_STATUS_UNSHIPPED);
//				orderShip = new OrderShip();
//				orderShip.setShippingStatus((byte) Constant.OI_SHIP_STATUS_UNSHIPPED);
			} else if (master.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
				// 订单总状态 取消,关闭 回退为 已确认
				newOrderInfo.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CONFIRMED);
			}
			// 更新订单信息
			masterOrderInfoMapper.updateByPrimaryKeySelective(newOrderInfo);
			// 更新支付单信息
			if (orderPay != null) {
				MasterOrderPayExample payExample = new MasterOrderPayExample();
				payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
				masterOrderPayMapper.updateByExampleSelective(orderPay, payExample);
			}
			// 更新发货单信息
//			if (orderShip != null) {
//				OrderShipExample shipExample = new OrderShipExample();
//				shipExample.or().andOrderSnEqualTo(orderSn);
//				orderShipMapper.updateByExampleSelective(orderShip, shipExample);
//			}
			MasterOrderInfoExtend orderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (orderInfoExtend == null) {
				MasterOrderInfoExtend insertExtend = new MasterOrderInfoExtend();
				insertExtend.setMasterOrderSn(masterOrderSn);
				insertExtend.setAgdist((byte) 0);
				insertExtend.setReviveStt((byte) 1);
				masterOrderInfoExtendMapper.insertSelective(insertExtend);
			} else {
				MasterOrderInfoExtend updateExtend = new MasterOrderInfoExtend();
				updateExtend.setReviveStt((byte) 1);
				MasterOrderInfoExtendExample example=new MasterOrderInfoExtendExample();
				example.or().andMasterOrderSnEqualTo(masterOrderSn);
				masterOrderInfoExtendMapper.updateByExampleSelective(updateExtend, example);
			}
			//订单记录日志
			MasterOrderAction action = masterOrderActionService.createOrderAction(master);
			action.setActionUser(orderStatus.getAdminUser());
			action.setActionNote(orderStatus.getMessage());
			action.setOrderStatus(newOrderInfo.getOrderStatus());
			action.setShippingStatus(newOrderInfo.getShipStatus());
			action.setPayStatus(newOrderInfo.getPayStatus());
			masterOrderActionService.insertOrderActionByObj(action);
			logger.debug("订单复活reviveOrder 成功！");
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("订单" + masterOrderSn + "订单复活失败:", e);
			info.setMessage("订单复活reviveOrder 异常:"+e.getMessage());
			// 记录操作日志异常信息
			MasterOrderAction action = masterOrderActionService.createOrderAction(master);
			action.setActionUser(orderStatus.getAdminUser());
			action.setActionNote("复活: <font style=color:red;>"+ e.getMessage() + "</font>");
			action.setOrderStatus(master.getOrderStatus());
			action.setShippingStatus(master.getShipStatus());
			action.setPayStatus(master.getPayStatus());
			masterOrderActionService.insertOrderActionByObj(action);
		}
		return info;
	}

	@SuppressWarnings("rawtypes")
	public ReturnInfo settleOrder(String masterOrderSn, OrderStatus orderStatus) throws Exception {
		logger.debug("订单结算：masterOrderSn=" + masterOrderSn + ";orderStatus=" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		info.setMessage("订单结算失败");
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			info.setMessage("[orderStatus]传入参数为空，不能进行订单结算操作！");
			return info;
		}
		MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if(orderInfo == null) {
			info.setMessage("订单[" + masterOrderSn + "]不存在，无法结算");
			return info;
		}
		//订单验证
		// 检查订单信息订单状态
		if (orderInfo.getPayStatus().intValue() == Constant.OI_PAY_STATUS_SETTLED) {
			info.setMessage("订单[" + masterOrderSn + "]已经是已结算状态");
			return info;
		}
		if (orderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			info.setMessage("订单[" + masterOrderSn + "]要处于已确定状态");
			return info;
		}
		//结算条件-订单状态验证
		if(orderInfo.getOrderStatus().intValue() == ConstantValues.ORDER_STATUS.COMPLETE.intValue()){
			info.setMessage("订单[" + masterOrderSn + "]订单已完结不可再结算");
			return info;
		}
		if (orderInfo.getShipStatus().intValue() < Constant.OI_SHIP_STATUS_ALLSHIPED){
			info.setMessage("订单[" + masterOrderSn + "]发货状态必须是已发货.部分收货.客户已收货状态！");
			return info;
		}
		if (orderInfo.getPayStatus().intValue() != Constant.OI_PAY_STATUS_PAYED) {
			info.setMessage("订单[" + masterOrderSn + "]要处于已付款状态");
			return info;
		}
		MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
		orderGoodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
		List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
		if(StringUtil.isListNull(orderGoodsList)){
			info.setMessage("订单[" + masterOrderSn + "]商品信息数据为空！");
			return info;
		}
		//结算数据提前验证
		ReturnInfo checkResult = orderSettleService.checkOrderSettle(masterOrderSn);
		if (checkResult.getIsOk() <= 0) {
			info.setMessage("订单[" + masterOrderSn + "]" + checkResult.getMessage());
			return info;
		}
		SettleParamObj paramObj = new SettleParamObj();
		paramObj.setBussType(1);
		paramObj.setDealCode(masterOrderSn);
		paramObj.setTools(false);
		paramObj.setUserId(orderStatus.getAdminUser());
		info = orderSettleService.MasterOrderSettle(paramObj);
		return info;
	}

	@SuppressWarnings("rawtypes")
	public ReturnInfo allocation(String orderSn, OrderStatus orderStatus) throws Exception {
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public ReturnInfo allocationByMaster(String masterOrderSn, OrderStatus orderStatus) throws Exception {
		return null;
	}

	public ReturnInfo copyOrder(String masterOrderSn, OrderStatus orderStatus) throws Exception {
		logger.debug("复制订单start ：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			ri.setMessage("传入的订单编号参数为空！不能进行未确认操作！");
			return ri;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == master) {
			ri.setMessage("没有获取到订单" + masterOrderSn + "的信息！不能进行未确认操作！");
			return ri;
		}
		// 生成订单号
		ServiceReturnInfo<String> siSn = systemOrderSnService.createMasterOrderSn();
		// 生成订单号失败
		if (!siSn.isIsok()) {
			logger.debug("生成订单号失败");
			ri.setMessage(siSn.getMessage());
			return ri;
		}
		final String newMasterOrderSn = siSn.getResult();
		MasterOrderInfo newOrderInfo = master;
		newOrderInfo.setMasterOrderSn(newMasterOrderSn);
		newOrderInfo.setOrderOutSn("");
		newOrderInfo.setAddTime(new Date());
		newOrderInfo.setConfirmTime(null);
		newOrderInfo.setUpdateTime(null);
		newOrderInfo.setClearTime(null);
		newOrderInfo.setTransType((byte)Constant.OI_TRANS_TYPE_PREPAY);
		newOrderInfo.setOrderType(Constant.OI_ORDER_TYPE_NORMAL_ORDER);
		newOrderInfo.setOrderStatus((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
		newOrderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_UNSHIPPED);
		newOrderInfo.setPayStatus((byte)Constant.OI_PAY_STATUS_UNPAYED);
		newOrderInfo.setRelatingOriginalSn(null);
		newOrderInfo.setRelatingRemoneySn(null);
		newOrderInfo.setRelatingReturnSn(null);
		newOrderInfo.setLockStatus(Constant.OI_LOCK_STATUS_UNLOCKED);
		newOrderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
		newOrderInfo.setNoticeStatus(Constant.OI_NOTICE_STATUS_UNNOTICED);
		newOrderInfo.setSplitStatus(Constant.SPLIT_STATUS_UNSPLITED);
		newOrderInfo.setSplitTime(null);
		newOrderInfo.setIntegralMoney(BigDecimal.valueOf(0d));
		newOrderInfo.setCancelCode("");
		newOrderInfo.setCancelReason("");
		newOrderInfo.setShippingTotalFee(BigDecimal.valueOf(0d));
		newOrderInfo.setInsureTotalFee(BigDecimal.valueOf(0d));
		newOrderInfo.setPayTotalFee(BigDecimal.valueOf(0d));
		newOrderInfo.setTotalPayable(BigDecimal.valueOf(0d));
		newOrderInfo.setGoodsAmount(BigDecimal.valueOf(0d));
		newOrderInfo.setTotalFee(BigDecimal.valueOf(0d));
		newOrderInfo.setMoneyPaid(BigDecimal.valueOf(0d));
		newOrderInfo.setSurplus(BigDecimal.valueOf(0d));
		newOrderInfo.setBonus(BigDecimal.valueOf(0d));
		newOrderInfo.setDiscount(BigDecimal.valueOf(0d));
		newOrderInfo.setGoodsCount(0);
		newOrderInfo.setBonusId(null);
		this.masterOrderInfoMapper.insert(newOrderInfo);
		MasterOrderInfoExtend infoExtend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (infoExtend != null) {
			MasterOrderInfoExtend newInfoExtend = infoExtend;
			newInfoExtend.setMasterOrderSn(newMasterOrderSn);
			newInfoExtend.setReviveStt((byte)0);
			newInfoExtend.setAgdist((byte)0);
			newInfoExtend.setOrderFinished(-1);
			newInfoExtend.setSettleQueue(0);
			newInfoExtend.setBillNo("");
			newInfoExtend.setIsReview(0);
			newInfoExtend.setRulePromotion("");
			this.masterOrderInfoExtendMapper.insertSelective(newInfoExtend);
		}
		// 原订单日志
		masterOrderActionService.insertOrderActionBySn(masterOrderSn, "通过复制订单生成订单：" + newMasterOrderSn, orderStatus.getAdminUser());
		// 复制订单日志
		masterOrderActionService.insertOrderAction(newMasterOrderSn, "通过复制订单生成订单：" + newMasterOrderSn + ",原订单：" + masterOrderSn, newOrderInfo.getPayStatus());
		MasterOrderAddressInfoExample example = new MasterOrderAddressInfoExample();
		example.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderAddressInfo> addressInfos = this.masterOrderAddressInfoMapper.selectByExample(example);
		if (StringUtil.isListNotNull(addressInfos)) {
			MasterOrderAddressInfo newAddressInfo = addressInfos.get(0);
			newAddressInfo.setMasterOrderSn(newMasterOrderSn);
			this.masterOrderAddressInfoMapper.insertSelective(newAddressInfo);
		}
		MasterOrderPayExample payExample = new MasterOrderPayExample();
		payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> orderPays = this.masterOrderPayMapper.selectByExample(payExample);
		if (StringUtil.isListNotNull(orderPays)) {
			MasterOrderPay newOrderPay = orderPays.get(0);
			newOrderPay.setMasterOrderSn(newMasterOrderSn);
			newOrderPay.setPayStatus((byte) Constant.OP_PAY_STATUS_UNPAYED);
			newOrderPay.setPayTotalfee(new BigDecimal(0D));
			newOrderPay.setCreateTime(new Date());
			newOrderPay.setPayTime(null);
			newOrderPay.setUpdateTime(null);
			String paySn = Constant.OP_BEGIN_WITH_FK + newMasterOrderSn.trim() + genCode(1, 2);
			newOrderPay.setMasterPaySn(paySn);
			this.masterOrderPayMapper.insert(newOrderPay);
			masterOrderActionService.insertOrderAction(newMasterOrderSn, "生成付款单：" + paySn, newOrderPay.getPayStatus());
		}
		ri.setIsOk(Constant.OS_YES);
		ri.setMessage("复制订单成功");
		ri.setOrderSn(newMasterOrderSn);
		logger.debug("复制订单 end ：orderSn=" + masterOrderSn);
		return ri;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo sWDI(String masterOrderSn, String orderSn,
			OrderStatus orderStatus) throws Exception {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		return ri;
	}

	@Override
	public void swdiPushMQ(String[] orderSns, String type){
		if (!StringUtil.isArrayNotNull(orderSns)) {
			logger.error("重新获取分仓发货信息放入MQ中:传入参数为空！");
			return ;
		}
		if (StringUtil.isTrimEmpty(type)) {
			type = "0";
		}
		for (String orderSn : orderSns) {
			try {
				if (StringUtil.isTrimEmpty(orderSn)) {
					continue ;
				}
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setMessage("订单SWDI");
				orderStatus.setType(type);
				orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
				if (Constant.order_type_master.equals(type)) {
					orderStatus.setMasterOrderSn(orderSn);
				} else {
					orderStatus.setOrderSn(orderSn);
				}
				distributeSwdiJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatus)));
			} catch (Exception e) {
				logger.error("订单[" + orderSn +"]SWDI 放入MQ队列异常" + e.getMessage(), e);
			}
		}
	}
	
	@Override
	public ReturnInfo judgeMasterOrderStatus(String masterOrderSn) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn] 参数为空");
			return info;
		}
		try {
			// 有效交货单
			List<OrderDistribute> distributes = this.orderDistributeService.selectEffectiveDistributes(masterOrderSn);
			if (StringUtil.isListNull(distributes)) {
				info.setMessage("订单[ " + masterOrderSn + "] 交货单列表为空！");
				return info;
			}
			MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			
			for (OrderDistribute distribute : distributes) {
				
			}
			
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setUpdateTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			if (updateMaster.getShipStatus() >= Constant.OI_SHIP_STATUS_ALLSHIPED) {
				info.setMessage("订单已发货");
				info.setIsOk(Constant.OS_YES);
			} else {
				info.setMessage("订单发货状态未发货.部分发货");
				return info;
			}
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]更新发货单状态" + e.getMessage(), e);
			info.setMessage("订单[" + masterOrderSn + "]更新发货单状态" + e.getMessage());
			return info;
		}
		info.setMessage("判断更新订单发货状态成功");
		info.setIsOk(Constant.OS_YES);
		return info;
	
	}

	/**
	 * @param i
	 * @param size
	 * @return
	 */
	private String genCode(Integer i, Integer size) {
		String num = i.toString();
		StringBuilder code = new StringBuilder("");
		for (int j = 0; j < size - num.length(); j++) {
			code.append("0");
		}
		return code + num;
	}
	
	public static boolean checkOrderShipStatus(OrderDistribute distribute) {
		return distribute == null || distribute.getOrderStatus() == 2 // 取消
				|| distribute.getOrderStatus() == 7 // 完成
				|| distribute.getPayStatus() == 3 // 已结算
				|| distribute.getShipStatus() == 5; // 客户已收货
	}
	
	@Override
	public ReturnInfo moveOrderFromHistoryToRecent(String historyMasterOrderSn,OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		ReturnInfo returnInfo=new ReturnInfo();
		logger.debug("历史订单转为订单开始：historyMasterOrderSn"+historyMasterOrderSn);
		returnInfo.setIsOk(Constant.YESORNO_NO);
		try{
			if(StringUtil.isNull(historyMasterOrderSn)){
				returnInfo.setMessage("订单号不能为空！");
				return returnInfo;
			}
			MasterOrderInfoHistory masterOrderInfoHistory = masterOrderInfoHistoryMapper.selectByPrimaryKey(historyMasterOrderSn);
			if(masterOrderInfoHistory==null){
				returnInfo.setMessage("没有找到该条订单号的信息！");
				return returnInfo;
			}
			//查询主单对应的交货单列表
			OrderDistributeHistoryExample exmaple = new OrderDistributeHistoryExample();
			exmaple.or().andMasterOrderSnEqualTo(historyMasterOrderSn);
			List<OrderDistributeHistory> list = orderDistributeHistoryMapper.selectByExample(exmaple);
			
			historyOrderToOrderMapper.historyMasOrdInfoToMasOrdInfo(historyMasterOrderSn);
			historyOrderToOrderMapper.historyMasOrdAddInfoToMasOrdAddInfo(historyMasterOrderSn);
			historyOrderToOrderMapper.historyMasOrdPayToMasOrdPay(historyMasterOrderSn);
			historyOrderToOrderMapper.historyMasOrdGoodsToMasOrdGoods(historyMasterOrderSn);
			historyOrderToOrderMapper.historyMasOrdActionToMasOrdAction(historyMasterOrderSn);
			historyOrderToOrderMapper.historyOrderDistributeToOrderDistribute(historyMasterOrderSn);
			if(list!=null&&list.size()>0){
				for(OrderDistributeHistory bean : list){
					String orderSn = bean.getOrderSn();
					historyOrderToOrderMapper.historyDistributeActionToDistributeAction(orderSn);
					historyOrderToOrderMapper.historyOrderDepotShipToOrderDepotShip(orderSn);
					historyOrderToOrderMapper.deleteHistoryDistributeAction(orderSn);
					historyOrderToOrderMapper.deleteHistoryOrderDepotShip(orderSn);
				}
			}
			//注意：由于交货单日志表和配送表只关联了交货单号，所以删除的时候最后删交货单历史表
			historyOrderToOrderMapper.deleteHistoryMasOrdInfo(historyMasterOrderSn);
			historyOrderToOrderMapper.deleteHistoryMasOrdAddInfo(historyMasterOrderSn);
			historyOrderToOrderMapper.deleteHistoryMasOrdPay(historyMasterOrderSn);
			historyOrderToOrderMapper.deleteHistoryMasOrdGoods(historyMasterOrderSn);
			historyOrderToOrderMapper.deleteHistoryMasOrdAction(historyMasterOrderSn);
			historyOrderToOrderMapper.deleteHistoryOrderDistribute(historyMasterOrderSn);
			
			//插入主单日志
			try{
				masterOrderActionService.insertOrderActionBySn(historyMasterOrderSn, orderStatus.getMessage(), orderStatus.getAdminUser());
			}catch(Exception ee){
				ee.printStackTrace();
			}
			
			returnInfo.setIsOk(Constant.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		}catch(Exception e){
			returnInfo.setMessage("将历史订单转为订单失败！异常="+e.toString());
			logger.error("历史订单转为订单失败！historyMasterOrderSn"+historyMasterOrderSn+"   异常",e);
			return returnInfo;
		}
		logger.debug("历史订单转为订单结束：historyMasterOrderSn"+historyMasterOrderSn+"成功！");
		return returnInfo;
	}

	@Override
	public ReturnInfo moveOrderFromRecentToHistory(String masterOrderSn) {
		// TODO Auto-generated method stub
		ReturnInfo returnInfo=new ReturnInfo();
		logger.debug("订单转为历史开始：MasterOrderSn"+masterOrderSn);
		returnInfo.setIsOk(Constant.YESORNO_NO);
		try {
			if(StringUtil.isNull(masterOrderSn)){
				returnInfo.setMessage("订单号不能为空！");
				return returnInfo;
			}
			
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(masterOrderInfo == null){
				returnInfo.setMessage("没有找到该条订单号的信息！");
				return returnInfo;
			}
			
			//查询主单对应的交货单列表
			OrderDistributeExample exmaple = new OrderDistributeExample();
			exmaple.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<OrderDistribute> list = orderDistributeMapper.selectByExample(exmaple);
			
			orderToHistoryMapper.masOrdInfoToHistory(masterOrderSn);
			orderToHistoryMapper.masOrdAddInfoToHistory(masterOrderSn);
			orderToHistoryMapper.masOrdPayToHistory(masterOrderSn);
			orderToHistoryMapper.masOrdGoodsToHistory(masterOrderSn);
			orderToHistoryMapper.masOrdActionToHistory(masterOrderSn);
			orderToHistoryMapper.orderDistributeToHistory(masterOrderSn);
			if(list!=null&&list.size()>0){
				for(OrderDistribute bean : list){
					String orderSn = bean.getOrderSn();
					orderToHistoryMapper.distributeActionToHistory(orderSn);
					orderToHistoryMapper.orderDepotShipToHistory(orderSn);
					orderToHistoryMapper.deleteDistributeAction(orderSn);
					orderToHistoryMapper.deleteOrderDepotShip(orderSn);
				}
			}
			//注意：由于交货单日志表和配送表只关联了交货单号，所以删除的时候最后删交货单表
			orderToHistoryMapper.deleteMasOrdInfo(masterOrderSn);
			orderToHistoryMapper.deleteMasOrdAddInfo(masterOrderSn);
			orderToHistoryMapper.deleteMasOrdPay(masterOrderSn);
			orderToHistoryMapper.deleteMasOrdGoods(masterOrderSn);
			orderToHistoryMapper.deleteMasOrdAction(masterOrderSn);
			orderToHistoryMapper.deleteOrderDistribute(masterOrderSn);
			
			returnInfo.setIsOk(Constant.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			returnInfo.setMessage("将订单转为历史失败！异常="+e.toString());
			logger.error("订单转为历史失败！MasterOrderSn"+masterOrderSn + "异常",e);
			return returnInfo;
		}
		logger.debug("订单转为历史结束：MasterOrderSn"+masterOrderSn+"成功！");
		return returnInfo;
	}

	@Override
	public ReturnInfo reSplitOrder(String masterOrderSn, OrderStatus orderStatus) {
		logger.info("订单[" + masterOrderSn + "] 重新拆单orderStatus：" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		
		orderDistributeJmsTemplate.send(new TextMessageCreator(masterOrderSn));
		info.setIsOk(Constant.OS_YES);
		info.setMessage("放入拆单队列成功");
		return info;
	}
	
	@Override
	public ReturnInfo reCreateOrder(String masterOrderSn, OrderStatus orderStatus) {
		logger.info("订单[" + masterOrderSn + "] 重新创建订单orderStatus：" + JSON.toJSONString(orderStatus));
		return null;
	}

	@Override
	public ReturnInfo moveOrderAction(String masterOrderSn,
			OrderStatus orderStatus) {
		logger.debug("订单[" + masterOrderSn + "] orderStatus：" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]订单编号不能为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("订单[" + masterOrderSn + "]传入的参数[orderStatus]为空！");
			info.setMessage("订单[" + masterOrderSn + "]传入的参数[orderStatus]为空！");
			return info;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderSn", masterOrderSn);
			map.put("isHistory", orderStatus.getIsHistory());
			orderDistributeDefineMapper.moveOrderAction(map);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("订单日志迁移成功");
		} catch (Exception e) {
			logger.error("订单" + masterOrderSn + "日志迁移失败:", e);
			info.setMessage("订单日志迁移异常:"+e.getMessage());
			return info;
		}
		return info;
	}

	@Override
	public ReturnInfo handworkOrder(String shopCode, String userId,
			List<String> skus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnInfo<String> sendGotCode(OrderStatus orderStatus) {
		logger.info("自提短信发送 orderStatus：" + JSON.toJSONString(orderStatus));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO, "发送短息失败");
		if (orderStatus == null) {
			logger.error("参数为空");
			return null;
		}
		String masterOrderSn = orderStatus.getMasterOrderSn();
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("订单号为空");
			return info;
		}

		try {
			String templateCode = "101";
			SystemMsgTemplate smt = systemMsgTemplateMapper.selectByPrimaryKey(templateCode);
			if (smt == null) {
				logger.error("短信模板不存在");
				return info;
			}
			String smsTemplate = smt.getTemplate();
			if (StringUtil.isTrimEmpty(smsTemplate)) {
				info.setMessage("smsTemplate短信模板为空");
				return info;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				info.setMessage("订单[" + masterOrderSn + "]不存在");
				return info;
			}
			if (StringUtil.isTrimEmpty(master.getStoreCode())) {
				info.setMessage("订单[" + masterOrderSn + "]线上店铺编码不存在");
				return info;
			}
			String storeAddress = orderStatus.getStoreAddress();
			if (StringUtil.isTrimEmpty(storeAddress)) {
				info.setMessage("订单[" + masterOrderSn + "]线下店铺[" + master.getStoreCode() +"]线下店铺地址为空");
				return info;
			}
			String mobile = master.getMobile();
			if (StringUtil.isTrimEmpty(mobile)) {
				info.setMessage("订单[" + masterOrderSn + "]手机号码不存在");
				return info;
			}
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNull(distributes)) {
				info.setMessage("订单[" + masterOrderSn + "]配货单不存在");
				return info;
			}
			String gotStatus = distributes.get(0).getGotCode();
			if (StringUtil.isTrimEmpty(gotStatus)) {
				info.setMessage("订单[" + masterOrderSn + "]自提码不存在");
				return info;
			}
			String userName = ConfigCenter.getProperty("sms.username");
			String password = ConfigCenter.getProperty("sms.password");
			String sendType = ConfigCenter.getProperty("sms.sendtype");
			String channelCode = ConfigCenter.getProperty("sms.refer");
			User u = new User();
			u.setUsername(userName);
			u.setPassword(password);
			Message msg = new Message();
			msg.setPhoneNO(mobile.trim());
			msg.setChannelCode(channelCode);
			msg.setSendType(sendType);
			String smsInfo = smsTemplate.replace("{$store_address}", storeAddress).replace("{$got_status}", gotStatus);
			msg.setMsgContent(smsInfo);
			logger.info("订单[" + masterOrderSn + "]发送自提码 request:" + JSON.toJSONString(msg) );
			State resultState = sMSService.send(u, msg);
			logger.info("订单[" + masterOrderSn + "]发送自提码 respone:" + JSON.toJSONString(resultState));
			if (!State.SUCCESSFULLY.equals(resultState.getState())) {
				// 发送失败
				logger.error(resultState.getPhoneNo() + "短信发送失败,原因:" + resultState.getMessage() + ",msg:" + JSON.toJSONString(msg));
			} else {
				// 发送成功
				logger.info(resultState.getPhoneNo() + "短信发送成功" + ",msg:" + JSON.toJSONString(msg));
				OrderDistribute updateDistribute = new OrderDistribute();
				updateDistribute.setSmsStatus(Constant.SMS_STATUS_YES);
				updateDistribute.setOrderSn(distributes.get(0).getOrderSn());
				updateDistribute.setUpdateTime(new Date());
				orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
				info.setMessage("自提码短信发送成功");
				info.setIsOk(Constant.OS_YES);
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "自提码短信发送失败" + e.getMessage(), e);
			info.setMessage("自提码短信发送失败" + e.getMessage());
		} finally {
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, info.getMessage(), orderStatus.getAdminUser());
		}
		return info;
	}
}
