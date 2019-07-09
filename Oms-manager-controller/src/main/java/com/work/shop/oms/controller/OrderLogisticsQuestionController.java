package com.work.shop.oms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;



import com.work.shop.oms.api.param.bean.OrderQuestionParam;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.service.OrderLogisticsQuestionService;
import com.work.shop.oms.vo.AdminUser;

@Controller
@RequestMapping(value = "OrderLogisticsQuestion")
public class OrderLogisticsQuestionController extends BaseController {
	private Logger logger = Logger.getLogger(OrderLogisticsQuestionController.class);

	@Resource
	private OrderLogisticsQuestionService orderLogisticsQuestionService;

/*	@Resource(name = "orderQuestionService")
	private OrderQuestionService orderQuestionService;*/
	
	@Resource(name = "orderQuestionProviderJmsTemplate")
	private JmsTemplate jmsTemplate;
	
	/**
	 * 获取导入问题的类型
	 * 
	 **/
/*	@RequestMapping(value = "getQuestionType.spmvc")
	public ModelAndView getQuestionType(HttpServletRequest request,
			HttpServletResponse response, String orderSn) throws Exception {
		JsonResult jsonResult = new JsonResult();
		List<OrderCustomDefine> list = orderLogisticsQuestionService.getQuestionType();
		if (0 < list.size()) {
			jsonResult.setData(list);
			jsonResult.setIsok(true);
		}
		writeObject(jsonResult, response);
		return null;
	}*/
	
	/**
	 * 导入物流问题单
	 * 
	 **/
	@RequestMapping(value = "importOrderLogisticsQuestionList.spmvc", headers = "content-type=multipart/*")
	public ModelAndView importOrderLogisticsQuestionList(
			HttpServletRequest request, HttpServletResponse response,
			 @RequestParam MultipartFile myfile, int logType, String  mainChild , String code)
			throws Exception {
		InputStream is = myfile.getInputStream();
		StringBuffer sb = new StringBuffer("");
		AdminUser adminUser = getLoginUser(request);
		List<OrderQuestionParam> list = new ArrayList<OrderQuestionParam>();
		try {
			//将流转化为list<OrderQuestion>对象
			 list = orderLogisticsQuestionService.importOrderLogisticsQuestionList(is, sb, logType, code, mainChild);
			if (!"".equals(sb.toString())) {
				writeMsgSucc(false,sb.toString() + ",请检查模版中记录",  response);
				return null;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导入物流问题单异常", e);
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
		}finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					logger.error("导入物流问题单 流关闭异常", e);
					e.printStackTrace();
				}
			}
		}
		
		String orderSn = "";
		
		try {

			if(null != list && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					list.get(i).setActionUser(adminUser.getUserName());
					//调用dubbo服务的导入 物流问题单
					final OrderQuestionParam param = list.get(i);
					orderSn = param.getOrderSn();
				    jmsTemplate.send(new MessageCreator() {
						public Message createMessage(Session session) throws JMSException {
							TextMessage message = session.createTextMessage();
							message.setText(JSONObject.toJSONString(param));
							return message;
						}
					});
					logger.debug("订单" +param.getOrderSn() + "添加至MQ队列任务QULOGIPROBLEM中");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("订单"+orderSn +"添加队列失败", e);
			return null;
		}

		writeMsgSucc(true,"导入成功！",  response);
		return null;
	}

}
