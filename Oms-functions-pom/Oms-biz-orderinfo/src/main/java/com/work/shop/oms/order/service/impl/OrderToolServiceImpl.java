package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.order.service.OrderToolService;
import com.work.shop.oms.orderget.service.ChannelManagerService;
@Service
public class OrderToolServiceImpl implements  OrderToolService{
	@Resource
	MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	OrderDistributeMapper orderDistributeMapper;
	@Resource
	OrderDepotShipMapper OrderDepotShipMapper;

	//@Resource
	ChannelManagerService channelManagerService;
	
	@Override
	public String orderTool(String masterOrderSn) {
		MasterOrderInfo masterOrderInfo =masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		masterOrderInfo.setMasterOrderSn(masterOrderSn+"X");
		masterOrderInfo.setOrderStatus((byte)1);
		masterOrderInfo.setPayStatus((byte)2);
		masterOrderInfoMapper.insertSelective(masterOrderInfo);
		
		MasterOrderGoodsExample masterOrderGoodsExample = new MasterOrderGoodsExample();
		masterOrderGoodsExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderGoods> MasterOrderGoodsList =masterOrderGoodsMapper.selectByExample(masterOrderGoodsExample);
		for(MasterOrderGoods masterOrderGoods:MasterOrderGoodsList){
			masterOrderGoods.setId(null);
			masterOrderGoods.setMasterOrderSn(masterOrderSn+"X");
			masterOrderGoods.setOrderSn(masterOrderGoods.getOrderSn()+"X");
			double a=channelManagerService.getOrderGoodsByTidAndSku(Long.parseLong(masterOrderInfo.getOrderOutSn()), masterOrderGoods.getCustomCode());
			masterOrderGoods.setTransactionPrice(new BigDecimal(a));
			masterOrderGoods.setSettlementPrice(new BigDecimal(a));
			masterOrderGoods.setDiscount(masterOrderGoods.getGoodsPrice().subtract(new BigDecimal(a)));
			masterOrderGoodsMapper.insertSelective(masterOrderGoods);
		}
		
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
		for(MasterOrderPay masterOrderPay:masterOrderPayList){
			masterOrderPay.setMasterOrderSn(masterOrderSn+"X");
			masterOrderPay.setMasterPaySn(masterOrderPay.getMasterPaySn()+"X");
			masterOrderPay.setPayStatus((byte)2);
			masterOrderPayMapper.insertSelective(masterOrderPay);
		}
		
		MasterOrderAddressInfo masterOrderAddressInfo =masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
		masterOrderAddressInfo.setMasterOrderSn(masterOrderSn+"X");
		masterOrderAddressInfoMapper.insertSelective(masterOrderAddressInfo);
		
		Set<String> orderSnSet = new HashSet<String>();
		OrderDistributeExample orderDistributeExample = new OrderDistributeExample();
		orderDistributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<OrderDistribute> orderDistributeList = orderDistributeMapper.selectByExample(orderDistributeExample);
		for(OrderDistribute orderDistribute:orderDistributeList){
			orderSnSet.add(orderDistribute.getOrderSn());
			orderDistribute.setOrderStatus((byte)1);
			orderDistribute.setPayStatus((byte)2);
			orderDistribute.setMasterOrderSn(masterOrderSn+"X");
			orderDistribute.setOrderSn(orderDistribute.getOrderSn()+"X");
			orderDistributeMapper.insertSelective(orderDistribute);
		}
		
		OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
		orderDepotShipExample.or().andOrderSnIn(new ArrayList<String>(orderSnSet));
		List<OrderDepotShip> orderDepotShipList = OrderDepotShipMapper.selectByExample(orderDepotShipExample);
		for(OrderDepotShip orderDepotShip:orderDepotShipList){
			orderDepotShip.setOrderSn(orderDepotShip.getOrderSn()+"X");
			OrderDepotShipMapper.insertSelective(orderDepotShip);
		}
		
		return masterOrderSn+"X";
	}

}
