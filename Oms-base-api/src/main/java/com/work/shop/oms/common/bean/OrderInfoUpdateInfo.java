package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;

public class OrderInfoUpdateInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6327516988446008736L;

	private OrderDistribute orderInfo;											// 订单信息
	
	private MasterOrderInfo master;												// 订单信息
	
	private List<OrderGoodsUpdateBean> orderGoodsUpdateInfos;					// 订单商品列表
	
	private List<CardPackageUpdateInfo> cardPackageUpdateInfos;					// 订单红包列表
	
	private OrderGoodsUpdateBean addOrderGoods;									// 添加订单商品

	private String channelCode;													// 添加商品渠道码
	
	private BigDecimal totalFee = new BigDecimal(0.00);							// 订单商品总金额
	
	private String message;														// 执行结果信息

	private boolean flag = false;												// 执行结果
	
	private String addGoodsType = "1";											// 添加商品类型：0 新增订单添加商品(新增订单，创建换单);1  编辑订单添加商品
	
	private String activityType;												// 订单活动类型 0.无活动;1.C2B;2美品会3.夜市'
	
	private String doERP;														// 是否下发ERP 0：否; 1：是
	
	public List<OrderGoodsUpdateBean> getOrderGoodsUpdateInfos() {
		return orderGoodsUpdateInfos;
	}

	public void setOrderGoodsUpdateInfos(List<OrderGoodsUpdateBean> orderGoodsUpdateInfos) {
		this.orderGoodsUpdateInfos = orderGoodsUpdateInfos;
	}

	public List<CardPackageUpdateInfo> getCardPackageUpdateInfos() {
		return cardPackageUpdateInfos;
	}

	public void setCardPackageUpdateInfos(
			List<CardPackageUpdateInfo> cardPackageUpdateInfos) {
		this.cardPackageUpdateInfos = cardPackageUpdateInfos;
	}

	public OrderGoodsUpdateBean getAddOrderGoods() {
		return addOrderGoods;
	}

	public void setAddOrderGoods(OrderGoodsUpdateBean addOrderGoods) {
		this.addOrderGoods = addOrderGoods;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public OrderDistribute getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderDistribute orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getAddGoodsType() {
		return addGoodsType;
	}

	public void setAddGoodsType(String addGoodsType) {
		this.addGoodsType = addGoodsType;
	}
	
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getDoERP() {
		return doERP;
	}

	public void setDoERP(String doERP) {
		this.doERP = doERP;
	}

	public MasterOrderInfo getMaster() {
		return master;
	}

	public void setMaster(MasterOrderInfo master) {
		this.master = master;
	}

	@Override
	public String toString() {
		return "OrderInfoUpdateInfo [orderInfo=" + orderInfo + ", master="
				+ master + ", orderGoodsUpdateInfos=" + orderGoodsUpdateInfos
				+ ", cardPackageUpdateInfos=" + cardPackageUpdateInfos
				+ ", addOrderGoods=" + addOrderGoods + ", channelCode="
				+ channelCode + ", totalFee=" + totalFee + ", message="
				+ message + ", flag=" + flag + ", addGoodsType=" + addGoodsType
				+ ", activityType=" + activityType + ", doERP=" + doERP + "]";
	}

}
