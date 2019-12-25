package com.work.shop.oms.controller.feign;

import com.work.shop.oms.common.bean.CommonResultData;
import com.work.shop.oms.controller.pojo.UserShopPointBean;
import com.work.shop.oms.controller.pojo.UserShopPointsRequestBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/17 4:29 下午
 * @Version V1.0
 **/

//@FeignClient("shopcenter-server-cii")
@FeignClient("shopcenter-server")
public interface UserPointFeign {

    @PostMapping("/CloudShopCenter-manager/custom/userPointsShop/getUserPointByUserAccount")
    CommonResultData<UserShopPointBean> getUserPointByUserAccount(UserShopPointsRequestBean userShopPointsRequestBean);
}
