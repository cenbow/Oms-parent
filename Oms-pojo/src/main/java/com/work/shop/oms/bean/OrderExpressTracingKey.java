package com.work.shop.oms.bean;

public class OrderExpressTracingKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_express_tracing.delivery_sn
     *
     * @mbggenerated
     */
    private String deliverySn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_express_tracing.depot_code
     *
     * @mbggenerated
     */
    private String depotCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_express_tracing.order_sn
     *
     * @mbggenerated
     */
    private String orderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_express_tracing.trackno
     *
     * @mbggenerated
     */
    private String trackno;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_express_tracing.delivery_sn
     *
     * @return the value of order_express_tracing.delivery_sn
     *
     * @mbggenerated
     */
    public String getDeliverySn() {
        return deliverySn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_express_tracing.delivery_sn
     *
     * @param deliverySn the value for order_express_tracing.delivery_sn
     *
     * @mbggenerated
     */
    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn == null ? null : deliverySn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_express_tracing.depot_code
     *
     * @return the value of order_express_tracing.depot_code
     *
     * @mbggenerated
     */
    public String getDepotCode() {
        return depotCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_express_tracing.depot_code
     *
     * @param depotCode the value for order_express_tracing.depot_code
     *
     * @mbggenerated
     */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode == null ? null : depotCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_express_tracing.order_sn
     *
     * @return the value of order_express_tracing.order_sn
     *
     * @mbggenerated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_express_tracing.order_sn
     *
     * @param orderSn the value for order_express_tracing.order_sn
     *
     * @mbggenerated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_express_tracing.trackno
     *
     * @return the value of order_express_tracing.trackno
     *
     * @mbggenerated
     */
    public String getTrackno() {
        return trackno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_express_tracing.trackno
     *
     * @param trackno the value for order_express_tracing.trackno
     *
     * @mbggenerated
     */
    public void setTrackno(String trackno) {
        this.trackno = trackno == null ? null : trackno.trim();
    }
}