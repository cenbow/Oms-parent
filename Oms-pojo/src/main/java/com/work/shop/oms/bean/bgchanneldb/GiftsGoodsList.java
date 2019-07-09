package com.work.shop.oms.bean.bgchanneldb;

public class GiftsGoodsList {
    private Integer id;

    private String promCode;

    private String goodsSn;

    private Integer giftsSum;

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

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn == null ? null : goodsSn.trim();
    }

    public Integer getGiftsSum() {
        return giftsSum;
    }

    public void setGiftsSum(Integer giftsSum) {
        this.giftsSum = giftsSum;
    }
    
}