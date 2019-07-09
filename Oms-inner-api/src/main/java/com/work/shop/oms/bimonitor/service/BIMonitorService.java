package com.work.shop.oms.bimonitor.service;

/**
 * 业务监控服务
 * 
 *
 */
public interface BIMonitorService {
	
	/**
	 * 发送业务监控服务
	 * @param queueName
	 * @param data
	 */
	void sendMonitorMessage(String queueName,String data);
}
