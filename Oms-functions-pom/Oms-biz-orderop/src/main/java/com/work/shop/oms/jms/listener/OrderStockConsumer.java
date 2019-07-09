package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 商城订单消费
 * @author QuYachu
 */
@Service
public class OrderStockConsumer extends Consumer {

	@Resource
	private ChannelStockService channelStockService;

	private static Logger logger = Logger.getLogger(OrderStockConsumer.class);

	/**
	 * 获取消息
	 * @param text
	 * @return
	 */
	@Override
	public String getDATA(String text) {
		try {
			logger.info("order_stock:" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("异步订单库存占用操作:参数为空");
				return Constant.OS_STR_NO;
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("异步订单库存占用操作:参数为空");
				return Constant.OS_STR_NO;
			}

			channelStockService.payOccupy(orderStatus.getMasterOrderSn());
		} catch (Exception e) {
			logger.error(text + "订单库存占用操作失败", e);
		}
		return Constant.OS_STR_YES;
	}
}
