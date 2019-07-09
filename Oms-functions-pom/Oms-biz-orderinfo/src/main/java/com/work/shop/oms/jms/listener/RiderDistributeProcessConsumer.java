package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.rider.service.RiderDistributeService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单下发到骑手平台消费
 * @author QuYachu
 *
 */
@Service("riderDistributeProcessConsumer")
public class RiderDistributeProcessConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(RiderDistributeProcessConsumer.class);
	
	@Resource
	private RiderDistributeService riderDistributeService;

	/**
	 * 获取队列信息
	 * @param text
	 * @return String
	 */
	@Override
	public String getDATA(String text) {
		logger.info("RiderDistributeProcess:" + text);
		try {
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("下发骑手平台操作:参数为空");
				return null;
			}
			OrderRiderDistributeLog logModel = JSONObject.parseObject(text, OrderRiderDistributeLog.class);
			
			// 下发到骑手平台
			ServiceReturnInfo<Boolean> result = riderDistributeService.sendRiderDistribute(logModel);
			if (result != null && !result.isIsok()) {
				logger.error(JSONObject.toJSONString(logModel) + ",下发骑手平台失败!" + result.getMessage());
			}
		} catch (Exception e) {
			logger.error("RiderDistributeProcess error" + e);
		}
		return null;
	}
}
