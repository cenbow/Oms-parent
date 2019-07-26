package com.work.shop.oms.order.response;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.common.bean.*;

/**
 * 订单管理返回对象
 * @author QuYachu
 */
public class OrderManagementResponse implements Serializable {

	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private String message;
	
	private OrderItemDetail itemDetail;
	
	private OrderItemStatusUtils orderItemStatusUtils;
	
	private List<OrderItemGoodsDetail> goodsDetails;
	
	private List<OrderItemDepotDetail> depotDetails;
	
	private List<OrderItemPayDetail> payDetails;
	
	private List<OrderItemAction> itemActions;
	
	private List<String> returnSns;

	private List<OrderItemDepotInfo> orderItemDepotInfos;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OrderItemDetail getItemDetail() {
		return itemDetail;
	}

	public void setItemDetail(OrderItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}

	public List<OrderItemGoodsDetail> getGoodsDetails() {
		return goodsDetails;
	}

	public void setGoodsDetails(List<OrderItemGoodsDetail> goodsDetails) {
		this.goodsDetails = goodsDetails;
	}

	public List<OrderItemDepotDetail> getDepotDetails() {
		return depotDetails;
	}

	public void setDepotDetails(List<OrderItemDepotDetail> depotDetails) {
		this.depotDetails = depotDetails;
	}

	public List<OrderItemPayDetail> getPayDetails() {
		return payDetails;
	}

	public void setPayDetails(List<OrderItemPayDetail> payDetails) {
		this.payDetails = payDetails;
	}

	public List<OrderItemAction> getItemActions() {
		return itemActions;
	}

	public void setItemActions(List<OrderItemAction> itemActions) {
		this.itemActions = itemActions;
	}

	public OrderItemStatusUtils getOrderItemStatusUtils() {
		return orderItemStatusUtils;
	}

	public void setOrderItemStatusUtils(OrderItemStatusUtils orderItemStatusUtils) {
		this.orderItemStatusUtils = orderItemStatusUtils;
	}

	public List<String> getReturnSns() {
		return returnSns;
	}

	public void setReturnSns(List<String> returnSns) {
		this.returnSns = returnSns;
	}

    public List<OrderItemDepotInfo> getOrderItemDepotInfos() {
        return orderItemDepotInfos;
    }

    public void setOrderItemDepotInfos(List<OrderItemDepotInfo> orderItemDepotInfos) {
        this.orderItemDepotInfos = orderItemDepotInfos;
    }
}
