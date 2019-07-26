package com.work.shop.oms.order.feign;

import com.work.shop.oms.bean.OrderReturnBean;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.vo.ReturnGoodsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * feign退单管理服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface ReturnManagementService {

    /**
     * 退单详情
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemGet")
    ReturnManagementResponse returnItemGet(ReturnManagementRequest request);

    /**
     * 退单创建初始化
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemCreateInit")
    ReturnManagementResponse returnItemCreateInit(ReturnManagementRequest request);

    /**
     * 退单沟通
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemCommunicate")
    ReturnManagementResponse returnItemCommunicate(ReturnManagementRequest request);

    /**
     * 查询退单日志
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/getReturnItemLog")
    ReturnManagementResponse getReturnItemLog(ReturnManagementRequest request);

    /**
     * 退单创建
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemCreate")
    ReturnManagementResponse returnItemCreate(ReturnManagementRequest request);

    /**
     * 退单锁定
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemLock")
    ReturnManagementResponse returnItemLock(ReturnManagementRequest request);

    /**
     * 退单解锁
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemUnLock")
    ReturnManagementResponse returnItemUnLock(ReturnManagementRequest request);

    /**
     * 退单确认
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemConfirm")
    ReturnManagementResponse returnItemConfirm(ReturnManagementRequest request);

    /**
     * 退单作废
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemCancel")
    ReturnManagementResponse returnItemCancel(ReturnManagementRequest request);

    /**
     * 退单完成
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemCompleted")
    ReturnManagementResponse returnItemCompleted(ReturnManagementRequest request);

    /**
     * 退单入库
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemStorage")
    ReturnManagementResponse returnItemStorage(ReturnManagementRequest request);

    /**
     * 退单结算
     * @param request 请求参数
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnItemSettlement")
    ReturnManagementResponse returnItemSettlement(ReturnManagementRequest request);

    /**
     * 退单待入库明细
     * @param request 请求参数
     * @return OmsBaseResponse<ReturnGoodsVO>
     */
    @PostMapping("/order/returnWaitStorageItem")
    OmsBaseResponse<ReturnGoodsVO> returnWaitStorageItem(ReturnManagementRequest request);

    /**
     * 退单退款完成
     * @param request 请求参数
     * 退单退款完成  退单号、实际退款金额、操作人
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnRefundCompleted")
    ReturnManagementResponse returnRefundCompleted(ReturnManagementRequest request);

    /**
     * 手动退款
     * @param request
     * @return
     */
    @PostMapping("/order/manualRefund")
    ReturnManagementResponse manualRefund(ReturnManagementRequest request);

    /**
     * 退单对账单已生成
     * @param request
     * @return ReturnManagementResponse
     */
    @PostMapping("/order/returnOrderBillCompleted")
    ReturnManagementResponse returnOrderBillCompleted(ReturnManagementRequest request);

    /**
     * 订单退款操作
     * @param orderReturnBean
     */
    @PostMapping("/order/doOrderReturnMoneyByCommon")
    void doOrderReturnMoneyByCommon(OrderReturnBean orderReturnBean);
}
