package com.work.shop.oms.orderget.service;

import java.util.List;
import java.util.Set;

import com.work.shop.oms.common.bean.ChannelDeliveryReturn;
import com.work.shop.oms.common.bean.ReturnInfo;

public interface ChannelOrderService {
	
	/**
	 * 下载外渠订单
	 * @param orderIdSet
	 * @param channelShortName
	 * @return
	 */
	public ReturnInfo downloadChannelOrder(Set<String> orderIdSet,String channelShortName);
	
	/**
	 * 发货外渠订单
	 * @param orderIdList
	 * @param channelCode
	 * @param enforce 默认false，是否强制更新
	 * @return
	 */
	public List<ChannelDeliveryReturn> deliveryChannelOrder(List<String> orderIdList,String channelCode,Boolean enforce);
	
	/**
	 * 转单回写外渠平台
	 * @param channelCode
	 * @param shopCode
	 * @param orderId
	 * @return 
	 */
	public void callBack(String channelCode, String shopCode,String orderId , String osNote);
	
	/**
	 * 重新拉单工具
	 * @param shopCode
	 * @param orderOuterSn
	 */
	public ReturnInfo reloadOrder(Set<String> orderOuterSn,String shopCode,String isReload);
	/**
	 * ag退单
	 * @param topRefund
	 */
//	public boolean taobaoAGRefund(TopRefund topRefund);
	/**
	 * 淘宝退单入库回写
	 * @param topRefund
	 */
	public boolean taobaoAGRefundStorage(String refundId,String channelCode);
	
	

}
