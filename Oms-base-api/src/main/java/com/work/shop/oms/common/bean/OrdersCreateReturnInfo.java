package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 批量订单生成返回对象
 * @author lemon
 */
public class OrdersCreateReturnInfo implements Serializable {

	private static final long serialVersionUID = -4918610418844534737L;

	/**
	 * 订单合并支付单号
	 */
	private String mergePaySn;

	/**
	 * 返回结果0：执行不成功；1：成功
	 */
	private int isOk = 0;

	/**
	 * 成功或失败信息
	 */
	private String message;

	/**
	 * 订单结果数据
	 */
	private List<OrderCreateReturnInfo> returnInfos;

	public OrdersCreateReturnInfo() {
	}

	/**
	 * 成功信息
	 * 
	 * @param mergePaySn 合并支付单号
	 * @param message 消息
	 * @param returnInfos 订单结果数据
	 */
	public OrdersCreateReturnInfo(String mergePaySn, String message, List<OrderCreateReturnInfo> returnInfos) {
		super();
		this.mergePaySn = mergePaySn;
		this.isOk = 1;
		this.message = message;
		this.returnInfos = returnInfos;
	}

	/**
	 * 返回消息
	 * @param mergePaySn 合并支付单号
	 * @param message 消息
	 */
	public OrdersCreateReturnInfo(String mergePaySn, String message) {
		super();
		this.mergePaySn = mergePaySn;
		this.isOk = 1;
		this.message = message;
	}

	/**
	 * 失败信息
	 * 
	 * @param message 消息
	 */
	public OrdersCreateReturnInfo(String message) {
		super();
		this.message = message;
		this.isOk = 0;
	}

	/**
	 * 返回信息
	 * @param message 消息
	 * @param returnInfos 订单结果数据
	 */
	public OrdersCreateReturnInfo(String message, List<OrderCreateReturnInfo> returnInfos) {
		super();
		this.message = message;
		this.returnInfos = returnInfos;
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

	public String getMergePaySn() {
		return mergePaySn;
	}

	public void setMergePaySn(String mergePaySn) {
		this.mergePaySn = mergePaySn;
	}

	public List<OrderCreateReturnInfo> getReturnInfos() {
		return returnInfos;
	}

	public void setReturnInfos(List<OrderCreateReturnInfo> returnInfos) {
		this.returnInfos = returnInfos;
	}
}
