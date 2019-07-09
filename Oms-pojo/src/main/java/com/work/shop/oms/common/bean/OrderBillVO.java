package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 订单结算批次清单VO(对应order_bill_list表)
 * @author lyh
 *
 */
public class OrderBillVO implements Serializable{

	private static final long serialVersionUID = 8938788041120895129L;
	
	private String billNo;//单据号
	private String channelCode;//渠道号
	private String billType;//业务类型:0未定义 ，1订单结算，2订单货到付款结算，3退单退款方式结算，4保证金结算，5日志，6邦付宝退款结算
	private String actionUser;//操作人
	private String note;//备注
	private String isTiming;//是否定时执行，0否1是
	private String execTime;//执行时间
	private String isSync;//同步状态：0未同步，1已同步，2作废，3部分同步，4同步失败，9同步中
	private String addTime;//添加时间
	private String updateTime;//更新时间
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIsTiming() {
		return isTiming;
	}
	public void setIsTiming(String isTiming) {
		this.isTiming = isTiming;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getIsSync() {
		return isSync;
	}
	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
