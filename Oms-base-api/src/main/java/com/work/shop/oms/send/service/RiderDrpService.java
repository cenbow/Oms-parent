package com.work.shop.oms.send.service;

import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.order.request.OrderQueryRequest;

/**
 * 推送drp
 * @author YeQingchao
 */
public interface RiderDrpService {
    /**
     * 配送成功信息推送drp
     * @param request
     * @return
     */
    public ServiceReturnInfo<Boolean> sendDrpByDeliverySuccess(OrderQueryRequest request);
}
