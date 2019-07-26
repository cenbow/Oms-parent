package com.work.shop.oms.controller.ship;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.ship.bean.DistributeRequest;
import com.work.shop.oms.ship.service.WkUdDistributeService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供应商订单分仓结果
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class WkUdDistributeController {

    private static final Logger logger = Logger.getLogger(WkUdDistributeController.class);

    @Resource
    private WkUdDistributeService wkUdDistributeService;

     /**
     * 接受供应商分仓结果数据
     * @param wkUdDistributes 分仓结果
     * @return boolean
     */
    @PostMapping("/doDistributeDepot")
    public boolean doDistributeDepot(@RequestBody List<WkUdDistribute> wkUdDistributes) {
        boolean result = false;

        try {
            result = wkUdDistributeService.depot(wkUdDistributes);
        } catch (Exception e) {
            logger.error("接受供应商分仓结果数据处理异常" + JSONObject.toJSONString(wkUdDistributes), e);
        }

        return result;
    }

    /**
     *  订单/交货单分仓
     *  orderSn不为空走交货单分配
     *  orderSn为空走订单分配
     * @param distributeRequest
     */
    @PostMapping("/doDistributeDepotByOrder")
    public ReturnInfo<Boolean> doDistributeDepotByOrder(@RequestBody DistributeRequest distributeRequest) {
        ReturnInfo<Boolean> result = null;

        try {
            result = wkUdDistributeService.depotByOrder(distributeRequest);
        } catch (Exception e) {
            logger.error("人工分仓结果数据处理异常" + JSONObject.toJSONString(distributeRequest), e);
        }

        return result;
    }
}
