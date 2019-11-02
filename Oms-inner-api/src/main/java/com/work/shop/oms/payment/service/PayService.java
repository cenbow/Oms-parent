package com.work.shop.oms.payment.service;

import java.util.List;

import com.work.shop.oms.bean.MergeOrderPay;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 内部使用支付相关接口
 * @author dell
 *
 */
public interface PayService {

	/**
	 * 创建支付单(如果是创建未支付支付单则备份并删除原未支付支付单)
	 * 	PayCode支付方式  如果不传默认为"支付宝"
	 *  payStatus支付状态   如果不传默认为"未支付"
	 *  payStatus '支付状态；0，未付款；1，部分付款；2，已付款；3，已结算;4,待确认',
	 *  payId
	 *  PayCode
	 *  payTotalfee
	 *  masterOrderSn
	 */
	public ReturnInfo<List<String>>  createMasterPay(String masterOrderSn, List<MasterPay> masterPayList);

    /**
     * 创建合并支付单
     * @param masterOrderSnList
     * @return
     */
	ReturnInfo<MergeOrderPay> createMergePay(List<String> masterOrderSnList);

	/**
	 * 订单支付成功
	 * @param orderStatus
	 * @return
	 */
	ReturnInfo<String> payStCh(OrderStatus orderStatus);

	/**
	 * 退单转入款入库确认
	 * masterOrderSn
	 * userId
	 * paySn
	 * adminUser
	 */
	public ReturnInfo orderReturnPayStCh(OrderStatus orderStatus);

	/**
	 * 退单转入款未确认
	 * masterOrderSn
	 * userId
	 * paySn
	 * adminUser
	 */
	public ReturnInfo orderReturnUnPayStCh(OrderStatus orderStatus);

	/**
	 * 主订单支付单未支付
	 * masterOrderSn
	 * userId
	 * paySn
	 * adminUser
	 * source  "OMS":OmsManager
	 */
	public ReturnInfo unPayStCh(OrderStatus orderStatus);

	/**
	 * 根据支付单号或订单号修改支付方式
	 * @param paySn
	 * @param newPayId
	 * @return
	 */
	ReturnInfo changeOrderPayMethod(String paySn, Integer newPayId, String actionUser);

}
