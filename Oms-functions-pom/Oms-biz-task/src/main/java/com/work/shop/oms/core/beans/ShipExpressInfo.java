package com.work.shop.oms.core.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShipExpressInfo extends BaseTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderSn;
	private String consignee;  //收货人
	
	private String recTel;
	private String recMobile;
	private String provinceId;
	private String provinceName;
	private String cityId;
	private String cityName;
	private String districtId;
	private String districtName;
	
	private String recAddress;
	private String recZipcode;
	private String invoiceNo;//快递单号
	private String weigh;  //重量
	private Integer companyId;  //物流公司ID
	private String  depotCode;  //发货仓或者门店号
	
	private String channelCode;  //下单渠道ID
	private String senderName;
	private String senderPhone;
	private String senderAddress;
	private String senderMobile;
	private String senderProvince;
	private String senderCity;
	private String senderDistrict;
	private String senderZipCode;


	//ship集合
	List<ShipExpressInfo> childShips = new ArrayList<ShipExpressInfo>();
	
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getRecTel() {
		return recTel;
	}
	public void setRecTel(String recTel) {
		this.recTel = recTel;
	}
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public List<ShipExpressInfo> getChildShips() {
		return childShips;
	}
	public void setChildShips(List<ShipExpressInfo> childShips) {
		this.childShips = childShips;
	}
	public String getRecMobile() {
		return recMobile;
	}
	public void setRecMobile(String recMobile) {
		this.recMobile = recMobile;
	}
	public String getRecAddress() {
		return recAddress;
	}
	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}
	public String getRecZipcode() {
		return recZipcode;
	}
	public void setRecZipcode(String recZipcode) {
		this.recZipcode = recZipcode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getWeigh() {
		return weigh;
	}
	public void setWeigh(String weigh) {
		this.weigh = weigh;
	}
	 
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSenderMobile() {
		return senderMobile;
	}
	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}
	public String getSenderProvince() {
		return senderProvince;
	}
	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}
	public String getSenderCity() {
		return senderCity;
	}
	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}
	public String getSenderZipCode() {
		return senderZipCode;
	}
	public void setSenderZipCode(String senderZipCode) {
		this.senderZipCode = senderZipCode;
	}
	public String getSenderDistrict() {
		return senderDistrict;
	}
	public void setSenderDistrict(String senderDistrict) {
		this.senderDistrict = senderDistrict;
	}
	
}
