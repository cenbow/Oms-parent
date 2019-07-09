package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 该类用于查询 返回 页面的数据
 * 
 * @author Administrator
 * 
 */
public class OrderQuestionSearchResultVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4304740695032675278L;
	
	private String questionReason;		                                //问题单原因

	private String logisticsType;										// 物流问题单类型

	private String outStockCode;										// 缺货条码
	
	private String lockUserName;										// 操作人

	private String no;													// 问题的序号
	
	private String orderSn;											// 关联订单号

	private String orderOutSn;										// 外部交易号
	
	private String reason;												// 问题单Code
	
	private int questionType;											// 问题单类型
	
	private String questionTypeStr;									
	
	private int channelUserLevel;										// 渠道会员等级
	
	private int useLevel;												// 渠道会员等级

	private String useLevelStr = "";									// 渠道会员等级字符串
	
	private String referer;												// 订单来源
	
	private String channelName;									     	// 订单店铺名称
	
	private String questionDesc;										// 问题单原因
	
	private int orderStatus;											// 订单状态
	
	private int payStatus;												// 支付总状态
	
	private int shipStatus;										    	// 发货总状态
	
	private Date addTime;										    	// 下单时间
	
	private int transType;												// 订单类型
	
	private String orderFrom;											// 订单来源
	
	private String userName;											// 下单人
	
	private String consignee;											// 收货人
	
	private BigDecimal totalFee;										// 订单总金额
	
	private BigDecimal totalPayable;									// 应付金额
	
	private String customerNote = "";									// 客服备注
	
	private String businessNote = "";									// 商家备注
	
	private int lockStatus;											   // 锁定状态

	private String processStatusStr = "";                       	   // 订单状态 字符串 用于拼接 字符串
	
	private String lockStatusStr;                                      // 锁定状态字符串
		
	private String transTypeStr;                                       // 订单类型的字符串

	private String operationStr;                                       // 操作字符串 根据 锁定状态 来 判断
	
	private Date questionAddTime;										// 问题单创建时间

	public String getQuestionReason() {
		return questionReason;
	}

	public void setQuestionReason(String questionReason) {
		this.questionReason = questionReason;
	}
	
	public String getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(String logisticsType) {
		this.logisticsType = logisticsType;
	}
	
	public String getOutStockCode() {
		return outStockCode;
	}

	public void setOutStockCode(String outStockCode) {
		this.outStockCode = outStockCode;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public int getChannelUserLevel() {
		return channelUserLevel;
	}

	public void setChannelUserLevel(int channelUserLevel) {
		this.channelUserLevel = channelUserLevel;
		/*
		 * {v: '11', n: 'VIP'}, {v: '21', n: 'SVIP'}, {v: '99', n: '内部员工'}, {v:
		 * '100', n: '内部员工VIP'}, {v: '101', n: '内部员工SVIP'}
		 */
		switch (channelUserLevel) {
		case 11:
			setUseLevelStr("VIP");
			break;
		case 21:
			setUseLevelStr("SVIP");
			break;
		case 99:
			setUseLevelStr("内部员工");
			break;
		case 100:
			setUseLevelStr("内部员工VIP");
			break;
		case 101:
			setUseLevelStr("内部员工SVIP");
			break;
		}
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
		switch (questionType) {
		case 0:
			setQuestionTypeStr("oms问题单");
			break;
		case 1:
			setQuestionTypeStr("缺货问题单");
			break;
		}
	}

	public String getQuestionTypeStr() {
		return questionTypeStr;
	}

	public void setQuestionTypeStr(String questionTypeStr) {
		this.questionTypeStr = questionTypeStr;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
		switch (orderStatus) {
		case 0:
			// （0，未确认；1，已确认；2，已取消；3，无效；4，退货；5，锁定；6，解锁；7，完成；8，拒收；9，已合并；10，已拆分；）
			setProcessStatusStr(getProcessStatusStr() + "未确认,");
			break;
		case 1:
			setProcessStatusStr(getProcessStatusStr() + "已确认,");
			break;
		case 2:
			setProcessStatusStr(getProcessStatusStr() + "已取消,");
			break;
		case 3:
			setProcessStatusStr(getProcessStatusStr() + "无效,");
			break;
		case 4:
			setProcessStatusStr(getProcessStatusStr() + "退货,");
			break;
		case 5:
			setProcessStatusStr(getProcessStatusStr() + "锁定,");
			break;
		case 6:
			setProcessStatusStr(getProcessStatusStr() + "解锁,");
			break;
		case 7:
			setProcessStatusStr(getProcessStatusStr() + "完成,");
			break;
		case 8:
			setProcessStatusStr(getProcessStatusStr() + "拒收,");
			break;
		case 9:
			setProcessStatusStr(getProcessStatusStr() + "已合并,");
			break;
		case 10:
			setProcessStatusStr(getProcessStatusStr() + "已拆分,");
			break;
		}
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
		// （0，未付款；1，部分付款；2，已付款；3，已结算）
		switch (payStatus) {
		case 0:
			setProcessStatusStr(getProcessStatusStr() + "未付款,");
			break;
		case 1:
			setProcessStatusStr(getProcessStatusStr() + "部分付款,");
			break;
		case 2:
			setProcessStatusStr(getProcessStatusStr() + "已付款,");
			break;
		case 3:
			setProcessStatusStr(getProcessStatusStr() + "已结算,");
			break;
		}
	}

	public int getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
		// （0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
		switch (shipStatus) {
		case 0:
			setProcessStatusStr(getProcessStatusStr() + "未发货");
			break;
		case 1:
			setProcessStatusStr(getProcessStatusStr() + "备货中");
			break;
		case 2:
			setProcessStatusStr(getProcessStatusStr() + "部分发货");
			break;
		case 3:
			setProcessStatusStr(getProcessStatusStr() + "已发货");
			break;
		case 4:
			setProcessStatusStr(getProcessStatusStr() + "部分收货");
			break;
		case 5:
			setProcessStatusStr(getProcessStatusStr() + "客户已收货");
			break;
		case 6:
			setProcessStatusStr(getProcessStatusStr() + "门店部分收货");
			break;
		case 7:
			setProcessStatusStr(getProcessStatusStr() + "门店收货");
			break;
		}

	}

	public String getUseLevelStr() {
		return useLevelStr;
	}

	public void setUseLevelStr(String useLevelStr) {
		this.useLevelStr = useLevelStr;
	}

	public String getLockStatusStr() {
		return lockStatusStr;
	}

	public void setLockStatusStr(String lockStatusStr) {
		this.lockStatusStr = lockStatusStr;
	}

	public String getProcessStatusStr() {
		return processStatusStr;
	}

	public void setProcessStatusStr(String processStatusStr) {
		this.processStatusStr = processStatusStr;
	}

	public String getTransTypeStr() {
		return transTypeStr;
	}

	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}

	public String getOperationStr() {
		return operationStr;
	}

	public void setOperationStr(String operationStr) {
		this.operationStr = operationStr;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
		// 调用该方法
		switch (transType) {
		case 1:
			setTransTypeStr("款到发货");
			break;
		case 2:
			setTransTypeStr("货到付款");
			break;
		case 3:
			setTransTypeStr("担保交易");
			break;
		}

	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}


	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(BigDecimal totalPayable) {
		this.totalPayable = totalPayable;
	}

	public String getCustomerNote() {
		return customerNote;
	}

	public void setCustomerNote(String customerNote) {
		this.customerNote = customerNote;
	}

	public String getBusinessNote() {
		return businessNote;
	}

	public void setBusinessNote(String businessNote) {
		this.businessNote = businessNote;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
		switch (lockStatus) {
		case 0:
			setLockStatusStr("未锁定");
			setOperationStr("返回正常单");
			break;
		case 1:
			setLockStatusStr("已锁定");
			setOperationStr("其他用户权限");
			break;

		}
	}

	public int getUseLevel() {
		return useLevel;
	}

	public void setUseLevel(int useLevel) {
		this.useLevel = useLevel;
		switch (useLevel) {
		case 0:
			setUseLevelStr("普通会员");
			break;
		case 1:
			setUseLevelStr("高级会员");
			break;
		case 2:
			setUseLevelStr("vip会员");
			break;
		case 3:
			setUseLevelStr("至尊vip会员");
			break;
		}
	}

	public String getLockUserName() {
		return lockUserName;
	}

	public void setLockUserName(String lockUserName) {
		this.lockUserName = lockUserName;
	}

	public String getQuestionDesc() {
		return questionDesc;
	}

	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	
	private String masterOrderSn;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
		
	private String masterQuestionDesc;

	public String getMasterQuestionDesc() {
		return masterQuestionDesc;
	}

	public void setMasterQuestionDesc(String masterQuestionDesc) {
		this.masterQuestionDesc = masterQuestionDesc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getQuestionAddTime() {
		return questionAddTime;
	}

	public void setQuestionAddTime(Date questionAddTime) {
		this.questionAddTime = questionAddTime;
	}

	private String customCode;  //缺货商品号
	
	
	private String depotCode;  //发货仓编码

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	
	private Short lackNum; //缺货量
	
	public Short getLackNum() {
		return lackNum;
	}

	public void setLackNum(Short lackNum) {
		this.lackNum = lackNum;
	}
	
	
}
