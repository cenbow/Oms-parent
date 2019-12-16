package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class CreateOrderReturnGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主单号
	 */
	private String masterOrderSn;

	/**
	 * 子单号
	 */
	private String orderSn;

	private String relatingReturnSn;
	private String customCode;

	private Integer goodsReturnNumber;

	private Integer goodsBuyNumber;
	private Integer chargeBackCount;
	private Integer haveReturnCount;
	private String extensionCode;
	private String extensionId;
	private String osDepotCode;
	private Double settlePrice = 0D;
	private Double shareSettle = 0D;
	private Double priceDifference = 0D;

	private Integer priceDifferNum = 0;

	private String priceDifferReason;

	private String returnReason;

	private String goodsThumb;//产品图片url
	private Double payPoints;//消费积分
	private Double settlementPrice;//财务价
	
	private Double goodsPrice;//成交价
	private Double marketPrice;//市场价
	private Double shareBonus;//红包分摊
	private Integer isGoodReceived;//是否收到货 （0 否  1 是）order_return_goods
	private Integer checkinStatus;//是否入库 （0未入库 1已入库 2待入库）order_return_goods
	private Integer qualityStatus;//退单质检状态 （0质检不通过、1质检通过）order_return_goods
	
	private String seller;//供销商编码
	private Double integralMoney;//积分使用金额
	private Integer salesMode;//商品销售模式：1为自营，2为买断，3为寄售，4为直发
	
	private String sizeName;
	private String colorName;
	private String goodsName;
	private String goodsSn;
	
	private String sap;
	
	private Integer bvValue;
	
	private Double discount; 										// 折让
	
	private Integer baseBvValue;
	
	private Integer boxGauge;  // 箱规

    /**
     * 成本价
     */
    private Double costPrice;
	
	public String getMasterOrderSn() {
        return masterOrderSn;
    }
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }
    public String getOrderSn() {
        return orderSn;
    }
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
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
	public Double getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
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
	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}
	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
	}
	public String getCustomCode() {
		return customCode;
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
	public Double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
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
	public Double getShareSettle() {
		return shareSettle;
	}
	public void setShareSettle(Double shareSettle) {
		this.shareSettle = shareSettle;
	}
	public Double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Integer getGoodsReturnNumber() {
		return goodsReturnNumber;
	}

	public void setGoodsReturnNumber(Integer goodsReturnNumber) {
		this.goodsReturnNumber = goodsReturnNumber;
	}

	public Integer getGoodsBuyNumber() {
		return goodsBuyNumber;
	}

	public void setGoodsBuyNumber(Integer goodsBuyNumber) {
		this.goodsBuyNumber = goodsBuyNumber;
	}

	public Integer getChargeBackCount() {
		return chargeBackCount;
	}
	public void setChargeBackCount(Integer chargeBackCount) {
		this.chargeBackCount = chargeBackCount;
	}
	public Integer getHaveReturnCount() {
		return haveReturnCount;
	}
	public void setHaveReturnCount(Integer haveReturnCount) {
		this.haveReturnCount = haveReturnCount;
	}
	public String getExtensionCode() {
		return extensionCode;
	}
	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}
	public String getExtensionId() {
		return extensionId;
	}
	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}
	public String getOsDepotCode() {
		return osDepotCode;
	}
	public void setOsDepotCode(String osDepotCode) {
		this.osDepotCode = osDepotCode;
	}
	public Double getPriceDifference() {
		return priceDifference;
	}

	public void setPriceDifference(Double priceDifference) {
		this.priceDifference = priceDifference;
	}

	public Integer getPriceDifferNum() {
		return priceDifferNum;
	}

	public void setPriceDifferNum(Integer priceDifferNum) {
		this.priceDifferNum = priceDifferNum;
	}

	public String getPriceDifferReason() {
		return priceDifferReason;
	}
	public void setPriceDifferReason(String priceDifferReason) {
		this.priceDifferReason = priceDifferReason;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getGoodsThumb() {
		return goodsThumb;
	}
	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
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
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public Double getDiscount() {
		return discount == null ? 0D : discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
	}
	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}
	public Integer getBoxGauge() {
		return boxGauge;
	}
	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
}
