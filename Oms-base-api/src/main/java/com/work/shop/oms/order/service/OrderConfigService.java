package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.OrderPeriodInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;

/**
 * 订单周期配置服务
 * @author QuYachu
 */
public interface OrderConfigService {
	
	/**
	 * 获取订单周期明细列表
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfoList();
	
	/**
	 * 获取订单周期明细
	 * @param request 参数
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request);

	/**
	 * 更新订单周期明细
	 * @param request 参数
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	OmsBaseResponse<OrderPeriodInfo> updateOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request);
}
