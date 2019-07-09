package com.work.shop.oms.channel.response;

import java.io.Serializable;

/**
 * 渠道库存同步日志返回信息
 * @author QuYachu
 *
 * @param <T>
 */
public class ChannelLogManagementResponse<T> implements Serializable {

	private static final long serialVersionUID = -3875722249876772536L;
	
	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
