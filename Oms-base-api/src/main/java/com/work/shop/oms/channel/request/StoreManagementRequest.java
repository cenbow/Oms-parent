package com.work.shop.oms.channel.request;

import java.io.Serializable;

import com.work.shop.oms.channel.bean.ChannelOperInfo;
import com.work.shop.oms.channel.bean.StoreOperInfo;

public class StoreManagementRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115261779726715323L;

	public static final Integer oper_type_add = 0;
	
	public static final Integer oper_type_update = 1;
	
	public static final Integer oper_type_act = 2;
	
	public static final Integer oper_type_close = 3;

	public static final Integer oper_type_search = 4;

	private Integer opertype; // 操作类型 0:新增 1:更新 2:激活 3:关闭 4:查询
	
	private Integer pageNo; // 页码
	
	private Integer pageSize; // 每页记录数
	
	private ChannelOperInfo channelOperInfo;

	private StoreOperInfo storeOperInfo;

	public Integer getOpertype() {
		return opertype;
	}

	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}

	public ChannelOperInfo getChannelOperInfo() {
		return channelOperInfo;
	}

	public void setChannelOperInfo(ChannelOperInfo channelOperInfo) {
		this.channelOperInfo = channelOperInfo;
	}

	public StoreOperInfo getStoreOperInfo() {
		return storeOperInfo;
	}

	public void setStoreOperInfo(StoreOperInfo storeOperInfo) {
		this.storeOperInfo = storeOperInfo;
	}

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
}