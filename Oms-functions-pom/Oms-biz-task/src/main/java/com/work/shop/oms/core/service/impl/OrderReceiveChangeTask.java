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
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.utils.Constant;

/**
 * 全渠道已发货14天变更收货状态
 * @author huangl
 *
 */
@Service("orderReceiveChangeTask")
public class OrderReceiveChangeTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(OrderReceiveChangeTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource(name="bgOrderInfoService")
	private BGOrderInfoService bGOrderInfoService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_ORDER_RECEIVE_CHANGE;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
		logger.debug("queryServiceData.dataLimit:" + dataLimit);
		//全渠道订单发货14天待更新收货状态数据
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("delay", 14);
		param.put("num", dataLimit);
		List<String> orderInfos = orderDistributeDefineMapper.getShipTimeoutOrder(param);
		logger.debug("queryServiceData.processsQuery.size:" + CollectionUtils.size(orderInfos));
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		if(CollectionUtils.isNotEmpty(orderInfos)){
			for (String masterOrderSn : orderInfos) {
				BaseTask obj = new BaseTask();
				obj.setOrderSn(masterOrderSn);
				taskDatas.add(obj);
			}
		}
		return taskDatas;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.debug("executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		ReturnInfo changeResult = null;
		try {
			changeResult = bGOrderInfoService.confirmReceipt(orderSn, null, Constant.OS_STRING_SYSTEM, "");
			logger.debug("executeTask.orderSn:"+orderSn+",changeResult:"+JSON.toJSONString(changeResult));
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("订单发货超时更新收货状态："+changeResult.getMessage());
			returnTask.setResponse(JSON.toJSONString(changeResult));
		} catch (Exception e) {
			logger.error("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setMsg("订单发货超时更新收货状态异常："+changeResult.getMessage());
			returnTask.setResponse(JSON.toJSONString(changeResult));
		}
		return returnTask;
	}

}
