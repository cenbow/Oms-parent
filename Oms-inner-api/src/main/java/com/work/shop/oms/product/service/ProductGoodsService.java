package com.work.shop.oms.product.service;

import java.util.List;

import com.work.shop.oms.bean.ProductGoods;
import com.work.shop.oms.common.bean.ChannelGoodsVo;
import com.work.shop.oms.common.bean.ReturnInfo;


/**
 * 
 * @author lemon
 *
 */
public interface ProductGoodsService {

	ReturnInfo selectChannelGoods(String channelCode, String goodsSn);
	
	ReturnInfo selectChannelSku(String channelCode, String Sku);
}
