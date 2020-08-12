package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.bean.ProductBarcodeList;

public class OrderGoodsUpdateBean implements Serializable, Comparable<OrderGoodsUpdateBean>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7043034332864312123L;
	
	private String masterOrderSn;
	
	private Long id;
	
	private Long indexId;
	
	private String depotCode;

	private String orderSn;

	private String goodsName;

	private String customCode;

	private Integer goodsNumber;

	private BigDecimal marketPrice;
	
	private Double doubleMarketPrice = 0.0D;

	private BigDecimal settlementPrice;
	
	private Double doubleSettlementPrice = 0.0D;

	private BigDecimal goodsPrice;
	
	private Double doubleGoodsPrice = 0.0D;

	private Float discount = 0.0F;

	private BigDecimal transactionPrice;
	
	private Double doubleTransactionPrice = 0.0D;

	private String groupName;

	private String goodsSizeName;

	private String goodsColorName;

	private String goodsThumb;

	private Integer sendNumber;

	private Integer isReal;

	private String extensionCode;

	private String extensionId;

	private Short isGift;

	private Integer isRejected;

	private String mergeFrom;

	private String exchangeFrom;

	private String splitTo;

	private String promotionDesc;

	private BigDecimal rankPoints;
	
	private Double doubleRankPoints  = 0D;

	private BigDecimal payPoints;
	
	private Double doublePayPoints = 0D;

	private String useCard;

	private BigDecimal shareBonus;

	private Double doubleShareBonus = 0D;
	
	private BigDecimal shareSurplus;
	
	private Double doubleShareSurplus = 0D;

	private BigDecimal realPayFee;
	
	private Double doubleRealPayFee = 0D;

	private Byte activityType;

	private String madeUrl;

	private Byte madeFlag;

	private Integer chargeBackCount;

	private String containerid;

	private String selleruser;

	private String goodsAttr;
	
	private String parentSn;
	
	private Double subTotal = 0D;									// 订单商品小计
	
	private String brandCode;									// 订单商品品牌

	private Long warehouseId;									// 发货单分仓ID
	
	private String warehCode;									// 发货单分仓CODE

	private String barcode;										// 条形码
	
	private Integer availableNumber;							// 库存量
	
	private String goodsSn;										// 商品编码
	
	private String currColorCode;								//当前商品颜色码

	private String currSizeCode;								//当前商品尺码
	
	private String currColorName;								// 当前商品颜色码名称

	private String currSizeName;								// 当前商品尺码名称
	
	private Integer returnNum;									//退单已退数量
	
	private Integer returnRemainNum;							//待退数量

	private Integer shippingStatus;								//配送状态
	
	private String shippingName;								//配送方式
	
	private String invoiceNo;										//快递单号

	private Integer occupiedAvailable;
	
	private Double goodsTotal = 0.0D;								// 订单商品合计
	
	//商品六位码获取的产品码信息
	private List<ProductBarcodeList> barcodeChild;
	
	private List<ProductBarcodeList> colorChild;					// 颜色码列表
	
	private List<ProductBarcodeList> sizeChild;						// 尺码列表
	
	private List<CardCoupon> cardCoupons;							//打折券
	
	private String isCardCoupon;									//是否使用打折券  0:使用、1:不使用
	
	private String singleShareBonus;								//单件商品分摊红包金额
	
	private Integer initGoodsNumber;								// 原商品数量
	
	private String uniqueKey;										// 订单商品唯一键
	
	private Integer lackNum;										// 缺货商品数量
	
	private Integer initLackNum;									// 缺货商品数量

	private Double integralMoney;									// 订单商品使用积分金额
	
	private Double initIntegralMoney;								// 订单商品初始使用积分金额
	
	private Integer integral;										// 订单商品使用积分数量
	
	private String supplierCode;									// 供应商编码
	
	private String supplierName;									// 供应商名称
	
	private String c2mItem;											// 订制属性列表
	
	private Integer deliveryType;									// 配送类型  1-门店发货单；2-工厂发货单；3-第三方仓库发货单
	
	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getSubTotal() {
		if (null == subTotal || subTotal.doubleValue() == 0) {
			if (null == transactionPrice) {
				transactionPrice = new BigDecimal(0.00);
			}
			if (null == goodsNumber) {
				subTotal = 0.00D;
			} else {
				subTotal = transactionPrice.doubleValue() * goodsNumber;
			}
		}
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getWarehCode() {
		return warehCode;
	}

	public void setWarehCode(String warehCode) {
		this.warehCode = warehCode;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public List<ProductBarcodeList> getColorChild() {
		return colorChild;
	}

	public void setColorChild(List<ProductBarcodeList> colorChild) {
		this.colorChild = colorChild;
	}

	public List<ProductBarcodeList> getSizeChild() {
		return sizeChild;
	}

	public void setSizeChild(List<ProductBarcodeList> sizeChild) {
		this.sizeChild = sizeChild;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getAvailableNumber() {
		return availableNumber;
	}

	public void setAvailableNumber(Integer availableNumber) {
		this.availableNumber = availableNumber;
	}

	public String getGoodsSn() {
		if (StringUtils.isEmpty(goodsSn) && StringUtils.isNotEmpty(customCode)) {
			goodsSn = customCode;
		}
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public List<CardCoupon> getCardCoupons() {
		return cardCoupons;
	}

	public void setCardCoupons(List<CardCoupon> cardCoupons) {
		this.cardCoupons = cardCoupons;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Integer getOccupiedAvailable() {
		return occupiedAvailable;
	}

	public void setOccupiedAvailable(Integer occupiedAvailable) {
		this.occupiedAvailable = occupiedAvailable;
	}

	public List<ProductBarcodeList> getBarcodeChild() {
		return barcodeChild;
	}

	public void setBarcodeChild(List<ProductBarcodeList> barcodeChild) {
		this.barcodeChild = barcodeChild;
	}

	public String getCurrColorCode() {
		return currColorCode;
	}

	public void setCurrColorCode(String currColorCode) {
		this.currColorCode = currColorCode;
	}

	public String getCurrSizeCode() {
		return currSizeCode;
	}

	public void setCurrSizeCode(String currSizeCode) {
		this.currSizeCode = currSizeCode;
	}

	public String getCurrColorName() {
		return currColorName;
	}

	public void setCurrColorName(String currColorName) {
		this.currColorName = currColorName;
	}

	public String getCurrSizeName() {
		return currSizeName;
	}

	public void setCurrSizeName(String currSizeName) {
		this.currSizeName = currSizeName;
	}

	public Integer getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}

	public Integer getReturnRemainNum() {
		return returnRemainNum;
	}

	public void setReturnRemainNum(Integer returnRemainNum) {
		this.returnRemainNum = returnRemainNum;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getIsCardCoupon() {
		return isCardCoupon;
	}

	public void setIsCardCoupon(String isCardCoupon) {
		this.isCardCoupon = isCardCoupon;
	}

	public String getSingleShareBonus() {
		return singleShareBonus;
	}

	public void setSingleShareBonus(String singleShareBonus) {
		this.singleShareBonus = singleShareBonus;
	}

	public Integer getInitGoodsNumber() {
		return initGoodsNumber;
	}

	public void setInitGoodsNumber(Integer initGoodsNumber) {
		this.initGoodsNumber = initGoodsNumber;
	}

	public int compareTo(OrderGoodsUpdateBean o) {
		return this.customCode.compareTo(customCode);
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGoodsSizeName() {
		return goodsSizeName;
	}

	public void setGoodsSizeName(String goodsSizeName) {
		this.goodsSizeName = goodsSizeName;
	}

	public String getGoodsColorName() {
		return goodsColorName;
	}

	public void setGoodsColorName(String goodsColorName) {
		this.goodsColorName = goodsColorName;
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}

	public Integer getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}

	public Integer getIsReal() {
		return isReal;
	}

	public void setIsReal(Integer isReal) {
		this.isReal = isReal;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public Short getIsGift() {
		return isGift;
	}

	public void setIsGift(Short isGift) {
		this.isGift = isGift;
	}

	public Integer getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Integer isRejected) {
		this.isRejected = isRejected;
	}

	public String getMergeFrom() {
		return mergeFrom;
	}

	public void setMergeFrom(String mergeFrom) {
		this.mergeFrom = mergeFrom;
	}

	public String getExchangeFrom() {
		return exchangeFrom;
	}

	public void setExchangeFrom(String exchangeFrom) {
		this.exchangeFrom = exchangeFrom;
	}

	public String getSplitTo() {
		return splitTo;
	}

	public void setSplitTo(String splitTo) {
		this.splitTo = splitTo;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
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
		this.useCard = useCard;
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

	public BigDecimal getRealPayFee() {
		return realPayFee;
	}

	public void setRealPayFee(BigDecimal realPayFee) {
		this.realPayFee = realPayFee;
	}

	public Byte getActivityType() {
		return activityType;
	}

	public void setActivityType(Byte activityType) {
		this.activityType = activityType;
	}

	public String getMadeUrl() {
		return madeUrl;
	}

	public void setMadeUrl(String madeUrl) {
		this.madeUrl = madeUrl;
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
		this.containerid = containerid;
	}

	public String getSelleruser() {
		return selleruser;
	}

	public void setSelleruser(String selleruser) {
		this.selleruser = selleruser;
	}

	public String getGoodsAttr() {
		return goodsAttr;
	}

	public void setGoodsAttr(String goodsAttr) {
		this.goodsAttr = goodsAttr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDoubleMarketPrice() {
		return doubleMarketPrice;
	}

	public void setDoubleMarketPrice(Double doubleMarketPrice) {
		this.doubleMarketPrice = doubleMarketPrice;
	}

	public Double getDoubleSettlementPrice() {
		return doubleSettlementPrice;
	}

	public void setDoubleSettlementPrice(Double doubleSettlementPrice) {
		this.doubleSettlementPrice = doubleSettlementPrice;
	}

	public Double getDoubleGoodsPrice() {
		return doubleGoodsPrice;
	}

	public void setDoubleGoodsPrice(Double doubleGoodsPrice) {
		this.doubleGoodsPrice = doubleGoodsPrice;
	}

	public Double getDoubleTransactionPrice() {
		return doubleTransactionPrice;
	}

	public void setDoubleTransactionPrice(Double doubleTransactionPrice) {
		this.doubleTransactionPrice = doubleTransactionPrice;
	}

	public Double getDoubleRankPoints() {
		return doubleRankPoints;
	}

	public void setDoubleRankPoints(Double doubleRankPoints) {
		this.doubleRankPoints = doubleRankPoints;
	}

	public Double getDoublePayPoints() {
		return doublePayPoints;
	}

	public void setDoublePayPoints(Double doublePayPoints) {
		this.doublePayPoints = doublePayPoints;
	}

	public Double getDoubleShareBonus() {
		return doubleShareBonus;
	}

	public void setDoubleShareBonus(Double doubleShareBonus) {
		this.doubleShareBonus = doubleShareBonus;
	}

	public Double getDoubleShareSurplus() {
		return doubleShareSurplus;
	}

	public void setDoubleShareSurplus(Double doubleShareSurplus) {
		this.doubleShareSurplus = doubleShareSurplus;
	}

	public Double getDoubleRealPayFee() {
		return doubleRealPayFee;
	}

	public void setDoubleRealPayFee(Double doubleRealPayFee) {
		this.doubleRealPayFee = doubleRealPayFee;
	}

	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn;
	}
	
	public Long getIndexId() {
		return indexId;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	public Double getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(Double goodsTotal) {
		this.goodsTotal = goodsTotal;
	}
	//   UNIQUE KEY `uniq_order_os` (`master_order_sn`,`order_sn`,`extension_code`,`custom_code`,`extension_id`,`depot_code`),
	public String getUniqueKey() {
		if (StringUtils.isNotEmpty(uniqueKey)) {
			return uniqueKey;
		}
		StringBuffer key = new StringBuffer("");
		
		if (StringUtils.isNotEmpty(orderSn)) {
			key.append(orderSn);
		}
		if (StringUtils.isNotEmpty(depotCode)) {
			key.append(depotCode);
		}
		if (StringUtils.isNotEmpty(customCode)) {
			key.append(customCode);
		}
		if (StringUtils.isNotEmpty(extensionCode)) {
			key.append(extensionCode);
		}
		if (StringUtils.isNotEmpty(extensionId)) {
			key.append(extensionId);
		}
		return key.toString();
	}
	
	public String getShippingStatusStr() {
		if (shippingStatus != null) {
			if (shippingStatus.intValue() == 0) {
				return "未发货";
			} else if (shippingStatus.intValue() == 1) {
				return "已发货";
			} else if (shippingStatus.intValue() == 2) {
				return "已收货";
			} else if (shippingStatus.intValue() == 2) {
				return "备货中";
			}
		}
		return "";
	}

	public Integer getLackNum() {
		return lackNum;
	}

	public void setLackNum(Integer lackNum) {
		this.lackNum = lackNum;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public Integer getInitLackNum() {
		return initLackNum;
	}

	public void setInitLackNum(Integer initLackNum) {
		this.initLackNum = initLackNum;
	}
	
	public Double getIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Double getInitIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return initIntegralMoney;
	}

	public void setInitIntegralMoney(Double initIntegralMoney) {
		this.initIntegralMoney = initIntegralMoney;
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
	
	public String getC2mItem() {
		return c2mItem;
	}

	public void setC2mItem(String c2mItem) {
		this.c2mItem = c2mItem;
	}
	
	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	@Override
	public String toString() {
		return "OrderGoodsUpdateBean [masterOrderSn=" + masterOrderSn + ", id="
				+ id + ", indexId=" + indexId + ", depotCode=" + depotCode
				+ ", orderSn=" + orderSn + ", goodsName=" + goodsName
				+ ", customCode=" + customCode + ", goodsNumber=" + goodsNumber
				+ ", marketPrice=" + marketPrice + ", doubleMarketPrice="
				+ doubleMarketPrice + ", settlementPrice=" + settlementPrice
				+ ", doubleSettlementPrice=" + doubleSettlementPrice
				+ ", goodsPrice=" + goodsPrice + ", doubleGoodsPrice="
				+ doubleGoodsPrice + ", discount=" + discount
				+ ", transactionPrice=" + transactionPrice
				+ ", doubleTransactionPrice=" + doubleTransactionPrice
				+ ", groupName=" + groupName + ", goodsSizeName="
				+ goodsSizeName + ", goodsColorName=" + goodsColorName
				+ ", goodsThumb=" + goodsThumb + ", sendNumber=" + sendNumber
				+ ", isReal=" + isReal + ", extensionCode=" + extensionCode
				+ ", extensionId=" + extensionId + ", isGift=" + isGift
				+ ", isRejected=" + isRejected + ", mergeFrom=" + mergeFrom
				+ ", exchangeFrom=" + exchangeFrom + ", splitTo=" + splitTo
				+ ", promotionDesc=" + promotionDesc + ", rankPoints="
				+ rankPoints + ", doubleRankPoints=" + doubleRankPoints
				+ ", payPoints=" + payPoints + ", doublePayPoints="
				+ doublePayPoints + ", useCard=" + useCard + ", shareBonus="
				+ shareBonus + ", doubleShareBonus=" + doubleShareBonus
				+ ", shareSurplus=" + shareSurplus + ", doubleShareSurplus="
				+ doubleShareSurplus + ", realPayFee=" + realPayFee
				+ ", doubleRealPayFee=" + doubleRealPayFee + ", activityType="
				+ activityType + ", madeUrl=" + madeUrl + ", madeFlag="
				+ madeFlag + ", chargeBackCount=" + chargeBackCount
				+ ", containerid=" + containerid + ", selleruser=" + selleruser
				+ ", goodsAttr=" + goodsAttr + ", parentSn=" + parentSn
				+ ", subTotal=" + subTotal + ", brandCode=" + brandCode
				+ ", warehouseId=" + warehouseId + ", warehCode=" + warehCode
				+ ", barcode=" + barcode + ", availableNumber="
				+ availableNumber + ", goodsSn=" + goodsSn + ", currColorCode="
				+ currColorCode + ", currSizeCode=" + currSizeCode
				+ ", currColorName=" + currColorName + ", currSizeName="
				+ currSizeName + ", returnNum=" + returnNum
				+ ", returnRemainNum=" + returnRemainNum + ", shippingStatus=" + shippingStatus
				+ ", shippingName=" + shippingName + ", invoiceNo=" + invoiceNo
				+ ", occupiedAvailable=" + occupiedAvailable + ", goodsTotal="
				+ goodsTotal + ", barcodeChild=" + barcodeChild
				+ ", colorChild=" + colorChild + ", sizeChild=" + sizeChild
				+ ", cardCoupons=" + cardCoupons + ", isCardCoupon="
				+ isCardCoupon + ", singleShareBonus=" + singleShareBonus
				+ ", initGoodsNumber=" + initGoodsNumber + ", uniqueKey="
				+ uniqueKey + ", lackNum=" + lackNum + ", initLackNum="
				+ initLackNum + ", integralMoney=" + integralMoney
				+ ", initIntegralMoney=" + initIntegralMoney + ", integral="
				+ integral + ", supplierCode=" + supplierCode
				+ ", supplierName=" + supplierName + ", c2mItem=" + c2mItem
				+ ", deliveryType=" + deliveryType + "]";
	}
}
