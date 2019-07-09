package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.CsChannelInfo;
import com.work.shop.oms.common.bean.ReturnInfo;



public interface ChannelInfoService {
	
	/**
	 * 根据店铺编码以及referer获取实际库存使用渠道
	 * @param shopCode
	 * @param referer
	 * @return
	 */
	public String getShopCode(String shopCode, Integer source);

	/**
	 * 根据店铺编码获取店铺所属渠道编码
	 * 
	 * @param shopCode
	 * @return
	 */
	public String getChannelCode(String shopCode);
	
	public ReturnInfo<List<CsChannelInfo>> findChannelInfoByChannelType(Short channelType);
	
	public ReturnInfo<CsChannelInfo> findSiteInfoBySiteCode(String siteCode);
	
	public ReturnInfo<List<ChannelShop>> findChannelShopByChannelCode(String siteCode);
	
	public ReturnInfo<ChannelShop> findShopInfoByShopCode(String shopCode);
}
