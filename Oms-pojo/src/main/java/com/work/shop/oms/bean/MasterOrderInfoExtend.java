package com.work.shop.oms.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单扩展信息
 * @author QuYachu
 */
public class MasterOrderInfoExtend {

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.master_order_sn
     *
     * @mbggenerated
     */
    private String masterOrderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.agdist
     *
     * @mbggenerated
     */
    private Byte agdist;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_group
     *
     * @mbggenerated
     */
    private Byte isGroup;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_advance
     *
     * @mbggenerated
     */
    private Byte isAdvance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_shop
     *
     * @mbggenerated
     */
    private Integer isShop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.shop_id
     *
     * @mbggenerated
     */
    private String shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.shop_name
     *
     * @mbggenerated
     */
    private String shopName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.shop_time
     *
     * @mbggenerated
     */
    private Date shopTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_cac
     *
     * @mbggenerated
     */
    private Integer isCac;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.rule_promotion
     *
     * @mbggenerated
     */
    private String rulePromotion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.revive_stt
     *
     * @mbggenerated
     */
    private Byte reviveStt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.enc_mobile
     *
     * @mbggenerated
     */
    private Integer encMobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.shop_code
     *
     * @mbggenerated
     */
    private String shopCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.assistant_id
     *
     * @mbggenerated
     */
    private String assistantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.order_finished
     *
     * @mbggenerated
     */
    private Integer orderFinished;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.settle_queue
     *
     * @mbggenerated
     */
    private Integer settleQueue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.bill_no
     *
     * @mbggenerated
     */
    private String billNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_review
     *
     * @mbggenerated
     */
    private Integer isReview;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.use_level
     *
     * @mbggenerated
     */
    private Integer useLevel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.channel_user_level
     *
     * @mbggenerated
     */
    private Integer channelUserLevel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.is_order_print
     *
     * @mbggenerated
     */
    private Integer isOrderPrint;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.inv_payee
     *
     * @mbggenerated
     */
    private String invPayee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.inv_content
     *
     * @mbggenerated
     */
    private String invContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.inv_type
     *
     * @mbggenerated
     */
    private String invType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.tax
     *
     * @mbggenerated
     */
    private BigDecimal tax;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.invoice_status
     *
     * @mbggenerated
     */
    private Integer invoiceStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.delivery_station_id
     *
     * @mbggenerated
     */
    private Integer deliveryStationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.free_post_type
     *
     * @mbggenerated
     */
    private Integer freePostType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.free_post_card
     *
     * @mbggenerated
     */
    private String freePostCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column master_order_info_extend.free_post_fee
     *
     * @mbggenerated
     */
    private BigDecimal freePostFee;
    
    /**
     * 订单当日流水号
     */
    private String orderIndex;
    
    /**
     * 纳税人识别号
     */
    private String invTaxer;

    /**
     * 发票税率
     */
    private String taxRate;

    /**
     * 发票开户银行
     */
    private String invBank;

    /**
     * 发票银行账号
     */
    private String invBankNo;

    /**
     * 发票公司名称
     */
    private String invCompanyName;

    /**
     * 发票公司地址
     */
    private String invCompanyAddress;

    /**
     * 可配送时间
     */
    private String riderTime;

    /**
     * 是否删除，0删除，1正常
     */
    private Integer isDel;

    /**
     * 客户合同号
     */
    private String customerContractNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 线下支付用户输入银行卡号
     */
    private String userBankNo;

    /**
     * 用户申请已支付
     */
    private int userPayApply;

    /**
     * 订单类型：1为一般订单，2为联采订单
     */
    private Short orderType;

    /**
     * 最后支付日期
     */
    private Date lastPayDate;

    /**
     * 子公司编码
     */
    private String companyCode;

    /**
     * 子公司名称
     */
    private String companyName;

    /**
     * 账期是否扣款(0未扣、1已扣)
     */
    private int payPeriodStatus;

    /**
     * 发票注册电话
     */
    private String invPhone;

    /**
     * 订单推送供应链状态，0未推送，1已推送
     */
    private Byte pushSupplyChain;

    /**
     * 销售bd
     */
    private String saleBd;

