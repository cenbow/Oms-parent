package com.work.shop.oms.payment.feign;

import com.work.shop.oms.bean.MergeOrderPay;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign订单支付服务接口
 * @author
 */
@FeignClient("OMSPAY-SERVICE")
public interface PayService {

    /**
     * 创建支付单(如果是创建未支付支付单则备份并删除原未支付支付单)
     * 	PayCode支付方式  如果不传默认为"支付宝"
     *  payStatus支付状态   如果不传默认为"未支付"
     *  payStatus '支付状态；0，未付款；1，部分付款；2，已付款；3，已结算;4,待确认',
     *  payId
     *  PayCode
     *  payTotalfee
     *  masterOrderSn
     */
    @PostMapping("/pay/createMasterPay")
    public ReturnInfo<List<String>> createMasterPay(@RequestParam(name="masterOrderSn") String masterOrderSn, List<MasterPay> masterPayList);

    /**
     * 创建合并支付单
     * @return
     */
    @PostMapping("/pay/createMergePay")
    public ReturnInfo<MergeOrderPay> createMergePay(@RequestParam(name="masterOrderSnList") List<String> masterOrderSnList);

    /**
     * 主订单支付单支付
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     * source   POS: POS端;"OMS":OmsManager;"FRONT":前端
     */
    @PostMapping("/pay/payStCh")
    ReturnInfo payStCh(OrderStatus orderStatus);

    /**
     * 退单转入款入库确认
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     */
    @PostMapping("/pay/orderReturnPayStCh")
    public ReturnInfo orderReturnPayStCh(OrderStatus orderStatus);

    /**
     * 退单转入款未确认
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     */
    @PostMapping("/pay/orderReturnUnPayStCh")
    public ReturnInfo orderReturnUnPayStCh(OrderStatus orderStatus);

    /**
     * 主订单支付单未支付
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     * source  "OMS":OmsManager
     */
    @PostMapping("/pay/unPayStCh")
    public ReturnInfo unPayStCh(OrderStatus orderStatus);

    /**
     * 根据支付单号或订单号修改支付方式
     * @param paySn
     * @param newPayId
     * @param actionUser
     * @return
     */
    @PostMapping("/pay/changeOrderPayMethod")
    public ReturnInfo changeOrderPayMethod(@RequestParam(name="paySn") String paySn, @RequestParam(name="newPayId") Integer newPayId,
                                           @RequestParam(name="actionUser") String actionUser);
}
