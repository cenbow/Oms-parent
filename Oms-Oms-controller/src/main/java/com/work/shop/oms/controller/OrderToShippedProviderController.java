package com.work.shop.oms.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 
 * 提供给ERP调用的接口，获取EEP给OS订单发货信息。将这个信息提交到MQ队列中
 * 
 * @author DE
 */
@Controller
public class OrderToShippedProviderController {


	Logger log = Logger.getLogger(OrderToShippedProviderController.class);

	@Resource
	DistributeShipService distributeShipService;
	/*
	 * 
	 */
	@RequestMapping(value = "/ordertoshippedprovier")
	@ResponseBody
	public String TOMQ(@RequestParam(value = "data") String data) {
		log.info("ordertoshippedprovier发货工具data=" + data);
		// 将发货信息列表拆分为按订单
		if (StringUtil.isTrimEmpty(data)) {
			log.error("订单发货信息为空！");
			return "0";
		}
		try {
			distributeShipService.acceptShipData(data);
		} catch (Exception e) {
			log.error("接收发货订单请求转换对象异常：", e);
		}
		return "1";
	}

}
