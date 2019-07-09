package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道操作日志
 * @author QuYachu
 *
 */
public class ChannelApiLogInfo implements Serializable {

	private static final long serialVersionUID = -1158478142065696930L;

	 private Integer logId;
	 
	 private String channelCode; // 渠道code
	 
	 private String shopCode; // 店铺code
	 
	 /**
	  * 0 价格调整/1上下架调整/2商品详情调整/3卖点调整
	  * 4商品名称调整/5商品条形码调整/6商品运费承担方式调整
	  * 7商品支持会员打折调整/8商品线上线下调整/9商品详情生成
	  * 10店铺经营商品生成
	  */
	 private String methodName; // 调整单类型 
	 
	 private String paramInfo; // 调整单号
	 
	 private String returnCode; // 执行结果 0失败/1成功
	 
	 private String returnMessage; // 执行信息
	 
	 private String user; // 操作人
	 
	 private Date requestTime; // 执行时间
	 
	 private String channelTitle; // 渠道名称
		
	 private String shopTitle; // 店铺名称
	
	 private String beginTime; // 请求开始时间
	
	 private String endTime; // 请求结束时间

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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParamInfo() {
		return paramInfo;
	}

	public void setParamInfo(String paramInfo) {
		this.paramInfo = paramInfo;
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
