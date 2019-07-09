package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ReturnChangeParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3558057059462036849L;
	
	private String actionUser;
	
	private String actionNote;
	
	private String content;
	
	private String siteCode;
	
	private String returnChangeSn;

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getActionNote() {
		return actionNote;
	}

	public void setActionNote(String actionNote) {
		this.actionNote = actionNote;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getReturnChangeSn() {
		return returnChangeSn;
	}

	public void setReturnChangeSn(String returnChangeSn) {
		this.returnChangeSn = returnChangeSn;
	}
}
