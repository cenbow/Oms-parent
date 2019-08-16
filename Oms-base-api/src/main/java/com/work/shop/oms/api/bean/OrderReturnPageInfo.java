package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

public class OrderReturnPageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1807388673460463840L;
	
	/**
	 * 订单编号
	 */
	private String orderSn;
	
	/**
	 * 退单编号
	 */
	private String	orderReturnSn;
	
	/**
	 * 退单商品总数量
	 */
	private int returnAllgoodsCount;
	
	/**
	 * 退单结算状态
	 */
	private int rpayStatus;
	
	/**
	 * 创建时间
	 */
	private String  orderCreateTime;
	
	/**
	 * 退单商品列表
	 */
	private List<OrderReturnGoodsInfo> orderGoodsInfo;

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
     * 客户合同号
     */
    private String customerContractNum;

    /**
     * 退货状态
     */
    private String shipStatus;
	
	public String getOrderSn() {
		return orderSn;
	}
	
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
	public String getOrderReturnSn() {
		return orderReturnSn;
	}
	
	public void setOrderReturnSn(String orderReturnSn) {
		this.orderReturnSn = orderReturnSn;
	}
	
	public int getReturnAllgoodsCount() {
		return returnAllgoodsCount;
	}
	
	public void setReturnAllgoodsCount(int returnAllgoodsCount) {
		this.returnAllgoodsCount = returnAllgoodsCount;
	}
	
	public int getRpayStatus() {
		return rpayStatus;
	}
	
	public void setRpayStatus(int rpayStatus) {
		this.rpayStatus = rpayStatus;
	}
	
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
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

    public String getCustomerContractNum() {
        return customerContractNum;
    }

    public void setCustomerContractNum(String customerContractNum) {
        this.customerContractNum = customerContractNum;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }
}
