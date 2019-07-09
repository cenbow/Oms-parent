package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class SystemRegionInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3237710488556224650L;
	
	private String region_id;
	private String parent_id;
	private String region_name;
	private String region_type;
	private String agency_id ;
	private double shipping_fee;
	private String zip_code;
	private double ems_fee;
	private double cod_fee;
	private String is_cod ;
	private String cod_pos ;
	private String is_cac ;
	private String is_verify_tel;
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getRegion_type() {
		return region_type;
	}
	public void setRegion_type(String region_type) {
		this.region_type = region_type;
	}
	public String getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(String agency_id) {
		this.agency_id = agency_id;
	}
	public double getShipping_fee() {
		return shipping_fee;
	}
	public void setShipping_fee(double shipping_fee) {
		this.shipping_fee = shipping_fee;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public double getEms_fee() {
		return ems_fee;
	}
	public void setEms_fee(double ems_fee) {
		this.ems_fee = ems_fee;
	}
	public double getCod_fee() {
		return cod_fee;
	}
	public void setCod_fee(double cod_fee) {
		this.cod_fee = cod_fee;
	}
	public String getIs_cod() {
		return is_cod;
	}
	public void setIs_cod(String is_cod) {
		this.is_cod = is_cod;
	}
	public String getCod_pos() {
		return cod_pos;
	}
	public void setCod_pos(String cod_pos) {
		this.cod_pos = cod_pos;
	}
	public String getIs_cac() {
		return is_cac;
	}
	public void setIs_cac(String is_cac) {
		this.is_cac = is_cac;
	}
	public String getIs_verify_tel() {
		return is_verify_tel;
	}
	public void setIs_verify_tel(String is_verify_tel) {
		this.is_verify_tel = is_verify_tel;
	}

}
