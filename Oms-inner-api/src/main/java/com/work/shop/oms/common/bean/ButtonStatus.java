package com.work.shop.oms.common.bean;

import java.io.Serializable;



public class ButtonStatus implements Serializable {

	private static final long serialVersionUID = 6820407517603188258L;
	/**
	 * 锁定状态
	 */
	private Integer lockStatus;
	/**
	 * 订单状态
	 */
	private Byte orderStatus;
	/**
	 * 付款状态
	 */
	private Byte payStatus;
	/**
	 * 配送状态
	 */
	private Byte shipStatus;
	/**
	 * 处理状态
	 */
	private Byte processStatus;
	/**
	 * 问题状态
	 */
	private Byte questionStatus;
	/**
	 * 物流问题状态
	 */
	private Byte logQuestionStatus;
	/**
	 * 通知状态
	 */
	private Byte noticeStatus;
	/**
	 * 交易类型
	 */
	private Byte transType;
	/**
	 * 是否占用可销售库存
	 */
	private Integer occAvailable;
	/**
	 * 更新类型
	 */
	private String updateType;
	
	private Integer userId;
	
	private String  userName;
	
	//==================
	//退单锁定状态
	private Byte returnOrderLockStatus;
	//退单状态
	private Byte returnOrderStatus;
	
	private Byte returnPayStatus;
	
	private Byte returnShipStatus;
	
	private Integer chasedOrNot;
	
	private Integer disposition;//处理意见
	
	private Integer returnType;//退单类型：1退货单、2拒收入库单、3退款单
	
	public ButtonStatus() {}

	public ButtonStatus(Integer lockStatus, Byte orderStatus, Byte payStatus,
			Byte shipStatus, Byte processStatus, Byte questionStatus,
			Byte noticeStatus, Byte transType, Integer occAvailable,
			String updateType, Byte logQuestionStatus) {
		this.lockStatus = lockStatus;
		this.orderStatus = orderStatus;
		this.payStatus = payStatus;
		this.shipStatus = shipStatus;
		this.processStatus = processStatus;
		this.questionStatus = questionStatus;
		this.noticeStatus = noticeStatus;
		this.transType = transType;
		this.occAvailable = occAvailable;
		this.updateType = updateType;
		this.logQuestionStatus = logQuestionStatus;
	}
	
	public ButtonStatus(Integer lockStatus, Byte orderStatus, Byte payStatus,
			Byte shipStatus, Byte processStatus, Byte questionStatus,
			Byte noticeStatus, Byte transType, Integer occAvailable,
			String updateType) {
		this.lockStatus = lockStatus;
		this.orderStatus = orderStatus;
		this.payStatus = payStatus;
		this.shipStatus = shipStatus;
		this.processStatus = processStatus;
		this.questionStatus = questionStatus;
		this.noticeStatus = noticeStatus;
		this.transType = transType;
		this.occAvailable = occAvailable;
		this.updateType = updateType;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}
	public Byte getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Byte getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Byte payStatus) {
		this.payStatus = payStatus;
	}
	public Byte getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(Byte shipStatus) {
		this.shipStatus = shipStatus;
	}
	public Byte getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Byte processStatus) {
		this.processStatus = processStatus;
	}
	public Byte getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(Byte questionStatus) {
		this.questionStatus = questionStatus;
	}
	public Byte getNoticeStatus() {
		return noticeStatus;
	}
	public void setNoticeStatus(Byte noticeStatus) {
		this.noticeStatus = noticeStatus;
	}
	public Byte getTransType() {
		return transType;
	}
	public void setTransType(Byte transType) {
		this.transType = transType;
	}
	public Integer getOccAvailable() {
		return occAvailable;
	}
	public void setOccAvailable(Integer occAvailable) {
		this.occAvailable = occAvailable;
	}
	public String getUpdateType() {
		return updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Byte getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Byte returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}

	public Byte getReturnPayStatus() {
		return returnPayStatus;
	}

	public void setReturnPayStatus(Byte returnPayStatus) {
		this.returnPayStatus = returnPayStatus;
	}

	public Byte getReturnShipStatus() {
		return returnShipStatus;
	}

	public void setReturnShipStatus(Byte returnShipStatus) {
		this.returnShipStatus = returnShipStatus;
	}

	public Integer getChasedOrNot() {
		return chasedOrNot;
	}

	public void setChasedOrNot(Integer chasedOrNot) {
		this.chasedOrNot = chasedOrNot;
	}

	public Integer getDisposition() {
		return disposition;
	}

	public void setDisposition(Integer disposition) {
		this.disposition = disposition;
	}

	public Integer getReturnType() {
		return returnType;
	}

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public Byte getReturnOrderLockStatus() {
		return returnOrderLockStatus;
	}

	public void setReturnOrderLockStatus(Byte returnOrderLockStatus) {
		this.returnOrderLockStatus = returnOrderLockStatus;
	}

	public Byte getLogQuestionStatus() {
		return logQuestionStatus;
	}

	public void setLogQuestionStatus(Byte logQuestionStatus) {
		this.logQuestionStatus = logQuestionStatus;
	}
}
