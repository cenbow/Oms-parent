package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 校验订单信息
 * @author QuYachu
 */
public class ValidateOrder implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	/**
	 * 问题原因code
	 */
	private String questionCode;

	/**
	 * 订单是否为团购
	 */
	private short isGroup;

	/**
	 * 是否为预售商品
	 */
	private short isAdvance;
	
	private String smsFlag;
	
	private String smsCode;

	/**
	 * 使用积分金额
	 */
	private Double integralMoney;

	/**
	 * 使用积分
	 */
	private Integer integral;

	/**
	 * 订单财务价
	 */
	private Double orderSettlementPrice;

	/**
	 * 支付单财务价
	 */
	private Double paySettlementPrice;

	/**
	 * 商品财务价
	 */
	private Double GoodsSettlementPrice;
	
	private String goodsQuestionCode;
	
	private String orderFrom;
	
	private String referer;
	
	private Double points;												// 使用点数
	
	private Double surplus;												// 余额支付
	
	private Boolean isXianxia;											// 是否线下支付 true:是;false:否

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public short getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(short isGroup) {
		this.isGroup = isGroup;
	}

	public short getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(short isAdvance) {
		this.isAdvance = isAdvance;
	}

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
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

	public Double getOrderSettlementPrice() {
		return orderSettlementPrice;
	}

	public void setOrderSettlementPrice(Double orderSettlementPrice) {
		this.orderSettlementPrice = orderSettlementPrice;
	}

	public Double getPaySettlementPrice() {
		return paySettlementPrice;
	}

	public void setPaySettlementPrice(Double paySettlementPrice) {
		this.paySettlementPrice = paySettlementPrice;
	}

	public Double getGoodsSettlementPrice() {
		return GoodsSettlementPrice;
	}

	public void setGoodsSettlementPrice(Double goodsSettlementPrice) {
		GoodsSettlementPrice = goodsSettlementPrice;
	}

	public String getGoodsQuestionCode() {
		return goodsQuestionCode;
	}

	public void setGoodsQuestionCode(String goodsQuestionCode) {
		this.goodsQuestionCode = goodsQuestionCode;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Double getSurplus() {
		return surplus;
	}

	public void setSurplus(Double surplus) {
		this.surplus = surplus;
	}

	public Boolean getIsXianxia() {
		return isXianxia;
	}

	public void setIsXianxia(Boolean isXianxia) {
		this.isXianxia = isXianxia;
	}
}
