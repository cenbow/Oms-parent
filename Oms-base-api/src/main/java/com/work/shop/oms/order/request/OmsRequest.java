package com.work.shop.oms.order.request;

import java.io.Serializable;

/**
 * 订单请求对象
 * @author QuYachu
 */
public class OmsRequest implements Serializable {

	private static final long serialVersionUID = 5014379068811962022L;

	/**
	 * 交货单编码
	 */
	private String orderSn;

	/**
	 * 订单编码
	 */
	private String masterOrderSn;

	/**
	 * 操作人
	 */
	private String actionUser;

	private String actionNote;

	public String getOrderSn() {
		return orderSn == null ? orderSn : orderSn.trim();
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getMasterOrderSn() {
		return masterOrderSn == null ? masterOrderSn : masterOrderSn.trim();
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getActionUser() {
		return actionUser == null ? "system" : actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

    public String getActionNote() {
        return actionNote;
    }

    public void setActionNote(String actionNote) {
        this.actionNote = actionNote;
    }
}
