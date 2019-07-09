package com.work.shop.test;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.Express100Service;
import com.work.shop.oms.api.express.service.OrderExpressPullService;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.TimeUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class Express100ServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<Express100Service> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<Express100Service>();
//		reference.setUrl("json://10.8.39.91:8088/ExpressService/dubbo/com.work.shop.oms.api.express.service.Express100Service");
		reference.setUrl("json://172.19.0.8:81/ExpressService/dubbo/com.work.shop.oms.api.express.service.Express100Service");

		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(Express100Service.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditGoods() throws Exception{
		try {
			String company = "yunda";
			String trackNo = "7760000033827";
			String com = "yunda";
			String invoice = "7760000033827";
			//ExpressInfo expressInfo = HttpClientUtil.getKuaidi100NEW(trackNo, company, null, null);
			//System.out.println(JSON.toJSONString(expressInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testOrderPullExpress() throws Exception{
		try {
			final Express100Service express100Service = reference.get();
			express100Service.express();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testJsonObject() throws Exception{
		try {
			String message = "{\"message\":\"您的IP地址被禁止访问，因过多调用kuaidi100.com的数据所致，请通过正规渠道调用，或联系kuaidi@kingdee.com http://ckd.im/app\",\"status\":\"0\"}";
			//ExpressInfo exInfo = JSON.parseObject(message, ExpressInfo.class);
		//	System.out.println(JSON.toJSONString(exInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
