package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderRefundBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1564906430637613889L;

	private String returnPaySn;

	private String relatingReturnSn;

	private Byte returnPayStatus;

	private Short returnPay;
	
	private String returnPayName;

	private BigDecimal returnFee;

	private Date addTime;

	private Date updateTime;

	private Byte backbalance;

	public String getReturnPaySn() {
		return returnPaySn;
	}

	public void setReturnPaySn(String returnPaySn) {
		this.returnPaySn = returnPaySn;
	}

	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}

	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
	}

	public Byte getReturnPayStatus() {
		return returnPayStatus;
	}

	public void setReturnPayStatus(Byte returnPayStatus) {
		this.returnPayStatus = returnPayStatus;
	}

	public Short getReturnPay() {
		return returnPay;
	}

	public void setReturnPay(Short returnPay) {
		this.returnPay = returnPay;
	}

	public BigDecimal getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(BigDecimal returnFee) {
		this.returnFee = returnFee;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Byte getBackbalance() {
		return backbalance;
	}

	public void setBackbalance(Byte backbalance) {
		this.backbalance = backbalance;
	}

	public String getReturnPayName() {
		return returnPayName;
	}

	public void setReturnPayName(String returnPayName) {
		this.returnPayName = returnPayName;
	}
}