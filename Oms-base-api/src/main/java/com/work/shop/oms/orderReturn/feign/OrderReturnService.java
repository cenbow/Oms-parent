package com.work.shop.oms.orderReturn.feign;

import com.work.shop.oms.api.bean.ReturnInfoPage;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnSearchParams;
import com.work.shop.oms.api.param.bean.ReturnStorageParam;
import com.work.shop.oms.common.bean.ApiReturnData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 退单服务
 * @author QuYachu
 */
@FeignClient("OMS-SERVICE")
public interface OrderReturnService {

    /**
     * 第三方平台退单查询接口
     * @param params
     * @return ApiReturnData
     */
    @PostMapping("/order/getReturnInfoPageList")
    ApiReturnData<Paging<ReturnInfoPage>> getReturnInfoPageList(ReturnSearchParams params);

    /**
     * 第三方平台 退单商品分供应商入库
     * @param returnStorageParam
     * @return ApiReturnData<Boolean>
     */
    @PostMapping("/order/returnStorageBySeller")
    ApiReturnData<Boolean> returnStorageBySeller(ReturnStorageParam returnStorageParam);
}
