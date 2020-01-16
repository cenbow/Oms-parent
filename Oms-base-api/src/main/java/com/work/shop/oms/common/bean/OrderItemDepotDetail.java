package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderItemDepotDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8834063845318792841L;

    
    private String depotCode;

    private String invoiceNo;

    private String orderSn;

    private Byte shippingStatus;

    private Date deliveryTime;

    private Date pickupDate;

    private Byte shippingId;

    private String shippingName;

    private Integer deliveryType;

    private Date depotTime;

    private Date creatTime;

    private Date updateTime;
 
    private Integer isDel;

    private Date deliveryConfirmTime;
    
	private String supplierCode;//供应商

    //供应商名称
    private String supplierName;
	
	private String goodsNumber;//商品数量

	private String goodsDecimalNumber;//商品数量小数部分
	
	private int delivery;//修改承运商权限

    /**
     * 分仓状态：0，未分仓 1，已分仓未通知 2，已分仓已通知
     */
    private int depotStatus;

    /**
     * 交货单商品
     */
    private List<OrderItemGoodsDetail> goodsDetailList;
	
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public int getDelivery() {
		return delivery;
	}
	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public Byte getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(Byte shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Date getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
	public Byte getShippingId() {
		return shippingId;
	}
	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}
	public Integer getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}
	public Date getDepotTime() {
		return depotTime;
	}
	public void setDepotTime(Date depotTime) {
		this.depotTime = depotTime;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public Date getDeliveryConfirmTime() {
		return deliveryConfirmTime;
	}
	public void setDeliveryConfirmTime(Date deliveryConfirmTime) {
		this.deliveryConfirmTime = deliveryConfirmTime;
	}

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<OrderItemGoodsDetail> getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List<OrderItemGoodsDetail> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
    }

    public int getDepotStatus() {
        return depotStatus;
    }

    public void setDepotStatus(int depotStatus) {
        this.depotStatus = depotStatus;
    }

	public String getGoodsDecimalNumber() {
		return goodsDecimalNumber;
	}

	public void setGoodsDecimalNumber(String goodsDecimalNumber) {
		this.goodsDecimalNumber = goodsDecimalNumber;
	}
}
