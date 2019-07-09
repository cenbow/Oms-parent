package com.work.shop.oms.payment.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.PayBackInfo;
import com.work.shop.oms.api.param.bean.PayReturnInfo;
import com.work.shop.oms.api.payment.service.OrderPaymentService;
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

public class OrderPaymentServiceTest extends TestCase {

	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderPaymentService> reference;
	
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<OrderPaymentService>();
		//reference.setUrl("json://10.8.35.115:8010/Oms/dubbo/com.work.shop.oms.order.service.OrderQueryService");
		reference.setUrl("json://172.26.0.4:8080/OmsPay/dubbo/com.work.shop.oms.api.payment.service.OrderPaymentService");
		reference.setTimeout(50000);
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderPaymentService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testPayStChClient() throws Exception { 
		final OrderPaymentService orderPaymentService = reference.get();
		
		PayBackInfo payBackInfo = new PayBackInfo();
		payBackInfo.setPaySn("FK190128191240432801");
		
		// 操作人
		payBackInfo.setActionUser("wx679375");
		
		// 订单号
		List<String> orderSnList = new ArrayList<String>(3);
		orderSnList.add("1901281912404328");
		payBackInfo.setOrderSnList(orderSnList);
		payBackInfo.setAmountToPay(201.5);
		
		payBackInfo.setPayNote("3019012802403617835999");
		
		PayReturnInfo payReturnInfo = orderPaymentService.payStChClient(payBackInfo);
		
		System.out.println(JSONObject.toJSONString(payReturnInfo));
	}
}
