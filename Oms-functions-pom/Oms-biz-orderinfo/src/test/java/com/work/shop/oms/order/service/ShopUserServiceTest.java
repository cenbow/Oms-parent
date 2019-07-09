package com.work.shop.oms.order.service;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;
import com.work.shop.oms.orderop.service.ShopUserService;

public class ShopUserServiceTest extends TestCase {

	String userId = "1100022360";
	private ApplicationConfig application = null;
	private ReferenceConfig<ShopUserService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<ShopUserService>();
		reference.setUrl("json://192.168.2.165:8089/Oms/dubbo/com.work.shop.oms.orderop.service.ShopUserService");
//		reference.setUrl("json://10.8.39.91:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://172.19.0.15:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://192.168.155.202:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(ShopUserService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOrderPageList() throws Exception{
		final ShopUserService shopUserService = reference.get();
		ReturnInfo<ShopUserInfo> info = shopUserService.getUserCreateOrderInfo(userId);
		System.out.println(JSON.toJSONString(info));  
		System.out.println("end");  
	}
}
