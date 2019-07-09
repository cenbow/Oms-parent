package com.work.shop.oms.express.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.Express100Service;
import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.mq.bean.TextMessageCreator;

/**
 * 快递100自动抓取任务
 * @author lemon
 */
@Service
public class Express100ServiceImpl implements Express100Service {

	private Logger logger = Logger.getLogger(Express100ServiceImpl.class);

	@Resource
	private OrderExpressTracingService orderExpressTracingService;

	@Resource(name = "pullExpressInfoProviderJmsTemplate")
	private JmsTemplate jmsTemplate;
	
	@Override
	public void express() {
		logger.info("Run express100Service Start:, " + new Date());
		// 当日已抓取的发货单号
		try {
			OrderExpressTracing selectTracing = new OrderExpressTracing();
			List<OrderExpressTracing> list = orderExpressTracingService.selectExpress(selectTracing);
			if (!list.isEmpty() && list.size() > 0) {
				for (final OrderExpressTracing oet : list) {
					String msg = JSON.toJSONString(oet);
					jmsTemplate.send(new TextMessageCreator(msg));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		logger.info("Run express100Service End:, " + new Date());
	}
}
