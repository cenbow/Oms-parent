package com.work.shop.oms.channel.bean;

import java.io.Serializable;

/**
 * 物流平台信息
 * @author QuYachu
 *
 */
public class AppAccountOperInfo implements Serializable {

	private static final long serialVersionUID = 8289629178733668209L;

	private String appName;
	
	private String appCode;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
}
