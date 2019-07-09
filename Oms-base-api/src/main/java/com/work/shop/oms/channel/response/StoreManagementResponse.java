package com.work.shop.oms.channel.response;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.channel.bean.ChannelOperInfo;
import com.work.shop.oms.channel.bean.StoreOperInfo;

public class StoreManagementResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private List<ChannelOperInfo> channelOperInfos;
	
	private List<StoreOperInfo> storeOperInfos;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ChannelOperInfo> getChannelOperInfos() {
		return channelOperInfos;
	}

	public void setChannelOperInfos(List<ChannelOperInfo> channelOperInfos) {
		this.channelOperInfos = channelOperInfos;
	}

	public List<StoreOperInfo> getStoreOperInfos() {
		return storeOperInfos;
	}

	public void setStoreOperInfos(List<StoreOperInfo> storeOperInfos) {
		this.storeOperInfos = storeOperInfos;
	}
}