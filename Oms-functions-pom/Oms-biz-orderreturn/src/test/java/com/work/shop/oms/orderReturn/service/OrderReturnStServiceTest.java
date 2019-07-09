package com.work.shop.oms.orderReturn.service;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;

public class OrderReturnStServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderReturnStService> reference = null;
	
	private OrderReturnStService orderReturnStService = null;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderReturnStService>();
		reference.setUrl("json://10.80.16.5:8080/Oms/dubbo/com.work.shop.oms.orderReturn.service.OrderReturnStService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderReturn.service.OrderReturnStService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderReturnStService.class);
		reference.setVersion("1.0.0");
		orderReturnStService = reference.get();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testConfirm() throws Exception{
		System.out.println(orderReturnStService.returnStorageCancle("TD160818805971", "system", "TD16081880597120160819021027"));
	}
	
	
}
