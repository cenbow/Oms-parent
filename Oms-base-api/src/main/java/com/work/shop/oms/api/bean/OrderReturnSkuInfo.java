package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class OrderReturnSkuInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5964418570509917949L;
	private	String skuSn;
    private Integer goodsNum;
    private Double goodsPrice;
	public Integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

}
