package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class OrderPayGoodsInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6214563245952738370L;
	
	
	private int goodsNum;
	private String goodsName;
	private String goodsColor;
	private String goodsSn;
	private String skuSn;
	private String goodsSize;
	private String	goodsUrl;
	private double	discountedPrice;
	private double	goodsPrice;
	private double transactionPrice;
	private double settlementPrice;
	private int isGift;
	private String extensionCode;//商品扩展属性
	private double payPoints;
	private int sendNumber;//占用库存
	private double integralMoney;//使用的积分金额
	private int integral;//使用的积分
	private String c2mItemStr;
	private String orderFrom;
	private String supplierCode;
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsColor() {
		return goodsColor;
	}
	public void setGoodsColor(String goodsColor) {
		this.goodsColor = goodsColor;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}
	public String getGoodsSize() {
		return goodsSize;
	}
	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public double getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	public int getIsGift() {
		return isGift;
	}
	public void setIsGift(int isGift) {
		this.isGift = isGift;
	}
	public String getExtensionCode() {
		return extensionCode;
	}
	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}
	public double getPayPoints() {
		return payPoints;
	}
	public void setPayPoints(double payPoints) {
		this.payPoints = payPoints;
	}
	public int getSendNumber() {
		return sendNumber;
	}
	public void setSendNumber(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	public double getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(double integralMoney) {
		this.integralMoney = integralMoney;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public String getC2mItemStr() {
		return c2mItemStr;
	}
	public void setC2mItemStr(String c2mItemStr) {
		this.c2mItemStr = c2mItemStr;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	
}
