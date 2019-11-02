package com.work.shop.oms.api.bean;

import java.io.Serializable;

/**
 * 供应商退单状态
 * @author QuYachu
 */
public class OrderReturnForSellers implements Serializable {

	private static final long serialVersionUID = 4702293880981739401L;

	/**
	 * 退单号
	 */
	private String returnSn;

	/**
	 * 退单状态：0未确定、1已确认、4无效、10已完成
	 */
	private Integer returnOrderStatus;

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}
	
	
}
