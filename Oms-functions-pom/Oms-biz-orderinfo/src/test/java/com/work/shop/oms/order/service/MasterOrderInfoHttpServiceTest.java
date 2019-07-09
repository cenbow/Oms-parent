package com.work.shop.oms.order.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.common.bean.ErpStatusInfoTemp;
import com.work.shop.oms.common.bean.GoodsCardInfo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.OrdersCreateReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.StringUtil;

public class MasterOrderInfoHttpServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private String orderFrom = "HQ01S116";
	private String referer = "wap";
	private String consignee = "YF";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "tuqin";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double shippingTotalFee = 0D;							// 运费
	private Double moneyPaid = 0D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	private int payStatus = 0;
	private double totalPayable = 0D;								
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<MasterOrderInfoService>();
//		reference.setUrl("json://10.80.16.2:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://10.101.1.81:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(MasterOrderInfoService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testCreate() throws UnsupportedEncodingException {
		List<ErpStatusInfoTemp> orderGoodsInfoList = new ArrayList<ErpStatusInfoTemp>();
		ErpStatusInfoTemp param = new ErpStatusInfoTemp();
		param.setAddTime(new Date());
		param.setOsOrderCode("1606301743324553s02");
		param.setDocType("DEG");
		param.setDocOrgSate("sss");
		orderGoodsInfoList.add(param);
//		String url = "http://10.100.22.201:8080/Oms/api/createOrders";
		String url = "http://localhost:8081/Oms/api/ordererpstatusprovier.do";
		List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
		valuePairs.add(new BasicNameValuePair("data", JSON.toJSONString(orderGoodsInfoList)));
		String encodeMobel = HttpClientUtil.post(url, valuePairs);
		System.out.println(URLDecoder.decode(encodeMobel, "utf-8"));
	}

}
