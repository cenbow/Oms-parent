package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单商品信息
 * @author QuYachu
 */
public class OrderGoodsInfo implements Serializable{

	private static final long serialVersionUID = 1856821668241632508L;
	
	/**
	 * 订单已退商品数量
	 */
	private int returnGoodsNum;
	
	/**
	 * 商品数量
	 */
	private int goodsNum;

	/**
	 * 交货单号
	 */
	private String subOrderSn;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品颜色名称
	 */
	private String goodsColor;

	/**
	 * 商品编码
	 */
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

	/**
	 * 商品扩展属性
	 */
	private String extensionCode;
	
	private double payPoints;
	
	private int sendNumber;

	/**
	 * 使用的积分金额
	 */
	private double integralMoney;

	/**
	 * 使用的积分
	 */
	private int integral;
	
	private String c2mItemStr;

	/**
	 * 采购申请号集合
	 */
	private List<String> buyerNoList;

	/**
	 * 采购申请行项目号集合
	 */
	private List<String> buyerLineNoList;

	/**
	 * 采购申请行项目商品数量
	 */
	private List<String> buyerGoodsNum;


	/**
	 * 订单单个商品状态(1、退货中;2、换货中;3、退货完成;4、换货完成)
	 */
	private int goodsStatus;
	
	private String orderFrom;
	
	private int bvValue;

	/**
	 * 预计发货日
	 */
	private String expectedShipDate;

	/**
	 * 综合税费
	 */
	private Double tax;

	/**
	 * 综合税率
	 */
	private Double taxRate;

	/**
	 * 基础BV值
	 */
	private Integer baseBvValue;

	/**
	 * 发货仓编码
	 */
	private String depotCode;

	/**
	 * 最小购买量
	 */
    private Integer minBuyNum;

	/**
	 * 购买单位
	 */
    private String buyUnit;

	/**
	 * 客户物料编码
	 */
    private String customerMaterialCode;

	/**
	 * 采购申请编号
	 */
	private String buyerNo;

	/**
	 * 采购申请行号
	 */
	private String buyerLineNo;

	/**
	 * 配送周期
	 */
    private String distributionCategory;

	/**
	 * 可申请售后数量
	 */
    private int canChangeNum;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

	/**
	 * 商品是否支持无库存下单 0：不支持 1：支持
	 */
	private Integer purchasesWithoutStockFlag;

	/**
	 * 商品走库存的数量
	 */
	private Integer withStockNumber;

	/**
	 * 商品不走库存的数量
	 */
	private Integer withoutStockNumber;

	/**
	 * 无库存下单发货周期
	 */
	private String withoutStockDeliveryCycle;

	/**
	 * 有货发货周期
	 */
	private String deliveryCycle;

	/**
	 * 商品销售类型
	 */
	private Integer saleType;

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

    public String getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(String buyerNo) {
        this.buyerNo = buyerNo;
    }

    public String getBuyerLineNo() {
        return buyerLineNo;
    }

    public void setBuyerLineNo(String buyerLineNo) {
        this.buyerLineNo = buyerLineNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

	public List<String> getBuyerNoList() {
		return buyerNoList;
	}

	public void setBuyerNoList(List<String> buyerNoList) {
		this.buyerNoList = buyerNoList;
	}

	public List<String> getBuyerLineNoList() {
		return buyerLineNoList;
	}

	public void setBuyerLineNoList(List<String> buyerLineNoList) {
		this.buyerLineNoList = buyerLineNoList;
	}

	public List<String> getBuyerGoodsNum() {
		return buyerGoodsNum;
	}

	public void setBuyerGoodsNum(List<String> buyerGoodsNum) {
		this.buyerGoodsNum = buyerGoodsNum;
	}

	public Integer getPurchasesWithoutStockFlag() {
		return purchasesWithoutStockFlag;
	}

	public void setPurchasesWithoutStockFlag(Integer purchasesWithoutStockFlag) {
		this.purchasesWithoutStockFlag = purchasesWithoutStockFlag;
	}

	public Integer getWithStockNumber() {
		return withStockNumber;
	}

	public void setWithStockNumber(Integer withStockNumber) {
		this.withStockNumber = withStockNumber;
	}

	public Integer getWithoutStockNumber() {
		return withoutStockNumber;
	}

	public void setWithoutStockNumber(Integer withoutStockNumber) {
		this.withoutStockNumber = withoutStockNumber;
	}

	public String getWithoutStockDeliveryCycle() {
		return withoutStockDeliveryCycle;
	}

	public void setWithoutStockDeliveryCycle(String withoutStockDeliveryCycle) {
		this.withoutStockDeliveryCycle = withoutStockDeliveryCycle;
	}

	public String getDeliveryCycle() {
		return deliveryCycle;
	}

	public void setDeliveryCycle(String deliveryCycle) {
		this.deliveryCycle = deliveryCycle;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
}
