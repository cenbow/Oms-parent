package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题单
 * @author YeQingchao
 */
public class MasterOrderQuestionBean implements Serializable {

    private static final long serialVersionUID = -2097998822027822004L;

    /**
     * 主订单号
     */
    private String masterOrderSn;

    /**
     * 问题单编码
     */
    private String questionCode;

    /**
     * 问题描述
     */
    private String questionDesc;

    /**
     * 问题类型：0.一般问题单，1.缺货问题单
     */
    private Integer questionType;

    /**
     * 添加时间
     */
    private Date addTime;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
