package com.work.shop.oms.api.bean;

import java.io.Serializable;


public class OrderGoodsInfo implements Serializable{
	
	/**平台商品相关数据
	 * 
	 */
	private static final long serialVersionUID = 1856821668241632508L;
	
	/**
	 * 订单已退商品数量
	 */
	private int returnGoodsNum;
	
	/**
	 * 商品数量
	 */
	private int goodsNum;
	
	private String subOrderSn;//子订单号
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品颜色名称
	 */
	private String goodsColor;
	
	private String goodsSn;
	
	/**
	 * 商品sku
	 */
	private String skuSn;
	
	/**
	 * 商品规格名称
	 */
	private String goodsSize;
	
	/**
	 * 商品图片
	 */
	private String	goodsUrl;
	
	private double	discountedPrice;
	
	private double	goodsPrice;
	
	private double transactionPrice;
	
	private double settlementPrice;
	
	private String extensionCode;//商品扩展属性
	
	private double payPoints;
	
	private int sendNumber;
	
	private double integralMoney;//使用的积分金额
	
	private int integral;//使用的积分
	
	private String c2mItemStr;
	
	private int goodsStatus;//订单单个商品状态(1、退货中;2、换货中;3、退货完成;4、换货完成)
	
	private String orderFrom;
	
	private int bvValue;
	
	private String expectedShipDate;// 预计发货日
	
	private Double tax;												// 综合税费
	
	private Double taxRate;											// 综合税率
	
	private Integer baseBvValue;									// 基础BV值
	
	private String depotCode;										// 发货仓编码

    private Integer minBuyNum;                                      //最小购买量

    private String buyUnit;                                         //购买单位

    private String customerMaterialCode;                            //客户物料编码

    private String distributionCategory;                            //配送周期

    private int canChangeNum;                                       //可申请售后数量
	
	public int getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(int goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsColor() {
		return goodsColor;
	}
	public void setGoodsColor(String goodsColor) {
		this.goodsColor = goodsColor;
	}
	public String getGoodsSize() {
		return goodsSize;
	}
	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}
	public int getReturnGoodsNum() {
		return returnGoodsNum;
	}
	public void setReturnGoodsNum(int returnGoodsNum) {
		this.returnGoodsNum = returnGoodsNum;
	}
	public double getPayPoints() {
		return payPoints;
	}
	public void setPayPoints(double payPoints) {
		this.payPoints = payPoints;
	}
	public String getExtensionCode() {
		return extensionCode;
	}
	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
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
	public int getSendNumber() {
		return sendNumber;
	}
	public void setSendNumber(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	public String getC2mItemStr() {
		return c2mItemStr;
	}
	public void setC2mItemStr(String c2mItemStr) {
		this.c2mItemStr = c2mItemStr;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public double getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	public String getSubOrderSn() {
		return subOrderSn;
	}
	public void setSubOrderSn(String subOrderSn) {
		this.subOrderSn = subOrderSn;
	}
	public int getBvValue() {
		return bvValue;
	}
	public void setBvValue(int bvValue) {
		this.bvValue = bvValue;
	}
	public String getExpectedShipDate() {
		return expectedShipDate;
	}
	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	
	public Double getTax() {
		return tax == null ? 0.00 : tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTaxRate() {
		return taxRate == null ? 0.00 : taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
	}

	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

    public Integer getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(Integer minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    public String getCustomerMaterialCode() {
        return customerMaterialCode;
    }

    public void setCustomerMaterialCode(String customerMaterialCode) {
        this.customerMaterialCode = customerMaterialCode;
    }

    public String getDistributionCategory() {
        return distributionCategory;
    }

    public void setDistributionCategory(String distributionCategory) {
        this.distributionCategory = distributionCategory;
    }

    public int getCanChangeNum() {
        return canChangeNum;
    }

    public void setCanChangeNum(int canChangeNum) {
        this.canChangeNum = canChangeNum;
    }
}
