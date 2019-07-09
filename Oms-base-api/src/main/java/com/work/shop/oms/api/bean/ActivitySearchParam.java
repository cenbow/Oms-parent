package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class ActivitySearchParam implements Serializable{
	
	/**
	 * 平台活动查询用户订单消息
	 */
	private static final long serialVersionUID = 4072269535238496675L;
	private String orderFrom;
	private Integer payStatus;
	private String userId;
	private String addTimeStart;
	private String addTimeEnd;
	private Double prizeMax;
	private Double prizeMin;
	private String siteCode;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAddTimeStart() {
		return addTimeStart;
	}
	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}
	public String getAddTimeEnd() {
		return addTimeEnd;
	}
	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}
	public Double getPrizeMax() {
		return prizeMax;
	}
	public void setPrizeMax(Double prizeMax) {
		this.prizeMax = prizeMax;
	}
	public Double getPrizeMin() {
		return prizeMin;
	}
	public void setPrizeMin(Double prizeMin) {
		this.prizeMin = prizeMin;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
}
