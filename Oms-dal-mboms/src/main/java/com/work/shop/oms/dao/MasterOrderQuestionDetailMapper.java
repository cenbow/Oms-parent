package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.MasterOrderQuestionDetail;

public interface MasterOrderQuestionDetailMapper {
	
	public List<MasterOrderQuestionDetail> getMasterOrderQuestionDetail(Map<String,Object> paramMap);

}
