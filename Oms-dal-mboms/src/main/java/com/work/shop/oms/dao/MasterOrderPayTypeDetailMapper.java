package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.MasterOrderPayTypeDetail;
import com.work.shop.oms.common.bean.OrderItemPayDetail;

public interface MasterOrderPayTypeDetailMapper {
	public List<MasterOrderPayTypeDetail> getMasterOrderPayTypeDetail(Map paramMap);
	
	public List<OrderItemPayDetail> getOrderItemPayDetail(String masterOrderSn);

}
