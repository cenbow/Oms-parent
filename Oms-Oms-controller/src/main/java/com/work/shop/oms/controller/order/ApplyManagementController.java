package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.service.ApplyManagementService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单售后申请管理
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class ApplyManagementController {

    private static final Logger logger = Logger.getLogger(ApplyManagementController.class);

    @Resource
    private ApplyManagementService applyManagementService;

    /**
     * 根据退换货申请单号, 获取申请日志
     * @param returnChangeSn 申请单号
     * @return OmsBaseResponse<GoodsReturnChangeAction>
     */
    @PostMapping("/findActionByReturnChangeSn")
    public OmsBaseResponse<GoodsReturnChangeAction> findActionByReturnChangeSn(@RequestParam(name="returnChangeSn") String returnChangeSn) {
        OmsBaseResponse<GoodsReturnChangeAction> returnBean = new OmsBaseResponse<GoodsReturnChangeAction>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.findActionByReturnChangeSn(returnChangeSn);
        } catch (Exception e) {
            logger.error("根据退换货申请单号, 获取申请日志异常:" + returnChangeSn, e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 根据申请单id,获取申请单详情
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChange>
     */
    @PostMapping("/findGoodsReturnChangeById")
    public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeById(@RequestBody OmsBaseRequest<ApplyItem> request) {
        OmsBaseResponse<GoodsReturnChange> returnBean = new OmsBaseResponse<GoodsReturnChange>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.findGoodsReturnChangeById(request);
        } catch (Exception e) {
            logger.error("根据申请单id,获取申请单详情异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 修改申请单状态
     * @param request 请求参数
     * @return OmsBaseResponse<String>
     */
    @PostMapping("/updateStatusBatch")
    public OmsBaseResponse<String> updateStatusBatch(@RequestBody OmsBaseRequest<ApplyItem> request) {
        OmsBaseResponse<String> returnBean = new OmsBaseResponse<String>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.updateStatusBatch(request);
        } catch (Exception e) {
            logger.error("修改申请单状态异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 根据订单号，查询退换货申请单
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChange>
     */
    @PostMapping("/findGoodsReturnChangeByOrderSn")
    public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeByOrderSn(@RequestBody OmsBaseRequest<ApplyItem> request) {
        OmsBaseResponse<GoodsReturnChange> returnBean = new OmsBaseResponse<GoodsReturnChange>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.findGoodsReturnChangeByOrderSn(request);
        } catch (Exception e) {
            logger.error("根据订单号，查询退换货申请单异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 根据订单号，获取退换货申请单详情
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChangeInfoVO>
     */
    @PostMapping("/findGoodsReturnChangeBySn")
    public OmsBaseResponse<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(@RequestBody OmsBaseRequest<ApplyItem> request) {
        OmsBaseResponse<GoodsReturnChangeInfoVO> returnBean = new OmsBaseResponse<GoodsReturnChangeInfoVO>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.findGoodsReturnChangeBySn(request);
        } catch (Exception e) {
            logger.error("根据订单号，获取退换货申请单详情异常:" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 更新订单商品价格
     * @param applyItem
     * @return
     */
    @PostMapping("/updateOrderPrice")
    public OmsBaseResponse<Boolean> updateOrderPrice(@RequestBody ApplyItem applyItem) {
        OmsBaseResponse<Boolean> returnBean = new OmsBaseResponse<Boolean>();
        returnBean.setSuccess(false);

        try {
            returnBean = applyManagementService.updateOrderPrice(applyItem);
        } catch (Exception e) {
            logger.error("更新订单商品价格异常:" + JSONObject.toJSONString(applyItem), e);
            returnBean.setMessage("更新订单商品价格异常");
        }

        return returnBean;
    }


}
