package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class MobileParams implements Serializable {

	private static final long serialVersionUID = -4449519600834590691L;
	
	private String userId;//用户id
	
	private String isHistory;//是否历史数据（0：否；1：是）
	
	private String orderSn;//订单号
	
	private String returnSn;//退单号
	
	private String skuSn;//商品编码
	
	private int status;//状态(查询详情的时候,1:退单;2:换单;)
	
	private int pageSize;//每页数据数目
	
	private int pageNum;//页码
	
	private String returnType;//退换类型，1：退货；2：换货
	
	private String siteCode;

	public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
}
