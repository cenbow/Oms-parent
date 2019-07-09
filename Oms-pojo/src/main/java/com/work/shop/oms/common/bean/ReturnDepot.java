package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ReturnDepot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8556486009815597519L;

	private String codeCd;
	
	private String codeDisplay;

	public String getCodeCd() {
		return codeCd;
	}

	public void setCodeCd(String codeCd) {
		this.codeCd = codeCd;
	}

	public String getCodeDisplay() {
		return codeDisplay;
	}

	public void setCodeDisplay(String codeDisplay) {
		this.codeDisplay = codeDisplay;
	}
}
