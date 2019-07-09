package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 操作点数bean
 * @author lemon
 *
 */
public class OptionPointsResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8179814989461235939L;

	private String code;										// 返回Code
	
	private String content;										// 流水号
	
	private String msg;											// 返回结果
	
	public OptionPointsResult() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
