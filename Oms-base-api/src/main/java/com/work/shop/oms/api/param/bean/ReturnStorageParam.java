package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class ReturnStorageParam implements Serializable{

	private static final long serialVersionUID = -4064612111007849102L;

	private String returnSn;//退单号
	
	private String seller;//供销商
	
	private String userName;//用户名
	
	private String depotCode;//仓库编码
	
	private boolean toERP;//

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public boolean isToERP() {
		return toERP;
	}

	public void setToERP(boolean toERP) {
		this.toERP = toERP;
	}
	
}
