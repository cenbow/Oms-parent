package com.work.shop.oms.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExChangeOrderSearchResultVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4083645064176064536L;
	private String orderSn;
	private String relating_exchange_sn="";
	private String relating_return_sn="";
	private String relating_remoney_sn="";
	private String add_time;
	private String order_from;
	private String referer;
	private String user_name="";
	private String consignee="";
	private int goods_count;
	private BigDecimal total_fee;
	private BigDecimal money_paid;
	private BigDecimal total_payable;
	private int lock_status;
	private String operator="";
	private String lock_status_Str="";
	
	public String getLock_status_Str() {
		return lock_status_Str;
	}
	public void setLock_status_Str(String lock_status_Str) {
		this.lock_status_Str = lock_status_Str;
	}
	//订单状态
	private int order_status;
	//支付总状态
	private int pay_status;
	//发货总状态
	private int ship_status;
	private String process_status_Str;
	
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
		switch(order_status)
		{
		case 0:
			//（0，未确认；1，已确认；2，已取消；3，无效；4，退货；5，锁定；6，解锁；7，完成；8，拒收；9，已合并；10，已拆分；）
			setProcess_status_Str("未确认,");
			break;
		case 1:
			setProcess_status_Str("已确认,");
			break;
		case 2:
			setProcess_status_Str("已取消,");
			break;
		case 3:
			setProcess_status_Str("无效,");
			break;
		case 4:
			setProcess_status_Str("退货,");
			break;
		case 5:
			setProcess_status_Str("锁定,");
			break;
		case 6:
			setProcess_status_Str("解锁,");
			break;
		case 7:
			setProcess_status_Str("完成,");
			break;
		case 8:
			setProcess_status_Str("拒收,");
			break;
		case 9:
			setProcess_status_Str("已合并,");
			break;
		case 10:
			setProcess_status_Str("已拆分,");
			break;
		}
	}
	public int getPay_status() {
		return pay_status;
	}
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
		//（0，未付款；1，部分付款；2，已付款；3，已结算）
				switch(pay_status)
				{
				case 0:
					setProcess_status_Str(getProcess_status_Str()+"未付款,");
					break;
				case 1:
					setProcess_status_Str(getProcess_status_Str()+"部分付款,");
					break;
				case 2:
					setProcess_status_Str(getProcess_status_Str()+"已付款,");
					break;
				case 3:
					setProcess_status_Str(getProcess_status_Str()+"已结算,");
					break;
				}
	}
	public int getShip_status() {
		return ship_status;
	}
	public void setShip_status(int ship_status) {
		this.ship_status = ship_status;
		//（0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
				switch(ship_status)
				{
				case 0:
					setProcess_status_Str(getProcess_status_Str()+"未发货");
					break;
				case 1:
					setProcess_status_Str(getProcess_status_Str()+"备货中");
					break;
				case 2:
					setProcess_status_Str(getProcess_status_Str()+"部分发货");
					break;
				case 3:
					setProcess_status_Str(getProcess_status_Str()+"已发货");
					break;
				case 4:
					setProcess_status_Str(getProcess_status_Str()+"部分收货");
					break;
				case 5:
					setProcess_status_Str(getProcess_status_Str()+"客户已收货");
					break;
				case 6:
					setProcess_status_Str(getProcess_status_Str()+"门店部分收货");
					break;
				case 7:
					setProcess_status_Str(getProcess_status_Str()+"门店收货");
					break;
				}
	}
	public String getProcess_status_Str() {
		return process_status_Str;
	}
	public void setProcess_status_Str(String process_status_Str) {
		this.process_status_Str = process_status_Str;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getRelating_exchange_sn() {
		return relating_exchange_sn;
	}
	public void setRelating_exchange_sn(String relating_exchange_sn) {
		this.relating_exchange_sn = relating_exchange_sn;
	}
	public String getRelating_return_sn() {
		return relating_return_sn;
	}
	public void setRelating_return_sn(String relating_return_sn) {
		this.relating_return_sn = relating_return_sn;
	}
	public String getRelating_remoney_sn() {
		return relating_remoney_sn;
	}
	public void setRelating_remoney_sn(String relating_remoney_sn) {
		this.relating_remoney_sn = relating_remoney_sn;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getOrder_from() {
		return order_from;
	}
	public void setOrder_from(String order_from) {
		this.order_from = order_from;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public int getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}
	public BigDecimal getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(BigDecimal money_paid) {
		this.money_paid = money_paid;
	}
	public BigDecimal getTotal_payable() {
		return total_payable;
	}
	public void setTotal_payable(BigDecimal total_payable) {
		this.total_payable = total_payable;
	}
	public int getLock_status() {
		return lock_status;
	}
	public void setLock_status(int lock_status) {
		this.lock_status = lock_status;
		if(lock_status==0)
		{
			setLock_status_Str("未锁定");
		}else
		{
			setLock_status_Str("已锁定");
		}
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
