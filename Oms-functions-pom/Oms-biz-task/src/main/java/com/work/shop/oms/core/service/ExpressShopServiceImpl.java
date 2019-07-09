package com.work.shop.oms.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.bean.TaskShopExpress;
import com.work.shop.oms.common.bean.OrderExpressInfo;
import com.work.shop.oms.core.beans.ShipExpressInfo;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.dao.TaskShopExpressMapper;
import com.work.shop.oms.service.ExpressShopService;

@Service("expressShopService")
public class ExpressShopServiceImpl implements ExpressShopService {

	private static Logger logger = LoggerFactory.getLogger(ExpressShopServiceImpl.class);

	@Resource(name = "systemRegionAreaMapper")
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource(name = "taskShopExpressMapper")
	private TaskShopExpressMapper taskShopExpressMapper; 
	
	@Override
	public ShipExpressInfo buildExpressInfo(OrderExpressInfo express){
		ShipExpressInfo shipExpress = new ShipExpressInfo();;
		shipExpress.setOrderSn(express.getOrderSn());
		shipExpress.setConsignee(express.getConsignee());
		
		//解密手机号
		shipExpress.setRecTel(mobileUtilDec(express.getRecTel()));
		shipExpress.setRecMobile(mobileUtilDec(express.getRecMobile()));
		
		shipExpress.setProvinceId(express.getProvinceId());
		shipExpress.setCityId(express.getCityId());
		shipExpress.setDistrictId(express.getDistrictId());
		
		shipExpress.setRecAddress(express.getRecAddress());
		shipExpress.setRecZipcode(express.getRecZipcode());
		shipExpress.setInvoiceNo(express.getInvoiceNo());
		shipExpress.setWeigh(express.getWeigh());
		shipExpress.setCompanyId(express.getCompanyId());
		shipExpress.setDepotCode(express.getDepotCode());
		shipExpress.setChannelCode(express.getChannelCode());
		shipExpress.setSenderName(express.getSenderName());
		shipExpress.setSenderPhone(express.getSenderPhone());
		if(express.getRecAddress().contains("&")){
			shipExpress.setRecAddress(express.getRecAddress().replace("&", "-"));
		}
		shipExpress.setSenderAddress(express.getSenderAddress());
		shipExpress.setSenderMobile(express.getSenderMobile());
		shipExpress.setSenderCity(express.getSenderCity());
		shipExpress.setSenderDistrict(express.getSenderDistrict());
		shipExpress.setSenderZipCode(express.getSenderZipCode());
		return shipExpress;
	};

	public Map<String,String> processRegionArea(ShipExpressInfo shipExpressInfo){
		//地区数据
		List<String> regionIdList = new ArrayList<String>();
		regionIdList.add(shipExpressInfo.getChildShips().get(0).getCityId());
		regionIdList.add(shipExpressInfo.getChildShips().get(0).getProvinceId());
		regionIdList.add(shipExpressInfo.getChildShips().get(0).getDistrictId());
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		systemRegionAreaExample.or().andRegionIdIn(regionIdList);
		List<SystemRegionArea> systemRegionList = systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		if(CollectionUtils.isEmpty(systemRegionList)){
			throw new RuntimeException("无法正常解析地区数据");
		}
		Map<String,String> systemRegionMap = new HashMap<String,String>();
		for (SystemRegionArea systemRegionArea : systemRegionList) {
			systemRegionMap.put(systemRegionArea.getRegionId(), systemRegionArea.getRegionName());
		}
		return systemRegionMap;
	}

	@Override
	public String mobileUtilDec(String mobile) {
		logger.debug(">>[mobileUtilDec]...begin....mobile:"+mobile);
		String encMobile = StringUtils.EMPTY;
		try {
			if(StringUtils.isNotBlank(mobile) && mobile.length() > 20){
				encMobile = mobile;
			}else{
				encMobile = mobile;
			}
		} catch (Exception e) {
			logger.debug(">>[mobileUtilDec]解密订单手机信息错误.mobile:"+mobile+e.getMessage(),e);
		}
		logger.debug(">>[mobileUtilDec]...end....mobile:"+mobile);
		return encMobile;
	}

	@Override
	public void callExpressResult(String orderSn, Integer shippingId,String depotCode,
			Integer isOk, String result) {
		logger.debug("callExpressResult.orderSn:"+orderSn+",shippingId:"+shippingId+",isOk:"+isOk+",result:"+result);
		try {
			TaskShopExpress taskShopExpress = new TaskShopExpress();
			taskShopExpress.setOrderSn(orderSn);
			taskShopExpress.setShippingId(shippingId.byteValue());
			taskShopExpress.setDepotCode(depotCode);
			taskShopExpress.setIsOk(isOk);
			taskShopExpress.setResponse(result);
			taskShopExpress.setAddTime(new Date());
			taskShopExpressMapper.insertSelective(taskShopExpress);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.debug("callExpressResult.exception.orderSn:"+orderSn+",shippingId:"+shippingId+e.getMessage(),e);
		}
	}
	
}
