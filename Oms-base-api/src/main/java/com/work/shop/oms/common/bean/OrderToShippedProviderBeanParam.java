package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;


public class OrderToShippedProviderBeanParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 579614030736767497L;

	private String orderSn;
	
	private List<DistributeShipBean> shipBeans;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public List<DistributeShipBean> getShipBeans() {
		return shipBeans;
	}

	public void setShipBeans(List<DistributeShipBean> shipBeans) {
		this.shipBeans = shipBeans;
	}
}