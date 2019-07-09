package com.work.shop.oms.splitorder.service;

import com.work.shop.oms.common.bean.OrderDepotResult;


/**
 * 拆单服务
 * @author lemon
 *
 */
public interface SplitOrderService {
	

	OrderDepotResult splitOrder(String masterOrderSn);
}
