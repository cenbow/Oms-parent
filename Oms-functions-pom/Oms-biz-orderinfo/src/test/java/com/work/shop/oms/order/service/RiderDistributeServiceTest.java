package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.rider.service.RiderDistributeService;

import junit.framework.TestCase;

public class RiderDistributeServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	private ReferenceConfig<RiderDistributeService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<RiderDistributeService>();
		reference.setUrl("json://192.168.2.11:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://172.31.249.149:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://172.19.0.15:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://192.168.155.202:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(RiderDistributeService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void saveRiderDistributeInfoListTest() {
		List<String> orderSnList = new ArrayList<String>();
		orderSnList.add("111111");
		orderSnList.add("111112");
		
		final RiderDistributeService service = reference.get();
		ServiceReturnInfo<Boolean> result = service.saveRiderDistributeInfoList(orderSnList, 1);
		System.out.println(JSONObject.toJSONString(result));
	}

}
