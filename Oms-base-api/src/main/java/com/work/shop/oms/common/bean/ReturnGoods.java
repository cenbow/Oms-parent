package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ReturnGoods implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4777447209476140996L;
	
	private String customCode;										// 商品11位码
	
	private String goodsSn;											// 商品编码
	
	private Double transactionPrice; 								// 成交价格

	private Double settlementPrice; 								// 财务价格
	
	private Integer goodsNumber;									// 商品数量
	
	private String extensionCode;									// 商品扩展属性；
	
	private Double goodsPrice;										// 商品吊牌价

	private Double shareBonus;										// 红包分摊金额
	
	private String goodsThumb;										// 商品快照图片地址
	
	private String goodsName;										// 商品名称

	private Double integralMoney;									// 使用积分金额
	
	private String colorName;										// 颜色名称
	
	private String sizeName;										// 尺码名称
	
	private String sap;												// sap
	
	private Double discount; 										// 折让
	
	private Integer bvValue;										// bvValue
	
	private Integer baseBvValue;									// 基础BV
	
	private String depotCode;										// 发货仓编码
	
	private String prIds;											// 订单促销ID
	
	public Double getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
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

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
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

	public Double getIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
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

	public Double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getPrIds() {
		return prIds;
	}

	public void setPrIds(String prIds) {
		this.prIds = prIds;
	}
}
