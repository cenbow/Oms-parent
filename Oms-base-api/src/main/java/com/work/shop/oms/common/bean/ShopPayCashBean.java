package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class ShopPayCashBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672062392452606300L;

	private String userName;

	private Float money;

	// 用户账户:0消费|1收入|2使用冻结资金|3退还到冻结资金|4将可用资金转到冻结资金|5将冻结资金转换为可用资金
	private Integer type;

	// 交易类型: 1现金充值 2 人工调减 3 人工调加 4 实物卡充值 5 积分兑换 6 渠道消费
	private Integer orderType;

	// 来源
	private String orderSource;

	private String orderNo;
	
	// 备注
	private String memo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	// 2013-07-04 add 接入邦付宝
	private String smsFlag;

	private String smsCode;

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
