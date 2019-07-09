package com.work.shop.oms.channel.service;

import com.work.shop.oms.channel.bean.ChannelApiResult;

public interface ChannelService {

	
	/**
	 * 获取渠道店铺信息
	 * @param channelType
	 * @param isSyn
	 * @return
	 */
	ChannelApiResult getChannelShop(Short[] channelType,Integer isSyn);
	
	/**
	 * 获取(线上所有需要同步库存)渠道店铺信息
	 * @param channelType
	 * @param isSyn
	 * @return
	 */
	ChannelApiResult getAllSynStockChannelShop();
	
	/**
	 * 获取渠道商品信息
	 * @param shopCode
	 * @param sku
	 * @param isOnSell
	 * @return
	 */
//	ChannelApiResult getChannelGoods(String shopCode,String sku,String isOnSell);
	/**
	 * 
	 * @param shopCode 店铺编码     必填
	 * @param sku  商品6位码     非必填
	 * @param status 商品状态列表     必填  状态值定义 0：下架 1 ： 上架   2： 售罄
	 * @param page 查询页码     必填
	 * @param pageSize  查询每页条目   必填
	 * @param isSync 0: 不同步;1:同步     非必填
	 * @return
	 */
	ChannelApiResult getChannelGoods(String shopCode,String sku,String[] status, int page, int pageSize, String isSync);

	/**
	 * 在售清单
	 * @param shopCode
	 * @param sku
	 * @param pageNo
	 * @param pageCount
	 * @return
	 */
	ChannelApiResult onSellList(String shopCode,String sku,int page, int pageSize);
	
	
	/**
	 * 在售清单
	 * @param shopCode
	 * @param sku
	 * @param pageNo
	 * @param pageCount
	 * @return
	 */
	ChannelApiResult onSellGoodsList(String shopCode,String sku,int page, int pageSize);
}
