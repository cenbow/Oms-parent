package com.work.shop.oms.orderReturn.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.bean.ReturnForward;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.vo.ReturnOrderParam;
import com.work.shop.oms.vo.SettleBillQueue;


public interface OrderReturnStService {
    
    /**
     * 退单入库
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @param userId 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderStorage(ReturnOrderParam returnOrderParam);
//  public ReturnInfo returnOrderStorage(String returnSn,String actionNote,String actionUser,Integer userId);
    /**
     * 退单入库撤销
     */
    public ReturnInfo<String> returnStorageCancle(String returnSn,String userName,String storageTimeStamp);
    
    /**
     * 退单结算
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderSettle(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单确认 + 确认后自动追单
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderConfirm(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单未确认
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderUnConfirm(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单已收货
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderReceive(ReturnOrderParam returnOrderParam);
    
    /**
     * 退单未收货
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderUnReceive(ReturnOrderParam returnOrderParam);
    
    /**
     * 退单质检通过
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderPass(ReturnOrderParam returnOrderParam);
    
    /**
     * 退单质检不通过
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderUnPass(ReturnOrderParam returnOrderParam);
 
    /**
     * 退单作废
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderInvalid(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单沟通
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderCommunicate(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单回退客服
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderBackToCs(String returnSn,String actionNote,String actionUser);

    /**
     * 退单复活
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderRevive(String returnSn,String actionNote,String actionUser);
    
    /**
     * 退单锁定
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @param userId 操作人ID
     * @return
     */
    public ReturnInfo<String> returnOrderLock(String returnSn,String actionNote,String actionUser,Integer userId);
    
    /**
     * 退单解锁
     * @param returnSn 退单号
     * @param actionNote 退单备注
     * @param actionUser 操作人
     * @param userId 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderUnLock(String returnSn,String actionNote,String actionUser,Integer userId);
    
    /**
     * 按照退单的付款方式进行单条结算
     * @param returnSn 退单号
     * @param payId 退款方式
     * @param returnMoney 当前退款方式结算金额
     * @param actionUser 操作人
     * @return
     */
    public ReturnInfo<String> returnOrderShareSettle(String returnSn,Integer payId,Double returnMoney,String actionUser);
    
    
    /**
     * 接收队列数据-按照退款方式结算退单数据
     * @param billQueue
     */
    public void callSettleReturnSharePay(SettleBillQueue billQueue);
    
    public void addReturnOrderAction(String returnSn,String note,String actionUser);
    
    
    public void updateDeposit(SettleBillQueue billQueue);
    
    public void updateOrderInfoOrOrderReturnLog(SettleBillQueue billQueue);

    public List<OrderReturnAction> getOrderReturnActionList(OrderReturnAction model);
    
    public Map<String,Object> returnForward(ReturnForward returnForward,String type,String userId);
}
