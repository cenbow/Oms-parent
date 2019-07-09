package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

public class OrderItemAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2925362566376248510L;

	private String name;
	
	private String orderSn;

    /**
     * 日志类型：1为订单日志，2为沟通，2为交货单日志
     */
	private int actionType;
	
	private List<OrderItemActionDetail> actionDetails;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OrderItemActionDetail> getActionDetails() {
		return actionDetails;
	}

	public void setActionDetails(List<OrderItemActionDetail> actionDetails) {
		this.actionDetails = actionDetails;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
