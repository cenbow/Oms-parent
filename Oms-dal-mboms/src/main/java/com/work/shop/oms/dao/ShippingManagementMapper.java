package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.ShippingManagementVO;

public interface ShippingManagementMapper {
	
	/**
	 * 承运商管理：获取承运商查询列表数量
	 * @param paramMap
	 * @return
	 */
	public int getShippingQueryListCount(Map<String,Object> paramMap);
	
	/**
	 * 承运商管理：获取承运商查询列表
	 * @param paramMap
	 * @return
	 */
	public List<ShippingManagementVO> getShippingQueryList(Map<String,Object> paramMap);

	/**
	 * 承运商管理：查询是否存在某些相同字段的记录
	 * @param paramMap
	 * @return
	 */
	public int checkSameRecord(Map<String,Object> paramMap);
	
	/**
	 * 承运商管理：新增承运商
	 * @param vo
	 * @return
	 */
	public int doAddShipping(ShippingManagementVO vo);
	
	/**
	 * 承运商管理：切换承运商状态（启用变禁用，禁用变启用）
	 * @param paramMap
	 * @return
	 */
	public int changeStatus(Map<String,Object> paramMap);
	
	/**
	 * 承运商管理：保存编辑承运商信息
	 * @param vo
	 * @return
	 */
	public int doSaveEdit(ShippingManagementVO vo);
}
