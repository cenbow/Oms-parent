package com.work.shop.oms.channel.request;

import java.io.Serializable;

import com.work.shop.oms.channel.bean.ChannelStockLogInfo;

/**
 * 渠道日志管理请求
 * @author QuYachu
 *
 */
public class ChannelLogManagementRequest<T> implements Serializable {

	private static final long serialVersionUID = 7476860642764740948L;

	private Integer pageNo; // 页码
	
	private Integer pageSize; // 每页记录数
	
	private T infoBean;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public T getInfoBean() {
		return infoBean;
	}

	public void setInfoBean(T infoBean) {
		this.infoBean = infoBean;
	}
	
}
