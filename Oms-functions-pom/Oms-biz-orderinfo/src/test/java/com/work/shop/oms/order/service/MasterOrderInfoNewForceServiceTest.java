package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderGoods;
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
import com.work.shop.oms.utils.StringUtil;

public class MasterOrderInfoNewForceServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private String orderFrom = "A0000001";
	private String referer = "新美力WAP商城";
	private String consignee = "YF";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "ceshi";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double points = 0D;										// 点数
	private Double shippingTotalFee = 0D;							// 运费
	private Double moneyPaid = 0D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	private int payStatus = 2;
	private double totalPayable = 0D;
	private String expectedShipDate = "2017-05-24";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<MasterOrderInfoService>();
//		reference.setUrl("json://192.168.2.183:8089/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setUrl("json://172.31.249.149:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://10.8.39.91:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://192.168.2.208:8089/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://172.19.0.8:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
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
	public void testCreateOrder() throws Exception{
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		String msg = "[{\"actionUser\":\"A00030U0002\",\"addTime\":1480415565244,\"beneficiaryId\":0,\"bonus\":0,\"bvValue\":10,\"cardTotalFee\":0,\"consignee\":\"\",\"discount\":20,\"email\":\"\",\"freePostFee\":0,\"freePostType\":0,\"fromAd\":0,\"goodsAmount\":100,\"goodsCount\":1,\"goodsSettlementPrice\":80,\"howOss\":\"\",\"insureTotalFee\":0,\"integral\":0,\"integralMoney\":0,\"invContent\":\"\",\"invPayee\":\"个人\",\"invType\":\"普通发票\",\"invoicesOrganization\":\"\",\"isAdvance\":0,\"isGroup\":0,\"isNow\":0,\"isOrderPrint\":0,\"isRestricted\":0,\"mergeFrom\":\"\",\"mobile\":\"13001234123\",\"moneyPaid\":79,\"orderCategory\":1,\"orderFrom\":\"C0000001\",\"orderOutSn\":\"\",\"orderSettlementPrice\":80,\"orderStatus\":0,\"orderType\":0,\"outletType\":0,\"packTotalFee\":0,\"parentId\":0,\"payList\":[{\"payCode\":\"alipay\",\"payId\":1,\"payNote\":\"3333333333333333333333333\",\"payStatus\":2,\"payTotalFee\":79,\"surplus\":0},{\"payCode\":\"points\",\"payId\":27,\"payNote\":\"3333333333333333333333333\",\"payStatus\":2,\"payTotalFee\":1,\"surplus\":0}],\"paySettlementPrice\":80,\"payStatus\":2,\"payTotalFee\":0,\"points\":1,\"posGroup\":0,\"postscript\":\"\",\"prName\":\"\",\"promotionSummry\":[],\"referer\":\"克丽缇娜WAP商城\",\"registerMobile\":\"12345678901\",\"shipList\":[{\"address\":\"东方路700号\",\"bestTime\":\"\",\"cacCode\":\"\",\"cardMessage\":\"\",\"chargeType\":0,\"city\":\"310100\",\"consignee\":\"YF\",\"country\":\"1\",\"deliveryType\":0,\"directRange\":0,\"district\":\"310115\",\"email\":\"abcd@qq.com\",\"goodsList\":[{\"barCode\":\"16010013\",\"bvValue\":10,\"colorName\":\"罂粟红\",\"customCode\":\"8300210000\",\"extensionCode\":\"common\",\"extensionId\":\"\",\"goodsName\":\"美唇笔\",\"goodsNumber\":1,\"goodsPrice\":100,\"goodsSn\":\"830021\",\"goodsThumb\":\"/sources/images/goods/TP/810743/810743_41_09.jpg\",\"integral\":0,\"integralMoney\":0,\"marketPrice\":100,\"promotionDesc\":\"\",\"salesMode\":1,\"sellerUser\":\"A00030U0006\",\"sendNumber\":0,\"shareBonus\":0,\"sizeName\":\"标准\",\"skuSn\":\"\",\"supplierCode\":\"MB\",\"transactionPrice\":80,\"useCard\":\"\"}],\"mobile\":\"18917519266\",\"province\":\"310000\",\"shippingCode\":\"99\",\"shippingDays\":0,\"shippingRequire\":\"\",\"signBuilding\":\"1\",\"tel\":\"021-83628324\",\"wayPaymentFreight\":\"02\",\"zipcode\":\"20000\"}],\"shippingTotalFee\":0,\"smsCode\":\"\",\"smsFlag\":\"\",\"source\":3,\"sourceCode\":\"\",\"splitFrom\":\"\",\"surplus\":0,\"tax\":0,\"toBuyer\":\"\",\"totalFee\":80,\"totalPayable\":0,\"transType\":1,\"userId\":\"ceshi\"}]";
		valuePairs.add(new BasicNameValuePair("orderInfos", msg));
		String url = "http://localhost:8089/Oms/api/createOrders";
		System.out.println("请求快递100接口 queryUrl：" + JSON.toJSONString(valuePairs));
		String response=com.work.shop.oms.utils.HttpClientUtil.post(url, valuePairs);
		System.out.println(response);
	}
	
	@Test
	public void testOMSWeb() throws Exception{
		final MasterOrderInfoService masterOrderInfoService = reference.get();
		long begin = System.currentTimeMillis();
		// 服务调用
		List<MasterOrder> masterOrders = new ArrayList<MasterOrder>();
		masterOrders.add(HQ01S116Order());
		System.out.println(JSON.toJSONString(masterOrders));
		OrdersCreateReturnInfo info = masterOrderInfoService.createOrders(masterOrders);
		System.out.println(" cost:isOk=" + JSON.toJSONString(info));
		System.out.println("end");
	}

	private MasterOrder HQ01S116Order() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser("A00030U0002");
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setTransType(transType);
		masterOrder.setSource(1);
		masterOrder.setIsNow(0);
		masterOrder.setRegisterMobile("12345678901");
		masterOrder.setBonusId(null);
		masterOrder.setExpectedShipDate(expectedShipDate);
		masterOrder.setIsAdvance((short)1);
		masterOrder.setTax(0D);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
