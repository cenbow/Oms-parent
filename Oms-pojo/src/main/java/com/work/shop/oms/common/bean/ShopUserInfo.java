package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShopUserInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8939067638222807654L;

	private Boolean isOk;

	private String message;

	private UserModel userModel;
	
	private DepotModel depotModel;
	
	private AddressModel model;

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public DepotModel getDepotModel() {
		return depotModel;
	}

	public void setDepotModel(DepotModel depotModel) {
		this.depotModel = depotModel;
	}

	public AddressModel getModel() {
		return model;
	}

	public void setModel(AddressModel model) {
		this.model = model;
	}
}