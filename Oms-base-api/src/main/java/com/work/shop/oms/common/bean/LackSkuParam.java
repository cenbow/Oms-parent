package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * sku缺货信息
 * @author QuYachu
 */
public class LackSkuParam implements Serializable{

	private static final long serialVersionUID = -7731081221823200491L;

	private String orderSn;

	private String customCode;

	private String depotCode;

	private Short lackNum = 1;

	private String deliverySn;

	private String lackReason;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Short getLackNum() {
		return lackNum;
	}

	public void setLackNum(Short lackNum) {
		this.lackNum = lackNum;
	}

	public String getDeliverySn() {
		return deliverySn;
	}

	public void setDeliverySn(String deliverySn) {
		this.deliverySn = deliverySn;
	}

	public String getLackReason() {
		return lackReason;
	}

	public void setLackReason(String lackReason) {
		this.lackReason = lackReason;
	}

	@Override
	public String toString() {
		return "LackSkuParam [orderSn=" + orderSn + ", customCode="
				+ customCode + ", depotCode=" + depotCode + ", lackNum=" + lackNum + ", deliverySn="
				+ deliverySn + ", lackReason=" + lackReason + "]";
	}
	
	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	private String questionCode;

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	
	private String opType;
	
	private String mainChild;

	public String getMainChild() {
		return mainChild;
	}

	public void setMainChild(String mainChild) {
		this.mainChild = mainChild;
	}
	
}
