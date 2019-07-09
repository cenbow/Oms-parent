package com.work.shop.oms.controller;


import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.service.CommonLogService;

@Controller
@RequestMapping(value = "commonLog")
public class CommonLogController extends BaseController {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private CommonLogService commonLogService;
	
	/**
	 * 根据主单号获取主单日志
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param isHistory
	 * @return
	 */
	@RequestMapping(value = "getMasterOrderAction")
	@ResponseBody
	public Paging getMasterOrderAction(HttpServletRequest request,
			HttpServletResponse response, String masterOrderSn, Integer isHistory){
		return commonLogService.getMasterOrderAction(getLoginUser(request), masterOrderSn, isHistory);
	}
	
	/**
	 * 根据交货单号获取交货单日志
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 */
	@RequestMapping(value = "getDistributeOrderAction")
	@ResponseBody
	public Paging getDistributeOrderAction(HttpServletRequest request,
			HttpServletResponse response, String orderSn, Integer isHistory){
		return commonLogService.getDistributeOrderAction(getLoginUser(request), orderSn, isHistory);
	}
	
	/**
	 * 联系方式解密
	 * @param request
	 * @param response
	 * @param mobile
	 * @param tel
	 * @param masterOrderSn
	 * @return
	 */
	@RequestMapping(value = "decodeLinkMobile")
	@ResponseBody
	public Map<String,String> decodeLinkMoble(HttpServletRequest request, HttpServletResponse response,
			String mobile, String tel, String masterOrderSn,String returnSn){
		return commonLogService.decodeLinkMoble(getLoginUser(request), mobile, tel, masterOrderSn,returnSn);
	}
	
	/**
	 * 获取退换货申请操作日志
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 */
	@RequestMapping(value = "getGoodsReturnChangeAction")
	@ResponseBody
	public Paging getGoodsReturnChangeAction(HttpServletRequest request,
			HttpServletResponse response, String orderSn, Integer isHistory){
		return commonLogService.getGoodsReturnChangeAction(getLoginUser(request), orderSn, isHistory);
	}

}
