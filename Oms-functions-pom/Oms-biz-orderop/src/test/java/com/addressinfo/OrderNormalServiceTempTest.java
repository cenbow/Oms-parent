package com.addressinfo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderNormalService;

public class OrderNormalServiceTempTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderNormalService> reference = null;
	
	private OrderNormalService orderNormalService = null;
	
	private String masterOrderSn = "1605091917280110";
	
	private String orderSn = "1605141127460150s01";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderNormalService>();
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderNormalService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeOpService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderNormalService.class);
		reference.setVersion("1.0.0");
		orderNormalService = reference.get();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditGoods() throws Exception{
		masterOrderSn = "1608152009268986";
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		ReturnInfo info = orderNormalService.normalOrderByOrderSn(masterOrderSn, orderStatus);
		System.out.println(info);
	}
	
	@Test
	public void testDistributeNormal() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("返回正常单：");
		orderStatus.setType("0");
		orderStatus.setSwitchFlag(false);
		ReturnInfo info = orderNormalService.normalOrderByOrderSn("1605301446460547s03", orderStatus);
		System.out.println(info);
	}
	
	@Test
	public void testAdvanceSaleClose() throws Exception{
		List<String> list = new ArrayList<String>();
		list.add("20209599141");
		ReturnInfo info = orderNormalService.advanceSaleClose("HQ01S115", list);
		System.out.println(info);
	}
}
