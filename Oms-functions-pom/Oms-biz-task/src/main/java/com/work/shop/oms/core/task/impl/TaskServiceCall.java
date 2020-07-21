package com.work.shop.oms.core.task.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.oms.api.express.feign.Express100Service;
import com.work.shop.oms.core.service.impl.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.task.ITaskServiceCall;

/**
 * 订单定时任务
 * @author QuYachu
 */
@Service("taskServiceCall")
public class TaskServiceCall implements ITaskServiceCall{
	private static Logger logger = LoggerFactory.getLogger(TaskServiceCall.class);
	
	@Resource(name="taskManagerFactory")
	private TaskManagerFactory taskManagerFactory;
	
	@Resource(name = "closeNoPayBanggoOrderTask")
	private CloseNoPayBanggoOrderTask closeNoPayBanggoOrderTask;
	
	@Resource(name = "closeNoPayGroupOrderTask")
	private CloseNoPayGroupOrderTask closeNoPayGroupOrderTask;
	
	@Resource(name = "orderReceiveChangeTask")
	private OrderReceiveChangeTask orderReceiveChangeTask;
	
	@Resource(name = "unLockOverTimeOrderTask")
	private UnLockOverTimeOrderTask unLockOverTimeOrderTask;
	
	@Resource
	private GotOrderNoticeTask gotOrderNoticeTask;
	
	@Resource
	private RiderDistTask riderDistTask;
	
	@Resource
	private AutoReceiptTask autoReceiptTask;
	
	@Resource
	private OrderDistributeOutTask orderDistributeOutTask;

	@Resource
	private Express100Service express100Service;

	@Resource
	private CompanyPayPeriodTask companyPayPeriodTask;

	@Resource
	private OrderShipReceiveTask orderShipReceiveTask;

	@Resource
	private OrderGroupBuyTask orderGroupBuyTask;

    /**
     * 处理订单任务
     * @param para
     */
	@Override
	public void processTask(Map<String, String> para) {
		String type = para.get("jobType")== null ? "" : para.get("jobType");
		Long beginTime = System.currentTimeMillis();
		logger.debug(">>>>TaskServiceCall.Thread.begin,param::" + para);
		try {
			if (StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_CLOSE_NOPAY_BANGGO_ORDER, type)) {
				//平台超时未付款订单取消
				taskManagerFactory.processTask(closeNoPayBanggoOrderTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_CLOSE_NOPAY_GROUP_ORDER, type)) {
				//平台团购订单未付款自动取消
				taskManagerFactory.processTask(closeNoPayGroupOrderTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_ORDER_RECEIVE_CHANGE, type)) {
				//全渠道已发货14天变更收货状态
				taskManagerFactory.processTask(orderReceiveChangeTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_UNLOCK_OVERTIME_ORDER, type)) {
				//解锁平台超时锁定订单
				taskManagerFactory.processTask(unLockOverTimeOrderTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_GOTORDER_NOTICE_TASK, type)) {
				// 自提订单定时发送提醒短信
				taskManagerFactory.processTask(gotOrderNoticeTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_RIDER_DIST_TASK, type)) {
				// 起手配送定时任务
				taskManagerFactory.processTask(riderDistTask);
			} else if(StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_AUTO_RECEIPT_TASK, type)) {
				// 起手配送自动签收定时任务
				taskManagerFactory.processTask(autoReceiptTask);
			} else if (StringUtils.equalsIgnoreCase(ConstantTask.TASK_JOB_TYPE_ORDER_DISTRIBUTE_OUT_TASK, type)) {
				// 订单配送出库定时任务
				taskManagerFactory.processTask(orderDistributeOutTask);
			} else if (ConstantTask.TASK_JOB_TYPE_ORDER_EXPRESS_TASK.equals(type)) {
				// 快递100物流信息抓取
				express100Service.express();
			} else if (ConstantTask.TASK_JOB_TYPE_COMPANY_PAY_TASK.equals(type)) {
				// 公司账期支付扣款
				taskManagerFactory.processTask(companyPayPeriodTask);
			} else if (ConstantTask.TASK_JOB_TYPE_ORDER_SHIP_RECEIVE_TASK.equals(type)) {
				// 订单发货超时签收
				taskManagerFactory.processTask(orderShipReceiveTask);
			} else if (ConstantTask.TASK_JOB_TYPE_ORDER_GROUP_BUY_TASK.equals(type)) {
				// 团购订单未按时支付尾款，取消订单
				taskManagerFactory.processTask(orderGroupBuyTask);
			} else{
				throw new RuntimeException(type + "无法解析任务类型");
			}
		} catch (Exception e) {
			logger.error("processTask.exception:"+e.getMessage() ,e);
		}
		logger.debug(">>>>TaskServiceCall.Thread.end,param::"+para+",time:"+(System.currentTimeMillis() - beginTime));
	}

}
