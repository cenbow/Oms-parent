package com.work.shop.oms.api.bean;

import java.io.Serializable;

/**
 * 用于返回请单信息和订单信息的封装类
 * @author Administrator
 *
 */
public class GoodsReturnChangeReturnInfo implements Serializable {

	private static final long serialVersionUID = 8516115911773302433L;

	private int isOK;

	private String message;

	private String returnChangeSn;

	private Integer orderStatus;

	private Integer goodsReturnStatus;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIsOK() {
		return isOK;
	}

	public void setIsOK(int isOK) {
		this.isOK = isOK;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getReturnChangeSn() {
		return returnChangeSn;
	}

	public void setReturnChangeSn(String returnChangeSn) {
		this.returnChangeSn = returnChangeSn;
	}

	public Integer getGoodsReturnStatus() {
		return goodsReturnStatus;
	}

	public void setGoodsReturnStatus(Integer goodsReturnStatus) {
		this.goodsReturnStatus = goodsReturnStatus;
	}

}
