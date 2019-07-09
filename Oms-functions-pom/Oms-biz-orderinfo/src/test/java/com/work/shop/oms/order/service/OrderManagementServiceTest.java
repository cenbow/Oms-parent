package com.work.shop.oms.order.service;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OrderManagementResponse;

import junit.framework.TestCase;

public class OrderManagementServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<OrderManagementService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");
		reference = new ReferenceConfig<OrderManagementService>();
		reference.setUrl("json://192.168.1.5:9010/Oms/dubbo/com.work.shop.oms.order.service.OrderManagementService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(OrderManagementService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOrderSettlement() throws Exception{
		final OrderManagementService orderManagementService = reference.get();
		OrderManagementRequest request = new OrderManagementRequest();
		request.setMasterOrderSn("1809102120053931");
		request.setActionUser("system");
		OrderManagementResponse response = orderManagementService.orderSettlement(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
}