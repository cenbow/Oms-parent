package com.work.shop.oms.order.feign;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoFinishBean;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.OrdersCreateReturnInfo;
import com.work.shop.oms.order.request.OmsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign订单接口服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface MasterOrderInfoService {

    /**
     * 生成订单
     * @param masterOrder 订单信息
     * @return OrderCreateReturnInfo
     */
    @PostMapping("/order/createOrder")
    OrderCreateReturnInfo createOrder(MasterOrder masterOrder);

    /**
     * 批量生成订单
     * @param masterOrders 订单信息列表
     * @return OrdersCreateReturnInfo
     */
    @PostMapping("/order/createOrders")
    OrdersCreateReturnInfo createOrders(List<MasterOrder> masterOrders);

    /**
     * 根据订单号查询订单信息
     * @param masterOrderSn 订单编码
     * @return MasterOrderInfo
     */
    @PostMapping("/order/getOrderInfoBySn")
    MasterOrderInfo getOrderInfoBySn(@RequestParam(name="masterOrderSn") String masterOrderSn);

    /**
     * 通过外部交易号, 获取对应的订单信息
     * @param outOrderSn 外部订单号
     * @return MasterOrderInfo
     */
    @PostMapping("/order/getOrderInfoByOutOrderSn")
    MasterOrderInfo getOrderInfoByOutOrderSn(@RequestParam(name="outOrderSn") String outOrderSn);

    /**
     * 处理订单生成后续操作
     *
     * @param orderBean 订单信息
     */
    @PostMapping("/order/dealOther")
    void dealOther(MasterOrderInfoFinishBean orderBean);

    /**
     * 添加订单日志
     * @param request 订单日志信息
     */
    @PostMapping("/order/insertOrderActionBySn")
    public void insertOrderActionBySn(OmsRequest request);
}
