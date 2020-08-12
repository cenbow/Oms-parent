/*
* 2014-8-18 上午10:29:31
* 吴健 HQ01U8435
*/

package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;

public class ProductGroupBuy implements Serializable{

    /**
     *id
     */
    private Integer id;

    /**
     *团购编码（yyyyMMdd+流水号）
     */
    private String groupBuyCode;

    /**
     *活动名称
     */
    private String groupBuyName;

    /**
     *开始时间
     */
    private Date beginTime;

    /**
     *结束时间
     */
    private Date endTime;

    /**
     *站点编码
     */
    private String siteCode;

    /**
     *店铺编码
     */
    private String channelCode;

    /**
     *团购商品类型（1品牌，2商品）
     */
    private byte groupGoodsType;

    /**
     *品牌编码
     */
    private String brandCode;

    /**
     *限购规则（0不限购、1限购）
     */
    private Short limitType;

    /**
     *限购类型（1件数、2重量）
     */
    private Short goodsType;

    /**
     *起订量(件数、总量吨)
     */
    private Double limitMin;

    /**
     *最大量(件数、重量吨)
     */
    private Double limitMax;

    /**
     *付款方式：0全额、1预付款
     */
    private Short payType;

    /**
     *付款比例
     */
    private Double payRate;

    /**
     *价格规则[{“10000”:"9.7"},{“20000”:"9.2"}]
     */
    private String priceRule;

    /**
     *状态（1团购中、2团购成功、3团购失败）
     */
    private Short groupBuyStatus;

    /**
     *订单数
     */
    private Integer orderTotal;

    /**
     *订单金额
     */
    private Double orderMoney;

    /**
     *成团人数
     */
    private Integer orderUser;

    /**
     *团购已购总件数/重量
     */
    private Double orderAmount;

    /**
     *渠道类型（1为pc，2为H5）
     */
    private Short channelType;

    /**
     *图标url
     */
    private String logoUrl;

    /**
     *团状态（-1为删除，0为禁用，1为正常，2为待批）
     */
    private Short status;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建人
     */
    private String createUser;

    /**
     *最后修改时间
     */
    private Date lastUpdateTime;

    /**
     *最后修改人
     */
    private String lastUpdateUser;

    /**
     * spu
     */
    private String productSysCode;

    public String getProductSysCode() {
        return productSysCode;
    }

    public void setProductSysCode(String productSysCode) {
        this.productSysCode = productSysCode;
    }


    public void setId(Integer id){
        this.id=id;
    }

    public Integer getId(){
        return id;
    }

    public void setGroupBuyCode(String groupBuyCode){
        this.groupBuyCode=groupBuyCode;
    }

    public String getGroupBuyCode(){
        return groupBuyCode;
    }

    public void setGroupBuyName(String groupBuyName){
        this.groupBuyName=groupBuyName;
    }

    public String getGroupBuyName(){
        return groupBuyName;
    }

    public void setBeginTime(Date beginTime){
        this.beginTime=beginTime;
    }

    public Date getBeginTime(){
        return beginTime;
    }

    public void setEndTime(Date endTime){
        this.endTime=endTime;
    }

    public Date getEndTime(){
        return endTime;
    }

    public void setSiteCode(String siteCode){
        this.siteCode=siteCode;
    }

    public String getSiteCode(){
        return siteCode;
    }

    public void setChannelCode(String channelCode){
        this.channelCode=channelCode;
    }

    public String getChannelCode(){
        return channelCode;
    }

    public void setGroupGoodsType(byte groupGoodsType){
        this.groupGoodsType=groupGoodsType;
    }

    public byte getGroupGoodsType(){
        return groupGoodsType;
    }

    public void setBrandCode(String brandCode){
        this.brandCode=brandCode;
    }

    public String getBrandCode(){
        return brandCode;
    }

    public void setLimitType(Short limitType){
        this.limitType=limitType;
    }

    public Short getLimitType(){
        return limitType;
    }

    public void setGoodsType(Short goodsType){
        this.goodsType=goodsType;
    }

    public Short getGoodsType(){
        return goodsType;
    }

    public void setLimitMin(Double limitMin){
        this.limitMin=limitMin;
    }

    public Double getLimitMin(){
        return limitMin;
    }

    public void setLimitMax(Double limitMax){
        this.limitMax=limitMax;
    }

    public Double getLimitMax(){
        return limitMax;
    }

    public void setPayType(Short payType){
        this.payType=payType;
    }

    public Short getPayType(){
        return payType;
    }

    public void setPayRate(Double payRate){
        this.payRate=payRate;
    }

    public Double getPayRate(){
        return payRate;
    }


    public void setGroupBuyStatus(Short groupBuyStatus){
        this.groupBuyStatus=groupBuyStatus;
    }

    public Short getGroupBuyStatus(){
        return groupBuyStatus;
    }

    public void setOrderTotal(Integer orderTotal){
        this.orderTotal=orderTotal;
    }

    public Integer getOrderTotal(){
        return orderTotal;
    }

    public void setOrderMoney(Double orderMoney){
        this.orderMoney=orderMoney;
    }

    public Double getOrderMoney(){
        return orderMoney;
    }

    public void setOrderUser(Integer orderUser){
        this.orderUser=orderUser;
    }

    public Integer getOrderUser(){
        return orderUser;
    }

    public void setOrderAmount(Double orderAmount){
        this.orderAmount=orderAmount;
    }

    public Double getOrderAmount(){
        return orderAmount;
    }

    public void setChannelType(Short channelType){
        this.channelType=channelType;
    }

    public Short getChannelType(){
        return channelType;
    }


    public void setStatus(Short status){
        this.status=status;
    }

    public Short getStatus(){
        return status;
    }

    public void setCreateTime(Date createTime){
        this.createTime=createTime;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateUser(String createUser){
        this.createUser=createUser;
    }

    public String getCreateUser(){
        return createUser;
    }

    public void setLastUpdateTime(Date lastUpdateTime){
        this.lastUpdateTime=lastUpdateTime;
    }

    public Date getLastUpdateTime(){
        return lastUpdateTime;
    }

    public void setLastUpdateUser(String lastUpdateUser){
        this.lastUpdateUser=lastUpdateUser;
    }

    public String getLastUpdateUser(){
        return lastUpdateUser;
    }

    public String getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(String priceRule) {
        this.priceRule = priceRule;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }



}
