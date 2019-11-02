package com.work.shop.oms.jms.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.AccountSettlementOrderBean;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.user.account.UserAccountService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单账户结算消费
 * @author QuYachu
 */
@Service
public class OrderSnAccountSettlementConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(OrderSnAccountSettlementConsumer.class);

	@Resource
	private UserAccountService userAccountService;

	@Override
	public String getDATA(String text) {
		try {
			logger.info("OrderSnAccountSettlementConsumer" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("参数为空");
				return Constant.OS_STR_NO;
			}
			AccountSettlementOrderBean accountSettlementOrderBean = JSON.parseObject(text, AccountSettlementOrderBean.class);
			if (accountSettlementOrderBean == null) {
				logger.error("参数为空");
				return Constant.OS_STR_NO;
			}

            ReturnInfo<Boolean> returnInfo = userAccountService.doUserSettlementAccount(accountSettlementOrderBean);
			logger.info("userAccountService.doUserSettlementAccount:" + JSONObject.toJSONString(returnInfo));
		} catch (Exception e) {
			logger.error(text + "订单账户结算消费操作失败", e);
		}
		return null;
	}
}
