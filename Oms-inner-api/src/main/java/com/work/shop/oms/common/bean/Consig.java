package com.work.shop.oms.common.bean;
import java.io.Serializable;


/**
 * 收货人相关信息
 * @author
 *
 */
public class Consig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2254910116412684027L;
	/**
	 * 收货人
	 */
	private String name;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 地址
	 */
	private String add;
	/**
	 * 邮编
	 */
	private String code;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 手机号
	 */
	private String mbl;
	/**
	 * 标志性建筑
	 */
	private String sign;
	/**
	 * 最近送货时间
	 */
	private String bestTime;
	
	private String userId;
	
	/**
	 * 国家id
	 */
	private int countryId;
	private String countryName;
	/**
	 * 省份id
	 */
	private int provinceId;
	private String provinceName;
	/**
	 * 城市id
	 */
	private int cityId;
	private String cityName;
	/**
	 * 地区id
	 */
	private int areaId;
	private String areaName;
	
	public Consig() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMbl() {
		return mbl;
	}
	public void setMbl(String mbl) {
		this.mbl = mbl;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBestTime() {
		return bestTime;
	}
	public void setBestTime(String bestTime) {
		this.bestTime= bestTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
}
