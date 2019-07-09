package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单出库
 * @author QuYachu
 *
 */
public class OrderOutBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单配送单号
	 */
	private String orderDistribution;
	
	private String siteCode;
	
	/**
	 * 仓库编码
	 */
	private String depotCode;
	
	/**
	 * 订单号
	 */
	private String orderSn;
	
	/**
	 * 联系人
	 */
	private String repName;
	
	private String mobile;
	
	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;
	
	/**
	 * 详细地址
	 */
	private String address;
	
	/**
	 * 出库商品列表
	 */
	private List<OutGoods> outGoodsList;

	public String getOrderDistribution() {
		return orderDistribution;
	}

	public void setOrderDistribution(String orderDistribution) {
		this.orderDistribution = orderDistribution;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OutGoods> getOutGoodsList() {
		return outGoodsList;
	}

	public void setOutGoodsList(List<OutGoods> outGoodsList) {
		this.outGoodsList = outGoodsList;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

}
