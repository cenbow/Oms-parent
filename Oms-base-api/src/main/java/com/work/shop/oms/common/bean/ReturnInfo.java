package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 公共返回结果对象
 * @author QuYachu
 * @param <T>
 */
public class ReturnInfo<T> implements Serializable{

	private static final long serialVersionUID = -5785052686949245384L;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 外部交易号
	 */
	private String orderOutSn;

	/**
	 * 退单编号
	 */
	private String returnSn;

	/**
	 * 原订单号
	 */
	private String relatingOrderSn;

	/**
	 * 返回结果0：不成功；1：成功
	 */
	private int isOk;

	/**
	 * 成功或失败信息
	 */
	private String message;

	/**
	 * 结果对象
	 */
	private T data;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public ReturnInfo(int isOk, String message) {
		super();
		this.isOk = isOk;
		this.message = message;
	}
	public ReturnInfo() {
	}
	public ReturnInfo(int isOk) {
		super();
		this.isOk = isOk;
	}
	public int getIsOk() {
		return isOk;
	}

	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReturnInfo [orderSn=" + orderSn + ", orderOutSn=" + orderOutSn
				+ ", returnSn=" + returnSn + ", relatingOrderSn="
				+ relatingOrderSn + ", isOk=" + isOk + ", message=" + message
				+ ", data=" + data + "]";
	}
}
