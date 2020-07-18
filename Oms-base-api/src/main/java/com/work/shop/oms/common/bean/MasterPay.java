package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付单
 * @author QuYachu
 */
public class MasterPay implements Serializable{

	private static final long serialVersionUID = 2625829109312426963L;

	/**
	 * 支付方式id
	 */
	private Integer payId;

	/**
	 * 支付方式
	 */
	private String payCode;

	/**
	 * 该订单使用余额的数量
	 */
	private Double surplus;

	/**
	 * 付款备注
	 */
	private String payNote;

	/**
	 * 付款单总金额
	 */
	private Double payTotalFee;

	/**
	 * 付款单支付状态
	 */
	private Integer payStatus;

	/**
	 * 订单预付时间
	 */
	private Date payTime;

	/**
	 * 订单预付时间
	 */
	private Date prepayTime;

	/**
	 * 期数
	 */
	private Integer paymentPeriod;

	/**
	 * 支付费率(%)
	 */
	private Double paymentRate;

    /**
     * 团购预付款金额
     */
    private BigDecimal prepayments;

    public BigDecimal getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(BigDecimal prepayments) {
        this.prepayments = prepayments;
    }

    public Integer getPayId() {
		return payId==null?0:payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Double getSurplus() {
		return surplus==null?0.00:surplus;
	}

	public void setSurplus(Double surplus) {
		this.surplus = surplus;
	}

	public String getPayNote() {
		return payNote==null?"":payNote;
	}

	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}

	public Double getPayTotalFee() {
		return payTotalFee==null?0.00:payTotalFee;
	}

	public void setPayTotalFee(Double payTotalFee) {
		this.payTotalFee = payTotalFee;
	}
	
	public Integer getPayStatus() {
		return payStatus==null ? 0 : payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getPrepayTime() {
		return prepayTime;
	}

	public void setPrepayTime(Date prepayTime) {
		this.prepayTime = prepayTime;
	}
	
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Integer getPaymentPeriod() {
		return paymentPeriod == null ? 0 : paymentPeriod;
	}

	public void setPaymentPeriod(Integer paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public Double getPaymentRate() {
		return paymentRate == null ? 0.0D : paymentRate;
	}

	public void setPaymentRate(Double paymentRate) {
		this.paymentRate = paymentRate;
	}
}
