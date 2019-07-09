package com.work.shop.oms.order.service;

import com.work.shop.oms.common.bean.PayReturnInfo;
import com.work.shop.oms.exception.OrderException;


public interface OrderPayStChService {
    
    /**
     * 后台订单已付款操作
     * @param orderSn 订单编号
     * @param paySn 付款单编号
     * @param actionNote 备注
     * @param actionUser 操作人
     * @param userId
     * @return 变更结果
     * @throws OrderException
     */
    PayReturnInfo payStCh(String orderSn, String paySn, String actionNote, String actionUser, Integer userId) throws OrderException;
    

}
