package com.work.shop.oms.orderop.service;

public interface JmsSendQueueService {

	/**
	 * 发送服务队列
	 * @param queueName
	 * @param data
	 */
	void sendQueueMessage(String queueName,String data);
}
