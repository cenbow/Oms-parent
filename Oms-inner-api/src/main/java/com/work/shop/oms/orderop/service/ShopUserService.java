package com.work.shop.oms.orderop.service;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;

/**
 * 店长用户服务
 * @author lemon
 *
 */
public interface ShopUserService {

	/**
	 * 获取创建订单用户信息
	 * @param userId - 用户id
	 */
	public ReturnInfo<ShopUserInfo> getUserCreateOrderInfo(String userId);
	
	/**
	 * 
	 * @param pushType
	 * @param masterOrderSn
	 * @param master
	 * @return
	 */
	public ReturnInfo<String> pushOrdreShop(Integer pushType, String masterOrderSn, MasterOrderInfo master);
}
