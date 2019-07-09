package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExchangePageOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orderSn;
	private String relatingOriginalSn;
	private String relatingReturnSn;
	private String relatingRemoneySn;

	//换货单支付方式
	private Integer payId;
	
	// 代理换货
	private Byte isAgent;

	// 商品总金额
	private Double goodsAmount;
	// 订单总金额
	private Double totalFee;
	// 已付款金额
	private Double moneyPaid;
	// 应付款金额
	private Double totalPayable;
	// 运费
	private Double shippingTotalFee;
	// 红包
	private Double bonus;
	private Double totalIntegralMoney;//积分使用金额
	// 商品数量
	private Integer goodsCount;
	// 订单总折扣
	private Double discount;
	
	private Integer bvValue;
	
	private Integer baseBvValue;
	
	public Double getTotalIntegralMoney() {
		return totalIntegralMoney;
	}
	public void setTotalIntegralMoney(Double totalIntegralMoney) {
		this.totalIntegralMoney = totalIntegralMoney;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getRelatingOriginalSn() {
		return relatingOriginalSn;
	}
	public void setRelatingOriginalSn(String relatingOriginalSn) {
		this.relatingOriginalSn = relatingOriginalSn;
	}
	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}
	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
	}
	public String getRelatingRemoneySn() {
		return relatingRemoneySn;
	}
	public void setRelatingRemoneySn(String relatingRemoneySn) {
		this.relatingRemoneySn = relatingRemoneySn;
	}
	
	public Integer getPayId() {
		return payId;
	}
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	public Byte getIsAgent() {
		return isAgent;
	}
	public void setIsAgent(Byte isAgent) {
		this.isAgent = isAgent;
	}
	public Double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public Double getMoneyPaid() {
		return moneyPaid;
	}
	public void setMoneyPaid(Double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}
	public Double getTotalPayable() {
		return totalPayable;
	}
	public void setTotalPayable(Double totalPayable) {
		this.totalPayable = totalPayable;
	}
	public Double getShippingTotalFee() {
		return shippingTotalFee;
	}
	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Integer getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
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
}
