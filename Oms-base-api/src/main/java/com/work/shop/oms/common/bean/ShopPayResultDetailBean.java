package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShopPayResultDetailBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//totalMoney(账户总金额), useMoney(当前可用金额), frozenMoney(当前已冻结总金额)
	private Float totalMoney;
	
	private Float useMoney;
	
	private Float frozenMoney;

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Float getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(Float useMoney) {
		this.useMoney = useMoney;
	}

	public Float getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(Float frozenMoney) {
		this.frozenMoney = frozenMoney;
	}
	
	

}
