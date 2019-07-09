package com.work.shop.oms.order.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.bean.PageListParam;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.TimeUtil;

public class BGOrderInfoServiceTest extends TestCase {

	String userId = "wx679150";
	String siteCode = "WXC";
	String orderSn = "1810161211134192";
	String isHistory = "0";
	String paySn = "FK1609281423392701";
	private ApplicationConfig application = null;
	private ReferenceConfig<BGOrderInfoService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<BGOrderInfoService>();
//		reference.setUrl("json://192.168.2.11:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://172.31.249.149:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://10.8.35.115:8010/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
		reference.setUrl("json://172.27.0.7:8010/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(BGOrderInfoService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOrderPageList() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		PageListParam searchParam = new PageListParam();
		searchParam.setUserId("quyachu8611");
		searchParam.setSiteCode("Chlitina");
		searchParam.setRstatus(0);
		searchParam.setPageNum(1);
		searchParam.setPageSize(10);
//		isHistory

		// {"flag":false,"from":"PC","goodsSn":"","isHistory":"0","orderSn":"","pageNum":1,"pageSize":10,"rstatus":0,"siteCode":"Chlitina","userId":"1100022360"}
		ApiReturnData info = bgOrderInfoService.orderPageList(searchParam);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testOrderReturnPageList() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		PageListParam searchParam = new PageListParam();
		searchParam.setUserId("M12345678901");
		searchParam.setSiteCode("NEWFORCE");
		searchParam.setPageNum(1);
		searchParam.setPageSize(10);
		ApiReturnData info = bgOrderInfoService.orderReturnPageList(searchParam);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}

	
	@Test
	public void testorderInfoDetail() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		orderSn = "1901231614384459";
		userId = "wx679148";
		siteCode = "WXC";
		ApiReturnData info = bgOrderInfoService.orderInfoDetail(orderSn, isHistory, userId, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testgetOrderGoodsByOrderSn() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		List<String> orderSns  = new ArrayList<String>();
		orderSns.add(orderSn);
		ApiReturnData info = bgOrderInfoService.getOrderGoodsByOrderSn(orderSns, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testgetOrderGoodsByPaySn() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ApiReturnData info = bgOrderInfoService.getOrderGoodsByPaySn(paySn, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testgetUserOrderType() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ApiReturnData info = bgOrderInfoService.getUserOrderType(userId, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testcancelOrderByMasterSn() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ApiReturnData info = bgOrderInfoService.cancelOrderByMasterSn(orderSn, userId, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testgetMasterOrderSnByPaySn() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ApiReturnData info = bgOrderInfoService.getMasterOrderSnByPaySn(paySn, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testchangeOrderUser() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ReturnInfo info = bgOrderInfoService.changeOrderUser("M123456789011", "M12345678901", siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	
	@Test
	public void testorderReview() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ReturnInfo info = bgOrderInfoService.orderReview(orderSn, true, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testgetOrderSimpleInfoByUser() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		ApiReturnData info = bgOrderInfoService.getOrderSimpleInfoByUser(userId, siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	
	@Test
	public void testConfirmReceipt() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		orderSn = "1707041911585321";
		ReturnInfo info = bgOrderInfoService.confirmReceipt(orderSn, null, "AAAAA", siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	
	@Test
	public void testConfirmReceipt2() throws Exception{
		System.out.println("1610171129266643S01".indexOf("1610171129266643") != -1);
		System.out.println("end");  
	}
	
	@Test
	public void testQueryNoPayOrder() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		PageListParam searchParam = new PageListParam();
		searchParam.setUserId("quyachu8611");
		searchParam.setSiteCode("Chlitina");
		ApiReturnData info = bgOrderInfoService.queryNoPayOrder(searchParam);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");
		// 172.31.249.149:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService?method=queryNoPayOrder&param=[{"userId":"quyachu8611","siteCode":"Chlitina"}]
		// {"data":["1704280924305157","1704280927095158","1704281420015159","1705031010545162","1705031017435163","1705031021325164","1705031041145165","1705031045065166","1705031051285167","1705031053325168","1705091017155187","1705091019335188"],"isOk":"1","message":"success"}
	}
	
	@Test
	public void testQueryRestrictionOrder() throws Exception{
		final BGOrderInfoService bgOrderInfoService = reference.get();
		PageListParam searchParam = new PageListParam();
		searchParam.setUserId("ceshi");
		searchParam.setSiteCode("qdm");
		Date startTime = TimeUtil.parseString2Date("2018-01-01 00:00:00");
		Date endTime = TimeUtil.parseString2Date("2018-06-02 00:00:00");
		searchParam.setStartTime(startTime);
		searchParam.setEndTime(endTime);
		searchParam.setShopCode("gz001");
		List<String> skuSns = new ArrayList<String>();
		skuSns.add("600058");
		skuSns.add("600300");
		searchParam.setSkuSns(skuSns);
		ApiReturnData info = bgOrderInfoService.queryRestrictionOrder(searchParam);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");
		// 172.31.249.149:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService?method=queryNoPayOrder&param=[{"userId":"quyachu8611","siteCode":"Chlitina"}]
		// {"data":["1704280924305157","1704280927095158","1704281420015159","1705031010545162","1705031017435163","1705031021325164","1705031041145165","1705031045065166","1705031051285167","1705031053325168","1705091017155187","1705091019335188"],"isOk":"1","message":"success"}
	}
}