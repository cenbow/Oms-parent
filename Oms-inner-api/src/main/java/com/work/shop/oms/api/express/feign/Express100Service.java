package com.work.shop.oms.api.express.feign;

import com.work.shop.oms.common.bean.ReturnInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * feign订单物流服务接口
 * @author
 */
@FeignClient("EXPRESS-SERVICE")
public interface Express100Service {

    /**
     * 快递100服务抓取
     * @return ReturnInfo<String>
     */
    @RequestMapping(value = "/express/express")
    @ResponseBody
    ReturnInfo<String> express();
}
