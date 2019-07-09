package com.work.shop.oms.bean;
import java.util.List;


public class ShippingOrg {

	private boolean orgOk;
	private String msg;
	private List<TrPriceInfo> priceInfos;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<TrPriceInfo> getPriceInfos() {
		return priceInfos;
	}
	public void setPriceInfos(List<TrPriceInfo> priceInfos) {
		this.priceInfos = priceInfos;
	}
	public ShippingOrg() {
	}
	public boolean getOrgOk() {
		return orgOk;
	}
	public void setOrgOk(boolean orgOk) {
		this.orgOk = orgOk;
	}
}
