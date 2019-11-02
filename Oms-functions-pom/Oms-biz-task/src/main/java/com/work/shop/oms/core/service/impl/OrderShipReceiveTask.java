package com.work.shop.oms.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderAccountPeriod;
import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.OrderPeriodDetailExample;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.OrderPeriodDetailMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.dao.define.OrderInfoSearchMapper;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.MqConfig;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 订单发货单超时自动签收
 * @author QuYachu
 */
@Service("orderShipReceiveTask")
public class OrderShipReceiveTask extends ATaskServiceProcess {

	private static Logger logger = LoggerFactory.getLogger(OrderShipReceiveTask.class);

	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;

	@Resource
	private OrderPeriodDetailMapper orderPeriodDetailMapper;

    @Resource
    private JmsSendQueueService jmsSendQueueService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_ORDER_SHIP_RECEIVE_TASK;
		super.initTaskConfig();
	}

	/**
	 * 数据查询
	 * @param orderIdList
	 * @param dataLimit
	 * @return List<BaseTask>
	 */
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {

        OrderPeriodDetailExample detailExample = new OrderPeriodDetailExample();
        detailExample.or().andPeriodIdEqualTo("6807").andFlagEqualTo(1);
        List<OrderPeriodDetail> details = orderPeriodDetailMapper.selectByExample(detailExample);
        if (StringUtil.isListNull(details)) {
            return null;
        }

        OrderPeriodDetail orderPeriodDetail = details.get(0);
        int periodValue = orderPeriodDetail.getPeriodValue();

		Date date = new Date();
        Date dayTime = TimeUtil.getBeforeSecond(date, periodValue * -1);
		Map<String, Object> queryMap = new HashMap<String, Object>(2);
		queryMap.put("dayTime", dayTime);
		queryMap.put("siteCode", "hbis");
		queryMap.put("limit", 500);
		List<String> orderDistributionList = orderDistributeDefineMapper.getShipTimeoutOrderByOrder(queryMap);

		List<BaseTask> taskDataList = new ArrayList<BaseTask>();
		if (orderDistributionList == null || orderDistributionList.size() == 0) {
			return taskDataList;
		}


		// 下发mq, 处理订单交货单自动收货情况
		for (String orderSn : orderDistributionList) {
            DistributeShippingBean distributeShippingBean = new DistributeShippingBean();
            // 订单编码
            distributeShippingBean.setOrderSn(orderSn);
            // 周期
            distributeShippingBean.setPeriodTime(periodValue);
            jmsSendQueueService.sendQueueMessage(MqConfig.order_sn_receive, JSON.toJSONString(distributeShippingBean));
		}

		return taskDataList;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {

		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_YES);
		returnTask.setMsg("成功");

		return returnTask;
	}
}
