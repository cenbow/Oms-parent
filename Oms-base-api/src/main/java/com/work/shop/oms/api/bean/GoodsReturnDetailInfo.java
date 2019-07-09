package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class GoodsReturnDetailInfo implements Serializable{
	
	/**
	 * 平台退换货申请单详情
	 */
	private static final long serialVersionUID = -6985878969612408408L;
	
	/**
	 * 订单编码
	 */
	private String orderSn;
	
	/**
	 * 下单时间
	 */
	private String orderCreateTime;
	
	/**
	 * 收货人手机号码
	 */
	private String mobile;
	
	private String tel;
	
	/**
	 * 收货人名称
	 */
	private String receiver;
	
	/**
	 * 配送地址
	 */
	private String shippingAddress;
	
	/**
	 * 退款金额
	 */
	private Double returnTotalFee;
	
	/**
	 * 退款原因
	 */
	private String returnReason;
	
	/**
	 * 退单状态 0未确定、1已确认、4无效、10已完成
	 */
	private Integer returnOrderStatus;
	
	/**
	 * 1 等待付款、2等待发货、3等待收货、4确认收货、5订单取消
	 */
	private String totalOrderStatus;
	
	private List<OrderGoodsInfo> goodsInfoList;
	
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public List<OrderGoodsInfo> getGoodsInfoList() {
		return goodsInfoList;
	}
	public void setGoodsInfoList(List<OrderGoodsInfo> goodsInfoList) {
		this.goodsInfoList = goodsInfoList;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getTotalOrderStatus() {
		return totalOrderStatus;
	}
	public void setTotalOrderStatus(String totalOrderStatus) {
		this.totalOrderStatus = totalOrderStatus;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Double getReturnTotalFee() {
		return returnTotalFee;
	}
	public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}
	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}
	


}
