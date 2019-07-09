package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class OrderQuestionNewDetail  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1970484597748992359L;
	private String orderSn;//交货单号
	private String questionCode;//问题单编码
	private String questionDesc;//问题单描述
	private Integer questionType;//问题单状态：0.一般问题单，1.缺货问题单
	private String supplierOrderSn;//供应商工单号
	private Date addTime;//记录添加时间
	private String name;//问题单类型名称
	private String note;//问题单类型描述
	private Short type;//order_custom_define.type
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
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
	public Integer getQuestionType() {
		return questionType;
	}
	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}
	public String getSupplierOrderSn() {
		return supplierOrderSn;
	}
	public void setSupplierOrderSn(String supplierOrderSn) {
		this.supplierOrderSn = supplierOrderSn;
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
