package com.work.shop.oms.bean;

public class ReturnNumber {

	private String skuSn;
	
	private Integer returnNumber;
	
	private String extensionCode;
	
	private String relatingOrdershipSn;

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public Integer getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(Integer returnNumber) {
		this.returnNumber = returnNumber;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getRelatingOrdershipSn() {
		return relatingOrdershipSn;
	}

	public void setRelatingOrdershipSn(String relatingOrdershipSn) {
		this.relatingOrdershipSn = relatingOrdershipSn;
	}
}
