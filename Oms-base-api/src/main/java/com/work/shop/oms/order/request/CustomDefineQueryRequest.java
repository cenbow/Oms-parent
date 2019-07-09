package com.work.shop.oms.order.request;

import java.io.Serializable;

public class CustomDefineQueryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115261779726715323L;
	
	private Integer type; // 类型

	private String code; // 编码

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}