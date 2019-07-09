package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.OrderPeriodDetailExample;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.bean.OrderRiderDistributeLogExample;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderPeriodDetailMapper;
import com.work.shop.oms.dao.OrderRiderDistributeLogMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

@Service("autoReceiptTask")
public class AutoReceiptTask extends ATaskServiceProcess {

	private static Logger logger = LoggerFactory.getLogger(AutoReceiptTask.class);
	@Resource
	private OrderRiderDistributeLogMapper orderRiderDistributeLogMapper;
	@Resource
	private OrderPeriodDetailMapper orderPeriodDetailMapper;

	@Resource(name="bgOrderInfoService")
	private BGOrderInfoService bGOrderInfoService;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_AUTO_RECEIPT_TASK;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
		logger.debug(taskName + "queryServiceData.dataLimit:" + dataLimit);
		OrderPeriodDetailExample detailExample = new OrderPeriodDetailExample();
		detailExample.or().andPeriodIdEqualTo("6805").andFlagEqualTo(1);
		List<OrderPeriodDetail> details = orderPeriodDetailMapper.selectByExample(detailExample);
		if (StringUtil.isListNull(details)) {
			return null;
		}
		long periodValue = details.get(0).getPeriodValue() * 1000;
		long curr = System.currentTimeMillis();
		Date startDate = new Date(curr - periodValue);
		OrderRiderDistributeLogExample logExample = new OrderRiderDistributeLogExample();
		logExample.or().andFinishTimeLessThanOrEqualTo(startDate).
			andOrderStatusEqualTo((short) Constant.OI_ORDER_DIS_DONE).
			andIsSysReceiptEqualTo(0);
		List<OrderRiderDistributeLog> distributeLogs = orderRiderDistributeLogMapper.selectByExample(logExample);
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		
		if (CollectionUtils.isNotEmpty(distributeLogs)) {
			for (OrderRiderDistributeLog item : distributeLogs) {
				MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(item.getMasterOrderSn());
				if (master == null) {
					continue;
				}
				// 已签收
				if (master.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
					OrderRiderDistributeLogExample distributeLogExample = new OrderRiderDistributeLogExample();
					distributeLogExample.or().andMasterOrderSnEqualTo(item.getMasterOrderSn());
					OrderRiderDistributeLog distributeLog = new OrderRiderDistributeLog();
					distributeLog.setIsSysReceipt(2);
					orderRiderDistributeLogMapper.updateByExampleSelective(distributeLog, logExample);
					continue;
				}
				BaseTask baseTask = new BaseTask();
				baseTask.setOrderSn(item.getMasterOrderSn());
				taskDatas.add(baseTask);
			}
		}
		return taskDatas;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.info("executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		ReturnInfo changeResult = null;
		try {
			changeResult = bGOrderInfoService.confirmReceipt(orderSn, null, Constant.OS_STRING_SYSTEM, "");
			if (changeResult != null && changeResult.getIsOk() == Constant.OS_YES) {
				OrderRiderDistributeLogExample logExample = new OrderRiderDistributeLogExample();
				logExample.or().andMasterOrderSnEqualTo(orderSn);
				OrderRiderDistributeLog distributeLog = new OrderRiderDistributeLog();
				distributeLog.setIsSysReceipt(1);
				orderRiderDistributeLogMapper.updateByExampleSelective(distributeLog, logExample);
			}
			logger.debug("executeTask.orderSn:" + orderSn + ",changeResult:" + JSON.toJSONString(changeResult));
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("订单签收超时自动签收：" + changeResult.getMessage());
			returnTask.setResponse(JSON.toJSONString(changeResult));
		} catch (Exception e) {
			logger.error("executeTask.orderSn:" + orderSn + " 错误信息:" + e.getMessage(), e);
			returnTask.setMsg("订单签收超时自动签收异常：" + changeResult.getMessage());
			returnTask.setResponse(JSON.toJSONString(changeResult));
		}
		return returnTask;
	}
	
	public static void main(String[] args) {
		long aa = System.currentTimeMillis();
		
		Date bb = new Date(aa - 7200 * 1000);
		
		System.out.println(TimeUtil.formatDate(bb));
	}
}
