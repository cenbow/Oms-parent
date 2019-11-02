package com.work.shop.oms.api.param.bean;


public class CreateOrderReturn implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String returnSn;

	private String relatingOrderSn;

	private Byte returnType;

	private Byte returnSettlementType;

	private Byte processType;

	private Integer backToCs;

	private String newOrderSn;

	private String returnReason;

	private Double returnTotalFee = 0D;

	private Double totalPriceDifference = 0D;

	private Double returnGoodsMoney = 0D;

	private Double returnBonusMoney = 0D;

	private Double payment = 0D;

	private Double returnIntegralMoney = 0D;

	private Double returnShipping = 0D;

	private Double returnTax = 0D;

	private Double returnPackMoney = 0D;

	private Double returnCard = 0D;

	private Double returnOtherMoney = 0D;
	
	private Double totalIntegralMoney =0D;//积分使用金额

	private String returnDesc;
	
	private String actionUser;
	
	private Integer refundType;
	private Integer haveRefund;

    /**
     * 关联申请单号
     */
    private String relatingChangeSn;

    /**
     * 交货单号
     */
    private String orderSn;

	public Double getTotalIntegralMoney() {
		return totalIntegralMoney;
	}

	public void setTotalIntegralMoney(Double totalIntegralMoney) {
		this.totalIntegralMoney = totalIntegralMoney;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}



	public Integer getHaveRefund() {
		return haveRefund;
	}

	public void setHaveRefund(Integer haveRefund) {
		this.haveRefund = haveRefund;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public Byte getReturnType() {
		return returnType;
	}

	public void setReturnType(Byte returnType) {
		this.returnType = returnType;
	}

	public Byte getReturnSettlementType() {
		return returnSettlementType;
	}

	public void setReturnSettlementType(Byte returnSettlementType) {
		this.returnSettlementType = returnSettlementType;
	}

	public Byte getProcessType() {
		return processType;
	}

	public void setProcessType(Byte processType) {
		this.processType = processType;
	}

	public Integer getBackToCs() {
		return backToCs;
	}

	public void setBackToCs(Integer backToCs) {
		this.backToCs = backToCs;
	}

	public String getNewOrderSn() {
		return newOrderSn;
	}

	public void setNewOrderSn(String newOrderSn) {
		this.newOrderSn = newOrderSn;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public Double getReturnTotalFee() {
		return returnTotalFee;
	}

	public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}

	public Double getTotalPriceDifference() {
		return totalPriceDifference;
	}

	public void setTotalPriceDifference(Double totalPriceDifference) {
		this.totalPriceDifference = totalPriceDifference;
	}

	public Double getReturnGoodsMoney() {
		return returnGoodsMoney;
	}

	public void setReturnGoodsMoney(Double returnGoodsMoney) {
		this.returnGoodsMoney = returnGoodsMoney;
	}

	public Double getReturnBonusMoney() {
		return returnBonusMoney;
	}

	public void setReturnBonusMoney(Double returnBonusMoney) {
		this.returnBonusMoney = returnBonusMoney;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getReturnIntegralMoney() {
		return returnIntegralMoney;
	}

	public void setReturnIntegralMoney(Double returnIntegralMoney) {
		this.returnIntegralMoney = returnIntegralMoney;
	}

	public Double getReturnShipping() {
		return returnShipping;
	}

	public void setReturnShipping(Double returnShipping) {
		this.returnShipping = returnShipping;
	}

	public Double getReturnTax() {
		return returnTax;
	}
	
	public void setReturnTax(Double returnTax) {
		this.returnTax = returnTax;
	}

	public Double getReturnPackMoney() {
		return returnPackMoney;
	}

	public void setReturnPackMoney(Double returnPackMoney) {
		this.returnPackMoney = returnPackMoney;
	}

	public Double getReturnCard() {
		return returnCard;
	}

	public void setReturnCard(Double returnCard) {
		this.returnCard = returnCard;
	}

	public Double getReturnOtherMoney() {
		return returnOtherMoney;
	}

	public void setReturnOtherMoney(Double returnOtherMoney) {
		this.returnOtherMoney = returnOtherMoney;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

    public String getRelatingChangeSn() {
        return relatingChangeSn;
    }

    public void setRelatingChangeSn(String relatingChangeSn) {
        this.relatingChangeSn = relatingChangeSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
