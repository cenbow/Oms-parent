package com.work.shop.oms.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderInfoSearchMapper;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 公司账期支付
 * @author QuYachu
 */
@Service("companyPayPeriodTask")
public class CompanyPayPeriodTask extends ATaskServiceProcess {

	private static Logger logger = LoggerFactory.getLogger(CompanyPayPeriodTask.class);

	@Resource
	private OrderInfoSearchMapper orderInfoSearchMapper;

	@Resource
	private MasterOrderInfoService masterOrderInfoService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_COMPANY_PAY_TASK;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {

		List<BaseTask> taskDataList = new ArrayList<BaseTask>();

		Map<String, Object> queryMap = new HashMap<String, Object>(2);
		queryMap.put("dateTime", TimeUtil.getDate(TimeUtil.YYYY_MM_DD_HH_MM_SS));
		List<OrderAccountPeriod> list = orderInfoSearchMapper.selectCompanyPayPeriodList(queryMap);
		if (list != null && list.size() > 0) {
			for (OrderAccountPeriod orderAccountPeriod : list) {
				masterOrderInfoService.processOrderPayPeriod(orderAccountPeriod);
			}
		}

		// 查询订单发货单
		List<OrderAccountPeriod> orderDepotShipList = orderInfoSearchMapper.selectCompanyPayPeriodListByOrderDepotShip(queryMap);
		if (orderDepotShipList != null && orderDepotShipList.size() > 0) {
			for (OrderAccountPeriod orderAccountPeriod : orderDepotShipList) {
                orderAccountPeriod.setOrderType(1);
				masterOrderInfoService.processOrderPayPeriod(orderAccountPeriod);
			}
		}

		return taskDataList;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.info(taskName + "executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		try {
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("公司账期支付扣款下发成功");
		} catch (Exception e) {
			logger.error("公司账期支付扣款" + orderSn + " ,错误信息:" + e.getMessage(), e);
			returnTask.setMsg("公司账期支付扣款下发异常：" + e.getMessage());
		}
		return returnTask;
	}
}
