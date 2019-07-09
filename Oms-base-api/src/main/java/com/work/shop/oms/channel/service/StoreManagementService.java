package com.work.shop.oms.channel.service;

import com.work.shop.oms.channel.request.StoreManagementRequest;
import com.work.shop.oms.channel.response.StoreManagementResponse;

/**
 * 渠道店铺管理
 * 
 * @author lemon
 *
 */
public interface StoreManagementService {
	
	/**
	 * 渠道店铺列表
	 * @param request
	 * @return
	 */
	public StoreManagementResponse storeManagement(StoreManagementRequest request);
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public StoreManagementResponse channelManagement(StoreManagementRequest request);
	
}
