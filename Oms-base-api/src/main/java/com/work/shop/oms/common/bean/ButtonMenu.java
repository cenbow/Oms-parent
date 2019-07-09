package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 按钮菜单
 * @author QuYachu
 */
public class ButtonMenu implements Serializable {

	private static final long serialVersionUID = 3277385256021152812L;

	private String name;
	
	private String action;
	
	private String authCode;

	/**
	 * 默认构造函数
	 */
	public ButtonMenu() {

	}
	
	public ButtonMenu(String name, String action) {
		this.name = name;
		this.action = action;
	}
	
	public ButtonMenu(String name, String action, String authCode) {
		this.name = name;
		this.action = action;
		this.authCode = authCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthValue(String authCode) {
		this.authCode = authCode;
	}
}