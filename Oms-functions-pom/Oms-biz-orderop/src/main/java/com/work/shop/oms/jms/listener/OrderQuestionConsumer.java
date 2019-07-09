package com.work.shop.oms.jms.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.OrderQuestionParam;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单问题单消费
 * @author QuYachu
 */
@Service
public class OrderQuestionConsumer extends Consumer {

	//@Resource
	private OrderQuestionService orderQuestionService;

	private static Logger logger = Logger.getLogger(OrderQuestionConsumer.class);

	@Override
	public String getDATA(String text) {
		logger.info("distributesupplier" + text);
		if (StringUtil.isTrimEmpty(text)) {
			logger.error("异步处理订单确认操作:参数为空");
			return "0";
		}
		try {
			OrderQuestionParam param = JSONObject.parseObject(text, OrderQuestionParam.class);
			try {
				if (null == param) {
					logger.error("设为物流问题单:问题单信息为空！");
					return null;
				}
				ReturnInfo info = new ReturnInfo(Constant.OS_NO);
				String orderSn = param.getOrderSn();
				String masterOrderSn = param.getMasterOrderSn();
				OrderStatus orderStatus = new OrderStatus(masterOrderSn, param.getNote(), param.getActionUser(), param.getCode());
				if (param.getLogType() == 1) {
					List<LackSkuParam> lackSkuParams = param.getLackSkuParams();
					// 物流缺货问题单
					info = orderQuestionService.addLackSkuQuestion(orderSn, lackSkuParams, orderStatus);
				} else {
					// 普通问题单
					if (StringUtil.isNotEmpty(orderSn)) {
						info = orderQuestionService.questionOrderByOrderSn(orderSn, orderStatus);
					} else {
						info = orderQuestionService.questionOrderByMasterSn(masterOrderSn, orderStatus);
					}
				}
				if (info.getIsOk() == 1) {
					logger.info("订单[" + orderSn + "]JMS 处理设为物流问题单成功！");
				} else {
					logger.info(info.getMessage());
				}
			} catch (Exception e) {
				logger.error("订单["+ param.getMasterOrderSn() +"]["+ param.getOrderSn() +"]设为物流问题单异常", e);
			}
		} catch (Exception e) {
			logger.error(text + "异步处理订单确认操作失败", e);
		}
		return null;
	}
}
