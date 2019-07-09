package com.work.shop.oms.config.service;

import java.util.List;

import com.work.shop.oms.bean.OrderCustomDefine;

public interface OrderCustomDefineService {
	
	public List<OrderCustomDefine> selectDefines(Short type);
	
	/**
	 * 获取商家自定义信息配置表
	 * @param type
	 * @return List<OrderCustomDefine>
	 */
	List<OrderCustomDefine> selectCustomDefine(OrderCustomDefine orderCustomDefine);

	
	public OrderCustomDefine selectCustomDefineByCode(String code);
}
