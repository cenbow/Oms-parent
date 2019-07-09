package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 渠道队列发货数据
 * @author huangl
 */
public class ChannelDeliveryQueue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//订单号
	private String orderId;
	
	//承运商编码
	private String companyCode;
	
	//快递单号
	private String companySid;
	
	//发货单号
	private String shipSn;
	
	//渠道号
	private String channelCode;
	
	//商品集合
	private String outerSkuList;
	
	//是否分仓
	private Integer splitFlag;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getShipSn() {
		return shipSn;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public String getCompanySid() {
		return companySid;
	}

	public void setCompanySid(String companySid) {
		this.companySid = companySid;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getOuterSkuList() {
		return outerSkuList;
	}

	public void setOuterSkuList(String outerSkuList) {
		this.outerSkuList = outerSkuList;
	}

	public Integer getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(Integer splitFlag) {
		this.splitFlag = splitFlag;
	}
}
