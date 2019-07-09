package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class OrderReturnGoodsInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5450177051909866432L;
	
	private int goodsNum;
	private String goodsName;
	private String goodsColor;
	private String goodsSn;
	private String skuSn;
	private String goodsSize;
	private String	goodsUrl;
	private double payPoints;
	private double transactionPrice;
	private String extensionCode;//商品扩展属性

    /**
     * 最小购买量
     */
    private Integer minBuyNum;

    /**
     * 物料编码
     */
    private String customerMaterialCode;

    /**
     * 单位
     */
    private String buyUnit;
	
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
	public double getPayPoints() {
		return payPoints;
	}
	public void setPayPoints(double payPoints) {
		this.payPoints = payPoints;
	}
	public double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public String getExtensionCode() {
		return extensionCode;
	}
	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

    public Integer getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(Integer minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getCustomerMaterialCode() {
        return customerMaterialCode;
    }

    public void setCustomerMaterialCode(String customerMaterialCode) {
        this.customerMaterialCode = customerMaterialCode;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }
}
