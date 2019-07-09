package com.work.shop.oms.order.request;

import java.io.Serializable;
import java.util.List;

/**
 * 订单管理请求
 * @author QuYachu
 */
public class OrderManagementRequest implements Serializable {

	private static final long serialVersionUID = -7115261779726715323L;

	/**
	 * 订单号
	 */
	private String masterOrderSn;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 操作人
	 */
	private String actionUser;

	/**
	 * 操作人id
	 */
	private String actionUserId;

	/**
	 * 自提码
	 */
	private String gotCode;

	/**
	 * 原因编码 取消、问题单等使用
	 */
	private String reasonCode;

	/**
	 * 支付单号
	 */
	private String paySn;

	/**
	 * 交货单列表
	 */
	private List<String> orderSnList;

	/**
	 * 交货单号
	 */
	private String shipSn;

	/**
	 * 仓库编码
	 */
	private String depotCode;

	/**
	 * 方法调用来源 POS:POS端;FRONT:前端;OMS:后台取消;ERP:ERP端
	 */
	private String source = "OMS";

    /**
     * 支付方式编码
     */
    private String payCode;

    /**
     * 订单类型0正常订单,1协议订单
     */
    private Integer orderType;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getMessage() {
		return message == null ? "" : message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(String actionUserId) {
		this.actionUserId = actionUserId;
	}

	public String getGotCode() {
		return gotCode;
	}

	public void setGotCode(String gotCode) {
		this.gotCode = gotCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	public List<String> getOrderSnList() {
		return orderSnList;
	}

	public void setOrderSnList(List<String> orderSnList) {
		this.orderSnList = orderSnList;
	}

	public String getShipSn() {
		return shipSn;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
}
