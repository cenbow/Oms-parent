package com.work.shop.oms.orderop.feign;

import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.feign.bean.OrderStockQuestion;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单操作服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface OrderCommonService {

    /**
     * 主订单取消(共用方法)
     *
     * @param masterOrderSn 主订单号
     * @param orderStatus   code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
     * @return ReturnInfo
     */
    @PostMapping("/order/cancelOrderByMasterSn")
    ReturnInfo<Boolean> cancelOrderByMasterSn(@RequestParam(name = "masterOrderSn") String masterOrderSn, OrderStatus orderStatus);

    /**
     * 子订单取消(共用方法)
     * @param orderStatus code:取消原因code;message:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/order/cancelOrderByOrderSn")
    ReturnInfo<Boolean> cancelOrderByOrderSn(OrderStatus orderStatus);

    /**
     * 设置主订单问题单
     *
     * @param orderSn 订单编码
     * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
     * @return ReturnInfo
     */
    @PostMapping("/order/questionOrderByOrderSn")
    ReturnInfo<Boolean> questionOrderByOrderSn(@RequestParam(name="orderSn") String orderSn, OrderStatus orderStatus);

    /**
     * 设置缺货问题单（短拣、短配、缺货问题单等）
     * @param orderStockQuestion 缺货信息
     * @return ReturnInfo
     */
    @PostMapping("/order/addLackSkuQuestion")
    ReturnInfo<Boolean> addLackSkuQuestion(OrderStockQuestion orderStockQuestion);

    /**
     * 第三方供应商发货前向OMS确认是否可以发货
     * @param request
     * @return DistOrderShipResponse
     */
    @PostMapping("/order/shippedConfirm")
    DistOrderShipResponse shippedConfirm(@RequestBody DistOrderShipRequest request);

    /**
     * 订单锁定
     * @param orderStatus 订单状态信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/order/lockOrder")
    ReturnInfo<String> lockOrder(OrderStatus orderStatus);

    /**
     * 订单解锁
     * @param orderStatus 订单状态信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/order/unLockOrder")
    ReturnInfo<String> unLockOrder(OrderStatus orderStatus);

    /**
     * 订单自提核销、订单配送签收
     * @param distributeShipBean 订单配送信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/order/cacWriteOff")
    ReturnInfo<String> cacWriteOff(DistributeShippingBean distributeShipBean);

    /**
     * 
     *  订单确认收货
     * @param masterOrderSn 订单编码
     * @param actionUser 操作人
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/order/confirmReceipt")
    @Deprecated
    ReturnInfo<Boolean> confirmReceipt(@RequestParam(name="masterOrderSn") String masterOrderSn, @RequestParam(name="actionUser", required = false) String actionUser);

    /**
     * 处理仓库订单交货单发货
     * @param distributeShipBean 订单交货单信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/order/processShip")
    ReturnInfo<String> processShip(@RequestBody DistributeShippingBean distributeShipBean);

    /**
     * 分配单发货确认
     * @param distributeShipBean 订单交货单信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/order/distOrderConfirm")
    ReturnInfo<String> distOrderConfirm(DistributeShippingBean distributeShipBean);

    /**
     * 编辑承运商
     * @param modifyInfo 编辑信息
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/order/editShippingType")
    ReturnInfo<Boolean> editShippingType(@RequestBody ConsigneeModifyInfo modifyInfo);
    
	/**
	 * 供应商发货
	 * @param request
	 * @return DistOrderShipResponse
	 */
    @PostMapping("/order/distOrderShip")
	DistOrderShipResponse distOrderShip(@RequestBody DistOrderShipRequest request);

	/**
	 * 收货确认(按照快递单号签收，快递单号不填写时按照订单签收)
	 * @param bean
	 * @return ReturnInfo<String>
	 */
    @PostMapping("/order/confirmationOfReceipt")
	ReturnInfo<String> confirmationOfReceipt(DistributeShippingBean bean);

    /**
     * 主订单编辑订单地址
     *
     * @param masterOrderSn 主订单号
     * @param consignInfo 客户信息
     * @return
     */
    @PostMapping("/order/editConsigneeInfoByMasterSn")
    ReturnInfo editConsigneeInfoByMasterSn(@RequestParam(name="masterOrderSn") String masterOrderSn, @RequestBody ConsigneeModifyInfo consignInfo);

    /**
     * 主订单编辑发票信息
     *
     * @param consignInfo 客户信息
     * @return
     */
    @PostMapping("/order/editInvInfoByMasterSn")
    ReturnInfo editInvInfoByMasterSn(ConsigneeModifyInfo consignInfo);

    /**
     * 主订单编辑发票地址信息
     *
     * @param consignInfo 客户信息
     * @return
     */
    @PostMapping("/order/editInvAddressInfoByMasterSn")
    ReturnInfo editInvAddressInfoByMasterSn(ConsigneeModifyInfo consignInfo);

    /**
     * 主订单编辑绑定团队信息
     *
     * @param masterOrderDetail 订单信息
     * @return
     */
    @PostMapping("/order/editBindTeam")
    ReturnInfo editBindTeam(@RequestParam(name = "actionUser")  String actionUser,MasterOrderDetail masterOrderDetail);
}
