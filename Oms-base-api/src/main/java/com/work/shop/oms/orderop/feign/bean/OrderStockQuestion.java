package com.work.shop.oms.orderop.feign.bean;

import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 订单库存问题信息
 * @author QuYachu
 */
public class OrderStockQuestion implements Serializable {

    /**
     * 配送单号
     */
    private String orderSn;

    /**
     * 订单状态对象
     */
    private OrderStatus orderStatus;

    /**
     * sku缺货信息列表
     */
    private List<LackSkuParam> lackSkuParams;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<LackSkuParam> getLackSkuParams() {
        return lackSkuParams;
    }

    public void setLackSkuParams(List<LackSkuParam> lackSkuParams) {
        this.lackSkuParams = lackSkuParams;
    }
}
