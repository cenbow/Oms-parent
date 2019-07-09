package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付单信息
 */
public class OrderItemPayDetail implements Serializable {

	private static final long serialVersionUID = -7916259317680681643L;

	/**
	 * 订单编码
	 */
	private String masterOrderSn;

	/**
	 * 付款单号
	 */
	private String masterPaySn;

	/**
	 * 付款单支付状态
	 */
	private String payStatus;

	/**
	 * 主付款单支付状态名称
	 */
	private String payStatusName;

	/**
	 * 主付款单总金额
	 */
	private String payTotalfee;

	/**
	 * 订单支付时间
	 */
	private String payTime;

	/**
	 * 付款单生成时间
	 */
	private String createTime;

	/**
	 * 最后更新时间
	 */
	private String updateTime;

	/**
	 * 付款最后期限
	 */
	private String payLasttime;

	/**
	 * 付款方式ID
	 */
	private String payId;

	/**
	 * 付款方式名称
	 */
	private String payName;

	/**
	 * 付款方式备注
	 */
	private String payNote;

	/**
	 * 已付款按钮
	 */
	private int pay;

	/**
	 * 未付款按钮
	 */
	private int unpay;

	/**
	 * 合并支付单号
	 */
	private String mergePaySn;

	/**
	 * 支付期数
	 */
	private int paymentPeriod;

	/**
	 * 支付费率 %
	 */
	private BigDecimal paymentRate;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}
	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
	public String getMasterPaySn() {
		return masterPaySn;
	}
	public void setMasterPaySn(String masterPaySn) {
		this.masterPaySn = masterPaySn;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayTotalfee() {
		return payTotalfee;
	}
	public void setPayTotalfee(String payTotalfee) {
		this.payTotalfee = payTotalfee;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getPayLasttime() {
		return payLasttime;
	}
	public void setPayLasttime(String payLasttime) {
		this.payLasttime = payLasttime;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getPayNote() {
		return payNote;
	}
	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}
	public String getPayStatusName() {
		return payStatusName;
	}
	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public int getUnpay() {
		return unpay;
	}
	public void setUnpay(int unpay) {
		this.unpay = unpay;
	}
	public String getMergePaySn() {
		return mergePaySn;
	}
	public void setMergePaySn(String mergePaySn) {
		this.mergePaySn = mergePaySn==null?"":mergePaySn;
	}

	public int getPaymentPeriod() {
		return paymentPeriod;
	}

	public void setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public BigDecimal getPaymentRate() {
		return paymentRate;
	}

	public void setPaymentRate(BigDecimal paymentRate) {
		this.paymentRate = paymentRate;
	}
}
