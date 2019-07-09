package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoodsTemp;

public class GoodsDistribute implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2557891803530921292L;

	private double scale;
	
	private String supplierCode;
	
	private List<MasterOrderGoodsTemp> goodsTemps;

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public List<MasterOrderGoodsTemp> getGoodsTemps() {
		return goodsTemps;
	}

	public void setGoodsTemps(List<MasterOrderGoodsTemp> goodsTemps) {
		this.goodsTemps = goodsTemps;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
}
