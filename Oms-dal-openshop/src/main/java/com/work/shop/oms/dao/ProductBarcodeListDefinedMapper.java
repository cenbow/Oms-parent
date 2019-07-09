package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.bean.ProductBarcodeList;

public interface ProductBarcodeListDefinedMapper {

	
	@ReadOnly
	List<ProductBarcodeList> selectProductBarcodeList(Map<String, Object> map);

	/**
	 * 根据sku查询颜色和尺寸
	 * @param skuSn
	 * @return
	 */
	public List<Map<String,String>> selectColorAndSizeBySKU(String skuSn);
}
