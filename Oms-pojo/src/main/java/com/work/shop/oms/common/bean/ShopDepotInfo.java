package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

public class ShopDepotInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8939067638222807654L;

	private Boolean isOk;

	private String message;

	private List<ReturnDepot> list;

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ReturnDepot> getList() {
		return list;
	}

	public void setList(List<ReturnDepot> list) {
		this.list = list;
	}
}