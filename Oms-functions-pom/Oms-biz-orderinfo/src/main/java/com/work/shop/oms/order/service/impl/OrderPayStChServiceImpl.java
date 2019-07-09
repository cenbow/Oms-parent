package com.work.shop.oms.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.common.bean.PayReturnInfo;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.exception.OrderException;
import com.work.shop.oms.order.service.OrderPayStChService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderPayStChServiceImpl implements OrderPayStChService {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MasterOrderInfoMapper masterOrderInfoMapper;
    @Resource
    private OrderReturnMapper orderReturnMapper;
    @Resource
    private MasterOrderPayMapper masterOrderPayMapper;
    @Resource
    private OrderDepotShipMapper orderDepotShipMapper;
    @Resource
    private OrderConfirmService orderConfirmService;

    /**
     * 后台订单已付款操作
     * @param orderSn 订单编号
     * @param paySn 付款单编号
     * @param actionNote 备注
     * @param actionUser 操作人
     * @return 变更结果
     * @throws OrderException
     */
    @Override
    public PayReturnInfo payStCh(String orderSn, String paySn, String actionNote, String actionUser,
            Integer userId) throws OrderException {
        logger.info("payStCh : orderSn=" + orderSn + "; paySn=" + paySn + ";actionNote="
            + actionNote + ";actionUser=" + actionUser);
        PayReturnInfo pri = new PayReturnInfo();
        pri.setIsOk(Constant.OS_NO);
        if(orderSn == null || orderSn.trim().isEmpty()) {
            pri.setMessage("payStCh： 传入订单编号参数为空！");
            return pri;
        }
        if(paySn == null || paySn.trim().isEmpty()) {
            pri.setMessage("payStCh： 传入付款单编号参数为空！");
            return pri;
        }
        MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
       /* 执行前提检查
        ReturnInfo checkRI = this.checkConditionOfPayExecution(orderInfo, orderSn, paySn);
        if(checkRI != null) {
            pri.setMessage(checkRI.getMessage());
            return pri;
        }*/
        MasterOrderPay orderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
        if(orderPay == null) {
            pri.setMessage("payStCh： 付款单"+ paySn +"不存在，不能进行付款操作！");
            return pri;
        }
        if(orderPay.getPayStatus() == Constant.OP_PAY_STATUS_PAYED || orderPay.getPayStatus() == Constant.OP_PAY_STATUS_SETTLED) {
            pri.setMessage("payStCh： 付款单"+ paySn +"已付款,不能进行付款操作！");
            return pri;
        }
        if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
            pri.setMessage(" 订单" + orderSn + "要处于未确定状态");
            return pri;
        }
        OrderDepotShipExample shipExample = new OrderDepotShipExample();
        shipExample.or().andOrderSnEqualTo(orderSn);
        List<OrderDepotShip> ships = orderDepotShipMapper.selectByExample(shipExample);
        if(!StringUtil.isNotNullForList(ships)) {
            pri.setMessage("payStCh： 订单"+ orderSn+ "的发货单不存在，不能进行付款操作！");
            return pri;
        }
        // 判断是否被非admin的当前人锁定
        /* if (actionUser != null && !actionUser.trim().equals("admin") 
                && !StringUtils.equalsIgnoreCase(ConstantValues.ACTION_USER_SYSTEM, actionUser)) {
            String judgeMsg = BrandUtil.judgeSelfLock(orderInfo.getLockStatus(), userId);
            if (StringUtil.isNotEmpty(judgeMsg)) {
                pri.setMessage("payStCh:" + orderSn + "不能进行订单已付款操作！失败原因:" + judgeMsg);
                return pri;
            }
        }
        String payNote = orderPay.getPayNote();
        if(payNote.trim().isEmpty()) {
            payNote = actionNote;
        } else{
            payNote = payNote+"; "+actionNote;
        }
        //对于平台的订单。付款时把付款备注中的数字放入外部交易号中。作为外部交易号
        if (StringUtil.isNotEmpty(payNote) && brandUtil.getChannelCode(orderInfo.getOrderFrom()).equals(Constant.BG_CHANNEL_CODE)) {
            List<String> s = new ArrayList<String>();
            String regex = "\\d*";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(payNote);
            while (m.find()) {
                if (!"".equals(m.group()))
                    s.add(m.group());
            }
            if (s.size() > 0) {
                if(orderPay.getPayId() == 7) { //仅仅是快钱方式支付时
                    orderInfo.setOrderOutSn("FK"+s.get(0));
                } else {
                    orderInfo.setOrderOutSn(s.get(0));
                }
            }
        }
        orderPay.setPayNote(payNote);
        OrderInfo txOInfo = new OrderInfo();
        // 更新前，将orderInfo内容复制给txOInfo，用于异常时的日志记录
        try {
            PropertyUtils.copyProperties(txOInfo, orderInfo);
        } catch (Exception e) {
            txOInfo = orderInfo;
            logger.error("复制OrderInfo信息异常"+ e.getMessage());
        }
        // 订单付款操作
        try {
            pri = this.payStChObject(orderInfo, orderPay, ships, actionNote, actionUser, false);
        } catch (Exception e) {
            // 订单付款操作异常处理
            logger.error("已付款："+ e.getMessage());
            pri.setMessage("已付款："+ e.getMessage());
            // 记录操作日志异常信息
            OrderAction orderAction = orderActionService.createQrderAction(txOInfo);
            orderAction.setActionUser(actionUser);
            orderAction.setActionNote("<font style=color:red;>已付款：异常信息 "+ e.getMessage() + "</font>");
            orderActionService.saveOrderAction(orderAction);
        }
        if(pri.getIsOk() == Constant.OS_YES) {
            //换单确认
            if(orderInfo.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER
                    && orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
                //this.exchangeOrderInfoDoConfirm(orderInfo, ships.get(0), actionUser);
                orderConfirmService.confirmOrderForOM(orderSn, null,"订单付款，自动确认", Constant.OS_STRING_SYSTEM, userId);
            }
            //订单确认
            else if (brandUtil.getChannelCode(orderInfo.getOrderFrom()).equals(Constant.BG_CHANNEL_CODE)) {
                if (orderInfo.getTransType() == Constant.OI_TRANS_TYPE_PREPAY 
                        && orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
                    if (orderInfo.getTotalFee().doubleValue() < 5000 && orderInfo.getGoodsCount() < 20) {
                        //this.exchangeOrderInfoDoConfirm(orderInfo, ships.get(0), actionUser);
                        orderConfirmService.confirmOrderForOM(orderSn, null,"订单付款，自动确认", Constant.OS_STRING_SYSTEM, userId);
                    }
                }
            }
        } else {
            throw new OrderException(pri.getMessage());
        }*/
        pri.setIsOk(Constant.OS_YES);
        pri.setMessage("订单已付款操作成功！");
        return pri;

    }

    

}
