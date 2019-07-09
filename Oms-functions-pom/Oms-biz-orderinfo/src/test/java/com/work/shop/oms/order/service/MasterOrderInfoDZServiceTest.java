package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.GoodsCardInfo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrdersCreateReturnInfo;

import junit.framework.TestCase;

public class MasterOrderInfoDZServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private String orderFrom = "gz001";
	private String referer = "钱大妈店铺";
	private String consignee = "YF";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "quyachu8611";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double points = 0D;										// 点数
	private Double shippingTotalFee = 10D;							// 运费
	private Double moneyPaid = 100D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	private int payStatus = 2;
	private double totalPayable = 0D;
	private String expectedShipDate = "2017-06-23";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");
		reference = new ReferenceConfig<MasterOrderInfoService>();
		reference.setUrl("json://10.8.35.115:8010/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
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
	public void testOMSWeb() throws Exception{
		final MasterOrderInfoService masterOrderInfoService = reference.get();
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
		masterOrder.setInsteadUserId("test");
		masterOrder.setTransType(transType);
		masterOrder.setSource(3);
		masterOrder.setIsNow(0);
		masterOrder.setRegisterMobile("12345678901");
		masterOrder.setBonusId(null);
		masterOrder.setExpectedShipDate(expectedShipDate);
		masterOrder.setIsAdvance((short)0);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
		goodsList.add(createMasterGoods("80000110023", "T9玛丽皇后水果茶礼盒", "/sources/images/goods/TP/810743/810743_41_09.jpg", 189D, 100D, 0D, 1, "common","10911A01109B"));
//		goodsList.add(createMasterGoods("80000110023", "美唇笔", "/sources/images/goods/TP/810743/810743_41_09.jpg", 1D, 1D, 0D, 1, "common","10911A01109A"));

		masterOrder.setShipList(createMasterShip(goodsList, null));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
		masterPays.add(createMasterPay(1, "alipay", 2, 110D));
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
		masterOrder.setOrderSettlementPrice(moneyPaid + totalPayable + surplus + points);
		masterOrder.setPoints(points);
		masterOrder.setBvValue(10);
		masterOrder.setBaseBvValue(10);
		masterOrder.setIsCac(1);
		masterOrder.setShopCode("101002"); // 线下店铺编码
		return masterOrder;
	}
	
	private List<MasterShip> createMasterShip(List<MasterGoods> goodsList, List<GoodsCardInfo> cardInfos) {
		MasterShip masterShip = new MasterShip();
		masterShip.setShippingCode("cac");
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
		masterShip.setDeliveryTime("2018-06-06 00:00:00");
		masterShip.setLatitude(31.216253000000000);
		masterShip.setLongitude(121.540057000000000);
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
		masterGoods.setExpectedShipDate(expectedShipDate);
		masterGoods.setDepotCode(depotCode);
		return masterGoods;
	}

	private MasterPay createMasterPay(Integer payId, String payCode, Integer payStatus, Double payTotalFee) {
		MasterPay masterPay = new MasterPay();
//		masterPay.setSurplus(surplus);
		masterPay.setPayId(payId);
		masterPay.setPayCode(payCode);
		masterPay.setPayStatus(payStatus);
		masterPay.setPayTotalFee(payTotalFee);
		masterPay.setPayNote("");
		return masterPay;
	}
}
