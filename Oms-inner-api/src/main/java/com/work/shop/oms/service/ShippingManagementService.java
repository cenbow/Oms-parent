package com.work.shop.oms.service;

import java.util.Map;




import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.ShippingManagementVO;
import com.work.shop.oms.utils.PageHelper;

public interface ShippingManagementService {
	/**
	 * 承运商管理：获取承运商查询列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getShippingQueryList(ShippingManagementVO vo,PageHelper helper);
	
	/**
	 * 承运商管理：新增承运商
	 * @param vo
	 * @return
	 */
	public Map doAddShipping(ShippingManagementVO vo);
	
	/**
	 * 承运商管理：切换承运商状态（启用变禁用，禁用变启用）
	 * @param shippingId
	 * @param enabled
	 * @return
	 */
	public Map changeStatus(String shippingId,String enabled);
	
	/**
	 * 承运商管理：保存编辑承运商信息
	 * @param vo
	 * @return
	 */
	public Map doSaveEdit(ShippingManagementVO vo);

}
