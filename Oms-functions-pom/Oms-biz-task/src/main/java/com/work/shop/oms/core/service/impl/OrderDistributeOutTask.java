package com.work.shop.oms.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.dao.define.DefineOrderMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.order.service.OrderQueryService;
import com.work.shop.oms.utils.Constant;

/**
 * 订单配送出库
 * @author QuYachu
 *
 */
@Service("orderDistributeOutTask")
public class OrderDistributeOutTask extends ATaskServiceProcess {
	
	private static Logger logger = LoggerFactory.getLogger(OrderDistributeOutTask.class);
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource
	private DefineOrderMapper defineOrderMapper;
	
	@Resource(name="orderDistributeProcessJmsTemplate")
	private JmsTemplate orderDistributeProcessJmsTemplate;
	
	@Resource
	private OrderQueryService orderQueryService;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_ORDER_DISTRIBUTE_OUT_TASK;
		super.initTaskConfig();
	}

	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("siteCode", "WBC");
		queryMap.put("depotCode", "XYC");
		
		orderQueryService.getOrderDistributeOut(queryMap);
		
		/*String distTime = TimeUtil.format2Date(new Date());
		queryMap.put("distTime", distTime);
		queryMap.put("shipStatus", 0); // 未发货
		queryMap.put("gotStatus", 0); // 未处理
		int count = orderDistributeDefineMapper.getOrderDistributeOutCount(queryMap);
		
		if (count > 0) {
			Map<String, String> addressMap = new HashMap<String, String>();
			int limit = 1000;
			int start = 0;
			while (start < count) {
				queryMap.put("start", start);
				queryMap.put("limit", limit);
				List<OrderItem> orderList = orderDistributeDefineMapper.getOrderDistributeOutList(queryMap);
				
				if (orderList != null && orderList.size() > 0) {
					for (OrderItem orderItem : orderList) {
						processOrderDistributeOut(orderItem, addressMap);
					}
				}
				
				start += limit;
			}
		}*/
		
		return null;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		logger.info(taskName + "executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(Constant.OS_NO);
		try {
			returnTask.setIsOk(Constant.OS_YES);
			returnTask.setMsg("订单配送出库成功");
		} catch (Exception e) {
			logger.error("订单配送出库" + orderSn + " ,错误信息:" + e.getMessage(), e);
			returnTask.setMsg("订单配送出库异常：" + e.getMessage());
		}
		return returnTask;
	}
	
	/**
	 * 处理订单配送单出库
	 * @param orderItem
	 */
	/*private void processOrderDistributeOut(OrderItem orderItem, Map<String, String> addressMap) {
		
		if (orderItem == null) {
			return;
		}
		
		// 配送单编码
		String orderSn = orderItem.getOuterOrderSn();
		
		try {
			OrderOutBean orderOutBean = new OrderOutBean();
			orderOutBean.setSiteCode(orderItem.getChannelCode());
			orderOutBean.setOrderSn(orderItem.getMasterOrderSn());
			orderOutBean.setOrderDistribution(orderSn);
			orderOutBean.setRepName(orderItem.getReceiverName());
			orderOutBean.setMobile(orderItem.getReceiverMobile());
			orderOutBean.setAddress(orderItem.getAddress());
			
			String province = orderItem.getProvince();
			if (StringUtils.isNotBlank(province)) {
				String provinceName = addressMap.get(province);
					if (StringUtils.isBlank(provinceName)) {
					// 省、市
					SystemRegionArea provinceArea = systemRegionAreaMapper.selectByPrimaryKey(province);
					if (provinceArea != null) {
						provinceName = provinceArea.getRegionName();
						addressMap.put(province, provinceName);
					}
				}
				orderOutBean.setProvince(provinceName);
			}
			
			String city = orderItem.getCity();
			if (StringUtils.isNotBlank(city)) {
				String cityName = addressMap.get(city);
					if (StringUtils.isBlank(cityName)) {
					// 省、市
					SystemRegionArea cityArea = systemRegionAreaMapper.selectByPrimaryKey(city);
					if (cityArea != null) {
						cityName = cityArea.getRegionName();
						addressMap.put(city, cityName);
					}
				}
				orderOutBean.setCity(cityName);
			}
			
			// 获取配送商品列表
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("masterOrderSn", orderItem.getMasterOrderSn());
			queryMap.put("orderSn", orderSn);
			String depotCode = orderItem.getDepotCode();
			if (StringUtils.isNotBlank(depotCode)) {
				queryMap.put("depotCode", depotCode);
			}
			List<MasterOrderGoods> goodsList = defineOrderMapper.getOrderDistributeGoods(queryMap);
			if (goodsList == null || goodsList.size() == 0) {
				return;
			}
			
			List<OutGoods> outGoodsList = new ArrayList<OutGoods>();
			for (MasterOrderGoods masterOrderGoods : goodsList) {
				OutGoods outGoods = new OutGoods();
				outGoods.setSku(masterOrderGoods.getCustomCode());
				outGoods.setAmount(masterOrderGoods.getGoodsNumber().intValue());
				outGoods.setBoxGauge(masterOrderGoods.getBoxGauge());
				outGoodsList.add(outGoods);
			}
			orderOutBean.setOutGoodsList(outGoodsList);
			
			// 下发MQ
			orderDistributeProcessJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(orderOutBean)));
			
			// 更新下发状态
			updateOrderDistribute(orderSn);
		} catch (Exception e) {
			logger.error("订单配送下发异常:" + JSONObject.toJSONString(orderItem), e);
		}
	}*/
	
	/**
	 * 更新配送状态出库下发
	 * @param orderSn
	 */
	/*private void updateOrderDistribute(String orderSn) {
		if (StringUtils.isBlank(orderSn)) {
			return;
		}
		OrderDistribute updateDistribute = new OrderDistribute();
		updateDistribute.setGotStatus(Constant.GOT_STATUS_YES);
		updateDistribute.setOrderSn(orderSn);
		updateDistribute.setUpdateTime(new Date());
		orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
	}*/

}
