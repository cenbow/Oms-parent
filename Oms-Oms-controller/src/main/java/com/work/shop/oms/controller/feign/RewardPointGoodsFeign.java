package com.work.shop.oms.controller.feign;

import com.work.shop.oms.common.bean.CommonResultData;
import com.work.shop.oms.controller.pojo.ProductRewardPointGoodsBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/17 4:29 下午
 * @Version V1.0
 **/

@FeignClient("product-service-cii")
public interface RewardPointGoodsFeign {

    @PostMapping("/getRewardPointGoodsBySNList")
    CommonResultData<List<ProductRewardPointGoodsBean>> getRewardPointGoodsBySNList(@RequestBody List<String> goodsSNList);
}
