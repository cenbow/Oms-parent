package com.work.shop.oms.order.feign;

import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign订单申请服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface ApplyManagementService {

    /**
     * 根据退换货申请单号, 获取申请日志
     * @param returnChangeSn 申请单号
     * @return OmsBaseResponse<GoodsReturnChangeAction>
     */
    @PostMapping("/order/findActionByReturnChangeSn")
    OmsBaseResponse<GoodsReturnChangeAction> findActionByReturnChangeSn(@RequestParam(name="returnChangeSn") String returnChangeSn);

    /**
     * 根据申请单id,获取申请单详情
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChange>
     */
    @PostMapping("/order/findGoodsReturnChangeById")
    OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeById(OmsBaseRequest<ApplyItem> request);

    /**
     * 修改申请单状态
     * @param request 请求参数
     * @return OmsBaseResponse<String>
     */
    @PostMapping("/order/updateStatusBatch")
    OmsBaseResponse<String> updateStatusBatch(OmsBaseRequest<ApplyItem> request);

    /**
     * 根据订单号，查询退换货申请单
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChange>
     */
    @PostMapping("/order/findGoodsReturnChangeByOrderSn")
    OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeByOrderSn(OmsBaseRequest<ApplyItem> request);

    /**
     * 根据订单号，获取退换货申请单详情
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChangeInfoVO>
     */
    @PostMapping("/order/findGoodsReturnChangeBySn")
    OmsBaseResponse<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(OmsBaseRequest<ApplyItem> request);
}
