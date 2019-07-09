package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.Constant;

public class MasterOrderInfoC2BServiceTest extends TestCase {

	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private int index = 0;
	private String orderFrom = "HQ01S116";
	private String referer = "WAP";
	private String consignee = "屈磊明";
	private String mobile = "18917519266";
	private String emil = "abcd@qq.com";
	private String userId = "rachaeltan";
	private Integer transType = 1;									// 交易类型
	private Double surplus = 0D;									// 余额宝
	private Double shippingTotalFee = 0D;							// 运费
	private Double moneyPaid = 100D;									// 已付款金额
	private Double bonus = 0D;										// 红包金额

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");
		reference = new ReferenceConfig<MasterOrderInfoService>();
//		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
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
			System.out.println(" cost:isOk=" + info + ";message=" + info.getMessage());
			System.out.println(" cost:orderSn=" + info.getOrderSn());

		} else {
			System.out.println();
		}
		System.out.println(" cost:" + (end - begin));  
		System.out.println("end");  
	}
	
	private MasterOrder thirdOrder() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser(Constant.OS_STRING_SYSTEM);
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setTransType(transType);
		masterOrder.setSource(5);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
//		goodsList.add(createMasterGoods("10102599030", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 40D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("10985598044", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 1, "common"));
//		goodsList.add(createMasterGoods("21384090140", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 20D, 0D, 2, "common"));
		
		goodsList.add(createMasterGoods("21384090140", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 329D, 100D, 0D, 1, "common"));

		
		masterOrder.setShipList(createMasterShip(goodsList));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
		masterPays.add(createMasterPay(1, "alipay", 2, 100D));
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
		masterOrder.setPayStatus((short)2);
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
		String c2mItems = "{\"num\":\"1\",\"buyer\":\"22\",\"diyId\":\"1\",\"cost\":\"0\",\"picPath1\":\"http:\\/\\/img.mixme.cn\\/sources\\/SupplyChain\\/C2M\\/ProductStyle\\/248425\\/248425_x--90x90.JPG\",\"sn\":\"700001\",\"name\":\"\\u7537\\u57fa\\u672c\\u56db\\u888b\\u6c34\\u6d17\\u88e4\",\"measure\":[{\"value\":\"50\",\"id\":\"62\",\"name\":\"\\u5408\\u4f53\\u76f4\\u7b52\"},{\"value\":\"29\",\"id\":\"63\",\"name\":\"\\u8170\\u5e26\\u7cfb\\u5728\\u809a\\u8110\\u4ee5\\u4e0b4\\u6307\\u5904\"},{\"value\":\"\",\"id\":\"26\",\"name\":\"\\u88e4\\u957f\"},{\"value\":\"\",\"id\":\"27\",\"name\":\"\\u76f4\\u6863\"},{\"value\":\"\",\"id\":\"28\",\"name\":\"\\u8170\\u56f4\"},{\"value\":\"\",\"id\":\"29\",\"name\":\"\\u81c0\\u56f4\"},{\"value\":\"\",\"id\":\"30\",\"name\":\"\\u5927\\u817f\\u6839\\u56f4\"},{\"value\":\"\",\"id\":\"31\",\"name\":\"\\u819d\\u56f4\"},{\"id\":\"32\",\"name\":\"\\u5c0f\\u817f\\u56f4\"},{\"id\":\"33\",\"name\":\"\\u811a\\u8e1d\\u56f4\"}],\"size\":\"4\",\"color\":\"268\",\"clothesID\":\"21166103146\",\"sellerPrice\":\"199.00\"}";
		masterGoods.setC2mItems(c2mItems);
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
