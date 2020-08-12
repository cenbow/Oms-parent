package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * @author QuYachu
 */
public class OrderDetailInfo  implements Serializable{

	private static final long serialVersionUID = -686887090939943154L;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 支付方式
	 */
	private String paymentMethod;

	/**
	 * 收货人
	 */
	private String receiver;

	/**
	 * 订单总金额
	 */
	private double totalfee;

	/**
	 * 订单支付金额
	 */
	private double payTotalfee;

	/**
	 * 商品总金额
	 */
	private double goodsAmount;

	/**
	 * 订单折扣
	 */
	private double discount;
	
	private double totalPayable;

	/**
	 * 流程订单状态  1：等待付款  2：付款成功  3：商品出库  4：确认收货
	 */
 	private String progressStatus;

	/**
	 * 订单创建时间
	 */
	private String orderCreateTime;

	/**
	 * 订单支付时间
	 */
	private String orderPayTime;

	/**
	 * 订单可以取消时间
	 */
	private String orderCancelTime;

	/**
	 * 收货地址
	 */
	private String shippingAddress;

	/**
	 * 邮费
	 */
	private double postage;

	/**
	 * 平台币
	 */
	private double surplus;

	/**
	 * 是否为闪购订单   0为普通订单  1为闪购订单
	 */
	private int sgtype;
	
	private String userId;

	/**
	 * 红包
	 */
	private double bonus;

	/**
	 * 用户手机号码
	 */
	private String mobile;

	/**
	 * 加密后的手机号码
	 */
	private String encryptMobile;

	/**
	 * 发票抬头
	 */
	private String invPayee;

	/**
	 * 发票内容
	 */
	private String invContent;

	/**
	 * 最佳配送状态
	 */
	private String bestTime;

	/**
	 * 发票类型
	 */
	private String invType;

	/**
	 * 订单状态 1待付款 2、待发货 3、待收货 4已完成 6已取消
	 */
	private int  totalOrderStatus;

	/**
	 * 发货状态
	 */
 	private int  shipStatus;

	/**
	 * 订单状态
	 */
	private int  orderStatus;

	/**
	 * 支付状态
	 */
	private int  payStatus;

	/**
	 * 交易类型
	 */
	private int transType;

	/**
	 * 是否被评论
	 */
	private int isReview;

	/**
	 * 已付款总金额
	 */
	private double moneyPaid;
 	
 	private int payId;

	/**
	 * 支付编码
	 */
	private String payCode;

	/**
	 * 是否需要发票
	 */
	private int isOrderPrint;

	/**
	 * 使用的积分金额
	 */
 	private double integralMoney;

	/**
	 * 使用积分
	 */
	private int  integral;
 	
	private String referer;

	/**
	 * 0:未处理;1:跨境订单;3:线上订单(B2C)
	 */
 	private int source;
 	
 	private List<OrderShipInfo>  orderShipInfo;

	/**
	 * 店铺编码
	 */
	private String channelCode;

	/**
	 * 店铺名称
	 */
	private String channelName;

	/**
	 * 订单类型 0，正常订单 1，补发订单  2，换货订单
	 */
	private int orderType;
 	
 	private String country;
 	
 	private String province;
 	
 	private String city;
 	
 	private String district;
 	
 	private String street;
 	
 	private String address;
 	
 	/**
 	 * 用户店铺名称
 	 */
 	private String userShopName;

	/**
	 * 免邮类型 0:不免邮;1:单品免邮;2:整单免邮;3:满足规则免邮
	 */
	private int freePostType;
 	
 	private String freePostCard;
 	
 	private double freePostFee;
 	
	private Integer bvValue;

	/**
	 * 点数
	 */
	private Double points;

	/**
	 * 是否为预售订单(0:否 1:是)
	 */
	private Integer isAdvance;

	/**
	 * 预计发货日
	 */
	private String expectedShipDate;

	/**
	 * 综合税费
	 */
	private Double tax;

	/**
	 * 用户身份证号码
	 */
	private String userCardNo;

