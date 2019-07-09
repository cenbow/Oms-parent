package com.work.shop.oms.config.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderCustomDefineExample;
import com.work.shop.oms.config.service.OrderCustomDefineService;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderCustomDefineServiceImpl implements OrderCustomDefineService{

	@Resource
	OrderCustomDefineMapper orderCustomDefineMapper;
	
	@Override
	public List<OrderCustomDefine> selectDefines(Short type) {
		OrderCustomDefineExample example = new OrderCustomDefineExample();
		OrderCustomDefineExample.Criteria criteria = example.or();
		if (type != null) {
			criteria.andTypeEqualTo(type);
		}
		return orderCustomDefineMapper.selectByExample(example);
	}

	@Override
	public List<OrderCustomDefine> selectCustomDefine(OrderCustomDefine orderCustomDefine) {
		OrderCustomDefineExample example = new OrderCustomDefineExample();
		OrderCustomDefineExample.Criteria criteria = example.or();
		
		if(null != orderCustomDefine.getType()){
			criteria.andTypeEqualTo(orderCustomDefine.getType());
		}
		
		if(null != orderCustomDefine.getDisplay()){
			criteria.andDisplayEqualTo(orderCustomDefine.getDisplay());
		}
		
	//	example.or().andTypeEqualTo(orderCustomDefine.getType()).andDisplayEqualTo(orderCustomDefine.getDisplay());
	//	example.or().andDisplayEqualTo(orderCustomDefine.getDisplay());
		return orderCustomDefineMapper.selectByExample(example);
	}
	
	@Override
	public OrderCustomDefine selectCustomDefineByCode(String code) {
		if(StringUtil.isTrimEmpty(code)){
			return null;
		}
		return orderCustomDefineMapper.selectByPrimaryKey(code);
	}
}
