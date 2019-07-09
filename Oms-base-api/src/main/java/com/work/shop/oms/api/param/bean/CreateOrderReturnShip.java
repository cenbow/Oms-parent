package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class CreateOrderReturnShip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String relatingReturnSn;

	private String depotCode;

	private String returnExpress;

	private String returnInvoiceNo;

	private String returnExpressImg;

	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}

	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getReturnExpress() {
		return returnExpress;
	}

	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

	public String getReturnExpressImg() {
		return returnExpressImg;
	}

	public void setReturnExpressImg(String returnExpressImg) {
		this.returnExpressImg = returnExpressImg;
	}
	
	
}
