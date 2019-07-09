package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 前台订单付款返回信息
 * @author zcl
 *
 */
public class PayReturnInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3458551704307964247L;

	private int isOk;
	
	private String message;
	
	private String orderSn;
	
	private String paySn;

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

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

}
