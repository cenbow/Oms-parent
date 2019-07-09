package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class AddressModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7183954750862536350L;

	private String addressCode;
	
	private String addressName;
	
	private Integer isDefault;

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
}
