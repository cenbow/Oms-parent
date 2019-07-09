package com.work.shop.oms.jms.listener;

import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.ConstantsUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.AsynProcessOrderBean;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.StringUtil;

import javax.annotation.Resource;

/**
 * 异步处理订单生成后续操作MQ队列监听类
 * @author lemon
 *
 */
@Service
public class AsynProcessOrderConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(AsynProcessOrderConsumer.class);

	/**
	 * 主订单服务
	 */
	@Resource
	private MasterOrderInfoService masterOrderInfoService;

	/**
	 * 获取队列消息
	 * @param text 消息体
	 * @return 返回结果
	 */
	@Override
	public String getDATA(String text) {
		try {
			logger.info("asynProcessOrderConsumer" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("异步处理订单生成后续操作:参数为空");
				return Constant.OS_STR_NO;
			}
			AsynProcessOrderBean bean = JSON.parseObject(text, AsynProcessOrderBean.class);
			if (bean != null) {
				masterOrderInfoService.dealOther(bean.getOrderSn(), bean.getValidateOrder(), bean.getOcpbStatus(), bean.getQuestionType());
			}
		} catch (Exception e) {
			logger.error(text + "异步处理订单生成后续操作失败", e);
		}
		return null;
	}
}