package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 退单列表
 * @author QuYachu
 *
 */
public class OrderReturnItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1721995237520233191L;

	private String masterOrderSn; // 订单号
	
	private String outerOrderSn; // 外部订单号
	
	private String userId; // 下单人

	private String receiverName; // 收货人姓名
	
	private String receiverMobile; // 收货人手机
	
	private String receiverTel; // 收货人电话

	private String channelCode; // 平台编码
	
	private String channelName; // 平台名称

	private String shopCode; // 店铺编码
	
	private String shopName; // 店铺名称
	
	private String referer; // 订单来源
	
	private String orderType; // 订单类型

	private Integer orderStatus; // 订单状态

	private Integer payStatus; // 支付状态

	private Integer shipStatus; // 发货状态

	private Integer questionStatus; // 问题单状态
	
	private String addTime; // 下单时间
	
	private Integer goodsCount; // 商品数量

	private Double totalFee; // 订单总金额

	private Double goodsAmount; // 商品总金额
	
	private Double moneyPaid; // 实收金额
	
	private Double discount;
	
	private Double shippingTotalFee;
	
	private String orderFrom;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getOuterOrderSn() {
		return outerOrderSn;
	}

	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Integer shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Integer getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Double getMoneyPaid() {
		return moneyPaid;
	}

	public void setMoneyPaid(Double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Double getShippingTotalFee() {
		return shippingTotalFee;
	}

	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
}