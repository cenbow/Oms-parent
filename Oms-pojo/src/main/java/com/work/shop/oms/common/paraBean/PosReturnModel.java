package com.work.shop.oms.common.paraBean;

import com.work.shop.oms.common.paraBean.OrderReturnPOSParaBean;

public class PosReturnModel {

	private OrderReturnPOSParaBean posReturnMsg;

	/**
	 * 
	 */
	public PosReturnModel() {
		super();
	}

	/**
	 * @param posReturnMsg
	 */
	public PosReturnModel(OrderReturnPOSParaBean posReturnMsg) {
		this();
		this.posReturnMsg = posReturnMsg;
	}

	/**
	 * @return the posReturnMsg
	 */
	public OrderReturnPOSParaBean getPosReturnMsg() {
		return posReturnMsg;
	}

	/**
	 * @param posReturnMsg
	 *            the posReturnMsg to set
	 */
	public void setPosReturnMsg(OrderReturnPOSParaBean posReturnMsg) {
		this.posReturnMsg = posReturnMsg;
	}
}
