package com.work.shop.oms.bean.bgchanneldb;

import java.math.BigDecimal;

public class PromotionsLimitMoney {
    private Integer id;

    private String promCode;

    private String promDetailsCode;

    private BigDecimal limitMoney;

    private String giftsGoodsSn;

    private Integer giftsGoodsCount;

    private Integer giftsGoodsSum;

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

    public String getPromDetailsCode() {
        return promDetailsCode;
    }

    public void setPromDetailsCode(String promDetailsCode) {
        this.promDetailsCode = promDetailsCode == null ? null : promDetailsCode.trim();
    }

    public BigDecimal getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(BigDecimal limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getGiftsGoodsSn() {
        return giftsGoodsSn;
    }

    public void setGiftsGoodsSn(String giftsGoodsSn) {
        this.giftsGoodsSn = giftsGoodsSn == null ? null : giftsGoodsSn.trim();
    }

    public Integer getGiftsGoodsCount() {
        return giftsGoodsCount;
    }

    public void setGiftsGoodsCount(Integer giftsGoodsCount) {
        this.giftsGoodsCount = giftsGoodsCount;
    }

    public Integer getGiftsGoodsSum() {
        return giftsGoodsSum;
    }

    public void setGiftsGoodsSum(Integer giftsGoodsSum) {
        this.giftsGoodsSum = giftsGoodsSum;
    }
}