	/**
	 * 用户身份证姓名
	 */
	private String userCardName;

	/**
	 * 店长省份编码
	 */
	private String provinceCode;

	/**
	 * 店长市编码
	 */
	private String cityCode;

	/**
	 * 店长区县编码
	 */
	private String districtCode;

	/**
	 * 店长地区编码
	 */
	private String areaCode;
	
	private String returnSns;
	
	private String exChangeSns;

	/**
	 * 1：退货；2：换货
	 */
	private int returnType = 0;

	/**
	 * 商品总数量
	 */
	private int goodsNumber;
	
	/**
	 * 是否自提 0 否 1 是自提
	 */
	private int isCac;
	
	/**
	 * 自提码
	 */
	private String gotCode;
	
	/**
	 * 自提状态 0:未提;1:已提;
	 */
	private int gotStatus;

	/**
	 * 线下店铺编码
	 */
	private String storeCode;

	/**
	 * 线下店铺名称
	 */
	private String storeName;

	/**
	 * 经度
	 */
	private Double longitude;

	/**
	 * 纬度
	 */
	private Double latitude;

	/**
	 * 配送时间范围
	 */
	private String deliveryTime;

    /**
     * 是否可以申请退款
     */
    private boolean isRefund;

    /**
     * 可配送时间，纯文字
     */
    private String riderTime;

    /**
     * 是否需要审核 0不需要、1需要
     */
    private int needAudit;

    /**
     * 发票收货人
     */
    private String invConsignee;

    /**
     * 发票收货人手机号
     */
    private String invMobile;

    /**
     * 发票收货地址
     */
    private String invAddress;

    /**
     * 配送方式
     */
    private String shippingName;

	/**
	 * 用户支付银行账号
	 */
	private String userBankNo;

	/**
	 * 用户申请支付 1未申请、2已申请
	 */
	private int userPayApply;

    /**
     * 支付期数
     */
	private Integer paymentPeriod;

    /**
     * 支付费率
     */
	private BigDecimal paymentRate;

    /**
     * 账期支付最后支付日期
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
	 * 子公司全称
	 */
	private String companyFullName;

    /**
     * 最后确认收货时间
     */
    private String lastDeliveryConfirmTime;

    /**
     * 订单取消原因
     */
    private String orderCancel;

    /**
     * 审核状态 0：未审单、1：已审单
     */
    private Byte auditStatus;

    /**
     * 签章状态： 0未签章、1已签章
     */
    private Byte signStatus;

    /**
     * 需要合同签章 0不需要、1需要
     */
    private Byte needSign;

	/**
	 * 订单商品销售类型：0正常商品 1 非标定制 2 可改价商品
	 */
	private Byte goodsSaleType;

	/**
	 * 价格变更状态：0 无 1 未确认  2 平台确认 3 用户确认
	 */
	private Byte priceChangeStatus;

    /**
     * 订单备注
     */
    private String postscript;

    /**
     * 合同号
     */
    private String signContractNum;

	/**
	 * 用户备注
	 */
	private String remark;

	/**
	 * 公司类型
	 */
	private Integer companyType;

	/**
	 * 特殊业务类型：外部买家铁信支付类型为1（此类型不允许其前端确认支付）
	 */
	private int specialType;

	/**
	 * 盈合ID
	 */
	private String boId;
	/**
	 * 团购ID
	 */
	private Integer groupId;
	/**
	 * 运营团购确认支付类型（-1为未确认，0为预付款，1为尾款）此字段在参与团购时有效
	 */
	private Integer isOperationConfirmPay;
	/**
	 * 客户团购确认支付类型（-1为未确认 0为预付款 1为尾款），此字段在参与团购时有效
	 */
	private Integer isConfirmPay;

	/**
	 * 尾款金额
	 */
	private BigDecimal balanceAmount;
	/**
	 * 预付款金额
	 */
	private BigDecimal prepayments;

	/**
	 *   团购状态（1团购中、2团购成功、3团购失败）
	 */
	private Integer groupBuyStatus;

