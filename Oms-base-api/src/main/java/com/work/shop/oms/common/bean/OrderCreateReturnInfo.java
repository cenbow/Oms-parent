package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderPay;

/**
 * 订单创建返回对象
 * @author lemon
 */
public class OrderCreateReturnInfo implements Serializable{

	private static final long serialVersionUID = -4918610418844534737L;

	/**
	 * 订单分配单编号
	 */
	private String orderSn;

	/**
	 * 订单来源
	 */
	private String orderFrom;

	/**
	 * 主订单编号
	 */
	private String masterOrderSn;

	/**
	 * 退单编号
	 */
	private String returnSn;

	/**
	 * 付款单编号
	 */
	private List<String> paySn = new ArrayList<String>();

	/**
	 * 立即分仓信息
	 */
	//private List<DepotInfo> depotInfos = null;

	/**
	 * 立即分仓商品信息
	 */
	//private List<MasterOrderGoods> orderGoods = null;

	/**
	 * 返回结果0：执行不成功；1：成功
	 */
	private int isOk = 0;

	/**
	 * 成功或失败信息
	 */
	private String message;

	public OrderCreateReturnInfo() {
	}

	/**
	 * 返回信息
	 * 
	 * @param masterOrderSn 订单编码
	 * @param returnSn 退单编码
	 * @param paySn 支付单
	 * @param message 信息
	 * @param orderFrom 来源
	 */
	public OrderCreateReturnInfo(String masterOrderSn, String returnSn, List<String> paySn, String message, String orderFrom) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.returnSn = returnSn;
		this.paySn = paySn;
		this.message = message;
		this.isOk = 1;
		this.orderFrom = orderFrom;
	}

	/**
	 * 返回信息
	 * @param masterOrderSn 订单编码
	 * @param returnSn 退单编码
	 * @param paySn 支付单列表
	 * @param orderGoods 订单商品列表
	 * @param message 消息
	 * @param orderFrom 来源
	 */
	/*public OrderCreateReturnInfo(String masterOrderSn, String returnSn,
			List<String> paySn, List<MasterOrderGoods> orderGoods,
			String message, String orderFrom) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.returnSn = returnSn;
		this.paySn = paySn;
		this.orderGoods = orderGoods;
		this.message = message;
		this.isOk = 1;
		this.orderFrom = orderFrom;
	}*/

	/**
	 * 返回信息
	 * @param masterOrderSn 订单编码
	 * @param returnSn 退单编码
	 * @param message 消息
	 * @param orderFrom 来源
	 */
	public OrderCreateReturnInfo(String masterOrderSn, String returnSn, String message, String orderFrom) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.returnSn = returnSn;
		this.message = message;
		this.isOk = 1;
		this.orderFrom = orderFrom;
	}

	/**
	 * 返回信息
	 * @param masterOrderSn 订单编码
	 * @param message 消息
	 * @param orderFrom 来源
	 */
	public OrderCreateReturnInfo(String masterOrderSn, String message, String orderFrom) {
		super();
		this.masterOrderSn = masterOrderSn;
		this.message = message;
		this.isOk = 0;
		this.orderFrom = orderFrom;
	}

	/**
	 * 返回信息
	 * @param message 消息
	 * @param orderFrom 来源
	 */
	public OrderCreateReturnInfo(String message, String orderFrom) {
		super();
		this.message = message;
		this.isOk = 0;
		this.orderFrom = orderFrom;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public List<String> getPaySn() {
		return paySn;
	}

	public void setPaySn(List<String> paySn) {
		this.paySn = paySn;
	}

	public void setPaySnByOrderBy(List<MasterOrderPay> orderPays) {
		if (orderPays == null || orderPays.isEmpty()) {
			return;
		}
		for (int i = 0; i < orderPays.size(); i++) {
			MasterOrderPay pay = orderPays.get(i);
			if (pay != null && !StringUtils.isEmpty(pay.getMasterPaySn())) {
				paySn.add(pay.getMasterPaySn());
			}
		}
	}

	/*public List<DepotInfo> getDepotInfos() {
		return depotInfos;
	}

	public void setDepotInfos(List<DepotInfo> depotInfos) {
		this.depotInfos = depotInfos;
	}*/

	public int getIsOk() {
		return isOk;
	}

	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*public List<MasterOrderGoods> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<MasterOrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}*/

	public String getMasterOrderSn() {
		return masterOrderSn;
	}

	public void setMasterOrderSn(String masterOrderSn) {
		this.masterOrderSn = masterOrderSn;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
}
