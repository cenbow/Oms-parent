package com.work.shop.oms.api.express.service;

import java.util.List;

import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.common.bean.ReturnInfo;


public interface OrderExpressPullService {
	
	/**
	 * 订单物流写入
	 * @param orderInfo
	 * @param orderShip
	 * @return
	 */
	public ReturnInfo<String> orderExpress(String orderSn);
	
	/**
	 * 退单物流写入
	 * @param orderReturn
	 * @param orderReturnShip
	 * @return
	 */
	public ReturnInfo<String> orderReturnExpress(OrderReturn orderReturn, OrderReturnShip orderReturnShip);
	
	
	public ReturnInfo<List<OrderDepotShip>> selectEffectiveShip(String orderSn);

}