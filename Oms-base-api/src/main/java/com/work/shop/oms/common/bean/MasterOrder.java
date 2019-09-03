package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主订单信息
 * @author QuYachu
 */
public class MasterOrder implements Serializable{

	private static final long serialVersionUID = 4828608531067407135L;

	private String actionUser;

    /**
     * 下单人ID
     */
	private String userId;

    /**
     * 代下单人ID
     */
	private String insteadUserId;

    /**
     * 下单时会员等级(新加)
     */
    private Integer useLevel;

    /**
     * 下单时的外渠会员等级
     */
	private Integer channelUserLevel;

    /**
     * 收货人名字
     */
    private String consignee;

    /**
     * 交易类型 1:款到发货 2:货到付款 3:担保交易
     */
	private Integer transType;

    /**
     * 订单状态 0:未确认 1:已确认 2:已取消 3:完成
     */
	private Integer orderStatus;

    /**
     * 支付状态 0:未付款 1:部分付款 2:已付款 3:已结算
     */
	private Short payStatus;

    /**
     * 是否要发票  0否、1要
     */
	private Byte isOrderPrint;

    /**
     * 订单来源（渠道）
     */
	private String orderFrom;

	private String mergeFrom;

	private String splitFrom;

    /**
     * 外部交易号
     */
	private String orderOutSn;

    /**
     * 订单当日流水号
     */
	private String orderIndex;

    /**
     * 受益人ID，如果开启提成功能
     */
	private Integer beneficiaryId;

    /**
     * 订单由某广告带来的广告id
     */
	private Integer fromAd;

    /**
     * 订单设备的来源
     */
	private String referer;

    /**
     * 门店ID
     */
	private String shopId;

    /**
     * 门店名称
     */
	private String shopName;

    /**
     * 门店接单时间
     */
	private Date shopTime;

    /**
     * 缺货处理方式
     */
	private String howOss;

    /**
     * 推荐分成的用户id
     */
	private Integer parentId;

    /**
     * 代销类型(0无;1是代销;2是CPS)
     */
	private Integer outletType;

    /**
     * CPS来源
     */
	private String sourceCode;

    /**
     * 发票类型
     */
	private String invType;

    /**
     * 发票抬头
     */
	private String invPayee;

    /**
     * 发票内容
     */
	private String invContent;

    /**
     * 纳税人识别号
     */
	private String invTaxer;

    /**
     * 综合税费
     */
	private Double tax;

    /**
     * 开户行
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
     * 配送总费用
     */
	private Double shippingTotalFee;

    /**
     * 保价总费用
     */
	private Double insureTotalFee;

    /**
     * 支付总费用
     */
	private Double payTotalFee;

	private Double packTotalFee;

    /**
     * 订单附言
     */
	private String postscript;

	private Double cardTotalFee;

    /**
     * 应付款总金额
     */
	private Double totalPayable;

    /**
     * 商品总金额
     */
	private Double goodsAmount;

    /**
     * 订单总金额
     */
	private Double totalFee;

    /**
     * 已付款金额
     */
	private Double moneyPaid;

    /**
     * 该订单使用余额的数量
     */
	private Double surplus;

    /**
     * 使用红包金额
     */
	private Double bonus;

    /**
     * 使用红包ID
     */
	private String bonusId;

    /**
     * 订单商品总数
     */
	private Integer goodsCount;

    /**
     * 折扣金额
     */
	private Double discount;

    /**
     * 下单时间
     */
	private Date addTime;

    /**
     * 商家给客户的留言
     */
	private String toBuyer;

    /**
     * 订单类型 0，正常订单 1，补发订单  2，换货订单
     */
	private Byte orderType;

    /**
     * 发票注册电话
     */
	private String invPhone;

	private List<Integer> promotionSummry = new ArrayList<Integer>();

    /**
     * 发货信息(数组单个元素结构参照：ship_list定义)
     */
	private List<MasterShip> shipList = new ArrayList<MasterShip>();

    /**
     * 付款信息(数组单个元素结构参照：pay_list定义)
     */
	private List<MasterPay> payList = new ArrayList<MasterPay>();

    /**
     * 收货人电话
     */
	private String tel;

    /**
     * 手机号
     */
	private String mobile;

    /**
     * 收货人的电子邮件
     */
	private String email;

    /**
     * 促销名称信息
     */
	private String prName;

    /**
     * 问题原因code
     */
	private String questionCode;

    /**
     * 订单种类
     */
	private Integer orderCategory;

