package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemOmsRole;
import com.work.shop.oms.bean.SystemOmsRolePermisionExample;
import com.work.shop.oms.bean.SystemOmsRolePermisionKey;
import com.work.shop.oms.bean.SystemResource;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.SystemOmsResourceVo;
import com.work.shop.oms.dao.SystemOmsRoleMapper;
import com.work.shop.oms.dao.SystemOmsRolePermisionMapper;
import com.work.shop.oms.service.SystemResourceService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;

@Controller
@RequestMapping(value = "systemResource")
public class SystemResourceController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SystemResourceService systemResourceService;
	
	@Resource
	private SystemOmsRolePermisionMapper systemOmsRolePermisionMapper;
	
	
	@Resource
	private SystemOmsRoleMapper systemOmsRoleMapper;

	/**
	 * 取权限列表
	 ***/
	@RequestMapping(value = "getsystemResourceList.spmvc")
	public ModelAndView getsystemResourceList(HttpServletRequest request,
			HttpServletResponse response, SystemResource systemResource, PageHelper helper)
			throws Exception {
		logger.info("SystemResourceController.systemResourceService start " + " [ SystemResource =  " + systemResource + "  ]");
		try {
			Paging paging = this.systemResourceService.getSystemResourceList(systemResource, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 添加权限
	 ***/
	@RequestMapping(value = "addSystemResource.spmvc")
	public ModelAndView addystemResource(HttpServletRequest request,
			HttpServletResponse response, SystemResource systemResource,
			String method) throws Exception {
		logger.info("SystemResourceController.addystemResource start  [ SystemResource =  "
				+ systemResource + "    method =" + method + "     ]");
		ModelAndView mav = new ModelAndView();
		if (StringUtil.isNotBlank(method) && "init".equals(method)) {
			mav.setViewName("sysResource/addSystemResource");
			return mav;
		} else if (StringUtil.isNotBlank(method) && "add".equals(method)) {
			JsonResult jsonResult = new JsonResult();
			try{
				systemResource.setOperUser(getUserName(request));
				int re = systemResourceService.addSystemResource(systemResource);
				if( 0 < re){
					jsonResult.setIsok(true);
					jsonResult.setMessage("添加成功!");
					writeObject(jsonResult, response);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("SystemResourceController.addystemResource  添加失败   [ Exception" + e +  "  ] ");
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败 !");
				writeObject(jsonResult, response);
				return null;
			}
			return null;
		}
		return null;
	}

	@RequestMapping(value = "updateSystemResource.spmvc")
	public ModelAndView updateSystemResource(HttpServletRequest request,
			HttpServletResponse response, SystemResource systemResource,
			String method) throws Exception {
		logger.info("SystemResourceController.updateSystemResource start  [ SystemResource =  "
				+ systemResource + "    method =" + method + "     ]");
		ModelAndView mav = new ModelAndView();
		JsonResult jsonResult = new JsonResult();
		//初始化
		if (StringUtil.isNotBlank(method) && "init".equals(method)) {
			SystemResource sr = systemResourceService.selectSystemResource(systemResource);
			mav.addObject("sr", sr);
			mav.setViewName("sysResource/addSystemResource");
			return mav;
		} else if (StringUtil.isNotBlank(method) && "update".equals(method)) {
			try{
				systemResource.setOperUser(getUserName(request));
				int re = systemResourceService.updateSystemResource(systemResource);
				if( 0 < re){
					jsonResult.setIsok(true);
					jsonResult.setMessage("修改成功!");
					writeObject(jsonResult, response);
					return null;
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("SystemResourceController.addystemResource  修改失败   [ Exception" + e +  "  ] ");
				jsonResult.setIsok(false);
				jsonResult.setMessage("修改失败 !");
				writeObject(jsonResult, response);
				return null;
			}
		}
		jsonResult.setIsok(false);
		jsonResult.setMessage("无数据修改 !");
		writeObject(jsonResult, response);
		return null;
		
	}
	
	/**
	 *删除资源
	 *String ids 多个id 
	 ***/
	@RequestMapping(value = "deleteSystemResource.spmvc")
	public ModelAndView deleteSystemResource(HttpServletRequest request,
			HttpServletResponse response, String ids) throws Exception {
		
		logger.info("SystemResourceController.deleteSystemResource start  [ ids = "+ids+  "]");
		
		String [] idArr= ids.split(",");
		List<Integer> list = new ArrayList<Integer>();
		
		JsonResult jsonResult = new JsonResult();
		
		for(String id: idArr){
			list.add(Integer.valueOf(id));
		}
		try {
			int delNum  = systemResourceService.deleteSystemResourceList(list);
			if( 0 < delNum){
				jsonResult.setIsok(true);
				jsonResult.setMessage("删除成功!");
				writeObject(jsonResult, response);
				return null;
			}
			
		} catch(Exception e){
			e.printStackTrace();
			logger.info("SystemResourceController.deleteSystemResource  修改失败   [ Exception  " + e +  "  ] ");
			jsonResult.setIsok(false);
			jsonResult.setMessage("修改失败 !");
			writeObject(jsonResult, response);
			return null;
		}
		
		jsonResult.setIsok(false);
		jsonResult.setMessage("无数据删除!");
		writeObject(jsonResult, response);

		return null;
	}
	
	/***
	 * 
	 ***/
	@RequestMapping(value = "addSystemAclResourceByExtjs5.spmvc")
	public ModelAndView addSystemAclResourceByExtjs5(HttpServletRequest request,
			HttpServletResponse response, String roleCode, String method) throws Exception {

		logger.info("SystemResourceController.addSystemAclResourceByExtjs5 start  [ resourceId = "+roleCode+  "]");

		JsonResult jsonResult = new JsonResult();
	
		if (StringUtil.isNotBlank(method) && "init".equals(method)) {
				
			List<SystemOmsResourceVo> list = systemResourceService.getAssigningPermissionsByResourceCode(roleCode);
	
				jsonResult.setData(list);
				writeObject(jsonResult,response);
				return null;
		
		
		} else if(StringUtil.isNotBlank(method) && "add".equals(method)){ //添加系统资源
		
		
			
			
		} else if(StringUtil.isNotBlank(method) && "update".equals(method)){//修改系统资源
			
		}
		
		return null;
	}
	
	/**
	 * 取权限列表
	 ***/
	@RequestMapping(value = "getsystemOmsResourceList.spmvc")
	public ModelAndView getsystemOmsResourceList(HttpServletRequest request,
			HttpServletResponse response, SystemOmsResource systemOmsResource, PageHelper helper,String method)
			throws Exception {
		logger.info("SystemResourceController.getsystemOmsResourceList start " + " [ SystemOmsResource =  " + systemOmsResource + "  ]");
		
		ModelAndView mav = new ModelAndView();
		
		if(StringUtil.isNotBlank(method) && method.equals("init")) {
			mav.setViewName("../mbapp/systemResourcePage");
			return mav;
		}
		try {
			Paging paging = this.systemResourceService.getSystemOmsResourceList(systemOmsResource, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			logger.error("获取资源列表异常", e);
		}
		return null;

	}
	
	/**
	 * 根据资源id取资源信息
	 ***/
	@RequestMapping(value = "getSystemOmsResourceByResourceId.spmvc")
	public ModelAndView getSystemOmsResourceByResourceId(HttpServletRequest request,
			HttpServletResponse response, SystemOmsResource systemOmsResource, PageHelper helper)
			throws Exception {	
		logger.info("SystemResourceController.getSystemOmsResourceByResourceId start " + " [ SystemOmsResource =  " + systemOmsResource + "  ]");
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		try {
			
			SystemOmsResource sor = systemResourceService.getSystemOmsResourceByResourceId(systemOmsResource);
			if(null != sor){
				jsonResult.setIsok(true);
				jsonResult.setData(sor);
			
			}		
			writeObject(jsonResult, response);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/***
	 * 添加或修改oms资源信息 
	 ***/
	@RequestMapping(value = "addSystemOmsResource.spmvc")
	public ModelAndView addSystemOmsResource(HttpServletRequest request,
			HttpServletResponse response, SystemOmsResource systemOmsResource, String method) throws Exception {

		JsonResult jsonResult = new JsonResult();
	
		if(StringUtil.isNotBlank(method) && "add".equals(method)){

			List<SystemOmsResource> list = systemResourceService.querySystemOmsResourceListByParentCodeAndResourceCode(systemOmsResource);
	
			if(StringUtil.isNotNullForList(list)){
				 jsonResult.setIsok(false);
				  jsonResult.setMessage("父类编码和资源编码已存在");
			
				  writeObject(jsonResult,response);
			
				  return null;
			}
			
			
			try{
			   int addCount = systemResourceService.addSystemOmsResource(systemOmsResource);
			   
			   if(0<addCount){
				   jsonResult.setIsok(true);
			
				   jsonResult.setMessage("添加成功");
			
				   writeObject(jsonResult,response);
				   return null;
			   }
			} catch(Exception e){
				e.printStackTrace();
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败");
	
				writeObject(jsonResult,response);
				return null;
			}

		} else if(StringUtil.isNotBlank(method) && "update".equals(method)){
		
			try{
				int updateCount = systemResourceService.updateSystemOmsResource(systemOmsResource);
				 
				if(0<updateCount){
					jsonResult.setIsok(true);
				 	jsonResult.setMessage("修改成功");
		
				 	writeObject(jsonResult,response);
				 	return null;
				}
			} catch(Exception e){
				e.printStackTrace();
				jsonResult.setIsok(false);
				jsonResult.setMessage("修改失败");
	
				writeObject(jsonResult,response);
				return null;
			}
			
		}
		
		return null;
	}
	
	/**
	 * 取角色列表
	 ***/
	@RequestMapping(value = "getSystemOmsRoleList.spmvc")
	public ModelAndView getSystemOmsRoleList(HttpServletRequest request,
			HttpServletResponse response, SystemOmsRole systemOmsRole, PageHelper helper, String method)
			throws Exception {
		logger.info("SystemResourceController.getSystemOmsRoleList start " + " [ SystemOmsResource =  " + systemOmsRole + "  ]");
		
		ModelAndView mav = new ModelAndView();
		
		if(StringUtil.isNotBlank(method) && method.equals("init")) {
			mav.setViewName("../mbapp/systemRolePage");
			return mav;
		}
		
		try {
			Paging paging = systemResourceService.getSystemOmsRoleList(systemOmsRole, helper);
			writeJson(paging, response);	
		} catch (Exception e) {
			logger.error("SystemResourceController.getSystemOmsRoleList ["+e.getMessage()+"]");
			e.printStackTrace();
		}
		
		logger.info("SystemResourceController.getSystemOmsRoleList end");
		return null;

	}
	
	/**
	 * 根据角色编码取角色信息
	 ***/
	@RequestMapping(value = "getSystemOmsRoleByResourceCode.spmvc")
	public ModelAndView getSystemOmsRoleByResourceId(HttpServletRequest request,
			HttpServletResponse response, SystemOmsRole systemOmsRole, PageHelper helper)
			throws Exception {	
		logger.info("SystemResourceController.getSystemOmsResourceByResourceId start " + " [ SystemOmsResource =  " + systemOmsRole + "  ]");
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		try {
			SystemOmsRole sor = systemResourceService.getSystemOmsRoleByResourceCode(systemOmsRole);
			if(null != sor){
				jsonResult.setIsok(true);
				jsonResult.setData(sor);
			
			}		
			writeObject(jsonResult, response);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * 进入分派权限页面 
	 ***/
	@RequestMapping(value = "enterAssigningPermissionsPage.spmvc")
	public ModelAndView enterAssigningPermissionsPage(HttpServletRequest request,
			HttpServletResponse response, String roleCode) throws Exception {
	
			try{	
				ModelAndView mav = new ModelAndView();
				mav.addObject("roleCode", roleCode);
				mav.setViewName("../mbapp/view/systemRole/systemRoleDispatchEditPage");
				return mav;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		
	}
	
	/**
	 * 进入分派权限js
	 ***/
	@RequestMapping(value = "enterAssigningPermissionsJs.spmvc")
	public ModelAndView enterAssigningPermissionsJs(HttpServletRequest request,
			HttpServletResponse response, String roleCode) throws Exception {
		
			Map<String,Object>data = new HashMap<String, Object>();
			
			try{
				data.put("roleCode", roleCode);
				outPrintJson(response, data);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			return null;
		
	}

	@RequestMapping(value = "saveDispatchRole.spmvc")
	public ModelAndView saveDispatchRole(HttpServletRequest request,
			HttpServletResponse response, String roleCode, String resourceId) throws Exception {
			
			JsonResult jsonResult = new JsonResult();
			
			if(StringUtil.isBlank(roleCode)){
				jsonResult.setMessage("角色为空");
				logger.info("SystemResourceController.saveDispatchRole  " + " [ roleCode is null  ]");
				jsonResult.setIsok(false);
				writeObject(jsonResult, response);
				return null;
			}
			
			
			try{
				SystemOmsRolePermisionExample systemOmsRolePermisionExample = new SystemOmsRolePermisionExample();
				SystemOmsRolePermisionExample.Criteria criteria = systemOmsRolePermisionExample.or();
				
				if(StringUtil.isNotBlank(roleCode)){
					criteria.andRoleCodeEqualTo(roleCode);
				}

				systemOmsRolePermisionMapper.deleteByExample(systemOmsRolePermisionExample);
				
				String [] resourceIds = resourceId.split(",");
					
				if( 0 < resourceIds.length){
					for(String id :resourceIds ){
						
						SystemOmsRolePermisionKey SystemOmsRolePermision = new SystemOmsRolePermisionKey();
						SystemOmsRolePermision.setResourceId(Integer.valueOf(id));
						SystemOmsRolePermision.setRoleCode(roleCode);
						
						systemOmsRolePermisionMapper.insert(SystemOmsRolePermision);
					}
					jsonResult.setMessage("添加成功！");
					jsonResult.setIsok(true);
					writeObject(jsonResult, response);
					return null;
				} else{
					jsonResult.setMessage("添加失败！");
					jsonResult.setIsok(true);
					writeObject(jsonResult, response);
					return null;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			return null;
		
	}
	
	/***
	 * 添加或修改oms资源信息 
	 ***/
	@RequestMapping(value = "addSystemOmsRole.spmvc")
	public ModelAndView addSystemOmsRole(HttpServletRequest request,
			HttpServletResponse response, SystemOmsRole systemOmsRole, String method) throws Exception {

		JsonResult jsonResult = new JsonResult();
			
		List<SystemOmsRole> list = systemResourceService.querySystemOmsRoleListByRoleCode(systemOmsRole);
	
		if(StringUtil.isNotNullForList(list)){
			/* jsonResult.setIsok(false);
			  jsonResult.setMessage("角色编码已存在");
			  writeObject(jsonResult,response);
			  return null;*/
			
			try{
				int updateCount = systemResourceService.updateSystemOmsRole(systemOmsRole);
				 
				if(0 < updateCount){
					jsonResult.setIsok(true);
				 	jsonResult.setMessage("修改成功");
		
				 	writeObject(jsonResult,response);
				 	return null;
				}
			} catch(Exception e){
				e.printStackTrace();
				jsonResult.setIsok(false);
				jsonResult.setMessage("修改失败");
	
				writeObject(jsonResult,response);
				return null;
			}
		} else{
			try{
				int addCount = systemResourceService.addSystemOmsRole(systemOmsRole);
			   
			   if(0 < addCount){
				   jsonResult.setIsok(true);
				   jsonResult.setMessage("添加成功");
				   writeObject(jsonResult,response);
				   return null;
			   }
			} catch(Exception e){
				e.printStackTrace();
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败");
	
				writeObject(jsonResult,response);
				return null;
			}

		}
		
		return null;
	}
	
	/***
	 *删除角色 
	 ***/
	@RequestMapping(value = "delSystemRole.spmvc")
	public ModelAndView delSystemRole(HttpServletRequest request,
			HttpServletResponse response, SystemOmsRole  systemOmsRole) throws Exception {
		
		logger.info("SystemResourceController.delSystemRole start  [ systemOmsRole = "+systemOmsRole+ "]");
	
		JsonResult jsonResult = new JsonResult();
	
		if(StringUtil.isBlank(systemOmsRole.getRoleCode())){
			jsonResult.setIsok(false);
			jsonResult.setMessage("角色码为空！");
			logger.info("SystemResourceController.delSystemRole   [ roleCode is null  " + systemOmsRole.getRoleCode() +  "  ] ");
			writeObject(jsonResult, response);
			return null;
		}
		
		try {
			int delNum = systemResourceService.delSystemOmsRole(systemOmsRole);
			if( 0 < delNum){
				jsonResult.setIsok(true);
				jsonResult.setMessage("删除成功!");
				writeObject(jsonResult, response);
				return null;
			}	
		} catch(Exception e){
			e.printStackTrace();
			logger.info("SystemResourceController.delSystemRole  修改失败   [ Exception  " + e +  "  ] ");
			jsonResult.setIsok(false);
			jsonResult.setMessage("修改失败 !");
			writeObject(jsonResult, response);
			return null;
		}
		
		jsonResult.setIsok(false);
		jsonResult.setMessage("无数据删除!");
		writeObject(jsonResult, response);

		return null;
	}
	
	
	/**
	 *删除资源
	 *String ids 多个id 
	 ***/
	@RequestMapping(value = "deleteSystemResourceAtExt5.spmvc")
	public ModelAndView deleteSystemResourceAtExt5(HttpServletRequest request,
			HttpServletResponse response, SystemOmsResource systemOmsResource) throws Exception {
		
		logger.info("SystemResourceController.deleteSystemResource start  [ ids = "+systemOmsResource+  "]");
		
		JsonResult jsonResult = new JsonResult();
		
		if(null == systemOmsResource.getResourceId()){
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败 !");
			writeObject(jsonResult, response);
			return null;
		}
		try {
			
			int delNum = systemResourceService.deleteSystemResource(systemOmsResource);
			
			if( 0 < delNum){
				jsonResult.setIsok(true);
				jsonResult.setMessage("删除成功!");
				writeObject(jsonResult, response);
				return null;
			}
			
		} catch(Exception e){
			e.printStackTrace();
			logger.info("SystemResourceController.deleteSystemResource  修改失败   [ Exception  " + e +  "  ] ");
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败 !");
			writeObject(jsonResult, response);
			return null;
		}
		
		jsonResult.setIsok(false);
		jsonResult.setMessage("无数据删除!");
		writeObject(jsonResult, response);

		return null;
	}
	
}
