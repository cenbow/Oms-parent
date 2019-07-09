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
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.utils.Constant;

/**
 * 平台团购订单未付款自动取消
 * @author huangl
 *
 */
@Service("closeNoPayGroupOrderTask")
public class CloseNoPayGroupOrderTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(CloseNoPayGroupOrderTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource
	private OrderCommonService orderCommonService;

	@Resource
	private ChannelShopService channelShopService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_CLOSE_NOPAY_GROUP_ORDER;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
		logger.debug("queryServiceData.dataLimit:" + dataLimit);
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("num", dataLimit);
		List<String> list = channelShopService.getShopCodesByChannelCode("");
		if (!StringUtil.isListNotNull(list)) {
			list = new ArrayList<String>();
			list.add("HQ01S116");
		}
		param.put("shopCodes", list);
		List<String> orderInfos = orderDistributeDefineMapper.getGroupOrderNoPayClose(param);
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
		ReturnInfo resultMessage = null;
		try {
			OrderStatus param = new OrderStatus();
			param.setMessage("团购订单支付超时关闭");
			param.setAdminUser(Constant.OS_STRING_SYSTEM);
			param.setSource(ConstantValues.METHOD_SOURCE_TYPE.OMS);
			param.setCode("8024"); // 订单付款超时取消
			param.setMasterOrderSn(orderSn);
			param.setType(ConstantValues.CREATE_RETURN.YES);
			resultMessage = orderCommonService.cancelOrderByMasterSn(orderSn, param);
			logger.debug("executeTask.orderSn:"+orderSn+",resultMessage:"+JSON.toJSONString(resultMessage));
			
			returnTask.setIsOk(ConstantValues.YESORNO_YES);
			returnTask.setMsg("团购订单支付超时关闭："+resultMessage.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		} catch (Exception e) {
			logger.debug("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setIsOk(ConstantValues.YESORNO_NO);
			returnTask.setMsg("团购订单支付超时关闭异常："+resultMessage.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		}
		return returnTask;
	}

}
