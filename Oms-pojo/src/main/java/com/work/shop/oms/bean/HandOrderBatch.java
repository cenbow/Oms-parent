package com.work.shop.oms.bean;

import java.util.Date;

public class HandOrderBatch {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.batch_no
     *
     * @mbggenerated
     */
    private String batchNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.process_status
     *
     * @mbggenerated
     */
    private Integer processStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.create_order_status
     *
     * @mbggenerated
     */
    private Integer createOrderStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.process_message
     *
     * @mbggenerated
     */
    private String processMessage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.create_user
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.update_user
     *
     * @mbggenerated
     */
    private String updateUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.channel_code
     *
     * @mbggenerated
     */
    private String channelCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.channel_name
     *
     * @mbggenerated
     */
    private String channelName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hand_order_batch.source_type
     *
     * @mbggenerated
     */
    private Integer sourceType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.id
     *
     * @return the value of hand_order_batch.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.id
     *
     * @param id the value for hand_order_batch.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.batch_no
     *
     * @return the value of hand_order_batch.batch_no
     *
     * @mbggenerated
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.batch_no
     *
     * @param batchNo the value for hand_order_batch.batch_no
     *
     * @mbggenerated
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.process_status
     *
     * @return the value of hand_order_batch.process_status
     *
     * @mbggenerated
     */
    public Integer getProcessStatus() {
        return processStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.process_status
     *
     * @param processStatus the value for hand_order_batch.process_status
     *
     * @mbggenerated
     */
    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.create_order_status
     *
     * @return the value of hand_order_batch.create_order_status
     *
     * @mbggenerated
     */
    public Integer getCreateOrderStatus() {
        return createOrderStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.create_order_status
     *
     * @param createOrderStatus the value for hand_order_batch.create_order_status
     *
     * @mbggenerated
     */
    public void setCreateOrderStatus(Integer createOrderStatus) {
        this.createOrderStatus = createOrderStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.process_message
     *
     * @return the value of hand_order_batch.process_message
     *
     * @mbggenerated
     */
    public String getProcessMessage() {
        return processMessage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.process_message
     *
     * @param processMessage the value for hand_order_batch.process_message
     *
     * @mbggenerated
     */
    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage == null ? null : processMessage.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.create_time
     *
     * @return the value of hand_order_batch.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.create_time
     *
     * @param createTime the value for hand_order_batch.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.create_user
     *
     * @return the value of hand_order_batch.create_user
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.create_user
     *
     * @param createUser the value for hand_order_batch.create_user
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.update_time
     *
     * @return the value of hand_order_batch.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.update_time
     *
     * @param updateTime the value for hand_order_batch.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.update_user
     *
     * @return the value of hand_order_batch.update_user
     *
     * @mbggenerated
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.update_user
     *
     * @param updateUser the value for hand_order_batch.update_user
     *
     * @mbggenerated
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.channel_code
     *
     * @return the value of hand_order_batch.channel_code
     *
     * @mbggenerated
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.channel_code
     *
     * @param channelCode the value for hand_order_batch.channel_code
     *
     * @mbggenerated
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.channel_name
     *
     * @return the value of hand_order_batch.channel_name
     *
     * @mbggenerated
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.channel_name
     *
     * @param channelName the value for hand_order_batch.channel_name
     *
     * @mbggenerated
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hand_order_batch.source_type
     *
     * @return the value of hand_order_batch.source_type
     *
     * @mbggenerated
     */
    public Integer getSourceType() {
        return sourceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hand_order_batch.source_type
     *
     * @param sourceType the value for hand_order_batch.source_type
     *
     * @mbggenerated
     */
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
}