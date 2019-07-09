package com.work.shop.oms.order.feign;

import com.work.shop.oms.bean.OrderPeriodInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 订单周期配置服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface OrderConfigService {

    /**
     * 获取订单周期明细列表
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/order/getOrderPeriodInfoList")
    OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfoList();

    /**
     * 获取订单周期明细
     * @param request 参数
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/order/getOrderPeriodInfo")
    OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request);

    /**
     * 更新订单周期明细
     * @param request 参数
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/order/updateOrderPeriodInfo")
    OmsBaseResponse<OrderPeriodInfo> updateOrderPeriodInfo(OmsBaseRequest<OrderPeriodInfo> request);
}
