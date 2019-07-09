package com.work.shop.oms.activemq.service;

import java.util.List;

import com.work.shop.oms.bean.QueueMqConfig;
import com.work.shop.oms.bean.ServerMqConfig;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 
 * @author lemon
 *
 */
public interface ServerQueueMqService {

	/**
	 * 获取服务MQ队列列表
	 * @param serverName
	 * @param serverIp
	 * @return
	 */
	ReturnInfo<List<QueueMqConfig>> getServerQueues(String serverName, String serverIp);

	/**
	 * 获取服务MQ队列
	 * @param serverName
	 * @param serverIp
	 * @return
	 */
	ReturnInfo<ServerMqConfig> getServer(String serverName, String serverIp);
	
	/**
	 * 创建服务MQ配置
	 * @param mqConfig
	 * @return
	 */
	ReturnInfo<ServerMqConfig> createServer(ServerMqConfig mqConfig);
	
	
	/**
	 * 更新服务MQ配置
	 * @param mqConfig
	 * @return
	 */
	ReturnInfo<ServerMqConfig> updateServer(ServerMqConfig mqConfig);
	
	/**
	 * 创建服务MQ队列
	 * @param mqConfig
	 * @return
	 */
	ReturnInfo<QueueMqConfig> createQueue(QueueMqConfig mqConfig);
	
	
	/**
	 * 更新服务MQ队列
	 * @param mqConfig
	 * @return
	 */
	ReturnInfo<QueueMqConfig> updateQueue(QueueMqConfig mqConfig);
	
	/**
	 * 更新服务MQ队列
	 * @param mqConfig
	 * @return
	 */
	ReturnInfo<QueueMqConfig> updateQueueById(QueueMqConfig mqConfig);
	
}
