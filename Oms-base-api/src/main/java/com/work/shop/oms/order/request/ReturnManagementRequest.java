package com.work.shop.oms.order.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.vo.StorageGoods;

/**
 * 退单操作请求
 * @author QuYachu
 */
public class ReturnManagementRequest implements Serializable {

	private static final long serialVersionUID = -7115261779726715323L;

	/**
	 * 订单号
	 */
	private String masterOrderSn;

	/**
	 * 退单号
	 */
	private String returnSn;
	
	private String message;
	
	private String actionUser;
	
	private String actionUserId;

	/**
	 * 自提码
	 */
	private String gotCode;
	
	private String returnType;

	/**
	 * 退款结果 默认true true:成功;false:失败
	 */
	private Boolean refundResult;

	/**
	 * 实际退款金额
	 */
	private BigDecimal actualRefundAmount;
	
	private CreateOrderReturnBean returnBean;

	/**
	 * 入库明细列表
	 */
	private List<StorageGoods> storageGoods;

    /**
     * 退款金额
     */
    private String returnOrderAmount;

    /**
     * 下单人
     */
    private String userId;

    private String siteCode;

    /**
     * 0为不确认退单，1为确认退单
     */
    private int isConfirm;

    /**
     * 交货单号
     */
    private String orderSn;

    public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getActionUser() {
		return actionUser == null ? "system" : actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(String actionUserId) {
		this.actionUserId = actionUserId;
	}

	public String getGotCode() {
		return gotCode;
	}

	public void setGotCode(String gotCode) {
		this.gotCode = gotCode;
	}

	public CreateOrderReturnBean getReturnBean() {
		return returnBean;
	}

	public void setReturnBean(CreateOrderReturnBean returnBean) {
		this.returnBean = returnBean;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<StorageGoods> getStorageGoods() {
		return storageGoods;
	}

	public void setStorageGoods(List<StorageGoods> storageGoods) {
		this.storageGoods = storageGoods;
	}

	public BigDecimal getActualRefundAmount() {
		return actualRefundAmount;
	}

	public void setActualRefundAmount(BigDecimal actualRefundAmount) {
		this.actualRefundAmount = actualRefundAmount;
	}

	public Boolean getRefundResult() {
		return refundResult == null ? true : refundResult;
	}

	public void setRefundResult(Boolean refundResult) {
		this.refundResult = refundResult;
	}

    public String getReturnOrderAmount() {
        return returnOrderAmount;
    }

    public void setReturnOrderAmount(String returnOrderAmount) {
        this.returnOrderAmount = returnOrderAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public int getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(int isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
