package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单商品详情
 * @author QuYachu
 */
public class OrderItemGoodsDetail implements Serializable {

	private static final long serialVersionUID = 8132744347474246081L;

	/**
	 * 货号
	 */
	private String goodsSn;


	private String sellerCode;
	/**
	 * 条形码
	 */
	private String barcode;

	/**
	 * 打折券金额
	 */
	private String cardMoney;

	/**
	 * 可用数量
	 */
	private String availableNumber;

	// 发货仓字段
	private Byte shippingId;
	private String shippingName;
	private Integer deliveryType;
	private String bak1;
	private Short regionId;
	private Date depotTime;
	private String pdwarhCode;
	private String pdwarhName;
	private String toUser;
	private String toUserPhone;
	private String provincecity;
	private String overTransCycle;

	/**
	 * 快递单号
	 */
	private String invoiceNo;

	private Byte shippingStatus;

	/**
	 * 发货时间
	 */
	private Date deliveryTime;

	private Date pickupDate;

	private String shippingStatusName;

	/**
	 * 打折券列表
	 */
	private List<Map> couponList;

	/**
	 * 缺货数量
	 */
	private String lackNum;

	/**
	 * 颜色码
	 */
	private String colorCode;

	/**
	 * 尺码
	 */
	private String sizeCode;

	/**
	 * 颜色名
	 */
	private String colorName;

	/**
	 * 尺码名
	 */
	private String sizeName;

	/**
	 * 订单商品小计
	 */
	private BigDecimal subTotal;

	/**
	 * 订单商品小计
	 */
    private String subTotalStr;

	/**
	 * 初始商品数量
	 */
	private Integer initGoodsNumber;

	/**
	 * 当前商品尺码
	 */
	private String currSizeCode;

	/**
	 * 当前商品颜色码
	 */
	private String currColorCode;

	/**
	 * 当前商品尺名名
	 */
	private String currSizeName;

	/**
	 * 当前商品颜色名
	 */
	private String currColorName;

	/**
	 * 初始积分金额
	 */
	private BigDecimal initIntegralMoney;

	/**
	 * 缺货商品数量
	 */
	private Integer initLackNum;

	/**
	 * 待退货数量
	 */
	private String returnRemainNum;

	/**
	 * 已退数量
	 */
	private String returnNum;

	/**
	 * 交货单状态
	 */
	private String orderStatus;

	private Long goodsId;

	private String depotCode;

	private String masterOrderSn;

	private String orderSn;

	private String goodsName;

	private String customCode;

	private String extensionCode;

	private String extensionId;

	private Integer goodsNumber;

	private BigDecimal goodsDecimalNumber;

	private BigDecimal goodsPrice;

	private BigDecimal goodsPriceNoTax;

	private BigDecimal transactionPrice;

	private BigDecimal transactionPriceNoTax;

	private BigDecimal settlementPrice;

	private Float discount;

	private BigDecimal integralMoney;

	private Integer integral;

	private String groupName;

	private String goodsSizeName;

	private String goodsColorName;

	private String goodsThumb;

	private Integer sendNumber;

	private String parentSn;

	private String mergeFrom;

	private String exchangeFrom;

	private String promotionDesc;

	private BigDecimal rankPoints;

	private BigDecimal payPoints;

	private String useCard;

	private BigDecimal shareBonus;

	private BigDecimal shareSurplus;

	private Byte madeFlag;

	private Integer chargeBackCount;

	private String containerid;

	private String selleruser;

	private Integer isDel;

	private String promotionId;

	private String c2mItem;

	private Integer protectFalg;

	private String supplierCode;

	private Integer salesMode;

	private String sap;

	private String bvValue;

	private Integer baseBvValue;

	private String expectedShipDate;

	/**
	 * 综合税费
	 */
	private BigDecimal tax;

	/**
	 * 综合税率
	 */
	private BigDecimal taxRate;

	/**
	 * 市场价
	 */
	private float marketPrice;

	/**
	 * 箱规
	 */
	private Integer boxGauge;

    /**
     * 最小购买量
     */
    private Integer minBuyNum;

    /**
     * 客户物料编码
     */
    private String customerMaterialCode;

    /**
     * 购买单位
     */
    private String buyUnit;

    /**
     * 发货周期
     */
    private String deliveryCycle;

    /**
     * 商品销售属性
     */
    private String goodsProp;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商描述
     */
    private String customerMaterialName;

    /**
     * 采购申请编号
     */
    private String buyerNo;

    /**
     * 采购申请行号
     */
    private String buyerLineNo;

	/**
	 * 进项税
	 */
	private BigDecimal inputTax;

