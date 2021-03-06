package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class OrderReturnShip implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5925880724265256340L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.relating_return_sn
     *
     * @mbggenerated
     */
    private String relatingReturnSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.chased_or_not
     *
     * @mbggenerated
     */
    private Integer chasedOrNot;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.is_good_received
     *
     * @mbggenerated
     */
    private Byte isGoodReceived;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.checkin_status
     *
     * @mbggenerated
     */
    private Byte checkinStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.quality_status
     *
     * @mbggenerated
     */
    private Integer qualityStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.depot_code
     *
     * @mbggenerated
     */
    private String depotCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.return_express
     *
     * @mbggenerated
     */
    private String returnExpress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.return_invoice_no
     *
     * @mbggenerated
     */
    private String returnInvoiceNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.return_express_img
     *
     * @mbggenerated
     */
    private String returnExpressImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.repair_advice
     *
     * @mbggenerated
     */
    private Integer repairAdvice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.chase_time
     *
     * @mbggenerated
     */
    private Date chaseTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.received_time
     *
     * @mbggenerated
     */
    private Date receivedTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.quality_time
     *
     * @mbggenerated
     */
    private Date qualityTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.checkin_time
     *
     * @mbggenerated
     */
    private Date checkinTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_return_ship.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.relating_return_sn
     *
     * @return the value of order_return_ship.relating_return_sn
     *
     * @mbggenerated
     */
    public String getRelatingReturnSn() {
        return relatingReturnSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.relating_return_sn
     *
     * @param relatingReturnSn the value for order_return_ship.relating_return_sn
     *
     * @mbggenerated
     */
    public void setRelatingReturnSn(String relatingReturnSn) {
        this.relatingReturnSn = relatingReturnSn == null ? null : relatingReturnSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.chased_or_not
     *
     * @return the value of order_return_ship.chased_or_not
     *
     * @mbggenerated
     */
    public Integer getChasedOrNot() {
        return chasedOrNot;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.chased_or_not
     *
     * @param chasedOrNot the value for order_return_ship.chased_or_not
     *
     * @mbggenerated
     */
    public void setChasedOrNot(Integer chasedOrNot) {
        this.chasedOrNot = chasedOrNot;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.is_good_received
     *
     * @return the value of order_return_ship.is_good_received
     *
     * @mbggenerated
     */
    public Byte getIsGoodReceived() {
        return isGoodReceived;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.is_good_received
     *
     * @param isGoodReceived the value for order_return_ship.is_good_received
     *
     * @mbggenerated
     */
    public void setIsGoodReceived(Byte isGoodReceived) {
        this.isGoodReceived = isGoodReceived;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.checkin_status
     *
     * @return the value of order_return_ship.checkin_status
     *
     * @mbggenerated
     */
    public Byte getCheckinStatus() {
        return checkinStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.checkin_status
     *
     * @param checkinStatus the value for order_return_ship.checkin_status
     *
     * @mbggenerated
     */
    public void setCheckinStatus(Byte checkinStatus) {
        this.checkinStatus = checkinStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.quality_status
     *
     * @return the value of order_return_ship.quality_status
     *
     * @mbggenerated
     */
    public Integer getQualityStatus() {
        return qualityStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.quality_status
     *
     * @param qualityStatus the value for order_return_ship.quality_status
     *
     * @mbggenerated
     */
    public void setQualityStatus(Integer qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.depot_code
     *
     * @return the value of order_return_ship.depot_code
     *
     * @mbggenerated
     */
    public String getDepotCode() {
        return depotCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.depot_code
     *
     * @param depotCode the value for order_return_ship.depot_code
     *
     * @mbggenerated
     */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode == null ? null : depotCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.return_express
     *
     * @return the value of order_return_ship.return_express
     *
     * @mbggenerated
     */
    public String getReturnExpress() {
        return returnExpress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.return_express
     *
     * @param returnExpress the value for order_return_ship.return_express
     *
     * @mbggenerated
     */
    public void setReturnExpress(String returnExpress) {
        this.returnExpress = returnExpress == null ? null : returnExpress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.return_invoice_no
     *
     * @return the value of order_return_ship.return_invoice_no
     *
     * @mbggenerated
     */
    public String getReturnInvoiceNo() {
        return returnInvoiceNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.return_invoice_no
     *
     * @param returnInvoiceNo the value for order_return_ship.return_invoice_no
     *
     * @mbggenerated
     */
    public void setReturnInvoiceNo(String returnInvoiceNo) {
        this.returnInvoiceNo = returnInvoiceNo == null ? null : returnInvoiceNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.return_express_img
     *
     * @return the value of order_return_ship.return_express_img
     *
     * @mbggenerated
     */
    public String getReturnExpressImg() {
        return returnExpressImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.return_express_img
     *
     * @param returnExpressImg the value for order_return_ship.return_express_img
     *
     * @mbggenerated
     */
    public void setReturnExpressImg(String returnExpressImg) {
        this.returnExpressImg = returnExpressImg == null ? null : returnExpressImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.repair_advice
     *
     * @return the value of order_return_ship.repair_advice
     *
     * @mbggenerated
     */
    public Integer getRepairAdvice() {
        return repairAdvice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.repair_advice
     *
     * @param repairAdvice the value for order_return_ship.repair_advice
     *
     * @mbggenerated
     */
    public void setRepairAdvice(Integer repairAdvice) {
        this.repairAdvice = repairAdvice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.chase_time
     *
     * @return the value of order_return_ship.chase_time
     *
     * @mbggenerated
     */
    public Date getChaseTime() {
        return chaseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.chase_time
     *
     * @param chaseTime the value for order_return_ship.chase_time
     *
     * @mbggenerated
     */
    public void setChaseTime(Date chaseTime) {
        this.chaseTime = chaseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.received_time
     *
     * @return the value of order_return_ship.received_time
     *
     * @mbggenerated
     */
    public Date getReceivedTime() {
        return receivedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.received_time
     *
     * @param receivedTime the value for order_return_ship.received_time
     *
     * @mbggenerated
     */
    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.quality_time
     *
     * @return the value of order_return_ship.quality_time
     *
     * @mbggenerated
     */
    public Date getQualityTime() {
        return qualityTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.quality_time
     *
     * @param qualityTime the value for order_return_ship.quality_time
     *
     * @mbggenerated
     */
    public void setQualityTime(Date qualityTime) {
        this.qualityTime = qualityTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.checkin_time
     *
     * @return the value of order_return_ship.checkin_time
     *
     * @mbggenerated
     */
    public Date getCheckinTime() {
        return checkinTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.checkin_time
     *
     * @param checkinTime the value for order_return_ship.checkin_time
     *
     * @mbggenerated
     */
    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.add_time
     *
     * @return the value of order_return_ship.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.add_time
     *
     * @param addTime the value for order_return_ship.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_return_ship.update_time
     *
     * @return the value of order_return_ship.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_return_ship.update_time
     *
     * @param updateTime the value for order_return_ship.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}