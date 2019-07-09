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
import com.work.shop.oms.bean.SystemConfig;
import com.work.shop.oms.bean.SystemConfigExample;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.SystemConfigMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 完结订单下单时间超过三个月移入历史表
 * @author huangl
 *
 */
@Service("copy2HistoryOrderTask")
public class Copy2HistoryOrderTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(Copy2HistoryOrderTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource
	private SystemConfigMapper systemConfigMapper;
	
	@Resource
	private OrderDistributeOpService orderDistributeOpService;
	
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_COPY_2_HISTORY_ORDER;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
		logger.debug("queryServiceData.dataLimit:" + dataLimit);
		
		//从系统配置中获取指定的时间段
		String delayDefault = "3";
	    SystemConfigExample systemConfigExample = new SystemConfigExample();
	    systemConfigExample.or().andCodeEqualTo("task_copy2history_Order");
	    List<SystemConfig> systemConfigList = systemConfigMapper.selectByExampleWithBLOBs(systemConfigExample);
	    if(CollectionUtils.isNotEmpty(systemConfigList)){
	    	if (StringUtil.isNotEmpty(systemConfigList.get(0).getValue())) {
	    		delayDefault = systemConfigList.get(0).getValue();
	    	}
	    }
	    Map<String,Object> param = new HashMap<String,Object>();
	    param.put("delay", delayDefault);//3个月
	    param.put("num", dataLimit);
	    List<String> orderInfos = orderDistributeDefineMapper.getFinishOrderThreeMonth(param);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		if(CollectionUtils.isNotEmpty(orderInfos)){
			for (String orderSn : orderInfos) {
				BaseTask obj = new BaseTask();
				obj.setOrderSn(orderSn);
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
		ReturnInfo resultMessage = null;
		try {
			resultMessage = orderDistributeOpService.moveOrderFromRecentToHistory(orderSn);
			logger.debug("executeTask.orderSn:"+orderSn+",resultMessage:"+JSON.toJSONString(resultMessage));
			if(resultMessage.getIsOk() == Constant.OS_YES){
				returnTask.setIsOk(Constant.OS_YES);
				returnTask.setMsg("完结订单移入历史表成功："+resultMessage.getMessage());
			}else{
				returnTask.setMsg("完结订单移入历史表失败："+resultMessage.getMessage());
			}
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		} catch (Exception e) {
			logger.debug("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setMsg("完结订单移入历史表异常："+resultMessage.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		}
		return returnTask;
	}

}
