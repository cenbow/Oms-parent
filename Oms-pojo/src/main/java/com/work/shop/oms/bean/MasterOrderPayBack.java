package com.work.shop.oms.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MasterOrderPayBack {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.master_pay_sn
     *
     * @mbggenerated
     */
    private String masterPaySn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.master_order_sn
     *
     * @mbggenerated
     */
    private String masterOrderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_status
     *
     * @mbggenerated
     */
    private Byte payStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_id
     *
     * @mbggenerated
     */
    private Byte payId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_name
     *
     * @mbggenerated
     */
    private String payName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_note
     *
     * @mbggenerated
     */
    private String payNote;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_totalfee
     *
     * @mbggenerated
     */
    private BigDecimal payTotalfee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_time
     *
     * @mbggenerated
     */
    private Date payTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.pay_lasttime
     *
     * @mbggenerated
     */
    private Date payLasttime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_pay_back.merge_pay_sn
     *
     * @mbggenerated
     */
    private String mergePaySn;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.master_pay_sn
     *
     * @return the value of master_order_pay_back.master_pay_sn
     *
     * @mbggenerated
     */
    public String getMasterPaySn() {
        return masterPaySn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.master_pay_sn
     *
     * @param masterPaySn the value for master_order_pay_back.master_pay_sn
     *
     * @mbggenerated
     */
    public void setMasterPaySn(String masterPaySn) {
        this.masterPaySn = masterPaySn == null ? null : masterPaySn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.master_order_sn
     *
     * @return the value of master_order_pay_back.master_order_sn
     *
     * @mbggenerated
     */
    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.master_order_sn
     *
     * @param masterOrderSn the value for master_order_pay_back.master_order_sn
     *
     * @mbggenerated
     */
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_status
     *
     * @return the value of master_order_pay_back.pay_status
     *
     * @mbggenerated
     */
    public Byte getPayStatus() {
        return payStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_status
     *
     * @param payStatus the value for master_order_pay_back.pay_status
     *
     * @mbggenerated
     */
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_id
     *
     * @return the value of master_order_pay_back.pay_id
     *
     * @mbggenerated
     */
    public Byte getPayId() {
        return payId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_id
     *
     * @param payId the value for master_order_pay_back.pay_id
     *
     * @mbggenerated
     */
    public void setPayId(Byte payId) {
        this.payId = payId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_name
     *
     * @return the value of master_order_pay_back.pay_name
     *
     * @mbggenerated
     */
    public String getPayName() {
        return payName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_name
     *
     * @param payName the value for master_order_pay_back.pay_name
     *
     * @mbggenerated
     */
    public void setPayName(String payName) {
        this.payName = payName == null ? null : payName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_note
     *
     * @return the value of master_order_pay_back.pay_note
     *
     * @mbggenerated
     */
    public String getPayNote() {
        return payNote;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_note
     *
     * @param payNote the value for master_order_pay_back.pay_note
     *
     * @mbggenerated
     */
    public void setPayNote(String payNote) {
        this.payNote = payNote == null ? null : payNote.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_totalfee
     *
     * @return the value of master_order_pay_back.pay_totalfee
     *
     * @mbggenerated
     */
    public BigDecimal getPayTotalfee() {
        return payTotalfee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_totalfee
     *
     * @param payTotalfee the value for master_order_pay_back.pay_totalfee
     *
     * @mbggenerated
     */
    public void setPayTotalfee(BigDecimal payTotalfee) {
        this.payTotalfee = payTotalfee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_time
     *
     * @return the value of master_order_pay_back.pay_time
     *
     * @mbggenerated
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_time
     *
     * @param payTime the value for master_order_pay_back.pay_time
     *
     * @mbggenerated
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.create_time
     *
     * @return the value of master_order_pay_back.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.create_time
     *
     * @param createTime the value for master_order_pay_back.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.update_time
     *
     * @return the value of master_order_pay_back.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.update_time
     *
     * @param updateTime the value for master_order_pay_back.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.pay_lasttime
     *
     * @return the value of master_order_pay_back.pay_lasttime
     *
     * @mbggenerated
     */
    public Date getPayLasttime() {
        return payLasttime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.pay_lasttime
     *
     * @param payLasttime the value for master_order_pay_back.pay_lasttime
     *
     * @mbggenerated
     */
    public void setPayLasttime(Date payLasttime) {
        this.payLasttime = payLasttime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_pay_back.merge_pay_sn
     *
     * @return the value of master_order_pay_back.merge_pay_sn
     *
     * @mbggenerated
     */
    public String getMergePaySn() {
        return mergePaySn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_pay_back.merge_pay_sn
     *
     * @param mergePaySn the value for master_order_pay_back.merge_pay_sn
     *
     * @mbggenerated
     */
    public void setMergePaySn(String mergePaySn) {
        this.mergePaySn = mergePaySn == null ? null : mergePaySn.trim();
    }
}