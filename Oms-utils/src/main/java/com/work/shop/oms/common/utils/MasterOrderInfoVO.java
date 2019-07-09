package com.work.shop.oms.common.utils;

import java.io.Serializable;
import java.util.List;


import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.MasterOrderPayTypeDetail;
import com.work.shop.oms.bean.OrderDepotShipDetail;
import com.work.shop.oms.common.bean.Common;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;


public class MasterOrderInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6749778367410048847L;
	
	private MasterOrderDetail masterOrderInfo;//主单信息(含主单表、扩展表、地址信息表)
	private OrderStatusUtils orderStatusUtils;//主单按钮状态信息
	private List<MasterOrderGoodsDetail> mergedMasOrdGoodsDetailList;//主单商品信息（含发货仓、配送信息，且合并）
	private List<DistributeOrderInfoVO> sonOrderList;//交货单配套信息
	private List<MasterOrderPayTypeDetail> masterOrderPayTypeDetailList;//付款信息
	private double returnSettleMoney = 0.00D;//主单下的退单总金额
	private List<OrderDepotShipDetail> orderDepotShipDetailList;//主单所有配送信息
	
	public MasterOrderDetail getMasterOrderInfo() {
		return masterOrderInfo;
	}
	public void setMasterOrderInfo(MasterOrderDetail masterOrderInfo) {
		this.masterOrderInfo = masterOrderInfo;
	}
	public OrderStatusUtils getOrderStatusUtils() {
		return orderStatusUtils;
	}
	public void setOrderStatusUtils(OrderStatusUtils orderStatusUtils) {
		this.orderStatusUtils = orderStatusUtils;
	}
	public List<MasterOrderGoodsDetail> getMergedMasOrdGoodsDetailList() {
		return mergedMasOrdGoodsDetailList;
	}
	public void setMergedMasOrdGoodsDetailList(
			List<MasterOrderGoodsDetail> mergedMasOrdGoodsDetailList) {
		this.mergedMasOrdGoodsDetailList = mergedMasOrdGoodsDetailList;
	}
	public List<DistributeOrderInfoVO> getSonOrderList() {
		return sonOrderList;
	}
	public void setSonOrderList(List<DistributeOrderInfoVO> sonOrderList) {
		this.sonOrderList = sonOrderList;
	}
	public List<MasterOrderPayTypeDetail> getMasterOrderPayTypeDetailList() {
		return masterOrderPayTypeDetailList;
	}
	public void setMasterOrderPayTypeDetailList(
			List<MasterOrderPayTypeDetail> masterOrderPayTypeDetailList) {
		this.masterOrderPayTypeDetailList = masterOrderPayTypeDetailList;
	}
	public double getReturnSettleMoney() {
		return returnSettleMoney;
	}
	public void setReturnSettleMoney(double returnSettleMoney) {
		this.returnSettleMoney = returnSettleMoney;
	}
	public List<OrderDepotShipDetail> getOrderDepotShipDetailList() {
		return orderDepotShipDetailList;
	}
	public void setOrderDepotShipDetailList(
			List<OrderDepotShipDetail> orderDepotShipDetailList) {
		this.orderDepotShipDetailList = orderDepotShipDetailList;
	}

	/**
	 * 订单信息
	 */
	private Common common;

	/**
	 * 商品明细信息
	 */
	private List<OrderGoodsUpdateBean> orderGoods;

	/**
	 * 付款信息VO
	 */
	private List<OrderPayVO> orderPaysVo;

	public Common getCommon() {
		return common;
	}
	public void setCommon(Common common) {
		this.common = common;
	}
	public List<OrderGoodsUpdateBean> getOrderGoods() {
		return orderGoods;
	}
	public void setOrderGoods(List<OrderGoodsUpdateBean> orderGoods) {
		this.orderGoods = orderGoods;
	}
	public List<OrderPayVO> getOrderPaysVo() {
		return orderPaysVo;
	}
	public void setOrderPaysVo(List<OrderPayVO> orderPaysVo) {
		this.orderPaysVo = orderPaysVo;
	}

}
