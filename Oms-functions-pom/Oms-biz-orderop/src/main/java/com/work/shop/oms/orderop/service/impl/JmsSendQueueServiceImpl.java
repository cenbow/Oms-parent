package com.work.shop.oms.orderop.service.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bimonitor.service.impl.BIMonitorServiceImpl;
import com.work.shop.oms.orderop.service.JmsSendQueueService;

@Service
public class JmsSendQueueServiceImpl implements JmsSendQueueService{

	@Resource(name = "putQueueJmsTemplate")
	private JmsTemplate putQueueJmsTemplate;

	Logger logger = Logger.getLogger(BIMonitorServiceImpl.class);

	@Override
	public void sendQueueMessage(String queueName, final String data) {
		String temp = "";
		logger.debug(temp = ("业务监控接口.队列名称 : " + queueName + ",传输数据:" + data));
		try {
			putQueueJmsTemplate.send(queueName, new MessageCreator() {
				@Override
				public javax.jms.Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(data);
				}
			});
			logger.debug(temp + ".发送成功");
		} catch (Exception e) {
			logger.error(temp, e);
		}
	}
}
