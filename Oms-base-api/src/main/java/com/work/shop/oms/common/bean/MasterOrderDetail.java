package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * @author QuYachu
 */
public class MasterOrderDetail implements Serializable {

	private static final long serialVersionUID = -923324309692289952L;
	// 主单信息表字段
	private String masterOrderSn;// 主订单编号，唯一键
	private Byte transType;// 交易类型
	private Integer orderType;// 订单类型
	private String relatingOriginalSn;// 换货单关联的原订单号
	private String relatingReturnSn;// 换货单关联退单编号
	private String relatingRemoneySn;// 换货单关联退款单号
	private String userId;// 用户ID
	private String userName;// 用户名
	private Byte orderStatus;// 订单状态
	private Byte payStatus;// 支付总状态
	private Byte shipStatus;// 发货总状态
	private Integer lockStatus;// 订单锁定状态
	private Integer questionStatus;// 问题单状态
	private Integer noticeStatus;// 通知收款状态
	private Byte splitStatus;// 拆单状态
	private Date splitTime;// 拆单时间
	private String orderFrom;// 订单来源
	private Date addTime;// 订单生成时间
	private Date confirmTime;// 订单确认时间
	private Date finishTime;// 订单完成时间
	private String clearTime;// 订单结算时间
	private Date questionTime;// 设为问题单时间
	private Date lockTime;// 订单锁定时间
	private Date updateTime;// 订单最后更新时间
	private Date noticeTime;// 通知收款时间
	private String orderOutSn;// 外部交易号
	private Integer beneficiaryId;// 受益人ID
	private Short fromAd;// 订单由某广告带来的广告id
	private String prIds;// 参加了哪些促销编号
	private String prName;// 参加促销的名字
	private String referer;// 订单的来源媒体
	private String howOos;// 缺货处理方式
	private Integer parentId;// 能获得推荐分成的用户id
	private Integer outletType;// 代销类型
	private String sourceCode;// CPS来源
	private BigDecimal shippingTotalFee;// 配送总费用
	private BigDecimal insureTotalFee;// 保价总费用
	private BigDecimal payTotalFee;// 支付总费用
	private BigDecimal totalPayable;// 应付款总金额
	private BigDecimal goodsAmount;// 商品总金额
	private BigDecimal totalFee;// 订单总金额
	private BigDecimal moneyPaid;// 已付款金额
	private BigDecimal surplus;// 订单使用余额
	private BigDecimal bonus;// 使用红包金额
	private String bonusId;// 使用红包ID
	private Integer goodsCount;// 订单商品总数
	private BigDecimal discount;// 订单商品折扣
	private BigDecimal integralMoney;// 使用积分金额
	private Integer integral;// 使用积分数量
	private String postscript;// 订单附言
	private String toBuyer;// 商家给客户的留言
	private String cancelCode;// 取消原因编码
	private String cancelReason;// 取消原因描述
	private Integer orderCategory;// 订单种类
	private String invoicesOrganization;// 单据组织
	private Integer isnow;// 是否立即下发ERP
	private Integer source;// 1:跨境订单;3:线上订单(B2C)

    /**
     * 需要合同签章 0不需要、1需要
     */
    private Byte needSign;

    /**
     * 签章状态 0未签章、1已签章
     */
    private Byte signStatus;

    /**
     * 签章合同号
     */
    private String signContractNum;


	/**
	 * 订单商品销售类型：0正常商品 1 非标定制 2 可改价商品
	 */
	private Integer goodsSaleType;

	/**
	 * 价格变更状态：0 无 1 未确认  2 平台确认 3 用户确认
	 */
	private Integer priceChangeStatus;


