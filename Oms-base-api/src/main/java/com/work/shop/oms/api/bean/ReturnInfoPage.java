package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ReturnInfoPage implements Serializable {

	private static final long serialVersionUID = 692789479987628628L;
	
	//退货单号、退货明细（商品名称、退货金额、退货数量、商品编码、商家货号）、发货单号、退货人姓名、退货金额、退货申请时间、退货完成时间 
	
	//退货单号
	private String returnSn; 
	
	//主单号
	private String masterOrderSn;
	
	//子单号
	private String relatingOrderSn;
	
	//退货明细（商品名称、退货金额、退货数量、商品编码、商家货号）
	private List<ReturnGoods> returnGoods;
	
	// 退货人姓名
	private String userName;// master_order_info
	
	//退款金额
	private Double returnTotalFee;
	
	//退货申请时间
	private Date addTime;
	
	//退货完成时间 
	private Date clearTime;
	
	//退单原因
	private String returnReason;//order_custom_define(open_shop)
	
	private String returnReasonName;
	
	//退单快递单号
	private String returnInvoiceNo;//order_return_ship
	
	//承运商
	private String shippingName;//system_shipping
	
	//退单状态
	private Integer returnOrderStatus;

	public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public List<ReturnGoods> getReturnGoods() {
		return returnGoods;
	}

	public void setReturnGoods(List<ReturnGoods> returnGoods) {
		this.returnGoods = returnGoods;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getReturnTotalFee() {
		return returnTotalFee;
	}

	public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getClearTime() {
		return clearTime;
	}

	public void setClearTime(Date clearTime) {
		this.clearTime = clearTime;
	}

	public String getReturnReasonName() {
		return returnReasonName;
	}

	public void setReturnReasonName(String returnReasonName) {
		this.returnReasonName = returnReasonName;
	}
	
}
