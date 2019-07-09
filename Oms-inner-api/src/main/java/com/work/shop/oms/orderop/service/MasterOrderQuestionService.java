package com.work.shop.oms.orderop.service;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;


/**
 * 主订单问题单、正常单操作
 * @author lemon
 *
 */
public interface MasterOrderQuestionService {

	
	/**
	 * 添加问题单操作共同接口
	 * @param orderStatus
	 * @return
	 */
	ReturnInfo addQuestionOrder(OrderStatus orderStatus);
	
	/**
	 * 添加问题单操作共同接口
	 * @param info
	 * @param orderStatus
	 * @return
	 */
	ReturnInfo addCommonQuestionOrder(MasterOrderInfo info, OrderStatus orderStatus);

	/**
	 * 返回正常单
	 * @param orderStatus
	 * @return
	 */
	public ReturnInfo returnNormal(OrderStatus orderStatus);

	/**
	 * 返回正常单
	 * @param info
	 * @param orderStatus
	 * @return
	 */
	public ReturnInfo returnNormalCommon(MasterOrderInfo info, OrderStatus orderStatus);

}
