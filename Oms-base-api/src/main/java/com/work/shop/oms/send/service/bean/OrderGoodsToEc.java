package com.work.shop.oms.send.service.bean;

public class OrderGoodsToEc {

    /**
     * sku
     */
    private String sku;

    /**
     * 商品单价(折前)
     */
    private String market_price;

    /**
     * 商品单价(折后)
     */
    private String goods_price;

    /**
     * 折扣
     */
    private String discount;

    /**
     * 数量
     */
    private int goods_number;

    /**
     * 是否礼品
     */
    private int is_gift;

    /**
     * 均摊到这个商品之后的商品折让总金额
     */
    private String discount_fee;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public int getIs_gift() {
        return is_gift;
    }

    public void setIs_gift(int is_gift) {
        this.is_gift = is_gift;
    }

    public String getDiscount_fee() {
        return discount_fee;
    }

    public void setDiscount_fee(String discount_fee) {
        this.discount_fee = discount_fee;
    }
}
