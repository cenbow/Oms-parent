package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 订单商品销售记录
 * @author QuYachu
 *
 */
public class OrderGoodsSaleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道
	 */
	private String channelCode;
	
	/**
	 * 订单来源
	 */
	private String orderFrom;
	
	/**
	 * 商品11位码
	 */
	private String customCode;
	
	/**
	 * 商品6位码
	 */
	private String goodsSn;
	
	/**
	 * 商品数量
	 */
	private Integer goodsNumber = 0;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	
}
