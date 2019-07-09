package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 订单结算详细列表清单(对应order_settle_bill表)
 * @author lyh
 *
 */
public class OrderSettleBillVO implements Serializable {

	private static final long serialVersionUID = 8220425529113631276L;
	
	private String id;//ID
	private String billNo;//单据批次号
	private String orderCode;//订单号/外部交易号/退单号
	private String orderCodeType;//0订单号/退单号 1外部交易号
	private String orderType;//订单类型 0，正常订单 1，补发订单 2，换货订单
	private String shippingId;//用户选择的配送方式id，取值表shipping
	private String returnPay;//退款方式 System_payment 字典数据
	private String money;//结算金额
	private String resultStatus;//处理结果:0.未定义1.结算成功2.结算失败
	private String resultMsg;//处理结果
	private String addTime;//添加时间
	private String clearTime;//结算时间
	private String updateTime;//更新时间
	private String message;//日志内容
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderCodeType() {
		return orderCodeType;
	}
	public void setOrderCodeType(String orderCodeType) {
		this.orderCodeType = orderCodeType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getShippingId() {
		return shippingId;
	}
	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}
	public String getReturnPay() {
		return returnPay;
	}
	public void setReturnPay(String returnPay) {
		this.returnPay = returnPay;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getClearTime() {
		return clearTime;
	}
	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
