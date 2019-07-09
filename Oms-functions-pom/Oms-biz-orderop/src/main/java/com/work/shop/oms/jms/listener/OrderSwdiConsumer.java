package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单拉取消费
 * @author QuYachu
 */
@Service
public class OrderSwdiConsumer extends Consumer {

	//@Resource
	private OrderDistributeOpService orderDistributeOpService;

	private static Logger logger = Logger.getLogger(OrderSwdiConsumer.class);

	@Override
	public String getDATA(String text) {
		logger.info("distributesupplier" + text);
		if (StringUtil.isTrimEmpty(text)) {
			logger.error("异步处理订单SWDI操作:参数为空");
			return "0";
		}
		try {
			logger.info("Get Message from order swdi :" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("拉取订单分仓发货:参数为空！");
				return null;
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("异步处理订单确认操作:参数为空");
				return "0";
			}
			try {
				if (Constant.order_type_master.equals(orderStatus.getType())) {
					orderDistributeOpService.sWDI(orderStatus.getMasterOrderSn(), null, orderStatus);
				} else {
					orderDistributeOpService.sWDI(null, orderStatus.getOrderSn(), orderStatus);
				}
			} catch (Exception e) {
				logger.error(text + "拉取订单分仓发货失败", e);
			}
			return null;
		} catch (Exception e) {
			logger.error(text + "异步处理订单确认操作失败", e);
		}
		return null;
	}
}
