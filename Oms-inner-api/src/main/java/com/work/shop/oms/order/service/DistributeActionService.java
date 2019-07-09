package com.work.shop.oms.order.service;

import java.math.BigDecimal;

import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderDistribute;

/**
 * 交货单日志Service
 * @author lemon
 *
 */
public interface DistributeActionService {

	
	
	/**
	 * 保存订单日志
	 * logtime default now
	 * actionUser default 'system'
	 * 
	 * @param orderAction
	 */
	void saveOrderAction(DistributeAction distributeAction);

	/**
	 * 保存订单日志，所有状态默认值
	 * orderstatus default 0
	 * rangeStatus default -1
	 * shippingStatus default 0
	 * payStatis default 0
	 * 
	 * 
	 * @param orderSn
	 * @param message
	 */
	void saveOrderAction(String orderSn, String message);

	/**
	 * 保存订单日志，传入付款单状态，其他默认
	 * @param orderSn
	 * @param message
	 * @param payStatus
	 */
	void saveOrderAction(String orderSn,String message,byte payStatus);

	/**
	 * 根据订单创建订单日志
	 * @param orderInfo
	 * @return
	 */
	DistributeAction createQrderAction(OrderDistribute distribute);

	/**
	 * 系统支付日志
	 * @param orderPay
	 * @param bonus
	 * @param payCode
	 */
	void saveOrderPayLog(MasterOrderPay orderPay, BigDecimal bonus, String payCode);

	/**
	 * 根据订单创建订单日志
	 * @param orderSn
	 * @param actionNote
	 * @param actionUser
	 * @return
	 */
	DistributeAction addOrderAction(String orderSn,String actionNote);
	
	/**
	 * 
	 * @param orderSn
	 * @param actionNote
	 * @param actionUser
	 * @return
	 */
	DistributeAction addOrderAction(String orderSn,String actionNote,String actionUser);
}
