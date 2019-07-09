package com.addressinfo;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

public class OrderConfirmServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderConfirmService> reference = null;
	
	private OrderConfirmService orderConfirmService = null;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderConfirmService>();
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderConfirmService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderConfirmService.class);
		reference.setVersion("1.0.0");
		orderConfirmService = reference.get();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testConfirm() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		System.out.println(orderConfirmService.confirmOrderByMasterSn("1605311641140573", orderStatus));
	}
	
	@Test
	public void testConfirmOrderSn() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
//		System.out.println(orderConfirmService.confirmOrderByOrderSn("1605311641140573s01", orderStatus));
		System.out.println(orderConfirmService.confirmOrderByMasterSn("1605311942430589", orderStatus));

	}
	
	@Test
	public void testUnConfirm() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("未确认：");
		System.out.println(orderConfirmService.unConfirmOrderByMasterSn("1605312017070590", orderStatus));
	}

	@Test
	public void testUnConfirmOrderSn() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("未确认：");
		System.out.println(orderConfirmService.unConfirmOrderByOrderSn("1605311736500587s01", orderStatus));
	}
	
	@Test
	public void testPosConfirm() throws Exception{
		ConsigneeModifyInfo modifyInfo = new ConsigneeModifyInfo();
		modifyInfo.setOrderSn("1605251719560498s01");
		System.out.println(orderConfirmService.posConfirmOrder(modifyInfo));
	}
}
