package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class GoodsReturnChange implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5959749960840864396L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.order_sn
     *
     * @mbggenerated
     */
    private String orderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.sku_sn
     *
     * @mbggenerated
     */
    private String skuSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.return_type
     *
     * @mbggenerated
     */
    private Integer returnType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.reason
     *
     * @mbggenerated
     */
    private Integer reason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.explain
     *
     * @mbggenerated
     */
    private String explain;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.redemption
     *
     * @mbggenerated
     */
    private Integer redemption;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.tag_type
     *
     * @mbggenerated
     */
    private Integer tagType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.exterior_type
     *
     * @mbggenerated
     */
    private Integer exteriorType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.gift_type
     *
     * @mbggenerated
     */
    private Integer giftType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.contact_name
     *
     * @mbggenerated
     */
    private String contactName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.contact_mobile
     *
     * @mbggenerated
     */
    private String contactMobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.contact_telephone_area
     *
     * @mbggenerated
     */
    private String contactTelephoneArea;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.contact_telephone
     *
     * @mbggenerated
     */
    private Integer contactTelephone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.contact_telephone_branch
     *
     * @mbggenerated
     */
    private Integer contactTelephoneBranch;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.return_sum
     *
     * @mbggenerated
     */
    private Integer returnSum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.create
     *
     * @mbggenerated
     */
    private Date create;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.returnchange_sn
     *
     * @mbggenerated
     */
    private String returnchangeSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.return_goods_img
     *
     * @mbggenerated
     */
    private String returnGoodsImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.site_code
     *
     * @mbggenerated
     */
    private String siteCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.shop_code
     *
     * @mbggenerated
     */
    private String shopCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.shop_name
     *
     * @mbggenerated
     */
    private String shopName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.store_code
     *
     * @mbggenerated
     */
    private String storeCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change.store_name
     *
     * @mbggenerated
     */
    private String storeName;

    private Integer isDel;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.id
     *
     * @return the value of goods_return_change.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.id
     *
     * @param id the value for goods_return_change.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.user_id
     *
     * @return the value of goods_return_change.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.user_id
     *
     * @param userId the value for goods_return_change.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.order_sn
     *
     * @return the value of goods_return_change.order_sn
     *
     * @mbggenerated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.order_sn
     *
     * @param orderSn the value for goods_return_change.order_sn
     *
     * @mbggenerated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.sku_sn
     *
     * @return the value of goods_return_change.sku_sn
     *
     * @mbggenerated
     */
    public String getSkuSn() {
        return skuSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.sku_sn
     *
     * @param skuSn the value for goods_return_change.sku_sn
     *
     * @mbggenerated
     */
    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn == null ? null : skuSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.return_type
     *
     * @return the value of goods_return_change.return_type
     *
     * @mbggenerated
     */
    public Integer getReturnType() {
        return returnType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.return_type
     *
     * @param returnType the value for goods_return_change.return_type
     *
     * @mbggenerated
     */
    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.reason
     *
     * @return the value of goods_return_change.reason
     *
     * @mbggenerated
     */
    public Integer getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.reason
     *
     * @param reason the value for goods_return_change.reason
     *
     * @mbggenerated
     */
    public void setReason(Integer reason) {
        this.reason = reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.explain
     *
     * @return the value of goods_return_change.explain
     *
     * @mbggenerated
     */
    public String getExplain() {
        return explain;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.explain
     *
     * @param explain the value for goods_return_change.explain
     *
     * @mbggenerated
     */
    public void setExplain(String explain) {
        this.explain = explain == null ? null : explain.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.redemption
     *
     * @return the value of goods_return_change.redemption
     *
     * @mbggenerated
     */
    public Integer getRedemption() {
        return redemption;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.redemption
     *
     * @param redemption the value for goods_return_change.redemption
     *
     * @mbggenerated
     */
    public void setRedemption(Integer redemption) {
        this.redemption = redemption;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.tag_type
     *
     * @return the value of goods_return_change.tag_type
     *
     * @mbggenerated
     */
    public Integer getTagType() {
        return tagType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.tag_type
     *
     * @param tagType the value for goods_return_change.tag_type
     *
     * @mbggenerated
     */
    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.exterior_type
     *
     * @return the value of goods_return_change.exterior_type
     *
     * @mbggenerated
     */
    public Integer getExteriorType() {
        return exteriorType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.exterior_type
     *
     * @param exteriorType the value for goods_return_change.exterior_type
     *
     * @mbggenerated
     */
    public void setExteriorType(Integer exteriorType) {
        this.exteriorType = exteriorType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.gift_type
     *
     * @return the value of goods_return_change.gift_type
     *
     * @mbggenerated
     */
    public Integer getGiftType() {
        return giftType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.gift_type
     *
     * @param giftType the value for goods_return_change.gift_type
     *
     * @mbggenerated
     */
    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.contact_name
     *
     * @return the value of goods_return_change.contact_name
     *
     * @mbggenerated
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.contact_name
     *
     * @param contactName the value for goods_return_change.contact_name
     *
     * @mbggenerated
     */
    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.contact_mobile
     *
     * @return the value of goods_return_change.contact_mobile
     *
     * @mbggenerated
     */
    public String getContactMobile() {
        return contactMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.contact_mobile
     *
     * @param contactMobile the value for goods_return_change.contact_mobile
     *
     * @mbggenerated
     */
    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.contact_telephone_area
     *
     * @return the value of goods_return_change.contact_telephone_area
     *
     * @mbggenerated
     */
    public String getContactTelephoneArea() {
        return contactTelephoneArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.contact_telephone_area
     *
     * @param contactTelephoneArea the value for goods_return_change.contact_telephone_area
     *
     * @mbggenerated
     */
    public void setContactTelephoneArea(String contactTelephoneArea) {
        this.contactTelephoneArea = contactTelephoneArea == null ? null : contactTelephoneArea.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.contact_telephone
     *
     * @return the value of goods_return_change.contact_telephone
     *
     * @mbggenerated
     */
    public Integer getContactTelephone() {
        return contactTelephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.contact_telephone
     *
     * @param contactTelephone the value for goods_return_change.contact_telephone
     *
     * @mbggenerated
     */
    public void setContactTelephone(Integer contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.contact_telephone_branch
     *
     * @return the value of goods_return_change.contact_telephone_branch
     *
     * @mbggenerated
     */
    public Integer getContactTelephoneBranch() {
        return contactTelephoneBranch;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.contact_telephone_branch
     *
     * @param contactTelephoneBranch the value for goods_return_change.contact_telephone_branch
     *
     * @mbggenerated
     */
    public void setContactTelephoneBranch(Integer contactTelephoneBranch) {
        this.contactTelephoneBranch = contactTelephoneBranch;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.remark
     *
     * @return the value of goods_return_change.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.remark
     *
     * @param remark the value for goods_return_change.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.status
     *
     * @return the value of goods_return_change.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.status
     *
     * @param status the value for goods_return_change.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.return_sum
     *
     * @return the value of goods_return_change.return_sum
     *
     * @mbggenerated
     */
    public Integer getReturnSum() {
        return returnSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.return_sum
     *
     * @param returnSum the value for goods_return_change.return_sum
     *
     * @mbggenerated
     */
    public void setReturnSum(Integer returnSum) {
        this.returnSum = returnSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.create
     *
     * @return the value of goods_return_change.create
     *
     * @mbggenerated
     */
    public Date getCreate() {
        return create;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.create
     *
     * @param create the value for goods_return_change.create
     *
     * @mbggenerated
     */
    public void setCreate(Date create) {
        this.create = create;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.returnchange_sn
     *
     * @return the value of goods_return_change.returnchange_sn
     *
     * @mbggenerated
     */
    public String getReturnchangeSn() {
        return returnchangeSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.returnchange_sn
     *
     * @param returnchangeSn the value for goods_return_change.returnchange_sn
     *
     * @mbggenerated
     */
    public void setReturnchangeSn(String returnchangeSn) {
        this.returnchangeSn = returnchangeSn == null ? null : returnchangeSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.return_goods_img
     *
     * @return the value of goods_return_change.return_goods_img
     *
     * @mbggenerated
     */
    public String getReturnGoodsImg() {
        return returnGoodsImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.return_goods_img
     *
     * @param returnGoodsImg the value for goods_return_change.return_goods_img
     *
     * @mbggenerated
     */
    public void setReturnGoodsImg(String returnGoodsImg) {
        this.returnGoodsImg = returnGoodsImg == null ? null : returnGoodsImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.site_code
     *
     * @return the value of goods_return_change.site_code
     *
     * @mbggenerated
     */
    public String getSiteCode() {
        return siteCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.site_code
     *
     * @param siteCode the value for goods_return_change.site_code
     *
     * @mbggenerated
     */
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode == null ? null : siteCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.shop_code
     *
     * @return the value of goods_return_change.shop_code
     *
     * @mbggenerated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.shop_code
     *
     * @param shopCode the value for goods_return_change.shop_code
     *
     * @mbggenerated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.shop_name
     *
     * @return the value of goods_return_change.shop_name
     *
     * @mbggenerated
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.shop_name
     *
     * @param shopName the value for goods_return_change.shop_name
     *
     * @mbggenerated
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.store_code
     *
     * @return the value of goods_return_change.store_code
     *
     * @mbggenerated
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.store_code
     *
     * @param storeCode the value for goods_return_change.store_code
     *
     * @mbggenerated
     */
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode == null ? null : storeCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change.store_name
     *
     * @return the value of goods_return_change.store_name
     *
     * @mbggenerated
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change.store_name
     *
     * @param storeName the value for goods_return_change.store_name
     *
     * @mbggenerated
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
