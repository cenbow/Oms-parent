package com.work.shop.oms.controller;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.OrderReturnDetailInfo;
import com.work.shop.oms.api.bean.PageListParam;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.api.orderinfo.service.BGReturnChangeService;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.order.service.ReturnManagementService;
import com.work.shop.oms.orderop.service.OrderCommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/returnOrder")
public class ReturnOrderController {

    @Resource(name="bgOrderInfoService")
    private BGOrderInfoService bgOrderInfoService;

    @Resource
    private BGReturnChangeService bgReturnChangeService;

    @Resource
    private OrderCommonService orderCommonService;

    @Resource
    private ReturnManagementService returnManagementService;

    @RequestMapping("/orderReturnPageList.do")
    public @ResponseBody String orderReturnPageList() {
        PageListParam searchParam = new PageListParam();
        searchParam.setUserId("wxa679353");
        searchParam.setSiteCode("WBC");
        searchParam.setPageNum(1);
        searchParam.setPageSize(10);
        ApiReturnData apiReturnData = bgOrderInfoService.orderReturnPageList(searchParam);
        return JSONObject.toJSONString(apiReturnData);
    }

    @RequestMapping("/cancelOrderByMasterSn.do")
    public @ResponseBody String cancelOrderByMasterSn(String masterOrderSn) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setAdminUser("yeqingchao");
        orderStatus.setCode("8012");
        orderStatus.setSource("OMS");
        orderStatus.setType("1");
        ReturnInfo returnInfo = orderCommonService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
        return JSONObject.toJSONString(returnInfo);
    }

    @RequestMapping("/returnItemCreateInit.do")
    public @ResponseBody String returnItemCreateInit(ReturnManagementRequest request) {
        ReturnManagementResponse response = returnManagementService.returnItemCreateInit(request);
        return JSONObject.toJSONString(response);
    }

    @RequestMapping("/orderReturnDetailNew.do")
    public @ResponseBody String orderReturnDetailNew(PageListParam searchParam) {
        ApiReturnData<OrderReturnDetailInfo> apiReturnData = bgOrderInfoService.orderReturnDetailNew(searchParam);
        return JSONObject.toJSONString(apiReturnData);
    }
}
