package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderQuestionVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2728518004004915063L;
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OrderQuestionVO() {
	}
	
}
