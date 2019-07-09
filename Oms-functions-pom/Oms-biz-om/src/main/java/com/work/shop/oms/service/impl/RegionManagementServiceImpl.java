package com.work.shop.oms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.RegionManagementVO;
import com.work.shop.oms.dao.RegionManagementMapper;
import com.work.shop.oms.service.RegionManagementService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;

@Service("regionManagementService")
public class RegionManagementServiceImpl implements RegionManagementService {
	@Resource
	private RegionManagementMapper regionManagementMapper;

	@Override
	public List<Map<String, String>> getRegionQueryCondition(String parentId) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try{
			list = regionManagementMapper.getRegionQueryCondition(parentId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Paging getRegionListByParentId(RegionManagementVO vo,
			PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		try{
			//处理查询条件  取范围最小的区域做入参
			String countryId = vo.getCountryRegion();//国家
			String provinceId = vo.getProvinceRegion();//省份
			String cityId = vo.getCityRegion();//城市
			String districtId = vo.getDistrictRegion();//区县
			String parentId = "";
			if(StringUtil.isNotNull(districtId)){
				parentId = districtId;
			}else if(StringUtil.isNotNull(cityId)){
				parentId = cityId;
			}else if(StringUtil.isNotNull(provinceId)){
				parentId = provinceId;
			}else if(StringUtil.isNotNull(countryId)){
				parentId = countryId;
			}else{
				parentId = "0";
			}
			//拼装入参
			paramMap.put("parentId", parentId);
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int regionListCount = regionManagementMapper.getRegionCountByParentId(paramMap);
			List<RegionManagementVO> regionList = regionManagementMapper.getRegionListByParentId(paramMap);
			//拼装返回参数
			paging.setRoot(regionList);
			paging.setTotalProperty(regionListCount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map doAddRegion(RegionManagementVO vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "2";//0：插入成功  1：因存在同级同名/存在相同区域ID插入失败  2：其他原因插入失败
		String msg = "失败！";
		try{
			//拼装入参
			String countryId = vo.getCountryRegion();//国家
			String provinceId = vo.getProvinceRegion();//省份
			String cityId = vo.getCityRegion();//城市
			String districtId = vo.getDistrictRegion();//区县
			String parentId = "";//区域父ID
			String regionType = "0";//区域级别
			if(StringUtil.isNotNull(districtId)){//要添加街道/乡镇级别区域
				parentId = districtId;
				regionType = "4";
			}else if(StringUtil.isNotNull(cityId)){//要添加区县级别区域
				parentId = cityId;
				regionType = "3";
			}else if(StringUtil.isNotNull(provinceId)){//要添加城市级别区域
				parentId = provinceId;
				regionType = "2";
			}else if(StringUtil.isNotNull(countryId)){//要添加省份级别区域
				parentId = countryId;
				regionType = "1";
			}else{//要添加国家级别区域
				parentId = "0";
			}
			vo.setParentId(parentId);
			vo.setRegionType(regionType);
			Date now = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			vo.setLastUpdateTime(sf.format(now));
			vo.setAgencyId("0");
			vo.setIsUpdate("0");
			//如果存在同名区域 返回失败及原因消息
			int count = regionManagementMapper.checkRegionName(vo);
			//如果存在相同regionId 返回失败及原因消息
			int sameCount = regionManagementMapper.checkRegionId(vo.getRegionId());
			if(count>0){
				code = "1";
				msg = "失败！存在同级同名区域！";
			}else if(sameCount>0){
				code = "1";
				msg = "失败！该区域ID已存在！";
			}else{
				//新增成功 返回成功消息
				regionManagementMapper.doAddRegion(vo);
				code= "0";
				msg = "成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map delRegion(String regionId,String regionType) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();//最终返回值
		String code = "1";//0：删除成功  1：删除失败
		String msg = "失败！";
		try{
			if(StringUtil.isNotNull(regionId)&&StringUtil.isNotNull(regionType)){
				//先查询regionId及其所有下面区域ID
				List<String> allNodeList = getChildRegionList(regionId,regionType);
				//删除regionId及其下面所有区域ID
				regionManagementMapper.delRegion(allNodeList);
				code= "0";
				msg = "成功！";
			}else{
				msg = "失败！获取不到区域ID！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public RegionManagementVO getRegionInfoByRegionId(String regionId) {
		// TODO Auto-generated method stub
		RegionManagementVO vo = new RegionManagementVO();
		try{
			vo = regionManagementMapper.getRegionInfoByRegionId(regionId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return vo;
	}

	@Transactional
	@Override
	public Map doSaveEdit(RegionManagementVO vo,String flag) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code="1";
		String msg = "保存失败！";
		try{
			//如果存在同名区域 返回失败及原因消息
			int count = regionManagementMapper.checkRegionName(vo);
			if(count>0){
				msg = "失败！存在同级同名区域！";
			}else if("1".equals(flag)){//同步所有下级区域的邮费啊、是否支持货到付款啊等信息
				String regionId = vo.getRegionId();
				String regionType = vo.getRegionType();
				//先查询regionId及其所有下面区域ID
				List<String> allNodeList = getChildRegionList(regionId,regionType);
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("allNodeList", allNodeList);
				paramMap.put("shippingFee", vo.getShippingFee());
				paramMap.put("emsFee", vo.getEmsFee());
				paramMap.put("codFee", vo.getCodFee());
				paramMap.put("isCod", vo.getIsCod());
				paramMap.put("codPos", vo.getCodPos());
				paramMap.put("isCac", vo.getIsCac());
				paramMap.put("isVerifyTel", vo.getIsVerifyTel());
				regionManagementMapper.doSaveChildRegionInfo(paramMap);
				regionManagementMapper.doSaveEdit(vo);
				code = "0";
				msg = "保存成功！";
			}else{
				regionManagementMapper.doSaveEdit(vo);
				code = "0";
				msg = "保存成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	private List<String> getChildRegionList(String regionId,String regionType){
		//先查询regionId及其所有下面区域ID
		List<String> allNodeList = new ArrayList<String>();//总集合，用于存放regionId及其下面所有子区域ID的结果集
		List<String> fatherList = new ArrayList<String>();//中间量  用于存放当前循环中的入参，即当前循环中的父区域ID集合
		allNodeList.add(regionId);//初始化
		fatherList.add(regionId);//初始化
		int i =  Integer.parseInt(regionType);//参数regionId所在的菜单级别[共5级区域：0-4，0下有四级，1下有三级，以此类推]
		while(i<4){
			//查询本轮循环中，fatherList的下级区域ID集合
			List<String> childList = regionManagementMapper.getChildRegionIdList(fatherList);
			//若下级区域ID集合不为空
			if(childList!=null&&childList.size()>0){
				//遍历下级区域ID集合
				for(int j=0;j<childList.size();j++){
					String childRegionId = (String)childList.get(j);
					//将下级区域ID集合的每一项添加到总集合中
					allNodeList.add(childRegionId);
				}
				//将本轮循环得到的下级区域ID集合作为下轮循环的父区域ID集合
				fatherList = childList;
			}
			i++;
		}
		return allNodeList;
	}
	
}
