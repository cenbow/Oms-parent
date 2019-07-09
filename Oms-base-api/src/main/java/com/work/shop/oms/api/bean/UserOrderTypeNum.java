package com.work.shop.oms.api.bean;

import java.io.Serializable;

/**
 * 用户订单类型数量
 * @author QuYachu
 *
 */
public class UserOrderTypeNum implements Serializable{

	private static final long serialVersionUID = -4125584105430857390L;

	//订单类型

	private int waitPayNum;//用户待付款订单数量
	
	private int waitShipNum;//用户待发货订单数量
	
	private int waitReceivingNum;//用户待收货订单数量
	
	private int waitReviewNum;//等待评论订单数量
	
	private int completedNum;//用户已评论订单数量
	
	private int orderReturnNum;//退单数

    //申请单类型
    /**
     * 取消数量
     */
    private int cancelNum;

    /**
     * 待沟通数量
     */
    private int commiuteNum;

    /**
     * 完成数量
     */
    private int completeNum;

    /**
     * 待处理数量
     */
    private int processNum;
	
	public int getWaitPayNum() {
		return waitPayNum;
	}
	public void setWaitPayNum(int waitPayNum) {
		this.waitPayNum = waitPayNum;
	}
	public int getWaitShipNum() {
		return waitShipNum;
	}
	public void setWaitShipNum(int waitShipNum) {
		this.waitShipNum = waitShipNum;
	}
	public int getWaitReceivingNum() {
		return waitReceivingNum;
	}
	public void setWaitReceivingNum(int waitReceivingNum) {
		this.waitReceivingNum = waitReceivingNum;
	}
	public int getCompletedNum() {
		return completedNum;
	}
	public void setCompletedNum(int completedNum) {
		this.completedNum = completedNum;
	}
	public int getWaitReviewNum() {
		return waitReviewNum;
	}
	public void setWaitReviewNum(int waitReviewNum) {
		this.waitReviewNum = waitReviewNum;
	}
	public int getOrderReturnNum() {
		return orderReturnNum;
	}
	public void setOrderReturnNum(int orderReturnNum) {
		this.orderReturnNum = orderReturnNum;
	}

    public int getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(int cancelNum) {
        this.cancelNum = cancelNum;
    }

    public int getCommiuteNum() {
        return commiuteNum;
    }

    public void setCommiuteNum(int commiuteNum) {
        this.commiuteNum = commiuteNum;
    }

    public int getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(int completeNum) {
        this.completeNum = completeNum;
    }

    public int getProcessNum() {
        return processNum;
    }

    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }
}
