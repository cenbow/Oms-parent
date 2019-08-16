package com.work.shop.oms.controller.orderinfo;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单信息查询
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class BgOrderInfoController {

    private static final Logger logger = Logger.getLogger(BgOrderInfoController.class);

    @Resource(name="bgOrderInfoService")
    private BGOrderInfoService bgOrderInfoService;

    /**
     * 平台前台查询用户订单列表
     * @param searchParam 订单列表查询参数
     * @return ApiReturnData 订单列表
     */
    @PostMapping("/orderPageList")
    public ApiReturnData<Paging<OrderPageInfo>> orderPageList(@RequestBody PageListParam searchParam) {

        ApiReturnData<Paging<OrderPageInfo>> returnBean = new ApiReturnData<Paging<OrderPageInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.orderPageList(searchParam);
        } catch (Exception e) {
            logger.error("查询用户订单列表异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询用户订单列表异常");
        }

        return returnBean;
    }

    /**
     * 平台前台查询用户退单列表
     * @param searchParam 查询参数
     * @return ApiReturnData 退单列表
     */
    @PostMapping("/orderReturnPageList")
    public ApiReturnData<Paging<OrderReturnPageInfo>> orderReturnPageList(@RequestBody PageListParam searchParam) {
        ApiReturnData<Paging<OrderReturnPageInfo>> returnBean = new ApiReturnData<Paging<OrderReturnPageInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.orderReturnPageList(searchParam);
        } catch (Exception e) {
            logger.error("查询用户退单列表异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询用户退单列表异常");
        }

        return returnBean;
    }

    /**
     * 查询退单详情
     * @param searchParam 退单编码
     * @return ApiReturnData 退单详情
     */
    @PostMapping("/orderReturnDetailNew")
    public ApiReturnData<OrderReturnDetailInfo> orderReturnDetailNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<OrderReturnDetailInfo> returnBean = new ApiReturnData<OrderReturnDetailInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.orderReturnDetailNew(searchParam);
        } catch (Exception e) {
            logger.error("查询退单详情异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询退单详情异常");
        }

        return returnBean;
    }

    /**
     * 查询换单详情
     * @param searchParam 查询参数
     * @return ApiReturnData 换单详情
     */
    @PostMapping("/orderExchangesDetailNew")
    public ApiReturnData<OrderExchangesDetailInfo> orderExchangesDetailNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<OrderExchangesDetailInfo> returnBean = new ApiReturnData<OrderExchangesDetailInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.orderExchangesDetailNew(searchParam);
        } catch (Exception e) {
            logger.error("查询换单详情异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询换单详情异常");
        }

        return returnBean;
    }

    /**
     * 平台前台查询用户订单详情
     * @param searchParam orderSn isHistory userId siteCode
     * @return ApiReturnData
     */
    @PostMapping("/orderInfoDetailNew")
    public ApiReturnData<OrderDetailInfo> orderInfoDetailNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<OrderDetailInfo> returnBean = new ApiReturnData<OrderDetailInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.orderInfoDetailNew(searchParam);
        } catch (Exception e) {
            logger.error("查询订单详情异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询订单详情异常");
        }

        return returnBean;
    }

    /**
     * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
     * @param searchParam orderSns siteCode
     * @return ApiReturnData
     */
    @PostMapping("/getOrderGoodsByOrderSnNew")
    public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByOrderSnNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<OrderShipInfo>> returnBean = new ApiReturnData<List<OrderShipInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getOrderGoodsByOrderSnNew(searchParam);
        } catch (Exception e) {
            logger.error("查询订单商品信息异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询订单商品信息异常");
        }

        return returnBean;
    }

    /**
     * 通过支付单号获取订单商品消息（app付款校验使用,平台pc统计商品用）
     * @param searchParam 查询参数
     * @return ApiReturnData
     */
    @PostMapping("/getOrderGoodsByPaySnNew")
    public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByPaySnNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<OrderShipInfo>> returnBean = new ApiReturnData<List<OrderShipInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getOrderGoodsByPaySnNew(searchParam);
        } catch (Exception e) {
            logger.error("查询订单商品信息异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询订单商品信息异常");
        }

        return returnBean;
    }

    /**
     * 统计用户订单数量
     * @param searchParam 查询参数
     * 			userId 用户id
     * 			siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/getUserOrderTypeNew")
    public ApiReturnData<UserOrderTypeNum> getUserOrderTypeNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<UserOrderTypeNum> returnBean = new ApiReturnData<UserOrderTypeNum>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getUserOrderTypeNew(searchParam);
        } catch (Exception e) {
            logger.error("查询用户订单数量异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询用户订单数量异常");
        }

        return returnBean;
    }

    /**
     * 根据条件查询用户（平台app用户升级调度任务使用）
     * @param searchParam 查询参数
     * @return ApiReturnData
     */
    @PostMapping("/getOrderInfoUser")
    public ApiReturnData<Paging<String>> getOrderInfoUser(@RequestBody SearchParam searchParam) {
        ApiReturnData<Paging<String>> returnBean = new ApiReturnData<Paging<String>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getOrderInfoUser(searchParam);
        } catch (Exception e) {
            logger.error("查询用户订单数量异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 平台活动使用
     * @param activitySearchParam 查询参数
     * @return ApiReturnData
     */
    @PostMapping("/getOrderSnByUser")
    public ApiReturnData<Paging<String>> getOrderSnByUser(@RequestBody ActivitySearchParam activitySearchParam) {
        ApiReturnData<Paging<String>> returnBean = new ApiReturnData<Paging<String>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getOrderSnByUser(activitySearchParam);
        } catch (Exception e) {
            logger.error("查询异常:" + JSONObject.toJSONString(activitySearchParam), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 取消订单
     * @param searchParam 查询参数
     *          orderSn 订单编码
     * 	        userId 用户id
     * 		    siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/cancelOrderByMasterSnNew")
    public ApiReturnData<Boolean> cancelOrderByMasterSnNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<Boolean> returnBean = new ApiReturnData<Boolean>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.cancelOrderByMasterSnNew(searchParam);
        } catch (Exception e) {
            logger.error("取消订单异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("取消订单异常");
        }

        return returnBean;
    }

    /**
     * 根据支付单号查询主订单号
     * @param searchParam 查询参数
     * 			paySn 支付单号
     * 			siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/getMasterOrderSnByPaySnNew")
    public ApiReturnData<List<String>> getMasterOrderSnByPaySnNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<String>> returnBean = new ApiReturnData<List<String>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getMasterOrderSnByPaySnNew(searchParam);
        } catch (Exception e) {
            logger.error("根据支付单号查询主订单号异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询订单异常");
        }

        return returnBean;
    }

    /**
     * 修改订单用户（平台）
     * @param searchParam
     *      userId 原用户
     *      newUserId 新用户
     *      siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/changeOrderUserNew")
    public ReturnInfo<Boolean> changeOrderUserNew(@RequestBody PageListParam searchParam) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();

        try {
            returnInfo = bgOrderInfoService.changeOrderUserNew(searchParam);
        } catch (Exception e) {
            logger.error("修改订单用户异常:" + JSONObject.toJSONString(searchParam), e);
            returnInfo.setMessage("修改订单用户异常");
        }

        return returnInfo;
    }

    /**
     * 订单评论接口（平台）
     * @param searchParam
     *      orderSn 订单编码
     *      flag 标志
     *      siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/orderReviewNew")
    public ReturnInfo<Boolean> orderReviewNew(@RequestBody PageListParam searchParam) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();

        try {
            returnInfo = bgOrderInfoService.orderReviewNew(searchParam);
        } catch (Exception e) {
            logger.error("订单评论接口异常:" + JSONObject.toJSONString(searchParam), e);
            returnInfo.setMessage("订单评论接口异常");
        }

        return returnInfo;
    }

    /**
     * 确认收货接口
     * @param searchParam 查询参数
     *      orderSn 订单号
     *      invoiceNo 快递单号
     *      userId 用户id
     *      siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/confirmReceiptNew")
    public ReturnInfo<Boolean> confirmReceiptNew(@RequestBody PageListParam searchParam) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();

        try {
            returnInfo = bgOrderInfoService.confirmReceiptNew(searchParam);
        } catch (Exception e) {
            logger.error("确认收货接口异常:" + JSONObject.toJSONString(searchParam), e);
            returnInfo.setMessage("确认收货口异常");
        }

        return returnInfo;
    }

    /**
     * 获取订单简易消息
     * @param searchParam 查询参数
     *      userId 用户id
     *      siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/getOrderSimpleInfoByUserNew")
    public ApiReturnData<List<MasterOrderInfo>> getOrderSimpleInfoByUserNew(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<MasterOrderInfo>> returnBean = new ApiReturnData<List<MasterOrderInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.getOrderSimpleInfoByUserNew(searchParam);
        } catch (Exception e) {
            logger.error("查询订单异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 平台前台查询用户未支付订单
     * @param searchParam userId siteCode
     * @return ApiReturnData
     */
    @PostMapping("/queryNoPayOrder")
    public ApiReturnData<List<String>> queryNoPayOrder(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<String>> returnBean = new ApiReturnData<List<String>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean =bgOrderInfoService.queryNoPayOrder(searchParam);
        } catch (Exception e) {
            logger.error("查询未支付订单异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 平台前台查询用户限购订单
     * @param searchParam 查询参数
     *      userId 用户id
     *      siteCode 站点编码
     * @return ApiReturnData
     */
    @PostMapping("/queryRestrictionOrder")
    public ApiReturnData<List<OrderGoodsInfo>> queryRestrictionOrder(@RequestBody PageListParam searchParam) {
        ApiReturnData<List<OrderGoodsInfo>> returnBean = new ApiReturnData<List<OrderGoodsInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgOrderInfoService.queryRestrictionOrder(searchParam);
        } catch (Exception e) {
            logger.error("用户限购订单异常:" + JSONObject.toJSONString(searchParam), e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 查询地区信息
     * @param searchParam 查询参数
     * @return OmsBaseResponse<SystemRegionArea>
     */
    @PostMapping("/querySystemRegionArea")
    public OmsBaseResponse<SystemRegionArea> querySystemRegionArea(@RequestBody SystemRegionArea searchParam) {
        OmsBaseResponse<SystemRegionArea> returnData = new OmsBaseResponse<SystemRegionArea>();
        returnData.setSuccess(false);
        returnData.setMessage("查询失败");

        try {
            returnData = bgOrderInfoService.querySystemRegionArea(searchParam);
        } catch (Exception e) {
            logger.error("地区信息异常:" + JSONObject.toJSONString(searchParam), e);
            returnData.setMessage("查询异常");
        }

        return returnData;
    }

    /**
     * 删除订单
     * @param searchParam 查询参数
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/deleteMasterOrder")
    public ApiReturnData<Boolean> deleteMasterOrder(@RequestBody PageListParam searchParam) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");
        apiReturnData.setMessage("删除订单失败");

        try {
            apiReturnData = bgOrderInfoService.deleteMasterOrder(searchParam);
        } catch (Exception e) {
            logger.error("删除订单异常:" + JSONObject.toJSONString(searchParam), e);
            apiReturnData.setMessage("删除订单异常");
        }

        return apiReturnData;
    }

    /**
     * 删除退单
     * @param searchParam 查询参数
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/deleteReturnOrder")
    public ApiReturnData<Boolean> deleteReturnOrder(@RequestBody PageListParam searchParam) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");
        apiReturnData.setMessage("删除退单失败");

        try {
            apiReturnData = bgOrderInfoService.deleteReturnOrder(searchParam);
        } catch (Exception e) {
            logger.error("删除退单异常:" + JSONObject.toJSONString(searchParam), e);
            apiReturnData.setMessage("删除退单异常");
        }

        return apiReturnData;
    }

    /**
     * 申请已支付
     * @param searchParam 申请信息
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/applyOrderPay")
    public ApiReturnData<Boolean> applyOrderPay(@RequestBody PageListParam searchParam) {

        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        apiReturnData.setMessage("操作失败");

        try {
            apiReturnData = bgOrderInfoService.applyOrderPay(searchParam);
        } catch (Exception e) {
            logger.error("申请已支付异常:" + JSONObject.toJSONString(searchParam), e);
            apiReturnData.setMessage("操作异常");
        }

        return apiReturnData;
    }

    /**
     * 延长收货
     * @param searchParam 请请信息
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/extendedReceipt")
    public ApiReturnData<Boolean> extendedReceipt(@RequestBody PageListParam searchParam) {

        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        apiReturnData.setMessage("操作失败");

        try {
            apiReturnData = bgOrderInfoService.extendedReceipt(searchParam);
        } catch (Exception e) {
            logger.error("延长收货异常:" + JSONObject.toJSONString(searchParam), e);
            apiReturnData.setMessage("操作异常");
        }

        return apiReturnData;
    }

    /**
     * 统计用户退单数量
     * @param searchParam
     * @return
     */
    @PostMapping("/getUserOrderReturnType")
    public ApiReturnData<UserOrderTypeNum> getUserOrderReturnType(@RequestBody PageListParam searchParam) {

        ApiReturnData<UserOrderTypeNum> apiReturnData = new ApiReturnData<UserOrderTypeNum>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        apiReturnData.setMessage("统计用户退单数量失败");

        try {
            apiReturnData = bgOrderInfoService.getUserOrderReturnType(searchParam);
        } catch (Exception e) {
            logger.error("统计用户退单数量异常:" + JSONObject.toJSONString(searchParam), e);
            apiReturnData.setMessage("统计用户退单数量异常");
        }

        return apiReturnData;
    }
}
