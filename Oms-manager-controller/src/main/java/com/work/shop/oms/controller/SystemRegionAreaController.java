package com.work.shop.oms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.service.SystemRegionAreaService;

@Controller
@RequestMapping(value = "common")
public class SystemRegionAreaController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SystemRegionAreaService systemRegionAreaService;
	
	/**
	 * 根据父id查找下一级区域列表
	 * @param request
	 * @param response
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "getSystemRegionAreaList")
	@ResponseBody
	public List<SystemRegionArea> getSystemRegionAreaList(HttpServletRequest request,
			HttpServletResponse response,String parentId){
		return systemRegionAreaService.getSystemRegionAreaList(parentId);
	}

}
