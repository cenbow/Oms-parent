package com.work.shop.oms.ship.bean;

import java.io.Serializable;

/**
 * 发货信息明细
 * @author QuYachu
 */
public class DistOrderPackageItems implements Serializable{

	private static final long serialVersionUID = -7271426991502693334L;

    /**
     * 商品编码
     */
	private String itemCode;

    /**
     * 商品数量
     */
	private Integer quantity;

    /**
     * 货主编码
     */
	private String ownerCode;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
}