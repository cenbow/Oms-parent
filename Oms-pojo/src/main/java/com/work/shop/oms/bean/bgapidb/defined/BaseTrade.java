/*
 * 2015-2-2 下午3:29:07
 * 吴健 HQ01U8435
 */

package com.work.shop.oms.bean.bgapidb.defined;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseTrade {
	private Integer isOs = 0;

	private Date addTime = null;

	private String channelCode;

	private String channelName;

	private String tradeFrom;
	
	private Integer downloadFinish = 0;
	
	private List<BaseOrder> orders=new ArrayList<BaseOrder>();

	public Integer getIsOs() {
		return isOs;
	}

	public void setIsOs(Integer isOs) {
		this.isOs = isOs;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}


	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public Integer getDownloadFinish() {
		return downloadFinish;
	}

	public void setDownloadFinish(Integer downloadFinish) {
		this.downloadFinish = downloadFinish;
	}
	public  List<BaseOrder> getOrders(){
		return orders;
	}
	
	public void setOrders(List<BaseOrder>  orders) {
		this.orders = orders;
	}

	public abstract String tradeId();
	
	public abstract String getLastModefiedTimeForManager();
	public final static Integer DOWNLOADFINISHED=1;
	public final static Integer N_DOWNLOADFINISHED=0;
	
	
}
