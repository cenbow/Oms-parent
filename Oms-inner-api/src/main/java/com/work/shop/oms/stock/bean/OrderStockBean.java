package com.work.shop.oms.stock.bean;

import java.io.Serializable;
import java.util.List;

public class OrderStockBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5908049423010804804L;
	
	private String shopCode; // 店铺编码
	
	private String orderOutSn; // 平台单号
	
	private String orderSn; // oms单号
	
	private List<ItemStock> itemStocks; // 订单商品

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public List<ItemStock> getItemStocks() {
		return itemStocks;
	}

	public void setItemStocks(List<ItemStock> itemStocks) {
		this.itemStocks = itemStocks;
	}
}
