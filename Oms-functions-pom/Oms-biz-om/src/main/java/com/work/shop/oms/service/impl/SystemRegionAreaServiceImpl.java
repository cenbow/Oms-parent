package com.work.shop.oms.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.service.SystemRegionAreaService;

@Service
public class SystemRegionAreaServiceImpl implements SystemRegionAreaService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
@Autowired
	private SystemRegionAreaMapper systemRegionAreaMapper;

	@Override
	public List<SystemRegionArea> getSystemRegionAreaList(String parentId) {
		// TODO Auto-generated method stub
		List<SystemRegionArea> list = new ArrayList<SystemRegionArea>();
		try{
			if(!"".equals(parentId)&&parentId!=null){
				SystemRegionAreaExample example = new SystemRegionAreaExample();
				example.or().andParentIdEqualTo(parentId);
				example.setOrderByClause("region_id asc");
				list = systemRegionAreaMapper.selectByExample(example);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 根据区域类型取区域地址列表
	 * @param regionType
	 * @return
	 */
	@Override
	public List<SystemRegionArea> getSystemRegionListByType(Integer regionType) {
		logger.debug("根据区域类型取区域地址列表:regionType=" + regionType);
		List<SystemRegionArea> regions = null;
		if (regionType == null) {
			logger.error("参数为空值");
			return null;
		}
		try {
			SystemRegionAreaExample s = new SystemRegionAreaExample();
			s.or().andRegionTypeEqualTo(regionType);
			regions = systemRegionAreaMapper.selectByExample(s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据区域类型取区域地址列表失败", e);
		}
		logger.debug("根据区域类型取区域地址列表结束！");
		return regions;
	}

	/**
	 * 根据区域类型以及父区域ID取区域地址列表
	 * @param regionType			区域类型不能为空
	 * @param pId					区域父ID可以为空
	 * @return
	 */
	@Override
	public List<SystemRegionArea> getSystemRegionListByTypeAndPid(Integer regionType, String pId) {
		logger.debug("根据区域类型取区域地址列表:regionType=" + regionType + ";pId=" + pId);
		if (regionType == null) {
			logger.error("参数为空值");
			return null;
		}
		List<SystemRegionArea> regions = null;
		try {
			SystemRegionAreaExample example = new SystemRegionAreaExample();
			SystemRegionAreaExample.Criteria criteria = example.or();
			criteria.andRegionTypeEqualTo(regionType);
			if (pId != null) {
				criteria.andParentIdEqualTo(pId);
			}
			regions = systemRegionAreaMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据区域类型以及父区域ID取区域地址列表失败", e);
		}
		logger.debug("根据区域类型以及父区域ID取区域地址列表结束！");
		return regions;
	}

    /**
     * 根据id获取地区信息
     * @param regionIds
     * @return
     */
    public List<SystemRegionArea> getSystemRegionAreaById(List<String> regionIds) {
        SystemRegionAreaExample example = new SystemRegionAreaExample();
        example.or().andRegionIdIn(regionIds);
        return systemRegionAreaMapper.selectByExample(example);
    }

}
