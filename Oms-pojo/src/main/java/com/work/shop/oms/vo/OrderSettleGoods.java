package com.work.shop.oms.vo;

import java.io.Serializable;


/**
 * 订单结算 - 商品列表
 * @author huangl
 *
 */
public class OrderSettleGoods  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String prodId;//11位编码
	private double unitPrice;//市场价
	private Double quantity;//商品数量
	private String sellerId;//导购
	private String locId;//货位
	private double amount;//市场价*数量
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getLocId() {
		return locId;
	}
	public void setLocId(String locId) {
		this.locId = locId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
