package com.work.shop.oms.orderReturn.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.work.shop.oms.bean.*;
import com.work.shop.oms.order.service.MasterOrderGoodsService;
import com.work.shop.oms.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.bean.OrderReturnForSellers;
import com.work.shop.oms.api.bean.ReturnGoods;
import com.work.shop.oms.api.bean.ReturnInfoPage;
import com.work.shop.oms.api.param.bean.CreateOrderRefund;
import com.work.shop.oms.api.param.bean.CreateOrderReturn;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.api.param.bean.CreateOrderReturnGoods;
import com.work.shop.oms.api.param.bean.CreateOrderReturnShip;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnSearchParams;
import com.work.shop.oms.api.param.bean.ReturnShipUpdateInfo;
import com.work.shop.oms.api.param.bean.ReturnStorageParam;
import com.work.shop.oms.api.param.bean.SellerBean;
import com.work.shop.oms.api.param.bean.SellerParam;
import com.work.shop.oms.api.param.bean.WmsData;
import com.work.shop.oms.api.param.bean.WmsReturnData;
import com.work.shop.oms.api.param.bean.WmsReturnGoods;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.CreateReturnVO;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OptionPointsBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.NumberUtil;
import com.work.shop.oms.dao.ErpWarehouseListMapper;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderPeriodDetailMapper;
import com.work.shop.oms.dao.OrderRefundMapper;
import com.work.shop.oms.dao.OrderReturnActionMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.dao.SystemReturnSnMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.dao.define.OrderReturnSearchMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderMonitorService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.service.BrandUtilService;
import com.work.shop.oms.service.MessageService;
import com.work.shop.oms.shoppay.service.ShopPayService;
import com.work.shop.oms.vo.ReturnOrderParam;
import com.work.shop.oms.vo.StorageGoods;

/**
 * 订单退货服务
 * @author QuYachu
 */
@Service("orderReturnService")
public class OrderReturnServiceImpl implements OrderReturnService {

	private static Logger logger = LoggerFactory.getLogger(OrderReturnServiceImpl.class);
	
	@Resource(name="orderReturnStService")
	private OrderReturnStService orderReturnStService;
	
	@Resource(name="orderMonitorServiceImpl")
	private OrderMonitorService orderMonitorService;
	
//	@Resource(name="orderExpressPullService")
//	private OrderExpressPullService orderExpressPullService;
	
	@Resource
	private RedisClient redisClient;

	@Resource
	BrandUtilService brandUtil;
	
	//@Resource
	private UserPointsService userPointsService;

	@Resource
	private SystemReturnSnMapper systemReturnSnMapper;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderActionService masterOrderActionService;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;

	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;

	@Resource
	private ErpWarehouseListMapper erpWarehouseListMapper;

	@Resource
	private PayService payService;

	@Resource
	private ChannelInfoService channelInfoService;

	@Resource
	private OrderActionService orderActionService;

	@Resource(name = "orderCancelService")
	private OrderCancelService orderCancelService;

	@Resource(name = "shopPayServiceImpl")
	private ShopPayService shopPayService;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

	@Resource
	private MasterOrderGoodsService masterOrderGoodsService;

