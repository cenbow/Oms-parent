package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

public class MasterOrderInfoBgServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private int index = 0;
	private String orderFrom = "HQ01S116";
	private String referer = "wap";
	private String consignee = "zhouyelin";
	private String mobile = "13001234123";
	private String emil = "abcd@qq.com";
	private String userId = "zhouyelin";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 61D;									// 余额宝
	private Double shippingTotalFee = 1D;							// 运费
	private Double moneyPaid = 0D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额
	private int payStatus = 2;
	private double totalPayable = 0D;								
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");  
		reference = new ReferenceConfig<MasterOrderInfoService>();
//		reference.setUrl("json://10.80.16.2:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setUrl("json://172.19.0.8:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://10.8.39.91:8088/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
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
	public void testOMSWeb() throws Exception{
		final MasterOrderInfoService masterOrderInfoService = reference.get();
		long begin = System.currentTimeMillis();
		// 服务调用
		MasterOrder masterOrder = thirdOrder();
		System.out.println(JSON.toJSONString(masterOrder));
		
		System.out.println("orderSettlementPrice:" + masterOrder.getOrderSettlementPrice());
		System.out.println("paySettlementPrice:" + masterOrder.getPaySettlementPrice());
		System.out.println("goodsSettlementPrice:" + masterOrder.getGoodsSettlementPrice());
		System.out.println("totalPayable:" + masterOrder.getTotalPayable());

		long end = System.currentTimeMillis();
		if (masterOrder.getOrderSettlementPrice().equals(masterOrder.getPaySettlementPrice())
				&& masterOrder.getOrderSettlementPrice().equals(masterOrder.getGoodsSettlementPrice())
				&& masterOrder.getTotalPayable() >= 0) {
			OrderCreateReturnInfo info = masterOrderInfoService.createOrder(masterOrder);
			System.out.println(" cost:isOk=" + JSON.toJSONString(info));
			System.out.println(" cost:orderSn=" + info.getOrderSn());

		} else {
			System.out.println();
		}
		System.out.println(" cost:" + (end - begin));  
		System.out.println("end");  
	}
	
	private MasterOrder thirdOrder() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser("A00030U0002");
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setTransType(transType);
		masterOrder.setSource(3);
		masterOrder.setIsNow(1);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
//		goodsList.add(createMasterGoods("52277700140", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 659D, 40D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("52277700142", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("20209599141", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 659D, 60D, 0D, 1, "common"));
		goodsList.add(createMasterGoods("22233330152", "", "", 199D, 10D, 0D, 6, "common"));
//		goodsList.add(createMasterGoods("10985598044", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 1, "common"));

//		goodsList.add(createMasterGoods("11019901047", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 159D, 40D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("11020375045", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 39D, 20D, 0D, 1, "common"));
		
		masterOrder.setShipList(createMasterShip(goodsList));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
//		masterPays.add(createMasterPay(1, "alipay", payStatus, 61D));
//		masterPays.add(createMasterPay(16, "jingdong", 2, 800D));
		masterPays.add(createMasterPay(3, "balance", 2, surplus));

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
			moneyPaid = goodsSettlementPrice + shippingTotalFee - surplus;
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
		Double totalPayable = 0D;
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
		masterOrder.setOrderSettlementPrice(moneyPaid + surplus);
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
