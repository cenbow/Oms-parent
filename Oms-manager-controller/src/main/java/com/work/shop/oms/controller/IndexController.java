package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.service.CommonService;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AuthResource;
import com.work.shop.united.client.dataobject.DataPermission;
import com.work.shop.united.client.facade.AuthCenterFacade;
import com.work.shop.united.client.filter.config.Config;

@Controller
public class IndexController extends BaseController {
	private static Logger logger = Logger.getLogger(IndexController.class);
	@Resource
	private CommonService commonService;
	
	/**
	 * 获取权限资源列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deskTop")
	public ModelAndView getAuthReource(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		List<AuthResource> authResources = null;
		// 获取桌面图标资源
		// 获取管理URL资源
		// 获取页面资源
		try {
			AuthCenterFacade auth = Config.getAuthCenterFacade();
			List<DataPermission> authList = auth.allDataPermissionOfApp(getUserName(request), Config.getAppId());

			List<String> authCodes = new ArrayList<String>();
			for (DataPermission premission : authList) {
				authCodes.add(premission.getCode());
			}
			HttpSession session = request.getSession();
			List<SystemOmsResource> desktopList= null;
			SystemOmsResource resource = new SystemOmsResource();
			resource.setResourceType("desktop_group");
			desktopList = commonService.selectSystemOmsResource(resource);
			if (StringUtil.isListNotNull(desktopList)) {
				authResources = new ArrayList<AuthResource>();
				for (SystemOmsResource obj : desktopList) {
					AuthResource pAuthResource = new AuthResource();
					pAuthResource.setResourceName(obj.getResourceName());
					pAuthResource.setIconCls("grid-shortcut");
					pAuthResource.setResourceCode(obj.getResourceCode());
					pAuthResource.setModule("tree-win-001");
					pAuthResource.setMenu(true);
					pAuthResource.setLeaf(false);
					pAuthResource.setFlag(2);
					pAuthResource.setResourceUrl("");
					authResources.add(pAuthResource);
					resource.setResourceType(null);
					resource.setParentCode(obj.getResourceCode());
					List<SystemOmsResource> groupList = commonService.selectSystemOmsResource(resource);
					for (SystemOmsResource omsResource : groupList) {
						//    { name: '子菜单2', iconCls: 'cpu-shortcut', module: '', id:"1011-002", menu:false, leaf:true, flag:3, url:"http://www.banggo.com" },
						AuthResource authResource = new AuthResource();
						authResource.setResourceName(omsResource.getResourceName());
						authResource.setIconCls("cpu-shortcut");
						authResource.setResourceCode(obj.getResourceCode() + "-" + omsResource.getResourceCode());
						authResource.setModule("");
						authResource.setMenu(false);
						authResource.setLeaf(true);
						authResource.setFlag(3);
						authResource.setResourceUrl(omsResource.getResourceUrl());
						authResources.add(authResource);
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取权限资源列表信息异常", e);
		}
		System.out.println(JSON.toJSONString(authResources));
		mav.addObject("authResources", JSON.toJSONString(authResources));
		mav.setViewName("../desktop");
		return mav;
	}

}
