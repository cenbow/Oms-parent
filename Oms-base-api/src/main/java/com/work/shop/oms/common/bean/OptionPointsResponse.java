package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 操作点数bean
 * @author lemon
 *
 */
public class OptionPointsResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8179814989461235939L;

	private Integer isOk;													// 0成功、1失败
	
	private String message;													// 提示信息
	
	private OptionPointsResult result;										
	
	public OptionPointsResponse() {
		super();
	}

	public Integer getIsOk() {
		return isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}

	public OptionPointsResult getResult() {
		return result;
	}

	public void setResult(OptionPointsResult result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
