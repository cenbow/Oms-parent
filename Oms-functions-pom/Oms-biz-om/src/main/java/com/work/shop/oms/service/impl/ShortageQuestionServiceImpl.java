package com.work.shop.oms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderQuestionLackSkuNew;
import com.work.shop.oms.bean.OrderQuestionLackSkuNewDel;
import com.work.shop.oms.bean.OrderQuestionLackSkuNewDelExample;
import com.work.shop.oms.bean.OrderQuestionLackSkuNewExample;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;
import com.work.shop.oms.dao.OrderQuestionLackSkuNewDelMapper;
import com.work.shop.oms.dao.OrderQuestionLackSkuNewMapper;
import com.work.shop.oms.service.ShortageQuestionService;

@Service
public class ShortageQuestionServiceImpl implements
		ShortageQuestionService {

	@Resource
	private OrderQuestionLackSkuNewMapper orderQuestionLackSkuNewMapper;
	@Resource
	private OrderQuestionLackSkuNewDelMapper orderQuestionLackSkuNewdelMapper;
	
	@Override
	public Paging getShortageQuestionList(OrderQuestionSearchVO searchVO) throws Exception {
		int num = 0;
		Paging paging = new Paging();
		if (searchVO.getProcessStatus() == null || searchVO.getProcessStatus() == 0) {
			OrderQuestionLackSkuNewExample example = new OrderQuestionLackSkuNewExample();
			example.or().andOrderSnEqualTo(searchVO.getOrderSn()).andQuestionCodeEqualTo(searchVO.getReason());
			List<OrderQuestionLackSkuNew> lackSkus = this.orderQuestionLackSkuNewMapper.selectByExample(example);
			num = this.orderQuestionLackSkuNewMapper.countByExample(example);
			paging.setRoot(lackSkus);
		} else {
			OrderQuestionLackSkuNewDelExample delExample = new OrderQuestionLackSkuNewDelExample();
			delExample.or().andOrderSnEqualTo(searchVO.getOrderSn()).andQuestionCodeEqualTo(searchVO.getReason());
			List<OrderQuestionLackSkuNewDel> lackSkuDels = this.orderQuestionLackSkuNewdelMapper.selectByExample(delExample);
			num = this.orderQuestionLackSkuNewdelMapper.countByExample(delExample);
			paging.setRoot(lackSkuDels);
		}
		paging.setTotalProperty(num);
		return paging;
	}
}
