package com.work.shop.oms.action.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderAction;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnActionExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.dao.OrderActionMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderReturnActionMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderActionServiceImpl implements OrderActionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	OrderActionMapper orderActionMapper;

	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private OrderReturnMapper orderReturnMapper;
	
	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;
	
	@Resource
	private OrderReturnActionMapper orderReturnActionMapper;


	@Override
	public void saveOrderAction(OrderAction orderAction) {
		if (orderAction == null) {
			logger.warn("orderAction is null");
			return;
		}
		if (orderAction.getLogTime() == null)
			orderAction.setLogTime(new Date());
		if(StringUtil.isEmpty(orderAction.getActionUser())) {
			orderAction.setActionUser("system");
		}
		orderActionMapper.insert(orderAction);
	}

	@Override
	public void saveOrderAction(String orderSn, String message) {
		saveOrderAction(orderSn,message,(byte)0);
	}

	@Override
	public void saveOrderAction(String orderSn, String message, byte payStatus) {
		OrderAction orderAction = new OrderAction();
		orderAction.setActionNote(message);
		orderAction.setOrderSn(orderSn);
		orderAction.setOrderStatus((byte)0);
		orderAction.setPayStatus(payStatus);
		orderAction.setQuestionStatus((byte)0);
		orderAction.setRangeStatus((byte)-1);
		orderAction.setShippingStatus((byte)0);
		saveOrderAction(orderAction);
	}

	@Override
	public OrderAction createQrderAction(OrderDistribute distribute) {
		OrderAction orderAction = new OrderAction();
		orderAction.setOrderSn(distribute.getOrderSn());
		orderAction.setActionUser(Constant.OS_STRING_SYSTEM);
		orderAction.setOrderStatus(distribute.getOrderStatus());
		orderAction.setShippingStatus(distribute.getShipStatus());
		orderAction.setPayStatus(distribute.getPayStatus());
		orderAction.setRangeStatus((byte) -1);
		orderAction.setQuestionStatus(distribute.getQuestionStatus().byteValue());
		orderAction.setLogTime(new Date());
		return orderAction;
	}
	
	@Override
	public void saveOrderPayLog(MasterOrderPay orderPay,
			java.math.BigDecimal bonus, String payCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public OrderAction addOrderAction(String orderSn, String actionNote) {
		return addOrderAction(orderSn, actionNote,Constant.OS_STRING_SYSTEM);
	}
	@Override
	public OrderAction addOrderAction(String orderSn, String actionNote,
			String actionUser) {
		OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if(order == null){
			throw new RuntimeException("无法获取有效的订单信息！");
		}
		OrderAction orderAction = new OrderAction();
		orderAction.setOrderSn(order.getOrderSn());
		orderAction.setOrderStatus(order.getOrderStatus());
		orderAction.setShippingStatus(order.getShipStatus());
		orderAction.setPayStatus(order.getPayStatus());
		orderAction.setQuestionStatus(order.getQuestionStatus().byteValue());
		orderAction.setActionUser(actionUser);
		orderAction.setActionNote(actionNote);
		orderAction.setLogTime(new Date());
		orderAction.setRangeStatus((byte)-1);
		saveOrderAction(orderAction);
		return orderAction;
	}

	@Override
    public OrderReturnAction addOrderReturnAction(String returnSn,
            String actionNote) {
        return addOrderReturnAction(returnSn, actionNote, Constant.OS_STRING_SYSTEM);
    }
    @Override
    public OrderReturnAction addOrderReturnAction(String returnSn, String actionNote, String actionUser)  {
        return addOrderReturnAction(returnSn, actionNote, actionUser, null);
    }

    @Override
    public OrderReturnAction addOrderReturnAction(String returnSn,String actionNote,String actionUser, Integer returnOrderStatus)  {
        OrderReturnAction action = new OrderReturnAction();
        action.setReturnSn(returnSn);
        action.setActionNote(actionNote);
        action.setActionUser(actionUser);
        action.setReturnOrderStatus(returnOrderStatus);
        return addOrderReturnAction(action);
    }

    /**
     * 添加退单日志
     * @param actionRequest
     * @return
     */
    @Override
    public OrderReturnAction addOrderReturnAction(OrderReturnAction actionRequest) {
        String returnSn = actionRequest.getReturnSn();
        OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
        if(orderReturn == null){
            throw new RuntimeException("无法获取有效的退单信息！");
        }
        OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
        if(orderReturnShip == null){
            throw new RuntimeException("无法获取有效的退单发货信息！");
        }
        // 设定要保存的退单操作日志信息
        OrderReturnAction orderReturnAction = new OrderReturnAction();
        orderReturnAction.setReturnSn(returnSn);

        Integer returnOrderStatus = actionRequest.getReturnOrderStatus();
        if (returnOrderStatus == null) {
            returnOrderStatus = orderReturn.getReturnOrderStatus().intValue();
        }
        orderReturnAction.setReturnOrderStatus(returnOrderStatus);
        orderReturnAction.setReturnShippingStatus(orderReturn.getShipStatus().intValue());
        orderReturnAction.setReturnPayStatus(orderReturn.getPayStatus().intValue());
        orderReturnAction.setIsGoodReceived(orderReturnShip.getIsGoodReceived());
        orderReturnAction.setQualityStatus(orderReturnShip.getQualityStatus());
        orderReturnAction.setCheckinStatus(orderReturnShip.getCheckinStatus());
        orderReturnAction.setActionUser(actionRequest.getActionUser());
        orderReturnAction.setActionNote(actionRequest.getActionNote());
        if (actionRequest.getLogType() != null) {
            orderReturnAction.setLogType(actionRequest.getLogType());
        }

        //orderReturnAction.setLogTime(new Date());
        orderReturnActionMapper.insertSelective(orderReturnAction);
        OrderReturnAction returnAction = new OrderReturnAction();
        try {
            /*BeanUtils.copyProperties(returnAction, orderReturnAction);*/
            returnAction.setActionId(orderReturnAction.getActionId());
            returnAction.setReturnSn(orderReturnAction.getReturnSn());
            returnAction.setActionUser(orderReturnAction.getActionUser());
            returnAction.setReturnOrderStatus(returnOrderStatus);
            returnAction.setReturnShippingStatus(orderReturnAction.getReturnShippingStatus());
            returnAction.setReturnPayStatus(orderReturnAction.getReturnPayStatus());
            returnAction.setIsGoodReceived(orderReturnAction.getIsGoodReceived());
            returnAction.setQualityStatus(orderReturnAction.getQualityStatus());
            returnAction.setCheckinStatus(orderReturnAction.getCheckinStatus());
            returnAction.setActionNote(orderReturnAction.getActionNote());
            returnAction.setLogTime(orderReturnAction.getLogTime());
            returnAction.setLogType(orderReturnAction.getLogType());
        } catch (Exception e) {
            logger.error("BeanUtils复制属性出错！",e);
        }
        return returnAction;
    }
    
    @Override
    public List<OrderReturnAction> getOrderReturnActionList(
            OrderReturnAction model)  {
        OrderReturnActionExample actionExample = new OrderReturnActionExample();
        if (StringUtil.isNotEmpty(model.getReturnSn())) {
            actionExample.or().andReturnSnEqualTo(model.getReturnSn());
            actionExample.setOrderByClause("log_time desc");
        }
        return this.orderReturnActionMapper.selectByExample(actionExample);
    }
}
