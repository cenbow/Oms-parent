package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单列表
 * @author QuYachu
 */
public class OrderPageInfo implements Serializable{

	/**
	 * 平台订单列表
	 */
	private static final long serialVersionUID = -2536055732175256444L;

    /**
     * 订单编号
     */
	private String orderSn;

    /**
     * 支付方式
     */
	private String paymentMethod;

	private String userId;

    /**
     * 收货人
     */
	private String receiver;

    /**
     * 订单总金额
     */
	private double totalfee;

    /**
     * 订单应付总金额
     */
	private double totalPayable;

    /**
     * 平台币
     */
	private double surplus;

    /**
     * 红包
     */
	private double bonus;

    /**
     * 邮费
     */
	private double shippingTotalFee;

	private double discount;

    /**
     * 订单创建时间
     */
 	private String orderCreateTime;

    /**
     * 订单可以取消时间
     */
 	private String orderCancelTime;

    /**
     * 订单类型
     */
	private int orderType;

    /**
     * 订单类型  1为订单  2为退单  3为换单
     */
	private int bgOrderType;

    /**
     * 是否为闪购订单   0为普通订单  1为闪购订单
     */
	private int sgtype;

	private String referer;

    /**
     * 支付方式id
     */
	private int payId;

    /**
     * 总订单状态 1待付款 2、待发货 3、待收货 4已完成 6已取消 9等待补款 10正在换货 11换货成功
     */
 	private int totalOrderStatus;

    /**
     * 订单状态
     */
 	private int orderStatus;

    /**
     * 付款状态
     */
 	private int payStatus;

    /**
     * 发货状态
     */
 	private int shipStatus;

    /**
     * 交易类型
     */
 	private int transType;

    /**
     * 订单是否被评论 0未评论 1已评论
     */
 	private int isReview;

    /**
     * 订单商品总数量
     */
 	private int goodsCount;

    /**
     * 申请退换商品款数
     */
 	private int returnGoodsType;

    /**
     * 订单商品款数
     */
 	private int goodsType;

    /**
     * 订单商品吊牌价总金额
     */
 	private double goodsAmount;

    /**
     * 已支付金额
     */
 	private double moneyPaid;

    /**
     * 使用的积分金额
     */
 	private double integralMoney;

    /**
     * 使用积分
     */
 	private int integral;

    /**
     * 0:未处理;1:跨境订单;3:线上订单(B2C)
     */
 	private int source;

    /**
     * 0:不免邮;1:单品免邮;2:整单免邮;3:满足规则免邮
     */
 	private int freePostType;

 	private String orderFrom;

    /**
     * 综合税费
     */
	private Double tax;

    /**
     * 订单使用包裹集合
     */
 	private List<OrderShipInfo>  orderShipInfo;

    /**
     * 商品集合
     */
	private List<OrderGoodsInfo> orderGoodsInfo;

    /**
     * 订单列表订单总状态(1、退货中;2、换货中;3、退货完成;4、换货完成)
     */
	private int newTotalOrderStatus;

    /**
     * 店铺编码
     */
	private String channelCode;

    /**
     * 店铺名称
     */
	private String channelName;

    /**
     * 是否为预售订单(0:否 1:是)
     */
	private Integer isAdvance;

    /**
     * 预计发货日
     */
	private String expectedShipDate;
	
	private Integer bvValue;
	
	private String returnSns;
	
	private String exChangeSns;

    /**
     * 1：退货；2：换货'
     */
	private int returnType = 0;

    /**
     * 最后支付时间
     */
	private Date payLastTime;

    /**
     * 客户合同号
     */
	private String customerContractNum;

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
     * 用户申请支付 1未申请、2已申请
     */
    private int userPayApply;

    /**
     * 是否可以延长收货
     */
    private boolean isReceipt;

	/**
	 * 订单商品销售类型：0正常商品 1 非标定制 2 可改价商品
	 */
	private Integer goodsSaleType;

	/**
	 * 价格变更状态：0 无 1 未确认  2 平台确认 3 用户确认
	 */
	private Integer priceChangeStatus;

	/**
	 * 公司类型
	 */
	private Integer companyType;

	/**
	 * 特殊业务类型：外部买家铁信支付类型为1（此类型不允许其前端确认支付）
	 */
	private int specialType;

	/**
	 * 盈合id
	 */
	private String boId;
	/**
	 * 团购id
	 */
	private Integer groupId;

	/**
	 *团购开始时间
	 */
	private Date groupBuyBeginTime;

	/**
	 *团购结束时间
	 */
	private Date groupBuyEndTime;

	/**
	 * 状态（1团购中、2团购成功、3团购失败）
	 */
	private Short groupBuyStatus;

	/**
	 * 客户团购确认支付类型（-1为未确认 0为预付款 1为尾款），此字段在参与团购时有效
	 */
	private Byte isConfirmPay;

	/**
	 * 运营团购确认支付类型（-1为未确认，0为预付款，1为尾款）此字段在参与团购时有效
	 */
	private Byte isOperationConfirmPay;

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

	public Byte getIsConfirmPay() {
		return isConfirmPay;
	}

