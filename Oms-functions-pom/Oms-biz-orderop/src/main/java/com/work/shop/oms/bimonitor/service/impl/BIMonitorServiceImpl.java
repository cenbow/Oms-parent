package com.work.shop.oms.bimonitor.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bimonitor.service.BIMonitorService;

@Service("bIMonitorService")
public class BIMonitorServiceImpl implements BIMonitorService {

//	@Resource(name = "bmoeProviderJmsTemplate")
//	JmsTemplate bmoeProviderJmsTemplate;

	Logger logger = Logger.getLogger(BIMonitorServiceImpl.class);

	@Override
	public void sendMonitorMessage(String queueName, final String data) {
		String temp = "";
		logger.debug(temp = ("业务监控接口.队列名称 : " + queueName + ",监控数据:" + data));

		/*try {
			bmoeProviderJmsTemplate.send(queueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(data);
				}
			});
			logger.debug(temp + ".发送成功");
		} catch (Exception e) {
			logger.error(temp, e);
		}*/
	}
}
