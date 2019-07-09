package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class MasterOrderInfoBean extends MasterOrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3265207390582109995L;

	private BigDecimal settlementPrice;										// 商品财务价格
	
	private BigDecimal afterChangePayable;									// 变更后应付款金额

	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public BigDecimal getAfterChangePayable() {
		return afterChangePayable;
	}

	public void setAfterChangePayable(BigDecimal afterChangePayable) {
		this.afterChangePayable = afterChangePayable;
	}
}
