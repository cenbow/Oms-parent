package com.work.shop.oms.common.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ChannelOrderVO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8217391648473892412L;
	
	private String channelNameStr;
	private String shopCode;
	private String channelOrderSn;
	private Integer isOs;
	private	String paymentStatus; //支付状态: 1.已支付 , 0.未支付;
	private String addTime;
	private String osOrderSn;
	private int downloadFinish;
    private String orderErrorMsg;
    private String channelName;
	
	
	
	private String transFormOrder;
	private String shipStatus;
	private String channelCode;
	private String createDate;
	private String companyCode;
	private String invoiceNo;
	private int isShipToChannel;
	private String shipTime;
	private int requestNum;
	

	
	public String getChannelNameStr() {
		if(null!= channelName){
			return channelName;
		}else{
			return shopCode;
		}
	}
	public void setChannelNameStr(String channelNameStr) {
		this.channelNameStr = channelNameStr;
	}
	public void setTransFormOrder(String transFormOrder) {
		this.transFormOrder = transFormOrder;
	}
	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}
	public String getTransFormOrder() {
		if(null==this.getOsOrderSn()||this.getOsOrderSn().length()<2){
			return "<span class=\"span0\">未转单</span>";
		}else{
			return "<span class=\"span1\">已转单</span>";
		}
	}
	
	public String getShipStatus() {
		String h="";
		if(this.getIsShipToChannel() > 0) {
			h = "<span class=\"span1\">已发货，已同步</span>";
		} else if(this.getIsShipToChannel() == 0){
			if(StringUtils.isNotBlank(this.getInvoiceNo())){
				h = "<span class=\"span0\"<span>已发货，未同步</span>";
			}else{
				h = "<span class=\"span0\"<span>未发货</span>";
			}
		}else{
			h = "<span class=\"span0\"<span>已发货，同步异常</span>";
		}
		return h;
	}
	
	
	public String getDownloadFinishStr() {
		String h = "";
		
		if(this.getDownloadFinish() == 1){
			h = "<span class=\"span0\"<span>是</span>";
		}else{
			h = "<span class=\"span0\"<span>否</span>";
		}
		
		return h;
	}
	
	
	 
	public Integer getIsOs() {
		return isOs;
	}
	public void setIsOs(Integer isOs) {
		this.isOs = isOs;
	}

	

	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	 private String outerOrderSn;

	public String getOuterOrderSn() {
		return outerOrderSn;
	}
	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}
	
	

	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
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
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public int getIsShipToChannel() {
		return isShipToChannel;
	}
	public void setIsShipToChannel(int isShipToChannel) {
		this.isShipToChannel = isShipToChannel;
	}
	public String getShipTime() {
		return shipTime;
	}
	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}
	public int getRequestNum() {
		return requestNum;
	}
	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}
	
	

	public int getDownloadFinish() {
		return downloadFinish;
	}
	public void setDownloadFinish(int downloadFinish) {
		this.downloadFinish = downloadFinish;
	}
	public String getOrderErrorMsg() {
		return orderErrorMsg;
	}
	public void setOrderErrorMsg(String orderErrorMsg) {
		this.orderErrorMsg = orderErrorMsg;
	}


}
