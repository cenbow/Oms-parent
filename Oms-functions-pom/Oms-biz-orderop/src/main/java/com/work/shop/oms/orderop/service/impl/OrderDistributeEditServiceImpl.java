package com.work.shop.oms.orderop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.erp.service.ErpInterfaceProxy;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.order.service.PurchaseOrderService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderDistributeEditService;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.NumberUtil;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.webservice.ErpResultBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

@Service
public class OrderDistributeEditServiceImpl implements OrderDistributeEditService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	@Resource
	private SystemShippingMapper systemShippingMapper;
//	@Resource
//	private StockCenterService stockCenterService;
	@Resource
	private ErpInterfaceProxy erpInterfaceProxy;

	@Resource
	private OrderQuestionLackSkuNewMapper orderQuestionLackSkuNewMapper;
	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private MasterOrderPayService masterOrderPayService;
	@Resource
	private DistributeShipService distributeShipService;

	@Resource(name="orderCancelService")
	private OrderCancelService orderCancelService;

	@Resource
	private ChannelStockService channelStockService;
	@Resource
	private OrderQuestionLackSkuNewDelMapper orderQuestionLackSkuNewDelMapper;

	@Resource(name = "orderConfirmService")
	private OrderConfirmService orderConfirmService;

	@Resource
	private PurchaseOrderService purchaseOrderService;

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editGoods(MasterOrderInfo master,
			OrderDistribute distribute, OrderInfoUpdateInfo infoUpdateInfo,
			String actionUser) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(master == null && distribute == null) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		if (StringUtil.isEmpty(actionUser)) {
			logger.error("[actionUser]传入参数为空，不能进行订单商品编辑操作！");
			info.setMessage("[actionUser]传入参数为空，不能进行订单商品编辑操作！");
			return info;
		}
		if (infoUpdateInfo == null) {
			logger.error("[infoUpdateInfo]传入参数为空，不能进行订单商品编辑操作！");
			info.setMessage("[infoUpdateInfo]传入参数为空，不能进行订单商品编辑操作！");
			return info;
		}
		List<OrderGoodsUpdateBean> orderGoodsUpdateBeans = infoUpdateInfo.getOrderGoodsUpdateInfos();
		if (StringUtil.isListNull(orderGoodsUpdateBeans)) {
			info.setMessage("[infoUpdateInfo]传入参数[orderGoodsUpdateInfos]为空，不能进行订单商品编辑操作！");
			return info;
		}
		// 校验订单编辑商品数据
		Map<String, String> validateMap = new HashMap<String, String>();
		for (OrderGoodsUpdateBean updateBean : orderGoodsUpdateBeans) {
			if (updateBean.getGoodsNumber() < 0) {
				if (updateBean.getId() == null || updateBean.getId().intValue() == 0) {
					info.setMessage("订单[" + updateBean.getOrderSn() +"]商品id不能为空！");
					return info;
				}
			} else {
				String skuKey = updateBean.getUniqueKey();
				String customCode = validateMap.get(skuKey);
				if (StringUtil.isNotEmpty(customCode)) {
					info.setMessage("订单商品惟一键[" + skuKey +"]重复，不能提交订单商品");
					return info;
				}
			}
		}
		// 获取订单扩展信息
		String masterOrderSn = "";
		/**
		 * 针对主单：（拆单前、拆单后）
		 *  拆单前：增删改商品信息 (已付款，逻辑删除)
		 *  拆单后：（未发货分配单商品）删改商品信息重新计算分摊金额，主订单下所有分配单都要做
		 */
		Double oldSumSettlement = 0D;// 原订单商品总财务价
		Double orderUseBonus = 0D;										// 订单使用红包总金额
//		byte spiltStatus = master.getSplitStatus();
		List<CardPackageUpdateInfo> packageUpdateInfos = infoUpdateInfo.getCardPackageUpdateInfos();
		String[] useBonus = {};											// 订单使用红包卡号列表
		if (StringUtil.isListNotNull(packageUpdateInfos)) {
			useBonus = new String[packageUpdateInfos.size()];
			int i = 0;
			for (CardPackageUpdateInfo packageUpdateInfo : packageUpdateInfos) {
				orderUseBonus += packageUpdateInfo.getCardMoney();
				useBonus[i] = packageUpdateInfo.getCardNo();
				i++;
			}
		}
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
		if (distribute != null) {
			masterOrderSn = distribute.getMasterOrderSn();
			criteria.andMasterOrderSnEqualTo(distribute.getMasterOrderSn());
			criteria.andOrderSnEqualTo(distribute.getOrderSn());
		} else if (master != null) {
			criteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			masterOrderSn = master.getMasterOrderSn();
		}
		List<MasterOrderGoods> orderGoodsOldList = masterOrderGoodsMapper.selectByExample(goodsExample);
		if (StringUtil.isListNotNull(orderGoodsOldList)) {
			// 商品唯一键KeyMap
			Map<String, MasterOrderGoods> oldGoodsMap = new HashMap<String, MasterOrderGoods>();
			// 商品主键KeyMap
			Map<Long, MasterOrderGoods> oldGoodsIdMap = new HashMap<Long, MasterOrderGoods>();
			// 发货仓商品数量
			Map<String, Integer> deptCodeGoodsCount = new HashMap<String, Integer>();
			for (MasterOrderGoods orderGoods : orderGoodsOldList) {
				String customCode = orderGoods.getCustomCode();
				String deptCode = orderGoods.getDepotCode();
				String extensionCode = orderGoods.getExtensionCode();
				String extensionId = orderGoods.getExtensionId();
				String skuKey = deptCode + customCode + extensionCode + extensionId;
				oldGoodsMap.put(skuKey, orderGoods);
				oldGoodsIdMap.put(orderGoods.getId(), orderGoods);
				Integer goodsNumber = deptCodeGoodsCount.get(deptCode);
				if (goodsNumber != null) {
					deptCodeGoodsCount.put(deptCode,  goodsNumber + orderGoods.getGoodsNumber());
				} else {
					deptCodeGoodsCount.put(deptCode, orderGoods.getGoodsNumber());
				}
				oldSumSettlement += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber();
			}
		}
		// 商品价格重新计算
		MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (extend == null) {
			logger.error("订单[" + masterOrderSn + "][MasterOrderInfoExtend]不能为空，不能进行订单商品编辑操作！");
			info.setMessage("订单[" + masterOrderSn + "][MasterOrderInfoExtend]不能为空，不能进行订单商品编辑操作！");
			return info;
		}
		try {
			MasterOrderInfoBean newMaster = new MasterOrderInfoBean();
//			Map<String, List<UpdateOrderDistributeBean>> supplierCodeMap = buildEditGoods(infoUpdateInfo, master, distribute, newMaster);
			
			Map<String, UpdateOrderDistributeBean> distributeMap = buildEditGoodsByOrderSn(infoUpdateInfo, master, distribute, newMaster);
			BigDecimal goodsUseBonus = newMaster.getBonus().setScale(2, BigDecimal.ROUND_HALF_UP);
			orderUseBonus = new BigDecimal(orderUseBonus).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (goodsUseBonus.doubleValue() > orderUseBonus) {
				info.setMessage("订单商品中使用的红包金额不能大于红包总面额");
				logger.error(masterOrderSn + "订单商品中使用的红包金额不能大于红包总面额");
				return info;
			}
			// 通知供应商更新商品信息
			// 未拆单 拆单中
			StringBuffer updateDistributeMsg = new StringBuffer();
//			if (spiltStatus != Constant.SPLIT_STATUS_UNSPLITED && spiltStatus != Constant.SPLIT_STATUS_SPLITTING) {
				/*for (String supplierCode : supplierCodeMap.keySet()) {
					OrderStatus orderStatus = new OrderStatus();
					orderStatus.setAdminUser(actionUser);
					orderStatus.setMessage("");
					List<UpdateOrderDistributeBean> distributeBeans = supplierCodeMap.get(supplierCode);
					if (supplierCode.equals(Constant.SUPPLIER_TYPE_MB)) {
						info = editGoodsByMbDistribute(getOrderSnsByUpdateObj(distributeBeans), distributeBeans, orderStatus, extend.getReviveStt());
					} else if (supplierCode.equals(Constant.SUPPLIER_TYPE_THIRD)){
						info = editGoodsByThirdDistribute(getOrderSnsByUpdateObj(distributeBeans), distributeBeans, orderStatus, extend.getReviveStt());
					}
					if (info.getIsOk() == Constant.OS_NO) {
						return info;
					}
				}*/
				Iterator<String> it = distributeMap.keySet().iterator();
				while(it.hasNext()) {
					String orderSn = it.next();
					if (StringUtil.isNotEmpty(orderSn)) {
						UpdateOrderDistributeBean distributeBean = distributeMap.get(orderSn);
						OrderDistribute orderDistribute = distributeBean.getDistribute();
						if (StringUtil.isListNull(distributeBean.getDeleteList()) && StringUtil.isListNull(distributeBean.getUpdateList())) {
							it.remove();
							continue;
						}
						if (orderDistribute.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLSHIPED) {
							it.remove();
							continue;
						}
						if (orderDistribute.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
							info.setMessage(" 订单[" + orderSn + "]要处于未确定状态");
							return info;
						}
						if (StringUtil.isTrimEmpty(orderDistribute.getSupplierCode())) {
							info.setMessage(" 订单[" + orderSn + "] 商品供应商标志缺失");
							return info;
						}
					}
				}
				/*for (String orderSn : distributeMap.keySet()) {
					if (StringUtil.isNotEmpty(orderSn)) {
						UpdateOrderDistributeBean distributeBean = distributeMap.get(orderSn);
						OrderDistribute orderDistribute = distributeBean.getDistribute();
						if (orderDistribute.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
							info.setMessage(" 订单[" + orderSn + "]要处于未确定状态");
							return info;
						}
						if (StringUtil.isTrimEmpty(orderDistribute.getSupplierCode())) {
							info.setMessage(" 订单[" + orderSn + "] 商品供应商标志缺失");
							return info;
						}
						if (StringUtil.isListNull(distributeBean.getDeleteList()) && StringUtil.isListNull(distributeBean.getUpdateList())) {
							distributeMap.remove(orderSn);
						}
					}
				}*/
				if (distributeMap != null && distributeMap.isEmpty()) {
					info.setIsOk(Constant.OS_YES);
					info.setMessage("订单商品没有变化");
					return info;
				}
				for (String orderSn : distributeMap.keySet()) {
					if (StringUtil.isNotEmpty(orderSn)) {
						OrderStatus orderStatus = new OrderStatus();
						orderStatus.setAdminUser(actionUser);
						orderStatus.setMessage("");
						UpdateOrderDistributeBean distributeBean = distributeMap.get(orderSn);
						OrderDistribute orderDistribute = distributeBean.getDistribute();
						// 通知供应商更新订单商品
						if (orderDistribute.getSupplierCode().equals(Constant.SUPPLIER_TYPE_MB)) {
							info = editGoodsByMbDistribute(orderDistribute.getOrderSn(), distributeBean, orderStatus, extend.getReviveStt());
						} else {
							info = editGoodsByThirdDistribute(orderDistribute.getOrderSn(), distributeBean, orderStatus, extend.getReviveStt());
						}
						if (info.getIsOk() == Constant.OS_NO) {
							return info;
						}
						List<UpdateOrderDistributeBean> distributeBeans = new ArrayList<UpdateOrderDistributeBean>();
						distributeBeans.add(distributeBean);
						info = updateOrderDistributeGoods(distributeBeans, extend.getReviveStt(), actionUser, master);
					} else {
						UpdateOrderDistributeBean distributeBean = distributeMap.get(orderSn);
						List<UpdateOrderDistributeBean> distributeBeans = new ArrayList<UpdateOrderDistributeBean>();
						distributeBeans.add(distributeBean);
						info = updateOrderDistributeGoods(distributeBeans, extend.getReviveStt(), actionUser, master);
					}
					if (info.getIsOk() == Constant.OS_NO) {
						return info;
					}
					updateDistributeMsg.append(info.getData());
				}
//			}
			if (info.getIsOk() == Constant.OS_NO) {
				return info;
			}
			// 如果更新单个子订单。其他子订单商品金额也需要参与计算。
			// 计算商品的应付款金额
			// 更新主订单信息
			//费用变更
			MasterOrderInfoBean updateMasterBean = builtMasterBean(newMaster, newMaster.getShippingTotalFee().doubleValue(),
					newMaster.getAfterChangePayable());
			if (updateMasterBean != null) {
				updateMasterBean.setPayStatus(master.getPayStatus());
			}
			ReturnInfo<String> tInfo = masterOrderPayService.editMasterOrderPay(masterOrderSn, master.getTotalPayable().doubleValue(), updateMasterBean);
			if (tInfo.getIsOk() == Constant.OS_NO) {
				return tInfo;
			}
			// 判断主订单是否有变化，更新差异并且记录更新日志
			StringBuffer msg = new StringBuffer();
			// 更新分配单信息
			/*for (String supplierCode : supplierCodeMap.keySet()) {
				List<UpdateOrderDistributeBean> distributeBeans = supplierCodeMap.get(supplierCode);
				info = updateOrderDistributeGoods(distributeBeans, extend.getReviveStt(), actionUser);
				if (info.getIsOk() == Constant.OS_NO) {
					return info;
				}
			}*/
			// 判断主订单是否有变化，更新差异并且记录更新日志
			MasterOrderInfo updateMaster = editMasterOrderInfo(master, msg, updateMasterBean);
			logger.debug("orderInfoMapper.updateByPrimaryKeySelective,OrderInfo:" + JSON.toJSONString(updateMaster));
			if (updateMaster != null) {
				masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			}
			// TODO 库存操作
			msg.append(updateDistributeMsg);
			if (StringUtil.isNotEmpty(msg.toString())) {
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, msg.toString(), actionUser);
			}
			// 订单发货状态判断
			// 分配单发货状态
			ReturnInfo shipInfo =  distributeShipService.judgeMasterShipedStatus(masterOrderSn);
			logger.info("订单[" + masterOrderSn + "]总发货单状态:" + JSON.toJSONString(shipInfo));
			if (shipInfo.getIsOk() == Constant.OS_YES) {
				OrderStatus orderStatus = new OrderStatus("订单删除商品发货创建退单", Constant.OS_STRING_SYSTEM);
				orderStatus.setType(ConstantValues.CREATE_RETURN.YES);
				ReturnInfo returnInfo = orderCancelService.createReturnOrder(masterOrderSn, orderStatus, 1);
				if (returnInfo.getIsOk() == Constant.OS_NO) {
					logger.error("订单[" + masterOrderSn + "]订单删除商品发货创建退单失败：" + info.getMessage());
				}
			}
			if (master.getSplitStatus().byteValue() != Constant.SPLIT_STATUS_SPLITED.byteValue()
					&& (master.getPayStatus() == Constant.OI_PAY_STATUS_PAYED 
						|| master.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED)) {
//				orderStockService.reloadOccupy(masterOrderSn);
			}
			if (shipInfo.getIsOk() == Constant.OS_YES) {
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setMasterOrderSn(masterOrderSn);
				orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
				orderStatus.setMessage("编辑商品发货确认:");
				orderStatus.setType(Constant.order_type_master);
				orderConfirmService.asynConfirmOrderByOrderSn(orderStatus);
			}
			info.setIsOk(Constant.OS_YES);
			info.setMessage("更新商品成功");
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "] 修改商品异常" + e.getMessage(), e);
			info.setMessage("订单[" + masterOrderSn + "] 修改商品异常" + e.getMessage());
		}
		return info;
	}
	
