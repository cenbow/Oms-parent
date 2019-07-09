package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.ship.service.WkUdDistributeService;

public class MasterOrderInfoQLTServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<WkUdDistributeService> reference;
	private int index = 0;
	private String orderFrom = "A00030S001";
	private String referer = "POS";
	private String consignee = "YF";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "A00030U0002";
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
		reference = new ReferenceConfig<WkUdDistributeService>();
		reference.setUrl("json://192.168.1.3:8080/Oms/dubbo/com.work.shop.oms.ship.service.WkUdDistributeService");
		reference.setTimeout(50000);
		reference.setConnections(10000);
		reference.setApplication(application);
		reference.setInterface(WkUdDistributeService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOMSWeb() throws Exception{
		final WkUdDistributeService wkUdDistributeService = reference.get();

		List<WkUdDistribute> wkUdDistributes = new ArrayList<WkUdDistribute>();
		WkUdDistribute wkUdDistribute = new WkUdDistribute();
		wkUdDistribute.setCreateDate(new Date());
//		wkUdDistribute.setDepotName("上海B2C订单MBMC统一发货仓(测试)");
//		wkUdDistribute.setDistId(13610856);
		wkUdDistribute.setDistWarehCode("A00001");
		wkUdDistribute.setRcvWarehcode("A00001");
		wkUdDistribute.setGoodsNumber("1");
		wkUdDistribute.setOuterCode("1712181546437686");
		wkUdDistribute.setProdId("110000001");
//		wkUdDistribute.setDistqty0(1);
//		wkUdDistribute.setDistrate((float)1);
//		wkUdDistribute.setOverTransCycle("2016/08/11");
//		wkUdDistribute.setPdwarhCode("HQ01");
//		wkUdDistribute.setPdwarhName("美特斯邦威上海(测试)");
//		wkUdDistribute.setProvincecity("上海上海市");
//		wkUdDistribute.setShipCode("yunda");
//		wkUdDistribute.setShipType(0);
//		wkUdDistribute.setToUser("gYj");
//		wkUdDistribute.setToUserPhone("18500174532");
//		wkUdDistribute.setVendeeAgentName("加盟");
		wkUdDistributes.add(wkUdDistribute);
		System.out.println(JSON.toJSONString(wkUdDistributes));
		boolean info = wkUdDistributeService.depot(wkUdDistributes);
		System.out.println(info);
	}
	
	private MasterOrder thirdOrder() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser("A00030U0002");
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setTransType(transType);
		masterOrder.setSource(1);
		masterOrder.setIsGroup((short)0);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
		goodsList.add(createMasterGoods("60124730152", "", "", 199D, 30D, 0D, 2, "common"));
		
		masterOrder.setShipList(createMasterShip(goodsList));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
		masterPays.add(createMasterPay(21, "shop_pay", payStatus, 60D));

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
			moneyPaid = goodsSettlementPrice + shippingTotalFee;
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
		Double totalPayable = totalFee - moneyPaid - surplus - bonus;
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
		masterOrder.setOrderSettlementPrice(moneyPaid + totalPayable + surplus);
		return masterOrder;
	}
	
	private List<MasterShip> createMasterShip(List<MasterGoods> goodsList) {
		MasterShip masterShip = new MasterShip();
		masterShip.setShippingCode("99");
		masterShip.setCountry("1");
		masterShip.setProvince("310000");
		masterShip.setCity("310100");
		masterShip.setDistrict("310115");
		masterShip.setAddress("康桥东路700号");
		masterShip.setMobile("18917519266");
		masterShip.setTel("8362");
		masterShip.setConsignee(consignee);
		masterShip.setEmail(emil);
		masterShip.setBestTime(null);
		masterShip.setSignBuilding("1");
		masterShip.setZipcode("20000");
		masterShip.setChargeType((byte)0);
		masterShip.setGoodsList(goodsList);
		List<MasterShip> masterShips = new ArrayList<MasterShip>();
		masterShips.add(masterShip);
		return masterShips;
	}
	
	private MasterGoods createMasterGoods(String sku, String goodsName, String goodsThumb, Double goodsPrice,
			double transactionPrice, double shareBonus, int goodsNumber, String extensionCode) {
		MasterGoods masterGoods = new MasterGoods();
		masterGoods.setItemId(index + "");
		masterGoods.setCustomCode(sku);
		masterGoods.setGoodsName(goodsName);
		masterGoods.setGoodsThumb(goodsThumb);
		masterGoods.setGoodsPrice(goodsPrice);
		masterGoods.setTransactionPrice(transactionPrice);
		masterGoods.setShareBonus(shareBonus);
		masterGoods.setGoodsNumber(goodsNumber);
		masterGoods.setUseCard("");
		masterGoods.setExtensionCode(extensionCode);
		masterGoods.setPromotionDesc("");
		masterGoods.setIntegralMoney(0D);
		masterGoods.setSellerUser("A00030U0006");
		index ++;
		return masterGoods;
	}
	
	private MasterPay createMasterPay(Integer payId, String payCode, Integer payStatus, Double payTotalFee) {
		MasterPay masterPay = new MasterPay();
		masterPay.setSurplus(surplus);
		masterPay.setPayId(payId);
		masterPay.setPayCode(payCode);
		masterPay.setPayStatus(payStatus);
		masterPay.setPayTotalFee(payTotalFee);
		return masterPay;
	}
}
