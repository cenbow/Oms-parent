package com.work.shop.oms.api.orderinfo.feign;

import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.response.OmsBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 订单信息服务接口
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface BgOrderInfoService {

    /**
     * 平台前台查询用户订单列表
     * @param searchParam 订单列表查询参数
     * @return ApiReturnData<Paging<OrderPageInfo>> 订单列表
     */
    @PostMapping("/order/orderPageList")
    public ApiReturnData<Paging<OrderPageInfo>> orderPageList(PageListParam searchParam);

    /**
     * 平台前台查询用户退单列表
     * @param searchParam 查询参数
     * @return ApiReturnData<Paging<OrderReturnPageInfo>> 退单列表
     */
    @PostMapping("/order/orderReturnPageList")
    public ApiReturnData<Paging<OrderReturnPageInfo>> orderReturnPageList(PageListParam searchParam);

    /**
     * 查询退单详情
     * @param searchParam 退单编码
     * @return ApiReturnData<OrderReturnDetailInfo> 退单详情
     */
    @PostMapping("/order/orderReturnDetailNew")
    public ApiReturnData<OrderReturnDetailInfo> orderReturnDetailNew(PageListParam searchParam);

    /**
     * 查询换单详情
     * @param searchParam 查询参数
     * @return ApiReturnData<OrderExchangesDetailInfo>
     */
    @PostMapping("/order/orderExchangesDetailNew")
    public ApiReturnData<OrderExchangesDetailInfo> orderExchangesDetailNew(PageListParam searchParam);

    /**
     * 平台前台查询用户订单详情
     * @param searchParam orderSn isHistory userId siteCode
     * @return ApiReturnData<OrderDetailInfo>
     */
    @PostMapping("/order/orderInfoDetailNew")
    public ApiReturnData<OrderDetailInfo> orderInfoDetailNew(PageListParam searchParam);

    /**
     * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
     * @param searchParam orderSns siteCode
     * @return ApiReturnData
     */
    @PostMapping("/order/getOrderGoodsByOrderSnNew")
    public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByOrderSnNew(PageListParam searchParam);

    /**
     * 通过支付单号获取订单商品消息（app付款校验使用,平台pc统计商品用）
     * @param searchParam 查询参数
     * @return ApiReturnData
     */
    @PostMapping("/order/getOrderGoodsByPaySnNew")
    public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByPaySnNew(PageListParam searchParam);

    /**
     * 统计用户订单数量
     * @param searchParam 查询参数
     * 			userId 用户id
     * 			siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/order/getUserOrderTypeNew")
    public ApiReturnData<UserOrderTypeNum> getUserOrderTypeNew(PageListParam searchParam);

    /**
     * 根据条件查询用户（平台app用户升级调度任务使用）
     * @param searchParam 查询参数
     * @return ApiReturnData<Paging<String>>
     */
    @PostMapping("/order/getOrderInfoUser")
    public ApiReturnData<Paging<String>> getOrderInfoUser(SearchParam searchParam);

    /**
     * 平台活动使用
     * @param activitySearchParam 查询参数
     * @return ApiReturnData
     */
    @PostMapping("/order/getOrderSnByUser")
    public ApiReturnData getOrderSnByUser(ActivitySearchParam activitySearchParam);

    /**
     * 取消订单
     * @param searchParam 查询参数
     *          orderSn 订单编码
     * 	        userId 用户id
     * 		    siteCode 站点编码
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/cancelOrderByMasterSnNew")
    public ApiReturnData<Boolean> cancelOrderByMasterSnNew(PageListParam searchParam);

    /**
     * 根据支付单号查询主订单号
     * @param searchParam 查询参数
     * 			paySn 支付单号
     * 			siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/order/getMasterOrderSnByPaySnNew")
    public ApiReturnData getMasterOrderSnByPaySnNew(PageListParam searchParam);

    /**
     * 修改订单用户（平台）
     * @param searchParam
     *      userId 原用户
     *      newUserId 新用户
     *      siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/order/changeOrderUserNew")
    public ReturnInfo changeOrderUserNew(PageListParam searchParam);

    /**
     * 订单评论接口（平台）
     * @param searchParam
     *      orderSn 订单编码
     *      flag 标志
     *      siteCode 站点编码
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/order/orderReviewNew")
    public ReturnInfo<Boolean> orderReviewNew(PageListParam searchParam);

    /**
     * 确认收货接口
     * @param searchParam 查询参数
     *      orderSn 订单号
     *      invoiceNo 快递单号
     *      userId 用户id
     *      siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/order/confirmReceiptNew")
    public ReturnInfo<Boolean> confirmReceiptNew(PageListParam searchParam);

    /**
     * 获取订单简易消息
     * @param searchParam 查询参数
     *      userId 用户id
     *      siteCode 站点编码
     * @return ApiReturnData<List<MasterOrderInfo>>
     */
    @PostMapping("/order/getOrderSimpleInfoByUserNew")
    public ApiReturnData<List<MasterOrderInfo>> getOrderSimpleInfoByUserNew(PageListParam searchParam);

    /**
     * 平台前台查询用户未支付订单
     * @param searchParam userId siteCode
     * @return ApiReturnData<List<String>>
     */
    @PostMapping("/order/queryNoPayOrder")
    public ApiReturnData queryNoPayOrder(PageListParam searchParam);

    /**
     * 平台前台查询用户限购订单
     * @param searchParam 查询参数
     *      userId 用户id
     *      siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/order/queryRestrictionOrder")
    public ApiReturnData<List<OrderGoodsInfo>> queryRestrictionOrder(PageListParam searchParam);

    /**
     * 查询地区信息
     * @param searchParam 查询参数
     * @return OmsBaseResponse<SystemRegionArea>
     */
    @PostMapping("/order/querySystemRegionArea")
    public OmsBaseResponse<SystemRegionArea> querySystemRegionArea(SystemRegionArea searchParam);

    /**
     * 删除订单
     * @param searchParam 查询参数
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/deleteMasterOrder")
    ApiReturnData<Boolean> deleteMasterOrder(@RequestBody PageListParam searchParam);

    /**
     * 删除退单
     * @param searchParam 查询参数
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/deleteReturnOrder")
    ApiReturnData<Boolean> deleteReturnOrder(@RequestBody PageListParam searchParam);

    /**
     * 申请已支付
     * @param searchParam 申请信息
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/applyOrderPay")
    ApiReturnData<Boolean> applyOrderPay(@RequestBody PageListParam searchParam);
}
