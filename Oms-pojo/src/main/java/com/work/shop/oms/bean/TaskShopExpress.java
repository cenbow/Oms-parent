package com.work.shop.oms.bean;

import java.util.Date;

public class TaskShopExpress {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.order_sn
     *
     * @mbggenerated
     */
    private String orderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.shipping_id
     *
     * @mbggenerated
     */
    private Byte shippingId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.depot_code
     *
     * @mbggenerated
     */
    private String depotCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.is_ok
     *
     * @mbggenerated
     */
    private Integer isOk;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.response
     *
     * @mbggenerated
     */
    private String response;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_shop_express.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.id
     *
     * @return the value of task_shop_express.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.id
     *
     * @param id the value for task_shop_express.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.order_sn
     *
     * @return the value of task_shop_express.order_sn
     *
     * @mbggenerated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.order_sn
     *
     * @param orderSn the value for task_shop_express.order_sn
     *
     * @mbggenerated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.shipping_id
     *
     * @return the value of task_shop_express.shipping_id
     *
     * @mbggenerated
     */
    public Byte getShippingId() {
        return shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.shipping_id
     *
     * @param shippingId the value for task_shop_express.shipping_id
     *
     * @mbggenerated
     */
    public void setShippingId(Byte shippingId) {
        this.shippingId = shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.depot_code
     *
     * @return the value of task_shop_express.depot_code
     *
     * @mbggenerated
     */
    public String getDepotCode() {
        return depotCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.depot_code
     *
     * @param depotCode the value for task_shop_express.depot_code
     *
     * @mbggenerated
     */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode == null ? null : depotCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.is_ok
     *
     * @return the value of task_shop_express.is_ok
     *
     * @mbggenerated
     */
    public Integer getIsOk() {
        return isOk;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.is_ok
     *
     * @param isOk the value for task_shop_express.is_ok
     *
     * @mbggenerated
     */
    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.response
     *
     * @return the value of task_shop_express.response
     *
     * @mbggenerated
     */
    public String getResponse() {
        return response;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.response
     *
     * @param response the value for task_shop_express.response
     *
     * @mbggenerated
     */
    public void setResponse(String response) {
        this.response = response == null ? null : response.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_shop_express.add_time
     *
     * @return the value of task_shop_express.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_shop_express.add_time
     *
     * @param addTime the value for task_shop_express.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}