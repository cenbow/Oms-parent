package com.work.shop.oms.order.service;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.OrderGoodsQuery;
import com.work.shop.oms.common.bean.OrderGoodsSaleBean;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.vo.OrderReturnListVO;

import junit.framework.TestCase;

public class OrderQueryServiceTest extends TestCase {

	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderQueryService> reference;
	
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<OrderQueryService>();
		//reference.setUrl("json://10.8.35.115:8010/Oms/dubbo/com.work.shop.oms.order.service.OrderQueryService");
		reference.setUrl("json://172.27.0.7:8010/Oms/dubbo/com.work.shop.oms.order.service.OrderQueryService");
		reference.setTimeout(50000);
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderQueryService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOrderQuery() throws Exception { 
		final OrderQueryService orderQueryService = reference.get();
		
		OrderQueryRequest request = new OrderQueryRequest();
		request.setInsteadUserId("10000");
		request.setChannelCode("WBC");
		request.setUserId("wxa679288");
		request.setPageNo(1);
		request.setPageSize(100);
		request.setExportType(1);
		OrderQueryResponse response = orderQueryService.orderQuery(request);
		
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testPickUpOrderQuery() throws Exception {
		final OrderQueryService orderQueryService = reference.get();
		OrderQueryRequest request = new OrderQueryRequest();
		request.setPageNo(1);
		request.setPageSize(20);
		request.setChannelCode("qdm");
		request.setShopCode("gz001");
		request.setStoreCode("201003,101176");
		request.setStartTime("2018-06-05 00:00:00");
		request.setEndTime("2018-06-09 23:59:59");
		OrderQueryResponse response = orderQueryService.pickUpOrderQuery(request);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testOrderReturnApplyQuery() throws Exception {
		final OrderQueryService orderQueryService = reference.get();
		OmsBaseRequest<GoodsReturnChangeVO> request = new OmsBaseRequest<GoodsReturnChangeVO>();
		request.setPageNo(1);
		request.setPageSize(20);
		GoodsReturnChangeVO changeVO = new GoodsReturnChangeVO();
		changeVO.setOrderSn("1711171812005718");
		request.setData(changeVO);
		OmsBaseResponse<GoodsReturnChangeBean> response = orderQueryService.orderReturnApplyQuery(request);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testRiderOrderQuery() throws Exception {
		final OrderQueryService orderQueryService = reference.get();
		OrderQueryRequest request = new OrderQueryRequest();
		request.setPageNo(1);
		request.setPageSize(20);
		request.setChannelCode("qdm");
		request.setShopCode("gz001");
		request.setStartTime("2018-06-08 00:00:00");
		request.setEndTime("2018-06-08 23:59:59");
		OmsBaseResponse<OrderRiderDistributeLog> response = orderQueryService.riderOrderQuery(request);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testWaitSettleOrderQuery() throws Exception {
		final OrderQueryService orderQueryService = reference.get();
		OmsBaseRequest<OrderItem> request = new OmsBaseRequest<OrderItem>();
		request.setPageNo(1);
		request.setPageSize(20);
		OrderItem item = new OrderItem();
		request.setData(item);
		OmsBaseResponse<OrderItem> response = orderQueryService.waitSettleOrderQuery(request);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testWaitSettleReturnQuery() throws Exception {
		final OrderQueryService orderQueryService = reference.get();
		OmsBaseRequest<OrderReturnListVO> request = new OmsBaseRequest<OrderReturnListVO>();
		request.setPageNo(1);
		request.setPageSize(20);
		OrderReturnListVO item = new OrderReturnListVO();
		request.setData(item);
		OmsBaseResponse<OrderReturnListVO> response = orderQueryService.waitSettleReturnQuery(request);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testOrderGoodsSaleQuery() throws Exception { 
		final OrderQueryService orderQueryService = reference.get();
		
		OmsBaseRequest<OrderGoodsQuery> request = new OmsBaseRequest<OrderGoodsQuery>();
		OrderGoodsQuery orderGoodsQuery = new OrderGoodsQuery();
		orderGoodsQuery.setBeginTime("2018-09-10 00:00:00");
		orderGoodsQuery.setEndTime("2018-10-09 23:59:59");
		orderGoodsQuery.setStart(0);
		orderGoodsQuery.setLimit(1000);
		request.setData(orderGoodsQuery);
		OmsBaseResponse<OrderGoodsSaleBean> response = orderQueryService.orderGoodsSaleQuery(request);
		
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testOrderDepotShipQuery() throws Exception { 
		final OrderQueryService orderQueryService = reference.get();
		
		OrderQueryRequest request = new OrderQueryRequest();
		request.setStartTime("2018-12-22 00:00:00");
		request.setEndTime("2019-01-24 00:00:00");
		request.setPageNo(1);
		request.setPageSize(10);
		request.setExportType(1);
		OrderQueryResponse response = orderQueryService.orderDepotShipQuery(request);
		
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testOrderReturnQuery() throws Exception { 
		final OrderQueryService orderQueryService = reference.get();
		
		OmsBaseRequest<OrderReturnListVO> request = new OmsBaseRequest<OrderReturnListVO>();
		request.setPageNo(1);
		request.setPageSize(10);
		
		OrderReturnListVO returnVo = new OrderReturnListVO();
		returnVo.setMasterOrderSn("1812051201044389");
		returnVo.setSearchType(1);
		
		request.setData(returnVo);
		
		OmsBaseResponse<OrderReturnListVO> response = orderQueryService.orderReturnQuery(request);
		
		System.out.println(JSONObject.toJSONString(response));
	}
}
