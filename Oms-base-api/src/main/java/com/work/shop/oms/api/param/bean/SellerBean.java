package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.api.bean.OrderReturnForSellers;
/**
 * 返回供销商对应条件的退单数据
 * @author cage
 *
 */
public class SellerBean implements Serializable {

	private static final long serialVersionUID = -1343386052574452952L;
	
	private String seller;//供应商编码
	
	private List<OrderReturnForSellers> orderReturnForSellers;//供应商所对应的退单数据

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public List<OrderReturnForSellers> getOrderReturnForSellers() {
		return orderReturnForSellers;
	}

	public void setOrderReturnForSellers(
			List<OrderReturnForSellers> orderReturnForSellers) {
		this.orderReturnForSellers = orderReturnForSellers;
	}
	
	
}
