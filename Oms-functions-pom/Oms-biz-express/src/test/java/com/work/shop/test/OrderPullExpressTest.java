package com.work.shop.test;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressPullService;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.TimeUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class OrderPullExpressTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderExpressPullService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderExpressPullService>();
		reference.setUrl("json://10.8.39.91:8088/ExpressService/dubbo/com.work.shop.oms.api.express.service.OrderExpressPullService");
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(OrderExpressPullService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditGoods() throws Exception{
		try {
			String com = "yunda";
			String invoice = "7760000033827";
			String from = null;
			String to = null;
//			ExpressInfo expressInfo = HttpClientUtil.getKuaiDi100ByHtmlApi(company, trackNo);
//			ExpressInfo expressInfo = HttpClientUtil.getKuaidi100WithNoVal(trackNo, company);
//			ExpressInfo expressInfo = HttpClientUtil.getKuaiDi100ByHtmlApi(company, trackNo);
			String queryUrl = "http://poll.kuaidi100.com/poll/query.do";
			Map<String, String> param = new HashMap<String, String>();
			param.put("com", com);
			param.put("num", invoice);
			param.put("from", from);
			param.put("to", to);
			String paramString = JSON.toJSONString(param);
			String key = "dXbJKywE4715";
			String customer = "9CCCE8F2601FD10B76473FE86E9AA74F";
			String sign = getSign(paramString, key, customer);
//			String queryUrl = kuaidi100Url + "?customer=" + customer + "&sign=" + sign+ "&param=" + paramString;
			System.out.println("请求快递100接口 queryUrl：" + queryUrl);
			
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("customer", customer));
			valuePairs.add(new BasicNameValuePair("sign", sign));
			valuePairs.add(new BasicNameValuePair("param", paramString));
			System.out.println("请求快递100接口 queryUrl：" + JSON.toJSONString(valuePairs));
			//String response=HttpClientUtil.post(queryUrl, valuePairs);
			//System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getSign(String paramString, String key, String customer) {
		String rs = paramString + key + customer;
		String md5String = MD5(rs);
		return md5String;
	}
	
	private static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			System.out.println("MD5异常");
			return null;
		}
	}
	
	@Test
	public void testOrderPullExpress() throws Exception{
		try {
			final OrderExpressPullService orderExpressPullService = reference.get();
			String orderSn = "16101416533755";
			ReturnInfo<String> info = orderExpressPullService.orderExpress(orderSn);
			System.out.println(JSON.toJSONString(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
