package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.OrderDistribute;

public interface OrderDistributeDetailMapper {
	
	public List<OrderDistribute> getOrderDistributeList(Map paramMap);

}
