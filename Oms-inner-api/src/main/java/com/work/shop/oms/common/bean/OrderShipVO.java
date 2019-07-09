package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.bean.OrderShip;




public class OrderShipVO extends OrderShip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -978768694829189293L;

	private String regionName;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
