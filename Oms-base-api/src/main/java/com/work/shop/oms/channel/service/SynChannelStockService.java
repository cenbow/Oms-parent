package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.channel.bean.ChannelApiResult;
import com.work.shop.oms.channel.bean.SyncStockParam;


public interface SynChannelStockService {

	/**
	 * 同步渠道库存
	 * @param stockParams
	 * @return ChannelApiResult
	 */
	public ChannelApiResult synStock(List<SyncStockParam> stockParams);
	
	/**
	 * 更新缓存拦截时间
	 * @param filterTime
	 * @return ChannelApiResult
	 */
	public ChannelApiResult updateFilterTime(long filterTime);
}
