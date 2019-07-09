package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.channel.bean.ChannelGoodsTicketOperInfo;
import com.work.shop.oms.channel.bean.TicketInfoVoInfo;
import com.work.shop.oms.channel.request.ChannelGoodsTicketManagementReqeust;
import com.work.shop.oms.channel.response.ChannelGoodsTicketManagementResponse;

public interface TicketInfoManagementService {
	
	/**
	 * 查询调整单明细列表
	 * @param request
	 * @return
	 */
	public ChannelGoodsTicketManagementResponse<List<TicketInfoVoInfo>> searchTicketInfo(ChannelGoodsTicketManagementReqeust<ChannelGoodsTicketOperInfo> request);

	/**
	 * 下载调整单明细列表
	 * @param request
	 * @return
	 */
	public ChannelGoodsTicketManagementResponse<List<TicketInfoVoInfo>> downLoadTicketInfo(ChannelGoodsTicketManagementReqeust<ChannelGoodsTicketOperInfo> request);
	
	/**
	 * 删除调整单明细信息
	 * @param request
	 * @return
	 */
	public ChannelGoodsTicketManagementResponse<Boolean> deleteTicketInfo(ChannelGoodsTicketManagementReqeust<String> request);
}
