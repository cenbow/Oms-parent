package com.work.shop.oms.service;

import java.util.List;

import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemShipping;

public interface CommonService {
	
	public List<OrderCustomDefine> selectOrderCustomDefine(OrderCustomDefine define);
	
	List<SystemShipping> selectSystemShippingList(SystemShipping systemShipping);
	
	/**
     * 获取退货仓库信息
     * @param area
     * @return
     */
    List<ErpWarehouseList> selectErpWarehouseList(ErpWarehouseList erpWarehouseList);
    
    public List<SystemOmsResource> selectSystemOmsResource(
			SystemOmsResource resource);

}
