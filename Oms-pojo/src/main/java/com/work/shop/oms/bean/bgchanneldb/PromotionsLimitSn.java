package com.work.shop.oms.bean.bgchanneldb;

public class PromotionsLimitSn {
    private Integer id;

    private String promCode;

    private String limitGoodsSn;

    private Integer limitCount;

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

    public String getLimitGoodsSn() {
        return limitGoodsSn;
    }

    public void setLimitGoodsSn(String limitGoodsSn) {
        this.limitGoodsSn = limitGoodsSn == null ? null : limitGoodsSn.trim();
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
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