package com.work.shop.oms.action.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.dao.DistributeActionMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

@Service
public class DistributeActionServiceImpl implements DistributeActionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	DistributeActionMapper distributeActionMapper;

	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	/**
	 * 保存交货单操作日志
	 */
	public void saveOrderAction(DistributeAction orderAction) {
		if (orderAction == null) {
			logger.warn("orderAction is null");
			return;
		}
		
		if (orderAction.getLogTime() == null)
			orderAction.setLogTime(new Date());
		
		if(StringUtil.isEmpty(orderAction.getActionUser())) {
			orderAction.setActionUser("system");
		}
		distributeActionMapper.insert(orderAction);
	}

	/**
	 * 保存交货单操作日志
	 * @param orderSn 订单号
	 * @param message 操作信息
	 */
	public void saveOrderAction(String orderSn, String message) {
		saveOrderAction(orderSn, message, (byte)0);
	}

	public void saveOrderAction(String orderSn, String message, byte payStatus) {
		DistributeAction orderAction = new DistributeAction();
		orderAction.setActionNote(message);
		orderAction.setOrderSn(orderSn);
		orderAction.setOrderStatus((byte)0);
		orderAction.setPayStatus(payStatus);
		orderAction.setQuestionStatus((byte)0);
		orderAction.setRangeStatus((byte)-1);
		orderAction.setShippingStatus((byte)0);
		saveOrderAction(orderAction);
	}

	public DistributeAction createQrderAction(OrderDistribute distribute) {
		DistributeAction orderAction = new DistributeAction();
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
	public DistributeAction addOrderAction(String orderSn, String actionNote) {
		return addOrderAction(orderSn, actionNote,Constant.OS_STRING_SYSTEM);
	}
	@Override
	public DistributeAction addOrderAction(String orderSn, String actionNote,
			String actionUser) {
		OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if(order == null){
			throw new RuntimeException("无法获取有效的订单信息！");
		}
		DistributeAction orderAction = new DistributeAction();
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
	
}
