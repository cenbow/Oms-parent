package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.List;

public class ReturnMobile implements Serializable {

	private static final long serialVersionUID = -5717377249357263582L;
	
	private String returnSn;//退单号
	
	private String relatingOrderSn;//关联订单号
	
	private int status;//状态  1退货中、3退货完成
	
	private String clearTime;//结算时间
	
	private List<GoodsMobile> goodsMobileList;//商品集合

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GoodsMobile> getGoodsMobileList() {
		return goodsMobileList;
	}

	public void setGoodsMobileList(List<GoodsMobile> goodsMobileList) {
		this.goodsMobileList = goodsMobileList;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}
	
	
	
}
