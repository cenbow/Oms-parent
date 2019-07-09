package com.work.shop.oms.channel.response;

import java.io.Serializable;

/**
 * 渠道商品调整单
 * @author QuYachu
 *
 * @param <T>
 */
public class ChannelGoodsTicketManagementResponse<T> implements Serializable {

	private static final long serialVersionUID = -2393142555910710263L;

	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private T data;
	
	public Boolean getSuccess() {
		return success;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
