package com.work.shop.oms.bean;

import java.io.Serializable;

public class MasterOrderAddressDetail extends MasterOrderAddressInfo implements
		Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3882583246093379600L;
	private String fullAddress;//详细地址

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	
	

}
