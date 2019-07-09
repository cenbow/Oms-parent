package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 公共接口返回对象
 * @author QuYachu
 */
public class ApiReturnData<T> implements Serializable{

	private static final long serialVersionUID = 2294483844604380480L;

	/**
	 * 是否成功 0失败、1成功
	 */
	private String isOk;

	/**
	 * 提示信息
	 */
	private String message;

	/**
	 * 返回结果
	 */
	private T data;
   
	public String getIsOk() {
		return isOk;
	}
	
	public void setIsOk(String isOk) {
		this.isOk = isOk;
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
}