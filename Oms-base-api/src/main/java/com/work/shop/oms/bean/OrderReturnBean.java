package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单退款信息
 * @author QuYachu
 */
public class OrderReturnBean implements Serializable {

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 退单号
     */
    private String returnSn;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 退款金额
     */
    private BigDecimal returnMoney;

    /**
     * 类型 1信用额度2账期支付 3结算账户
     */
    private int type;

    /**
     * 是否已结算 0未结算、1已结算
     */
    private int settlement;

    /**
     * 交易流水号
     */
    private String payNo;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getReturnSn() {
        return returnSn;
    }

    public void setReturnSn(String returnSn) {
        this.returnSn = returnSn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(BigDecimal returnMoney) {
        this.returnMoney = returnMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getSettlement() {
        return settlement;
    }

    public void setSettlement(int settlement) {
        this.settlement = settlement;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
}
