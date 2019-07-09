package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsReturnChangeDetailVo implements Serializable {

    private static final long serialVersionUID = -7839279667014923245L;

    private String goodsName;

    private String customCode;

    private String extensionCode;

    private String extensionId;

    private Short goodsNumber;

    private BigDecimal goodsPrice;

    private BigDecimal transactionPrice;

    private BigDecimal settlementPrice;

    private Float discount;

    private String goodsSizeName;

    private String goodsColorName;

    private String goodsThumb;

    private String useCard;

    private BigDecimal shareBonus;

    private BigDecimal marketPrice;

    private String goodsSn;

    private Integer redemption;

    private Integer tagType;

    private Integer exteriorType;

    private Integer giftType;

    private String remark;

    private Integer returnSum;

    private Integer downReturnSum;

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
     * 供应商名称
     */
    private String supplierName;

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

    public Short getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Short goodsNumber) {
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

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public Integer getRedemption() {
        return redemption;
    }

    public void setRedemption(Integer redemption) {
        this.redemption = redemption;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getExteriorType() {
        return exteriorType;
    }

    public void setExteriorType(Integer exteriorType) {
        this.exteriorType = exteriorType;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getReturnSum() {
        return returnSum;
    }

    public void setReturnSum(Integer returnSum) {
        this.returnSum = returnSum;
    }

    public Integer getDownReturnSum() {
        return downReturnSum;
    }

    public void setDownReturnSum(Integer downReturnSum) {
        this.downReturnSum = downReturnSum;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
