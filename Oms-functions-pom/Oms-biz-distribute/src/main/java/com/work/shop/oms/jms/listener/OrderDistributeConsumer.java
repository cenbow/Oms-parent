package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.mq.listener.Consumer;

/**
 * 拆单MQ队列监听类
 * @author lemon
 *
 */
@Service
public class OrderDistributeConsumer extends Consumer {

	@Resource
	private OrderDistributeService orderDistributeService;
	
	private static Logger logger = Logger.getLogger(OrderDistributeConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("订单异步处理拆单：" + text);
			orderDistributeService.orderDistribute(text);
		} catch (Exception e) {
			logger.error("订单异步处理拆单失败", e);
		}
		return null;
	}
}