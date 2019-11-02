package com.work.shop.oms.order.request;

import java.io.Serializable;

public class OrderQueryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115261779726715323L;
	
	private Integer pageNo; // 页码
	
	private Integer pageSize; // 每页记录数
	
	private String masterOrderSn; // 订单号
	
	private String outerOrderSn; // 外部订单号
	
	private String userId; // 下单人
	
	private String receiverName; // 收货人姓名
	
	private String receiverMobile; // 收货人手机
	
	private String receiverTel; // 收货人电话

	private String channelCode; // 平台编码
	
	private String shopCode; // 店铺编码
	
	private String storeCode; // 线下店铺编码
	
	private String referer; // 订单来源
	
	private Integer orderType; // 订单类型,0正常订单，1联采订单

	private Integer orderStatus; // 订单状态

	private Integer payStatus; // 支付状态

	private Integer shipStatus; // 发货状态

	private Integer questionStatus; // 问题单状态
	
	private Integer splitStatus; // 拆掉状态
	
	private Integer timeType; // 时间类型 0：下单时间;1：确认时间
	
	private Integer orderView; // 订单显示类型 0:有效订单;1:全部订单;2:隐藏订单
	
	private String startTime; // 开始时间
	
	private String endTime; // 结束时间
	
	private String deliveryStratTime; // 配送开始时间
	
	private String deliveryEndTime; // 配送结束时间
	
	private String gotStatus; // 自提状态 0未提、1已提
	
	private Integer queryType = 0; // 查询类型 0:订单列表;1:待结算列表
	
	private String actionUser; // 操作用户
	
	private String insteadUserId; // BD编码
	
	private String tmsCode; // 配送编码
	
	private String depotCode; // 仓库编码
	
	private int exportType; // 导出类型 0 订单列表、1订单商品列表

    private String message;

    /**
     * 支付方式id
     */
    private String payId;

    public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

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

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
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

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
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

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Integer getSplitStatus() {
		return splitStatus;
	}

	public void setSplitStatus(Integer splitStatus) {
		this.splitStatus = splitStatus;
	}

	public Integer getOrderView() {
		return orderView;
	}

	public void setOrderView(Integer orderView) {
		this.orderView = orderView;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getGotStatus() {
		return gotStatus;
	}

	public void setGotStatus(String gotStatus) {
		this.gotStatus = gotStatus;
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

	public String getTmsCode() {
		return tmsCode;
	}

	public void setTmsCode(String tmsCode) {
		this.tmsCode = tmsCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDeliveryStratTime() {
		return deliveryStratTime;
	}

	public String getDeliveryEndTime() {
		return deliveryEndTime;
	}

	public void setDeliveryStratTime(String deliveryStratTime) {
		this.deliveryStratTime = deliveryStratTime;
	}

	public void setDeliveryEndTime(String deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}

	public int getExportType() {
		return exportType;
	}

	public void setExportType(int exportType) {
		this.exportType = exportType;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }
}
