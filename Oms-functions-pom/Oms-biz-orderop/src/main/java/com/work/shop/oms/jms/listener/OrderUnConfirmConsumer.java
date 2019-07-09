package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单未确认消费
 * @author QuYachu
 */
@Service
public class OrderUnConfirmConsumer extends Consumer {

	//@Resource
	private OrderConfirmService orderConfirmService;

	private static Logger logger = Logger.getLogger(OrderUnConfirmConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("distributesupplier" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("异步处理订单确认操作:参数为空");
				return "0";
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("异步处理订单确认操作:参数为空");
				return "0";
			}
			if (Constant.order_type_master.equals(orderStatus.getType())) {
				orderConfirmService.unConfirmOrderByMasterSn(orderStatus.getMasterOrderSn(), orderStatus);
			} else {
				orderConfirmService.unConfirmOrderByOrderSn(orderStatus.getOrderSn(), orderStatus);
			}
		} catch (Exception e) {
			logger.error(text + "异步处理订单确认操作失败", e);
		}
		return null;
	}
}
