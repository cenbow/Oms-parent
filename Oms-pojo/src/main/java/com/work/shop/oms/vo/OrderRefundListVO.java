package com.work.shop.oms.vo;

import java.io.Serializable;

import com.work.shop.oms.bean.OrderRefund;

public class OrderRefundListVO extends OrderRefund implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2597761164804342733L;
	
	private Integer isHistory;//是否历史订单
	
	private String payName;//支付方式名称
	
	private String returnPayStatusName;//退单财务状态名称
	
	private String orderOutSn;//关联外部交易号 order_info
	
	private String referer;//订单的来源媒体 order_info
	
	private String relatingOrderSn;//关联订单号 order_return
	
	private Integer returnOrderStatus;//退单状态 order_return
	
	private String returnOrderStatusName;
	
	private Integer payStatus;//退款总状态 order_return
	
	private String payStatusName;
	
	private Integer shipStatus;//退单发货状态 order_return
	
	private String shipStatusName;
	
	private String returnTotalStatus;//退单综合状态（退单状态+退款总状态+退单发货状态）
	
	private Integer returnType;//退单类型  order_return
	
	private String returnTypeName;
	
	private String channelName;//订单来源
	
	private String payNote;//退款方式备注order_pay
	
	private Integer haveRefund;//是否需要退款
	
	private Integer isGoodReceived;//是否收到货
	
	private String isGoodReceivedName;
	
	private Integer checkinStatus;//是否入库
	
	private Integer checkinStatusName;
	
	private Integer qualityStatus;//退单质检状态
	
	private Integer qualityStatusName;
	
	// 来源
	private String orderFrom;
	private String[] orderFroms;
	private String orderFromFirst;
	private String orderFromSec;


	public Integer getIsGoodReceived() {
		return isGoodReceived;
	}

	public void setIsGoodReceived(Integer isGoodReceived) {
		this.isGoodReceived = isGoodReceived;
	}

	public Integer getCheckinStatus() {
		return checkinStatus;
	}

	public void setCheckinStatus(Integer checkinStatus) {
		this.checkinStatus = checkinStatus;
	}

	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}

	public Integer getHaveRefund() {
		return haveRefund;
	}

	public void setHaveRefund(Integer haveRefund) {
		this.haveRefund = haveRefund;
	}

	public String getPayNote() {
		return payNote;
	}

	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}
	
	public String getReturnTotalStatus() {
		StringBuffer str=new StringBuffer();
		str.append(this.getReturnOrderStatusName());
		str.append(",");
		str.append(this.getPayStatusName());
		str.append(",");
		str.append(this.getCheckinStatusName());
		str.append(",");
		str.append(this.getIsGoodReceivedName());
		str.append(",");
		str.append(this.getQualityStatusName());
		return str.toString();
	}
	
	public String getShipStatusName() {
		if (null == this.getShipStatus()) {
			return "";
		}
		switch (this.getShipStatus()) {
		case 0:
			return "未收货";
		case 1:
			return "已收货未质检";
		case 2:
		//	return "质检通过待入库";
			return "待入库";
		case 3:
			return "已入库";
		default:
			return this.getShipStatus().toString();
		}
	}
	
	public String getPayStatusName() {
		if (null == this.getPayStatus()) {
			return "";
		}
		switch (this.getPayStatus()) {
		case 0:
			return "未结算";
		case 1:
			return "已结算";
		case 2:
			return "待结算";
		default:
			return this.getPayStatus().toString();
		}
	}
	
	public String getReturnOrderStatusName() {
		if (null == this.getReturnOrderStatus()) {
			return "";
		}
		switch (this.getReturnOrderStatus()) {
		case 0:
			return "未确定";
		case 1:
			return "已确认";
		case 4:
			return "无效";
		case 10:
			return "已完成";
		default:
			return this.getReturnOrderStatus().toString();
		}
	}

	public String getReturnPayStatusName() {
		if (null == this.getReturnPayStatus()) {
			return "";
		}
		switch (this.getReturnPayStatus()) {
		case 0:
			return "未结算";
		case 1:
			return "已结算";
		case 2:
			return "待结算";
		default:
			return this.getReturnPayStatus().toString();
		}
	}
	
	public String getReturnTypeName() {
		if (null == this.getReturnType()) {
			return "";
		}
		switch (this.getReturnType()) {
		case 1:
			return "退货单";
		case 2:
			return "拒收入库单";
		case 3:
			return "普通退款单";
		case 4:
			return "额外退款单";
		default:
			return this.getReturnType().toString();
		}
	}
	
	public String getIsGoodReceivedName() {
		if (null == this.getIsGoodReceived()) {
			return "";
		}
		switch (this.getIsGoodReceived()) {
		case 0:
			return "未收货";
		case 1:
			return "已收货";
		default:
			return this.getIsGoodReceived().toString();
		}
	}
	
	public String getCheckinStatusName() {
		if (null == this.getCheckinStatus()) {
			return "";
		}
		switch (this.getCheckinStatus()) {
		case 0:
			return "未入库";
		case 1:
			return "已入库";
		case 2:
			return "待入库";
		default:
			return this.getCheckinStatus().toString();
		}
	}
	
	public String getQualityStatusName() {
		if (null == this.getQualityStatus()) {
			return "";
		}
		switch (this.getQualityStatus()) {
		case 0:
			return "质检不通过";
		case 1:
			return "质检通过";
		default:
			return this.getQualityStatus().toString();
		}
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String[] getOrderFroms() {
		return orderFroms;
	}

	public void setOrderFroms(String[] orderFroms) {
		this.orderFroms = orderFroms;
	}

	public String getOrderFromFirst() {
		return orderFromFirst;
	}

	public void setOrderFromFirst(String orderFromFirst) {
		this.orderFromFirst = orderFromFirst;
	}

	public String getOrderFromSec() {
		return orderFromSec;
	}

	public void setOrderFromSec(String orderFromSec) {
		this.orderFromSec = orderFromSec;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
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

	public Integer getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Integer shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Integer getReturnType() {
		return returnType;
	}

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Integer isHistory) {
		this.isHistory = isHistory;
	}
	
	
}
