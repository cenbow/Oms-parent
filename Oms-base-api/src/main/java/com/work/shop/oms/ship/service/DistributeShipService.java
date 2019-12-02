package com.work.shop.oms.ship.service;

import java.util.List;

import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;

/**
 *订单发货服务
 * @author lemon
 *
 */
public interface DistributeShipService {

	/**
	 * 物流系统返回发货信息处理
	 * @param orderSn 
	 * @param shipProviderList
	 * @param isSystem 是否系统分仓  true:是; false:否
	 */
	public ReturnInfo processShip(String orderSn, List<DistributeShipBean> shipProviderList, boolean isSystem);

	/**
	 * 订单自提核销
	 * @param distributeShipBean
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> cacWriteOff(DistributeShippingBean distributeShipBean);
	
	/**
	 * 配送单拣货中
	 * @param distributeShipBean
	 * @return
	 */
	public ReturnInfo<String> processDistributePicking(DistributeShippingBean distributeShipBean);
	
	/**
	 * 配送单拣货完成
	 * @param distributeShipBean
	 * @return
	 */
	public ReturnInfo<String> processDistributePickUp(DistributeShippingBean distributeShipBean);

	/**
	 * 订单取消日志记录
	 * @param distributeShipBean
	 * @return
	 */
	public ReturnInfo<String> processOrderDistributeCancel(DistributeShippingBean distributeShipBean);
	
	/**
	 * 物流系统返回发货信息处理
	 * @param distributeShipBean 发货信息
	 * @param isSystem 是否系统分仓  true:是; false:否
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> processShip(DistributeShippingBean distributeShipBean, boolean isSystem);
	
	/**
	 * 重新更新发货接口
	 * @param orderSn
	 */
	public void reShipped(String orderSn) ;
	
	
	public String acceptShipData(String data);

	/**
	 * 供应商发货前向OMS确认是否可以发货
	 * @param request
	 * @return DistOrderShipResponse
	 */
	DistOrderShipResponse shippedConfirm(DistOrderShipRequest request);
	
	/**
	 * 接受物流发货数据
	 * @param providerBeanParams
	 * @return
	 */
	public ReturnInfo acceptShip(List<OrderToShippedProviderBeanParam> providerBeanParams);
	
	/**
	 * 判断主订单发货状态
	 * @param masterOrderSn 订单号
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> judgeMasterShipedStatus(String masterOrderSn);
	
	/**
	 * 判断交货单已发货状态
	 * @param masterOrderSn
	 * @return
	 */
	public ReturnInfo<Byte> judgeDistributeShipedStatus(String orderSn);

	/**
	 *  全渠道订单发货14天后更新已收货状态
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @return ReturnInfo
	 */
	ReturnInfo confirmReceipt(String masterOrderSn, String actionUser);

	/**
	 * 分配单发货确认(订单)
	 * @param distributeShipBean 分配单发货信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> distOrderConfirm(DistributeShippingBean distributeShipBean);
	
	/**
	 * 分配单发货确认(交货单)
	 * @param distributeShipBean 配送出库对象
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> distDeliveryConfirm(DistributeShippingBean distributeShipBean);

	/**
	 * 分配单收货确认(交货单)
	 * @param distributeShipBean 分配单发货信息
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> distReceiptConfirm(DistributeShippingBean distributeShipBean);
	
	/**
	 * 供应商发货
	 * @param request
	 * @return DistOrderShipResponse
	 */
	DistOrderShipResponse distOrderShip(DistOrderShipRequest request);
	
	/**
	 * 收货确认(按照快递单号签收，快递单号为空时按照订单签收)
	 * @param bean
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> confirmationOfReceipt(DistributeShippingBean bean);

	/**
	 * 处理订单确认收货结果
	 * @param masterOrderSn
	 */
	void processMasterShipResult(String masterOrderSn);

}
