package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单支付信息
 * @author QuYachu
 */
public class OrderPayInfo implements Serializable{

	private static final long serialVersionUID = 7471450320973956060L;

	/**
	 * 支付单号
	 */
	private String paySn;

	/**
	 * 支付id
	 */
	private int payId;

	/**
	 * 支付名称
	 */
	private String payName;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 支付状态
	 */
	private int payStatus;

	/**
	 * 最后支付时间
	 */
	private String payLastTime;

	/**
	 * 支付总额
	 */
	private double payTotalfee;

	/**
	 * 订单状态
	 */
	private int orderStatus;

	/**
	 * 价格未定 状态
	 */
	private Integer  orderPayPriceNo;

	/**
	 * 特殊业务类型：外部买家铁信支付类型为1（此类型不允许其前端确认支付）
	 */
	private int specialType;
	
	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	public double getPayTotalfee() {
		return payTotalfee;
	}

	public void setPayTotalfee(double payTotalfee) {
		this.payTotalfee = payTotalfee;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayLastTime() {
		return payLastTime;
	}

	public void setPayLastTime(String payLastTime) {
		this.payLastTime = payLastTime;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderPayPriceNo() {
		return orderPayPriceNo;
	}

	public void setOrderPayPriceNo(Integer orderPayPriceNo) {
		this.orderPayPriceNo = orderPayPriceNo;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}
}
