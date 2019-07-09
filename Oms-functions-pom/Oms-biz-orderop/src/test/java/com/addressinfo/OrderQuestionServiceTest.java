package com.addressinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.DistributeShipBean;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.OrderToShippedProviderBeanParam;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.orderop.service.OrderQuestionService;

public class OrderQuestionServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderQuestionService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderQuestionService>();
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderQuestionService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderQuestionService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testMasterQuestion() throws Exception{
		final OrderQuestionService orderQuestionService = reference.get();
//		ReturnInfo info = orderCommonService.questionOrderByOrderSn("1605301446460547s03", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		ReturnInfo info = orderQuestionService.questionOrderByMasterSn("1606011117540598", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		System.out.println(info.toString());
	}
	
	@Test
	public void testQuestion() throws Exception{
		final OrderQuestionService orderQuestionService = reference.get();
//		ReturnInfo info = orderCommonService.questionOrderByOrderSn("1605301446460547s03", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		ReturnInfo info = orderQuestionService.questionOrderByOrderSn("1606011117540598s03", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		System.out.println(info.toString());
	}
	
	
	@Test
	public void testEditGoods() throws Exception{
		List<LackSkuParam> lackSkuParams = new ArrayList<LackSkuParam>();
		LackSkuParam param = new LackSkuParam();
		param.setCustomCode("20780470140");
		param.setDepotCode("A01339S011");
		param.setLackNum((short)1);
		param.setQuestionCode("9953");
		param.setLackReason("ssss");
		lackSkuParams.add(param);
		final OrderQuestionService orderQuestionService = reference.get();
		System.out.println(JSON.toJSONString(lackSkuParams));
		ReturnInfo info = orderQuestionService.addLackSkuQuestion("1607041123084727S01", lackSkuParams, new OrderStatus("", "问题单", "HQ01UC771", "9953"));
		System.out.println(info);
	}
}
