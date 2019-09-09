package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单退金额
 * @author QuYachu
 *
 */
public class OrderReturnMoneyBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 站点编码
	 */
	private String siteCode;
	
	/**
	 * 订单编码
	 */
	private String orderSn;
	
	/**
	 * 下单人
	 */
	private String userId;
	
	/**
	 * 退单编码
	 */
	private String returnPaySn;
	
	/**
	 * 退款金额
	 */
	private String returnOrderAmount;

	/**
	 * 退款金额
	 */
	private BigDecimal returnFee;

	/**
	 * 退款状体
	 */
	private int backBalance;

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReturnPaySn() {
		return returnPaySn;
	}

	public void setReturnPaySn(String returnPaySn) {
		this.returnPaySn = returnPaySn;
	}

	public String getReturnOrderAmount() {
		return returnOrderAmount;
	}

	public void setReturnOrderAmount(String returnOrderAmount) {
		this.returnOrderAmount = returnOrderAmount;
	}

	public BigDecimal getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(BigDecimal returnFee) {
		this.returnFee = returnFee;
	}

	public int getBackBalance() {
		return backBalance;
	}

	public void setBackBalance(int backBalance) {
		this.backBalance = backBalance;
	}
}
