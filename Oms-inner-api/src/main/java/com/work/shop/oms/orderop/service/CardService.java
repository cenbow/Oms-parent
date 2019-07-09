package com.work.shop.oms.orderop.service;

import java.util.List;

public interface CardService {
	/*
	*//**
	 * 使用红包并且记录日志
	 * @param orderSn
	 * @param CardNo 卡号
	 * @param userId 用户ID
	 *//*
	void useCardPackage(String orderSn, String cardNo,String userId);

	*//**
	 *  
	 * 使用打折卷并且记录日志
	 * @param cardNo  
	 * @param userId
	 *//*
	void userCardCoupon(String cardNo, String userId);
	
	*//**
	 * 订单取消时，退卡处理
	 * @param orderSn		订单号
	 * @param userId		下单用户ID
	 * @param bonusMoney	订单使用红包金额
	 *//*
	void cancelCardForOrderCancel(String orderSn, String userId, double bonusMoney);
	
	*//**
	 * 根据订单号，获取订单使用的红包ID
	 * @param orderSn
	 * @return
	 *//*
	public List<String> getBonusIdsByOrderSn(String orderSn);
	
	*//**
	 * 订单取消时，券卡操作失败日志
	 * @param userId
	 * @param cardNo
	 * @param cardTable
	 * @param cardMoneyNew
	 *//*
	void logCardFailed(String userId, String cardNo, String cardTable, double cardMoneyNew);*/
}
