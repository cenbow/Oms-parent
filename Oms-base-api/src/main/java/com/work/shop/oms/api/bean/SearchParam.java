package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class SearchParam implements Serializable{
	
	/**
	 * 平台app用户升级定时任务使用查询bean
	 */
	private static final long serialVersionUID = -3861334204694497103L;
	private String orderFrom;
	private String orderStatus;
	private String dateStart;
	private String dateEnd;
	private Double minMoneyPaid;
	private Double maxMoneyPaid;
	private Integer level;
	private int pnum;
	private int psize;
	
	private String siteCode;
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public int getPsize() {
		return psize;
	}
	public void setPsize(int psize) {
		this.psize = psize;
	}
	public int getPnum() {
		return pnum;
	}
	public void setPnum(int pnum) {
		this.pnum = pnum;
	}
	public Double getMinMoneyPaid() {
		return minMoneyPaid;
	}
	public void setMinMoneyPaid(Double minMoneyPaid) {
		this.minMoneyPaid = minMoneyPaid;
	}
	public Double getMaxMoneyPaid() {
		return maxMoneyPaid;
	}
	public void setMaxMoneyPaid(Double maxMoneyPaid) {
		this.maxMoneyPaid = maxMoneyPaid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
}
