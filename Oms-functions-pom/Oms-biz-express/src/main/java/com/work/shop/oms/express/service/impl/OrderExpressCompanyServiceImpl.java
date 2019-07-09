package com.work.shop.oms.express.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.express.service.OrderExpressCompanyService;
import com.work.shop.oms.bean.OrderExpressCompany;
import com.work.shop.oms.bean.OrderExpressCompanyExample;
import com.work.shop.oms.dao.OrderExpressCompanyMapper;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderExpressCompanyServiceImpl implements OrderExpressCompanyService{

	private Logger logger = Logger.getLogger(OrderExpressCompanyServiceImpl.class);
	
	@Resource
	private OrderExpressCompanyMapper orderExpressCompanyMapper;
	
	@Override
	public OrderExpressCompany selectCompanyByCode(String companyCode) {
		OrderExpressCompanyExample companyExample = new OrderExpressCompanyExample();
		companyExample.or().andCompanyCodeEqualTo(companyCode);
		List<OrderExpressCompany> list = orderExpressCompanyMapper.selectByExample(companyExample);
		if (StringUtil.isListNull(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public OrderExpressCompany selectCompanyById(Integer companyId) {
		return orderExpressCompanyMapper.selectByPrimaryKey(companyId);
	}
}
