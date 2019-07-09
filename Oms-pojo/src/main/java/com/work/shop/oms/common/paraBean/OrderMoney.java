package com.work.shop.oms.common.paraBean;


import java.io.Serializable;

public class OrderMoney implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2915171260402717855L;
	
	private String billNo;

	private String orderSn;
	
	private String orderOutSn;
	
	private double orderMoney;
	
	private String actionUser;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public double getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(double orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

}