//	private List<OrderDistribute> bulidPayInfo(String masterOrderSn) {
//		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
//		List<OrderDistribute> distributes = 
//	}

	private MasterOrderInfo editMasterOrderInfo(MasterOrderInfo master, StringBuffer msg, MasterOrderInfoBean newMaster) {
		MasterOrderInfo newOrderInfo = new MasterOrderInfo();
		boolean flag = false;
		StringBuffer message = new StringBuffer();
		message.append("修改订单信息：");
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
		// 订单状态
		if (master.getPayStatus().intValue() == newMaster.getPayStatus().intValue()) {
			flag = true;
			newOrderInfo.setPayStatus(newMaster.getPayStatus());
			master.setPayStatus(newMaster.getPayStatus());
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
	
	private OrderDistribute editOrderDistribute(OrderDistribute distribute, StringBuffer msg, OrderDistributeBean newDistribute) {
		OrderDistribute newOrderDistribute = new OrderDistribute();
		boolean flag = false;
		StringBuffer message = new StringBuffer();
		message.append("修改订单信息：");
		// 商品总金额
		if (distribute.getGoodsAmount().compareTo(newDistribute.getGoodsAmount().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newGoodsAmount = newDistribute.getGoodsAmount().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderDistribute.setGoodsAmount(newGoodsAmount);
			message.append("</br>商品总金额：" + distribute.getGoodsAmount() + " → " + newGoodsAmount);
			distribute.setGoodsAmount(newGoodsAmount);
		}
		// 订单总金额
		BigDecimal totalFee = newDistribute.getGoodsAmount().subtract(newDistribute.getDiscount());
		if (distribute.getTotalFee().compareTo(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)) != 0 ) {
			flag = true;
			newOrderDistribute.setTotalFee(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)); // 订单总金额
			message.append("</br>订单总金额：" + distribute.getTotalFee() + " → " + newDistribute.getTotalFee());
			distribute.setTotalFee(totalFee.setScale(4, BigDecimal.ROUND_HALF_UP)); // 订单总金额
		}
		// 应付款总金额
		if (distribute.getTotalPayable().compareTo(new BigDecimal(newDistribute.getAfterChangePayable()).setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newTotalPayable = new BigDecimal(newDistribute.getAfterChangePayable()).setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderDistribute.setTotalPayable(newTotalPayable); // 应付款总金额
			message.append("</br>应付款总金额：" + distribute.getTotalPayable() + " → " + newTotalPayable);
			distribute.setTotalPayable(newTotalPayable); // 应付款总金额
		}
		// 商品折让金额
		if (distribute.getDiscount().compareTo(newDistribute.getDiscount().setScale(4, BigDecimal.ROUND_HALF_UP)) != 0) {
			flag = true;
			BigDecimal newDiscountAmount = newDistribute.getDiscount().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderDistribute.setDiscount(newDiscountAmount);
			message.append("</br>折让金额：" + distribute.getDiscount() + " → " + newDiscountAmount);
			distribute.setDiscount(newDiscountAmount);
		}
		// 商品总数量
		if (!distribute.getGoodsCount().equals(newDistribute.getGoodsCount())) {
			flag = true;
			newOrderDistribute.setGoodsCount(newDistribute.getGoodsCount());
			message.append("</br>商品总数量：" + distribute.getGoodsCount() + " → " + newDistribute.getGoodsCount());
			distribute.setGoodsCount(newDistribute.getGoodsCount());
		}
		// 红包总金额
		if (distribute.getBonus().compareTo(newDistribute.getBonus()) != 0) {
			flag = true;
			BigDecimal newOrderUseBonus = newDistribute.getBonus().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderDistribute.setBonus(newOrderUseBonus);
			message.append("</br>红包总金额：" + distribute.getBonus() + " → " + newOrderUseBonus);
			distribute.setBonus(newOrderUseBonus);
		}
		// 积分总金额
		if (distribute.getIntegralMoney().compareTo(newDistribute.getIntegralMoney()) != 0) {
			flag = true;
			BigDecimal newIntegralMoney = newDistribute.getIntegralMoney().setScale(4, BigDecimal.ROUND_HALF_UP);
			newOrderDistribute.setIntegralMoney(newIntegralMoney);
			message.append("</br>积分总金额：" + distribute.getIntegralMoney() + " → " + newIntegralMoney);
			distribute.setBonus(newIntegralMoney);
		}
		message.append("</br>");
		if (flag) {
			// orderInfo整理 订单状态 订单金额相关
			newOrderDistribute.setMasterOrderSn(distribute.getMasterOrderSn());
			newOrderDistribute.setPayStatus(newDistribute.getPayStatus());
			newOrderDistribute.setUpdateTime(new Date());
			msg.append(message.toString());
		} else {
			newOrderDistribute = null;
		}
		return newOrderDistribute;
	}

	/*
	 * 组装变更后订单、订单商品数据
	 * 更加供应商组装变更商品数据
	 * 1修改主单商品：
	 * 		1.1已拆单：根据子订单通知各供应商并且修改本地商品数据
	 * 			1.2 根据供应商分类批量同步至不同的供应商平台（MB、第三方）
	 * 		1.2未拆单：修改本地商品数据
	 * 2修改子单商品：
	 * 		2.1 根据子订单通知各供应商并且修改本地商品数据（同1.1）
	 * 
	 * 
	 */
	private Map<String, List<UpdateOrderDistributeBean>> buildEditGoods(OrderInfoUpdateInfo infoUpdateInfo,
			MasterOrderInfo master, OrderDistribute distribute, MasterOrderInfoBean newMaster) {
		logger.debug("构建OMS订单、订单商品信息以及供应商订单、订单商品信息");
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
		List<MasterOrderGoods> notUpdateOrderGoodsList = new ArrayList<MasterOrderGoods>();
		String masterOrderSn = "";
		if (distribute != null) {
			criteria.andOrderSnEqualTo(distribute.getOrderSn());
			MasterOrderGoodsExample notUpdateGoodsExample = new MasterOrderGoodsExample();
			notUpdateGoodsExample.or().andMasterOrderSnEqualTo(distribute.getMasterOrderSn())
					.andOrderSnNotEqualTo(distribute.getOrderSn()).andIsDelEqualTo(0);
			notUpdateOrderGoodsList = masterOrderGoodsMapper.selectByExample(notUpdateGoodsExample);
			masterOrderSn = distribute.getMasterOrderSn();
		} else if (master != null) {
			masterOrderSn = master.getMasterOrderSn();
		}
		criteria.andMasterOrderSnEqualTo(masterOrderSn);
		criteria.andIsDelEqualTo(0);
		List<MasterOrderGoods> orderGoodsOldList = masterOrderGoodsMapper.selectByExample(goodsExample);
		// 商品唯一键KeyMap
		Map<String, MasterOrderGoods> oldGoodsMap = new HashMap<String, MasterOrderGoods>();
		// 商品主键KeyMap
		Map<Long, MasterOrderGoods> oldGoodsIdMap = new HashMap<Long, MasterOrderGoods>();
		// 订单分配单发货仓
		Set<String> depotSet = new HashSet<String>();
		Double oldDisSettlement = 0D;														// 原订单商品总财务价
		for (MasterOrderGoods orderGoods : orderGoodsOldList) {
			String orderSn = orderGoods.getOrderSn(), customCode = orderGoods.getCustomCode(), deptCode = orderGoods.getDepotCode(),
					extensionCode = orderGoods.getExtensionCode(), extensionId = orderGoods.getExtensionId();
			String skuKey = orderSn + deptCode + customCode + extensionCode + extensionId;
			oldGoodsMap.put(skuKey, orderGoods);
			oldGoodsIdMap.put(orderGoods.getId(), orderGoods);
			depotSet.add(orderSn + deptCode);
			oldDisSettlement += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber();
		}
		// 商品价格重新计算
		// 遍历修改后商品信息 找出做过新增、删除、修改的商品并分别放入各自的集合中
		// 未拆单
		// 已拆单：根据子订单更新商品且同步供应商
		Double settlement = 0D;											// 商品总财务价
		Double transaction = 0D;										// 商品总成交价
		Double afterChangePayable = 0D;									// 订单商品变更后应付款
		Double goodsAmount = 0D;										// 商品总金额
		Integer goodsNumber = 0;										// 商品总数量
		BigDecimal goodsUseBonus = new BigDecimal(0.0);					// 商品分摊红包总金额
		Double integralMoney = 0D;										// 订单使用积分金额
		Double orderUseBonus = 0D;										// 订单使用红包总金额
		Double discount = 0D;											// 商品总折扣
		Map<String, UpdateOrderDistributeBean> updateDistributeMap = new HashMap<String, UpdateOrderDistributeBean>();
		// 比对更新后商品变化（增删改未变）
		for(OrderGoodsUpdateBean updateObj : infoUpdateInfo.getOrderGoodsUpdateInfos()) {
			updateObj.setMasterOrderSn(masterOrderSn);
			String orderSnGroupKey = StringUtil.isTrimEmpty(updateObj.getOrderSn()) ? Constant.DETAILS_DEPOT_CODE : updateObj.getOrderSn();
			String skuKey = updateObj.getUniqueKey();
			// 根据商品Id或者商品唯一键确定原商品记录
			MasterOrderGoods oldGoods = null;
			if (updateObj.getId() != null) {
				oldGoods = oldGoodsIdMap.get(updateObj.getId());
				oldGoodsIdMap.remove(updateObj.getId());
			} else {
				oldGoods = oldGoodsMap.get(skuKey);
				oldGoodsMap.remove(skuKey);
			}
			// 删除
			if (updateObj.getGoodsNumber().intValue() < 0) {
				// 移除原纪录数据
				editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 1, editOrderGoodsDeleteGoods(oldGoods, updateObj.getGoodsNumber()));
				continue;
			} else if (null != updateObj.getId() || oldGoods != null) {
				// 更新
				Double shareBonusChange = Math.abs(NumberUtil.sub(updateObj.getDoubleShareBonus(), oldGoods.getShareBonus().doubleValue()));
				Double transactionChange = Math.abs(NumberUtil.sub(updateObj.getDoubleTransactionPrice(), oldGoods.getTransactionPrice().doubleValue()));
				// 金额,折扣有变化|| C2B商品图片信息变化
				if (shareBonusChange > 0.0001 || transactionChange > 0.0001
						|| ((Constant.ORDER_TYPE_C2B.equals(updateObj.getExtensionCode())
								|| Constant.ORDER_TYPE_C2M.equals(updateObj.getExtensionCode()))
								&& !StringUtils.equalsIgnoreCase(oldGoods.getC2mItem(), updateObj.getC2mItem()))) {
					editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 2, editOrderGoodsUpdateGoods(updateObj));
				} else {
					// 商品颜色尺码变更
					if (updateObj.getCustomCode().equals(oldGoods.getCustomCode())) {
						editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 3, editOrderGoodsUpdateGoods(updateObj));
					} else {
						editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 2, editOrderGoodsUpdateGoods(updateObj));
					}
				}
			} else {
				// 新增
				editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 0, editOrderGoodsUpdateGoods(updateObj));
			}
			goodsUseBonus = goodsUseBonus.add(new BigDecimal(updateObj.getDoubleShareBonus() * updateObj.getGoodsNumber()));
			transaction += (updateObj.getDoubleTransactionPrice() * updateObj.getGoodsNumber());
			settlement += (updateObj.getDoubleSettlementPrice() * updateObj.getGoodsNumber());
			if (updateObj.getIntegralMoney() != null) {
				integralMoney += (updateObj.getIntegralMoney() * updateObj.getGoodsNumber());					// 积分
			}
			goodsAmount += updateObj.getDoubleGoodsPrice() * updateObj.getGoodsNumber();
			discount += (updateObj.getDoubleGoodsPrice() - updateObj.getDoubleTransactionPrice());
			goodsNumber += updateObj.getGoodsNumber().intValue();
			orderUseBonus += updateObj.getDoubleShareBonus();
		}
		// 未更新商品数据
		if (StringUtil.isListNotNull(notUpdateOrderGoodsList)) {
			for (MasterOrderGoods orderGoods :notUpdateOrderGoodsList) {
				goodsUseBonus = goodsUseBonus.add(new BigDecimal(orderGoods.getShareBonus().doubleValue() * orderGoods.getGoodsNumber()));
				transaction += (orderGoods.getTransactionPrice().doubleValue() * orderGoods.getGoodsNumber());
				settlement += (orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber());
				if (orderGoods.getIntegralMoney() != null) {
					integralMoney += (orderGoods.getIntegralMoney().doubleValue() * orderGoods.getGoodsNumber());					// 积分
				}
				goodsAmount += orderGoods.getGoodsPrice().doubleValue() * orderGoods.getGoodsNumber();
				discount += (orderGoods.getGoodsPrice().doubleValue() - orderGoods.getTransactionPrice().doubleValue());
				goodsNumber += orderGoods.getGoodsNumber().intValue();
				orderUseBonus += orderGoods.getShareBonus().doubleValue();
			}
		}
		afterChangePayable = transaction + master.getShippingTotalFee().doubleValue() - master.getMoneyPaid().doubleValue()
				- master.getSurplus().doubleValue() - integralMoney - orderUseBonus;
		newMaster.setSettlementPrice(new BigDecimal(settlement));
		newMaster.setGoodsAmount(new BigDecimal(goodsAmount));
		newMaster.setDiscount(new BigDecimal(discount));
		newMaster.setGoodsCount(goodsNumber);
		newMaster.setBonus(new BigDecimal(orderUseBonus));
		newMaster.setIntegralMoney(new BigDecimal(integralMoney));
		newMaster.setMoneyPaid(master.getMoneyPaid());
		newMaster.setAfterChangePayable(new BigDecimal(afterChangePayable));
		newMaster.setShippingTotalFee(master.getShippingTotalFee());
		newMaster.setTotalFee(new BigDecimal(settlement + master.getShippingTotalFee().doubleValue()));
		// 组装出变化后的订单商品信息
		Map<String, List<UpdateOrderDistributeBean>> supplierCodeMap = new HashMap<String, List<UpdateOrderDistributeBean>>();
		for (String distributeKey : updateDistributeMap.keySet()) {
			// 未拆单类型
			UpdateOrderDistributeBean bean = updateDistributeMap.get(distributeKey);
			if (bean == null) {
				continue ;
			}
			OrderDistribute updateDistribute = bean.getDistribute();
			String supplierType = "";
			if (updateDistribute != null) {
				supplierType = bean.getDistribute().getSupplierCode().equals(Constant.SUPPLIER_TYPE_MB) 
						? Constant.SUPPLIER_TYPE_MB : Constant.SUPPLIER_TYPE_THIRD;
			} else {
				supplierType = Constant.SUPPLIER_TYPE_MB;
			}
			List<UpdateOrderDistributeBean> distributeBeans = supplierCodeMap.get(supplierType);
			if (StringUtil.isListNull(distributeBeans)) {
				distributeBeans = new ArrayList<UpdateOrderDistributeBean>();
			}
			distributeBeans.add(bean);
			supplierCodeMap.put(supplierType, distributeBeans);
		}
		return supplierCodeMap;
	}
	
	private Map<String, UpdateOrderDistributeBean> buildEditGoodsByOrderSn(OrderInfoUpdateInfo infoUpdateInfo,
			MasterOrderInfo master, OrderDistribute distribute, MasterOrderInfoBean newMaster) {
		logger.debug("构建OMS订单、订单商品信息以及供应商订单、订单商品信息");
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
		List<MasterOrderGoods> notUpdateOrderGoodsList = new ArrayList<MasterOrderGoods>();
		String masterOrderSn = "";
		if (distribute != null) {
			criteria.andOrderSnEqualTo(distribute.getOrderSn());
			// 非当前交货单其他有效交货单
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria noCancelcriteria = distributeExample.or();
			noCancelcriteria.andMasterOrderSnEqualTo(distribute.getMasterOrderSn());
			noCancelcriteria.andOrderSnNotEqualTo(distribute.getOrderSn());
			noCancelcriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			List<OrderDistribute> noCancelDistributes = this.orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNotNull(noCancelDistributes)) {
				List<String> orderSns = new ArrayList<String>();
				for (OrderDistribute noCancelDistribute : noCancelDistributes) {
					orderSns.add(noCancelDistribute.getOrderSn());
				}
				MasterOrderGoodsExample notUpdateGoodsExample = new MasterOrderGoodsExample();
				notUpdateGoodsExample.or().andMasterOrderSnEqualTo(distribute.getMasterOrderSn())
						.andOrderSnIn(orderSns).andIsDelEqualTo(0);
				notUpdateOrderGoodsList = masterOrderGoodsMapper.selectByExample(notUpdateGoodsExample);
			}
			masterOrderSn = distribute.getMasterOrderSn();
		} else if (master != null) {
			masterOrderSn = master.getMasterOrderSn();
		}
		criteria.andMasterOrderSnEqualTo(masterOrderSn);
		criteria.andIsDelEqualTo(0);
		List<MasterOrderGoods> orderGoodsOldList = masterOrderGoodsMapper.selectByExample(goodsExample);
		// 商品唯一键KeyMap
		Map<String, MasterOrderGoods> oldGoodsMap = new HashMap<String, MasterOrderGoods>();
		// 商品主键KeyMap
		Map<Long, MasterOrderGoods> oldGoodsIdMap = new HashMap<Long, MasterOrderGoods>();
		// 订单分配单发货仓
		Set<String> depotSet = new HashSet<String>();
		Double oldDisSettlement = 0D;														// 原订单商品总财务价
		for (MasterOrderGoods orderGoods : orderGoodsOldList) {
			String orderSn = orderGoods.getOrderSn(), customCode = orderGoods.getCustomCode(), deptCode = orderGoods.getDepotCode(),
					extensionCode = orderGoods.getExtensionCode(), extensionId = orderGoods.getExtensionId();
			String skuKey = orderSn + deptCode + customCode + extensionCode + extensionId;
			oldGoodsMap.put(skuKey, orderGoods);
			oldGoodsIdMap.put(orderGoods.getId(), orderGoods);
			depotSet.add(orderSn + deptCode);
			oldDisSettlement += orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber();
		}
		// 商品价格重新计算
		// 遍历修改后商品信息 找出做过新增、删除、修改的商品并分别放入各自的集合中
		// 未拆单
		// 已拆单：根据子订单更新商品且同步供应商
		Double settlement = 0D;											// 商品总财务价
		Double transaction = 0D;										// 商品总成交价
		Double afterChangePayable = 0D;									// 订单商品变更后应付款
		Double goodsAmount = 0D;										// 商品总金额
		Integer goodsNumber = 0;										// 商品总数量
		BigDecimal goodsUseBonus = new BigDecimal(0.0);					// 商品分摊红包总金额
		Double integralMoney = 0D;										// 订单使用积分金额
		Double orderUseBonus = 0D;										// 订单使用红包总金额
		Double discount = 0D;											// 商品总折扣
		Map<String, UpdateOrderDistributeBean> updateDistributeMap = new HashMap<String, UpdateOrderDistributeBean>();
		// 比对更新后商品变化（增删改未变）
		for(OrderGoodsUpdateBean updateObj : infoUpdateInfo.getOrderGoodsUpdateInfos()) {
			updateObj.setMasterOrderSn(masterOrderSn);
			String orderSnGroupKey = StringUtil.isTrimEmpty(updateObj.getOrderSn()) ? Constant.DETAILS_DEPOT_CODE : updateObj.getOrderSn();
			String skuKey = updateObj.getUniqueKey();
			// 根据商品Id或者商品唯一键确定原商品记录
			MasterOrderGoods oldGoods = null;
			if (updateObj.getId() != null) {
				oldGoods = oldGoodsIdMap.get(updateObj.getId());
				oldGoodsIdMap.remove(updateObj.getId());
			} else {
				oldGoods = oldGoodsMap.get(skuKey);
				oldGoodsMap.remove(skuKey);
			}
			// 删除
			if (updateObj.getGoodsNumber().intValue() < 0) {
				// 移除原纪录数据
				editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 1, editOrderGoodsDeleteGoods(oldGoods, updateObj.getGoodsNumber()));
				continue;
			} else if (null != updateObj.getId() || oldGoods != null) {
				// 更新
				Double shareBonusChange = Math.abs(NumberUtil.sub(updateObj.getDoubleShareBonus(), oldGoods.getShareBonus().doubleValue()));
				Double transactionChange = Math.abs(NumberUtil.sub(updateObj.getDoubleTransactionPrice(), oldGoods.getTransactionPrice().doubleValue()));
				// 金额,折扣有变化|| C2B商品图片信息变化
				if (shareBonusChange > 0.0001 || transactionChange > 0.0001
						|| ((Constant.ORDER_TYPE_C2B.equals(updateObj.getExtensionCode())
								|| Constant.ORDER_TYPE_C2M.equals(updateObj.getExtensionCode()))
								&& !StringUtils.equalsIgnoreCase(oldGoods.getC2mItem(), updateObj.getC2mItem()))) {
					editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 2, editOrderGoodsUpdateGoods(updateObj));
				} else {
					// 商品颜色尺码变更
					if (updateObj.getCustomCode().equals(oldGoods.getCustomCode())) {
						editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 3, editOrderGoodsUpdateGoods(updateObj));
					} else {
						editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 2, editOrderGoodsUpdateGoods(updateObj));
					}
				}
			} else {
				// 新增
				editOrderDistributeGoods(updateDistributeMap, orderSnGroupKey, 0, editOrderGoodsUpdateGoods(updateObj));
			}
			goodsUseBonus = goodsUseBonus.add(new BigDecimal(updateObj.getDoubleShareBonus() * updateObj.getGoodsNumber()));
			transaction += (updateObj.getDoubleTransactionPrice() * updateObj.getGoodsNumber());
			settlement += (updateObj.getDoubleSettlementPrice() * updateObj.getGoodsNumber());
			if (updateObj.getIntegralMoney() != null) {
				integralMoney += (updateObj.getIntegralMoney() * updateObj.getGoodsNumber());					// 积分
			}
			goodsAmount += updateObj.getDoubleGoodsPrice() * updateObj.getGoodsNumber();
			discount += (updateObj.getDoubleGoodsPrice() - updateObj.getDoubleTransactionPrice()) * updateObj.getGoodsNumber();
			goodsNumber += updateObj.getGoodsNumber().intValue();
			orderUseBonus += updateObj.getDoubleShareBonus() * updateObj.getGoodsNumber();
		}
		// 未更新商品数据
		if (StringUtil.isListNotNull(notUpdateOrderGoodsList)) {
			for (MasterOrderGoods orderGoods :notUpdateOrderGoodsList) {
				goodsUseBonus = goodsUseBonus.add(new BigDecimal(orderGoods.getShareBonus().doubleValue() * orderGoods.getGoodsNumber()));
				transaction += (orderGoods.getTransactionPrice().doubleValue() * orderGoods.getGoodsNumber());
				settlement += (orderGoods.getSettlementPrice().doubleValue() * orderGoods.getGoodsNumber());
				if (orderGoods.getIntegralMoney() != null) {
					integralMoney += (orderGoods.getIntegralMoney().doubleValue() * orderGoods.getGoodsNumber());					// 积分
				}
				goodsAmount += orderGoods.getGoodsPrice().doubleValue() * orderGoods.getGoodsNumber();
				discount += (orderGoods.getGoodsPrice().doubleValue() - orderGoods.getTransactionPrice().doubleValue()) * orderGoods.getGoodsNumber();
				goodsNumber += orderGoods.getGoodsNumber().intValue();
				orderUseBonus += orderGoods.getShareBonus().doubleValue() * orderGoods.getGoodsNumber();
			}
		}
		/*afterChangePayable = settlement + master.getShippingTotalFee().doubleValue() - master.getMoneyPaid().doubleValue()
				- master.getSurplus().doubleValue() - master.getIntegralMoney().doubleValue() - master.getBonus().doubleValue();*/
		afterChangePayable = transaction + master.getShippingTotalFee().doubleValue() - master.getMoneyPaid().doubleValue()
				- master.getSurplus().doubleValue() - integralMoney - orderUseBonus;
		newMaster.setSettlementPrice(new BigDecimal(settlement));
		newMaster.setGoodsAmount(new BigDecimal(goodsAmount));
		newMaster.setDiscount(new BigDecimal(discount));
		newMaster.setGoodsCount(goodsNumber);
		newMaster.setBonus(new BigDecimal(orderUseBonus));
		newMaster.setIntegralMoney(new BigDecimal(integralMoney));
		newMaster.setMoneyPaid(master.getMoneyPaid());
		newMaster.setSurplus(master.getSurplus());
		newMaster.setAfterChangePayable(new BigDecimal(afterChangePayable));
		newMaster.setShippingTotalFee(master.getShippingTotalFee());
		newMaster.setTotalFee(new BigDecimal(settlement + master.getShippingTotalFee().doubleValue()));
		// 组装出变化后的订单商品信息
		return updateDistributeMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editGoodsByMasterSn(String masterOrderSn,
			OrderInfoUpdateInfo infoUpdateInfo, String actionUser) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (actionUser == null) {
			logger.error("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			info.setMessage("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			return info;
		}
		logger.info("订单商品编辑：masterOrderSn=" + masterOrderSn + ";actionUser=" + actionUser + ";infoUpdateInfo=" + infoUpdateInfo);
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if(master == null) {
			logger.warn("订单[" + masterOrderSn + "]不存在，不能编辑商品");
			info.setMessage("订单[" + masterOrderSn + "]不存在，不能编辑商品");
			return info;
		}
		if (master.getShipStatus() > Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			logger.error("订单[" + masterOrderSn + "]订单已经发货，不能进行订单编辑商品操作！");
			info.setMessage("订单[" + masterOrderSn + "]订单已经发货，不能进行订单编辑商品操作！");
			return info;
		}
		info = editGoods(master, null, infoUpdateInfo, actionUser);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editGoodsByOrderSn(String orderSn,
			OrderInfoUpdateInfo infoUpdateInfo, String actionUser) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if (actionUser == null) {
			logger.error("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			info.setMessage("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			return info;
		}
		logger.info("订单商品编辑：orderSn=" + orderSn + ";actionUser=" + actionUser + ";infoUpdateInfo=" + infoUpdateInfo);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if(distribute == null) {
			logger.warn("订单[" + orderSn + "]不存在，不能编辑商品");
			info.setMessage("订单[" + orderSn + "]不存在，不能编辑商品");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能编辑商品");
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能编辑商品");
			return info;
		}
		if (distribute.getShipStatus() > Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			logger.error("订单[" + orderSn + "]订单已经发货，不能进行订单编辑商品操作！");
			info.setMessage("订单[" + orderSn + "]订单已经发货，不能进行订单编辑商品操作！");
			return info;
		}
		info = editGoods(master, distribute, infoUpdateInfo, actionUser);
		return info;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editShippingFee(String masterOrderSn, String actionUser, Double shippingFee){
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (actionUser == null) {
			logger.error("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			info.setMessage("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			return info;
		}
		logger.debug("订单费用编辑：masterOrderSn=" + masterOrderSn + ";actionUser=" + actionUser + ";shippingFee=" + shippingFee);
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if(master == null) {
			logger.warn("订单[" + masterOrderSn + "]不存在，不能编辑费用");
			info.setMessage("订单[" + masterOrderSn + "]不存在，不能编辑费用");
			return info;
		}
		//费用变更
		double shipDiff = shippingFee.doubleValue() - master.getShippingTotalFee().doubleValue();
		double newTotalPayable = master.getTotalFee().doubleValue() - master.getMoneyPaid().doubleValue()
				- master.getSurplus().doubleValue() - master.getBonus().doubleValue()
				- master.getIntegralMoney().doubleValue() + shipDiff;
		MasterOrderInfoBean newMaster = builtMasterBean(master, shippingFee, new BigDecimal(newTotalPayable));
		ReturnInfo<String> tInfo = masterOrderPayService.editMasterOrderPay(masterOrderSn, master.getTotalPayable().doubleValue(), newMaster);
		if (tInfo.getIsOk() == Constant.OS_NO) {
			return tInfo;
		}
		// 判断主订单是否有变化，更新差异并且记录更新日志
		StringBuffer msg = new StringBuffer();
		msg.append(tInfo.getData());
		MasterOrderInfo updateMaster = editMasterOrderInfo(master, msg, newMaster);
		if(updateMaster != null){
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
		}
		masterOrderActionService.insertOrderActionBySn(masterOrderSn, msg.toString(), actionUser);
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单配送信息更新成功");
		return info;
	}

	/**
	 * 编辑承运商
	 * @param modifyInfo 更新信息
	 * @return ReturnInfo
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public ReturnInfo editShippingType(ConsigneeModifyInfo modifyInfo) {
		logger.info("订单承运商编辑：modifyInfo=" + JSON.toJSONString(modifyInfo));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (modifyInfo == null) {
			logger.error("[modifyInfo]不能都为空！");
			info.setMessage("[modifyInfo]不能都为空！");
			return info;
		}
		String orderSn = modifyInfo.getOrderSn();
		String actionUser = modifyInfo.getActionUser();
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if (StringUtil.isTrimEmpty(modifyInfo.getShippingCode()) && modifyInfo.getShippingId() == null ) {
			logger.error("[shippingId][shippingCode]不能都为空！");
			info.setMessage("[shippingId][shippingCode]不能都为空！");
			return info;
		}
		if (StringUtil.isTrimEmpty(modifyInfo.getDepotCode()) ) {
			logger.error("[depotCode]不能都为空！");
			info.setMessage("[depotCode]不能都为空！");
			return info;
		}
		if (actionUser == null) {
			logger.error("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			info.setMessage("[actionUser]传入参数为空，不能进行订单编辑商品操作！");
			return info;
		}
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if(distribute == null) {
			logger.warn("订单[" + orderSn + "]不存在，不能编辑承运商");
			info.setMessage("订单[" + orderSn + "]不存在，不能编辑承运商");
			return info;
		}
		MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (extend == null) {
			logger.warn("订单[" + distribute.getMasterOrderSn() + "]扩展信息不存在，不能编辑承运商");
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]扩展信息不存在，不能编辑承运商");
			return info;
		}

		OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
		depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(modifyInfo.getDepotCode());
		List<OrderDepotShip> orderDepotShips = orderDepotShipMapper.selectByExample(depotShipExample);
		if (StringUtil.isListNull(orderDepotShips)) {
			info.setMessage("订单[" + orderSn + "]分配仓[" + modifyInfo.getDepotCode() + "] 不存在，不能编辑承运商");
			return info;
		}
		OrderDepotShip depotShip = orderDepotShips.get(0);
		SystemShipping shipping = new SystemShipping();
		String actionNote = "";
		if (modifyInfo.getShippingId() != null && modifyInfo.getShippingId() > 0) {
			shipping = systemShippingMapper.selectByPrimaryKey(modifyInfo.getShippingId());
		} else {
			SystemShippingExample example = new SystemShippingExample();
			example.or().andShippingCodeEqualTo(modifyInfo.getShippingCode());
			List<SystemShipping> shippings = systemShippingMapper.selectByExample(example);
			if(StringUtil.isListNull(shippings)){
				info.setMessage("订单[" + orderSn + "]承运商[" + modifyInfo.getShippingCode() + "] 不存在，不能编辑承运商");
				return info;
			}
			shipping = shippings.get(0);
		}
		if (shipping == null) {
			info.setMessage("订单[" + orderSn + "]承运商[" + modifyInfo.getShippingId() + "] 不存在，不能编辑承运商");
			return info;
		}
		OrderDepotShip updateDepotShip = new OrderDepotShip();
		updateDepotShip.setDepotCode(modifyInfo.getDepotCode());
		updateDepotShip.setOrderSn(orderSn);
		updateDepotShip.setShippingId(shipping.getShippingId());
		updateDepotShip.setShippingName(shipping.getShippingName());
		updateDepotShip.setUpdateTime(new Date());
		actionNote += "编辑承运商：承运商由 "+ depotShip.getShippingName()+" 修改为 "+ shipping.getShippingName() + ";<br />";
		OrderDepotShipExample example = new OrderDepotShipExample();
		OrderDepotShipExample.Criteria criteria = example.or();
		criteria.andOrderSnEqualTo(orderSn);
		criteria.andDepotCodeEqualTo(modifyInfo.getDepotCode());
		if (StringUtil.isNotEmpty(modifyInfo.getInvoiceNo())) {
			updateDepotShip.setInvoiceNo(modifyInfo.getInvoiceNo());
			criteria.andInvoiceNoEqualTo(depotShip.getInvoiceNo());
			actionNote += "快递单号: " + depotShip.getInvoiceNo()  + " 修改为 "+ modifyInfo.getInvoiceNo() + ";<br />";
		}
		orderDepotShipMapper.updateByExampleSelective(updateDepotShip, example);
		distributeActionService.addOrderAction(orderSn, actionNote, actionUser);
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单修改承运商更新成功");
		return info;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editOrderOther(String masterOrderSn, String actionUser, OrderOtherModifyInfo otherModify){
		logger.debug("订单其他更新masterOrderSn:"+masterOrderSn+",actionUser:"+actionUser+",otherModify:"+JSON.toJSONString(otherModify));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		info.setMessage("订单其它信息更新失败");
		try {
			if(StringUtils.isBlank(masterOrderSn)){
				info.setMessage("[masterOrderSn]订单号参数不能为空");
				return info;
			}
			if(StringUtils.isBlank(actionUser)){
				info.setMessage("[actionUser]操作人参数不能为空");
				return info;
			}
			if(otherModify == null){
				info.setMessage("[otherModify]其他信息参数不能为空");
				return info;
			}
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(master == null){
				info.setMessage("订单[" + masterOrderSn + "] 不存在");
				return info;
			}
			MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if(extend == null){
				info.setMessage("订单[" + masterOrderSn + "] 扩展信息不存在");
				return info;
			}
			if(master.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_UNCONFIRMED){
				info.setMessage("订单[" + masterOrderSn + "] 非未确认状态不可进行修改操作");
				return info;
			}
			String actionNote = "订单其它信息更新.";
			MasterOrderInfoExtend updateExtend = new MasterOrderInfoExtend();
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateExtend.setMasterOrderSn(masterOrderSn);
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setUpdateTime(new Date());
			if(!StringUtils.equalsIgnoreCase(updateExtend.getInvType(), otherModify.getInvType() + StringUtils.EMPTY)){
				actionNote += "发票类型由 "+ extend.getInvType()+" 修改为" + otherModify.getInvType() +";<br />";
				updateExtend.setInvType(otherModify.getInvType() + StringUtils.EMPTY);
			}
			if(!StringUtils.equalsIgnoreCase(extend.getInvPayee(), otherModify.getInvPayee())){
				actionNote += "发票抬头由 " + extend.getInvPayee()+" 修改为" + otherModify.getInvPayee() +";<br />";
				updateExtend.setInvPayee(otherModify.getInvPayee());
			}
			if(!StringUtils.equalsIgnoreCase(extend.getInvContent(), otherModify.getInvContent())){
				actionNote += "发票内容由 "+extend.getInvContent()+" 修改为" + otherModify.getInvContent() +";<br />";
				updateExtend.setInvContent(otherModify.getInvContent());
			}
			if(!StringUtils.equalsIgnoreCase(master.getPostscript(), otherModify.getPostscript())){
				actionNote += "客户给商家留言由 "+ master.getPostscript()+" 修改为" + otherModify.getPostscript() +";<br />";
				updateMaster.setPostscript(otherModify.getPostscript());
			}
			if(!StringUtils.equalsIgnoreCase(master.getToBuyer(), otherModify.getToBuyer())){
				actionNote += "商家给客户的留言由 "+ master.getToBuyer()+" 修改为" + otherModify.getToBuyer() +";<br />";
				updateMaster.setToBuyer(otherModify.getToBuyer());
			}
			if(!StringUtils.equalsIgnoreCase(master.getHowOos(), otherModify.getHowOos())){
				actionNote += "缺货处理方式由 "+ master.getToBuyer()+" 修改为" + otherModify.getToBuyer() +";<br />";
				updateMaster.setHowOos(otherModify.getHowOos());
			}
			masterOrderInfoExtendMapper.updateByPrimaryKeySelective(updateExtend);
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, actionNote, actionUser);
			info.setIsOk(ConstantValues.YESORNO_YES);
			info.setMessage("订单其它信息更新成功");
		} catch (Exception e) {
			logger.error("updateOrderOtherPage.orderSn:"+ masterOrderSn + e.getMessage(), e);
			info.setIsOk(ConstantValues.YESORNO_NO);
			info.setMessage("订单其它信息更新失败");
		}
		return info;
	}


	/**
	 * 订单商品编辑
	 * 
	 * @param orderSn
	 * @param distributeBean
	 * @param orderStatus
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ReturnInfo editGoodsByThirdDistribute(String orderSn, UpdateOrderDistributeBean distributeBean,
			OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(distributeBean == null) {
			logger.warn("[" + orderSn + "]订单不存在，不能编辑商品");
			info.setMessage("[" + orderSn + "]订单不存在，不能编辑商品");
			return info;
		}
		logger.debug("订单编辑商品 orderSns=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = distributeBean.getDistribute();
		if (distribute.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED) {
			if (StringUtil.isListNotNull(distributeBean.getDeleteList())
					|| StringUtil.isListNotNull(distributeBean.getUpdateList()) && distribute != null) {
				if (OrderAttributeUtil.doERP(distribute, isrelive)) {
					
				}
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单[" + orderSn + "]通知供应商编辑商品成功");
		logger.debug("订单[" + orderSn + "]通知供应商编辑商品成功");
		return info;
	}
	
	
	/**
	 * 订单编辑商品(共用方法)
	 * 
	 * @param orderSn
	 * @param distributeBean
	 * @param orderStatus
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ReturnInfo editGoodsByMbDistribute(String orderSn, UpdateOrderDistributeBean distributeBean,
			OrderStatus orderStatus, int isrelive) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(distributeBean == null) {
			logger.warn("[" + orderSn + "]订单不存在，不能编辑商品");
			info.setMessage("[" + orderSn + "]订单不存在，不能编辑商品");
			return info;
		}
		logger.debug("订单编辑商品 orderSns=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = distributeBean.getDistribute();
		// 调用ERP
		ErpResultBean res = ErpResultBean.getNewInstance();
		OrderInfoMappingBean updateErpGoods = new OrderInfoMappingBean();
		updateErpGoods.setDetail_ERP(createMbIncrementGoods(distributeBean));
		UpdateOrderInfo updateOrderInfo = buildMbUpdateOrderInfo(distribute, updateErpGoods, false);
//		logger.info("订单["+ orderSn +"]修改商品同步至ERP订单信息 start：>>>>>" + updateOrderInfo.toString());
		// 商品没有变化时不做通知ERP处理
		if (distribute.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED) {
			if (StringUtil.isListNotNull(distributeBean.getDeleteList())
					|| StringUtil.isListNotNull(distributeBean.getUpdateList())) {
				if (OrderAttributeUtil.doERP(distribute, isrelive)) {
					logger.info("订单["+ orderSn +"]修改商品同步至ERP订单信息 start：>>>>>" + updateOrderInfo.toString());
					try {
						res = erpInterfaceProxy.UpdateProductByIdtDtl(updateOrderInfo);
					} catch (Exception e) {
						logger.error("订单["+ orderSn +"]修改商品同步至ERP订单信息异常：>>>>>" + e.getMessage(), e);
						saveOrderAction(distribute, orderStatus.getAdminUser(), "<font style=color:red;>编辑商品：错误信息 "+ res.toString() + "</font>");
						info.setMessage("订单["+ orderSn +"]修改商品同步至ERP订单信息异常：>>>>>" + e.getMessage());
						return info;
					}
					logger.info("订单["+ orderSn +"]修改商品同步至ERP订单信息结果：>>>>>" + res.toString());
					if (res.getCode() != Constant.OS_YES) {
						info.setIsOk(Constant.OS_NO);
						info.setMessage("订单[" + orderSn + "]编辑商品通知MB供应商异常：" + res.getMessage());
						// 记录异常日志
						saveOrderAction(distribute, orderStatus.getAdminUser(), "<font style=color:red;>编辑商品：错误信息 "+ res.toString() + "</font>");
						return info;
					}
				}
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单[" + orderSn + "]编辑商品成功");
		logger.debug("订单[" + orderSn + "]编辑商品成功");
		return info;
	}

	private List<OrderDetailsInfo> createMbIncrementGoods(UpdateOrderDistributeBean orderDistributeBean) {
		List<OrderDetailsInfo> detailsInfos = new ArrayList<OrderDetailsInfo>();
		Map<String, OrderDetailsInfo> updateGoodsMap = new HashMap<String, OrderDetailsInfo>(); 
		List<MasterOrderGoods> updateList = orderDistributeBean.getUpdateList();
		if (StringUtil.isListNotNull(updateList)) {
			for (MasterOrderGoods orderGoods : updateList) {
				String skuKey = (StringUtil.isTrimEmpty(orderGoods.getDepotCode()) ? "" : orderGoods.getDepotCode()) + orderGoods.getCustomCode();
				OrderDetailsInfo detailsInfo = updateGoodsMap.get(skuKey);
				if (detailsInfo != null) {
					updateGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, detailsInfo));
				} else {
					updateGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, null));
				}
			}
		}
		
		List<MasterOrderGoods> notUpdateList = orderDistributeBean.getNotUpdateList();
		if (StringUtil.isListNotNull(notUpdateList)) {
			for (MasterOrderGoods orderGoods : notUpdateList) {
				String skuKey = (StringUtil.isTrimEmpty(orderGoods.getDepotCode()) ? "" : orderGoods.getDepotCode()) + orderGoods.getCustomCode();
				OrderDetailsInfo detailsInfo = updateGoodsMap.get(skuKey);
				if (detailsInfo != null) {
					updateGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, detailsInfo));
				} else {
					updateGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, null));
				}
			}
		}
		
		List<MasterOrderGoods> deleteList = orderDistributeBean.getDeleteList();
		Map<String, OrderDetailsInfo> deleteGoodsMap = new HashMap<String, OrderDetailsInfo>();
		if (StringUtil.isListNotNull(deleteList)) {
			for (MasterOrderGoods orderGoods : deleteList) {
				String skuKey = (StringUtil.isTrimEmpty(orderGoods.getDepotCode()) ? "" : orderGoods.getDepotCode()) + orderGoods.getCustomCode();
				OrderDetailsInfo detailsInfo = deleteGoodsMap.get(skuKey);
				if (detailsInfo != null) {
					deleteGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, detailsInfo));
				} else {
					deleteGoodsMap.put(skuKey, createMbUpdateGoodsMap(orderGoods, null));
				}
			}
		}
		
		// 剩余商品单价
		for (String skuKey : updateGoodsMap.keySet()) {
			OrderDetailsInfo updateGoodsObj = updateGoodsMap.get(skuKey);
			double unitPrice = updateGoodsObj.getUnitPrice() / updateGoodsObj.getCount();
			updateGoodsObj.setUnitPrice(unitPrice);
			double aturePrice = updateGoodsObj.getAturePrice() / updateGoodsObj.getCount();
			updateGoodsObj.setAturePrice(aturePrice);
			double settlementPrice = updateGoodsObj.getSettlementPrice() / updateGoodsObj.getCount();
			updateGoodsObj.setSettlementPrice(settlementPrice);
			if (updateGoodsObj.getUnitPrice() > 0) {
				updateGoodsObj.setDiscRate(aturePrice / updateGoodsObj.getUnitPrice() * 100);
			} else {
				updateGoodsObj.setDiscRate(100D);
			}
			updateGoodsObj.setC2bgoodList(null);
			updateGoodsObj.setCount(0);
			
			OrderDetailsInfo deleteDetailsInfo = deleteGoodsMap.get(skuKey);
			if (deleteDetailsInfo != null) {
				updateGoodsObj.setCount(updateGoodsObj.getCount() - deleteDetailsInfo.getCount());
				deleteGoodsMap.remove(skuKey);
			}
			detailsInfos.add(updateGoodsObj);
		}
		for (String skuKey : deleteGoodsMap.keySet()) {
			OrderDetailsInfo deleteDetailsInfo = deleteGoodsMap.get(skuKey);
			double unitPrice = deleteDetailsInfo.getUnitPrice() / deleteDetailsInfo.getCount();
			deleteDetailsInfo.setUnitPrice(unitPrice);
			double aturePrice = deleteDetailsInfo.getAturePrice() / deleteDetailsInfo.getCount();
			deleteDetailsInfo.setAturePrice(aturePrice);
			double settlementPrice = deleteDetailsInfo.getSettlementPrice() / deleteDetailsInfo.getCount();
			deleteDetailsInfo.setSettlementPrice(settlementPrice);
			if (deleteDetailsInfo.getUnitPrice() > 0) {
				deleteDetailsInfo.setDiscRate(aturePrice / deleteDetailsInfo.getUnitPrice() * 100);
			} else {
				deleteDetailsInfo.setDiscRate(100D);
			}
			deleteDetailsInfo.setC2bgoodList(null);
			if (deleteDetailsInfo != null) {
				deleteDetailsInfo.setCount(-deleteDetailsInfo.getCount());
				detailsInfos.add(deleteDetailsInfo);
			}
		}
		return detailsInfos;
	}
	
	
	/**
	 * 同步ERP商品组装
	 * @param orderGoods
	 * @param detailsInfo
	 * @return
	 */
	private OrderDetailsInfo createMbUpdateGoodsMap(MasterOrderGoods orderGoods, OrderDetailsInfo detailsInfo) {
		// 判断是否是ERP商品
		OrderDetailsInfo mergerdetailsInfo = new OrderDetailsInfo();
		int goodsNumber = orderGoods.getGoodsNumber();
		if (detailsInfo == null) {
			mergerdetailsInfo.setProdNum(orderGoods.getCustomCode());
			mergerdetailsInfo.setCount(orderGoods.getGoodsNumber());
			mergerdetailsInfo.setUnitPrice(orderGoods.getGoodsPrice().doubleValue() * goodsNumber);
			mergerdetailsInfo.setAturePrice(orderGoods.getTransactionPrice().doubleValue() * goodsNumber);
			mergerdetailsInfo.setSettlementPrice(orderGoods.getSettlementPrice().doubleValue() * goodsNumber);
			if (orderGoods.getGoodsPrice().doubleValue() > 0) {
				mergerdetailsInfo.setDiscRate(orderGoods.getSettlementPrice().doubleValue() / orderGoods.getGoodsPrice().doubleValue() * 100);
			} else {
				mergerdetailsInfo.setDiscRate(100D);
			}
			mergerdetailsInfo.setAmount(orderGoods.getSettlementPrice().doubleValue() * goodsNumber);
			mergerdetailsInfo.setDwarhCode(orderGoods.getDepotCode());
		} else {
			mergerdetailsInfo.setProdNum(orderGoods.getCustomCode());
			int goodsCount = goodsNumber + detailsInfo.getCount();
			double aturePrice = (goodsNumber * orderGoods.getTransactionPrice().doubleValue())
					+ detailsInfo.getAturePrice();
			double settlementPrice = (goodsNumber * orderGoods.getSettlementPrice().doubleValue())
					+ detailsInfo.getSettlementPrice();
			double unitPrice = (goodsNumber * orderGoods.getGoodsPrice().doubleValue())
					+ detailsInfo.getUnitPrice();
			mergerdetailsInfo.setCount(goodsCount);
			mergerdetailsInfo.setUnitPrice(unitPrice);
			mergerdetailsInfo.setAturePrice(aturePrice);
			mergerdetailsInfo.setSettlementPrice(settlementPrice);
			mergerdetailsInfo.setAmount(aturePrice);
			mergerdetailsInfo.setDwarhCode(orderGoods.getDepotCode());
		}
		if (Constant.ORDER_TYPE_C2B.equals(orderGoods.getExtensionCode())) {
			C2bGood c2bGood = new C2bGood();
			if (StringUtil.isListNotNull(mergerdetailsInfo.getC2bgoodList())) {
				c2bGood.setB2cCount((int)goodsNumber);
				c2bGood.setB2cGoodsId(orderGoods.getExtensionId());
				c2bGood.setB2cGoodsSku(orderGoods.getCustomCode());
				mergerdetailsInfo.getC2bgoodList().add(c2bGood);
			} else {
				List<C2bGood> list = new ArrayList<C2bGood>();
				c2bGood.setB2cCount((int)goodsNumber);
				c2bGood.setB2cGoodsId(orderGoods.getExtensionId());
				c2bGood.setB2cGoodsSku(orderGoods.getCustomCode());
				mergerdetailsInfo.setC2bgoodList(list);
			}
		}
		return mergerdetailsInfo;
	}
	
	/**
	 * 组织通知ERP Bean
	 * 
	 * @param distribute
	 * @param compareResult
	 * @param depotFlag
	 * @return
	 */
	private UpdateOrderInfo buildMbUpdateOrderInfo(OrderDistribute distribute, OrderInfoMappingBean compareResult, Boolean depotFlag) {
		UpdateOrderInfo updateOrderGoods = new UpdateOrderInfo();
		updateOrderGoods.setOrderSn(distribute.getOrderSn());
		OrderMasterInfo master = updateOrderGoods.getMaster();
		// 处理Master信息
		master.setOrderSn(distribute.getOrderSn());
		master.setShipCode(null);// 非必输
		if (distribute.getSource().intValue() == 4 || distribute.getSource().intValue() == 5) {
			master.setType(1);// 订单类型C2b
		} else {
			master.setType(0);// 订单类型B2C
		}
		master.setOrderAmount(distribute.getTotalFee().doubleValue());// 订单总额
		master.setShippingFee(distribute.getShippingTotalFee().doubleValue()); // 运费
		if (distribute.getTotalFee().doubleValue() < 0.0001) {
			master.setDiscount(100d);// 这种情况不会出现
		} else {
			master.setDiscount((1 - NumberUtil.div(distribute.getDiscount().doubleValue(), distribute.getTotalFee().doubleValue(), 2)) * 100);// 优惠金额
		}
		master.setReceivables(distribute.getTotalFee().doubleValue() - distribute.getBonus().doubleValue());// 应收款
		master.setPaidFee(distribute.getMoneyPaid().doubleValue());// 已付款金额
		master.setFee(distribute.getTotalPayable().doubleValue()); // 待收款...
		master.setOtherFee(0D); // 保价费
		// 处理Details 信息
		updateOrderGoods.setDetail(compareResult.getDetail_ERP());
		logger.debug("比较结果:" + JSON.toJSONString(updateOrderGoods));
		return updateOrderGoods;
	}

	private ReturnInfo<String> updateOrderDistributeGoods(List<UpdateOrderDistributeBean> distributeBeans,
			byte reviveStt, String actionUser, MasterOrderInfo master) {
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		// 遍历分配单更新
		StringBuffer masterMsg = new StringBuffer();
		StringBuffer msg = new StringBuffer();
		Date lastUpdateTime = null;
		for (UpdateOrderDistributeBean distributeBean : distributeBeans) {
			String orderSn = "";
			try {
				// 拆单订单-新增商品
				OrderDistributeBean newDistribute = distributeBean.getDistribute();
				if (newDistribute == null) {
					// 新增主订单商品商品
					// 新增商品列表
					// 商品没有变化时，不需要更新
					if (StringUtil.isListNull(distributeBean.getAddList())
							&& StringUtil.isListNull(distributeBean.getDeleteList())
							&& StringUtil.isListNull(distributeBean.getUpdateList())) {
						continue;
					}
					if (StringUtil.isListNotNull(distributeBean.getDeleteList())) {
						masterMsg.append("删除商品：</br>");
						for (MasterOrderGoods deleteGoods : distributeBean.getDeleteList()) {
							masterOrderGoodsMapper.deleteByPrimaryKey(deleteGoods.getId());
							masterMsg.append("\t企业SKU码:" + deleteGoods.getCustomCode())
								.append(" 件数:"+ deleteGoods.getGoodsNumber())
								.append(" 商品价格:"+ deleteGoods.getGoodsPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
								.append(" 成交价格:"+ deleteGoods.getTransactionPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
								.append(" 财务价格:"+ deleteGoods.getSettlementPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
								.append(" 分摊金额:"+ deleteGoods.getShareBonus().setScale(4, BigDecimal.ROUND_HALF_UP))
								.append(" 促销信息:"+ deleteGoods.getPromotionDesc() + "</br>");
						}
					}
					// 修改商品列表
					if (StringUtil.isListNotNull(distributeBean.getUpdateList())) {
						masterMsg.append("更新商品：</br>");
						for (MasterOrderGoods modifyGoods : distributeBean.getUpdateList()) {
							MasterOrderGoods oldGoods = masterOrderGoodsMapper.selectByPrimaryKey(modifyGoods.getId());
							masterOrderGoodsMapper.updateByPrimaryKeySelective(modifyGoods);
							masterMsg.append("\t企业SKU码:" + oldGoods.getCustomCode())
								.append(" 件数:" + oldGoods.getGoodsNumber())
								.append(" <font color='red'>修改为</font>")
								.append( modifyGoods.getGoodsNumber())
								.append(" 价格:" + oldGoods.getSettlementPrice())
								.append(" <font color='red'>修改为</font>")
								.append( modifyGoods.getGoodsNumber())
								.append("</br>");
						}
					}
					if (StringUtil.isListNotNull(distributeBean.getAddList())) {
						masterMsg.append("新增商品：</br>");
						for (MasterOrderGoods addGoods : distributeBean.getAddList()) {
							masterOrderGoodsMapper.insertSelective(addGoods);
							masterMsg.append("\t企业SKU码:" + addGoods.getCustomCode() )
							.append(" 件数:"+ addGoods.getGoodsNumber())
							.append(" 价格:"+ addGoods.getSettlementPrice().setScale(4, BigDecimal.ROUND_HALF_UP)+ "</br>");
						}
					}
				} else {
					// 更新分配单商品
					orderSn = newDistribute.getOrderSn();
					// 商品没有变化时，不需要更新
					if (StringUtil.isListNull(distributeBean.getDeleteList())
							&& StringUtil.isListNull(distributeBean.getUpdateList())) {
						continue;
					}
					OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
					lastUpdateTime = distribute.getLastUpdateTime();
					if (StringUtil.isListNotNull(distributeBean.getDeleteList())) {
						msg.append("删除商品：</br>");
						for (MasterOrderGoods deleteGoods : distributeBean.getDeleteList()) {
							/*if (OrderAttributeUtil.doERP(distribute, reviveStt)) {
								deleteGoods.setIsDel(Constant.IS_DEL_DEL);
								masterOrderGoodsMapper.updateByPrimaryKeySelective(deleteGoods);
							} else {
								masterOrderGoodsMapper.deleteByPrimaryKey(deleteGoods.getId());
							}*/
							deleteGoods.setIsDel(Constant.IS_DEL_DEL);
							masterOrderGoodsMapper.updateByPrimaryKeySelective(deleteGoods);
							msg.append("\t企业SKU码:" + deleteGoods.getCustomCode())
							.append(" 件数:"+ deleteGoods.getGoodsNumber())
							.append(" 商品价格:"+ deleteGoods.getGoodsPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
							.append(" 成交价格:"+ deleteGoods.getTransactionPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
							.append(" 财务价格:"+ deleteGoods.getSettlementPrice().setScale(4, BigDecimal.ROUND_HALF_UP))
							.append(" 分摊金额:"+ deleteGoods.getShareBonus().setScale(4, BigDecimal.ROUND_HALF_UP))
							.append(" 促销信息:"+ deleteGoods.getPromotionDesc() + "</br>");
						OrderQuestionLackSkuNew lackSku = new OrderQuestionLackSkuNew();
						lackSku.setLackNum(0);
						lackSku.setDepotCode(deleteGoods.getDepotCode());
						lackSku.setCustomCode(deleteGoods.getCustomCode());
						lackSku.setExtensionCode(deleteGoods.getExtensionCode());
						lackSku.setExtensionId(deleteGoods.getExtensionId());
						updateLackSku(orderSn, lackSku, 1);
						}
					}
					// 修改商品列表
					if (StringUtil.isListNotNull(distributeBean.getUpdateList())) {
						msg.append("更新商品：</br>");
						for (MasterOrderGoods modifyGoods : distributeBean.getUpdateList()) {
							MasterOrderGoods oldGoods = masterOrderGoodsMapper.selectByPrimaryKey(modifyGoods.getId());
							masterOrderGoodsMapper.updateByPrimaryKeySelective(modifyGoods);
							msg.append("\t企业SKU码:" + oldGoods.getCustomCode())
								.append(" 件数:" + oldGoods.getGoodsNumber())
								.append(" <font color='red'>修改为</font>")
								.append( modifyGoods.getGoodsNumber())
								.append(" 价格:" + oldGoods.getSettlementPrice())
								.append(" <font color='red'>修改为</font>")
								.append( modifyGoods.getGoodsNumber())
								.append("</br>");
							OrderQuestionLackSkuNew lackSku = new OrderQuestionLackSkuNew();
							lackSku.setDepotCode(modifyGoods.getDepotCode());
							lackSku.setCustomCode(modifyGoods.getCustomCode());
							lackSku.setExtensionCode(modifyGoods.getExtensionCode());
							lackSku.setExtensionId(modifyGoods.getExtensionId());
							lackSku.setLackNum(1);
							updateLackSku(orderSn, lackSku, 0);
						}
					}
					if (!distributeBean.getDeleteDepots().isEmpty()) {
						for (String depotCode : distributeBean.getDeleteDepots()) {
							if (!distributeBean.getNotUpdateDepots().contains(depotCode)) {
								OrderDepotShipExample shipExample = new OrderDepotShipExample();
								shipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode);
								orderDepotShipMapper.deleteByExample(shipExample);
							}
						}
					}
					masterMsg.append(msg);
					logger.debug("更新分配单：distribute=" + JSON.toJSONString(newDistribute));
					double settlement = newDistribute.getGoodsAmount().doubleValue() - newDistribute.getDiscount().doubleValue();
					double afterChangePayable = settlement - distribute.getMoneyPaid().doubleValue()
							- distribute.getSurplus().doubleValue() - distribute.getIntegralMoney().doubleValue();
					newDistribute.setSettlementPrice(settlement);
					newDistribute.setAfterChangePayable(afterChangePayable);
					OrderDistribute updateDistribute = editOrderDistribute(distribute, msg, newDistribute);
					if (updateDistribute != null) {
						orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
					}
					// 分配单发货状态
					distributeShipService.judgeDistributeShipedStatus(orderSn);
					if (msg != null && StringUtil.isNotEmpty(msg.toString())) {
						distributeActionService.addOrderAction(orderSn, msg.toString(), actionUser);
					}
					logger.debug("订单[" + orderSn + "]分配单修改商品成功");
				}
			} catch (Exception e) {
				logger.error("订单[" + orderSn + "]OMS分配单修改商品异常" + e.getMessage() , e);
				info.setMessage("订单[" + orderSn + "]OMS分配单修改商品异常" + e.getMessage());
				return info;
			}
			msg = new StringBuffer();
			if (StringUtil.isListNotNull(distributeBean.getDeleteList())) {
				boolean doStock = lastUpdateTime == null ? true : false;
//				orderStockService.realeseBySku(distributeBean.getDeleteList(), master.getMasterOrderSn(), doStock);
			}
			if (master.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_UNPAYED) {
				if (StringUtil.isListNotNull(distributeBean.getAddList())) {
					channelStockService.preOccupyByGoodsList(master.getMasterOrderSn(), master.getOrderFrom(), distributeBean.getAddList());
				}
			} else {
				if (StringUtil.isListNotNull(distributeBean.getAddList())) {
//					orderStockService.occupyBySku(distributeBean.getAddList(), master.getMasterOrderSn());
				}
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("");
		info.setData(masterMsg.toString());
		return info;
	}
	
	public Date getDate(Date t, int sec) {
		long milliseconds = t.getTime();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds + sec * 1000);
		Date f = c.getTime();
		return f;
	}
	
	/**
	 * 编辑订单商品信息 根据子订单组装商品数据（增删改商品列表）
	 * 
	 * @param updateDistributeMap
	 * @param orderSnKey
	 * @param type 0：增加;1：删除;2：更新 3：没变化
	 * @param updateObj
	 */
	private void editOrderDistributeGoods(Map<String, UpdateOrderDistributeBean> updateDistributeMap,
			String orderSnKey, int type, MasterOrderGoods updateObj) {
		String orderSn = StringUtil.isTrimEmpty(updateObj.getOrderSn()) ? StringUtil.EMPTY : updateObj.getOrderSn();
		// 更新子订单信息
		UpdateOrderDistributeBean distributeBean = updateDistributeMap.get(orderSn);
		// 根据供应商分组
		if (distributeBean == null) {
			distributeBean = new UpdateOrderDistributeBean();
		}
		Double disGoodsAmount = 0D;											// 商品总金额
		Integer disGoodsNumber = 0;											// 商品总数量
		Double disTransaction = 0D;											// 商品总成交价
		Double disSettlement = 0D;											// 商品总财务价
		Double disDiscount = 0D;											// 商品总折扣
		Double disBonus = 0D;												// 订单红包金额
		Double disIntegralMoney = 0D;										// 订单使用积分金额
		OrderDistributeBean distribute = distributeBean.getDistribute();
		if (distribute != null) {
			disGoodsAmount = distribute.getGoodsAmount().doubleValue();
			disGoodsNumber = distribute.getGoodsCount();
			disDiscount = distribute.getDiscount().doubleValue();
			disBonus = distribute.getBonus().doubleValue();
			disIntegralMoney = distribute.getIntegralMoney().doubleValue();
		} else if (StringUtil.isNotEmpty(orderSn)) {
			OrderDistribute oldDistribute = this.orderDistributeMapper.selectByPrimaryKey(orderSn);
			distribute = new OrderDistributeBean();
			distribute.setSupplierCode(oldDistribute.getSupplierCode());
			distribute.setOrderSn(orderSn);
			distribute.setSource(oldDistribute.getSource());
			distribute.setOrderStatus(oldDistribute.getOrderStatus());
			distribute.setShippingTotalFee(oldDistribute.getShippingTotalFee());
			distribute.setMoneyPaid(oldDistribute.getMoneyPaid());
			distribute.setTotalPayable(oldDistribute.getTotalPayable());
			distribute.setPayStatus(oldDistribute.getPayStatus());
			distribute.setShipStatus(oldDistribute.getShipStatus());
			distribute.setQuestionStatus(oldDistribute.getQuestionStatus());
			distribute.setLastUpdateTime(oldDistribute.getLastUpdateTime());
			distribute.setOrderType(oldDistribute.getOrderType());
			distribute.setSource(oldDistribute.getSource());
			distribute.setReferer(oldDistribute.getReferer());
		}
		switch (type) {
		case 0:
			distributeBean.getAddList().add(updateObj);
			break;
		case 1:
			distributeBean.getDeleteList().add(updateObj);
			distributeBean.getDeleteDepots().add(updateObj.getDepotCode());
			break;
		case 2:
			distributeBean.getUpdateList().add(updateObj);
			distributeBean.getNotUpdateDepots().add(updateObj.getDepotCode());
			break;
		case 3:
			distributeBean.getNotUpdateList().add(updateObj);
			distributeBean.getNotUpdateDepots().add(updateObj.getDepotCode());
			break;
		default:
			distributeBean.getNotUpdateList().add(updateObj);
			distributeBean.getNotUpdateDepots().add(updateObj.getDepotCode());
			break;
		}
		// 删除操作不统计
		if (type != 1 && distribute != null) {
			disTransaction += (updateObj.getTransactionPrice().doubleValue() * updateObj.getGoodsNumber());
			disSettlement += (updateObj.getSettlementPrice().doubleValue() * updateObj.getGoodsNumber());
			if (updateObj.getIntegralMoney() != null) {
				disIntegralMoney += (updateObj.getIntegralMoney().doubleValue() * updateObj.getGoodsNumber());
			}
			// 商品总金额
			disGoodsAmount += updateObj.getGoodsPrice().doubleValue() * updateObj.getGoodsNumber();
			// 商品总折扣
			disDiscount += (updateObj.getGoodsPrice().doubleValue() - updateObj.getTransactionPrice().doubleValue());
			disGoodsNumber += updateObj.getGoodsNumber().intValue();
			// 红包
			disBonus += updateObj.getShareBonus().doubleValue() * updateObj.getGoodsNumber();
			Double disTotalFee = 0D;										// 订单总金额
			disTotalFee = disGoodsAmount - disDiscount;
			if (type != 0) {
				distribute.setGoodsAmount(new BigDecimal(disGoodsAmount));
				distribute.setDiscount(new BigDecimal(disDiscount));
				distribute.setTotalFee(new BigDecimal(disTotalFee));
				distribute.setBonus(new BigDecimal(disBonus));
				distribute.setIntegralMoney(new BigDecimal(disIntegralMoney));
				distribute.setGoodsCount(disGoodsNumber);
			}
		}
		distributeBean.setDistribute(distribute);
		updateDistributeMap.put(orderSn, distributeBean);
	}
	
	/**
	 * 更新问题单缺货商品
	 * 
	 * @param orderSn
	 * @param lackSku
	 * @param type 0 :更新1：删除
	 */
	private void updateLackSku(String orderSn, OrderQuestionLackSkuNew lackSku, int type) {
		if (lackSku == null) {
			return ;
		}
		try {
			OrderQuestionLackSkuNew newLackSku = new OrderQuestionLackSkuNew();
			int num = lackSku.getLackNum();
			if (lackSku.getLackNum() == null) {
				num = 0;
			}
			newLackSku.setLackNum(num);
			OrderQuestionLackSkuNewExample example = new OrderQuestionLackSkuNewExample();
			example.or().andOrderSnEqualTo(orderSn)
				.andDepotCodeEqualTo(lackSku.getDepotCode())
				.andCustomCodeEqualTo(lackSku.getCustomCode())
				.andExtensionCodeEqualTo(lackSku.getExtensionCode())
				.andExtensionIdEqualTo(lackSku.getExtensionId());
			if (type == 0) {
				orderQuestionLackSkuNewMapper.updateByExample(newLackSku, example);
			} else {
				List<OrderQuestionLackSkuNew> lackSkuNews = this.orderQuestionLackSkuNewMapper.selectByExample(example);
				if (StringUtil.isListNotNull(lackSkuNews)) {
					for (OrderQuestionLackSkuNew deleteSkuNew : lackSkuNews) {
						OrderQuestionLackSkuNewDel lackSkuDel = new OrderQuestionLackSkuNewDel();
						try {
							BeanUtils.copyProperties(lackSkuDel, deleteSkuNew);
						} catch (Exception e) {
							logger.error("复制OrderQuestionLackSkuNewDel信息异常", e);
						}
						orderQuestionLackSkuNewDelMapper.insertSelective(lackSkuDel);
					}
					orderQuestionLackSkuNewMapper.deleteByExample(example);
				}
			}
		} catch (Exception e) {
			logger.error("订单[" + orderSn + "]更新缺货商品缺货商品" + e.getMessage(), e);
		}
	}

	/**
	 * 编辑订单商品信息
	 * @param bean
	 * @return
	 */
	private MasterOrderGoods editOrderGoodsUpdateGoods(OrderGoodsUpdateBean bean) {
		if (null == bean) {
			return null;
		}
		MasterOrderGoods goods = new MasterOrderGoods();
		goods.setId(bean.getId());
		goods.setMasterOrderSn(bean.getMasterOrderSn());
		goods.setDepotCode(bean.getDepotCode());
		goods.setOrderSn(bean.getOrderSn());
		goods.setGoodsName(bean.getGoodsName());
		goods.setCustomCode(bean.getCustomCode());
		goods.setGoodsNumber(bean.getGoodsNumber());
		goods.setSettlementPrice(new BigDecimal(bean.getDoubleSettlementPrice()));
		goods.setGoodsPrice(new BigDecimal(bean.getDoubleGoodsPrice()));
		goods.setDiscount(BigDecimal.valueOf(bean.getDiscount()));
		goods.setTransactionPrice(new BigDecimal(bean.getDoubleTransactionPrice()));
		goods.setGroupName(bean.getGroupName());
		goods.setGoodsSizeName(bean.getCurrSizeName());
		goods.setGoodsColorName(bean.getCurrColorName());
		goods.setGoodsThumb(bean.getGoodsThumb());
//		goods.setSendNumber(bean.getSendNumber());
		goods.setExtensionCode(bean.getExtensionCode());
		goods.setExtensionId(bean.getExtensionId());
		goods.setPromotionDesc(bean.getPromotionDesc());
		goods.setUseCard(bean.getUseCard());
		goods.setShareBonus(new BigDecimal(bean.getDoubleShareBonus()));
		goods.setShareSurplus(new BigDecimal(0.0));
		goods.setMadeFlag((byte)0);
		goods.setIntegralMoney(BigDecimal.valueOf(bean.getIntegralMoney()));
		goods.setSupplierCode(bean.getSupplierCode()); // 供应商编码
//		goods.setSalesMode(bean.gets); // 销售模式
		if (StringUtil.isNotEmpty(bean.getC2mItem())) {
			goods.setC2mItem(bean.getC2mItem());
		}
		return goods;
	}
	
	/**
	 * 编辑订单商品信息
	 * @param oldGoods
	 * @return
	 */
	private MasterOrderGoods editOrderGoodsDeleteGoods(MasterOrderGoods oldGoods, int goodsNumber) {
		if (null == oldGoods) {
			return null;
		}
		MasterOrderGoods targetgoods = new MasterOrderGoods();
		// 更新前，将orderInfo内容复制给txOInfo，用于异常时的日志记录
		try {
			PropertyUtils.copyProperties(targetgoods, oldGoods);
		} catch (Exception e) {
			logger.error("复制targetgoods信息异常"+ e.getMessage());
		}
		targetgoods.setGoodsNumber(Math.abs(goodsNumber));
		return targetgoods;
	}
	
	private List<String> getOrderSnsByUpdateObj(List<UpdateOrderDistributeBean> distributeBeans) {
		List<String> orderSns = new ArrayList<String>();
		for (UpdateOrderDistributeBean distribute : distributeBeans) {
			orderSns.add(distribute.getDistribute().getOrderSn());
		}
		return orderSns;
	}

	/**
	 * 主订单编辑订单地址
	 *
	 * @param masterOrderSn 主订单号
	 * @param consignInfo 客户信息
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editConsigneeInfoByMasterSn(String masterOrderSn, ConsigneeModifyInfo consignInfo) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		if (consignInfo == null) {
			logger.error("[consignInfo]不能都为空！");
			info.setMessage("[consignInfo]不能都为空！");
			return info;
		}
		if (StringUtil.isTrimEmpty(consignInfo.getActionUser())) {
			logger.error("[actionUser]传入参数为空，不能编辑收货人信息操作！");
			info.setMessage("[actionUser]传入参数为空，不能编辑收货人信息操作！");
			return info;
		}
		logger.info("订单编辑收货人：masterOrderSn=" + masterOrderSn + ";consignInfo=" + consignInfo);
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (master == null) {
			logger.error("订单[" + masterOrderSn + "]不存在，不能编辑收货人信息操作！");
			info.setMessage("订单[" + masterOrderSn + "]不存在，不能编辑收货人信息操作！");
			return info;
		}
		if (master.getShipStatus() > Constant.OI_SHIP_STATUS_UNSHIPPED) {
			logger.error("订单[" + masterOrderSn + "]分配单已经发货，不能编辑收货人信息操作！");
			info.setMessage("订单[" + masterOrderSn + "]分配单已经发货，不能编辑收货人信息操作！");
			return info;
		}
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
			OrderDistributeExample mbDistributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria mbCriteria = mbDistributeExample.or();
			mbCriteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			mbCriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			mbCriteria.andSupplierCodeEqualTo(Constant.SUPPLIER_TYPE_MB);
			mbDistributes = this.orderDistributeMapper.selectByExample(mbDistributeExample);
		}
		info = editConsigneeInfo(master, mbDistributes, thDistributes, consignInfo);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editConsigneeInfoByOrderSn(ConsigneeModifyInfo consignInfo) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (consignInfo == null) {
			logger.error("[consignInfo]不能都为空！");
			info.setMessage("[consignInfo]不能都为空！");
			return info;
		}
		String orderSn = consignInfo.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		if (StringUtil.isTrimEmpty(consignInfo.getActionUser())) {
			logger.error("[actionUser]传入参数为空，不能编辑收货人信息操作！");
			info.setMessage("[actionUser]传入参数为空，不能编辑收货人信息操作！");
			return info;
		}
		logger.info("订单编辑收货人：orderSn=" + orderSn + ";consignInfo=" + consignInfo);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]不存在，不能编辑收货人信息操作！");
			info.setMessage("订单[" + orderSn + "]不存在，不能编辑收货人信息操作！");
			return info;
		}
		if (distribute.getShipStatus() > Constant.OI_SHIP_STATUS_UNSHIPPED) {
			logger.error("订单[" + orderSn + "]发货单已经发货，不能编辑收货人信息操作！");
			info.setMessage("订单[" + orderSn + "]发货单已经发货，不能编辑收货人信息操作！");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单取消操作！");
			info.setMessage("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单取消操作！");
			return info;
		}
		if (master.getShipStatus() > Constant.OI_SHIP_STATUS_UNSHIPPED) {
			logger.error("订单[" + distribute.getMasterOrderSn() + "]部分发货单已经发货，不能编辑收货人信息操作！");
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]部分发货单已经发货，不能编辑收货人信息操作！");
			return info;
		}
		try {
			consignInfo.setConsignee(URLDecoder.decode(consignInfo.getConsignee(), "UTF-8"));
			consignInfo.setAddress(URLDecoder.decode(consignInfo.getAddress(), "UTF-8"));
		} catch (Exception e) {
			logger.error(orderSn + "编辑收货人异常" + e.getMessage(), e);
		}
		/*Map<String,String> systemRegionMap = new HashMap<String, String>();
		Set<String> areaIdList = new HashSet<String>();
		areaIdList.add(consignInfo.getCountry());
		areaIdList.add(consignInfo.getProvince());
		areaIdList.add(consignInfo.getCity());
		areaIdList.add(consignInfo.getDistrict());
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		systemRegionAreaExample.or().andRegionNameIn(new ArrayList<String>(areaIdList));
		List<SystemRegionArea> systemRegionAreaList = systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		if(CollectionUtils.isEmpty(systemRegionAreaList)){
			info.setMessage("订单[" + orderSn + "]收货地址信息OMS地区信息表中不存在");
			return info;
		}
		for (SystemRegionArea systemRegionArea : systemRegionAreaList) {
			systemRegionMap.put(systemRegionArea.getRegionName(), systemRegionArea.getRegionId());
		}
		consignInfo.setCountry(systemRegionMap.get(consignInfo.getCountry()));
		consignInfo.setProvince(systemRegionMap.get(consignInfo.getProvince()));
		consignInfo.setCity(systemRegionMap.get(consignInfo.getCity()));
		consignInfo.setDistrict(systemRegionMap.get(consignInfo.getDistrict()));*/
		List<OrderDistribute> mbDistributes = null;
		List<OrderDistribute> thDistributes = null;
		if (Constant.SUPPLIER_TYPE_MB.equals(distribute.getSupplierCode())) {
			mbDistributes = new ArrayList<OrderDistribute>();
			mbDistributes.add(distribute);
		} else {
			thDistributes = new ArrayList<OrderDistribute>();
			thDistributes.add(distribute);
		}
		info = editConsigneeInfo(master, mbDistributes, thDistributes, consignInfo);
		return info;
	}

	/**
	 * 编辑订单收货人信息
	 * @param master 订单信息
	 * @param mbDistributes 主分配单
	 * @param thDistributes 其他分配单
	 * @param consignInfo 客户信息
	 * @return
	 */
	private ReturnInfo editConsigneeInfo(MasterOrderInfo master, List<OrderDistribute> mbDistributes, List<OrderDistribute> thDistributes,
			ConsigneeModifyInfo consignInfo) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null && mbDistributes == null && thDistributes == null) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		// 订单编码
		String masterOrderSn = master.getMasterOrderSn();
		ri.setMessage("收货信息更新失败");
		// 操作人
		String actionUser = consignInfo.getActionUser();
		try {
			logger.info("订单编辑收货人信息：masterOrderSn=" + masterOrderSn + ";actionUser=" + actionUser + ";consignInfo=" + consignInfo);
			// 订单原收货人信息
			MasterOrderAddressInfo orderAddressInfo = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (orderAddressInfo == null){
				ri.setMessage("订单[" + masterOrderSn + "]收货信息不存在");
				return ri;
			}
			StringBuffer actionNote = new StringBuffer();
			MasterOrderAddressInfo updateOrderAddressInfo = editAddressInfo(masterOrderSn, consignInfo, orderAddressInfo, actionNote);
			if (updateOrderAddressInfo == null) {
				ri.setMessage(actionNote.toString());
				return ri;
			}
			MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (extend == null) {
				ri.setMessage("订单[" + masterOrderSn + "]扩展表数据不存在");
				return ri;
			}
			// 有子订单时：子订单未确认
			if (StringUtil.isListNotNull(mbDistributes)) {
				info = editConsigneeInfoByMbDistribute(getOrderSns(mbDistributes), mbDistributes, consignInfo,
						actionUser, actionNote.toString(), extend.getReviveStt());
				// 处理失败
				if (info.getIsOk() == Constant.OS_NO) {
					return info;
				}
			}
			updateOrderAddressInfo.setMasterOrderSn(masterOrderSn);
			updateOrderAddressInfo.setUpdateTime(new Date());
			masterOrderAddressInfoMapper.updateByPrimaryKeySelective(updateOrderAddressInfo);
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, actionNote.toString(), actionUser);
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("收货信息更新成功！");

            //更新关联采购单地址信息
            updatePurchaseOrderAddress(consignInfo);

		} catch (Exception ex) {
			logger.error("订单[" + masterOrderSn + "]编辑收货人信息异常：" + ex.getMessage(), ex);
			ri.setMessage("编辑收货人信息失败,"+ex.getMessage());
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "收货信息更新失败," + ex.getMessage(), actionUser);
		}
		return ri;
	}
	
	/**
	 * 编辑收货人信息
	 * 
	 * @param masterOrderSn
	 * @param consignInfo
	 * @param orderAddressInfo
	 * @param actionNote
	 * @return
	 */
	@Override
	public MasterOrderAddressInfo editAddressInfo(String masterOrderSn, ConsigneeModifyInfo consignInfo, MasterOrderAddressInfo orderAddressInfo,
			StringBuffer actionNote) {
		Map<String,String> systemRegionMap = new HashMap<String, String>();
		Set<String> areaIdList = new HashSet<String>();
		areaIdList.add(orderAddressInfo.getCountry());
		areaIdList.add(orderAddressInfo.getProvince());
		areaIdList.add(orderAddressInfo.getCity());
		areaIdList.add(orderAddressInfo.getDistrict());
		areaIdList.add(orderAddressInfo.getStreet());
		areaIdList.add(consignInfo.getCountry());
		areaIdList.add(consignInfo.getProvince());
		areaIdList.add(consignInfo.getCity());
		areaIdList.add(consignInfo.getDistrict());
		areaIdList.add(consignInfo.getStreet());
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		systemRegionAreaExample.or().andRegionIdIn(new ArrayList<String>(areaIdList));
		List<SystemRegionArea> systemRegionAreaList = systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		if(CollectionUtils.isEmpty(systemRegionAreaList)){
			actionNote.append("订单[" + masterOrderSn + "]收货地址信息OMS地区信息表中不存在");
			return null;
		}
		for (SystemRegionArea systemRegionArea : systemRegionAreaList) {
			systemRegionMap.put(systemRegionArea.getRegionId(), systemRegionArea.getRegionName());
		}
		//联系电话
		String encMoile = consignInfo.getMobile();
		String encTel = consignInfo.getTel();
		MasterOrderAddressInfo updateOrderAddressInfo = new MasterOrderAddressInfo();
		if (!StringUtils.equalsIgnoreCase(consignInfo.getConsignee(), orderAddressInfo.getConsignee()) && StringUtils.isNotBlank(consignInfo.getConsignee())) {
			actionNote.append("收件人 "+orderAddressInfo.getConsignee()+" → " + consignInfo.getConsignee() + ";<br />");
			updateOrderAddressInfo.setConsignee(consignInfo.getConsignee());
		}
		if (!StringUtils.equalsIgnoreCase(consignInfo.getEmail(), orderAddressInfo.getEmail())) {
			actionNote.append("邮箱由 "+orderAddressInfo.getEmail()+" → " + consignInfo.getEmail() + ";<br />");
			updateOrderAddressInfo.setEmail(consignInfo.getEmail());
		}
		if (!StringUtils.equalsIgnoreCase(consignInfo.getAddress(), orderAddressInfo.getAddress())) {
			actionNote.append("详细地址由 "+orderAddressInfo.getAddress()+" → " + consignInfo.getAddress() + ";<br />");
			updateOrderAddressInfo.setAddress(consignInfo.getAddress());
		}
		if (!StringUtils.equalsIgnoreCase(consignInfo.getZipcode(), orderAddressInfo.getZipcode())) {
			actionNote.append("邮编由 "+orderAddressInfo.getZipcode()+" → " + consignInfo.getZipcode() + ";<br />");
			updateOrderAddressInfo.setZipcode(consignInfo.getZipcode());
		}
		if (!StringUtils.equalsIgnoreCase(encTel, orderAddressInfo.getTel())) {
			actionNote.append("联系电话已变更;<br />");
			updateOrderAddressInfo.setTel(encTel);
		}
		if (!StringUtils.equalsIgnoreCase(encMoile, orderAddressInfo.getMobile())) {
			actionNote.append("移动手机已变更;<br />");
			updateOrderAddressInfo.setMobile(encMoile);
		}
//		if (!StringUtils.equalsIgnoreCase(consignInfo.getSignBuilding(), orderAddressInfo.getSignBuilding())) {
//			actionNote.append("标志性建筑由 "+orderAddressInfo.getSignBuilding()+" → " + consignInfo.getSignBuilding() + ";<br />");
//			updateOrderAddressInfo.setSignBuilding(consignInfo.getSignBuilding());
//		}
//		if (!StringUtils.equalsIgnoreCase(consignInfo.getBestTime(), orderAddressInfo.getBestTime())) {
//			actionNote.append("最佳送货时间由 "+orderAddressInfo.getBestTime()+" → " + consignInfo.getBestTime() + ";<br />");
//			updateOrderAddressInfo.setBestTime(consignInfo.getBestTime());
//		}
//		if (!StringUtils.equalsIgnoreCase(consignInfo.getCountry(), orderAddressInfo.getCountry())) {
//			actionNote.append("国家由 "+systemRegionMap.get(orderAddressInfo.getCountry())+" → " + systemRegionMap.get(consignInfo.getCountry()) + ";<br />");
//			updateOrderAddressInfo.setCountry(consignInfo.getCountry());
//		}
		consignInfo.setCountry(systemRegionMap.get(consignInfo.getCountry()));
		if (!StringUtils.equalsIgnoreCase(consignInfo.getProvince(), orderAddressInfo.getProvince())) {
			actionNote.append("省份由 "+systemRegionMap.get(orderAddressInfo.getProvince())+" → " + systemRegionMap.get(consignInfo.getProvince()) + ";<br />");
			updateOrderAddressInfo.setProvince(consignInfo.getProvince());
		}
		consignInfo.setProvince(systemRegionMap.get(consignInfo.getProvince()));
		if (!StringUtils.equalsIgnoreCase(consignInfo.getCity(), orderAddressInfo.getCity())) {
			actionNote.append("城市由 "+systemRegionMap.get(orderAddressInfo.getCity())+" → " + systemRegionMap.get(consignInfo.getCity()) + ";<br />");
			updateOrderAddressInfo.setCity(consignInfo.getCity());
		}
		consignInfo.setCity(systemRegionMap.get(consignInfo.getCity()));
		if (!StringUtils.equalsIgnoreCase(consignInfo.getDistrict(), orderAddressInfo.getDistrict())) {
			actionNote.append("区县由 "+systemRegionMap.get(orderAddressInfo.getDistrict())+" → " + systemRegionMap.get(consignInfo.getDistrict()) + ";<br />");
			updateOrderAddressInfo.setDistrict(consignInfo.getDistrict());
		}
		consignInfo.setDistrict(systemRegionMap.get(consignInfo.getDistrict()));
//		if (!StringUtils.equalsIgnoreCase(consignInfo.getStreet(), orderAddressInfo.getStreet())) {
//			actionNote.append("街道由 "+systemRegionMap.get(orderAddressInfo.getStreet())+" → " + systemRegionMap.get(consignInfo.getStreet()) + ";<br />");
//			updateOrderAddressInfo.setStreet(consignInfo.getStreet());
//		}
//		if (StringUtils.isEmpty(consignInfo.getStreet())) {
//			String street = systemRegionMap.get(consignInfo.getStreet());
//			consignInfo.setAddress((street == null ? "" : street) + consignInfo.getAddress());
//		}
		return updateOrderAddressInfo;
	}

    /**
     * 主订单编辑发票地址信息
     * @param consignInfo
     * @return
     */
    @Override
    public ReturnInfo<String> editInvAddressInfoByMasterSn(ConsigneeModifyInfo consignInfo) {
        ReturnInfo<String> info = new ReturnInfo<>();
        info.setMessage("主订单编辑发票地址信息失败");
        if (consignInfo == null) {
            info.setMessage("[consignInfo]不能都为空！");
            return info;
        }
        String masterOrderSn = consignInfo.getOrderSn();
        if (StringUtil.isTrimEmpty(masterOrderSn)) {
            info.setMessage("订单号不能都为空！");
            return info;
        }
        String actionUser = consignInfo.getActionUser();
        if (StringUtil.isTrimEmpty(actionUser)) {
            info.setMessage("操作人为空！");
            return info;
        }

        try {
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (master == null) {
                info.setMessage("订单[" + masterOrderSn + "]不存在，不能编辑发票地址信息操作！");
                return info;
            }
            if (master.getShipStatus() > Constant.OI_SHIP_STATUS_UNSHIPPED) {
                info.setMessage("订单[" + masterOrderSn + "]分配单已经发货，不能编辑发票地址信息操作！");
                return info;
            }

            //校验收货信息
            MasterOrderAddressInfo addressInfo = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (addressInfo == null) {
                info.setMessage("订单[" + masterOrderSn + "]收货信息为空！");
                return info;
            }

            MasterOrderAddressInfo updateAddress = new MasterOrderAddressInfo();
            StringBuffer sb = new StringBuffer();
            //发票收货人
            String invConsignee = consignInfo.getInvConsignee();
            if (StringUtils.isNotBlank(invConsignee) && !invConsignee.equalsIgnoreCase(addressInfo.getInvConsignee())) {
                updateAddress.setInvConsignee(invConsignee);
                sb.append("发票收货人由‘" + addressInfo.getInvConsignee() + "’→‘" + invConsignee + "’；<br />");
            }

            //发票收货人手机号
            String invMobile = consignInfo.getInvMobile();
            if (StringUtils.isNotBlank(invMobile) && !invMobile.equalsIgnoreCase(addressInfo.getInvMobile())) {
                updateAddress.setInvMobile(invMobile);
                sb.append("发票收货人手机号由‘" + addressInfo.getInvMobile() + "’→‘" + invMobile + "’；<br />");
            }

            //发票收货人地址
            String invAddress = consignInfo.getInvAddress();
            if (StringUtils.isNotBlank(invAddress) && !invAddress.equalsIgnoreCase(addressInfo.getInvAddress())) {
                updateAddress.setInvAddress(invAddress);
                sb.append("发票收货人地址由‘" + addressInfo.getInvAddress() + "’→‘" + invAddress + "’；<br />");
            }

            //无修改信息返回修改成功
            String msg = sb.toString();
            if (StringUtils.isBlank(msg)) {
                info.setIsOk(1);
                info.setMessage("更新成功");
                return info;
            }
            updateAddress.setMasterOrderSn(masterOrderSn);
            int update = masterOrderAddressInfoMapper.updateByPrimaryKeySelective(updateAddress);
            if (update > 0) {
                info.setIsOk(1);
                info.setMessage("更新成功");
            }

            //添加日志
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, "编辑发票地址信息：<br />" + msg, actionUser);
        } catch (Exception e) {
            logger.error("主订单编辑发票地址信息异常" + JSONObject.toJSONString(consignInfo), e);
        }
        return info;
    }
	
	private ReturnInfo judgeMasterOrderStatus(String masterOrderSn, MasterOrderInfo master, byte orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (master == null) {
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
	
	@SuppressWarnings("rawtypes")
	private ReturnInfo editConsigneeInfoByMbDistribute(List<String> orderSns, List<OrderDistribute> distributes,
			ConsigneeModifyInfo consignInfo, String actionUser, String actionNote, byte isrelive){
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法编辑收货人");
			info.setMessage("[" + orderSns + "]订单不存在，无法编辑收货人");
			return info;
		}
		logger.debug("订单编辑收货人 orderSns=" + orderSns + ";actionUser=" + actionUser);
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			if (!ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(consignInfo.getSource())
					&& !ConstantValues.METHOD_SOURCE_TYPE.POS.equals(consignInfo.getSource())) {
				/* 执行前提检查 */
				if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
					info.setMessage(" 订单" + orderSn + "要处于未确定状态");
					return info;
				}
			}
			if (distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
					|| distribute.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				info.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
				return info;
			}
		}
		logger.info("订单编辑收货人 orderSns=" + orderSns + ";actionUser=" + actionUser);
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			try {
				// 通知未发货的分配单
				if (OrderAttributeUtil.doERP(distribute, isrelive)
						&& !ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(consignInfo.getSource())) {
					logger.info("订单[" + orderSns + "]编辑收货人 consignInfo=" + consignInfo);
					ErpResultBean res = ErpResultBean.getNewInstance();
					res = erpInterfaceProxy.changeConsigneeInfo(distribute.getOrderSn(), consignInfo);// 失败处理
					logger.info("订单[" + orderSns + "]编辑收货人  res:"+JSON.toJSONString(res));
					if (res.getCode() != 1) {
						info.setMessage("回调ERP失败："+res.getMessage());
						return info;
					}
				}
				try {
					//订单操作记录
					DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
					orderAction.setActionUser(actionUser);
					orderAction.setActionNote(actionNote);
					distributeActionService.saveOrderAction(orderAction);
					logger.debug("订单[" + orderSn + "]编辑收货人成功!");
				} catch (Exception e) {
					logger.error("订单[" + orderSn + "]编辑收货人失败，错误信息：", e);
					DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
					orderAction.setActionUser(actionUser);
					orderAction.setActionNote("<font style=color:red;>编辑收货人：错误信息 "+ e.getMessage() + "</font>");
					distributeActionService.saveOrderAction(orderAction);
					info.setMessage("订单[" + orderSns + "]编辑收货人失败，错误信息：" + e.getMessage());
					return info;
				}
			} catch (Exception e) {
				logger.error("订单[" + orderSn + "]通知供应商编辑收货人异常：" + e.getMessage(), e);
				info.setMessage("订单[" + orderSn + "]通知供应商编辑收货人异常：" + e.getMessage());
				//订单操作记录
				DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
				orderAction.setActionUser(actionUser);
				orderAction.setActionNote("<font style=color:red;>编辑收货人：通知供应商编辑收货人异常 "+ e.getMessage() + "</font>");
				distributeActionService.saveOrderAction(orderAction);
				return info;
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单["+orderSns+"]编辑收货人成功");
		logger.debug("订单["+orderSns+"]编辑收货人成功");
		return info;
	}

	private List<String> getOrderSns(List<OrderDistribute> distributes) {
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		return orderSns;
	}
	
	private MasterOrderInfoBean builtMasterBean(MasterOrderInfo master, double shippingFee, BigDecimal newTotalPayable) {
		MasterOrderInfoBean newMaster = new MasterOrderInfoBean();
		double shipDiff = shippingFee - master.getShippingTotalFee().doubleValue();
		newMaster.setTotalPayable(newTotalPayable);
		newMaster.setAfterChangePayable(newTotalPayable);
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
	
	private void saveOrderAction(OrderDistribute distribute, String actionUser, String msg) {
		DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
		orderAction.setActionUser(actionUser);
		orderAction.setActionNote(msg);
		distributeActionService.saveOrderAction(orderAction);
	}

    /**
     * 更新关联采购单地址信息
     * @param consignInfo
     */
    private void updatePurchaseOrderAddress(ConsigneeModifyInfo consignInfo) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        //主订单号
        purchaseOrder.setMasterOrderSn(consignInfo.getOrderSn());
        //国家
        purchaseOrder.setCountry(consignInfo.getCountry());
        //省
        purchaseOrder.setProvince(consignInfo.getProvince());
        //市
        purchaseOrder.setCity(consignInfo.getCity());
        //区
        purchaseOrder.setDistrict(consignInfo.getDistrict());
        //地址
        purchaseOrder.setAddress(consignInfo.getAddress());
        //邮编
        purchaseOrder.setZipcode(consignInfo.getZipcode());
        //手机号
        purchaseOrder.setMobile(consignInfo.getMobile());
        //电话
        purchaseOrder.setTel(consignInfo.getTel());
        //收货人
        purchaseOrder.setConsignee(consignInfo.getConsignee());

        purchaseOrderService.updateBatchByMasterOrderSn(purchaseOrder);
    }

    /**
     * 获取地区信息
     * @param regionIds
     * @return
     */
    private Map<String, SystemRegionArea> getAreaInfoMap(List<String> regionIds) {
        if (regionIds == null || regionIds.size() == 0) {
            return null;
        }

        SystemRegionAreaExample example = new SystemRegionAreaExample();
        example.or().andRegionIdIn(regionIds);
        List<SystemRegionArea> systemRegionAreas =  systemRegionAreaMapper.selectByExample(example);
        if (StringUtil.isListNull(systemRegionAreas)) {
            return null;
        }

        Map<String, SystemRegionArea> map = new HashMap<>(16);
        for (SystemRegionArea area : systemRegionAreas) {
            map.put(area.getRegionId(), area);
        }

        return map;
    }
}
