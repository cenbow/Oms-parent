package com.work.shop.oms.orderop.service;

import java.util.List;

import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.exception.OrderException;


/**
 * 主子订单（分配单）操作服务
 * @author lemon
 *
 */
public interface OrderDistributeOpService {

	/**
	 * 订单锁定
	 *
	 * @param masterOrderSn 订单编码
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return ReturnInfo
	 * @throws OrderException
	 */
	ReturnInfo lockOrder(String masterOrderSn, OrderStatus orderStatus) throws OrderException;

	/**
	 * 订单解锁
	 * @param masterOrderSn
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return ReturnInfo
	 * @throws OrderException
	 */
	ReturnInfo unLockOrder(String masterOrderSn, OrderStatus orderStatus) throws OrderException;

	/**
	 * 订单通知收款
	 * 
	 * @param masterOrderSn
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo noticeReceivables(String masterOrderSn, OrderStatus orderStatus) throws Exception;

	/**
	 * 复活订单（返回订单上一状态）
	 * 
	 * @param orderSn
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo reviveOrder(String orderSn, OrderStatus orderStatus) throws Exception;

	/**
	 * 订单结算
	 * 
	 * @param orderSn 订单编码
	 * @param orderStatus message:备注;adminUser:操作人;userId:操作人唯一编号
	 * @return ReturnInfo<String>
	 * @throws Exception
	 */
	ReturnInfo<String> settleOrder(String orderSn, OrderStatus orderStatus) throws Exception;

	/**
	 * 订单分配
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo allocation(String orderSn, OrderStatus orderStatus) throws Exception;

	/**
	 * 主订单分配
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo allocationByMaster(String masterOrderSn, OrderStatus orderStatus) throws Exception;
	/**
	 * 复制订单
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo copyOrder(String orderSn, OrderStatus orderStatus) throws Exception;

	/**
	 * 重新获取分仓发货信息
	 * @param masterOrderSn
	 * @param orderSn
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo sWDI(String masterOrderSn, String orderSn, OrderStatus orderStatus) throws Exception;

	/**
	 *  重新获取分仓发货信息放入MQ中
	 *  type 0 : 订单 1：交货单
	 * @param orderSns
	 */
	public void swdiPushMQ(String[] orderSns, String type);
	
	/**
	 * 判断主订单订单状态
	 * @param masterOrderSn
	 * @return
	 */
	public ReturnInfo judgeMasterOrderStatus(String masterOrderSn);
	
	/**
	 * 将订单从历史表中移到近期表
	 * @param historyMasterOrderSn
	 * @return
	 */
	public ReturnInfo moveOrderFromHistoryToRecent(String historyMasterOrderSn,OrderStatus orderStatus);
	
	/**
	 * 将订单从近期表移入到历史表中
	 * @param MasterOrderSn
	 * @return
	 */
	public ReturnInfo moveOrderFromRecentToHistory(String MasterOrderSn);

	/**
	 * 订单重新拆单
	 * 
	 * @param masterOrderSn
	 * @param orderStatus
	 * @return
	 */
	public ReturnInfo reSplitOrder(String masterOrderSn, OrderStatus orderStatus);
	
	public ReturnInfo reCreateOrder(String masterOrderSn, OrderStatus orderStatus);
	
	/**
	 * 订单日志迁移
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus   adminUser：操作人;isHistory: 是否历史单 0：否 1：是
	 * @return
	 */
	public ReturnInfo moveOrderAction(String masterOrderSn, OrderStatus orderStatus);
	
	public ReturnInfo handworkOrder(String shopCode, String userId, List<String> skus);

	/**
	 * 
	 * @param orderStatus orderSn storeAddress actionUser
	 * @return
	 */
	public ReturnInfo<String> sendGotCode(OrderStatus orderStatus);
}
