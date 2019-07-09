package com.work.shop.oms.service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;

public interface ShortageQuestionService {
	
	/**
	 * 订单列表(分页);
	 * @param model;
	 * @param helper;
	 * @return Paging;
	 */
	Paging getShortageQuestionList(OrderQuestionSearchVO searchVO) throws Exception;

}
