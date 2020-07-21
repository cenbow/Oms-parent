package com.work.shop.oms.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderInfoExtendExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ProductGroupBuy;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.product.service.ProductGroupBuyService;
import com.work.shop.oms.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台超时未付款订单取消
 * @author huangl
 *
 */
@Service("orderGroupBuyTask")
public class OrderGroupBuyTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(OrderGroupBuyTask.class);

	@Resource
	private ProductGroupBuyService productGroupBuyService;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private OrderCommonService orderCommonService;

	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_ORDER_GROUP_BUY_TASK;
		super.initTaskConfig();
	}
	
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
		logger.info("TASK_JOB_TYPE_ORDER_GROUP_BUY_TASK.dataLimit:" + dataLimit);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		//假设拿到了团购信息
		ReturnInfo returnInfo = productGroupBuyService.selectProductGroupBuy();
		List<ProductGroupBuy> productGroupBuyList = (List<ProductGroupBuy>)returnInfo.getData();
		if(productGroupBuyList == null || productGroupBuyList.size() == 0){
			return taskDatas;
		}
		List<Integer> groupIdList = new ArrayList<>();
		for (ProductGroupBuy productGroupBuy : productGroupBuyList) {
			groupIdList.add(productGroupBuy.getId());
		}
		//根据团购id跟状态订单
		MasterOrderInfoExtendExample example = new MasterOrderInfoExtendExample();
		example.createCriteria().andGroupIdIn(groupIdList);
		List<MasterOrderInfoExtend> masterOrderInfoExtends = masterOrderInfoExtendMapper.selectByExample(example);
		if(masterOrderInfoExtends == null || masterOrderInfoExtends.size() == 0){
			return taskDatas;
		}
		List<String> masterOrderSnList = new ArrayList<>();
		for (MasterOrderInfoExtend masterOrderInfoExtend : masterOrderInfoExtends) {
			masterOrderSnList.add(masterOrderInfoExtend.getMasterOrderSn());
		}
		MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
		masterOrderInfoExample.createCriteria().andMasterOrderSnIn(masterOrderSnList).andOrderStatusNotEqualTo(Byte.valueOf("2"));
		List<MasterOrderInfo> masterOrderInfosList = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
		if(masterOrderInfosList == null || masterOrderInfosList.size() == 0){
			return taskDatas;
		}
		for (MasterOrderInfo masterOrderInfo : masterOrderInfosList) {
			BaseTask obj = new BaseTask();
			obj.setOrderSn(masterOrderInfo.getMasterOrderSn());
			taskDatas.add(obj);
		}
		return taskDatas;
	}

	public ReturnTask executeTask(BaseTask obj) {
		logger.info("executeTask.BaseObj:" + JSON.toJSONString(obj));
		//订单号
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		ReturnInfo resultMessage = null;
		try {
			OrderStatus param = new OrderStatus();
			param.setMessage("团购订单未支付尾款取消，预付款不退");
			param.setAdminUser(Constant.OS_STRING_SYSTEM);
			param.setSource(ConstantValues.METHOD_SOURCE_TYPE.OMS);
			param.setCode("10001"); // 团购订单未支付尾款取消
			param.setOrderSn(orderSn);
			param.setType(ConstantValues.CREATE_RETURN.NO);
			resultMessage = orderCommonService.cancelOrderByMasterSn(orderSn, param);
			
			logger.info("executeTask.orderSn:"+orderSn+",resultMessage:"+JSON.toJSONString(resultMessage));
			
			returnTask.setIsOk(ConstantValues.YESORNO_YES);
			returnTask.setMsg("团购订单未支付尾款取消："+resultMessage.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		} catch (Exception e) {
			logger.error("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setIsOk(ConstantValues.YESORNO_NO);
			returnTask.setMsg("团购订单未支付尾款取消异常："+resultMessage.getMessage());
			returnTask.setResponse(JSON.toJSONString(resultMessage));
		}
		return returnTask;
	}
}
