package com.work.shop.oms.orderReturn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.api.param.bean.ExchangeOrderBean;
import com.work.shop.oms.api.param.bean.ExchangePageOrder;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderAddressInfoExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.SystemPaymentWithBLOBs;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.orderReturn.service.OrderExchangeStService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;

@Service
public class OrderExchangeStServiceImpl implements OrderExchangeStService {

	private static Logger logger = Logger.getLogger(OrderExchangeStServiceImpl.class);
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	
	@Resource
	private MasterOrderInfoService masterOrderInfoService;
	
	@Resource
	private MasterOrderInfoExtendMapper  masterOrderInfoExtendMapper  ;
	
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	
	@Resource(name="systemPaymentMapper")
	private SystemPaymentMapper systemPaymentMapper;
	
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	
	@Resource(name="orderReturnService")
	private OrderReturnService orderReturnService;
	
	@Resource(name="orderCancelService")
	private OrderCancelService orderCancelService;
	
	@Resource
	private OrderReturnStService orderReturnStService;
	
	@Resource(name="orderReturnMapper")
	private OrderReturnMapper orderReturnMapper;
	
	@Resource(name="orderReturnShipMapper")
	private OrderReturnShipMapper orderReturnShipMapper;
	
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private OrderActionService orderActionService;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private RedisClient redisClient;
	
	
	/**
	 * 换货单只是针对商品，赠品也可以换货，但是只能用赠品进行换货
	 * 
	 * 
	 */
	@Override
	public ReturnInfo createNewExchangeOrder(ExchangeOrderBean exchangeOrderBean) {
		logger.info("createNewExchangeOrder.begin...relatingOrderSn:"+exchangeOrderBean.getRelatingOrderSn()+",exchangeOrderBean:"+JSON.toJSONString(exchangeOrderBean));
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo.setIsOk(Constant.YESORNO_NO);
		returnInfo.setMessage("换货单创建失败");
		String relOrderActionNote = StringUtils.EMPTY;
		String exchangeActionNote = StringUtils.EMPTY;
		String relatingOrderSn = StringUtils.EMPTY;//原订单号
		String exchangeOrderSn = StringUtils.EMPTY;//换货单号
		String relatingReturnSn = StringUtils.EMPTY;//关联退货单
		try {
			//页面数据
			ExchangePageOrder pageOrder = exchangeOrderBean.getPageOrder();
			List<MasterGoods> pageGoods = exchangeOrderBean.getPageGoods();
			if (com.work.shop.oms.utils.StringUtil.isListNull(pageGoods)) {
				 returnInfo.setMessage("换货单商品为空！");
				 return returnInfo;
			}
			//换单数据组装
			MasterOrder oInfo = new MasterOrder();
			relatingOrderSn = exchangeOrderBean.getRelatingOrderSn();
			returnInfo.setRelatingOrderSn(relatingOrderSn);
			OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(relatingOrderSn);
			MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderDistribute.getMasterOrderSn());
			if(orderInfo == null){
				throw new RuntimeException("原订单不存在请迁移原订单("+relatingOrderSn+")后再进行换单操作");
			}
			int orderType = 0; // 未发货:0;已发货:1;
			if (orderInfo.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLSHIPED) {
				orderType = 1;
			}
			processOrderInfo(orderInfo, pageOrder, oInfo);
			Integer bvValue = 0;
			Integer baseBvValue = 0;
			for (MasterGoods masterGoods :pageGoods) {
				bvValue += masterGoods.getBvValue();
				baseBvValue += masterGoods.getBaseBvValue();
			}
			oInfo.setBvValue(bvValue);
			oInfo.setBaseBvValue(baseBvValue);
			processOrderShip(orderDistribute.getMasterOrderSn(), pageOrder, oInfo);
//			processOrderGoods(pageGoods, oInfo);
			oInfo.getShipList().get(0).setGoodsList(pageGoods);
			exchangeOrderBean.setMasterOrderSn(orderInfo.getMasterOrderSn());
			processOrderPay(exchangeOrderBean,oInfo, orderType);
			
			long result = -1;
			try {
				result = redisClient.setnx("exchange_relating_order_sn" + exchangeOrderBean.getRelatingOrderSn() , exchangeOrderBean.getRelatingOrderSn());
				// redis 超时时间为5秒钟
				redisClient.expire("exchange_relating_order_sn" + exchangeOrderBean.getRelatingOrderSn(), 5);
			} catch (Throwable e) {
				logger.error("退单保存：判断同一时间内用户多次提交,读取redis异常:" + e);
			}
			if (result == 0) {
				throw new RuntimeException("短时间(5s)内用户不允许多次提交同一换单！");
			}
			//生成主单
			OrderCreateReturnInfo omsReturn = masterOrderInfoService.createOrder(oInfo);
			if(omsReturn.getIsOk() == Constant.OS_YES){
				exchangeOrderSn = omsReturn.getMasterOrderSn();
				relOrderActionNote += "换货单创建成功" + exchangeOrderSn;
				exchangeActionNote += "换货单创建成功" + exchangeOrderSn;
				
				//更新换货单号到退货单中
				exchangeOrderBean.getOrderReturnBean().getCreateOrderReturn().setNewOrderSn(exchangeOrderSn);
				//更新原订单号到换货单中
				MasterOrderInfo updateExchangeOrder = new MasterOrderInfo();
				updateExchangeOrder.setMasterOrderSn(exchangeOrderSn);
				updateExchangeOrder.setRelatingOriginalSn(orderDistribute.getMasterOrderSn());
				updateExchangeOrder.setUpdateTime(new Date());
				masterOrderInfoMapper.updateByPrimaryKeySelective(updateExchangeOrder);
				
				//退单生成
				CreateOrderReturnBean orderReturnBean = buildOrderReturn(exchangeOrderBean);
				ReturnInfo returnMsg = orderReturnService.createOrderReturn(orderReturnBean);
				String returnSn = returnMsg.getReturnSn();
				//确认退单
				ReturnInfo confirmResult = orderReturnStService.returnOrderConfirm(returnSn, "退单确认：", exchangeOrderBean.getActionUser());
				logger.info("退单确认："+JSON.toJSON(confirmResult));
				logger.info("createNewExchangeOrder.createOrderReturn.returnMsg:"+JSON.toJSONString(returnMsg)+",orderReturnBean:"+JSON.toJSONString(orderReturnBean));
				if(returnMsg != null && returnMsg.getIsOk() > 0){
					relOrderActionNote += ",退单创建成功 "+returnMsg.getReturnSn();
					relatingReturnSn = returnMsg.getReturnSn();
					returnInfo.setReturnSn(returnMsg.getReturnSn());
					
					//更新退货单号到换货单中
					updateExchangeOrder = new MasterOrderInfo();
					updateExchangeOrder.setMasterOrderSn(exchangeOrderSn);
					updateExchangeOrder.setRelatingReturnSn(returnMsg.getReturnSn());
					updateExchangeOrder.setUpdateTime(new Date());
					masterOrderInfoMapper.updateByPrimaryKeySelective(updateExchangeOrder);
					// 原订单是未发货，需要取消订单
					if (orderInfo.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_UNSHIPPED) {
						String cancelCode = "8023";
						String cancelReason = "换货订单取消";
						
						// 主单取消
						/*MasterOrderInfo updateMaster = new MasterOrderInfo();
						updateMaster.setMasterOrderSn(orderDistribute.getMasterOrderSn());
						updateMaster.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CANCLED);
						updateMaster.setCancelCode(cancelCode);
						updateMaster.setCancelReason(cancelReason);
						updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
						updateMaster.setUpdateTime(new Date());
						masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
						masterOrderActionService.insertOrderActionBySn(orderDistribute.getMasterOrderSn(), "订单换货取消", exchangeOrderBean.getActionUser());
						OrderDistribute updateDistribute = new OrderDistribute();
						updateDistribute.setQuestionStatus(ConstantValues.ORDERQUESTION_STATUS.NORMAL);
						updateDistribute.setQuestionTime(null);
						updateDistribute.setOrderStatus((byte) Constant.OI_ORDER_STATUS_CANCLED);
						updateDistribute.setCancelCode(cancelCode);
						updateDistribute.setCancelReason(cancelReason);
						updateDistribute.setOrderSn(relatingOrderSn);
						updateDistribute.setUpdateTime(new Date());
						orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
						distributeActionService.addOrderAction(relatingOrderSn, "订单换货取消", exchangeOrderBean.getActionUser());*/
						
						/*OrderStatus orderStatus = new OrderStatus();
						orderStatus.setMasterOrderSn(orderInfo.getMasterOrderSn());
						orderStatus.setAdminUser(orderInfo.getUserName());
						orderStatus.setMessage("订单换货取消");
						orderStatus.setCode(cancelCode);
						orderStatus.setType("2");//是否创建退单 1：创建;2：不创建
						orderStatus.setSource("OMS");//取消来源 POS:POS端;FRONT:前端;OMS:后台取消
						orderCancelService.cancelOrderByMasterSn(orderDistribute.getMasterOrderSn(), orderStatus);*/
					}
				}else{
					throw new RuntimeException("换货单创建成功("+exchangeOrderSn+")但关联退货单创建失败,"+returnMsg.getMessage());
				}
			}else{
				throw new RuntimeException("换货单创建失败，"+omsReturn.getMessage());
			}
			
			returnInfo.setIsOk(Constant.YESORNO_YES);
			returnInfo.setOrderSn(exchangeOrderSn);
			returnInfo.setMessage("换货单创建成功("+exchangeOrderSn+");退单创建成功("+relatingReturnSn+").");
		} catch (Exception e) {
			relOrderActionNote += ","+e.getMessage();
			logger.error("原订单" + exchangeOrderBean.getRelatingOrderSn() + "createNewExchangeOrder. 失败"+e.getMessage(),e);
			returnInfo.setIsOk(Constant.YESORNO_NO);
			returnInfo.setMessage(relOrderActionNote);
		}finally{
			if(StringUtils.isNotBlank(relOrderActionNote)){
				distributeActionService.addOrderAction(relatingOrderSn, relOrderActionNote, exchangeOrderBean.getActionUser());
			}
			if(StringUtils.isNotBlank(exchangeActionNote)){
				masterOrderActionService.insertOrderActionBySn(exchangeOrderSn, exchangeActionNote, exchangeOrderBean.getActionUser());
			}
		}
		return returnInfo;
	}
	
	/**
	 * 订单主题组装
	 * @param orderInfo
	 * @param pageOrder
	 * @param oInfo
	 */
	private void processOrderInfo(MasterOrderInfo orderInfo,ExchangePageOrder pageOrder,MasterOrder oInfo){
		oInfo.setAddTime(new Date());
		oInfo.setActionUser(orderInfo.getUserId());
		MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(orderInfo.getMasterOrderSn());
		oInfo.setUseLevel(masterOrderInfoExtend.getUseLevel());
		oInfo.setChannelUserLevel(masterOrderInfoExtend.getChannelUserLevel());
		oInfo.setUserId(orderInfo.getUserId());
		oInfo.setTransType(ConstantValues.ORDER_INFO_TRANS_TYPE.PAYMENT_BEFORE_DELIVERY);//换单统一设置成款到发货
		oInfo.setReferer(orderInfo.getReferer());
		oInfo.setOrderFrom(orderInfo.getOrderFrom());
		oInfo.setPayTotalFee(0D);
		oInfo.setInvPayee(masterOrderInfoExtend.getInvPayee());
		oInfo.setInvType(masterOrderInfoExtend.getInvType());
		oInfo.setInvContent(masterOrderInfoExtend.getInvContent());
		oInfo.setParentId(orderInfo.getParentId());
		oInfo.setTax(masterOrderInfoExtend.getTax().doubleValue());
		oInfo.setOutletType(orderInfo.getOutletType());
		oInfo.setInsureTotalFee(orderInfo.getInsureTotalFee().doubleValue());
		oInfo.setOrderType(orderInfo.getOrderType().byteValue());
		oInfo.setPostscript(orderInfo.getPostscript());
		oInfo.setSourceCode(orderInfo.getSourceCode());
		oInfo.setSource(orderInfo.getSource());//线上订单
		oInfo.setSource(orderInfo.getSource());//线上订单
		oInfo.setRegisterMobile(orderInfo.getRegisterMobile());
		oInfo.setBvValue(orderInfo.getBvValue());
		oInfo.setBaseBvValue(orderInfo.getBaseBvValue());
		oInfo.setInsteadUserId(orderInfo.getInsteadUserId());
		//页面更新
		oInfo.setOrderType(ConstantValues.ORDERRETURN_RELATIN_ORDER_TYPE.EXCHANGE.byteValue());
		//oInfo.setOrderStatus(ConstantValues.ORDER_STATUS.CONFIRMED.intValue());
		//oInfo.setPayStatus(payStatus);
		oInfo.setGoodsAmount(pageOrder.getGoodsAmount());//商品总金额
		oInfo.setDiscount(pageOrder.getDiscount());//订单折扣
		oInfo.setShippingTotalFee(pageOrder.getShippingTotalFee());//运费
		oInfo.setTotalFee(pageOrder.getTotalFee());//订单总金额
		oInfo.setMoneyPaid(pageOrder.getMoneyPaid());//已付款金额
		oInfo.setBonus(pageOrder.getBonus());//红包
		oInfo.setTotalPayable(pageOrder.getTotalPayable());//应再付款金额
	}
	
	/**
	 * 发货单组装
	 * @param orderSn
	 * @param pageOrder
	 * @param oInfo
	 */
	private void processOrderShip(String orderSn,ExchangePageOrder pageOrder,MasterOrder oInfo){
		MasterOrderAddressInfoExample orderAddressInfoExample = new MasterOrderAddressInfoExample();
		orderAddressInfoExample.or().andMasterOrderSnEqualTo(orderSn);
		List<MasterOrderAddressInfo> orderAddressInfoList = masterOrderAddressInfoMapper.selectByExample(orderAddressInfoExample);
		if(CollectionUtils.isEmpty(orderAddressInfoList)){
			throw new RuntimeException("原订单下用户地址信息列表为空 ");
		}
		MasterOrderAddressInfo orderAddressInfo = orderAddressInfoList.get(0);
		MasterShip ship = new MasterShip();
		
		ship.setZipcode(orderAddressInfo.getZipcode());
		ship.setMobile(orderAddressInfo.getMobile());
		ship.setTel(orderAddressInfo.getTel());
		ship.setSignBuilding(orderAddressInfo.getSignBuilding());
		ship.setEmail(orderAddressInfo.getEmail());
		ship.setShippingDays(orderAddressInfo.getShippingDays().shortValue());
//		ship.setDeliveryType(orderAddressInfo.getDeliveryType().byteValue());
		ship.setConsignee(orderAddressInfo.getConsignee());
		ship.setShippingCode("99");
		ship.setCacCode(orderAddressInfo.getCacCode());

		ship.setAddress(orderAddressInfo.getAddress());
		ship.setCountry(orderAddressInfo.getCountry());
		ship.setProvince(orderAddressInfo.getProvince());
		ship.setCity(orderAddressInfo.getCity());
		ship.setDistrict(orderAddressInfo.getDistrict());
		ship.setAreaCode(orderAddressInfo.getAreaCode());
		ship.setShippingAddress(orderAddressInfo.getShippingAddress());
		//页面更新
//		ship.setShippingFee(pageOrder.getShippingTotalFee());
		
		List<MasterShip> shipList = new ArrayList<MasterShip>();
		shipList.add(ship);
		oInfo.setShipList(shipList);
	}
	
	/**
	 * 发货单组装
	 * @param orderSn
	 * @param oInfo
	 * @param orderType 未发货:0;已发货:1;
	 * 不考虑红包ID，不考虑红包金额
	 */
	private void processOrderPay(ExchangeOrderBean exchangeOrderBean,MasterOrder oInfo, int orderType){
		List<MasterPay> payList = new ArrayList<MasterPay>();
		/*Byte payId = 0;
		if(exchangeOrderBean.getPageOrder().getPayId() != null 
				&& exchangeOrderBean.getPageOrder().getPayId().intValue() != 0){
			payId = exchangeOrderBean.getPageOrder().getPayId().byteValue();
		}*/
//		SystemPaymentWithBLOBs payment = systemPaymentMapper.selectByPrimaryKey(payId);
//		if(payment == null){
//			throw new RuntimeException("原订单付款方式无效");
//		}
		MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
		orderPayExample.or().andMasterOrderSnEqualTo(exchangeOrderBean.getMasterOrderSn());
		List<MasterOrderPay> orderPayList = masterOrderPayMapper.selectByExample(orderPayExample);
//		List<PayType> orderPayList = JSON.parseArray(orderDistributeMapper.selectByPrimaryKey(exchangeOrderBean.getRelatingOrderSn()).getPayInfo(), PayType.class);
		if(CollectionUtils.isEmpty(orderPayList)){
			throw new RuntimeException("原订单付款单列表为空");
		}
		//原订单付款方式只有一种余额支付，并且换单设置余额支付，则不能创建换单
		/*if(orderPayList.size() == 1 && payId.intValue() == 3){
			throw new RuntimeException("换货单创建不能使用 [全部余额支付] 付款方式 ");
		}*/
		/*if (orderType == 0) {
			for (MasterOrderPay orderPay : orderPayList) {
				MasterPay pay = new MasterPay();
				//已付款订单
				SystemPaymentWithBLOBs payment = systemPaymentMapper.selectByPrimaryKey(orderPay.getPayId());
				if(payment == null){
					throw new RuntimeException("原订单付款方式无效");
				}
				pay.setPayId(Integer.valueOf(orderPay.getPayId()));
				pay.setPayCode(payment.getPayCode());
				pay.setPayTime(new Date());
				//针对退货金额待确认，在退货单入库后做已付款处理
				pay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.WAIT_CONFRIM);
				pay.setPayTotalFee(orderPay.getPayTotalfee().doubleValue());
				pay.setPayNote(orderPay.getPayNote());
				payList.add(pay);
			}
		} else {
			MasterPay pay = new MasterPay();
			pay.setPayId(3);
			pay.setPayCode("balance");
			pay.setPayTime(new Date());
			//针对退货金额待确认，在退货单入库后做已付款处理
			pay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.WAIT_CONFRIM);
			pay.setPayTotalFee(oInfo.getMoneyPaid());
			pay.setPayNote("");
			payList.add(pay);
		}*/
		MasterPay pay = new MasterPay();
		pay.setPayId(3);
		pay.setPayCode("balance");
		pay.setPayTime(new Date());
		//针对退货金额待确认，在退货单入库后做已付款处理
		pay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.WAIT_CONFRIM);
		pay.setPayTotalFee(oInfo.getMoneyPaid());
		pay.setPayNote("");
		payList.add(pay);
		ExchangePageOrder pageOrder = exchangeOrderBean.getPageOrder();
		double totalPayable = pageOrder.getTotalPayable();
		if(totalPayable > 0){
			MasterPay unPay = new MasterPay();
			//换货 > 退货 应补款totalPayable
			unPay.setPayId(3);
			unPay.setPayCode("balance");
			//payCode
			unPay.setPayTotalFee(totalPayable);
			unPay.setPayTime(new Date());
			unPay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.PAYED);
			payList.add(unPay);
			oInfo.setSurplus(totalPayable);
			oInfo.setTotalPayable(0D);
			oInfo.setPayStatus((short)Constant.OI_PAY_STATUS_PARTPAYED);
		}
		oInfo.setPayList(payList);
	}

	/**
	 * 退单数据修正
	 * @param exchangeOrderBean
	 * @return
	 */
	private CreateOrderReturnBean buildOrderReturn(ExchangeOrderBean exchangeOrderBean){
		CreateOrderReturnBean orderReturnBean = exchangeOrderBean.getOrderReturnBean();
		orderReturnBean.setActionUser(exchangeOrderBean.getActionUser());
		return orderReturnBean;
	}
	
	@Override
	public ReturnInfo cancelExchangeOrder(String orderSn,String actionNote, String actionUser,Integer userId) {
		logger.debug("cancelExchangeOrder.begin...orderSn:"+orderSn+",actionNote:"+actionNote+",actionUser:"+actionUser+";userId:"+userId);
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setOrderSn(orderSn);
		MasterOrderInfo orderInfo = null;
		try {
			orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
			if(orderInfo == null){
				throw new RuntimeException("换货单不存在");
			}
			
			//验证退单
			if(StringUtils.isBlank(orderInfo.getRelatingReturnSn())){
				throw new RuntimeException("换货单关联退货单不存在");
			}
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderInfo.getRelatingReturnSn());
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息");
			}
			if(orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS){
				throw new RuntimeException("换货单关联退单类型非退货单");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderInfo.getRelatingReturnSn());
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效的退单仓库信息");
			}
			if(orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
				throw new RuntimeException("换货单关联退货单的货物已入库，不能再取消");
			}
			//调用Oms作废换货单,针对差额生成退款单
			String cancelCode = "8023";
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(orderInfo.getMasterOrderSn());
			orderStatus.setAdminUser(orderInfo.getUserName());
			orderStatus.setMessage("Oms作废换货单");
			orderStatus.setCode(cancelCode);
			orderStatus.setType("1");//是否创建退单 1：创建;2：不创建
			orderStatus.setSource("OMS");//取消来源 POS:POS端;FRONT:前端;OMS:后台取消
			/*OrderCancelMessage cancelMsg = orderCancelService.cancelOrder(orderInfo, cancelCode, actionNote, actionUser, true, ConstantValues.METHOD_SOURCE_TYPE.OMS,
					ConstantValues.CREATE_RETURN.YES);*/
			ReturnInfo<String> cancelMsg = orderCancelService.cancelOrderByMasterSn(orderInfo.getMasterOrderSn(), orderStatus);
			logger.info("cancelExchangeOrder.cancelOrderForOM.exchangeOrderSn:"+orderInfo.getMasterOrderSn()+",response:"+JSON.toJSONString(cancelMsg));
			if(cancelMsg.getIsOk()>0){
				actionNote += "换货单取消成功;";
				//作废退单
				ReturnInfo cancelReturnMsg = orderReturnStService.returnOrderInvalid(orderInfo.getRelatingReturnSn(), actionNote, actionUser);
				logger.debug("cancelExchangeOrder.returnOrderInvalid.exchangeOrderSn:"+orderInfo.getMasterOrderSn()+",returnSn:"+orderInfo.getRelatingReturnSn()+",response:"+JSON.toJSONString(cancelReturnMsg));
				if(cancelReturnMsg != null && cancelReturnMsg.getIsOk() > 0){
					actionNote += "关联退货单取消成功("+orderInfo.getRelatingReturnSn()+");";
				}else{
					actionNote += "关联退货单取消失败("+orderInfo.getRelatingReturnSn()+")," + cancelReturnMsg.getMessage() ;
				}
			}else{
				actionNote += "换货单取消失败，" + cancelMsg.getMessage();
				throw new RuntimeException("换货单取消失败，" + cancelMsg.getMessage());
			}
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("cancelExchangeOrder,换货订单"+orderSn+"，退货单：" + orderInfo.getRelatingReturnSn() + ", 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setIsOk(ConstantValues.YESORNO_NO);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		}
		return returnInfo;
	}
	
	@Override
	public ReturnInfo onlyReturnGoods(String orderSn,String actionUser, String actionNote,Integer userId) {
		logger.debug("onlyReturnGoods.begin...orderSn:"+orderSn+",actionNote:"+actionNote+",actionUser:"+actionUser+";userId:"+userId);
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		returnInfo.setMessage("操作失败");
		returnInfo.setOrderSn(orderSn);
		MasterOrderInfo orderInfo = null;
		try {
			//验证换单、退单
			orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
			if(orderInfo == null){
				throw new RuntimeException("作废的换单不存在");
			}
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderInfo.getRelatingReturnSn());
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息");
			}
			// 作废换货新订单
			if (actionNote == null || "".equals(actionNote)) {
				actionNote += "作废换货单(保留退货)：对换货订单【" + orderSn + "】执行“只退货”操作;<br />";
			} else {
				actionNote += "作废换货单(保留退货)：" + actionNote.trim() + ";<br />";
			}
			//作废换货单
		  //调用Oms作废换货单,针对差额生成退款单
			String cancelCode = "8023";
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(orderInfo.getMasterOrderSn());
			orderStatus.setAdminUser(orderInfo.getUserName());
			orderStatus.setMessage("Oms作废换货单");
			orderStatus.setCode(cancelCode);
			orderStatus.setType(ConstantValues.CREATE_RETURN.NO);//是否创建退单 1：创建;2：不创建
			orderStatus.setSource("OMS");//取消来源 POS:POS端;FRONT:前端;OMS:后台取消
			/*OrderCancelMessage cancelMsg = orderCancelService.cancelOrder(orderInfo, cancelCode, actionNote, actionUser, true, ConstantValues.METHOD_SOURCE_TYPE.OMS,
					ConstantValues.CREATE_RETURN.YES);*/
			ReturnInfo<String> cancelMsg = orderCancelService.cancelOrderByMasterSn(orderInfo.getMasterOrderSn(), orderStatus);
			logger.info("cancelExchangeOrder.cancelOrderForOM.exchangeOrderSn:"+orderInfo.getMasterOrderSn()+",response:"+JSON.toJSONString(cancelMsg));
			if(cancelMsg.getIsOk()>0){
				returnInfo.setIsOk(ConstantValues.YESORNO_YES);
				actionNote += "换货单取消成功！<br />";
			}else{
				returnInfo.setIsOk(ConstantValues.YESORNO_NO);
				actionNote += "换货单取消失败，" + cancelMsg.getMessage()+"<br />";
				throw new RuntimeException("换货单取消失败，" + cancelMsg.getMessage());
			}
			
			//换货单取消-不生成退款单
		/*  //作废换单取消生成的退款单
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andRelatingOrderSnEqualTo(orderSn).andRefundTypeEqualTo(ConstantValues.ORDERRETURN_REFUND_TYPE.CANCEL_ORDER);
			List<OrderReturn> orderReturnForCannelList = orderReturnMapper.selectByExample(orderReturnExample);
			if(CollectionUtils.isEmpty(orderReturnForCannelList)){
				throw new RuntimeException("换单取消生成的退款单不存在");
			}
			OrderReturn orderReturnForCannel = orderReturnForCannelList.get(0);
			ReturnInfo cannelReturnResult = orderReturnStService.returnOrderInvalid(orderReturnForCannel.getReturnSn(), "换单["+orderSn+"]仅退货操作，取消退款单"+orderReturnForCannel.getReturnSn(), actionUser);
			logger.debug("换单进退货操作，换单取消生成的退款单作废:"+JSON.toJSONString(cannelReturnResult));
			if(cannelReturnResult.getIsOk() > 0){
				actionNote += "换单取消生成退款单作废成功["+orderReturnForCannel.getReturnSn()+"]";
			}else{
				actionNote += "换单取消生成退款单作废失败["+orderReturnForCannel.getReturnSn()+"]";
			}*/
			//更新换单对应退货单需要退款、退单处理方式变为退货
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andNewOrderSnEqualTo(orderSn).andReturnTypeEqualTo(ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS);
			List<OrderReturn> orderReturnForGoodsList = orderReturnMapper.selectByExample(orderReturnExample);
			if(CollectionUtils.isEmpty(orderReturnForGoodsList)){
				throw new RuntimeException("换单对应生成的退货单不存在");
			}
			OrderReturn orderReturnForGoods = orderReturnForGoodsList.get(0);
			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(orderReturnForGoods.getReturnSn());
			updateOrderReturn.setHaveRefund(ConstantValues.YESORNO_YES.byteValue());
			updateOrderReturn.setProcessType(ConstantValues.ORDERRETURN_PROCESS_TYPE.RETURN);
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);
			orderActionService.addOrderReturnAction(orderReturnForGoods.getReturnSn(), "", actionUser);
			actionNote += "换单对应退货单更新为 需要退款["+orderReturnForGoods.getReturnSn()+"]";
			
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("onlyReturnGoods,新订单"+orderSn+"，退货单：" + orderInfo.getRelatingReturnSn() + ", 操作失败，Msg："+e.getMessage(),e);
			returnInfo.setIsOk(ConstantValues.YESORNO_NO);
			returnInfo.setMessage("操作失败:"+e.getMessage());
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				masterOrderActionService.insertOrderActionBySn(orderSn, actionNote, actionUser);
			}
		}
		return returnInfo;
	}
}
