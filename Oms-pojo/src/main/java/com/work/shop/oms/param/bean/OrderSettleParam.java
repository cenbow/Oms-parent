package com.work.shop.oms.param.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.work.shop.oms.vo.OrderSettleGoods;

/**
 * 订单结算
 * @author cage
 *
 */
public class OrderSettleParam  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String shopId;//渠道号
	private String orderNo;//订单号
	private String returnNo;//退单号
	private String docDate;//下单日期
	private Double ttlQty;//商品总数量(包括赠品) | 退货总数量
	private Double ttlVal;//订单总金额，包括红包、红包分摊额和运费、财物价格
	private String sellerId;//导购可能为空
	private String saleMode;////TL:退货||HH:换货||RL:零售 
	private String o2oSaleType;//字段来源OS数据库中order_info表中的referer（如果是云货架单据，则直接和OS一样赋值为“YHJ”,否则排除YHJ和全流通之外的都为“B2C”）
	private String depotCode;//退货处理仓
	private Integer bussType;//业务类型：0:入库 1:结算
	private List<OrderSettleGoods> details = new ArrayList<OrderSettleGoods>();//商品列表
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getReturnNo() {
		return returnNo;
	}
	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}
	
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public Double getTtlQty() {
		return ttlQty;
	}
	public void setTtlQty(Double ttlQty) {
		this.ttlQty = ttlQty;
	}
	public Double getTtlVal() {
		return ttlVal;
	}
	public void setTtlVal(Double ttlVal) {
		this.ttlVal = ttlVal;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSaleMode() {
		return saleMode;
	}
	public void setSaleMode(String saleMode) {
		this.saleMode = saleMode;
	}
	public String getO2oSaleType() {
		return o2oSaleType;
	}
	public void setO2oSaleType(String o2oSaleType) {
		this.o2oSaleType = o2oSaleType;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public Integer getBussType() {
		return bussType;
	}
	public void setBussType(Integer bussType) {
		this.bussType = bussType;
	}
	public List<OrderSettleGoods> getDetails() {
		return details;
	}
	public void setDetails(List<OrderSettleGoods> details) {
		this.details = details;
	}
}
