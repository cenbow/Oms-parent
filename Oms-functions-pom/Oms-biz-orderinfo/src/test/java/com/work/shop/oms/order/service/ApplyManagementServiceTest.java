package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;

import junit.framework.TestCase;

public class ApplyManagementServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<ApplyManagementService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");
		reference = new ReferenceConfig<ApplyManagementService>();
		reference.setUrl("json://10.8.35.115:8010/Oms/dubbo/com.work.shop.oms.order.service.ApplyManagementService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(ApplyManagementService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOMSWeb() throws Exception{
		final ApplyManagementService applyManagementService = reference.get();
		OmsBaseRequest<ApplyItem> request = new OmsBaseRequest<ApplyItem>();
		ApplyItem applyItem = new ApplyItem();
		applyItem.setMasterOrderSn("1711171812005718");
		request.setData(applyItem);
		OmsBaseResponse<GoodsReturnChange> response = applyManagementService.findGoodsReturnChangeByOrderSn(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testUpdateStatusBatch() throws Exception{
		final ApplyManagementService applyManagementService = reference.get();
		List<Integer> applyIds = new ArrayList<Integer>();
		applyIds.add(229);
		OmsBaseRequest<ApplyItem> request = new OmsBaseRequest<ApplyItem>();
		request.setActionUser("admin");
		ApplyItem applyItem = new ApplyItem();
		applyItem.setMasterOrderSn("1711171812005718");
		applyItem.setApplyStatus(2);
		applyItem.setApplyIds(applyIds);
		request.setData(applyItem);
		OmsBaseResponse<String> response = applyManagementService.updateStatusBatch(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}

}