//		goodsList.add(createMasterGoods("52277700140", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 659D, 40D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("52277700142", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("20209599141", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 659D, 60D, 0D, 1, "common"));
		goodsList.add(createMasterGoods("8300210000", "美唇笔", "/sources/images/goods/TP/810743/810743_41_09.jpg", 100D, 100D, 0D, 100, "common"));
//		goodsList.add(createMasterGoods("22233330152", "", "/sources/images/goods/TP/810743/810743_41_09.jpg", 100D, 0D, 0D, 1, "gift"));
		
		//		goodsList.add(createMasterGoods("10985598044", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 1, "common"));

//		goodsList.add(createMasterGoods("11019901047", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 159D, 40D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("11020375045", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 39D, 20D, 0D, 1, "common"));
		
//		List<GoodsCardInfo> cardInfos = new ArrayList<GoodsCardInfo>();
//		cardInfos.add(createGoodsCardInfo("22233330152", 10000D, 6000D, "30900046742491"));
//		cardInfos.add(createGoodsCardInfo("22233330152", 100D, 80D, null));
		
		masterOrder.setShipList(createMasterShip(goodsList, null));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
		masterPays.add(createMasterPay(1, "alipay", payStatus, 10000D));
//		masterPays.add(createMasterPay(27, "points", payStatus, 1D));

		
//		masterPays.add(createMasterPay(16, "jingdong", 2, 800D));
//		masterPays.add(createMasterPay(3, "balance", 2, surplus));

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
			goodsSettlementPrice += (masterGoods.getTransactionPrice()
					- masterGoods.getShareBonus() - masterGoods.getIntegralMoney()) * goodsNumber;
		}
		if (payStatus == 0) {
			moneyPaid = 0D;
			totalPayable = goodsSettlementPrice + shippingTotalFee;
		} else {
			moneyPaid = goodsSettlementPrice + shippingTotalFee - points;
			totalPayable = 0D;
		}
		double paySettlementPrice = 0D;
		for (MasterPay masterPay : masterPays) {
			paySettlementPrice += masterPay.getPayTotalFee();
		}
		masterOrder.setGoodsSettlementPrice(goodsSettlementPrice + shippingTotalFee);
		masterOrder.setPaySettlementPrice(paySettlementPrice);
		masterOrder.setShippingTotalFee(shippingTotalFee);
		Double totalFee = goodsAmount - discount + shippingTotalFee;
		masterOrder.setTotalFee(totalFee);
//		Double totalPayable = totalFee - moneyPaid - surplus - bonus;
		masterOrder.setTotalPayable(totalPayable);
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
		masterOrder.setSurplus(surplus);
		masterOrder.setOrderSettlementPrice(moneyPaid + totalPayable + surplus + points);
		masterOrder.setPoints(points);
		masterOrder.setBvValue(10);
		return masterOrder;
	}
	
	private List<MasterShip> createMasterShip(List<MasterGoods> goodsList, List<GoodsCardInfo> cardInfos) {
		MasterShip masterShip = new MasterShip();
		masterShip.setShippingCode("99");
		masterShip.setCountry("1");
		masterShip.setProvince("310000");
		masterShip.setCity("310100");
		masterShip.setDistrict("310115");
		masterShip.setAddress("东方路700号");
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
		masterShip.setUserCardName("张三桑");
		masterShip.setUserCardNo("333333123405374628");
//		masterShip.setDepotCode("HQ01W870");
		List<MasterShip> masterShips = new ArrayList<MasterShip>();
		masterShips.add(masterShip);
		return masterShips;
	}
	
	private MasterGoods createMasterGoods(String sku, String goodsName, String goodsThumb, Double goodsPrice,
			double transactionPrice, double shareBonus, int goodsNumber, String extensionCode) {
		MasterGoods masterGoods = new MasterGoods();
//		masterGoods.setItemId(index + "");
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
		masterGoods.setSizeName("标准");
		masterGoods.setColorName("罂粟红");
		masterGoods.setBarCode("16010013");
		masterGoods.setGoodsSn(sku.substring(0, 6));
		masterGoods.setBvValue(10);
		masterGoods.setExpectedShipDate(expectedShipDate);
		masterGoods.setTax(0D);
		masterGoods.setTaxRate(0D);
		return masterGoods;
	}
	
	private GoodsCardInfo createGoodsCardInfo(String customCode, Double goodsPrice,
			double transactionPrice, String cardCouponNo) {
		GoodsCardInfo cardInfo = new GoodsCardInfo();
		cardInfo.setCardCouponNo(cardCouponNo);
		cardInfo.setMarketPrice(goodsPrice);
		cardInfo.setGoodsPrice(goodsPrice);
		cardInfo.setTransactionPrice(transactionPrice);
		cardInfo.setCustomCode(customCode);
		return cardInfo;
	}
	private MasterPay createMasterPay(Integer payId, String payCode, Integer payStatus, Double payTotalFee) {
		MasterPay masterPay = new MasterPay();
		masterPay.setSurplus(surplus);
		masterPay.setPayId(payId);
		masterPay.setPayCode(payCode);
		masterPay.setPayStatus(payStatus);
		masterPay.setPayTotalFee(payTotalFee);
		masterPay.setPayNote("3333333333333333333333333");
		return masterPay;
	}
}
