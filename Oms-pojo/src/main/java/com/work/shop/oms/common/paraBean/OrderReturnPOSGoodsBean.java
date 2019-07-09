package com.work.shop.oms.common.paraBean;

import java.io.Serializable;

/**
 * 退货商品明细
 * @author Administrator
 *
 */
public class OrderReturnPOSGoodsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -561271937586714517L;
	
	
	/**
	 * 退货商品 11位码
	 */
	private String prodNum;
	
	/**
	 * 退货数量
	 */
	private int Qty;
	
	
	/**
	 * 退货金额
	 */
	private double amount=0.00d;
	
	
	/**
	 * 备注信息
	 */
	private String remark;


	public String getProdNum() {
		return prodNum;
	}


	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}


	public int getQty() {
		return Qty;
	}


	public void setQty(int qty) {
		Qty = qty;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
	
	
	
}
