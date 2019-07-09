package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.api.bean.OrderReturnSkuInfo;
import com.work.shop.oms.common.bean.ReturnInfo;


public interface GoodsReturnChangeService {

    
    /**
     * 退单结算
     * @param orderSn
     * @param orderReturnSkuInfoList
     * @param orderReturnSn
     * @return
     */
    public ReturnInfo<String> goodsReturnChangeClearing(String orderSn,String orderReturnSn,List<OrderReturnSkuInfo> orderReturnSkuInfoList);

    /**
     * 修改申请单状态
     * @param status
     * @param id
     * @param orderSn
     * @param userCode
     * @param actionNote
     * @return
     */
    public ReturnInfo<String> updateStatus(Integer status, Integer id,String orderSn,String userCode,String actionNote);

}
