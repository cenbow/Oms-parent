package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 区域管理VO
 * @author lyh
 *
 */
public class RegionManagementVO implements Serializable{

	private static final long serialVersionUID = -7075468985126818293L;
	
	/**
	 * 查询form入参
	 */
	private String countryRegion;//国家
	private String provinceRegion;//省份
	private String cityRegion;//城市
	private String districtRegion;//区县
	/**
	 * 查询结果出参
	 */
	private String regionId;//区域ID
	private String parentId;//父区域ID
	private String regionName;//区域名称
	private String regionType;//区域级别[0：国家；1：省份；2：城市；3：区县；4：街道/乡镇]
	private String zipCode;//邮编
	private String shippingFee;//运费
	private String emsFee;//EMS费用
	private String codFee;//货到付款费用
	private String isCod;//是否支持货到付款
	private String codPos;//是否支持POS刷卡
	private String isCac;//是否支持自提
	private String isVerifyTel;//货到付款是否验证手机号[0:不验证;1:验证]
	private String agencyId;//办事处的id,这里有一个bug,同一个省不能有多个办事处,该字段只记录最新的那个办事处的id
	private String lastUpdateTime;//最近一次更新时间
	private String isUpdate;//是否更新
	
	
	
	
	public String getCountryRegion() {
		return countryRegion;
	}
	public void setCountryRegion(String countryRegion) {
		this.countryRegion = countryRegion;
	}
	public String getProvinceRegion() {
		return provinceRegion;
	}
	public void setProvinceRegion(String provinceRegion) {
		this.provinceRegion = provinceRegion;
	}
	public String getCityRegion() {
		return cityRegion;
	}
	public void setCityRegion(String cityRegion) {
		this.cityRegion = cityRegion;
	}
	public String getDistrictRegion() {
		return districtRegion;
	}
	public void setDistrictRegion(String districtRegion) {
		this.districtRegion = districtRegion;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getEmsFee() {
		return emsFee;
	}
	public void setEmsFee(String emsFee) {
		this.emsFee = emsFee;
	}
	public String getCodFee() {
		return codFee;
	}
	public void setCodFee(String codFee) {
		this.codFee = codFee;
	}
	public String getIsCod() {
		return isCod;
	}
	public void setIsCod(String isCod) {
		this.isCod = isCod;
	}
	public String getCodPos() {
		return codPos;
	}
	public void setCodPos(String codPos) {
		this.codPos = codPos;
	}
	public String getIsCac() {
		return isCac;
	}
	public void setIsCac(String isCac) {
		this.isCac = isCac;
	}
	public String getIsVerifyTel() {
		return isVerifyTel;
	}
	public void setIsVerifyTel(String isVerifyTel) {
		this.isVerifyTel = isVerifyTel;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

}
