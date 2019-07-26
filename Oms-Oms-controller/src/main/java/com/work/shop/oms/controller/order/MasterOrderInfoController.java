package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoFinishBean;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.order.request.OmsRequest;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 主订单服务
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class MasterOrderInfoController {

    private static final Logger logger = Logger.getLogger(MasterOrderInfoController.class);

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

    @Resource
    private MasterOrderActionService masterOrderActionService;

    @Resource
    private MasterOrderInfoExtendService masterOrderInfoExtendService;

    /**
     * 生成订单
     * @param masterOrder 订单信息
     * @return OrderCreateReturnInfo
     */
    @PostMapping("/createOrder")
    public OrderCreateReturnInfo createOrder(@RequestBody MasterOrder masterOrder) {
        OrderCreateReturnInfo returnBean = new OrderCreateReturnInfo();

        try {
            returnBean = masterOrderInfoService.createOrder(masterOrder);
        } catch (Exception e) {
            logger.error("生成订单异常:" + JSONObject.toJSONString(masterOrder), e);
            returnBean.setMessage("生成订单异常");
        }

        return returnBean;
    }

    /**
     * 批量生成订单
     * @param masterOrders 订单信息列表
     * @return OrdersCreateReturnInfo
     */
    @PostMapping("/createOrders")
    public OrdersCreateReturnInfo createOrders(@RequestBody List<MasterOrder> masterOrders) {
        OrdersCreateReturnInfo returnBean = new OrdersCreateReturnInfo();

        try {
            returnBean = masterOrderInfoService.createOrders(masterOrders);
        } catch (Exception e) {
            logger.error("生成订单异常:" + JSONObject.toJSONString(masterOrders), e);
            returnBean.setMessage("生成订单异常");
        }

        return returnBean;
    }

    /**
     * 根据订单号查询订单信息
     * @param masterOrderSn 订单编码
     * @return MasterOrderInfo
     */
     @PostMapping("/getOrderInfoBySn")
     public MasterOrderInfo getOrderInfoBySn(@RequestParam(name="masterOrderSn") String masterOrderSn) {
         MasterOrderInfo masterOrderInfo = null;

         try {
             masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
         } catch (Exception e) {
             logger.error("通过订单编码:" + masterOrderSn + "异常", e);
         }

         return masterOrderInfo;
     }

    /**
     * 通过外部交易号, 获取对应的订单信息
     * @param outOrderSn 外部订单号
     * @return MasterOrderInfo
     */
    @PostMapping("/getOrderInfoByOutOrderSn")
    public MasterOrderInfo getOrderInfoByOutOrderSn(@RequestParam(name="outOrderSn") String outOrderSn) {
        MasterOrderInfo masterOrderInfo = null;

        try {
            masterOrderInfo = masterOrderInfoService.getOrderInfoByOutOrderSn(outOrderSn);
        } catch (Exception e) {
            logger.error("通过外部交易编码:" + outOrderSn + "异常", e);
        }

        return masterOrderInfo;
    }

    /**
     * 处理订单生成后续操作
     *
     * @param orderBean 订单信息
     */
    @PostMapping("/dealOther")
    public void dealOther(@RequestBody MasterOrderInfoFinishBean orderBean) {
        try {
            masterOrderInfoService.dealOther(orderBean.getMasterOrderSn(), orderBean.getValidateOrder(), orderBean.getOcpbStatus(), orderBean.getQustionType());
        } catch (Exception e) {
            logger.error("处理订单:" + JSONObject.toJSONString(orderBean) + "异常", e);
        }
    }

    /**
     * 添加订单日志
     *
     * @param request 订单日志信息
     */
    @PostMapping("/insertOrderActionBySn")
    public void insertOrderActionBySn(@RequestBody OmsRequest request) {
        try {
            masterOrderActionService.insertOrderActionBySn(request.getMasterOrderSn(), request.getActionNote(), request.getActionUser());
        } catch (Exception e) {
            logger.error("添加订单日志:" + JSONObject.toJSONString(request) + "异常", e);
        }
    }

    /**
     * 更新订单推送供应链状态
     * @param request 订单号
     */
    @PostMapping("/updatePushSupplyChain")
    public Boolean updatePushSupplyChain(@RequestBody OmsRequest request) {
        try {
            return masterOrderInfoExtendService.updatePushSupplyChain(request.getMasterOrderSn());
        } catch (Exception e) {
            logger.error(request.getMasterOrderSn() + "更新订单推送供应链状态:" + "异常", e);
        }
        return false;
    }
}
