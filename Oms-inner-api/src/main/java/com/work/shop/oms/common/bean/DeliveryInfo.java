package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class DeliveryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8035664639336939855L;

	private String orderSn;
	
	private String depotCode;
	
	private String carriers;
	
	private String invoiceNo;
	
	private String custumCode;
	
	private Integer goodsNumber;
	
	private String goodsDepot;
	
	private int delivery;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getCarriers() {
		return carriers;
	}

	public void setCarriers(String carriers) {
		this.carriers = carriers;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustumCode() {
		return custumCode;
	}

	public void setCustumCode(String custumCode) {
		this.custumCode = custumCode;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsDepot() {
		return goodsDepot;
	}

	public void setGoodsDepot(String goodsDepot) {
		this.goodsDepot = goodsDepot;
	}

	public int getDelivery() {
		return delivery;
	}

	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}
}
