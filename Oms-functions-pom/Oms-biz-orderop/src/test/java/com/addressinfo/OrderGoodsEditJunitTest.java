package com.addressinfo;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderDistributeEditService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class OrderGoodsEditJunitTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderDistributeEditService> reference = null;
	private String channelCode = "HQ01S116";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderDistributeEditService>();
//		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeEditService");
		reference.setUrl("json://10.100.22.201:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeEditService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderDistributeEditService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditShipFee() throws Exception{
		final OrderDistributeEditService distributeEditService = reference.get();
		ReturnInfo info = distributeEditService.editShippingFee("1605181017270308", "HQ01UC771", 5D);
		System.out.println(JSON.toJSONString(info));
	}
	
	@Test
	public void testEditGoods() throws Exception{
		final OrderDistributeEditService distributeEditService = reference.get();
		long begin = System.currentTimeMillis();
		// 服务调用
		OrderInfoUpdateInfo infoUpdateInfo = new OrderInfoUpdateInfo();
		infoUpdateInfo.setChannelCode(channelCode);
		
		List<OrderGoodsUpdateBean> orderGoodsUpdateInfos = new ArrayList<OrderGoodsUpdateBean>();
		orderGoodsUpdateInfos.add(buildUpdateGoods("1605122024210138s01", 745l, "54302740144", 32.5D, 32.5D, 0D, "V02015", (short)1, "V02015W100"));
		orderGoodsUpdateInfos.add(buildUpdateGoods("1605122024210138s01", 746l, "88158240140", 32.5D, 32.5D, 0D, "V02015", (short)1, "V02015W100"));
		orderGoodsUpdateInfos.add(buildUpdateGoods("1605122024210138s02", 747l, "20150592137", 35.1D, 35.1D, 0D, "MB", (short)1, "HQ01W700"));
//		orderGoodsUpdateInfos.add(buildUpdateGoods("1605122024210138s02", 748l, "60124730152", 35.1D, 35.1D, 0D, "MB", (short)1, "HQ01W850"));
//		orderGoodsUpdateInfos.add(buildUpdateGoods("", 748l, "60124730152", null, null, null, null, (short)-1, null)); // delete
		orderGoodsUpdateInfos.add(buildUpdateGoods("", 887l, "", null, null, null, null, (short)-1, null)); // delete
//		orderGoodsUpdateInfos.add(buildAddGoods("", null, "60124730152", 35.1D, 35.1D, 0D, "MB", (short)1, "")); // add

//		orderGoodsUpdateInfos.add(buildUpdateGoods("1605131130430147s01", 808l, "54302740142", 69D, 69D, 0D, "V02015", (short)1, "V02015W100"));

//		orderGoodsUpdateInfos.add(buildUpdateGoods("1605061944480091s02", 396l, "88158240140", null, null, null,"V02015", (short)-1,"V02015W100"));
//		orderGoodsUpdateInfos.add(buildUpdateGoods("", null, "54302740142",  2D, 2D, 0D, "V020151", (short)1));
//		orderGoodsUpdateInfos.add(buildUpdateGoods("1605061944480091s01", 394l, "21384090140", 6D, 6D, 0D, "V2015",(short)1,"V2015W001"));
		// 删除 373
		infoUpdateInfo.setOrderGoodsUpdateInfos(orderGoodsUpdateInfos);
		System.out.println(infoUpdateInfo.toString());
//		ReturnInfo info = distributeEditService.editGoodsByOrderSn("1605041535190087s02", infoUpdateInfo, "HQ01UC771");
		ReturnInfo info = distributeEditService.editGoodsByMasterSn("1605122024210138", infoUpdateInfo, "HQ01UC771");

		long end = System.currentTimeMillis();
		System.out.println(" cost:isOk=" + info.getIsOk() + ";message=" + info.getMessage());
		System.out.println(" cost:" + (end - begin));  
		System.out.println("end");  
	}
	
	private OrderGoodsUpdateBean buildUpdateGoods(String orderSn, Long id, String sku, Double TransactionPrice,
			Double settlementPrice, Double shareBonus, String supplierCode, int number, String depot) {
		if (StringUtil.isTrimEmpty(depot)) {
			depot = Constant.DETAILS_DEPOT_CODE;
		}
		OrderGoodsUpdateBean updateBean = new OrderGoodsUpdateBean();
		updateBean.setOrderSn(orderSn);
		updateBean.setCustomCode(sku);
		updateBean.setDoubleGoodsPrice(1D);
		updateBean.setDoubleTransactionPrice(TransactionPrice);
		updateBean.setDoubleSettlementPrice(settlementPrice);
		updateBean.setDoubleShareBonus(shareBonus);
		updateBean.setIntegralMoney(0D);
		updateBean.setId(id);
		updateBean.setSupplierCode(supplierCode);
		updateBean.setDiscount(0F);
		updateBean.setGoodsNumber(number);
		updateBean.setDepotCode(depot);
		return updateBean;
	}
	
	private OrderGoodsUpdateBean buildAddGoods(String orderSn, Long id, String sku, Double TransactionPrice,
			Double settlementPrice, Double shareBonus, String supplierCode, int number, String depot) {
		if (StringUtil.isTrimEmpty(depot)) {
			depot = Constant.DETAILS_DEPOT_CODE;
		}
		OrderGoodsUpdateBean updateBean = new OrderGoodsUpdateBean();
		updateBean.setOrderSn(orderSn);
		updateBean.setCustomCode(sku);
		updateBean.setDoubleGoodsPrice(1D);
		updateBean.setDoubleTransactionPrice(TransactionPrice);
		updateBean.setDoubleSettlementPrice(settlementPrice);
		updateBean.setDoubleShareBonus(shareBonus);
		updateBean.setIntegralMoney(0D);
		updateBean.setId(id);
		updateBean.setSupplierCode(supplierCode);
		updateBean.setDiscount(0F);
		updateBean.setGoodsNumber(number);
		updateBean.setDepotCode(depot);
		updateBean.setGoodsName("xxxxxx");
		updateBean.setExtensionCode("common");
		updateBean.setExtensionId("5");
		updateBean.setDepotCode(Constant.DETAILS_DEPOT_CODE);
		updateBean.setCurrSizeName("aaa");
		updateBean.setCurrColorName("aaa");
		return updateBean;
	}
}
