package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

public class PayBackInfo implements Serializable{
	
	
	/**
	 * 支付回调消息传输bean
	 */
	private static final long serialVersionUID = -1389411522412850536L;
	List<String> orderSnList;
	/**
	 * 支付单号
	 */
	String paySn;
	String actionNoteIn;
	String actionUser;
	String payNote;
	String payId;
	
	/**
	 * 支付金额
	 */
	double amountToPay;
	
	String from;
	
	public List<String> getOrderSnList() {
		return orderSnList;
	}
	public void setOrderSnList(List<String> orderSnList) {
		this.orderSnList = orderSnList;
	}
	public String getPaySn() {
		return paySn;
	}
	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
	public String getActionNoteIn() {
		return actionNoteIn;
	}
	public void setActionNoteIn(String actionNoteIn) {
		this.actionNoteIn = actionNoteIn;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public String getPayNote() {
		return payNote;
	}
	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public double getAmountToPay() {
		return amountToPay;
	}
	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}
	

}
