package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GoodsReturnChangeMobile implements Serializable {

	private static final long serialVersionUID = -1949285717988812607L;
	
	private double returnPrice;//退单金额
	
	private double exchangePrice;//换单金额
	
	private double shippingPrice;//运费
	
	private String reason;//退换货原因
	
	private String orderSn;//订单号
	
	private String explain;//退换货说明
	
	private String userName;//联系人
	
	private String mobile;//联系电话
	
	private String createTime;//申请时间
	
	private String channelCode;//店铺编码
	
 	private String channelName;//店铺名称
 	
 	private int status;//'状态：0：已取消；1：待沟通；2：已完成；3：待处理'....
 	
 	private List<GoodsMobile> orderGoodsList;//换单商品
 	
 	private List<GoodsMobile> returnGoodsList;//退单商品
 	
 	private String clearTime;//退单结算时间
 	
 	private String orderCreateTime;//换单生成时间

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public double getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(double returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<GoodsMobile> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<GoodsMobile> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public List<GoodsMobile> getReturnGoodsList() {
		return returnGoodsList;
	}

	public void setReturnGoodsList(List<GoodsMobile> returnGoodsList) {
		this.returnGoodsList = returnGoodsList;
	}

	public double getExchangePrice() {
		return exchangePrice;
	}

	public void setExchangePrice(double exchangePrice) {
		this.exchangePrice = exchangePrice;
	}

	public double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	
	
}
