package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.PurchaseOrder;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.request.OrderManagementRequest;

/**
 * 采购单服务
 * 	采购单创建
 * @author lemon
 *
 */
public interface PurchaseOrderService {

	/**
	 * 采购单创建(交货单)
	 * @param request
	 */
	ReturnInfo<String> purchaseOrderCreate(OrderManagementRequest request);
	
	/**
	 * 采购单创建(订单)
	 * @param request
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> purchaseOrderCreateByMaster(OrderManagementRequest request);

    /**
     * 订单推送供应链
     * @param masterOrderSn
     * @param actionUser
     * @param actionUserId
     * @param supplierCode 供应商编码
     * @param type 0买家->超市，1超市->供应商
     */
    void pushJointPurchasing(String masterOrderSn, String actionUser, String actionUserId, String supplierCode, int type);

    /**
     * 更新采购单签章状态
     * @param orderStatus
     * @return
     */
    ReturnInfo<String> updatePushSupplyChain(PurchaseOrder purchaseOrder);

    /**
     * 根据主键更新采购单
     * @param purchaseOrder
     */
    public void updatePurchaseOrder(PurchaseOrder purchaseOrder);
}
