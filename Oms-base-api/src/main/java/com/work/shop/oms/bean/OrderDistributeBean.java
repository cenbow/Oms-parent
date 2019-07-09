package com.work.shop.oms.bean;

import java.io.Serializable;

public class OrderDistributeBean extends OrderDistribute implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2302029364377716677L;

	private double settlementPrice;										// 商品财务价格
	
	private double afterChangePayable;									// 变更后应付款金额

	public double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public double getAfterChangePayable() {
		return afterChangePayable;
	}

	public void setAfterChangePayable(double afterChangePayable) {
		this.afterChangePayable = afterChangePayable;
	}
}