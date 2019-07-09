package com.work.shop.oms.service;

import java.util.List;


import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.common.bean.OrderDepotResult;
import com.work.shop.oms.exception.ErpDepotException;


/**
 * 订单下发ERP DUBBO服务
 * 
 * @author tony
 * 
 */
public interface OrderPushService {

	/**
	 * 订单下发erp 不可以重复下发 提供给生成订单，订单确认使用
	 * 
	 * @param orderSn
	 */
	public OrderDepotResult pushOrderToErp(String orderSn) throws ErpDepotException;

	/**
	 * 订单下发erp 可以重复下发 提供给POS和OS下发工具使用
	 * 
	 * @param orderSn
	 */
	public void pushOrderToErpPosConfirm(String orderSn);
}
