package com.work.shop.oms.orderReturn.service;

import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderGoodsChargeBack;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.paraBean.ParaChargebackBean;

public interface OrderPosReturnService {

	/**
	 * 保存门店接口退单记录请求
	 * @param paraBean
	 */
	void saveOrderGoodsChargeBackData(OrderGoodsChargeBack paraBean);
	
	
	/**
	 * 创建POS退单申请
	 * @param paraBean
	 */
	ServiceReturnInfo<OrderDistribute> createPosReturnApplication(ParaChargebackBean paraBean);
}
