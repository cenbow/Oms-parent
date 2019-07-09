package com.work.shop.oms.order.service;

import com.work.shop.oms.api.bean.OrderInfo;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;

/**
 * 主订单服务
 * @author lemon
 *
 */
public interface MasterOrderInfoOPService {


	/**
	 * 创建订单方法，生成订单号，插入订单数据，生成付款单，生成发货单等操作
	 * @param order 订单接口传入order对象
	 * @return 订单主表信息
	 */
	ServiceReturnInfo<OrderCreateReturnInfo> createOrder(MasterOrder masterOrder);

	/**
	 * 主订单确认
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	ServiceReturnInfo<OrderCreateReturnInfo> confirmOrder(String masterOrderSn, String message);

	
	/**
	 * 根据订单号查询订单信息
	 * @param orderSn
	 * @return
	 */
	public OrderInfo getOrderInfoBySn(String orderSn);
}
