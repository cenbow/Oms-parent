package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;

/**
 * 商品交货单信息
 * @author QuYachu
 */
public class GoodsDistribute implements Serializable{

	private static final long serialVersionUID = -2557891803530921292L;

	private double scale;

	/**
	 * 供应商编码
	 */
	private String supplierCode;

    /**
     * 供应商名称
     */
	private String supplierName;

	/**
	 * 发货仓编码
	 */
	private String depotCode;

	/**
	 * 配送时间
	 */
	private String distTime;

	/**
	 * 发货仓编码（多个）
	 */
	private List<String> depotCodes;

	/**
	 * 商品信息
	 */
	private List<MasterOrderGoods> orderGoods;

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public List<MasterOrderGoods> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<MasterOrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public List<String> getDepotCodes() {
		return depotCodes;
	}

	public void setDepotCodes(List<String> depotCodes) {
		this.depotCodes = depotCodes;
	}

	public String getDistTime() {
		return distTime;
	}

	public void setDistTime(String distTime) {
		this.distTime = distTime;
	}

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
