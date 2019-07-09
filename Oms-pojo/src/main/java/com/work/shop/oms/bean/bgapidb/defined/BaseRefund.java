/*
* 2015-2-2 下午3:29:14
* 吴健 HQ01U8435
*/

package com.work.shop.oms.bean.bgapidb.defined;



public abstract class BaseRefund {
	
	private String outReturnId;
	private String shopCode;
	private String outRefundType;

	public String getOutReturnId() {
		return outReturnId;
	}

	public void setOutReturnId(String outReturnId) {
		this.outReturnId = outReturnId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getOutRefundType() {
		return outRefundType;
	}

	public void setOutRefundType(String outRefundType) {
		this.outRefundType = outRefundType;
	}




	
}
