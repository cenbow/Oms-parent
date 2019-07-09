package com.work.shop.oms.controller.order;

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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.methods.HttpPost;
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
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.OrdersCreateReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.StringUtil;

public class MasterOrderControllerTest extends TestCase {

	private String orderFrom = "hbis";
	private String referer = "wap";
	private String consignee = "YF";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "10000000";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double shippingTotalFee = 0D;							// 运费
	private Double moneyPaid = 0D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	private int payStatus = 2;
	private double totalPayable = 0D;
	private double points = 0;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testCreate() throws Exception{
		// 服务调用
		MasterOrder masterOrder = HQ01S116Order();
		System.out.println(JSON.toJSONString(masterOrder));
		String url = "http://10.0.75.1:7795/order/createOrder";
		String info = null;
		try {
			System.out.println("orderSettlementPrice:" + masterOrder.getOrderSettlementPrice());
			System.out.println("paySettlementPrice:" + masterOrder.getPaySettlementPrice());
			System.out.println("goodsSettlementPrice:" + masterOrder.getGoodsSettlementPrice());
			System.out.println("totalPayable:" + masterOrder.getTotalPayable());

			if (masterOrder.getOrderSettlementPrice().equals(masterOrder.getPaySettlementPrice())
					&& masterOrder.getOrderSettlementPrice().equals(masterOrder.getGoodsSettlementPrice())
					&& masterOrder.getTotalPayable() >= 0) {
				HttpClient httpclient = new HttpClient();
				PostMethod post = new PostMethod(url);
				post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
				System.out.println(JSON.toJSONString(masterOrder));
				post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//				post.setRequestHeader("Accept", "application/json; charset=UTF-8");
				post.setRequestBody(JSON.toJSONString(masterOrder));
				httpclient.executeMethod(post);
				info = new String(post.getResponseBody(), "utf-8");
				System.out.println(info);
			} else {
				System.out.println("订单金额异常");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testPay() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7795/order/orderUnLock";
		String info = null;
		try {
			// FKHL190530175829457501	HL1905301758294575	0	36	线下
			OrderManagementRequest request = new OrderManagementRequest();
			request.setMasterOrderSn("1906141429514895");
			request.setMessage("");
			request.setActionUserId("13");
			request.setActionUser("admin");
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//			post.setRequestHeader("Accept", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(request));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("end");
	}
	
	
	@Test
	public void testOrderReviewCompleted() throws Exception{
		// 服务调用
		String url = "http://10.0.75.1:7795/order/orderReviewCompleted";
		String info = null;
		try {
			OrderManagementRequest request = new OrderManagementRequest();
			request.setMasterOrderSn("1906181453174905");
			request.setMessage("");
			request.setActionUserId("13");
			request.setActionUser("admin");
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			post.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//			post.setRequestHeader("Accept", "application/json; charset=UTF-8");
			post.setRequestBody(JSON.toJSONString(request));
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(), "utf-8");
			System.out.println(info);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("end");
	}

	private MasterOrder HQ01S116Order() {
		MasterOrder masterOrder = new MasterOrder();
//		masterOrder.setOrderOutSn("123456789113");
		masterOrder.setActionUser("A00030U0002");
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setInsteadUserId("test");
		masterOrder.setTransType(transType);
		masterOrder.setSource(3);
		masterOrder.setIsNow(0);
//		masterOrder.setRegisterMobile("12345678901");
		masterOrder.setBonusId(null);
//		masterOrder.setExpectedShipDate(expectedShipDate);
		masterOrder.setIsAdvance((short)0);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
		goodsList.add(createMasterGoods("AA9070", "XXXXX", "/sources/images/goods/AA9070/AA9070_00.jpg", 189D, 10D, 0D, 10, "common","hbis"));
//		goodsList.add(createMasterGoods("80000110023", "美唇笔", "/sources/images/goods/TP/810743/810743_41_09.jpg", 1D, 1D, 0D, 1, "common","10911A01109A"));

		masterOrder.setShipList(createMasterShip(goodsList, null));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
//		masterPays.add(createMasterPay(36, "xianxia", 2, 100D, null, null));
		masterPays.add(createMasterPay(35, "zhangqi", 2, 100D, 5, 10D));
		
		
		masterOrder.setPayList(masterPays);
		double goodsAmount = 0;
		int goodsCount = 0;
		double discount = 0D;
		double goodsSettlementPrice = 0D;
		for (MasterGoods masterGoods :goodsList) {
			Integer goodsNumber = masterGoods.getGoodsNumber();
			goodsAmount += masterGoods.getGoodsPrice() * goodsNumber;
			goodsCount += masterGoods.getGoodsNumber();
			discount += (masterGoods.getGoodsPrice() - masterGoods.getTransactionPrice()) * goodsNumber;
			goodsSettlementPrice += (masterGoods.getTransactionPrice() - masterGoods.getShareBonus() - masterGoods.getIntegralMoney()) * goodsNumber;
		}
		if (payStatus == 0) {
			moneyPaid = 0D;
			totalPayable = goodsSettlementPrice + shippingTotalFee;
		} else if (payStatus == 1) {
			moneyPaid = 0D;
			totalPayable = goodsSettlementPrice + shippingTotalFee - points - surplus;
		} else {
			moneyPaid = goodsSettlementPrice + shippingTotalFee - points - surplus;
			totalPayable = 0D;
		}
		double paySettlementPrice = 0D;
		for (MasterPay masterPay : masterPays) {
			paySettlementPrice += masterPay.getPayTotalFee();
		}
		masterOrder.setNeedAudit(1);
		masterOrder.setGoodsSettlementPrice(goodsSettlementPrice + shippingTotalFee);
		masterOrder.setPaySettlementPrice(paySettlementPrice);
		masterOrder.setShippingTotalFee(shippingTotalFee);
		Double totalFee = goodsAmount - discount + shippingTotalFee;
		masterOrder.setTotalFee(totalFee);
//		Double totalPayable = totalFee - moneyPaid - surplus - bonus;
		masterOrder.setTotalPayable(totalPayable);
		masterOrder.setSurplus(surplus);
		masterOrder.setMoneyPaid(moneyPaid);
		masterOrder.setBonus(bonus);
		masterOrder.setPrName("");
		masterOrder.setGoodsAmount(goodsAmount);
		masterOrder.setGoodsCount(goodsCount);
		masterOrder.setDiscount(discount);
		masterOrder.setInvPayee("个人");
		masterOrder.setInvType("普通发票");
		masterOrder.setOrderType((byte)0);
		masterOrder.setOrderStatus(0);
		masterOrder.setPayStatus((short)payStatus);
		masterOrder.setMobile(mobile);
		masterOrder.setOrderSettlementPrice(moneyPaid + totalPayable + surplus + 0);
		masterOrder.setPoints(points);
		masterOrder.setBvValue(10);
		masterOrder.setBaseBvValue(10);
		masterOrder.setIsCac(0);
		return masterOrder;
	}
	
	private List<MasterShip> createMasterShip(List<MasterGoods> goodsList, List<GoodsCardInfo> cardInfos) {
		MasterShip masterShip = new MasterShip();
		masterShip.setShippingCode("99");
		masterShip.setCountry("1");
		masterShip.setProvince("310000");
		masterShip.setCity("310100");
		masterShip.setDistrict("310115");
		masterShip.setAddress("江苏省太仓市城厢镇新华东路54号3楼（发货）0512-53510086");
		masterShip.setMobile("18917519266");
		masterShip.setTel("021-83628324");
		masterShip.setConsignee(consignee);
		masterShip.setEmail(emil);
		masterShip.setBestTime(null);
		masterShip.setSignBuilding("1");
		masterShip.setZipcode("20000");
		masterShip.setChargeType((byte)0);
		masterShip.setGoodsList(goodsList);
		masterShip.setCardInfos(cardInfos);
		
//		masterShip.setProvinceCode("310000");
//		masterShip.setCityCode("310100");
//		masterShip.setDistrictCode("310115");
		masterShip.setAreaCode("1100005386");
		List<MasterShip> masterShips = new ArrayList<MasterShip>();
		masterShips.add(masterShip);
		return masterShips;
	}
	
	private MasterGoods createMasterGoods(String sku, String goodsName, String goodsThumb, Double goodsPrice,
			double transactionPrice, double shareBonus, int goodsNumber, String extensionCode, String depotCode) {
		MasterGoods masterGoods = new MasterGoods();
		masterGoods.setCustomCode(sku);
		masterGoods.setGoodsName(goodsName);
		masterGoods.setGoodsThumb(goodsThumb);
		masterGoods.setGoodsPrice(goodsPrice);
		masterGoods.setMarketPrice(goodsPrice);
		masterGoods.setTransactionPrice(transactionPrice);
		masterGoods.setShareBonus(shareBonus);
		masterGoods.setGoodsNumber(goodsNumber);
		masterGoods.setUseCard("");
		masterGoods.setExtensionCode(extensionCode);
		masterGoods.setPromotionDesc("");
		masterGoods.setIntegralMoney(0D);
		masterGoods.setSellerUser("A00030U0006");
		masterGoods.setSalesMode(1);
		masterGoods.setSupplierCode("MB");
		masterGoods.setSizeName("1盒");
		masterGoods.setColorName("无色");
		masterGoods.setBarCode("16010011");
		masterGoods.setGoodsSn(sku.substring(0, 6));
		masterGoods.setBvValue(50);
		masterGoods.setBaseBvValue(50);
//		masterGoods.setExpectedShipDate(expectedShipDate);
		masterGoods.setDepotCode(depotCode);
		masterGoods.setSupplierCode("ABC");
		masterGoods.setDisCount(goodsPrice - transactionPrice);
		return masterGoods;
	}

	private MasterPay createMasterPay(Integer payId, String payCode, Integer payStatus, Double payTotalFee, Integer paymentPeriod, Double paymentRate) {
		MasterPay masterPay = new MasterPay();
//		masterPay.setSurplus(surplus);
		masterPay.setPayId(payId);
		masterPay.setPayCode(payCode);
		masterPay.setPayStatus(payStatus);
		masterPay.setPayTotalFee(payTotalFee);
		masterPay.setPayNote("");
		masterPay.setPaymentPeriod(paymentPeriod); // 期数
		masterPay.setPaymentRate(paymentRate);
		return masterPay;
	}
}
