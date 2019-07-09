package com.work.shop.oms.service;

import com.work.shop.oms.bean.OrderAddressInfo;

public interface OrderAddressInfoService {
	
	/**
	 * 根据订单号获取收货信息
	 * @param orderSn
	 * @return
	 */
	OrderAddressInfo findOrderShipByOrderSn(String orderSn);
}
