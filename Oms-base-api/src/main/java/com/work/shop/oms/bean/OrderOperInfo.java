package com.work.shop.oms.bean;

import java.io.Serializable;

public class OrderOperInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1721995237520233191L;

	private String masterOrderSn; // 订单号
	
	private String outerOrderSn; // 外部订单号
	
	private String userId; // 下单人
	
	private String receiverName; // 收货人姓名
	
	private String receiverMobile; // 收货人手机
	
	private String receiverTel; // 收货人电话

	private String channelCode; // 平台编码
	
	private String shopCode; // 店铺编码
	
	private String orderType; // 订单类型

	private Integer orderStatus; // 订单状态

	private Integer payStatus; // 支付状态

	private Integer shipStatus; // 发货状态

	private Integer questionStatus; // 问题单状态
	
	private String startTime; // 开始时间
	
	private String endTime; // 结束时间

	private String addTime; // 下单时间
	
}