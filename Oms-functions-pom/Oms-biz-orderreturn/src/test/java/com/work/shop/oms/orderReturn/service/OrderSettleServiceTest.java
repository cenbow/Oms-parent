package com.work.shop.oms.orderReturn.service;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.vo.SettleParamObj;

public class OrderSettleServiceTest extends TestCase {
    
    private ApplicationConfig application = null;
    private ReferenceConfig<OrderSettleService> reference;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = new ApplicationConfig();  
        application.setName("OMS");  
        reference = new ReferenceConfig<OrderSettleService>();
        reference.setUrl("json://10.8.39.174:8080/Oms/dubbo/com.work.shop.oms.orderReturn.service.OrderSettleService");
//      reference.setUrl('json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService');
        reference.setTimeout(500);
        reference.setConnections(10);
        reference.setApplication(application);
        reference.setInterface(OrderSettleService.class);
        reference.setVersion("1.0.0");
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Test
    public void testCreateOrderReturn(){
        OrderSettleService orderSettleService = reference.get();
        SettleParamObj paramObj = new SettleParamObj();
        paramObj.setDealCode("TD160823035189");
        paramObj.setBussType(Constant.ERP_BUSS_TYPE_SETTLE_CANCLE);
        paramObj.setUserId("woya");
//        ReturnInfo<String> result = orderSettleService.OrderSettleCancle(paramObj);
        ReturnInfo<String> result = orderSettleService.returnSettleCancle(paramObj);
        System.out.println(JSON.toJSONString(result));
    }
    

}
