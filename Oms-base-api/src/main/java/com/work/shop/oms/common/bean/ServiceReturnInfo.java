package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 接口返回结果
 * @author
 *
 * @param <T>
 */
public class ServiceReturnInfo<T> implements Serializable {

	private static final long serialVersionUID = -953007018370504719L;

	/**
	 * 接口调用时候成功
	 */
	private boolean isok;

	/**
	 * 接口返回提示信息
	 */
	private String message;

	/**
	 * 接口返回类型 T一定是实现序列化接口的类或者 泛型为可序列化的接口
	 */
	private T result;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

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


	public ServiceReturnInfo(boolean isok, String message, T t) {
		super();
		this.isok = isok;
		this.message = message;
		this.result = t;
	}

	public ServiceReturnInfo() {}

	/**
	 * 接口调用成功
	 * 
	 * @param t
	 */
	public ServiceReturnInfo(T t) {
		this(true, "", t);
	}
	
	/**
	 * 接口调用失败
	 * @param message 失败原因
	 */
	public ServiceReturnInfo(String message) {
		this(false, message, null);
	}

	@Override
	public String toString() {
		return "ServiceReturnInfo [isok=" + isok + ", message=" + message + ", result=" + result + "]";
	}
}
