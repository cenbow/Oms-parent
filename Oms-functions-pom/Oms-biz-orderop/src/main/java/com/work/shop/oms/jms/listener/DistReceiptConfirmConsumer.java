package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 配送单收货确认
 * @author QuYachu
 *
 */
@Service
public class DistReceiptConfirmConsumer extends Consumer {

	@Resource
	private DistributeShipService distributeShipService;

	private static Logger logger = Logger.getLogger(DistReceiptConfirmConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("distDeliveryConfirm" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("配送单收货确认操作:参数为空");
				return Constant.OS_STR_NO;
			}
			DistributeShippingBean distributeShipBean = JSON.parseObject(text, DistributeShippingBean.class);
			if (distributeShipBean == null) {
				logger.error("配送单收货确认操作:参数为空");
				return Constant.OS_STR_NO;
			}
			ReturnInfo<String> info = distributeShipService.distReceiptConfirm(distributeShipBean);
			logger.info("配送单收货确认response:" + JSON.toJSONString(info));
		} catch (Exception e) {
			logger.error(text + "配送单收货确认操作失败", e);
		}
		return Constant.OS_STR_YES;
	}
}
