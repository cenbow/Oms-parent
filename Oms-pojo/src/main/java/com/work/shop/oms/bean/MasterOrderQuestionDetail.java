package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class MasterOrderQuestionDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9059022188346578412L;

	private String masterOrderSn;
	private String questionCode;
	private String questionDesc;
	private Date addTime;
	private String name;
	private String note;
	private Short type;
	public String getMasterOrderSn() {
		return masterOrderSn;
	}
	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
	public String getQuestionCode() {
		return questionCode;
	}
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	public String getQuestionDesc() {
		return questionDesc;
	}
	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}

}
