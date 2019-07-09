package com.work.shop.oms.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.work.shop.oms.common.bean.C2bGood;

public class OrderDetailsInfo {
	private String prodNum ;										// 商品编码

	private String dwarhCode;										// 实际仓

	private int count;												// 变化量

	private Double unitPrice;										// 单价

	private Double aturePrice;										// 成交价
	
	private Double settlementPrice;									// 财务价

	private Double discRate;										// 折扣

	private Double amount;											// 金额
	
	private List<C2bGood> c2bgoodList = new ArrayList<C2bGood>();	// C2B商品列表
	
	public String getProdNum() {
		return prodNum;
	}
	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	public String getDwarhCode() {
		return dwarhCode;
	}
	public void setDwarhCode(String dwarhCode) {
		this.dwarhCode = dwarhCode;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getAturePrice() {
		return aturePrice;
	}
	public void setAturePrice(Double aturePrice) {
		this.aturePrice = aturePrice;
	}
	public Double getDiscRate() {
		return discRate;
	}
	public void setDiscRate(Double discRate) {
		this.discRate = discRate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public List<C2bGood> getC2bgoodList() {
		return c2bgoodList;
	}
	public void setC2bgoodList(List<C2bGood> c2bgoodList) {
		this.c2bgoodList = c2bgoodList;
	}
	public Double getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	@Override
	public String toString() {
		return "OrderDetailsInfo [prodNum=" + prodNum + ", dwarhCode="
				+ dwarhCode + ", count=" + count + ", unitPrice=" + unitPrice
				+ ", aturePrice=" + aturePrice + ", settlementPrice="
				+ settlementPrice + ", discRate=" + discRate + ", amount="
				+ amount + ", c2bgoodList=" + c2bgoodList + "]";
	}
}
