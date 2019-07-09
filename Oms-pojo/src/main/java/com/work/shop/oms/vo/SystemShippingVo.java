package com.work.shop.oms.vo;

import com.work.shop.oms.bean.SystemShipping;

public class SystemShippingVo extends SystemShipping {
	
	private Double shippingFee;
	
	private Double freeMoney;
	
	private String depotCode;
	
	public Double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public Double getFreeMoney() {
		return freeMoney;
	}

	public void setFreeMoney(Double freeMoney) {
		this.freeMoney = freeMoney;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
}
