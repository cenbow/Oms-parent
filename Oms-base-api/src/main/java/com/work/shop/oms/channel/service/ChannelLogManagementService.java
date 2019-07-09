package com.work.shop.oms.channel.service;

import java.util.List;

import com.work.shop.oms.channel.bean.AppAccountOperInfo;
import com.work.shop.oms.channel.bean.ChannelApiLogInfo;
import com.work.shop.oms.channel.bean.ChannelStockLogInfo;
import com.work.shop.oms.channel.request.ChannelLogManagementRequest;
import com.work.shop.oms.channel.response.ChannelLogManagementResponse;

/**
 * 统一渠道日志管理
 * @author QuYachu
 *
 */
public interface ChannelLogManagementService {
	
	/**
	 * 查询渠道库存同步日志列表
	 * @param request
	 * @return
	 */
	public ChannelLogManagementResponse<List<ChannelStockLogInfo>> searchChannelStockLogList(ChannelLogManagementRequest<ChannelStockLogInfo> request);
	
	/**
	 * 查询渠道系统操作日志列表
	 * @param request
	 * @return
	 */
	public ChannelLogManagementResponse<List<ChannelApiLogInfo>> searchChannelApiLogList(ChannelLogManagementRequest<ChannelApiLogInfo> request);

	/**
	 * 获取物流平台列表
	 * @param request
	 * @return
	 */
	public ChannelLogManagementResponse<List<AppAccountOperInfo>> searchAppAccountList(ChannelLogManagementRequest<AppAccountOperInfo> request);
}