	@Override
	public ReturnInfo<String> orderReturnFinish(OrderStatus orderStatus) {
		logger.info("退单完成通知 orderStatus" + JSON.toJSONString(orderStatus));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (orderStatus == null) {
			info.setMessage("参数[orderStatus] 不能为空");
			return info;
		}
		String returnSn = orderStatus.getReturnSn();
		OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
		if(orderReturn == null){
			info.setMessage("退单[" + returnSn +"] 信息查询结果为空");
			return info;
		}
		OrderReturnShip ship = orderReturnShipMapper.selectByPrimaryKey(returnSn);
		if(ship == null){
			info.setMessage("退单[" + returnSn +"] 退货单信息查询结果为空");
			return info;
		}
		if (orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE.intValue()
				|| orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED.intValue()) {
			info.setMessage("退单[" + returnSn +"] 状态已结算|已完结");
			return info;
		}
		try {
			// 退单成功
			if("1".equals(orderStatus.getCode())){
				OrderReturn updateReturn = new OrderReturn();
				updateReturn.setReturnSn(returnSn);
				updateReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.COMPLETE);
				updateReturn.setPayStatus((byte) Constant.OR_PAY_STATUS_SETTLED);
				updateReturn.setShipStatus((byte)3);
				updateReturn.setUpdateTime(new Date());
				orderReturnMapper.updateByPrimaryKeySelective(updateReturn);
				OrderReturnShip updateReturnShip = new OrderReturnShip();
				updateReturnShip.setRelatingReturnSn(returnSn);
				updateReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
				updateReturnShip.setUpdateTime(new Date());
				orderReturnShipMapper.updateByPrimaryKeySelective(updateReturnShip);
				OrderReturnGoods updateReturnGoods = new OrderReturnGoods();
				updateReturnGoods.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
				updateReturnGoods.setCheckinTime(new Date());
				OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
				orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
				orderReturnGoodsMapper.updateByExampleSelective(updateReturnGoods, orderReturnGoodsExample);
				orderActionService.addOrderReturnAction(returnSn, orderStatus.getMessage(), "XOMS");
				if (orderReturn.getProcessType().intValue() == Constant.OR_PROCESS_TYPE_EXCHANGE) {
					if ("1".equals(orderStatus.getNewOrderSnType())) {
						MasterOrderPayExample payExample = new MasterOrderPayExample();
						payExample.or().andMasterOrderSnEqualTo(orderReturn.getNewOrderSn()).andPayStatusEqualTo((byte) Constant.OP_PAY_STATUS_COMFIRM);
						List<MasterOrderPay> payList = masterOrderPayMapper.selectByExample(payExample);
						//确认换单退单转入款
						for (MasterOrderPay orderPay : payList) {
							OrderStatus payOrderStatus = new OrderStatus();
							payOrderStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
							payOrderStatus.setMasterOrderSn(orderReturn.getNewOrderSn());
							payOrderStatus.setPaySn(orderPay.getMasterPaySn());
							if (StringUtil.isNotEmpty(orderPay.getPayNote())) {
								payOrderStatus.setMessage(orderPay.getPayNote());
							}
							ReturnInfo payResult = payService.orderReturnPayStCh(payOrderStatus);
							logger.info("退货单完成时调用换货单付款接口修改待确认付款单数据：returnSn:"+returnSn+",payStCh:"+JSON.toJSONString(payResult));
						}
					} else {
						//确认换单退单转入款
						OrderStatus cancelStatus = new OrderStatus();
						cancelStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
						cancelStatus.setMasterOrderSn(orderReturn.getNewOrderSn());
						cancelStatus.setCode("8023");
						cancelStatus.setType(ConstantValues.CREATE_RETURN.NO);
						cancelStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.OMS);
						cancelStatus.setMessage("退货单["+orderReturn.getReturnSn()+"]完成撤销换货单");
						ReturnInfo cancelInfo = orderCancelService.cancelOrderByMasterSn(orderReturn.getNewOrderSn(), cancelStatus);
						logger.info("退货单完成调用换货单取消接口数据：returnSn:"+returnSn+",cancelInfo:"+JSON.toJSONString(cancelInfo));
						if (cancelInfo.getIsOk() == Constant.OS_NO) {
							info.setMessage(cancelInfo.getMessage());
							return info;
						}
					}
				} else {
					String userId = orderReturn.getUserId();
					// 退单完结后返回点数
					if (orderReturn.getSiteCode().equals(Constant.KELTI)) {
						if (orderReturn.getPoints().doubleValue() > 0.0) {
							Double points = orderReturn.getPoints().doubleValue();
							String pointsNote = null;
							try {
								OptionPointsBean pointsBean = new OptionPointsBean(userId, orderReturn.getReturnTotalFee().doubleValue(),
										returnSn, points, orderReturn.getBvValue(), 1);
								ReturnInfo<Integer> returnInfo = userPointsService.optionPoints(pointsBean);
								if(returnInfo.getIsOk() == Constant.OS_YES){
									logger.debug("退单完结-点数("+ points + ")冻结成功！orderSn:" + returnSn +",userId:"+userId);
									pointsNote = "退单完结-点数(" + points + ")冻结成功！";
									orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
									pointsBean.setOrderType(4);
									ReturnInfo<Integer> returnPointsInfo = userPointsService.optionPoints(pointsBean);
									if(returnPointsInfo.getIsOk() == Constant.OS_YES){
										logger.debug("退单完结-点数("+ points + ")退返成功！orderSn:" + returnSn +",userId:"+userId);
										pointsNote = "退单完结-点数("+ points + ")退返成功！";
										orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
									} else {
										logger.error("退单完结-点数(" + points + ")退返失败！orderSn:"+ returnSn +",userId:" + userId);
										pointsNote = "退单完结-点数(" + points + ")退返失败！错误信息:"+ returnPointsInfo.getMessage();
										orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
										// 释放冻结点数
										// 记录日志
										pointsBean.setOrderType(2);
										ReturnInfo<Integer> realesePointsInfo = userPointsService.optionPoints(pointsBean);
										if(realesePointsInfo.getIsOk() == Constant.OS_YES){
											logger.debug("退单完结-点数("+ points + ")释放成功！orderSn:" + returnSn +",userId:"+userId);
											pointsNote = "退单完结-点数("+ points + ")释放成功！";
											orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
										} else {
											logger.error("退单完结-点数(" + points + ")释放失败！orderSn:"+ returnSn +",userId:" + userId);
											pointsNote = "退单完结-点数(" + points + ")释放失败！错误信息:"+realesePointsInfo.getMessage();
											orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
										}
									}
								} else {
									logger.error("退单完结-点数(" + points + ")冻结失败！orderSn:"+ returnSn +",userId:" + userId);
									pointsNote = "退单完结-点数(" + points + ")冻结失败！返回信息:"+ returnInfo.getMessage();
									orderActionService.addOrderReturnAction(returnSn, pointsNote, Constant.OS_STRING_SYSTEM);
								}
							} catch (Exception e) {
								logger.error(returnSn + "用户ID：" + userId + "退单完结-退还点数("+points+")失败" + e.getMessage(), e);
								pointsNote = "退单完结-点数(" + points + ")退还失败！错误信息:" + e.getMessage();
							}
						}
					}
					// 退单完结余额退换  换货退单不退余额
					if (Constant.Chlitina.equals(orderReturn.getSiteCode())) {
						OrderRefundExample refundExample = new OrderRefundExample();
						refundExample.or().andRelatingReturnSnEqualTo(returnSn).andReturnPayEqualTo((short)3);
						List<OrderRefund> orderRefunds = this.orderRefundMapper.selectByExample(refundExample);
						if (StringUtil.isListNotNull(orderRefunds)) {
							OrderRefund orderRefund = orderRefunds.get(0);
							// 使用余额支付
							if (orderRefund.getReturnFee().floatValue() > 0) {
								String memo = "订单[" + returnSn + "]退单完结后返还余额：" + orderRefund.getReturnFee();
								ReturnInfo<String> apiBack = shopPayService.backSurplus(userId,
										orderRefund.getReturnFee().floatValue(), returnSn, memo);
								String msg = "";
								if (apiBack.getIsOk() == Constant.OS_YES) {
									msg = "余额[" + orderRefund.getReturnFee() + "]返还成功,流水号[" + apiBack.getData() + "]";
								} else {
									msg = "余额[" + orderRefund.getReturnFee() + "]返还失败,原因:" + apiBack.getMessage();
								}
								orderActionService.addOrderReturnAction(returnSn, msg, Constant.OS_STRING_SYSTEM);
							}
						}
					}
				}
				// 扣减BV bvvalue 克缇丽娜、新美力都使用|退单换单退单积分都扣减
				if (orderReturn.getBvValue() != null && orderReturn.getBvValue().intValue() > 0) {
					if (Constant.KELTI.equals(orderReturn.getSiteCode())) {
						ReturnInfo<String> returnInfo = userPointsService.customerUseBvByKT(orderReturn.getUserId(),
								orderReturn.getBvValue(), returnSn, "1");
						logger.info(JSON.toJSONString(returnInfo));
						String note = "退单完结-扣减BV(" + orderReturn.getBvValue() + ")";
						if (returnInfo == null || returnInfo.getIsOk() == Constant.OS_NO) {
							note += (returnInfo == null ? "调用接口返回为空！" : returnInfo.getMessage());
						} else {
							note += "成功";
						}
						orderActionService.addOrderReturnAction(returnSn, note, Constant.OS_STRING_SYSTEM);
					} else if (Constant.NEWFORCE.equals(orderReturn.getSiteCode())) {
						ReturnInfo<String> returnInfo = userPointsService.customerUseBv(orderReturn.getUserId(),
								orderReturn.getBvValue(), returnSn, "1");
						logger.info(JSON.toJSONString(returnInfo));
						String note = "退单完结-扣减BV(" + orderReturn.getBvValue() + ")";
						if (returnInfo == null || returnInfo.getIsOk() == Constant.OS_NO) {
							note += (returnInfo == null ? "调用接口返回为空！" : returnInfo.getMessage());
						} else {
							note += "成功";
						}
						orderActionService.addOrderReturnAction(returnSn, note, Constant.OS_STRING_SYSTEM);
					}
				}
				// 原订单作废
				/*MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
				if (master != null) {
					if (master.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLSHIPED) {
						//确认换单退单转入款
						OrderStatus cancelStatus = new OrderStatus();
						cancelStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
						cancelStatus.setMasterOrderSn(orderReturn.getNewOrderSn());
						cancelStatus.setCode("8023");
						cancelStatus.setType(ConstantValues.CREATE_RETURN.NO);
						cancelStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.OMS);
						cancelStatus.setMessage("退货单["+orderReturn.getReturnSn()+"]完成撤销换货单");
						ReturnInfo cancelInfo = orderCancelService.cancelOrderByMasterSn(orderReturn.getMasterOrderSn(), cancelStatus);
						logger.info("退货单完成调用原订单取消接口数据：returnSn:"+returnSn+",cancelInfo:"+JSON.toJSONString(cancelInfo));
					}
				}*/
			} else {
				// 退单作废
				ReturnInfo returnInfo = orderReturnStService.returnOrderInvalid(returnSn, orderStatus.getMessage(), "XOMS");
				if (returnInfo.getIsOk() == Constant.OS_NO) {
					info.setMessage(returnInfo.getMessage());
					return info;
				}
				if (orderReturn.getProcessType().intValue() == Constant.OR_PROCESS_TYPE_EXCHANGE) {
					// 换单作废
					orderStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
					orderStatus.setMasterOrderSn(orderReturn.getNewOrderSn());
					orderStatus.setCode("8023");
					orderStatus.setType(ConstantValues.CREATE_RETURN.NO);
					orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.OMS);
					orderStatus.setMessage("退货单["+orderReturn.getReturnSn()+"]撤销关联换货单撤销");
					ReturnInfo cancelInfo = orderCancelService.cancelOrderByMasterSn(orderReturn.getNewOrderSn(), orderStatus);
					logger.info("退货单失败时调用换货单取消接口数据：returnSn:"+returnSn+",cancelInfo:"+JSON.toJSONString(cancelInfo));
					if (cancelInfo.getIsOk() == Constant.OS_NO) {
						info.setMessage(cancelInfo.getMessage());
						return info;
					}
				}
			}
			info.setIsOk(Constant.OS_YES);
			info.setMessage("退单完成");
		} catch (Exception e) {
			logger.error("退单[" + returnSn + "]完成更新异常：" + e.getMessage(), e);
			info.setMessage("退单[" + returnSn + "]完成更新异常：" + e.getMessage());
		}
		return info;
	}

	@Override
	public ReturnInfo<String> giveUserPoints(String uid, Integer points, String orderSn) {
		return userPointsService.giveUserPoints(uid, points, orderSn);
	}
	
	@Override
	public ReturnInfo<String> plusAndMinusPoints(SettleOrderInfo settleOrderInfo) {
		return userPointsService.processUserPoints(settleOrderInfo);
	}
	
	@Override
	public ReturnInfo<String> deductionsPoints(String uid, Integer points,
			String dealCode) {
		return userPointsService.deductionsPoints(uid, points, dealCode);
	}
	
	/**
	 * 创建退单
	 */
	@Override
	public ReturnInfo<String> createOrderReturn(CreateOrderReturnBean param) {
		logger.info("createOrderReturn relOrderSn:"+param.getRelatingOrderSn()+",start:" + JSON.toJSONString(param));
		ReturnInfo<String> orm = new ReturnInfo<String>();
		orm.setOrderSn(param.getRelatingOrderSn());
		long result = -1;
		try {
			result = redisClient.setnx("return_relating_order_sn" + param.getRelatingOrderSn() , param.getRelatingOrderSn());
			// redis 超时时间为5秒钟
			redisClient.expire("return_relating_order_sn" + param.getRelatingOrderSn(), 5);
		} catch (Throwable e) {
			logger.error("退单保存：判断同一时间内用户多次提交,读取redis异常:" + e);
		}
		
		if (result == 0) {
			throw new RuntimeException("短时间(5s)内用户不允许多次提交同一退单！");
		}
		orm.setIsOk(Constant.YESORNO_NO);
		try {
			if (StringUtils.isBlank(param.getRelatingOrderSn())) {
				throw new RuntimeException("退单关联订单号为空");
			}
			String returnSn = generateOrderReturnSn();
			if (StringUtils.isBlank(returnSn)) {
				throw new RuntimeException("退单号生成失败");
			}
			if (param.getReturnType() != Constant.OR_RETURN_TYPE_RETURN
					&& param.getReturnType() != Constant.OR_RETURN_TYPE_REJECT
					&& param.getReturnType() != Constant.OR_RETURN_TYPE_EXREFUND
					&& param.getReturnType() != Constant.OR_RETURN_TYPE_LOSERETURN) {
				throw new RuntimeException("退单类型传入失败,returnType:" + param.getReturnType());
			}
			param.setOrderReturnSn(returnSn);
			param.setAddTime(new Date());
			orm = createOrderReturnAllIn(param);
			orm.setReturnSn(returnSn);
			orm.setMessage("退单创建成功" + returnSn);
			orm.setIsOk(Constant.YESORNO_YES);
		} catch (Exception e) {
			orm.setIsOk(Constant.YESORNO_NO);
			orm.setMessage("生成退单失败: returnType:" + param.getReturnType() + ",orderSn:"+ param.getRelatingOrderSn()+","+e.getMessage());
			logger.error("生成退单失败:orderSn:"+param.getRelatingOrderSn()+",错误信息:"+e.getMessage(),e);
		}
		logger.info("createOrderReturn end :" + JSON.toJSONString(orm));
		return orm;
	}

	@Override
	public ReturnInfo<String> updateOrderReturn(CreateOrderReturnBean orderReturnBean) {
		logger.info("updateOrderReturn start:"+ JSON.toJSONString(orderReturnBean));
		ReturnInfo<String> orm = new ReturnInfo<String>();
		orm.setReturnSn(orderReturnBean.getOrderReturnSn());
		orm.setIsOk(Constant.YESORNO_NO);
		String actionNote = "退单数据更新成功 ";
		boolean updateShipFlag = false;
		
		if(StringUtils.isBlank(orderReturnBean.getOrderReturnSn())){
			throw new RuntimeException("退单号为空更新失败");
		}
		if (CollectionUtils.isNotEmpty(orderReturnBean.getCreateOrderRefundList())) {
			for (CreateOrderRefund createOrderRefund : orderReturnBean.getCreateOrderRefundList()) {
				if(StringUtils.isBlank(createOrderRefund.getReturnPaySn())){
					throw new RuntimeException("付款单号为空更新失败");
				}
			}
		}
		
		OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
		if(orderReturn == null){
			throw new RuntimeException("退单数据不存在");
		}
		OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
		if(orderReturnShip == null){
			throw new RuntimeException("退单发货数据不存在");
		}
		OrderRefundExample orderRefundExample = new OrderRefundExample();
		orderRefundExample.or().andRelatingReturnSnEqualTo(orderReturnBean.getOrderReturnSn());
		List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
		if(CollectionUtils.isEmpty(orderRefundList)){
			throw new RuntimeException("退款数据不存在");
		}
		Map<String,OrderRefund> orderRefundMap = new HashMap<String, OrderRefund>();
		for (OrderRefund orderRefund : orderRefundList) {
			orderRefundMap.put(orderRefund.getReturnPaySn(), orderRefund);
		}
		
		SystemPaymentExample systemPaymentExample = new SystemPaymentExample();
		systemPaymentExample.or();
		List<SystemPayment> systemPaymentList = systemPaymentMapper.selectByExample(systemPaymentExample);
		Map<String, String> systemPaymentMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(systemPaymentList)){
			for (SystemPayment systemPayment : systemPaymentList) {
				systemPaymentMap.put(systemPayment.getPayId()+StringUtils.EMPTY, systemPayment.getPayName());
			}
		}
		
		SystemShippingExample systemShippingExample = new SystemShippingExample();
		systemShippingExample.or();
		List<SystemShipping> systemShippingList = systemShippingMapper.selectByExample(systemShippingExample);
		Map<String, String> systemShippingMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(systemShippingList)){
			for (SystemShipping systemShipping : systemShippingList) {
				systemShippingMap.put(systemShipping.getShippingCode(), systemShipping.getShippingName());
			}
		}
		try {
			//退单表更新
			if (orderReturnBean.getCreateOrderReturn() != null ){
				OrderReturn updateOrderReturn = new OrderReturn();
				updateOrderReturn.setReturnSn(orderReturnBean.getOrderReturnSn());
				updateOrderReturn.setUpdateTime(new Date());
				
				int returnCount = 0 ;
				if(orderReturnBean.getCreateOrderReturn().getHaveRefund() != null && orderReturn.getHaveRefund().intValue() != orderReturnBean.getCreateOrderReturn().getHaveRefund().intValue()){
					returnCount ++;
					updateOrderReturn.setHaveRefund(orderReturnBean.getCreateOrderReturn().getHaveRefund().byteValue());
					actionNote += " 是否需要退款 由 "+(orderReturn.getHaveRefund().intValue() == ConstantValues.YESORNO_YES.intValue() ? "需要退款" : "无须退款")+" 修改为 "+(orderReturnBean.getCreateOrderReturn().getHaveRefund() == ConstantValues.YESORNO_YES.intValue() ? "需要退款" : "无须退款")+"；<br />";				   
				}
				if(orderReturnBean.getCreateOrderReturn().getReturnReason() != null && !StringUtils.equalsIgnoreCase(orderReturn.getReturnReason(),StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturn().getReturnReason()))){
					returnCount ++;
					updateOrderReturn.setReturnReason(orderReturnBean.getCreateOrderReturn().getReturnReason());
					actionNote += "退单原因 由"+orderReturn.getReturnReason()+" 修改为"+orderReturnBean.getCreateOrderReturn().getReturnReason()+"；<br />";
				}
				if(orderReturnBean.getCreateOrderReturn().getProcessType() != null && orderReturn.getProcessType().intValue() != orderReturnBean.getCreateOrderReturn().getProcessType().intValue()){
					returnCount ++;
					updateOrderReturn.setProcessType(orderReturnBean.getCreateOrderReturn().getProcessType());
					actionNote += " 处理方式由 "+orderReturn.getProcessType()+" 修改为 "+updateOrderReturn.getProcessType()+"；<br />";
				}
				if(orderReturnBean.getCreateOrderReturn().getReturnSettlementType() != null && orderReturn.getReturnSettlementType().intValue() != orderReturnBean.getCreateOrderReturn().getReturnSettlementType().intValue()){
					returnCount ++;
					updateOrderReturn.setReturnSettlementType(orderReturnBean.getCreateOrderReturn().getReturnSettlementType());
					actionNote += "预付款/保证金由 "+orderReturn.getReturnSettlementType()+" 修改为 "+updateOrderReturn.getReturnSettlementType()+"；<br />";				  
				}
				if(orderReturnBean.getCreateOrderReturn().getNewOrderSn() != null &&!StringUtils.equalsIgnoreCase(orderReturn.getNewOrderSn(),StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturn().getNewOrderSn()))){
					returnCount ++;
					updateOrderReturn.setNewOrderSn(orderReturnBean.getCreateOrderReturn().getNewOrderSn());
					actionNote += "换货单号由 "+orderReturn.getNewOrderSn()+" 修改为 "+updateOrderReturn.getNewOrderSn()+"；<br />";				 
				}
				if(returnCount > 0){
					orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
				}
			}
			
			//退单发货表更新
			if (orderReturnBean.getCreateOrderReturnShip() != null && orderReturnBean.getCreateOrderReturnShip().getRelatingReturnSn() !=null){
				OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
				updateOrderReturnShip.setRelatingReturnSn(orderReturnBean.getOrderReturnSn());
				updateOrderReturnShip.setUpdateTime(new Date());
				
				int shipCount = 0;
				if(!StringUtils.equalsIgnoreCase(orderReturnShip.getReturnExpress(), StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getReturnExpress()))){
					shipCount ++;
					updateShipFlag = true ;
					updateOrderReturnShip.setReturnExpress(StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getReturnExpress()));
					actionNote += " 配送方式由 "+systemShippingMap.get(orderReturnShip.getReturnExpress())+" 修改为 "+systemShippingMap.get(updateOrderReturnShip.getReturnExpress())+"；<br />";					
				}
				if(!StringUtils.equalsIgnoreCase(orderReturnShip.getReturnInvoiceNo(), StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getReturnInvoiceNo()))){
					shipCount ++;
					updateShipFlag = true ;
					updateOrderReturnShip.setReturnInvoiceNo(StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getReturnInvoiceNo()));
					actionNote += " 退货单快递单号由 "+orderReturnShip.getReturnInvoiceNo()+" 修改为 "+updateOrderReturnShip.getReturnInvoiceNo()+"；<br />";				   
				}
				if(!StringUtils.equalsIgnoreCase(orderReturnShip.getDepotCode(), StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getDepotCode()))){
					shipCount ++;
					updateOrderReturnShip.setDepotCode(StringUtils.trimToEmpty(orderReturnBean.getCreateOrderReturnShip().getDepotCode()));
					actionNote += " 退货仓库由 "+orderReturnShip.getDepotCode()+" 修改为 "+updateOrderReturnShip.getDepotCode()+"；<br />";				  
					OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
					goodsExample.or().andRelatingReturnSnEqualTo(orderReturnBean.getOrderReturnSn());
					OrderReturnGoods returnGoods = new OrderReturnGoods();
					returnGoods.setOsDepotCode(updateOrderReturnShip.getDepotCode());
					orderReturnGoodsMapper.updateByExampleSelective(returnGoods, goodsExample);
				}
				if(shipCount > 0){
					orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
				}
			}
			orderActionService.addOrderReturnAction(orderReturnBean.getOrderReturnSn(),actionNote, orderReturnBean.getActionUser());
			//当退单物流中有承运商和快递单号时，添加物流监控
			if(updateShipFlag){
				OrderReturn ormOrderReturn = orderReturnMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
				OrderReturnShip ormOrderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
				if(StringUtils.isNotBlank(ormOrderReturnShip.getReturnExpress()) && StringUtils.isNotBlank(ormOrderReturnShip.getReturnInvoiceNo())){
					logger.info("更新退单信息添加物流监控：returnSn:"+orderReturnBean.getOrderReturnSn()+",orderReturn:"+JSON.toJSONString(ormOrderReturn)+",orderReturnShip:"+JSON.toJSONString(ormOrderReturnShip));
//					orderExpressPullService.orderReturnExpress(ormOrderReturn, ormOrderReturnShip);
				}
			}
			orm.setIsOk(Constant.YESORNO_YES);
			orm.setMessage("数据更新成功");
		} catch (Exception e) {
			logger.error("updateOrderReturn.returnSn:"+orderReturnBean.getOrderReturnSn()+"错误信息:"+e.getMessage(),e);
			orm.setIsOk(Constant.YESORNO_NO);
			orm.setMessage("数据更新失败,异常如下:"+e.getMessage());
		}
		logger.info("updateOrderReturn end:" + JSON.toJSONString(orm));
		return orm;
	}
	
	@Override
	public ReturnInfo<String> updateOrderReturnFee(CreateOrderReturnBean orderReturnBean) {
		logger.info("updateOrderReturnFee start:"+ JSON.toJSONString(orderReturnBean));
		ReturnInfo<String> orm = new ReturnInfo<String>();
		orm.setReturnSn(orderReturnBean.getOrderReturnSn());
		orm.setIsOk(Constant.YESORNO_NO);
		String actionNote = "退单费用信息更新成功 ";
		
		if(StringUtils.isBlank(orderReturnBean.getOrderReturnSn())){
			throw new RuntimeException("退单号为空更新失败");
		}
		if (CollectionUtils.isNotEmpty(orderReturnBean.getCreateOrderRefundList())) {
			for (CreateOrderRefund createOrderRefund : orderReturnBean.getCreateOrderRefundList()) {
				if(StringUtils.isBlank(createOrderRefund.getReturnPaySn())){
					throw new RuntimeException("付款单号为空更新失败");
				}
			}
		}
		
		OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
		if(orderReturn == null){
			throw new RuntimeException("退单数据不存在");
		}
		OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderReturnBean.getOrderReturnSn());
		if(orderReturnShip == null){
			throw new RuntimeException("退单发货数据不存在");
		}
		OrderRefundExample orderRefundExample = new OrderRefundExample();
		orderRefundExample.or().andRelatingReturnSnEqualTo(orderReturnBean.getOrderReturnSn());
		List<OrderRefund> orderRefundList = orderRefundMapper.selectByExample(orderRefundExample);
		if(CollectionUtils.isEmpty(orderRefundList)){
			throw new RuntimeException("退款数据不存在");
		}
		Map<String,OrderRefund> orderRefundMap = new HashMap<String, OrderRefund>();
		for (OrderRefund orderRefund : orderRefundList) {
			orderRefundMap.put(orderRefund.getReturnPaySn(), orderRefund);
		}
		
		SystemPaymentExample systemPaymentExample = new SystemPaymentExample();
		systemPaymentExample.or();
		List<SystemPayment> systemPaymentList = systemPaymentMapper.selectByExample(systemPaymentExample);
		Map<String, String> systemPaymentMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(systemPaymentList)){
			for (SystemPayment systemPayment : systemPaymentList) {
				systemPaymentMap.put(systemPayment.getPayId()+StringUtils.EMPTY, systemPayment.getPayName());
			}
		}
		 
		try {
			//付款信息更新
			if (CollectionUtils.isNotEmpty(orderReturnBean.getCreateOrderRefundList())) {
				for (CreateOrderRefund createOrderRefund : orderReturnBean.getCreateOrderRefundList()) {
					OrderRefund updateOrderRefund = new OrderRefund();
					updateOrderRefund.setRelatingReturnSn(orderReturnBean.getOrderReturnSn());
					updateOrderRefund.setReturnPaySn(createOrderRefund.getReturnPaySn());
					updateOrderRefund.setReturnPay(createOrderRefund.getReturnPay());
					updateOrderRefund.setReturnFee(BigDecimal.valueOf(createOrderRefund.getReturnFee()));
					updateOrderRefund.setUpdateTime(new Date());
					
					int refundCount = 0;
					if(createOrderRefund.getReturnPay() != null && orderRefundMap.get(createOrderRefund.getReturnPaySn()).getReturnPay() != createOrderRefund.getReturnPay()){
						refundCount ++;
						actionNote += " 退款单"+createOrderRefund.getReturnPaySn()+"退款方式 由 "+systemPaymentMap.get(orderRefundMap.get(createOrderRefund.getReturnPaySn()).getReturnPay() + StringUtils.EMPTY)+" 修改为 "+systemPaymentMap.get(createOrderRefund.getReturnPay()+StringUtils.EMPTY)+"；<br />";				   
					}
					if(orderRefundMap.get(createOrderRefund.getReturnPaySn()).getReturnFee().compareTo(BigDecimal.valueOf(createOrderRefund.getReturnFee())) != 0){
						refundCount ++;
						actionNote += " 退款单"+createOrderRefund.getReturnPaySn()+"退款金额 由 "+orderRefundMap.get(createOrderRefund.getReturnPaySn()).getReturnFee().doubleValue()+" 修改为 "+createOrderRefund.getReturnFee().doubleValue()+"；<br />";				 
					}
					if(refundCount > 0){
						orderRefundMapper.updateByPrimaryKeySelective(updateOrderRefund);
					}
				}
			}
			
			//退单表更新
			if (orderReturnBean.getCreateOrderReturn() != null ){
				OrderReturn updateOrderReturn = new OrderReturn();
				updateOrderReturn.setReturnSn(orderReturnBean.getOrderReturnSn());
				updateOrderReturn.setUpdateTime(new Date());
				
				int returnCount = 0 ;
				/*if(orderReturn.getHaveRefund().intValue() != orderReturnBean.getCreateOrderReturn().getHaveRefund().intValue()){
					returnCount ++;
					updateOrderReturn.setHaveRefund(orderReturnBean.getCreateOrderReturn().getHaveRefund().byteValue());
					actionNote += " 是否需要退款 由 "+(orderReturn.getHaveRefund().intValue() == ConstantValues.YESORNO_YES.intValue() ? "需要退款" : "无须退款")+" 修改为 "+(orderReturnBean.getCreateOrderReturn().getHaveRefund() == ConstantValues.YESORNO_YES.intValue() ? "需要退款" : "无须退款")+"；<br />";				   
				}*/
				if(orderReturn.getReturnTotalFee().compareTo(BigDecimal.valueOf(orderReturnBean.getCreateOrderReturn().getReturnTotalFee())) != 0){
					returnCount ++;
					updateOrderReturn.setReturnTotalFee(ConstantsUtil.obj2Bigdecimal(orderReturnBean.getCreateOrderReturn().getReturnTotalFee()));
					actionNote += " 退单总金额 由 "+orderReturn.getReturnTotalFee().doubleValue()+" 修改为 "+orderReturnBean.getCreateOrderReturn().getReturnTotalFee().doubleValue()+"；<br />";				 
				}
				if(orderReturn.getReturnShipping().compareTo(BigDecimal.valueOf(orderReturnBean.getCreateOrderReturn().getReturnShipping())) != 0){
					returnCount ++;
					updateOrderReturn.setReturnShipping(ConstantsUtil.obj2Bigdecimal(orderReturnBean.getCreateOrderReturn().getReturnShipping()));
					actionNote += " 退单配送费用 由 "+orderReturn.getReturnShipping().doubleValue()+" 修改为 "+orderReturnBean.getCreateOrderReturn().getReturnShipping().doubleValue()+"；<br />";					
				}
				if(orderReturn.getReturnOtherMoney().compareTo(BigDecimal.valueOf(orderReturnBean.getCreateOrderReturn().getReturnOtherMoney())) != 0){
					returnCount ++;
					updateOrderReturn.setReturnOtherMoney(ConstantsUtil.obj2Bigdecimal(orderReturnBean.getCreateOrderReturn().getReturnOtherMoney()));
					actionNote += " 退单其他费用 由 "+orderReturn.getReturnOtherMoney().doubleValue()+" 修改为 "+orderReturnBean.getCreateOrderReturn().getReturnOtherMoney().doubleValue()+"；<br />";					
				}
				if(orderReturn.getIntegralMoney().compareTo(BigDecimal.valueOf(orderReturnBean.getCreateOrderReturn().getTotalIntegralMoney())) != 0){
					returnCount ++;
					updateOrderReturn.setIntegralMoney(ConstantsUtil.obj2Bigdecimal(orderReturnBean.getCreateOrderReturn().getTotalIntegralMoney()));
					actionNote += " 退单积分使用金额 由 "+orderReturn.getIntegralMoney().doubleValue()+" 修改为 "+orderReturnBean.getCreateOrderReturn().getTotalIntegralMoney().doubleValue()+"；<br />";				   
				}
				if(orderReturn.getReturnBonusMoney().compareTo(BigDecimal.valueOf(orderReturnBean.getCreateOrderReturn().getReturnBonusMoney())) != 0){
					returnCount ++;
					updateOrderReturn.setReturnBonusMoney(ConstantsUtil.obj2Bigdecimal(orderReturnBean.getCreateOrderReturn().getReturnBonusMoney()));
					actionNote += " 退单红包金额 由 "+orderReturn.getReturnBonusMoney().doubleValue()+" 修改为 "+orderReturnBean.getCreateOrderReturn().getReturnBonusMoney().doubleValue()+"；<br />";					
				}
				
				if(returnCount > 0){
					orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
				}
			}
			
			orderActionService.addOrderReturnAction(orderReturnBean.getOrderReturnSn(),actionNote, orderReturnBean.getActionUser());
			
			orm.setIsOk(Constant.YESORNO_YES);
			orm.setMessage("数据更新成功");
		} catch (Exception e) {
			logger.error("updateOrderReturnFee.returnSn:"+orderReturnBean.getOrderReturnSn()+"错误信息:"+e.getMessage(),e);
			orm.setIsOk(Constant.YESORNO_NO);
			orm.setMessage("费用更新失败,异常如下:"+e.getMessage());
		}
		logger.info("updateOrderReturnFee end:" + JSON.toJSONString(orm));
		return orm;
	}

	/**
	 * 创建退款单
	 * @param createReturnVO
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> createOrderReturnPay(CreateReturnVO createReturnVO) {
		logger.info("createOrderReturnPay..orderSn:" + createReturnVO.getMasterOrderSn() + ",createReturnVO:" + JSON.toJSONString(createReturnVO));
		ReturnInfo<String> orderReturnMessage = new ReturnInfo<String>();
		orderReturnMessage.setOrderSn(createReturnVO.getMasterOrderSn());
		orderReturnMessage.setIsOk(Constant.YESORNO_NO);
		orderReturnMessage.setMessage("退单创建失败");
		try {
			if (createReturnVO.getReturnMoney().doubleValue() < 0) {
				throw new RuntimeException("退款单退款金额必须大于等于零");
			}
			if (StringUtils.isBlank(createReturnVO.getActionUser())) {
				throw new RuntimeException("退款单操作人不能为空");
			}
			MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(createReturnVO.getMasterOrderSn());
			if (orderInfo == null) {
				throw new RuntimeException("退单关联的订单数据为空");
			}

            CreateOrderReturn createOrderReturn = new CreateOrderReturn();
			String sourceName = StringUtils.EMPTY;
			if (StringUtils.isNotBlank(createReturnVO.getReturnSource()) && StringUtils.equalsIgnoreCase(createReturnVO.getReturnSource(), ConstantValues.ORDERRETURN_REFUND_SOURCE.DELETE_GOODS)){
				sourceName = "OS商品删除退单创建";
				createOrderReturn.setRefundType(ConstantValues.ORDERRETURN_REFUND_TYPE.DELETE_GOODS.intValue());
				createOrderReturn.setReturnReason(ConstantValues.ORDERRETURN_REASON.RETURN_PAY_REASON_DELETE_GOODS);
				// 0:无须退款；1:需要退款
				createOrderReturn.setHaveRefund(ConstantValues.YESORNO_NO);
				createOrderReturn.setReturnSettlementType(ConstantValues.ORDERRETURN_SETTLMENT_TYPE.PRE_PAY.byteValue());
			}else if(StringUtils.isNotBlank(createReturnVO.getReturnSource()) && StringUtils.equalsIgnoreCase(createReturnVO.getReturnSource(), ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP)){
				sourceName = "OS订单发货退单创建";
				createOrderReturn.setRefundType(ConstantValues.ORDERRETURN_REFUND_TYPE.SHIP_ORDER.intValue());
				createOrderReturn.setReturnReason(ConstantValues.ORDERRETURN_REASON.RETURN_PAY_REASON_ALLSHIP);
				createOrderReturn.setHaveRefund(ConstantValues.YESORNO_YES);
				createOrderReturn.setReturnSettlementType(ConstantValues.ORDERRETURN_SETTLMENT_TYPE.PRE_PAY.byteValue());
			} else if(StringUtils.isNotBlank(createReturnVO.getReturnSource()) && StringUtils.equalsIgnoreCase(createReturnVO.getReturnSource(), ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_CANCEL)) {
				sourceName = "OS订单取消退单创建";
				createOrderReturn.setRefundType(ConstantValues.ORDERRETURN_REFUND_TYPE.CANCEL_ORDER.intValue());
                // 退单原因
				createOrderReturn.setReturnReason(ConstantValues.ORDERRETURN_REASON.RETURN_PAY_REASON_CANCEL);
				createOrderReturn.setHaveRefund(ConstantValues.YESORNO_YES);
				createOrderReturn.setReturnSettlementType(ConstantValues.ORDERRETURN_SETTLMENT_TYPE.PRE_PAY.byteValue());
				createOrderReturn.setProcessType((byte)1);
			} else {
				throw new RuntimeException("退款单来源为空无法正常处理");
			}
			
			//多次生成退款单校验
			OrderReturnExample returnExample = new OrderReturnExample();
			returnExample.or().andMasterOrderSnEqualTo(createReturnVO.getMasterOrderSn()).andReturnTypeEqualTo(ConstantValues.ORDERRETURN_TYPE.RETURN_PAY).andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY);
			List<OrderReturn> returnList = orderReturnMapper.selectByExample(returnExample);
			if (returnList.size() >= 1) {
				throw new RuntimeException("退款单无法重复创建!");
			}
			
			CreateOrderReturnBean orderReturnBean = new CreateOrderReturnBean();
			orderReturnBean.setRelatingOrderSn(createReturnVO.getMasterOrderSn());
			orderReturnBean.setReturnType(Constant.OR_RETURN_TYPE_REFUND);
			orderReturnBean.setActionUser(createReturnVO.getActionUser());
			String returnSn = generateOrderReturnSn();
			if (StringUtils.isBlank(returnSn)) {
				throw new RuntimeException("退单号生成失败");
			}
			orderReturnBean.setOrderReturnSn(returnSn);
			//已经生成退款单
			List<String> haveReturnReturnSnList = new ArrayList<String>();
			//退单商品
			/*List<CreateOrderReturnGoods> osCreateGoods = new ArrayList<CreateOrderReturnGoods>();
			for(com.work.shop.oms.common.bean.CreateOrderReturnGoods createOrderReturnGoods : createReturnVO.getOrderReturnGoodsList()){
				CreateOrderReturnGoods newCreateOrderReturnGoods = new CreateOrderReturnGoods();
				BeanUtils.copyProperties(newCreateOrderReturnGoods, createOrderReturnGoods);
				osCreateGoods.add(newCreateOrderReturnGoods);
			}*/
			List<CreateOrderReturnGoods> goodsList = processReturnPayGoods(createReturnVO.getMasterOrderSn(), createReturnVO.getReturnSource(), haveReturnReturnSnList,createReturnVO.getOrderReturnGoodsList());
			// 退商品总金额
			Double returnGoodsMoney = NumberUtil.getDoubleByValue(createReturnVO.getReturnMoney() - createReturnVO.getReturnShipping(), 2);
			// 退款金额平摊到退单商品上
			List<CreateOrderReturnGoods> returnGoodsList = shareReturnAmtForOrder(goodsList, returnGoodsMoney);
			orderReturnBean.setCreateOrderReturnGoodsList(returnGoodsList);
			
			// 退单
			// 操作人 前端传入
			createOrderReturn.setActionUser(createReturnVO.getActionUser()); 
			// 退单总金额
			createOrderReturn.setReturnTotalFee(createReturnVO.getReturnMoney());
			// 退商品金额
			createOrderReturn.setReturnGoodsMoney(returnGoodsMoney);
			// 退运费
			createOrderReturn.setReturnShipping(createReturnVO.getReturnShipping());
			orderReturnBean.setCreateOrderReturn(createOrderReturn);
			
			//退款 - 付款金额默认
			List<CreateOrderRefund> returnPayList = processReturnPayList(createReturnVO.getMasterOrderSn(), orderInfo.getTransType().intValue(), createReturnVO.getReturnMoney());
			orderReturnBean.setCreateOrderRefundList(returnPayList);
		
			// 公共方法调用
			orderReturnBean.setActionNote(sourceName);
			orderReturnBean.setPoints(createReturnVO.getPoints());
			orderReturnBean.setBvValue(createReturnVO.getBvValue());
			orderReturnBean.setBaseBvValue(createReturnVO.getBaseBvValue());
			
			CreateOrderReturnShip createOrderReturnShip = new CreateOrderReturnShip();
			createOrderReturnShip.setDepotCode(Constant.DETAILS_DEPOT_CODE);
			orderReturnBean.setCreateOrderReturnShip(createOrderReturnShip);
			orderReturnMessage = createOrderReturnAllIn(orderReturnBean);
			
			//作废订单下其他退款单信息 - 取消发货时 处理
			if((StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP, createReturnVO.getReturnSource())
					||StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_CANCEL, createReturnVO.getReturnSource()))
							&& orderReturnMessage.getIsOk() > 0
							&& CollectionUtils.isNotEmpty(haveReturnReturnSnList)){
				
				OrderReturnExample orderReturnExample = new OrderReturnExample();
				orderReturnExample.or().andReturnSnIn(haveReturnReturnSnList);
				OrderReturn updateOrderReturn = new OrderReturn();
				updateOrderReturn.setReturnOrderStatus(ConstantValues.ORDERRETURN_STATUS.INVALIDITY);
				updateOrderReturn.setUpdateTime(new Date());
				int num = orderReturnMapper.updateByExampleSelective(updateOrderReturn, orderReturnExample);
				if (num > 0) {
					List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(orderReturnExample);
					if(CollectionUtils.isNotEmpty(orderReturnList)){
						for (OrderReturn or : orderReturnList) {
							if(StringUtils.equalsIgnoreCase(or.getRelatingOrderSn(), or.getMasterOrderSn())){
								masterOrderActionService.insertOrderActionBySn(or.getMasterOrderSn(), createReturnVO.getMasterOrderSn() + "作废当前退单，生成总退单("+returnSn+")", createReturnVO.getActionUser());
							}else{
								orderActionService.addOrderReturnAction(or.getReturnSn(),
										createReturnVO.getMasterOrderSn() + "作废当前退单，生成总退单("+returnSn+")", createReturnVO.getActionUser());
							}
						}
					}
				}
			}
			String allActionNote = orderReturnMessage.getMessage();
			logger.info("createOrderReturnPay.orderSn:"+createReturnVO.getMasterOrderSn()+",orderReturnMessage:"+JSON.toJSONString(orderReturnMessage)+",createOrderReturn:"+JSON.toJSONString(createOrderReturn));
			if(orderReturnMessage.getIsOk() > 0 && orderInfo.getOrderType().intValue() != ConstantValues.ORDERRETURN_RELATIN_ORDER_TYPE.EXCHANGE.intValue()){
				//取消发货调用时，  退单创建成功后  , 自动确认
				if(createOrderReturn.getRefundType().intValue() == ConstantValues.ORDERRETURN_REFUND_TYPE.CANCEL_ORDER.intValue()
						|| createOrderReturn.getRefundType().intValue() == ConstantValues.ORDERRETURN_REFUND_TYPE.SHIP_ORDER.intValue()){
					ReturnInfo<String> confirmResult = orderReturnStService.returnOrderConfirm(returnSn, "退单创建自动确认", ConstantValues.ACTION_USER_SYSTEM);
					logger.info("createOrderReturnPay.orderSn:"+createReturnVO.getMasterOrderSn()+",confirmResult:"+JSON.toJSONString(confirmResult));
					if(confirmResult.getIsOk() > 0){
						allActionNote += "，自动确认成功。";
					}else{
						allActionNote += "，自动确认失败，" + confirmResult.getMessage();
					}
				}
			}
			
			orderReturnMessage.setIsOk(Constant.YESORNO_YES);
			orderReturnMessage.setMessage(allActionNote);
		} catch (Exception e) {
			logger.error("createOrderReturnPay.orderSn:"+orderReturnMessage.getOrderSn()+",occorderror:" + e.getMessage(),e);
			orderReturnMessage.setIsOk(Constant.YESORNO_NO);
			orderReturnMessage.setMessage("退款单创建失败，" + e.getMessage());
		}

		return orderReturnMessage;
	}

	/**
	 * 退单单-付款单平台总退单金额，默认在线支付
	 * @param masterOrderSn
     * @param transType
	 * @param totalReturnMoney //退款总金额
	 */
	private List<CreateOrderRefund> processReturnPayList(String masterOrderSn, Integer transType, Double totalReturnMoney){
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		if(Constant.OI_TRANS_TYPE_PRESHIP == transType.intValue()){
			//货到付款
			masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		}else{
			masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusEqualTo(ConstantValues.OP_ORDER_PAY_STATUS.PAYED.byteValue());
		}
		List<MasterOrderPay> orderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
		List<CreateOrderRefund> createOrderRefundList = new ArrayList<CreateOrderRefund>();
		
		if(CollectionUtils.isEmpty(orderPayList)){
			throw new RuntimeException("原订单付款单信息不存在！");
		}
		
		if(orderPayList.size() == 1){//只有一种支付方式
			CreateOrderRefund createOrderRefund = new CreateOrderRefund();
			createOrderRefund.setReturnPay(orderPayList.get(0).getPayId().shortValue());
			createOrderRefund.setReturnFee(totalReturnMoney);
			createOrderRefundList.add(createOrderRefund);
		}else{//多种支付方式
			Map<Byte,Double> limitOrderPay = new HashMap<Byte, Double>();
			boolean existSurplusPay = false;//是否存在余额支付
			Byte surplusPay = Byte.valueOf("3");//余额支付
			for (MasterOrderPay masterOrderPay : orderPayList) {
				if(!limitOrderPay.containsKey(masterOrderPay.getPayId())){
					limitOrderPay.put(masterOrderPay.getPayId(), masterOrderPay.getPayTotalfee().doubleValue());
				}else{
					limitOrderPay.put(masterOrderPay.getPayId(), limitOrderPay.get(masterOrderPay.getPayId()).doubleValue() + masterOrderPay.getPayTotalfee().doubleValue());
				}
			}
			
			if(limitOrderPay.containsKey(surplusPay)){
				//存在余额支付
				limitOrderPay.remove(surplusPay);
				existSurplusPay = true;
			}
				
			//平摊非余额支付
			Set<Byte> payIdSet = limitOrderPay.keySet();
			for (Byte payId : payIdSet) {
				if(totalReturnMoney <= 0){
					break;
				}
				//按照支付方式进行平摊
				double returnMoney = 0D;
				if(totalReturnMoney > limitOrderPay.get(payId)){
					returnMoney = limitOrderPay.get(payId);
					totalReturnMoney = totalReturnMoney - returnMoney;
				}else{
					returnMoney = totalReturnMoney;
					totalReturnMoney = 0D;
				}
				CreateOrderRefund createOrderRefund = new CreateOrderRefund();
				createOrderRefund.setReturnFee(returnMoney);
				createOrderRefund.setReturnPay(payId.shortValue());
				createOrderRefundList.add(createOrderRefund);
			}
			if(existSurplusPay && totalReturnMoney > 0){
				CreateOrderRefund createOrderRefund = new CreateOrderRefund();
				createOrderRefund.setReturnFee(totalReturnMoney);
				createOrderRefund.setReturnPay(surplusPay.shortValue());
				createOrderRefundList.add(createOrderRefund);
			}
			
		}
		
	  /*  Byte surplusPay = Byte.valueOf("3");//余额支付
		boolean existSurplusPay = false;//是否存在余额支付
		Map<Byte,Double> limitOrderPay = new HashMap<Byte, Double>();
		if(CollectionUtils.isNotEmpty(orderPayList)){
			for (OrderPay orderPay : orderPayList) {
				double totalPayFee = 0D;
				if(orderPay.getPayId().byteValue() == surplusPay.byteValue()){
					totalPayFee = orderPay.getSurplus().doubleValue();
				}else{
					totalPayFee = orderPay.getPayTotalfee().doubleValue();
				}
				if(!limitOrderPay.containsKey(orderPay.getPayId())){
					limitOrderPay.put(orderPay.getPayId(), totalPayFee);
				}else{
					limitOrderPay.put(orderPay.getPayId(), limitOrderPay.get(orderPay.getPayId()).doubleValue() + totalPayFee);
				}
				//非余额支付的付款单中，存在余额
				if(orderPay.getPayId().byteValue() != surplusPay.byteValue() && orderPay.getSurplus().doubleValue() > 0){
					if(!limitOrderPay.containsKey(surplusPay)){
						limitOrderPay.put(surplusPay, orderPay.getSurplus().doubleValue());
					}else{
						limitOrderPay.put(surplusPay, limitOrderPay.get(surplusPay).doubleValue() + orderPay.getSurplus().doubleValue());
					}
				}
			}
		}

		logger.debug("processReturnPayList.orderSn:"+orderSn+",limitOrderPay:"+JSON.toJSONString(limitOrderPay));
		if(limitOrderPay.containsKey(surplusPay)){
			//存在余额支付
			limitOrderPay.remove(surplusPay);
			existSurplusPay = true;
		}
		if(MapUtils.isEmpty(limitOrderPay)){
			//当前只有余额支付
			CreateOrderRefund createOrderRefund = new CreateOrderRefund();
			createOrderRefund.setReturnFee(totalReturnMoney);
			createOrderRefund.setReturnPay(surplusPay.shortValue());
			createOrderRefundList.add(createOrderRefund);
		}else{
			//平摊非余额支付
			Set<Byte> payIdSet = limitOrderPay.keySet();
			for (Byte payId : payIdSet) {
				if(totalReturnMoney <= 0){
					break;
				}
				//按照支付方式进行平摊
				double returnMoney = 0D;
				if(totalReturnMoney > limitOrderPay.get(payId)){
					returnMoney = limitOrderPay.get(payId);
					totalReturnMoney = totalReturnMoney - returnMoney;
				}else{
					returnMoney = totalReturnMoney;
					totalReturnMoney = 0D;
				}
				CreateOrderRefund createOrderRefund = new CreateOrderRefund();
				createOrderRefund.setReturnFee(returnMoney);
				createOrderRefund.setReturnPay(payId.shortValue());
				createOrderRefundList.add(createOrderRefund);
			}
			if(existSurplusPay && totalReturnMoney > 0){
				CreateOrderRefund createOrderRefund = new CreateOrderRefund();
				createOrderRefund.setReturnFee(totalReturnMoney);
				createOrderRefund.setReturnPay(surplusPay.shortValue());
				createOrderRefundList.add(createOrderRefund);
			}
		}*/
		return createOrderRefundList;
	}
	
	/**
	 * 退单商品数据处理
	 * @param orderSn
	 * @param sourceType 退单来源
	 * @param osCreateGoods
	 * @return List<CreateOrderReturnGoods>
	 */
	private List<CreateOrderReturnGoods> processReturnPayGoods(String orderSn,String sourceType,List<String> haveReturnReturnSnList,List<CreateOrderReturnGoods> osCreateGoods){
		
		List<CreateOrderReturnGoods> allCreateGoodsList = new ArrayList<CreateOrderReturnGoods>();
		
		//汇总已经退款的退单的商品数据
		List<CreateOrderReturnGoods> haveReturnGoodsList = new ArrayList<CreateOrderReturnGoods>();
		if(StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP, sourceType)
				||StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_CANCEL, sourceType)){
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andMasterOrderSnEqualTo(orderSn).
			andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY).
			andReturnTypeEqualTo(ConstantValues.ORDERRETURN_TYPE.RETURN_PAY).andRefundTypeIn(Arrays.asList(new Byte[]{ConstantValues.ORDERRETURN_REFUND_TYPE.CANCEL_ORDER,ConstantValues.ORDERRETURN_REFUND_TYPE.SHIP_ORDER}));
			List<OrderReturn> totalRefundReturnList = orderReturnMapper.selectByExample(orderReturnExample);			
			if(CollectionUtils.isNotEmpty(totalRefundReturnList)){
				throw new RuntimeException("当前已存在订单取消发货类型的总退款单("+totalRefundReturnList.get(0).getReturnSn()+")");
			}
			
			//订单取消、发货时，汇总当前已经生成的退款单(有效退款单)
			orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andRelatingOrderSnEqualTo(orderSn).
			andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY).
			andReturnTypeEqualTo(ConstantValues.ORDERRETURN_TYPE.RETURN_PAY).andRefundTypeEqualTo(ConstantValues.ORDERRETURN_REFUND_TYPE.DELETE_GOODS);
			List<OrderReturn> returnPayList = orderReturnMapper.selectByExample(orderReturnExample);
			/*if(CollectionUtils.isEmpty(returnPayList)
					&& StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP, sourceType)){
				throw new RuntimeException("发货订单费用调整，请客服同学对应额外退款单操作.");
			}*/
			if (CollectionUtils.isNotEmpty(returnPayList)) {
				for (OrderReturn orderReturn : returnPayList) {
					haveReturnReturnSnList.add(orderReturn.getReturnSn());
				}
				if (CollectionUtils.isNotEmpty(haveReturnReturnSnList)) {
					OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
					orderReturnGoodsExample.or().andRelatingReturnSnIn(haveReturnReturnSnList);
					List<OrderReturnGoods> orderReturnGoods = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
					
					if (CollectionUtils.isEmpty(orderReturnGoods) && 
							StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP, sourceType)){
						throw new RuntimeException("无法获取订单发货时有效的退款单商品数据集合");				
					}
					if (CollectionUtils.isNotEmpty(orderReturnGoods)) {
						for (OrderReturnGoods goodsItem : orderReturnGoods) {
							CreateOrderReturnGoods createGoods = new CreateOrderReturnGoods();
							createGoods.setCustomCode(goodsItem.getCustomCode());
							createGoods.setExtensionCode(goodsItem.getExtensionCode());
							createGoods.setExtensionId(goodsItem.getExtensionId());
							createGoods.setOsDepotCode(goodsItem.getOsDepotCode());
							createGoods.setGoodsBuyNumber(goodsItem.getGoodsBuyNumber().shortValue());
							createGoods.setGoodsReturnNumber(goodsItem.getGoodsReturnNumber().shortValue());
							createGoods.setGoodsPrice(goodsItem.getGoodsPrice().doubleValue());
							createGoods.setMarketPrice(goodsItem.getMarketPrice().doubleValue());
							createGoods.setShareBonus(goodsItem.getShareBonus().doubleValue());
							createGoods.setSettlementPrice(goodsItem.getSettlementPrice().doubleValue());
							createGoods.setSettlePrice(goodsItem.getSettlementPrice().doubleValue());
                            createGoods.setCostPrice(goodsItem.getCostPrice().doubleValue());
							haveReturnGoodsList.add(createGoods);
						}
					}
				}
				
			}
		}
		
		//取消时，将当前订单中的商品与退单表中商品汇总；发货时，仅仅使用退单表中的商品数据
		if (StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_CANCEL, sourceType)) {
			allCreateGoodsList.addAll(mergeCreateReturnGoods(osCreateGoods, haveReturnGoodsList));
		}/*else if(StringUtils.equalsIgnoreCase(ConstantValues.ORDERRETURN_REFUND_SOURCE.ORDER_ALLSHIP, sourceType)){
			allCreateGoodsList.addAll(haveReturnGoodsList);
		}*/else{
			allCreateGoodsList.addAll(osCreateGoods);
		}
		logger.info("processReturnPayGoods.orderSn:"+orderSn+",sourceType:"+sourceType+",osCreateGoods:"+JSON.toJSONString(osCreateGoods)+",haveReturnGoodsList:"+JSON.toJSONString(haveReturnGoodsList)+",allCreateGoodsList:"+JSON.toJSON(allCreateGoodsList));
		return allCreateGoodsList;
	}
	
	
	private List<CreateOrderReturnGoods> mergeCreateReturnGoods(List<CreateOrderReturnGoods> osCreateGoods,List<CreateOrderReturnGoods> haveReturnGoodsList){
		List<CreateOrderReturnGoods> allProcGoodsList = new ArrayList<CreateOrderReturnGoods>();
		allProcGoodsList.addAll(osCreateGoods);
		// 购买量 = 退货量
		if(CollectionUtils.isNotEmpty(haveReturnGoodsList)){
			for (CreateOrderReturnGoods createOrderReturnGoods : haveReturnGoodsList) {
				createOrderReturnGoods.setGoodsBuyNumber(createOrderReturnGoods.getGoodsReturnNumber());	
			}
			allProcGoodsList.addAll(haveReturnGoodsList);
		}
		Map<String,CreateOrderReturnGoods> allReturnGoodsMap = new HashMap<String, CreateOrderReturnGoods>();
		CreateOrderReturnGoods tempGoods = null;
		if(CollectionUtils.isNotEmpty(allProcGoodsList)){
			for (CreateOrderReturnGoods createOrderReturnGoods : allProcGoodsList) {
				String key = createOrderReturnGoods.getCustomCode()+createOrderReturnGoods.getExtensionCode()+createOrderReturnGoods.getExtensionId()+createOrderReturnGoods.getOsDepotCode();
				
				if(allReturnGoodsMap.containsKey(key)){
					tempGoods = allReturnGoodsMap.get(key);
					tempGoods.setGoodsBuyNumber(Integer.valueOf(tempGoods.getGoodsBuyNumber().intValue() + createOrderReturnGoods.getGoodsBuyNumber().intValue()).shortValue());
					tempGoods.setGoodsReturnNumber(Integer.valueOf(tempGoods.getGoodsReturnNumber().intValue() + createOrderReturnGoods.getGoodsReturnNumber().intValue()).shortValue());
					createOrderReturnGoods = tempGoods;
				}
				allReturnGoodsMap.put(key, createOrderReturnGoods);
			}
		}
		List<CreateOrderReturnGoods> allNewGoodsList = new ArrayList<CreateOrderReturnGoods>();
		for (CreateOrderReturnGoods createOrderReturnGoods : allReturnGoodsMap.values()) {
			allNewGoodsList.add(createOrderReturnGoods);
		}
		return allNewGoodsList;
	}
	/*
	 * 生成退单内部方法
	 * 
	 * @param param
	 * 
	 * @return
	 */
	private ReturnInfo<String> createOrderReturnAllIn(CreateOrderReturnBean param) {
		ReturnInfo<String> orm = new ReturnInfo<String>();
		orm.setOrderSn(param.getRelatingOrderSn());
		orm.setReturnSn(param.getOrderReturnSn());
		logger.info("createOrderReturnAllIn relOrderSn:"+param.getRelatingOrderSn()+",param:"+ JSON.toJSONString(param));
		String relatingOrderSn = param.getRelatingOrderSn();
		String returnSn = param.getOrderReturnSn();
		OrderReturn ormOrderReturn = null;
		List<OrderRefund> ormOrderRefundList = null;
		OrderReturnShip ormOrderReturnShip = null;
		List<OrderReturnGoods> ormOrderReturnGoodsList = null;
		// 未发货取消订单是退款
		// 退单类型：1退货单、2拒收入库单、3退款单(取消订单,删除商品等) 4额外退款单
		if (param.getReturnType().intValue() == 3 || param.getReturnType().intValue() == 1 || param.getReturnType().intValue() == 4) {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(relatingOrderSn);
			// 处理orderReturn
			ormOrderReturn = processOrderReturnByMaster(param, master);
			logger.info("processOrderReturn->orderSn:"+param.getRelatingOrderSn()+",orderReturn:"+ JSON.toJSONString(ormOrderReturn));
			// 处理orderRefund 退款单
			ormOrderRefundList = processOrderRefundByMaster(param, master);
			logger.info("processOrderRefund->orderSn:"+param.getRelatingOrderSn()+",OrderRefund:"+ JSON.toJSONString(ormOrderRefundList));

			// 处理orderReturnShip
			ormOrderReturnShip = processOrderReturnShipByMaster(param, master);
			logger.info("processOrderReturnShip->orderSn:"+param.getRelatingOrderSn()+",ormOrderReturnShip:"+ JSON.toJSONString(ormOrderReturnShip));
			ormOrderReturn.setReturnType(param.getReturnType().byteValue());
			// 处理orderReturnGoods
			ormOrderReturnGoodsList = processOrderReturnGoodsByMaster(param, master);
		} else {
			OrderDistribute oInfo = orderDistributeMapper.selectByPrimaryKey(relatingOrderSn);
			if(param.getReturnType().intValue() < 3){
				if(oInfo == null){
					throw new RuntimeException("退单关联订单号("+relatingOrderSn+")不存在");
				}
			}
			// 处理orderReturn
			ormOrderReturn = processOrderReturn(param, oInfo);
			ormOrderReturn.setRefundType(ConstantValues.ORDERRETURN_REFUND_TYPE.DELETE_GOODS);
			logger.info("processOrderReturn->orderSn:"+param.getRelatingOrderSn()+",orderReturn:"+ JSON.toJSONString(ormOrderReturn));

			// 处理orderRefund
			ormOrderRefundList = processOrderRefund(param, oInfo);
			logger.info("processOrderRefund->orderSn:"+param.getRelatingOrderSn()+",OrderRefund:"+ JSON.toJSONString(ormOrderRefundList));

			// 处理orderReturnShip
			ormOrderReturnShip = processOrderReturnShip(param,oInfo);
			logger.info("processOrderReturnShip->orderSn:"+param.getRelatingOrderSn()+",ormOrderReturnShip:"+ JSON.toJSONString(ormOrderReturnShip));

			// 处理orderReturnGoods
			ormOrderReturnGoodsList = processOrderReturnGoods(param, oInfo);
		}
		if (CollectionUtils.isEmpty(ormOrderReturnGoodsList)) {
			throw new RuntimeException("匹配退货商品数据为空");
		}
		logger.info("processOrderReturnGoods->orderSn:"+param.getRelatingOrderSn()+",ormOrderReturnGoodsList:"+ JSON.toJSONString(ormOrderReturnGoodsList));
		ormOrderReturn.setBvValue(param.getBvValue());
		ormOrderReturn.setBaseBvValue(param.getBaseBvValue());
		ormOrderReturn.setAddTime(new Date());//取消退单添加退单时间
		// 持久化到数据库
		orderReturnMapper.insertSelective(ormOrderReturn); // order_return
		if (CollectionUtils.isNotEmpty(ormOrderReturnGoodsList)) {
			for (OrderReturnGoods orderReturnGoods : ormOrderReturnGoodsList) {
				try {
					orderReturnGoodsMapper.insertSelective(orderReturnGoods);
				} catch (Exception e) {
					logger.error("退单商品写入异常" + e.getMessage(), e);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(ormOrderRefundList)) {
			for (OrderRefund orf : ormOrderRefundList) {
				orderRefundMapper.insertSelective(orf); // order_refund
			}
		}
		orderReturnShipMapper.insertSelective(ormOrderReturnShip);
		if(StringUtils.isBlank(ormOrderReturn.getRelatingOrderSn())){
			masterOrderActionService.insertOrderActionBySn(ormOrderReturn.getMasterOrderSn(), "关联新退单成功:" + returnSn, param.getActionUser());
		}
		/*else{
			distributeActionService.addOrderAction(param.getRelatingOrderSn(),"关联新退单成功:" + returnSn, param.getActionUser());
		}*/
		// 插入新退单Action
		orderActionService.addOrderReturnAction(param.getOrderReturnSn(),"退单生成成功:"+StringUtils.trimToEmpty(param.getActionNote()), param.getActionUser(), 2);

		//当退单物流中有承运商和快递单号时，添加物流监控
		if(StringUtils.isNotBlank(ormOrderReturnShip.getReturnExpress()) && StringUtils.isNotBlank(ormOrderReturnShip.getReturnInvoiceNo())){
			logger.info("退单保存添加物流监控：returnSn:"+ormOrderReturn.getReturnSn()+",orderReturn:"+JSON.toJSONString(ormOrderReturn)+",orderReturnShip:"+JSON.toJSONString(ormOrderReturnShip));
//			orderExpressPullService.orderReturnExpress(ormOrderReturn, ormOrderReturnShip);
		}
		orm.setIsOk(Constant.YESORNO_YES);
		logger.info("createOrderReturnAllIn end:" + JSON.toJSONString(orm));
		return orm;
	}

	/**
	 * 转换扩充原始OrderReturn实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param originalOrderReturn
	 * @param oInfo
	 * @param returnType
	 * @return
	 */
	private OrderReturn processOrderReturnByMaster(CreateOrderReturnBean param, MasterOrderInfo oInfo) {
		OrderReturn orderReturn = new OrderReturn();
		orderReturn.setReturnSn(param.getOrderReturnSn()); // 退单编号
		if (oInfo != null) {//为子单生成退单
			orderReturn.setRelatingOrderSn(param.getRelatingOrderSn());
		}
		orderReturn.setReturnType(param.getReturnType().byteValue());
		//退款单：1删除商品、2取消 、 3发货
		if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY.intValue()){
			orderReturn.setRefundType(param.getCreateOrderReturn().getRefundType().byteValue());
		}
		orderReturn.setReturnReason(param.getCreateOrderReturn().getReturnReason());
		orderReturn.setReturnDesc(param.getCreateOrderReturn().getReturnDesc());
		orderReturn.setHaveRefund(param.getCreateOrderReturn().getHaveRefund().byteValue());
		orderReturn.setPayStatus((byte) Constant.OR_PAY_STATUS_UNSETTLED);// 待结算
		
		orderReturn.setNewOrderSn(param.getCreateOrderReturn().getNewOrderSn());
		orderReturn.setProcessType(param.getCreateOrderReturn().getProcessType());
		orderReturn.setReturnSettlementType(param.getCreateOrderReturn().getReturnSettlementType());
		orderReturn.setReturnReason(param.getCreateOrderReturn().getReturnReason());
		orderReturn.setReturnDesc(param.getCreateOrderReturn().getReturnDesc());
		orderReturn.setTimeoutStatus(Constant.OR_TIMEOUT_STATUS_NORMAL);
		orderReturn.setBvValue(oInfo.getBvValue());
		orderReturn.setBaseBvValue(oInfo.getBaseBvValue());
		orderReturn.setPoints(oInfo.getPoints());
		if (null != oInfo) {
			orderReturn.setRelatingOrderType(oInfo.getOrderType()); // 关联订单类型
			orderReturn.setChannelCode(oInfo.getOrderFrom());
			orderReturn.setMasterOrderSn(oInfo.getMasterOrderSn());
			orderReturn.setUserId(oInfo.getUserId()); // 下单人
			orderReturn.setSiteCode(oInfo.getChannelCode());
			orderReturn.setShopName(oInfo.getShopName());
			MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(oInfo.getMasterOrderSn());
			orderReturn.setStoreCode(extend.getShopCode());
			orderReturn.setStoreName(extend.getShopName());
		} else {
			//主单生成退单
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(param.getRelatingOrderSn());
			orderReturn.setRelatingOrderType(masterOrderInfo.getOrderType()); // 关联订单类型
			orderReturn.setChannelCode(masterOrderInfo.getOrderFrom());
			orderReturn.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
			orderReturn.setUserId(masterOrderInfo.getUserId()); // 下单人
			orderReturn.setSiteCode(masterOrderInfo.getChannelCode());
			orderReturn.setShopName(masterOrderInfo.getShopName());
		}
		
		Integer totalGoodsReturnNumber = 0;
		Integer totalPriceDifferNum = 0;
		if (param.getCreateOrderReturnGoodsList() != null) {
			for (CreateOrderReturnGoods org : param.getCreateOrderReturnGoodsList()) {
				totalGoodsReturnNumber += (org.getGoodsReturnNumber() == null ? 0
						: org.getGoodsReturnNumber());
				totalPriceDifferNum += (org.getPriceDifferNum() == null ? 0
						: org.getPriceDifferNum());
			}
		}
		orderReturn.setReturnAllgoodsCount(totalGoodsReturnNumber); // 退单商品总数量// 且会重新计算
		orderReturn.setPricedifferGoodsTotal(totalPriceDifferNum); // 退差价商品总数量// 且会重新计算
		orderReturn.setReturnTotalFee(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnTotalFee())); //退款总金额
		orderReturn.setTotalPriceDifference(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getTotalPriceDifference()));//退商品总差价
		orderReturn.setReturnGoodsMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnGoodsMoney()));//退商品金额
		orderReturn.setReturnBonusMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnBonusMoney()));//退红包金额
		//payment 支付商家费用
		//return_integral_money 退积分
		orderReturn.setReturnShipping(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnShipping()));//退运费
		orderReturn.setReturnTax(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnTax()));//退发票税费
		orderReturn.setReturnPackMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnPackMoney()));//退包装费
		orderReturn.setReturnCard(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnCard()));//退贺卡费
		orderReturn.setReturnOtherMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnOtherMoney()));//退其它费用
		orderReturn.setIntegralMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getTotalIntegralMoney()));//积分使用金额
		
		orderReturn.setBackToCs(0);
		orderReturn.setAddTime(param.getAddTime());
		orderReturn.setUpdateTime(param.getAddTime());
		orderReturn.setActionUser(" ");
        orderReturn.setRelatingChangeSn(param.getCreateOrderReturn().getRelatingChangeSn());
        orderReturn.setOrderSn(param.getCreateOrderReturn().getOrderSn());
		return orderReturn;
	}
	
	
	/**
	 * 转换扩充原始OrderReturn实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param originalOrderReturn
	 * @param oInfo
	 * @param returnType
	 * @return
	 */
	private OrderReturn processOrderReturn(CreateOrderReturnBean param,
			OrderDistribute oInfo) {
		OrderReturn orderReturn = new OrderReturn();
		orderReturn.setReturnSn(param.getOrderReturnSn()); // 退单编号
		if(oInfo != null){//为子单生成退单
			orderReturn.setRelatingOrderSn(param.getRelatingOrderSn());
		}
		orderReturn.setReturnType(param.getReturnType().byteValue());
		//退款单：1删除商品、2取消 、 3发货
		if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY.intValue()){
			orderReturn.setRefundType(param.getCreateOrderReturn().getRefundType().byteValue());
		}
		orderReturn.setReturnReason(param.getCreateOrderReturn().getReturnReason());
		orderReturn.setReturnDesc(param.getCreateOrderReturn().getReturnDesc());
		orderReturn.setHaveRefund(param.getCreateOrderReturn().getHaveRefund().byteValue());
		orderReturn.setPayStatus((byte) Constant.OR_PAY_STATUS_UNSETTLED);// 待结算
		
		orderReturn.setNewOrderSn(param.getCreateOrderReturn().getNewOrderSn());
		orderReturn.setProcessType(param.getCreateOrderReturn().getProcessType());
		orderReturn.setReturnSettlementType(param.getCreateOrderReturn().getReturnSettlementType());
		orderReturn.setReturnReason(param.getCreateOrderReturn().getReturnReason());
		orderReturn.setReturnDesc(param.getCreateOrderReturn().getReturnDesc());
		orderReturn.setTimeoutStatus(Constant.OR_TIMEOUT_STATUS_NORMAL);
		if(null != oInfo){
			orderReturn.setRelatingOrderType(oInfo.getOrderType()); // 关联订单类型
			orderReturn.setChannelCode(oInfo.getOrderFrom());
			orderReturn.setMasterOrderSn(oInfo.getMasterOrderSn());
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(oInfo.getMasterOrderSn());
			orderReturn.setUserId(masterOrderInfo.getUserId()); // 下单人
			orderReturn.setSiteCode(masterOrderInfo.getChannelCode());
			orderReturn.setShopName(masterOrderInfo.getShopName());
			orderReturn.setPoints(oInfo.getPoints());
			orderReturn.setBvValue(oInfo.getBvValue());
			orderReturn.setBaseBvValue(masterOrderInfo.getBaseBvValue());
			MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(oInfo.getMasterOrderSn());
			orderReturn.setStoreCode(extend.getShopCode());
			orderReturn.setStoreName(extend.getShopName());
		}else{
			//主单生成退单
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(param.getRelatingOrderSn());
			orderReturn.setRelatingOrderType(masterOrderInfo.getOrderType()); // 关联订单类型
			orderReturn.setChannelCode(masterOrderInfo.getOrderFrom());
			orderReturn.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
			orderReturn.setUserId(masterOrderInfo.getUserId()); // 下单人
			orderReturn.setSiteCode(masterOrderInfo.getChannelCode());
			orderReturn.setShopName(masterOrderInfo.getShopName());
			orderReturn.setPoints(BigDecimal.valueOf(param.getPoints()));
			orderReturn.setBvValue(param.getBvValue());
			orderReturn.setBaseBvValue(masterOrderInfo.getBaseBvValue());
			MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(param.getRelatingOrderSn());
			orderReturn.setStoreCode(extend.getShopCode());
			orderReturn.setStoreName(extend.getShopName());
		}
		
		Integer totalGoodsReturnNumber = 0;
		Integer totalPriceDifferNum = 0;
		if (param.getCreateOrderReturnGoodsList() != null) {
			for (CreateOrderReturnGoods org : param.getCreateOrderReturnGoodsList()) {
				totalGoodsReturnNumber += (org.getGoodsReturnNumber() == null ? 0
						: org.getGoodsReturnNumber());
				totalPriceDifferNum += (org.getPriceDifferNum() == null ? 0
						: org.getPriceDifferNum());
			}
		}
		orderReturn.setReturnAllgoodsCount(totalGoodsReturnNumber); // 退单商品总数量// 且会重新计算
		orderReturn.setPricedifferGoodsTotal(totalPriceDifferNum); // 退差价商品总数量// 且会重新计算
		orderReturn.setReturnTotalFee(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnTotalFee())); //退款总金额
		orderReturn.setTotalPriceDifference(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getTotalPriceDifference()));//退商品总差价
		orderReturn.setReturnGoodsMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnGoodsMoney()));//退商品金额
		orderReturn.setReturnBonusMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnBonusMoney()));//退红包金额
		//payment 支付商家费用
		//return_integral_money 退积分
		orderReturn.setReturnShipping(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnShipping()));//退运费
		orderReturn.setReturnTax(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnTax()));//退发票税费
		orderReturn.setReturnPackMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnPackMoney()));//退包装费
		orderReturn.setReturnCard(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnCard()));//退贺卡费
		orderReturn.setReturnOtherMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getReturnOtherMoney()));//退其它费用
		orderReturn.setIntegralMoney(ConstantsUtil.obj2Bigdecimal(param.getCreateOrderReturn().getTotalIntegralMoney()));//积分使用金额
		
		orderReturn.setBackToCs(0);
		orderReturn.setAddTime(param.getAddTime());
		orderReturn.setUpdateTime(param.getAddTime());
		orderReturn.setActionUser(" ");
		return orderReturn;
	}

	/**
	 * 转换扩充原始OrderRefund实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 * 退单Refund几条记录由原订单的付款方式种类决定同时，具体退款单金额由退单主表的退款总金额return_total_fee决定
	 */
	private List<OrderRefund> processOrderRefundByMaster(CreateOrderReturnBean param, MasterOrderInfo oInfo) {
		List<CreateOrderRefund> createOrderRefundList = param.getCreateOrderRefundList();
		if (CollectionUtils.isEmpty(createOrderRefundList)) {
			throw new RuntimeException("退款数据为空");
		}
		int count = 0 ;
		List<OrderRefund> orderReturnList = new ArrayList<OrderRefund>();
		for (CreateOrderRefund createOrderRefund : createOrderRefundList) {
			OrderRefund orderRefund = new OrderRefund();
			// 退款单号
			orderRefund.setReturnPaySn(OrderUtils.generateRefundSn(param.getOrderReturnSn(), count));
			// 关联退单号
			orderRefund.setRelatingReturnSn(param.getOrderReturnSn()); 
			// 0 未结算
			orderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);
			// 退款方式
			orderRefund.setReturnPay(createOrderRefund.getReturnPay());
			// 退款金额
			orderRefund.setReturnFee(ConstantsUtil.obj2Bigdecimal(createOrderRefund.getReturnFee()));
			
			orderRefund.setAddTime(param.getAddTime());
			orderRefund.setUpdateTime(param.getAddTime());
			orderReturnList.add(orderRefund);
			count ++;
		}
		return orderReturnList;
	}
	
	/**
	 * 转换扩充原始OrderRefund实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 * 退单Refund几条记录由原订单的付款方式种类决定同时，具体退款单金额由退单主表的退款总金额return_total_fee决定
	 */
	private List<OrderRefund> processOrderRefund(CreateOrderReturnBean param,
			OrderDistribute oInfo) {
		List<CreateOrderRefund> createOrderRefundList = param.getCreateOrderRefundList();
		if(CollectionUtils.isEmpty(createOrderRefundList)){
			throw new RuntimeException("退款数据为空");
		}
		int count = 0 ;
		List<OrderRefund> orderReturnList = new ArrayList<OrderRefund>();
		for (CreateOrderRefund createOrderRefund : createOrderRefundList) {
			OrderRefund orderRefund = new OrderRefund();
			orderRefund.setReturnPaySn(OrderUtils.generateRefundSn(param.getOrderReturnSn(),count));
			orderRefund.setRelatingReturnSn(param.getOrderReturnSn());
			orderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.UNSETTLED);

			orderRefund.setReturnPay(createOrderRefund.getReturnPay());
			orderRefund.setReturnFee(ConstantsUtil.obj2Bigdecimal(createOrderRefund.getReturnFee()));
			
			orderRefund.setAddTime(param.getAddTime());
			orderRefund.setUpdateTime(param.getAddTime());
			orderReturnList.add(orderRefund);
			count ++;
		}
		return orderReturnList;
	}
	
	/**
	 * OrderReturnShip转换
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 */
	private OrderReturnShip processOrderReturnShipByMaster(CreateOrderReturnBean param, MasterOrderInfo oInfo) {
		OrderReturnShip orderReturnShip = new OrderReturnShip();
		// 退单编号
		orderReturnShip.setRelatingReturnSn(param.getOrderReturnSn());
		if (param.getCreateOrderReturnShip() != null) {
			// 退货快递公司编码
			orderReturnShip.setReturnExpress(param.getCreateOrderReturnShip().getReturnExpress());
			// 退换货物流信息图片
			orderReturnShip.setReturnExpressImg(param.getCreateOrderReturnShip().getReturnExpressImg());
			// 退货快递单号
			orderReturnShip.setReturnInvoiceNo(param.getCreateOrderReturnShip().getReturnInvoiceNo());
			// 退货仓库
			orderReturnShip.setDepotCode(param.getCreateOrderReturnShip().getDepotCode());
		}
		if (StringUtil.isTrimEmpty(orderReturnShip.getDepotCode())) {
			if (StringUtil.isListNotNull(param.getCreateOrderReturnGoodsList())) {
				orderReturnShip.setDepotCode(param.getCreateOrderReturnGoodsList().get(0).getOsDepotCode());
			} else {
				orderReturnShip.setDepotCode(Constant.DETAILS_DEPOT_CODE);
			}
		}
		orderReturnShip.setAddTime(new Date());
		orderReturnShip.setUpdateTime(new Date());
		return orderReturnShip;
	}

	/**
	 * OrderReturnShip转换
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 */
	private OrderReturnShip processOrderReturnShip(CreateOrderReturnBean param,
			OrderDistribute oInfo) {
		OrderReturnShip orderReturnShip = new OrderReturnShip();
		orderReturnShip.setRelatingReturnSn(param.getOrderReturnSn());
		if(param.getCreateOrderReturnShip() != null){
			orderReturnShip.setReturnExpress(param.getCreateOrderReturnShip().getReturnExpress());
			orderReturnShip.setReturnExpressImg(param.getCreateOrderReturnShip().getReturnExpressImg());
			orderReturnShip.setReturnInvoiceNo(param.getCreateOrderReturnShip().getReturnInvoiceNo());
			orderReturnShip.setDepotCode(param.getCreateOrderReturnShip().getDepotCode());
		}
		orderReturnShip.setAddTime(new Date());
		orderReturnShip.setUpdateTime(new Date());
		return orderReturnShip;
	}

	/**
	 * 转换扩充原始OrderReturnGoods实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 */
	private List<OrderReturnGoods> processOrderReturnGoodsByMaster(CreateOrderReturnBean param, MasterOrderInfo oInfo) {
		
		List<CreateOrderReturnGoods> goodsList = param.getCreateOrderReturnGoodsList();
		List<OrderReturnGoods> orderReturnGoodsList = new ArrayList<OrderReturnGoods>();
		int bvValue = 0;
		int baseBvValue = 0;
		if (CollectionUtils.isNotEmpty(goodsList)) {
			for (CreateOrderReturnGoods createGoods : goodsList) {
				String depotCode = Constant.DETAILS_DEPOT_CODE.equals(createGoods.getOsDepotCode())
						? Constant.DETAILS_DEPOT_CODE : createGoods.getOsDepotCode();
				if (createGoods.getBvValue() != null) {
					bvValue += Integer.valueOf(createGoods.getBvValue());
				}
				if (createGoods.getBaseBvValue() != null) {
					baseBvValue += createGoods.getBaseBvValue();
				}
				OrderReturnGoods orderReturnGoods = new OrderReturnGoods();
				orderReturnGoods.setRelatingReturnSn(param.getOrderReturnSn());
				orderReturnGoods.setGoodsSn(createGoods.getCustomCode().substring(0, 6));
				orderReturnGoods.setSeller(createGoods.getSeller());//供销商编码
				if (param.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()) {
					orderReturnGoods.setIntegralMoney(ConstantsUtil.obj2Bigdecimal(createGoods.getIntegralMoney()));//积分使用金额
				}
				
				if (null != createGoods.getCustomCode()) {
					String goodsSn = createGoods.getCustomCode().substring(0, 6);
					/*ProductGoodsWithBLOBs productGoods = productGoodsMapper.selectByPrimaryKey(goodsSn);
					if(null != productGoods){
						orderReturnGoods.setGoodsName(productGoods.getGoodsName());
					}else {
						orderReturnGoods.setGoodsName(null);
					}*/
				}
				orderReturnGoods.setGoodsSizeName(createGoods.getSizeName());
				orderReturnGoods.setGoodsColorName(createGoods.getColorName());
				orderReturnGoods.setGoodsName(createGoods.getGoodsName());
				orderReturnGoods.setGoodsSn(createGoods.getGoodsSn());
				orderReturnGoods.setGoodsPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getGoodsPrice()));
				orderReturnGoods.setMarketPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getMarketPrice()));
				orderReturnGoods.setShareBonus(ConstantsUtil.obj2Bigdecimal(createGoods.getShareBonus()));
				
				//新数据带入
				orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
				orderReturnGoods.setRelatingReturnSn(param.getOrderReturnSn());
				orderReturnGoods.setCustomCode(createGoods.getCustomCode());
				orderReturnGoods.setExtensionCode(createGoods.getExtensionCode());
				orderReturnGoods.setExtensionId(createGoods.getExtensionId());
				orderReturnGoods.setOsDepotCode(depotCode);
				orderReturnGoods.setGoodsThumb(createGoods.getGoodsThumb());//图片url
				orderReturnGoods.setPayPoints(ConstantsUtil.obj2Bigdecimal(createGoods.getPayPoints()));//消费积分
				orderReturnGoods.setSeller(createGoods.getSeller());
				orderReturnGoods.setSap(createGoods.getSap());
				orderReturnGoods.setBvValue(createGoods.getBvValue() == null ? "0" : createGoods.getBvValue() + "");
				orderReturnGoods.setBaseBvValue(createGoods.getBaseBvValue());
				if (oInfo != null) {
					orderReturnGoods.setMasterOrderSn(oInfo.getMasterOrderSn());//主单号
					orderReturnGoods.setOrderSn(null);//子单号
				} else {
					orderReturnGoods.setMasterOrderSn(param.getRelatingOrderSn());//主单号
				}
				orderReturnGoods.setSalesMode(createGoods.getSalesMode().byteValue());//商品销售模式：1为自营，2为买断，3为寄售，4为直发
				
				if(param.getReturnType().intValue() == Constant.OR_RETURN_TYPE_RETURN
						|| param.getReturnType() == Constant.OR_RETURN_TYPE_REJECT){
					//退货单、拒收入库
					orderReturnGoods.setGoodsReturnNumber(createGoods.getGoodsReturnNumber());
					orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
					orderReturnGoods.setChargeBackCount(createGoods.getChargeBackCount());
					orderReturnGoods.setHaveReturnCount(createGoods.getHaveReturnCount());
					orderReturnGoods.setSettlementPrice(BigDecimal.valueOf(createGoods.getSettlementPrice()));
					orderReturnGoods.setReturnReason(createGoods.getReturnReason());
					orderReturnGoods.setDiscount(createGoods.getDiscount().floatValue());
				} else if (param.getReturnType().intValue() == Constant.OR_RETURN_TYPE_REFUND) {
					//退款单
					orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
					orderReturnGoods.setGoodsReturnNumber(createGoods.getGoodsReturnNumber());
					orderReturnGoods.setShareSettle(ConstantsUtil.obj2Bigdecimal(createGoods.getShareSettle()));
					orderReturnGoods.setSettlementPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getShareSettle()));
				} else {
					//额外退款单
					orderReturnGoods.setSettlementPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getSettlementPrice()));
					orderReturnGoods.setPriceDifference(ConstantsUtil.obj2Bigdecimal(createGoods.getPriceDifference()));
					orderReturnGoods.setPriceDifferNum(createGoods.getPriceDifferNum());
					orderReturnGoods.setPriceDifferReason(createGoods.getReturnReason());
				}
				orderReturnGoods.setGoodsThumb(createGoods.getGoodsThumb());
				orderReturnGoods.setBoxGauge(createGoods.getBoxGauge());
                orderReturnGoods.setCostPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getCostPrice()));
				orderReturnGoodsList.add(orderReturnGoods);
			}
		}
		param.setBvValue(bvValue);
		param.setBaseBvValue(baseBvValue);
		return orderReturnGoodsList;
	}
	
	/**
	 * 转换扩充原始OrderReturnGoods实体 得到可直接持久化到数据库的Bean
	 * 
	 * @param param
	 * @param oInfo
	 * @return
	 */
	private List<OrderReturnGoods> processOrderReturnGoods(CreateOrderReturnBean param, OrderDistribute oInfo) {
		List<CreateOrderReturnGoods> goooooodsList = param.getCreateOrderReturnGoodsList();
		List<OrderReturnGoods> orderReturnGoodsList = new ArrayList<OrderReturnGoods>();
		int bvValue = 0;
		int baseBvValue = 0;
		if (CollectionUtils.isNotEmpty(goooooodsList)) {
//			String orderDepotCode = param.getCreateOrderReturnShip().getDepotCode();
			for (CreateOrderReturnGoods createGoods : goooooodsList) {
//				String depotCode = Constant.DETAILS_DEPOT_CODE.equals(orderDepotCode)
//						? createGoods.getOsDepotCode() : orderDepotCode;
				String depotCode = createGoods.getOsDepotCode();
				if (createGoods.getBvValue() != null) {
					bvValue += Integer.valueOf(createGoods.getBvValue());
				}
				if (createGoods.getBaseBvValue() != null) {
					baseBvValue += createGoods.getBaseBvValue();
				}
				OrderReturnGoods orderReturnGoods = new OrderReturnGoods();
				orderReturnGoods.setRelatingReturnSn(param.getOrderReturnSn());
				orderReturnGoods.setGoodsSn(createGoods.getCustomCode().substring(0, 6));
				orderReturnGoods.setSeller(createGoods.getSeller());//供销商编码
				if(param.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()){
					orderReturnGoods.setIntegralMoney(ConstantsUtil.obj2Bigdecimal(createGoods.getIntegralMoney()));//积分使用金额
				}
				orderReturnGoods.setGoodsSizeName(createGoods.getSizeName());
				orderReturnGoods.setGoodsColorName(createGoods.getColorName());
				orderReturnGoods.setGoodsName(createGoods.getGoodsName());
				orderReturnGoods.setGoodsSn(createGoods.getGoodsSn());
				orderReturnGoods.setGoodsPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getGoodsPrice()));
				orderReturnGoods.setMarketPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getMarketPrice()));
				orderReturnGoods.setShareBonus(ConstantsUtil.obj2Bigdecimal(createGoods.getShareBonus()));
				orderReturnGoods.setDiscount(createGoods.getDiscount().floatValue());
                orderReturnGoods.setCostPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getCostPrice()));
				
				//新数据带入
				orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
				orderReturnGoods.setRelatingReturnSn(param.getOrderReturnSn());
				orderReturnGoods.setCustomCode(createGoods.getCustomCode());
				orderReturnGoods.setExtensionCode(createGoods.getExtensionCode());
				orderReturnGoods.setExtensionId(createGoods.getExtensionId());
				orderReturnGoods.setOsDepotCode(depotCode);
				orderReturnGoods.setGoodsThumb(createGoods.getGoodsThumb());//图片url
				orderReturnGoods.setPayPoints(ConstantsUtil.obj2Bigdecimal(createGoods.getPayPoints()));//消费积分
				orderReturnGoods.setSeller(createGoods.getSeller());
				orderReturnGoods.setSap(createGoods.getSap());
				orderReturnGoods.setBvValue(createGoods.getBvValue() == null ? "0" : createGoods.getBvValue() + "");
				orderReturnGoods.setBaseBvValue(createGoods.getBaseBvValue());
				if(oInfo != null){
					orderReturnGoods.setMasterOrderSn(oInfo.getMasterOrderSn());//主单号
					orderReturnGoods.setOrderSn(oInfo.getOrderSn());//子单号
				}else{
					orderReturnGoods.setMasterOrderSn(param.getRelatingOrderSn());//主单号
				}
				orderReturnGoods.setSalesMode(createGoods.getSalesMode().byteValue());//商品销售模式：1为自营，2为买断，3为寄售，4为直发
				
				if(param.getReturnType().intValue() == Constant.OR_RETURN_TYPE_RETURN
						|| param.getReturnType() == Constant.OR_RETURN_TYPE_REJECT){
					//退货单、拒收入库
					orderReturnGoods.setGoodsReturnNumber(createGoods.getGoodsReturnNumber());
					orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
					orderReturnGoods.setChargeBackCount(createGoods.getChargeBackCount());
					orderReturnGoods.setHaveReturnCount(createGoods.getHaveReturnCount());
					orderReturnGoods.setSettlementPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getSettlementPrice()));
					orderReturnGoods.setReturnReason(createGoods.getReturnReason());
				}else if(param.getReturnType().intValue() == Constant.OR_RETURN_TYPE_REFUND){
					//退款单
					orderReturnGoods.setGoodsBuyNumber(createGoods.getGoodsBuyNumber());
					orderReturnGoods.setGoodsReturnNumber(createGoods.getGoodsReturnNumber());
					orderReturnGoods.setShareSettle(ConstantsUtil.obj2Bigdecimal(createGoods.getShareSettle()));
					orderReturnGoods.setSettlementPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getShareSettle()));
				}else{
					//额外退款单
					orderReturnGoods.setSettlementPrice(ConstantsUtil.obj2Bigdecimal(createGoods.getSettlementPrice()));
					orderReturnGoods.setPriceDifference(ConstantsUtil.obj2Bigdecimal(createGoods.getPriceDifference()));
					orderReturnGoods.setPriceDifferNum(createGoods.getPriceDifferNum());
					orderReturnGoods.setPriceDifferReason(createGoods.getReturnReason());
				}
				orderReturnGoods.setGoodsThumb(createGoods.getGoodsThumb());
				orderReturnGoods.setBoxGauge(createGoods.getBoxGauge());
				orderReturnGoodsList.add(orderReturnGoods);
			}
		}
		param.setBvValue(bvValue);
		param.setBaseBvValue(baseBvValue);
		return orderReturnGoodsList;
	}

	// 生成returnSn
	private String generateOrderReturnSn() {
		SystemReturnSn systemReturnSn = new SystemReturnSn();
		int rs = systemReturnSnMapper.insert(systemReturnSn);
		if (rs > 0) {
			Integer autoId = systemReturnSn.getAutoId();
			logger.debug("generate system return sn : {}", autoId);
			return OrderUtils.mergeAutoId(autoId);
		}
		return null;
	}
	

	
	private static Map<String, OrderGoods> bulidReturnGoodsMap(
			List<OrderGoods> oGoodsList) {
		Map<String, OrderGoods> sku_returnGoods = new HashMap<String, OrderGoods>();
		if (oGoodsList != null && oGoodsList.size() > 0) {
			for (OrderGoods og : oGoodsList) {
				String key = buildKey(og, null,og.getOrderSn());
				sku_returnGoods.put(key, og);
			}
		}
		return sku_returnGoods;
	}

	/**
	 * 订单退款金额分摊到非赠品的商品上
	 * @param goodsList
	 * @param totalReturnMoney 订单退款总金额
	 * @return
	 */
	public List<CreateOrderReturnGoods> shareReturnAmtForOrder(List<CreateOrderReturnGoods> goodsList, double totalReturnMoney){
		if (CollectionUtils.isEmpty(goodsList)) {
			throw new RuntimeException("退货商品列表为空");
		}
		double totalGoodsAmount = 0 ;
		for (CreateOrderReturnGoods createOrderReturnGoods : goodsList) {
			totalGoodsAmount += createOrderReturnGoods.getSettlePrice() * createOrderReturnGoods.getGoodsReturnNumber();
		}
/*	  if(totalReturnMoney > totalGoodsAmount){
			throw new RuntimeException("退款总金额("+FormatUtil.roundDouble(totalReturnMoney,2)+")错误必须小于等于商品总财物价格之和("+FormatUtil.roundDouble(totalGoodsAmount,2)+")");
		}*/
		double remainPromAmt = totalReturnMoney;
		//利用统筹进行讲促销分摊到商品上
		for (int i = 0; i < goodsList.size(); i++) {
			CreateOrderReturnGoods goods = goodsList.get(i);
			if(!StringUtils.equalsIgnoreCase("gift", goods.getExtensionCode())){
				double singleShareAmt = 0d;
				if(i == goodsList.size() - 1){
					singleShareAmt = CommonUtils.roundDouble(remainPromAmt/goods.getGoodsReturnNumber().intValue(),2);
				}else{
					singleShareAmt = CommonUtils.roundDouble((goods.getSettlePrice() /totalGoodsAmount) * totalReturnMoney,2);
					remainPromAmt = CommonUtils.roundDouble(remainPromAmt - singleShareAmt * goods.getGoodsReturnNumber().intValue(),2);			 
				}
				goods.setShareSettle(singleShareAmt);
				goodsList.set(i, goods);
			}
		}
		return goodsList;
	}
	
	@Override
	public void sendReturnMessage(String mobile, String message,String actionUser, String returnSn) {
		logger.debug("sendReturnMessage.begin..returnSn:"+returnSn+",actionNote:"+actionUser+",message:"+message+",mobile:"+mobile);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setReturnSn(returnSn);
		String actionNote = StringUtils.EMPTY;
		
		try{
			if(StringUtils.isBlank(returnSn)){
				throw new RuntimeException("退单号为空");
			}
			//退单验证
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			if(StringUtils.isBlank(mobile)){
				MasterOrderAddressInfoExample orderAddressInfoExample = new MasterOrderAddressInfoExample();
				orderAddressInfoExample.or().andMasterOrderSnEqualTo(orderReturn.getMasterOrderSn());
				List<MasterOrderAddressInfo> orderAddressList = masterOrderAddressInfoMapper.selectByExample(orderAddressInfoExample);
				if(CollectionUtils.isEmpty(orderAddressList)){
					throw new RuntimeException("收件人地址信息不存在！");
				}
				mobile = orderAddressList.get(0).getMobile();
			}
			if(StringUtils.isBlank(mobile)){
				throw new RuntimeException("手机号码为空无法正常发送短信");
			}
			if(StringUtils.isBlank(message)){
				throw new RuntimeException("短信内容为空无法正常发送短信");
			}
			//发送结算短信
			messageService.sendMobileMessage(mobile, message, orderReturn.getChannelCode(), ConstantValues.MOBILE_MESSAGE_SENDTYPE_209);
			
			orderActionService.addOrderReturnAction(returnSn, "短信发送成功["+message+"]", actionUser);
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("sendReturnMessage"+returnSn+" 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		} finally{
			//保存日志
			if(StringUtils.isNotBlank(actionNote)){
				orderActionService.addOrderReturnAction(returnSn, "沟通："+actionNote,actionUser);			 
			}
		}
	}
	
	
	@Override
	public ReturnInfo checkOrderCanSettle(String orderSn,Double settleMoney) {
		MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
		Double moneyPaid = masterOrderInfo.getMoneyPaid().doubleValue();
		Double surplus = masterOrderInfo.getSurplus().doubleValue();
		logger.debug("checkOrderCanSettle.begin"+orderSn + ",moneyPaid:"+moneyPaid+",settleMoney:"+settleMoney);
		
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setOrderSn(orderSn);
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("订单结算退单条件验证失败");
		try {
			BigDecimal totalReturnMoney = BigDecimal.ZERO;
			String actionCall = StringUtils.EMPTY;
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			OrderReturnExample.Criteria criteria = orderReturnExample.or();
			criteria.andMasterOrderSnEqualTo(orderSn);
			criteria.andHaveRefundEqualTo(ConstantValues.YESORNO_YES.byteValue());//需要退款
			criteria.andReturnSettlementTypeNotEqualTo(Byte.valueOf("2"));//不是保证金
			criteria.andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY);
			criteria.andReturnTypeNotEqualTo(ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE);
//		  andReturnTypeIn(Arrays.asList(new Byte[]{ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS,ConstantValues.ORDERRETURN_TYPE.RETURN_PAY,ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY}));
			List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(orderReturnExample);
			//平台订单结算校验

			double returnGoodMoney = 0d;//退货单总金额
			double returnPayMoney = 0d;//退款单总金额
			double returnExtraPayMoney = 0d;//额外退款单总金额（预付款）
			for (OrderReturn orderReturn : orderReturnList) {
				if(orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED.intValue()){
				
					//额外退款单 只考虑预付款
					if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()){
						if(orderReturn.getReturnSettlementType().intValue() == ConstantValues.ORDERRETURN_SETTLMENT_TYPE.PRE_PAY){
							returnExtraPayMoney +=orderReturn.getReturnTotalFee().doubleValue();
						}
					}
					//退款单
					if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY.intValue()){
						returnPayMoney += orderReturn.getReturnTotalFee().doubleValue();
					}
					
					//退货单
					if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() || orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
						returnGoodMoney += orderReturn.getReturnTotalFee().doubleValue();
					}
				}
			}
			logger.info("checkOrderCanSettle.process"+orderSn + ",moneyPaid:"+moneyPaid+",settleMoney:"+settleMoney+",returnGoodMoney:"+returnGoodMoney+",returnPayMoney:"+returnPayMoney+",returnExtraPayMoney:"+returnExtraPayMoney+",actionCall:"+actionCall);
			if(moneyPaid  - returnGoodMoney - returnPayMoney - returnExtraPayMoney - settleMoney > 0.01){
				//已付款金额- 退货单总金额  -退款单总金额 - 额外退款单总金额（预付款）> 应结算金额
				throw new RuntimeException("订单结算金额异常 ：应结算金额["+settleMoney+"] 小于 已付款金额["+moneyPaid+"] 减去 退货单总金额["+returnGoodMoney+"] 减去 普通退款单总金额["+returnPayMoney+"] 减去 额外退款单总金额["+returnExtraPayMoney+"]");
			}else if(moneyPaid - returnGoodMoney - returnPayMoney - returnExtraPayMoney - settleMoney < -0.01){
				//已付款金额- 退货单总金额  -退款单总金额 - 额外退款单总金额（预付款）< 应结算金额
				throw new RuntimeException("订单结算金额异常 ：应结算金额["+settleMoney+"] 大于 已付款金额["+moneyPaid+"] 减去 退货单总金额["+returnGoodMoney+"] 减去 普通退款单总金额["+returnPayMoney+"] 减去 额外退款单总金额["+returnExtraPayMoney+"]");
			}
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("订单结算退单条件验证成功");
		} catch (Exception e) {
			logger.error("checkOrderCanSettle.orderSn:"+orderSn+",exception:"+e.getMessage(),e);
			returnInfo.setIsOk(ConstantValues.YESORNO_NO);
			returnInfo.setMessage(e.getMessage());
		}
		
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> updateReturnGoods(CreateOrderReturnBean param) {
		logger.info("updateReturnGoods.begin:"+JSON.toJSONString(param));
		if(CollectionUtils.isEmpty(param.getCreateOrderReturnGoodsList())){
			throw new RuntimeException("待更新的退单商品列表为空");
		}
		String actionNote = "退单商品更新:<br />";
		String actionUser = param.getActionUser();
		String returnSn = param.getOrderReturnSn();
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setReturnSn(returnSn);
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("退单商品更新失败");
		boolean inputFlag = true;
		try {
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单数据不存在");
			}
			OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getMasterOrderSn());
			if(orderInfo == null && masterOrderInfo == null){
				throw new RuntimeException("退单关联订单不存在");
			}
			Byte returnType = orderReturn.getReturnType();
			List<CreateOrderReturnGoods> returnGoodsList = param.getCreateOrderReturnGoodsList();
			if(CollectionUtils.isEmpty(returnGoodsList)){
				throw new RuntimeException("待更新退单商品异常为空");
			}
			/*if(orderReturn.getReturnOrderStatus().intValue() != ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM.intValue()){
				throw new RuntimeException("退单状态异常，未确认方可进行编辑操作");
			}*/
			OrderReturnGoodsExample returnGoodsExample = new OrderReturnGoodsExample();
			returnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> oldReturnGoodsList = orderReturnGoodsMapper.selectByExample(returnGoodsExample);
			if(CollectionUtils.isEmpty(oldReturnGoodsList)){
				throw new RuntimeException("退单原商品异常为空");
			}
			//key = 商品唯一码   value = 商品实体
			Map<String,OrderReturnGoods> orderReturnGoodsMap = new HashMap<String, OrderReturnGoods>();
			for (OrderReturnGoods orderReturnGoods : oldReturnGoodsList) {
				orderReturnGoodsMap.put(orderReturnGoods.getCustomCode() + orderReturnGoods.getExtensionCode() + orderReturnGoods.getExtensionId() + orderReturnGoods.getOsDepotCode(), orderReturnGoods);
			}
			
			OrderReturnGoods updateOrderReturnGoods = new OrderReturnGoods();
			List<OrderReturnGoods> updateReturnGoodsList = new ArrayList<OrderReturnGoods>();
			List<OrderReturnGoods> delReturnGoodsList = new ArrayList<OrderReturnGoods>();
			//入库，质检，收货标识
			boolean checkinStatusFlag = true,qualityStatusFlag = true,isGoodReceivedFlag =true;
			for (CreateOrderReturnGoods createOrderReturnGoods : returnGoodsList) {
				int innerCount = 0;
				String innerAction = "商品编码["+createOrderReturnGoods.getCustomCode()+"] <br />";
				String key = createOrderReturnGoods.getCustomCode() + createOrderReturnGoods.getExtensionCode() +createOrderReturnGoods.getExtensionId() + createOrderReturnGoods.getOsDepotCode();
				OrderReturnGoods oldReturnGoods = orderReturnGoodsMap.get(key);
				updateOrderReturnGoods = new OrderReturnGoods();
				updateOrderReturnGoods.setCustomCode(createOrderReturnGoods.getCustomCode());
				updateOrderReturnGoods.setExtensionCode(createOrderReturnGoods.getExtensionCode());
				updateOrderReturnGoods.setExtensionId(createOrderReturnGoods.getExtensionId());
				updateOrderReturnGoods.setOsDepotCode(createOrderReturnGoods.getOsDepotCode());
				
				//修改收货状态
				if(isGoodReceivedFlag && createOrderReturnGoods.getIsGoodReceived().intValue() != ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED.intValue()){
					isGoodReceivedFlag = false;
				}
				//质检
				if(qualityStatusFlag && createOrderReturnGoods.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue()){
					qualityStatusFlag = false;
				}
				//入库
				if(checkinStatusFlag && createOrderReturnGoods.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()){
					checkinStatusFlag = false;
				}
				
				if(returnType == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() || returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
					updateOrderReturnGoods.setGoodsReturnNumber(createOrderReturnGoods.getGoodsReturnNumber());
					updateOrderReturnGoods.setReturnReason(createOrderReturnGoods.getReturnReason());
					if(oldReturnGoods != null && 
							oldReturnGoods.getGoodsReturnNumber().intValue() != updateOrderReturnGoods.getGoodsReturnNumber().intValue()){
						innerCount ++;
						innerAction +=  "退货数量由 "+oldReturnGoods.getGoodsReturnNumber().intValue()+"调整为 "+updateOrderReturnGoods.getGoodsReturnNumber().intValue()+"<br />";
					}
					if(oldReturnGoods != null && 
							!StringUtils.equalsIgnoreCase(oldReturnGoods.getReturnReason(), updateOrderReturnGoods.getReturnReason())){
						innerCount ++;
						innerAction += "退货原因由 "+oldReturnGoods.getReturnReason()+"调整为 "+updateOrderReturnGoods.getReturnReason() + "<br />";
					}
					if(updateOrderReturnGoods.getGoodsReturnNumber().intValue() > 0 && innerCount > 0){
						actionNote += innerAction;
						updateReturnGoodsList.add(updateOrderReturnGoods);
					}
					if(updateOrderReturnGoods.getGoodsReturnNumber().intValue() == 0){
						actionNote += innerAction;
						delReturnGoodsList.add(updateOrderReturnGoods);
					}else{
						
					}
				}else if(returnType == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()){
					updateOrderReturnGoods.setPriceDifference(BigDecimal.valueOf(createOrderReturnGoods.getPriceDifference()));
					updateOrderReturnGoods.setPriceDifferNum(createOrderReturnGoods.getPriceDifferNum());
					updateOrderReturnGoods.setPriceDifferReason(createOrderReturnGoods.getReturnReason());
					if(oldReturnGoods != null && 
							oldReturnGoods.getPriceDifference().doubleValue() != updateOrderReturnGoods.getPriceDifference().doubleValue()){
						innerCount ++;
						innerAction += "商品差价由 "+oldReturnGoods.getPriceDifference().doubleValue()+"调整为 "+updateOrderReturnGoods.getPriceDifference().doubleValue()+"<br />";
					}
					if(oldReturnGoods != null && 
							oldReturnGoods.getPriceDifferNum().intValue() != updateOrderReturnGoods.getPriceDifferNum().intValue()){
						innerCount ++;
						innerAction += "商品差价数量由 "+oldReturnGoods.getPriceDifferNum().intValue()+"调整为 "+updateOrderReturnGoods.getPriceDifferNum().intValue()+"<br />";
					}
					if(oldReturnGoods != null && 
							!StringUtils.equalsIgnoreCase(oldReturnGoods.getPriceDifferReason(), updateOrderReturnGoods.getPriceDifferReason())){
						innerCount ++;
						innerAction += "差价原因由 "+oldReturnGoods.getPriceDifferReason()+"调整为 "+updateOrderReturnGoods.getPriceDifferReason()+"<br />";
					}
					if(innerCount > 0){
						actionNote += innerAction;
						updateReturnGoodsList.add(updateOrderReturnGoods);					  
					}
				}else{
					throw new RuntimeException("退单类型异常，商品更新仅针对额外退款单和退货单操作");
				}
				orderReturnGoodsMap.remove(key);
			}
			if(MapUtils.isNotEmpty(orderReturnGoodsMap)){
				for(String key : orderReturnGoodsMap.keySet()){
					OrderReturnGoods deleteGoods = (OrderReturnGoods) MapUtils.getObject(orderReturnGoodsMap, key);
					updateOrderReturnGoods = new OrderReturnGoods();
					updateOrderReturnGoods.setCustomCode(deleteGoods.getCustomCode());
					updateOrderReturnGoods.setExtensionCode(deleteGoods.getExtensionCode());
					updateOrderReturnGoods.setExtensionId(deleteGoods.getExtensionId());
					updateOrderReturnGoods.setOsDepotCode(deleteGoods.getOsDepotCode());
					delReturnGoodsList.add(updateOrderReturnGoods);
				}
			}
			
			//更新数据库
			if(CollectionUtils.isNotEmpty(updateReturnGoodsList)){
				for (OrderReturnGoods orderReturnGoods : updateReturnGoodsList) {
					OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
					orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn).
					andCustomCodeEqualTo(orderReturnGoods.getCustomCode()).
					andExtensionCodeEqualTo(orderReturnGoods.getExtensionCode()).
					andExtensionIdEqualTo(orderReturnGoods.getExtensionId()).
					andOsDepotCodeEqualTo(orderReturnGoods.getOsDepotCode());
					
					List<OrderReturnGoods> returnGoodList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
					if(returnGoodList.get(0).getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.intValue()
							|| returnGoodList.get(0).getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()){
						throw new RuntimeException("已入库或部分入库商品无法编辑！");
					}
					
					OrderReturnGoods updateGoods = new OrderReturnGoods();
					
					if(returnType == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() || returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
						updateGoods.setGoodsReturnNumber(orderReturnGoods.getGoodsReturnNumber());
						
						updateGoods.setReturnReason(orderReturnGoods.getReturnReason());					
					}else{
						updateGoods.setPriceDifference(orderReturnGoods.getPriceDifference());
						updateGoods.setPriceDifferNum(orderReturnGoods.getPriceDifferNum());
						updateGoods.setPriceDifferReason(orderReturnGoods.getPriceDifferReason());
					}
					orderReturnGoodsMapper.updateByExampleSelective(updateGoods, orderReturnGoodsExample);
				}
			}
			if(CollectionUtils.isNotEmpty(delReturnGoodsList)){
				for (OrderReturnGoods orderReturnGoods : delReturnGoodsList) {
					OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
					orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn).
					andCustomCodeEqualTo(orderReturnGoods.getCustomCode()).
					andExtensionCodeEqualTo(orderReturnGoods.getExtensionCode()).
					andExtensionIdEqualTo(orderReturnGoods.getExtensionId()).
					andOsDepotCodeEqualTo(orderReturnGoods.getOsDepotCode());
					
					List<OrderReturnGoods> returnGoodList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
					if(returnGoodList.get(0).getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE.intValue()
							|| returnGoodList.get(0).getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()){
						throw new RuntimeException("已入库或部分入库商品无法删除！");
					}
					
					orderReturnGoodsMapper.deleteByExample(orderReturnGoodsExample);
				}
			}
			
			//查询最新商品数据，重新计算退单金额
			double totalGoodsMoney = 0D;
			int totalGoodsNum = 0;
			double totalDiffMoney = 0D;
			int totalDiffNum = 0;
			double totalIntegralMoney = 0D;
			double returnBonusMoney = 0D;
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> goodsReturnList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			
			if(CollectionUtils.isNotEmpty(goodsReturnList)){
				for (OrderReturnGoods orderReturnGoods : goodsReturnList) {
					if(returnType == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() || returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
						//退货单
						totalGoodsMoney += orderReturnGoods.getGoodsPrice().doubleValue() * orderReturnGoods.getGoodsReturnNumber().intValue();
						totalGoodsNum += orderReturnGoods.getGoodsReturnNumber().intValue();
						totalIntegralMoney += orderReturnGoods.getIntegralMoney().doubleValue()* orderReturnGoods.getGoodsReturnNumber().intValue();//积分使用金额
						returnBonusMoney += orderReturnGoods.getShareBonus().doubleValue()* orderReturnGoods.getGoodsReturnNumber().intValue();//红包
					}else{
						//额外退款单
						totalDiffMoney += orderReturnGoods.getPriceDifference().doubleValue() * orderReturnGoods.getPriceDifferNum().intValue();
						totalDiffNum += orderReturnGoods.getPriceDifferNum().intValue();
//						totalIntegralMoney += orderReturnGoods.getIntegralMoney().doubleValue()* orderReturnGoods.getPriceDifferNum().intValue();//积分使用金额
//						returnBonusMoney += orderReturnGoods.getShareBonus().doubleValue()* orderReturnGoods.getPriceDifferNum().intValue();//红包
					}
					
					
				}
			}
			
			//修改退单商品后校验退单入库状态(校验扫描入库数量和退货数量是否相等)
			List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			for(OrderReturnGoods orderReturnGoods:orderReturnGoodsList){
				if(orderReturnGoods.getProdscannum().intValue() != orderReturnGoods.getGoodsReturnNumber().intValue()){
					inputFlag = false ;
					break;
				}else{
					if(orderReturnGoods.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()){
						OrderReturnGoods updateGoods = new OrderReturnGoods();
						updateGoods.setId(orderReturnGoods.getId());
						updateGoods.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
						updateGoods.setQualityStatus(ConstantValues.ORDERRETURN_ISPASS_STATUS.PASS);
						updateGoods.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
						orderReturnGoodsMapper.updateByPrimaryKeySelective(updateGoods);
					}
				}
				
			}
			OrderReturn updateOrderReturn = new OrderReturn();
			OrderReturnShip updateOrderReturnShip = new OrderReturnShip();
			updateOrderReturnShip.setRelatingReturnSn(returnSn);
			if(isGoodReceivedFlag){
				updateOrderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
			}
			
			if(qualityStatusFlag){
				updateOrderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
				updateOrderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue());
			}
			
			if(checkinStatusFlag || inputFlag){
				if(returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
					updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED);
				}else{
					updateOrderReturn.setPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
				}
				updateOrderReturnShip.setIsGoodReceived(ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED);
				updateOrderReturnShip.setQualityStatus(ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS.intValue());
				updateOrderReturnShip.setCheckinStatus(ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.byteValue());
				OrderRefundExample refundExample = new OrderRefundExample(); 
				refundExample.or().andRelatingReturnSnEqualTo(returnSn);
				List<OrderRefund> refundList = orderRefundMapper.selectByExample(refundExample);
				for (OrderRefund orderRefund : refundList) {
					if(returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
						orderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED);
					}else{
						orderRefund.setReturnPayStatus(ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE);
					}
					orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
				}
			}
			if(isGoodReceivedFlag || qualityStatusFlag || checkinStatusFlag || inputFlag){
				orderReturnShipMapper.updateByPrimaryKeySelective(updateOrderReturnShip);
			}
			
			
			//退货单
			// return_total_fee
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setUpdateTime(new Date());
			updateOrderReturn.setIntegralMoney(BigDecimal.valueOf(totalIntegralMoney));
			updateOrderReturn.setReturnBonusMoney(BigDecimal.valueOf(returnBonusMoney));
			boolean updateReturnTotalFeeFlag = false;
			if(returnType == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() || returnType == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
				updateOrderReturn.setReturnAllgoodsCount(totalGoodsNum);
				updateOrderReturn.setReturnGoodsMoney(BigDecimal.valueOf(totalGoodsMoney));
				updateOrderReturn.setReturnTotalFee(BigDecimal.valueOf(totalGoodsMoney + orderReturn.getReturnShipping().doubleValue() + orderReturn.getReturnOtherMoney().doubleValue() - totalIntegralMoney-returnBonusMoney));
				
				if(totalGoodsNum != orderReturn.getReturnAllgoodsCount().intValue()){
					actionNote += "总退货量由 "+orderReturn.getReturnAllgoodsCount().intValue()+" 调整为 " +totalGoodsNum;
				}
				if(totalGoodsMoney != orderReturn.getReturnGoodsMoney().doubleValue()){
					actionNote += "退货总金额由 "+orderReturn.getReturnGoodsMoney().doubleValue()+" 调整为 " +totalGoodsMoney;
				}
				if(updateOrderReturn.getReturnTotalFee().doubleValue() != orderReturn.getReturnTotalFee().doubleValue()){
					updateReturnTotalFeeFlag = true;
					actionNote += "退款总金额由 "+orderReturn.getReturnTotalFee().doubleValue()+" 调整为 " +updateOrderReturn.getReturnTotalFee().doubleValue();
				}
			}else{
				//额外退款单
				updateOrderReturn.setPricedifferGoodsTotal(totalDiffNum);
				updateOrderReturn.setTotalPriceDifference(BigDecimal.valueOf(totalDiffMoney));
				updateOrderReturn.setReturnTotalFee(BigDecimal.valueOf(totalDiffMoney + orderReturn.getReturnShipping().doubleValue() + orderReturn.getReturnOtherMoney().doubleValue() - totalIntegralMoney-returnBonusMoney));
				if(totalDiffNum != orderReturn.getPricedifferGoodsTotal().intValue()){
					actionNote += "总退差价商品数量 由 "+orderReturn.getPricedifferGoodsTotal().intValue()+" 调整为 " +totalDiffNum;
				}
				if(totalDiffMoney != updateOrderReturn.getTotalPriceDifference().doubleValue()){
					actionNote += "退差价总金额由 "+orderReturn.getTotalPriceDifference().doubleValue()+" 调整为 " +totalDiffMoney;
				}
				if(updateOrderReturn.getReturnTotalFee().doubleValue() != orderReturn.getReturnTotalFee().doubleValue()){
					
					actionNote += "退款总金额由 "+orderReturn.getReturnTotalFee().doubleValue()+" 调整为 " +updateOrderReturn.getReturnTotalFee().doubleValue();
				}
			}
			
			//付款单重新平摊
			List<OrderRefund> updateRefundList = new ArrayList<OrderRefund>();
			int transType = 0;
			if(orderInfo != null){
				transType = orderInfo.getTransType().intValue();
			}else{
				transType = masterOrderInfo.getTransType().intValue();
			}
			List<OrderRefund> orderRefundList = processOrderRefundShareList(returnSn, transType, updateOrderReturn.getReturnTotalFee().doubleValue());
			if(CollectionUtils.isNotEmpty(orderRefundList)){
				for (OrderRefund orderRefund : orderRefundList) {
					OrderRefund updateOrderRefund = new OrderRefund();
					updateOrderRefund.setReturnPay(orderRefund.getReturnPay());
					updateOrderRefund.setReturnFee(orderRefund.getReturnFee());
					updateRefundList.add(updateOrderRefund);
				}
			}
			if(CollectionUtils.isNotEmpty(updateRefundList)){
				for (OrderRefund orderRefund : orderRefundList) {
					OrderRefundExample orderRefundExample = new OrderRefundExample();
					orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn).andReturnPayEqualTo(orderRefund.getReturnPay());
					
					OrderRefund updateRefund = new OrderRefund();
					updateRefund.setReturnFee(orderRefund.getReturnFee());
					updateRefund.setUpdateTime(new Date());
					orderRefundMapper.updateByExampleSelective(updateRefund, orderRefundExample);
				}
			}
			if(updateOrderReturn != null){
				orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			}
			if(updateReturnTotalFeeFlag && orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					&& StringUtils.isNotBlank(orderReturn.getNewOrderSn()) ){
				List<MasterPay> masterPayList = new ArrayList<MasterPay>();
				MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
				masterOrderPayExample.or().andMasterOrderSnEqualTo(orderReturn.getNewOrderSn()).andPayStatusEqualTo(Constant.OI_PAY_STATUS_UNPAYED);
				List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
				MasterPay unpay=new MasterPay();
				if(CollectionUtils.isNotEmpty(masterOrderPayList)){//有未支付的付款单
					unpay.setPayId(Integer.valueOf(masterOrderPayList.get(0).getPayId()));
					unpay.setPayTotalFee(masterOrderPayList.get(0).getPayTotalfee().doubleValue()+orderReturn.getReturnTotalFee().doubleValue()-updateOrderReturn.getReturnTotalFee().doubleValue());
					
				}else{//无未支付的退款单，默认支付宝
					unpay.setPayId(1); 
					unpay.setPayTotalFee(orderReturn.getReturnTotalFee().doubleValue()-updateOrderReturn.getReturnTotalFee().doubleValue());
				}
				masterPayList.add(unpay);
				MasterPay payInfo=new MasterPay();
				payInfo.setPayId(Integer.valueOf(ConstantValues.EXCHANGE_ORDER_RETURN_2_PAY_ID));
				payInfo.setPayStatus(Constant.OP_PAY_STATUS_COMFIRM);
				payInfo.setPayTotalFee(updateOrderReturn.getReturnTotalFee().doubleValue());
//				payInfo.setPayTime(new Date());
				masterPayList.add(payInfo);
				payService.createMasterPay(orderReturn.getNewOrderSn(), masterPayList);
				
				OrderReturnShip ship = orderReturnShipMapper.selectByPrimaryKey(returnSn);
				if(ship.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()){
					MasterOrderPayExample payExample = new MasterOrderPayExample();
					payExample.or().andMasterOrderSnEqualTo(orderReturn.getNewOrderSn()).andPayIdEqualTo(Byte.valueOf(ConstantValues.EXCHANGE_ORDER_RETURN_2_PAY_ID));
					List<MasterOrderPay> payList = masterOrderPayMapper.selectByExample(payExample);
					//确认换单退单转入款
					OrderStatus orderStatus = new OrderStatus();
					orderStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
					orderStatus.setMasterOrderSn(orderReturn.getNewOrderSn());
					orderStatus.setPaySn(payList.get(0).getMasterPaySn());
					orderStatus.setMessage("退货单["+orderReturn.getReturnSn()+"]入库更新换货单待确认付款单");
					ReturnInfo payResult = payService.orderReturnPayStCh(orderStatus);
					logger.info("退货单入库时调用换货单付款接口修改待确认付款单数据：returnSn:"+returnSn+",payStCh:"+JSON.toJSONString(payResult));
					
				}
				
				//修改换单应额付款金额和已支付金额
				double lessMoney = orderReturn.getReturnTotalFee().doubleValue()-updateOrderReturn.getReturnTotalFee().doubleValue();
				MasterOrderInfo exchangeOrder = masterOrderInfoMapper.selectByPrimaryKey(orderReturn.getNewOrderSn());
				MasterOrderInfo info = new MasterOrderInfo();
				info.setMasterOrderSn(orderReturn.getNewOrderSn());
				info.setMoneyPaid(BigDecimal.valueOf(exchangeOrder.getMoneyPaid().doubleValue()-lessMoney));
				info.setTotalPayable(BigDecimal.valueOf(exchangeOrder.getTotalPayable().doubleValue() +lessMoney));
				masterOrderInfoMapper.updateByPrimaryKeySelective(info);
			}
			if(StringUtils.isNotBlank(actionNote)){
				orderActionService.addOrderReturnAction(returnSn, actionNote, actionUser);
			}
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("退单商品更新成功");
		} catch (Exception e) {
			logger.debug("updateReturnGoods.orderSn:"+returnSn+",exception:"+e.getMessage(),e);
			returnInfo.setIsOk(ConstantValues.YESORNO_NO);
			returnInfo.setMessage(e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 退单-付款单平摊总退单金额，默认在线支付
	 * @param orderSn 
	 * @param totalReturnMoney //退款总金额
	 */
	private List<OrderRefund> processOrderRefundShareList(String returnSn,Integer transType,Double totalReturnMoney){
		logger.info("平摊退款金额，returnSn："+returnSn+",transType:"+transType+",totalReturnMoney"+totalReturnMoney);
		OrderRefundExample orderRefundExample = new OrderRefundExample();
		orderRefundExample.or().andRelatingReturnSnEqualTo(returnSn);
		
		List<OrderRefund> refundList = orderRefundMapper.selectByExample(orderRefundExample);
		
		Byte surplusPay = Byte.valueOf("3");//余额支付
		boolean existSurplusPay = false;//是否存在余额支付
		List<OrderRefund> createOrderPayList = new ArrayList<OrderRefund>();
		Map<Byte,Double> limitOrderPay = new HashMap<Byte, Double>();
		
		if(CollectionUtils.isNotEmpty(refundList)){
			for (OrderRefund orderRefund : refundList) {
				
				if(!limitOrderPay.containsKey(orderRefund.getReturnPay())){
					limitOrderPay.put(orderRefund.getReturnPay().byteValue(), orderRefund.getReturnFee().doubleValue());
				}else{
					limitOrderPay.put(orderRefund.getReturnPay().byteValue(), limitOrderPay.get(orderRefund.getReturnPay().byteValue()).doubleValue() + orderRefund.getReturnFee().doubleValue());
				}
				
			}
		}

		logger.info("processOrderRefundShareList.returnSn:"+returnSn+",limitOrderPay:"+JSON.toJSONString(limitOrderPay));
		
		
		if(limitOrderPay.containsKey(surplusPay)){
			//存在余额支付
			limitOrderPay.remove(surplusPay);
			existSurplusPay = true;
		}
		if(refundList.size() == 1){//只有一种支付方式
			OrderRefund orderRefund = new OrderRefund();
			orderRefund.setReturnFee(BigDecimal.valueOf(totalReturnMoney));
			orderRefund.setReturnPay(refundList.get(0).getReturnPay());
			createOrderPayList.add(orderRefund);
		}else{
			//平摊非余额支付
			Set<Byte> payIdSet = limitOrderPay.keySet();
			for (Byte payId : payIdSet) {
				if(totalReturnMoney <= 0){
					break;
				}
				//按照支付方式进行平摊
				double returnMoney = 0D;
				if(totalReturnMoney > limitOrderPay.get(payId)){
					returnMoney = limitOrderPay.get(payId);
					totalReturnMoney = totalReturnMoney - returnMoney;
				}else{
					returnMoney = totalReturnMoney;
					totalReturnMoney = 0D;
				}
				OrderRefund orderRefund = new OrderRefund();
				orderRefund.setReturnFee(BigDecimal.valueOf(returnMoney));
				orderRefund.setReturnPay(payId.shortValue());
				createOrderPayList.add(orderRefund);
			}
			if(existSurplusPay && totalReturnMoney > 0){
				OrderRefund orderRefund = new OrderRefund();
				orderRefund.setReturnFee(BigDecimal.valueOf(totalReturnMoney));
				orderRefund.setReturnPay(surplusPay.shortValue());
				createOrderPayList.add(orderRefund);
			}
		}
		return createOrderPayList;
	}
	
	
	/**
	 * 构建key
	 * 
	 * @param og
	 * @param org
	 * @return
	 */
	private static String buildKey(OrderGoods og, CreateOrderReturnGoods org,String relOrderSn) {
		if (og != null) {
			return relOrderSn + "_" + og.getCustomCode() + "_" + og.getDepotCode() + "_"
					+ og.getExtensionCode() + "_" + og.getExtensionId();
		}
		if (org != null) {// org.getOsDepotCode()
			return relOrderSn + "_" +org.getCustomCode() + "_" + org.getOsDepotCode() + "_"
					+ org.getExtensionCode() + "_" + org.getExtensionId();
		}
		return "";
	}

	@Resource(name="messageService")
	private MessageService messageService;

	@Autowired
	private OrderReturnMapper orderReturnMapper;

	@Autowired
	private OrderReturnShipMapper orderReturnShipMapper;

	@Autowired
	private OrderDistributeMapper orderDistributeMapper;

	@Autowired
	private OrderRefundMapper orderRefundMapper;
	
	@Autowired
	private SystemPaymentMapper systemPaymentMapper;
	
	@Autowired
	private SystemShippingMapper systemShippingMapper;

	@Autowired
	private OrderPeriodDetailMapper orderPeriodDetailMapper;

	@Autowired
	private OrderReturnGoodsMapper orderReturnGoodsMapper;
	
	@Autowired
	private DistributeActionService distributeActionService;

//	@Autowired
//	private OrderPayMapper orderPayMapper;

	@Autowired
	OrderReturnActionMapper orderReturnActionMapper;
	
//	@Autowired
//	ProductBarcodeListMapper productBarcodeListMapper;
//	
//	@Autowired
//	ProductGoodsMapper productGoodsMapper;
	
	@Resource(name="orderReturnSearchMapper")
	private OrderReturnSearchMapper orderReturnSearchMapper;
	
	@Autowired
	private OrderCustomDefineMapper orderCustomDefineMapper;

    /**
     * 填充退单商品信息
     * @param returnSearchParams
     * @param returnInfoPageList
     */
	private void fillReturnInfoGoods(ReturnSearchParams returnSearchParams, List<ReturnInfoPage> returnInfoPageList) {
        //1、将退单原因code置换为name;2、获取商品数据
        for (ReturnInfoPage returnInfoPage : returnInfoPageList) {
            if (null != returnInfoPage.getReturnReason()) {
                String returnReasonName = returnInfoPage.getReturnReason();
                OrderCustomDefine orderCustomDefine = orderCustomDefineMapper.selectByPrimaryKey(returnInfoPage.getReturnReason());
                if (orderCustomDefine != null) {
                    returnReasonName = orderCustomDefine.getName();
                }
                returnInfoPage.setReturnReasonName(returnReasonName);
            }

            //获取商品数据
            OrderReturnGoodsExample example = new OrderReturnGoodsExample();
            OrderReturnGoodsExample.Criteria criteria = example.or();
            criteria.andRelatingReturnSnEqualTo(returnInfoPage.getReturnSn());
            //按照查询条件删选数据
            if (null != returnSearchParams.getCustomCode()) {
                criteria.andCustomCodeEqualTo(returnSearchParams.getCustomCode());
            }
            if (null != returnSearchParams.getGoodsSn()) {
                criteria.andGoodsSnEqualTo(returnSearchParams.getGoodsSn());
            }
            if (null != returnSearchParams.getGoodsName()) {
                returnSearchParams.setGoodsName("%"+returnSearchParams.getGoodsName()+"%");
                criteria.andGoodsNameLike(returnSearchParams.getGoodsName());
            }
            if (null != returnSearchParams.getSeller()) {
                criteria.andSellerEqualTo(returnSearchParams.getSeller());
            }
            List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(example);
            List<ReturnGoods> returnGoodsList = new ArrayList<ReturnGoods>();
            if (CollectionUtils.isNotEmpty(orderReturnGoodsList)) {
            	// 获取订单商品
                String masterOrderSn = returnInfoPage.getMasterOrderSn();
                Map<String, MasterOrderGoods> masterOrderGoodsMap = getOrderGoodsList(masterOrderSn);

                BigDecimal returnTotalSettlementPrice = BigDecimal.valueOf(0);
                BigDecimal returnTotalSettlementUntaxPrice = BigDecimal.valueOf(0);
                for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                    ReturnGoods returnGoods = new ReturnGoods();
                    returnGoods.setId(orderReturnGoods.getId());

                    String customCode = orderReturnGoods.getCustomCode();
                    returnGoods.setCustomCode(customCode);
                    MasterOrderGoods masterOrderGoods = masterOrderGoodsMap.get(customCode);
                    if (masterOrderGoods != null) {
                        returnGoods.setInputTax(masterOrderGoods.getInputTax());
                    }

					returnGoods.setGoodsSn(orderReturnGoods.getGoodsSn());
                    returnGoods.setGoodsName(orderReturnGoods.getGoodsName());
					returnGoods.setGoodsThumb(orderReturnGoods.getGoodsThumb());
                    returnGoods.setGoodsReturnNumber(orderReturnGoods.getGoodsReturnNumber());
					returnGoods.setGoodsColorName(orderReturnGoods.getGoodsColorName());
                    returnGoods.setGoodsSizeName(orderReturnGoods.getGoodsSizeName());
                    returnGoods.setSettlementPrice(CommonUtils.roundDouble(orderReturnGoods.getSettlementPrice().doubleValue(),2));

                    // 协议价
					BigDecimal costPrice = orderReturnGoods.getCostPrice();
                    if (costPrice != null) {
                        int goodsReturnNumber = orderReturnGoods.getGoodsReturnNumber();
                        costPrice = MathOperation.setScale(costPrice, 2);
                        returnGoods.setCostPrice(costPrice);
                        BigDecimal totalCostPrice = MathOperation.mul(costPrice, BigDecimal.valueOf(goodsReturnNumber));
                        returnTotalSettlementUntaxPrice = returnTotalSettlementUntaxPrice.add(totalCostPrice);

                        BigDecimal totalTaxPrice = totalCostPrice;
                        BigDecimal inputTax = masterOrderGoods.getInputTax();
                        if (inputTax != null && inputTax.doubleValue() > 0) {
                            inputTax = inputTax.add(BigDecimal.valueOf(100));
                            BigDecimal taxPrice = costPrice.multiply(inputTax);
                            totalTaxPrice = MathOperation.mul(taxPrice, BigDecimal.valueOf(goodsReturnNumber));
                            totalTaxPrice = MathOperation.div(totalTaxPrice, BigDecimal.valueOf(100), 2);
                        }
                        returnTotalSettlementPrice = returnTotalSettlementPrice.add(totalTaxPrice);
                    }

                    returnGoods.setIsGoodReceived(orderReturnGoods.getIsGoodReceived().intValue());
                    returnGoods.setCheckinStatus(orderReturnGoods.getCheckinStatus().intValue());
                    returnGoods.setQualityStatus(orderReturnGoods.getQualityStatus().intValue());
                    returnGoods.setBarcode(orderReturnGoods.getBarcode());
                    returnGoodsList.add(returnGoods);
                }

                returnInfoPage.setReturnTotalSettlementUnTaxPrice(returnTotalSettlementUntaxPrice.doubleValue());
                returnInfoPage.setReturnTotalSettlementPrice(returnTotalSettlementPrice.doubleValue());
            }

            //添加关联商品数据
            returnInfoPage.setReturnGoods(returnGoodsList);
        }
    }

    /**
     * 获取订单商品列表
     * @param masterOrderSn
     * @return Map<String, MasterOrderGoods>
     */
    private Map<String, MasterOrderGoods> getOrderGoodsList(String masterOrderSn) {
        List<MasterOrderGoods> orderGoodsList = masterOrderGoodsService.selectByMasterOrderSn(masterOrderSn);

        Map<String, MasterOrderGoods> orderGoodsMap = new HashMap<String, MasterOrderGoods>();
        for (MasterOrderGoods masterOrderGoods : orderGoodsList) {
            String customCode = masterOrderGoods.getCustomCode();
            orderGoodsMap.put(customCode, masterOrderGoods);
        }

        return orderGoodsMap;
    }

	/**
	 * 第三方平台退单查询接口
	 * @param returnSearchParams
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData<Paging<ReturnInfoPage>> getReturnInfoPageList(ReturnSearchParams returnSearchParams) {
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		apiReturnData.setMessage("查询退单成功！");
		try {
			logger.info("第三方退单查询接口begin:"+JSON.toJSONString(returnSearchParams));
			//默认退单生成时间排序
			returnSearchParams.setOrderBy("order by orn.add_time desc");
			if (returnSearchParams.getPageNum() <= 0 ) {
				returnSearchParams.setPageNum(1);
			}
			if (returnSearchParams.getPageSize() <= 0) {
				returnSearchParams.setPageSize(10);
			}
			returnSearchParams.setStart((returnSearchParams.getPageNum() - 1) * returnSearchParams.getPageSize());
			List<ReturnInfoPage> returnInfoPageList = orderReturnSearchMapper.getReturnInfoPageList(returnSearchParams);
			if (CollectionUtils.isEmpty(returnInfoPageList)) {
				apiReturnData.setIsOk(Constant.OS_STR_YES);
				apiReturnData.setMessage("查询退单数据为空，请重新设置查询条件！");
				apiReturnData.setData(null);
				return apiReturnData;
			}

			// 填充退单商品信息
            fillReturnInfoGoods(returnSearchParams, returnInfoPageList);

			apiReturnData.setIsOk(Constant.OS_STR_YES);
			int count = orderReturnSearchMapper.getReturnInfoPageCount(returnSearchParams);
			Paging paging = new Paging(count, returnInfoPageList);
			apiReturnData.setData(paging);
			logger.info("第三方退单查询接口返回数据,apiReturnData:"+JSON.toJSONString(apiReturnData)+",paging:"+JSON.toJSONString(paging+",returnInfoPageList:"+JSON.toJSONString(returnInfoPageList)));
			
		} catch (Exception e) {
			logger.error("第三方退单查询接口出错！message:"+e.getMessage()+"e:"+e);
			apiReturnData.setMessage(e.getMessage());
		}
		
		return apiReturnData;
	}

    /**
     * 供应商查询对应的退单及退单状态数据
     * @param sellerParam
     * @return ApiReturnData
     */
	@Override
	public ApiReturnData<List<SellerBean>> getOrderReturnForSeller(SellerParam sellerParam) {
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		apiReturnData.setMessage("查询退单成功！");
		Map<String,Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		List<SellerBean> sellerBeanList = new ArrayList<SellerBean>();
		try {
			logger.info("通过供应商查询退单数据接口begin:"+JSON.toJSONString(sellerParam));
			List<String> sellers = sellerParam.getSellers();
			if (CollectionUtils.isNotEmpty(sellers)) {
				for (String seller : sellers) {
					SellerBean sellerBean = new SellerBean();
					params.put("seller", seller);
					if (null != sellerParam.getDateFrom()) {
						params.put("dateFrom", sellerParam.getDateFrom());
					}
					
					if (null != sellerParam.getDateTo()) {
						params.put("dateTo", sellerParam.getDateTo());
					}
					
					if (null != sellerParam.getUpdateTimeBegin()) {
						params.put("updateTimeBegin", sellerParam.getUpdateTimeBegin());
					}
					
					if (null != sellerParam.getUpdateTimeEnd()) {
						params.put("updateTimeEnd", sellerParam.getUpdateTimeEnd());
					}
					
					if (null != sellerParam.getReturnOrderStatus()) {
						params.put("returnOrderStatus", sellerParam.getReturnOrderStatus());
					}
					List<OrderReturnForSellers> orderReturnList = orderReturnSearchMapper.getOrderReturnForSeller(params);
					sellerBean.setOrderReturnForSellers(orderReturnList);
					sellerBean.setSeller(seller);
					sellerBeanList.add(sellerBean);
				}
				
			}
			logger.info("通过供销商和时间段查询退单数据接口最终数据sellerBeanList:" + JSON.toJSONString(sellerBeanList));
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setData(sellerBeanList);
		} catch (Exception e) {
			logger.error("通过供销商和时间段查询退单数据接口出错！"+e);
			apiReturnData.setMessage(e.getMessage());
		}
		return apiReturnData;
	}

	/**
	 * 供应商退货入库
	 * @param returnStorageParam
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData returnStorageBySeller(ReturnStorageParam returnStorageParam) {

		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		apiReturnData.setMessage("退单商品通过供应商入库成功！");

        String returnSn = returnStorageParam.getReturnSn();
        String seller = returnStorageParam.getSeller();
		logger.info("退单商品通过供应商入库returnSn："+returnSn+",seller"+seller+"开始,参数returnStorageParam："+JSON.toJSONString(returnStorageParam));
		try {
			OrderReturnGoodsExample example = new OrderReturnGoodsExample();
			example.or().andRelatingReturnSnEqualTo(returnSn).andSellerEqualTo(seller);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(example);
			if (CollectionUtils.isEmpty(returnGoodsList)) {
				throw new RuntimeException("供应商seller："+seller+"所对应退单returnSn："+returnSn+"商品不存在！");
			}
			List<StorageGoods> storageGoodsList = new ArrayList<StorageGoods>();
			for (OrderReturnGoods orderReturnGoods : returnGoodsList) {
				if (orderReturnGoods.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED.intValue()){
					throw new RuntimeException("退单returnSn:"+returnSn+"中商品customCode:"+orderReturnGoods.getCustomCode()+"已入库！");
				}
				StorageGoods storageGoods = new StorageGoods();
				storageGoods.setId(orderReturnGoods.getId());
				storageGoods.setProdScanNum(orderReturnGoods.getGoodsReturnNumber());
				storageGoodsList.add(storageGoods);
			}
			ReturnOrderParam returnOrderParam = new ReturnOrderParam();
			returnOrderParam.setUserName(returnStorageParam.getUserName());
			returnOrderParam.setStorageGoods(storageGoodsList);
			returnOrderParam.setPullInAll(false);
			returnOrderParam.setReturnSn(returnSn);
			returnOrderParam.setActionNote("第三方通过接口入库：");
			returnOrderParam.setToERP(returnStorageParam.isToERP());
			
			//更新仓库编码
			OrderReturnShip updateReturnShip = new OrderReturnShip();
			updateReturnShip.setRelatingReturnSn(returnSn);
			//校验仓库
			if (StringUtils.isEmpty(returnStorageParam.getDepotCode())) {
				throw new RuntimeException("仓库不能为空"); 
			}
			updateReturnShip.setDepotCode(returnStorageParam.getDepotCode());
			orderReturnShipMapper.updateByPrimaryKeySelective(updateReturnShip);
			
			// 已收货
			ReturnInfo<String> result = orderReturnStService.returnOrderReceive(returnOrderParam);
			if (result.getIsOk() > 0) {
				//质检通过
				result = orderReturnStService.returnOrderPass(returnOrderParam);
			}
			ReturnInfo<String> returnInfo = orderReturnStService.returnOrderStorage(returnOrderParam);
			if (returnInfo.getIsOk() == 0) {
				throw new RuntimeException("退单returnSn:"+returnSn+"中商品goods:"+JSON.toJSONString(storageGoodsList)+"入库失败,原因："+returnInfo.getMessage());
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("退单商品通过供应商入库失败,message:"+e.getMessage(),e);
			apiReturnData.setMessage("退单商品通过供应商入库失败,message:"+e.getMessage()+",e:"+e);
		}
		return apiReturnData;
	}
	
	@Override
	public ApiReturnData updateReturnInvoiceNo(ReturnShipUpdateInfo returnShipUpdateInfo){
		String orderSn = returnShipUpdateInfo.getOrderSn();
		String returnInvoiceNo = returnShipUpdateInfo.getReturnInvoiceNo();
		logger.info("OrderReturnServiceImpl-updateReturnInvoiceNo：orderSn:"+orderSn+"returnInvoiceNo:"+returnInvoiceNo+"returnExpress:"+returnShipUpdateInfo.getReturnExpress()+",操作人"+returnShipUpdateInfo.getUserName());
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk("0");
		apiReturnData.setMessage("更新订单所对应退单快递单号、承运商成功");
		try {
			if(StringUtils.isBlank(orderSn)){
				throw new RuntimeException("订单号为空！");
			}
			if(StringUtils.isBlank(returnInvoiceNo)){
				throw new RuntimeException("快递单号为空！");
			}
			OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(orderSn);
			if(orderInfo == null){
				throw new RuntimeException("订单"+orderSn+"不存在！");
			}
			OrderReturnExample example = new OrderReturnExample();
			example.or().andRelatingOrderSnEqualTo(orderSn);
			List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(example);
			if(CollectionUtils.isEmpty(orderReturnList)){
				throw new RuntimeException("订单"+orderSn+"关联退单不存在！");
			}
			for (OrderReturn orderReturn : orderReturnList) {
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderReturn.getReturnSn());
				OrderReturnShip updataShip = new OrderReturnShip();
				updataShip.setRelatingReturnSn(orderReturnShip.getRelatingReturnSn());
				if(StringUtils.isBlank(returnShipUpdateInfo.getReturnExpress())){
					updataShip.setReturnExpress(returnShipUpdateInfo.getReturnExpress());
				}
				if(!StringUtils.equalsIgnoreCase(orderReturnShip.getReturnInvoiceNo(), returnInvoiceNo)){
					updataShip.setReturnInvoiceNo(returnInvoiceNo);
					orderReturnShipMapper.updateByPrimaryKeySelective(updataShip);
				}
			}
			apiReturnData.setIsOk("1");
			
		} catch (Exception e) {
			logger.error("OrderReturnServiceImpl-updateReturnInvoiceNo，更新订单所对应退单快递单号出错："+e.getMessage(),e);
			apiReturnData.setMessage("更新订单"+orderSn+"所对应退单快递单号出错："+e.getMessage());
		}
		
		return apiReturnData;
	}
	
	@Override
	public ApiReturnData getRFDataByReturnInvoiceNo(String returnInvoiceNo) {
		String logText ="通过快递单号获取WMS入库所需要的信息";
		logger.info(logText+"开始,returnInvoiceNo:"+returnInvoiceNo);
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk("0");
		apiReturnData.setMessage(logText+"成功");
		try {
			WmsData wmsData = getWmsDataByReturnInvoiceNo(returnInvoiceNo);
			apiReturnData.setIsOk("1");
			apiReturnData.setData(wmsData);
		} catch (Exception e) {
			logger.error(logText+e.getMessage());
		}
		return apiReturnData;
	}
	
	private WmsData getWmsDataByReturnInvoiceNo(String returnInvoiceNo) {
		if(StringUtils.isBlank(returnInvoiceNo)){
			throw new RuntimeException("快递单号为空");
		}
		WmsData  wmsData = new WmsData();
		List<WmsReturnData> wmsReturnDataList = new ArrayList<WmsReturnData>();
		wmsData.setReturnInvoiceNo(returnInvoiceNo);
		//通过快递单号查询出与其关联的退单、订单、退单商品信息
		OrderReturnShipExample returnShipExample = new OrderReturnShipExample();
		returnShipExample.or().andReturnInvoiceNoEqualTo(returnInvoiceNo);
		List<OrderReturnShip> returnShipList = orderReturnShipMapper.selectByExample(returnShipExample);
		if(CollectionUtils.isNotEmpty(returnShipList)){
			for (OrderReturnShip orderReturnShip : returnShipList) {
				WmsReturnData wmsReturnData = new WmsReturnData();
				List<WmsReturnGoods> wmsReturnGoodsList = new ArrayList<WmsReturnGoods>();
				String returnSn = orderReturnShip.getRelatingReturnSn();
				
				//关联退单信息
				OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
				//排除无效、已入库和退款的退单
				if(orderReturn.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY.intValue()
						|| orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.intValue()
						|| orderReturn.getReturnType().intValue() > ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE){
					continue;
				}
				wmsData.setOrderSn(orderReturn.getRelatingOrderSn());
				wmsReturnData.setReturnSn(returnSn);
				
				//关联退单商品信息
				OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
				goodsExample.or().andRelatingReturnSnEqualTo(returnSn).andCheckinStatusNotEqualTo(ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE.byteValue());
				List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
				for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
					WmsReturnGoods wmsReturnGoods = new WmsReturnGoods();
					wmsReturnGoods.setId(orderReturnGoods.getId());
					wmsReturnGoods.setCustomCode(orderReturnGoods.getCustomCode());
					wmsReturnGoods.setGoodsReturnNumber(orderReturnGoods.getGoodsReturnNumber());
					wmsReturnGoods.setBarcode(orderReturnGoods.getBarcode());
//				  wmsReturnGoods.setProdScanNum(orderReturnGoods.getProdscannum());
					wmsReturnGoods.setRemark(orderReturnGoods.getRemark());
					wmsReturnGoods.setRelatingReturnSn(orderReturnGoods.getRelatingReturnSn());
					wmsReturnGoods.setSeller(orderReturnGoods.getSeller());
					if(orderReturnGoods.getSalesMode().intValue() != 4 && !StringUtils.equalsIgnoreCase(orderReturnGoods.getExtensionCode(), "c2m")
							&& !StringUtils.equalsIgnoreCase(orderReturnGoods.getExtensionCode(), "c2b")){//`sales_mode` tinyint(5) DEFAULT '1' COMMENT '商品销售模式：1为自营，2为买断，3为寄售，4为直发',
						//美邦商品
						wmsReturnGoods.setGoodsMark(0);
					}else{//第三方商品
						wmsReturnGoods.setGoodsMark(1);
					}
					
					wmsReturnGoodsList.add(wmsReturnGoods);
				}
				wmsReturnData.setWmsReturnGoods(wmsReturnGoodsList);
				
				wmsReturnDataList.add(wmsReturnData);
			}
			wmsData.setWmsReturnData(wmsReturnDataList);
			
		}
		return wmsData;
	}

	@Override
	public ApiReturnData returnStorageFromWMS(WmsData wmsDataParam) {
		String logText ="WMS入库";
		logger.info(logText+"开始,WmsData:"+JSON.toJSONString(wmsDataParam));
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk("0");
		apiReturnData.setMessage(logText+"成功");
		try {
			
			//通过快递单号查询出关联信息
			WmsData wnsData = getWmsDataByReturnInvoiceNo(wmsDataParam.getReturnInvoiceNo());
			logger.info("通过快递单号查询出关联信息:"+JSON.toJSONString(wnsData));
			String orderSn = wnsData.getOrderSn();
			
			//校验仓库
			if(StringUtils.isBlank(wmsDataParam.getDepotCode())){
				throw new RuntimeException("仓库不能为空");
			}
			
			//校验仓库（判断是否为oms配置仓库）
			ErpWarehouseListExample erpWarehouseListExample = new ErpWarehouseListExample();
			ErpWarehouseListExample.Criteria criteria = erpWarehouseListExample.or();
			List<ErpWarehouseList> depotList = erpWarehouseListMapper.selectByExample(erpWarehouseListExample);
			boolean depotFlag = false;
			if(CollectionUtils.isNotEmpty(depotList)){
				for (ErpWarehouseList erpWarehouseList : depotList) {
				   if(StringUtils.equalsIgnoreCase(wmsDataParam.getDepotCode(), erpWarehouseList.getWarehouseCode())){
					   depotFlag = true;
					   break;
				   }
				}
			}
			if(!depotFlag){
				throw new RuntimeException("所传仓库("+wmsDataParam.getDepotCode()+")未配置！");
			}
			
			List<WmsReturnGoods> returnGoodsList = new ArrayList<WmsReturnGoods>();
			List<WmsReturnGoods> storageGoodList = new ArrayList<WmsReturnGoods>();
			List<ReturnOrderParam> finalStorageList = new ArrayList<ReturnOrderParam>();
			//校验商品数量
			int originalNum = 0;
			int storageNum =0;
			
			for(WmsReturnData wmsReturnData : wnsData.getWmsReturnData()){
				ReturnOrderParam returnOrderParam = new ReturnOrderParam();
				returnOrderParam.setReturnSn(wmsReturnData.getReturnSn());
				finalStorageList.add(returnOrderParam);
				
				//更新仓库编码
				OrderReturnShip updateReturnShip = new OrderReturnShip();
				updateReturnShip.setRelatingReturnSn(wmsReturnData.getReturnSn());
				updateReturnShip.setDepotCode(wmsDataParam.getDepotCode());
				orderReturnShipMapper.updateByPrimaryKeySelective(updateReturnShip);
				
				for(WmsReturnGoods wmsReturnGoods : wmsReturnData.getWmsReturnGoods()){
					OrderReturnGoods returnGood = orderReturnGoodsMapper.selectByPrimaryKey(wmsReturnGoods.getId());
					wmsReturnGoods.setProdScanNum(returnGood.getProdscannum());
					wmsReturnGoods.setSettlementPrice(returnGood.getSettlementPrice().doubleValue());
					originalNum += wmsReturnGoods.getGoodsReturnNumber();
					returnGoodsList.add(wmsReturnGoods);
				}
			}
			
			for(WmsReturnData storageReturnData:wmsDataParam.getWmsReturnData()){
				for(WmsReturnGoods storageReturnGoods : storageReturnData.getWmsReturnGoods()){
					storageNum += storageReturnGoods.getProdScanNum();
					storageGoodList.add(storageReturnGoods);
				}
			}
			
			if(originalNum != storageNum){
				if(!wmsDataParam.isPartStorage()){//不支持部分入库
					throw new RuntimeException("订单"+orderSn+"扫描入库商品数量"+storageNum+"与原订单商品数量"+originalNum+"不符！");
				}
			}
			
			//对比商品进行入库
			boolean deleteFlag = true;
			boolean againBeginFlag = false;
			for(int i =0;i<storageGoodList.size();){
				againBeginFlag = false;
				WmsReturnGoods storageReturnGoods = storageGoodList.get(i);
				for(WmsReturnGoods wmsReturnGoods : returnGoodsList){
					deleteFlag = true;
					if(storageGoodList.size() <= 0){
						break;
					}
					//只有商品11位码和国标码同时相同时
					if(StringUtils.equalsIgnoreCase(storageReturnGoods.getCustomCode(), wmsReturnGoods.getCustomCode()) 
							&& StringUtils.equalsIgnoreCase(storageReturnGoods.getBarcode(), wmsReturnGoods.getBarcode())){
						String returnSn = wmsReturnGoods.getRelatingReturnSn();
						
						//拼装最终的入库数据
						for(ReturnOrderParam finalData : finalStorageList){
							if(StringUtils.equalsIgnoreCase(finalData.getReturnSn(), returnSn)){
								StorageGoods storageGoods = new StorageGoods();
								if(storageReturnGoods.getProdScanNum()+wmsReturnGoods.getProdScanNum() <= wmsReturnGoods.getGoodsReturnNumber() ){
									//当扫描入库数量少于原退单商品数量
									storageGoods.setProdScanNum(storageReturnGoods.getProdScanNum());
									wmsReturnGoods.setProdScanNum(storageReturnGoods.getProdScanNum()+wmsReturnGoods.getProdScanNum());
								}else{//当扫描入库数量大于原退单商品数量
									deleteFlag = false;
									storageGoods.setProdScanNum(wmsReturnGoods.getGoodsReturnNumber());
									storageReturnGoods.setProdScanNum(storageReturnGoods.getProdScanNum()+wmsReturnGoods.getProdScanNum()-wmsReturnGoods.getGoodsReturnNumber());
									wmsReturnGoods.setProdScanNum(wmsReturnGoods.getGoodsReturnNumber());
									
								}
								storageGoods.setId(wmsReturnGoods.getId());
								storageGoods.setCustomCode(wmsReturnGoods.getCustomCode());
								storageGoods.setSettlementPrice(wmsReturnGoods.getSettlementPrice());
								storageGoods.setRemark(storageReturnGoods.getRemark());
								if(CollectionUtils.isEmpty(finalData.getStorageGoods())){
									finalData.setStorageGoods(new ArrayList<StorageGoods>());
								}
								finalData.getStorageGoods().add(storageGoods);
								if(deleteFlag){
									againBeginFlag = true;
									storageGoodList.remove(i);
								}
								break;
							}
						}
						if(againBeginFlag){//当入库商品已经比对上，并且扫描数量和退货量相等时
							break;
						}
					}
				}
				i++;
				if(againBeginFlag){
					i=0;
				}
			}
			
			//再次校验入库商品是否还有没有对应上的商品（要是全部对应上了，storageGoodList就应该为空）
			if(CollectionUtils.isNotEmpty(storageGoodList)){
				logger.info("入库商品中有与订单不匹配的商品:"+JSON.toJSONString(storageGoodList));
				StringBuffer goodsBuff = new StringBuffer();
				goodsBuff.append("(");
				for( int i=0;i<storageGoodList.size();i++){
					WmsReturnGoods wmsReturnGoods = storageGoodList.get(i);
					goodsBuff.append(wmsReturnGoods.getCustomCode());
					if(i != storageGoodList.size()-1){
						goodsBuff.append(",");
					}
				}
				goodsBuff.append(")");
				throw new RuntimeException("商品或商品扫描件数不匹配:"+goodsBuff.toString());
			}
			
			for(ReturnOrderParam finalData : finalStorageList){
				if(CollectionUtils.isEmpty(finalData.getStorageGoods())){
					continue;
				}
				ReturnOrderParam returnOrderParam = new ReturnOrderParam();
				returnOrderParam.setUserName(wmsDataParam.getUserName());
//			  returnOrderParam.setGoodsId(finalData.getStorageGoods());
				returnOrderParam.setPullInAll(false);
				returnOrderParam.setReturnSn(finalData.getReturnSn());
				returnOrderParam.setStorageGoods(finalData.getStorageGoods());
				returnOrderParam.setActionNote("WMS通过接口入库：");
				returnOrderParam.setToERP(true);
				
				logger.info("WMS入库 ReturnOrderParam："+JSON.toJSONString(returnOrderParam));
				OrderReturn checkReturn = orderReturnMapper.selectByPrimaryKey(finalData.getReturnSn());
				ReturnInfo<ChannelShop> info = channelInfoService.findShopInfoByShopCode(checkReturn.getChannelCode());
				ChannelShop channelShop = new ChannelShop();
				if (null != info && info.getIsOk() == Constant.OS_YES && null != info.getData()) {
					channelShop = info.getData();
				} else {
					throw new RuntimeException("店铺("+checkReturn.getChannelCode()+")不允许rf扫描入库!");
				}
				if(Integer.valueOf(channelShop.getShopType()) == 1){//加盟店铺不允许rf扫描入库
					throw new RuntimeException("加盟店铺("+checkReturn.getChannelCode()+")不允许rf扫描入库!");
				}
				//已收货
				ReturnInfo<String> result = orderReturnStService.returnOrderReceive(returnOrderParam);
				if(result.getIsOk() > 0){
					//质检通过
					result = orderReturnStService.returnOrderPass(returnOrderParam);
				}
				ReturnInfo<String> returnInfo =orderReturnStService.returnOrderStorage(returnOrderParam);
				if(returnInfo.getIsOk() == 0){
					throw new RuntimeException(returnInfo.getMessage());
				}
			}
			apiReturnData.setIsOk("1");
			
		} catch (Exception e) {
			logger.error(logText+"出错,message:"+e.getMessage());
			apiReturnData.setMessage(e.getMessage());
		}
		return apiReturnData;
	}

	@Override
	public ReturnInfo orderExpress(String orderSn) {
//		return orderExpressPullService.orderExpress(orderSn);
		return null;
	}
	
	@Override
	public ReturnInfo orderReturnExpress(OrderReturn orderReturn, OrderReturnShip orderReturnShip) {
//		return orderExpressPullService.orderReturnExpress(orderReturn, orderReturnShip);
		return null;
	}

    /**
     * 获取有效退单列表
     * @param masterOrderSn
     * @return
     */
	@Override
	public ReturnInfo<List<OrderReturn>> geteffectiveReturns(String masterOrderSn) {
		ReturnInfo<List<OrderReturn>> info = new ReturnInfo<List<OrderReturn>>(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]传入参数为空！");
			info.setMessage("[masterOrderSn]传入参数为空！");
			return info;
		}
		OrderReturnExample returnExample = new OrderReturnExample();
		returnExample.or().andMasterOrderSnEqualTo(masterOrderSn).andReturnOrderStatusNotEqualTo((byte) 4);
		List<OrderReturn> orderReturns = orderReturnMapper.selectByExample(returnExample);
		info.setIsOk(Constant.OS_YES);
		info.setData(orderReturns);
		info.setMessage("查询成功！");
		return info;
	}
}
