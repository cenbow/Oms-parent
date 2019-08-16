package com.work.shop.oms.api.orderinfo.feign;

import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.param.bean.CreateGoodsReturnChange;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnChangeGoodsBean;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单退换服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface BgReturnChangeService {

    /**
     * 根据退换申请单号查询明细
     * @param returnChangeGoodsBean 查询条件
     * @return ApiReturnData
     */
    @PostMapping("/order/getGoodsReturnChangeDetailList")
    ApiReturnData<ReturnChangeDetailInfo> getGoodsReturnChangeDetailList(ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 创建订单商品退换货信息
     * @param createGoodsReturnChange 退换信息
     * @return ReturnInfo
     */
    @PostMapping("/order/createGoodsReturnChangeNew")
    ReturnInfo<Boolean> createGoodsReturnChangeNew(CreateGoodsReturnChange createGoodsReturnChange);

    /**
     * 创建订单商品退换货信息
     * @param actionUser 操作人
     * @param actionNote 操作备注helper
     * @param content 申请内容
     * @param siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/order/createGoodsReturnChange")
    ReturnInfo<Boolean> createGoodsReturnChange(@RequestParam(name="actionUser") String actionUser,
                                                       @RequestParam(name="actionNote") String actionNote,
                                                       @RequestParam(name="content") String content,
                                                       @RequestParam(name="siteCode") String siteCode);

    /**
     * 查询换单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @PostMapping("/order/getExchangeOrderList")
    ApiReturnData<Paging<OrderMobile>> getExchangeOrderList(MobileParams mobileParams);

    /**
     * 查询退单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @PostMapping("/order/getOrderReturnList")
    ApiReturnData<Paging<ReturnMobile>> getOrderReturnList(MobileParams mobileParams);

    /**
     * 查询申请单详细
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @PostMapping("/order/getGoodsReturnChange")
    ApiReturnData<GoodsReturnChangeMobile> getGoodsReturnChange(MobileParams mobileParams);

    /**
     * 退换货申请单详情获得数据接口
     * @param orderSn 订单编码
     * @param userId 用户id
     * @param siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/order/getBGGoodsReturnInfo")
    ApiReturnData<GoodsReturnDetailInfo> getBGGoodsReturnInfo(@RequestParam(name="orderSn") String orderSn,
                                                                     @RequestParam(name="userId") String userId,
                                                                     @RequestParam(name="siteCode") String siteCode);

    /**
     * 退换货申请单列表获得数据接口
     * @param searchParam 查询参数
     * @param siteCode 站点
     * @return ApiReturnData
     */
    @PostMapping("/order/getBGGoodsReturnPageList")
    ApiReturnData<Paging<GoodsReturnPageInfo>> getBGGoodsReturnPageList(@RequestParam(name="searchParam") String searchParam,
                                                                               @RequestParam(name="siteCode") String siteCode);

    /**
     * 退货回寄快递消息获取接口
     * @param returnSn 退单编码
     * @param returnExpress 退货快递
     * @param returnInvoiceNo 退货单号
     * @param returnExpressImg 退货图片
     * @param siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/order/setOrderReturnShipInfo")
    ReturnInfo<Boolean> setOrderReturnShipInfo(@RequestParam(name="returnSn") String returnSn, @RequestParam(name="returnExpress") String returnExpress,
                                                      @RequestParam(name="returnInvoiceNo") String returnInvoiceNo,
                                                      @RequestParam(name="returnExpressImg") String returnExpressImg, @RequestParam(name="siteCode") String siteCode);

    /**
     * 取消申请单
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    @PostMapping("/order/cancelGoodsReturnChange")
    GoodsReturnChangeReturnInfo cancelGoodsReturnChange(@RequestParam(name="channelCode") String channelCode,
                                                        @RequestParam(name="returnChangeSn") String returnChangeSn,
                                                        @RequestParam(name="siteCode") String siteCode,
                                                        @RequestParam(name="actionUser") String actionUser);

    /**
     * 删除申请单
     * @param returnChangeGoodsBean
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/deleteGoodsReturnChange")
    public ApiReturnData<Boolean> deleteGoodsReturnChange(@RequestBody ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 沟通申请单
     * @param returnChangeGoodsBean
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/communicationChange")
    public ApiReturnData<Boolean> communicationChange(@RequestBody ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 申请单列表页获取申请单类型数量
     * @param param
     * @return
     */
    @PostMapping("/order/getChangeOrderTypeNum")
    public ApiReturnData<UserOrderTypeNum> getChangeOrderTypeNum(@RequestBody PageListParam param);

    /**
     * 申请单审核（自动创建退单并确认）
     * @param returnChangeGoodsBean
     * @return
     */
    @PostMapping("/order/examinationPassed")
    public ApiReturnData<Boolean> examinationPassed(@RequestBody CreateGoodsReturnChange returnChangeGoodsBean);

    /**
     * 申请单驳回
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    @PostMapping("/order/rejectGoodsReturnChange")
    public GoodsReturnChangeReturnInfo rejectGoodsReturnChange(@RequestParam(name="channelCode") String channelCode,
                                                               @RequestParam(name="returnChangeSn") String returnChangeSn,
                                                               @RequestParam(name="siteCode") String siteCode);
}
