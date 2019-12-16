package com.addressinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.DistributeShipBean;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.OrderToShippedProviderBeanParam;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.utils.TimeUtil;

import junit.framework.TestCase;

public class OrderCommonServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderCommonService> reference = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderCommonService>();
//		reference.setUrl("json://192.168.2.146:8089/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
		reference.setUrl("json://192.168.196.151:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderCommonService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderCommonService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testQuestion() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
//		ReturnInfo info = orderCommonService.questionOrderByOrderSn("1605301446460547s03", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		ReturnInfo info = orderCommonService.questionOrderByOrderSn("1605301446460547s03", new OrderStatus("", "问题单", "HQ01UC771", "19"));
		System.out.println(info.toString());
	}
	
	
	@Test
	public void testShip() throws Exception{
		String orderSn = "1809102120053931";
		String depotCode = "10911A01109B";
		final OrderCommonService orderCommonService = reference.get();
		List<OrderToShippedProviderBeanParam> providerBeanParams = new ArrayList<OrderToShippedProviderBeanParam>();
		OrderToShippedProviderBeanParam beanParam = new OrderToShippedProviderBeanParam();
		beanParam.setOrderSn(orderSn);
		List<DistributeShipBean> shipBeans = new ArrayList<DistributeShipBean>();
		DistributeShipBean shipBean = new DistributeShipBean();
		shipBean.setDistWarehCode(depotCode);
		shipBean.setMajorSendWarehCode(depotCode);
		shipBean.setTtlQty(1);
		shipBean.setShipCode("STO");
		shipBean.setCsbNum("1234321");
		shipBean.setShipDate(TimeUtil.formatDate(new Date()));
		shipBean.setShipType(0);
		shipBean.setCustomCode("10008000010");
		shipBean.setSynFlag(1);
		shipBeans.add(shipBean);
		shipBean.setOrderSn(orderSn);
		beanParam.setShipBeans(shipBeans);
		
		providerBeanParams.add(beanParam);
		ReturnInfo info = orderCommonService.acceptShip(providerBeanParams);
		System.out.println(info.toString());
	}
	
	@Test
	public void testSwdi() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01S771");
		orderStatus.setMessage("SWDI");
//		orderCommonService.s("1605021411510081", "", orderStatus);
//		distributeOpService.lockOrder("1605091055130102", orderStatus);
	}

	@Test
	public void testEditAddress() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		ConsigneeModifyInfo consignInfo = new ConsigneeModifyInfo();
		consignInfo.setAddress("康桥东路700号2");
		consignInfo.setConsignee("屈磊明");
		consignInfo.setActionUser("HQ01UC771");
		consignInfo.setOrderSn("1605162108080269s01");
		ReturnInfo info = orderCommonService.editConsigneeInfoByOrderSn(consignInfo);
		System.out.println(info.toString());
	}

	@Test
	public void testEditShippingType() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		ConsigneeModifyInfo consignInfo = new ConsigneeModifyInfo();
//		consignInfo.setAddress("康桥东路700号2");
//		consignInfo.setConsignee("屈磊明");
		consignInfo.setShippingId((byte) 25);
		consignInfo.setDepotCode("HQ01W520");
		consignInfo.setInvoiceNo("sssss");
		consignInfo.setOrderSn("1605162108080269s01");
		consignInfo.setActionUser("HQ01S771");
		ReturnInfo info = orderCommonService.editShippingType(consignInfo);
		System.out.println(info.toString());
	}
	
	@Test
	public void testEditShipFee() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		ReturnInfo info = orderCommonService.editShippingFee("1605181017270308", "HQ01UC771", 5D);
		System.out.println(JSON.toJSONString(info));
	}

	@Test
	public void testCancel() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("admin");
		orderStatus.setMessage("取消");
		orderStatus.setType("1");
		orderStatus.setCode("8011");
