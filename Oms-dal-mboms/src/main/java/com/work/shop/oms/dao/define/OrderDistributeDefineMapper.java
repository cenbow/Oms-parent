package com.work.shop.oms.dao.define;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderShipGoodsItem;
import com.work.shop.oms.common.bean.OrderExpressInfo;

public interface OrderDistributeDefineMapper {

	@ReadOnly
	List<String> selectQuestionOrder(Map param);
	
	@ReadOnly
	List<String> getPayTimeoutOrderInfo(Map<String,Object> map);

	@ReadOnly
	List<String> getShipTimeoutOrder(Map<String,Object> map);

	@ReadOnly
	List<String> getNoPayCloseOrderOfPos(Map<String,Object> map);
	
	@ReadOnly
	List<String> getNoPayNightOrderOfBanggo(Map<String,Object> map);
	
	@ReadOnly
	List<String> getLockOrderOverTimeOfBanggo(Map<String,Object> map);
	
	@ReadOnly
	List<String> getFinishOrderThreeMonth(Map<String,Object> map);
	
	@ReadOnly
	List<String> getGroupOrderNoPayClose(Map<String,Object> map);
	
	@ReadOnly
	List<OrderExpressInfo> queryOrderExpressInfoHtky(Map<String, Object> queryPara);
	
	@ReadOnly
	List<OrderExpressInfo> queryOrderExpressInfoYto(Map<String, Object> queryPara);
	
	@ReadOnly
	List<String> getCopyOrder(Map<String, Object> map);
	
	@ReadOnly
	int getCopyOrderCount(Map<String, Object> map);
	
	@ReadOnly
	List<String> getSwdiOrder(Map<String, Object> map);
	
	@ReadOnly
	int getSwdiOrderCount(Map<String, Object> map);
	
	@ReadOnly
	List<String> getPushERPOrder(Map<String, Object> map);
	
	@ReadOnly
	void moveOrderAction(Map<String, Object> map);
	
	@ReadOnly
	List<OrderItem> getGotOrder(Map<String, Object> map);
	
	@ReadOnly
	List<OrderItem> getRiderDistOrder(Map<String, Object> map);
	
	/**
	 * 获取B2B配送出库订单总数
	 * @param map
	 * @return
	 */
	@ReadOnly
	int getOrderDistributeOutCount(Map<String, Object> map);
	
	/**
	 * 获取B2B配送出库订单列表
	 * @param map
	 * @return
	 */
	@ReadOnly
	List<OrderItem> getOrderDistributeOutList(Map<String, Object> map);
	
	/**
	 * 获取B2B配送出库订单商品总数
	 * @param map
	 * @return
	 */
	@ReadOnly
	int getOrderDistributeOutGoodsCount(Map<String, Object> map);
	
	/**
	 * 获取B2B配送出库订单商品列表
	 * @param map
	 * @return
	 */
	@ReadOnly
	List<OrderShipGoodsItem> getOrderDistributeOutGoodsList(Map<String, Object> map);

    /**
	 * 获取超时自动收货列表
	 * @param map
	 * @return
	 */
    @ReadOnly
    List<String> getShipTimeoutOrderByOrder(Map<String, Object> map);
}