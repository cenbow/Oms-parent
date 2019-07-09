package com.work.shop.oms.mq.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单退货完成消费
 * @author QuYachu
 */
@Service
public class OrderReturnFinishConsumer extends Consumer {

	//@Resource
	private OrderReturnService orderReturnService;

	private static Logger logger = Logger.getLogger(OrderReturnFinishConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("orderreturnfinish:" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("退单完成通知操作:参数为空");
				return "0";
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("退单完成通知操作: orderStatus参数为空");
				return "0";
			}
			orderReturnService.orderReturnFinish(orderStatus);
		} catch (Exception e) {
			logger.error(text + "退单完成通知操作失败", e);
		}
		return "1";
	}
}
