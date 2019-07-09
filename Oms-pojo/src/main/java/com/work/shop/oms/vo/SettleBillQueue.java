package com.work.shop.oms.vo;

import java.io.Serializable;

/**
 * 批量结算数据
 * @author huangl
 *
 */
public class SettleBillQueue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//详细数据ID
	private String billNo;// 单据号 CommonUtils.getSettleBillCode()  (必填)
	private String orderCode;// 订单号/退单号  (必填)
	private Integer orderCodeType;// 单号类型：0订单号/退单号   1外部交易号
	private Byte shippingId;// 承运商ID(订单货到付款结算时使用)
	private Integer returnPay;// 退款方式(退单结算使用)
	
	private Double money;// 结算金额 (必填)
	
	private Byte returnSettlementType;// 1,预付款，2保证金',

	private String message; // 调整日志
	
	private String actionUser; //
	
	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Byte getReturnSettlementType() {
		return returnSettlementType;
	}

	public void setReturnSettlementType(Byte returnSettlementType) {
		this.returnSettlementType = returnSettlementType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrderCodeType() {
		return orderCodeType;
	}

	public void setOrderCodeType(Integer orderCodeType) {
		this.orderCodeType = orderCodeType;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	 
	public Byte getShippingId() {
		return shippingId;
	}

	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}

	public Integer getReturnPay() {
		return returnPay;
	}

	public void setReturnPay(Integer returnPay) {
		this.returnPay = returnPay;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
}
