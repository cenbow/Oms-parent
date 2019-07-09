package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.OrderRiderDistributeLogMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.rider.service.RiderDistributeService;
import com.work.shop.oms.utils.Constant;
@Service("riderDistTask")
public class RiderDistTask extends ATaskServiceProcess{

	private static Logger logger = LoggerFactory.getLogger(RiderDistTask.class);
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;

	@Resource(name = "riderDistributeProcessJmsTemplate")
	private JmsTemplate riderDistributeProcessJmsTemplate;
	@Resource(name="riderDistributeService")
	private RiderDistributeService riderDistributeService;
	@Resource(name="orderRiderDistributeLogMapper")
	private OrderRiderDistributeLogMapper orderRiderDistributeLogMapper;

	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_RIDER_DIST_TASK;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
		logger.debug(taskName + "queryServiceData.dataLimit:" + dataLimit);
		OrderQueryRequest request = new OrderQueryRequest();
		OrderQueryResponse response = riderDistributeService.riderDistGet(request);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		if (!response.getSuccess()) {
			return null;
		}
		if (CollectionUtils.isNotEmpty(response.getOrderItems())) {
			for (OrderItem orderItem : response.getOrderItems()) {
				OrderRiderDistributeLog distributeLog = new OrderRiderDistributeLog();
				distributeLog.setMasterOrderSn(orderItem.getMasterOrderSn());
				logger.info("配送单信息写入MQ" + JSON.toJSONString(distributeLog));
				riderDistributeProcessJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(distributeLog)));
			}
		}
		return taskDatas;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.info(taskName + "executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		try {
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("订单自提码发送成功");
		} catch (Exception e) {
			logger.error("订单自提码发送"+ orderSn +" ,错误信息:"+e.getMessage(),e);
			returnTask.setMsg("订单自提码发送异常：" + e.getMessage());
		}
		return returnTask;
	}
}