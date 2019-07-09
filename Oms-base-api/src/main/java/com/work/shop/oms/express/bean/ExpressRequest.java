package com.work.shop.oms.express.bean;

import java.io.Serializable;

/**
 * 物流请求
 * @author QuYachu
 */
public class ExpressRequest implements Serializable {

    /**
     * 订单编码
     */
    private String orderSn;

    /**
     * 快递单号
     */
    private String trackNo;

    /**
     * 仓库编码
     */
    private String depotCode;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }
}
