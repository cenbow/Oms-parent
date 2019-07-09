package com.work.shop.oms.common.paraBean;

import java.io.Serializable;
import java.util.List;

public class OrderReturnPOSParaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8184153739831217770L;
	
	/**
	 * os 订单号
	 */
	private String osOrderNo;
	
	/**
	 * 外部交易号
	 */
	private String wxOrderNo;
	
	
	/**
	 *  0 退货状态
	 */
	private String returnStatus;
	
	/**
	 *  退款状态
       0  未退款  1  已退款
	 */
	private String refundStatus="0";
	/**
	 *  CASH  现金   CARD   入卡
	 */
	private String returnType;
	
	
	/**
	 * 退货门店
	 */
	private String shopCode;
	
	
	/**
	 * 退款银行
	 */
	private String bankName;
	
	
	/**
	 *  退款卡号
	 */
	private String cardNo;
	
	private double fee=0.00d;
	
	/**
	 * 退款金额
	 */
	private double returnAmount=0.00d;
	
	
	/**
	 * 退款备注
	 */
	private String remark;
	
	
	/**
	 * 退单商品明细
	 */
	private List<OrderReturnPOSGoodsBean> goodsList;
	
	
	
	

	public double getFee() {
		return fee;
	}


	public void setFee(double fee) {
		this.fee = fee;
	}


	public String getRefundStatus() {
		return refundStatus;
	}


	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}


	public String getOsOrderNo() {
		return osOrderNo;
	}


	public void setOsOrderNo(String osOrderNo) {
		this.osOrderNo = osOrderNo;
	}


	public String getWxOrderNo() {
		return wxOrderNo;
	}


	public void setWxOrderNo(String wxOrderNo) {
		this.wxOrderNo = wxOrderNo;
	}


	public String getReturnStatus() {
		return returnStatus;
	}


	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}


	public String getReturnType() {
		return returnType;
	}


	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}


	public String getShopCode() {
		return shopCode;
	}


	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}


	public double getReturnAmount() {
		return returnAmount;
	}


	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public List<OrderReturnPOSGoodsBean> getGoodsList() {
		return goodsList;
	}


	public void setGoodsList(List<OrderReturnPOSGoodsBean> goodsList) {
		this.goodsList = goodsList;
	}
	
	
	
	
}
