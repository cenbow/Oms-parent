package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.OrderQuestionNewDetail;

public interface OrderQuestionNewDetailMapper {
	
	public List<OrderQuestionNewDetail> getOrderQuestionNewDetail(Map<String,Object> paramMap);
	

}
