package com.work.shop.oms.api.ship.bean;

import java.io.Serializable;
import java.util.Date;

public class WkUdDistribute implements Serializable {
	private static final long serialVersionUID = 3898531623221266640L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.wk_id
     *
     * @mbggenerated
     */
    private Integer wkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.dist_id
     *
     * @mbggenerated
     */
    private Integer distId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.outer_code
     *
     * @mbggenerated
     */
    private String outerCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.prod_id
     *
     * @mbggenerated
     */
    private String prodId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.distqty_0
     *
     * @mbggenerated
     */
    private Integer distqty0;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.distqty_1
     *
     * @mbggenerated
     */
    private Integer distqty1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.dist_wareh_code
     *
     * @mbggenerated
     */
    private String distWarehCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.region_id
     *
     * @mbggenerated
     */
    private Short regionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.ship_code
     *
     * @mbggenerated
     */
    private String shipCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.create_date
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.last_modified_date
     *
     * @mbggenerated
     */
    private Date lastModifiedDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.distrate
     *
     * @mbggenerated
     */
    private Float distrate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.rcv_warehcode
     *
     * @mbggenerated
     * @deprecated
     */
    private String rcvWarehcode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.agdist
     *
     * @mbggenerated
     */
    private Byte agdist;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.ship_sn
     *
     * @mbggenerated
     */
    private String shipSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.goods_number
     *
     * @mbggenerated
     */
    private String goodsNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.county
     *
     * @mbggenerated
     */
    private String county;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.city
     *
     * @mbggenerated
     */
    private String city;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.province
     *
     * @mbggenerated
     */
    private String province;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.country
     *
     * @mbggenerated
     */
    private String country;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.ship_type
     *
     * @mbggenerated
     */
    private Integer shipType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.ship_flag
     *
     * @mbggenerated
     */
    private Integer shipFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.syn_flag
     *
     * @mbggenerated
     */
    private Integer synFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.vendee_agent_name
     *
     * @mbggenerated
     */
    private String vendeeAgentName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.pdwarh_code
     *
     * @mbggenerated
     */
    private String pdwarhCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.pdwarh_name
     *
     * @mbggenerated
     */
    private String pdwarhName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.to_user
     *
     * @mbggenerated
     */
    private String toUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.to_user_phone
     *
     * @mbggenerated
     */
    private String toUserPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.provincecity
     *
     * @mbggenerated
     */
    private String provincecity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.over_trans_cycle
     *
     * @mbggenerated
     */
    private String overTransCycle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wk_ud_distribute.depot_name
     *
     * @mbggenerated
     */
    private String depotName;
    
