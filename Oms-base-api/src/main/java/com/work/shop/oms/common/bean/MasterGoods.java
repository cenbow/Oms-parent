package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;

/**
 * 配送商品信息
 * @author QuYachu
 */
public class MasterGoods implements Serializable {

	private static final long serialVersionUID = 4777447209476140996L;

	/**
	 * sku
	 */
	private String skuSn;

	/**
	 * 商品打折券号
	 */
	private String useCard;

	/**
	 * 商品sap码(条码)
	 */
	private String barCode;

	/**
	 * 商品11位码
	 */
	private String customCode;

	/**
	 * 成交价格
	 */
	private Double transactionPrice;

	/**
	 * 商品数量
	 */
	private Integer goodsNumber;

	/**
	 * 商品编码
	 */
	private String goodsSn;

	/**
	 * 商品扩展属性
	 */
	private String extensionCode;

	/**
	 * 商品扩展属性行号
	 */
	private String extensionId;

	/**
	 * 商品吊牌价
	 */
	private Double marketPrice;

	/**
	 * 商品销售价
	 */
	private Double goodsPrice;

	/**
	 * 促销信息
	 */
	private String promotionDesc;

	/**
	 * 商品总折扣
	 */
	private Double disCount;

	/**
	 * 红包分摊金额
	 */
	private Double shareBonus;

	/**
	 * 红包
	 */
	private Double bonus;

	/**
	 * 导购
	 */
	private String sellerUser;

	/**
	 * 预留货位
	 */
	private String containerId;

	/**
	 * 商品快照图片地址
	 */
	private String goodsThumb;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 购物项id
	 */
	private String itemId;

	/**
	 * 使用积分金额
	 */
	private Double integralMoney;

	/**
	 * 使用积分数量
	 */
	private Integer integral;

	/**
	 * C2M 定制化属性列表
	 */
	private String c2mItems;

	/**
	 * 促销id
	 */
	private String promotionId;

	/**
	 * 颜色名称
	 */
	private String colorName;

	/**
	 * 尺码名称
	 */
	private String sizeName;

	/**
	 * 商品销售模式：1为自营，2为买断，3为寄售，4为直发
	 */
	private Integer salesMode;

	/**
	 * 商品所属供应商
	 */
	private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

	/**
	 * 库存占用量
	 */
	private Short sendNumber;

	/**
	 * 发货仓编码
	 */
	private String depotCode;

	/**
	 * bvValue
	 */
	private Integer bvValue;

	/**
	 * 预计发货日
	 */
	private String expectedShipDate;

	/**
	 * 综合税费
	 */
	private Double tax;

	/**
	 * 综合税率
	 */
	private Double taxRate;

	/**
	 * 基础BV值
	 */
	private Integer baseBvValue;

	/**
	 * 进项税
	 */
    private Double inputTax;

	/**
	 * 销项税
	 */
	private Double outputTax;

	/**
	 * 成本价
	 */
	private Double costPrice;

	/**
	 * 箱规
	 */
	private Integer boxGauge;

	/**
	 * 配送周期信息
	 */
	private String distributionCategory;

	/**
	 * 卖家sku编码
	 */
    private String sellerSkuSn;

	/**
	 * 卖家商品编码
	 */
	private String sellerGoodsSn;

	/**
	 * 最小起订量
	 */
	private int minBuyNum;

	/**
	 * 客户物料编码
	 */
	private String customerMaterialCode;

	/**
	 * 单位
	 */
	private String buyUnit;

	/**
	 * 发货周期
	 */
	private String deliveryCycle;

	/**
	 * 商品属性
	 */
	private String goodsProp;

    /**
     * 物料描述
     */
	private String customerMaterialName;

    /**
     * 采购申请编号
     */
	private String buyerNo;

    /**
     * 采购申请行号
     */
	private String buyerLineNo;

    /**
     * 商品加价金额
     */
	private BigDecimal goodsAddPrice;

