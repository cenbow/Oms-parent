package com.work.shop.oms.express.jms.listener;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingKey;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.config.service.SystemShippingService;

public class OrderReturnExpressPullConsumer implements MessageListener{

	private final static Logger logger = Logger.getLogger(OrderReturnExpressPullConsumer.class);
	
	@Resource
	private SystemShippingService systemShippingService;
	
	@Resource
	private OrderExpressTracingService orderExpressTracingService;
	
	@Override
	public void onMessage(Message text) {
		TextMessage textMessage = (TextMessage) text;
		String data = null;
		try{
			data = URLDecoder.decode(textMessage.getText() , "UTF-8");
			logger.info("Get Message from QUORDERERPSTATUSINFO :"+data);
			Map<String, Object> sub  = JSON.parseObject(data,HashMap.class);
			OrderReturn orderReturn=JSON.parseObject(sub.get("orderReturn").toString(), OrderReturn.class);
			OrderReturnShip orderReturnShip=JSON.parseObject(sub.get("orderReturnShip").toString(), OrderReturnShip.class);
			String orderRturnSn = orderReturn.getReturnSn();//退货：填的是退货单号（订单填的是发货物流单号）
			String depotCode=orderReturnShip.getDepotCode();
			OrderExpressTracingKey expressTracingKey = new OrderExpressTracingKey();
			expressTracingKey.setDeliverySn(orderRturnSn);
			expressTracingKey.setDepotCode(depotCode);
			expressTracingKey.setOrderSn(orderRturnSn);
			OrderExpressTracing oet = orderExpressTracingService.selectByPrimaryKey(expressTracingKey);
			SystemShipping systemShipping = systemShippingService.getSystemShipByShipCode(orderReturnShip.getReturnExpress());
			if(null==oet){
				OrderExpressTracing e=new OrderExpressTracing();
				e.setAddTime(new Date());
//				e.setCompanyId(systemShipping.getShippingId().intValue());
				e.setCompanyCode(systemShipping.getShippingCode());
				e.setDeliveryTime(new Date());
				e.setDepotCode(depotCode);
				e.setExpressStatus(0F);
				e.setFetchCount((byte) 0);
				e.setFetchFlag((byte) 0);
				e.setOrderSn(orderRturnSn);
				e.setConfirmTime(orderReturn.getConfirmTime());//订单确认时间（退货的为退货确认时间）
				e.setOrderType(-1);//退货为-1 
				e.setTrackno(orderReturnShip.getReturnInvoiceNo());
				e.setDeliverySn(orderRturnSn);
				orderExpressTracingService.insertSelective(e);
			}else{
				oet.setTrackno(orderReturnShip.getReturnInvoiceNo());
				orderExpressTracingService.updateByPrimaryKeySelective(oet);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
	}

}
