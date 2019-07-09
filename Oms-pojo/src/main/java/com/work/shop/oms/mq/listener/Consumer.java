package com.work.shop.oms.mq.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 * 消息监听器消费者
 * @author QuYachu
 */
public abstract class Consumer implements MessageListener {
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			if (logger.isInfoEnabled()) {
				// logger.info("onMessage " + JSON.toJSONString(text));
			}
			getDATA(text);
		} catch (Exception e) {
			logger.error("get message error", e);
		}
	}

	public abstract String getDATA(String text);
}
