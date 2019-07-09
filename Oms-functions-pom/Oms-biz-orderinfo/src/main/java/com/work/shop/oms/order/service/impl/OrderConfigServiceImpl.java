package com.work.shop.oms.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.OrderPeriodInfo;
import com.work.shop.oms.config.service.OrderPeriodDetailService;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.service.OrderConfigService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;

/**
 * 订单配置管理
 * @author QuYachu
 *
 */
@Service("orderConfigService")
public class OrderConfigServiceImpl implements OrderConfigService {
	
	private Logger logger = Logger.getLogger(OrderConfigServiceImpl.class);

	@Resource
	private OrderPeriodDetailService orderPeriodDetailService;
	
	@Resource
	private RedisClient redisClient;
	
	/**
	 * 获取订单周期明细列表
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	@Override
	public OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfoList() {
		OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
		returnBean.setSuccess(false);
		returnBean.setMessage("查询失败");
		
		try {
			List<OrderPeriodDetail> list = orderPeriodDetailService.selectList(1);
			
			List<OrderPeriodInfo> returnList = new ArrayList<OrderPeriodInfo>();
			if (list != null && list.size() > 0) {
				for (OrderPeriodDetail model : list) {
					OrderPeriodInfo orderPeriodInfo = new OrderPeriodInfo();
					cloneObj(orderPeriodInfo, model);
					returnList.add(orderPeriodInfo);
				}
			}
			returnBean.setSuccess(true);
			returnBean.setMessage("查询成功");
			returnBean.setList(returnList);
		} catch (Exception e) {
			logger.error("查询订单周期明细列表异常", e);
			returnBean.setMessage("查询异常:" + e.getMessage());
		}
		return returnBean;
	}

	/**
	 * 获取订单周期明细
	 * @param request 参数
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	@Override
	public OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request) {
		OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
		returnBean.setSuccess(false);
		returnBean.setMessage("查询失败");
		
		if (request == null) {
			returnBean.setMessage("参数为空");
			return returnBean;
		}
		
		OrderPeriodInfo orderPeriodInfo = request.getData();
		if (orderPeriodInfo == null) {
			returnBean.setMessage("数据为空");
			return returnBean;
		}
		if (orderPeriodInfo.getPeriodSeries() == null) {
			returnBean.setMessage("周期系列id为空");
			return returnBean;
		}
		if (StringUtils.isBlank(orderPeriodInfo.getPeriodId())) {
			returnBean.setMessage("周期id为空");
			return returnBean;
		}
		
		try {
			OrderPeriodDetail orderPeriodDetail = orderPeriodDetailService.selectByPeriodAndType(orderPeriodInfo.getPeriodSeries(), orderPeriodInfo.getPeriodId());
			
			OrderPeriodInfo returnModel = new OrderPeriodInfo();
			cloneObj(returnModel, orderPeriodDetail);
			
			returnBean.setSuccess(true);
			returnBean.setMessage("查询成功");
			returnBean.setData(returnModel);
		} catch (Exception e) {
			logger.error("查询订单周期明细列表异常", e);
			returnBean.setMessage("查询异常:" + e.getMessage());
		}
		return returnBean;
	}

	/**
	 * 更新订单周期明细
	 * @param request 参数
	 * @return OmsBaseResponse<OrderPeriodInfo>
	 */
	@Override
	public OmsBaseResponse<OrderPeriodInfo> updateOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request) {
		OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
		returnBean.setSuccess(false);
		returnBean.setMessage("更新失败");
		
		if (request == null) {
			returnBean.setMessage("参数为空");
			return returnBean;
		}
		
		OrderPeriodInfo orderPeriodInfo = request.getData();
		if (orderPeriodInfo == null) {
			returnBean.setMessage("数据为空");
			return returnBean;
		}
		if (orderPeriodInfo.getPeriodSeries() == null) {
			returnBean.setMessage("周期系列id为空");
			return returnBean;
		}
		if (StringUtils.isBlank(orderPeriodInfo.getPeriodId())) {
			returnBean.setMessage("周期id为空");
			return returnBean;
		}
		if (orderPeriodInfo.getPeriodValue() == null) {
			returnBean.setMessage("周期值为空");
			return returnBean;
		}
		try {
			OrderPeriodDetail model = new OrderPeriodDetail();
			model.setPeriodSeries(orderPeriodInfo.getPeriodSeries());
			model.setPeriodId(orderPeriodInfo.getPeriodId());
			model.setPeriodValue(orderPeriodInfo.getPeriodValue());
			int result = orderPeriodDetailService.updateOrderPeriodDetail(model);
			if (result > 0) {
				returnBean.setSuccess(true);
				returnBean.setMessage("更新成功");
				
				// 付款时间
				if (Constant.OS_PAYMENT_PERIOD.equals(orderPeriodInfo.getPeriodId())) {
					int pe = 1;
					try {
						redisClient.set("OS_PAYMENT_PERIOD_" + pe, orderPeriodInfo.getPeriodValue() + "");
					} catch (Throwable e) {
						logger.error("redis is null error ," + e);
					}
				} else {
					try {
						redisClient.set(Constant.OS_ORDER_PERIOD + orderPeriodInfo.getPeriodId(), orderPeriodInfo.getPeriodValue() + "");
					} catch (Throwable e) {
						logger.error("redis is null error ," + e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("更新订单周期明细异常", e);
			returnBean.setMessage("更新异常:" + e.getMessage());
		}
		
		return returnBean;
	}
	
	private void cloneObj(Object destObj, Object origObj) {
		try {
			BeanUtils.copyProperties(destObj, origObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
