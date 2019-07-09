package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OtherOrderParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2015842823669618790L;
	
	 private String orderId;//订单编号  *
	
    private String skuId;//平台商品款码 *

    private String iid;//平台商品码

    private String outerId;//11位商品码 *

    private String title;//商品名称 *

    private String goodsSn;//6为商品码

    private String price;//商品成交价格 *

    private String originPrice;//商品原价

    private String num;//商品数量 *

    private String extensionCode;//商品扩展属性  common普通商品  gift赠品 *

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}


    
    
}
