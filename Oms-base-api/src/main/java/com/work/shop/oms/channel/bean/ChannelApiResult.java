package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.work.shop.oms.api.bean.ChannelShop;
import com.work.shop.oms.api.ship.bean.ApiChannelGoods;


public class ChannelApiResult<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -616127696347656054L;

	/** 返回码 0 接口成功返回 **/
	private String code;
	
	private String message;
	
	private String total;
	
	private List<ChannelShop> channelShopList;
	
	private List<ApiChannelGoods> channelGoodsList;
	
	private List<Map<String,String>> shellList;
	
	private Integer numIid;
	
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<ChannelShop> getChannelShopList() {
		return channelShopList;
	}

	public void setChannelShopList(List<ChannelShop> channelShopList) {
		this.channelShopList = channelShopList;
	}

	public Integer getNumIid() {
		return numIid;
	}

	public void setNumIid(Integer numIid) {
		this.numIid = numIid;
	}

	public List<ApiChannelGoods> getChannelGoodsList() {
		return channelGoodsList;
	}

	public void setChannelGoodsList(List<ApiChannelGoods> channelGoodsList) {
		this.channelGoodsList = channelGoodsList;
	}

	public List<Map<String, String>> getShellList() {
		return shellList;
	}

	public void setShellList(List<Map<String, String>> shellList) {
		this.shellList = shellList;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
