package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 出库商品
 * @author QuYachu
 *
 */
public class OutGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sku;
	
	//出库数量
	private Integer amount;
	
	//箱规
	private Integer boxGauge;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}
	
}
