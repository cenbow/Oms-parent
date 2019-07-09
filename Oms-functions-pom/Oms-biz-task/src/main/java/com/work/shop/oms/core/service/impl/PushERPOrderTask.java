package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.utils.Constant;

/**
 * 下发未下发订单
 * @author
 *
 */

@Service("pushERPOrderTask")
public class PushERPOrderTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(PushERPOrderTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;

	@Resource
	private DistributeSupplierService distributeSupplierService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_PUSHERP_ORDER;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
		logger.debug(taskName + "queryServiceData.dataLimit:" + dataLimit);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("num", dataLimit);
		List<String> orderSns = orderDistributeDefineMapper.getPushERPOrder(param);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		if(CollectionUtils.isNotEmpty(orderSns)){
			for (String orderSn : orderSns) {
				BaseTask obj = new BaseTask();
				obj.setOrderSn(orderSn);
				taskDatas.add(obj);
			}
		}
		return taskDatas;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.debug(taskName + "executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		try {
			// 调用下发接口
			distributeSupplierService.distribute(orderSn);
			logger.debug("executeTask.orderSns:" + orderSn);
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("放入下发ERP队列成功");
		} catch (Exception e) {
			logger.debug("executeTask.orderSn:"+ orderSn +" 错误信息:",e);
			returnTask.setMsg("批量下发ERP异常：" + e.getMessage());
		}
		return returnTask;
	}
}
