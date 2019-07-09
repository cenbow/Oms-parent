package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 操作点数bean
 * @author lemon
 *
 */
public class OptionPointsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8179814989461235939L;

	private String userId;											// 用户id 必须
	
	private Double orderAmount;										//订单金额 必须
	
	private String orderNo;											//订单编号 必须
	
	private Double orderPoints;										//订单使用点数 必须
	
	private Integer orderBv;										//订单bv 必须
	
	private int orderType;											//操作类型  1冻结资金、2释放资金、3消费资金、4退返资金
	
	public OptionPointsBean(String userId, Double orderAmount, String orderNo,
			Double orderPoints, Integer orderBv, int orderType) {
		this.userId = userId;
		this.orderAmount = orderAmount;
		this.orderNo = orderNo;
		this.orderPoints = orderPoints;
		this.orderBv = orderBv;
		this.orderType = orderType;
	}
	
	public OptionPointsBean() {
		super();
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Double getOrderPoints() {
		return orderPoints;
	}
	public void setOrderPoints(Double orderPoints) {
		this.orderPoints = orderPoints;
	}
	public Integer getOrderBv() {
		return orderBv;
	}
	public void setOrderBv(Integer orderBv) {
		this.orderBv = orderBv;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
