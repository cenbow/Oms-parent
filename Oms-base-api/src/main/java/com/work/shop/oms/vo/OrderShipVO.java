package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.OrderItem;

/**
 * 订单仓库配送单
 * @author QuYachu
 *
 */
public class OrderShipVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<MasterOrderGoods> goodsList;
	
	private OrderItem orderItem;

	public List<MasterOrderGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<MasterOrderGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	
}
