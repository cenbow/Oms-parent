package com.work.shop.oms.dao;

import java.util.Map;

import com.work.shop.oms.common.bean.MasterOrderDetail;

/**
 * 自定义mapper
 * @author lyh
 *
 */
public interface MasterOrderInfoDetailMapper {
	
	public MasterOrderDetail selectMasOrdDetByMasterOrderSn(Map paramMap);
	
	public MasterOrderDetail getMasterOrderPayInfo(Map paramMap);
	
}
