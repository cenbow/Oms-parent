package com.work.shop.oms.api.express.service;

import com.work.shop.oms.bean.OrderExpressCompany;

public interface OrderExpressCompanyService {
	
	
	
	public OrderExpressCompany selectCompanyByCode(String companyCode);
	
	
	public OrderExpressCompany selectCompanyById(Integer companyId);
}
