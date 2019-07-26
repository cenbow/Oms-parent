package com.work.shop.oms.utils;


/**
 * MQ常量设置类
 * 
 * @author
 * 
 */
public class MqConfig {

	/**
	 * 退款单下发(微信支付)
	 */
	public static final String cloud_center_orderRefund = "cloud-center-orderRefund";

    /**
     * 退款单下发
     */
	public static final String CLOUD_ORDER_ACCOUNT_RETURN = "order_account_return";

	/**
	 * 分配后通知打印
	 */
	public static final String cloud_center_orderDistribute = "cloud-center-orderDistribute";

	/**
	 * 外渠订单取消
	 */
	public static final String cloud_channel_orderCancel = "cloud-channel-orderCancel";

	/**
	 * 订单取消
	 */
	public static final String supplier_order_cancel = "supplier_order_cancel";

	/**
	 * 订单收货
	 */
	public static final String supplier_order_receive = "supplier_order_receive";

	/**
	 * 订单超时自动收货
	 */
	public static final String supplier_order_receive_task = "supplier_order_receive_task ";

    /**
     * 订单超时自动签收
     */
	public static final String order_sn_receive = "order_sn_receive";
}
