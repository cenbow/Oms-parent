package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductGoodsCache implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String goodsSn;

    private String goodsName;
   
    private Integer catId;

    private String brandCode;

    private BigDecimal marketPrice;

    private BigDecimal protectPrice;

    private String goodsThumb;

    private String goodsImg;

    private String originalImg;

    private String sizePicture;

    private String cardPicture;

    private String originalBrand;

    private String sellerCode;
    
    private byte salesMode;

	public byte getSalesMode() {
		return salesMode;
	}


	public void setSalesMode(byte salesMode) {
		this.salesMode = salesMode;
	}


	public String getGoodsSn() {
		return goodsSn;
	}


	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}


	public String getGoodsName() {
		return goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	public Integer getCatId() {
		return catId;
	}


	public void setCatId(Integer catId) {
		this.catId = catId;
	}


	public String getBrandCode() {
		return brandCode;
	}


	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}


	public BigDecimal getMarketPrice() {
		return marketPrice;
	}


	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}


	public BigDecimal getProtectPrice() {
		return protectPrice;
	}


	public void setProtectPrice(BigDecimal protectPrice) {
		this.protectPrice = protectPrice;
	}


	public String getGoodsThumb() {
		return goodsThumb;
	}


	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}


	public String getGoodsImg() {
		return goodsImg;
	}


	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}


	public String getOriginalImg() {
		return originalImg;
	}


	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
	}

	public String getSizePicture() {
		return sizePicture;
	}


	public void setSizePicture(String sizePicture) {
		this.sizePicture = sizePicture;
	}


	public String getCardPicture() {
		return cardPicture;
	}


	public void setCardPicture(String cardPicture) {
		this.cardPicture = cardPicture;
	}


	public String getOriginalBrand() {
		return originalBrand;
	}


	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}


	public String getSellerCode() {
		return sellerCode;
	}


	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

   
}
