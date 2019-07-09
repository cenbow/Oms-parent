package com.work.shop.oms.send.service;

import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.request.OrderQueryRequest;

public interface OrderToEcService {

    /**
     * 订单确认后推送百胜E3
     * @param orderQueryRequest
     * @return
     */
    public ServiceReturnInfo<Boolean> sendOrderToEc(OrderQueryRequest orderQueryRequest);
}
