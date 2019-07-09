package com.work.shop.oms.action.service;

import java.math.BigDecimal;
import java.util.List;

import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderAction;
import com.work.shop.oms.bean.OrderDistribute;

public interface OrderActionService {

	
	/**
	 * 保存订单日志
	 * logtime default now
	 * actionUser default 'system'
	 * 
	 * @param orderAction
	 */
	void saveOrderAction(OrderAction orderAction);

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
	void saveOrderAction(String orderSn,String message);

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
	OrderAction createQrderAction(OrderDistribute distribute);

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
	OrderAction addOrderAction(String orderSn,String actionNote);
	
	/**
	 * 
	 * @param orderSn
	 * @param actionNote
	 * @param actionUser
	 * @return
	 */
	OrderAction addOrderAction(String orderSn,String actionNote,String actionUser);
	
	OrderReturnAction addOrderReturnAction(String returnSn,String actionNote);
	    
	OrderReturnAction addOrderReturnAction(String returnSn,String actionNote,String actionUser);

    /**
     * 添加退单日志
     * @param returnSn
     * @param actionNote
     * @param actionUser
     * @param returnOrderStatus
     * @return
     */
    OrderReturnAction addOrderReturnAction(String returnSn,String actionNote,String actionUser, Integer returnOrderStatus);
	
	/**
     * 退单操作日志列表
     * 
     * @param model
     * @return
     * @throws Exception
     */
    List<OrderReturnAction> getOrderReturnActionList(OrderReturnAction model);

    /**
     * 添加退单日志
     * @param actionRequest
     * @return
     */
    public OrderReturnAction addOrderReturnAction(OrderReturnAction actionRequest);
}
