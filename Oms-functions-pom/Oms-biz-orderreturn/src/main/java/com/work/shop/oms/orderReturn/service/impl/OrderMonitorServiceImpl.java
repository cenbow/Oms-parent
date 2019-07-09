package com.work.shop.oms.orderReturn.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.orderReturn.service.OrderMonitorService;
import com.work.shop.oms.param.bean.OrderMonitor;

@Service
public class OrderMonitorServiceImpl implements OrderMonitorService {

    private static Logger logger = Logger.getLogger(OrderMonitorServiceImpl.class);

    @Override
    public void monitorForReturnGoods(String returnSn, String osOrderSn) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setOrderId(osOrderSn);
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitor.setCurrentTime(formatDate.format(new Date()));
        } catch (Exception e) {
            logger.error("monitorForReturnGoods.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForReturnCancel(String returnSn, String cancenReason) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setCancelReason(cancenReason);
        } catch (Exception e) {
            logger.error("monitorForReturnCancel.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForReturnInput(String returnSn, String channelCode,
            String customCode, Integer number, String depotCode) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setUnitId(channelCode);
            monitor.setProdId(customCode);
            monitor.setOrderQty(number);
            monitor.setReturnWareId(depotCode);
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitor.setCurrentTime(formatDate.format(new Date()));
        } catch (Exception e) {
            logger.error("monitorForReturnInput.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForReturnInputException(String returnSn, String exception) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setEntryException(exception);
        } catch (Exception e) {
            logger.error("monitorForReturnInputException.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForReturnSettle(String returnSn, String channelCode,
            String customCode, Double price,Integer number) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setProdId(customCode);
            monitor.setPrice(price);
            monitor.setOrderQty(number);
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitor.setCurrentTime(formatDate.format(new Date()));
            
        } catch (Exception e) {
            logger.error("monitorForReturnSettle.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForReturnSettleException(String returnSn,
            String exception) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setReturnId(returnSn);
            monitor.setReturnSettleException(exception);
            
        } catch (Exception e) {
            logger.error("monitorForReturnSettleException.returnSn:" + returnSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForOrderSettle(String orderSn, String channelCode,
            String customCode, Integer number, Double price) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setOrderId(orderSn);
            monitor.setUnitId(channelCode);
            monitor.setProdId(customCode);
            monitor.setPrice(price);
            monitor.setOrderQty(number);
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitor.setCurrentTime(formatDate.format(new Date()));
            
        } catch (Exception e) {
            logger.error("monitorForOrderSettle.orderSn:" + orderSn + ",message:"+e.getMessage(),e);
        }
    }

    @Override
    public void monitorForOrderSettleException(String orderSn, String exception) {
        try {
            OrderMonitor monitor = new OrderMonitor();
            monitor.setOrderId(orderSn);
            monitor.setOrderSettleException(exception);
            
        } catch (Exception e) {
            logger.error("monitorForOrderSettleException.orderSn:" + orderSn + ",message:"+e.getMessage(),e);
        }
    }

}
