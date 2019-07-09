package com.work.shop.oms.jms.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.distribute.service.NoticeDistributeService;
import com.work.shop.oms.mq.listener.Consumer;

/**
 * 通知分配MQ队列监听类
 * @author lemon
 *
 */
@Service
public class DistributeNoticeListener extends Consumer {

	private static Logger logger = Logger.getLogger(DistributeNoticeListener.class);
	
	private static final String keyword = "交货单分配通知[notice_distribute]：";

	@Resource
	private NoticeDistributeService noticeDistributeService;

	@Override
	public String getDATA(String text) {
		try {
			logger.info(keyword + text);
			List<WkUdDistribute> items = JSON.parseArray(text, WkUdDistribute.class);
			String orderSn = getOrderSn(items);
			noticeDistributeService.processDistribute(orderSn, items, true);
		} catch (Exception e) {
			logger.error(keyword + "处理失败" + text, e);
		}
		return null;
	}

	/**
	 * 获取订单配送单号
	 * @param wkUdDistributeList
	 * @return
	 */
	private String getOrderSn(java.util.List<WkUdDistribute> wkUdDistributeList) {
		logger.debug(keyword + "获取订单号");
		if (wkUdDistributeList == null || wkUdDistributeList.isEmpty()) {
			return "";
		}
		WkUdDistribute temp = wkUdDistributeList.get(0);
		if (temp == null) {
			return "";
		}
		return temp.getOuterCode();
	}
}