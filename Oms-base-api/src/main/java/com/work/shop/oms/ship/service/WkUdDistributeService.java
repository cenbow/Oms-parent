package com.work.shop.oms.ship.service;

import java.util.List;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;

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
}
