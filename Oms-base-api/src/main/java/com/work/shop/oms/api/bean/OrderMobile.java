package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

public class OrderMobile implements Serializable {

	private static final long serialVersionUID = 4100369967559779384L;
	
	private String orderSn;//订单号
	
	private String returnSn;//退单号
	
	private int status;//状态
	
	private double orderPrice;//订单金额
	
	private List<GoodsMobile> goodsMobileList;//订单商品

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}


	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GoodsMobile> getGoodsMobileList() {
		return goodsMobileList;
	}

	public void setGoodsMobileList(List<GoodsMobile> goodsMobileList) {
		this.goodsMobileList = goodsMobileList;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	

}
