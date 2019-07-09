package com.work.shop.oms.vo;

import java.io.Serializable;

public class ReturnPaymentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6527708365688356738L;

	private Byte payId;

	private String payCode;

	private String payName;
	
	private boolean selected;

	public Byte getPayId() {
		return payId;
	}

	public void setPayId(Byte payId) {
		this.payId = payId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}