	// 主单扩展表字段
	private Byte agdist;// 订单二次分配总数
	private Byte isGroup;// 是否为团购订单
	private Byte isAdvance;// 是否为预售商品
	private Integer isShop;// 是否可被门店获取
	private String shopId;// 门店ID
	private String shopName;// 门店名称
	private Date shopTime;// 门店接单时间
	private Integer isCac;// 是否自提
	private String rulePromotion;// 订单生成时当时促销镜像编码
	private Byte reviveStt;// 订单复活状态
	private Integer encMobile;// 手机加密
	private String shopCode;// 线下门店ID
	private String assistantId;// 店员工号
	private Integer orderFinished;// 订单完成标志
	private Integer settleQueue;// 订单队列推送标志
	private String billNo;// ERP结算小票号
	private Integer isReview;// 订单是否评论
	private Integer useLevel;// 下单时会员等级
	private Integer channelUserLevel;// 下单时的淘宝拍拍会员等级
	private Integer isOrderPrint;// 是否要发票
	private String invPayee;// 发票抬头
	private String invContent;// 发票内容
	private String invType;// 发票类型
	private BigDecimal tax;// 发票税额
	private Integer invoiceStatus;// 发票状态
	private Integer deliveryStationId;// 发票ID
    private String invPhone;
    private Byte pushSupplyChain; //订单推送供应链，0未推送，1已推送

    /**
     * 是否需要审核 0不需要、1需要
     */
    private Integer needAudit;

    /**
     * 开户银行
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
     * 客户合同号
     */
    private String customerContractNum;

    /**
     * 用户备注
     */
    private String remark;


	// 主单地址信息表字段
	private String consignee;// 收货人的姓名
	private String country;// 收货人的国家
	private String countryName;// 收货人的国家
	private String province;// 收货人的省份
	private String provinceName;// 收货人的省份
	private String city;// 收货人的城市
	private String cityName;// 收货人的城市
	private String district;// 收货人的地区
	private String districtName;// 收货人的地区
	private String street;// 收货人的街道
	private String streetName;// 收货人的街道
	private String address;// 收货人的详细地址
	private String zipcode;// 收货人的邮政编码
	private String tel;// 收货人的电话号码
	private String mobile;// 收货人的手机号码
	private String email;// 收货人的电子邮件
	private String bestTime;// 收货人的最佳送货时间
	private String signBuilding;// 收货人的地址的标志性建筑
	private Byte shippingId;// 用户选择的配送方式id
	private String shippingName;// 用户选择的配送方式的名称
	private Byte shippingDays;// 订单承诺发货天数
	private Integer chargeType;// 支付类型：0. 不限 1.POS机支付; 2.现金支付;
	private String cacCode;// 自提点编码
	private Date creatTime;// 生成时间

    /**
     * 发票收货人姓名
     */
    private String invConsignee;

    /**
     * 发票收货人手机号码
     */
    private String invMobile;

    /**
     * 发票邮寄地址
     */
    private String invAddress;

	/* private Date updateTime; 与主单表字段重复 注释掉这个 */
	private Short regionId;// 发货仓地区ID
	private String wayPaymentFreight;// 是否自提
	// 自定义字段
	private String channelName;// 渠道名称
	private String fullAddress;// 详细地址
	private String totalPriceDiscount;// 总折扣金额
	private String logqDesc;// 主单问题单类型描述
	private String bonusName;// 红包名称
	private String siteCode; // 站点编码
	private String depotCode; // 发货仓编码
	private Integer bvValue;// 订单bv值
	private Integer points;// 点数
	private String expectedShipDate;// 预计发货日
	private String userCardNo;// 用户身份证号码
	private String userCardName;// 用户身份证姓名
	private double userRealMoney;// 预付卡余额
	private String warehName;// 仓库名称
	private String channelCode; // 渠道编码
	private String storeCode; // 线下门店编码
	private String storeName; // 线下门店名称

	private String orderIndex; // 订单当日流水号

	private String invTaxer; // 纳税人标识

	/**
	 * 用户支付银行账号
	 */
	private String userBankNo;

	/**
	 * 用户是否已支付设置 1未设置、2已支付申请
	 */
	private int userPayApply;

    /**
     * 创建订单类型，0正常订单，1联采订单
     */
	private Integer createOrderType;

    /**
     * 公司id
     */
	private String companyId;

    /**
     * bd编码
     */
	private String insteadUserId;

    /**
     * bd编码
     */
	private String saleBd;

