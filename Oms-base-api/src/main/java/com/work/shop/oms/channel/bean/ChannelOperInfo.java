package com.work.shop.oms.channel.bean;

import java.io.Serializable;

public class ChannelOperInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1721995237520233191L;

	private String channelCode; // 渠道编码
	
	private Integer channelType; // 渠道类型 1:线上直营渠道 2：线上加盟渠道 3：线下直营渠道 4：线下加盟渠道
	
	private String channelTitle; // 渠道名称
	
	private Integer channelStatus; // 渠道状态
	
	private String backup; // 备注

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getChannelType() {
		return channelType;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public Integer getChannelStatus() {
		return channelStatus == null ? 0 : channelStatus;
	}

	public void setChannelStatus(Integer channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getBackup() {
		return backup;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}
}