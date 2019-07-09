package com.work.shop.oms.send.service.bean;

import java.io.Serializable;

/**
 * DRP骑手时效接口(大众点评)请求参数
 * @author YeQingchao
 */
public class DrpRequest implements Serializable {

    private static final long serialVersionUID = 4347494901491070288L;
    /**
     * 机构账号（DRP提供）
     */
    private String orgcode;

    /**
     * 接口方法名
     */
    private String orgmethod;

    /**
     * 品牌标识(EHT等…)
     */
    private String brand;

    /**
     *  接口无需传输,DRP提供
     */
    private String token;

    /**
     * 时间戳[时间毫秒数/1000]
     */
    private String timestamp;

    /**
     * 格式（json/xml）
     */
    private String format = "json";

    /**
     * 验证码
     */
    private String sign;

    /**
     * Json字符串 list<DrpOrderInfo>集合
     */
    private String jsonstr;

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgmethod() {
        return orgmethod;
    }

    public void setOrgmethod(String orgmethod) {
        this.orgmethod = orgmethod;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getJsonstr() {
        return jsonstr;
    }

    public void setJsonstr(String jsonstr) {
        this.jsonstr = jsonstr;
    }
}
