package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单账期支付信息
 * @author QuYachu
 */
public class OrderAccountPeriod implements Serializable {

    /**
     * 订单编码
     */
    private String masterOrderSn;

    /**
     * 交货单号
     */
    private String orderSn;

    /**
     * 快递单号
     */
    private String invoiceNo;

    /**
     * 发货单序号
     */
    private int orderNumber;

    /**
     * 下单人
     */
    private String userId;

    /**
     * 下单时间
     */
    private Date addTime;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 支付金额
     */
    private BigDecimal orderMoney;

    /**
     * 类型 1支付、2退款
     */
    private int type;

    /**
     * 期数
     */
    private int paymentPeriod;

    /**
     * 加价率
     */
    private BigDecimal paymentRate;

    /**
     * 内行支付业务类型 0 内行现金、1内行银承
     */
    private int payType;

    /**
     * 订单类型 0订单、1发货单
     */
    private int orderType;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(int paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public BigDecimal getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(BigDecimal paymentRate) {
        this.paymentRate = paymentRate;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
