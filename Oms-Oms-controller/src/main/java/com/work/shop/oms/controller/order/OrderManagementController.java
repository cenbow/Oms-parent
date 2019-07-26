package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ShippingInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderManagementResponse;
import com.work.shop.oms.order.service.OrderManagementService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *订单管理
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderManagementController {

    private static final Logger logger = Logger.getLogger(OrderManagementController.class);

    @Resource
    private OrderManagementService orderManagementService;

    /**
     * 订单详情查询
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderItemGet")
    public OrderManagementResponse orderItemGet(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderItemGet(request);
        } catch (Exception e) {
            logger.error("订单详情查询异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 沟通
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/communicate")
    public OrderManagementResponse communicate(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.communicate(request);
        } catch (Exception e) {
            logger.error("沟通异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 查询订单日志
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/getOrderItemLog")
    public OrderManagementResponse getOrderItemLog(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.getOrderItemLog(request);
        } catch (Exception e) {
            logger.error("查询订单日志异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 自提核销
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/gotWriteOff")
    public OrderManagementResponse gotWriteOff(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.gotWriteOff(request);
        } catch (Exception e) {
            logger.error("自提核销异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单拣货
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderDistributePicking")
    public OrderManagementResponse orderDistributePicking(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderDistributePicking(request);
        } catch (Exception e) {
            logger.error("订单拣货异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单拣货完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderDistributePickUp")
    public OrderManagementResponse orderDistributePickUp(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderDistributePickUp(request);
        } catch (Exception e) {
            logger.error("订单拣货完成异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单取消通知接收
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderDistributeCancel")
    public OrderManagementResponse orderDistributeCancel(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderDistributeCancel(request);
        } catch (Exception e) {
            logger.error("订单取消异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 承运商列表
     * @param request 请求参数
     * @return OmsBaseResponse<List<ShippingInfo>>
     */
    @PostMapping("/getSystemShipping")
    public OmsBaseResponse<List<ShippingInfo>> getSystemShipping(@RequestBody OmsBaseRequest<ShippingInfo> request) {
        OmsBaseResponse<List<ShippingInfo>> returnBean = new OmsBaseResponse<List<ShippingInfo>>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.getSystemShipping(request);
        } catch (Exception e) {
            logger.error("承运商列表异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 发送自提码短信
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/sendGotCode")
    public OrderManagementResponse sendGotCode(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.sendGotCode(request);
        } catch (Exception e) {
            logger.error("发送自提码短信异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单结算
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderSettlement")
    public OrderManagementResponse orderSettlement(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderSettlement(request);
        } catch (Exception e) {
            logger.error("订单结算异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单正常单
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderNormal")
    public OrderManagementResponse orderNormal(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderNormal(request);
        } catch (Exception e) {
            logger.error("订单正常单异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单问题单
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderQuestion")
    public OrderManagementResponse orderQuestion(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderQuestion(request);
        } catch (Exception e) {
            logger.error("订单问题单异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单未付款
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderUnPay")
    public OrderManagementResponse orderUnPay(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderUnPay(request);
        } catch (Exception e) {
            logger.error("订单未付款异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单已付款
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderPay")
    public OrderManagementResponse orderPay(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderPay(request);
        } catch (Exception e) {
            logger.error("订单已付款异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单确认
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderConfirm")
    public OrderManagementResponse orderConfirm(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderConfirm(request);
        } catch (Exception e) {
            logger.error("订单确认异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单未确认
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderUnConfirm")
    public OrderManagementResponse orderUnConfirm(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderUnConfirm(request);
        } catch (Exception e) {
            logger.error("订单确认异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单锁定
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderLock")
    public OrderManagementResponse orderLock(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderLock(request);
        } catch (Exception e) {
            logger.error("订单锁定异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单解锁
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderUnLock")
    public OrderManagementResponse orderUnLock(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderUnLock(request);
        } catch (Exception e) {
            logger.error("订单解锁异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单审单完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderReviewCompleted")
    public OrderManagementResponse orderReviewCompleted(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderReviewCompleted(request);
        } catch (Exception e) {
            logger.error("订单审单完成异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
    
    /**
     * 订单审单驳回
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderReviewReject")
    public OrderManagementResponse orderReviewReject(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderReviewReject(request);
        } catch (Exception e) {
            logger.error("订单审单驳回异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 账期支付扣款成功
     * @param request
     * @return OrderManagementResponse
     */
    @PostMapping("/orderPayPeriodSuccess")
    public OrderManagementResponse orderPayPeriodSuccess(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderPayPeriodSuccess(request);
        } catch (Exception e) {
            logger.error("账期支付扣款成功异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 账期支付扣款失败
     * @param request
     * @return OrderManagementResponse
     */
    @PostMapping("/orderPayPeriodError")
    public OrderManagementResponse orderPayPeriodError(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderPayPeriodError(request);
        } catch (Exception e) {
            logger.error("账期支付扣款失败处理异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 合同签章完成
     * @param request 请求参数
     * @return OrderManagementResponse
     */
    @PostMapping("/orderSignCompleted")
    public OrderManagementResponse orderSignCompleted(@RequestBody OrderManagementRequest request) {
        OrderManagementResponse returnBean = new OrderManagementResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderManagementService.orderSignCompleted(request);
        } catch (Exception e) {
            logger.error("合同签章完成异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
}
