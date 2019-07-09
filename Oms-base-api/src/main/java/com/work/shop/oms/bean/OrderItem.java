package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 订单列表
 */
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 1721995237520233191L;

	/**
	 * 订单号
	 */
	private String masterOrderSn;

	/**
	 * 外部订单号
	 */
	private String outerOrderSn;

	/**
	 * 下单人
	 */
	private String userId;

	/**
	 * 收货人姓名
	 */
	private String receiverName;

	/**
	 * 收货人手机
	 */
	private String receiverMobile;

	/**
	 * 收货人电话
	 */
	private String receiverTel;

	/**
	 * 收货人省
	 */
	private String province;

	/**
	 * 收货人市
	 */
	private String city;

	/**
	 * 收货人地址
	 */
	private String address;
	
	/**
	 * 用户店铺名称
	 */
	private String userShopName;

	/**
	 * 平台编码
	 */
	private String channelCode;

	/**
	 * 平台名称
	 */
	private String channelName;

	/**
	 * 店铺编码
	 */
	private String shopCode;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 订单来源
	 */
	private String referer;

    /**
     * 订单类型
     */
	private String orderType;

    /**
     * 订单状态
     */
	private Integer orderStatus;

    /**
     * 支付状态
     */
	private Integer payStatus;

    /**
     * 发货状态
     */
	private Integer shipStatus;

    /**
     * 问题单状态
     */
	private Integer questionStatus;

	/**
	 * 下单时间
	 */
	private String addTime;

	/**
	 * 商品数量
	 */
	private Integer goodsCount;

	/**
	 * 订单总金额
	 */
	private Double totalFee;

	/**
	 * 商品总金额
	 */
	private Double goodsAmount;

	/**
	 * 实收金额
	 */
	private Double moneyPaid;

	/**
	 * 优惠折扣
	 */
	private Double discount;

	/**
	 * 运费
	 */
	private Double shippingTotalFee;

	/**
	 * 订单渠道店铺
	 */
	private String orderFrom;

	/**
	 * 是否自提 0：否;1：是
	 */
	private Integer isCac;

	/**
	 * 自提状态 0 未提、1已提
	 */
	private String gotStatus;

	/**
	 * 自提码
	 */
	private String gotCode;

	/**
	 * 退单编码
	 */
	private String returnSn;

	/**
	 * 退商品数量
	 */
	private Integer returnGoodsCount;

	/**
	 * 退款总金额
	 */
	private Double returnTotalFee;

	/**
	 * 线下店铺编码
	 */
	private String storeCode;

	/**
	 * 线下店铺名称
	 */
	private String storeName;

	/**
	 * 订单配送时间
	 */
	private String deliveryTime;

	/**
	 * 订单配送完成时间
	 */
	private String deliveryConfirmTime;

	/**
	 * 时间类型 0：下单时间;1：确认时间
	 */
	private Integer timeType;

	/**
	 * 订单显示类型 0:有效订单;1:全部订单;2:隐藏订单
	 */
	private Integer orderView;

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 查询类型 0:订单列表;1:待结算列表
	 */
	private Integer queryType = 0;

	/**
	 * 操作用户
	 */
	private String actionUser;

	/**
	 * BD编码
	 */
	private String insteadUserId;

	/**
	 * 仓库编码
	 */
	private String depotCode;

    /**
     * 用户是否已打款 1未设置、2已打款
     */
	private int userPayApply;

    /**
     * 线下支付用户银行卡号
     */
	private String userBankNo;

    /**
     * 用户是否已打款 文字
     */
    private String userPayApplyStr;

    /**
     * 创建订单类型，1正常订单，2联采订单
     */
    private Integer createOrderType;
	
	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getOuterOrderSn() {
		return outerOrderSn;
	}

	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
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

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Integer shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Integer getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Double getMoneyPaid() {
		return moneyPaid;
	}

	public void setMoneyPaid(Double moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Double getShippingTotalFee() {
		return shippingTotalFee;
	}

	public void setShippingTotalFee(Double shippingTotalFee) {
		this.shippingTotalFee = shippingTotalFee;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public Integer getReturnGoodsCount() {
		return returnGoodsCount;
	}

	public Double getReturnTotalFee() {
		return returnTotalFee;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public void setReturnGoodsCount(Integer returnGoodsCount) {
		this.returnGoodsCount = returnGoodsCount;
	}

	public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}

	public String getGotStatus() {
		return gotStatus;
	}

	public void setGotStatus(String gotStatus) {
		this.gotStatus = gotStatus;
	}

	public String getGotCode() {
		return gotCode;
	}

	public void setGotCode(String gotCode) {
		this.gotCode = gotCode;
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

	public Integer getIsCac() {
		return isCac;
	}

	public void setIsCac(Integer isCac) {
		this.isCac = isCac;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Integer getOrderView() {
		return orderView;
	}

	public void setOrderView(Integer orderView) {
		this.orderView = orderView;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getInsteadUserId() {
		return insteadUserId;
	}

	public void setInsteadUserId(String insteadUserId) {
		this.insteadUserId = insteadUserId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDeliveryConfirmTime() {
		return deliveryConfirmTime;
	}

	public void setDeliveryConfirmTime(String deliveryConfirmTime) {
		this.deliveryConfirmTime = deliveryConfirmTime;
	}

	public String getUserShopName() {
		return userShopName;
	}

	public void setUserShopName(String userShopName) {
		this.userShopName = userShopName;
	}

    public int getUserPayApply() {
        return userPayApply;
    }

    public void setUserPayApply(int userPayApply) {
        this.userPayApply = userPayApply;
    }

    public String getUserBankNo() {
        return userBankNo;
    }

    public void setUserBankNo(String userBankNo) {
        this.userBankNo = userBankNo;
    }

    public String getUserPayApplyStr() {
        String str = "";
        if (userPayApply == 1) {
            str = "未支付";
        } else if (userPayApply == 2) {
            str = "已支付";
        }
        return str;
    }

    public void setUserPayApplyStr(String userPayApplyStr) {
        this.userPayApplyStr = userPayApplyStr;
    }

    public Integer getCreateOrderType() {
        return createOrderType;
    }

    public void setCreateOrderType(Integer createOrderType) {
        this.createOrderType = createOrderType;
    }
}
