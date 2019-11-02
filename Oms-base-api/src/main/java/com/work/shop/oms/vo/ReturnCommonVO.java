package com.work.shop.oms.vo;

import java.io.Serializable;

public class ReturnCommonVO implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnSn;//退单号
	 private String returnDesc;//退单备注
	 
	 private String relatingOrderSn;//关联订单号
	 private String masterOrderSn;//主订单号
	 private Integer returnType;//退单类型
	 private Integer returnOrderStatus = 0;//退单状态
	 private Integer payStatus = 0;//付款状态
	 private Integer shipsStatus = 0;//发货状态
	 private Integer returnOrderIspass = 0;//质检状态
	 private String returnStatusDisplay;//退单状态显示
	 private Double orderPayedMoney;//订单已付款金额
	 
	 private Integer haveRefund;//是否退款
	 private String channelName;//来源
	 private String channelCode;//来源
	 private String userId;//操作人
	 private String returnReason;//退单原因

    /**
     * 退单原因
     */
    private String returnReasonStr;
	 
	 
	 private Integer chasedOrNot;	 //是否追单
	 private Integer returnSettlementType;//预付款/保证金
	 
	
	 private String returnExpress; //配送方式
	 private String returnInvoiceNo;//退货单快递单号
	 private Integer processType;//处理状态
	 
	 private String newOrderSn;//换货订单
	 private String depotCode;//退货仓库
	 
	 private String addTime;//退单时间
	 private String checkInTime;//入库时间
	 private String clearTime;//结算时间
	 
	 private Integer lockStatus = 0;//锁定状态
	 
	 private Integer isGoodReceived;//是否收货
	 private Integer checkinStatus = 0;//入库状态
	 private Integer qualityStatus;//退单质检状态 
	 private Integer toErp; // 下发XOMS
	 private String siteCode;//站点
	 
	public String getMasterOrderSn() {
        return masterOrderSn;
    }
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }
    public Integer getQualityStatus() {
		return qualityStatus;
	}
	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}
	private Integer backToCs = 0;//回退客服
	 
	 private Integer refundType;//0:未处理；1：删除商品退款；2：订单取消；3：订单发货

    /**
     * 是否退款
     */
    private int backBalance;

    /**
     * 支付方式编码
     */
    private String payCode;

	public Integer getRefundType() {
		return refundType;
	}
	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}
	public String getReturnSn() {
		return returnSn;
	}
	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}
	public String getReturnDesc() {
		return returnDesc;
	}
	
	public Double getOrderPayedMoney() {
		return orderPayedMoney;
	}
	public void setOrderPayedMoney(Double orderPayedMoney) {
		this.orderPayedMoney = orderPayedMoney;
	}
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
	
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public Integer getHaveRefund() {
		return haveRefund;
	}
	public void setHaveRefund(Integer haveRefund) {
		this.haveRefund = haveRefund;
	}
	public Integer getLockStatus() {
		return lockStatus;
	}
	
	public Integer getCheckinStatus() {
		return checkinStatus;
	}
	
	public Integer getBackToCs() {
		return backToCs;
	}
	public void setBackToCs(Integer backToCs) {
		this.backToCs = backToCs;
	}
	public void setCheckinStatus(Integer checkinStatus) {
		this.checkinStatus = checkinStatus;
	}
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}
	
	public String getReturnStatusDisplay() {
		return returnStatusDisplay;
	}
	public void setReturnStatusDisplay(String returnStatusDisplay) {
		this.returnStatusDisplay = returnStatusDisplay;
	}
	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}
	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}
	public Integer getReturnType() {
		return returnType;
	}
	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}
	public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}
	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Integer getShipsStatus() {
		return shipsStatus;
	}
	
	public String getReturnExpress() {
		return returnExpress;
	}
	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}
	public void setShipsStatus(Integer shipsStatus) {
		this.shipsStatus = shipsStatus;
	}
	public Integer getReturnOrderIspass() {
		return returnOrderIspass;
	}
	public void setReturnOrderIspass(Integer returnOrderIspass) {
		this.returnOrderIspass = returnOrderIspass;
	}
	public Integer getIsGoodReceived() {
		return isGoodReceived;
	}
	public void setIsGoodReceived(Integer isGoodReceived) {
		this.isGoodReceived = isGoodReceived;
	}
	public Integer getChasedOrNot() {
		return chasedOrNot;
	}
	public void setChasedOrNot(Integer chasedOrNot) {
		this.chasedOrNot = chasedOrNot;
	}
	public Integer getReturnSettlementType() {
		return returnSettlementType;
	}
	public void setReturnSettlementType(Integer returnSettlementType) {
		this.returnSettlementType = returnSettlementType;
	}
	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}
	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}
	public Integer getProcessType() {
		return processType;
	}
	public void setProcessType(Integer processType) {
		this.processType = processType;
	}
	public String getNewOrderSn() {
		return newOrderSn;
	}
	public void setNewOrderSn(String newOrderSn) {
		this.newOrderSn = newOrderSn;
	}
	public String getAddTime() {
		return addTime;
	}
	
 
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getClearTime() {
		return clearTime;
	}
	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Integer getToErp() {
		return toErp;
	}
	public void setToErp(Integer toErp) {
		this.toErp = toErp;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

    public String getReturnReasonStr() {
        return returnReasonStr;
    }

    public void setReturnReasonStr(String returnReasonStr) {
        this.returnReasonStr = returnReasonStr;
    }

    public int getBackBalance() {
        return backBalance;
    }

    public void setBackBalance(int backBalance) {
        this.backBalance = backBalance;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
}
