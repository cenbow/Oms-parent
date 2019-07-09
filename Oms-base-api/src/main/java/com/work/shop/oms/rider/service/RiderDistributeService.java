package com.work.shop.oms.rider.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.OrderQueryResponse;

/**
 * 骑手配送服务
 * @author QuYachu
 *
 */
public interface RiderDistributeService {
	
	/**
	 * 保存到骑手平台派单信息表中
	 * @param orderSnList
	 * @param orderType 1订单号、2外部交易号
	 * @return
	 */
	public ServiceReturnInfo<Boolean> saveRiderDistributeInfoList(List<String> orderSnList, int orderType);
	
	/**
	 * 下发订单到骑手平台
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> sendRiderDistribute(OrderRiderDistributeLog logModel);
	
	/**
	 * 下发订单到骑手平台
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> sendRiderDistributeByUser(OrderRiderDistributeLog logModel);
	
	/**
	 * 订单配送取消
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> sendOrderRiderCancel(OrderRiderDistributeLog logModel); 
	
	/**
	 * 获取骑手配送日志
	 * @param logModel
	 * @return
	 */
	public OrderRiderDistributeLog getModel(OrderRiderDistributeLog logModel);

	/**
	 * 查询需要骑手配送的配送订单
	 * @param request
	 * @return
	 */
	public OrderQueryResponse riderDistGet(OrderQueryRequest request);
	
	/**
	 * 骑手配送接单成功
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> riderDistAcceptOrders(OrderRiderDistributeLog logModel);
	
	/**
	 * 骑手配送取单成功
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> riderDistTakeOrders(OrderRiderDistributeLog logModel);
	
	/**
	 * 骑手配送完成
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> riderDistCompleted(OrderRiderDistributeLog logModel);
	
	/**
	 * 骑手配送取消
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<Boolean> riderDistCancel(OrderRiderDistributeLog logModel);
	
	/**
	 * 查询配送订单记录总数
	 * @param param
	 * @return
	 */
	public int getRiderDistributeCount(Map<String, Object> param);
	
	/**
	 * 查询配送订单记录列表
	 * @param param
	 * @return
	 */
	public List<OrderRiderDistributeLog> getOrderRiderList(Map<String, Object> param);
}
