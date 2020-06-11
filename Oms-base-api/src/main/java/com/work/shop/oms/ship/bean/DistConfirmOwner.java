package com.work.shop.oms.ship.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;

/**
 * 发货确认信息
 * @author QuYachu
 */
public class DistConfirmOwner implements Serializable {

	private static final long serialVersionUID = 4783709070679140122L;

	/**
	 * 交货单号
	 */
	private String orderSn;

	/**
	 * 货主仓编码 ERP逻辑仓
	 */
	private String ownerCode;

	/**
	 * 物流公司编码
	 */
	private String invoiceNo;

	/**
	 * 订单配送时间
	 */
	private Date deliveryTime;

	/**
	 * 配送方式id
	 */
	private Byte shippingId;

	/**
	 * 配送方式的名称
	 */
	private String shippingName;

	/**
	 * 操作类型 0：不变 1：新增 2：删除 3：更新
	 */
	private Integer operType;

	/**
	 * 配送类型 0:仓库发货单; 1:门店发货单; 2:工厂发货单; 3:第三方仓库发货单
	 */
	private Integer deliveryType;

	/**
	 * 原物流公司编码
	 */
	private String preInvoiceNo;

	/**
	 * 揽件时间
	 */
	private Date pickupDate;

	/**
	 * 分配时间
	 */
	private Date depotTime;

	/**
	 * 实发组织编码
	 */
    private String pdwarhCode;

	/**
	 * 实发组织名称
	 */
	private String pdwarhName;

	/**
	 * 实发组织联系人
	 */
    private String toUser;

	/**
	 * 实发组织联系电话
	 */
	private String toUserPhone;

	/**
	 * 实发组织省/城市
	 */
    private String provincecity;

	/**
	 * 预计发货周期
	 */
	private String overTransCycle;

	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;

	/**
	 * 商品列表
	 */
	private List<MasterOrderGoods> goodsItems;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Byte getShippingId() {
		return shippingId;
	}

	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public List<MasterOrderGoods> getGoodsItems() {
		return goodsItems;
	}

	public void setGoodsItems(List<MasterOrderGoods> goodsItems) {
		this.goodsItems = goodsItems;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getPreInvoiceNo() {
		return preInvoiceNo;
	}

	public void setPreInvoiceNo(String preInvoiceNo) {
		this.preInvoiceNo = preInvoiceNo;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public Date getDepotTime() {
		return depotTime;
	}

	public void setDepotTime(Date depotTime) {
		this.depotTime = depotTime;
	}

	public String getOverTransCycle() {
		return overTransCycle;
	}

	public void setOverTransCycle(String overTransCycle) {
		this.overTransCycle = overTransCycle;
	}

	public String getPdwarhCode() {
		return pdwarhCode;
	}

	public void setPdwarhCode(String pdwarhCode) {
		this.pdwarhCode = pdwarhCode;
	}

	public String getPdwarhName() {
		return pdwarhName;
	}

	public void setPdwarhName(String pdwarhName) {
		this.pdwarhName = pdwarhName;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getToUserPhone() {
		return toUserPhone;
	}

	public void setToUserPhone(String toUserPhone) {
		this.toUserPhone = toUserPhone;
	}

	public String getProvincecity() {
		return provincecity;
	}

	public void setProvincecity(String provincecity) {
		this.provincecity = provincecity;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
}