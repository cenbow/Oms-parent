package com.work.shop.oms.common.bean;

import com.work.shop.oms.common.bean.OrderToShippedProviderBean;


public class OrderToShippedProviderBean {
	
	@Override
	public String toString() {
		return "OrderToShippedProviderBean [id=" + id + ", outer_code="
				+ outer_code + ", code=" + code + ", bf_org_wareh_id="
				+ bf_org_wareh_id + ", dist_wareh_code=" + dist_wareh_code
				+ ", region_id=" + region_id + ", ttl_qty=" + ttl_qty
				+ ", bf_org_tsp_com_id=" + bf_org_tsp_com_id + ", ship_code="
				+ ship_code + ", csb_num=" + csb_num + ", ship_date="
				+ ship_date + ", create_date=" + create_date
				+ ", last_modified_date=" + last_modified_date
				+ ", major_send_wareh_code=" + major_send_wareh_code
				+ ", ship_type=" + ship_type + ", goodsNumber=" + goodsNumber
				+ ", SKU=" + SKU + "]";
	}
	private String id ;
	private String outer_code ;
	private String code ;
	private String bf_org_wareh_id ;
	private String dist_wareh_code ;
	private String region_id ;
	private String ttl_qty ;
	private String bf_org_tsp_com_id ;
	private String ship_code ;
	private String csb_num ;
	private String ship_date ;
	private String create_date ;
	private String last_modified_date ;
	private String major_send_wareh_code ;
	private String ship_type ;
	private String goodsNumber ;
	private String SKU ;
	   
	

	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getSKU() {
		return SKU;
	}
	public void setSKU(String sKU) {
		SKU = sKU;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOuter_code() {
		return outer_code;
	}
	public void setOuter_code(String outer_code) {
		this.outer_code = outer_code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBf_org_wareh_id() {
		return bf_org_wareh_id;
	}
	public void setBf_org_wareh_id(String bf_org_wareh_id) {
		this.bf_org_wareh_id = bf_org_wareh_id;
	}
	public String getDist_wareh_code() {
		return dist_wareh_code;
	}
	public void setDist_wareh_code(String dist_wareh_code) {
		this.dist_wareh_code = dist_wareh_code;
	}
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getTtl_qty() {
		return ttl_qty;
	}
	public void setTtl_qty(String ttl_qty) {
		this.ttl_qty = ttl_qty;
	}
	public String getBf_org_tsp_com_id() {
		return bf_org_tsp_com_id;
	}
	public void setBf_org_tsp_com_id(String bf_org_tsp_com_id) {
		this.bf_org_tsp_com_id = bf_org_tsp_com_id;
	}
	public String getShip_code() {
		return ship_code;
	}
	public void setShip_code(String ship_code) {
		this.ship_code = ship_code;
	}
	public String getCsb_num() {
		return csb_num;
	}
	public void setCsb_num(String csb_num) {
		this.csb_num = csb_num;
	}
	public String getShip_date() {
		return ship_date;
	}
	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(String last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public String getMajor_send_wareh_code() {
		return major_send_wareh_code;
	}
	public void setMajor_send_wareh_code(String major_send_wareh_code) {
		this.major_send_wareh_code = major_send_wareh_code;
	}
	public String getShip_type() {
		return ship_type;
	}
	public void setShip_type(String ship_type) {
		this.ship_type = ship_type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((major_send_wareh_code == null) ? 0 : major_send_wareh_code.hashCode());
		result = prime * result + ((outer_code == null) ? 0 : outer_code.hashCode());
		result = prime * result + ((ship_code == null) ? 0 : ship_code.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderToShippedProviderBean other = (OrderToShippedProviderBean) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (major_send_wareh_code == null) {
			if (other.major_send_wareh_code != null)
				return false;
		} else if (!major_send_wareh_code.equals(other.major_send_wareh_code))
			return false;
		if (outer_code == null) {
			if (other.outer_code != null)
				return false;
		} else if (!outer_code.equals(other.outer_code))
			return false;
		if (ship_code == null) {
			if (other.ship_code != null)
				return false;
		} else if (!ship_code.equals(other.ship_code))
			return false;
		return true;
	}
	 

				  
	  
	  
	  
}