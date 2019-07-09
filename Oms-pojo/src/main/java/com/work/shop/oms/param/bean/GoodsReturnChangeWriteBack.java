package com.work.shop.oms.param.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.api.bean.OrderReturnSkuInfo;


public class GoodsReturnChangeWriteBack implements Serializable{
	/**有范退货回写状态封装类
	 * 
	 */
	private static final long serialVersionUID = 3645441000853296752L;
	//1不退货退款  2退货退款
	private String returnType;
	private String orderSn;
	private String orderOutSn;
	private double returnShipping;
	private List<OrderReturnSkuInfo> rerurnGoods;
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public List<OrderReturnSkuInfo> getRerurnGoods() {
		return rerurnGoods;
	}
	public void setRerurnGoods(List<OrderReturnSkuInfo> rerurnGoods) {
		this.rerurnGoods = rerurnGoods;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getOrderOutSn() {
		return orderOutSn;
	}
	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}
	public double getReturnShipping() {
		return returnShipping;
	}
	public void setReturnShipping(double returnShipping) {
		this.returnShipping = returnShipping;
	}

}
