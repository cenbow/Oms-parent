package com.work.shop.oms.bean;

import java.io.Serializable;

public class CustomDefine implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -244546323279752247L;

	private String code; // 编码
	
	private String name; // 名称
	
	private Integer type; // 类型

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}