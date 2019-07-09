package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DistributeOrder implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	private String masterOrderSn;										// 订单号
	
	private String shipSn;												// 交货单号
	
	private Integer orderType;											// 订单类型  0：正常订单 2：换货订单
	
	private String relatingShipSn;										// 换货单关联的原订单号
	
	private String relatingReturnSn;									// 换货单关联退货单号

	private String userId;												// 下单人ID
	
	private String registerMobile;										// 注册手机号码
	
	private String consignee;											// 收货人名字

	private Integer orderStatus;										// 订单状态

	private String shopCode;											// 订单来源（渠道）

	private String referer;												// 订单设备的来源
	
	private String invType;												// 发票类型

	private String invPayee;											// 发票抬头

	private String invContent;											// 发票内容

	private Double tax;													// 发票税额

	private Double shippingTotalFee;									// 配送总费用

	private String postscript;											// 订单附言

	private Double goodsAmount;											// 商品总金额

	private Double totalFee;											// 商品总金额

	private Double moneyPaid;											// 已付款金额

	private Double bonus;												// 使用红包金额

	private Integer goodsCount;											// 订单商品总数

	private Double discount;											// 折扣金额

	private Double integralMoney;										// 使用积分金额

	private String addTime;												// 订单创建时间

	private String payTime;												// 订单支付时间
	
	private String tel;													// 收货人电话

	private String mobile;												// 手机号

	private String email;												// 收货人的电子邮件

	private String prName;												// 订单促销
	
	private String prIds;												// 订单促销ID
	
	private short isGroup;												// 订单是否为团购
	
	private short isAdvance;											// 是否为预售商品
	
	private String shippingCode;										// 配送方式
	
	private String shippingName;										// 配送方式名称
	
	private String country;												// 收货人国家
	
	private String province;											// 收货人省份
	
	private String city;												// 收货人城市
	
	private String district;											// 收货人区县
	
	private String address;												// 收货人详细地址
	
	private String zipcode;												// 收货人邮政编码
	
	private String bestTime;											// 最佳送货时间
	
	private String depotCode;											// 发货仓编码
	
	private Integer bvValue;											// bvValue
	
	private Double points;												// 点数
	
	private String expectedShipDate;									// 预计发货日
	
	private Double complexTax;											// 综合税费
	
 	private int sourceType;												// '订单来源类型  1:跨境订单;3:线上订单(B2C);'
 	
	private String userCardNo;											// 用户身份证号码

	private String userCardName;										// 用户身份证姓名
	
	private Integer baseBvValue;										// 基础BV
	
	private String provinceCode;										// 店长省份编码
	
	private String cityCode;											// 店长市编码

	private String districtCode;										// 店长区县编码
	
	private String areaCode;											// 店长地区编码
	
	private Integer isCac;												// 是否自提 0:否 1:是
	
	private Integer pushType;											// 推送类型 0：下单; 1: 支付; 2: 取消
	
	private String shippingAddress;										// 收货地址

	private List<DistributeGoods> distributeGoods = new ArrayList<DistributeGoods>();
	
	private List<DistributePay> distributePays = new ArrayList<DistributePay>();
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getOrderStatus() {
		return orderStatus == null ? 0 : orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getReferer() {
		return referer == null ? "" : referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
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

	public Double getMoneyPaid() {
		return moneyPaid == null ? 0.00 : moneyPaid;
	}

	public void setMoneyPaid(Double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public Double getBonus() {
		return bonus == null ? 0.00 : bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getDiscount() {
		return discount == null ? 0.00 : discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getInvType() {
		return invType == null ? "" : invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getInvPayee() {
		return invPayee == null ? "" : invPayee;
	}

	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}

	public String getInvContent() {
		return invContent == null ? "" : invContent;
	}

	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}

	public Double getTax() {
		return tax == null ? 0.00 : tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getShippingTotalFee() {
		return shippingTotalFee == null ? 0.00 : shippingTotalFee;
	}

	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
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

	public String getPrName() {
		if (prName != null) {
			if (prName.length() > 255) {
				prName = prName.substring(0, 254);
			}
		}
		return prName == null ? "" : prName;
	}

	public void setPrName(String prName) {
		this.prName = prName;
	}

	public short getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(short isGroup) {
		this.isGroup = isGroup;
	}

	public short getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(short isAdvance) {
		this.isAdvance = isAdvance;
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

	public String getBestTime() {
		return bestTime;
	}

	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public List<DistributeGoods> getDistributeGoods() {
		return distributeGoods;
	}

	public void setDistributeGoods(List<DistributeGoods> distributeGoods) {
		this.distributeGoods = distributeGoods;
	}

	public String getShipSn() {
		return shipSn;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public String getRegisterMobile() {
		return registerMobile;
	}

	public void setRegisterMobile(String registerMobile) {
		this.registerMobile = registerMobile;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPrIds() {
		return prIds;
	}

	public void setPrIds(String prIds) {
		this.prIds = prIds;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public List<DistributePay> getDistributePays() {
		return distributePays;
	}

	public void setDistributePays(List<DistributePay> distributePays) {
		this.distributePays = distributePays;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getRelatingShipSn() {
		return relatingShipSn;
	}

	public void setRelatingShipSn(String relatingShipSn) {
		this.relatingShipSn = relatingShipSn;
	}

	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}

	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
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

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public Double getComplexTax() {
		return complexTax;
	}

	public void setComplexTax(Double complexTax) {
		this.complexTax = complexTax;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getUserCardName() {
		return userCardName;
	}

	public void setUserCardName(String userCardName) {
		this.userCardName = userCardName;
	}

	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
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

	public Integer getIsCac() {
		return isCac == null ? 0 : isCac;
	}

	public void setIsCac(Integer isCac) {
		this.isCac = isCac;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getShippingAddress() {
		return shippingAddress == null ? "" : shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
}
