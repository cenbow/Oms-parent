package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;

public class UserOrderInfo implements Serializable {
	/**
	 * 用户订单列表初始数据
	 */
	private static final long serialVersionUID = 7286559905002317028L;
	/**
	 * 订单相关
	 */
	private Date addTime;
	private String payName;
	private double totalPayable;
	private double totalfee;
	private double payTotalfee;
	private double surplus;
	private double bonus;
	private Date payTime;
	private double shippingTotalFee;
	private String relatingOriginalSn;//换单原单号
	private int goodsCount;//订单商品数量
	private double goodsAmount;//商品总金额
	private int returnAllgoodsCount;//退单商品数量
	private String orderSn;
	private String userId;
	private String consignee;
	private int orderStatus;
	private int payStatus;
	private int shipStatus;
	private int orderType;
	private int rpayStatus;
	private int TransType;
	private int moneyTreatmentType;
	private int payId;
	private int isReview;
	private double moneyPaid;
	private int source;
	private String channelCode;//店铺编码
	private String channelName;//店铺名称

	
	/**
	 * 退单相关
	 */
	private String	orderReturnSn;//退单编号
	private double returnTotalFee;//退款总金额
	
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public double getSurplus() {
		return surplus;
	}
	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public double getShippingTotalFee() {
		return shippingTotalFee;
	}
	public void setShippingTotalFee(double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}
	public String getRelatingOriginalSn() {
		return relatingOriginalSn;
	}
	public void setRelatingOriginalSn(String relatingOriginalSn) {
		this.relatingOriginalSn = relatingOriginalSn;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public String getOrderReturnSn() {
		return orderReturnSn;
	}
	public void setOrderReturnSn(String orderReturnSn) {
		this.orderReturnSn = orderReturnSn;
	}
	public double getReturnTotalFee() {
		return returnTotalFee;
	}
	public void setReturnTotalFee(double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}
	public double getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(double totalfee) {
		this.totalfee = totalfee;
	}
	public double getTotalPayable() {
		return totalPayable;
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}
	public double getPayTotalfee() {
		return payTotalfee;
	}
	public void setPayTotalfee(double payTotalfee) {
		this.payTotalfee = payTotalfee;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	public int getReturnAllgoodsCount() {
		return returnAllgoodsCount;
	}
	public void setReturnAllgoodsCount(int returnAllgoodsCount) {
		this.returnAllgoodsCount = returnAllgoodsCount;
	}
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getRpayStatus() {
		return rpayStatus;
	}
	public void setRpayStatus(int rpayStatus) {
		this.rpayStatus = rpayStatus;
	}
	public int getTransType() {
		return TransType;
	}
	public void setTransType(int transType) {
		TransType = transType;
	}
	public int getMoneyTreatmentType() {
		return moneyTreatmentType;
	}
	public void setMoneyTreatmentType(int moneyTreatmentType) {
		this.moneyTreatmentType = moneyTreatmentType;
	}
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
	}
	public int getIsReview() {
		return isReview;
	}
	public void setIsReview(int isReview) {
		this.isReview = isReview;
	}
	public double getMoneyPaid() {
		return moneyPaid;
	}
	public void setMoneyPaid(double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	

}
