package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShopPayResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//接口调用状态
	private Integer status;
	
	private String msg;
	
	private String result;
	//消费流水号
	private String orderSerialNo;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrderSerialNo() {
		return orderSerialNo;
	}

	public void setOrderSerialNo(String orderSerialNo) {
		this.orderSerialNo = orderSerialNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BanggoSurplusResultBean [status=" + status + ", msg=" + msg + ", result=" + result + ", orderSerialNo=" + orderSerialNo + "]";
	}
	
	
}
