package com.shop.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.ship.service.WkUdDistributeService;

public class OrderDistributeServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<WkUdDistributeService> reference = null;
	
	
	private WkUdDistributeService wkUdDistributeService;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<WkUdDistributeService>();
//		reference.setUrl("json://10.100.22.200:8080/" + OrderDistributeService.class.getName());
		reference.setUrl("json://192.168.22.235:8070/Oms/dubbo/com.work.shop.oms.ship.service.WkUdDistributeService");
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(OrderDistributeService.class);
		reference.setVersion("1.0.0");
		wkUdDistributeService = reference.get();
	}
	
	
	@Test 
	public void orderDistributeTest() {
		List<WkUdDistribute> wkUdDistributes = new ArrayList<WkUdDistribute>();
		WkUdDistribute wkUdDistribute = new WkUdDistribute();
		wkUdDistribute.setCreateDate(new Date());
		wkUdDistribute.setDepotName("上海B2C订单MBMC统一发货仓(测试)");
		wkUdDistribute.setDistId(13610856);
		wkUdDistribute.setDistWarehCode("HQ01W850");
		wkUdDistribute.setDistqty0(5);
		wkUdDistribute.setDistrate((float)1);
		wkUdDistribute.setOuterCode("1608091531234633S01");
		wkUdDistribute.setOverTransCycle("2016/08/11");
		wkUdDistribute.setPdwarhCode("HQ01");
		wkUdDistribute.setPdwarhName("美特斯邦威上海(测试)");
		wkUdDistribute.setProdId("60124730152");
		wkUdDistribute.setProvincecity("上海上海市");
		wkUdDistribute.setRcvWarehcode("HQ01W850");
		wkUdDistribute.setShipCode("yunda");
		wkUdDistribute.setShipType(0);
		wkUdDistribute.setToUser("gYj");
		wkUdDistribute.setToUserPhone("18500174532");
		wkUdDistribute.setVendeeAgentName("加盟");
		wkUdDistributes.add(wkUdDistribute);
		
		WkUdDistribute wkUdDistribute2 = new WkUdDistribute();
		wkUdDistribute2.setCreateDate(new Date());
		wkUdDistribute2.setDepotName("上海B2C订单MBMC统一发货仓(测试)");
		wkUdDistribute2.setDistId(13610856);
		wkUdDistribute2.setDistWarehCode("HQ01W700");
		wkUdDistribute2.setDistqty0(5);
		wkUdDistribute2.setDistrate((float)1);
		wkUdDistribute2.setOuterCode("1608091531234633S01");
		wkUdDistribute2.setOverTransCycle("2016/08/11");
		wkUdDistribute2.setPdwarhCode("HQ01");
		wkUdDistribute2.setPdwarhName("美特斯邦威上海(测试)");
		wkUdDistribute2.setProdId("60124730152");
		wkUdDistribute2.setProvincecity("上海上海市");
		wkUdDistribute2.setRcvWarehcode("HQ01W700");
		wkUdDistribute2.setShipCode("yunda");
		wkUdDistribute2.setShipType(0);
		wkUdDistribute2.setToUser("gYj");
		wkUdDistribute2.setToUserPhone("18500174532");
		wkUdDistribute2.setVendeeAgentName("加盟");
		wkUdDistributes.add(wkUdDistribute2);
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
