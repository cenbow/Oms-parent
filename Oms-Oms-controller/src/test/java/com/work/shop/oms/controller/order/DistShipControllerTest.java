package com.work.shop.oms.controller.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.orderop.feign.OrderCommonService;
import com.work.shop.oms.ship.bean.DistOrderPackageItems;
import com.work.shop.oms.ship.bean.DistOrderPackages;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;

import junit.framework.TestCase;

public class DistShipControllerTest extends TestCase {

//	private OrderCommonService orderCommonService;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testDistOrderShip() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7795/order/distOrderShip";
		String info = null;
		try {
			DistOrderShipRequest request = new DistOrderShipRequest();
			String orderSn = "1906181453174905S01";
			String warehouseCode = "hbis";
			request.setOrderSn(orderSn);
			request.setWarehouseCode(warehouseCode);
			
			List<DistOrderPackages> orderPackages = new ArrayList<DistOrderPackages>();
			DistOrderPackages orderPackage = new DistOrderPackages();
			orderPackage.setDeliveryTime(new Date());
			orderPackage.setExpressCode("822615035529");
			orderPackage.setLogisticsCode("yuantong");
			orderPackage.setLogisticsName("申通快递");
			List<DistOrderPackageItems> items = new ArrayList<DistOrderPackageItems>();
			DistOrderPackageItems item = new DistOrderPackageItems();
			item.setItemCode("AA9070");
			item.setOwnerCode(warehouseCode);
			item.setQuantity(5);
			items.add(item);

			orderPackage.setItems(items);
			orderPackages.add(orderPackage);
			request.setPackages(orderPackages);

//			DistOrderShipResponse response = orderCommonService.distOrderShip(request);
			
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			System.out.println(JSON.toJSONString(request));
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//			post.setRequestHeader("Accept", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(request));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testShippedConfirm() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7795/order/shippedConfirm";
		String info = null;
		try {
			DistOrderShipRequest request = new DistOrderShipRequest();
			String orderSn = "1906141546564897S01";
			request.setOrderSn(orderSn);
//			DistOrderShipResponse response = orderCommonService.shippedConfirm(request);

			
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			System.out.println(JSON.toJSONString(request));
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//			post.setRequestHeader("Accept", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(request));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testOrderExpressOmsQuery() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7785/express/orderExpressOmsQuery";
		String info = null;
		try {
			OrderExpressTracing tracing = new OrderExpressTracing();
			String orderSn = "1906181453174905";
			tracing.setOrderSn(orderSn);
			tracing.setTrackno("822615035529");
			tracing.setDepotCode("hbis");
			
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			System.out.println(JSON.toJSONString(tracing));
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(tracing));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testOrderExpressQuery() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7785/express/orderExpressQuery";
		String info = null;
		try {
			OrderExpressTracing tracing = new OrderExpressTracing();
			String orderSn = "1906181453174905";
			tracing.setOrderSn(orderSn);
			
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			System.out.println(JSON.toJSONString(tracing));
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(tracing));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}