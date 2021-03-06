package com.work.shop.oms.bean;

import java.util.Date;

public class OrderShipHistory extends OrderShipHistoryKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.shipping_status
     *
     * @mbggenerated
     */
    private Byte shippingStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.express_status
     *
     * @mbggenerated
     */
    private Float expressStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.shipping_id
     *
     * @mbggenerated
     */
    private Byte shippingId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.shipping_name
     *
     * @mbggenerated
     */
    private String shippingName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.invoice_no
     *
     * @mbggenerated
     */
    private String invoiceNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.delivery_type
     *
     * @mbggenerated
     */
    private Integer deliveryType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.delivery_time
     *
     * @mbggenerated
     */
    private Date deliveryTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.bak1
     *
     * @mbggenerated
     */
    private String bak1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.creat_time
     *
     * @mbggenerated
     */
    private Date creatTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.delivery_confirm_time
     *
     * @mbggenerated
     */
    private Date deliveryConfirmTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.region_id
     *
     * @mbggenerated
     */
    private Short regionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.pickup_date
     *
     * @mbggenerated
     */
    private Date pickupDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.depot_time
     *
     * @mbggenerated
     */
    private Date depotTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.pdwarh_code
     *
     * @mbggenerated
     */
    private String pdwarhCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.pdwarh_name
     *
     * @mbggenerated
     */
    private String pdwarhName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.to_user
     *
     * @mbggenerated
     */
    private String toUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.to_user_phone
     *
     * @mbggenerated
     */
    private String toUserPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.provincecity
     *
     * @mbggenerated
     */
    private String provincecity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.over_trans_cycle
     *
     * @mbggenerated
     */
    private String overTransCycle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship_history.is_del
     *
     * @mbggenerated
     */
    private Integer isDel;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.shipping_status
     *
     * @return the value of order_ship_history.shipping_status
     *
     * @mbggenerated
     */
    public Byte getShippingStatus() {
        return shippingStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.shipping_status
     *
     * @param shippingStatus the value for order_ship_history.shipping_status
     *
     * @mbggenerated
     */
    public void setShippingStatus(Byte shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.express_status
     *
     * @return the value of order_ship_history.express_status
     *
     * @mbggenerated
     */
    public Float getExpressStatus() {
        return expressStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.express_status
     *
     * @param expressStatus the value for order_ship_history.express_status
     *
     * @mbggenerated
     */
    public void setExpressStatus(Float expressStatus) {
        this.expressStatus = expressStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.shipping_id
     *
     * @return the value of order_ship_history.shipping_id
     *
     * @mbggenerated
     */
    public Byte getShippingId() {
        return shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.shipping_id
     *
     * @param shippingId the value for order_ship_history.shipping_id
     *
     * @mbggenerated
     */
    public void setShippingId(Byte shippingId) {
        this.shippingId = shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.shipping_name
     *
     * @return the value of order_ship_history.shipping_name
     *
     * @mbggenerated
     */
    public String getShippingName() {
        return shippingName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.shipping_name
     *
     * @param shippingName the value for order_ship_history.shipping_name
     *
     * @mbggenerated
     */
    public void setShippingName(String shippingName) {
        this.shippingName = shippingName == null ? null : shippingName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.invoice_no
     *
     * @return the value of order_ship_history.invoice_no
     *
     * @mbggenerated
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.invoice_no
     *
     * @param invoiceNo the value for order_ship_history.invoice_no
     *
     * @mbggenerated
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.delivery_type
     *
     * @return the value of order_ship_history.delivery_type
     *
     * @mbggenerated
     */
    public Integer getDeliveryType() {
        return deliveryType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.delivery_type
     *
     * @param deliveryType the value for order_ship_history.delivery_type
     *
     * @mbggenerated
     */
    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.delivery_time
     *
     * @return the value of order_ship_history.delivery_time
     *
     * @mbggenerated
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.delivery_time
     *
     * @param deliveryTime the value for order_ship_history.delivery_time
     *
     * @mbggenerated
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.bak1
     *
     * @return the value of order_ship_history.bak1
     *
     * @mbggenerated
     */
    public String getBak1() {
        return bak1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.bak1
     *
     * @param bak1 the value for order_ship_history.bak1
     *
     * @mbggenerated
     */
    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.creat_time
     *
     * @return the value of order_ship_history.creat_time
     *
     * @mbggenerated
     */
    public Date getCreatTime() {
        return creatTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.creat_time
     *
     * @param creatTime the value for order_ship_history.creat_time
     *
     * @mbggenerated
     */
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.update_time
     *
     * @return the value of order_ship_history.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.update_time
     *
     * @param updateTime the value for order_ship_history.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.delivery_confirm_time
     *
     * @return the value of order_ship_history.delivery_confirm_time
     *
     * @mbggenerated
     */
    public Date getDeliveryConfirmTime() {
        return deliveryConfirmTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.delivery_confirm_time
     *
     * @param deliveryConfirmTime the value for order_ship_history.delivery_confirm_time
     *
     * @mbggenerated
     */
    public void setDeliveryConfirmTime(Date deliveryConfirmTime) {
        this.deliveryConfirmTime = deliveryConfirmTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.region_id
     *
     * @return the value of order_ship_history.region_id
     *
     * @mbggenerated
     */
    public Short getRegionId() {
        return regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.region_id
     *
     * @param regionId the value for order_ship_history.region_id
     *
     * @mbggenerated
     */
    public void setRegionId(Short regionId) {
        this.regionId = regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.pickup_date
     *
     * @return the value of order_ship_history.pickup_date
     *
     * @mbggenerated
     */
    public Date getPickupDate() {
        return pickupDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.pickup_date
     *
     * @param pickupDate the value for order_ship_history.pickup_date
     *
     * @mbggenerated
     */
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.depot_time
     *
     * @return the value of order_ship_history.depot_time
     *
     * @mbggenerated
     */
    public Date getDepotTime() {
        return depotTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.depot_time
     *
     * @param depotTime the value for order_ship_history.depot_time
     *
     * @mbggenerated
     */
    public void setDepotTime(Date depotTime) {
        this.depotTime = depotTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.pdwarh_code
     *
     * @return the value of order_ship_history.pdwarh_code
     *
     * @mbggenerated
     */
    public String getPdwarhCode() {
        return pdwarhCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.pdwarh_code
     *
     * @param pdwarhCode the value for order_ship_history.pdwarh_code
     *
     * @mbggenerated
     */
    public void setPdwarhCode(String pdwarhCode) {
        this.pdwarhCode = pdwarhCode == null ? null : pdwarhCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.pdwarh_name
     *
     * @return the value of order_ship_history.pdwarh_name
     *
     * @mbggenerated
     */
    public String getPdwarhName() {
        return pdwarhName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.pdwarh_name
     *
     * @param pdwarhName the value for order_ship_history.pdwarh_name
     *
     * @mbggenerated
     */
    public void setPdwarhName(String pdwarhName) {
        this.pdwarhName = pdwarhName == null ? null : pdwarhName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.to_user
     *
     * @return the value of order_ship_history.to_user
     *
     * @mbggenerated
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.to_user
     *
     * @param toUser the value for order_ship_history.to_user
     *
     * @mbggenerated
     */
    public void setToUser(String toUser) {
        this.toUser = toUser == null ? null : toUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.to_user_phone
     *
     * @return the value of order_ship_history.to_user_phone
     *
     * @mbggenerated
     */
    public String getToUserPhone() {
        return toUserPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.to_user_phone
     *
     * @param toUserPhone the value for order_ship_history.to_user_phone
     *
     * @mbggenerated
     */
    public void setToUserPhone(String toUserPhone) {
        this.toUserPhone = toUserPhone == null ? null : toUserPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.provincecity
     *
     * @return the value of order_ship_history.provincecity
     *
     * @mbggenerated
     */
    public String getProvincecity() {
        return provincecity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.provincecity
     *
     * @param provincecity the value for order_ship_history.provincecity
     *
     * @mbggenerated
     */
    public void setProvincecity(String provincecity) {
        this.provincecity = provincecity == null ? null : provincecity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.over_trans_cycle
     *
     * @return the value of order_ship_history.over_trans_cycle
     *
     * @mbggenerated
     */
    public String getOverTransCycle() {
        return overTransCycle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.over_trans_cycle
     *
     * @param overTransCycle the value for order_ship_history.over_trans_cycle
     *
     * @mbggenerated
     */
    public void setOverTransCycle(String overTransCycle) {
        this.overTransCycle = overTransCycle == null ? null : overTransCycle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship_history.is_del
     *
     * @return the value of order_ship_history.is_del
     *
     * @mbggenerated
     */
    public Integer getIsDel() {
        return isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship_history.is_del
     *
     * @param isDel the value for order_ship_history.is_del
     *
     * @mbggenerated
     */
    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}