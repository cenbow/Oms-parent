package com.work.shop.oms.distribute.service;

import java.util.List;

import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.OrderDepotResult;

/**
 * 生成子订单服务
 * @author lemon
 *
 */
public interface OrderDistributeService {

	/**
	 * 创建子订单服务
	 * @param masterOrderSn
	 * @return OrderDepotResult
	 */
	OrderDepotResult orderDistribute(String masterOrderSn);

	/**
	 * 获取主订单下有效交货单
	 * 
	 * @param masterOrderSn
	 * @return
	 */
	List<OrderDistribute> selectEffectiveDistributes(String masterOrderSn);

    /**
     * 根据交货单号获取交货单信息
     * @param orderSn
     * @return
     */
    OrderDistribute selectDistributesByOrderSn(String orderSn);

	/**
	 * 获取主订单下有效分仓发货单
	 * 
	 * @param orderSn
	 * @return
	 */
	List<OrderDepotShip> selectEffectiveShips(String orderSn);
	
	/**
	 * 订单数据下发XOMS系统
	 * 
	 * @param masterOrderSn
	 * @return
	 */
	OrderDepotResult pushXoms(String masterOrderSn);
}