    /**
     * 交货单信息
     */
    private List<OrderItemDepotDetail> depotDetails;

    public String getSaleBd() {
        return saleBd;
    }

    public void setSaleBd(String saleBd) {
        this.saleBd = saleBd;
    }

    public String getInsteadUserId() {
        return insteadUserId;
    }

    public void setInsteadUserId(String insteadUserId) {
        this.insteadUserId = insteadUserId;
    }

    public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee == null ? null : consignee.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district == null ? null : district.trim();
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street == null ? null : street.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode == null ? null : zipcode.trim();
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel == null ? null : tel.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getBestTime() {
		return bestTime;
	}

	public void setBestTime(String bestTime) {
		this.bestTime = bestTime == null ? null : bestTime.trim();
	}

	public String getSignBuilding() {
		return signBuilding;
	}

	public void setSignBuilding(String signBuilding) {
		this.signBuilding = signBuilding == null ? null : signBuilding.trim();
	}

	public Byte getShippingId() {
		return shippingId;
	}

	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName == null ? null : shippingName.trim();
	}

	public Byte getShippingDays() {
		return shippingDays;
	}

	public void setShippingDays(Byte shippingDays) {
		this.shippingDays = shippingDays;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public String getCacCode() {
		return cacCode;
	}

	public void setCacCode(String cacCode) {
		this.cacCode = cacCode == null ? null : cacCode.trim();
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Short getRegionId() {
		return regionId;
	}

	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}

	public String getWayPaymentFreight() {
		return wayPaymentFreight;
	}

	public void setWayPaymentFreight(String wayPaymentFreight) {
		this.wayPaymentFreight = wayPaymentFreight == null ? null : wayPaymentFreight.trim();
	}

	public Byte getAgdist() {
		return agdist;
	}

	public void setAgdist(Byte agdist) {
		this.agdist = agdist;
	}

	public Byte getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Byte isGroup) {
		this.isGroup = isGroup;
	}

	public Byte getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(Byte isAdvance) {
		this.isAdvance = isAdvance;
	}

	public Integer getIsShop() {
		return isShop;
	}

	public void setIsShop(Integer isShop) {
		this.isShop = isShop;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId == null ? null : shopId.trim();
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName == null ? null : shopName.trim();
	}

	public Date getShopTime() {
		return shopTime;
	}

	public void setShopTime(Date shopTime) {
		this.shopTime = shopTime;
	}

	public Integer getIsCac() {
		return isCac;
	}

	public void setIsCac(Integer isCac) {
		this.isCac = isCac;
	}

	public String getRulePromotion() {
		return rulePromotion;
	}

	public void setRulePromotion(String rulePromotion) {
		this.rulePromotion = rulePromotion == null ? null : rulePromotion.trim();
	}

	public Byte getReviveStt() {
		return reviveStt;
	}

	public void setReviveStt(Byte reviveStt) {
		this.reviveStt = reviveStt;
	}

	public Integer getEncMobile() {
		return encMobile;
	}

	public void setEncMobile(Integer encMobile) {
		this.encMobile = encMobile;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode == null ? null : shopCode.trim();
	}

	public String getAssistantId() {
		return assistantId;
	}

	public void setAssistantId(String assistantId) {
		this.assistantId = assistantId == null ? null : assistantId.trim();
	}

	public Integer getOrderFinished() {
		return orderFinished;
	}

	public void setOrderFinished(Integer orderFinished) {
		this.orderFinished = orderFinished;
	}

	public Integer getSettleQueue() {
		return settleQueue;
	}

	public void setSettleQueue(Integer settleQueue) {
		this.settleQueue = settleQueue;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public Integer getIsReview() {
		return isReview;
	}

	public void setIsReview(Integer isReview) {
		this.isReview = isReview;
	}

	public Integer getUseLevel() {
		return useLevel;
	}

	public void setUseLevel(Integer useLevel) {
		this.useLevel = useLevel;
	}

	public Integer getChannelUserLevel() {
		return channelUserLevel;
	}

	public void setChannelUserLevel(Integer channelUserLevel) {
		this.channelUserLevel = channelUserLevel;
	}

	public Integer getIsOrderPrint() {
		return isOrderPrint;
	}

	public void setIsOrderPrint(Integer isOrderPrint) {
		this.isOrderPrint = isOrderPrint;
	}

	public String getInvPayee() {
		return invPayee;
	}

	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee == null ? null : invPayee.trim();
	}

	public String getInvContent() {
		return invContent;
	}

	public void setInvContent(String invContent) {
		this.invContent = invContent == null ? null : invContent.trim();
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType == null ? null : invType.trim();
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Integer getDeliveryStationId() {
		return deliveryStationId;
	}

	public void setDeliveryStationId(Integer deliveryStationId) {
		this.deliveryStationId = deliveryStationId;
	}

	public Byte getTransType() {
		return transType;
	}

	public void setTransType(Byte transType) {
		this.transType = transType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getRelatingOriginalSn() {
		return relatingOriginalSn;
	}

	public void setRelatingOriginalSn(String relatingOriginalSn) {
		this.relatingOriginalSn = relatingOriginalSn == null ? null : relatingOriginalSn.trim();
	}

	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}

	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn == null ? null : relatingReturnSn.trim();
	}

	public String getRelatingRemoneySn() {
		return relatingRemoneySn;
	}

	public void setRelatingRemoneySn(String relatingRemoneySn) {
		this.relatingRemoneySn = relatingRemoneySn == null ? null : relatingRemoneySn.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Byte getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Byte payStatus) {
		this.payStatus = payStatus;
	}

	public Byte getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Byte shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}

	public Integer getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(Integer noticeStatus) {
		this.noticeStatus = noticeStatus;
	}

	public Byte getSplitStatus() {
		return splitStatus;
	}

	public void setSplitStatus(Byte splitStatus) {
		this.splitStatus = splitStatus;
	}

	public Date getSplitTime() {
		return splitTime;
	}

	public void setSplitTime(Date splitTime) {
		this.splitTime = splitTime;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom == null ? null : orderFrom.trim();
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime == null ? "" : clearTime;
	}

	public Date getQuestionTime() {
		return questionTime;
	}

	public void setQuestionTime(Date questionTime) {
		this.questionTime = questionTime;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn == null ? null : orderOutSn.trim();
	}

	public Integer getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(Integer beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public Short getFromAd() {
		return fromAd;
	}

	public void setFromAd(Short fromAd) {
		this.fromAd = fromAd;
	}

	public String getPrIds() {
		return prIds;
	}

	public void setPrIds(String prIds) {
		this.prIds = prIds == null ? null : prIds.trim();
	}

	public String getPrName() {
		return prName;
	}

	public void setPrName(String prName) {
		this.prName = prName == null ? null : prName.trim();
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer == null ? null : referer.trim();
	}

	public String getHowOos() {
		return howOos;
	}

	public void setHowOos(String howOos) {
		this.howOos = howOos == null ? null : howOos.trim();
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getOutletType() {
		return outletType;
	}

	public void setOutletType(Integer outletType) {
		this.outletType = outletType;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode == null ? null : sourceCode.trim();
	}

	public BigDecimal getShippingTotalFee() {
		return shippingTotalFee;
	}

	public void setShippingTotalFee(BigDecimal shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}

	public BigDecimal getInsureTotalFee() {
		return insureTotalFee;
	}

	public void setInsureTotalFee(BigDecimal insureTotalFee) {
		this.insureTotalFee = insureTotalFee;
	}

	public BigDecimal getPayTotalFee() {
		return payTotalFee;
	}

	public void setPayTotalFee(BigDecimal payTotalFee) {
		this.payTotalFee = payTotalFee;
	}

	public BigDecimal getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(BigDecimal totalPayable) {
		this.totalPayable = totalPayable;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getMoneyPaid() {
		return moneyPaid;
	}

	public void setMoneyPaid(BigDecimal moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public BigDecimal getSurplus() {
		return surplus;
	}

	public void setSurplus(BigDecimal surplus) {
		this.surplus = surplus;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public String getBonusId() {
		return bonusId;
	}

	public void setBonusId(String bonusId) {
		this.bonusId = bonusId == null ? null : bonusId.trim();
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(BigDecimal integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript == null ? null : postscript.trim();
	}

	public String getToBuyer() {
		return toBuyer;
	}

	public void setToBuyer(String toBuyer) {
		this.toBuyer = toBuyer == null ? null : toBuyer.trim();
	}

	public String getCancelCode() {
		return cancelCode;
	}

	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode == null ? null : cancelCode.trim();
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason == null ? null : cancelReason.trim();
	}

	public Integer getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(Integer orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getInvoicesOrganization() {
		return invoicesOrganization;
	}

	public void setInvoicesOrganization(String invoicesOrganization) {
		this.invoicesOrganization = invoicesOrganization == null ? null : invoicesOrganization.trim();
	}

	public Integer getIsnow() {
		return isnow;
	}

	public void setIsnow(Integer isnow) {
		this.isnow = isnow;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTotalPriceDiscount() {
		return totalPriceDiscount;
	}

	public void setTotalPriceDiscount(String totalPriceDiscount) {
		this.totalPriceDiscount = totalPriceDiscount;
	}

	public String getLogqDesc() {
		return logqDesc;
	}

	public void setLogqDesc(String logqDesc) {
		this.logqDesc = logqDesc;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getBonusName() {
		return bonusName;
	}

	public void setBonusName(String bonusName) {
		this.bonusName = bonusName == null ? "" : bonusName.trim();
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Integer getBvValue() {
		return bvValue;
	}

	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getUserCardName() {
		return userCardName;
	}

	public void setUserCardName(String userCardName) {
		this.userCardName = userCardName;
	}

	public double getUserRealMoney() {
		return userRealMoney;
	}

	public void setUserRealMoney(double userRealMoney) {
		this.userRealMoney = userRealMoney;
	}

	public String getWarehName() {
		return warehName;
	}

	public void setWarehName(String warehName) {
		this.warehName = warehName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

    public Integer getNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(Integer needAudit) {
        this.needAudit = needAudit;
    }

    public String getInvBank() {
        return invBank;
    }

    public void setInvBank(String invBank) {
        this.invBank = invBank;
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

    public String getRiderTime() {
        return riderTime;
    }

    public void setRiderTime(String riderTime) {
        this.riderTime = riderTime;
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

    public String getInvConsignee() {
        return invConsignee;
    }

    public void setInvConsignee(String invConsignee) {
        this.invConsignee = invConsignee;
    }

    public String getInvMobile() {
        return invMobile;
    }

    public void setInvMobile(String invMobile) {
        this.invMobile = invMobile;
    }

    public String getInvAddress() {
        return invAddress;
    }

    public void setInvAddress(String invAddress) {
        this.invAddress = invAddress;
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

    public Integer getCreateOrderType() {
        return createOrderType;
    }

    public void setCreateOrderType(Integer createOrderType) {
        this.createOrderType = createOrderType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<OrderItemDepotDetail> getDepotDetails() {
        return depotDetails;
    }

    public void setDepotDetails(List<OrderItemDepotDetail> depotDetails) {
        this.depotDetails = depotDetails;
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

    public Byte getNeedSign() {
        return needSign;
    }

    public void setNeedSign(Byte needSign) {
        this.needSign = needSign;
    }

    public Byte getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Byte signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignContractNum() {
        return signContractNum;
    }

    public void setSignContractNum(String signContractNum) {
        this.signContractNum = signContractNum;
    }

	public Integer getGoodsSaleType() {
		return goodsSaleType;
	}

	public void setGoodsSaleType(Integer goodsSaleType) {
		this.goodsSaleType = goodsSaleType;
	}

	public Integer getPriceChangeStatus() {
		return priceChangeStatus;
	}

	public void setPriceChangeStatus(Integer priceChangeStatus) {
		this.priceChangeStatus = priceChangeStatus;
	}
}
