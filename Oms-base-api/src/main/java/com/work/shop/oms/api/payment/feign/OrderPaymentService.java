package com.work.shop.oms.api.payment.feign;

import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.api.param.bean.PayBackInfo;
import com.work.shop.oms.api.param.bean.PayReturnInfo;
import com.work.shop.oms.common.bean.ApiReturnData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign订单支付服务
 * @author QuYachu
 */
@FeignClient("OMSPAY-SERVICE")
public interface OrderPaymentService {

    /**
     * 获取订单支付(平台付款时使用)
     * 传支付单号则查询支付单消息
     * 传订单号 则查询该订单未支付支付单消息
     * 传多个订单号，则生成合并支付单并返回支付消息
     * @param paySn 支付单号
     * @param masterOrderSnList 订单编码列表
     * @return ApiReturnData<OrderPayInfo>
     */
    @PostMapping("/order/getOrderPayInfo")
    ApiReturnData<OrderPayInfo> getOrderPayInfo(@RequestParam(name="paySn", required = false) String paySn, @RequestParam(name="masterOrderSnList") List<String> masterOrderSnList);

    /**
     * 变更支付方式
     * @param paySn 支付单号
     * @param newPayId  支付方式id
     * @return ApiReturnData
     */
    @PostMapping("/order/changeOrderPayMethod")
    ApiReturnData changeOrderPayMethod(@RequestParam(name="paySn") String paySn, @RequestParam(name="newPayId") Integer newPayId);

    /**
     * 支付前锁定支付单
     * @param paySn 支付单号
     * @param actionUser 操作用户
     * @return ApiReturnData
     */
    @PostMapping("/order/lockOrderBeforePayment")
    ApiReturnData lockOrderBeforePayment(@RequestParam(name="paySn") String paySn, @RequestParam(name="actionUser") String actionUser);

    /**
     * 前端支付成功回调
     * @param payBackInfo 支付成功信息
     * @return PayReturnInfo
     */
    @PostMapping("/order/payStChClient")
    PayReturnInfo payStChClient(PayBackInfo payBackInfo);

    /**
     * 根据支付单号获取对应的订单号
     * @param paySn 支付单号
     * @return ApiReturnData<List<String>>
     */
    @PostMapping("/order/getOrderSnByPaySn")
    ApiReturnData<List<String>> getOrderSnByPaySn(@RequestParam(name="paySn") String paySn);
}