	/**
	 * 销项税
	 */
	private BigDecimal outputTax;

	/**
	 * 协议价
	 */
	private BigDecimal costPrice;

    /**
     * 加价金额
     */
	private BigDecimal goodsAddPrice;

	/**
	 * 商品购买数量中走库存的数量
	 */
	private Integer withStockNumber;

	/**
	 * 商品购买数量中不走库存的数量
	 */
	private Integer withoutStockNumber;

	/**
	 * 商品销售类型
	 */
	private Integer saleType;

	/**
	 * 保护价
	 */
	private BigDecimal protectPrice;

	/**
	 * 商品账期字典id，关联商品库system_payment_period表的id
	 */
	private Integer goodsPaymentPeriodId;

	/**
	 * 商城商品是否支持无库存下单 0：不支持 1：支持
	 */
	private Integer purchasesWithoutStockFlag;

	/**
	 * 无库存下单发货周期
	 */
	private String withoutStockDeliveryCycle;

	/**
	 * 无库存下单补库存时使用的仓库
	 */
	private String withoutStockDepotNo;

	/**
	 * 商品数据来源  1、是店铺普通模板 2、店铺快速模板 3、快速自营模板
	 */
	private Integer  dataSources;

    public BigDecimal getGoodsAddPrice() {
        return goodsAddPrice;
    }

    public void setGoodsAddPrice(BigDecimal goodsAddPrice) {
        this.goodsAddPrice = goodsAddPrice;
    }

