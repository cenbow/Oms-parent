package com.work.shop.oms.controller;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.paraBean.ParaChargebackBean;
import com.work.shop.oms.orderReturn.service.OrderPosReturnService;
/**
 * 门店退单处理
 * @author Administrator
 *
 */
@Controller
public class ChargebackOrderController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private OrderPosReturnService orderPosReturnService;
	
	@RequestMapping(value = "/ShopChargeback.do")
	@ResponseBody
	public String shopChargebackFunction(HttpServletRequest request,
			@RequestParam(value = "chargebackParam", required = true, defaultValue = "") String chargebackParam) {

		addRequestKeyLog(request, "shopChargebackFunction", "chargebackParam");
		ServiceReturnInfo<OrderDistribute> result = new ServiceReturnInfo<OrderDistribute>(false, "门店退单处理失败", null);
		if (StringUtils.isBlank(chargebackParam)) {
			logger.error("[shopChargebackFunction][parameter] error : chargebackParam={}" + chargebackParam);
			result.setMessage("参数[chargebackParam]不能为空");
			return objectToJsonString(result);
		}
		ParaChargebackBean paraBean = null;
		try {
			paraBean = JSON.parseObject(chargebackParam, ParaChargebackBean.class);
		} catch (Exception e) {
			result.setMessage("参数解析失败：" + e.getMessage());
			return objectToJsonString(result);
		}
		if (paraBean == null || paraBean.getGoodsList() == null || paraBean.getGoodsList().size() == 0) {
			result.setMessage("请求参数为空");
			return objectToJsonString(result);
		}
		if (paraBean != null & paraBean.getGoodsList() != null && paraBean.getGoodsList().size() > 10) {
			result.setMessage("单次请求退单商品数量超过限制10件");
			return objectToJsonString(result);
		}
		result = orderPosReturnService.createPosReturnApplication(paraBean);
		if (!result.isIsok() || result.getResult() == null) {
			return objectToJsonString(result);
		}
		return objectToJsonString(result);
	}

	protected void addRequestKeyLog(HttpServletRequest request, String methodName, String... paramNames) {
		logger.debug("the method is api " + methodName + "-->Start");
		if (paramNames == null || paramNames.length <= 0 || paramNames[0] == null || StringUtils.equals(paramNames[0], "")) {
			Map<String, String[]> m = request.getParameterMap();
			Iterator<String> it = m.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = ((String[]) m.get(k))[0];
				logger.debug("this request key is " + k + " value is " + v);
			}
		} else {
			for (String paramName : paramNames) {
				String KEY_VALUE = request.getParameter(paramName) == null ? "" : request.getParameter(paramName);
				logger.debug("this request key is " + paramName + " value is " + KEY_VALUE);
			}
		}
		logger.debug("*********" + methodName + "*********END");
	}

	/**
	 * 打印输入值
	 * 
	 * @param o
	 * @return
	 */
	protected String objectToJsonString(Object o) {
		String json = JSON.toJSONString(o);
		return json;
	}
}
