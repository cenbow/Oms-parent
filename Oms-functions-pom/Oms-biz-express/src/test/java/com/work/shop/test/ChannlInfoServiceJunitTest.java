package com.work.shop.test;
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
		try {
			String company = "zhongtong";
			String trackNo = "532450657174";
//			ExpressInfo expressInfo = HttpClientUtil.getKuaiDi100ByHtmlApi(company, trackNo);
			//ExpressInfo expressInfo = HttpClientUtil.getKuaidi100WithNoVal(trackNo, company);
//			ExpressInfo expressInfo = HttpClientUtil.getKuaiDi100ByHtmlApi(company, trackNo);
			//System.out.println(JSON.toJSONString(expressInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
