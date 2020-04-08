package com.work.shop.oms.order.service.impl;

import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.NumberUtil;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderGoodsService;
import com.work.shop.oms.order.service.OrderPriceService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单商品信息服务
 * @author QuYachu
 */
@Service
public class MasterOrderGoodsServiceImpl implements MasterOrderGoodsService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private OrderPriceService orderPriceService;

	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;

	/**
	 * 新增订单商品信息
	 * @param masterOrderSn 订单号
	 * @param masterOrder masterOrder 订单参数信息
	 * @param masterOrderInfo masterOrderInfo 订单信息
	 */
	@Override
	public ReturnInfo<List<MasterOrderGoods>> insertMasterOrderGoods(String masterOrderSn, MasterOrder masterOrder, MasterOrderInfo masterOrderInfo) {
		logger.info("订单[" + masterOrderSn + "]保存订单商品信息");
		ReturnInfo<List<MasterOrderGoods>> info = new ReturnInfo<List<MasterOrderGoods>>(Constant.OS_NO);
		List<MasterGoods> list = masterOrder.getShipList().get(0).getGoodsList();
		List<MasterOrderGoods> orderGoodsList = new ArrayList<MasterOrderGoods>();
		if (StringUtil.isListNull(list)) {
			info.setMessage("订单商品数据为空");
			return info;
		}
		for (MasterGoods masterGoods : list) {
			try {
				orderGoodsList.add(insertOrderGoods(masterGoods, masterOrderSn, masterOrderInfo.getOrderFrom(), masterOrderInfo.getSource()));
			} catch (Exception e) {
				logger.error(masterOrderSn + "创建订单商品信息异常" + e.getMessage(), e);
			}
		}
		// 处理订单商品价格
		orderPriceService.financialPrice(masterOrderInfo, orderGoodsList);
		// 订单商品总财务价
		double settlementPrice = 0D;
		for (MasterOrderGoods orderGoods : orderGoodsList) {
			settlementPrice += (orderGoods.getSettlementPrice().doubleValue() + orderGoods.getTax().doubleValue())
				* orderGoods.getGoodsNumber();
			/*settlementPrice += (orderGoods.getSettlementPrice().add(orderGoods.getTax()))
					.multiply(new BigDecimal(orderGoods.getGoodsNumber()).add(orderGoods.getGoodsDecimalNumber())).doubleValue();*/
			BigDecimal goodsDecimalNumber = orderGoods.getGoodsDecimalNumber();
			if (goodsDecimalNumber != null && goodsDecimalNumber.doubleValue() > 0) {
				settlementPrice += NumberUtil.getDecimalValue(orderGoods.getSettlementPrice().multiply(goodsDecimalNumber), 2).doubleValue();
			}
			masterOrderGoodsMapper.insertSelective(orderGoods);
		}
		// 商品结算价 = 商品结算价 + 综合税费
		settlementPrice += masterOrder.getTax();
		settlementPrice = NumberUtil.getDoubleByValue(settlementPrice, 2);
		masterOrder.setGoodsSettlementPrice(settlementPrice);
		logger.info("订单：" + masterOrderSn + "，商品总结算价格:" + settlementPrice + "," + masterOrder.getPaySettlementPrice());
		try {
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(masterOrderInfo);
			orderAction.setActionNote("优惠券和余额平摊计算正确！");
			masterOrderActionService.insertOrderActionByObj(orderAction);
		} catch (Exception e) {
			logger.error(masterOrderSn + "优惠券和余额平摊计算动作成功。但动作日志插入异常", e);
		}
		info.setData(orderGoodsList);
		info.setMessage("生成订单商品成功");
		info.setIsOk(Constant.OS_YES);
		return info;
	}

	/**
	 * 通过订单编码获取订单商品列表
	 * @param masterOrderSn 订单编码
	 * @return List<MasterOrderGoods>
	 */
	@Override
	public List<MasterOrderGoods> selectByMasterOrderSn(String masterOrderSn) {
		if (StringUtil.isEmpty(masterOrderSn)) {
			return null;
		}
		MasterOrderGoodsExample example = new MasterOrderGoodsExample();
		example.or().andMasterOrderSnEqualTo(masterOrderSn);
		return masterOrderGoodsMapper.selectByExample(example);
	}

	@Override
	public List<MasterOrderGoods> selectByOrderSn(String orderSn) {
		if (StringUtil.isEmpty(orderSn)) {
			return null;
		}
		MasterOrderGoodsExample example = new MasterOrderGoodsExample();
		example.or().andOrderSnEqualTo(orderSn);
		return masterOrderGoodsMapper.selectByExample(example);
	}
	
	/**
	 * 保存订单商品
	 * @param masterGoods masterGoods 订单商品
	 * @param masterOrderSn 订单编码
	 * @param orderFrom 渠道店铺编码
	 * @param source
	 * @return MasterOrderGoods
	 */
	private MasterOrderGoods insertOrderGoods(MasterGoods masterGoods, String masterOrderSn, String orderFrom, int source) {
		logger.debug("创建订单:" + masterOrderSn + "商品信息");

		MasterOrderGoods masterOrderGoods = new MasterOrderGoods();
		// 订单编码
		masterOrderGoods.setMasterOrderSn(masterOrderSn);
		// 商品仓库编码
		masterOrderGoods.setDepotCode(StringUtil.isTrimEmpty(masterGoods.getDepotCode())
				? Constant.DETAILS_DEPOT_CODE : masterGoods.getDepotCode());

        fillGoodsBaseInfo(masterOrderGoods, masterGoods);
		//处理无库存下单相关
		logger.info("创建订单:" + masterOrderSn + "商品信息:" + masterGoods.getGoodsSn() + "支持无库存标识：" + masterGoods.getPurchasesWithoutStockFlag() + ",goodsNum=" + masterOrderGoods.getGoodsNumber());
		masterOrderGoods.setWithStockNumber(masterGoods.getWithStockNumber());
		masterOrderGoods.setWithoutStockNumber(masterGoods.getWithoutStockNumber());
		masterOrderGoods.setPurchasesWithoutStockFlag(masterGoods.getPurchasesWithoutStockFlag());
		masterOrderGoods.setWithoutStockDeliveryCycle(masterGoods.getWithoutStockDeliveryCycle());
		masterOrderGoods.setWithoutStockDepotNo(masterGoods.getWithoutStockDepotNo());
        fillGoodsDetail(masterOrderGoods, masterGoods);

		return masterOrderGoods;
	}

    /**
     * 填充商品基本信息
     * @param masterOrderGoods 商品信息
     * @param masterGoods 商品信息
     */
	private void fillGoodsBaseInfo(MasterOrderGoods masterOrderGoods, MasterGoods masterGoods) {

        // 商品市场价
        double goodsPrice = getGoodsPrice(masterGoods);
        masterOrderGoods.setGoodsName(StringUtil.isTrimEmpty(masterGoods.getGoodsName()) ? "" : masterGoods.getGoodsName());
        masterOrderGoods.setGoodsThumb(masterGoods.getGoodsThumb());
        // 商品颜色名称
        masterOrderGoods.setGoodsColorName(StringUtil.isNotEmpty(masterGoods.getColorName()) ? masterGoods.getColorName() :  "");
        // 商品规格名称
        masterOrderGoods.setGoodsSizeName(StringUtil.isNotEmpty(masterGoods.getSizeName()) ? masterGoods.getSizeName() :  "");
        // 商品销售模式：1为自营，2为买断，3为寄售，4为直发
        Byte salesMode = masterGoods.getSalesMode() == null ? null : Byte.valueOf(masterGoods.getSalesMode() + "");
        masterOrderGoods.setSalesMode(salesMode);
        // 商品所属供应商
        String supplierCode = masterGoods.getSupplierCode();
        // 供应商编码
        masterOrderGoods.setSupplierCode(supplierCode);
        // 商品6位码
        masterOrderGoods.setGoodsSn(masterGoods.getGoodsSn());
        // 商品折扣价
        setGoodsDiscount(masterGoods, goodsPrice, masterOrderGoods);
        // 商品价格
        masterOrderGoods.setGoodsPrice(BigDecimal.valueOf(masterGoods.getGoodsPrice()));
        // 商品市场价
        masterOrderGoods.setMarketPrice(BigDecimal.valueOf(goodsPrice));
        // 商品11位码
        masterOrderGoods.setCustomCode(masterGoods.getCustomCode());
        // 商品类型
        masterOrderGoods.setExtensionCode(StringUtil.isEmpty(masterGoods.getExtensionCode()) ? "common" : masterGoods.getExtensionCode());
        // 红包分摊
        masterOrderGoods.setShareBonus(BigDecimal.valueOf(masterGoods.getShareBonus() == null ? 0d : masterGoods.getShareBonus()));
        // 商品打折券
        masterOrderGoods.setUseCard(masterGoods.getUseCard());
        // 拆分后商品行
        if (StringUtil.isNotEmpty(masterGoods.getExtensionId())) {
            masterOrderGoods.setExtensionId(masterGoods.getExtensionId());
        } else {
            masterOrderGoods.setExtensionId("0");
        }
        // 促销id
        masterOrderGoods.setPromotionId(masterGoods.getPromotionId());
        // 套装名称
        masterOrderGoods.setGroupName("");
        // 商品数量
		logger.info("fillGoodsBaseInfo masterOrderGoods goodsNum=" + masterGoods.getGoodsNumber());
		masterOrderGoods.setGoodsNumber(masterGoods.getGoodsNumber());
        // 商品数量小数部分
		masterOrderGoods.setGoodsDecimalNumber(masterGoods.getGoodsDecimals());
        // 父商品sn
        masterOrderGoods.setParentSn("");
        // 成交价格
        masterOrderGoods.setTransactionPrice(BigDecimal.valueOf(masterGoods.getTransactionPrice()));
        //库存占用数量 向上取整
		BigDecimal sendNumberDecimal = masterGoods.getGoodsDecimals().add(BigDecimal.valueOf(masterGoods.getSendNumber()));
		int sendNumber = sendNumberDecimal.setScale( 0, BigDecimal.ROUND_UP ).intValue();
        // 占用库存数量
        masterOrderGoods.setSendNumber(sendNumber);
        // 促销名称
        masterOrderGoods.setPromotionDesc(masterGoods.getPromotionDesc());
        // 财务结算价
        masterOrderGoods.setSettlementPrice(BigDecimal.valueOf(0));
    }

    /**
     * 填充商品信息
     * @param masterOrderGoods 商品信息
     * @param masterGoods 商品信息
     */
	private void fillGoodsDetail(MasterOrderGoods masterOrderGoods, MasterGoods masterGoods) {
        // 小计取整
        masterOrderGoods.setPayPoints(new BigDecimal(Math.floor(masterGoods.getGoodsNumber() * masterGoods.getTransactionPrice())));
        masterOrderGoods.setRankPoints(new BigDecimal(Math.floor(masterGoods.getGoodsNumber() * masterGoods.getTransactionPrice())));

        // 导购
        masterOrderGoods.setSelleruser(masterGoods.getSellerUser());
        // 货位
        masterOrderGoods.setContainerid(masterGoods.getContainerId());
        // 积分
        masterOrderGoods.setIntegral(masterGoods.getIntegral());
        // 积分金额
        masterOrderGoods.setIntegralMoney(BigDecimal.valueOf(masterGoods.getIntegralMoney()));
        // 定制属性
        if (StringUtil.isNotEmpty(masterGoods.getC2mItems())) {
            masterOrderGoods.setC2mItem(masterGoods.getC2mItems());
        }
        // 商品条码
        masterOrderGoods.setSap(masterGoods.getBarCode());
        // 商品bv值
        masterOrderGoods.setBvValue(masterGoods.getBvValue() == null ? "0" : masterGoods.getBvValue().toString());
        // 商品预计发货日
        masterOrderGoods.setExpectedShipDate(masterGoods.getExpectedShipDate());
        // 综合税费
        masterOrderGoods.setTax(BigDecimal.valueOf(masterGoods.getTax()));
        // 综合税率
        masterOrderGoods.setTaxRate(BigDecimal.valueOf(masterGoods.getTaxRate()));
        // 商品基础bv
        masterOrderGoods.setBaseBvValue(masterGoods.getBaseBvValue());

        // 进项税
        Double inputTax = masterGoods.getInputTax();
        if (inputTax != null) {
            masterOrderGoods.setInputTax(BigDecimal.valueOf(inputTax));
        }
        // 销项税
        Double outputTax = masterGoods.getOutputTax();
        if (outputTax != null) {
            masterOrderGoods.setOutputTax(BigDecimal.valueOf(outputTax));
        }
        // 成本价
        Double costPrice = masterGoods.getCostPrice();
        if (costPrice != null) {
            masterOrderGoods.setCostPrice(BigDecimal.valueOf(costPrice));
        }
        // 箱规
        Integer boxGauge = masterGoods.getBoxGauge();
        if (boxGauge != null) {
            masterOrderGoods.setBoxGauge(boxGauge.shortValue());
        }
        // 配送周期
        String distributionCategory = masterGoods.getDistributionCategory();
        masterOrderGoods.setDistributionCategory(distributionCategory);

        //卖家sku
        masterOrderGoods.setSellerCustomCode(masterGoods.getSellerSkuSn());
        // 商品编码
        masterOrderGoods.setSellerGoodsSn(masterGoods.getSellerGoodsSn());

        // 单位
        masterOrderGoods.setBuyUnit(masterGoods.getBuyUnit());
        // 最小起订量
        masterOrderGoods.setMinBuyNum(masterGoods.getMinBuyNum());
        // 发货周期
        masterOrderGoods.setDeliveryCycle(masterGoods.getDeliveryCycle());
        // 销售属性
        masterOrderGoods.setGoodsProp(masterGoods.getGoodsProp());
        // 客户物料编码
        masterOrderGoods.setCustomerMaterialCode(masterGoods.getCustomerMaterialCode());
        //供应商名称
        masterOrderGoods.setSupplierName(masterGoods.getSupplierName());
        //物料描述
        masterOrderGoods.setCustomerMaterialName(masterGoods.getCustomerMaterialName());
        //采购申请编号
        masterOrderGoods.setBuyerNo(masterGoods.getBuyerNo());
        //采购申请行号
        masterOrderGoods.setBuyerLineNo(masterGoods.getBuyerLineNo());

        //加价金额
        BigDecimal goodsAddPrice = masterGoods.getGoodsAddPrice();
        if (goodsAddPrice != null) {
            masterOrderGoods.setGoodsAddPrice(goodsAddPrice);
        }
        //商品数据来源
		masterOrderGoods.setDataSources(masterGoods.getDataSources());

    }

	/**
	 * 获取商品销售价
	 * @param masterGoods 订单商品
	 * @return double
	 */
	private double getGoodsPrice(MasterGoods masterGoods) {
		double goodsPrice = 0.00;
		if (masterGoods.getGoodsPrice() != null) {
			if (masterGoods.getGoodsPrice() > 0) {
				goodsPrice = masterGoods.getGoodsPrice();
			}
		}
		if (masterGoods.getMarketPrice() != null) {
			if (masterGoods.getMarketPrice() > 0) {
				goodsPrice = masterGoods.getMarketPrice();
			}
		}
		return goodsPrice;
	}

	/**
	 * 单间折扣金额
	 * @param masterGoods
	 * @param goodsPrice
	 * @param orderGoods
	 */
	private void setGoodsDiscount(MasterGoods masterGoods, double goodsPrice, MasterOrderGoods orderGoods) {
		// 商品折扣
		BigDecimal discount = BigDecimal.valueOf(0);
		// 商品折扣金额 = 商品销售价 - 商品成交价
		/*BigDecimal price = BigDecimal.valueOf(goodsPrice);
		BigDecimal transactionPrice = BigDecimal.valueOf(masterGoods.getTransactionPrice());
		orderGoods.setDiscount(price.subtract(transactionPrice));*/

		Double goodsDiscount = masterGoods.getDisCount();
		if (goodsDiscount != null && goodsDiscount > 0) {
            discount = new BigDecimal(goodsDiscount.toString());
		}
		orderGoods.setDiscount(discount);
	}
}
