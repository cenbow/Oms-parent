package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class PullAndTurn implements Serializable{

	private static final long serialVersionUID = 7961718494582560647L;
	
	private String channelOrderSn;//外部交易号
	private String osOrderSn; //OS订单号
	private String channelCode; //渠道

	private String shipStatus; //

	private String convertStatus; //
	private String channelName; //
	
	private String startTime;
	
	private String endTime;

	private String start;
	private String limit;
	
	public String getChannelOrderSn() {
		return channelOrderSn;
	}
	public void setChannelOrderSn(String channelOrderSn) {
		this.channelOrderSn = channelOrderSn;
	}
	public String getOsOrderSn() {
		return osOrderSn;
	}
	public void setOsOrderSn(String osOrderSn) {
		this.osOrderSn = osOrderSn;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
/*	public String getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}*/
	
	public String getConvertStatus() {
		return convertStatus;
	}
	public void setConvertStatus(String convertStatus) {
		this.convertStatus = convertStatus;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	

/*	private Integer isOk;

	public Integer getIsOk() {
		return isOk;
	}
	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}*/
	

/*	private Integer isOk;

	public Integer getIsOk() {
		return isOk;
	}
	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}*/
	
	private Integer isOs;

	public Integer getIsOs() {
		return isOs;
	}
	public void setIsOs(Integer isOs) {
		this.isOs = isOs;
	}

	private Integer downloadFinish;

	public Integer getDownloadFinish() {
		return downloadFinish;
	}
	public void setDownloadFinish(Integer downloadFinish) {
		this.downloadFinish = downloadFinish;
	}
	
	public String getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}
	
    private Integer isOk;

	public Integer getIsOk() {
		return isOk;
	}
	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}
	
	private String shopCode; //渠道号码

	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
}