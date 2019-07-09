package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.bean.GoodsReturnChange;

public class GoodsReturnChangeInfoVO extends GoodsReturnChange implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9205277062051844876L;
	
	private String goodsName;
	
	private String goodsSn;
	
	private String colorName;
	
	private String sizeName;
	
	private String custumCode;
	
	private String goodsPrice;
	
	private String transactionPrice;
	
	private String discount;
	
	private Integer goodsNumber;
	
	private String promotionDesc;
	
	private String useCard;

	private String groupPromotionName;
	
	private String extensionId;
	
	private String extensionCode;
	
	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getCustumCode() {
		return custumCode;
	}

	public void setCustumCode(String custumCode) {
		this.custumCode = custumCode;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(String transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	public String getUseCard() {
		return useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard;
	}

	public String getGroupPromotionName() {
		return groupPromotionName;
	}

	public void setGroupPromotionName(String groupPromotionName) {
		this.groupPromotionName = groupPromotionName;
	}
}
