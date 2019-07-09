package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.send.service.OrderToEcService;
import com.work.shop.oms.send.service.RiderDrpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.rider.service.RiderDistributeService;

/**
 * 订单物流下发
 * @author quyac
 *
 */
@Controller
public class OrderRiderDistributeController extends BaseController{
	
	@Resource(name="riderDistributeService")
	private RiderDistributeService riderDistributeService;

	@Resource(name="orderToEcService")
	private OrderToEcService orderToEcService;

	@Resource(name="riderDrpService")
	private RiderDrpService riderDrpService;

	@RequestMapping("/doSaveFile.do")
	public @ResponseBody String doSaveFile() {
		
		List<String > orderSnList = new ArrayList<String>();
		orderSnList.add("sn123456");
		orderSnList.add("sn123457");
		orderSnList.add("000000");
		ServiceReturnInfo<Boolean> result = riderDistributeService.saveRiderDistributeInfoList(orderSnList, 1);
		
		return JSONObject.toJSONString(result);
	}

    @RequestMapping("/sendOrderToEc.do")
    public @ResponseBody String sendOrderToEc(String masterOrderSn, HttpServletResponse response) {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setMasterOrderSn(masterOrderSn);
        ServiceReturnInfo<Boolean> serviceReturnInfo = orderToEcService.sendOrderToEc(orderQueryRequest);
        outPrintJson(response, serviceReturnInfo);
        return JSONObject.toJSONString(serviceReturnInfo);
    }

    @RequestMapping("/sendOrderToDrp.do")
    public @ResponseBody String sendOrderToDrp(String masterOrderSn) {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setMasterOrderSn(masterOrderSn);
        orderQueryRequest.setActionUser("system");
        ServiceReturnInfo<Boolean> returnInfo = riderDrpService.sendDrpByDeliverySuccess(orderQueryRequest);
        return JSONObject.toJSONString(returnInfo);
    }
}
