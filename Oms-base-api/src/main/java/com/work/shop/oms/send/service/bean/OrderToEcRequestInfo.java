package com.work.shop.oms.send.service.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ec一小时达请求信息
 * @author YeQingchao
 */
public class OrderToEcRequestInfo implements Serializable {

    private static final long serialVersionUID = 662168342429327958L;

    private String total;

    private List<OrderToEcRequest> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OrderToEcRequest> getData() {
        return data;
    }

    public void setData(List<OrderToEcRequest> data) {
        this.data = data;
    }
}
