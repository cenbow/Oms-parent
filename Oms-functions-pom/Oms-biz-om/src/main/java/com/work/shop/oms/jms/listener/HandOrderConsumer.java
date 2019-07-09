package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.HandOrderService;

@Service
public class HandOrderConsumer extends Consumer {
	private static Logger logger = Logger.getLogger(HandOrderConsumer.class);
	
	@Resource(name="handOrderServiceImpl")
	private HandOrderService handOrderService;

	@Override
	public String getDATA(String text) {
		logger.info("执行手工打单操作 start" + text);
		try {
			ReturnInfo<String> info = handOrderService.doCreateOrder(text);
			logger.info("执行手工打单操作 end " + text + ";info:" + JSON.toJSONString(info));
		} catch (Exception e) {
			logger.error(text + "执行手工打单操作失败", e);
		}
		return "";
	}

}
