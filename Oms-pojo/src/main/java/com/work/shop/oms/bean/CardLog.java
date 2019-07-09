package com.work.shop.oms.bean;

import java.util.Date;

public class CardLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.cl_id
     *
     * @mbggenerated
     */
    private Integer clId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.cl_desc
     *
     * @mbggenerated
     */
    private String clDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.cl_type
     *
     * @mbggenerated
     */
    private Byte clType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.card_no
     *
     * @mbggenerated
     */
    private String cardNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.card_type_en_name
     *
     * @mbggenerated
     */
    private String cardTypeEnName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.card_money
     *
     * @mbggenerated
     */
    private Float cardMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_log.comefrom
     *
     * @mbggenerated
     */
    private String comefrom;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.cl_id
     *
     * @return the value of card_log.cl_id
     *
     * @mbggenerated
     */
    public Integer getClId() {
        return clId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.cl_id
     *
     * @param clId the value for card_log.cl_id
     *
     * @mbggenerated
     */
    public void setClId(Integer clId) {
        this.clId = clId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.cl_desc
     *
     * @return the value of card_log.cl_desc
     *
     * @mbggenerated
     */
    public String getClDesc() {
        return clDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.cl_desc
     *
     * @param clDesc the value for card_log.cl_desc
     *
     * @mbggenerated
     */
    public void setClDesc(String clDesc) {
        this.clDesc = clDesc == null ? null : clDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.user_id
     *
     * @return the value of card_log.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.user_id
     *
     * @param userId the value for card_log.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.cl_type
     *
     * @return the value of card_log.cl_type
     *
     * @mbggenerated
     */
    public Byte getClType() {
        return clType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.cl_type
     *
     * @param clType the value for card_log.cl_type
     *
     * @mbggenerated
     */
    public void setClType(Byte clType) {
        this.clType = clType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.create_time
     *
     * @return the value of card_log.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.create_time
     *
     * @param createTime the value for card_log.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.card_no
     *
     * @return the value of card_log.card_no
     *
     * @mbggenerated
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.card_no
     *
     * @param cardNo the value for card_log.card_no
     *
     * @mbggenerated
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.card_type_en_name
     *
     * @return the value of card_log.card_type_en_name
     *
     * @mbggenerated
     */
    public String getCardTypeEnName() {
        return cardTypeEnName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.card_type_en_name
     *
     * @param cardTypeEnName the value for card_log.card_type_en_name
     *
     * @mbggenerated
     */
    public void setCardTypeEnName(String cardTypeEnName) {
        this.cardTypeEnName = cardTypeEnName == null ? null : cardTypeEnName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.card_money
     *
     * @return the value of card_log.card_money
     *
     * @mbggenerated
     */
    public Float getCardMoney() {
        return cardMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.card_money
     *
     * @param cardMoney the value for card_log.card_money
     *
     * @mbggenerated
     */
    public void setCardMoney(Float cardMoney) {
        this.cardMoney = cardMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_log.comefrom
     *
     * @return the value of card_log.comefrom
     *
     * @mbggenerated
     */
    public String getComefrom() {
        return comefrom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_log.comefrom
     *
     * @param comefrom the value for card_log.comefrom
     *
     * @mbggenerated
     */
    public void setComefrom(String comefrom) {
        this.comefrom = comefrom == null ? null : comefrom.trim();
    }
}