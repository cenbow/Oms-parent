package com.work.shop.oms.orderReturn.service;

public interface OrderMonitorService {

    /**
     * 退货单/拒收入库单生成
     * @param orderId
     * @param osOrderSn
     */
    public void monitorForReturnGoods(String returnSn,String osOrderSn);
    
    /**
     * 退货单/拒收入库单作废取消
     * @param orderId
     * @param cancenReason
     */
    public void monitorForReturnCancel(String returnSn,String cancenReason);
    
    /**
     * 退货单/拒收入库单 入库操作
     * @param orderId
     * @param cancenReason
     */
    public void monitorForReturnInput(String returnSn,String channelCode,String customCode,Integer number,String depotCode);
    
    /**
     * 退货单/拒收入库单 入库异常
     * @param orderId
     * @param exception
     */
    public void monitorForReturnInputException(String returnSn,String exception);

    /**
     * 退货单 结算
     * @param orderId
     * @param exception
     */
    public void monitorForReturnSettle(String returnSn, String channelCode,String customCode, Double price,Integer number);
    /**
     * 退货单 结算异常
     * @param orderId
     * @param exception
     */
    public void monitorForReturnSettleException(String returnSn,String exception);

    /**
     * 订单 结算
     * @param orderId
     * @param exception
     */
    public void monitorForOrderSettle(String orderSn,String channelCode,String customCode,Integer number,Double price);
    
    /**
     * 订单 结算异常
     * @param orderId
     * @param exception
     */
    public void monitorForOrderSettleException(String orderSn,String exception);
}
