package com.work.shop.oms.api.param.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateOrderReturnBean implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private CreateOrderReturn createOrderReturn = new CreateOrderReturn();
	private CreateOrderReturnShip createOrderReturnShip = new CreateOrderReturnShip();
	private List<CreateOrderRefund> createOrderRefundList = new ArrayList<CreateOrderRefund>();
	private List<CreateOrderReturnGoods> createOrderReturnGoodsList = new ArrayList<CreateOrderReturnGoods>();
	
	private String relatingOrderSn; //关联原订单orderSn 必输
	private String  orderReturnSn;  //退单Sn
	private Integer returnType; //退单类型：1退货单、2拒收入库单、3退款单(取消订单,删除商品等) 4额外退款单
	private Date  addTime;  //退单时间
	private String actionUser;
	private String actionNote;
	private String goodsId;//入库商品id
	
	private Integer bvValue;// bvValue
	private Double points;// 点数
	
	private Integer baseBvValue;
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public CreateOrderReturn getCreateOrderReturn() {
		return createOrderReturn;
	}
	public void setCreateOrderReturn(CreateOrderReturn createOrderReturn) {
		this.createOrderReturn = createOrderReturn;
	}
	public List<CreateOrderRefund> getCreateOrderRefundList() {
		return createOrderRefundList;
	}
	
	public String getActionNote() {
		return actionNote;
	}
	public void setActionNote(String actionNote) {
		this.actionNote = actionNote;
	}
	public void setCreateOrderRefundList(
			List<CreateOrderRefund> createOrderRefundList) {
		this.createOrderRefundList = createOrderRefundList;
	}
	public List<CreateOrderReturnGoods> getCreateOrderReturnGoodsList() {
		return createOrderReturnGoodsList;
	}
	public void setCreateOrderReturnGoodsList(
			List<CreateOrderReturnGoods> createOrderReturnGoodsList) {
		this.createOrderReturnGoodsList = createOrderReturnGoodsList;
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
	public String getOrderReturnSn() {
		return orderReturnSn;
	}
	public void setOrderReturnSn(String orderReturnSn) {
		this.orderReturnSn = orderReturnSn;
	}
	public Integer getReturnType() {
		return returnType;
	}
	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}
	public Date getAddTime() {
		return addTime == null ?  new Date() : addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public CreateOrderReturnShip getCreateOrderReturnShip() {
		return createOrderReturnShip;
	}
	public void setCreateOrderReturnShip(CreateOrderReturnShip createOrderReturnShip) {
		this.createOrderReturnShip = createOrderReturnShip;
	}
	public Integer getBvValue() {
		return bvValue;
	}
	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}
	public Double getPoints() {
		return points == null ? 0 : points;
	}
	public void setPoints(Double points) {
		this.points = points;
	}
	public Integer getBaseBvValue() {
		return baseBvValue == null ? 0 : baseBvValue;
	}
	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}
}
