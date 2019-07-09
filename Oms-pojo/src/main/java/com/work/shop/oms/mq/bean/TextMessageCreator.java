package com.work.shop.oms.mq.bean;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.MessageCreator;

public class TextMessageCreator implements MessageCreator {

	private String message;

	public TextMessageCreator(String message) {
		this.message = message;
	}

	public Message createMessage(Session session) throws JMSException {
		TextMessage message = session.createTextMessage();
		message.setText(this.message);
		return message;
	}
}
