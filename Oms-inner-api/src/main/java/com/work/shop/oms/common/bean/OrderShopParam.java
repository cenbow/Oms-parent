package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderShopParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String shortName;
	private String shortText;
	private String channelCode;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getShortText() {
		return shortText;
	}
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	
}
