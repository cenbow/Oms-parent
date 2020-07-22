package com.work.shop.oms.api.payment.service;

import java.util.List;

import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.api.param.bean.PayBackInfo;
import com.work.shop.oms.api.param.bean.PayReturnInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.common.bean.ApiReturnData;

/**
 * 订单支付服务接口
 * @author QuYachu
 */
public interface OrderPaymentService {

	/**
	 * 获取订单支付(平台付款时使用)
	 * 传支付单号则查询支付单消息
	 * 传订单号 则查询该订单未支付支付单消息
	 * 传多个订单号，则生成合并支付单并返回支付消息
	 * @param paySn 支付单号
	 * @param masterOrderSnList 订单编码列表
	 * @return ApiReturnData<OrderPayInfo>
	 */
	ApiReturnData<OrderPayInfo> getOrderPayInfo(String paySn, List<String> masterOrderSnList);

	/**
	 * 获取指定的支付总金额
	 * @param masterOrderSnList
	 * @return
	 */
	ApiReturnData<OrderPayInfo> getOrderPayMoneyInfo(List<String> masterOrderSnList);

	/**
	 * 变更支付方式
	 * @param paySn 支付单号
	 * @param newPayId  支付方式id
	 * @return ApiReturnData
	 */
	ApiReturnData changeOrderPayMethod(String paySn, Integer newPayId);

	/**
	 * 支付前锁定支付单
	 * @param paySn 支付单号
	 * @param actionUser 操作用户
	 * @return ApiReturnData
	 */
	ApiReturnData lockOrderBeforePayment(String paySn, String actionUser);

	/**
	 * 前端支付成功回调
	 * @param payBackInfo 支付成功信息
	 * @return PayReturnInfo
	 */
	PayReturnInfo payStChClient(PayBackInfo payBackInfo);

	/**
	 * 根据支付单号获取对应的订单号
	 * @param paySn 支付单号
	 * @return ApiReturnData<List<String>>
	 */
	ApiReturnData<List<String>> getOrderSnByPaySn(String paySn);

	/**
	 * 根据支付单号获取对应的订单号
	 * @param paySn 支付单号
	 * @return ApiReturnData<List<String>>
	 */
	ApiReturnData<List<MasterOrderPay>> getOrderPaySnByMergePaySn(String paySn);

    ApiReturnData<MasterOrderPay> getGroupBuyOrderPay(String masterOrderSn);
}
