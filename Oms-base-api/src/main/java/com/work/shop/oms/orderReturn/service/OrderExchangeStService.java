package com.work.shop.oms.orderReturn.service;

import com.work.shop.oms.api.param.bean.ExchangeOrderBean;
import com.work.shop.oms.common.bean.ReturnInfo;


public interface OrderExchangeStService {

    /**
     * 创建换单
     * @param exchangeOrderBean
     * @return
     */
    public ReturnInfo<String> createNewExchangeOrder(ExchangeOrderBean exchangeOrderBean);
    
    /**
     * 作废换单
     * @param orderSn  换单编号
     * @param actionNote 操作备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> cancelExchangeOrder(String orderSn,String actionNote, String actionUser,Integer userId);
    
    /**
     * 换货单只退货
     * @param orderSn  换单编号
     * @param actionUser    操作人
     * @param actionNote    操作备注
     * @return
     */
    public ReturnInfo<String> onlyReturnGoods(String orderSn,String actionUser, String actionNote,Integer userId);
    
}
