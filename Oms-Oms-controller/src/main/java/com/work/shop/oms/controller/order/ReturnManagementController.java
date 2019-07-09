package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.order.service.ReturnManagementService;
import com.work.shop.oms.vo.ReturnGoodsVO;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 退单管理
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class ReturnManagementController {

    private static final Logger logger = Logger.getLogger(ReturnManagementController.class);

    @Resource
    private ReturnManagementService returnManagementService;

    /**
     * 退单详情
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemGet")
    public ReturnManagementResponse returnItemGet(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemGet(request);
        } catch (Exception e) {
            logger.error("退单详情查询异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单创建初始化
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemCreateInit")
    public ReturnManagementResponse returnItemCreateInit(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemCreateInit(request);
        } catch (Exception e) {
            logger.error("退单创建初始化异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单沟通
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemCommunicate")
    public ReturnManagementResponse returnItemCommunicate(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemCommunicate(request);
        } catch (Exception e) {
            logger.error("退单沟通异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 查询退单日志
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/getReturnItemLog")
    public ReturnManagementResponse getReturnItemLog(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.getReturnItemLog(request);
        } catch (Exception e) {
            logger.error("查询退单日志异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单创建
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemCreate")
    public ReturnManagementResponse returnItemCreate(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemCreate(request);
        } catch (Exception e) {
            logger.error("退单创建异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单锁定
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemLock")
    public ReturnManagementResponse returnItemLock(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemLock(request);
        } catch (Exception e) {
            logger.error("退单锁定异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单解锁
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemUnLock")
    public ReturnManagementResponse returnItemUnLock(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemUnLock(request);
        } catch (Exception e) {
            logger.error("退单解锁异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单确认
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemConfirm")
    public ReturnManagementResponse returnItemConfirm(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemConfirm(request);
        } catch (Exception e) {
            logger.error("退单确认异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单作废
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemCancel")
    public ReturnManagementResponse returnItemCancel(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemCancel(request);
        } catch (Exception e) {
            logger.error("退单作废异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单完成
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemCompleted")
    public ReturnManagementResponse returnItemCompleted(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemCompleted(request);
        } catch (Exception e) {
            logger.error("退单完成异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单入库
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemStorage")
    public ReturnManagementResponse returnItemStorage(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemStorage(request);
        } catch (Exception e) {
            logger.error("退单入库异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单结算
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnItemSettlement")
    public ReturnManagementResponse returnItemSettlement(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnItemSettlement(request);
        } catch (Exception e) {
            logger.error("退单结算异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 退单待入库明细
     * @param request 请求参数
     * @return OmsBaseResponse<ReturnGoodsVO>
     */
    @PostMapping("/returnWaitStorageItem")
    public OmsBaseResponse<ReturnGoodsVO> returnWaitStorageItem(@RequestBody ReturnManagementRequest request) {
        OmsBaseResponse<ReturnGoodsVO> returnBean = new OmsBaseResponse<ReturnGoodsVO>();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnWaitStorageItem(request);
        } catch (Exception e) {
            logger.error("退单待入库明细异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单退款完成
     * @param request 请求参数
     * 退单退款完成  退单号、实际退款金额、操作人
     * @return ReturnManagementResponse
     */
    @PostMapping("/returnRefundCompleted")
    public ReturnManagementResponse returnRefundCompleted(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.returnRefundCompleted(request);
        } catch (Exception e) {
            logger.error("退单退款完成异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 手动退款
     * @param request
     * @return
     */
    @PostMapping("/manualRefund")
    public ReturnManagementResponse manualRefund(@RequestBody ReturnManagementRequest request) {
        ReturnManagementResponse returnBean = new ReturnManagementResponse();
        returnBean.setSuccess(false);
        try {
            returnBean = returnManagementService.manualRefund(request);
        } catch (Exception e) {
            logger.error("手动退款异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }
}
