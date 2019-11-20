package com.work.shop.oms.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.OrderContractRequest;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.channel.bean.OfflineStoreInfo;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.OrderGoodsQuery;
import com.work.shop.oms.common.bean.OrderGoodsSaleBean;
import com.work.shop.oms.order.request.CustomDefineQueryRequest;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.CustomDefineQueryResponse;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.order.service.OrderQueryService;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.OrderShipVO;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 订单查询
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderQueryController {

    private static final Logger logger = Logger.getLogger(OrderQueryController.class);

    @Resource
    private OrderQueryService orderQueryService;

    /**
     * 订单列表查询
     * @param request 查询参数
     * @return OrderQueryResponse
     */
    @PostMapping("/orderQuery")
    public OrderQueryResponse orderQuery(@RequestBody OrderQueryRequest request) {
        OrderQueryResponse returnBean = new OrderQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderQuery(request);
        } catch (Exception e) {
            logger.error("订单列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }
        return returnBean;
    }

    /**
     * 退单列表查询
     * @param request 查询参数
     * @return OmsBaseResponse<OrderReturnListVO>
     */
    @PostMapping("/orderReturnQuery")
    public OmsBaseResponse<OrderReturnListVO> orderReturnQuery(@RequestBody OmsBaseRequest<OrderReturnListVO> request) {
        OmsBaseResponse<OrderReturnListVO> returnBean = new OmsBaseResponse<OrderReturnListVO>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderReturnQuery(request);
        } catch (Exception e) {
            logger.error("退单列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单申请列表查询
     * @param request 查询参数
     * @return OmsBaseResponse<GoodsReturnChangeBean>
     */
    @PostMapping("/orderReturnApplyQuery")
    public OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQuery(@RequestBody OmsBaseRequest<GoodsReturnChangeVO> request) {
        OmsBaseResponse<GoodsReturnChangeBean> returnBean = new OmsBaseResponse<GoodsReturnChangeBean>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderReturnApplyQuery(request);
        } catch (Exception e) {
            logger.error("申请列表查询异常" + JSONObject.toJSONString(request));
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单申请单列表(新)
     * @param request 请求参数
     * @return OmsBaseResponse<GoodsReturnChangeBean>
     */
    @PostMapping("/orderReturnApplyQueryNew")
    public OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQueryNew(@RequestBody OmsBaseRequest<GoodsReturnChangeVO> request) {
        OmsBaseResponse<GoodsReturnChangeBean> returnBean = new OmsBaseResponse<GoodsReturnChangeBean>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderReturnApplyQueryNew(request);
        } catch (Exception e) {
            logger.error("申请列表查询异常" + JSONObject.toJSONString(request));
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 自定义编码列表
     * @param request 查询参数
     * @return CustomDefineQueryResponse
     */
    @PostMapping("/customDefineQuery")
    public CustomDefineQueryResponse customDefineQuery(@RequestBody CustomDefineQueryRequest request) {
        CustomDefineQueryResponse returnBean = new CustomDefineQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.customDefineQuery(request);
        } catch (Exception e) {
            logger.error("自定义编码列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 自提订单列表查询
     * @param request 查询参数
     * @return OrderQueryResponse
     */
    @PostMapping("/pickUpOrderQuery")
    public OrderQueryResponse pickUpOrderQuery(@RequestBody OrderQueryRequest request) {
        OrderQueryResponse returnBean = new OrderQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.pickUpOrderQuery(request);
        } catch (Exception e) {
            logger.error("自提订单列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 线下店铺列表
     * @param request 查询参数
     * @return OmsBaseResponse<OfflineStoreInfo>
     */
    @PostMapping("/offlineStoreManagement")
    public OmsBaseResponse<OfflineStoreInfo> offlineStoreManagement(@RequestBody OmsBaseRequest<OfflineStoreInfo> request) {
        OmsBaseResponse<OfflineStoreInfo> returnBean = new OmsBaseResponse<OfflineStoreInfo>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.offlineStoreManagement(request);
        } catch (Exception e) {
            logger.error("线下店铺列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 配送订单列表查询
     * @param request 查询参数
     * @return OmsBaseResponse<OrderRiderDistributeLog>
     */
    @PostMapping("/riderOrderQuery")
    public OmsBaseResponse<OrderRiderDistributeLog> riderOrderQuery(@RequestBody OrderQueryRequest request) {
        OmsBaseResponse<OrderRiderDistributeLog> returnBean = new OmsBaseResponse<OrderRiderDistributeLog>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.riderOrderQuery(request);
        } catch (Exception e) {
            logger.error("配送订单列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 获取骑手配送详情
     * @param request 查询参数
     * @return OmsBaseResponse<OrderRiderDistributeLog>
     */
    @PostMapping("/riderOrderDetail")
    public OmsBaseResponse<OrderRiderDistributeLog> riderOrderDetail(@RequestBody OrderQueryRequest request) {
        OmsBaseResponse<OrderRiderDistributeLog> returnBean = new OmsBaseResponse<OrderRiderDistributeLog>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.riderOrderDetail(request);
        } catch (Exception e) {
            logger.error("骑手配送详情查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 通知骑手配送取消
     * @param request 请求参数
     * @return OmsBaseResponse<Boolean>
     */
    @PostMapping("/riderOrderCancel")
    public OmsBaseResponse<Boolean> riderOrderCancel(@RequestBody OrderQueryRequest request) {
        OmsBaseResponse<Boolean> returnBean = new OmsBaseResponse<Boolean>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.riderOrderCancel(request);
        } catch (Exception e) {
            logger.error("通知骑手配送取消异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 通知订单下发到骑手平台
     * @param request 请求参数
     * @return OmsBaseResponse<Boolean>
     */
    @PostMapping("/riderOrderSend")
    public OmsBaseResponse<Boolean> riderOrderSend(@RequestBody OrderQueryRequest request) {
        OmsBaseResponse<Boolean> returnBean = new OmsBaseResponse<Boolean>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.riderOrderSend(request);
        } catch (Exception e) {
            logger.error("通知订单下发到骑手平台异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 订单待结算列表查询
     * @param request 请求参数
     * @return OmsBaseResponse<OrderItem>
     */
    @PostMapping("/waitSettleOrderQuery")
    public OmsBaseResponse<OrderItem> waitSettleOrderQuery(@RequestBody OmsBaseRequest<OrderItem> request) {
        OmsBaseResponse<OrderItem> returnBean = new OmsBaseResponse<OrderItem>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.waitSettleOrderQuery(request);
        } catch (Exception e) {
            logger.error("订单待结算列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退单待结算列表
     * @param request 请求参数
     * @return OmsBaseResponse<OrderReturnListVO>
     */
    @PostMapping("/waitSettleReturnQuery")
    public OmsBaseResponse<OrderReturnListVO> waitSettleReturnQuery(@RequestBody OmsBaseRequest<OrderReturnListVO> request) {
        OmsBaseResponse<OrderReturnListVO> returnBean = new OmsBaseResponse<OrderReturnListVO>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.waitSettleReturnQuery(request);
        } catch (Exception e) {
            logger.error("退单待结算列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 查询商品销售记录列表
     * @param request 请求参数
     * @return OmsBaseResponse<OrderGoodsSaleBean>
     */
    @PostMapping("/orderGoodsSaleQuery")
    public OmsBaseResponse<OrderGoodsSaleBean> orderGoodsSaleQuery(@RequestBody OmsBaseRequest<OrderGoodsQuery> request) {
        OmsBaseResponse<OrderGoodsSaleBean> returnBean = new OmsBaseResponse<OrderGoodsSaleBean>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderGoodsSaleQuery(request);
        } catch (Exception e) {
            logger.error("商品销售记录列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 查询订单仓库配送列表
     * @param request 请求参数
     * @return OrderQueryResponse
     */
    @PostMapping("/orderDepotShipQuery")
    public OrderQueryResponse orderDepotShipQuery(@RequestBody OrderQueryRequest request) {
        OrderQueryResponse returnBean = new OrderQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderDepotShipQuery(request);
        } catch (Exception e) {
            logger.error("订单仓库配送列表查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 查询订单仓库配送单详情
     * @param request 请求参数
     * @return OmsBaseResponse<OrderShipVO>
     */
    @PostMapping("/queryOrderDepotShipDetail")
    public OmsBaseResponse<OrderShipVO> queryOrderDepotShipDetail(@RequestBody OrderQueryRequest request) {
        OmsBaseResponse<OrderShipVO> returnBean = new OmsBaseResponse<OrderShipVO>();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.queryOrderDepotShipDetail(request);
        } catch (Exception e) {
            logger.error("订单仓库配送单详情查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 请求处理仓库发送配送
     * @param param 请求参数
     */
    @PostMapping("/getOrderDistributeOut")
    public void getOrderDistributeOut(@RequestBody Map<String, Object> param) {
        try {
            orderQueryService.getOrderDistributeOut(param);
        } catch (Exception e) {
            logger.error("请求处理仓库发送配送异常", e);
        }
    }

    /**
     * 订单合同列表查询
     * @param request 查询参数
     * @return OrderQueryResponse
     */
    @PostMapping("/orderContractQuery")
    public OrderQueryResponse orderContractQuery(@RequestBody OrderContractRequest request) {
        OrderQueryResponse returnBean = new OrderQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderContractQuery(request);
        } catch (Exception e) {
            logger.error("订单合同列表查询查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 订单列表导出查询
     * @param request
     * @return
     */
    @PostMapping("/orderQueryByExport")
    public OrderQueryResponse orderQueryByExport(@RequestBody OrderQueryRequest request) {
        OrderQueryResponse returnBean = new OrderQueryResponse();
        returnBean.setSuccess(false);

        try {
            returnBean = orderQueryService.orderQueryByExport(request);
        } catch (Exception e) {
            logger.error("订单列表导出查询异常" + JSONObject.toJSONString(request), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }
}