	public void setIsConfirmPay(Byte isConfirmPay) {
		this.isConfirmPay = isConfirmPay;
	}

	public Byte getIsOperationConfirmPay() {
		return isOperationConfirmPay;
	}

	public void setIsOperationConfirmPay(Byte isOperationConfirmPay) {
		this.isOperationConfirmPay = isOperationConfirmPay;
	}

	public Short getGroupBuyStatus() {
		return groupBuyStatus;
	}

	public void setGroupBuyStatus(Short groupBuyStatus) {
		this.groupBuyStatus = groupBuyStatus;
	}

	public Date getGroupBuyBeginTime() {
		return groupBuyBeginTime;
	}

	public void setGroupBuyBeginTime(Date groupBuyBeginTime) {
		this.groupBuyBeginTime = groupBuyBeginTime;
	}

	public Date getGroupBuyEndTime() {
		return groupBuyEndTime;
	}

	public void setGroupBuyEndTime(Date groupBuyEndTime) {
		this.groupBuyEndTime = groupBuyEndTime;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
	public int getNewTotalOrderStatus() {
		return newTotalOrderStatus;
	}
	public void setNewTotalOrderStatus(int newTotalOrderStatus) {
		this.newTotalOrderStatus = newTotalOrderStatus;
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
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public List<OrderShipInfo> getOrderShipInfo() {
		return orderShipInfo;
	}
	public void setOrderShipInfo(List<OrderShipInfo> orderShipInfo) {
		this.orderShipInfo = orderShipInfo;
	}
	public List<OrderGoodsInfo> getOrderGoodsInfo() {
		return orderGoodsInfo;
	}
	public void setOrderGoodsInfo(List<OrderGoodsInfo> orderGoodsInfo) {
		this.orderGoodsInfo = orderGoodsInfo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	
	public int getTotalOrderStatus() {
		
		/**
		 * 优先判断换单补款的情况
		*/
		//等待付款
		//订单状态：未确认 0
		//支付状态：未支付  部分付款 0 1  
		if (orderType == 0 && orderStatus == 0 && (payStatus == 0 || payStatus == 1)) {
			return 1;
		}
		//等待发货
		//支付状态：已支付 2
		//订单状态：未确认，确认，锁定，解锁状态  0 1 5 6
		//发货状态：未发货，备货中 0 1
		if (orderType == 0 && payStatus == 2 &&
			(orderStatus == 0 || orderStatus == 1 || orderStatus == 5 || orderStatus == 6) &&
			(shipStatus == 0 || shipStatus == 1)) {
			return 2;
		}
		//等待收货
		//发货状态：已发货 3
		if (orderType == 0 && (shipStatus == 3 || shipStatus == 2 || shipStatus == 4)) {
			return 3;
		}
		//确认收货未评价
		//发货状态:已签收 5
		//评价状态：未评价  0
		if (orderType == 0 && shipStatus == 5) {
			return 4;
		}
				
		//订单取消
		//订单状态：已取消  2
		if (orderType == 0 && orderStatus == 2) {
			return 6;//交易关闭
		}
		//等待补款
		//钱款处理方式: 补款 1
		if (orderType == 2 && totalPayable > 0) {
			return 9;//等待补款
		}
		//正在换货
		//订单类型：换单     2
		//支付状态：已付款 2
		if (orderType == 2 && totalPayable<=0 && shipStatus != 5) {
			return 10;//正在换货
		}
		//换货成功
		//订单类型：换单 2
		//发货状态：已收货 5
		if (orderType == 2 && shipStatus == 5) {
			return 11;//换货成功
		}
		return 0;
	}
	public void setTotalOrderStatus(int totalOrderStatus) {
		this.totalOrderStatus = totalOrderStatus;
	}
	public double getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public int getBgOrderType() {
		return bgOrderType;
	}
	public void setBgOrderType(int bgOrderType) {
		this.bgOrderType = bgOrderType;
	}
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
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
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
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
	public int getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
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
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public double getShippingTotalFee() {
		return shippingTotalFee;
	}
	public void setShippingTotalFee(double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getReturnGoodsType() {
		return returnGoodsType;
	}
	public void setReturnGoodsType(int returnGoodsType) {
		this.returnGoodsType = returnGoodsType;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
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
	public Integer getBvValue() {
		return bvValue;
	}
	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
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
	public String getOrderCancelTime() {
		return orderCancelTime;
	}
	public void setOrderCancelTime(String orderCancelTime) {
		this.orderCancelTime = orderCancelTime;
	}

    public Date getPayLastTime() {
        return payLastTime;
    }

    public void setPayLastTime(Date payLastTime) {
        this.payLastTime = payLastTime;
    }

    public String getCustomerContractNum() {
        return customerContractNum;
    }

    public void setCustomerContractNum(String customerContractNum) {
        this.customerContractNum = customerContractNum;
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

    public int getUserPayApply() {
        return userPayApply;
    }

    public void setUserPayApply(int userPayApply) {
        this.userPayApply = userPayApply;
    }

    public boolean isReceipt() {
        return isReceipt;
    }

    public void setReceipt(boolean receipt) {
        isReceipt = receipt;
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
}
