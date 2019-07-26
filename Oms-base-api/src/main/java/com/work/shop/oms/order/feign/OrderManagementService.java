package com.work.shop.oms.order.feign;

import com.work.shop.oms.common.bean.ShippingInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderManagementResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 订单管理
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface OrderManagementService {

    /**
     * 订单详情查询
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderItemGet")
    OrderManagementResponse orderItemGet(OrderManagementRequest request);

    /**
     * 沟通
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/communicate")
    OrderManagementResponse communicate(OrderManagementRequest request);

    /**
     * 查询订单日志
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/getOrderItemLog")
    OrderManagementResponse getOrderItemLog(OrderManagementRequest request);

    /**
     * 自提核销
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/gotWriteOff")
    OrderManagementResponse gotWriteOff(OrderManagementRequest request);

    /**
     * 订单拣货
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderDistributePicking")
    OrderManagementResponse orderDistributePicking(OrderManagementRequest request);

    /**
     * 订单拣货完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderDistributePickUp")
    OrderManagementResponse orderDistributePickUp(OrderManagementRequest request);

    /**
     * 订单取消通知接收
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderDistributeCancel")
    OrderManagementResponse orderDistributeCancel(OrderManagementRequest request);

    /**
     * 承运商列表
     * @param request 请求参数
     * @return OmsBaseResponse<List<ShippingInfo>>
     */
    @PostMapping("/order/getSystemShipping")
    OmsBaseResponse<List<ShippingInfo>> getSystemShipping(OmsBaseRequest<ShippingInfo> request);

    /**
     * 发送自提码短信
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/sendGotCode")
    OrderManagementResponse sendGotCode(OrderManagementRequest request);

    /**
     * 订单结算
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderSettlement")
    OrderManagementResponse orderSettlement(OrderManagementRequest request);

    /**
     * 订单正常单
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderNormal")
    OrderManagementResponse orderNormal(OrderManagementRequest request);

    /**
     * 订单问题单
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderQuestion")
    OrderManagementResponse orderQuestion(OrderManagementRequest request);

    /**
     * 订单未付款
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderUnPay")
    OrderManagementResponse orderUnPay(OrderManagementRequest request);

    /**
     * 订单已付款
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderPay")
    OrderManagementResponse orderPay(OrderManagementRequest request);
    
    /**
     * 订单确认
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderConfirm")
    OrderManagementResponse orderConfirm(OrderManagementRequest request);
    
    /**
     * 订单未确认
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderUnConfirm")
    OrderManagementResponse orderUnConfirm(OrderManagementRequest request);
    
    /**
     * 订单锁定
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderLock")
    OrderManagementResponse orderLock(OrderManagementRequest request);
    
    /**
     * 订单解锁
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderUnLock")
    OrderManagementResponse orderUnLock(OrderManagementRequest request);
    
    /**
     * 订单审单完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderReviewCompleted")
    OrderManagementResponse orderReviewCompleted(OrderManagementRequest request);
    
    /**
     * 订单审单驳回
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderReviewReject")
    OrderManagementResponse orderReviewReject(OrderManagementRequest request);

    /**
     * 账期支付扣款成功
     * @param request
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderPayPeriodSuccess")
    OrderManagementResponse orderPayPeriodSuccess(OrderManagementRequest request);

    /**
     * 账期支付扣款失败
     * @param request
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderPayPeriodError")
    OrderManagementResponse orderPayPeriodError(OrderManagementRequest request);

    /**
     * 合同签章完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/order/orderSignCompleted")
    OrderManagementResponse orderSignCompleted(OrderManagementRequest request);
}
