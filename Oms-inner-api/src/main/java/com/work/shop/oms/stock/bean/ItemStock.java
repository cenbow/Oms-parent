package com.work.shop.oms.stock.bean;

import java.io.Serializable;

public class ItemStock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4668001303696414060L;

	private String customCode;
	
	private int goodsNumber;

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public int getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
}
