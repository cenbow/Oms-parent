package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 订单商品查询
 * @author QuYachu
 *
 */
public class OrderGoodsQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String beginTime;
	
	private String endTime;
	
	private int start;
	
	private int limit;

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

	public int getStart() {
		return start;
	}

	public int getLimit() {
		return limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
