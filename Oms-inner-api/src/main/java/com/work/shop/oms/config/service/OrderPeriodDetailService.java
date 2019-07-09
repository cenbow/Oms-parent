package com.work.shop.oms.config.service;

import java.util.List;

import com.work.shop.oms.bean.OrderPeriodDetail;

/**
 * 订单周期明细服务
 * @author lemon
 *
 */
public interface OrderPeriodDetailService {

	/**
	 * 
	 * @param period
	 * @param type
	 * @return
	 */
	OrderPeriodDetail selectByPeriodAndType(int period, String type);
	
	/**
	 * 获取可显示处理的订单周期明细表
	 * @param flag
	 * @return
	 */
	public List<OrderPeriodDetail> selectList(int flag);
	
	/**
	 * 更新订单周期明细
	 * @param model
	 * @return
	 */
	public int updateOrderPeriodDetail(OrderPeriodDetail model);
}
