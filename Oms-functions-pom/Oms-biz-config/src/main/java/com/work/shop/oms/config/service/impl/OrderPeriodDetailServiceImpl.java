package com.work.shop.oms.config.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.OrderPeriodDetailExample;
import com.work.shop.oms.config.service.OrderPeriodDetailService;
import com.work.shop.oms.dao.OrderPeriodDetailMapper;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderPeriodDetailServiceImpl implements OrderPeriodDetailService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	OrderPeriodDetailMapper orderPeriodDetailMapper;
	
	@Override
	public OrderPeriodDetail selectByPeriodAndType(int period, String type) {
		if (period == 0 || StringUtil.isEmpty(type)) {
			return null;
		}
		OrderPeriodDetailExample example = new OrderPeriodDetailExample();
		example.or().andPeriodSeriesEqualTo(period).andPeriodIdEqualTo(type);
		List<OrderPeriodDetail> list = orderPeriodDetailMapper.selectByExample(example);
		if (StringUtil.isListNotNull(list)) {
			return list.get(0);
		}
		return null;
	}
	
	public List<OrderPeriodDetail> selectList(int flag) {
		OrderPeriodDetailExample example = new OrderPeriodDetailExample();
		example.or().andFlagEqualTo(flag);
		List<OrderPeriodDetail> list = orderPeriodDetailMapper.selectByExample(example);
		return list;
	}
	
	/**
	 * 更新订单周期明细
	 * @param model
	 * @return
	 */
	public int updateOrderPeriodDetail(OrderPeriodDetail model) {
		return orderPeriodDetailMapper.updateByPrimaryKeySelective(model);
	}
}
