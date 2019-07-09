package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderMasterInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8561734213780427383L;
	private String orderSn;
	private String shipCode;											//快递代码
	private int type = 0;												// 0 : B2C ; 1: C2b
	private Double orderAmount;											//订单总额
	private Double shippingFee;											//运费
	private Double discount;											//优惠金额
	private Double receivables;											//应收款
	private Double paidFee;												//已付款金额
	private Double fee;													//待收款
	private Double otherFee;											//保价费   其它费用
	
	public String getShipCode() {
		return shipCode;
	}
	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getReceivables() {
		return receivables;
	}
	public void setReceivables(Double receivables) {
		this.receivables = receivables;
	}
	public Double getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(Double paidFee) {
		this.paidFee = paidFee;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public OrderMasterInfo() {
		super();
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "OrderMasterInfo [orderSn=" + orderSn + ", shipCode=" + shipCode + ", type=" + type + ", orderAmount=" + orderAmount
				+ ", shippingFee=" + shippingFee + ", discount=" + discount + ", receivables=" + receivables + ", paidFee=" + paidFee + ", fee=" + fee
				+ ", otherFee=" + otherFee + "]";
	}
}
