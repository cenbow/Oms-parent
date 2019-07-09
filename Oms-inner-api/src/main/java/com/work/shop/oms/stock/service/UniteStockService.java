package com.work.shop.oms.stock.service;

import java.util.List;

import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.stockcenter.client.dto.SkuStock;
import com.work.shop.stockcenter.client.dto.StockOperatePO;

/**
 * 新库存占用、释放、查询服务
 * 
 * @author lemon
 *
 */
public interface UniteStockService {

	
	/**
	 * 通知实占库存
	 * @param masterOrderSn 主单号
	 * @return ReturnInfo
	 */
	public ReturnInfo occupy(String masterOrderSn);
	
	/**
	 * 拆单后分配单实占库存
	 * @param masterOrderSn 主单号
	 * @return ReturnInfo
	 */
	ReturnInfo<String> distOccupy(String masterOrderSn);

	/**
	 * 释放库存
	 * @param masterOrderSn 主单号
	 * @return ReturnInfo
	 */
	ReturnInfo<String> realese(String masterOrderSn);
	
	/**
	 * 拉单占用库存
	 * 
	 * @param stockOperatePO
	 * @return
	 */
	public ReturnInfo occupyByOutChannel(StockOperatePO stockOperatePO);
	
	/**
	 * 根据商品编码列表与渠道号批量查询库存
	 * @param skus
	 * @param shopCode
	 * @return
	 */
	public List<SkuStock> queryStockBySkus(List<String> skus, String shopCode);
	
	/**
	 * 根据商品编码列表与渠道号查询库存
	 * 
	 * @param sku
	 * @param shopCode
	 * @return
	 */
	public SkuStock queryStockBySku(String sku, String shopCode, String depotCode);
}
