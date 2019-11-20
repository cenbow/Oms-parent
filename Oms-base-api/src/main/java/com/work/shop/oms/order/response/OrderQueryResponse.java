package com.work.shop.oms.order.response;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.api.bean.OrderContractBean;
import com.work.shop.oms.bean.OrderGoodsItem;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderQueryExportResult;
import com.work.shop.oms.bean.OrderShipGoodsItem;

/**
 * 订单查询结果返回对象
 * @author lemon
 */
public class OrderQueryResponse implements Serializable {

	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private Integer totalProperty;
	
	private String message;
	
	private List<OrderItem> orderItems;
	
	/**
	 * 订单商品列表
	 */
	private List<OrderGoodsItem> orderGoodsItems;
	
	/**
	 * 订单仓库配送商品列表
	 */
	private List<OrderShipGoodsItem> orderShipGoodsItems;

    /**
     * 订单合同列表
     */
	private List<OrderContractBean> orderContractBeans;

    /**
     * 公司订单导出结合
     */
	private List<OrderQueryExportResult> orderQueryExportResults;
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<OrderGoodsItem> getOrderGoodsItems() {
		return orderGoodsItems;
	}

	public void setOrderGoodsItems(List<OrderGoodsItem> orderGoodsItems) {
		this.orderGoodsItems = orderGoodsItems;
	}

	public List<OrderShipGoodsItem> getOrderShipGoodsItems() {
		return orderShipGoodsItems;
	}

	public void setOrderShipGoodsItems(List<OrderShipGoodsItem> orderShipGoodsItems) {
		this.orderShipGoodsItems = orderShipGoodsItems;
	}

    public List<OrderContractBean> getOrderContractBeans() {
        return orderContractBeans;
    }

    public void setOrderContractBeans(List<OrderContractBean> orderContractBeans) {
        this.orderContractBeans = orderContractBeans;
    }

    public List<OrderQueryExportResult> getOrderQueryExportResults() {
        return orderQueryExportResults;
    }

    public void setOrderQueryExportResults(List<OrderQueryExportResult> orderQueryExportResults) {
        this.orderQueryExportResults = orderQueryExportResults;
    }
}
