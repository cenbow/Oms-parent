package com.work.shop.oms.jms.listener;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.Constants;
import com.work.shop.oms.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单超时收货消费
 * @author QuYachu
 */
@Service
public class OrderSnReceiveConsumer extends Consumer {

	@Resource
	private DistributeShipService distributeShipService;

	private static Logger logger = Logger.getLogger(OrderSnReceiveConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("OrderSnReceiveConsumer" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("参数为空");
				return Constant.OS_STR_NO;
			}
			DistributeShippingBean distributeShippingBean = JSON.parseObject(text, DistributeShippingBean.class);
			if (distributeShippingBean == null) {
				logger.error("参数为空");
				return Constant.OS_STR_NO;
			}
			// 系统超时自动签收
			distributeShippingBean.setType(1);
            distributeShippingBean.setActionUser(Constant.OS_STRING_SYSTEM);
			distributeShipService.confirmationOfReceipt(distributeShippingBean);
		} catch (Exception e) {
			logger.error(text + "订单发货超时自动签收操作失败", e);
		}
		return null;
	}
}
