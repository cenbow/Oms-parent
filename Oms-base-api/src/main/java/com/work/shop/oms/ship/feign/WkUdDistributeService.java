package com.work.shop.oms.ship.feign;

import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.ship.bean.DistributeRequest;
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

    /**
     *  订单/交货单分仓
     *  orderSn不为空走交货单分配
     *  orderSn为空走订单分配
     * @param distributeRequest
     */
    @PostMapping("/order/doDistributeDepotByOrder")
    ReturnInfo<Boolean> doDistributeDepotByOrder(DistributeRequest distributeRequest);
}
