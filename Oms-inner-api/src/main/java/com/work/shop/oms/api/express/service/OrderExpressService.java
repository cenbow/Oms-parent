package com.work.shop.oms.api.express.service;

import java.util.List;

import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.express.bean.ExpressInfo;

/**
 * 物流接受查询服务
 * @author name
 *
 */
public interface OrderExpressService {
	
	/**
	 * 订单物流写入
	 * @param orderSn
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> orderExpress(String orderSn);
	
	/**
	 * 退单物流写入
	 * @param returnSn
	 * @return ReturnInfo<String>
	 */
	public ReturnInfo<String> orderReturnExpress(String returnSn);
	
	/**
	 * 订单物流查询（查询条件：订单号）
	 * @param tracing
	 * @return ReturnInfo<List<ExpressInfo>>
	 */
	ReturnInfo<List<ExpressInfo>> orderExpressQuery(OrderExpressTracing tracing);

	/**
	 * oms订单物流查询（查询条件：订单号、仓编码、快递单号）
	 * @param tracing
	 * @return ReturnInfo<List<ExpressInfo>>
	 */
	public ReturnInfo<List<ExpressInfo>> orderExpressOmsQuery(OrderExpressTracing tracing);

}