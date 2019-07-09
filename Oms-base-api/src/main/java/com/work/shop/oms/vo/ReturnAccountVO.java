package com.work.shop.oms.vo;

import java.io.Serializable;

public class ReturnAccountVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double returnGoodsMoney;//退货总金额
	private Double totalPriceDifference;	//退产品差价
	private Double returnShipping;//配送费用
	private Double returnOtherMoney;//退其他费用
	private Double returnBonusMoney;//红包金额
	private Double returnTotalFee;//退款总金额
	private Double returnDiffMoney;//差额
	private Double totalIntegralMoney;//使用总的积分金额
	
	private Double bonus;//原订单红包
	private Double shippingTotalFee;//原订单配送总费用
	
	//原订单付款备注
	private String orderPayDesc;
	
	public Double getTotalIntegralMoney() {
		return totalIntegralMoney;
	}
	public void setTotalIntegralMoney(Double totalIntegralMoney) {
		this.totalIntegralMoney = totalIntegralMoney;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Double getShippingTotalFee() {
		return shippingTotalFee;
	}
	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}
	public Double getReturnGoodsMoney() {
		return returnGoodsMoney;
	}
	
	public String getOrderPayDesc() {
		return orderPayDesc;
	}
	public void setOrderPayDesc(String orderPayDesc) {
		this.orderPayDesc = orderPayDesc;
	}
	public void setReturnGoodsMoney(Double returnGoodsMoney) {
		this.returnGoodsMoney = returnGoodsMoney;
	}
	public Double getTotalPriceDifference() {
		return totalPriceDifference;
	}
	public void setTotalPriceDifference(Double totalPriceDifference) {
		this.totalPriceDifference = totalPriceDifference;
	}
	public Double getReturnShipping() {
		return returnShipping;
	}
	public void setReturnShipping(Double returnShipping) {
		this.returnShipping = returnShipping;
	}
	public Double getReturnOtherMoney() {
		return returnOtherMoney;
	}
	public void setReturnOtherMoney(Double returnOtherMoney) {
		this.returnOtherMoney = returnOtherMoney;
	}
	public Double getReturnBonusMoney() {
		return returnBonusMoney;
	}
	public void setReturnBonusMoney(Double returnBonusMoney) {
		this.returnBonusMoney = returnBonusMoney;
	}
	public Double getReturnTotalFee() {
		return returnTotalFee;
	}
	public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}
	public Double getReturnDiffMoney() {
		return returnDiffMoney;
	}
	public void setReturnDiffMoney(Double returnDiffMoney) {
		this.returnDiffMoney = returnDiffMoney;
	}
	
	 
}
