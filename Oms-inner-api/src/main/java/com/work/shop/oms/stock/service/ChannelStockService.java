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

}
