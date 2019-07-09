package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 分配单发货确认信息
 * @author QuYachu
 */
public class DistributeShippingBean implements Serializable {

	private static final long serialVersionUID = 576965025144712546L;

	/**
	 * 出库时间
	 */
	private Date shipDate;

	/**
	 * 商品编码
	 */
	private String customCode;

	/**
	 * 快递单号
	 */
	private String invoiceNo;

	/**
	 * 交货单号
	 */
	private String shipSn;

	/**
	 * 订单号
	 */
	private String orderSn;

	/**
	 * 配送编码
	 */
	private String shippingCode;

	/**
	 * 配送名称
	 */
	private String shippingName;
	
	private String depotCode;
	
	private String actionUser;
	
	private Boolean isSystem;

	/**
	 * 交货单列表
	 */
	private List<String> shipSnList;

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getShipSn() {
		return shipSn;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getActionUser() {
		return actionUser == null ? "system" : actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	public List<String> getShipSnList() {
		return shipSnList;
	}

	public void setShipSnList(List<String> shipSnList) {
		this.shipSnList = shipSnList;
	}
}