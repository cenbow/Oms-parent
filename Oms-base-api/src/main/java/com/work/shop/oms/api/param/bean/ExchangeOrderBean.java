package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.common.bean.MasterGoods;

/**
 * 换货单实体类
 * @author cage
 *
 */
public class ExchangeOrderBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String relatingOrderSn; //关联原订单orderSn
	private ExchangePageOrder pageOrder; //页面订单金额相关
//	private List<ExchangePageGoods> pageGoods;//页面商品相关
	
	private List<MasterGoods> pageGoods;//页面商品相关
	
	private String actionUser;//操作用户
	
	//换货单实体
	private CreateOrderReturnBean orderReturnBean;
	
	private String masterOrderSn; //关联原订单orderSn

		
	 
	public CreateOrderReturnBean getOrderReturnBean() {
		return orderReturnBean;
	}
	public void setOrderReturnBean(CreateOrderReturnBean orderReturnBean) {
		this.orderReturnBean = orderReturnBean;
	}
	 
	public ExchangePageOrder getPageOrder() {
		return pageOrder;
	}
	public void setPageOrder(ExchangePageOrder pageOrder) {
		this.pageOrder = pageOrder;
	}
//	public List<ExchangePageGoods> getPageGoods() {
//		return pageGoods;
//	}
//	public void setPageGoods(List<ExchangePageGoods> pageGoods) {
//		this.pageGoods = pageGoods;
//	}
	
	public List<MasterGoods> getPageGoods() {
		return pageGoods;
	}
	public void setPageGoods(List<MasterGoods> pageGoods) {
		this.pageGoods = pageGoods;
	}
	
	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public String getMasterOrderSn() {
		return masterOrderSn;
	}
	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}
}
