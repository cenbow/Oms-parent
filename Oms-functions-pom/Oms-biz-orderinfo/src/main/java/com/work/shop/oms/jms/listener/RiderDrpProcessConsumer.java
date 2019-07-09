package com.work.shop.oms.jms.listener;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.send.service.RiderDrpService;
import com.work.shop.oms.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 配送完成订单推送drp
 * @author YeQingchao
 */
@Service("riderDrpProcessConsumer")
public class RiderDrpProcessConsumer extends Consumer {

    private Logger logger = Logger.getLogger(RiderDrpProcessConsumer.class);

    //@Resource(name="riderDrpService")
    private RiderDrpService riderDrpService;

    @Override
    public String getDATA(String text) {
        logger.info("RiderDrpProcess:" + text);
        try {
            if (StringUtil.isTrimEmpty(text)) {
                logger.error("配送完成订单推送drp:参数为空");
                return null;
            }
            OrderQueryRequest request = JSONObject.parseObject(text, OrderQueryRequest.class);

            // 配送完成订单推送drp
            ServiceReturnInfo<Boolean> result = riderDrpService.sendDrpByDeliverySuccess(request);
            if (result != null && !result.isIsok()) {
                logger.error(JSONObject.toJSONString(request) + ",配送完成订单推送drp失败!" + result.getMessage());
            }
        } catch (Exception e) {
            logger.error("RiderDrpProcess error" + e);
        }

        return null;
    }
}
