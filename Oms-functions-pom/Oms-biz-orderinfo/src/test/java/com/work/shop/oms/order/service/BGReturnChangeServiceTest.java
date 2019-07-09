package com.work.shop.oms.order.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.CreateGoodsReturnChange;
import com.work.shop.oms.api.param.bean.GoodsReturnChangeDetailInfo;
import com.work.shop.oms.api.param.bean.ReturnChangeGoodsBean;
import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.bean.PageListParam;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.api.orderinfo.service.BGReturnChangeService;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;

import javax.annotation.Resource;

public class BGReturnChangeServiceTest extends TestCase {

	String userId = "M12345678901";
	String siteCode = "NEWFORCE";
	String orderSn = "1610111529126611";
	String isHistory = "0";
	String paySn = "FK1609281423392701";
	private ApplicationConfig application = null;
	private ReferenceConfig<BGReturnChangeService> reference;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		application = new ApplicationConfig();
		application.setName("OMS");
		reference = new ReferenceConfig<BGReturnChangeService>();
//		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService");
//		reference.setUrl("json://172.19.0.40:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGReturnChangeService");
//		reference.setUrl("json://172.19.0.15:8080/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGOrderInfoService");
//		reference.setUrl("json://192.168.155.201:8089/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGReturnChangeService");
        reference.setUrl("json://192.168.196.138:8580/Oms/dubbo/com.work.shop.oms.api.orderInfo.service.BGReturnChangeService");
		reference.setTimeout(50000);
		reference.setConnections(1000);
		reference.setApplication(application);
		reference.setInterface(BGReturnChangeService.class);
		reference.setVersion("1.0.0");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testOrderPageList() throws Exception{
		final BGReturnChangeService bgReturnChangeService = reference.get();
		String actionUser = "M150611100032";
		String actionNote = "退单";
		String content = "[{\"contactMobile\":\"13944447777\",\"contactName\":\"吴笔显\",\"contactTelephone\":0,\"contactTelephoneBranch\":0,\"explain\":\"退货唉\",\"exteriorType\":0,\"giftType\":0,\"isHistory\":\"0\",\"orderSn\":\"16102817525984\",\"reason\":2,\"redemption\":0,\"returnSum\":1,\"returnType\":1,\"skuSn\":\"8300210000\",\"tagType\":0,\"userId\":\"M150611100032\"}]";
		ReturnInfo info = bgReturnChangeService.createGoodsReturnChange(actionUser, actionNote, content, siteCode);
		System.out.println(JSON.toJSONString(info));  
		System.out.println("end");  
	}
	
	@Test
	public void testGetBGGoodsReturnInfo() throws Exception{
		final BGReturnChangeService bgReturnChangeService = reference.get();
		String actionUser = "KL2101285623";
		String actionNote = "退单";
		orderSn = "1611291343126934";
		siteCode = "KELTI";
		ApiReturnData info = bgReturnChangeService.getBGGoodsReturnInfo(orderSn, actionUser, siteCode);
		System.out.println(JSON.toJSONString(info));  
		System.out.println("end");  
	}
	
	@Test
	public void testGetBGGoodsReturnPageList() throws Exception{
		final BGReturnChangeService bgReturnChangeService = reference.get();
		String actionUser = "KL2101285623";
		String actionNote = "退单";
		orderSn = "1611291343126934";
		siteCode = "KELTI";
		PageListParam searchParam = new PageListParam();
		searchParam.setUserId(actionUser);
		searchParam.setPageNum(1);
		searchParam.setPageSize(10);
		ApiReturnData info = bgReturnChangeService.getBGGoodsReturnPageList("{\"userId\":\"KL2101285623\",\"pageSize\":10,\"rStatus\":\"-1\",\"isHistory\":0,\"pageNum\":1}", siteCode);
		System.out.println(JSON.toJSONString(info));
		System.out.println("end");  
	}
	
	@Test
	public void testConfirmReceipt2() throws Exception{
		System.out.println("1610171129266643S01".indexOf("1610171129266643") != -1);
		System.out.println("end");  
	}

	@Test
	public void testCreateGoodsReturnChange() {
        final BGReturnChangeService bgReturnChangeService = reference.get();

        CreateGoodsReturnChange createGoodsReturnChange = new CreateGoodsReturnChange();
        createGoodsReturnChange.setActionUser("wx679149");
        createGoodsReturnChange.setContactMobile("17321042146");
        createGoodsReturnChange.setContactName("超");
        createGoodsReturnChange.setSiteCode("WXC");
        createGoodsReturnChange.setOrderSn("1903042050524486");
        createGoodsReturnChange.setReturnType(1);
        createGoodsReturnChange.setReason(1);

        List<GoodsReturnChangeDetailInfo> list = new ArrayList<GoodsReturnChangeDetailInfo>();
        GoodsReturnChangeDetailInfo goodsBean = new GoodsReturnChangeDetailInfo();
        goodsBean.setCustomCode("20011300002");
        goodsBean.setExtensionCode("common");
        goodsBean.setReturnSum(1);
        list.add(goodsBean);
        createGoodsReturnChange.setGoodsList(list);

        ReturnInfo returnChangeNew = bgReturnChangeService.createGoodsReturnChangeNew(createGoodsReturnChange);
        System.out.print(JSONObject.toJSONString(returnChangeNew));
    }

    @Test
    public void testGetGoodsReturnChangeDetailList() {
        final BGReturnChangeService bgReturnChangeService = reference.get();

        ReturnChangeGoodsBean returnChangeGoodsBean = new ReturnChangeGoodsBean();
        returnChangeGoodsBean.setSiteCode("WXC");
        returnChangeGoodsBean.setOrderSn("1903042050524486");
        returnChangeGoodsBean.setReturnChangeSn("HS20190402164148");
        ApiReturnData list = bgReturnChangeService.getGoodsReturnChangeDetailList(returnChangeGoodsBean);
        System.out.print(JSONObject.toJSONString(list));
    }
}
