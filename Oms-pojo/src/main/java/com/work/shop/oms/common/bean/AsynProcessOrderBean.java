package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.ValidateOrder;

public class AsynProcessOrderBean implements Serializable {

	private static final long serialVersionUID = 8006844061933538952L;

	private String orderSn;

	private OcpbStatus ocpbStatus;

	private ValidateOrder validateOrder;
	
	private Integer questionType;

	public AsynProcessOrderBean(){}
	
	public AsynProcessOrderBean(String orderSn, OcpbStatus ocpbStatus, ValidateOrder validateOrder, Integer questionType) {
		super();
		this.orderSn = orderSn;
		this.ocpbStatus = ocpbStatus;
		this.validateOrder = validateOrder;
		this.questionType = questionType;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public OcpbStatus getOcpbStatus() {
		return ocpbStatus;
	}

	public void setOcpbStatus(OcpbStatus ocpbStatus) {
		this.ocpbStatus = ocpbStatus;
	}

	public ValidateOrder getValidateOrder() {
		return validateOrder;
	}

	public void setValidateOrder(ValidateOrder validateOrder) {
		this.validateOrder = validateOrder;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}
}
