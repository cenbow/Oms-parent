package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.List;

public class SettleOrderInfo implements Serializable {

	private static final long serialVersionUID = -108025905564805053L;
	
	// (注：如果是来源是订单则是UID用户ID，如果是小票则是卡号，由卡号得到用户UID)
	private String uvid;// 用户标识(用户UID 或者 卡号)
	private String ordersn;// 票据标识（订单号 和 小票号）
	private List<SettleGoodsInfo> goodsinfos;// 商品信息集合
	private int source;// 来源标示 enum RECEIPT_SOURCE 1为ERP 2为平台
	private String paytype;// 支付方式
	private double settlement;// 结算金额
	private String shopid;// 门店ID
	private String guideid;// 导购员ID
	private long time;// 交易时间
	private String doStatus;// 操作类型
	private double multiple = 1;// 积分倍率
	private Integer bvvalue;
	
	public String getUvid() {
		return uvid;
	}
	public void setUvid(String uvid) {
		this.uvid = uvid;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public List<SettleGoodsInfo> getGoodsinfos() {
		return goodsinfos;
	}
	public void setGoodsinfos(List<SettleGoodsInfo> goodsinfos) {
		this.goodsinfos = goodsinfos;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public double getSettlement() {
		return settlement;
	}
	public void setSettlement(double settlement) {
		this.settlement = settlement;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getGuideid() {
		return guideid;
	}
	public void setGuideid(String guideid) {
		this.guideid = guideid;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getDoStatus() {
		return doStatus;
	}
	public void setDoStatus(String doStatus) {
		this.doStatus = doStatus;
	}
	public double getMultiple() {
		return multiple;
	}
	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}
	public Integer getBvvalue() {
		return bvvalue;
	}
	public void setBvvalue(Integer bvvalue) {
		this.bvvalue = bvvalue;
	}
}
