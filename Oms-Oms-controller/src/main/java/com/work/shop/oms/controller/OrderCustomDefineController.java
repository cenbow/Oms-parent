package com.work.shop.oms.controller;

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
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.config.service.OrderCustomDefineService;
import com.work.shop.oms.order.service.SystemOrderSnService;


@Controller
@RequestMapping(value = "define")
public class OrderCustomDefineController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OrderCustomDefineService orderCustomDefineService;
	
	@Resource
	private SystemOrderSnService systemOrderSnService;


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
			ServiceReturnInfo<String> sss =  systemOrderSnService.createMasterOrderSn();
			if (sss != null) {
				logger.debug(sss.getResult());
			}
			List<OrderCustomDefine> defines = orderCustomDefineService.selectDefines(type);
			Paging paging = new Paging(defines.size(), defines);
			outPrintJson(response, paging);
		} catch (Exception e) {
			logger.debug("自定义属性", e);
			e.printStackTrace();
		}
		return null;
	}
}
