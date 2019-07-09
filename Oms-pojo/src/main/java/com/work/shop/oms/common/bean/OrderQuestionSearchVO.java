package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 该vo对象 用于查询  vo
 * @author Administrator
 *
 */
public class OrderQuestionSearchVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9147404690614173284L;
	
	
	private String selectTimeType;					// 时间查询类型
	
	private String sort;							//排序的字段
	
	private String dir;								//升序或降序
	
	private Integer id;		
	
	private Integer orderStatus;										//订单状态

	private Integer payStatus;											//支付状态

	private Integer shipStatus;											//发货状态
	
	private String orderSn;												//关联订单号
	
	private String orderFormFirst;										//一级关联订单来源类型
	
	private String orderFormSec;										//二级关联订单来源渠道
	
	private String orderForm;											//三级关联订单来源店铺
	
	//private String[] orderForm;		
	
	private String[] orderFormsVo;
	
	private String referer;
	
	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSelectTimeType() {
		return selectTimeType;
	}

	public void setSelectTimeType(String selectTimeType) {
		this.selectTimeType = selectTimeType;
	}
	
	public String[] getOrderFormsVo() {
		return orderFormsVo;
	}

	public void setOrderFormsVo(String[] orderFormsVo) {
		this.orderFormsVo = orderFormsVo;
	}

	private String listDataTypehis;										//是否是历史记录
	
	public String getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(String orderForm) {
		this.orderForm = orderForm;
	}

	private List<String> orderForms;									//三级关联订单来源店铺列表
	
	private Integer useLevel;											//平台会员等级
	
	private Integer channelUseLevel;									//线上店铺会员等级
	
	private Integer transType;											//交易类型 显示 订单类型
	
	private String userName;											//下单人
	
	private String addTimeStart;										//搜索下单时间  开始
	
	private String addTimeEnd;											//搜索 下单时间 结束
	
	private String questionTimeStart;									//搜索问题单时间 开始
	
	private String questionTimeEnd;										//搜索 问题单 时间 结束
	
	private String reason;												//问题单原因
	
	private Integer processStatus;										//问题单处理的状态
	
	private int start = 0;												//分页 起始页
	
	private int limits = 20;											//分页 每页条数
	
	private String listDataType;										//OS问题单 与 物流问题单的区分
	
	private String depotCode;											//发货仓库编码
	
	private String deliverySn;											//交货单编码
	
	private String orderClause;											//排序字段

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderFormFirst() {
		return orderFormFirst;
	}

	public void setOrderFormFirst(String orderFormFirst) {
		this.orderFormFirst = orderFormFirst;
	}

	public String getOrderFormSec() {
		return orderFormSec;
	}

	public void setOrderFormSec(String orderFormSec) {
		this.orderFormSec = orderFormSec;
	}

/*	public String getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(String orderForm) {
		this.orderForm = orderForm;
	}*/

	public String getListDataTypehis() {
		return listDataTypehis;
	}

	public void setListDataTypehis(String listDataTypehis) {
		this.listDataTypehis = listDataTypehis;
	}

	public List<String> getOrderForms() {
		return orderForms;
	}

	public void setOrderForms(List<String> orderForms) {
		this.orderForms = orderForms;
	}

	public Integer getUseLevel() {
		return useLevel;
	}

	public void setUseLevel(Integer useLevel) {
		this.useLevel = useLevel;
	}

	public Integer getChannelUseLevel() {
		return channelUseLevel;
	}

	public void setChannelUseLevel(Integer channelUseLevel) {
		this.channelUseLevel = channelUseLevel;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddTimeStart() {
		return addTimeStart;
	}

	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

	public String getQuestionTimeStart() {
		return questionTimeStart;
	}

	public void setQuestionTimeStart(String questionTimeStart) {
		this.questionTimeStart = questionTimeStart;
	}

	public String getQuestionTimeEnd() {
		return questionTimeEnd;
	}

	public void setQuestionTimeEnd(String questionTimeEnd) {
		this.questionTimeEnd = questionTimeEnd;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimits() {
		return limits;
	}

	public void setLimits(int limits) {
		this.limits = limits;
	}

	public String getListDataType() {
		return listDataType;
	}

	public void setListDataType(String listDataType) {
		this.listDataType = listDataType;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDeliverySn() {
		return deliverySn;
	}

	public void setDeliverySn(String deliverySn) {
		this.deliverySn = deliverySn;
	}

	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	@Override
	public String toString() {
		return "OrderQuestionSearchVO [id=" + id + ", orderStatus="
				+ orderStatus + ", payStatus=" + payStatus + ", shipStatus="
				+ shipStatus + ", orderSn=" + orderSn + ", orderFormFirst="
				+ orderFormFirst + ", orderFormSec=" + orderFormSec
				+ ", orderForm=" + orderForm + ", listDataTypehis="
				+ listDataTypehis + ", orderForms=" + orderForms
				+ ", useLevel=" + useLevel + ", channelUseLevel="
				+ channelUseLevel + ", transType=" + transType + ", userName="
				+ userName + ", addTimeStart=" + addTimeStart + ", addTimeEnd="
				+ addTimeEnd + ", questionTimeStart=" + questionTimeStart
				+ ", questionTimeEnd=" + questionTimeEnd + ", reason=" + reason
				+ ", processStatus=" + processStatus + ", start=" + start
				+ ", limits=" + limits + ", listDataType=" + listDataType
				+ ", depotCode=" + depotCode + ", deliverySn=" + deliverySn
				+ ", orderClause=" + orderClause + "]";
	}
	
	
	private String mainOrChild;

	public String getMainOrChild() {
		return mainOrChild;
	}

	public void setMainOrChild(String mainOrChild) {
		this.mainOrChild = mainOrChild;
	}
	
	private String masterOrderSn;

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	
	private String exportState;  //导出状态 1：导出  0：查询

	public String getExportState() {
		return exportState;
	}

	public void setExportState(String exportState) {
		this.exportState = exportState;
	}
	

}
