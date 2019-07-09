package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;

public interface OrderPriceService {
	
	/**
	 * 计算订单财务价格
	 * @param orderInfo
	 * @param orderGoods 商品信息
	 * @param type 分摊红包类型
	 * @return
	 */
	public void financialPrice(MasterOrderInfo masterOrderInfo, List<MasterOrderGoods> masterOrderGoods) ;

}
