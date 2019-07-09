package com.work.shop.oms.express.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.work.shop.oms.api.express.service.OrderExpressService;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressPullService;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderExpressPullServiceImpl implements OrderExpressPullService{

	private Logger logger = Logger.getLogger(OrderExpressPullServiceImpl.class);
	
	@Resource(name = "orderExpressPullProviderJmsTemplate")
	private JmsTemplate orderExpressJmsTemplate;
	
	@Resource(name = "returnExpressPullProviderJmsTemplate")
	private JmsTemplate returnExpressjmsTemplate;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;

	@Resource
	private OrderExpressService orderExpressService;
	
	@Override
	public ReturnInfo<String> orderExpress(String orderSn) {
		logger.info("orderExpressPullService.orderExpress Start: orderSn=" + orderSn);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			final String msg = orderSn;
			orderExpressJmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(msg);
				}
			});
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		}catch (Exception e) {
			logger.error("orderExpressPullService.orderExpress Start: orderSn=" + orderSn + e.getMessage(), e);
		}
		logger.info("orderExpressPullService.orderExpress End: orderSn=" + orderSn);
		return info;
	}

	@Override
	public ReturnInfo<String> orderReturnExpress(OrderReturn orderReturn,
			OrderReturnShip orderReturnShip) {
		logger.info("orderExpressPullServiceorderReturnExpress Start: orderReturn="
			+ JSON.toJSONString(orderReturnShip) + ";orderReturnShip" + JSON.toJSONString(orderReturnShip));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (orderReturn == null) {
			info.setMessage("参数[orderReturn]不能为空");
			return info;
		}
		if (orderReturnShip == null) {
			info.setMessage("参数[orderReturnShip]不能为空");
			return info;
		}
		try{
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("orderReturn", orderReturn);
			map1.put("orderReturnShip", orderReturnShip);
			final Map<String, Object>  map2=map1;
			returnExpressjmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					String msg = JSON.toJSONString(map2);
					return session.createTextMessage(msg);
				}
			});
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		}catch (Exception e) {
			logger.error("orderExpressPullService.orderExpress :returnSn=" + orderReturn.getReturnSn() + e.getMessage(), e);
		}
		logger.info("orderExpressPullService.orderExpress End: returnSn" + orderReturn.getReturnSn());
		return info;
	}

	@Override
	public ReturnInfo<List<OrderDepotShip>> selectEffectiveShip(String orderSn) {
		ReturnInfo<List<OrderDepotShip>> info = new ReturnInfo<List<OrderDepotShip>>(Constant.OS_NO);
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		distributeExample.or().andMasterOrderSnEqualTo(orderSn)
			.andOrderStatusNotEqualTo(Constant.OD_ORDER_STATUS_CANCLED)
			.andIsDelEqualTo(0);
		List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNull(distributes)) {
			info.setMessage("交货单为空！");
			return info;
		}
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
		depotShipExample.or().andOrderSnIn(orderSns).andIsDelEqualTo(0);
		List<OrderDepotShip> orderShipList = orderDepotShipMapper.selectByExample(depotShipExample);
		info.setMessage("交货单为空！");
		info.setIsOk(Constant.OS_YES);
		info.setData(orderShipList);
		return info;
	}
}
