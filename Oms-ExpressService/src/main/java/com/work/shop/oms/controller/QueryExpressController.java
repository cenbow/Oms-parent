package com.work.shop.oms.controller;

import java.util.List;

import com.work.shop.oms.express.bean.ExpressRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressService;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.express.bean.ExpressInfo;

/**
 * 物流查询
 * @author lemon
 */
@RestController
@RequestMapping("/express")
public class QueryExpressController {

	private static final Logger logger = Logger.getLogger(QueryExpressController.class);

	@Autowired
	private OrderExpressService orderExpressService;

	/**
	 * 订单物流写入
	 * @param orderSn 订单编码
	 * @return ReturnInfo<String>
	 */
	@RequestMapping(value = "/orderExpress")
	@ResponseBody
	public ReturnInfo<String> orderExpressOmsQuery(@RequestParam(name="orderSn") String orderSn) {
		ReturnInfo<String> info = new ReturnInfo<>();
		info.setMessage("查询失败");

		try {
			info = orderExpressService.orderExpress(orderSn);
		} catch (Exception e) {
			logger.error("oms查询物流信息orderSn = " + orderSn + "异常:" + e.getMessage(), e);
		}

		logger.debug("oms查询物流信息.end-orderSn:" + orderSn);
		return info;
	}
	
	/**
	 * 退单物流写入
	 * @param orderReturn
	 * @param orderReturnShip
	 * @return ReturnInfo<String>
	 */
	@RequestMapping(value = "/orderReturnExpress")
	@ResponseBody
	public ReturnInfo<String> orderReturnExpress(@RequestParam(name="returnSn") String returnSn) {
		ReturnInfo<String> info = new ReturnInfo<>();
		info.setMessage("查询失败");

		try {
			info = orderExpressService.orderReturnExpress(returnSn);
		} catch (Exception e) {
			logger.error("oms查询物流信息returnSn = " + returnSn + "异常:" + e.getMessage(), e);
		}

		logger.debug("oms查询物流信息.end-returnSn:" + returnSn);
		return info;
	}

	/**
	 * 整单查询物流信息
	 * @param expressRequest
	 * @return ReturnInfo<List<ExpressInfo>>
	 */
	@RequestMapping(value = "/orderExpressQuery")
	@ResponseBody
	public ReturnInfo<List<ExpressInfo>> orderExpressQuery(@RequestBody ExpressRequest expressRequest) {
		ReturnInfo<List<ExpressInfo>> info = new ReturnInfo<>();
		info.setMessage("查询失败");

		try {
            OrderExpressTracing tracing = new OrderExpressTracing();
            tracing.setOrderSn(expressRequest.getOrderSn());
            tracing.setTrackno(expressRequest.getTrackNo());
			info = orderExpressService.orderExpressQuery(tracing);
		} catch (Exception e) {
			logger.error("查询物流信息tracing = " + JSON.toJSONString(expressRequest) + "异常:" + e.getMessage(), e);
		}

		return info;
	}

	/**
	 * OMS查询物流信息
	 * @param expressRequest
	 * @return
	 */
	@RequestMapping(value = "/orderExpressOmsQuery")
	@ResponseBody
	public ReturnInfo<List<ExpressInfo>> orderExpressOmsQuery(@RequestBody ExpressRequest expressRequest) {
		ReturnInfo<List<ExpressInfo>> info = new ReturnInfo<>();
		info.setMessage("查询失败");

		try {
            OrderExpressTracing tracing = new OrderExpressTracing();
            tracing.setOrderSn(expressRequest.getOrderSn());
            tracing.setTrackno(expressRequest.getTrackNo());
            tracing.setDepotCode(expressRequest.getDepotCode());
			info = orderExpressService.orderExpressOmsQuery(tracing);
		} catch (Exception e) {
			logger.error("oms查询物流信息tracing = " + JSON.toJSONString(expressRequest) + "异常:" + e.getMessage(), e);
		}

		logger.debug("oms查询物流信息.end-tracing:" + JSON.toJSONString(expressRequest));
		return info;
	}
}