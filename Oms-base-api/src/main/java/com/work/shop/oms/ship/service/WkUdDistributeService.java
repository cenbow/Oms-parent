package com.work.shop.oms.ship.service;

import java.util.List;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.ship.bean.DistributeRequest;

/**
 * 接受供应商分仓结果服务(临时接口)
 * @author lemon
 *
 */
public interface WkUdDistributeService {

	/**
	 * 接受供应商分仓结果数据
	 * @param wkUdDistributes 分仓结果
	 * @return boolean
	 */
	boolean depot(List<WkUdDistribute> wkUdDistributes);

    /**
     *  订单/交货单分仓
     *  orderSn不为空走交货单分配
     *  orderSn为空走订单分配
     * @param distributeRequest
     */
	ReturnInfo<Boolean> depotByOrder(DistributeRequest distributeRequest);
}
