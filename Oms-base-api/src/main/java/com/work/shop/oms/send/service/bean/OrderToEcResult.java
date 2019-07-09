package com.work.shop.oms.send.service.bean;

import java.io.Serializable;

/**
 * 百胜EC一小时达返回结果
 * @author YeQingchao
 */
public class OrderToEcResult implements Serializable {
    private static final long serialVersionUID = 1473631725793201951L;

    private String status;

    private String message;

    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
