package com.work.shop.oms.common.paraBean;

import java.io.Serializable;

public class ParaBackGoodsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4763751249416336684L;
	
	
	/**
	 * 标签
	 */
	private long id;
	/**
	 * 11位码
	 */
	private String sku_sn;	
	
	/**
	 * 商品数量
	 */
	private int goods_sum;
	
	/**
	 * 商品金额
	 */
	private double goods_money=0.00d;
	
	/**
	 * 退款金额
	 */
	private double goods_amount=0.00d;
	
	/**
	 * 备注信息
	 */
	private String return_msg;
	
	

	public String getSku_sn() {
		return sku_sn;
	}

	public void setSku_sn(String sku_sn) {
		this.sku_sn = sku_sn;
	}

	public int getGoods_sum() {
		return goods_sum;
	}

	public void setGoods_sum(int goods_sum) {
		this.goods_sum = goods_sum;
	}

	public double getGoods_money() {
		return goods_money;
	}

	public void setGoods_money(double goods_money) {
		this.goods_money = goods_money;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getGoods_amount() {
		return goods_amount;
	}

	public void setGoods_amount(double goods_amount) {
		this.goods_amount = goods_amount;
	}
	
	
	
}
