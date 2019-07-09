package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class SystemRegionMatchInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3175558329556390863L;
	private String countryCode;
	private String provinceCode;
	private String cityCode;
	private String districtCode;
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	

}
