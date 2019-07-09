package com.work.shop.oms.distribute.service;

import java.util.List;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;

/**
 * 通知供应商分仓以及处理分仓后数据写入OMS服务
 * @author lemon
 *
 */
public interface NoticeDistributeService {

	/**
	 * 全量处理订单下的分仓信息
	 * 1.保存ERP原始信息
	 * 2.根据虚拟仓库合并ordership
	 * 3.备份原始orderShip后删除重新建立ordership
	 * 4.备份原始orderGoods后删除重新建立orderGoods
	 * 5.备份原始orderGoodsWarehouse后删除重新建立orderGoodsWarehouse
	 * 6.修改orderInfo分仓通知状态
	 * @param orderSn
	 * @param shipProviderList
	 * @param isSystem 是否系统分仓 true:是;false:否
	 */
	void processDistribute(String orderSn, List<WkUdDistribute> shipProviderList,boolean isSystem);
}
