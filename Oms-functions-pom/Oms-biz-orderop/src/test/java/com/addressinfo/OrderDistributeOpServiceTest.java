package com.addressinfo;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

public class OrderDistributeOpServiceTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<OrderDistributeOpService> reference = null;
	
	private OrderDistributeOpService distributeOpService = null;
	
	private String masterOrderSn = "140429783791";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();  
		application.setName("OMS");  
		reference = new ReferenceConfig<OrderDistributeOpService>();
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeOpService");
//		reference.setUrl("json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeOpService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(OrderDistributeOpService.class);
		reference.setVersion("1.0.0");
		distributeOpService = reference.get();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testMoveOrderFromHistoryToRecent() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UE609");
		orderStatus.setMessage("移入近期");
		distributeOpService.moveOrderFromHistoryToRecent(masterOrderSn, orderStatus);
	}
	
	@Test
	public void testMoveOrderFromRecentToHistory() throws Exception{
		distributeOpService.moveOrderFromRecentToHistory(masterOrderSn);
	}
	
	@Test
	public void testEditGoods() throws Exception{
		distributeOpService = reference.get();
		distributeOpService.copyOrder("", new OrderStatus());
	}
	@Test
	public void testLockOrder() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setUserId(110);
		distributeOpService.lockOrder(masterOrderSn, orderStatus);
	}
	
	@Test
	public void testLock() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("锁定：");
		orderStatus.setUserId(110);
		ReturnInfo info = distributeOpService.lockOrder(masterOrderSn, orderStatus);
		System.out.println(info.toString());
	}
	
	@Test
	public void testUnLnck() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setUserId(110);
		ReturnInfo info = distributeOpService.unLockOrder(masterOrderSn, orderStatus);
		System.out.println(info.toString());
	}
	
	@Test
	public void testNoticeReceivable() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setUserId(110);
		ReturnInfo info = distributeOpService.noticeReceivables(masterOrderSn, orderStatus);
		System.out.println(info.toString());
	}
	
	@Test
	public void testReviveOrder() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setUserId(110);
		ReturnInfo info = distributeOpService.reviveOrder(masterOrderSn, orderStatus);
		System.out.println(info.toString());
	}
	
	@Test
	public void testSWDI() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setUserId(110);
		ReturnInfo info = distributeOpService.sWDI(masterOrderSn, "", orderStatus);
		System.out.println(info.toString());
	}
	
	@Test
	public void testMoveOrderAction() throws Exception{
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setAdminUser("HQ01UC771");
		orderStatus.setMessage("确认：");
		orderStatus.setIsHistory(1);
		ReturnInfo info = distributeOpService.moveOrderAction("140507019975", orderStatus);
		System.out.println(info.toString());
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
}