	/**
	 * 当前时间
	 */
	private Long currentTime;
	/**
	 * 团购订单原价
	 */
	private BigDecimal groupBuyMoney;

	/**
	 *订单团购商品1失效,0正常
	 */
	private Integer isGroupDel;

	public Integer getIsGroupDel() {
		return isGroupDel;
	}

	public void setIsGroupDel(Integer isGroupDel) {
		this.isGroupDel = isGroupDel;
	}

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}



	public BigDecimal getGroupBuyMoney() {
		return groupBuyMoney;
	}

	public void setGroupBuyMoney(BigDecimal groupBuyMoney) {
		this.groupBuyMoney = groupBuyMoney;
	}

	public Integer getGroupBuyStatus() {
		return groupBuyStatus;
	}

	public void setGroupBuyStatus(Integer groupBuyStatus) {
		this.groupBuyStatus = groupBuyStatus;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getPrepayments() {
		return prepayments;
	}

	public void setPrepayments(BigDecimal prepayments) {
		this.prepayments = prepayments;
	}

	public Integer getIsOperationConfirmPay() {
		return isOperationConfirmPay;
	}

	public void setIsOperationConfirmPay(Integer isOperationConfirmPay) {
		this.isOperationConfirmPay = isOperationConfirmPay;
	}

	public Integer getIsConfirmPay() {
		return isConfirmPay;
	}

	public void setIsConfirmPay(Integer isConfirmPay) {
		this.isConfirmPay = isConfirmPay;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public int getOrderType() {
        return orderType;
    }
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public double getPostage() {
		return postage;
	}
	public void setPostage(double postage) {
		this.postage = postage;
	}
	public double getSurplus() {
		return surplus;
	}
	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public List<OrderShipInfo> getOrderShipInfo() {
		return orderShipInfo;
	}
	public void setOrderShipInfo(List<OrderShipInfo> orderShipInfo) {
		this.orderShipInfo = orderShipInfo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEncryptMobile() {
		return encryptMobile;
	}
	public void setEncryptMobile(String encryptMobile) {
		this.encryptMobile = encryptMobile;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getOrderPayTime() {
		return orderPayTime;
	}
	public void setOrderPayTime(String orderPayTime) {
		this.orderPayTime = orderPayTime;
	}
	public String getProgressStatus() {
		return progressStatus;
	}
	public void setProgressStatus(String progressStatus) {
		this.progressStatus = progressStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getPayTotalfee() {
		return payTotalfee;
	}
	public void setPayTotalfee(double payTotalfee) {
		this.payTotalfee = payTotalfee;
	}
	public double getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(double totalfee) {
		this.totalfee = totalfee;
	}
	public double getTotalPayable() {
		return totalPayable;
	}
	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getInvPayee() {
		return invPayee;
	}
	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}
	public String getInvContent() {
		return invContent;
	}
	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}
	public String getBestTime() {
		return bestTime;
	}
	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}
	public String getInvType() {
		return invType;
	}
	public void setInvType(String invType) {
		this.invType = invType;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getTotalOrderStatus() {
		return totalOrderStatus;
	}
	public void setTotalOrderStatus(int totalOrderStatus) {
		this.totalOrderStatus = totalOrderStatus;
	}
	public int getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getTransType() {
		return transType;
	}
	public void setTransType(int transType) {
		this.transType = transType;
	}
	public int getIsReview() {
		return isReview;
	}
	public void setIsReview(int isReview) {
		this.isReview = isReview;
	}
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
	}
	public int getIsOrderPrint() {
		return isOrderPrint;
	}
	public void setIsOrderPrint(int isOrderPrint) {
		this.isOrderPrint = isOrderPrint;
	}
	public double getMoneyPaid() {
		return moneyPaid;
	}
	public void setMoneyPaid(double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}
	public double getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(double integralMoney) {
		this.integralMoney = integralMoney;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getSgtype() {
		if("闪购".equals(referer)){
			return 1;
		}
		return sgtype;
	}
	public void setSgtype(int sgtype) {
		this.sgtype = sgtype;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public int getFreePostType() {
		return freePostType;
	}
	public void setFreePostType(int freePostType) {
		this.freePostType = freePostType;
	}
	public String getFreePostCard() {
		return freePostCard;
	}
	public void setFreePostCard(String freePostCard) {
		this.freePostCard = freePostCard;
	}
	public double getFreePostFee() {
		return freePostFee;
	}
	public void setFreePostFee(double freePostFee) {
		this.freePostFee = freePostFee;
	}
	public Integer getBvValue() {
		return bvValue;
	}
	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}
	public Double getPoints() {
		return points;
	}
	public void setPoints(Double points) {
		this.points = points;
	}
	public Integer getIsAdvance() {
		return isAdvance;
	}
	public void setIsAdvance(Integer isAdvance) {
		this.isAdvance = isAdvance;
	}
	public String getExpectedShipDate() {
		return expectedShipDate;
	}
	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
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
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getReturnSns() {
		return returnSns;
	}
	public void setReturnSns(String returnSns) {
		this.returnSns = returnSns;
	}
	public String getExChangeSns() {
		return exChangeSns;
	}
	public void setExChangeSns(String exChangeSns) {
		this.exChangeSns = exChangeSns;
	}
	public int getReturnType() {
		return returnType;
	}
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	public int getIsCac() {
		return isCac;
	}
	public String getGotCode() {
		return gotCode;
	}
	public int getGotStatus() {
		return gotStatus;
	}
	public void setIsCac(int isCac) {
		this.isCac = isCac;
	}
	public void setGotCode(String gotCode) {
		this.gotCode = gotCode;
	}
	public void setGotStatus(int gotStatus) {
		this.gotStatus = gotStatus;
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
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public int getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getOrderCancelTime() {
		return orderCancelTime;
	}
	public void setOrderCancelTime(String orderCancelTime) {
		this.orderCancelTime = orderCancelTime;
	}
	public String getUserShopName() {
		return userShopName;
	}
	public void setUserShopName(String userShopName) {
		this.userShopName = userShopName;
	}

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }

    public String getRiderTime() {
        return riderTime;
    }

    public void setRiderTime(String riderTime) {
        this.riderTime = riderTime;
    }

    public int getNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(int needAudit) {
        this.needAudit = needAudit;
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

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
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

    public Integer getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(Integer paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public BigDecimal getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(BigDecimal paymentRate) {
        this.paymentRate = paymentRate;
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

    public String getLastDeliveryConfirmTime() {
        return lastDeliveryConfirmTime;
    }

    public void setLastDeliveryConfirmTime(String lastDeliveryConfirmTime) {
        this.lastDeliveryConfirmTime = lastDeliveryConfirmTime;
    }

    public String getOrderCancel() {
        return orderCancel;
    }

    public void setOrderCancel(String orderCancel) {
        this.orderCancel = orderCancel;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Byte getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Byte signStatus) {
        this.signStatus = signStatus;
    }

    public Byte getNeedSign() {
        return needSign;
    }

    public void setNeedSign(Byte needSign) {
        this.needSign = needSign;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getSignContractNum() {
        return signContractNum;
    }

    public void setSignContractNum(String signContractNum) {
        this.signContractNum = signContractNum;
    }

	public Byte getGoodsSaleType() {
		return goodsSaleType;
	}

	public void setGoodsSaleType(Byte goodsSaleType) {
		this.goodsSaleType = goodsSaleType;
	}

	public Byte getPriceChangeStatus() {
		return priceChangeStatus;
	}

	public void setPriceChangeStatus(Byte priceChangeStatus) {
		this.priceChangeStatus = priceChangeStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCompanyType() {
		return companyType;
	}

	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public String getBoId() {
		return boId;
	}

	public void setBoId(String boId) {
		this.boId = boId;
	}

	public String getCompanyFullName() {
		return companyFullName;
	}

	public void setCompanyFullName(String companyFullName) {
		this.companyFullName = companyFullName;
	}
}