    /**
     * 单据组织
     */
	private String invoicesOrganization;

    /**
     * 订单是否为团购
     */
	private short isGroup;

    /**
     * 是否为预售商品
     */
	private short isAdvance;
	
	private byte isRestricted;

	private String packageCode;
	
	private String smsFlag;
	
	private String smsCode;

	private String rulePromotion;

    /**
     * 门店所属的加盟商编号（2014-12版本新增）
     */
	private String agentId;

    /**
     * 是否立即下发ERP（0否，1是）旧版
     */
	private int posGroup;

    /**
     * 是否立即下发ERP（0否，1是）
     */
	private int isNow;

    /**
     * 使用积分金额
     */
	private Double integralMoney;

    /**
     * 使用点数（一点=一元）
     */
	private Double points;

    /**
     * 使用积分
     */
	private Integer integral;

    /**
     * 订单来源类型 (3:线上订单(B2C)、6:线上订单(B2B))
     */
	private Integer source;

    /**
     * 订单财务价
     */
	private Double orderSettlementPrice;

    /**
     * 支付单财务价
     */
	private Double paySettlementPrice;

    /**
     * 商品财务价
     */
	private Double goodsSettlementPrice;

    /**
     * 使用打折券
     */
	private String useCards;
	
	private String goodsQuestionCode;

    /**
     * 免邮券卡号
     */
	private String freePostCard;

    /**
     * 免邮前邮费金额
     */
	private Double freePostFee;

    /**
     * 免邮类型 0:不免邮;1:单品免邮;2:整单免邮;3:满足规则免邮;
     */
	private Integer freePostType;

    /**
     * 站点编码
     */
	private String siteCode;

    /**
     * 注册手机号码
     */
	private String registerMobile;

    /**
     * bvValue
     */
	private Integer bvValue;

    /**
     * 预计发货日
     */
	private String expectedShipDate;

    /**
     * 基础BV值
     */
	private Integer baseBvValue;

    /**
     * 是否自提 0:否 1:是
     */
	private Integer isCac;

    /**
     * 线下店铺编码
     */
	private String shopCode;

    /**
     * 渠道店铺名称
     */
	private String channelShopName;

    /**
     * 是否需要审核 0不需要、1需要
     */
	private Integer needAudit;

	/**
	 * 是否需要合同签章 0不需要、1需要
	 */
	private Integer needSign;

    /**
     * 客户合同号
     */
	private String customerContractNum;

