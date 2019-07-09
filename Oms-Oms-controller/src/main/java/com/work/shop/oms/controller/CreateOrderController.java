package com.work.shop.oms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.OrderValidateService;
import com.work.shop.oms.utils.CommonUtils;
import com.work.shop.oms.utils.Constant;


/**
 * 订单创建接口
 * @author lemon
 *
 */
@Controller
public class CreateOrderController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "masterOrderinfoServiceImpl")
	private MasterOrderInfoService masterOrderInfoService;
	@Resource
	private OrderValidateService orderValidateService;

	@Resource
	private OrderDistributeService orderDistributeService;

	
	@RequestMapping(value = "/createOrder111")
	@ResponseBody
	public String createOrder(HttpServletRequest request, @RequestParam(value = "orderInfo", required = false, defaultValue = "") String orderParam) {
		logger.info("createOrder get create order request : " + orderParam);
		// 获取系统信息
		SystemInfo systemInfo = CommonUtils.getSystemInfo(request);
		ServiceReturnInfo<MasterOrder> validateinfo = orderValidateService.orderFormat(orderParam);
		// 验证不通过直接返回错误信息不会创建订单
		if (!validateinfo.isIsok()) {
			logger.error("validate error : " + validateinfo.getMessage() + orderParam);
			return orderValidateService.errorMessage(systemInfo, validateinfo);
		}
		MasterOrder masterOrder = validateinfo.getResult();
		OrderCreateReturnInfo returninfo = masterOrderInfoService.createOrder(masterOrder);
		if (returninfo.getIsOk() == Constant.OS_NO) {
			return errorMessage(systemInfo, returninfo);
		}
		return successMessage(systemInfo, returninfo);
	}
	
	@RequestMapping(value = "/createOrders111")
	@ResponseBody
	public String createOrders(HttpServletRequest request, @RequestParam(value = "orderInfos", required = false, defaultValue = "") String orderParam) {
		logger.info("createOrder get create order request : " + orderParam);
		// 获取系统信息
		SystemInfo systemInfo = CommonUtils.getSystemInfo(request);
		ServiceReturnInfo<List<MasterOrder>> validateinfo = orderValidateService.orderListFormat(orderParam);
		// 验证不通过直接返回错误信息不会创建订单
		if (!validateinfo.isIsok()) {
			logger.error("validate error : " + validateinfo.getMessage() + orderParam);
			return orderValidateService.errorMessage(systemInfo, validateinfo);
		}
		List<MasterOrder> masterOrders = validateinfo.getResult();
		OrdersCreateReturnInfo returninfo = masterOrderInfoService.createOrders(masterOrders);
		if (returninfo.getIsOk() == Constant.OS_NO) {
			return errorMessage(systemInfo, returninfo);
		}
		return successMessage(systemInfo, returninfo);
	}
	
	public String errorMessage(SystemInfo systemInfo, OrderCreateReturnInfo returninfo) {
		String s = JSON.toJSONString(returninfo);
		logger.error("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String successMessage(SystemInfo systemInfo, OrderCreateReturnInfo returninfo) {
		/*if (returninfo.getDepotInfos().isEmpty()) {
			returninfo.setDepotInfos(null);
		}*/
		String s = JSON.toJSONString(returninfo);
		logger.info("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public String errorMessage(SystemInfo systemInfo, OrdersCreateReturnInfo returninfo) {
		String s = JSON.toJSONString(returninfo);
		logger.error("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String successMessage(SystemInfo systemInfo, OrdersCreateReturnInfo returninfo) {
		String s = JSON.toJSONString(returninfo);
		logger.info("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

    @RequestMapping(value = "/orderDistribute.do")
    @ResponseBody
    public String orderDistribute(HttpServletRequest request,String masterOrderSn) {
        OrderDepotResult result = orderDistributeService.orderDistribute(masterOrderSn);
        return JSON.toJSONString(result);
    }
}
