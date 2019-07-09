package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.work.shop.oms.utils.TimeUtil;

public class MasterOrderInfoServiceTest extends TestCase {
	private ApplicationConfig application = null;
	private ReferenceConfig<MasterOrderInfoService> reference;
	private int index = 0;
	private String orderFrom = "HQ01S116";
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
		reference = new ReferenceConfig<MasterOrderInfoService>();
//		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
		reference.setTimeout(500);
		reference.setConnections(10);
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
		OrderCreateReturnInfo info = masterOrderInfoService.createOrder(masterOrder);
		long end = System.currentTimeMillis();
		System.out.println(" cost:isOk=" + info + ";message=" + info.getMessage());  
		System.out.println(" cost:" + (end - begin));  
		System.out.println("end");  
	}
	
	private MasterOrder mbOrder() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser(Constant.OS_STRING_SYSTEM);
		masterOrder.setAddTime(new Date());
//		masterOrder.setBonus(bonus);
		return masterOrder;
	}
	
	private MasterOrder thirdOrder() {
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser(Constant.OS_STRING_SYSTEM);
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(orderFrom);
		masterOrder.setReferer(referer);
		masterOrder.setUserId(userId);
		masterOrder.setTransType(transType);
		List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
		goodsList.add(createMasterGoods("54302740142", "男长袖衬衫", "/sources/images/goods/TP/810743/810743_41_09.jpg", 1D, 1D, 0D, 2, "prize"));
		goodsList.add(createMasterGoods("21384090140", "中性基本净色中帮硫化鞋", "/sources/images/goods/TP/881535/881535_70_09.jpg", 1D, 1D, 0D, 4, "prize"));
		masterOrder.setShipList(createMasterShip(goodsList));
		List<MasterPay> masterPays = new ArrayList<MasterPay>();
		masterPays.add(createMasterPay(1, "alipay", 2, 8D));
		masterPays.add(createMasterPay(16, "jingdong", 2, 6D));
//		masterPays.add(createMasterPay(3, "balance", 2, 8D));

		masterOrder.setPayList(masterPays);
		double goodsAmount = 0;
		int goodsCount = 0;
		double discount = 0D;
		for (MasterGoods masterGoods :goodsList) {
			goodsAmount += masterGoods.getGoodsNumber();
			goodsCount += masterGoods.getGoodsNumber();
			discount += (masterGoods.getGoodsPrice() - masterGoods.getTransactionPrice());
		}
		masterOrder.setShippingTotalFee(shippingTotalFee);
		Double totalFee = goodsAmount - discount + shippingTotalFee;
		masterOrder.setTotalFee(totalFee);
		Double totalPayable = totalFee - moneyPaid - surplus - bonus;
		masterOrder.setTotalPayable(totalPayable);
		masterOrder.setMoneyPaid(8D);
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
	
	@Test
	public void testDate(){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		endTime = calendar.getTime();
		Date startTime = new Date();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(startTime);
		calendar2.set(Calendar.DATE, calendar2.get(Calendar.DATE) - 30);
		startTime = calendar2.getTime();
		System.out.println(TimeUtil.formatDate(endTime));
		System.out.println(TimeUtil.formatDate(startTime));
	}
	
}
