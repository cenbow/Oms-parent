package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单配送商品信息
 * @author QuYachu
 *
 */
public class OrderShipGoodsItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单号
	 */
	private String masterOrderSn;
	
	/**
	 * 订单配送单号
	 */
	private String orderSn;
	
	/**
	 * 渠道编码
	 */
	private String channelCode;
	
	/**
	 * 渠道店铺编码
	 */
	private String shopCode;
	
	/**
	 * 下单时间
	 */
	private Date addTime;
	
	/**
	 * 下单人
	 */
	private String userId;
	
	/**
	 * 订单状态
	 */
	private Integer orderStatus;
	
	/**
	 * 收货人姓名
	 */
	private String receiverName;
	
	/**
	 * 收货人手机号码
	 */
	private String receiverMobile;
	
	/**
	 * 配送状态
	 */
	private Integer shipStatus;
	
	/**
	 * 配送出库时间
	 */
	private Date deliveryTime;
	
	/**
	 * 配送完成时间
	 */
	private Date deliveryConfirmTime;
	
	/**
	 * 仓库编码
	 */
	private String  depotCode;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * sku
	 */
	private String customCode;
	
	/**
	 * 货号
	 */
	private String goodsSn;
	
	/**
	 * 条码
	 */
	private String sap;
	
	/**
	 * 箱规
	 */
	private Integer boxGauge;
	
	/**
	 * 商品数量
	 */
	private Integer goodsNumber;

	/**
	 * 商品销售类型  0 正常商品 1 非标定制
	 */
	private Integer saleType;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public Date getAddTime() {
		return addTime;
	}

	public String getUserId() {
		return userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getCustomCode() {
		return customCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public String getSap() {
		return sap;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public Date getDeliveryConfirmTime() {
		return deliveryConfirmTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public void setDeliveryConfirmTime(Date deliveryConfirmTime) {
		this.deliveryConfirmTime = deliveryConfirmTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public Integer getShipStatus() {
		return shipStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setShipStatus(Integer shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
}
