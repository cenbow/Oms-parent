package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.channel.bean.ChannelGoodsTicketOperInfo;
import com.work.shop.oms.channel.request.ChannelGoodsTicketManagementReqeust;
import com.work.shop.oms.channel.response.ChannelGoodsTicketManagementResponse;

/**
 * 调整单信息接口服务
 * @author QuYachu
 *
 */
public interface ChannelGoodsTicketManagementService {
	
	/**
	 * 获取商品新增、价格、上下架调整单列表
	 * @param request
	 * @return
	 */
	public ChannelGoodsTicketManagementResponse<List<ChannelGoodsTicketOperInfo>> searchChannelGoodsTicketInfo(ChannelGoodsTicketManagementReqeust<ChannelGoodsTicketOperInfo> request);

}
