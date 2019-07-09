package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class ReturnChangeGoodsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点
     */
    private String siteCode;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 退换类型，1：退货；2：换货
     */
    private Integer returnType;

    /**
     * 退换原因
     * 1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
     */
    private Integer reason;

    /**
     * 退换单申请单号
     */
    private String returnChangeSn;

    private Integer pageNo;

    private Integer pageSize;

    private String userId;

    private String note;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getReturnChangeSn() {
        return returnChangeSn;
    }

    public void setReturnChangeSn(String returnChangeSn) {
        this.returnChangeSn = returnChangeSn;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
