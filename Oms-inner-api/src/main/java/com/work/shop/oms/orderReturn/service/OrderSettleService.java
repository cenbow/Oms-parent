package com.work.shop.oms.orderReturn.service;

import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.vo.SettleBillQueue;
import com.work.shop.oms.vo.SettleParamObj;

/**
 * 结算接口
 * @author cage
 */
public interface OrderSettleService {

    /**
     * 订单-结算
     * 
     * @param orderSn 订单编号
     * @return ReturnInfo
     */
    public ReturnInfo<String> settleNormalOrder(SettleParamObj paramObj);
    
    /**
     * 订单-积分计算
     * 
     * @param orderSn 订单编号
     * @param bussType 0:入库  1：结算
     * @return ReturnInfo
     */
    public ReturnInfo<String> settleOrderPoint(SettleParamObj paramObj);

    /**
     * 订单结算完成
     * @param paramObj
     * @return ReturnInfo<String>
     */
    ReturnInfo<String> masterOrderSettle(SettleParamObj paramObj);
    
    /**
     * 订单结算撤销
     */
    public ReturnInfo<String> OrderSettleCancle(SettleParamObj paramObj);
    /**
     * 退单-结算
     * 
     * @param orderSn 订单编号
     * @param bussType 0:入库  1：结算
     * @return ReturnInfo
     */
    public ReturnInfo<String> settleReturnOrder(SettleParamObj paramObj);
    
    /**
     * 退单结算撤销
     */
    public ReturnInfo<String> returnSettleCancle(SettleParamObj paramObj);
    /**
     * 退单-入库
     * 
     * @param orderSn 订单编号
     * @param bussType 0:入库  1：结算
     * @return ReturnInfo
     */
    public ReturnInfo<String> inputReturnOrder(SettleParamObj paramObj);

    
    /**
     * 订单结算验证
     * @param orderSn
     * @return
     */
    public ReturnInfo<String> checkOrderSettle(String orderSn);
    
    /**
     * 退单入库结算验证
     * @param returnSn
     * @return
     */
    ReturnInfo<String> checkReturnSettle(String returnSn);
    
    /**
     * 接收队列数据-按照结算订单数据(正常订单以及货到付款订单)
     * @param billQueue
     */
    public void callSettleOrder(SettleBillQueue billQueue);
    
    /**
     * 批量结算订单调用方法
     * @param orderSnOrOutSn 订单号/外部交易号
     * @param actionUser
     * @param actionNote
     * @param userId
     * @return
     * @throws Exception
     */
    public ReturnInfo settleOrder(String orderSnOrOutSn,String actionUser,String actionNote,Integer userId);

}
