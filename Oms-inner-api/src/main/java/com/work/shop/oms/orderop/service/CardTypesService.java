package com.work.shop.oms.orderop.service;

import com.work.shop.oms.common.bean.CardInfo;

/**
 * cardCoupon，cardUserPackage，cardPackage相关操作
 *
 * @author lemon
 *
 */
public interface CardTypesService {
	
	/**
	 * 根据类型，获得表名
	 * @param typeCode 类型
	 * @return
	 *//*
	public String getCardTableName(String typeCode);
	
	*//**
	 * 根据卡号，表名获取券卡信息
	 * @param cardNo
	 * @param tableName
	 * @return
	 *//*
	public CardInfo getCardInfo(String cardNo, String tableName);
	
	*//**
	 * 保存券卡信息
	 * @param ci
	 * @param cardTable
	 * @param actionNote
	 * @param preCardNo
	 *//*
	public void saveCardInfo(CardInfo ci, String cardTable, String actionNote, String preCardNo);
	
	*//**
	 * 根据旧的卡号，重新生成新卡号
	 * @param oldCardNo
	 * @param tableName
	 * @return
	 *//*
	public String generateNewCardNo(String oldCardNo, String tableName);
	
	*//**
	 * 红包作废
	 * @param cardNo
	 *//*
	public void repealCard(String cardNo);*/
}
