package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 订单商品服务
 * @author QuYachu
 */
public interface MasterOrderGoodsService {

	/**
	 * 保存订单商品
	 * @param masterOrderSn 订单编码
	 * @param masterOrder 订单参数信息
	 * @param masterOrderInfo 订单信息
	 * @return ReturnInfo<List<MasterOrderGoods>>
	 */
	ReturnInfo<List<MasterOrderGoods>> insertMasterOrderGoods(String masterOrderSn, MasterOrder masterOrder, MasterOrderInfo masterOrderInfo);

	/**
	 * 使用主订单号查询订单商品
	 * @param masterOrderSn
	 * @return
	 */
	List<MasterOrderGoods> selectByMasterOrderSn(String masterOrderSn);

	/**
	 * 使用子订单号查询订单商品
	 * @param orderSn
	 * @return
	 */
	List<MasterOrderGoods> selectByOrderSn(String orderSn);
}
