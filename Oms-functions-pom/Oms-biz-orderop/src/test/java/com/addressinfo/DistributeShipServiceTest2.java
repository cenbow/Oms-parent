package com.addressinfo;

import java.util.Date;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.ship.service.DistributeShipService;

import junit.framework.TestCase;

public class DistributeShipServiceTest2 extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<DistributeShipService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<DistributeShipService>();
		reference.setUrl("json://192.168.1.5:9010/Oms/dubbo/com.work.shop.oms.ship.service.DistributeShipService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
		reference.setTimeout(5000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(DistributeShipService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testProcessShip() throws Exception {
		final DistributeShipService distributeShipService = reference.get();
		DistributeShippingBean distributeShipBean = new DistributeShippingBean();
		distributeShipBean.setOrderSn("1809102120053931S01");
		distributeShipBean.setShipDate(new Date());
		distributeShipBean.setShippingCode("STO");
		distributeShipBean.setInvoiceNo("1234321");
		System.out.println(JSON.toJSON(distributeShipBean));
		ReturnInfo<String> info = distributeShipService.processShip(distributeShipBean, false);
		System.out.println(JSON.toJSONString(info));
	}
}
