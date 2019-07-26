package com.work.shop.oms.ship.bean;

import java.io.Serializable;

public class DistributeRequest implements Serializable {

    private static final long serialVersionUID = -9100079073935702948L;

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 交货单号
     */
    private String orderSn;

    /**
     * 仓库编码
     */
    private String deoptCode;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getDeoptCode() {
        return deoptCode;
    }

    public void setDeoptCode(String deoptCode) {
        this.deoptCode = deoptCode;
    }
}
