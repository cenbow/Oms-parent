package com.work.shop.oms.controller;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.api.param.bean.PayBackInfo;
import com.work.shop.oms.api.param.bean.PayReturnInfo;
import com.work.shop.oms.api.payment.service.OrderPaymentService;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单支付服务
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderPaymentController {

    private static final Logger logger = Logger.getLogger(OrderPaymentController.class);

    @Resource
    private OrderPaymentService orderPaymentService;

    /**
     * 获取订单支付(平台付款时使用)
     * 传支付单号则查询支付单消息
     * 传订单号 则查询该订单未支付支付单消息
     * 传多个订单号，则生成合并支付单并返回支付消息
     * @param paySn 支付单号
     * @param masterOrderSnList 订单编码列表
     * @return ApiReturnData<OrderPayInfo>
     */
    @PostMapping("/getOrderPayInfo")
    public ApiReturnData<OrderPayInfo> getOrderPayInfo(@RequestParam(name="paySn", required = false) String paySn, @RequestParam(name="masterOrderSnList") List<String> masterOrderSnList) {
        ApiReturnData<OrderPayInfo> returnBean = new ApiReturnData<OrderPayInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = orderPaymentService.getOrderPayInfo(paySn, masterOrderSnList);
        } catch (Exception e) {
            logger.error("获取订单支付信息异常paySn:" + paySn + ",masterOrderSnList:" + masterOrderSnList);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 获取订单支付金额信息
     * @param masterOrderSnList 订单编码列表
     * @return ApiReturnData<OrderPayInfo>
     */
    @PostMapping("/getOrderPayMoneyInfo")
    public ApiReturnData<OrderPayInfo> getOrderPayMoneyInfo(@RequestParam(name="masterOrderSnList") List<String> masterOrderSnList) {
        ApiReturnData<OrderPayInfo> returnBean = new ApiReturnData<OrderPayInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = orderPaymentService.getOrderPayMoneyInfo(masterOrderSnList);
        } catch (Exception e) {
            logger.error("获取订单支付金额信息异常,masterOrderSnList:" + masterOrderSnList);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 变更支付方式
     * @param paySn 支付单号
     * @param newPayId  支付方式id
     * @return ApiReturnData
     */
    @PostMapping("/changeOrderPayMethod")
    public ApiReturnData changeOrderPayMethod(@RequestParam(name="paySn") String paySn, @RequestParam(name="newPayId") Integer newPayId) {
        ApiReturnData returnBean = new ApiReturnData();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = orderPaymentService.changeOrderPayMethod(paySn, newPayId);
        } catch (Exception e) {
            logger.error("变更支付异常:paySn" + paySn + ",newPayId:" + newPayId, e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 支付前锁定支付单
     * @param paySn 支付单号
     * @param actionUser 操作用户
     * @return ApiReturnData
     */
    @PostMapping("/lockOrderBeforePayment")
    public ApiReturnData lockOrderBeforePayment(@RequestParam(name="paySn") String paySn, @RequestParam(name="actionUser") String actionUser) {
        ApiReturnData returnBean = new ApiReturnData();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = orderPaymentService.lockOrderBeforePayment(paySn, actionUser);
        } catch (Exception e) {
            logger.error("支付前锁定支付异常:paySn" + paySn + ",actionUser:" + actionUser, e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 前端支付成功回调
     * @param payBackInfo 支付成功信息
     * @return PayReturnInfo
     */
    @PostMapping("/payStChClient")
    public PayReturnInfo payStChClient(@RequestBody PayBackInfo payBackInfo) {
        PayReturnInfo returnBean = new PayReturnInfo();

        try {
            returnBean = orderPaymentService.payStChClient(payBackInfo);
        } catch (Exception e) {
            logger.error("支付成功回调异常:" + JSONObject.toJSONString(payBackInfo), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 根据支付单号获取对应的订单号
     * @param paySn 支付单号
     * @return ApiReturnData<List<String>>
     */
    @PostMapping("/getOrderSnByPaySn")
    public ApiReturnData<List<String>> getOrderSnByPaySn(@RequestParam(name="paySn") String paySn) {
        ApiReturnData<List<String>> returnBean = new ApiReturnData<List<String>>();
        returnBean.setIsOk(Constant.OS_STR_NO);
        try {
            returnBean = orderPaymentService.getOrderSnByPaySn(paySn);
        } catch (Exception e) {
            logger.error("根据支付单号获取对应的订单号：" + paySn + ",异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 根据支付单号获取对应的订单号
     * @param paySn 支付单号
     * @return ApiReturnData<List<String>>
     */
    @PostMapping("/getOrderPaySnByMergePaySn")
    public ApiReturnData<List<MasterOrderPay>> getOrderPaySnByMergePaySn(@RequestParam(name="paySn") String paySn) {
        ApiReturnData<List<MasterOrderPay>> returnBean = new ApiReturnData<List<MasterOrderPay>>();
        returnBean.setIsOk(Constant.OS_STR_NO);
        try {
            returnBean = orderPaymentService.getOrderPaySnByMergePaySn(paySn);
        } catch (Exception e) {
            logger.error("根据支付单号获取对应的订单号：" + paySn + ",异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }
}
