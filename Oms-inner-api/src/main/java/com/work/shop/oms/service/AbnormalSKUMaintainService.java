package com.work.shop.oms.service;

import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderSku;
import com.work.shop.oms.utils.PageHelper;


public interface AbnormalSKUMaintainService {
	/**
	 * 异常SKU调整：查询待调整列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getAbnormalSKUList(OrderSku vo,PageHelper helper);
	
	/**
	 * 异常SKU调整：保存调整单
	 * @param vo
	 * @return
	 */
	public Map doSaveEdit(OrderSku vo);
	
	/**
	 * 根据sku查询颜色和尺寸
	 * @param skuSn
	 * @return
	 */
	public Map selectColorAndSizeBySKU(String skuSn);

}
