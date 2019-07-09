/*
* 2014-8-18 上午10:29:31
* 吴健 HQ01U8435
*/

package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ChannelOrder  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3706428204043321382L;
	private String channelOrderSn;
	private String osOrderSn;
	private String channelName;
	private String channelCode;
	private String createDate;
	private String addTime;
	private String companyCode;
	private String invoiceNo;
	private int isShipToChannel;
	private String shipTime;
	private int requestNum;

	public String getChannelOrderSn() {
		return channelOrderSn;
	}
	public ChannelOrder() {
	}
	public ChannelOrder(String cc, String cn) {
		this.channelCode = cc;
		this.channelName = cn;
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
	
	private int downloadFinish;

	public int getDownloadFinish() {
		return downloadFinish;
	}
	public void setDownloadFinish(int downloadFinish) {
		this.downloadFinish = downloadFinish;
	}

}