    public Double getShareBonus() {
		return shareBonus;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	public String getSkuSn() {
		return skuSn == null ? "" : skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public Double getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getBarCode() {
		return barCode == null ? "" : barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getCustomCode() {
		return customCode == null ? "" : customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getExtensionCode() {
		return extensionCode == null ? "" : extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getExtensionId() {
		return extensionId == null  || "".equals(extensionId.trim())  ? "" : extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public String getUseCard() {
		return useCard == null ? "" : useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public static void main(String[] args) {
		String str = "{\"goods_name\":\"\u7537\u591a\u8272\u6761\u7eb9\u77ed\u8896\u6064\",\"skuSn\":\"6100064461046\",\"goodsNumber\":1,\"market_price\":\"159.00\",\"isShare\":1,\"goods_attr\":\"\",\"goods_price\":\"159.00\",\"send_number\":0,\"is_real\":1,\"use_card\":null,\"extension_id\":\"common\",\"parent_sn\":0,\"discount\":0,\"extensionCode\":\"common\",\"transactionPrice \":159}";
		MasterGoods g = JSON.parseObject(str, MasterGoods.class);
		System.out.println(g);
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getSellerUser() {
		return sellerUser;
	}

	public void setSellerUser(String sellerUser) {
		this.sellerUser = sellerUser;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public Double getIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		integral = integral == null ? 0 : integral;
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getC2mItems() {
		return c2mItems;
	}

	public void setC2mItems(String c2mItems) {
		this.c2mItems = c2mItems;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Integer getSalesMode() {
		return salesMode == null ? 1 : salesMode;
	}

	public void setSalesMode(Integer salesMode) {
		this.salesMode = salesMode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Short getSendNumber() {
		return sendNumber == null ? 0 : sendNumber;
	}

	public void setSendNumber(Short sendNumber) {
		this.sendNumber = sendNumber;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Integer getBvValue() {
		return bvValue == null ? 0 : bvValue;
	}

	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	
	public Double getTax() {
		return tax == null ? 0.00 : tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTaxRate() {
		return taxRate == null ? 0.00 : taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
	}

	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}

	public Double getInputTax() {
		return inputTax;
	}

	public void setInputTax(Double inputTax) {
		this.inputTax = inputTax;
	}

	public Double getOutputTax() {
		return outputTax;
	}

	public void setOutputTax(Double outputTax) {
		this.outputTax = outputTax;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

	public String getDistributionCategory() {
		return distributionCategory;
	}

	public void setDistributionCategory(String distributionCategory) {
		this.distributionCategory = distributionCategory;
	}

	public Double getDisCount() {
		return disCount;
	}

	public void setDisCount(Double disCount) {
		this.disCount = disCount;
	}

    public String getSellerSkuSn() {
        return sellerSkuSn;
    }

    public void setSellerSkuSn(String sellerSkuSn) {
        this.sellerSkuSn = sellerSkuSn;
    }

    public String getSellerGoodsSn() {
        return sellerGoodsSn;
    }

    public void setSellerGoodsSn(String sellerGoodsSn) {
        this.sellerGoodsSn = sellerGoodsSn;
    }

    public int getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(int minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getCustomerMaterialCode() {
        return customerMaterialCode;
    }

    public void setCustomerMaterialCode(String customerMaterialCode) {
        this.customerMaterialCode = customerMaterialCode;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getGoodsProp() {
        return goodsProp;
    }

    public void setGoodsProp(String goodsProp) {
        this.goodsProp = goodsProp;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCustomerMaterialName() {
        return customerMaterialName;
    }

    public void setCustomerMaterialName(String customerMaterialName) {
        this.customerMaterialName = customerMaterialName;
    }

    public String getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(String buyerNo) {
        this.buyerNo = buyerNo;
    }

    public String getBuyerLineNo() {
        return buyerLineNo;
    }

    public void setBuyerLineNo(String buyerLineNo) {
        this.buyerLineNo = buyerLineNo;
    }

    public BigDecimal getGoodsAddPrice() {
        return goodsAddPrice;
    }

    public void setGoodsAddPrice(BigDecimal goodsAddPrice) {
        this.goodsAddPrice = goodsAddPrice;
    }
}
