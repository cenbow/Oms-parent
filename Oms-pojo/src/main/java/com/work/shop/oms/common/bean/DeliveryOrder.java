package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class DeliveryOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3773612788466057979L;

	private String oid;//
	
	private String orderType;//
	
	private String companyCode;//
	
	private String outSid;//
	
	private Integer isOk;//
	
	private String channelCode;//
	
	private Integer maxRequestNums;//
	
	private String errorKey;//
	
	private String errorName;//
	
	private String errorInfo;//
	
	private String addTime; //
	
	private String updateTime; //

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getOutSid() {
		return outSid;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}

	public Integer getIsOk() {
		return isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getMaxRequestNums() {
		return maxRequestNums;
	}

	public void setMaxRequestNums(Integer maxRequestNums) {
		this.maxRequestNums = maxRequestNums;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	private String shopCode;//渠道编码
	
	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

}
