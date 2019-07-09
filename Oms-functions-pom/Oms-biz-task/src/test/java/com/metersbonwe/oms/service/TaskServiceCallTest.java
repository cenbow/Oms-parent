package com.metersbonwe.oms.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.core.task.ITaskServiceCall;
import com.work.shop.oms.orderop.service.OrderConfirmService;

import junit.framework.TestCase;

public class TaskServiceCallTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<ITaskServiceCall> reference = null;
	
	private ITaskServiceCall taskServiceCall = null;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<ITaskServiceCall>();
		reference.setUrl("json://192.168.196.151:8080/Oms/dubbo/com.work.shop.oms.core.task.ITaskServiceCall");
		reference.setTimeout(500000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(ITaskServiceCall.class);
		reference.setVersion("1.0.0");
		taskServiceCall = reference.get();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testConfirm() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("jobType", "gotOrderNoticeTask");
		taskServiceCall.processTask(param);
	}
	
	@Test
	public void testRiderDistTask() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("jobType", "riderDistTask");
		taskServiceCall.processTask(param);
	}
	
	
	public void testAutoReceiptTask() throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("jobType", "autoReceiptTask");
		taskServiceCall.processTask(param);
	}
	
	@Test
	public void testOrderDistributeOutTask() throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("jobType", "orderDistributeOutTask");
		taskServiceCall.processTask(param);
	}
}