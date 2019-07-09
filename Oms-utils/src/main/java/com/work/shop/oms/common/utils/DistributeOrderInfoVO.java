package com.work.shop.oms.common.utils;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.OrderDepotShipDetail;
import com.work.shop.oms.bean.OrderDistribute;

public class DistributeOrderInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5502590854814478222L;
	
	private OrderDistribute sonOrder;//交货单表信息
	private DistributeOrderStatusUtils distributeOrderStatusUtils;//交货单按钮可用信息
	private List<String> relatingExOrRetOrdSns;//关联换货单和退单列表
	private List<String> questionReason;//交货单问题单类型信息
	private List<MasterOrderGoodsDetail> sonOrderGoodsList;//交货单商品列表
	private List<OrderDepotShipDetail> sonOrderDepotShipList;//交货单配送信息列表；
	
	public OrderDistribute getSonOrder() {
		return sonOrder;
	}
	public void setSonOrder(OrderDistribute sonOrder) {
		this.sonOrder = sonOrder;
	}
	public DistributeOrderStatusUtils getDistributeOrderStatusUtils() {
		return distributeOrderStatusUtils;
	}
	public void setDistributeOrderStatusUtils(
			DistributeOrderStatusUtils distributeOrderStatusUtils) {
		this.distributeOrderStatusUtils = distributeOrderStatusUtils;
	}
	public List<String> getRelatingExOrRetOrdSns() {
		return relatingExOrRetOrdSns;
	}
	public void setRelatingExOrRetOrdSns(List<String> relatingExOrRetOrdSns) {
		this.relatingExOrRetOrdSns = relatingExOrRetOrdSns;
	}
	public List<String> getQuestionReason() {
		return questionReason;
	}
	public void setQuestionReason(List<String> questionReason) {
		this.questionReason = questionReason;
	}
	public List<MasterOrderGoodsDetail> getSonOrderGoodsList() {
		return sonOrderGoodsList;
	}
	public void setSonOrderGoodsList(List<MasterOrderGoodsDetail> sonOrderGoodsList) {
		this.sonOrderGoodsList = sonOrderGoodsList;
	}
	public List<OrderDepotShipDetail> getSonOrderDepotShipList() {
		return sonOrderDepotShipList;
	}
	public void setSonOrderDepotShipList(
			List<OrderDepotShipDetail> sonOrderDepotShipList) {
		this.sonOrderDepotShipList = sonOrderDepotShipList;
	}	

}