  /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.master_order_sn
     *
     * @return the value of master_order_info_extend.master_order_sn
     *
     * @mbggenerated
     */
    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.master_order_sn
     *
     * @param masterOrderSn the value for master_order_info_extend.master_order_sn
     *
     * @mbggenerated
     */
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.agdist
     *
     * @return the value of master_order_info_extend.agdist
     *
     * @mbggenerated
     */
    public Byte getAgdist() {
        return agdist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.agdist
     *
     * @param agdist the value for master_order_info_extend.agdist
     *
     * @mbggenerated
     */
    public void setAgdist(Byte agdist) {
        this.agdist = agdist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_group
     *
     * @return the value of master_order_info_extend.is_group
     *
     * @mbggenerated
     */
    public Byte getIsGroup() {
        return isGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_group
     *
     * @param isGroup the value for master_order_info_extend.is_group
     *
     * @mbggenerated
     */
    public void setIsGroup(Byte isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_advance
     *
     * @return the value of master_order_info_extend.is_advance
     *
     * @mbggenerated
     */
    public Byte getIsAdvance() {
        return isAdvance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_advance
     *
     * @param isAdvance the value for master_order_info_extend.is_advance
     *
     * @mbggenerated
     */
    public void setIsAdvance(Byte isAdvance) {
        this.isAdvance = isAdvance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_shop
     *
     * @return the value of master_order_info_extend.is_shop
     *
     * @mbggenerated
     */
    public Integer getIsShop() {
        return isShop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_shop
     *
     * @param isShop the value for master_order_info_extend.is_shop
     *
     * @mbggenerated
     */
    public void setIsShop(Integer isShop) {
        this.isShop = isShop;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.shop_id
     *
     * @return the value of master_order_info_extend.shop_id
     *
     * @mbggenerated
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.shop_id
     *
     * @param shopId the value for master_order_info_extend.shop_id
     *
     * @mbggenerated
     */
    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.shop_name
     *
     * @return the value of master_order_info_extend.shop_name
     *
     * @mbggenerated
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.shop_name
     *
     * @param shopName the value for master_order_info_extend.shop_name
     *
     * @mbggenerated
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.shop_time
     *
     * @return the value of master_order_info_extend.shop_time
     *
     * @mbggenerated
     */
    public Date getShopTime() {
        return shopTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.shop_time
     *
     * @param shopTime the value for master_order_info_extend.shop_time
     *
     * @mbggenerated
     */
    public void setShopTime(Date shopTime) {
        this.shopTime = shopTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_cac
     *
     * @return the value of master_order_info_extend.is_cac
     *
     * @mbggenerated
     */
    public Integer getIsCac() {
        return isCac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_cac
     *
     * @param isCac the value for master_order_info_extend.is_cac
     *
     * @mbggenerated
     */
    public void setIsCac(Integer isCac) {
        this.isCac = isCac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.rule_promotion
     *
     * @return the value of master_order_info_extend.rule_promotion
     *
     * @mbggenerated
     */
    public String getRulePromotion() {
        return rulePromotion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.rule_promotion
     *
     * @param rulePromotion the value for master_order_info_extend.rule_promotion
     *
     * @mbggenerated
     */
    public void setRulePromotion(String rulePromotion) {
        this.rulePromotion = rulePromotion == null ? null : rulePromotion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.revive_stt
     *
     * @return the value of master_order_info_extend.revive_stt
     *
     * @mbggenerated
     */
    public Byte getReviveStt() {
        return reviveStt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.revive_stt
     *
     * @param reviveStt the value for master_order_info_extend.revive_stt
     *
     * @mbggenerated
     */
    public void setReviveStt(Byte reviveStt) {
        this.reviveStt = reviveStt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.enc_mobile
     *
     * @return the value of master_order_info_extend.enc_mobile
     *
     * @mbggenerated
     */
    public Integer getEncMobile() {
        return encMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.enc_mobile
     *
     * @param encMobile the value for master_order_info_extend.enc_mobile
     *
     * @mbggenerated
     */
    public void setEncMobile(Integer encMobile) {
        this.encMobile = encMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.shop_code
     *
     * @return the value of master_order_info_extend.shop_code
     *
     * @mbggenerated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.shop_code
     *
     * @param shopCode the value for master_order_info_extend.shop_code
     *
     * @mbggenerated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.assistant_id
     *
     * @return the value of master_order_info_extend.assistant_id
     *
     * @mbggenerated
     */
    public String getAssistantId() {
        return assistantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.assistant_id
     *
     * @param assistantId the value for master_order_info_extend.assistant_id
     *
     * @mbggenerated
     */
    public void setAssistantId(String assistantId) {
        this.assistantId = assistantId == null ? null : assistantId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.order_finished
     *
     * @return the value of master_order_info_extend.order_finished
     *
     * @mbggenerated
     */
    public Integer getOrderFinished() {
        return orderFinished;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.order_finished
     *
     * @param orderFinished the value for master_order_info_extend.order_finished
     *
     * @mbggenerated
     */
    public void setOrderFinished(Integer orderFinished) {
        this.orderFinished = orderFinished;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.settle_queue
     *
     * @return the value of master_order_info_extend.settle_queue
     *
     * @mbggenerated
     */
    public Integer getSettleQueue() {
        return settleQueue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.settle_queue
     *
     * @param settleQueue the value for master_order_info_extend.settle_queue
     *
     * @mbggenerated
     */
    public void setSettleQueue(Integer settleQueue) {
        this.settleQueue = settleQueue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.bill_no
     *
     * @return the value of master_order_info_extend.bill_no
     *
     * @mbggenerated
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.bill_no
     *
     * @param billNo the value for master_order_info_extend.bill_no
     *
     * @mbggenerated
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_review
     *
     * @return the value of master_order_info_extend.is_review
     *
     * @mbggenerated
     */
    public Integer getIsReview() {
        return isReview;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_review
     *
     * @param isReview the value for master_order_info_extend.is_review
     *
     * @mbggenerated
     */
    public void setIsReview(Integer isReview) {
        this.isReview = isReview;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.use_level
     *
     * @return the value of master_order_info_extend.use_level
     *
     * @mbggenerated
     */
    public Integer getUseLevel() {
        return useLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.use_level
     *
     * @param useLevel the value for master_order_info_extend.use_level
     *
     * @mbggenerated
     */
    public void setUseLevel(Integer useLevel) {
        this.useLevel = useLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.channel_user_level
     *
     * @return the value of master_order_info_extend.channel_user_level
     *
     * @mbggenerated
     */
    public Integer getChannelUserLevel() {
        return channelUserLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.channel_user_level
     *
     * @param channelUserLevel the value for master_order_info_extend.channel_user_level
     *
     * @mbggenerated
     */
    public void setChannelUserLevel(Integer channelUserLevel) {
        this.channelUserLevel = channelUserLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.is_order_print
     *
     * @return the value of master_order_info_extend.is_order_print
     *
     * @mbggenerated
     */
    public Integer getIsOrderPrint() {
        return isOrderPrint;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.is_order_print
     *
     * @param isOrderPrint the value for master_order_info_extend.is_order_print
     *
     * @mbggenerated
     */
    public void setIsOrderPrint(Integer isOrderPrint) {
        this.isOrderPrint = isOrderPrint;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.inv_payee
     *
     * @return the value of master_order_info_extend.inv_payee
     *
     * @mbggenerated
     */
    public String getInvPayee() {
        return invPayee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.inv_payee
     *
     * @param invPayee the value for master_order_info_extend.inv_payee
     *
     * @mbggenerated
     */
    public void setInvPayee(String invPayee) {
        this.invPayee = invPayee == null ? null : invPayee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.inv_content
     *
     * @return the value of master_order_info_extend.inv_content
     *
     * @mbggenerated
     */
    public String getInvContent() {
        return invContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.inv_content
     *
     * @param invContent the value for master_order_info_extend.inv_content
     *
     * @mbggenerated
     */
    public void setInvContent(String invContent) {
        this.invContent = invContent == null ? null : invContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.inv_type
     *
     * @return the value of master_order_info_extend.inv_type
     *
     * @mbggenerated
     */
    public String getInvType() {
        return invType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.inv_type
     *
     * @param invType the value for master_order_info_extend.inv_type
     *
     * @mbggenerated
     */
    public void setInvType(String invType) {
        this.invType = invType == null ? null : invType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.tax
     *
     * @return the value of master_order_info_extend.tax
     *
     * @mbggenerated
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.tax
     *
     * @param tax the value for master_order_info_extend.tax
     *
     * @mbggenerated
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.invoice_status
     *
     * @return the value of master_order_info_extend.invoice_status
     *
     * @mbggenerated
     */
    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.invoice_status
     *
     * @param invoiceStatus the value for master_order_info_extend.invoice_status
     *
     * @mbggenerated
     */
    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.delivery_station_id
     *
     * @return the value of master_order_info_extend.delivery_station_id
     *
     * @mbggenerated
     */
    public Integer getDeliveryStationId() {
        return deliveryStationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.delivery_station_id
     *
     * @param deliveryStationId the value for master_order_info_extend.delivery_station_id
     *
     * @mbggenerated
     */
    public void setDeliveryStationId(Integer deliveryStationId) {
        this.deliveryStationId = deliveryStationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.free_post_type
     *
     * @return the value of master_order_info_extend.free_post_type
     *
     * @mbggenerated
     */
    public Integer getFreePostType() {
        return freePostType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.free_post_type
     *
     * @param freePostType the value for master_order_info_extend.free_post_type
     *
     * @mbggenerated
     */
    public void setFreePostType(Integer freePostType) {
        this.freePostType = freePostType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.free_post_card
     *
     * @return the value of master_order_info_extend.free_post_card
     *
     * @mbggenerated
     */
    public String getFreePostCard() {
        return freePostCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.free_post_card
     *
     * @param freePostCard the value for master_order_info_extend.free_post_card
     *
     * @mbggenerated
     */
    public void setFreePostCard(String freePostCard) {
        this.freePostCard = freePostCard == null ? null : freePostCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column master_order_info_extend.free_post_fee
     *
     * @return the value of master_order_info_extend.free_post_fee
     *
     * @mbggenerated
     */
    public BigDecimal getFreePostFee() {
        return freePostFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column master_order_info_extend.free_post_fee
     *
     * @param freePostFee the value for master_order_info_extend.free_post_fee
     *
     * @mbggenerated
     */
    public void setFreePostFee(BigDecimal freePostFee) {
        this.freePostFee = freePostFee;
    }

	public String getOrderIndex() {
		return orderIndex;
	}

	public String getInvTaxer() {
		return invTaxer;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public void setInvTaxer(String invTaxer) {
		this.invTaxer = invTaxer;
	}

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getInvBank() {
        return invBank;
    }

    public void setInvBank(String invBank) {
        this.invBank = invBank;
    }

    public String getRiderTime() {
        return riderTime;
    }

    public void setRiderTime(String riderTime) {
        this.riderTime = riderTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getCustomerContractNum() {
        return customerContractNum;
    }

    public void setCustomerContractNum(String customerContractNum) {
        this.customerContractNum = customerContractNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvBankNo() {
        return invBankNo;
    }

    public void setInvBankNo(String invBankNo) {
        this.invBankNo = invBankNo;
    }

    public String getInvCompanyName() {
        return invCompanyName;
    }

    public void setInvCompanyName(String invCompanyName) {
        this.invCompanyName = invCompanyName;
    }

    public String getInvCompanyAddress() {
        return invCompanyAddress;
    }

    public void setInvCompanyAddress(String invCompanyAddress) {
        this.invCompanyAddress = invCompanyAddress;
    }

    public String getUserBankNo() {
        return userBankNo;
    }

    public void setUserBankNo(String userBankNo) {
        this.userBankNo = userBankNo;
    }

    public int getUserPayApply() {
        return userPayApply;
    }

    public void setUserPayApply(int userPayApply) {
        this.userPayApply = userPayApply;
    }

    public Short getOrderType() {
        return orderType;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

    public Date getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getPayPeriodStatus() {
        return payPeriodStatus;
    }

    public void setPayPeriodStatus(int payPeriodStatus) {
        this.payPeriodStatus = payPeriodStatus;
    }

    public String getInvPhone() {
        return invPhone;
    }

    public void setInvPhone(String invPhone) {
        this.invPhone = invPhone;
    }

    public Byte getPushSupplyChain() {
        return pushSupplyChain;
    }

    public void setPushSupplyChain(Byte pushSupplyChain) {
        this.pushSupplyChain = pushSupplyChain;
    }

    public String getSaleBd() {
        return saleBd;
    }

    public void setSaleBd(String saleBd) {
        this.saleBd = saleBd;
    }
}
