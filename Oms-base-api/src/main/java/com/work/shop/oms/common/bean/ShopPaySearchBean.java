package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShopPaySearchBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//接口调用状态
	private Integer status;
	
	//消费流水号
	private String orderSerialNo;
	
	private ShopPayResultDetailBean result;

	public ShopPayResultDetailBean getResult() {
		return result;
	}

	public void setResult(ShopPayResultDetailBean result) {
		this.result = result;
	}

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

	
	

}
