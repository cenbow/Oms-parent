package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.SystemRegionInfo;
import com.work.shop.oms.api.param.bean.SystemRegionMatchInfo;
import com.work.shop.oms.api.param.bean.SystemRegionSimpleInfo;
import com.work.shop.oms.api.region.service.SystemRegionService;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.bean.SystemRegionMatch;
import com.work.shop.oms.bean.SystemRegionMatchExample;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.dao.SystemRegionMatchMapper;
import com.work.shop.oms.redis.RedisClient;
@Service
public class SystemRegionServiceImpl implements SystemRegionService {
	
	private final static Logger logger = Logger.getLogger(SystemRegionServiceImpl.class);

//	@Resource
//	private SystemRegionMapper systemRegionMapper;
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource
	private SystemRegionMatchMapper systemRegionMatchMapper;
	
	@Resource
	private RedisClient redisClient;
	
	private final String RegionT="RegionArea_T";
	private final String RegionP="RegionArea_P";
	private final String Region="RegionArea";
	//中国下所有省级消息
	private final String RegionP1="RegionArea_P1";
	                 
	
	
	/**
	 * 通过regionId 列表 查询区域地址信息
	 * @param paraList
	 * @return
	 */
	public List<SystemRegionArea> getSystemRegionListByRegId(List<String> paraList) {
		logger.debug("通过regionId 列表 查询区域地址信息" );
		if (paraList != null && paraList.size() > 0) {
			SystemRegionAreaExample systemExa=new SystemRegionAreaExample();
			systemExa.or().andRegionIdIn(paraList);
			return systemRegionAreaMapper.selectByExample(systemExa);	
		}
		return null;
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
	
	
	@Override
	public String setSystemRegionToRedis() {
		try {
			logger.info("redis更新地区消息开始！");
			SystemRegionAreaExample example = new SystemRegionAreaExample();
			example.or().andRegionTypeEqualTo(0);
			List<SystemRegionInfo> T0_list=toRegionInfo(systemRegionAreaMapper.selectByExample(example));
			example.clear();
			example.or().andRegionTypeEqualTo(1);
			List<SystemRegionInfo> T1_list=toRegionInfo(systemRegionAreaMapper.selectByExample(example));
			example.clear();
			example.or().andRegionTypeEqualTo(2);
			List<SystemRegionInfo> T2_list=toRegionInfo(systemRegionAreaMapper.selectByExample(example));
			example.clear();
			example.or().andRegionTypeEqualTo(3);
			List<SystemRegionInfo> T3_list=toRegionInfo(systemRegionAreaMapper.selectByExample(example));
			example.clear();
			example.or().andRegionTypeEqualTo(4);
			List<SystemRegionInfo> T4_list=toRegionInfo(systemRegionAreaMapper.selectByExample(example));
			String json0 = JSON.toJSONString(T0_list);
			redisClient.set(RegionT+0, json0);
			String json1 = JSON.toJSONString(T1_list);
			redisClient.set(RegionT+1, json1);
			String json2 = JSON.toJSONString(T2_list);
			redisClient.set(RegionT+2, json2);
			String json3 = JSON.toJSONString(T3_list);
			redisClient.set(RegionT+3, json3);
			String json4 = JSON.toJSONString(T4_list);
			redisClient.set(RegionT+4, json4);
			
			for(SystemRegionInfo systemRegionInfo0:T0_list){
				List<SystemRegionInfo> plist=new ArrayList<SystemRegionInfo>();
				for(SystemRegionInfo systemRegionInfo1:T1_list){
					if(systemRegionInfo1.getParent_id().equals(systemRegionInfo0.getRegion_id())){
						plist.add(systemRegionInfo1);
					}
				}
				String jsonp = JSON.toJSONString(plist);
				redisClient.set(RegionP+systemRegionInfo0.getRegion_id(), jsonp);
				String json = JSON.toJSONString(systemRegionInfo0);
				redisClient.set(Region+systemRegionInfo0.getRegion_id(), json);
			}
			
			for(SystemRegionInfo systemRegionInfo1:T1_list){
				List<SystemRegionInfo> plist=new ArrayList<SystemRegionInfo>();
				for(SystemRegionInfo systemRegionInfo2:T2_list){
					if(systemRegionInfo2.getParent_id().equals(systemRegionInfo1.getRegion_id())){
						plist.add(systemRegionInfo2);
					}
				}
				String jsonp = JSON.toJSONString(plist);
				redisClient.set(RegionP+systemRegionInfo1.getRegion_id(), jsonp);
				String json = JSON.toJSONString(systemRegionInfo1);
				redisClient.set(Region+systemRegionInfo1.getRegion_id(), json);
			}
			
			for(SystemRegionInfo systemRegionInfo2:T2_list){
				List<SystemRegionInfo> plist=new ArrayList<SystemRegionInfo>();
				for(SystemRegionInfo systemRegionInfo3:T3_list){
					if(systemRegionInfo3.getParent_id().equals(systemRegionInfo2.getRegion_id())){
						plist.add(systemRegionInfo3);
					}
				}
				String jsonp = JSON.toJSONString(plist);
				redisClient.set(RegionP+systemRegionInfo2.getRegion_id(), jsonp);
				String json = JSON.toJSONString(systemRegionInfo2);
				redisClient.set(Region+systemRegionInfo2.getRegion_id(), json);
			}
			
			for(SystemRegionInfo systemRegionInfo3:T3_list){
				List<SystemRegionInfo> plist=new ArrayList<SystemRegionInfo>();
				for(SystemRegionInfo systemRegionInfo4:T4_list){
					if(systemRegionInfo4.getParent_id().equals(systemRegionInfo3.getRegion_id())){
						plist.add(systemRegionInfo4);
					}
				}
				String jsonp = JSON.toJSONString(plist);
				redisClient.set(RegionP+systemRegionInfo3.getRegion_id(), jsonp);
				String json = JSON.toJSONString(systemRegionInfo3);
				redisClient.set(Region+systemRegionInfo3.getRegion_id(), json);
			}
			
			for(SystemRegionInfo systemRegionInfo4:T4_list){
				List<SystemRegionInfo> plist=new ArrayList<SystemRegionInfo>();
				plist.add(systemRegionInfo4);
				String json = JSON.toJSONString(systemRegionInfo4);
				redisClient.set(Region+systemRegionInfo4.getRegion_id(), json);
			}
			logger.info("redis更新地区消息结束！");
		} catch (Exception e) {
			logger.error("redis更新地区消息失败：", e);
			return "Fail";
		}
		return "Success";
	}
	
	
	private List<SystemRegionInfo> toRegionInfo(List<SystemRegionArea> systemRegionAreaList){
		List<SystemRegionInfo> list=new ArrayList<SystemRegionInfo>();
		for(SystemRegionArea systemRegionArea:systemRegionAreaList){
		SystemRegionInfo systemRegionInfo = new SystemRegionInfo();
		systemRegionInfo.setAgency_id(systemRegionArea.getAgencyId().toString());
		systemRegionInfo.setCod_fee(systemRegionArea.getCodFee());
		systemRegionInfo.setCod_pos(systemRegionArea.getCodPos().toString());
		systemRegionInfo.setEms_fee(systemRegionArea.getEmsFee());
		systemRegionInfo.setIs_cac(systemRegionArea.getIsCac().toString());
		systemRegionInfo.setIs_cod(systemRegionArea.getIsCod().toString());
		systemRegionInfo.setIs_verify_tel(systemRegionArea.getIsVerifyTel().toString());
		systemRegionInfo.setParent_id(systemRegionArea.getParentId());
		systemRegionInfo.setRegion_id(systemRegionArea.getRegionId());
		systemRegionInfo.setRegion_name(systemRegionArea.getRegionName());
		systemRegionInfo.setRegion_type(systemRegionArea.getRegionType().toString());
		systemRegionInfo.setShipping_fee(systemRegionArea.getShippingFee());
		systemRegionInfo.setZip_code(systemRegionArea.getZipCode());
		list.add(systemRegionInfo);
		}
		return list;
	}

	@Override
	public String getSystemRegionArea(String region_type, String region_id) {
		String date="";
        if(region_type.length()>0&&region_id.length()==0){
			date=redisClient.get(RegionT+region_type);
		}
        if(region_type.length()==0&&region_id.length()>0){
			date=redisClient.get(RegionP+region_id);
		}
        if(region_id.length()==0&&region_type.length()==0){
			date=redisClient.get(RegionP1);
		}
		return date;
	}
	
	@Override
	public SystemRegionInfo getSystemRegionAreaName(String region_id) {
		if(region_id==null||region_id.length()==0){
			return null;
		}
		SystemRegionInfo systemRegionInfo=new SystemRegionInfo();
		String date=redisClient.get(Region+region_id);
		try {
			systemRegionInfo=JSON.parseObject(date, SystemRegionInfo.class);
		} catch (Exception e) {
			logger.error("getSystemRegionAreaName异常：", e);
		}
		return systemRegionInfo;
	}

	@Override
	public List<SystemRegionSimpleInfo> getChinaSystemRegionArea() {
		List<SystemRegionSimpleInfo> list=new ArrayList<SystemRegionSimpleInfo>();
		String date1=redisClient.get(RegionP1);
		List<SystemRegionInfo> list1=JSON.parseArray(date1, SystemRegionInfo.class);
		for(SystemRegionInfo systemRegionInfo1:list1){
			String date2=redisClient.get(RegionP+systemRegionInfo1.getRegion_id());
			List<SystemRegionInfo> list2=JSON.parseArray(date2, SystemRegionInfo.class);
			for(SystemRegionInfo systemRegionInfo2:list2){
				String date3=redisClient.get(RegionP+systemRegionInfo2.getRegion_id());
				List<SystemRegionInfo> list3=JSON.parseArray(date3, SystemRegionInfo.class);
				for(SystemRegionInfo systemRegionInfo3:list3){
					SystemRegionSimpleInfo systemRegionSimpleInfo = new SystemRegionSimpleInfo();
					systemRegionSimpleInfo.setParent_id(systemRegionInfo3.getParent_id());
					systemRegionSimpleInfo.setRegion_id(systemRegionInfo3.getRegion_id());
					systemRegionSimpleInfo.setRegion_name(systemRegionInfo3.getRegion_name());
					systemRegionSimpleInfo.setRegion_type(systemRegionInfo3.getRegion_type());
					list.add(systemRegionSimpleInfo);
				}
				SystemRegionSimpleInfo systemRegionSimpleInfo = new SystemRegionSimpleInfo();
				systemRegionSimpleInfo.setParent_id(systemRegionInfo2.getParent_id());
				systemRegionSimpleInfo.setRegion_id(systemRegionInfo2.getRegion_id());
				systemRegionSimpleInfo.setRegion_name(systemRegionInfo2.getRegion_name());
				systemRegionSimpleInfo.setRegion_type(systemRegionInfo2.getRegion_type());
				list.add(systemRegionSimpleInfo);
			}
			SystemRegionSimpleInfo systemRegionSimpleInfo = new SystemRegionSimpleInfo();
			systemRegionSimpleInfo.setParent_id(systemRegionInfo1.getParent_id());
			systemRegionSimpleInfo.setRegion_id(systemRegionInfo1.getRegion_id());
			systemRegionSimpleInfo.setRegion_name(systemRegionInfo1.getRegion_name());
			systemRegionSimpleInfo.setRegion_type(systemRegionInfo1.getRegion_type());
			list.add(systemRegionSimpleInfo);
		}
		
		return list;
	}

	@Override
	public ApiReturnData getRegionAreaByOldId(String cCode,
			String pCode, String ciCode, String dCode) {
		logger.info("新老地区转换：cCode="+cCode+",pCode="+pCode+",ciCode="+ciCode+",dCode="+dCode);
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk("0");
		SystemRegionMatchInfo systemRegionMatchInfo = new SystemRegionMatchInfo();
		//如果没有传入国家id，则默认为中国
		if(cCode==null||cCode.equals("0")){
			cCode="1";
		}
		try {
			SystemRegionMatchExample systemRegionMatchExample =new SystemRegionMatchExample();
			List<String> list=new ArrayList<String>();
			list.add(cCode);
			list.add(pCode);
			list.add(ciCode);
			list.add(dCode);
			systemRegionMatchExample.or().andOldRegionIdIn(list);
			List<SystemRegionMatch> slist = systemRegionMatchMapper.selectByExample(systemRegionMatchExample);
			for(SystemRegionMatch systemRegionMatch:slist){
				if(systemRegionMatch.getOldRegionId().equals(cCode)){
					systemRegionMatchInfo.setCountryCode(systemRegionMatch.getNewAreaId());
				}
				if(systemRegionMatch.getOldRegionId().equals(pCode)){
					systemRegionMatchInfo.setProvinceCode(systemRegionMatch.getNewAreaId());
				}
				if(systemRegionMatch.getOldRegionId().equals(ciCode)){
					systemRegionMatchInfo.setCityCode(systemRegionMatch.getNewAreaId());
				}
				if(systemRegionMatch.getOldRegionId().equals(dCode)){
					systemRegionMatchInfo.setDistrictCode(systemRegionMatch.getNewAreaId());
				}
			}
			apiReturnData.setIsOk("1");
			apiReturnData.setData(systemRegionMatchInfo);
		logger.info("已转换地区数据：cCode="+systemRegionMatchInfo.getCountryCode()+",pCode="+systemRegionMatchInfo.getProvinceCode()+
				",ciCode="+systemRegionMatchInfo.getCityCode()+",dCode="+systemRegionMatchInfo.getDistrictCode());
		} catch (Exception e) {
			logger.error("新老地区转换异常：", e);
			apiReturnData.setMessage("新老地区转换异常："+e.toString());
		}
		return apiReturnData;
	}
}
