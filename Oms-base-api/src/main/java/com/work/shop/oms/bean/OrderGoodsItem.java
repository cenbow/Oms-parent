package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品信息
 * @author QuYachu
 *
 */
public class OrderGoodsItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单号
	 */
	private String masterOrderSn;
	
	private String outerOrderSn; // 外部订单号
	
	private String userId; // 下单人
	
	private String orderType; // 订单类型
	
	private Integer orderStatus; // 订单状态

	private Integer payStatus; // 支付状态

	private Integer shipStatus; // 发货状态
	
	private String addTime; // 下单时间
	
	private String orderFrom; // 订单渠道店铺
	
	private String referer; // 订单来源
	
	private String channelCode; // 平台编码
	
	private String insteadUserId; // BD编码
	
	private String depotCode; // 仓库编码
	
	private String goodsName; // 商品名称
	
	private String goodsSn; // 商品编码
	
	private String customCode; // 商品sku
	
	private String extensionCode; // 商品类型
	
	/**
	 * 商品数量
	 */
	private Integer goodsNumber;
	
	/**
	 * 商品售价
	 */
	private BigDecimal goodsPrice;
	
	/**
	 * 商品总售价
	 */
	private BigDecimal totalGoodsPrice;
	
	/**
	 * 商品成交价
	 */
	private BigDecimal transactionPrice;
	
	/**
	 * 商品总成交价
	 */
	private BigDecimal totalTransactionPrice;
	
	/**
	 * 商品结算价
	 */
	private BigDecimal settlementPrice;
	
	private BigDecimal discount; // 优惠价
	
	private BigDecimal shareBonus; // 红包优惠价
	
	private String promotionId; // 促销id
	
	/**
	 * 商品sap码
	 */
	private String sap;
	
	/**
	 * 箱规
	 */
	private Integer boxGauge;
	
	/**
	 * 成本价
	 */
	private BigDecimal costPrice; 
	
	/**
	 * 进项税
	 */
	private BigDecimal inputTax;
	
	/**
	 * 出项税
	 */
	private BigDecimal outputTax; 
	
	/**
	 * 收货人
	 */
	private String consignee;
	
	/**
	 * 收货人手机号码
	 */
	private String mobile;
	
	/**
	 * 收货人地址
	 */
	private String address;

    /**
     * 支付方式
     */
    private String payMethod;

	/**
	 * 下单公司名称
	 */
	private String companyName;

	/**
	 * 物料描述
	 */
	private String customerMaterialName;

	/**
	 * 客户物料编码
	 */
	private String customerMaterialCode;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public String getOuterOrderSn() {
		return outerOrderSn;
	}

	public String getUserId() {
		return userId;
	}

	public String getOrderType() {
		return orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public Integer getShipStatus() {
		return shipStatus;
	}

	public String getAddTime() {
		return addTime;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public String getReferer() {
		return referer;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public String getInsteadUserId() {
		return insteadUserId;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public String getCustomCode() {
		return customCode;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public BigDecimal getShareBonus() {
		return shareBonus;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public String getSap() {
		return sap;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public BigDecimal getInputTax() {
		return inputTax;
	}

	public BigDecimal getOutputTax() {
		return outputTax;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public void setShipStatus(Integer shipStatus) {
		this.shipStatus = shipStatus;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setInsteadUserId(String insteadUserId) {
		this.insteadUserId = insteadUserId;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public void setShareBonus(BigDecimal shareBonus) {
		this.shareBonus = shareBonus;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public void setInputTax(BigDecimal inputTax) {
		this.inputTax = inputTax;
	}

	public void setOutputTax(BigDecimal outputTax) {
		this.outputTax = outputTax;
	}

	public BigDecimal getTotalGoodsPrice() {
		return totalGoodsPrice;
	}

	public void setTotalGoodsPrice(BigDecimal totalGoodsPrice) {
		this.totalGoodsPrice = totalGoodsPrice;
	}

	public BigDecimal getTotalTransactionPrice() {
		return totalTransactionPrice;
	}

	public void setTotalTransactionPrice(BigDecimal totalTransactionPrice) {
		this.totalTransactionPrice = totalTransactionPrice;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerMaterialName() {
		return customerMaterialName;
	}

	public void setCustomerMaterialName(String customerMaterialName) {
		this.customerMaterialName = customerMaterialName;
	}

	public String getCustomerMaterialCode() {
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode) {
		this.customerMaterialCode = customerMaterialCode;
	}
}