//		ReturnInfo info = orderCommonService.cancelOrderByOrderSn("1605311736500587s01", orderStatus);
		ReturnInfo info = orderCommonService.cancelOrderByMasterSn("1810161040354191", orderStatus);
		System.out.println(info);
	}
	
	@Test
	public void testPosConfirmOrder() throws Exception{
		// Impl.posConfirmOrder(OrderConfirmServiceImpl.java:281)] POS订单确认：modifyInfo=ConsigneeModifyInfo
		//[orderSn=1612071319420276S01, actionUser=A00021U8811, consignee=null, country=0, province=null, city=null, district=null, street=null, email=null, zipcode=null, address=null, tel=null, mobile=null, signBuilding=null, bestTime=null, oldMobile=null, oldTel=null, shippingId=null, shippingCode=null, depotCode=null, invoiceNo=null, postscript=null, InvPayee=null, invContent=null, paySn=null, orderOutSn=00310512D, shippingFee=0.0, payTotalFee=0.0, payId=21, source=OMS]

		final OrderCommonService orderCommonService = reference.get();
		ConsigneeModifyInfo modifyInfo = new ConsigneeModifyInfo();
		modifyInfo.setOrderOutSn("1111111D");
		modifyInfo.setPayId("1");
		modifyInfo.setPayTotalFee(60D);
		modifyInfo.setConsignee("YF2");
		modifyInfo.setProvince("310000");
		modifyInfo.setCity("310100");
		modifyInfo.setDistrict("310115");
		modifyInfo.setAddress("康桥东路700号-1");
		modifyInfo.setMobile("1111111111");
		modifyInfo.setOrderSn("1612071319420276S01");
		modifyInfo.setActionUser("HQ01S771");
		ReturnInfo info = orderCommonService.posConfirmOrder(modifyInfo);
		System.out.println(JSON.toJSONString(info));
	}
	
	
	@Test
	public void testQuestionOrder() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		OrderStatus orderStatus = new OrderStatus("", "问题单", "38");
		orderStatus.setAdminUser("HQ01S771");
		List<LackSkuParam> lackSkuParams = new ArrayList<LackSkuParam>();
		LackSkuParam lackSkuParam = new LackSkuParam();
		lackSkuParam.setCustomCode("21384090140");
		lackSkuParam.setDepotCode("V2015W001");
		lackSkuParam.setLackNum(1);
		lackSkuParam.setOrderSn("1605161555440212");
		lackSkuParams.add(lackSkuParam);
		ReturnInfo info = orderCommonService.addLackSkuQuestion("1605161555440212s02", lackSkuParams, orderStatus);
		System.out.println(JSON.toJSONString(info));
	}
	
	
	@Test
	public void testMoveOrderFromHistoryToRecent() throws Exception{
		final OrderCommonService orderCommonService = reference.get();
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UE609");
		orderStatus.setMessage("移入近期");
//		orderCommonService.shippedConfirm("1607131416528372S01", orderStatus);
	}
	
	@Test
	public void testDistOrderConfirm() throws Exception{
		String orderSn = "1809102120053931";
		String distSn = "1809102120053931S01";
		String depotCode = "10911A01109B";
		final OrderCommonService orderCommonService = reference.get();
		DistributeShippingBean distributeShipBean = new DistributeShippingBean();
		distributeShipBean.setDepotCode(depotCode);
		distributeShipBean.setInvoiceNo("1234321");
		distributeShipBean.setOrderSn(orderSn);
		distributeShipBean.setShipDate(new Date());
		distributeShipBean.setShipSn(distSn);
		distributeShipBean.setShippingCode("STO");
		distributeShipBean.setShippingName("申通");
		ReturnInfo info = orderCommonService.distOrderConfirm(distributeShipBean);
		System.out.println(info.toString());
	}
	
	
	@Test
	public void testCacWriteOff() throws Exception{
		String orderSn = "1809191725524058";
		String actionUser = "system";
		final OrderCommonService orderCommonService = reference.get();
		ReturnInfo info = orderCommonService.confirmReceipt(orderSn, actionUser);
		System.out.println(info.toString());
	}
}
