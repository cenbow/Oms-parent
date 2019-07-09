package com.work.shop.oms.core.beans;

import java.io.Serializable;

public class BaseTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderSn;
	
	private String[] orderSns;
	
	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String[] getOrderSns() {
		return orderSns;
	}

	public void setOrderSns(String[] orderSns) {
		this.orderSns = orderSns;
	}
}
