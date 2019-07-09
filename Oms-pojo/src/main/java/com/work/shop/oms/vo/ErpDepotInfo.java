package com.work.shop.oms.vo;

import java.util.List;

public class ErpDepotInfo {
	
	private String code;
	
	private List<ErpStatus> erpStatusList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ErpStatus> getErpStatusList() {
		return erpStatusList;
	}

	public void setErpStatusList(List<ErpStatus> erpStatusList) {
		this.erpStatusList = erpStatusList;
	}
}
