package com.work.shop.oms.orderop.service;

import com.work.shop.oms.bean.OrderReturnMoneyBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 订单取消服务
 * @author QuYachu
 */
public interface OrderCancelService {

	/**
	 * 订单取消(共用方法)
	 * 
	 * @param masterOrderInfo
	 * @param distribute
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return
	 */
	/*public ReturnInfo cancelOrder(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderStatus orderStatus);*/

	/**
	 * 主订单取消(共用方法)
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	ReturnInfo cancelOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);
	
	/**
	 * 子订单取消(共用方法)
	 * 
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	ReturnInfo cancelOrderByOrderSn(OrderStatus orderStatus);
	
	/**
	 * 取消、发货创建退单
	 * 
	 * @param masterOrderSn
	 * @param orderStatus
	 * @param createType	0 : 取消订单 1：发货订单
	 * @return
	 */
	public ReturnInfo createReturnOrder(String masterOrderSn, OrderStatus orderStatus, int createType);
	
	/**
	 * 订单退款消息发送
	 * @param orderReturnMoneyBean
	 */
	public void doOrderReturnMoney(OrderReturnMoneyBean orderReturnMoneyBean);
}
