package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.ShippingInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderManagementResponse;

import java.util.List;

/**
 * 订单管理
 * 
 * @author lemon
 *
 */
public interface OrderManagementService {
	
	/**
	 * 订单详情查询
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderItemGet(OrderManagementRequest request);
	
	/**
	 * 沟通
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse communicate(OrderManagementRequest request);

	/**
	 * 查询订单日志
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse getOrderItemLog(OrderManagementRequest request);
	
	/**
	 * 自提核销
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse gotWriteOff(OrderManagementRequest request);
	
	/**
	 * 订单拣货
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderDistributePicking(OrderManagementRequest request);
	
	/**
	 * 订单拣货完成
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderDistributePickUp(OrderManagementRequest request);

	/**
	 * 订单取消通知接收
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderDistributeCancel(OrderManagementRequest request);

	/**
	 * 承运商列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<List<ShippingInfo>>
	 */
	OmsBaseResponse<List<ShippingInfo>> getSystemShipping(OmsBaseRequest<ShippingInfo> request);

	/**
	 * 发送自提码短信
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse sendGotCode(OrderManagementRequest request);
	
	/**
	 * 订单结算
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderSettlement(OrderManagementRequest request);
	
	/**
	 * 订单正常单
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderNormal(OrderManagementRequest request);
	
	/**
	 * 订单问题单
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderQuestion(OrderManagementRequest request);
	
	/**
	 * 订单未付款
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderUnPay(OrderManagementRequest request);
	
	/**
	 * 订单已付款
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderPay(OrderManagementRequest request);
	
	/**
	 * 订单确认
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderConfirm(OrderManagementRequest request);
	
	/**
	 * 订单未确认
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderUnConfirm(OrderManagementRequest request);
	
	/**
	 * 订单锁定
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderLock(OrderManagementRequest request);
	
	/**
	 * 订单解锁
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderUnLock(OrderManagementRequest request);
	
	/**
	 * 订单审单完成
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderReviewCompleted(OrderManagementRequest request);
	
	/**
	 * 订单审单驳回
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderReviewReject(OrderManagementRequest request);

	/**
	 * 账期支付扣款成功
	 * @param request
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderPayPeriodSuccess(OrderManagementRequest request);

	/**
	 * 账期支付扣款失败
	 * @param request
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderPayPeriodError(OrderManagementRequest request);

	/**
	 * 合同签章完成
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse orderSignCompleted(OrderManagementRequest request);

	/**
	 * 订单结算账户完成
	 * @param request
	 * @return
	 */
	OrderManagementResponse orderSettlementAccountCompleted(OrderManagementRequest request);

	/**
	 * 订单下发供应商采购单
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	OrderManagementResponse sendPurchaseOrder(OrderManagementRequest request);

    /**
     * 订单设置签章
     * @param request
     * @return
     */
    OrderManagementResponse setSignByOrder(OrderManagementRequest request);

	/**
	 * 团购订单成功处理
	 * @param request
	 * @return
	 */
	OrderManagementResponse groupBuySuccess(OrderManagementRequest request);
}
