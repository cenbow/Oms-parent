package com.work.shop.oms.stock.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.work.shop.oms.stock.bean.StockResultBean;
import com.work.shop.stockcenter.client.dto.SkuStock;


/*** 
 *  库存占用释放查询OS所有项目共同接口。
 * 
 */
public interface StockCenterService {
	
	/**
	 * 占用库存,并做兼容处理，是占用库存的唯一入口
	 * @param orderSn 订单号
	 * @param isPayFirst 支付时占用 1：支付占用; 0：正常占用 (默认为0)
	 * @return 封装结果对象
	 */
	public StockResultBean occupy(String orderSn, String isPayFirst);//------------------1)占用（根据订单里的商品）
	
	/**
	 * 占用库存,并做兼容处理，是占用库存的唯一入口
	 * @param orderSn 订单号
	 * @param resource 发起占用来源(订单)  0：订单创建; 1：订单确认; 2：订单支付
	 * @return 封装结果对象
	 */
	public StockResultBean asynOccupy(String orderSn, String resource);//------------------1)占用（根据订单里的商品）

	/**
	 * 释放库存,并做兼容处理，是释放库存的唯一入口
	 * @param orderSn 订单号
	 * @param flag   true 库存中心，false 旧方式
	 * @return 封装结果对象
	 */
	public StockResultBean realese(String orderSn);//----------2)释放（对订单的商品所占库存）
	
	
	/**
	 * 新版释放库存，释放指定sku库存（暂时不用）
	 * @param orderSn 订单号
	 * @param flag   true 库存中心，false 旧方式
	 * @return 封装结果对象
	 */
	public StockResultBean realeseBySku(List<SkuStock> sku,String channel,String orderSn);//----------3)释放（对商品的）
	
	
	/**
	 * 查询sku库存（分新老版本，兼容的时候只走老版本）
	 * @param orderSn  订单号
	 * @param appointType   指定处理方式，不知道后续会自动查询获取
	 * @return 库存数量 
	 */
	public List<SkuStock>  skuStockNum(String orderSn);//--------------------------4查询（）
	
	
	/**
	 * 查询订单中是否含有低于指定库存值的商品，只在转订单的时候调用
	 * @param    orderSn     订单号
	 * @param    number      指定阀值
	 * @param    payStatus   订单支付状态
	 * @return   StockResultBean
	 */
	public StockResultBean checkStockInAppointNumber(String orderSn, int number, Byte payStatus);
	
	
	/**
	 * 按照指定渠道，sku查询改sku库存;
	 * @param       sku       11位码
	 * @param       channel   渠道
	 * @return skustock
	 */
	
	public SkuStock getOnlyOneStockNum(String sku,String channel);
	
	
	
	public List<SkuStock> queryStockBySkus(List<String> skus,String channel);
	
	public void modifyGoodsRelease(String orderSn, String channel,
			Map<String, Integer> reduceGoodsNumMap);
	
	/**
	 * 订单占用库存的状态
	 * 返回1 已经全部占用
	 * 返回0 部分或者为占用库存
	 * 			
	 * @return
	 */
	public int getOrderOccStatus(String orderSn);
	
	
	
	/**
	 * 根据指定sn码查询其下所有sku的库存量
	 * 
	 * @param skuSn
	 * 			商品6位码
	 * @param shopCode
	 * 			店铺号
	 * @return
	 */
	List<SkuStock> getSkuStockList(String skuSn,String shopCode);


	public Map<String, Integer> querySkuStock(HashSet<String> skus);
	
	/**
	 * 分配通知统一库存
	 * @param orderSn
	 */
	public void callStockCenter(String orderSn);
}
