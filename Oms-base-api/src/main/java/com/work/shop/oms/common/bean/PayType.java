package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class PayType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475741002818867341L;

	private int pId;
	
	private String code;
	
	private Double payFee;
	
	public PayType() {
	}

	public PayType(int pId, Double payFee) {
		this.pId = pId;
		this.payFee = payFee;
	}

	public int getpId() {
		return pId;
	}

	public void setPId(int pId) {
		this.pId = pId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPayFee() {
		return payFee;
	}

	public void setPayFee(Double payFee) {
		this.payFee = payFee;
	}
}
