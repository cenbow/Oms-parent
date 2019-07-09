package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.OptionPointsBean;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 点数API服务
 * @author lemon
 *
 */
public interface PointsApiService {

	/**
	 * 查询用户点数
	 * @param userName
	 * @param orderAmount
	 * @return
	 */
	public Float searchUserPayService(String userId, Double orderAmount);

	/**
	 * 操作点数
	 * 	userId:用户id 必须
	 * 	orderAmount:订单金额 必须
	 * 	orderNo：订单编号 必须
	 * 	orderPoints:订单使用点数 必须
	 * 	orderBv:订单bv 必须
	 * 	orderType: 操作类型  1冻结资金、2释放资金、3消费资金、4退返资金
	 * 
	 * @param pointsBean
	 * @return
	 */
	public ReturnInfo<Integer> optionPoints(OptionPointsBean pointsBean);
	
}
