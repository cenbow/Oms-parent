package com.work.shop.oms.bean;

import java.math.BigDecimal;

public class PurchaseOrderLine {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.purchase_order_code
     *
     * @mbggenerated
     */
    private String purchaseOrderCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.depot_code
     *
     * @mbggenerated
     */
    private String depotCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.master_order_sn
     *
     * @mbggenerated
     */
    private String masterOrderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_name
     *
     * @mbggenerated
     */
    private String goodsName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.custom_code
     *
     * @mbggenerated
     */
    private String customCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_sn
     *
     * @mbggenerated
     */
    private String goodsSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_number
     *
     * @mbggenerated
     */
    private Integer goodsNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_size_name
     *
     * @mbggenerated
     */
    private String goodsSizeName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_color_name
     *
     * @mbggenerated
     */
    private String goodsColorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.goods_thumb
     *
     * @mbggenerated
     */
    private String goodsThumb;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_order_line.supplier_code
     *
     * @mbggenerated
     */
    private String supplierCode;

    /**
     * 成本价
     */
    private BigDecimal price;

    /**
     * 进项税
     */
    private BigDecimal inputTax;

    /**
     * 销项税
     */
    private BigDecimal outputTax;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.id
     *
     * @return the value of purchase_order_line.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.id
     *
     * @param id the value for purchase_order_line.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.purchase_order_code
     *
     * @return the value of purchase_order_line.purchase_order_code
     *
     * @mbggenerated
     */
    public String getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.purchase_order_code
     *
     * @param purchaseOrderCode the value for purchase_order_line.purchase_order_code
     *
     * @mbggenerated
     */
    public void setPurchaseOrderCode(String purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode == null ? null : purchaseOrderCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.depot_code
     *
     * @return the value of purchase_order_line.depot_code
     *
     * @mbggenerated
     */
    public String getDepotCode() {
        return depotCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.depot_code
     *
     * @param depotCode the value for purchase_order_line.depot_code
     *
     * @mbggenerated
     */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode == null ? null : depotCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.master_order_sn
     *
     * @return the value of purchase_order_line.master_order_sn
     *
     * @mbggenerated
     */
    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.master_order_sn
     *
     * @param masterOrderSn the value for purchase_order_line.master_order_sn
     *
     * @mbggenerated
     */
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_name
     *
     * @return the value of purchase_order_line.goods_name
     *
     * @mbggenerated
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_name
     *
     * @param goodsName the value for purchase_order_line.goods_name
     *
     * @mbggenerated
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.custom_code
     *
     * @return the value of purchase_order_line.custom_code
     *
     * @mbggenerated
     */
    public String getCustomCode() {
        return customCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.custom_code
     *
     * @param customCode the value for purchase_order_line.custom_code
     *
     * @mbggenerated
     */
    public void setCustomCode(String customCode) {
        this.customCode = customCode == null ? null : customCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_sn
     *
     * @return the value of purchase_order_line.goods_sn
     *
     * @mbggenerated
     */
    public String getGoodsSn() {
        return goodsSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_sn
     *
     * @param goodsSn the value for purchase_order_line.goods_sn
     *
     * @mbggenerated
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn == null ? null : goodsSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_number
     *
     * @return the value of purchase_order_line.goods_number
     *
     * @mbggenerated
     */
    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_number
     *
     * @param goodsNumber the value for purchase_order_line.goods_number
     *
     * @mbggenerated
     */
    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_size_name
     *
     * @return the value of purchase_order_line.goods_size_name
     *
     * @mbggenerated
     */
    public String getGoodsSizeName() {
        return goodsSizeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_size_name
     *
     * @param goodsSizeName the value for purchase_order_line.goods_size_name
     *
     * @mbggenerated
     */
    public void setGoodsSizeName(String goodsSizeName) {
        this.goodsSizeName = goodsSizeName == null ? null : goodsSizeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_color_name
     *
     * @return the value of purchase_order_line.goods_color_name
     *
     * @mbggenerated
     */
    public String getGoodsColorName() {
        return goodsColorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_color_name
     *
     * @param goodsColorName the value for purchase_order_line.goods_color_name
     *
     * @mbggenerated
     */
    public void setGoodsColorName(String goodsColorName) {
        this.goodsColorName = goodsColorName == null ? null : goodsColorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.goods_thumb
     *
     * @return the value of purchase_order_line.goods_thumb
     *
     * @mbggenerated
     */
    public String getGoodsThumb() {
        return goodsThumb;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.goods_thumb
     *
     * @param goodsThumb the value for purchase_order_line.goods_thumb
     *
     * @mbggenerated
     */
    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb == null ? null : goodsThumb.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_order_line.supplier_code
     *
     * @return the value of purchase_order_line.supplier_code
     *
     * @mbggenerated
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_order_line.supplier_code
     *
     * @param supplierCode the value for purchase_order_line.supplier_code
     *
     * @mbggenerated
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getInputTax() {
        return inputTax;
    }

    public void setInputTax(BigDecimal inputTax) {
        this.inputTax = inputTax;
    }

    public BigDecimal getOutputTax() {
        return outputTax;
    }

    public void setOutputTax(BigDecimal outputTax) {
        this.outputTax = outputTax;
    }
}
