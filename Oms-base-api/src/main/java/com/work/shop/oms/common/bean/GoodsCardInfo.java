package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class GoodsCardInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4777447209476140996L;

	private String customCode;										// 商品11位码
	
	private Double marketPrice;										// 商品市场价
	
	private Double goodsPrice;										// 商品吊牌价
	
	private Double transactionPrice; 								// 成交价格

	private Double shareBonus;										// 红包分摊金额

	private Double integralMoney;									// 使用积分金额

	private Integer integral;										// 使用积分数量
	
	private String cardCouponNo;									// 券卡卡号

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
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

	public Double getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
	}

	public Double getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getCardCouponNo() {
		return cardCouponNo;
	}

	public void setCardCouponNo(String cardCouponNo) {
		this.cardCouponNo = cardCouponNo;
	}
}
