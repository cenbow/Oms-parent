package com.work.shop.oms.dao.define;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;

import com.work.shop.oms.api.bean.UserOrderInfo;
import com.work.shop.oms.bean.OrderGoods;
import com.work.shop.oms.bean.OrderPay;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.StockGoods;


public interface OrderMapper {
	
/*	@ReadOnly
	List<StockGoods> getOrderGoodsByOrderSn(String orderSn);
	
	@Writer
	void occupyNewOrderGoodsSendNumber(Map<String, Object> params);
	@Writer
	void reviveOrder(Map<String, Object> params);
	@ReadOnly
	List<Map<String, Object>> getCustumCodeAndOrderFromByOrderSn(String orderSn);

	@ReadOnly
	Integer getSystemConfigStockNumber();

	@ReadOnly
	String getVedpotCodeByOrderSn(String orderSn);
	@ReadOnly
	String thirteenChangeToEleven(String sku);
	
	@ReadOnly
	List<OrderGoods> selectOrderGoodsByGoods(Map<String, Object> params);*/
	
	@ReadOnly
	List<GoodsReturnChangeInfoVO> selectGoodsReturnChangeBySn(String orderSn);
	
/*	
	
	@ReadOnly
	List<UserOrderInfo> selectUserOrderInfo(Map<String, Object> params);
	
	@ReadOnly
	List<UserOrderInfo> selectUserOrderInfoByMobile(Map<String, Object> params);
	
	@ReadOnly
	List<UserOrderInfo> selectUserOrderReturnInfo(Map<String, Object> params);
	
	@ReadOnly
	Integer selectUserOrderInfoByMobileCount(Map<String, Object> params);
	
	@ReadOnly
	Integer selectUserOrderCountByType(Map<String, Object> params);

	@ReadOnly
	OrderPay selectMaxOrderPayByOrderSn(String orderSn);*/
	

}