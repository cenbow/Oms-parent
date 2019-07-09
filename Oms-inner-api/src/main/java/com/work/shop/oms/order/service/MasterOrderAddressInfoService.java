package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.common.bean.MasterShip;

/**
 * 主订单收货人信息创建
 * @author lemon
 *
 */
public interface MasterOrderAddressInfoService {

	/**
	 * 保存收货人地址
	 * @param masterShip 订单配送信息
	 * @param masterOrderSn 订单编码
	 */
	void insertMasterOrderAddressInfo(MasterShip masterShip, String masterOrderSn);

	/**
	 * 根据订单号获取订单收件人信息
	 * @param masterOrderSn
	 * @return
	 */
	MasterOrderAddressInfo selectAddressInfo(String masterOrderSn);
}
