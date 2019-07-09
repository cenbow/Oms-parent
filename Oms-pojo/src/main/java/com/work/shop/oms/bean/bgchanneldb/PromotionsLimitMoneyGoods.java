package com.work.shop.oms.bean.bgchanneldb;

public class PromotionsLimitMoneyGoods {
    private String promCode;

    private String promDetailsCode;

    private String giftsSkuSn;

    private Integer giftsSkuCount;

    private Byte giftsPriority;

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

    public String getGiftsSkuSn() {
        return giftsSkuSn;
    }

    public void setGiftsSkuSn(String giftsSkuSn) {
        this.giftsSkuSn = giftsSkuSn == null ? null : giftsSkuSn.trim();
    }

    public Integer getGiftsSkuCount() {
        return giftsSkuCount;
    }

    public void setGiftsSkuCount(Integer giftsSkuCount) {
        this.giftsSkuCount = giftsSkuCount;
    }

    public Byte getGiftsPriority() {
        return giftsPriority;
    }

    public void setGiftsPriority(Byte giftsPriority) {
        this.giftsPriority = giftsPriority;
    }
}