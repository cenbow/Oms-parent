package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;

public class ReturnPay implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	private String paySn;									// 支付单编码
	
	private String payCode;									// 支付方式编码
	
	private String payName;									// 支付方式名称

	private String payNote;									// 付款备注

	private Double payTotalFee;								// 付款单总金额
	
	private Integer payStatus;								// 付款单支付状态
	
	private String createTime;								// 支付单创建时间
	
	private String payTime;									// 支付单支付时间

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayNote() {
		return payNote;
	}

	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}

	public Double getPayTotalFee() {
		return payTotalFee;
	}

	public void setPayTotalFee(Double payTotalFee) {
		this.payTotalFee = payTotalFee;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
}
