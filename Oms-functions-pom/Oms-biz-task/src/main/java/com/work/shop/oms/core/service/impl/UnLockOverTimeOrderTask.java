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
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.Constant;

/**
 * 解锁平台超时锁定订单
 * @author huangl
 *
 */
@Service("unLockOverTimeOrderTask")
public class UnLockOverTimeOrderTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(UnLockOverTimeOrderTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource(name="orderDistributeOpService")
	private OrderDistributeOpService orderDistributeOpService;
	@Resource
	private ChannelShopService channelShopService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_UNLOCK_OVERTIME_ORDER;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
		logger.debug("queryServiceData.dataLimit:" + dataLimit);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("num", dataLimit);
		List<String> orderInfos = orderDistributeDefineMapper.getLockOrderOverTimeOfBanggo(param);
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
		ReturnInfo resultBean = null;
		try {
			resultBean = orderDistributeOpService.unLockOrder(orderSn, new OrderStatus("定时任务解锁", Constant.OS_STRING_SYSTEM));
			logger.debug("executeTask.orderSn:"+orderSn+",ReturnInfo:"+JSON.toJSONString(resultBean));
			if(resultBean.getIsOk() > 0){
				returnTask.setIsOk(Constant.OS_YES);
				returnTask.setMsg("解锁成功");
			}else{
				returnTask.setMsg("解锁失败："+resultBean.getMessage());
				returnTask.setResponse(JSON.toJSONString(resultBean));
			}
		} catch (Exception e) {
			logger.error("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setMsg("解锁异常："+resultBean.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultBean));
		}
		return returnTask;
	}

}
