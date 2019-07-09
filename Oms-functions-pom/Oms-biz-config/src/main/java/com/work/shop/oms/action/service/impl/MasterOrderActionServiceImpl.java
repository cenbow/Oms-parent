package com.work.shop.oms.action.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单日志服务
 * @author QuYachu
 */
@Service
public class MasterOrderActionServiceImpl implements MasterOrderActionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	/**
	 * 保存订单确认日志
	 * @param masterOrderAction 订单日志
	 */
	@Override
	public void insertOrderActionByObj(MasterOrderAction masterOrderAction) {
		if (masterOrderAction == null) {
			logger.warn("orderAction is null");
			return;
		}
		if (masterOrderAction.getLogTime() == null) {
			masterOrderAction.setLogTime(new Date());
		}
		if (StringUtil.isEmpty(masterOrderAction.getActionUser())) {
			masterOrderAction.setActionUser(Constant.OS_STRING_SYSTEM);
		}
		masterOrderActionMapper.insertSelective(masterOrderAction);
	}

	/**
	 * 保存订单日志
	 * @param masterOrderSn 订单编码
	 * @param message 日志信息
	 * @param actionUser 操作人
	 */
	@Override
	public void insertOrderActionBySn(String masterOrderSn, String message, String actionUser) {
        insertOrderActionBySn(masterOrderSn, message, actionUser, 0);
	}

    /**
     * 保存订单日志
     * @param masterOrderSn 订单编码
     * @param message 日志信息
     * @param actionUser 操作人
     * @param logType 日志类型：0为订单操作，1为沟通
     */
    @Override
    public void insertOrderActionBySn(String masterOrderSn, String message, String actionUser, Integer logType) {
        MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
        if (masterOrderInfo == null) {
            throw new RuntimeException("无法获取有效的订单信息！");
        }
        MasterOrderAction orderAction = new MasterOrderAction();
        orderAction.setActionNote(message);
        orderAction.setMasterOrderSn(masterOrderSn);
        orderAction.setOrderStatus(masterOrderInfo.getOrderStatus());
        orderAction.setPayStatus(masterOrderInfo.getPayStatus());
        orderAction.setShippingStatus(masterOrderInfo.getShipStatus());
        orderAction.setLogTime(new Date());
        if (StringUtil.isEmpty(actionUser)) {
            orderAction.setActionUser(Constant.OS_STRING_SYSTEM);
        } else {
            orderAction.setActionUser(actionUser);
        }
        if (logType == null) {
            orderAction.setLogType((byte) 0);
        } else {
            orderAction.setLogType(logType.byteValue());
        }
        masterOrderActionMapper.insertSelective(orderAction);
    }

	/**
	 * 创建订单日志
	 * @param masterOrderInfo 订单信息
	 * @return MasterOrderAction
	 */
	@Override
	public MasterOrderAction createOrderAction(MasterOrderInfo masterOrderInfo) {
		MasterOrderAction orderAction = new MasterOrderAction();
		// 订单编码
		orderAction.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
		// 订单状态
		orderAction.setOrderStatus(masterOrderInfo.getOrderStatus());
		// 订单支付状态
		orderAction.setPayStatus(masterOrderInfo.getPayStatus());
		// 订单发货状态
		orderAction.setShippingStatus(masterOrderInfo.getShipStatus());
		orderAction.setLogTime(new Date());
		orderAction.setActionUser(Constant.OS_STRING_SYSTEM);
		return orderAction;
	}

	/**
	 * 新增订单日志
	 * @param masterOrderSn 订单编码
	 * @param message 信息
	 * @param payStatus 支付状态
	 */
	@Override
	public void insertOrderAction(String masterOrderSn, String message, byte payStatus) {
		MasterOrderAction orderAction = new MasterOrderAction();
		orderAction.setActionNote(message);
		orderAction.setMasterOrderSn(masterOrderSn);
		orderAction.setOrderStatus((byte)0);
		orderAction.setPayStatus(payStatus);
		orderAction.setShippingStatus((byte)0);
		orderAction.setLogTime(new Date());
		orderAction.setActionUser(Constant.OS_STRING_SYSTEM);
		masterOrderActionMapper.insertSelective(orderAction);
	}

}
