package com.work.shop.oms.shoppay.service;

import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.ShopPayCashBean;
import com.work.shop.oms.common.bean.ShopPayResultBean;

/**
 * 店铺支付 查询消费服务
 * @author lemon
 *
 */
public interface ShopPayService {
	
	/**
	 * 查询用户店铺支付金额
	 * @param userName
	 * @return
	 */
	public Float searchUserPayService(String userName);

	/**
	 * 校验店铺支付是否可用(冻结店铺支付金额)
	 * @param userName
	 * @param bangFuBaoCashBean
	 * @return
	 */
	public ServiceReturnInfo<OcpbStatus> validateShopPay(MasterOrder masterOrder);

	/**
	 * 店铺支付消费(扣减店铺支付金额)
	 * @param userName
	 * @param bangFuBaoCashBean
	 * @return
	 */
	public ShopPayResultBean payService(String userName, String masterOrderSn, ShopPayCashBean payCashBean);
	
	/**
	 * 校验店铺支付是否可用(释放冻结店铺支付金额)
	 * @param masterOrder
	 * @param ocpbStatus
	 * @return
	 */
	public ServiceReturnInfo<OcpbStatus> releaseShopPay(MasterOrder masterOrder, OcpbStatus ocpbStatus);
	
	/**
	 * 余额退还
	 * 
	 * @param userId
	 * @param surplus
	 * @param returnSn
	 * @param memo
	 * @return
	 */
	public ReturnInfo<String> backSurplus(String userId, float surplus, String returnSn, String memo);
}
