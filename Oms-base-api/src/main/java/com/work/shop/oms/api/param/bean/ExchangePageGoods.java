package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class ExchangePageGoods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商品主键
	 */
	private String customCode;
	private String extensionCode;
	
	/**
	 * 价格相关
	 */
	private Double marketPrice;
	private Double goodsPrice;
	private Double transactionPrice;
	private Double shareBonus;
	
	private String goodsThumb;//产品图片url
	private Double integralMoney;//积分使用金额
	
	public Double getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Double getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}

	//购买量
	private Short goodsNumber;

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Double getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public Short getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Short goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	 
	
}
