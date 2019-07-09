package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.ServiceReturnInfo;

/**
 * 系统订单号服务
 * @author QuYachu
 */
public interface SystemOrderSnService {

	/**
	 * 主订单单号编码生成
	 * @return ServiceReturnInfo<String>
	 */
	ServiceReturnInfo<String> createMasterOrderSn();
	
	/**
	 * 子订单单号编码生成
	 * @return
	 */
	public ServiceReturnInfo<String> createOrderSn(String masterOrderSn);
}
