package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.annotation.Service;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.dao.BatchDecodeListMapper;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.mq.listener.Consumer;

@Service
public class BatchDecodeConsumer extends Consumer {
	private static Logger logger = Logger.getLogger(BatchDecodeConsumer.class);
	@Resource
	private OrderBillListMapper orderBillListMapper;
	@Resource
	private BatchDecodeListMapper batchDecodeListMapper;

	@Override
	public String getDATA(String text) {
		String billNo = "";
		try {} catch (Exception e) {
			OrderBillList param = new OrderBillList();
			param.setBillNo(billNo);
			param.setIsSync(new Byte("4"));
			orderBillListMapper.updateByPrimaryKeySelective(param);
			logger.error(text + "批量解密联系方式操作失败", e);
		}
		return "";
	}

}
