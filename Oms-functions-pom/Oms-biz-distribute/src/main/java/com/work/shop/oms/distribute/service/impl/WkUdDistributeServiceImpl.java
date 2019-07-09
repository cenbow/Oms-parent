package com.work.shop.oms.distribute.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.ship.service.WkUdDistributeService;

/**
 * 供应商订单分仓结果服务
 * @author lemon
 */
@Service
public class WkUdDistributeServiceImpl implements WkUdDistributeService {

	private Logger log = Logger.getLogger(WkUdDistributeServiceImpl.class);

	@Resource(name = "noticeDistributeProducerJmsTemplate")
	private JmsTemplate jmsTemplate;

	/**
	 * 接受供应商分仓结果数据	
	 * @param depots 分仓结果
	 * @return boolean
	 */
	@Override
	public boolean depot(final List<WkUdDistribute> depots) {
		String msg = null;
		try {
			msg = JSON.toJSONString(depots);
			final String msg1 = msg;
			log.info("os收到分仓数据" + msg);
			jmsTemplate.send(new TextMessageCreator(msg1));
			log.debug("发送到MQ结束" + msg);
		} catch (Exception e) {
			log.error("os收到分仓数据异常",e);
			if(msg == null) {
				return false;
			}
			for (int i = 0; i < 3; i++) {
				try {
					jmsTemplate.send(new TextMessageCreator(msg));
					continue;
				}catch (Exception e1) {
					log.error("os收到分仓数据重试异常",e1);
				}
			}
			return false;
		}
		return true;
	}
}
