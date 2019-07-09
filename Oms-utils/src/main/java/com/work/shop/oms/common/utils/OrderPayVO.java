package com.work.shop.oms.common.utils;

import com.work.shop.oms.bean.OrderPay;
import com.work.shop.oms.utils.TimeUtil;

public class OrderPayVO extends OrderPay {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2805787180322719656L;

	private String bonusName;
	
	private int pay;
	
	private int unpay;

	private String formatPayTime;
	
	private String formatUserPayTime;
	
	private String formatPayLasttime;
	
	public String getBonusName() {
		return bonusName;
	}

	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
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

	public String getFormatPayTime() {
		if (getPayTime() != null) {
			return TimeUtil.formatDate(getPayTime());
		}
		return null;
	}

	public void setFormatPayTime(String formatPayTime) {
		this.formatPayTime = formatPayTime;
	}

	public String getFormatUserPayTime() {
		if (getUserPayTime() != null) {
			return TimeUtil.formatDate(getUserPayTime());
		}
		return null;
	}

	public void setFormatUserPayTime(String formatUserPayTime) {
		this.formatUserPayTime = formatUserPayTime;
	}

	public String getFormatPayLasttime() {
		if (getPayLasttime() != null) {
			return TimeUtil.formatDate(getPayLasttime());
		}
		return null;
	}

	public void setFormatPayLasttime(String formatPayLasttime) {
		this.formatPayLasttime = formatPayLasttime;
	}
}
