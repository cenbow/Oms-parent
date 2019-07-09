package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ChannelWarehouseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2749245430817877090L;
	
	private String outOrderSn;
	private String masterOrderSn;
	private List<String> skuList;
	private Date ctime; 
	private String depotCode;
	private String shopcode;
	private String cstatus;
	public String getOutOrderSn() {
		return outOrderSn;
	}
	public void setOutOrderSn(String outOrderSn) {
		this.outOrderSn = outOrderSn;
	}
	public String getMasterOrderSn() {
		return masterOrderSn;
	}
	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
	public List<String> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getShopcode() {
		return shopcode;
	}
	public void setShopcode(String shopcode) {
		this.shopcode = shopcode;
	}
	public String getCstatus() {
		return cstatus;
	}
	public void setCstatus(String cstatus) {
		this.cstatus = cstatus;
	}

}
