package com.addressinfo;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderDistributeEditService;
import com.work.shop.oms.utils.HttpClientUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class OrderAddressEditJunitTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	ReferenceConfig<OrderDistributeEditService> reference;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderDistributeEditService>();
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeEditService");
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(OrderDistributeEditService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOMSWeb() throws Exception{
		final OrderDistributeEditService addressInfoEditService = reference.get();
		long begin = System.currentTimeMillis();
		// 服务调用
		ConsigneeModifyInfo consignInfo = new ConsigneeModifyInfo();
		consignInfo.setCountry("1");
		consignInfo.setProvince("310000");
		consignInfo.setCity("310100");
		consignInfo.setDistrict("310115");
		consignInfo.setStreet("");
		consignInfo.setAddress("康桥东路700号");
		consignInfo.setMobile("18917519266");
		consignInfo.setTel("8362");
		consignInfo.setConsignee("测试671");
		consignInfo.setEmail("1");
		consignInfo.setBestTime("1");
		consignInfo.setSignBuilding("1");
		consignInfo.setZipcode("20000");
		consignInfo.setActionUser("HQ01UC771");
//		ReturnInfo info = blogQueryService.editConsigneeInfoByOrderSn("1605021411510081s01", consignInfo, "HQ01UC771");
		ReturnInfo info = addressInfoEditService.editConsigneeInfoByMasterSn("1605021411510081", consignInfo);
		long end = System.currentTimeMillis();
		System.out.println(" cost:isOk=" + info.getIsOk() + ";message=" + info.getMessage());  
		System.out.println(" cost:" + (end - begin));  
		System.out.println("end");  
	}
	
	@Test
	public void testAddress() {
		ConsigneeModifyInfo consignInfo = new ConsigneeModifyInfo();
		consignInfo.setCountry("1");
		consignInfo.setProvince("310000");
		consignInfo.setCity("310100");
		consignInfo.setDistrict("310115");
		consignInfo.setStreet("");
		consignInfo.setAddress("康桥东路700号");
		consignInfo.setMobile("18917519266");
		consignInfo.setTel("8362");
		consignInfo.setConsignee("测试671");
		consignInfo.setEmail("1");
		consignInfo.setBestTime("1");
		consignInfo.setSignBuilding("1");
		consignInfo.setZipcode("20000");
		consignInfo.setActionUser("HQ01UC771");
		consignInfo.setSource("ERP");
		consignInfo.setOrderSn("1606101611371166s01");
		List<Object> param = new ArrayList<Object>();
		param.add(consignInfo);
		String url = "http://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService";
		try {
			List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
			valuePairs.add(new BasicNameValuePair("method", "editConsigneeInfoByOrderSn"));
//			valuePairs.add(new BasicNameValuePair("param", URLEncoder.encode(JSON.toJSONString(param), "utf-8")));
			valuePairs.add(new BasicNameValuePair("param", JSON.toJSONString(param)));
			String encodeMobel = HttpClientUtil.post(url, valuePairs);
			System.out.println(URLDecoder.decode(encodeMobel, "utf-8"));
		} catch (Exception e) {
			
		}
	}
}
