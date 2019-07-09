package com.work.shop.oms.bean;

import java.io.Serializable;

public class OrderDepotShipDetail extends OrderDepotShip implements
		Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8834063845318792841L;
	private String supplierCode;//供应商
	private String shippingName;//承运商
	private String goodsNumber;//商品数量
	private int delivery;//修改承运商权限
	
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public int getDelivery() {
		return delivery;
	}
	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}
	
	
}
