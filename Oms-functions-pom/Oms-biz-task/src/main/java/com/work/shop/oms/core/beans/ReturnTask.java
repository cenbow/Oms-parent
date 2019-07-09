package com.work.shop.oms.core.beans;

import java.io.Serializable;

public class ReturnTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int isOk;
	private String msg;
	private String orderSn;
	
	private String response;
	
	public int getIsOk() {
		return isOk;
	}
	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
