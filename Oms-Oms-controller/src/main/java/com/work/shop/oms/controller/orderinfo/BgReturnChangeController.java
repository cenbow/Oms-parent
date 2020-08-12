package com.work.shop.oms.controller.orderinfo;

import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.orderinfo.service.BGReturnChangeService;
import com.work.shop.oms.api.param.bean.CreateGoodsReturnChange;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnChangeGoodsBean;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单退换
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class BgReturnChangeController {

    private static final Logger logger = Logger.getLogger(BgReturnChangeController.class);

    @Resource
    private BGReturnChangeService bgReturnChangeService;

    /**
     * 根据退换申请单号查询明细
     * @param returnChangeGoodsBean 查询条件
     * @return ApiReturnData<ReturnChangeDetailInfo>
     */
    @PostMapping("/getGoodsReturnChangeDetailList")
    public ApiReturnData<ReturnChangeDetailInfo> getGoodsReturnChangeDetailList(@RequestBody ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<ReturnChangeDetailInfo> returnBean = new ApiReturnData<ReturnChangeDetailInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getGoodsReturnChangeDetailList(returnChangeGoodsBean);
        } catch (Exception e) {
            logger.error("获取申请单明细异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 创建订单商品退换货信息
     * @param actionUser 操作人
     * @param actionNote 操作备注helper
     * @param content 申请内容
     * @param siteCode 站点编码
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/createGoodsReturnChange")
    public ReturnInfo<Boolean> createGoodsReturnChange(@RequestParam(name="actionUser") String actionUser, @RequestParam(name="actionNote") String actionNote,
                                              @RequestParam(name="content") String content, @RequestParam(name="siteCode") String siteCode) {
        ReturnInfo<Boolean> returnBean = new ReturnInfo<Boolean>();

        try {
            returnBean = bgReturnChangeService.createGoodsReturnChange(actionUser, actionNote, content, siteCode);
        } catch (Exception e) {
            logger.error("创建退换申请单异常", e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 创建订单商品退换货信息
     * @param createGoodsReturnChange 退换信息
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/createGoodsReturnChangeNew")
    public ReturnInfo<Boolean> createGoodsReturnChangeNew(@RequestBody CreateGoodsReturnChange createGoodsReturnChange) {
        ReturnInfo<Boolean> returnBean = new ReturnInfo<Boolean>();

        try {
            returnBean = bgReturnChangeService.createGoodsReturnChangeNew(createGoodsReturnChange);
        } catch (Exception e) {
            logger.error("创建退换信息异常", e);
            returnBean.setMessage("处理异常");
        }

        return returnBean;
    }

    /**
     * 平台手机端查询换单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData<Paging<OrderMobile>>
     */
    @PostMapping("/getExchangeOrderList")
    public ApiReturnData<Paging<OrderMobile>> getExchangeOrderList(@RequestBody MobileParams mobileParams) {
        ApiReturnData<Paging<OrderMobile>> returnBean = new ApiReturnData<Paging<OrderMobile>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getExchangeOrderList(mobileParams);
        } catch (Exception e) {
            logger.error("查询换单列表异常", e);
            returnBean.setMessage("查询异常");
        }
        return returnBean;
    }

    /**
     * 平台手机端查询退单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData<Paging<ReturnMobile>>
     */
    @PostMapping("/getOrderReturnList")
    public ApiReturnData<Paging<ReturnMobile>> getOrderReturnList(@RequestBody MobileParams mobileParams) {
        ApiReturnData<Paging<ReturnMobile>> returnBean = new ApiReturnData<Paging<ReturnMobile>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getOrderReturnList(mobileParams);
        } catch (Exception e) {
            logger.error("查询退单列表异常", e);
            returnBean.setMessage("查询异常");
        }
        return returnBean;
    }

    /**
     * 平台手机端查询申请单详细
     * @param mobileParams 查询条件
     * @return ApiReturnData<GoodsReturnChangeMobile>
     */
    @PostMapping("/getGoodsReturnChange")
    public ApiReturnData<GoodsReturnChangeMobile> getGoodsReturnChange(@RequestBody MobileParams mobileParams) {
        ApiReturnData<GoodsReturnChangeMobile> returnBean = new ApiReturnData<GoodsReturnChangeMobile>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getGoodsReturnChange(mobileParams);
        } catch (Exception e) {
            logger.error("查询申请单详情异常", e);
            returnBean.setMessage("查询异常");
        }
        return returnBean;
    }

    /**
     * 平台前台退换货申请单详情获得数据接口
     * @param orderSn 订单编码
     * @param userId 用户id
     * @param siteCode 站点编码
     * @return ApiReturnData<GoodsReturnDetailInfo>
     */
    @PostMapping("/getBGGoodsReturnInfo")
    public ApiReturnData<GoodsReturnDetailInfo> getBGGoodsReturnInfo(@RequestParam(name="orderSn") String orderSn, @RequestParam(name="userId") String userId,
                                              @RequestParam(name="siteCode") String siteCode) {
        ApiReturnData<GoodsReturnDetailInfo> returnBean = new ApiReturnData<GoodsReturnDetailInfo>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getBGGoodsReturnInfo(orderSn, userId, siteCode);
        } catch (Exception e) {
            logger.error("申请单详情异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 前台退换货申请单列表获得数据接口
     * @param searchParam 查询条件
     * @param siteCode 站点编码
     * @return ApiReturnData<Paging<GoodsReturnPageInfo>>
     */
    @PostMapping("/getBGGoodsReturnPageList")
    public ApiReturnData<Paging<GoodsReturnPageInfo>> getBGGoodsReturnPageList(@RequestParam(name="searchParam") String searchParam, @RequestParam(name="siteCode") String siteCode) {
        ApiReturnData<Paging<GoodsReturnPageInfo>> returnBean = new ApiReturnData<Paging<GoodsReturnPageInfo>>();
        returnBean.setIsOk(Constant.OS_STR_NO);

        try {
            returnBean = bgReturnChangeService.getBGGoodsReturnPageList(searchParam, siteCode);
        } catch (Exception e) {
            logger.error("申请列表异常", e);
            returnBean.setMessage("查询异常");
        }

        return returnBean;
    }

    /**
     * 退货回寄快递消息获取接口
     * @param returnSn 退单编码
     * @param returnExpress 退货快递
     * @param returnInvoiceNo 退货单号
     * @param returnExpressImg 退货图片
     * @param siteCode 站点编码
     * @return ReturnInfo
     */
    @PostMapping("/setOrderReturnShipInfo")
    public ReturnInfo<Boolean> setOrderReturnShipInfo(@RequestParam(name="returnSn") String returnSn, @RequestParam(name="returnExpress") String returnExpress,
                                             @RequestParam(name="returnInvoiceNo") String returnInvoiceNo, @RequestParam(name="returnExpressImg", required = false) String returnExpressImg,
                                             @RequestParam(name="siteCode") String siteCode) {
        ReturnInfo<Boolean> returnBean = new ReturnInfo<Boolean>();

        try {
            returnBean = bgReturnChangeService.setOrderReturnShipInfo(returnSn, returnExpress, returnInvoiceNo, returnExpressImg, siteCode);
        } catch (Exception e) {
            logger.error("退货快递信息保存异常", e);
            returnBean.setMessage("保存异常");
        }

        return returnBean;
    }

    /**
     * 取消申请单
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    @PostMapping("/cancelGoodsReturnChange")
    public GoodsReturnChangeReturnInfo cancelGoodsReturnChange(@RequestParam(name="channelCode") String channelCode,
                                                               @RequestParam(name="returnChangeSn") String returnChangeSn,
                                                               @RequestParam(name="siteCode") String siteCode,
                                                               @RequestParam(name="actionUser") String actionUser) {
        GoodsReturnChangeReturnInfo returnBean = new GoodsReturnChangeReturnInfo();

        try {
            returnBean = bgReturnChangeService.cancelGoodsReturnChange(channelCode, returnChangeSn, siteCode, actionUser);
        } catch (Exception e) {
            logger.error("取消申请单异常", e);
            returnBean.setMessage("取消异常");
        }

        return returnBean;
    }

    /**
     * 删除申请单
     * @param returnChangeGoodsBean
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/deleteGoodsReturnChange")
    public ApiReturnData<Boolean> deleteGoodsReturnChange(@RequestBody ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");

        try {
            apiReturnData = bgReturnChangeService.deleteGoodsReturnChange(returnChangeGoodsBean);
        } catch (Exception e) {
            logger.error("删除申请单异常", e);
            apiReturnData.setMessage("删除异常");
        }

        return apiReturnData;
    }

    /**
     * 沟通申请单
     * @param returnChangeGoodsBean
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/communicationChange")
    public ApiReturnData<Boolean> communicationChange(@RequestBody ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");

        try {
            apiReturnData = bgReturnChangeService.communicationChange(returnChangeGoodsBean);
        } catch (Exception e) {
            logger.error("沟通申请单异常", e);
            apiReturnData.setMessage("沟通异常");
        }

        return apiReturnData;
    }

    /**
     * 申请单列表页获取申请单类型数量
     * @param param
     * @return
     */
    @PostMapping("/getChangeOrderTypeNum")
    public ApiReturnData<UserOrderTypeNum> getChangeOrderTypeNum(@RequestBody PageListParam param) {
        ApiReturnData<UserOrderTypeNum> apiReturnData = new ApiReturnData<UserOrderTypeNum>();
        apiReturnData.setIsOk("0");

        try {
            apiReturnData = bgReturnChangeService.getChangeOrderTypeNum(param);
        } catch (Exception e) {
            logger.error("申请单类型数量异常", e);
            apiReturnData.setMessage("申请单类型数量异常");
        }

        return apiReturnData;
    }

    /**
     * 申请单审核（自动创建退单并确认）
     * @param returnChangeGoodsBean
     * @return
     */
    @PostMapping("/examinationPassed")
    public ApiReturnData<Boolean> examinationPassed(@RequestBody CreateGoodsReturnChange returnChangeGoodsBean) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");

        try {
            apiReturnData = bgReturnChangeService.examinationPassed(returnChangeGoodsBean);
        } catch (Exception e) {
            logger.error("申请单审核异常", e);
            apiReturnData.setMessage("审核异常");
        }

        return apiReturnData;
    }

    /**
     * 驳回申请单
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    @PostMapping("/rejectGoodsReturnChange")
    public GoodsReturnChangeReturnInfo rejectGoodsReturnChange(@RequestParam(name="channelCode") String channelCode,
                                                               @RequestParam(name="returnChangeSn") String returnChangeSn,
                                                               @RequestParam(name="siteCode") String siteCode) {
        GoodsReturnChangeReturnInfo returnBean = new GoodsReturnChangeReturnInfo();

        try {
            returnBean = bgReturnChangeService.rejectGoodsReturnChange(channelCode, returnChangeSn, siteCode);
        } catch (Exception e) {
            logger.error("驳回申请单异常", e);
            returnBean.setMessage("驳回异常");
        }

        return returnBean;
    }
}
