package com.work.shop.oms.ship.feign;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * feign接受供应商分仓结果服务
 * @author lemon
 *
 */
@FeignClient("OMS-SERVICE")
public interface WkUdDistributeService {

    /**
     * 接受供应商分仓结果数据
     * @param wkUdDistributes 分仓结果
     * @return boolean
     */
    @PostMapping("/order/doDistributeDepot")
    boolean doDistributeDepot(List<WkUdDistribute> wkUdDistributes);
}
