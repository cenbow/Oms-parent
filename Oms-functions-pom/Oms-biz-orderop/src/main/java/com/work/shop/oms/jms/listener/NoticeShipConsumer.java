package com.work.shop.oms.jms.listener;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.StringUtil;
/**
 * 发货MQ队列监听类
 * @author lemon
 *
 */
@Service
public class NoticeShipConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(NoticeShipConsumer.class);

	 @Resource
	private DistributeShipService distributeShipService;

	@Override
	public String getDATA(String text) {
		try {
			logger.info("shipped-getDATA" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("接收订单发货数据为空");
				return null;
			}
			/*OrderToShippedProviderBeanParam providerBeanParam = JSONObject.parseObject(text, OrderToShippedProviderBeanParam.class);
			if (providerBeanParam == null) {
				logger.error("接收订单发货数据参数为空");
				return null;
			}
			distributeShipService.processShip(providerBeanParam.getOrderSn(), providerBeanParam.getShipBeans(), true);*/
			
			DistributeShippingBean distributeShipBean = JSONObject.parseObject(text, DistributeShippingBean.class);
			if (distributeShipBean == null) {
				logger.error("接收订单发货数据参数为空");
				return null;
			}
			distributeShipService.processShip(distributeShipBean, true);
		} catch (Exception e) {
			logger.error("订单发货数据处理失败", e);
		}
		return null;
	}
}