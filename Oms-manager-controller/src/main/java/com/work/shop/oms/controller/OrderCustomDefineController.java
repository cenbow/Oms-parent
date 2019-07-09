package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.config.service.OrderCustomDefineService;


@Controller
@RequestMapping(value = "define")
public class OrderCustomDefineController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 自定义属性json
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDefines")
	public ModelAndView getDefinesByChannelType(HttpServletRequest request,
			HttpServletResponse response, Short type) throws Exception {
		logger.debug("自定义属性");
		System.out.println("自定义属性222");
		try {
			List<OrderCustomDefine> defines = new ArrayList<OrderCustomDefine>();
			OrderCustomDefine define = new OrderCustomDefine();
			define.setCode("xxxx");
			define.setName("xxxx");
			defines.add(define);
			Paging paging = new Paging(defines.size(), defines);
			outPrintJson(response, paging);
		} catch (Exception e) {
			logger.debug("自定义属性", e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Resource
	private OrderCustomDefineService orderCustomDefineService;
	
	/**
	 * 获取商家自定义信息配置列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "customDefineList.spmvc")
	public void customDefineList(HttpServletRequest request, HttpServletResponse response, Short type,OrderCustomDefine orderCustomDefine) throws Exception {
		List<OrderCustomDefine> list = orderCustomDefineService.selectCustomDefine(orderCustomDefine);
		JsonResult result = new JsonResult();
		result.setData(list);
		//outPrintJson(response, result);
		writeObject(result,response);
	}
	
	
}
