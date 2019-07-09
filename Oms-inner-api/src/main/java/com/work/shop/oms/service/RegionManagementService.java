package com.work.shop.oms.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.RegionManagementVO;
import com.work.shop.oms.utils.PageHelper;

public interface RegionManagementService {
	
	/**
	 * 区域管理：区域下拉菜单数据
	 * @param parentId
	 * @return
	 */
	public List<Map<String,String>> getRegionQueryCondition(String parentId);
	
	/**
	 * 区域管理：根据父区域ID查询区域列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getRegionListByParentId(RegionManagementVO vo,PageHelper helper);
	
	/**
	 * 区域管理：添加下级区域
	 * @param vo
	 * @return
	 */
	public Map doAddRegion(RegionManagementVO vo);
	
	/**
	 * 区域管理：删除regionId对应的区域及其所有下级区域
	 * @param regionId
	 * @return
	 */
	public Map delRegion(String regionId,String regionType);
	
	/**
	 * 区域管理：根据区域ID查询详细信息
	 * @param regionId
	 * @return
	 */
	public RegionManagementVO getRegionInfoByRegionId(String regionId);
	
	/**
	 * 区域管理：保存区域编辑信息
	 * @param vo
	 * @return
	 */
	public Map doSaveEdit(RegionManagementVO vo,String flag);

}
