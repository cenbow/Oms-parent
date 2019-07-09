package com.work.shop.oms.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.vo.ReturnGoodsVO;
import com.work.shop.oms.vo.StorageGoods;

import junit.framework.TestCase;

public class ReturnManagementServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<ReturnManagementService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");
		reference = new ReferenceConfig<ReturnManagementService>();
		reference.setUrl("json://192.168.1.5:9010/Oms/dubbo/com.work.shop.oms.order.service.ReturnManagementService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(ReturnManagementService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testReturnItemCreate() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		ApplyItem applyItem = new ApplyItem();
		applyItem.setMasterOrderSn("1711171812005718");
		ReturnManagementResponse response = returnManagementService.returnItemCreate(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testUpdateStatusBatch() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		ApplyItem applyItem = new ApplyItem();
		applyItem.setMasterOrderSn("1711171812005718");
		ReturnManagementResponse response = returnManagementService.returnItemConfirm(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testReturnItemGet() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setMasterOrderSn("");
		request.setReturnSn("TD170928038886");
		request.setReturnType("1");
		ReturnManagementResponse response = returnManagementService.returnItemGet(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testReturnItemCreateInit() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setMasterOrderSn("1806012324343746");
		request.setReturnType("3");
		request.setActionUser("aaaa");
		request.setActionUserId("1");
		ReturnManagementResponse response = returnManagementService.returnItemCreateInit(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testReturnWaitStorageItem() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setReturnSn("TD180910039340");

		OmsBaseResponse<ReturnGoodsVO> response = returnManagementService.returnWaitStorageItem(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	
	@Test
	public void testReturnItemStorage() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setReturnSn("TD180910039340");
		List<StorageGoods> storageGoods = new ArrayList<StorageGoods>();
		StorageGoods goods = new StorageGoods();
		goods.setCustomCode("10008000010");
		goods.setId(2412L);
		goods.setProdScanNum(1);
		storageGoods.add(goods);
		request.setStorageGoods(storageGoods);
		ReturnManagementResponse response = returnManagementService.returnItemStorage(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testReturnItemSettlement() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setReturnSn("TD180911039370");
		request.setActionUser("system");
		ReturnManagementResponse response = returnManagementService.returnItemSettlement(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
	@Test
	public void testReturnRefundCompleted() throws Exception{
		final ReturnManagementService returnManagementService = reference.get();
		ReturnManagementRequest request = new ReturnManagementRequest();
		request.setReturnSn("TD180911039363");
		request.setActionUser("system");
		request.setActualRefundAmount(new BigDecimal(0.01));
		ReturnManagementResponse response = returnManagementService.returnRefundCompleted(request);
		System.out.println("cost:isOk=" + JSON.toJSONString(response));
		System.out.println("end");
	}
	
}