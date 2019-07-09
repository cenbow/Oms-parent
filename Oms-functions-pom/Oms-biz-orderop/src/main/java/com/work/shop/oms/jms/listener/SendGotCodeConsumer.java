package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 自提码处理消费
 * @author QuYachu
 */
@Service
public class SendGotCodeConsumer extends Consumer {

	//@Resource
	private OrderDistributeOpService orderDistributeOpService;

	private static Logger logger = Logger.getLogger(SendGotCodeConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("distributesupplier" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("发送自提码短信认操作:参数为空");
				return "0";
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("发送自提码短信操作:参数为空");
				return "0";
			}
			orderDistributeOpService.sendGotCode(orderStatus);
		} catch (Exception e) {
			logger.error(text + "发送自提码短信操作失败", e);
		}
		return null;
	}
}
