package com.work.shop.oms.core.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChannelShopService {
	
	private static Logger logger = LoggerFactory.getLogger(ChannelShopService.class);
	
	/**
	 * 根据渠道号获取渠道店铺编码
	 * @param channelCode
	 * @return
	 */
	public List<String> getShopCodesByChannelCode(String channelCode){
		if (StringUtils.isEmpty(channelCode)) {
			return null;
		}
		List<String> shopCodes = null;
		return shopCodes;
	}
}
