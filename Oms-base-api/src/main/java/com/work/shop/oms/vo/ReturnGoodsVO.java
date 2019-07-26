package com.work.shop.oms.vo;


import java.io.Serializable;

public class ReturnGoodsVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;//商品id
	private String goodsName;//商品名称
	private String extensionCode;//商品属性
	private String extensionId;//商品扩展属性ID
	private String goodsSn;//货号
	private String goodsColorName;//颜色
	private String goodsSizeName;//尺寸
	private String customCode;//企业商品编码
	private Double marketPrice;//商品价格
	private Double goodsPrice;//成交价
	private Double settlementPrice;//财务价格;
	private Double shareBonus;	//分摊金额
	private Double shareSettle;//财务分摊金额
	private Integer goodsBuyNumber;//购买量
	private Double discount = 0D;//折扣
    /**
     * 成本价
     */
    private Double costPrice;
	
	//退货
	private String osDepotCode;//所属发货仓
	private Integer goodsReturnNumber;// 退单商品数量
	private Integer shopReturnCount;//门店退货量
	private Integer havedReturnCount;//已退货量
	private Integer canReturnCount;//可退货量;
	private String returnReason;//退换货原因
	private Integer salesMode;//
	
	//退款
	private Integer priceDifferNum;//退差价数量
	private Double priceDifference;//退差价单价
	private Double priceDiffTotal;//退差价小计
	
	private String goodsThumb;//图片url
	private Double payPoints;//消费积分
	
	private String seller;//供销商编码
	private Integer prodScanNum;//实际入库数量
	private Integer isGoodReceived;//是否收到货 （0 否  1 是）order_return_goods
	private Integer checkinStatus;//是否入库 （0未入库 1已入库 2待入库）order_return_goods
	private Integer qualityStatus;//退单质检状态 （0质检不通过、1质检通过）order_return_goods
	private Double integralMoney;//使用积分金额
	
	private String sap;
	
	private Integer bvValue;
	
	private Integer baseBvValue;
	
	/**
	 * 箱规
	 */
	private Integer boxGauge;

    /**
     * 退货申请
     */
    //是否申请
    private boolean isChange;

    /**
     * 待退货数量
     */
    private Integer dealReturnNum;

    /**
     * 供应商名称
     */
    private String supplierName;

	public Integer getProdScanNum() {
		return prodScanNum;
	}
	public void setProdScanNum(Integer prodScanNum) {
		this.prodScanNum = prodScanNum;
	}
	public Double getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIsGoodReceived() {
		return isGoodReceived;
	}
	public void setIsGoodReceived(Integer isGoodReceived) {
		this.isGoodReceived = isGoodReceived;
	}
	public Integer getCheckinStatus() {
		return checkinStatus;
	}
	public void setCheckinStatus(Integer checkinStatus) {
		this.checkinStatus = checkinStatus;
	}
	public Integer getQualityStatus() {
		return qualityStatus;
	}
	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public Double getPayPoints() {
		return payPoints;
	}
	public void setPayPoints(Double payPoints) {
		this.payPoints = payPoints;
	}
	public String getGoodsThumb() {
		return goodsThumb;
	}
	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}
	public Double getShareSettle() {
		return shareSettle == null ? 0 : shareSettle;
	}
	public void setShareSettle(Double shareSettle) {
		this.shareSettle = shareSettle;
	}
	public String getExtensionId() {
		return extensionId;
	}
	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}
	public String getGoodsColorName() {
		return goodsColorName;
	}
	public void setGoodsColorName(String goodsColorName) {
		this.goodsColorName = goodsColorName;
	}
	public String getGoodsSizeName() {
		return goodsSizeName;
	}
	public void setGoodsSizeName(String goodsSizeName) {
		this.goodsSizeName = goodsSizeName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getExtensionCode() {
		return extensionCode;
	}
	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public String getCustomCode() {
		return customCode;
	}
	
	public Double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Double getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	public Double getShareBonus() {
		return shareBonus;
	}
	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
	}
	
	public Integer getGoodsBuyNumber() {
		return goodsBuyNumber;
	}
	public void setGoodsBuyNumber(Integer goodsBuyNumber) {
		this.goodsBuyNumber = goodsBuyNumber;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getOsDepotCode() {
		return osDepotCode;
	}
	public void setOsDepotCode(String osDepotCode) {
		this.osDepotCode = osDepotCode;
	}
	public Integer getShopReturnCount() {
		return shopReturnCount;
	}
	public void setShopReturnCount(Integer shopReturnCount) {
		this.shopReturnCount = shopReturnCount;
	}
	public Integer getHavedReturnCount() {
		return havedReturnCount;
	}
	public void setHavedReturnCount(Integer havedReturnCount) {
		this.havedReturnCount = havedReturnCount;
	}
	public Integer getCanReturnCount() {
		return canReturnCount;
	}
	public void setCanReturnCount(Integer canReturnCount) {
		this.canReturnCount = canReturnCount;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Integer getPriceDifferNum() {
		return priceDifferNum;
	}
	public void setPriceDifferNum(Integer priceDifferNum) {
		this.priceDifferNum = priceDifferNum;
	}
	public Double getPriceDifference() {
		return priceDifference;
	}
	public void setPriceDifference(Double priceDifference) {
		this.priceDifference = priceDifference;
	}
	public Double getPriceDiffTotal() {
		return priceDiffTotal;
	}
	public void setPriceDiffTotal(Double priceDiffTotal) {
		this.priceDiffTotal = priceDiffTotal;
	}
	public Integer getSalesMode() {
		return salesMode;
	}
	public void setSalesMode(Integer salesMode) {
		this.salesMode = salesMode;
	}
	public String getSap() {
		return sap;
	}
	public void setSap(String sap) {
		this.sap = sap;
	}
	public Integer getBvValue() {
		return bvValue;
	}
	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}
	public Integer getBaseBvValue() {
		return baseBvValue;
	}
	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}
	public Integer getGoodsReturnNumber() {
		return goodsReturnNumber;
	}
	public void setGoodsReturnNumber(Integer goodsReturnNumber) {
		this.goodsReturnNumber = goodsReturnNumber;
	}
	public Integer getBoxGauge() {
		return boxGauge;
	}
	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public Integer getDealReturnNum() {
        return dealReturnNum;
    }

    public void setDealReturnNum(Integer dealReturnNum) {
        this.dealReturnNum = dealReturnNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
}
