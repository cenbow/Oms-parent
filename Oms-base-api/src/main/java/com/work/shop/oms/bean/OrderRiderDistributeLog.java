package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单骑手配送日志
 * @author QuYachu
 *
 */
public class OrderRiderDistributeLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer logId;
	
	/**
	 * 订单编码
	 */
	private String masterOrderSn;
	
	/**
	 * 外部交易号
	 */
	private String orderOutSn;

	/**
	 * 渠道编码
	 */
	private String channelCode;
	
	/**
	 * 店铺编码
	 */
	private String shopCode;
	
	/**
	 * 店铺名称
	 */
	private String shopName;
	
	/**
	 * 骑手配送平台编码
	 */
	private String tmsCode;
	
	/**
	 * 骑手配送平台名称
	 */
	private String tmsName;
	
	/**
	 * 骑手配送平台店铺编码
	 */
	private String shopNo;
	
	/**
	 * 骑手平台订单号
	 */
	private String clientOrderSn;
	
	/**
	 * 配送状态0未配送、1配送中、2配送完成、3配送取消、4接单
	 */
	private Integer orderStatus;
	
	/**
	 * 订单状态(异常单)
	 */
	private String statusMsg;
	
	/**
	 * 新增时间
	 */
	private Date addTime;
	
	/**
	 * 发单时间
	 */
	private Date createTime;
	
	/**
	 * 接单时间
	 */
	private Date acceptTime;
	
	/**
	 * 取货时间
	 */
	private Date fetchTime;
	
	/**
	 * 完成时间
	 */
	private Date finishTime;
	
	/**
	 * 取消时间
	 */
	private Date cancelTime;
	
	/**
	 * 配送距离,单位为米
	 */
	private Double distance;
	
	/**
	 * 配送费,单位为元
	 */
	private Double deliveryFee;
	
	/**
	 * 优惠费用
	 */
	private Double couponFee;
	
	/**
	 * 实际支付费用
	 */
	private Double actualFee;
	
	/**
	 * 配送信息备注
	 */
	private String distributeMsg;
	
	/**
	 * 配送类型 0初始配送、1重新配送
	 */
	private int addType;
	
	/**
	 * 操作用户
	 */
	private String actionUser;

	/**
	 * 是否系统签收 0:否;1:是;2:客户签收;
	 */
	private Integer isSysReceipt;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getTmsCode() {
		return tmsCode;
	}

	public void setTmsCode(String tmsCode) {
		this.tmsCode = tmsCode;
	}

	public String getTmsName() {
		return tmsName;
	}

	public void setTmsName(String tmsName) {
		this.tmsName = tmsName;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getClientOrderSn() {
		return clientOrderSn;
	}

	public void setClientOrderSn(String clientOrderSn) {
		this.clientOrderSn = clientOrderSn;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public Date getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Date fetchTime) {
		this.fetchTime = fetchTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Double getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Double couponFee) {
		this.couponFee = couponFee;
	}

	public Double getActualFee() {
		return actualFee;
	}

	public void setActualFee(Double actualFee) {
		this.actualFee = actualFee;
	}

	public String getDistributeMsg() {
		return distributeMsg;
	}

	public void setDistributeMsg(String distributeMsg) {
		this.distributeMsg = distributeMsg;
	}

	public int getAddType() {
		return addType;
	}

	public void setAddType(int addType) {
		this.addType = addType;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Integer getIsSysReceipt() {
		return isSysReceipt;
	}

	public void setIsSysReceipt(Integer isSysReceipt) {
		this.isSysReceipt = isSysReceipt;
	}
}