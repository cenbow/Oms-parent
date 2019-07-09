package com.work.shop.oms.order.request;

import java.io.Serializable;

public class OmsBaseRequest<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115261779726715323L;
	
	private String actionUser;
	
	private Integer pageNo; // 页码
	
	private Integer pageSize; // 每页记录数
	
	private T data;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
}