package com.work.shop.oms.common.bean;

import java.math.BigDecimal;
import java.util.List;

import com.work.shop.oms.bean.ChannelGoods;
import com.work.shop.oms.bean.ProductBarcodeList;

public class ChannelGoodsVo extends ChannelGoods {

	private Integer catId; 

	private String brandCode;

	private String skuSn;
	
	private String colorCode;
	
	private String sizeCode;
	
	private String custumCode;
	
	private String colorName;
	
	private String sizeName;
	
	
	private List<ProductBarcodeList> colorChild;
	
	private List<ProductBarcodeList> sizeChild;
	
	private String currColorCode;           //当前商品颜色码

	private String currSizeCode;            //当前商品尺码
	


	public List<ProductBarcodeList> getColorChild() {
		return colorChild;
	}

	public void setColorChild(List<ProductBarcodeList> colorChild) {
		this.colorChild = colorChild;
	}

	public List<ProductBarcodeList> getSizeChild() {
		return sizeChild;
	}

	public void setSizeChild(List<ProductBarcodeList> sizeChild) {
		this.sizeChild = sizeChild;
	}

	public String getCurrColorCode() {
		return currColorCode;
	}

	public void setCurrColorCode(String currColorCode) {
		this.currColorCode = currColorCode;
	}

	public String getCurrSizeCode() {
		return currSizeCode;
	}

	public void setCurrSizeCode(String currSizeCode) {
		this.currSizeCode = currSizeCode;
	}

	private BigDecimal marketPrice;


	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
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

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
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

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}
	
}
