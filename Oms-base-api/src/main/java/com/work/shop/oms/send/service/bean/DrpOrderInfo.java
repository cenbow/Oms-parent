package com.work.shop.oms.send.service.bean;

import java.io.Serializable;

/**
 * DRP骑手时效接口(大众点评)配送信息
 * @author YeQingchao
 */
public class DrpOrderInfo implements Serializable {

    private static final long serialVersionUID = 6203257150609133260L;
    /**
     * 送达时间,2018-03-09 12:47:00
     */
    private String accept_time;

    /**
     * 距离,米
     */
    private String distance;

    /**
     * 点评商城订单号
     */
    private String orderno;

    /**
     * 接单时间，2018-03-09 12:47:00
     */
    private String  post_time ;

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
}
