package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderExchangesDetailInfo implements Serializable{
	
	/**
	 * 换单
	 */
	private static final long serialVersionUID = -3054037172321953987L;
	private String	orderSn;
	private String	orderCreateTime;//订单创建时间
	private String	createPayTime;//等待补款时间
	private String	orderPayTime;//补款成功时间
	private String	receiveTime;//美邦收货时间
	private String	qualityTime;//商品质检时间
	private double  totalPayable;//应付款
	private double	returnPostage;//退邮费
	private double	postage;//换单邮费
	private double returnGoodsMoney;//退商品金额
	private double goodsAmount;//商品金额
	private String returnSn;//退单编号
	private String  returnExpress;//退单快递公司编码
	private String  returnInvoiceNo;//退单快递单号
	private String returnExpressImg;//退单物流图片
	private String userId;
	private int payStatus;
	private int shipStatus;
 	private double integralMoney;//使用的积分金额
 	private int  integral;//使用积分
	private int  progressStatus; //流程状态     1 => '等待补款',2 => '补款成功',3 => '美邦收货',4 => '商品质检',5 => '商品出库',6 => '确认收货'

	private List<OrderShipInfo>	orderShipInfo;
	private List<OrderReturnGoodsInfo>	returnGoodsInfo;
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public double getPostage() {
		return postage;
	}
	public void setPostage(double postage) {
		this.postage = postage;
	}
	public List<OrderShipInfo> getOrderShipInfo() {
		return orderShipInfo;
	}
	public void setOrderShipInfo(List<OrderShipInfo> orderShipInfo) {
		this.orderShipInfo = orderShipInfo;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getCreatePayTime() {
		return createPayTime;
	}
	public void setCreatePayTime(String createPayTime) {
		this.createPayTime = createPayTime;
	}
	public String getOrderPayTime() {
		return orderPayTime;
	}
	public void setOrderPayTime(String orderPayTime) {
		this.orderPayTime = orderPayTime;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getQualityTime() {
		return qualityTime;
	}
	public void setQualityTime(String qualityTime) {
		this.qualityTime = qualityTime;
	}
	public double getReturnPostage() {
		return returnPostage;
	}
	public void setReturnPostage(double returnPostage) {
		this.returnPostage = returnPostage;
	}
	public double getReturnGoodsMoney() {
		return returnGoodsMoney;
	}
	public void setReturnGoodsMoney(double returnGoodsMoney) {
		this.returnGoodsMoney = returnGoodsMoney;
	}
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getReturnSn() {
		return returnSn;
	}
	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}
	public String getReturnExpress() {
		return returnExpress;
	}
	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}
	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}
	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}
	public String getReturnExpressImg() {
		return returnExpressImg;
	}
	public void setReturnExpressImg(String returnExpressImg) {
		this.returnExpressImg = returnExpressImg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}
	public int getProgressStatus() {
		return progressStatus;
	}
	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}
	public double getTotalPayable() {
		return totalPayable;
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}
	public double getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(double integralMoney) {
		this.integralMoney = integralMoney;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public List<OrderReturnGoodsInfo> getReturnGoodsInfo() {
		return returnGoodsInfo;
	}
	public void setReturnGoodsInfo(List<OrderReturnGoodsInfo> returnGoodsInfo) {
		this.returnGoodsInfo = returnGoodsInfo;
	}
	

}
