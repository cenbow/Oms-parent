package com.work.shop.oms.config.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.bean.SystemShippingExample;
import com.work.shop.oms.config.service.SystemShippingService;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.utils.StringUtil;


@Service
public class SystemShippingServiceImpl implements SystemShippingService{

	@Resource
	private SystemShippingMapper systemShippingMapper;
	
	@Override
	public SystemShipping getSystemShipByShipCode(String shippingCode) {
		if (StringUtil.isNotBlank(shippingCode)) {

			SystemShippingExample systemShipExa = new SystemShippingExample();
			systemShipExa.or().andShippingCodeEqualTo(shippingCode);
			List<SystemShipping> resultList = systemShippingMapper.selectByExample(systemShipExa);
			if (resultList != null && resultList.size() > 0) {
				return resultList.get(0);
			}
		}
		return null;
	}
}
