package com.work.shop.oms.controller.orderreturn;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.ReturnInfoPage;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnSearchParams;
import com.work.shop.oms.api.param.bean.ReturnStorageParam;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单退单
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderReturnController {

    private static final Logger logger = Logger.getLogger(OrderReturnController.class);

    @Resource
    private OrderReturnService orderReturnService;

    /**
     * 第三方平台退单查询接口
     * @param params
     * @return ApiReturnData
     */
     @PostMapping("/getReturnInfoPageList")
     public ApiReturnData<Paging<ReturnInfoPage>> getReturnInfoPageList(@RequestBody ReturnSearchParams params) {
         ApiReturnData<Paging<ReturnInfoPage>> returnInfo = new ApiReturnData<Paging<ReturnInfoPage>>();
         returnInfo.setMessage("查询失败");

         try {
             returnInfo = orderReturnService.getReturnInfoPageList(params);
         } catch (Exception e) {
             logger.error("供应商查询退单:" + JSONObject.toJSONString(params) + "异常", e);
             returnInfo.setMessage("查询异常");
         }
         return returnInfo;
     }

    /**
     * 第三方平台 退单商品分供应商入库
     * @param returnStorageParam
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/returnStorageBySeller")
    public ApiReturnData<Boolean> returnStorageBySeller(@RequestBody ReturnStorageParam returnStorageParam) {
        ApiReturnData<Boolean> returnInfo = new ApiReturnData<Boolean>();
        returnInfo.setMessage("操作失败");

        try {
            returnInfo = orderReturnService.returnStorageBySeller(returnStorageParam);
        } catch (Exception e) {
            logger.error("供应商退单入库:" + JSONObject.toJSONString(returnStorageParam) + "异常", e);
            returnInfo.setMessage("操作异常");
        }
        return returnInfo;
    }
}
