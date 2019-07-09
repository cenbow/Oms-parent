package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.ProductGoods;
import com.work.shop.oms.common.bean.ChannelGoodsVo;

public interface ChannelGoodsDefinedMapper {

	@ReadOnly
	List<ChannelGoodsVo> searchGoodsForOrder(Map<String, Object> params);
	
	@ReadOnly
	List<ChannelGoodsVo> getGoodsInfoOfOrderInfo(Map<String, Object> paramMap);
	
	@Writer
	List<ProductGoods> selectGoodsProtectPrice();
}