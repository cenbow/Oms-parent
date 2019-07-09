package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.OrderDepotShipDetail;
import com.work.shop.oms.common.bean.OrderItemDepotDetail;

public interface OrderDepotShipDetailMapper {
	public List<OrderDepotShipDetail> getOrderDepotShipDetail(Map paramMap);

	public List<OrderItemDepotDetail> getOrderItemDepotDetail(String masterOrderSn);
}
