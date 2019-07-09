package com.work.shop.oms.vo;

public class DeliveryInfoParam {
	
	private String depotCode;
	
	private Integer shippingId;
	
	private Double shippingTotalFee;
	
	private Integer transType;
	
	private String country;
	
	private String countryName;

	private String province;
	
	private String provinceName;
	
	private String city;
	
	private String cityName;
	
	private String district;
	
	private String districtName;

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Integer getShippingId() {
		return shippingId;
	}

	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
	}

	public Double getShippingTotalFee() {
		return shippingTotalFee;
	}

	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "DeliveryInfoParam [depotCode=" + depotCode + ", shippingId="
				+ shippingId + ", shippingTotalFee=" + shippingTotalFee
				+ ", transType=" + transType + ", country=" + country
				+ ", countryName=" + countryName + ", province=" + province
				+ ", provinceName=" + provinceName + ", city=" + city
				+ ", cityName=" + cityName + ", district=" + district
				+ ", districtName=" + districtName + "]";
	}
}
