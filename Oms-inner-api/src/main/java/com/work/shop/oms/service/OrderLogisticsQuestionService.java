package com.work.shop.oms.service;

import java.io.InputStream;
import java.util.List;

import com.work.shop.oms.api.param.bean.OrderQuestionParam;
import com.work.shop.oms.vo.QuestionTypeVO;

public interface OrderLogisticsQuestionService {
	
	public List<QuestionTypeVO> judgeQuestionType(String masterOrderSn);
	
	/**
	 * 导入物流问题单
	 * @param is
	 * @param sb
	 * @param logType 问题单类型  0：问题单 1：缺货物流问题单
	 * @param code 问题单原因
	 * @param mainChild 订单类型  main:订单号，child:交货单
	 * @return
	 * @throws Exception
	 */
	List<OrderQuestionParam> importOrderLogisticsQuestionList(InputStream is, StringBuffer sb, int logType, String code, String  mainChild) throws Exception ;

}
