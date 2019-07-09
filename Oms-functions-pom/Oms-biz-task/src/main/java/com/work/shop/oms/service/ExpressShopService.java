package com.work.shop.oms.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.OrderExpressInfo;
import com.work.shop.oms.core.beans.ShipExpressInfo;

public interface ExpressShopService {

	/**
	 * 发货数据转换
	 * @param express
	 * @return
	 */
	public ShipExpressInfo buildExpressInfo(OrderExpressInfo express);
	
	/**
	 * 地区数据解析
	 * @param shipExpressInfo
	 * @return
	 */
	public Map<String,String> processRegionArea(ShipExpressInfo shipExpressInfo);
	
	/**
	 * 手机解密
	 * @param mobile
	 * @return
	 */
	public String mobileUtilDec(String mobile);
	
	/**
	 * 物流信息推送后处理结果
	 * @param orderSn
	 * @param shippingId
	 * @param depotCode
	 * @param isOk
	 * @param result
	 */
	public void callExpressResult(String orderSn,Integer shippingId,String depotCode,Integer isOk,String result);
}
