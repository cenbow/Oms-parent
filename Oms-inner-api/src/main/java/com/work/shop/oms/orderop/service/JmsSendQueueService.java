package com.work.shop.oms.orderop.service;

/**
 * MQ发放服务
 * @author QuYachu
 */
public interface JmsSendQueueService {

	/**
	 * 发送服务队列
	 * @param queueName 队列名称
	 * @param data 数据
	 */
	void sendQueueMessage(String queueName,String data);
}
