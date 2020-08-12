package com.work.shop.oms.stock.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.stockcenter.client.dto.StockOperatePO;

/**
 * 渠道库存服务
 * @author QuYachu
 */
public interface ChannelStockService {

	/**
	 * 下单成功通知平台占库存
	 * @param masterOrderSn
	 * @return ReturnInfo
	 */
	ReturnInfo preOccupy(String masterOrderSn);
	
	/**
	 * 支付成功通知平台占库存
	 * @param masterOrderSn
	 * @return ReturnInfo
	 */
	ReturnInfo payOccupy(String masterOrderSn);
	
	/**
	 * 新增商品成功后通知平台占库存
	 * @param po
	 * @return
	 */
	public ReturnInfo preOccupyByGoodsList(String masterOrderSn, String shopCode, List<MasterOrderGoods> goodsList);
	
	/**
	 * 订单支付成功占库存（支付成功库存实时扣减）
	 * @param po
	 * @return
	 */
	public ReturnInfo occupy(StockOperatePO po, String isPayFirst);

	
	/**
	 * 订单支付成功占库存（支付成功库存实时扣减）
	 * @param po
	 * @return
	 */
	public ReturnInfo occupy(StockOperatePO po);
	
	/**
	 * 释放库存
	 * @param po
	 * @return
	 */
	public ReturnInfo realese(StockOperatePO po);
	
	/**
	 * 订单取消通知商城释放库存
	 * @param masterOrderSn
	 * @return ReturnInfo
	 */
	ReturnInfo cancelRealese(String masterOrderSn);
	
	/**
	 * 库存查询
	 * @param sku
	 * @param shopCode
	 * @return
	 */
	public ReturnInfo<Integer> queryStockBySku(String sku, String shopCode, String siteCode, String depotCode);

	/**
	 * 订单取消后，如果是无库存下单补加过库存，减掉补加的库存
	 * 包括场景：订单支付后如果被审核驳回、订单下单后未支付用户取消、订单下单后超期未支付自动取消
	 *
	 * @param masterOrderSn
	 * @param MasterOrderGoodsList
	 * @return void
	 * @author matianqi
	 * @date 2020-02-59 12:16
	 */
	void checkAndDeductWithoutStockInventory(String masterOrderSn, List<MasterOrderGoods> MasterOrderGoodsList);
}
