package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.channel.bean.ChannelApiResult;

/**
 * @author wangkui
 * 
 **/
public interface ProductGoodsCacheService {

	/**
	 * 根据商品sku编码批量查询商品信息
	 * 
	 * @param skus
	 * @return
	 */
	public ChannelApiResult searchProductGoodsInfo(List<String> skus);  // 批量查询
	
	public ChannelApiResult searchProductGoodsInfoSingle(String sku);  // 单条查询

	/**
	 * 根据商品货号批量查询商品信息
	 * 
	 * @param goodsSns
	 * @return
	 */
	public ChannelApiResult searchProductGoodsInfoByGoodsSn(List<String> goodsSns); // 批量查询
	
	public ChannelApiResult searchProductGoodsInfoByGoodsSnSingle(String goodsSn);  // 单条查询
	
}
