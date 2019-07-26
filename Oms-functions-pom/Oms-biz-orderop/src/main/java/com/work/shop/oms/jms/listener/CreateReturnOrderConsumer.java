package com.work.shop.oms.jms.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.order.service.ReturnManagementService;
import com.work.shop.oms.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CreateReturnOrderConsumer extends Consumer {

	@Resource
	private ReturnManagementService returnManagementService;

	private static Logger logger = Logger.getLogger(CreateReturnOrderConsumer.class);

	@Override
	public String getDATA(String text) {
		try {
			logger.info("CreateReturnOrderConsumer" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("异步处理订单确认操作:参数为空");
				return "0";
			}
            ReturnManagementRequest request = JSON.parseObject(text, ReturnManagementRequest.class);
			if (request == null) {
				logger.error("异步处理订单确认操作:参数为空");
				return "0";
			}
            logger.error("创建退单开始 -> 请求参数:" + JSONObject.toJSONString(request));
            ReturnManagementResponse response = returnManagementService.returnItemCreate(request);
            logger.error("创建退单结束 -> 返回结果:" + JSONObject.toJSONString(response));
        } catch (Exception e) {
			logger.error(text + "异步处理订单确认操作失败", e);
		}
		return null;
	}
}
