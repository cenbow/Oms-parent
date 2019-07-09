package com.work.shop.oms.api.ship.bean;

import java.io.Serializable;

public class ApiChannelGoods implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3579887304847821998L;

	private String shopCode;
	
	private String sku;
	
	private Byte isOnSell;
	
	private Integer isSyn;

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Byte getIsOnSell() {
		return isOnSell;
	}

	public void setIsOnSell(Byte isOnSell) {
		this.isOnSell = isOnSell;
	}

	public Integer getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(Integer isSyn) {
		this.isSyn = isSyn;
	}

}
