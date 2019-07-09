package com.work.shop.oms.orderReturn.service;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.api.express.service.OrderExpressPullService;

public class OrderExpressPullServiceTest extends TestCase {
    
    private ApplicationConfig application = null;
    
    private ReferenceConfig<OrderExpressPullService> reference = null;
    
    private OrderExpressPullService orderExpressPullService = null;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = new ApplicationConfig();  
        application.setName("OMS");  
        reference = new ReferenceConfig<OrderExpressPullService>();
      reference.setUrl("json://10.80.16.50:6688/com.work.shop.oms.api.express.service.OrderExpressPullService");
        reference.setTimeout(50000);
        reference.setConnections(1000);
        reference.setApplication(application);
        reference.setInterface(OrderExpressPullService.class);
        reference.setVersion("1.0.0");
        orderExpressPullService = reference.get();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Test
    public void testConfirm() throws Exception{
        System.out.println(orderExpressPullService.orderExpress("1608041020036655"));
    }
    
    
}
