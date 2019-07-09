package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class JsonResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252624739225311546L;

	/** 结果code */
	private boolean isok = true;// 成功标示

	/** 结果返回消息，默认为空字符 */
	private String message = "";

	private Integer totalProperty;

	private Object data;

	public boolean isIsok() {
		return isok;
	}

	public void setIsok(boolean isok) {
		this.isok = isok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
