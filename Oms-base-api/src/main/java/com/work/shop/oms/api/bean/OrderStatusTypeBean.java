package com.work.shop.oms.api.bean;

import java.io.Serializable;

/**
 * 订单状态类型
 * @author
 */
public class OrderStatusTypeBean implements Serializable {

    private static final long serialVersionUID = -9188072053359576290L;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 数量
     */
    private int num;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
