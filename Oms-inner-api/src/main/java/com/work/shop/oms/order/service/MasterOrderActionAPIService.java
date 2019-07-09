package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.ReturnInfo;

public interface MasterOrderActionAPIService {
	
	/**
	 * 保存订单日志
	 * @param orderOutSn
	 * @param message
	 * @param actionUser  默认 system
	 */
	ReturnInfo insertOrderActionByOutSn(String orderOutSn, String message, String actionUser);

}
