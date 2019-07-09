package com.work.shop.oms.ship.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 物流发货信息
 * @author QuYachu
 */
public class DistOrderPackages implements Serializable {

	private static final long serialVersionUID = -7429505006804451992L;

	/**
	 * 物流公司编码
	 */
	private String logisticsCode;

	/**
	 * 物流公司名称
	 */
	private String logisticsName;

	/**
	 * 运单号
	 */
	private String expressCode;

	/**
	 * 发货时间
	 */
	private Date deliveryTime;

	/**
	 * 商品信息
	 */
	private List<DistOrderPackageItems> items;
	
	private Map<String, String> ownerCodeMap;

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	
	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public List<DistOrderPackageItems> getItems() {
		return items;
	}

	public void setItems(List<DistOrderPackageItems> items) {
		this.items = items;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public Map<String, String> getOwnerCodeMap() {
		return ownerCodeMap;
	}

	public void setOwnerCodeMap(Map<String, String> ownerCodeMap) {
		this.ownerCodeMap = ownerCodeMap;
	}
}