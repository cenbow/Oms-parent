package com.work.shop.oms.api.param.bean;


import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.common.bean.LackSkuParam;

public class OrderQuestionParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4785993758091492114L;
	
	private String actionUser;
	
	private String note;
	
	private String orderSn;
	
	private List<LackSkuParam> lackSkuParams;

	private String code;
	
	private int logType;														// 0: 一般问题单;1: 缺货问题单
	
	private String masterOrderSn;
	
	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<LackSkuParam> getLackSkuParams() {
		return lackSkuParams;
	}

	public void setLackSkuParams(List<LackSkuParam> lackSkuParams) {
		this.lackSkuParams = lackSkuParams;
	}

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
	
	private String mainChild;

	public String getMainChild() {
		return mainChild;
	}

	public void setMainChild(String mainChild) {
		this.mainChild = mainChild;
	}

}
