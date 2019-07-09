package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReturnOrder implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	private String returnSn;											// 退货单号
	
	private Integer haveRefund;											// 是否退款 0:无须退款；1:需要退款
	
	private Integer processType;										// 退单类型 1:退货; 2:换货
	
	private String orderSn;												// 关联订单号
	
	private String shipSn;												// 关联交货单号
	
	private String newOrderSn;											// 关联换货单号

	private String userId;												// 下单人ID
	
	private String shopCode;											// 订单来源（渠道）

	private Double returnShippingFee;									// 配送总费用

	private String postscript;											// 订单附言

	private Double goodsAmount;											// 商品总金额

	private Double totalFee;											// 商品总金额

	private Double bonus;												// 使用红包金额

	private Integer goodsCount;											// 订单商品总数

	private Double integralMoney;										// 使用积分金额
	
	private Double discount; 											// 折让

	private String addTime;												// 退单创建时间
	
	private String shippingCode;										// 配送方式
	
	private String shippingName;										// 配送方式名称
	
	private String returnInvoiceNo;										// 退货单快递单号
	
	private String returnReason;										// 退单原因
	
	private String consignee;											// 收货人名字
	
	private String tel;													// 收货人电话

	private String mobile;												// 手机号

	private String email;												// 收货人的电子邮件
	
	private String country;												// 收货人国家
	
	private String province;											// 收货人省份
	
	private String city;												// 收货人城市
	
	private String district;											// 收货人区县
	
	private String address;												// 收货人详细地址
	
	private String zipcode;												// 收货人邮政编码
	
	private String depotCode;											// 发货仓编码
	
	private List<ReturnGoods> returnGoods = new ArrayList<ReturnGoods>();
	
	private List<ReturnPay> returnPays = new ArrayList<ReturnPay>();
	
	private Integer bvValue;											// bvValue
	
	private Double points;												// 点数
	
	private Integer baseBvValue;										// 基础BV
	
	private String areaCode;											// 店长地区编码
	
	private String reasonCode;											// 退单原因编码
	
	private Integer createReturnType;									// 创建退单类型 1:退单; 2:取消
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public Integer getGoodsCount() {
		return goodsCount == null ? 0 : goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getPostscript() {
		return postscript == null ? "" : postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public Double getGoodsAmount() {
		return goodsAmount == null ? 0.00 : goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Double getBonus() {
		return bonus == null ? 0.00 : bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getReturnShippingFee() {
		return returnShippingFee;
	}

	public void setReturnShippingFee(Double returnShippingFee) {
		this.returnShippingFee = returnShippingFee;
	}

	public Double getTotalFee() {
		return totalFee == null ? 0.00 : totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getConsignee() {
		return consignee == null ? "" : consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public List<ReturnGoods> getReturnGoods() {
		return returnGoods;
	}

	public void setReturnGoods(List<ReturnGoods> returnGoods) {
		this.returnGoods = returnGoods;
	}

	public String getShipSn() {
		return shipSn;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public List<ReturnPay> getReturnPays() {
		return returnPays;
	}

	public void setReturnPays(List<ReturnPay> returnPays) {
		this.returnPays = returnPays;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public Integer getHaveRefund() {
		return haveRefund;
	}

	public void setHaveRefund(Integer haveRefund) {
		this.haveRefund = haveRefund;
	}

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getNewOrderSn() {
		return newOrderSn;
	}

	public void setNewOrderSn(String newOrderSn) {
		this.newOrderSn = newOrderSn;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getBvValue() {
		return bvValue;
	}

	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Integer getBaseBvValue() {
		return baseBvValue;
	}

	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public Integer getCreateReturnType() {
		return createReturnType;
	}

	public void setCreateReturnType(Integer createReturnType) {
		this.createReturnType = createReturnType;
	}
}
