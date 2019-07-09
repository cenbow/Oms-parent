package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;

public class OrderDepotResult implements Serializable {

	private static final long serialVersionUID = -2038097009946301893L;

	/**
	 * 1:SUCCESS; -1:ERROR，接口调用失败; 2：返回调用方异常，OUT_ERROR
	 */
	private int result = -1;
	
	private List<WkUdDistribute> depotList;
	
	private String message;
	
	private String orderSn;
	
	public OrderDepotResult(int result, String message) {
		this.result = result;
		this.message = message;
	}

	public OrderDepotResult() {
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<WkUdDistribute> getDepotList() {
		return depotList;
	}

	public void setDepotList(List<WkUdDistribute> depotList) {
		this.depotList = depotList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	@Override
	public String toString() {
		return "OrderDepotResult [result=" + result + ", depotList="
				+ depotList + ", message=" + message + ", orderSn=" + orderSn
				+ "]";
	}
}
