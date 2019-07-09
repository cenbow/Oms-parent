package com.work.shop.oms.send.service.bean;

import java.io.Serializable;
import java.util.Map;

public class DrpToResult implements Serializable {

    private static final long serialVersionUID = 7250826268148705718L;

    /**
     * 返回状态
     */
    private boolean isSuccess;

    /**
     * 返回数据
     */
    private Map<String, String> map;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
