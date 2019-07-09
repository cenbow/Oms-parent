package com.addressinfo;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.utils.TimeUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class ChannlInfoServiceJunitTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<ChannelService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<ChannelService>();
		reference.setUrl("json://10.100.200.64:80/ChannelService/dubbo/com.work.shop.oms.channel.service.ChannelService");
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(ChannelService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditGoods() throws Exception{
		ChannelService channelService = reference.get();
		try {
			channelService.getAllSynStockChannelShop();
//			String result = channelInfoService.findChannelShopByChannelCode("BG_CHANNEL_CODE");
			System.out.println(JSON.toJSONString(channelService.getAllSynStockChannelShop()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testShip() throws Exception{
		DistributeShippingBean distributeShipBean = new DistributeShippingBean();
		distributeShipBean.setOrderSn("1609211558547506");
		distributeShipBean.setShippingCode("sto");
		distributeShipBean.setShippingName("申通快递");
		distributeShipBean.setDepotCode("HQ01W500");
		distributeShipBean.setInvoiceNo("33333789");
		distributeShipBean.setShipDate(new Date());
		System.out.println(JSON.toJSONString(distributeShipBean));
	}
}
