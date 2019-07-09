package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单批量处理返回结果值
 * @author
 *
 */
public class BatchReturnInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3154684685072560655L;

	private String orderSn;// 订单编号
	
	private String returnSn;//退单编号
	
	private int isOk;// 返回结果0：执行不成功；1：成功
	
	private String message;// 成功或失败信息
	
	private List<String> failOrdeSnList = new ArrayList<String>(); //批处理失败订单编号
	
	private List<String> successOrderSnList = new ArrayList<String>();  //批处理成功订单编号

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
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

	public List<String> getFailOrdeSnList() {
		return failOrdeSnList;
	}

	public void setFailOrdeSnList(List<String> failOrdeSnList) {
		this.failOrdeSnList = failOrdeSnList;
	}

	public List<String> getSuccessOrderSnList() {
		return successOrderSnList;
	}

	public void setSuccessOrderSnList(List<String> successOrderSnList) {
		this.successOrderSnList = successOrderSnList;
	}
}
