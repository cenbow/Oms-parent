package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class UserModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3310812697394554087L;

	private String mobile;
	
	private String shopCode;
	
	private String userName;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
