package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道库存同步日志
 * @author QuYachu
 *
 */
public class ChannelStockLogInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer logId;
	
	private String channelCode; // 渠道编码
	
	private String shopCode; // 店铺编码
	
	private String skuSn; // 11位商品码
	
	private String returnCode; // 执行结果 0失败、1成功
	
	private String returnMessage; // 执行信息
	
	private String user; // 操作人
	
	private Date requestTime; // 执行时间
	
	private Integer stock; // 库存量
	
	private String channelTitle; // 渠道名称
	
	private String shopTitle; // 店铺名称
	
	private String beginTime; // 开始时间
	
	private String endTime; // 结束时间

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Integer getStock() {
		if (stock == null) 
			stock = 0;
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
