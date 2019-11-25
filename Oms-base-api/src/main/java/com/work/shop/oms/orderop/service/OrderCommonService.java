package com.work.shop.oms.orderop.service;

import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;

import java.util.List;

/**
 * 订单操作服务问题单、取消、挂起等
 * @author lemon
 *
 */
public interface OrderCommonService {

	/**
	 * 设置主订单问题单
	 * 
	 * @param orderSn 子订单号
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return ReturnInfo
	 */
	ReturnInfo questionOrderByOrderSn(String orderSn, OrderStatus orderStatus);

	/**
	 * 提供给ERP，第一次分配前通知OS商品缺货，生成缺货问题单
	 * @param orderSn 订单号
	 * @param lackSkuParams 缺货商品列表
	 * @param shortReason 缺货原因
	 * @return
	 */
	ReturnInfo noticeOsFromErpForShort(String orderSn, List<LackSkuParam> lackSkuParams, String shortReason);
	
	/**
	 * 主订单取消(共用方法)
	 * 
	 * @param masterOrderSn 主订单号
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	ReturnInfo cancelOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);

	/**
	 * 子订单取消(共用方法)
	 * @param orderStatus code:取消原因code;message:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	ReturnInfo cancelOrderByOrderSn(OrderStatus orderStatus);
	
	/**
	 * 第三方供应商发货前向OMS确认是否可以发货
	 * 
	 * @param request
	 * @return ReturnInfo
	 */
	DistOrderShipResponse shippedConfirm(DistOrderShipRequest request);
	
	/**
	 * 设置缺货问题单（短拣、短配、缺货问题单等）
	 * 
	 * @param orderSn 配送单号
	 * @param lackSkuParams 缺货商品列表
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;supplierOrderSn:供应商工单ID
	 * @return ReturnInfo
	 */
	ReturnInfo addLackSkuQuestion(String orderSn, List<LackSkuParam> lackSkuParams, OrderStatus orderStatus);
	
	/**
	 * 接受物流发货数据
	 * @param providerBeanParams
	 * @return
	 */
	ReturnInfo acceptShip(List<OrderToShippedProviderBeanParam> providerBeanParams);
	
	/**
	 * 编辑收货人信息
	 *
	 * @param consignInfo
	 * @return
	 */
	ReturnInfo editConsigneeInfoByOrderSn(ConsigneeModifyInfo consignInfo);

	/**
	 * 编辑承运商
	 * 
	 * @param modifyInfo 客户信息
	 * @param modifyInfo
	 * @return
	 */
	ReturnInfo editShippingType(ConsigneeModifyInfo modifyInfo);
	
	/**
	 * 子订单未确认
	 * 
	 * @param modifyInfo
	 * @return
	 */
	 ReturnInfo posConfirmOrder(ConsigneeModifyInfo modifyInfo);

	/**
	 * 编辑订单费用信息
	 * 
	 * @param masterOrderSn
	 * @param actionUser
	 * @param shippingFee
	 * @return
	 * @throws Exception
	 */
	ReturnInfo editShippingFee(String masterOrderSn, String actionUser, Double shippingFee);
	
	/**
	 * 编辑订单其他信息
	 * 
	 * @param orderSn
	 * @param actionUser
	 * @param otherModify
	 * @return
	 */
	ReturnInfo editOrderOther(String masterOrderSn, String actionUser, OrderOtherModifyInfo otherModify);
	
	/**
	 * 主订单编辑商品
	 * 
	 * @param masterOrderSn 主订单号
	 * @param infoUpdateInfo 订单品数据
	 * @param actionUser 操作人;
	 * @return
	 */
	ReturnInfo editGoodsByMasterSn(String masterOrderSn, OrderInfoUpdateInfo infoUpdateInfo, String actionUser);

	/**
	 * 子订单编辑商品
	 * 
	 * @param orderSn 子订单号
	 * @param infoUpdateInfo 订单品数据
	 * @param actionUser 操作人;
	 * @return
	 */
	ReturnInfo editGoodsByOrderSn(String orderSn, OrderInfoUpdateInfo infoUpdateInfo, String actionUser);

	/**
	 * 主订单编辑订单地址
	 * 
	 * @param masterOrderSn 主订单号
	 * @param consignInfo 客户信息
	 * @return
	 */
	ReturnInfo editConsigneeInfoByMasterSn(String masterOrderSn, ConsigneeModifyInfo consignInfo);

	/**
	 * 发送业务监控服务
	 * @param queueName
	 * @param data
	 */
	void sendMonitorMessage(String queueName, String data);
	
	/**
	 * 重新更新发货接口
	 * @param orderSn
	 */
	void reShipped(String orderSn);
	
	/**
	 * 物流系统返回发货信息处理
	 * @param orderSn 
	 * @param shipProviderList
	 * @param isSystem 是否系统分仓  true:是; false:否
	 */
	ReturnInfo processShip(String orderSn, List<DistributeShipBean> shipProviderList, boolean isSystem);

	/**
	 * 订单自提核销
	 * @param distributeShipBean 订单配送信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> cacWriteOff(DistributeShippingBean distributeShipBean);

	/**
	 * 
	 * @param orderSn 订单号
	 * @param customerCode 11位码
	 * @return
	 */
	ReturnInfo getOrderGoodsByOrderSnAndCustomCode(String orderSn, String customerCode);
	
	/**
	 *  全渠道订单发货14天后更新已收货状态
	 * @param masterOrderSn
	 * @param actionUser
	 * @return ReturnInfo
	 */
	@Deprecated
	ReturnInfo confirmReceipt(String masterOrderSn, String actionUser);

	/**
	 * 处理系统仓库发货
	 * @param distributeShipBean 交货单发货信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> processShip(DistributeShippingBean distributeShipBean);

	/**
	 * 订单锁定
	 * @param orderStatus 订单状态信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> lockOrder(OrderStatus orderStatus);

	/**
	 * 订单解锁
	 * @param orderStatus 订单状态信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> unLockOrder(OrderStatus orderStatus);
	
	/**
	 * 分配单发货确认
	 * @param distributeShipBean 分配单发货信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> distOrderConfirm(DistributeShippingBean distributeShipBean);
	
	/**
	 * 收货确认(按照快递单号签收，快递单号不填写时按照订单签收)
	 * @param bean
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> confirmationOfReceipt(DistributeShippingBean bean);

    /**
     * 主订单编辑发票信息
     *
     * @param consignInfo 客户信息
     * @return
     */
    ReturnInfo<String> editInvInfoByMasterSn(ConsigneeModifyInfo consignInfo);

    /**
     * 主订单编辑发票地址信息
     *
     * @param consignInfo 客户信息
     * @return
     */
    ReturnInfo<String> editInvAddressInfoByMasterSn(ConsigneeModifyInfo consignInfo);
}
