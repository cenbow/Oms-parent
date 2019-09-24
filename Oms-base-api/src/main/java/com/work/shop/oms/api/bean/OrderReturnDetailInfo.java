package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderReturnDetailInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4569200674440021268L;
	private String	orderReturnSn;//退单编号
	private double	bonus; //红包金额
	private double	returnPostage; //退邮费
	private double  returnGoodsMoney;//退单商品金额
	private String	applyTime; //生成退单时间
	private String	receiveTime;// 入库时间
	private String	qualityTime;// 质检时间
	private String	clearingTime;// 结算时间
	private String  returnExpress;//退单快递公司编码
	private String  returnInvoiceNo;//退单快递单号
	private String returnExpressImg;//退单物流图片
	private String userId;
	private int payStatus;
	private int  progressStatus;//流程状态          1 => '申请退货',   2 => '美邦收货', 3 => '商品质检',  4 => '退货完成',

    /**
     * 收货人名称
     */
    private String consignee;

    /**
     * 联系人
     */
    private String contactUser;

    /**
     * 联系人电话
     */
    private String contactMobile;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退单原因
     */
    private String returnReason;

    /**
     * 退款状态，0未退，1已退
     */
    private Integer backBalance;

    private String backBalanceStr;

    /**
     * 退单状态，0未确定、1已确认、4无效、10已完成
     */
    private Integer returnOrderStatus;

    private String returnOrderStatusStr;

    /**
     * 操作人
     */
    private String actionUser;

    private String orderSn;

	private List<OrderReturnGoodsInfo> orderGoodsInfo;//商品集合

    /**
     * 退单日志
     */
    private List<OrderReturnAction> actions;

    /**
     *  客户合同号
     */
    private String customerContractNum;

    /**
     * 退货状态
     */
    private String shipStatus;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

	public String getOrderReturnSn() {
		return orderReturnSn;
	}
	public void setOrderReturnSn(String orderReturnSn) {
		this.orderReturnSn = orderReturnSn;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public double getReturnPostage() {
		return returnPostage;
	}
	public void setReturnPostage(double returnPostage) {
		this.returnPostage = returnPostage;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getQualityTime() {
		return qualityTime;
	}
	public void setQualityTime(String qualityTime) {
		this.qualityTime = qualityTime;
	}
	public String getClearingTime() {
		return clearingTime;
	}
	public void setClearingTime(String clearingTime) {
		this.clearingTime = clearingTime;
	}
	public double getReturnGoodsMoney() {
		return returnGoodsMoney;
	}
	public void setReturnGoodsMoney(double returnGoodsMoney) {
		this.returnGoodsMoney = returnGoodsMoney;
	}
	public String getReturnExpress() {
		return returnExpress;
	}
	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}
	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}
	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}
	public String getReturnExpressImg() {
		return returnExpressImg;
	}
	public void setReturnExpressImg(String returnExpressImg) {
		this.returnExpressImg = returnExpressImg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getProgressStatus() {
		return progressStatus;
	}
	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}
	public List<OrderReturnGoodsInfo> getOrderGoodsInfo() {
		return orderGoodsInfo;
	}
	public void setOrderGoodsInfo(List<OrderReturnGoodsInfo> orderGoodsInfo) {
		this.orderGoodsInfo = orderGoodsInfo;
	}

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public Integer getBackBalance() {
        return backBalance;
    }

    public void setBackBalance(Integer backBalance) {
        this.backBalance = backBalance;
    }

    public String getBackBalanceStr() {
	    if (backBalance == 0) {
	        return "未退";
        } else if (backBalance == 1) {
	        return "已退";
        }
        return "";
    }

    public void setBackBalanceStr(String backBalanceStr) {
        this.backBalanceStr = backBalanceStr;
    }

    public Integer getReturnOrderStatus() {
        return returnOrderStatus;
    }

    public void setReturnOrderStatus(Integer returnOrderStatus) {
        this.returnOrderStatus = returnOrderStatus;
    }

    public String getReturnOrderStatusStr() {
        if (returnOrderStatus == 0) {
            return "待处理";
        } else if (returnOrderStatus == 1) {
            return "已确认";
        } else if (returnOrderStatus == 4) {
            return "无效";
        } else if (returnOrderStatus == 10) {
            return "已完成";
        }
        return "";
    }

    public void setReturnOrderStatusStr(String returnOrderStatusStr) {
        this.returnOrderStatusStr = returnOrderStatusStr;
    }

    public String getActionUser() {
        return actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<OrderReturnAction> getActions() {
        return actions;
    }

    public void setActions(List<OrderReturnAction> actions) {
        this.actions = actions;
    }

    public String getCustomerContractNum() {
        return customerContractNum;
    }

    public void setCustomerContractNum(String customerContractNum) {
        this.customerContractNum = customerContractNum;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
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
}
