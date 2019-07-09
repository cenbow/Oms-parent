package com.work.shop.oms.service;

import java.util.List;

import com.work.shop.oms.bean.SystemRegionArea;

public interface SystemRegionAreaService {
	
	public List<SystemRegionArea> getSystemRegionAreaList(String parentId);
	
	
	
	
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
     * 根据id获取地区信息
     * @param regionIds
     * @return
     */
    public List<SystemRegionArea> getSystemRegionAreaById(List<String> regionIds);

}
