package com.work.shop.oms.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.RegionManagementVO;
import com.work.shop.oms.service.RegionManagementService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.PageHelper;


@Controller
@RequestMapping(value = "regionManagement")
public class RegionManagementController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String SERVER_DOMAIN = ConfigCenter.getProperty("serverDomain");
	@Resource
	private RegionManagementService regionManagementService;
	
	
	/**
	 * 区域管理：根据父区域ID查询区域列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getRegionListByParentId")
	@ResponseBody
	public Paging getRegionListByParentId(RegionManagementVO vo,PageHelper helper){
		return regionManagementService.getRegionListByParentId(vo,helper);
	}
	
	/**
	 * 区域管理：添加下级区域
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doAddRegion")
	@ResponseBody
	public Map doAddRegion(RegionManagementVO vo){
		return regionManagementService.doAddRegion(vo);
	}
	
	/**
	 * 区域管理：删除regionId对应的区域及其所有下级区域
	 * @param regionId
	 * @return
	 */
	@RequestMapping(value="delRegion")
	@ResponseBody
	public Map delRegion(String regionId,String regionType){
		return regionManagementService.delRegion(regionId,regionType);
	}
	
	/**
	 * 区域管理：根据区域ID查询详细信息
	 * @param regionId
	 * @return
	 */
	@RequestMapping(value="getRegionInfoByRegionId")
	@ResponseBody
	public RegionManagementVO getRegionInfoByRegionId(String regionId){
		return regionManagementService.getRegionInfoByRegionId(regionId);
	}
	
	/**
	 * 区域管理：保存区域编辑信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doSaveEdit")
	@ResponseBody
	public Map doSaveEdit(RegionManagementVO vo,String flag){
		return regionManagementService.doSaveEdit(vo,flag);
	}

}
