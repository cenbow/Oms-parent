package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单合同列表请求参数
 * @author YeQingchao
 */
public class OrderContractRequest implements Serializable {

    private static final long serialVersionUID = 8103872133923561591L;

    private Integer pageNo; // 页码

    private Integer pageSize; // 每页记录数

    private int start;

    private int limit;

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 站点编码
     */
    private String channelCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 订单状态 0，取消；1，正常
     */
    private Integer orderStatus;

    /**
     * 签章状态 0未签章、1已签章
     */
    private Integer signStatus;

    /**
     * 签章合同号
     */
    private String signContractNum;

    /**
     * 签章时间查询开始时间
     */
    private String startTime;

    /**
     * 签章时间查询结束时间
     */
    private String endTime;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignContractNum() {
        return signContractNum;
    }

    public void setSignContractNum(String signContractNum) {
        this.signContractNum = signContractNum;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
