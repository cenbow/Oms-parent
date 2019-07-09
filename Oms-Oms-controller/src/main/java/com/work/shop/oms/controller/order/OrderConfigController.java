package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.OrderPeriodInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.service.OrderConfigService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单周期配置
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderConfigController {

    private static final Logger logger = Logger.getLogger(OrderConfigController.class);

    @Resource
    private OrderConfigService orderConfigService;

    /**
     * 获取订单周期明细列表
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/getOrderPeriodInfoList")
    public OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfoList() {

        OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
        returnBean.setSuccess(false);
        try {
            returnBean = orderConfigService.getOrderPeriodInfoList();
        } catch (Exception e) {
            logger.error("获取订单周期配置列表异常", e);
            returnBean.setMessage("查询异常");
        }
        return returnBean;
    }

    /**
     * 获取订单周期明细
     * @param request 参数
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/getOrderPeriodInfo")
    public OmsBaseResponse<OrderPeriodInfo> getOrderPeriodInfo(@RequestBody OmsBaseRequest<OrderPeriodInfo> request) {
        OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderConfigService.getOrderPeriodInfo(request);
        } catch (Exception e) {
            logger.error("根据:" + JSONObject.toJSONString(request) + "查询明细异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 更新订单周期明细
     * @param request 参数
     * @return OmsBaseResponse<OrderPeriodInfo>
     */
    @PostMapping("/updateOrderPeriodInfo")
    public OmsBaseResponse<OrderPeriodInfo> updateOrderPeriodInfo(@RequestBody OmsBaseRequest<OrderPeriodInfo> request) {
        OmsBaseResponse<OrderPeriodInfo> returnBean = new OmsBaseResponse<OrderPeriodInfo>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderConfigService.updateOrderPeriodInfo(request);
        } catch (Exception e) {
            logger.error("根据:" + JSONObject.toJSONString(request) + "更新明细异常", e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
}
