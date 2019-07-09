package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class ReturnShipUpdateInfo implements Serializable {

	private static final long serialVersionUID = -6640752452691483576L;
	
	private String orderSn;//订单号
	private String returnInvoiceNo;//快递单号
	private String returnExpress ;//承运商
	
	private String userName;//操作人

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

	public String getReturnExpress() {
		return returnExpress;
	}

	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