    public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode == null ? null : depotCode.trim();
	}

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn == null ? null : orderSn.trim();
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode == null ? null : customCode.trim();
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode == null ? null : extensionCode.trim();
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId == null ? null : extensionId.trim();
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public BigDecimal getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(BigDecimal integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName == null ? null : groupName.trim();
	}

	public String getGoodsSizeName() {
		return goodsSizeName;
	}

	public void setGoodsSizeName(String goodsSizeName) {
		this.goodsSizeName = goodsSizeName == null ? null : goodsSizeName.trim();
	}

	public String getGoodsColorName() {
		return goodsColorName;
	}

	public void setGoodsColorName(String goodsColorName) {
		this.goodsColorName = goodsColorName == null ? null : goodsColorName.trim();
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb == null ? null : goodsThumb.trim();
	}

	public Integer getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}

	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn == null ? null : parentSn.trim();
	}

	public String getMergeFrom() {
		return mergeFrom;
	}

	public void setMergeFrom(String mergeFrom) {
		this.mergeFrom = mergeFrom == null ? null : mergeFrom.trim();
	}

	public String getExchangeFrom() {
		return exchangeFrom;
	}

	public void setExchangeFrom(String exchangeFrom) {
		this.exchangeFrom = exchangeFrom == null ? null : exchangeFrom.trim();
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc == null ? null : promotionDesc.trim();
	}

	public BigDecimal getRankPoints() {
		return rankPoints;
	}

	public void setRankPoints(BigDecimal rankPoints) {
		this.rankPoints = rankPoints;
	}

	public BigDecimal getPayPoints() {
		return payPoints;
	}

	public void setPayPoints(BigDecimal payPoints) {
		this.payPoints = payPoints;
	}

	public String getUseCard() {
		return useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard == null ? null : useCard.trim();
	}

	public BigDecimal getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(BigDecimal shareBonus) {
		this.shareBonus = shareBonus;
	}

	public BigDecimal getShareSurplus() {
		return shareSurplus;
	}

	public void setShareSurplus(BigDecimal shareSurplus) {
		this.shareSurplus = shareSurplus;
	}

	public Byte getMadeFlag() {
		return madeFlag;
	}

	public void setMadeFlag(Byte madeFlag) {
		this.madeFlag = madeFlag;
	}

	public Integer getChargeBackCount() {
		return chargeBackCount;
	}

	public void setChargeBackCount(Integer chargeBackCount) {
		this.chargeBackCount = chargeBackCount;
	}

	public String getContainerid() {
		return containerid;
	}

	public void setContainerid(String containerid) {
		this.containerid = containerid == null ? null : containerid.trim();
	}

	public String getSelleruser() {
		return selleruser;
	}

	public void setSelleruser(String selleruser) {
		this.selleruser = selleruser == null ? null : selleruser.trim();
	}

	public Integer getIsDel() {
		return isDel;
	}

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId == null ? null : promotionId.trim();
	}

	public String getC2mItem() {
		return c2mItem;
	}

	public void setC2mItem(String c2mItem) {
		this.c2mItem = c2mItem == null ? null : c2mItem.trim();
	}

	public Integer getProtectFalg() {
		return protectFalg;
	}

	public void setProtectFalg(Integer protectFalg) {
		this.protectFalg = protectFalg;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode == null ? null : supplierCode.trim();
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCardMoney() {
		return cardMoney;
	}

	public void setCardMoney(String cardMoney) {
		this.cardMoney = cardMoney;
	}

	public String getAvailableNumber() {
		return availableNumber;
	}

	public void setAvailableNumber(String availableNumber) {
		this.availableNumber = availableNumber;
	}

	public Byte getShippingId() {
		return shippingId;
	}

	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public Short getRegionId() {
		return regionId;
	}

	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}

	public Date getDepotTime() {
		return depotTime;
	}

	public void setDepotTime(Date depotTime) {
		this.depotTime = depotTime;
	}

	public String getPdwarhCode() {
		return pdwarhCode;
	}

	public void setPdwarhCode(String pdwarhCode) {
		this.pdwarhCode = pdwarhCode;
	}

	public String getPdwarhName() {
		return pdwarhName;
	}

	public void setPdwarhName(String pdwarhName) {
		this.pdwarhName = pdwarhName;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getToUserPhone() {
		return toUserPhone;
	}

	public void setToUserPhone(String toUserPhone) {
		this.toUserPhone = toUserPhone;
	}

	public String getProvincecity() {
		return provincecity;
	}

	public void setProvincecity(String provincecity) {
		this.provincecity = provincecity;
	}

	public String getOverTransCycle() {
		return overTransCycle;
	}

	public void setOverTransCycle(String overTransCycle) {
		this.overTransCycle = overTransCycle;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Byte getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Byte shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getShippingStatusName() {
		return shippingStatusName;
	}

	public void setShippingStatusName(String shippingStatusName) {
		this.shippingStatusName = shippingStatusName;
	}

	public List<Map> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Map> couponList) {
		this.couponList = couponList;
	}

	public String getLackNum() {
		return lackNum;
	}

	public void setLackNum(String lackNum) {
		this.lackNum = lackNum;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public BigDecimal getSubTotal() {
        if (null == subTotal || subTotal.doubleValue() == 0) {
            if (null != sellerCode && "hbis".equalsIgnoreCase(sellerCode)) {
                if (null == transactionPriceNoTax) {
                    transactionPriceNoTax = BigDecimal.ZERO;
                }
                if (null == goodsNumber && (goodsDecimalNumber == null || goodsDecimalNumber.compareTo(BigDecimal.ZERO) == 0)) {
                    subTotal = BigDecimal.ZERO;
                } else {
                    BigDecimal number = new BigDecimal(goodsNumber);
                    if (goodsDecimalNumber != null && goodsDecimalNumber.compareTo(BigDecimal.ZERO) != 0) {
                        number = number.add(goodsDecimalNumber);
                    }
                    BigDecimal noTax = transactionPriceNoTax.multiply(number).setScale(2, BigDecimal.ROUND_HALF_UP);
                    if (null == outputTax) {
                        outputTax = BigDecimal.ZERO;
                    }
                    BigDecimal tax = transactionPriceNoTax.multiply(outputTax).multiply(number).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    subTotal = noTax.add(tax).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
            } else {
                if (null == transactionPrice) {
                    transactionPrice = new BigDecimal(0.00);
                }
                if (null == goodsNumber && (goodsDecimalNumber == null || goodsDecimalNumber.compareTo(BigDecimal.ZERO) == 0)) {
                    subTotal = new BigDecimal(0.00);
                } else {
                    BigDecimal number = new BigDecimal(goodsNumber);
                    if (goodsDecimalNumber != null && goodsDecimalNumber.compareTo(BigDecimal.ZERO) != 0) {
                        number = number.add(goodsDecimalNumber);
                    }
                    subTotal = transactionPrice.multiply(number).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //subTotal = transactionPrice.doubleValue() * goodsNumber;
                }
            }
        }
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public Integer getInitGoodsNumber() {
		return initGoodsNumber;
	}

	public void setInitGoodsNumber(Integer initGoodsNumber) {
		this.initGoodsNumber = initGoodsNumber;
	}

	public String getCurrSizeCode() {
		return currSizeCode;
	}

	public void setCurrSizeCode(String currSizeCode) {
		this.currSizeCode = currSizeCode;
	}

	public String getCurrColorCode() {
		return currColorCode;
	}

	public void setCurrColorCode(String currColorCode) {
		this.currColorCode = currColorCode;
	}

	public BigDecimal getInitIntegralMoney() {
		return initIntegralMoney;
	}

	public void setInitIntegralMoney(BigDecimal initIntegralMoney) {
		this.initIntegralMoney = initIntegralMoney;
	}

	public String getCurrSizeName() {
		return currSizeName;
	}

	public void setCurrSizeName(String currSizeName) {
		this.currSizeName = currSizeName;
	}

	public String getCurrColorName() {
		return currColorName;
	}

	public void setCurrColorName(String currColorName) {
		this.currColorName = currColorName;
	}

	public Integer getInitLackNum() {
		return initLackNum;
	}

	public void setInitLackNum(Integer initLackNum) {
		this.initLackNum = initLackNum;
	}

	public String getReturnRemainNum() {
		return returnRemainNum;
	}

	public void setReturnRemainNum(String returnRemainNum) {
		this.returnRemainNum = returnRemainNum == null ? "" : returnRemainNum;
	}

	public String getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(String returnNum) {
		this.returnNum = returnNum == null ? "" : returnNum;
	}

	public Integer getSalesMode() {
		return salesMode;
	}

	public void setSalesMode(Integer salesMode) {
		this.salesMode = salesMode;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public String getBvValue() {
		return bvValue;
	}

	public void setBvValue(String bvValue) {
		this.bvValue = bvValue;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getBaseBvValue() {
		return baseBvValue;
	}

	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}

	public float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

    public Integer getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(Integer minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getCustomerMaterialCode() {
        return customerMaterialCode;
    }

    public void setCustomerMaterialCode(String customerMaterialCode) {
        this.customerMaterialCode = customerMaterialCode;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getGoodsProp() {
        return goodsProp;
    }

    public void setGoodsProp(String goodsProp) {
        this.goodsProp = goodsProp;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSubTotalStr() {
        BigDecimal subTotal = getSubTotal();
        subTotalStr = "";
        if (subTotal != null) {
            subTotalStr = subTotal.toString();
        }
        return subTotalStr;
    }

    public void setSubTotalStr(String subTotalStr) {
        this.subTotalStr = subTotalStr;
    }

    public String getCustomerMaterialName() {
        return customerMaterialName;
    }

    public void setCustomerMaterialName(String customerMaterialName) {
        this.customerMaterialName = customerMaterialName;
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

	public BigDecimal getInputTax() {
		return inputTax;
	}

	public void setInputTax(BigDecimal inputTax) {
		this.inputTax = inputTax;
	}

	public BigDecimal getOutputTax() {
		return outputTax;
	}

	public void setOutputTax(BigDecimal outputTax) {
		this.outputTax = outputTax;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getGoodsDecimalNumber() {
		return goodsDecimalNumber;
	}

	public void setGoodsDecimalNumber(BigDecimal goodsDecimalNumber) {
		this.goodsDecimalNumber = goodsDecimalNumber;
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

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

	public BigDecimal getProtectPrice() {
		return protectPrice;
	}

	public void setProtectPrice(BigDecimal protectPrice) {
		this.protectPrice = protectPrice;
	}

	public Integer getGoodsPaymentPeriodId() {
		return goodsPaymentPeriodId;
	}

	public void setGoodsPaymentPeriodId(Integer goodsPaymentPeriodId) {
		this.goodsPaymentPeriodId = goodsPaymentPeriodId;
	}

	public Integer getPurchasesWithoutStockFlag() {
		return purchasesWithoutStockFlag;
	}

	public void setPurchasesWithoutStockFlag(Integer purchasesWithoutStockFlag) {
		this.purchasesWithoutStockFlag = purchasesWithoutStockFlag;
	}

	public String getWithoutStockDeliveryCycle() {
		return withoutStockDeliveryCycle;
	}

	public void setWithoutStockDeliveryCycle(String withoutStockDeliveryCycle) {
		this.withoutStockDeliveryCycle = withoutStockDeliveryCycle;
	}

	public String getWithoutStockDepotNo() {
		return withoutStockDepotNo;
	}

	public void setWithoutStockDepotNo(String withoutStockDepotNo) {
		this.withoutStockDepotNo = withoutStockDepotNo;
	}

	public Integer getDataSources() {
		return dataSources;
	}

	public void setDataSources(Integer dataSources) {
		this.dataSources = dataSources;
	}

	public BigDecimal getGoodsPriceNoTax() {
		return goodsPriceNoTax;
	}

	public void setGoodsPriceNoTax(BigDecimal goodsPriceNoTax) {
		this.goodsPriceNoTax = goodsPriceNoTax;
	}

	public BigDecimal getTransactionPriceNoTax() {
		return transactionPriceNoTax;
	}

	public void setTransactionPriceNoTax(BigDecimal transactionPriceNoTax) {
		this.transactionPriceNoTax = transactionPriceNoTax;
	}
}