    private String extensionId;

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.wk_id
     *
     * @return the value of wk_ud_distribute.wk_id
     *
     * @mbggenerated
     */
    public Integer getWkId() {
        return wkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.wk_id
     *
     * @param wkId the value for wk_ud_distribute.wk_id
     *
     * @mbggenerated
     */
    public void setWkId(Integer wkId) {
        this.wkId = wkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.dist_id
     *
     * @return the value of wk_ud_distribute.dist_id
     *
     * @mbggenerated
     */
    public Integer getDistId() {
        return distId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.dist_id
     *
     * @param distId the value for wk_ud_distribute.dist_id
     *
     * @mbggenerated
     */
    public void setDistId(Integer distId) {
        this.distId = distId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.outer_code
     *
     * @return the value of wk_ud_distribute.outer_code
     *
     * @mbggenerated
     */
    public String getOuterCode() {
        return outerCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.outer_code
     *
     * @param outerCode the value for wk_ud_distribute.outer_code
     *
     * @mbggenerated
     */
    public void setOuterCode(String outerCode) {
        this.outerCode = outerCode == null ? null : outerCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.prod_id
     *
     * @return the value of wk_ud_distribute.prod_id
     *
     * @mbggenerated
     */
    public String getProdId() {
        return prodId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.prod_id
     *
     * @param prodId the value for wk_ud_distribute.prod_id
     *
     * @mbggenerated
     */
    public void setProdId(String prodId) {
        this.prodId = prodId == null ? null : prodId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.distqty_0
     *
     * @return the value of wk_ud_distribute.distqty_0
     *
     * @mbggenerated
     */
    public Integer getDistqty0() {
        return distqty0;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.distqty_0
     *
     * @param distqty0 the value for wk_ud_distribute.distqty_0
     *
     * @mbggenerated
     */
    public void setDistqty0(Integer distqty0) {
        this.distqty0 = distqty0;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.distqty_1
     *
     * @return the value of wk_ud_distribute.distqty_1
     *
     * @mbggenerated
     */
    public Integer getDistqty1() {
        return distqty1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.distqty_1
     *
     * @param distqty1 the value for wk_ud_distribute.distqty_1
     *
     * @mbggenerated
     */
    public void setDistqty1(Integer distqty1) {
        this.distqty1 = distqty1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.dist_wareh_code
     *
     * @return the value of wk_ud_distribute.dist_wareh_code
     *
     * @mbggenerated
     */
    public String getDistWarehCode() {
        return distWarehCode;
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.dist_wareh_code
     *
     * @param distWarehCode the value for wk_ud_distribute.dist_wareh_code
     *
     * @mbggenerated
     */
    public void setDistWarehCode(String distWarehCode) {
        this.distWarehCode = distWarehCode == null ? null : distWarehCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.region_id
     *
     * @return the value of wk_ud_distribute.region_id
     *
     * @mbggenerated
     */
    public Short getRegionId() {
        return regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.region_id
     *
     * @param regionId the value for wk_ud_distribute.region_id
     *
     * @mbggenerated
     */
    public void setRegionId(Short regionId) {
        this.regionId = regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.ship_code
     *
     * @return the value of wk_ud_distribute.ship_code
     *
     * @mbggenerated
     */
    public String getShipCode() {
        return shipCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.ship_code
     *
     * @param shipCode the value for wk_ud_distribute.ship_code
     *
     * @mbggenerated
     */
    public void setShipCode(String shipCode) {
        this.shipCode = shipCode == null ? null : shipCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.create_date
     *
     * @return the value of wk_ud_distribute.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.create_date
     *
     * @param createDate the value for wk_ud_distribute.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.last_modified_date
     *
     * @return the value of wk_ud_distribute.last_modified_date
     *
     * @mbggenerated
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.last_modified_date
     *
     * @param lastModifiedDate the value for wk_ud_distribute.last_modified_date
     *
     * @mbggenerated
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.distrate
     *
     * @return the value of wk_ud_distribute.distrate
     *
     * @mbggenerated
     */
    public Float getDistrate() {
        return distrate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.distrate
     *
     * @param distrate the value for wk_ud_distribute.distrate
     *
     * @mbggenerated
     */
    public void setDistrate(Float distrate) {
        this.distrate = distrate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.rcv_warehcode
     *
     * @return the value of wk_ud_distribute.rcv_warehcode
     *
     * @mbggenerated
     */
    public String getRcvWarehcode() {
        return rcvWarehcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.rcv_warehcode
     *
     * @param rcvWarehcode the value for wk_ud_distribute.rcv_warehcode
     *
     * @mbggenerated
     */
    public void setRcvWarehcode(String rcvWarehcode) {
        this.rcvWarehcode = rcvWarehcode == null ? null : rcvWarehcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.agdist
     *
     * @return the value of wk_ud_distribute.agdist
     *
     * @mbggenerated
     */
    public Byte getAgdist() {
        return agdist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.agdist
     *
     * @param agdist the value for wk_ud_distribute.agdist
     *
     * @mbggenerated
     */
    public void setAgdist(Byte agdist) {
        this.agdist = agdist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.ship_sn
     *
     * @return the value of wk_ud_distribute.ship_sn
     *
     * @mbggenerated
     */
    public String getShipSn() {
        return shipSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.ship_sn
     *
     * @param shipSn the value for wk_ud_distribute.ship_sn
     *
     * @mbggenerated
     */
    public void setShipSn(String shipSn) {
        this.shipSn = shipSn == null ? null : shipSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.goods_number
     *
     * @return the value of wk_ud_distribute.goods_number
     *
     * @mbggenerated
     */
    public String getGoodsNumber() {
        return goodsNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.goods_number
     *
     * @param goodsNumber the value for wk_ud_distribute.goods_number
     *
     * @mbggenerated
     */
    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber == null ? null : goodsNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.county
     *
     * @return the value of wk_ud_distribute.county
     *
     * @mbggenerated
     */
    public String getCounty() {
        return county;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.county
     *
     * @param county the value for wk_ud_distribute.county
     *
     * @mbggenerated
     */
    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.city
     *
     * @return the value of wk_ud_distribute.city
     *
     * @mbggenerated
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.city
     *
     * @param city the value for wk_ud_distribute.city
     *
     * @mbggenerated
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.province
     *
     * @return the value of wk_ud_distribute.province
     *
     * @mbggenerated
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.province
     *
     * @param province the value for wk_ud_distribute.province
     *
     * @mbggenerated
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.country
     *
     * @return the value of wk_ud_distribute.country
     *
     * @mbggenerated
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.country
     *
     * @param country the value for wk_ud_distribute.country
     *
     * @mbggenerated
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.ship_type
     *
     * @return the value of wk_ud_distribute.ship_type
     *
     * @mbggenerated
     */
    public Integer getShipType() {
        return shipType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.ship_type
     *
     * @param shipType the value for wk_ud_distribute.ship_type
     *
     * @mbggenerated
     */
    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.ship_flag
     *
     * @return the value of wk_ud_distribute.ship_flag
     *
     * @mbggenerated
     */
    public Integer getShipFlag() {
        return shipFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.ship_flag
     *
     * @param shipFlag the value for wk_ud_distribute.ship_flag
     *
     * @mbggenerated
     */
    public void setShipFlag(Integer shipFlag) {
        this.shipFlag = shipFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.update_time
     *
     * @return the value of wk_ud_distribute.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.update_time
     *
     * @param updateTime the value for wk_ud_distribute.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.syn_flag
     *
     * @return the value of wk_ud_distribute.syn_flag
     *
     * @mbggenerated
     */
    public Integer getSynFlag() {
        return synFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.syn_flag
     *
     * @param synFlag the value for wk_ud_distribute.syn_flag
     *
     * @mbggenerated
     */
    public void setSynFlag(Integer synFlag) {
        this.synFlag = synFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.vendee_agent_name
     *
     * @return the value of wk_ud_distribute.vendee_agent_name
     *
     * @mbggenerated
     */
    public String getVendeeAgentName() {
        return vendeeAgentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.vendee_agent_name
     *
     * @param vendeeAgentName the value for wk_ud_distribute.vendee_agent_name
     *
     * @mbggenerated
     */
    public void setVendeeAgentName(String vendeeAgentName) {
        this.vendeeAgentName = vendeeAgentName == null ? null : vendeeAgentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.pdwarh_code
     *
     * @return the value of wk_ud_distribute.pdwarh_code
     *
     * @mbggenerated
     */
    public String getPdwarhCode() {
        return pdwarhCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.pdwarh_code
     *
     * @param pdwarhCode the value for wk_ud_distribute.pdwarh_code
     *
     * @mbggenerated
     */
    public void setPdwarhCode(String pdwarhCode) {
        this.pdwarhCode = pdwarhCode == null ? null : pdwarhCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.pdwarh_name
     *
     * @return the value of wk_ud_distribute.pdwarh_name
     *
     * @mbggenerated
     */
    public String getPdwarhName() {
        return pdwarhName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.pdwarh_name
     *
     * @param pdwarhName the value for wk_ud_distribute.pdwarh_name
     *
     * @mbggenerated
     */
    public void setPdwarhName(String pdwarhName) {
        this.pdwarhName = pdwarhName == null ? null : pdwarhName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.to_user
     *
     * @return the value of wk_ud_distribute.to_user
     *
     * @mbggenerated
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.to_user
     *
     * @param toUser the value for wk_ud_distribute.to_user
     *
     * @mbggenerated
     */
    public void setToUser(String toUser) {
        this.toUser = toUser == null ? null : toUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.to_user_phone
     *
     * @return the value of wk_ud_distribute.to_user_phone
     *
     * @mbggenerated
     */
    public String getToUserPhone() {
        return toUserPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.to_user_phone
     *
     * @param toUserPhone the value for wk_ud_distribute.to_user_phone
     *
     * @mbggenerated
     */
    public void setToUserPhone(String toUserPhone) {
        this.toUserPhone = toUserPhone == null ? null : toUserPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.provincecity
     *
     * @return the value of wk_ud_distribute.provincecity
     *
     * @mbggenerated
     */
    public String getProvincecity() {
        return provincecity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.provincecity
     *
     * @param provincecity the value for wk_ud_distribute.provincecity
     *
     * @mbggenerated
     */
    public void setProvincecity(String provincecity) {
        this.provincecity = provincecity == null ? null : provincecity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.over_trans_cycle
     *
     * @return the value of wk_ud_distribute.over_trans_cycle
     *
     * @mbggenerated
     */
    public String getOverTransCycle() {
        return overTransCycle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.over_trans_cycle
     *
     * @param overTransCycle the value for wk_ud_distribute.over_trans_cycle
     *
     * @mbggenerated
     */
    public void setOverTransCycle(String overTransCycle) {
        this.overTransCycle = overTransCycle == null ? null : overTransCycle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wk_ud_distribute.depot_name
     *
     * @return the value of wk_ud_distribute.depot_name
     *
     * @mbggenerated
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wk_ud_distribute.depot_name
     *
     * @param depotName the value for wk_ud_distribute.depot_name
     *
     * @mbggenerated
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	@Override
	public String toString() {
		return "WkUdDistribute [wkId=" + wkId + ", distId=" + distId + ", outerCode=" + outerCode + ", prodId=" + prodId + ", distqty0=" + distqty0 + ", distqty1=" + distqty1 + ", distWarehCode=" + distWarehCode + ", regionId=" + regionId + ", shipCode=" + shipCode + ", createDate=" + createDate
				+ ", lastModifiedDate=" + lastModifiedDate + ", distrate=" + distrate + ", rcvWarehcode=" + rcvWarehcode + ", agdist=" + agdist + ", shipSn=" + shipSn + ", goodsNumber=" + goodsNumber + ", county=" + county + ", city=" + city + ", province=" + province + ", country=" + country
				+ ", shipType=" + shipType + ", shipFlag=" + shipFlag + ", updateTime=" + updateTime + ", synFlag=" + synFlag + ", vendeeAgentName=" + vendeeAgentName + ", pdwarhCode=" + pdwarhCode + ", pdwarhName=" + pdwarhName + ", toUser=" + toUser + ", toUserPhone=" + toUserPhone
				+ ", provincecity=" + provincecity + ", overTransCycle=" + overTransCycle + ", depotName=" + depotName + "]";
	}
    
    
}