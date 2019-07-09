package com.work.shop.oms.jms.listener;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.send.service.OrderToEcService;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单下发到EC平台
 * @author YeQingchao
 */
@Service("riderEcProcessConsumer")
public class RiderEcProcessConsumer extends Consumer {

    private static final Logger logger = Logger.getLogger(RiderEcProcessConsumer.class);

    //@Resource
    private OrderToEcService orderToEcService;

    @Override
    public String getDATA(String text) {
        logger.info("RiderEcProcess:" + text);
        try {
            if (StringUtil.isTrimEmpty(text)) {
                logger.error("下发EC系统一小时达操作:参数为空");
                return null;
            }
            OrderQueryRequest request = JSONObject.parseObject(text, OrderQueryRequest.class);

            // 下发EC系统一小时达
            ServiceReturnInfo<Boolean> result = orderToEcService.sendOrderToEc(request);
            if (result != null && !result.isIsok()) {
                logger.error(JSONObject.toJSONString(request) + ",下发EC系统一小时达失败!" + result.getMessage());
            }
        } catch (Exception e) {
            logger.error("RiderEcProcess error" + e);
        }

        return null;
    }
}
