package com.work.shop.oms.distribute.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.common.bean.OrderDepotResult;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.exception.ErpDepotException;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;

/**
 * 供应商交货单服务
 * @author QuYachu
 */
@Service("distributeSupplierService")
public class DistributeSupplierServiceImpl implements DistributeSupplierService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	private DistributeActionService distributeActionService;
//	@Resource
//	private BIMonitorService biMonitorService;
	@Resource(name = "distributeSupplierProducerJmsTemplate")
	private JmsTemplate distributeSupplierJmsTemplate;
//	@Resource
//	private SupplierService supplierService;
//	@Resource
//	private ThirdSupplierService thirdSupplierService;
	@Resource
	private OrderCommonService orderCommonService;
	
	@Override
	public OrderDepotResult executeDistribute(OrderDistribute distribute, boolean isRePush) throws Exception {
		long startTime = System.currentTimeMillis();
		OrderDepotResult result = new OrderDepotResult(Constant.OS_NO, "分发异常");
		if (distribute == null) {
			logger.error("找不到对应的订单：" + distribute);
			result.setMessage("找不到对应的订单：" + distribute);
			return result;
		}
		String orderSn = distribute.getOrderSn();
		String masterOrderSn = distribute.getMasterOrderSn();
		String msg = "交货单[" + orderSn + "]开始下发";
		if (isRePush) {
			msg = "交货单[" + orderSn + "]开始立即（强制指定）下发";
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (master == null) {
			logger.error("订单[" + masterOrderSn + "] 不存在");
			result.setMessage("订单[" + masterOrderSn + "] 不存在");
			return result;
		}
		saveLogInfo(msg, distribute);
		// 订单状态检查 商品、支付单、地址等
		logger.info(msg);
		if (StringUtil.isEmpty(orderSn)) {
			return null;
		}
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		goodsExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
		int count = masterOrderGoodsMapper.countByExample(goodsExample);
		if (count == 0) {
			logger.error("pushOrderToErp : 订单" + orderSn + "订单商品为空,下发终止");
			return result;
		}
		MasterOrderAddressInfo address = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == address) {
			logger.error("pushOrderToErp : 订单" + orderSn + "收货地址为空,下发终止");
			return result;
		}
		MasterOrderPayExample payExample = new MasterOrderPayExample();
		payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> orderPays = masterOrderPayMapper.selectByExample(payExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("pushOrderToErp : 订单" + orderSn + "订单付款单为空,下发终止");
			return result;
		}

		MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (null == extend) {
			logger.error("订单" + orderSn + "扩展信息不存在,下发终止");
			return result;
		}
		// MB供应商
		List<String> orderSns = new ArrayList<String>();
		orderSns.add(orderSn);
		if (distribute.getSupplierCode().equals(Constant.SUPPLIER_TYPE_MB)) {
//			result = supplierService.executeDistribute(master, extend, orderPays.get(0), orderSn, address, isRePush);
		} else {
//			result = thirdSupplierService.executeDistribute(master, extend, orderPays.get(0), orderSn, address, isRePush);
		}
		
		logger.debug("订单" + orderSn + "保存订单日志结束");
		long endTime = System.currentTimeMillis();
		logger.info("下发供应商用时：" + (endTime - startTime));
		return result;
	}

	@Override
	public OrderDepotResult executeMaster(String masterOrderSn, boolean isRePush)
			throws Exception {
		long startTime = System.currentTimeMillis();
		OrderDepotResult result = new OrderDepotResult(Constant.OS_NO, "分发异常");
		if (isRePush) {
			
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (master == null) {
			logger.error("订单[" + masterOrderSn + "] 不存在");
			result.setMessage("订单[" + masterOrderSn + "] 不存在");
			return result;
		}
		MasterOrderAddressInfo address = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == address) {
			logger.error("订单" + masterOrderSn + "收货地址为空,下发终止");
			result.setMessage("订单" + masterOrderSn + "收货地址为空,下发终止");
			return result;
		}
		MasterOrderPayExample payExample = new MasterOrderPayExample();
		payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> orderPays = masterOrderPayMapper.selectByExample(payExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("订单" + masterOrderSn + "订单付款单为空,下发终止");
			result.setMessage("订单" + masterOrderSn + "订单付款单为空,下发终止");
			return result;
		}
		MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (null == extend) {
			logger.error("订单" + masterOrderSn + "扩展信息不存在,下发终止");
			result.setMessage("订单" + masterOrderSn + "扩展信息不存在,下发终止");
			return result;
		}
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNull(distributes)) {
			logger.error("订单" + masterOrderSn + "交货单不存在,下发终止");
			result.setMessage("订单" + masterOrderSn + "交货单不存在,下发终止");
			return result;
		}
		for (OrderDistribute distribute : distributes) {
			List<String> orderSns = new ArrayList<String>();
			orderSns.add(distribute.getOrderSn());
			String orderSn = distribute.getOrderSn();
			if (distribute.getSupplierCode().equals(Constant.SUPPLIER_TYPE_MB)) {
//				result = supplierService.executeDistribute(master, extend, orderPays.get(0), orderSn, address, isRePush);
			} else {
//				result = thirdSupplierService.executeDistribute(master, extend, orderPays.get(0), orderSn, address, isRePush);
			}
		}
		logger.debug("订单" + masterOrderSn + "保存订单日志结束");
		long endTime = System.currentTimeMillis();
		logger.info("下发供应商用时：" + (endTime - startTime));
		return result;
	}
	
	/**
	 * 根据供应商不同批量分发
	 * @param orderSn
	 * @param isRePush
	 * @return
	 * @throws Exception
	 */
	@Override
	public OrderDepotResult executeDistributes(String orderSn, boolean isRePush) throws Exception {
		OrderDepotResult result = new OrderDepotResult(Constant.OS_NO, "分发异常");
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]交货单号不能为空！");
			result.setMessage("[orderSn]交货单号不能为空！");
			return result;
		}
		OrderDistribute distribute = this.orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]信息不存在！");
			result.setMessage("订单[" + orderSn + "]信息不存在！");
			return result;
		}
		saveLogInfo("交货单[" + orderSn + "]开始下发", distribute);
		String masterOrderSn = distribute.getMasterOrderSn();
		MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (masterOrderInfo == null) {
			logger.error("订单[" + masterOrderSn + "]信息不存在");
			result.setMessage("订单[" + masterOrderSn + "]信息不存在");
			return result;
		}
		// 订单状态检查 商品、支付单、地址等
		MasterOrderAddressInfo address = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == address) {
			logger.error("订单[" + masterOrderSn + "]收货地址不存在,下发终止");
			return result;
		}
		MasterOrderPayExample payExample = new MasterOrderPayExample();
		payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> orderPays = masterOrderPayMapper.selectByExample(payExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("订单[" + masterOrderSn + "]订单付款单为空,下发终止");
			return result;
		}

		MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (null == extend) {
			logger.error("订单" + masterOrderSn + "扩展信息不存在,下发终止");
			return result;
		}
		// MB供应商
		if (distribute.getSupplierCode().equals(Constant.SUPPLIER_TYPE_MB)) {
//			result = supplierService.executeDistribute(masterOrderInfo, extend, orderPays.get(0), orderSn, address, isRePush);
		} else {
//			result = thirdSupplierService.executeDistribute(masterOrderInfo, extend, orderPays.get(0), orderSn, address, isRePush);
		}
		logger.debug("订单" + masterOrderSn + "保存订单日志结束");
		return result;
	}

	@Override
	public OrderDepotResult executeDistributeByMq(String orderSn)
			throws Exception {
		OrderDepotResult result = new OrderDepotResult(Constant.OS_YES, "分发成功");
		String msg = "订单[" + orderSn + "]开始分发";
		logger.debug(msg);
		// 检查子订单是否存在
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.error("找不到对应的订单" + orderSn);
			result.setResult(Constant.OS_NO);
			result.setMessage("找不到对应的订单" + orderSn);
			return result;
		}
		executeDistribute(distribute, false);
		// 异步分配
		return result;
	}

	@Override
	public OrderDepotResult distribute(String orderSn) throws Exception {
		OrderDepotResult result = new OrderDepotResult(Constant.OS_YES, "分发成功");
		String msg = "订单" + orderSn + "开始分发";
		logger.debug(msg);
		// 检查子订单是否存在
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.error("找不到对应的订单" + orderSn);
			result.setResult(Constant.OS_NO);
			result.setMessage("找不到对应的订单" + orderSn);
			return null;
		}
		// 检查订单是否符合分发条件
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("找不到对应的订单" + distribute.getMasterOrderSn());
			result.setResult(Constant.OS_NO);
			result.setMessage("找不到对应的订单" + distribute.getMasterOrderSn());
			return null;
		}
		// 检查订单是否可以分发
		ReturnInfo ri = isSatisfyDistribute(distribute, master);
		if (ri == null || ri.getIsOk() == Constant.OS_NO) {
			logger.error(ri == null ? orderSn + "返回结果为空" : ri.getMessage());
			saveLogInfo("交货单下发异常:" + ri.getMessage(), distribute);
			monitorErrorMessage(orderSn, "订单分发异常:" + ri.getMessage());
			return null;
		}
		// 如果是需要立即下发直接走立即下发流程
		// 全流通散购单走立即下发分配逻辑
		if (OrderAttributeUtil.isNowDistribute(distribute.getSource(), distribute.getIsnow())) {
			// 立即下发立即分配
			return executeDistribute(distribute, false);
		}
		// 异步下发
		distributeSupplierJmsTemplate.send(new TextMessageCreator(orderSn));
		return result;
	}

	@Override
	public OrderDepotResult executeMasterByMq(String masterOrderSn) {
		OrderDepotResult result = new OrderDepotResult(Constant.OS_NO, "下发失败");
		logger.info("");
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			result.setMessage("[masterOrderSn] 不能为空！");
			return result;
		}
		// 检查子订单是否存在
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		OrderDistributeExample.Criteria criteria = distributeExample.or();
		criteria.andMasterOrderSnEqualTo(masterOrderSn);
		criteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
		List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNull(distributes)) {
			logger.error("订单[" + masterOrderSn + "]交货单不存在,下发终止");
			result.setMessage("订单[" + masterOrderSn + "]交货单不存在,下发终止");
			return result;
		}
		for (OrderDistribute distribute : distributes) {
			final String param = distribute.getOrderSn();
			distributeSupplierJmsTemplate.send(new TextMessageCreator(param));
		}
		result.setMessage("下发成功");
		result.setResult(Constant.OS_YES);
		return result;
	}

	@Override
	public ReturnInfo isSatisfyDistribute(OrderDistribute distribute, MasterOrderInfo master) {
//		logger.debug("isDistribute : " + distribute);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		String tempMessage = "";
		if (distribute == null) {
			logger.error(tempMessage = "isPushErp : 订单不存在");
			ri.setMessage(tempMessage);
			return ri;
		}
		// 订单号
		String orderSn = distribute.getOrderSn();
		logger.debug("isDistribute : orderSn=" + orderSn);
		// 如果是问题单状态 需要停止下发
		if (distribute.getQuestionStatus().equals(Constant.OI_QUESTION_STATUS_QUESTION)) {
			logger.debug(tempMessage = "isPushErp : 订单" + orderSn + "是问题单questionStatus = " + distribute.getQuestionStatus() + ",下发终止.");
			ri.setMessage(tempMessage);
			return ri;
		}

		if (OrderAttributeUtil.isNowDistribute(distribute.getSource(), distribute.getIsnow())) {
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("订单号" + orderSn + "需要立即下发");
			return ri;
		}

		// 订单状态
		int orderStatus = distribute.getOrderStatus();
		// 付款状态
		int payStatus = master.getPayStatus();
		// 全流通订单已付款
		if (Constant.OI_PAY_STATUS_PAYED == payStatus && OrderAttributeUtil.isPosOrder(distribute.getSource())) {
			logger.debug("订单号" + distribute.getOrderSn() + "需要立即下发");
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("订单号" + distribute.getOrderSn() + "需要立即下发");
			return ri;
		}
		// 已确认
		if (Constant.OI_ORDER_STATUS_CONFIRMED != orderStatus) {
			logger.debug(tempMessage = "订单号" + orderSn + "订单状态状态：" + orderStatus + "，不符合下发条件");
			ri.setMessage(tempMessage);
			return ri;
		}

		// 如果是货到付款的
		if (Constant.OI_TRANS_TYPE_PRESHIP == distribute.getTransType()) {
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("订单" + orderSn + "是款到发货的需要下发");
			return ri;
		}

		// 已经付款
		if (Constant.OI_PAY_STATUS_PAYED != payStatus) {
			logger.debug(tempMessage = "订单" + orderSn + "付款状态：" + payStatus + "，不符合下发条件");
			ri.setMessage(tempMessage);
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		ri.setMessage("订单号" + orderSn + "需要立即下发");
		return ri;
	}

	private DistributeAction saveLogInfo(String message, OrderDistribute orderInfo) {
		logger.debug(message);
		DistributeAction orderAction = distributeActionService.createQrderAction(orderInfo);
		orderAction.setActionUser("SYSTEM");
		orderAction.setActionNote(message);
		distributeActionService.saveOrderAction(orderAction);
		return orderAction;
	}
	
	/**
	 * 更新OS订单下发状态
	 * 
	 * @param orderSn
	 * @param nowDate
	 */
	private void updateOsOrderInfoPushStatus(String orderSn, Date nowDate) {
		logger.debug("正在更新订单分发状态");
		OrderDistribute oi = new OrderDistribute();
		oi.setOrderSn(orderSn);
		oi.setLastUpdateTime(nowDate);
		orderDistributeMapper.updateByPrimaryKeySelective(oi);
	}
	
	private Integer getIsNow(OrderDistribute distribute) {
		return distribute.getIsnow() == null ? 0 : distribute.getIsnow();
	}

	/**
	 * 下发的时候合并商品
	 * 
	 * @param orderGoodsList
	 * @return
	 * @throws ErpDepotException
	 */
	@Override
	public List<MasterOrderGoods> groupByOrderGoods(List<MasterOrderGoods> orderGoodsList) throws Exception {
		String order_sn = orderGoodsList.get(0).getOrderSn();
		logger.debug("订单" + order_sn + "开始合并商品");
		List<MasterOrderGoods> templist = new ArrayList<MasterOrderGoods>(orderGoodsList.size());
		Map<String, MasterOrderGoods> map = new HashMap<String, MasterOrderGoods>();
		for (MasterOrderGoods ogBean : orderGoodsList) {
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
				MasterOrderGoods ogs = map.get(customCode);
				setAvgTransactionPrice(ogs, ogBean);
				setAvgSettlementPrice(ogs, ogBean);
				setAvgShareBonus(ogs, ogBean);
				setAvgIntegralMoney(ogs, ogBean);
				setAvgDiscount(ogs, ogBean);
				setGoodsNumber(ogBean, ogs);
				logger.debug("订单" + order_sn + "商品" + customCode + "合并完成");
			}
		}
		for (Iterator<MasterOrderGoods> iterator = map.values().iterator(); iterator.hasNext();) {
			templist.add(iterator.next());
		}
		if (StringUtil.isListNull(templist)) {
			logger.debug("订单下发时合并商品异常.订单号：" + order_sn);
			throw new Exception("订单下发时合并商品异常.订单号：" + order_sn);
		}
		return templist;
	}

	private void setGoodsNumber(MasterOrderGoods ogBean, MasterOrderGoods ogs) {
		ogs.setGoodsNumber(ogs.getGoodsNumber() + ogBean.getGoodsNumber());
	}
	
	private void setAvgDiscount(MasterOrderGoods ogs, MasterOrderGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsNumber(), ogs.getDiscount(), ogBean.getGoodsNumber(), ogs.getDiscount());
		ogs.setDiscount(bd);
	}
	
	private void setAvgTransactionPrice(MasterOrderGoods ogs, MasterOrderGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsNumber(), ogs.getTransactionPrice(), ogBean.getGoodsNumber(), ogBean.getTransactionPrice());
		ogs.setTransactionPrice(bd);
		// ogs.setGoodsNumber((short) (ogs.getGoodsNumber() +
		// ogBean.getGoodsNumber()));
	}

	private void setAvgSettlementPrice(MasterOrderGoods ogs, MasterOrderGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsNumber(), ogs.getSettlementPrice(), ogBean.getGoodsNumber(), ogBean.getSettlementPrice());
		ogs.setSettlementPrice(bd);
	}
	
	private void setAvgShareBonus(MasterOrderGoods ogs, MasterOrderGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsNumber(), ogs.getShareBonus(), ogBean.getGoodsNumber(), ogBean.getShareBonus());
		ogs.setShareBonus(bd);
	}
	
	private void setAvgIntegralMoney(MasterOrderGoods ogs, MasterOrderGoods ogBean) {
		BigDecimal bd = setAvgPrice(ogs.getGoodsNumber(), ogs.getIntegralMoney(), ogBean.getGoodsNumber(), ogBean.getIntegralMoney());
		ogs.setIntegralMoney(bd);
	}

	private BigDecimal setAvgPrice(Integer goodsNumber, BigDecimal transactionPrice, Integer goodsNumber2, BigDecimal transactionPrice2) {
		BigDecimal price1 = transactionPrice.multiply(BigDecimal.valueOf(goodsNumber)).setScale(5, BigDecimal.ROUND_HALF_UP);
		BigDecimal price2 = transactionPrice2.multiply(BigDecimal.valueOf(goodsNumber2)).setScale(5, BigDecimal.ROUND_HALF_UP);
		BigDecimal sum = price1.add(price2);
		BigDecimal result = sum.divide(BigDecimal.valueOf(goodsNumber + goodsNumber2), 5, BigDecimal.ROUND_HALF_UP);
		return result;
	}
	
	private void monitorErrorMessage(String orderSn, String errorMessage) {
		String data = "{'orderId':'" + orderSn + "','orderException':'" + errorMessage + "'}";
		logger.debug("订单下发异常监控数据:" + data);
		orderCommonService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_EXCEPTION, data);
	}
}
