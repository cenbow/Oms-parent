package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShippingInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8955163182932281844L;

	private Byte shippingId;

	private String shippingCode;

	private String shippingName;

	private String shippingDesc;

	private String insure;

	private Integer supportCod;

	private Integer enabled;

	private Integer isReceivePrint;

	private String modelImg;

	private String defalutDelivery;

	private Integer isCommonUse;

	public Byte getShippingId() {
		return shippingId;
	}

	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingDesc() {
		return shippingDesc;
	}

	public void setShippingDesc(String shippingDesc) {
		this.shippingDesc = shippingDesc;
	}

	public String getInsure() {
		return insure;
	}

	public void setInsure(String insure) {
		this.insure = insure;
	}

	public Integer getSupportCod() {
		return supportCod;
	}

	public void setSupportCod(Integer supportCod) {
		this.supportCod = supportCod;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getIsReceivePrint() {
		return isReceivePrint;
	}

	public void setIsReceivePrint(Integer isReceivePrint) {
		this.isReceivePrint = isReceivePrint;
	}

	public String getModelImg() {
		return modelImg;
	}

	public void setModelImg(String modelImg) {
		this.modelImg = modelImg;
	}

	public String getDefalutDelivery() {
		return defalutDelivery;
	}

	public void setDefalutDelivery(String defalutDelivery) {
		this.defalutDelivery = defalutDelivery;
	}

	public Integer getIsCommonUse() {
		return isCommonUse;
	}

	public void setIsCommonUse(Integer isCommonUse) {
		this.isCommonUse = isCommonUse;
	}
}