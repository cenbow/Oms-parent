package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单库存释放消费
 * @author QuYachu
 */
@Service
public class OrderStockRealeseConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(OrderStockRealeseConsumer.class);

	@Resource
	private ChannelStockService channelStockService;

	@Override
	public String getDATA(String text) {
		try {
			logger.info("order_stock_realese:" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("订单库存释放操作:参数为空");
				return Constant.OS_STR_NO;
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("异步处理订单库存释放操作:参数为空");
				return Constant.OS_STR_NO;
			}
			// 创建退单
			if (Constant.order_type_distribute.equals(orderStatus.getType())) {
				//orderStockService.realeseByOrderSn(orderStatus.getOrderSn(), orderStatus.getMasterOrderSn());
			} else {
				channelStockService.cancelRealese(orderStatus.getMasterOrderSn());
			}
		} catch (Exception e) {
			logger.error(text + "订单库存释放操作失败", e);
		}
		return Constant.OS_STR_YES;
	}
}
