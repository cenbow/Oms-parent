package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单状态信息对象
 * @author QuYachu
 *
 */
public class OrderStatus implements Serializable {

	private static final long serialVersionUID = -2179440476599236972L;
	
	private String masterOrderSn;
	
	private String mergePaySn;
	
	private String orderSn;
	
	private String message;
	
	private String adminUser = "system";
	
	private String adminUserId;

	private Integer userId = 0;
	
	private Integer isHistory = 0;
	
	private String code;
	
	private boolean switchFlag;
	
	private Date endTime;

	/**
	 * 是否创建退单 0不创建、1创建
	 */
	private String type;

    /**
     * 库存释放
     */
	private boolean stockRealese;
	
	private String paySn;

	/**
	 * 支付金额
	 */
	private double payFee;

	/**
	 * 方法调用来源 POS:POS端;FRONT:前端;OMS:后台取消;ERP:ERP端
	 */
	private String source = "OMS";

	private String supplierOrderSn;

	/**
	 * 问题单类型
	 */
	private String questionType;
	
	private String orderOutSn;
	
	private String returnSn;

	/**
	 * 换单处理类型 0:撤销;1:确认   换货退单时设值
	 */
	private String newOrderSnType;

	/**
	 * 自提门店地址
	 */
	private String storeAddress;

    /**
     * 支付方式编码
     */
    private String payCode;
    
	/**
	 * 问题单类型列表（需要返回正常单的类型添加进来）
	 */
	private List<Integer> questionTypes;

    /**
     * 是否更新申请单状态
     */
    private boolean isUpdateReturnChangeStatus;

    /**
     * 退单类型，6为申请退款
     */
    private Integer returnType;

	public OrderStatus() {
		super();
	}
	
	public OrderStatus(String message, String adminUser) {
		super();
		this.message = message;
		this.adminUser = adminUser;
	}

	public OrderStatus(String orderSn, String message, String adminUser, Integer userId) {
		super();
		this.orderSn = orderSn;
		this.message = message;
		this.adminUser = adminUser;
		this.userId = userId;
	}

	public OrderStatus(String masterOrderSn, String message, String code) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.message = message;
		this.code = code;
	}

	public OrderStatus(String masterOrderSn, String message, String adminUser,
			String code) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.message = message;
		this.adminUser = adminUser;
		this.code = code;
	}
	
	public OrderStatus(String masterOrderSn, String orderSn, String message,
			String adminUser, String code) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.orderSn = orderSn;
		this.message = message;
		this.adminUser = adminUser;
		this.code = code;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Integer isHistory) {
		this.isHistory = isHistory;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

    public boolean isStockRealese() {
        return stockRealese;
    }

    public void setStockRealese(boolean stockRealese) {
        this.stockRealese = stockRealese;
    }

    public void setType(String type) {
		this.type = type;
	}

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

//	public Byte getDepotStatus() {
//		return depotStatus;
//	}
//
//	public void setDepotStatus(Byte depotStatus) {
//		this.depotStatus = depotStatus;
//	}
//
//	public Byte getOrderStatus() {
//		return orderStatus;
//	}
//
//	public void setOrderStatus(Byte orderStatus) {
//		this.orderStatus = orderStatus;
//	}
//
//	public Byte getShipStatus() {
//		return shipStatus;
//	}
//
//	public void setShipStatus(Byte shipStatus) {
//		this.shipStatus = shipStatus;
//	}
//
//	public Byte getPayStatus() {
//		return payStatus;
//	}
//
//	public void setPayStatus(Byte payStatus) {
//		this.payStatus = payStatus;
//	}

	public String getSupplierOrderSn() {
		return supplierOrderSn;
	}

	public void setSupplierOrderSn(String supplierOrderSn) {
		this.supplierOrderSn = supplierOrderSn;
	}
	
	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public boolean isSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(boolean switchFlag) {
		this.switchFlag = switchFlag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	@Override
	public String toString() {
		return "OrderStatus [masterOrderSn=" + masterOrderSn + ", orderSn="
				+ orderSn + ", message=" + message + ", adminUser=" + adminUser
				+ ", userId=" + userId + ", isHistory=" + isHistory + ", code="
				+ code + ", switchFlag=" + switchFlag + ", endTime=" + endTime
				+ ", type=" + type + ", paySn=" + paySn + ", source=" + source
				+ ", supplierOrderSn=" + supplierOrderSn + ", questionType="
				+ questionType + "]";
	}

	public double getPayFee() {
		return payFee;
	}

	public void setPayFee(double payFee) {
		this.payFee = payFee;
	}

	public String getMergePaySn() {
		return mergePaySn;
	}

	public void setMergePaySn(String mergePaySn) {
		this.mergePaySn = mergePaySn;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getNewOrderSnType() {
		return newOrderSnType;
	}

	public void setNewOrderSnType(String newOrderSnType) {
		this.newOrderSnType = newOrderSnType;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

	public List<Integer> getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(List<Integer> questionTypes) {
		this.questionTypes = questionTypes;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

    public boolean isUpdateReturnChangeStatus() {
        return isUpdateReturnChangeStatus;
    }

    public void setUpdateReturnChangeStatus(boolean updateReturnChangeStatus) {
        isUpdateReturnChangeStatus = updateReturnChangeStatus;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }
}
