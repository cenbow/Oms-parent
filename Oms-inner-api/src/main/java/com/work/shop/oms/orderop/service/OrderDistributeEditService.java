package com.work.shop.oms.orderop.service;

import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.common.bean.OrderOtherModifyInfo;
import com.work.shop.oms.common.bean.ReturnInfo;

public interface OrderDistributeEditService {

	/**
	 * 订单编辑商品(共用方法)
	 * 
	 * @param masterOrderInfo 主订单
	 * @param distribute 子订单
	 * @param infoUpdateInfo 订单品数据
	 * @param orderStatus message:备注;actionUser:操作人;
	 * @return
	 */
	public ReturnInfo editGoods(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderInfoUpdateInfo infoUpdateInfo, String actionUser);

	/**
	 * 主订单编辑商品
	 * 
	 * @param masterOrderSn 主订单号
	 * @param infoUpdateInfo 订单品数据
	 * @param orderStatus message:备注;actionUser:操作人;
	 * @return
	 */
	public ReturnInfo editGoodsByMasterSn(String masterOrderSn, OrderInfoUpdateInfo infoUpdateInfo, String actionUser);
	
	/**
	 * 子订单编辑商品
	 * 
	 * @param orderSn 子订单号
	 * @param orderStatus message:备注;actionUser:操作人;
	 * @return
	 */
	/**
	 * 
	 * @param orderSn 子订单号
	 * @param infoUpdateInfo 订单品数据
	 * @param orderStatus message:备注;actionUser:操作人;
	 * @return
	 */
	public ReturnInfo editGoodsByOrderSn(String orderSn, OrderInfoUpdateInfo infoUpdateInfo, String actionUser);
	
	/**
	 * 编辑订单费用信息
	 * 
	 * @param masterOrderSn
	 * @param actionUser
	 * @param shippingFee
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo editShippingFee(String masterOrderSn, String actionUser, Double shippingFee);

	/**
	 * 编辑承运商
	 * @param modifyInfo 更新信息
	 * @return ReturnInfo
	 */
	ReturnInfo editShippingType(ConsigneeModifyInfo modifyInfo);
	
	/**
	 * 编辑订单其他信息
	 * 
	 * @param orderSn
	 * @param actionUser
	 * @param otherModify
	 * @return
	 */
	public ReturnInfo editOrderOther(String masterOrderSn, String actionUser, OrderOtherModifyInfo otherModify);
	
	/**
	 * 主订单编辑订单地址
	 * 
	 * @param masterOrderSn 主订单号
	 * @param consignInfo 客户信息
	 * @return
	 */
	public ReturnInfo editConsigneeInfoByMasterSn(String masterOrderSn, ConsigneeModifyInfo consignInfo);

	/**
	 * 
	 * @param orderSn 子订单号
	 * @param infoUpdateInfo 订单品数据
	 * @param orderStatus message:备注;actionUser:操作人;
	 * @return
	 */
	public ReturnInfo editConsigneeInfoByOrderSn(ConsigneeModifyInfo consignInfo);
	
	/**
	 * 构建收货人信息
	 * 
	 * @param masterOrderSn
	 * @param consignInfo
	 * @param orderAddressInfo
	 * @param actionNote
	 * @return
	 */
	public MasterOrderAddressInfo editAddressInfo(String masterOrderSn, ConsigneeModifyInfo consignInfo, MasterOrderAddressInfo orderAddressInfo,
			StringBuffer actionNote);
}
