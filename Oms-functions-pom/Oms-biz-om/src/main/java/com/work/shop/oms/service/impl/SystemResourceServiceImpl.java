package com.work.shop.oms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemOmsResourceExample;
import com.work.shop.oms.bean.SystemOmsRole;
import com.work.shop.oms.bean.SystemOmsRoleExample;
import com.work.shop.oms.bean.SystemOmsRolePermisionExample;
import com.work.shop.oms.bean.SystemOmsRolePermisionKey;
import com.work.shop.oms.bean.SystemResource;
import com.work.shop.oms.bean.SystemResourceExample;
import com.work.shop.oms.common.bean.SystemOmsResourceVo;
import com.work.shop.oms.common.paraBean.SystemOmsResourceSearchExample;
import com.work.shop.oms.dao.SystemOmsResourceMapper;
import com.work.shop.oms.dao.SystemOmsRoleMapper;
import com.work.shop.oms.dao.SystemOmsRolePermisionMapper;
import com.work.shop.oms.dao.SystemResourceMapper;
import com.work.shop.oms.dao.define.SystemOmsResourceMapperDefined;
import com.work.shop.oms.service.SystemResourceService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;

@Service
public class SystemResourceServiceImpl implements SystemResourceService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SystemOmsRoleMapper systemOmsRoleMapper;
	@Resource
	private SystemOmsResourceMapperDefined systemOmsResourceMapperDefined;
	@Resource
	private SystemOmsResourceMapper systemOmsResourceMapper;
	@Resource
	private SystemResourceMapper systemResourceMapper;
	@Resource
	private SystemOmsRolePermisionMapper systemOmsRolePermisionMapper;

	/**
	 * 根据角色CODE获取角色信息
	 * @param roleCodes
	 * @return
	 */
	@Override
	public List<SystemOmsRole> getSystemOmsRolesByRoleCode(
			List<String> roleCodes) {
		// TODO Auto-generated method stub
		SystemOmsRoleExample systemOmsRoleExample = new SystemOmsRoleExample();
		if(StringUtil.isListNotNull(roleCodes)){
			systemOmsRoleExample.or().andRoleCodeIn(roleCodes);
		} else {
			return null;
		}
		return systemOmsRoleMapper.selectByExampleWithBLOBs(systemOmsRoleExample);
	}

	/**
	 * 根据角色获取权限资源信息
	 * @param roleCodes
	 * @return
	 */
	@Override
	public List<SystemOmsResource> getSystemResourceByRole(
			List<String> roleCodes) {
		// TODO Auto-generated method stub
		return systemOmsResourceMapperDefined.selectSystemResourceByRoleCodes(roleCodes);
	}
	
	/**
	 * 取权限资源信息和个数; 
	 * @param systemResource  资源信息;
	 * @param helper
	 * @return Paging
	 **/
	public Paging getSystemResourceList(SystemResource systemResource, PageHelper helper) {
		logger.info("SystemResourceServiceImpl.getSystemResourceList start " + " [ SystemResource =  " + systemResource + "  ]");
		
		Paging paing = new Paging();
		SystemResourceExample systemResourceExample = new SystemResourceExample();
		SystemResourceExample.Criteria criteria = systemResourceExample.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if(StringUtil.isNotBlank(systemResource.getResourceType()) && !"-1".equals(systemResource.getResourceType())) {
			criteria.andResourceTypeEqualTo(systemResource.getResourceType());
		}
		if(StringUtil.isNotBlank(systemResource.getResourceCode())) {
			criteria.andResourceNameLike("%"+systemResource.getResourceCode()+"%");
		}
		if(StringUtil.isNotBlank(systemResource.getResourceName())) {
			criteria.andResourceNameLike("%"+systemResource.getResourceName()+"%");
		}
		if(StringUtil.isNotBlank(systemResource.getResourceUrl())) {
			criteria.andResourceUrlLike("%"+systemResource.getResourceUrl()+"%");
		}
		
		
		
		List<SystemResource> srList = systemResourceMapper.selectByExample(systemResourceExample);
		int count = systemResourceMapper.countByExample(systemResourceExample);
		logger.info("SystemResourceServiceImpl.getSystemResourceList end");
		paing.setRoot(srList);
		paing.setTotalProperty(count);
		return paing;
	}
	
	/**
	 * 添加权限资源; 
	 * @param systemResource  资源信息;
	 * @return int;
	 ***/
	public int addSystemResource(SystemResource systemResource) {
		
		Date date = new Date();
		
		logger.info("SystemResourceServiceImpl.addSystemResource start "
				+ " [ SystemResource =  " + systemResource + "  ]");

		systemResource.setAddTime(date);
		systemResource.setSortOrder((byte)0);
		
		return systemResourceMapper.insert(systemResource);
	}
	
	/**
	 * 查询 权限资源信息; 
	 *@param systemResource  资源信息;
	 * @return SystemResource
	 ***/
	public SystemResource selectSystemResource(
			SystemResource systemResource) {
		
		logger.info("SystemResourceServiceImpl.selectSystemResource start "
				+ " [ SystemResource =  " + systemResource + "  ]");
		
		SystemResourceExample systemResourceExample = new SystemResourceExample();
		SystemResourceExample.Criteria criteria = systemResourceExample.or();

		int  ResourceId = 0;
		
		if(null != systemResource.getResourceId()){
			ResourceId = systemResource.getResourceId();
		}
		
		return systemResourceMapper.selectByPrimaryKey(ResourceId);
	}
	
	/***
	 * 修改权限资源; 
	 * @param systemResource  资源信息;
	 * @return int;
	 **/
	public int updateSystemResource(SystemResource systemResource) {
		
		logger.info("SystemResourceServiceImpl.updateSystemResource start "
				+ " [ SystemResource =  " + systemResource + "  ]");
		
		Date date = new Date();
		systemResource.setUpdateTime(date);
		byte sortOrder = systemResource.getSortOrder()!= null ? systemResource.getSortOrder() : 0;
		systemResource.setSortOrder(sortOrder);
		return systemResourceMapper.updateByPrimaryKey(systemResource);
	}
	
	/**
	 *删除资源项; 
	 **/
	public int deleteSystemResourceList(List<Integer> list) {
	   int num =0;
	   for(Integer i: list){
		   if(0<systemResourceMapper.deleteByPrimaryKey(i)) {
			   num++;
		   }	
	   }
	   return num;
	}
	
	@Override
	public List<SystemOmsResourceVo> getAssigningPermissionsByResourceCode(
			String roleCode) {
		
		logger.info("SystemResourceServiceImpl.getAssigningPermissionsByResourceCode start "
				+ " [ resourceCode =  " + roleCode + "  ]");
		
		if(null ==  roleCode || "" == roleCode){
			logger.info("SystemResourceServiceImpl.getAssigningPermissionsByResourceCode  "
					+ " [ resourceCode is null ]");
			return null;
		}
		 
		//第一级                                                                                                           
		List<SystemOmsResourceVo> desktopGroupList  = checkConnectionSystemResource( "desktop_group", roleCode,false);
		
		//第二级
		List<SystemOmsResourceVo> urlList  = checkConnectionSystemResource( "url", roleCode,false ); 
		
		//第三级
		List<SystemOmsResourceVo> actList  = checkConnectionSystemResource( "act", roleCode, true);
		
		//第二和第三
	    for( SystemOmsResourceVo  secondResourceVo :urlList ){
	    	Integer  secondResourceId =  secondResourceVo.getResourceId();

	    	
	    	List<SystemOmsResourceVo> childList =  new ArrayList<SystemOmsResourceVo>();
	  
	      	for(SystemOmsResourceVo resourceVo :actList){
	    
	    		Integer parentId =  resourceVo.getParentId();

	    		if(null != parentId && null != secondResourceId){
	    			
	    
	    			
	    			if(parentId.intValue() == secondResourceId.intValue()){
	    			
	    				childList.add(resourceVo);
	    			}
	    		}
	    	}
	      	secondResourceVo.setList(childList);
			
		}

	    ////第二和第一
	    for( SystemOmsResourceVo  firstResourceVo:  desktopGroupList){
	    	Integer firstResourceId =  firstResourceVo.getResourceId();
	    	
	    	List<SystemOmsResourceVo> rootChildList =  new ArrayList<SystemOmsResourceVo>();
	    	
	    	for(SystemOmsResourceVo resourceVo :urlList){
	  	    		Integer parentId =  resourceVo.getParentId();
	  	
	  	    		if(null != parentId && null != firstResourceId){
	  	    			if(parentId.intValue() ==firstResourceId.intValue()){
	  	    				rootChildList.add(resourceVo);
	  	    			}
	  	    		}
	  	   	}
	    	firstResourceVo.setList(rootChildList);
	    }
        return desktopGroupList;

	}
	
	/***
	    * 校验系统和角色关联 
	    ***/
	   private   List<SystemOmsResourceVo> checkConnectionSystemResource(String resourcType,String roleCode,boolean leaf){
		   
			List<String> resourceTypeList = new  ArrayList<String>();
			resourceTypeList.add(resourcType);

			List<SystemOmsResourceVo> systemOmsResourceList = this.getSystemOmsResourceListByResoureTypeList(resourceTypeList);
			
			for(SystemOmsResourceVo resource: systemOmsResourceList){
					
				Integer resourceId = resource.getResourceId();
				
				if(null != resourceId ){
					SystemOmsRolePermisionKey spk = new SystemOmsRolePermisionKey();
					spk.setRoleCode(roleCode);
					spk.setResourceId(resourceId);
					
					List<SystemOmsRolePermisionKey> sorpkList = this.getSystemOmsRolePermisionList(spk);
			
					if(StringUtil.isNotNullForList(sorpkList)){
						resource.setSelected(true);
					}
					
					if(leaf){
						resource.setLeaf(true);
					}
				}
				
			}

			return systemOmsResourceList;
		   
	   }
	   
	   public List<SystemOmsResourceVo> getSystemOmsResourceListByResoureTypeList(List<String> list) {
			
			logger.info("SystemResourceServiceImpl.getSystemOmsResourceListByResoureTypeList start " + " [ list =  " + list + "  ]");
			
			SystemOmsResourceSearchExample systemOmsResourceExample = new SystemOmsResourceSearchExample();
			SystemOmsResourceSearchExample.Criteria criteria = systemOmsResourceExample.or();
			criteria.andIsShowEqualTo((byte)1);
			if(StringUtil.isNotNullForList(list)){
				criteria.andResourceTypeIn(list);
			}
			
			List<SystemOmsResourceVo> srList = systemOmsResourceMapperDefined.selectSystemOmsResourceByExample(systemOmsResourceExample);
			
			System.out.println(srList.size());

			logger.info("SystemResourceServiceImpl.getSystemOmsResourceListByResoureTypeList end");
		
			return srList;
	   }
	   
	   @Override
		public List<SystemOmsRolePermisionKey> getSystemOmsRolePermisionList(
				SystemOmsRolePermisionKey systemOmsRolePermision) {
			
			SystemOmsRolePermisionExample example = new SystemOmsRolePermisionExample();
			SystemOmsRolePermisionExample.Criteria criteria = example.or();
			
			if(StringUtil.isNotBlank(systemOmsRolePermision.getRoleCode())){
				criteria.andRoleCodeEqualTo(systemOmsRolePermision.getRoleCode());
			}
			
			if(null != systemOmsRolePermision.getResourceId()){
				criteria.andResourceIdEqualTo(systemOmsRolePermision.getResourceId());
			}
			
			// TODO Auto-generated method stub
			return systemOmsRolePermisionMapper.selectByExample(example);
		}
	   
	   /**
		 * 查询资源信息 
		 ***/
		public Paging getSystemOmsResourceList(SystemOmsResource systemOmsResource,
				PageHelper helper) {
			
		logger.info("SystemResourceServiceImpl.getSystemOmsResourceList start " + " [ SystemOmsResource =  " + systemOmsResource + "  ]");
			
			Paging paing = new Paging();
			SystemOmsResourceExample systemOmsResourceExample = new SystemOmsResourceExample();
			SystemOmsResourceExample.Criteria criteria = systemOmsResourceExample.or();
			criteria.limit(helper.getStart(), helper.getLimit());
			if(StringUtil.isNotBlank(systemOmsResource.getResourceType()) && !"-1".equals(systemOmsResource.getResourceType())) {
				criteria.andResourceTypeEqualTo(systemOmsResource.getResourceType());
			}
			
			if(StringUtil.isNotBlank(systemOmsResource.getResourceCode())) {

				criteria.andResourceCodeLike("%"+systemOmsResource.getResourceCode()+"%");
			}
			
			if(StringUtil.isNotBlank(systemOmsResource.getResourceName())) {
				criteria.andResourceNameLike("%"+systemOmsResource.getResourceName()+"%");
			}
			
			if(StringUtil.isNotBlank(systemOmsResource.getResourceUrl())) {
				criteria.andResourceUrlLike("%"+systemOmsResource.getResourceUrl()+"%");
			}
			
			if(StringUtil.isNotBlank(systemOmsResource.getParentCode())) {
				criteria.andParentCodeEqualTo(systemOmsResource.getParentCode());
			}
			
			if(systemOmsResource.getIsShow() != null && systemOmsResource.getIsShow().intValue() != -1) {
				criteria.andIsShowEqualTo(systemOmsResource.getIsShow());
			}
			
			List<SystemOmsResource> srList = systemOmsResourceMapper.selectByExample(systemOmsResourceExample);
			int count = systemOmsResourceMapper.countByExample(systemOmsResourceExample);
			logger.info("SystemResourceServiceImpl.getSystemOmsResourceList end");
			paing.setRoot(srList);
			paing.setTotalProperty(count);
			return paing;
		}
		
		@Override
		public SystemOmsResource getSystemOmsResourceByResourceId(
				SystemOmsResource systemOmsResource) {
			
			SystemOmsResourceExample systemOmsResourceExample = new SystemOmsResourceExample();
			SystemOmsResourceExample.Criteria criteria = systemOmsResourceExample.or();
			
			if(null == systemOmsResource.getResourceId()) {
				//criteria.andResourceIdEqualTo(systemOmsResource.getResourceId());
				return null;
			}
			
			// TODO Auto-generated method stub
			return systemOmsResourceMapper.selectByPrimaryKey(systemOmsResource.getResourceId());
		}
		
		@Override
		public List<SystemOmsResource> querySystemOmsResourceListByParentCodeAndResourceCode(
				SystemOmsResource systemOmsResource) {
			
			SystemOmsResourceExample systemOmsResourceExample = new SystemOmsResourceExample();
			SystemOmsResourceExample.Criteria criteria = systemOmsResourceExample.or();
			
			if(StringUtil.isNotBlank(systemOmsResource.getResourceCode())){
				criteria.andResourceCodeEqualTo(systemOmsResource.getResourceCode());
			}
			
			if(StringUtil.isNotBlank(systemOmsResource.getParentCode())){
				criteria.andParentCodeEqualTo(systemOmsResource.getParentCode());
			}

			return systemOmsResourceMapper.selectByExample(systemOmsResourceExample);
		}
		
		@Override
		public int addSystemOmsResource(SystemOmsResource systemResource) {
			// TODO Auto-generated method stub
			return systemOmsResourceMapper.insertSelective(systemResource);
		}
		
		@Override
		public int updateSystemOmsResource(SystemOmsResource systemResource) {
			
			SystemOmsResourceExample systemOmsResourceExample = new SystemOmsResourceExample();
			SystemOmsResourceExample.Criteria criteria = systemOmsResourceExample.or();
			criteria.andResourceIdEqualTo(systemResource.getResourceId());
			// TODO Auto-generated method stub
			return systemOmsResourceMapper.updateByExampleSelective(systemResource, systemOmsResourceExample);
		}
		
		@Override
		public Paging getSystemOmsRoleList(SystemOmsRole systemOmsRole,
				PageHelper helper) {
			Paging paing = new Paging();
			SystemOmsRoleExample systemOmsRoleExample = new SystemOmsRoleExample();
			SystemOmsRoleExample.Criteria criteria = systemOmsRoleExample.or();
			criteria.limit(helper.getStart(), helper.getLimit());
			if(StringUtil.isNotBlank(systemOmsRole.getRoleName())){		
				criteria.andRoleNameLike("%"+systemOmsRole.getRoleName()+"%");
			}
			
			if(StringUtil.isNotBlank(systemOmsRole.getRoleCode())){
				criteria.andRoleCodeLike("%"+systemOmsRole.getRoleCode()+"%");
			}
			
			int num = systemOmsRoleMapper.countByExample(systemOmsRoleExample);	
			List<SystemOmsRole> list = systemOmsRoleMapper.selectByExample(systemOmsRoleExample);
			paing.setTotalProperty(num);
			paing.setRoot(list);
			return paing;
		}
		
		@Override
		public SystemOmsRole getSystemOmsRoleByResourceCode(
				SystemOmsRole systemOmsRole) {
			
			return systemOmsRoleMapper.selectByPrimaryKey(systemOmsRole.getRoleCode());
		}
		
		public List<SystemOmsRole> querySystemOmsRoleListByRoleCode(SystemOmsRole systemOmsRole) {
			
			SystemOmsRoleExample systemOmsRoleExample = new SystemOmsRoleExample();
			SystemOmsRoleExample.Criteria criteria = systemOmsRoleExample.or();
			
		    if(StringUtil.isNotBlank(systemOmsRole.getRoleCode())){
		    	criteria.andRoleCodeEqualTo(systemOmsRole.getRoleCode());
			}

			return systemOmsRoleMapper.selectByExample(systemOmsRoleExample);
		}
		
		@Override
		public int updateSystemOmsRole(SystemOmsRole systemOmsRole) {
			
			SystemOmsRoleExample systemOmsRoleExample = new SystemOmsRoleExample();
			SystemOmsRoleExample.Criteria criteria = systemOmsRoleExample.or();
			criteria.andRoleCodeEqualTo(systemOmsRole.getRoleCode());
			return systemOmsRoleMapper.updateByExampleSelective(systemOmsRole, systemOmsRoleExample);
		}
		
		@Override
		public int addSystemOmsRole(SystemOmsRole systemOmsRole) {
			return systemOmsRoleMapper.insertSelective(systemOmsRole);
		}
		
		@Override
		public int delSystemOmsRole(SystemOmsRole systemOmsRole) {
			return systemOmsRoleMapper.deleteByPrimaryKey(systemOmsRole.getRoleCode());
		}
		
		@Override
		public int deleteSystemResource(SystemOmsResource systemOmsResource) {
			return systemOmsResourceMapper.deleteByPrimaryKey(systemOmsResource.getResourceId());
		}

}
