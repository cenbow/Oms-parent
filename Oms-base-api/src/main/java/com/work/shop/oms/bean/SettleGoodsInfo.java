package com.work.shop.oms.bean;

import java.io.Serializable;

public class SettleGoodsInfo implements Serializable {

	private static final long serialVersionUID = 1891478115870418759L;
	
	private String goodssku;//商品sku
	private String goodsname;//商品名称
	private double unitprice;//单价
	private int count;//数量
	private double cost;//成交价 (当商品数量超过1的时候，这个成交价是需要原先的单件商品成交价*数量)
	private double rate;//折扣率
	private Integer bvvalue;
	
	public String getGoodssku() {
		return goodssku;
	}
	public void setGoodssku(String goodssku) {
		this.goodssku = goodssku;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public double getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(double unitprice) {
		this.unitprice = unitprice;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public Integer getBvvalue() {
		return bvvalue;
	}
	public void setBvvalue(Integer bvvalue) {
		this.bvvalue = bvvalue;
	}
}
