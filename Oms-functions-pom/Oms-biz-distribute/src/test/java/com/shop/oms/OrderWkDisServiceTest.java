package com.shop.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.ship.service.WkUdDistributeService;

import junit.framework.TestCase;

public class OrderWkDisServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<WkUdDistributeService> reference = null;
	
	
	private WkUdDistributeService wkUdDistributeService;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<WkUdDistributeService>();
		reference.setUrl("json://192.168.1.5:9010/Oms/dubbo/com.work.shop.oms.ship.service.WkUdDistributeService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(WkUdDistributeService.class);
		reference.setVersion("1.0.0");
		wkUdDistributeService = reference.get();
	}
	
	
	@Test 
	public void orderDistributeTest() {
		List<WkUdDistribute> wkUdDistributes = new ArrayList<WkUdDistribute>();
		WkUdDistribute wkUdDistribute = new WkUdDistribute();
		wkUdDistribute.setCreateDate(new Date());
		wkUdDistribute.setDistWarehCode("D00001");
		wkUdDistribute.setRcvWarehcode("D00001");
		wkUdDistribute.setGoodsNumber("1");
		wkUdDistribute.setOuterCode("1809131738023966S01");
		wkUdDistribute.setProdId("10006600009");
		wkUdDistributes.add(wkUdDistribute);
		System.out.println(JSON.toJSONString(wkUdDistributes));
		boolean info = this.wkUdDistributeService.depot(wkUdDistributes);
		System.out.println(info);
	}


	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public static void main(String[] args) {
		String str = "1611010006166916,1611010006486919";
//		String url = "http://10.8.39.91:8089/Oms/dubbo/com.work.shop.oms.distribute.service.OrderDistributeService";
		String url = "http://172.19.0.8:8080/Oms/dubbo/com.work.shop.oms.distribute.service.OrderDistributeService";
		String[] arr = str.split(",");
		for (String masterOrderSn : arr) {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			List<Object> objects = new ArrayList<Object>();
			objects.add(masterOrderSn);
			valuePairs.add(new BasicNameValuePair("method", "pushXoms"));
			valuePairs.add(new BasicNameValuePair("param", JSON.toJSONString(objects)));
			System.out.println("请求重新推送接口接口 " + JSON.toJSONString(valuePairs));
			String response=com.work.shop.oms.utils.HttpClientUtil.post(url, valuePairs);
			System.out.println("masterOrderSn:" + masterOrderSn + " ;info:" + response);
		}
	}
}
