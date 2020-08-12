package com.work.shop.oms.controller.jmsmessage;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ProductGroupBuyBean;
import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.controller.service.RewardPointChangeLogService;
import com.work.shop.oms.order.service.GroupBuyOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Map;


//团购结果(成功或失败)mq消费
public class GroupBuyResutlListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GroupBuyOrderService groupBuyOrderService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            logger.info("团购成功mq消费: {}", textMessage.getText());
            if (!StringUtils.isEmpty(textMessage.getText())) {
                ProductGroupBuyBean groupBuyBean = JSON.parseObject(textMessage.getText(), ProductGroupBuyBean.class);
                if (groupBuyBean == null|| groupBuyBean.getId() == null || groupBuyBean.getParticipateGroupType() == null) {
                    logger.error("团购成功mq消费失败,参数错误!"+JSON.toJSONString(groupBuyBean));
                    return;
                }
                groupBuyOrderService.processGroupByResult(groupBuyBean);
            }
        } catch (Exception e) {
            logger.error("团购成功mq消费失败: {}", e.getMessage());
        }
    }
}
