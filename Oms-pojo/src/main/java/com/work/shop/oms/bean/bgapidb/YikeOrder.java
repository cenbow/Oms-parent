package com.work.shop.oms.bean.bgapidb;

import java.math.BigDecimal;

import com.work.shop.oms.bean.bgapidb.defined.BaseOrder;

public class YikeOrder extends BaseOrder{
    private Integer id;

    private String code;

    private String barCode;

    private String itemNo;

    private BigDecimal priceSell;

    private BigDecimal priceOriginal;

    private BigDecimal amount;

    private BigDecimal discountMoney;

    private BigDecimal discountAmount;

    private BigDecimal frDiscountAmount;

    private BigDecimal couponDiscountAmount;

    private BigDecimal rewardFee;

    private Integer useBonus;

    private String isGift;

    private String returnQuantity;

    private BigDecimal returnPrice;

    private BigDecimal returnMoney;

    private Integer quantity;

    private String productName;

    private String useCouponValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo == null ? null : itemNo.trim();
    }

    public BigDecimal getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(BigDecimal priceSell) {
        this.priceSell = priceSell;
    }

    public BigDecimal getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(BigDecimal priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFrDiscountAmount() {
        return frDiscountAmount;
    }

    public void setFrDiscountAmount(BigDecimal frDiscountAmount) {
        this.frDiscountAmount = frDiscountAmount;
    }

    public BigDecimal getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public BigDecimal getRewardFee() {
        return rewardFee;
    }

    public void setRewardFee(BigDecimal rewardFee) {
        this.rewardFee = rewardFee;
    }

    public Integer getUseBonus() {
        return useBonus;
    }

    public void setUseBonus(Integer useBonus) {
        this.useBonus = useBonus;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift == null ? null : isGift.trim();
    }

    public String getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(String returnQuantity) {
        this.returnQuantity = returnQuantity == null ? null : returnQuantity.trim();
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public BigDecimal getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(BigDecimal returnMoney) {
        this.returnMoney = returnMoney;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getUseCouponValue() {
        return useCouponValue;
    }

    public void setUseCouponValue(String useCouponValue) {
        this.useCouponValue = useCouponValue == null ? null : useCouponValue.trim();
    }
}