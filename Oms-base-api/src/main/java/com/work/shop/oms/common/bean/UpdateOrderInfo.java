package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateOrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3329223137663911975L;
	private String orderSn;
	private OrderMasterInfo master=new OrderMasterInfo();
	private List<OrderDetailsInfo> detail =new ArrayList<OrderDetailsInfo>();
	
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public OrderMasterInfo getMaster() {
		return master;
	}
	public void setMaster(OrderMasterInfo master) {
		this.master = master;
	}
	public List<OrderDetailsInfo> getDetail() {
		return detail;
	}
	public void setDetail(List<OrderDetailsInfo> detail) {
		this.detail = detail;
	}
	@Override
	public String toString() {
		return "UpdateOrderInfo [orderSn=" + orderSn + ", master=" + master + ", detail=" + detail + "]";
	}
}
