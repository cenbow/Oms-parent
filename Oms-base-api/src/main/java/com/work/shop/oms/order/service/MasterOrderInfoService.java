package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.*;

/**
 * 主订单服务
 * @author lemon
 *
 */
public interface MasterOrderInfoService {

	/**
	 * 创建订单方法，生成订单号，插入订单数据，生成付款单，生成发货单等操作
	 * 
	 * @param masterOrder 订单接口传入order对象
	 * @return OrderCreateReturnInfo 订单主表信息
	 */
	OrderCreateReturnInfo createOrder(MasterOrder masterOrder);
	
	/**
	 * 批量创建订单方法，生成订单号，插入订单数据，生成付款单，生成发货单等操作
	 * 
	 * @param masterOrders 订单接口传入order对象
	 * @return 订单主表信息
	 */
	OrdersCreateReturnInfo createOrders(List<MasterOrder> masterOrders);

	/**
	 * 根据订单号查询订单信息
	 * @param masterOrderSn 订单编码
	 * @return MasterOrderInfo
	 */
	MasterOrderInfo getOrderInfoBySn(String masterOrderSn);
	
	/**
	 * 通过外部交易号, 获取对应的订单信息
	 * @param outOrderSn 外部订单号
	 * @return MasterOrderInfo
	 */
	MasterOrderInfo getOrderInfoByOutOrderSn(String outOrderSn);

	/**
	 * 处理订单生成后续操作
	 * 
	 * @param masterOrderSn 订单编码
	 * @param validateOrder 校验订单信息
	 * @param ocpbStatus 平台币状态
	 * @param qt 问题单状态
	 */
	void dealOther(String masterOrderSn, ValidateOrder validateOrder, OcpbStatus ocpbStatus, int qt);

	/**
	 * 处理订单账期支付
	 * @param masterOrderSn
	 * @return ReturnInfo<Boolean>
	 */
	ReturnInfo<Boolean> processOrderPayPeriod(String masterOrderSn);
}
