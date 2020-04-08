package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单合同
 * @author YeQingchao
 */
public class OrderContractBean implements Serializable {

    private static final long serialVersionUID = 8103872133923561591L;

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 订单状态 0，未确认；1，已确认；2，已取消；3，完成
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
     * 签章时间
     */
    private Date signCompleteTime;

    /**
     * 订单类型 0：正常订单  1联采订单
     */
    private Integer orderType;

    /**
     * 问题单状态 0 正常单 1 问题单
     */
    private Integer questionStatus;

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

    public Date getSignCompleteTime() {
        return signCompleteTime;
    }

    public void setSignCompleteTime(Date signCompleteTime) {
        this.signCompleteTime = signCompleteTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(Integer questionStatus) {
        this.questionStatus = questionStatus;
    }
}
