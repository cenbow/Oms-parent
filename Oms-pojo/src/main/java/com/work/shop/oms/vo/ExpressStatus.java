package com.work.shop.oms.vo;

import java.util.List;

public class ExpressStatus {

	private String codenumber;
	
	private String com;
	
	private String companytype;

	private String condition;
	
	private List<ExpressInfo> data;

	public String getCodenumber() {
		return codenumber;
	}

	public void setCodenumber(String codenumber) {
		this.codenumber = codenumber;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<ExpressInfo> getData() {
		return data;
	}

	public void setData(List<ExpressInfo> data) {
		this.data = data;
	}

	public String getCompanytype() {
		return companytype;
	}

	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}
}
