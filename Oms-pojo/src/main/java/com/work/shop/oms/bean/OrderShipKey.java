package com.work.shop.oms.bean;

public class OrderShipKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship.depot_code
     *
     * @mbggenerated
     */
    private String depotCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_ship.order_sn
     *
     * @mbggenerated
     */
    private String orderSn;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship.depot_code
     *
     * @return the value of order_ship.depot_code
     *
     * @mbggenerated
     */
    public String getDepotCode() {
        return depotCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship.depot_code
     *
     * @param depotCode the value for order_ship.depot_code
     *
     * @mbggenerated
     */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode == null ? null : depotCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_ship.order_sn
     *
     * @return the value of order_ship.order_sn
     *
     * @mbggenerated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_ship.order_sn
     *
     * @param orderSn the value for order_ship.order_sn
     *
     * @mbggenerated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }
}