package com.work.shop.oms.orderget.service;

import java.util.List;
import java.util.Set;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderShopParam;
import com.work.shop.oms.common.bean.OrderSku;
import com.work.shop.oms.common.bean.OtherTradeParam;
import com.work.shop.oms.common.bean.PullAndTurn;
import com.work.shop.oms.common.bean.ReturnInfo;


/**
 * 渠道管理-提供工具服务
 * @author huangl
 *
 */
public interface ChannelManagerService {

	/**
	 * 查询转单数据
	 * @param pullAndTurn
	 * @return
	 */
	public Paging channelOrderList(PullAndTurn pullAndTurn);
	
	/**
	 * 转单外渠订单
	 * @param orderIdList
	 * @param channelCode
	 * @return
	 */
	public ReturnInfo convertChannelOrder(List<String> orderIdList,String channelCode);
	
	/**
	 * 补全商品编码
	 * @param orderId
	 * @param channelCode
	 * @param goodsKey
	 * @param newSku
	 * @return
	 */
	public ReturnInfo fillSkuChannelOrder(OrderSku orderSku);
	
	/**
	 * 查询缺失商品的订单List
	 * @param orderSku
	 * @return
	 */
	public Paging findMissingSkuOrderList(OrderSku orderSku);
	
	
	/**
	 * 加载渠道配置数据-动态下拉选框
	 * @param dataType 0拉单、1转单、2发货、3退单
	 * @return
	 */
	public List<OrderShopParam> loadOrderShopData(Integer dataType);
	
	/**
	 * bgapidb手工导入订单
	 * @param otherOrders
	 */
	public void  createOtherOrder(List<OtherTradeParam> otherOrders);
	/**
	 * 回写调整单
	 * @param message
	 * @param orderCode
	 * @param billNo
	 * @param resultStatus
	 * @param orderEnd
	 */
	public void seveBill(String message,String orderCode,String billNo,byte resultStatus,boolean orderEnd);
	/**
	 * 根据外部交易号和sku 11位码查询商品价格（修复数据临时使用）
	 * @param tid
	 * @param sku
	 * @return
	 */
	public double getOrderGoodsByTidAndSku(long tid,String sku);
	
	public String missOrderTool(Set<String> orders,String channel);
	
	
	/**	
	 * 查询转单数据
	 * @param pullAndTurn
	 * @return
	 */
	public Paging deliveryOrderList(PullAndTurn pullAndTurn);
	
	
}
