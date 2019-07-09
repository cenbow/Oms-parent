package com.work.shop.oms.bean.bgchanneldb;

import java.math.BigDecimal;

public class PromotionsListInfor {
    private Integer id;

    private String promCode;

    private Integer goodsCount;

    private BigDecimal limitMoney;

    private Integer giftsCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromCode() {
        return promCode;
    }

    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(BigDecimal limitMoney) {
        this.limitMoney = limitMoney;
    }

    public Integer getGiftsCount() {
        return giftsCount;
    }

    public void setGiftsCount(Integer giftsCount) {
        this.giftsCount = giftsCount;
    }
}