    /**
     * 配送时间
     */
	private String riderTime;

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
     * 创建订单类型，0为一般订单，1为联采订单
     */
	private Integer createOrderType;

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
     * 销售bd
     */
    private String saleBd;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Integer getOrderStatus() {
		return orderStatus == null ? 0 : orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getFromAd() {
		return fromAd == null ? 0 : fromAd;
	}

	public void setFromAd(Integer fromAd) {
		this.fromAd = fromAd;
	}

	public String getHowOss() {
		return howOss == null ? "" : howOss;
	}

	public void setHowOss(String howOss) {
		this.howOss = howOss;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public String getReferer() {
		return referer == null ? "" : referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getOrderOutSn() {
		return orderOutSn == null ? "" : orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public Integer getGoodsCount() {
		return goodsCount == null ? 0 : goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getPostscript() {
		return postscript == null ? "" : postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public String getToBuyer() {
		return toBuyer == null ? "" : toBuyer;
	}

	public void setToBuyer(String toBuyer) {
		this.toBuyer = toBuyer;
	}

	public Double getGoodsAmount() {
		return goodsAmount == null ? 0.00 : goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Double getMoneyPaid() {
		return moneyPaid == null ? 0.00 : moneyPaid;
	}

	public void setMoneyPaid(Double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public Double getSurplus() {
		return surplus == null ? 0.00 : surplus;
	}

	public void setSurplus(Double surplus) {
		this.surplus = surplus;
	}

	public Double getBonus() {
		return bonus == null ? 0.00 : bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public String getBonusId() {
		return bonusId;
	}

	public void setBonusId(String bonusId) {
		this.bonusId = bonusId;
	}

	public Double getDiscount() {
		return discount == null ? 0.00 : discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getInvType() {
		return invType == null ? "" : invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getInvPayee() {
		return invPayee == null ? "" : invPayee;
	}

	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}

	public String getInvContent() {
		return invContent == null ? "" : invContent;
	}

	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}

	public Double getTax() {
		return tax == null ? 0.00 : tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getShippingTotalFee() {
		return shippingTotalFee == null ? 0.00 : shippingTotalFee;
	}

	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}

	public Double getInsureTotalFee() {
		return insureTotalFee == null ? 0.00 : insureTotalFee;
	}

	public void setInsureTotalFee(Double insureTotalFee) {
		this.insureTotalFee = insureTotalFee;
	}

	public List<MasterShip> getShipList() {
		return shipList;
	}

	public void setShipList(List<MasterShip> shipList) {
		this.shipList = shipList;
	}

	public List<MasterPay> getPayList() {
		return payList;
	}

	public void setPayList(List<MasterPay> payList) {
		this.payList = payList;
	}

	public String getMergeFrom() {
		return mergeFrom == null ? "" : mergeFrom;
	}

	public void setMergeFrom(String mergeFrom) {
		this.mergeFrom = mergeFrom;
	}

	public String getSplitFrom() {
		return splitFrom == null ? "" : splitFrom;
	}

	public void setSplitFrom(String splitFrom) {
		this.splitFrom = splitFrom;
	}

	public Short getPayStatus() {
		return payStatus == null ? 0 : payStatus;
	}

	public void setPayStatus(Short payStatus) {
		this.payStatus = payStatus;
	}

	public Byte getIsOrderPrint() {
		return isOrderPrint == null ? 0 : isOrderPrint;
	}

	public void setIsOrderPrint(Byte isOrderPrint) {
		this.isOrderPrint = isOrderPrint;
	}

	public Integer getParentId() {
		return parentId == null ? 0 : parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getOutletType() {
		return outletType == null ? 0 : outletType;
	}

	public void setOutletType(Integer outletType) {
		this.outletType = outletType;
	}

	public String getSourceCode() {
		return sourceCode == null ? "" : sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Double getCardTotalFee() {
		return cardTotalFee == null ? 0.00 : cardTotalFee;
	}

	public void setCardTotalFee(Double cardTotalFee) {
		this.cardTotalFee = cardTotalFee;
	}

	public Double getTotalFee() {
		return totalFee == null ? 0.00 : totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getPackTotalFee() {
		return packTotalFee == null ? 0.00 : packTotalFee;
	}

	public void setPackTotalFee(Double packTotalFee) {
		this.packTotalFee = packTotalFee;
	}

	public Double getTotalPayable() {
		return totalPayable == null ? 0.00 : totalPayable;
	}

	public void setTotalPayable(Double totalPayable) {
		this.totalPayable = totalPayable;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getConsignee() {
		return consignee == null ? "" : consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Integer getBeneficiaryId() {
		return beneficiaryId == null ? 0 : beneficiaryId;
	}

	public void setBeneficiaryId(Integer beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public Double getPayTotalFee() {
		return payTotalFee == null ? 0.00 : payTotalFee;
	}

	public void setPayTotalFee(Double payTotalFee) {
		this.payTotalFee = payTotalFee;
	}

	public Byte getOrderType() {
		return orderType == null ? 0 : orderType;
	}

	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}

	public String getActionUser() {
		return actionUser == null ? "" : actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public List<Integer> getPromotionSummry() {
		return promotionSummry;
	}

	public void setPromotionSummry(List<Integer> promotionSummry) {
		this.promotionSummry = promotionSummry;
	}

	public String getTel() {
//		tel = shipList.get(0).getTel();
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
//		mobile = shipList.get(0).getMobile();
		return mobile;
		//return mobile == null ? "" : mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
//		email = shipList.get(0).getEmail();
//		return email;
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrName() {
		if (prName != null) {
			if (prName.length() > 255) {
				prName = prName.substring(0, 254);
			}
		}
		return prName == null ? "" : prName;
	}

	public void setPrName(String prName) {
		this.prName = prName;
	}

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public Integer getOrderCategory() {
		orderCategory = orderCategory == null ? 1 : orderCategory;
		return orderCategory;
	}

	public void setOrderCategory(Integer orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getInvoicesOrganization() {
		invoicesOrganization = invoicesOrganization == null ? "" : invoicesOrganization;
		return invoicesOrganization;
	}

	public void setInvoicesOrganization(String invoicesOrganization) {
		this.invoicesOrganization = invoicesOrganization;
	}

	public short getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(short isGroup) {
		this.isGroup = isGroup;
	}

	public short getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(short isAdvance) {
		this.isAdvance = isAdvance;
	}
	
	public byte getIsRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(byte isRestricted) {
		this.isRestricted = isRestricted;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getSmsFlag() {
		return smsFlag==null?"":smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getSmsCode() {
		return smsCode==null?"":smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Date getShopTime() {
		return shopTime;
	}

	public void setShopTime(Date shopTime) {
		this.shopTime = shopTime;
	}

	public String getRulePromotion() {
		return rulePromotion;
	}

	public void setRulePromotion(String rulePromotion) {
		this.rulePromotion = rulePromotion;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public int getIsNow() {
		return isNow;
	}

	public void setIsNow(int isNow) {
		this.isNow = isNow;
	}

	public int getPosGroup() {
		return posGroup;
	}

	public void setPosGroup(int posGroup) {
		this.posGroup = posGroup;
	}

	public Double getIntegralMoney() {
		integralMoney = integralMoney == null ? 0.00 : integralMoney;
		return integralMoney;
	}

	public void setIntegralMoney(Double integralMoney) {
		this.integralMoney = integralMoney;
	}

	public Integer getIntegral() {
		integral = integral == null ? 0 : integral;
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getSource() {
		source = source == null ? 3 : source;
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Double getOrderSettlementPrice() {
		return orderSettlementPrice;
	}

	public void setOrderSettlementPrice(Double orderSettlementPrice) {
		this.orderSettlementPrice = orderSettlementPrice;
	}

	public Double getPaySettlementPrice() {
		return paySettlementPrice;
	}

	public void setPaySettlementPrice(Double paySettlementPrice) {
		this.paySettlementPrice = paySettlementPrice;
	}

	public Double getGoodsSettlementPrice() {
		return goodsSettlementPrice;
	}

	public void setGoodsSettlementPrice(Double goodsSettlementPrice) {
		this.goodsSettlementPrice = goodsSettlementPrice;
	}

	public String getUseCards() {
		return useCards;
	}

	public void setUseCards(String useCards) {
		this.useCards = useCards;
	}

	public String getGoodsQuestionCode() {
		return goodsQuestionCode;
	}

	public void setGoodsQuestionCode(String goodsQuestionCode) {
		this.goodsQuestionCode = goodsQuestionCode;
	}

	public String getFreePostCard() {
		return freePostCard;
	}

	public void setFreePostCard(String freePostCard) {
		this.freePostCard = freePostCard;
	}

	public Double getFreePostFee() {
		return freePostFee == null ? 0 : freePostFee;
	}

	public void setFreePostFee(Double freePostFee) {
		this.freePostFee = freePostFee;
	}

	public Integer getFreePostType() {
		return freePostType == null ? 0 : freePostType;
	}

	public void setFreePostType(Integer freePostType) {
		this.freePostType = freePostType;
	}

	public String getSiteCode() {
		return (siteCode == null || "".equals(siteCode)) ? "YIKE" : siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getRegisterMobile() {
		return registerMobile;
	}

	public void setRegisterMobile(String registerMobile) {
		this.registerMobile = registerMobile;
	}

	public Double getPoints() {
		return points == null ? 0D : points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Integer getBvValue() {
		return bvValue == null ? 0 : bvValue;
	}

	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
	}

	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}

	public Integer getIsCac() {
		return isCac == null ? 0 : isCac;
	}

	public void setIsCac(Integer isCac) {
		this.isCac = isCac;
	}

	public String getInsteadUserId() {
		return insteadUserId;
	}

	public void setInsteadUserId(String insteadUserId) {
		this.insteadUserId = insteadUserId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getChannelShopName() {
		return channelShopName;
	}

	public void setChannelShopName(String channelShopName) {
		this.channelShopName = channelShopName;
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

	public Integer getNeedAudit() {
		return needAudit == null ? 0 : needAudit;
	}

	public void setNeedAudit(Integer needAudit) {
		this.needAudit = needAudit;
	}

    public String getCustomerContractNum() {
        return customerContractNum;
    }

    public void setCustomerContractNum(String customerContractNum) {
        this.customerContractNum = customerContractNum;
    }

    public String getRiderTime() {
        return riderTime;
    }

    public void setRiderTime(String riderTime) {
        this.riderTime = riderTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getInvPhone() {
        return invPhone;
    }

    public void setInvPhone(String invPhone) {
        this.invPhone = invPhone;
    }

	public Integer getNeedSign() {
		return needSign;
	}

	public void setNeedSign(Integer needSign) {
		this.needSign = needSign;
	}

    public String getSaleBd() {
        return saleBd;
    }

    public void setSaleBd(String saleBd) {
        this.saleBd = saleBd;
    }
}
