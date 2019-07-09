package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 承运商管理VO
 * @author lyh
 *
 */
public class ShippingManagementVO implements Serializable {

	private static final long serialVersionUID = -9126083996763650176L;
	/**
	 * 查询结果出参
	 */
	private String shippingId;//承运商ID
	private String shippingCode;//承运商编码
	private String shippingName;//承运商名称
	private String shippingDesc;//承运商描述
	private String insure;//保价费用，单位元，或者是百分数，该值直接输出为报价费用
	private String supportCod;//是否支持货到付款，1，支持；0，不支持
	private String enabled;//该配送方式是否被禁用，1，可用；0，禁用
	private String shippingPrint;//打印模板
	private String shippingPrint2;//货到付款打印模板
	private String isReceivePrint;//是否是货到付款模板
	private String modelImg;//模板图片
	private String defalutDelivery;//是否是默认配送方式
	private String isCommonUse;//是否常用
	
	public String getShippingId() {
		return shippingId;
	}
	public void setShippingId(String shippingId) {
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
	public String getSupportCod() {
		return supportCod;
	}
	public void setSupportCod(String supportCod) {
		this.supportCod = supportCod;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getShippingPrint() {
		return shippingPrint;
	}
	public void setShippingPrint(String shippingPrint) {
		this.shippingPrint = shippingPrint;
	}
	public String getShippingPrint2() {
		return shippingPrint2;
	}
	public void setShippingPrint2(String shippingPrint2) {
		this.shippingPrint2 = shippingPrint2;
	}
	public String getIsReceivePrint() {
		return isReceivePrint;
	}
	public void setIsReceivePrint(String isReceivePrint) {
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
	public String getIsCommonUse() {
		return isCommonUse;
	}
	public void setIsCommonUse(String isCommonUse) {
		this.isCommonUse = isCommonUse;
	}
	
	

}
