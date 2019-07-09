package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class DepotModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -546867546202269930L;

	private String depotCode;
	
	private String depotName;
	
	private Integer flag;
	
	private String lgortName;
	
	private String werksName;

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getLgortName() {
		return lgortName;
	}

	public void setLgortName(String lgortName) {
		this.lgortName = lgortName;
	}

	public String getWerksName() {
		return werksName;
	}

	public void setWerksName(String werksName) {
		this.werksName = werksName;
	}
}
