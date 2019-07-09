package com.work.shop.oms.service;

import java.util.List;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemOmsRole;
import com.work.shop.oms.bean.SystemOmsRolePermisionKey;
import com.work.shop.oms.bean.SystemResource;
import com.work.shop.oms.common.bean.SystemOmsResourceVo;
import com.work.shop.oms.utils.PageHelper;

public interface SystemResourceService {
	/**
     * 根据角色CODE获取角色信息
     * @param roleCodes
     * @return
     */
	List<SystemOmsRole> getSystemOmsRolesByRoleCode(List<String> roleCodes);
	
	/**
     * 根据角色获取权限资源信息
     * @param roleCodes
     * @return
     */
	List<SystemOmsResource> getSystemResourceByRole(List<String> roleCodes);
	
	/**
	 * 取权限资源信息和个数; 
	 **/
	Paging getSystemResourceList(SystemResource systemResource, PageHelper helper);
	
	/**
	 * 添加权限资源; 
	 ***/
	int addSystemResource(SystemResource systemResource);
	
	/**
	 * 查询 权限资源信息; 
	 ***/
	SystemResource selectSystemResource(SystemResource systemResource);
	
	/***
	 * 修改权限资源; 
	 **/
	int updateSystemResource(SystemResource systemResource);
	
	int deleteSystemResourceList(List<Integer> list);
	
	/***
	 * 获取分派权限
	 * **/
    List<SystemOmsResourceVo>	getAssigningPermissionsByResourceCode(String resourceCode);
    
    /***
	 * 获取角色权限关联表
	 * **/
	List<SystemOmsRolePermisionKey> getSystemOmsRolePermisionList(SystemOmsRolePermisionKey resourceCode);
       
     /**
   	 * 取权限资源信息和个数; 
   	 **/
   	Paging getSystemOmsResourceList(SystemOmsResource systemOmsResource, PageHelper helper); 
   	
   	SystemOmsResource getSystemOmsResourceByResourceId(SystemOmsResource systemOmsResource);
   	
   	/**
	 *  资源编码和父类编码作为参数查询系统资源 
	 ***/
	List<SystemOmsResource> querySystemOmsResourceListByParentCodeAndResourceCode(SystemOmsResource systemOmsResource);
	
	/**
	 * 添加权限资源; 
	 ***/
	int addSystemOmsResource(SystemOmsResource systemOmsResource);
	
	/***
	 * 修改权限资源; 
	 **/
	int updateSystemOmsResource(SystemOmsResource systemOmsResource);
	
	/**
	 * 取角色信息;
	 **/
	Paging getSystemOmsRoleList(SystemOmsRole systemOmsRole, PageHelper helper); 
	
	SystemOmsRole getSystemOmsRoleByResourceCode(SystemOmsRole systemOmsRole);
	
	List<SystemOmsRole> querySystemOmsRoleListByRoleCode(SystemOmsRole systemOmsRole);
	
	public  int updateSystemOmsRole(SystemOmsRole systemOmsRole);
	
	int addSystemOmsRole(SystemOmsRole systemOmsRole);
	
	int delSystemOmsRole(SystemOmsRole systemOmsRole);
	
	/***
     *删除资源信息 
     ***/
	int deleteSystemResource(SystemOmsResource systemOmsResource);

}
