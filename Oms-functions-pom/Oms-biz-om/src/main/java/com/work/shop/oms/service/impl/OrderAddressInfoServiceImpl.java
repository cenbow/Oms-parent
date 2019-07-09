package com.work.shop.oms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.OrderAddressInfo;
import com.work.shop.oms.bean.OrderAddressInfoExample;
import com.work.shop.oms.dao.OrderAddressInfoMapper;
import com.work.shop.oms.service.OrderAddressInfoService;

@Service
public class OrderAddressInfoServiceImpl implements OrderAddressInfoService {
	
	@Resource
	private OrderAddressInfoMapper orderAddressInfoMapper;
	
	@Override
	public OrderAddressInfo findOrderShipByOrderSn(String orderSn) {
		OrderAddressInfoExample orderAddressInfoExample=new OrderAddressInfoExample();
		orderAddressInfoExample.or().andOrderSnEqualTo(orderSn);
		List<OrderAddressInfo>list=orderAddressInfoMapper.selectByExample(orderAddressInfoExample);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
