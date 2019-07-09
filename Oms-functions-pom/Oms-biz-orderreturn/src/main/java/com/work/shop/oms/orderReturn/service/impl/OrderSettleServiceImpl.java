package com.work.shop.oms.orderReturn.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.bean.OrderReturnSkuInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderGoods;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.OrderReturnShipExample;
import com.work.shop.oms.bean.OrderSettleBill;
import com.work.shop.oms.bean.OrderSettleBillExample;
import com.work.shop.oms.bean.ProductBarcodeListExample;
import com.work.shop.oms.bean.SettleGoodsInfo;
import com.work.shop.oms.bean.SettleOrderInfo;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.OrderSettleBillMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderMonitorService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.param.bean.LstSfSchTaskExecOosDtls;
import com.work.shop.oms.param.bean.OrderSettleParam;
import com.work.shop.oms.param.bean.SfSchTaskExecOosInfo;
import com.work.shop.oms.utils.CommonUtils;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.OrderSettleGoods;
import com.work.shop.oms.vo.SettleBillQueue;
import com.work.shop.oms.vo.SettleParamObj;
import com.work.shop.oms.vo.StorageGoods;

@Service
public class OrderSettleServiceImpl implements OrderSettleService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "orderSettleProviderJmsTemplate")
	private JmsTemplate jmsTemplateForOrder;
	
	@Resource(name = "returnInputProviderJmsTemplate")
	private JmsTemplate jmsTemplateForReturnInput;
	
	@Resource(name = "orderDistributeMapper")
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	
//	@Resource(name = "productBarcodeListMapper")
//	private ProductBarcodeListMapper productBarcodeListMapper;
	
	@Resource(name = "orderReturnMapper")
	private OrderReturnMapper orderReturnMapper;
	
	@Resource(name = "orderReturnShipMapper")
	private OrderReturnShipMapper orderReturnShipMapper;
	
	@Resource(name = "orderReturnGoodsMapper")
	private OrderReturnGoodsMapper orderReturnGoodsMapper;
	
	//@Resource
	private UserPointsService userPointsService;
	
	@Resource(name = "orderMonitorServiceImpl")
	private OrderMonitorService orderMonitorService;
	
	@Resource
	private OrderActionService orderActionService;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	private OrderBillListMapper orderBillListMapper;
	@Resource
	private OrderSettleBillMapper orderSettleBillMapper;
	@Resource
	private OrderReturnService orderReturnService;
	
	
	/***
	 * 结算-普通订单
	 * 1.根据订单号获取有效地订单数据
	 * 2.将订单数据放入到队列中
	 * 3.积分处理
	 * 4.调用异常处理
	 */
	@Override
	public ReturnInfo<String> settleOrderPoint(SettleParamObj paramObj) {
		Assert.notNull(paramObj, "paramObj is null");
		logger.info("[OrderSettleService.settleNormalOrder]start  paramObj:"+JSON.toJSONString(paramObj));
		ReturnInfo<String> returnInfo = new ReturnInfo<String>(Constant.OS_NO);
		if (paramObj == null) {
			returnInfo.setMessage("参数[paramObj] 不能为空");
			return returnInfo;
		}
		String orderSn = paramObj.getDealCode();//子单号，具体结算只针对子订单
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(orderSn);
		returnInfo.setMessage("订单完成赠送失败");
		OrderSettleParam settleObj = new OrderSettleParam();
		
		String actionNote = StringUtils.EMPTY;
		String pointsNote = StringUtils.EMPTY;
		try {
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
			if (masterOrderInfo.getClearTime() != null 
					|| masterOrderInfo.getPayStatus().intValue() == Constant.OI_PAY_STATUS_SETTLED ) {
				returnInfo.setMessage("订单[" + orderSn + "] 已经结算");
				return returnInfo;
			}
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			//工具推送添加订单结算时间
			updateMaster.setUpdateTime(new Date());
			
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(orderSn).andIsDelNotEqualTo(ConstantValues.YESORNO_YES);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			
			//拼装数据
			settleObj.setShopId(masterOrderInfo.getOrderFrom());
			settleObj.setOrderNo(masterOrderInfo.getMasterOrderSn());
			logger.info("[OrderSettleService.settleNormalOrder]process dealCode:" + paramObj.getDealCode()
					+ ",updateTime:"+updateMaster.getUpdateTime());
			Date clearTime = new Date();
			settleObj.setDocDate(TimeUtil.formatDate(clearTime));
			settleObj.setSellerId(goodsList.get(0).getSelleruser());
			settleObj.setSaleMode(ConstantValues.ORDER_SELLTE_SALEMODE.SALE);
			settleObj.setO2oSaleType("B2C");
			List<OrderSettleGoods> settleDetails = settleObj.getDetails();
			OrderSettleGoods settleItem = null;
			settleObj.setBussType(paramObj.getBussType());//业务类型：结算
			double totalSettlementPrice = 0D;
			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
			List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
			double totalSettlement = 0d;
			
			//同Sku商品合并
			List<MasterOrderGoods> itemGoodsList = new ArrayList<MasterOrderGoods>();
			Map<String,List<MasterOrderGoods>> goodsListMap = new HashMap<String,List<MasterOrderGoods>>();
			for (MasterOrderGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				
				SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
				settleGoodsInfo.setGoodssku(customCode);
				settleGoodsInfo.setCost(orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue());
				settleGoodsInfo.setCount(orderGoods.getGoodsNumber().intValue());
				settleGoodsInfo.setGoodsname(orderGoods.getGoodsName());
				settleGoodsInfo.setUnitprice(orderGoods.getGoodsPrice().doubleValue());
				settleGoodsInfo.setRate(CommonUtils.roundDouble(orderGoods.getTransactionPrice().doubleValue()/orderGoods.getGoodsPrice().doubleValue(),2));
				//结算撤销积分扣减
				/*if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_SETTLE_CANCLE){
					settleGoodsInfo.setCost(0-orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue());
				}*/
				settleGoodsInfo.setBvvalue(Integer.valueOf(orderGoods.getBvValue()) * orderGoods.getGoodsNumber());
				settleGoodsInfoList.add(settleGoodsInfo);
				totalSettlement += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue();
				
				if(goodsListMap.containsKey(customCode+orderGoods.getSettlementPrice().toString())){
					itemGoodsList = goodsListMap.get(customCode+orderGoods.getSettlementPrice().toString());
				}else{
					itemGoodsList = new ArrayList<MasterOrderGoods>();
				}
				orderGoods.setCustomCode(customCode);
				itemGoodsList.add(orderGoods);
				goodsListMap.put(customCode+orderGoods.getSettlementPrice().toString(), itemGoodsList);
			}
			int ttlQty = 0;
			List<OrderGoods> newGoodsList = new ArrayList<OrderGoods>();
			for (String codeKey : goodsListMap.keySet()) {
				itemGoodsList = goodsListMap.get(codeKey);
				double settleMoney = 0D;
				int goodsNumber = 0;
				String selleruser = StringUtils.EMPTY;
				String containerid = StringUtils.EMPTY;
				for (MasterOrderGoods orderGoods : itemGoodsList) {
					settleMoney += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber().intValue();
					goodsNumber += orderGoods.getGoodsNumber().intValue();
					if(StringUtils.isBlank(selleruser)){
						selleruser = orderGoods.getSelleruser();
					}
					if(StringUtils.isBlank(containerid)){
						containerid = orderGoods.getContainerid();
					}
				}
				double singleSettleMoney = settleMoney / goodsNumber;
				OrderGoods itemGoods = new OrderGoods();
				itemGoods.setCustomCode(codeKey);
				itemGoods.setSelleruser(selleruser);
				itemGoods.setContainerid(containerid);
				itemGoods.setSettlementPrice(BigDecimal.valueOf(settleMoney / goodsNumber));
				itemGoods.setGoodsNumber((short)goodsNumber);
				newGoodsList.add(itemGoods);
				
				//最终数据拼装
				settleItem = new OrderSettleGoods();
				settleItem.setProdId(itemGoodsList.get(0).getCustomCode());//十一位商品编码
				settleItem.setUnitPrice(CommonUtils.roundDouble(singleSettleMoney, 2));//单个财务价
				settleItem.setQuantity(Double.valueOf(goodsNumber));//数量
				ttlQty = ttlQty + goodsNumber;
				settleItem.setSellerId(selleruser);
				settleItem.setLocId(containerid);
				settleItem.setAmount(CommonUtils.roundDouble(settleMoney, 2));//总财务价
				totalSettlementPrice += settleItem.getAmount();
				settleDetails.add(settleItem);
			}
			
			settleObj.setTtlQty(Double.valueOf(ttlQty));
			settleObj.setTtlVal(totalSettlementPrice);
			settleObj.setDetails(settleDetails);
			
			settleOrderInfo.setUvid(masterOrderInfo.getUserId());
			settleOrderInfo.setOrdersn(orderSn);
			settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
			settleOrderInfo.setGuideid(masterOrderInfo.getOrderFrom());
			settleOrderInfo.setShopid(masterOrderInfo.getOrderFrom());
			settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
			settleOrderInfo.setTime(new Date().getTime());
			
			//赠送消费积分Math.floor(-2.1) 平台送积分(当再次推送订单到ERP时不再进行积分扣除)
			if (!masterOrderInfo.getChannelCode().equals(Constant.Chlitina)) {
				if(!paramObj.isTools()){
					int point = (int)Math.floor(totalSettlement);
					ReturnInfo<String> pointResponse = userPointsService.processUserPoints(settleOrderInfo);
					if(pointResponse.getIsOk() > 0){
						logger.info("订单结算-结算数据时CAS积分("+point+")赠送成功！orderSn:"+orderSn+",userId:"+masterOrderInfo.getUserId()+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "订单结算-结算数据时CAS积分("+point+")赠送成功！";
					}else{
						logger.info("订单结算-结算数据时CAS积分("+point+")赠送失败！orderSn:"+orderSn+",userId:"+masterOrderInfo.getUserId()+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "订单结算-结算数据时CAS积分("+point+")赠送失败！错误信息:"+pointResponse.getMessage();
						throw new Exception(pointsNote);
					}
				}
			}
			// 更新主单结算状态
			updateMaster.setMasterOrderSn(orderSn);
			updateMaster.setPayStatus((byte) Constant.OI_PAY_STATUS_SETTLED);
			updateMaster.setOrderStatus((byte) Constant.OI_ORDER_STATUS_FINISHED);
			updateMaster.setClearTime(new Date());
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			logger.info(">>>>settleNormalOrder.orderSn:"+orderSn+",settleObj:"+JSON.toJSONString(settleObj));
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(orderSn);
			returnInfo.setMessage("子单结算失败，错误信息:"+e.getMessage());
			actionNote = "订单结算-结算数据推送失败，错误信息:"+e.getMessage();
			//业务监控
			logger.error("settleNormalOrder-exception-orderSn:"+orderSn+",Msg："+e.getMessage(),e);
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				masterOrderActionService.insertOrderActionBySn(orderSn, actionNote, Constant.OS_STRING_SYSTEM);
			}
			if(StringUtils.isNotBlank(pointsNote)){
				masterOrderActionService.insertOrderActionBySn(orderSn, pointsNote, Constant.OS_STRING_SYSTEM);
			}
		}
		logger.info("[OrderSettleService.settleNormalOrder]end.......returnInfo:"+JSON.toJSONString(returnInfo));
		return returnInfo;
	}
	
	@Override
	public ReturnInfo<String> MasterOrderSettle(SettleParamObj paramObj) {
		logger.info("主单结算 start.paramObj:"+JSON.toJSONString(paramObj));
		Assert.notNull(paramObj, "paramObj is null");
		String masterOrderSn = paramObj.getDealCode();//主单号
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(Constant.OS_NO);
		returnInfo.setOrderSn(masterOrderSn);
		returnInfo.setMessage("订单结算失败");
		String actionNote = "订单结算（操作者"+paramObj.getUserId()+"）：";
		try {
			MasterOrderInfo order = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(order == null){
				throw new RuntimeException("无法获取有效的主订单信息！");
			}
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo(ConstantValues.ORDER_STATUS.CANCELED);
			List<OrderDistribute> orderDistributeList = orderDistributeMapper.selectByExample(distributeExample);
			for (OrderDistribute orderDistribute : orderDistributeList) {
				//更新数据
				OrderDistribute updateOrderInfo = new OrderDistribute();
				updateOrderInfo.setOrderSn(orderDistribute.getOrderSn());
				updateOrderInfo.setOrderStatus(Integer.valueOf(Constant.OI_ORDER_STATUS_FINISHED).byteValue());
				updateOrderInfo.setPayStatus(ConstantValues.ORDER_PAY_STATUS.SETTLEMENT.byteValue());
				updateOrderInfo.setUpdateTime(new Date());
				orderDistributeMapper.updateByPrimaryKeySelective(updateOrderInfo);
				if (!paramObj.isTools()) {
					//校验子订单
					ReturnInfo<String> checkResult = checkOrderDistribute(orderDistribute.getOrderSn());
					if (checkResult.getIsOk() < 1) {
						throw new RuntimeException(checkResult.getMessage());
					}
				}
			}
			//修改主订单状态
			MasterOrderInfo updateMasterOrderInfo = new MasterOrderInfo();
			updateMasterOrderInfo.setMasterOrderSn(masterOrderSn);
			updateMasterOrderInfo.setPayStatus(ConstantValues.ORDER_PAY_STATUS.SETTLEMENT.byteValue());
			updateMasterOrderInfo.setOrderStatus((byte)Constant.OI_ORDER_STATUS_FINISHED);
			updateMasterOrderInfo.setClearTime(new Date());
			MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
			masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
			for (MasterOrderPay masterOrderPay : masterOrderPayList) {
				MasterOrderPay updatePay = new MasterOrderPay();
				updatePay.setMasterPaySn(masterOrderPay.getMasterPaySn());
				updatePay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.SETTLEMENT.byteValue());
				masterOrderPayMapper.updateByPrimaryKeySelective(updatePay);
			}
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMasterOrderInfo);
			actionNote +="订单结算成功！";
			returnInfo.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			actionNote +=" 订单结算出错:" + e.getMessage();
			returnInfo.setOrderSn(masterOrderSn);
			returnInfo.setMessage("订单结算出错:"+e.getMessage());
			logger.error(actionNote, e);
		} finally {
			if (StringUtils.isNotBlank(actionNote)) {
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, actionNote, paramObj.getUserId()); 
			}
		}
		return returnInfo;
	}
	
	private ReturnInfo<String> checkOrderDistribute(String orderSn) {
		Assert.notNull(orderSn, "paramObj is null");
		logger.info("订单结算:子单校验：start.......orderSn:"+orderSn);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(Constant.OS_NO);
		returnInfo.setOrderSn(orderSn);
		returnInfo.setMessage("订单结算子单校验：");
		try {
			if(StringUtils.isBlank(orderSn)){
				throw new RuntimeException("结算参数中单据编码为空！");
			}
			OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderSn);
			if(order == null){
				throw new RuntimeException("无法获取有效的订单信息！");
			}
			
			OrderDepotShipExample shipExample = new OrderDepotShipExample();
			shipExample.or().andOrderSnEqualTo(orderSn);
			List<OrderDepotShip> shipList = orderDepotShipMapper.selectByExample(shipExample);
			if(CollectionUtils.isEmpty(shipList)){
				throw new RuntimeException("无法获取有效的发货单！");
			}
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andOrderSnEqualTo(orderSn);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(goodsList)){
				throw new RuntimeException("无法获取有效的商品列表！");
			}
			for (MasterOrderGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("商品编码为空");
				}
			}
			returnInfo.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			returnInfo.setOrderSn(orderSn);
			returnInfo.setMessage(" 订单结算子单校验出错，错误信息:"+e.getMessage());
		}
		return returnInfo;
	}

	@Override
	public ReturnInfo<String> OrderSettleCancle(SettleParamObj paramObj) {
		Assert.notNull(paramObj, "paramObj is null");
		String masterOrderSn = paramObj.getDealCode();//主单号
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(masterOrderSn);
		returnInfo.setMessage("订单结算撤销：");
		String actionNote = "订单结算撤销（操作者"+paramObj.getUserId()+"）：";
		logger.info("[OrderSettleService.OrderSettleCancle]start.......dealCode:"+paramObj.getDealCode()+",paramObj:"+JSON.toJSONString(paramObj));
		try {
			MasterOrderInfo order = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(order == null){
				throw new RuntimeException("无法获取有效的主订单信息！");
			}
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo(ConstantValues.ORDER_STATUS.CANCELED);
			List<OrderDistribute> orderDistributeList = orderDistributeMapper.selectByExample(distributeExample);
			for (OrderDistribute orderDistribute : orderDistributeList) {
				
				SettleParamObj param = new SettleParamObj();
				param.setDealCode(orderDistribute.getOrderSn());
				param.setBussType(Constant.ERP_BUSS_TYPE_SETTLE_CANCLE);
				param.setTools(false);
				ReturnInfo<String> returnVal = this.settleNormalOrder(param);
				if(returnVal.getIsOk() < 1){
					throw new RuntimeException(returnVal.getMessage());
				}
				
			}
			
			//修改主订单状态
			MasterOrderInfo updateMasterOrderInfo = new MasterOrderInfo();
			updateMasterOrderInfo.setMasterOrderSn(masterOrderSn);
			updateMasterOrderInfo.setPayStatus(ConstantValues.ORDER_PAY_STATUS.PAYED.byteValue());
			updateMasterOrderInfo.setOrderStatus(ConstantValues.ORDER_STATUS.CONFIRMED);
//			updateMasterOrderInfo.setClearTime(new Date());
			
			Double moneyPay = 0d;
			
			MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
			masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
			
			for (MasterOrderPay masterOrderPay : masterOrderPayList) {
				MasterOrderPay updatePay = new MasterOrderPay();
				updatePay.setMasterPaySn(masterOrderPay.getMasterPaySn());
				updatePay.setPayStatus(ConstantValues.OP_ORDER_PAY_STATUS.PAYED.byteValue());
				masterOrderPayMapper.updateByPrimaryKeySelective(updatePay);
				moneyPay += masterOrderPay.getPayTotalfee().doubleValue();
			}
			//付款单金额 - 余额-积分使用金额-红包 = 已支付金额
			moneyPay = moneyPay - order.getSurplus().doubleValue()-order.getIntegralMoney().doubleValue()-order.getBonus().doubleValue();
			//货到付款的订单
			if(order.getTransType().intValue() == ConstantValues.ORDER_INFO_TRANS_TYPE.CASH_ON_DELIVERY){ 
				updateMasterOrderInfo.setMoneyPaid(BigDecimal.valueOf(0));
				updateMasterOrderInfo.setTotalPayable(BigDecimal.valueOf(moneyPay));
			}
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMasterOrderInfo);
			
			actionNote +="订单结算撤销成功！";
			
				
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(masterOrderSn);
			returnInfo.setMessage(" 订单结算撤销出错:"+e.getMessage());
			actionNote +=" 订单结算撤销出错:"+e.getMessage();
			logger.error(returnInfo.getMessage(), e);
		}finally{
			if(StringUtils.isNotBlank(actionNote)){
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, actionNote, paramObj.getUserId()); 
			}
		}
	   
		return returnInfo;
	}
	
	/***
	 * 结算-普通订单
	 * 1.根据订单号获取有效地订单数据
	 * 2.将订单数据放入到队列中
	 * 3.积分处理
	 * 4.调用异常处理
	 */
	@Override
	public ReturnInfo<String> settleNormalOrder(SettleParamObj paramObj) {
		Assert.notNull(paramObj, "paramObj is null");
		logger.info("[OrderSettleService.settleNormalOrder]start.......dealCode:"+paramObj.getDealCode()+",paramObj:"+JSON.toJSONString(paramObj));
		String orderSn = paramObj.getDealCode();//子单号，具体结算只针对子订单
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(orderSn);
		returnInfo.setMessage("子单结算结算操作！");
		OrderSettleParam settleObj = new OrderSettleParam();
		
		String actionNote = StringUtils.EMPTY;
		String pointsNote = StringUtils.EMPTY;
		
		try {
			OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderSn);
			
			//工具推送添加订单结算时间
			order.setUpdateTime(new Date());
			orderDistributeMapper.updateByPrimaryKeySelective(order);
			
			
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andOrderSnEqualTo(orderSn).andIsDelNotEqualTo(ConstantValues.YESORNO_YES);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			
			//拼装数据
			settleObj.setShopId(order.getOrderFrom());
			settleObj.setOrderNo(order.getOrderSn());
			DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			logger.info("[OrderSettleService.settleNormalOrder]process.......dealCode:"+paramObj.getDealCode()+",updateTime:"+order.getUpdateTime());
			
			Date clearTime = order.getUpdateTime() == null ? new Date() : order.getUpdateTime();
			settleObj.setDocDate(formatDate.format(clearTime));
			settleObj.setSellerId(goodsList.get(0).getSelleruser());
			settleObj.setSaleMode(ConstantValues.ORDER_SELLTE_SALEMODE.SALE);
			if(StringUtils.equalsIgnoreCase("YHJ", order.getReferer())){
				settleObj.setO2oSaleType(order.getReferer());			   
			}else{
				settleObj.setO2oSaleType("B2C");
			}
			List<OrderSettleGoods> settleDetails = settleObj.getDetails();
			OrderSettleGoods settleItem = null;
			settleObj.setBussType(paramObj.getBussType());//业务类型：结算
			double totalSettlementPrice = 0D;
			
			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
			List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
			double totalSettlement = 0d;
			
			//同Sku商品合并
			List<MasterOrderGoods> itemGoodsList = new ArrayList<MasterOrderGoods>();
			Map<String,List<MasterOrderGoods>> goodsListMap = new HashMap<String,List<MasterOrderGoods>>();
			for (MasterOrderGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				
				SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
				settleGoodsInfo.setGoodssku(customCode);
				settleGoodsInfo.setCost(orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue());
				settleGoodsInfo.setCount(orderGoods.getGoodsNumber().intValue());
				settleGoodsInfo.setGoodsname(orderGoods.getGoodsName());
				settleGoodsInfo.setUnitprice(orderGoods.getGoodsPrice().doubleValue());
				settleGoodsInfo.setRate(CommonUtils.roundDouble(orderGoods.getTransactionPrice().doubleValue()/orderGoods.getGoodsPrice().doubleValue(),2));
			   //结算撤销积分扣减
				if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_SETTLE_CANCLE){
					settleGoodsInfo.setCost(0-orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue());
				}
				settleGoodsInfoList.add(settleGoodsInfo);
				totalSettlement += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue();
				
			  
				if(goodsListMap.containsKey(customCode+orderGoods.getSettlementPrice().toString())){
					itemGoodsList = goodsListMap.get(customCode+orderGoods.getSettlementPrice().toString());
				}else{
					itemGoodsList = new ArrayList<MasterOrderGoods>();
				}
				orderGoods.setCustomCode(customCode);
				itemGoodsList.add(orderGoods);
				goodsListMap.put(customCode+orderGoods.getSettlementPrice().toString(), itemGoodsList);
			}
			int ttlQty = 0;
			List<OrderGoods> newGoodsList = new ArrayList<OrderGoods>();
			for (String codeKey : goodsListMap.keySet()) {
				itemGoodsList = goodsListMap.get(codeKey);
				double settleMoney = 0D;
				int goodsNumber = 0;
				String selleruser = StringUtils.EMPTY;
				String containerid = StringUtils.EMPTY;
				for (MasterOrderGoods orderGoods : itemGoodsList) {
					
					settleMoney += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber().intValue();
					goodsNumber += orderGoods.getGoodsNumber().intValue();
					if(StringUtils.isBlank(selleruser)){
						selleruser = orderGoods.getSelleruser();
					}
					if(StringUtils.isBlank(containerid)){
						containerid = orderGoods.getContainerid();
					}
				}
				double singleSettleMoney = settleMoney / goodsNumber;
				OrderGoods itemGoods = new OrderGoods();
				itemGoods.setCustomCode(codeKey);
				itemGoods.setSelleruser(selleruser);
				itemGoods.setContainerid(containerid);
				itemGoods.setSettlementPrice(BigDecimal.valueOf(settleMoney / goodsNumber));
				itemGoods.setGoodsNumber((short)goodsNumber);
				newGoodsList.add(itemGoods);
				
				
				//最终数据拼装
				settleItem = new OrderSettleGoods();
				settleItem.setProdId(itemGoodsList.get(0).getCustomCode());//十一位商品编码
				settleItem.setUnitPrice(CommonUtils.roundDouble(singleSettleMoney, 2));//单个财务价
				settleItem.setQuantity(Double.valueOf(goodsNumber));//数量
				ttlQty = ttlQty + goodsNumber;
				settleItem.setSellerId(selleruser);
				settleItem.setLocId(containerid);
				settleItem.setAmount(CommonUtils.roundDouble(settleMoney, 2));//总财务价
				totalSettlementPrice += settleItem.getAmount();
				settleDetails.add(settleItem);
				
				//业务监控
				orderMonitorService.monitorForOrderSettle(orderSn, order.getOrderFrom(), codeKey, goodsNumber, settleItem.getUnitPrice());
			}
			
			settleObj.setTtlQty(Double.valueOf(ttlQty));
			settleObj.setTtlVal(totalSettlementPrice);
			settleObj.setDetails(settleDetails);
			
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(order.getMasterOrderSn());
			
			settleOrderInfo.setUvid(masterOrderInfo.getUserId());
			settleOrderInfo.setOrdersn(orderSn);
			settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
			settleOrderInfo.setGuideid(ConstantValues.HQ01S116);
			settleOrderInfo.setShopid(ConstantValues.HQ01S116);
			settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
			settleOrderInfo.setTime(new Date().getTime());
			
			//赠送消费积分Math.floor(-2.1) 平台送积分(当再次推送订单到ERP时不再进行积分扣除)
			if(!paramObj.isTools()){
				if(StringUtils.equalsIgnoreCase("YHJ", order.getReferer()) || StringUtils.equalsIgnoreCase(ConstantValues.HQ01S116, order.getOrderFrom())){
					int point = (int)Math.floor(totalSettlement);
					ReturnInfo<String> pointResponse = userPointsService.processUserPoints(settleOrderInfo);
					if(pointResponse.getIsOk() > 0){
						logger.info("订单结算-结算数据时CAS积分("+point+")赠送成功！orderSn:"+orderSn+",userId:"+masterOrderInfo.getUserId()+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "订单结算-结算数据时CAS积分("+point+")赠送成功！";
					}else{
						logger.info("订单结算-结算数据时CAS积分("+point+")赠送失败！orderSn:"+orderSn+",userId:"+masterOrderInfo.getUserId()+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "订单结算-结算数据时CAS积分("+point+")赠送失败！错误信息:"+pointResponse.getMessage();
					}
				}
			}
			
			//结算撤销积分扣减,子单更改订单状态
			if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_SETTLE_CANCLE){
				OrderDistribute updateOrderDistribute = new OrderDistribute();
				updateOrderDistribute.setOrderSn(orderSn);
				updateOrderDistribute.setOrderStatus(Constant.OD_ORDER_STATUS_CONFIRMED);
				updateOrderDistribute.setPayStatus(Constant.OI_PAY_STATUS_PAYED);
				orderDistributeMapper.updateByPrimaryKeySelective(updateOrderDistribute);
			}
			
			  //放入队列
			logger.info(">>>>settleNormalOrder.orderSn:"+orderSn+",settleObj:"+JSON.toJSONString(settleObj));
			sendMessage(jmsTemplateForOrder, JSON.toJSONString(settleObj));
			if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_SETTLE_CANCLE){
				actionNote = "子单结算撤销-数据成功推向ERP";
			}else{
				actionNote = "子单结算-数据成功推向ERP";
			}
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(orderSn);
			returnInfo.setMessage(" 子单结算失败，错误信息:"+e.getMessage());
			actionNote = " 子单结算-结算数据推送失败，错误信息:"+e.getMessage();
			
			//业务监控
			orderMonitorService.monitorForOrderSettleException(orderSn, "子单结算失败:"+e.getMessage());

			logger.error("settleNormalOrder-exception-orderSn:"+orderSn+",Msg："+e.getMessage(),e);
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				distributeActionService.addOrderAction(orderSn, actionNote); 
			}
			if(StringUtils.isNotBlank(pointsNote)){
				distributeActionService.addOrderAction(orderSn, pointsNote); 
			}
		}
		logger.info("[OrderSettleService.settleNormalOrder]end.......returnInfo:"+JSON.toJSONString(returnInfo));
		return returnInfo;
	}
	
	/***
	 * 结算-退货单
	 * 1.根据订单号获取有效地订单数据
	 * 2.分析订单数据
	 * 3.将分析好数据放入到队列中
	 * 3.调用异常处理
	 */
	@Override
	public ReturnInfo<String> settleReturnOrder(SettleParamObj paramObj){
		Assert.notNull(paramObj, "paramObj is null");
		logger.info("[OrderSettleService.settleReturnOrder]start.......dealCode:"+paramObj.getDealCode()+",paramObj:"+JSON.toJSONString(paramObj));
		
		String returnSn = paramObj.getDealCode();
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(returnSn);
		returnInfo.setMessage("退货单订单结算操作！");
		
		OrderSettleParam settleObj = new OrderSettleParam();
		String actionNote = StringUtils.EMPTY;
		String pointsNote = StringUtils.EMPTY;
		if(StringUtils.isBlank(returnSn)){
			throw new RuntimeException("结算单据编码为空！");
		}
		
		try {
			if(paramObj.getBussType().intValue() != Constant.ERP_BUSS_TYPE_SETTLE 
					&& paramObj.getBussType().intValue() != Constant.ERP_BUSS_TYPE_SETTLE_ADJUST){
				throw new RuntimeException("OS通知ERP业务类型异常BussType:"+paramObj.getBussType());
			}
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息！");
			}
			//使用工具推送退单的时候，添加结算时间
			orderReturn.setClearTime(new Date());
			if(orderReturn.getInitClearTime() == null){
				orderReturn.setInitClearTime(new Date());
			}
			orderReturnMapper.updateByPrimaryKeySelective(orderReturn);
			
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效的退单发货信息！");
			}
			if(orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() &&
					orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue() &&
					orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
				throw new RuntimeException("只有退货单和拒收入库单才能进行结算或入库操作！");
			}
			if(!paramObj.isTools()){
				if(orderReturn.getPayStatus().intValue() != ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED.intValue()){
					throw new RuntimeException("退货单非已结算状态！");
				}
				if(orderReturnShip.getCheckinStatus().intValue() == ConstantValues.YESORNO_NO.intValue()){
					throw new RuntimeException("退货单非已入库状态不可进行结算操作！");
				}
				if(orderReturnShip.getQualityStatus().intValue() == ConstantValues.YESORNO_NO.intValue()){
					throw new RuntimeException("退货单非质检通过不可进行结算操作！");
				}
			}
			OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			if(order == null){
				throw new RuntimeException("无法获取有效的订单信息！");
			}
			if(StringUtils.equalsIgnoreCase("全流通", order.getReferer())){
				throw new RuntimeException("全流通订单退货无法进行结算操作！");
			}
			OrderReturnShipExample shipExample = new OrderReturnShipExample();
			shipExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnShip> shipList = orderReturnShipMapper.selectByExample(shipExample);
			if(CollectionUtils.isEmpty(shipList)){
				throw new RuntimeException("无法获取有效的退单发货单！");
			}
			
			OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
			goodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> goodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(goodsList)){
				throw new RuntimeException("无法获取退货单下有效的商品信息！");
			}
			
			//拼装数据
			settleObj.setShopId(order.getOrderFrom());
			settleObj.setOrderNo(order.getOrderSn());
			settleObj.setReturnNo(returnSn);
			DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date clearTime = orderReturn.getClearTime() == null ? new Date() : orderReturn.getClearTime();
			logger.info("[OrderSettleService.settleReturnOrder]process.......dealCode:"+paramObj.getDealCode()+",clearTime:"+orderReturn.getClearTime());
			
			settleObj.setDocDate(formatDate.format(clearTime));//退单下单时间
			settleObj.setTtlQty(orderReturn.getReturnAllgoodsCount().doubleValue());
			settleObj.setSaleMode(ConstantValues.ORDER_SELLTE_SALEMODE.RETURN);//退货
			if(StringUtils.equalsIgnoreCase("YHJ", order.getReferer())){
				settleObj.setO2oSaleType(order.getReferer());//FIXME huangl 暂时取原订单的Referer
			}else{
				settleObj.setO2oSaleType("B2C");
			}
			settleObj.setDepotCode(shipList.get(0).getDepotCode());//退货处理仓-可为空
			settleObj.setBussType(paramObj.getBussType());
			List<OrderSettleGoods> settleDetails = settleObj.getDetails();
			OrderSettleGoods settleItem = null;
			
			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
			List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
			double totalSettlement = 0d;
			
			//总价格：财物价格 + 红包分摊额
			double totalUserPayAmt = 0d;
			List<OrderReturnSkuInfo> returnSkuList = new ArrayList<OrderReturnSkuInfo>();
			//2015-05-14 同Sku商品合并
			List<OrderReturnGoods> itemGoodsList = new ArrayList<OrderReturnGoods>();
			Map<String,List<OrderReturnGoods>> goodsListMap = new HashMap<String,List<OrderReturnGoods>>();
			for (OrderReturnGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("无法获取有效的退货商品编码！");
				}
				
				SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
				settleGoodsInfo.setGoodssku(customCode);
				settleGoodsInfo.setCost(0-orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsReturnNumber().doubleValue());
				settleGoodsInfo.setCount(orderGoods.getGoodsReturnNumber().intValue());
				settleGoodsInfo.setGoodsname(orderGoods.getGoodsName());
				settleGoodsInfo.setUnitprice(orderGoods.getMarketPrice().doubleValue());
				settleGoodsInfo.setRate(CommonUtils.roundDouble(orderGoods.getGoodsPrice().doubleValue()/orderGoods.getMarketPrice().doubleValue(),2));
				settleGoodsInfoList.add(settleGoodsInfo);
				totalSettlement += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsReturnNumber().doubleValue();
				
				ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				productExample.or().andCustumCodeEqualTo(customCode);
				/*List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}*/
				if(goodsListMap.containsKey(customCode+orderGoods.getSettlementPrice().toString())){
					itemGoodsList = goodsListMap.get(customCode+orderGoods.getSettlementPrice().toString());
				}else{
					itemGoodsList = new ArrayList<OrderReturnGoods>();
				}
				itemGoodsList.add(orderGoods);
				goodsListMap.put(customCode+orderGoods.getSettlementPrice().toString(), itemGoodsList);
			}
			int ttlQty = 0;
			for (String codeKey : goodsListMap.keySet()) {
				itemGoodsList = goodsListMap.get(codeKey);
				double settleMoney = 0D;
				int goodsNumber = 0;
				for (OrderReturnGoods orderGoods : itemGoodsList) {
					
					settleMoney += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsReturnNumber().intValue();						
					goodsNumber += orderGoods.getGoodsReturnNumber();
				}
				double singleSettleMoney = settleMoney / goodsNumber;
				//最终数据拼装
				settleItem = new OrderSettleGoods();
				settleItem.setProdId(itemGoodsList.get(0).getCustomCode());//十一位商品编码
				settleItem.setUnitPrice(CommonUtils.roundDouble(singleSettleMoney,2));//单个财务价
				settleItem.setQuantity(Double.valueOf(goodsNumber));//数量
				settleItem.setAmount(CommonUtils.roundDouble(settleMoney,2));//总财务价
				totalUserPayAmt += settleItem.getAmount();
				ttlQty += settleItem.getQuantity().intValue();
				settleDetails.add(settleItem);
				
				OrderReturnSkuInfo returnSku = new OrderReturnSkuInfo();
				returnSku.setSkuSn(codeKey);
				returnSku.setGoodsNum(goodsNumber);
				returnSku.setGoodsPrice(singleSettleMoney);
				returnSkuList.add(returnSku);
				
				//业务监控
				orderMonitorService.monitorForReturnSettle(returnSn, orderReturn.getChannelCode(), codeKey, settleItem.getUnitPrice(),goodsNumber);
			}
			settleObj.setTtlQty(Integer.valueOf(ttlQty).doubleValue());
			settleObj.setTtlVal(totalUserPayAmt);
			settleObj.setDetails(settleDetails);
			
			settleOrderInfo.setUvid(getUserId(order));
			settleOrderInfo.setOrdersn(returnSn);
			settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
			settleOrderInfo.setGuideid(ConstantValues.HQ01S116);
			settleOrderInfo.setShopid(ConstantValues.HQ01S116);
			settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
			settleOrderInfo.setTime(new Date().getTime());
			
			//扣除消费积分  
			if(!paramObj.isTools()){
				if((StringUtils.equalsIgnoreCase("YHJ", order.getReferer()) || StringUtils.equalsIgnoreCase(ConstantValues.HQ01S116, order.getOrderFrom()))){
					int point = (int)Math.floor(totalSettlement);
					ReturnInfo<String> pointResponse = userPointsService.processUserPoints(settleOrderInfo);
					if(pointResponse.getIsOk() > 0){
						logger.info("退货单结算-结算数据时CAS积分("+point+")扣除成功！returnSn:"+returnSn+",userId:"+getUserId(order)+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "退货单结算-结算数据时CAS积分("+point+")扣除成功！";
					}else{
						logger.info("退货单结算-结算数据时CAS积分("+point+")扣除失败！returnSn:"+returnSn+",userId:"+getUserId(order)+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "退货单结算-结算数据时CAS积分("+point+")扣除失败！错误信息:"+pointResponse.getMessage();
					}
				}
			}
		   
			//放入队列
			logger.info(">>>>settleNormalOrder.orderSn:"+returnSn+",settleObj:"+JSON.toJSONString(settleObj));
			sendMessage(jmsTemplateForOrder, JSON.toJSONString(settleObj));
			
			actionNote = "退货单结算-数据成功推向ERP";
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(returnSn);
			returnInfo.setMessage(" 退货单结算失败，错误信息:"+e.getMessage());
			actionNote = " 退货单结算-结算失败，错误信息:"+e.getMessage();
			
			
			//业务监控
			orderMonitorService.monitorForReturnSettleException(returnSn, "退单结算失败：" + e.getMessage());
			
			logger.info("settleReturnOrder-exception-orderSn:"+returnSn+",Msg："+e.getMessage(),e);
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				orderActionService.addOrderReturnAction(returnSn, actionNote);			  
			}
			if(StringUtils.isNotBlank(pointsNote)){
				orderActionService.addOrderReturnAction(returnSn, pointsNote);
			}
		}
		logger.info("[OrderSettleService.settleReturnOrder]end.......returnInfo:"+JSON.toJSONString(returnInfo));
		return returnInfo;
	}
	
	@Override
	public ReturnInfo<String> returnSettleCancle(SettleParamObj paramObj) {
		Assert.notNull(paramObj, "paramObj is null");
		logger.info("[OrderSettleService.returnSettleCancle]start.......dealCode:"+paramObj.getDealCode()+",paramObj:"+JSON.toJSONString(paramObj));
		
		String returnSn = paramObj.getDealCode();
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(returnSn);
		returnInfo.setMessage("退货单订单结算撤销操作！");
		
		OrderSettleParam settleObj = new OrderSettleParam();
		String actionNote = StringUtils.EMPTY;
		String pointsNote = StringUtils.EMPTY;
		if(StringUtils.isBlank(returnSn)){
			throw new RuntimeException("结算单据编码为空！");
		}
		
		try {
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息！");
			}
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效的退单发货信息！");
			}
			OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			if(order == null){
				throw new RuntimeException("无法获取有效的订单信息！");
			}
			
			OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
			goodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> goodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(goodsList)){
				throw new RuntimeException("无法获取退货单下有效的商品信息！");
			}
			
			//拼装数据
			settleObj.setShopId(order.getOrderFrom());
			settleObj.setOrderNo(order.getOrderSn());
			settleObj.setReturnNo(returnSn);
			DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date clearTime = orderReturn.getClearTime() == null ? new Date() : orderReturn.getClearTime();
			logger.info("[OrderSettleService.settleReturnOrder]process.......dealCode:"+paramObj.getDealCode()+",clearTime:"+orderReturn.getClearTime());
			
			settleObj.setDocDate(formatDate.format(clearTime));//退单下单时间
			settleObj.setTtlQty(orderReturn.getReturnAllgoodsCount().doubleValue());
			settleObj.setSaleMode(ConstantValues.ORDER_SELLTE_SALEMODE.RETURN);//退货
			if(StringUtils.equalsIgnoreCase("YHJ", order.getReferer())){
				settleObj.setO2oSaleType(order.getReferer());//FIXME huangl 暂时取原订单的Referer
			}else{
				settleObj.setO2oSaleType("B2C");
			}
			settleObj.setDepotCode(orderReturnShip.getDepotCode());//退货处理仓-可为空
			settleObj.setBussType(paramObj.getBussType());
			List<OrderSettleGoods> settleDetails = settleObj.getDetails();
			OrderSettleGoods settleItem = null;
			
			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
			List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
			double totalSettlement = 0d;
			
			//总价格：财物价格 + 红包分摊额
			double totalUserPayAmt = 0d;
			List<OrderReturnSkuInfo> returnSkuList = new ArrayList<OrderReturnSkuInfo>();
			//2015-05-14 同Sku商品合并
			List<OrderReturnGoods> itemGoodsList = new ArrayList<OrderReturnGoods>();
			Map<String,List<OrderReturnGoods>> goodsListMap = new HashMap<String,List<OrderReturnGoods>>();
			for (OrderReturnGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("无法获取有效的退货商品编码！");
				}
				
				
				
				SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
				settleGoodsInfo.setGoodssku(customCode);
				settleGoodsInfo.setCost(0-orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsReturnNumber().doubleValue());
				settleGoodsInfo.setCount(orderGoods.getGoodsReturnNumber().intValue());
				settleGoodsInfo.setGoodsname(orderGoods.getGoodsName());
				settleGoodsInfo.setUnitprice(orderGoods.getMarketPrice().doubleValue());
				settleGoodsInfo.setRate(CommonUtils.roundDouble(orderGoods.getGoodsPrice().doubleValue()/orderGoods.getMarketPrice().doubleValue(),2));
				//退单结算撤销积分恢复
				if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_SETTLE_CANCLE){
					settleGoodsInfo.setCost(orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsReturnNumber().doubleValue());
				}
				settleGoodsInfoList.add(settleGoodsInfo);
				totalSettlement += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsReturnNumber().doubleValue();
				
				ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				productExample.or().andCustumCodeEqualTo(customCode);
				/*List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}*/
				if(goodsListMap.containsKey(customCode+orderGoods.getSettlementPrice().toString())){
					itemGoodsList = goodsListMap.get(customCode+orderGoods.getSettlementPrice().toString());
				}else{
					itemGoodsList = new ArrayList<OrderReturnGoods>();
				}
				itemGoodsList.add(orderGoods);
				goodsListMap.put(customCode+orderGoods.getSettlementPrice().toString(), itemGoodsList);
			}
			int ttlQty = 0;
			for (String codeKey : goodsListMap.keySet()) {
				itemGoodsList = goodsListMap.get(codeKey);
				double settleMoney = 0D;
				int goodsNumber = 0;
				for (OrderReturnGoods orderGoods : itemGoodsList) {
					
					settleMoney += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsReturnNumber().intValue();						
					goodsNumber += orderGoods.getGoodsReturnNumber();
				}
				double singleSettleMoney = settleMoney / goodsNumber;
				//最终数据拼装
				settleItem = new OrderSettleGoods();
				settleItem.setProdId(itemGoodsList.get(0).getCustomCode());//十一位商品编码
				settleItem.setUnitPrice(CommonUtils.roundDouble(singleSettleMoney,2));//单个财务价
				settleItem.setQuantity(Double.valueOf(goodsNumber));//数量
				settleItem.setAmount(CommonUtils.roundDouble(settleMoney,2));//总财务价
				totalUserPayAmt += settleItem.getAmount();
				ttlQty += settleItem.getQuantity().intValue();
				settleDetails.add(settleItem);
				
				OrderReturnSkuInfo returnSku = new OrderReturnSkuInfo();
				returnSku.setSkuSn(codeKey);
				returnSku.setGoodsNum(goodsNumber);
				returnSku.setGoodsPrice(singleSettleMoney);
				returnSkuList.add(returnSku);
				
				//业务监控
				orderMonitorService.monitorForReturnSettle(returnSn, orderReturn.getChannelCode(), codeKey, settleItem.getUnitPrice(),goodsNumber);
			}
			settleObj.setTtlQty(Integer.valueOf(ttlQty).doubleValue());
			settleObj.setTtlVal(totalUserPayAmt);
			settleObj.setDetails(settleDetails);
			
			settleOrderInfo.setUvid(getUserId(order));
			settleOrderInfo.setOrdersn(returnSn);
			settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
			settleOrderInfo.setGuideid(ConstantValues.HQ01S116);
			settleOrderInfo.setShopid(ConstantValues.HQ01S116);
			settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
			settleOrderInfo.setTime(new Date().getTime());
			
			//扣除消费积分  
			if(!paramObj.isTools()){
				if((StringUtils.equalsIgnoreCase("YHJ", order.getReferer()) || StringUtils.equalsIgnoreCase(ConstantValues.HQ01S116, order.getOrderFrom()))){
					int point = (int)Math.floor(totalSettlement);
					ReturnInfo<String> pointResponse = userPointsService.processUserPoints(settleOrderInfo);
					if(pointResponse.getIsOk() > 0){
						logger.info("退货单结算撤销-CAS积分("+point+")扣除成功！returnSn:"+returnSn+",userId:"+getUserId(order)+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "退货单结算撤销-CAS积分("+point+")扣除成功！";
					}else{
						logger.info("退货单结算撤销-CAS积分("+point+")扣除失败！returnSn:"+returnSn+",userId:"+getUserId(order)+",totalSettlement:"+totalSettlement+",pointResponse:"+JSON.toJSONString(pointResponse));
						pointsNote = "退货单结算撤销-CAS积分("+point+")扣除失败！错误信息:"+pointResponse.getMessage();
					}
				}
			}

			//放入队列
			logger.info(">>>>settleNormalOrder.orderSn:"+returnSn+",settleObj:"+JSON.toJSONString(settleObj));
			sendMessage(jmsTemplateForOrder, JSON.toJSONString(settleObj));
			
			actionNote = "退货单结算撤销-数据成功推向ERP";
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(returnSn);
			returnInfo.setMessage(" 退货单结算撤销失败，错误信息:"+e.getMessage());
			actionNote = " 退货单结算撤销-失败，错误信息:"+e.getMessage();
			
			
			//业务监控
			orderMonitorService.monitorForReturnSettleException(returnSn, "退单结算撤销失败：" + e.getMessage());
			
			logger.info("returnSettleCancle-exception-returnSn:"+returnSn+",Msg："+e.getMessage(),e);
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				orderActionService.addOrderReturnAction(returnSn, actionNote);			  
			}
			if(StringUtils.isNotBlank(pointsNote)){
				orderActionService.addOrderReturnAction(returnSn, pointsNote);
			}
		}
		logger.info("[OrderSettleService.returnSettleCancle]end.......returnInfo:"+JSON.toJSONString(returnInfo));
		return returnInfo;
	}
	
	private String getUserId(OrderDistribute orderInfo) {
		if(StringUtils.isNotBlank(orderInfo.getMasterOrderSn())){
		  return masterOrderInfoMapper.selectByPrimaryKey(orderInfo.getMasterOrderSn()).getUserId();
		}else{
			return null;
		}
	}
	
	@Override
	public ReturnInfo<String> inputReturnOrder(SettleParamObj paramObj) {
		Assert.notNull(paramObj, "paramObj is null");
		logger.info("[OrderSettleService.settleReturnOrder]start.......dealCode:"+paramObj.getDealCode()+",paramObj:"+JSON.toJSONString(paramObj));
		
		String returnSn = paramObj.getDealCode();
		String actionNote = StringUtils.EMPTY;
		String pointsNote = StringUtils.EMPTY;
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setIsOk(1);
		returnInfo.setOrderSn(returnSn);
		returnInfo.setMessage("退货单入库操作成功");
		
		//三个字段加上时间  时间格式为“yyyyMMddHH24miss”例如“TD15112088683320151201141801”
		String timeStr =returnSn+DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
		SfSchTaskExecOosInfo inputObj = new SfSchTaskExecOosInfo();
		
		try {
			if(StringUtils.isBlank(returnSn)){
				throw new RuntimeException("退单号编码为空！");
			}
			/*if(paramObj.getBussType().intValue() != Constant.ERP_BUSS_TYPE_INPUT){
				throw new RuntimeException("OS通知ERP业务类型异常BussType:"+paramObj.getBussType());
			}*/
			inputObj.SRC_DOC_TYPE ="2";
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息！");
			}
			
			OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
			if(orderReturnShip == null){
				throw new RuntimeException("无法获取有效的退单发货信息！");
			}
			
			//失货退货单带承运商备注
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
//				SystemShippingExample systemShippingExample=new SystemShippingExample();
//				systemShippingExample.or().andShippingCodeEqualTo(orderReturnShip.getReturnExpress());
//				List<SystemShipping> systemShippingList = systemShippingMapper.selectByExample(systemShippingExample);
//				if(CollectionUtils.isEmpty(systemShippingList)){
//					throw new RuntimeException("失货退货单"+returnSn+"承运商("+orderReturnShip.getReturnExpress()+")不存在");
//				}
//				timeStr += ",失货单,"+systemShippingList.get(0).getShippingName()+orderReturnShip.getReturnExpress();
				timeStr += orderReturnShip.getReturnExpress();
			}
			inputObj.B2C_DOC_CODE = timeStr;
			inputObj.SRC_DOC_CODE = timeStr;
			inputObj.OS_DOC_CODE = timeStr;
			for(StorageGoods storageGoods : paramObj.getStorageGoods()){
				OrderReturnGoods updateGoods = new OrderReturnGoods();
				updateGoods.setId(storageGoods.getId());
				updateGoods.setStorageTimeStamp(timeStr);
				orderReturnGoodsMapper.updateByPrimaryKeySelective(updateGoods);
			}
			
			if(orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() &&
					orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue() &&
					orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
				throw new RuntimeException("只有退货单、拒收入库单和失货退货单才能进行结算或入库操作！");
			}
			if(!paramObj.isTools()){
				if(orderReturnShip.getCheckinStatus().intValue() == ConstantValues.YESORNO_NO.intValue()){
					throw new RuntimeException("退货单入库状态异常！");
				}
				if(orderReturnShip.getQualityStatus().intValue() == ConstantValues.YESORNO_NO.intValue()){
					throw new RuntimeException("退货单质检不通过不可进行入库操作！");
				}
			}
			OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderReturn.getRelatingOrderSn());
			if(order == null){
				throw new RuntimeException("无法获取有效的订单信息！");
			}
			OrderReturnShipExample shipExample = new OrderReturnShipExample();
			shipExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnShip> shipList = orderReturnShipMapper.selectByExample(shipExample);
			if(CollectionUtils.isEmpty(shipList)){
				throw new RuntimeException("无法获取有效的退单发货单！");
			}
			
			/*OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
			if(StringUtils.isNotBlank(paramObj.getGoodsId())){
				List<Long> idsList = new ArrayList<Long>();
				for(int i = 0; i< paramObj.getGoodsId().split(",").length;i++){
					idsList.add(Long.valueOf( paramObj.getGoodsId().split(",")[i]));
				}
				goodsExample.or().andRelatingReturnSnEqualTo(returnSn).andIdIn(idsList);
			}else{
				goodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			}
			
			List<OrderReturnGoods> goodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(goodsList)){
				throw new RuntimeException("无法获取退货单下有效的商品信息！");
			}
		 
			List<LstSfSchTaskExecOosDtls> inputDetails = new ArrayList<LstSfSchTaskExecOosDtls>();
			LstSfSchTaskExecOosDtls inputItem = null;
			
			//总价格：财物价格 + 红包分摊额
			List<OrderReturnGoods> itemGoodsList = new ArrayList<OrderReturnGoods>();
			Map<String,List<OrderReturnGoods>> goodsListMap = new HashMap<String,List<OrderReturnGoods>>();
			for (OrderReturnGoods orderGoods : goodsList) {
				String customCode = orderGoods.getCustomCode();
				
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("无法获取有效的退货商品编码！");
				}
				ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				productExample.or().andCustumCodeEqualTo(customCode);
				List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}
				if(goodsListMap.containsKey(customCode)){
					itemGoodsList = goodsListMap.get(customCode);
				}else{
					itemGoodsList = new ArrayList<OrderReturnGoods>();
				}
				itemGoodsList.add(orderGoods);
				goodsListMap.put(customCode, itemGoodsList);
				inputItem = new LstSfSchTaskExecOosDtls();
				inputItem.PROD_NUM = customCode;
				inputItem.QTY = orderGoods.getProdscannum().intValue();
				inputItem.PRICE = orderGoods.getSettlementPrice().doubleValue();
				inputItem.LOC_ID = "";
				inputDetails.add(inputItem);
				
				//业务监控
				orderMonitorService.monitorForReturnInput(returnSn, orderReturn.getChannelCode(), customCode, orderGoods.getProdscannum().intValue(), orderReturnShip.getDepotCode());
			}
			for (String codeKey : goodsListMap.keySet()) {
				itemGoodsList = goodsListMap.get(codeKey);
				double settleMoney = 0D;
				int goodsNumber = 0;
				for (OrderReturnGoods orderGoods : itemGoodsList) {
					
					settleMoney += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsReturnNumber().intValue();						
					goodsNumber += orderGoods.getGoodsReturnNumber();
				}
				double singleSettleMoney = settleMoney / goodsNumber;
				//最终数据拼装
				inputItem = new LstSfSchTaskExecOosDtls();
				inputItem.PROD_NUM = codeKey;
				inputItem.QTY = goodsNumber;
				inputItem.PRICE = CommonUtils.roundDouble(singleSettleMoney,2);
				inputItem.LOC_ID = "";
				inputDetails.add(inputItem);
				
				//业务监控
				orderMonitorService.monitorForReturnInput(returnSn, orderReturn.getChannelCode(), codeKey, goodsNumber, orderReturnShip.getDepotCode());
			}*/
			List<LstSfSchTaskExecOosDtls> inputDetails = new ArrayList<LstSfSchTaskExecOosDtls>();
			LstSfSchTaskExecOosDtls inputItem = null;
			
			/**
			 * 推送之前合并商品
			 */
			Map<String,StorageGoods> checkStorageGoodsMap = new HashMap<String,StorageGoods>();
			List<StorageGoods> checkStorageGoodsList = new ArrayList<StorageGoods>();
			for (StorageGoods storageGoods : paramObj.getStorageGoods()) {
				if(!checkStorageGoodsMap.containsKey(storageGoods.getCustomCode())){
					checkStorageGoodsMap.put(storageGoods.getCustomCode(),storageGoods);
				}else{//customCode相同的商品合并
					StorageGoods checkStorageGoods = checkStorageGoodsMap.get(storageGoods.getCustomCode());
					checkStorageGoods.setProdScanNum(checkStorageGoods.getProdScanNum()+storageGoods.getProdScanNum());
					checkStorageGoods.setSettlementPrice(checkStorageGoods.getSettlementPrice());
					checkStorageGoodsMap.put(storageGoods.getCustomCode(), checkStorageGoods);
				}
			}
			for(StorageGoods storageGoods:checkStorageGoodsMap.values()){
				checkStorageGoodsList.add(storageGoods);
			}
			
			
			
			for (StorageGoods storageGoods : checkStorageGoodsList) {
				String customCode = storageGoods.getCustomCode();
				
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("无法获取有效的退货商品编码！");
				}
				ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				productExample.or().andCustumCodeEqualTo(customCode);
				/*List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}*/
				inputItem = new LstSfSchTaskExecOosDtls();
				inputItem.PROD_NUM = customCode;
				inputItem.QTY = storageGoods.getProdScanNum();
				inputItem.PRICE = storageGoods.getSettlementPrice().doubleValue();
				inputItem.LOC_ID = "";
				inputDetails.add(inputItem);
				
				//业务监控
				orderMonitorService.monitorForReturnInput(returnSn, orderReturn.getChannelCode(), customCode, storageGoods.getProdScanNum(), orderReturnShip.getDepotCode());
			}
			inputObj.DISP_WAREH_CODE = orderReturn.getChannelCode();
			inputObj.SHOP_CODE = orderReturnShip.getDepotCode();
			inputObj.lstSfSchTaskExecOosDtls = inputDetails;
			
			//放入队列
			logger.info(">>>>>inputReturnOrder.returnSn:"+returnSn+",bussType:"+paramObj.getBussType()+",inputObj:"+JSON.toJSONString(inputObj));
			
			sendMessage(jmsTemplateForReturnInput, JSON.toJSONString(inputObj));
			
			actionNote = "退货单入库-数据成功推向ERP";
			if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_INPUT_CANCLE){
				actionNote = "退货单入库撤销成功!";
			}
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(returnSn);
			returnInfo.setMessage(" 退货单入库失败，错误信息:"+e.getMessage());
			actionNote = " 退货单-入库失败，错误信息:"+e.getMessage();
			if(paramObj.getBussType().intValue() == Constant.ERP_BUSS_TYPE_INPUT_CANCLE){
				returnInfo.setMessage(" 退货单入库撤销失败，错误信息:"+e.getMessage());
				actionNote = " 退货单-入库撤销失败，错误信息:"+e.getMessage();
			}
			
			logger.error("inputReturnOrder-exception-orderSn:"+returnSn+",Msg："+e.getMessage(),e);
			
			//业务监控
			orderMonitorService.monitorForReturnInputException(returnSn, "入库失败:" + e.getMessage());
		} finally{
			if(StringUtils.isNotBlank(actionNote)){
				orderActionService.addOrderReturnAction(returnSn, actionNote);			  
			}
			if(StringUtils.isNotBlank(pointsNote)){
				orderActionService.addOrderReturnAction(returnSn, pointsNote);
			}
		}
		logger.info("[OrderSettleService.inputReturnOrder]end.......returnInfo:"+JSON.toJSONString(returnInfo));
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
	/**
	 * 验证订单结算数据完整性
	 * @param returnSn
	 * @return
	 */
	@Override
	public ReturnInfo<String> checkOrderSettle(String orderSn) {
		logger.debug("checkOrderSettle.start....orderSn:"+orderSn);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setOrderSn(orderSn);
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		try {
			MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
			if(orderInfo == null){
				throw new RuntimeException("无法获取有效的订单信息！");
			}
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(orderSn);
			List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(orderGoodsList)){
				throw new RuntimeException("无法获取有效的商品列表！");
			}
			//int goodsCount = 0;
			for (MasterOrderGoods orderGoods : orderGoodsList) {
				if(StringUtils.isBlank(orderGoods.getCustomCode())){
					throw new RuntimeException("订单中商品11位编码为空不可进行操作");
				}
				//ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				//productExample.or().andCustumCodeEqualTo(orderGoods.getCustomCode());
				/*List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}*/
				//goodsCount += orderGoods.getGoodsNumber().intValue();
				/*double diffMoney = FormatUtil.roundDouble(orderGoods.getTransactionPrice().doubleValue() - orderGoods.getShareBonus().doubleValue() - orderGoods.getSettlementPrice().doubleValue()-orderGoods.getIntegralMoney().doubleValue(),2);
				if(diffMoney > 0.01 || diffMoney < -0.01){
					throw new RuntimeException("订单商品("+orderGoods.getCustomCode()+")商品成交价("+orderGoods.getTransactionPrice().doubleValue()+")-财务价格("+orderGoods.getSettlementPrice().doubleValue()+")-红包分摊额("+orderGoods.getShareBonus().doubleValue()+")-积分金额("+orderGoods.getIntegralMoney().doubleValue()+")等值存在差异("+diffMoney+")");
				}*/
			}
			/*if(goodsCount != orderInfo.getGoodsCount().intValue()){
				throw new RuntimeException("实际商品数量("+goodsCount+")与订单不符("+orderInfo.getGoodsCount()+")");
			}*/
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("验证通过");
			logger.debug("checkOrderSettle 验证通过 orderSn:"+orderSn+",resultInfo:"+returnInfo);
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setOrderSn(orderSn);
			returnInfo.setMessage(e.getMessage());
			logger.debug("checkOrderSettle-exception-orderSn:"+orderSn+",Msg："+e.getMessage(),e);
		}
		return returnInfo;
	}

	/**
	 * 验证退单结算数据完整性
	 * @param returnSn
	 * @return
	 */
	@Override
	public ReturnInfo<String> checkReturnSettle(String returnSn) {
		logger.info("checkReturnSettle.start....returnSn:"+returnSn);
		ReturnInfo<String> returnInfo = new ReturnInfo<String>();
		returnInfo.setReturnSn(returnSn);
		returnInfo.setIsOk(ConstantValues.YESORNO_NO);
		try {
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("无法获取有效的退单信息！");
			}
			if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY){
				returnInfo.setIsOk(ConstantValues.YESORNO_YES);
				returnInfo.setMessage("验证通过");
				logger.info("checkReturnSettle 由于退款单 验证通过 returnSn:"+returnSn+",resultInfo:"+returnInfo);
				return returnInfo;
			}
			OrderReturnShipExample shipExample = new OrderReturnShipExample();
			shipExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnShip> shipList = orderReturnShipMapper.selectByExample(shipExample);
			if(CollectionUtils.isEmpty(shipList)){
				throw new RuntimeException("无法获取有效的退单发货单！");
			}
		 
			OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
			goodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isEmpty(returnGoodsList)){
				throw new RuntimeException("无法获取退货单下有效的商品信息！");
			}
			//int returnGoodsCount = 0;
			for (OrderReturnGoods orderReturnGoods : returnGoodsList) {
				String customCode = orderReturnGoods.getCustomCode();
				if(StringUtils.isBlank(customCode)){
					throw new RuntimeException("退货商品编码为空不可进行操作");
				}
				ProductBarcodeListExample productExample = new ProductBarcodeListExample();
				productExample.or().andCustumCodeEqualTo(customCode);
				/*List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productExample);
				if(CollectionUtils.isEmpty(productList)){
					throw new RuntimeException("无法获取有效商品11位编码数据！");
				}*/
				//returnGoodsCount += orderReturnGoods.getGoodsReturnNumber().intValue();
				/*double diffMoney = FormatUtil.roundDouble(orderReturnGoods.getGoodsPrice().doubleValue() - orderReturnGoods.getShareBonus().doubleValue() - orderReturnGoods.getSettlementPrice().doubleValue(),2);
				if(diffMoney > 0.01 || diffMoney < -0.01){
					throw new RuntimeException("退货商品("+orderReturnGoods.getCustomCode()+")商品成交价("+orderReturnGoods.getGoodsPrice().doubleValue()+")、财务价格("+orderReturnGoods.getSettlementPrice().doubleValue()+")、红包分摊额("+orderReturnGoods.getShareBonus().doubleValue()+")等值存在差异("+diffMoney+")"); 
				}*/
			}
			/*if(returnGoodsCount != orderReturn.getReturnAllgoodsCount().intValue()){
				throw new RuntimeException("实际退货商品数量("+returnGoodsCount+")与退货单不符("+orderReturn.getReturnAllgoodsCount()+")");
			}*/
			returnInfo.setIsOk(ConstantValues.YESORNO_YES);
			returnInfo.setMessage("验证通过");
			logger.info("checkReturnSettle 验证通过 returnSn:"+returnSn+",resultInfo:"+returnInfo);
		} catch (Exception e) {
			returnInfo.setIsOk(0);
			returnInfo.setMessage(e.getMessage());
			logger.error("checkReturnSettle-exception-returnSn:"+returnSn+",Msg："+e.getMessage(),e);
		}
		return returnInfo;
	}

	
	@Override
	public void callSettleOrder(SettleBillQueue billQueue) {
		logger.info("callSettleOrder.begin..SettleBillQueue"+JSON.toJSONString(billQueue));
		OrderSettleBillExample orderSettleBillExample = new OrderSettleBillExample();
		orderSettleBillExample.or().andIdEqualTo(Long.valueOf(billQueue.getId()));
		//orderSettleBillExample.or().andBillNoEqualTo(billQueue.getBillNo()).andOrderCodeEqualTo(billQueue.getOrderCode()).andResultStatusEqualTo(ConstantValues.ORDER_SETTLE_BILL_RESULT.NOPROCESS.byteValue());
		
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
			MasterOrderInfoExample orderInfoExample = new MasterOrderInfoExample();
			if(billQueue.getOrderCodeType().intValue() == 0){
				orderInfoExample.or().andMasterOrderSnEqualTo(billQueue.getOrderCode());
			}else{
				orderInfoExample.or().andOrderOutSnEqualTo(billQueue.getOrderCode());
			}
			List<MasterOrderInfo> orderInfoList = masterOrderInfoMapper.selectByExample(orderInfoExample);
			if(CollectionUtils.isEmpty(orderInfoList)){
				throw new RuntimeException("近三个月数据中，订单编号/外部交易号为"+billQueue.getOrderCode()+"的订单不存在");
			}
			MasterOrderInfo orderInfo = orderInfoList.get(0);
			updateOrderSettleBill.setOrderType(orderInfo.getOrderType());//订单类型
			
			if(orderInfo.getOrderStatus().intValue() != ConstantValues.ORDER_STATUS.CONFIRMED.intValue()){
				throw new RuntimeException("待结算订单当前非已确认状态");
			}
			if(orderInfo.getShipStatus().intValue() < ConstantValues.ORDERINFO_SHIP_STATUS.SHIPED.intValue()){
				throw new RuntimeException("待结算订单当前非已发货状态");
			}
			/*if(orderInfo.getOrderType().intValue() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER){
				throw new RuntimeException("待结算订单是换货单请客服人工干预结算");
			}*/
			List<OrderSettleBill> settleBillList = orderSettleBillMapper.selectByExample(orderSettleBillExample);
			if(CollectionUtils.isEmpty(settleBillList)){
				throw new RuntimeException("待结算订单调整单据不存在");
			}
			if(CollectionUtils.size(settleBillList) > 1){
				throw new RuntimeException("待结算订单调整单据不是唯一");
			}
			orderSettleBill = settleBillList.get(0);
			OrderDistributeExample example = new OrderDistributeExample();
			example.or().andMasterOrderSnEqualTo(orderInfo.getMasterOrderSn());
			List<OrderDistribute> orderDistributeList = orderDistributeMapper.selectByExample(example);
			if(CollectionUtils.isEmpty(orderDistributeList)){
				throw new RuntimeException("关联子单为空！");
			}
			OrderDepotShipExample orderShipExample = new OrderDepotShipExample();
			orderShipExample.or().andOrderSnEqualTo(orderDistributeList.get(0).getOrderSn());
			
			List<OrderDepotShip> orderShipList = orderDepotShipMapper.selectByExample(orderShipExample);
			if(CollectionUtils.isEmpty(orderShipList)){
				throw new RuntimeException("待结算订单发货单数据不存在");
			}
			//如果是货到付款要求配送方式必须一致
			if(orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.ORDER_DELIVERY_PAY.intValue()){
				if(billQueue.getShippingId().intValue() != orderShipList.get(0).getShippingId().intValue()){
					throw new RuntimeException("待结算订单数据中配送方式("+orderShipList.get(0).getShippingId()+")与结算要求("+billQueue.getShippingId()+")不符");
				}
				//要求订单应付款总金额与订单结算金额一致
				if(orderInfo.getTotalPayable().compareTo(BigDecimal.valueOf(billQueue.getMoney())) != 0){
					throw new RuntimeException("待结算订单应付款金额("+orderInfo.getTotalPayable()+")与结算金额("+billQueue.getMoney()+")不符");
				}
			}else{
				if(orderInfo.getPayStatus().intValue() != ConstantValues.ORDER_PAY_STATUS.PAYED.intValue()){
					throw new RuntimeException("待结算订单当前不是已付款状态");
				}
				//要求订单所关联退单必须全部已结算，并且订单已付款金额 +订单使用的余额-退单总金额 =已结算金额
//			  ReturnInfo checkResult = orderReturnService.checkOrderCanSettle(orderInfo.getOrderSn(), orderInfo.getMoneyPaid().doubleValue(),orderInfo.getSurplus().doubleValue(), billQueue.getMoney());
				ReturnInfo checkResult = orderReturnService.checkOrderCanSettle(orderInfo.getMasterOrderSn(), billQueue.getMoney());
				logger.info("callSettleOrder.process.orderSn:"+orderInfo.getMasterOrderSn()+",checkResult:"+JSON.toJSONString(checkResult));
				if(checkResult.getIsOk() < ConstantValues.YESORNO_YES.intValue()){
					throw new RuntimeException(checkResult.getMessage());
				}
			}
			
			ReturnInfo settleResult = this.settleOrder(orderInfo.getMasterOrderSn(), "批量结算操作("+orderSettleBill.getBillNo()+")", orderBillList.getActionUser(), null);
			if(settleResult.getIsOk() > 0){
				updateOrderSettleBill.setClearTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.SUCCESS.byteValue());
				updateOrderSettleBill.setResultMsg(settleResult.getMessage());
				updateOrderSettleBill.setUpdateTime(new Date());
			}else{
				updateOrderSettleBill.setUpdateTime(new Date());
				updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
				updateOrderSettleBill.setResultMsg(settleResult.getMessage());
			}
		} catch (Exception e) {
			logger.error("callSettleOrder.exception..orderCode:"+billQueue.getOrderCode(),e);
			updateOrderSettleBill.setUpdateTime(new Date());
			updateOrderSettleBill.setResultStatus(ConstantValues.ORDER_SETTLE_BILL_RESULT.FAILED.byteValue());
			updateOrderSettleBill.setResultMsg("结算异常:"+e.getMessage());
		}finally{
			orderSettleBillMapper.updateByExampleSelective(updateOrderSettleBill, orderSettleBillExample);
		}
		
		
	}
	
	@Override
	public ReturnInfo settleOrder(String orderSnOrOutSn, String actionUser, String actionNote, Integer userId) {
		logger.info("订单结算settleOrder service begin: orderSnOrOutSn="+orderSnOrOutSn +";actionNote="+actionNote+";actionUser="+actionUser);
		ReturnInfo returnInfo=new ReturnInfo(Constant.OS_NO);
		try {
			//订单验证
			MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSnOrOutSn);
			
			if(orderInfo == null){
				MasterOrderInfoExample orderInfoExample = new MasterOrderInfoExample();
				orderInfoExample.or().andOrderOutSnEqualTo(orderSnOrOutSn);
				List<MasterOrderInfo> orderInfoList = masterOrderInfoMapper.selectByExample(orderInfoExample);
				if(CollectionUtils.isEmpty(orderInfoList)){
					returnInfo.setOrderSn(orderSnOrOutSn);
					throw new RuntimeException("近三个月数据中，没有取得订单编号/外部交易号为"+orderSnOrOutSn+"的订单");
				}
				orderInfo = orderInfoList.get(0);
			}
			returnInfo.setOrderSn(orderInfo.getMasterOrderSn());
			returnInfo.setOrderOutSn(orderInfo.getOrderOutSn());
			
			// 检查订单信息订单状态
			if (orderInfo.getPayStatus().intValue() == Constant.OI_PAY_STATUS_SETTLED) {
				throw new RuntimeException(" 订单" + orderInfo.getMasterOrderSn() + "已经是已结算状态");
			}
			if (orderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
				throw new RuntimeException(" 订单" + orderInfo.getMasterOrderSn() + "要处于已确定状态");
			}
			if (orderInfo.getShipStatus().intValue() < Constant.OI_SHIP_STATUS_ALLSHIPED){
				throw new RuntimeException(" 订单" + orderInfo.getMasterOrderSn() + "发货状态必须是已发货.部分收货.客户已收货状态！");
			}
			if(StringUtils.equalsIgnoreCase("全流通", orderInfo.getReferer())){
				throw new RuntimeException("全流通订单无法进行结算操作！");
			}
			if (orderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP) {
				if (orderInfo.getNoticeStatus().intValue() == Constant.OI_NOTICE_STATUS_UNNOTICED 
						&& (orderInfo.getPayStatus().intValue() == Constant.OI_PAY_STATUS_UNPAYED
								|| orderInfo.getPayStatus().intValue() == Constant.OI_PAY_STATUS_PARTPAYED)) {
					logger.info(" 订单" + orderInfo.getMasterOrderSn() + "交易类型是货到付款类型订单,通知状态必须是未通知状态,支付状态必须是未付款或部分付款状态！");
				} else if (orderInfo.getNoticeStatus().intValue() == Constant.OI_NOTICE_STATUS_NOTICED
						&& orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED) {
					logger.info(" 订单" + orderInfo.getMasterOrderSn() + "交易类型是货到付款类型订单,通知状态是已通知状态,支付状态必须是部分付款状态！");
				}else{
					throw new RuntimeException("货到付款的订单"+orderInfo.getMasterOrderSn()+"当前付款状态此时不能进行结算操作");
				}
			} else if (orderInfo.getPayStatus().intValue() != Constant.OI_PAY_STATUS_PAYED) {
				throw new RuntimeException(" 订单" + orderInfo.getMasterOrderSn() + "要处于已付款状态");
			}
			
			/*OrderDepotShipExample orderShipExample = new OrderDepotShipExample();
			orderShipExample.or().andOrderSnEqualTo(orderInfo.getMasterOrderSn());
			List<OrderDepotShip> orderShipList = orderDepotShipMapper.selectByExample(orderShipExample);
			if(CollectionUtils.isEmpty(orderShipList)){
				throw new RuntimeException("发货信息数据为空！");
			}*/
			MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
			orderGoodsExample.or().andMasterOrderSnEqualTo(orderInfo.getMasterOrderSn());
			List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
			if(CollectionUtils.isEmpty(orderGoodsList)){
				throw new RuntimeException("商品信息数据为空！");
			}
			//结算数据提前验证
			ReturnInfo checkResult = this.checkOrderSettle(orderInfo.getMasterOrderSn());
			if(checkResult.getIsOk() <= 0){
				throw new RuntimeException(checkResult.getMessage());
			}
			
			//结算条件-订单状态验证
			if(orderInfo.getOrderStatus().intValue() == ConstantValues.ORDER_STATUS.COMPLETE.intValue()){
				throw new RuntimeException("订单已完结不可再结算");
			}
			//已确认已发货
			if(orderInfo.getOrderStatus().intValue() == ConstantValues.ORDER_STATUS.CONFIRMED.intValue()
					&& (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED ||
					orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED ||
					orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED)){
				//货到付款
				if(orderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP){
					if((orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_UNPAYED 
							|| orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED) &&
							orderInfo.getNoticeStatus() == Constant.OI_NOTICE_STATUS_UNNOTICED){
					}else if(orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED){
						//货到付款-已付款
					}else{
						throw new RuntimeException("货到付款的订单"+orderInfo.getMasterOrderSn()+"此时不能进行结算操作！");
					}
				}else{
					if(orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PAYED){
						throw new RuntimeException("订单"+orderInfo.getMasterOrderSn()+"非货到付款订单不是已付款，不能进行结算操作！");
					}
				}
			}else{
				throw new RuntimeException("订单"+orderInfo.getMasterOrderSn()+"必须处于已确定和已全部发货状态才可结算！");
			}
			
		  /*  //订单不是已结算或已付款状态，即：是货到付款状态时。调用付款接口  2016-06-27
			if(orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PAYED 
					&& orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_SETTLED) {
				OrderPayExample orderPayExample = new OrderPayExample();
				orderPayExample.or().andOrderSnEqualTo(orderInfo.getOrderSn()).andPayStatusEqualTo(ConstantValues.OP_ORDER_PAY_STATUS.UNPAYED.byteValue());
				List<OrderPay> orderPayList = orderPayMapper.selectByExample(orderPayExample);
				if(CollectionUtils.size(orderPayList) != 1){
					throw new RuntimeException("货到付款订单部分付款未付款单异常");
				}
				//调用付款接口
				orderPayStChService.payStChObject(orderInfo, orderPayList.get(0).getPaySn(), "结算货到付款订单时自动付款", actionUser, false);
			}*/
			/* //更新数据
			OrderDistribute updateOrderInfo = new OrderDistribute();
			updateOrderInfo.setOrderSn(orderInfo.getMasterOrderSn());
			
			
			/更新数据
			OrderDistribute updateOrderInfo = new OrderDistribute();
			updateOrderInfo.setOrderSn(orderDistribute.getOrderSn());
			updateOrderInfo.setOrderStatus(Integer.valueOf(Constant.OI_ORDER_STATUS_FINISHED).byteValue());
			updateOrderInfo.setPayStatus(ConstantValues.ORDER_PAY_STATUS.SETTLEMENT.byteValue());
//			updateOrderInfo.setClearTime(new Date());
//			updateOrderInfo.setFinishTime(new Date());
			updateOrderInfo.setUpdateTime(new Date());
			orderDistributeMapper.updateByPrimaryKeySelective(updateOrderInfo);*/
		
			/*OrderPayExample orderPayExample = new OrderPayExample();
			orderPayExample.or().andOrderSnEqualTo(orderInfo.getOrderSn());
			OrderPay updateOrderPay = new OrderPay();
			updateOrderPay.setPayStatus(Integer.valueOf(Constant.OP_PAY_STATUS_SETTLED).byteValue());
			updateOrderPay.setUpdateTime(new Date());
			orderPayMapper.updateByExampleSelective(updateOrderPay, orderPayExample);*/
			
			//推送ERP结算数据
			SettleParamObj paramObj = new SettleParamObj();
			paramObj.setDealCode(orderInfo.getMasterOrderSn());
			paramObj.setBussType(Constant.ERP_BUSS_TYPE_SETTLE);
			paramObj.setTools(false);
			paramObj.setUserId("system");
			ReturnInfo returnVal = this.MasterOrderSettle(paramObj);
			logger.info(">>>>普通订单结算" + orderInfo.getMasterOrderSn() + " Oms结算数据推送成功,returnVal:"+JSON.toJSONString(returnVal));
			if(returnVal == null || returnVal.getIsOk() < 1){
				throw new RuntimeException(returnVal.getMessage());
			}
			
			
			//超时单
			//orderTimeoutService.complteTimeOutObject(ConstantValues.ORDER_TIMEOUT_RELATING_TYPE_ORDER, orderInfo.getOrderSn());

//		  logger.info(">>>>>[Os_Call_Oms]pushOrderToErp.orderSn:"+orderInfo.getOrderSn());
//		  orderPushToErpServiceImpl.pushOrderToErp(orderInfo.getOrderSn());

			
			returnInfo.setIsOk(Constant.OS_YES);
			returnInfo.setMessage("订单结算成功");
		} catch (Exception e) {
			logger.error(orderSnOrOutSn + "订单结算settleOrder 异常:"+e.getMessage(), e);
			returnInfo.setMessage("订单结算settleOrder 异常:"+e.toString());
		}
		return returnInfo;
	}
}
