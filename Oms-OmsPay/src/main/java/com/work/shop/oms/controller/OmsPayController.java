package com.work.shop.oms.controller;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.MergeOrderPay;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.payment.service.PayService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * OMS订单支付
 * @author QuYachu
 */
@RestController
@RequestMapping("/pay")
public class OmsPayController {

    private static final Logger logger = Logger.getLogger(OmsPayController.class);

    @Resource
    private PayService payService;

    /**
     * 创建支付单(如果是创建未支付支付单则备份并删除原未支付支付单)
     * @param masterOrderSn
     * @param masterPayList
     * 	PayCode支付方式  如果不传默认为"支付宝"
     *  payStatus支付状态   如果不传默认为"未支付"
     *  payStatus '支付状态；0，未付款；1，部分付款；2，已付款；3，已结算;4,待确认',
     *  payId
     *  PayCode
     *  payTotalfee
     *  masterOrderSn
     */
    @PostMapping("/createMasterPay")
    public ReturnInfo<List<String>> createMasterPay(@RequestParam(name = "masterOrderSn") String masterOrderSn, @RequestBody List<MasterPay> masterPayList) {
        ReturnInfo<List<String>> returnInfo = new ReturnInfo<List<String>>();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.createMasterPay(masterOrderSn, masterPayList);
        } catch (Exception e) {
            logger.error("createMasterPay:" + masterOrderSn + "," + JSONObject.toJSONString(masterPayList), e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 创建合并支付单
     * @param masterOrderSnList
     * @return
     */
    @PostMapping("/createMergePay")
    public ReturnInfo<MergeOrderPay> createMergePay(@RequestBody List<String> masterOrderSnList) {
        ReturnInfo<MergeOrderPay> returnInfo = new ReturnInfo<MergeOrderPay>();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.createMergePay(masterOrderSnList);
        } catch (Exception e) {
            logger.error("createMergePay:" + JSONObject.toJSONString(masterOrderSnList), e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 主订单支付单支付
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     * source   POS: POS端;"OMS":OmsManager;"FRONT":前端
     */
    @PostMapping("/payStCh")
    public ReturnInfo payStCh(@RequestBody OrderStatus orderStatus) {
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.payStCh(orderStatus);
        } catch (Exception e) {
            logger.error("payStCh:" + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 退单转入款入库确认
     * @param orderStatus
     * masterOrderSn
     * userId
     * paySn
     * adminUser
     */
    @PostMapping("/orderReturnPayStCh")
    public ReturnInfo orderReturnPayStCh(@RequestBody OrderStatus orderStatus) {
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.orderReturnPayStCh(orderStatus);
        } catch (Exception e) {
            logger.error("orderReturnPayStCh:" + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 退单转入款未确认
     * @param orderStatus
     * masterOrderSn 订单编码
     * userId 操作人id
     * paySn 支付单号
     * adminUser
     */
    @PostMapping("/orderReturnUnPayStCh")
    public ReturnInfo orderReturnUnPayStCh(@RequestBody OrderStatus orderStatus) {
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.orderReturnUnPayStCh(orderStatus);
        } catch (Exception e) {
            logger.error("orderReturnUnPayStCh:" + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 主订单支付单未支付
     * @param orderStatus
     * masterOrderSn 订单号
     * paySn 支付单号
     * userId
     * adminUser
     * source  "OMS" 来源
     */
    @PostMapping("/unPayStCh")
    public ReturnInfo unPayStCh(@RequestBody OrderStatus orderStatus) {
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.unPayStCh(orderStatus);
        } catch (Exception e) {
            logger.error("unPayStCh:" + JSONObject.toJSONString(orderStatus) + "异常", e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }

    /**
     * 根据支付单号或订单号修改支付方式
     * @param paySn 支付单号
     * @param newPayId 支付方式id
     * @param actionUser 操作人
     * @return
     */
    @PostMapping("/changeOrderPayMethod")
    public ReturnInfo changeOrderPayMethod(@RequestParam(name="paySn") String paySn, @RequestParam(name="newPayId") Integer newPayId,
                                           @RequestParam(name="actionUser") String actionUser) {
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setMessage("失败");

        try {
            returnInfo = payService.changeOrderPayMethod(paySn, newPayId, actionUser);
        } catch (Exception e) {
            logger.error("changeOrderPayMethod->paySn:" + paySn + ",newPayId:" + newPayId + ",actionUser:" + actionUser + "异常", e);
            returnInfo.setMessage("操作异常");
        }

        return returnInfo;
    }
}
