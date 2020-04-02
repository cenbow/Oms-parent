package com.work.shop.oms.orderop.service;

import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;

public interface OrderConfirmService {

	/**
	 * 订单确认(共用方法)
	 * 
	 * @param masterOrderInfo
	 * @param distribute
	 * @param orderStatus
	 * @return
	 */
//	public ReturnInfo confirmOrder(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderStatus orderStatus);

	/**
	 * 主订单确认(共用方法)
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 * @return ReturnInfo
	 */
	ReturnInfo confirmOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);
	
	/**
	 * 子订单确认(共用方法)
	 * 
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 */
	public  ReturnInfo confirmOrderByOrderSn(String orderSn, OrderStatus orderStatus);
	
	
	/**
	 * 订单未确认(共用方法)
	 * 
	 * @param masterOrderInfo
	 * @param distribute
	 * @param orderStatus
	 * @return
	 */
//	public ReturnInfo unConfirmOrder(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderStatus orderStatus);

	/**
	 * 主订单未确认(共用方法)
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 * @return
	 */
	public ReturnInfo unConfirmOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);

	/**
	 * 子订单未确认(共用方法)
	 * 
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 */
	public  ReturnInfo unConfirmOrderByOrderSn(String orderSn, OrderStatus orderStatus);
	
	/**
	 * POS订单确认
	 * 
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 */
	public  ReturnInfo posConfirmOrder(ConsigneeModifyInfo modifyInfo);
	
	/**
	 * 主订单确认(共用方法)
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 * @return
	 */
	public void asynConfirmOrderByOrderSn(OrderStatus orderStatus);

	/**
	 *  订单改价确认
	 * @param masterOrderSn 主订单号
	 * @param orderStatus
	 * @return
	 */
	public ReturnInfo changePriceConfirmOrder(String masterOrderSn, OrderStatus orderStatus);
}
