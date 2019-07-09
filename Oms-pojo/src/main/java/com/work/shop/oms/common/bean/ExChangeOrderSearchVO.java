package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ExChangeOrderSearchVO  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 655925925522425673L;
	
	private String orderSn;
	private String relating_exchange_sn;
	private String relating_return_sn;
	private String relating_remoney_sn;
	private int orderStatus=-1;//（0，未确认；1，已确认；2，已取消；3，无效；4，退货；.....
	private String listDataType;
	private int payStatus=-1;//0，未付款；1，部分付款；2，已付款；3，已结算
	private int shipStatus=-1;//0，未发货；1，备货中；2，部分发货；3，已发货；...
	private String operator;
	private String user_name;
	private String consignee;
	private String startTime;
	private String endTime;
	private int order_form_first;
	private String order_form_sec;
	private String order_form;
	private String referer;
	
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		if(StringUtils.isNotEmpty(referer))
		{
		this.referer = referer;
		}
	}
	//分页
	private int start=0;
	//条数
	private int limits=20;
	//组装的order_froms
	private List<String> order_forms;
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		if(StringUtils.isNotEmpty(orderSn))
		{
			this.orderSn = orderSn;
		}
	}
	public String getRelating_exchange_sn() {
		return relating_exchange_sn;
	}
	public void setRelating_exchange_sn(String relating_exchange_sn) {
		if(StringUtils.isNotEmpty(relating_exchange_sn))
		{
			this.relating_exchange_sn = relating_exchange_sn;
		}
	}
	public String getRelating_return_sn() {
		return relating_return_sn;
	}
	public void setRelating_return_sn(String relating_return_sn) {
		if(StringUtils.isNotEmpty(relating_return_sn))
		{
			this.relating_return_sn = relating_return_sn;
		}
	}
	public String getRelating_remoney_sn() {
		return relating_remoney_sn;
	}
	public void setRelating_remoney_sn(String relating_remoney_sn) {
		if(StringUtils.isNotEmpty(relating_remoney_sn))
		{
			this.relating_remoney_sn = relating_remoney_sn;
		}
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getListDataType() {
		return listDataType;
	}
	public void setListDataType(String listDataType) {
		
		if(StringUtils.isNotEmpty(listDataType))
		{
			this.listDataType = listDataType;
		}
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		if(StringUtils.isNotEmpty(operator))
		{
			this.operator = operator;
		}
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		if(StringUtils.isNotEmpty(user_name))
		{
			this.user_name = user_name;
		}
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		if(StringUtils.isNotEmpty(consignee))
		{
			this.consignee = consignee;
		}
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		if(StringUtils.isNotEmpty(startTime))
		{
			this.startTime = startTime;
		}
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		if(StringUtils.isNotEmpty(endTime))
		{
			this.endTime = endTime;
		}
	}
	public int getOrder_form_first() {
		return order_form_first;
	}
	public void setOrder_form_first(int order_form_first) {
		this.order_form_first = order_form_first;
	}
	public String getOrder_form_sec() {
		return order_form_sec;
	}
	public void setOrder_form_sec(String order_form_sec) {
		if(StringUtils.isNotEmpty(order_form_sec))
		{
			this.order_form_sec = order_form_sec;
		}
	}
	public String getOrder_form() {
		return order_form;
	}
	public void setOrder_form(String order_form) {
		if(StringUtils.isNotEmpty(order_form))
		{
			this.order_form = order_form;
		}
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
	public List<String> getOrder_forms() {
		return order_forms;
	}
	public void setOrder_forms(List<String> order_forms) {
		this.order_forms = order_forms;
	}
}
