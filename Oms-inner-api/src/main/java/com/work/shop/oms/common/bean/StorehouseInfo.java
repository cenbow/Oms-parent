package com.work.shop.oms.common.bean;

import java.util.List;

import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;


public class StorehouseInfo {
	
	/**
	 * 发货信息
	 **/
	private List<OrderShipVO> osList;

	/**
	 * 商品信息
	 **/
	private List<OrderGoodsUpdateBean> ogvoList;
	
	private String userLevelName;
	
	private String userTypeName;
	
	/**
	 * 会员商品折扣率
	 */
	private int userLevelDiscount;

	public List<OrderShipVO> getOsList() {
		return osList;
	}

	public void setOsList(List<OrderShipVO> osList) {
		this.osList = osList;
	}

	public List<OrderGoodsUpdateBean> getOgvoList() {
		return ogvoList;
	}

	public void setOgvoList(List<OrderGoodsUpdateBean> ogvoList) {
		this.ogvoList = ogvoList;
	}
	
	public String getUserLevelName() {
		return userLevelName;
	}

	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public int getUserLevelDiscount() {
		return userLevelDiscount;
	}

	public void setUserLevelDiscount(int userLevelDiscount) {
		this.userLevelDiscount = userLevelDiscount;
	}

}
