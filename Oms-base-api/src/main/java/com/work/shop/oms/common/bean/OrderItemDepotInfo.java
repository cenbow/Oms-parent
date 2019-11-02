package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

public class OrderItemDepotInfo implements Serializable {

    private static final long serialVersionUID = 2161871944115052632L;

    /**
     * 交货单号
     */
    private String orderSn;

    /**
     * 发货单信息
     */
    private List<OrderItemDepotDetail> depotDetails;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<OrderItemDepotDetail> getDepotDetails() {
        return depotDetails;
    }

    public void setDepotDetails(List<OrderItemDepotDetail> depotDetails) {
        this.depotDetails = depotDetails;
    }
}
