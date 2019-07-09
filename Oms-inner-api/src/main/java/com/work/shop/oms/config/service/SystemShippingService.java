package com.work.shop.oms.config.service;

import com.work.shop.oms.bean.SystemShipping;

/**
 * 
 * @author lemon
 *
 */
public interface SystemShippingService {
	
	SystemShipping getSystemShipByShipCode(String shippingCode);

}
