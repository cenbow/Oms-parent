package com.work.shop.oms.service;

import java.util.List;

import com.work.shop.oms.channel.bean.ChannelApiResult;

public interface CacheDataService {

	/**
	 * 根据商品sku编码批量查询商品信息
	 * 
	 * @param skus
	 * @return
	 */
	public ChannelApiResult searchProductGoodsInfo(List<String> skus);  // 批量查询
	
	public ChannelApiResult searchProductGoodsInfoSingle(String sku);  // 单条查询
}
