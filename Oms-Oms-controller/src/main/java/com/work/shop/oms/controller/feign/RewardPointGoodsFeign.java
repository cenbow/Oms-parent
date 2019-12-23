package com.work.shop.oms.controller.feign;

import com.work.shop.oms.controller.pojo.ProductRewardPointGoodsBean;
import com.work.shop.pca.common.ResultData;
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
    ResultData<List<ProductRewardPointGoodsBean>> getRewardPointGoodsBySNList(@RequestBody List<String> goodsSNList);

//    @PostMapping("/changeStockAndSalesVolume")
//    ResultData<String> changeStockAndSalesVolume(@RequestBody List<ParamChangeRewardPointGoodsStockBean> paramList);
}
