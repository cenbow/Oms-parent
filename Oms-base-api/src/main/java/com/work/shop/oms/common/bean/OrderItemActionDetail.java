package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;

public class OrderItemActionDetail implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8007042391343682618L;

	private Integer actionId;

    private String orderSn;

    private String actionUser;

    private Byte orderStatus;

    private Byte shippingStatus;

    private Byte payStatus;

    private Date logTime;

    private String actionNote;

    /**
     * 日志类型：0为订单操作，1为沟通
     */
    private Byte logType;

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Byte getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Byte shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Byte getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Byte payStatus) {
		this.payStatus = payStatus;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getActionNote() {
		return actionNote;
	}

	public void setActionNote(String actionNote) {
		this.actionNote = actionNote;
	}
	public String getOrderStatusStr() {
		if(null==this.getOrderStatus()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getOrderStatus()) {
		case 0:
			str.append("未确认");
			break;
		case 1:
			str.append("已确认");
			break;
		case 2:
			str.append("已取消");
			break;
		case 3:
			str.append("完成");
			break;
		default:
			str.append(this.getOrderStatus().toString());
			break;
		};
		return str.toString();
	}

	public String getPayStatusStr() {
		if(null==this.getPayStatus()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getPayStatus()) {
		case 0:
			str.append("未付款");
			break;
		case 1:
			str.append("部分付款");
			break;
		case 2:
			str.append("已付款");
			break;
		case 3:
			str.append("已结算");
			break;
		default:
			str.append(this.getPayStatus().toString());
			break;
		};
		return str.toString();
	}

	public String getShipStatusStr() {
		StringBuffer str=new StringBuffer();
		if(null==this.getShippingStatus()){return "";}
		switch (this.getShippingStatus()) {
		case 0:
			str.append("未发货");
			break;
		case 1:
			str.append("备货中");
			break;
		case 2:
			str.append("部分发货");
			break;
		case 3:
			str.append("已发货");
			break;
		case 4:
			str.append("部分收货");
			break;
		case 5:
			str.append("客户已收货");
			break;
		case 8:
			str.append("备货完成");
			break;
		default:
			str.append(this.getShippingStatus().toString());
			break;
		};
		return str.toString();
	}
	
	public String getLogTimeStr() {
		if (getLogTime() == null) {
			return "";
		}
		return TimeUtil.formatDate(getLogTime());
	}

    public Byte getLogType() {
        return logType;
    }

    public void setLogType(Byte logType) {
        this.logType = logType;
    }
}
