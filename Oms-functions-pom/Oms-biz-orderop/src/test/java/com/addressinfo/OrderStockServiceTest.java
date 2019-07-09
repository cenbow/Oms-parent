package com.addressinfo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.stockcenter.client.dto.PlatformSkuStock;
import com.work.shop.stockcenter.client.dto.SkuStock;
import com.work.shop.stockcenter.client.dto.StockOperatePO;

public class OrderStockServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<UniteStockService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<UniteStockService>();
		reference.setUrl("json://192.168.1.3:8080/Oms/dubbo/com.work.shop.oms.stock.service.UniteStockService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
		reference.setTimeout(5000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(UniteStockService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testSkuStockNum() throws Exception{
		final UniteStockService orderStockService = reference.get();
//		ReturnInfo info = orderStockService.skuStockNum("1605191359250364");
//		System.out.println(JSON.toJSONString(info));
	}
	
	@Test
	public void testStockBySkus() throws Exception{
		String businessId = "123456789112";
		String shopCode = "21015";
		final UniteStockService orderStockService = reference.get();

		StockOperatePO stockOperatePO = new StockOperatePO();
		List<PlatformSkuStock> skuStocks = new ArrayList<PlatformSkuStock>();
		PlatformSkuStock skuStock = new PlatformSkuStock();
		skuStock.setSku("80000110023");
		skuStock.setStock(1);
		skuStocks.add(skuStock);
		stockOperatePO.setSkuStockList(skuStocks);
		stockOperatePO.setBusinessId(businessId);
		stockOperatePO.setChannelCode(shopCode);
		ReturnInfo info = orderStockService.occupyByOutChannel(stockOperatePO);
		System.out.println(JSON.toJSONString(info));
	}
	
	
	@Test
	public void testOccupy() throws Exception{
		final UniteStockService orderStockService = reference.get();
		ReturnInfo info = orderStockService.occupy("1606071605221014");
		System.out.println(JSON.toJSONString(info));
	}
	
	@Test
	public void testRealese() throws Exception{
		final UniteStockService orderStockService = reference.get();
		ReturnInfo info = orderStockService.realese("1606071605221014");
		System.out.println(JSON.toJSONString(info));
	}
	
}
