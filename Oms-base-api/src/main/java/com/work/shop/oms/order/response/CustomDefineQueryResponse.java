package com.work.shop.oms.order.response;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.CustomDefine;

public class CustomDefineQueryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private List<CustomDefine> customDefines;
	
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

	public List<CustomDefine> getCustomDefines() {
		return customDefines;
	}

	public void setCustomDefines(List<CustomDefine> customDefines) {
		this.customDefines = customDefines;
	}
}