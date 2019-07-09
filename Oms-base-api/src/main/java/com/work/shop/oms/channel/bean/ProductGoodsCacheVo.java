package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductGoodsCacheVo implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4906192982359759726L;

    private String goodsSn;                        

    private String colorCode;

    private String sizeCode;

    private String barcode;

    private String custumCode;

    private String colorName;

    private String colorSeries;

    private String sizeName;

    private String sellerCode;

    private String businessBarcode;

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCustumCode() {
		return custumCode;
	}

	public void setCustumCode(String custumCode) {
		this.custumCode = custumCode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getColorSeries() {
		return colorSeries;
	}

	public void setColorSeries(String colorSeries) {
		this.colorSeries = colorSeries;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public String getBusinessBarcode() {
		return businessBarcode;
	}

	public void setBusinessBarcode(String businessBarcode) {
		this.businessBarcode = businessBarcode;
	}

}