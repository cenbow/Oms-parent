package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 价格更新
 * @author YeQingchao
 */
public class UpdateGoodsItem implements Serializable {

    /**
     * 商品sku
     */
    private String sku;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    private BigDecimal transactionPriceNoTax;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getTransactionPriceNoTax() {
        return transactionPriceNoTax;
    }

    public void setTransactionPriceNoTax(BigDecimal transactionPriceNoTax) {
        this.transactionPriceNoTax = transactionPriceNoTax;
    }
}
