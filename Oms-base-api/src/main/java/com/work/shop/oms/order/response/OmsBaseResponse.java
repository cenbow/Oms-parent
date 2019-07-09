package com.work.shop.oms.order.response;

import java.io.Serializable;
import java.util.List;

public class OmsBaseResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private T data;
	
	private List<T> list;
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}