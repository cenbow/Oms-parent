package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单发货信息
 * @author QuYachu
 */
public class OrderShipInfo implements Serializable {

	private static final long serialVersionUID = 6362273849349265616L;

	/**
	 * 商品出库时间
	 */
	private String orderShipTime;

	/**
	 * 确认收货时间
	 */
	private String deliveryConfirmTime;

	/**
	 * 快递公司名称
	 */
	private String shippingName;

	/**
	 * 快递公司code
	 */
	private String shippingId;

	/**
	 * 快递单号
	 */
	private String invoiceNo;

	/**
	 * 仓库编码
	 */
	private String depotCode;

	/**
	 * 订单号
	 */
	private String orderSn;

	/**
	 * 店铺来源
	 */
	private String orderFrom;

    /**
     * 配送状态0，未发货；1，已发货；2，已收货；3，备货中；4备货完成，6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁
     */
	private Integer shippingStatus = 0;

    /**
     * 是否延长收货 0否、1是
     */
	private Integer isReceipt;

	/**
	 * 包裹商品集合
	 */
	private List<OrderGoodsInfo> orderGoodsInfo;

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public List<OrderGoodsInfo> getOrderGoodsInfo() {
		return orderGoodsInfo;
	}

	public void setOrderGoodsInfo(List<OrderGoodsInfo> orderGoodsInfo) {
		this.orderGoodsInfo = orderGoodsInfo;
	}

	public String getDeliveryConfirmTime() {
		return deliveryConfirmTime;
	}

	public void setDeliveryConfirmTime(String deliveryConfirmTime) {
		this.deliveryConfirmTime = deliveryConfirmTime;
	}

	public String getOrderShipTime() {
		return orderShipTime;
	}

	public void setOrderShipTime(String orderShipTime) {
		this.orderShipTime = orderShipTime;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }
}
