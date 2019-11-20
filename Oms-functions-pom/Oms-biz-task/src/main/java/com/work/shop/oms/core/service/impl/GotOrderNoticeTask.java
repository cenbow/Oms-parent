package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.cart.api.StoreInfoCartApi;
import com.work.shop.cart.api.bean.StoreInfoBean;
import com.work.shop.cart.api.bean.service.ShopCartRequest;
import com.work.shop.cart.api.bean.service.ShopCartResponse;
import com.work.shop.cart.api.bean.service.StoreInfoRequest;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
@Service("gotOrderNoticeTask")
public class GotOrderNoticeTask extends ATaskServiceProcess{

	private static Logger logger = LoggerFactory.getLogger(GotOrderNoticeTask.class);
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;

	//@Resource
	private StoreInfoCartApi storeInfoCartAPI;

	@Resource(name = "sendGotCodeJmsTemplate")
	private JmsTemplate sendGotCodeJmsTemplate;
	
	private static Map<String, String> storeInfoMap = new HashMap<String, String>();

	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_GOTORDER_NOTICE_TASK;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
		logger.debug(taskName + "queryServiceData.dataLimit:" + dataLimit);
		Map<String, Object> params = new HashMap<String, Object>();
		List<OrderItem> orderItems = orderDistributeDefineMapper.getGotOrder(params);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		if(CollectionUtils.isNotEmpty(orderItems)){
			ShopCartRequest<StoreInfoRequest> request = new ShopCartRequest<StoreInfoRequest>();
			logger.info("查询渠道店铺信息:request" + JSON.toJSONString(request));
			ShopCartResponse<List<StoreInfoBean>> response = storeInfoCartAPI.getStoreInfoList(request);
			logger.info("查询渠道店铺信息:response" + JSON.toJSONString(response));
			if (response == null) {
				logger.error("查询渠道店铺信息失败：返回结果为空");
				return null;
			}
			if (StringUtil.isListNull(response.getData())) {
				logger.error("查询渠道店铺信息失败：店铺数据为空" + response.getMsg());
				return null;
			}
			storeInfoMap = new HashMap<String, String>();
			for (StoreInfoBean bean : response.getData()) {
				storeInfoMap.put(bean.getStoreCode(), bean.getAddress());
			}
			for (OrderItem orderItem : orderItems) {
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
				orderStatus.setMasterOrderSn(orderItem.getMasterOrderSn());
				String storeAddress = storeInfoMap.get(orderItem.getStoreCode());
				orderStatus.setStoreAddress(storeAddress);
				logger.info("发送短信内容写入MQ" + JSON.toJSONString(orderStatus));
				sendGotCodeJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatus)));
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
