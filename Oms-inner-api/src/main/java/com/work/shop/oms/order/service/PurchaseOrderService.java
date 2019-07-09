package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.request.OrderManagementRequest;

/**
 * 采购单服务
 * 	采购单创建
 * @author lemon
 *
 */
public interface PurchaseOrderService {

	/**
	 * 采购单创建(交货单)
	 * @param request
	 */
	ReturnInfo<String> purchaseOrderCreate(OrderManagementRequest request);
	
	/**
	 * 采购单创建(订单)
	 * @param request
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> purchaseOrderCreateByMaster(OrderManagementRequest request);
}
