package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ChannelDeliveryReturn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8513569637121176277L;
	private String outSn;
	private boolean success;
	private String msg;
	
	private String osShipSn;
	private String subCode;
	private String subMsg;
	private Integer orderCount;
	private String response;
	private String orderIdList;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	
	public String getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(String orderIdList) {
		this.orderIdList = orderIdList;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public String getOsShipSn() {
		return osShipSn;
	}
	public void setOsShipSn(String osShipSn) {
		this.osShipSn = osShipSn;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOutSn() {
		return outSn;
	}
	public void setOutSn(String outSn) {
		this.outSn = outSn;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getSubMsg() {
		return subMsg;
	}
	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}
	
}
