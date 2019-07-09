package com.work.shop.oms.api.bean;

import java.io.Serializable;

public class ChannelOnsaleGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7320052505356764696L;
	private String shopCode;
	private String sku6;   //6位
	private String sku11; //11位
	private String numId;  //平台商品Id
	private String channelStock;  //库存
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getSku6() {
		return sku6;
	}
	public void setSku6(String sku6) {
		this.sku6 = sku6;
	}
	public String getSku11() {
		return sku11;
	}
	public void setSku11(String sku11) {
		this.sku11 = sku11;
	}
	public String getNumId() {
		return numId;
	}
	public void setNumId(String numId) {
		this.numId = numId;
	}
	public String getChannelStock() {
		return channelStock;
	}
	public void setChannelStock(String channelStock) {
		this.channelStock = channelStock;
	}
	
	
	
	
	
	
	
}
