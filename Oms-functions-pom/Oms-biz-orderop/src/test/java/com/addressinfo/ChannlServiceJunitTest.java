package com.addressinfo;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.bean.ChannelShop;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.StringUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class ChannlServiceJunitTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<ChannelService> reference = null;
	private int index = 0;
	private String channelCode = "HQ01S116";
	private String referer = "WAP";
	private String consignee = "屈磊明";
	private String mobile = "18917519266";
	private String emil = "abcd@qq.com";
	private String userId = "屈磊明";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double shippingTotalFee = 6D;							// 运费
	private Double moneyPaid = 8D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	
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
		ReturnInfo<List<ChannelShop>> info = new ReturnInfo<List<ChannelShop>>(Constant.OS_NO);
		List<ChannelShop> infos = new ArrayList<ChannelShop>();
		try {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("shopCode", "HQ01S115"));
			valuePairs.add(new BasicNameValuePair("type", String.valueOf(1)));
			String result = HttpClientUtil.post("http://10.100.200.64/ChannelService/custom/channelShopApi/findShopInfoByShopCode.spmvc", valuePairs);
			JsonResult jsonResult = null;
			if (StringUtil.isNotEmpty(result)) {
				jsonResult = JSONObject.parseObject(result, JsonResult.class);
			}
			if (null != jsonResult && jsonResult.getData() != null) {
				infos = JSONArray.parseArray(jsonResult.getData().toString(), ChannelShop.class);
			}
			info.setData(infos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(infos));
	}
}
