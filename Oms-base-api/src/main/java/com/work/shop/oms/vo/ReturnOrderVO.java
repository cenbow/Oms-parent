package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.bean.OrderRefund;
import com.work.shop.oms.bean.OrderRefundBean;
import com.work.shop.oms.common.bean.PayType;

/**
 * 退单详情bean
 * @author Cage
 *
 */
public class ReturnOrderVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ReturnCommonVO returnCommon; // 退单基本信息
	private List<ReturnGoodsVO> returnGoodsList; // 退单商品信息
	private List<OrderRefund> returnRefundList; // 退单退款信息
	private List<OrderRefundBean> returnRefundBeanList; // 退单退款信息
	private ReturnAccountVO returnAccount; // 退单账目信息
	private List<PayType> orderPayList; // 原订单支付信息
	
	//退单操作
	private List<OrderReturnAction> returnActionList; // 退单日志信息
	//按钮事件参数
	private Integer disposition = 0;
	private String actionNote;
	private Integer orderShipType = 0;
	
	public List<PayType> getOrderPayList() {
		return orderPayList;
	}
	public void setOrderPayList(List<PayType> orderPayList) {
		this.orderPayList = orderPayList;
	}
	public ReturnCommonVO getReturnCommon() {
		return returnCommon;
	}
	public void setReturnCommon(ReturnCommonVO returnCommon) {
		this.returnCommon = returnCommon;
	}
	
	public List<ReturnGoodsVO> getReturnGoodsList() {
		return returnGoodsList;
	}
	public void setReturnGoodsList(List<ReturnGoodsVO> returnGoodsList) {
		this.returnGoodsList = returnGoodsList;
	}
	public List<OrderRefund> getReturnRefundList() {
		return returnRefundList;
	}
	public void setReturnRefundList(List<OrderRefund> returnRefundList) {
		this.returnRefundList = returnRefundList;
	}
	public ReturnAccountVO getReturnAccount() {
		return returnAccount;
	}
	public void setReturnAccount(ReturnAccountVO returnAccount) {
		this.returnAccount = returnAccount;
	}
	public Integer getDisposition() {
		return disposition;
	}
	public void setDisposition(Integer disposition) {
		this.disposition = disposition;
	}
	public String getActionNote() {
		return actionNote;
	}
	public void setActionNote(String actionNote) {
		this.actionNote = actionNote;
	}
	public List<OrderReturnAction> getReturnActionList() {
		return returnActionList;
	}
	public void setReturnActionList(List<OrderReturnAction> returnActionList) {
		this.returnActionList = returnActionList;
	}
	public Integer getOrderShipType() {
		return orderShipType;
	}
	public void setOrderShipType(Integer orderShipType) {
		this.orderShipType = orderShipType;
	}
	public List<OrderRefundBean> getReturnRefundBeanList() {
		return returnRefundBeanList;
	}
	public void setReturnRefundBeanList(List<OrderRefundBean> returnRefundBeanList) {
		this.returnRefundBeanList = returnRefundBeanList;
	}
}