package com.work.shop.oms.activemq.service;

import java.util.List;

import com.work.shop.oms.bean.QueueMqConfig;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * MQ 队列管理服务
 * @author lemon
 *
 */
public interface ActiveMQService {

	/**
	 * 关闭MQ队列的所有监听
	 * @param queueName
	 * @return
	 */
	ReturnInfo closeAllMQqueue(String queueName);
	
	/**
	 * 删除MQ队列监听
	 * @param queueName
	 * @param num
	 * @return
	 */
	ReturnInfo deleteMQqueue(String queueName, int num);
	
	/**
	 * 添加MQ队列监听
	 * @param queueName
	 * @param num
	 * @return
	 */
	ReturnInfo addMQqueue(String queueName, int num);

	/**
	 * 更新队列监听
	 * @param queueName
	 * @param listeserRef
	 * @param concurrency
	 * @return
	 */
	ReturnInfo updateQueueLintener(String queueName, String listeserRef, String concurrency);
	
	/**
	 * 注册队列监听
	 * @param queueName
	 * @param listeserRef
	 * @param concurrency
	 * @return
	 */
	ReturnInfo registeQueueLintener(String queueName, String listeserRef, String concurrency);
}
