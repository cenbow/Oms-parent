package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.MasterOrderAddressDetail;
import com.work.shop.oms.bean.MasterOrderAddressInfo;

public interface MasterOrderAddressDetailMapper {
	
	public MasterOrderAddressDetail selectMasOrdAddDetByOrderSnByMasterOrderSn(Map paramMap);
	
	public List<MasterOrderAddressInfo> getBatchDecodeDoloadList(Map<String,Object> paramMap);

}
