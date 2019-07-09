package com.work.shop.oms.jms.listener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderStockDistConsumer extends Consumer {  

	private static Logger logger = Logger.getLogger(OrderStockDistConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("order_stock_dist:" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("订单库存占用操作:参数为空");
				return "0";
			}
//			orderStockService.callStockCenter(text);
		} catch (Exception e) {
			logger.error(text + "订单库存占用操作失败", e);
		}
		return "";
	}
}
