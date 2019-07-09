package com.work.shop.oms.channel.bean;

import java.io.Serializable;

public class StoreOperInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7267107175037739020L;

	private String shopCode; // 店铺编码
	
	private String shopTitle; // 店铺名称
	
	private String channelCode; // 渠道编码
	
	private Integer shopStatus; // 店铺状态 0: 未激活，1: 已激活
	
	private Integer isSyn; // 是否同步库存 0:否;1:是

	private String backup; // 备注
	
	private String shopType; // 店铺类型 0:自营 1：加盟
	
	private Integer shopChannel; // 0集团店铺，1外部渠道店铺
	
	private String parentShopCode; // 父店铺编号
	
	private String tmsCode; // 配送物流编码
	
	private String tmsShopCode; // 物流店铺编码
	
	private String areaCode; // 地区编码
	
	private Integer depotType; // 仓库类型 1单仓配发、2仓库共享、3多仓配发
	
	private Double longitude; // 经度
	
	private Double latitude; // 纬度
	
	private Integer areaType; // 配送类型（0：圆形配送，1：围栏配送）
	
	private Double scope; // 配送范围（公里）
	
	private String scopeArea; // 配送区域(多个经纬度的围栏)
	
	private String country; // 国家
	
	private String province; // 省
	
	private String city; // 市
	
	private String district; // 区域
	
	private String shopAddress; // 门店地址
	
	private String shopTel; // 联系电话
	
	private String shopImg; // 门店图标
	
	private Double distance; // 距离
 
	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getShopStatus() {
		return shopStatus == null ? 0 : shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public String getBackup() {
		return backup;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}

	public String getShopType() {
		return shopType == null ? "0" : shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getParentShopCode() {
		return parentShopCode;
	}

	public void setParentShopCode(String parentShopCode) {
		this.parentShopCode = parentShopCode;
	}

	public String getTmsCode() {
		return tmsCode;
	}

	public void setTmsCode(String tmsCode) {
		this.tmsCode = tmsCode;
	}

	public String getTmsShopCode() {
		return tmsShopCode;
	}

	public void setTmsShopCode(String tmsShopCode) {
		this.tmsShopCode = tmsShopCode;
	}

	public Integer getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(Integer isSyn) {
		this.isSyn = isSyn;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getDepotType() {
		return depotType;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Integer getAreaType() {
		return areaType;
	}

	public Double getScope() {
		return scope;
	}

	public String getScopeArea() {
		return scopeArea;
	}

	public String getCountry() {
		return country;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDepotType(Integer depotType) {
		this.depotType = depotType;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}

	public void setScope(Double scope) {
		this.scope = scope;
	}

	public void setScopeArea(String scopeArea) {
		this.scopeArea = scopeArea;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public String getShopTel() {
		return shopTel;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getShopChannel() {
		return shopChannel;
	}

	public void setShopChannel(Integer shopChannel) {
		this.shopChannel = shopChannel;
	}
}