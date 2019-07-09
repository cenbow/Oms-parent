package com.work.shop.oms.vo;

import java.io.Serializable;


public class AdminUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 335066890164699159L;
	private String sessionKey;
	private String userId;
	private String userName;
	private StringBuilder roles;
	private String supRole;

	public StringBuilder getRoles() {
		return roles;
	}

	public void setRoles(StringBuilder roles) {
		this.roles = roles;
	}

	public AdminUser() {
	}

	public AdminUser(Object[] o) {
		String sessionKey = "";
		if(o[0]!=null){
			sessionKey = o[0].toString().trim();
		}
		setSessionKey(sessionKey);
		StringBuilder roles = new StringBuilder();
		if(o[1]!=null){
			roles.append(o[1].toString().trim());
		}else{
			roles.append("");
		}
		setRoles(roles);
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSupRole() {
		return supRole;
	}

	public void setSupRole(String supRole) {
		this.supRole = supRole;
	}
	
	
}
