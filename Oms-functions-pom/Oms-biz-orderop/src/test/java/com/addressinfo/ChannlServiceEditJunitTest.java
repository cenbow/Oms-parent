package com.addressinfo;
import java.net.InetAddress;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.channel.service.ChannelService;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * dubbo功能junit测试 （编辑收货人信息）
 * @author lemon
 *
 */
public class ChannlServiceEditJunitTest extends TestCase {
	
	private ApplicationConfig application = null;
	
	private ReferenceConfig<ChannelService> reference = null;
	private int index = 0;
	private String channelCode = "HQ01S116";
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
		reference = new ReferenceConfig<ChannelService>();
		reference.setUrl("json://10.100.200.64:80/ChannelService/dubbo/com.work.shop.oms.channel.service.ChannelService");
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(ChannelService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testEditGoods() throws Exception{
//		final ChannelService channelService = reference.get();
//		System.out.println(JSON.toJSONString(channelService.getAllSynStockChannelShop()));
		
		// TODO Auto-generated method stub
				InetAddress addr = InetAddress.getLocalHost();
				try {
					System.out.println(addr.getHostAddress());
					System.out.println(addr.getHostName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
