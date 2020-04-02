package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 公司订单查询导出
 * @author YeQingchao
 */
public class OrderQueryExportResult implements Serializable {

    private static final long serialVersionUID = -7063497994458802145L;

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 渠道店铺
     */
    private String shopName;

    /**
     * 订单类型
     */
    private String orderTypeStr;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品数量小数部分
     */
    private BigDecimal goodsDecimalNum;

    /**
     * 订单总金额
     */
    private BigDecimal orderTotalAmount;

    /**
     * 采购总金额
     */
    private BigDecimal purchaseTotalAmount;

    /**
     * 下单人
     */
    private String userId;

    /**
     * 下单公司名称
     */
    private String companyName;

    /**
     * 下单公司ID
     */
    private String companyCode;

    /**
     * 下单时间
     */
    private Date addTime;

    /**
     * 收货人
     */
    private String consignee;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public BigDecimal getPurchaseTotalAmount() {
        return purchaseTotalAmount;
    }

    public void setPurchaseTotalAmount(BigDecimal purchaseTotalAmount) {
        this.purchaseTotalAmount = purchaseTotalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public BigDecimal getGoodsDecimalNum() {
        return goodsDecimalNum;
    }

    public void setGoodsDecimalNum(BigDecimal goodsDecimalNum) {
        this.goodsDecimalNum = goodsDecimalNum;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
