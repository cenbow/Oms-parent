package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.mq.listener.Consumer;
/**
 * 分发MQ队列监听类
 * @author lemon
 *
 */
@Service
public class DistributeSupplierConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(DistributeSupplierConsumer.class);

	//@Resource
	private DistributeSupplierService distributeSupplierService;

	@Override
	public String getDATA(String text) {
		try {
			logger.info("distributesupplier" + text);
			distributeSupplierService.executeDistributeByMq(text);
		} catch (Exception e) {
			logger.error("处理失败", e);
		}
		return null;
	}
}