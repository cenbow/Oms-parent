package com.work.shop.oms.controller.jmsmessage;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.controller.service.RewardPointChangeLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/23 12:45 下午
 * @Version V1.0
 **/

//添加积分变更记录
public class AddRewardPointChangeLogListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RewardPointChangeLogService rewardPointChangeLogService;

    @Override
    public void onMessage(Message message) {
        TextMessage objMessage = null;
        if (message instanceof TextMessage) {
            objMessage = (TextMessage) message;
        } else {
            return;
        }

        try {
            if (!StringUtils.isEmpty(objMessage.getText())) {
                RewardPointChangeLogBean rewardPointChangeLogBean = JSON.parseObject(objMessage.getText(), RewardPointChangeLogBean.class);
                if (rewardPointChangeLogBean == null) {
                    logger.error("添加积分变更记录错误: 没有积分变更记录!");
                    return;
                }
                rewardPointChangeLogService.addRewardPointChangeLog(rewardPointChangeLogBean);
                logger.info("添加积分变更记录: {}", rewardPointChangeLogBean.toString());
            }
        } catch (Exception e) {
            logger.error("添加积分变更记录错误: {}", e.getMessage());
        }
    }
}
