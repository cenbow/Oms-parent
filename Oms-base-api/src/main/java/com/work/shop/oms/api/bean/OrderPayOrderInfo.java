package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

public class OrderPayOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4773423046238298926L;

	
	private String orderSn;
	//订单需支付金额
	private double totalPayable;
	//订单金额   支付金额+红包+积分+平台币(运费加商品成交价)
	private double totalFee;
	//红包
	private double bonus;
	//平台币
	private double surplus;
	//商品总原价金额
	private double goodsAmount;
	//商品已支付金额
	private double moneyPaid;
	//使用积分
	private int integral;
	private String orderFrom;
	private int orderStatus;
	private int orderType;
	private String userId;
	private String addTime;
	
	private List<OrderPayGoodsInfo> goodsInfo;
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public double getTotalPayable() {
		return totalPayable;
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public double getSurplus() {
		return surplus;
	}
	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public double getMoneyPaid() {
		return moneyPaid;
	}
	public void setMoneyPaid(double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public List<OrderPayGoodsInfo> getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(List<OrderPayGoodsInfo> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
}
