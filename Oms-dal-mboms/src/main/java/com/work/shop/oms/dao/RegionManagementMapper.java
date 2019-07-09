package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.RegionManagementVO;

public interface RegionManagementMapper {	
	
	/**
	 * 区域管理：区域下拉菜单数据
	 * @param parentId
	 * @return
	 */
	public List<Map<String,String>> getRegionQueryCondition(String parentId);
	
	/**
	 * 区域管理：根据父区域ID查询区域列表
	 * @param vo
	 * @return
	 */
	public List<RegionManagementVO> getRegionListByParentId(Map<String,Object> paramMap);
	
	/**
	 * 区域管理：根据父区域ID查询区域总记录数
	 * @param vo
	 * @return
	 */
	public int getRegionCountByParentId(Map<String,Object> paramMap);
	
	/**
	 * 区域管理：检查是否存在同级同名记录
	 * @param paramMap
	 * @return
	 */
	public int checkRegionName(RegionManagementVO vo);
	
	/**
	 * 区域管理：新增下级区域
	 * @param paramMap
	 * @return
	 */
	public int doAddRegion(RegionManagementVO vo);
	
	/**
	 * 区域管理：获取regionId下级子区域的ID集合
	 * @param regionId
	 * @return
	 */
	public List getChildRegionIdList(List paramList);
	
	/**
	 * 区域管理：删除paramList中的所有记录
	 * @param paramList
	 * @return
	 */
	public int delRegion(List paramList);
	
	/**
	 * 区域管理：查询regionId是否已存在
	 * @param regionId
	 * @return
	 */
	public int checkRegionId(String regionId);
	
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
	public int doSaveEdit(RegionManagementVO vo);
	
	/**
	 * 区域管理：区域编辑保存 同步保存所有下级区域的快递费用、EMS费用、货到付款费用、是否支持货到付款、是否支持POS刷卡、是否支持自提、是否支持货到付款验证手机号信息
	 * @param paramMap
	 * @return
	 */
	public int doSaveChildRegionInfo(Map<String,Object> paramMap);

}
