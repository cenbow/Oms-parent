package com.work.shop.oms.core.beans;

public class ProductPrice extends BaseTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String goodsSn;
	
	private Double price;

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
