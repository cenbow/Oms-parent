package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderCancelInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8882618665533413033L;
	private int cancelCode;
	private String cancelRemark;
	private String orderSn;
	private String cancelIdtSource; //撤单来源(空或0：ERP，1：OS，2：POS)
	
	public OrderCancelInfo(int cancelCode, String cancelRemark, String orderSn, String cancelIdtSource) {
		super();
		this.cancelCode = cancelCode;
		this.cancelRemark = cancelRemark;
		this.orderSn = orderSn;
		this.cancelIdtSource = cancelIdtSource;
	}
	
	public OrderCancelInfo() {
		super();
	}

	public int getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(int cancelCode) {
		this.cancelCode = cancelCode;
	}
	public String getCancelRemark() {
		return cancelRemark;
	}
	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getCancelIdtSource() {
		return cancelIdtSource;
	}
	public void setCancelIdtSource(String cancelIdtSource) {
		this.cancelIdtSource = cancelIdtSource;
	}

	@Override
	public String toString() {
		return "OrderCancelInfo [cancelCode=" + cancelCode + ", cancelRemark=" + cancelRemark + ", orderSn=" 
				+ orderSn + ", cancelIdtSource=" + cancelIdtSource + "]";
	}
	
}
