package com.work.shop.oms.param.bean;

import java.io.Serializable;

/**
 * 监控实体
 * @author cage
 *
 */
public class OrderMonitor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String returnId;
	
	private String orderId;
	
	private String unitId;
	
	private String prodId;
	
	private Double price;
	
	private Integer orderQty;
	
	private String returnWareId;
	
	private String entryException;
	
	private String returnSettleException;
	
	private String orderSettleException;
	
	private String currentTime;
	
	private String cancelReason;

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public String getReturnWareId() {
		return returnWareId;
	}

	public void setReturnWareId(String returnWareId) {
		this.returnWareId = returnWareId;
	}

	public String getEntryException() {
		return entryException;
	}

	public void setEntryException(String entryException) {
		this.entryException = entryException;
	}

	public String getReturnSettleException() {
		return returnSettleException;
	}

	public void setReturnSettleException(String returnSettleException) {
		this.returnSettleException = returnSettleException;
	}

	public String getOrderSettleException() {
		return orderSettleException;
	}

	public void setOrderSettleException(String orderSettleException) {
		this.orderSettleException = orderSettleException;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	
}
