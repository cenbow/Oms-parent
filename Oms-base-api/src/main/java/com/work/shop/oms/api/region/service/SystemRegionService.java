package com.work.shop.oms.api.region.service;

import java.util.List;

import com.work.shop.oms.api.param.bean.SystemRegionInfo;
import com.work.shop.oms.api.param.bean.SystemRegionSimpleInfo;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.common.bean.ApiReturnData;

public interface SystemRegionService {

	/**
	 * 通过regionId 列表 查询区域地址信息
	 * @param paraList
	 * @return
	 */
	List<SystemRegionArea> getSystemRegionListByRegId(List<String> paraList);
	
	/**
	 * 根据区域类型取区域地址列表
	 * @param regionType			区域类型不能为空
	 * @return
	 */
	List<SystemRegionArea> getSystemRegionListByType(Integer regionType);

	/**
	 * 根据区域类型以及父区域ID取区域地址列表
	 * @param regionType			区域类型不能为空
	 * @param pId					区域父ID可以为空
	 * @return
	 */
	List<SystemRegionArea> getSystemRegionListByTypeAndPid(Integer regionType, String pId);
	/**
	 * 将地区消息保存至缓存
	 */
	String setSystemRegionToRedis();
	/**
	 * 根据地区层级或者pid取下一级所有地区消息
	 * @param region_type
	 * @param region_id
	 * @return
	 */
	String getSystemRegionArea(String region_type,String region_id);
	/**
	 * 根据地区层级或者pid取下一级所有地区消息
	 * @param region_type
	 * @param region_id
	 * @return
	 */
	SystemRegionInfo getSystemRegionAreaName(String region_id);
	/**
	 * 获取中国下所有地区消息
	 * @return
	 */
	List<SystemRegionSimpleInfo> getChinaSystemRegionArea();
	/**
	 * 根据老地区id获取新地区id（平台创建订单使用）
	 * @return
	 */
	ApiReturnData getRegionAreaByOldId(String cCode,String pCode,String ciCode,String dCode);
}
