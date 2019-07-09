package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.bean.OrderReturnGoods;

public class ReturnOrderGoods extends OrderReturnGoods implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	private String promotionId;											// 订单促销ID

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
}
