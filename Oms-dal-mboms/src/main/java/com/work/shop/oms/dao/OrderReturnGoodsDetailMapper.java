package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.OrderReturnGoods;

public interface OrderReturnGoodsDetailMapper {
	
	public List<OrderReturnGoods> getReturnNumberByMasOrdSn(Map<String,Object> paramMap);
	
	List<String> getReturnToErpData(Map<String,Object> map);
	
	public List<OrderReturnGoods> getStorageTimesList(Map<String,Object> paramMap);

}
