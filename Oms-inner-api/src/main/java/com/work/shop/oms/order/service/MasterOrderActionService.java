package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderInfo;

/**
 * 订单日志服务
 * @author QuYachu
 */
public interface MasterOrderActionService {

	/**
	 * 保存订单确认日志
	 * @param masterOrderAction 订单日志
	 */
	void insertOrderActionByObj(MasterOrderAction masterOrderAction);

	/**
	 * 保存订单日志
	 * @param masterOrderSn 订单编码
	 * @param message 日志信息
	 * @param actionUser 操作人
	 */
	void insertOrderActionBySn(String masterOrderSn, String message, String actionUser);

	/**
	 * 根据订单创建订单日志
	 * @param masterOrderInfo 订单信息
	 * @return MasterOrderAction
	 */
	MasterOrderAction createOrderAction(MasterOrderInfo masterOrderInfo);
	
	
	/**
	 * 保存订单日志，传入付款单状态，其他默认
	 * @param masterOrderSn 订单编码
	 * @param message 信息
	 * @param payStatus 支付状态
	 */
	void insertOrderAction(String masterOrderSn, String message, byte payStatus);

    /**
     * 保存订单日志
     * @param masterOrderSn 订单编码
     * @param message 日志信息
     * @param actionUser 操作人
     * @param logType 日志类型：0为订单操作，1为沟通
     */
    public void insertOrderActionBySn(String masterOrderSn, String message, String actionUser, Integer logType);

}
