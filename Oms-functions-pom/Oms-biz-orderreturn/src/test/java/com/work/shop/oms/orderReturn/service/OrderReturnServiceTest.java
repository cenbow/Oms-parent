package com.work.shop.oms.orderReturn.service;

import org.junit.Test;

import junit.framework.TestCase;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderReturn.service.OrderReturnService;

public class OrderReturnServiceTest extends TestCase {
    
    private ApplicationConfig application = null;
    private ReferenceConfig<OrderReturnService> reference;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = new ApplicationConfig();  
        application.setName("OMS");  
        reference = new ReferenceConfig<OrderReturnService>();
        reference.setUrl("json://10.8.39.174:8080/Oms/dubbo/com.work.shop.oms.orderReturn.service.OrderReturnService");
//      reference.setUrl('json://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.order.service.MasterOrderInfoService');
        reference.setTimeout(500);
        reference.setConnections(10);
        reference.setApplication(application);
        reference.setInterface(OrderReturnService.class);
        reference.setVersion("1.0.0");
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Test
    public void testCreateOrderReturn(){
        OrderReturnService orderReturnService = reference.get();
   
        String json = "{'actionNote':'','actionUser':'花花','createOrderRefundList':[{'returnFee':70.2,'returnPay':1}],'createOrderReturn':{'haveRefund':1,'newOrderSn':'','payment':0,'processType':1,'relatingOrderSn':'1605241044100427s03','returnBonusMoney':0,'returnCard':0,'returnDesc':'','returnGoodsMoney':70.2,'returnIntegralMoney':0,'returnOtherMoney':0,'returnPackMoney':0,'returnReason':'33','returnSettlementType':1,'returnShipping':0,'returnSn':'','returnTax':0,'returnTotalFee':70.2,'returnType':1,'totalIntegralMoney':0,'totalPriceDifference':0},'createOrderReturnGoodsList':[{'chargeBackCount':0,'customCode':'20150592137','extensionCode':'common','extensionId':'4','goodsBuyNumber':1,'goodsPrice':35.1,'goodsReturnNumber':1,'goodsThumb':'/sources/images/goods/MB/201505/201505_00.jpg','haveReturnCount':0,'integralMoney':0,'marketPrice':99,'osDepotCode':'HQ01W500','payPoints':35.1,'priceDifferNum':1,'relatingReturnSn':'','returnReason':'','seller':'MB','settlePrice':0,'settlementPrice':35.1,'shareBonus':0,'shareSettle':0},{'chargeBackCount':0,'customCode':'60124730152','extensionCode':'common','extensionId':'2','goodsBuyNumber':1,'goodsPrice':35.1,'goodsReturnNumber':1,'goodsThumb':'/sources/images/goods/MB/601247/601247_00.jpg','haveReturnCount':0,'integralMoney':0,'marketPrice':99,'osDepotCode':'HQ01W850','payPoints':35.1,'priceDifferNum':1,'relatingReturnSn':'','returnReason':'','seller':'MB','settlePrice':0,'settlementPrice':35.1,'shareBonus':0,'shareSettle':0}],'createOrderReturnShip':{'depotCode':'','relatingReturnSn':'','returnExpress':'','returnInvoiceNo':''},'orderReturnSn':'','relatingOrderSn':'1605241044100427s03','returnType':1}";
        CreateOrderReturnBean createOrderReturnBean = JSON.parseObject(json, CreateOrderReturnBean.class);
        ReturnInfo<String> result = orderReturnService.createOrderReturn(createOrderReturnBean);
        System.out.println(JSON.toJSONString(result));
    }
    

}
