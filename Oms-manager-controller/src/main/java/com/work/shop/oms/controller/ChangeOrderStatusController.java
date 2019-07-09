package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.DistributeQuestionExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderQuestionExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.OrderDepotResult;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.DistributeQuestionMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.exception.OrderException;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderExchangeStService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.orderop.service.OrderNormalService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.payment.service.PayService;
import com.work.shop.oms.service.OrderLogisticsQuestionService;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.BatchReturnInfo;
import com.work.shop.oms.vo.QuestionTypeVO;

@Controller
@RequestMapping(value = "orderStatus")
public class ChangeOrderStatusController extends BaseController {
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="orderDistributeOpService")
	private OrderDistributeOpService orderDistributeOpService;
	
	@Resource(name="orderConfirmService")
	private OrderConfirmService orderConfirmService;
	
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private OrderLogisticsQuestionService orderLogisticsQuestionService;
	
	@Resource(name="orderNormalService")
	private OrderNormalService orderNormalService;
	
	@Resource(name="orderQuestionService")
	private OrderQuestionService orderQuestionService;
	
	@Resource(name="uniteStockService")
	private UniteStockService uniteStockService;
	
	@Resource
	private MasterOrderActionService masterOrderActionService;
	
	@Resource
	private DistributeActionService distributeActionService;
	
	@Resource(name="orderCommonService")
	private OrderCommonService orderCommonService;
	
	@Resource
	private DistributeQuestionMapper distributeQuestionMapper;
	
	@Resource
	private MasterOrderQuestionMapper masterOrderQuestionMapper;
	
	@Resource
	private PayService payService;
	
	@Resource
	private OrderReturnStService orderReturnStService;
	
	@Resource(name="orderExchangeStService")
	private OrderExchangeStService orderExchangeStService;
	
	@Resource(name="distributeSupplierService")
	private DistributeSupplierService distributeSupplierService;
	
	/**
	 * 锁定主订单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "lock")
	@ResponseBody
	public Map lock(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "lock:锁定失败";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("锁定");
			ReturnInfo ri = orderDistributeOpService.lockOrder(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "lock:锁定成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 解锁主订单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "unlock")
	@ResponseBody
	public Map unlock(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "unlock:解锁失败";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("解锁");
			ReturnInfo ri = orderDistributeOpService.unLockOrder(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "unlock:解锁成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 确认主订单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "confirm")
	@ResponseBody
	public Map confirm(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "confirm:确认订单失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage("确认订单");
			ReturnInfo ri = orderConfirmService.confirmOrderByMasterSn(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "confirm:确认订单成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 未确认主单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "unconfirm")
	@ResponseBody
	public Map unconfirm(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "unconfirm:未确认订单失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage("未确认订单");
			ReturnInfo ri = orderConfirmService.unConfirmOrderByMasterSn(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "unconfirm:未确认订单成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "cancel")
	@ResponseBody
	public Map cancel(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,
			String message,String code,String type,String orderType,String relatingReturnSn){
		Map map = new HashMap();
		String returnCode = "0";//0:失败  1:成功
		String msg = "取消订单失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(code)||code==null){
				msg = "取消订单原因不能为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(type)||type==null){
				msg = "是否生成退单不能为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(message)||message==null){
				message = "取消订单";
			}

			ReturnInfo rs = null;
			//判断订单是否换单  如果是需要取消退单
			try{
				if("2".equals(orderType)&&!"".equals(relatingReturnSn)&&relatingReturnSn!=null){
					rs = orderReturnStService.returnOrderInvalid(relatingReturnSn, "取消换货单的退单", adminUser.getUserName());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage(message);
			orderStatus.setCode(code);
			orderStatus.setType(type);//是否创建退单 1：创建;2：不创建
			orderStatus.setSource("OMS");//取消来源 POS:POS端;FRONT:前端;OMS:后台取消
			ReturnInfo ri = orderCommonService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
			
			if(ri!=null&&rs!=null){
				if(ri.getIsOk()==1&&rs.getIsOk()==1){
					returnCode = "1";
					msg = "取消订单成功！";
				}else if(ri.getIsOk()==1){
					msg = "取消订单成功！"+rs.getMessage();
				}else if(rs.getIsOk()==1){
					msg = "取消换单的退单成功！"+ri.getMessage();
				}
			}else if(ri!=null&rs==null&&"2".equals(orderType)){
				if(ri.getIsOk()==1){
					msg = "取消订单成功！取消换单的退单失败！";
				}else{
					msg = ri.getMessage()+"取消换单的退单失败！";
				}
			}else if(ri!=null&rs==null){
				if(ri.getIsOk()==1){
					returnCode = "1";
					msg = "取消订单成功！";
				}else{
					msg = ri.getMessage();
				}
			}else if(rs!=null&&ri==null){
				if(rs.getIsOk()==1){
					msg = "取消订单失败！取消换单的退单成功！";
				}else{
					msg = "取消订单失败！"+rs.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", returnCode);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 返回正常单
	 * @param request
	 * @param response
	 * @param status
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "normal")
	public ModelAndView normal(HttpServletRequest request, HttpServletResponse response,String hasOccupyStock, OrderStatus status, String method) throws Exception {
		ModelAndView mav = new ModelAndView();
		if (StringUtil.isNotNull(method) && method.equals("init")) { // 初始化返回正常单页面
			if (null == status || StringUtil.isEmpty(status.getMasterOrderSn()) ) {
				return null;
			}
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(status.getMasterOrderSn());
			mav.setViewName("orderInfo/turnNormalOrder");
			if(null !=masterOrderInfo ) {
				mav.addObject("orderInfo", masterOrderInfo);
			}
			//OS问题单
			MasterOrderQuestionExample osExample = new MasterOrderQuestionExample();
			MasterOrderQuestionExample.Criteria oscriteria = osExample.createCriteria();
			oscriteria.andMasterOrderSnEqualTo(status.getMasterOrderSn());
			oscriteria.andQuestionTypeEqualTo(0);
			int osQuesCount = masterOrderQuestionMapper.countByExample(osExample); 
			//物流问题单
			MasterOrderQuestionExample shipExample = new MasterOrderQuestionExample();
			MasterOrderQuestionExample.Criteria shipcriteria = shipExample.createCriteria();
			shipcriteria.andMasterOrderSnEqualTo(status.getMasterOrderSn());
			shipcriteria.andQuestionTypeEqualTo(1);
			int shipQuesCount = masterOrderQuestionMapper.countByExample(shipExample); 
			String desc = "";
			List<QuestionTypeVO> list  = new ArrayList<QuestionTypeVO>();
			if(osQuesCount>0 && shipQuesCount>0){
				desc = "此问题单既是OS问题单又是物流问题单。";
				QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OS问题单", "0");
				list.add(osTypeVO);
				QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
				list.add(LogiTypeVO);
				QuestionTypeVO allTypeVO = new QuestionTypeVO(desc, "全部正常化", "2");
				list.add(allTypeVO);
			}else{
				if (shipQuesCount > 0) {
					desc = "此问题单是物流问题单。";
					QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
					list.add(LogiTypeVO);
				}
				if (osQuesCount > 0) {
					desc = "此问题单是OS问题单。";
					QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OS问题单", "0");
					list.add(osTypeVO);
				}
			}
			if (StringUtil.isNotNullForList(list)) {
				mav.addObject("list", list);
			}
			outPrintJson(response, list);
			return null;
		}
		boolean success = false;
		String msg = "normal : 返回正常单失败";
		try {
			if (null == status) {
				writeMsgSucc(false, "订单编号为空！", response);
				return null;
			}
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				writeMsgSucc(false, "登录用户信息不存在！", response);
				return null;
			}
			//是否占用库存
			if("1".equals(hasOccupyStock)){
				status.setSwitchFlag(true);
			}else{
				status.setSwitchFlag(false);
			}
			//操作人
			status.setAdminUser(adminUser.getUserName());
			ReturnInfo ri = orderNormalService.normalOrderByMasterSn(status.getMasterOrderSn(), status);
			if (null != ri && ri.getIsOk() == Constant.OS_YES) {
				success = true;
				msg = ri.getMessage();
			} else if (null != ri) {
				msg = ri.getMessage();
			}
		} catch(OrderException oe) {
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
		return null;
	}
	
	/**
	 * 设为问题单
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "question")
	public void question(HttpServletRequest request, HttpServletResponse response,String hasReleaseStock, OrderStatus orderStatus) throws Exception {
		boolean success = false;
		String msg = "question : 设为问题单失败";
		if (null == orderStatus) {
			writeMsgSucc(false, "订单编号为空！", response);
			return;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		//是否占库存
		if("1".equals(hasReleaseStock)){
			orderStatus.setSwitchFlag(true);
		}else{
			orderStatus.setSwitchFlag(false);
		}
		//操作人
		orderStatus.setAdminUser(adminUser.getUserName());
		try {
			ReturnInfo ri = orderQuestionService.questionOrderByMasterSn(orderStatus.getMasterOrderSn(), orderStatus);
			if (null != ri && ri.getIsOk() == Constant.OS_YES) {
				success = true;
				msg = ri.getMessage();
			} else if (null != ri) {
				msg = ri.getMessage();
			}
		} catch(OrderException oe) {
			oe.printStackTrace();
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	
	/**
	 * 通知收款
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "notice")
	@ResponseBody
	public Map notice(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "notice:通知收款失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("通知收款");
			ReturnInfo ri = orderDistributeOpService.noticeReceivables(masterOrderSn,orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "notice:通知收款成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 释放库存
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "release")
	@ResponseBody
	public Map release(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "release:释放库存失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			ReturnInfo ri = uniteStockService.realese(masterOrderSn);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "release:释放库存成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 占用库存
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "occupy")
	@ResponseBody
	public Map occupy(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "occupy:占用库存失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			ReturnInfo ri = uniteStockService.occupy(masterOrderSn);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "occupy:占用库存成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 订单分仓发货
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "swdi")
	@ResponseBody
	public Map swdi(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "swdi:订单分仓发货失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("订单分仓发货");
			ReturnInfo ri = orderDistributeOpService.sWDI(masterOrderSn, "", orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "swdi:订单分仓发货成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 移入近期
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "recent")
	@ResponseBody
	public Map recent(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "移入近期失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("移入近期");
			ReturnInfo ri = orderDistributeOpService.moveOrderFromHistoryToRecent(masterOrderSn,orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "移入近期成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 复制订单
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "copyorder")
	@ResponseBody
	public Map copyorder(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "copyorder:复制订单失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("复制订单");
			ReturnInfo ri = orderDistributeOpService.copyOrder(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = ri.getOrderSn();
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 沟通
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @param type 沟通对象：0：主单，1：交货单
	 * @return
	 */
	@RequestMapping(value = "communicate")
	@ResponseBody
	public Map communicate(HttpServletRequest request, HttpServletResponse response,String orderSn,String message,String type){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "沟通失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if("0".equals(type)){//插入主单日志
				masterOrderActionService.insertOrderActionBySn(orderSn, message, adminUser.getUserName());
			}else if("1".equals(type)){//插入交货单日志
				OrderDistribute order = orderDistributeMapper.selectByPrimaryKey(orderSn);
				if(order == null){
					throw new RuntimeException("无法获取有效的订单信息！");
				}
				DistributeAction distributeAction = distributeActionService.createQrderAction(order);
				distributeAction.setActionUser(adminUser.getUserName());
				distributeAction.setActionNote(message);
				distributeActionService.saveOrderAction(distributeAction);
			}
			code = "1";
			msg = "沟通成功！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 交货单确认
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "sonConfirm")
	@ResponseBody
	public Map sonConfirm(HttpServletRequest request, HttpServletResponse response,String orderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "交货单确认失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "交货单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage(message);
			ReturnInfo ri = orderConfirmService.confirmOrderByOrderSn(orderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "交货单确认成功！";
				}else{
					msg = ri.getMessage();
				}
			}
			code = "1";
			msg = "交货单确认成功！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 交货单未确认
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "sonUnConfirm")
	@ResponseBody
	public Map sonUnConfirm(HttpServletRequest request, HttpServletResponse response,String orderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "交货单未确认失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "交货单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage(message);
			ReturnInfo ri = orderConfirmService.unConfirmOrderByOrderSn(orderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "交货单未确认成功！";
				}else{
					msg = ri.getMessage();
				}
			}
			code = "1";
			msg = "交货单未确认成功！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 取消交货单
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param message
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "sonCancel")
	@ResponseBody
	public Map sonCancel(HttpServletRequest request, HttpServletResponse response,String orderSn,String message,String code,String type){
		Map map = new HashMap();
		String returnCode = "0";//0:失败  1:成功
		String msg = "取消交货单失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "订单编号为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(code)||code==null){
				msg = "取消交货单原因不能为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(type)||type==null){
				msg = "是否生成退单不能为空！";
				map.put("code", returnCode);
				map.put("msg", msg);
				return map;
			}
			if("".equals(message)||message==null){
				message = "取消交货单";
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setOrderSn(orderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage(message);
			orderStatus.setCode(code);
			orderStatus.setType(type);//是否创建退单 1：创建;2：不创建
			orderStatus.setSource("OMS");//取消来源 POS:POS端;FRONT:前端;OMS:后台取消
			ReturnInfo ri = orderCommonService.cancelOrderByOrderSn(orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					returnCode = "1";
					msg = "取消交货单成功！";
				}else{
					msg = ri.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", returnCode);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 交货单分仓发货
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "sonSwdi")
	@ResponseBody
	public Map sonSwdi(HttpServletRequest request, HttpServletResponse response,String orderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "交货单分仓发货失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "交货单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setOrderSn(orderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("交货单分仓发货");
			ReturnInfo ri = orderDistributeOpService.sWDI("",orderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "交货单分仓发货成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 交货单设置问题单
	 * @param request
	 * @param response
	 * @param hasReleaseStock
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "sonQuestion")
	public void sonQuestion(HttpServletRequest request, HttpServletResponse response,String hasReleaseStock, OrderStatus orderStatus) throws Exception {
		boolean success = false;
		String msg = "交货单设为问题单失败";
		if (null == orderStatus) {
			writeMsgSucc(false, "交货单编号为空！", response);
			return;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		//是否占库存
		if("1".equals(hasReleaseStock)){
			orderStatus.setSwitchFlag(true);
		}else{
			orderStatus.setSwitchFlag(false);
		}
		//操作人
		orderStatus.setAdminUser(adminUser.getUserName());
		try {
			ReturnInfo ri = orderQuestionService.questionOrderByOrderSn(orderStatus.getOrderSn(), orderStatus);
			if (null != ri && ri.getIsOk() == Constant.OS_YES) {
				success = true;
				msg = ri.getMessage();
			} else if (null != ri) {
				msg = ri.getMessage();
			}
		} catch(OrderException oe) {
			oe.printStackTrace();
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	
	/**
	 * 交货单返回正常单
	 * @param request
	 * @param response
	 * @param hasOccupyStock
	 * @param status
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sonNormal")
	public ModelAndView sonNormal(HttpServletRequest request, HttpServletResponse response,String hasOccupyStock, OrderStatus status, String method) throws Exception {
		ModelAndView mav = new ModelAndView();
		if (StringUtil.isNotNull(method) && method.equals("init")) { // 初始化返回正常单页面
			if (null == status || StringUtil.isEmpty(status.getOrderSn()) ) {
				return null;
			}
			OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(status.getOrderSn());
			mav.setViewName("orderInfo/turnNormalDistributeOrder");
			if(null !=orderDistribute ) {
				mav.addObject("orderInfo", orderDistribute);
			}
			//OS问题单数量
			DistributeQuestionExample osexample = new DistributeQuestionExample();
			DistributeQuestionExample.Criteria oscriteria = osexample.createCriteria();
			oscriteria.andOrderSnEqualTo(status.getOrderSn());
			oscriteria.andQuestionTypeEqualTo(0);//OS问题单
			int osQuestionCount = distributeQuestionMapper.countByExample(osexample);
			//物流问题单数量
			DistributeQuestionExample shipexample = new DistributeQuestionExample();
			DistributeQuestionExample.Criteria shipcriteria = shipexample.createCriteria();
			shipcriteria.andOrderSnEqualTo(status.getOrderSn());
			shipcriteria.andQuestionTypeEqualTo(1);//物流问题单
			int shipQuestionCount = distributeQuestionMapper.countByExample(shipexample);
			String desc = "";
			List<QuestionTypeVO> list = new ArrayList<QuestionTypeVO>();
			if (osQuestionCount > 0 && shipQuestionCount > 0) {
				desc = "此问题单既是OS问题单又是物流问题单。";
				QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OS问题单", "0");
				list.add(osTypeVO);
				QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
				list.add(LogiTypeVO);
				QuestionTypeVO allTypeVO = new QuestionTypeVO(desc, "全部正常化", "2");
				list.add(allTypeVO);
			} else {
				if (shipQuestionCount > 0) {
					desc = "此问题单是物流问题单。";
					QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
					list.add(LogiTypeVO);
				}
				if (osQuestionCount > 0) {
					desc = "此问题单是OS问题单。";
					QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OS问题单", "0");
					list.add(osTypeVO);
				}
			}
			if (StringUtil.isNotNullForList(list)) {
				mav.addObject("list", list);
			}
			outPrintJson(response, list);
			return null;
		}
		boolean success = false;
		String msg = "交货单返回正常单失败";
		try {
			if (null == status) {
				writeMsgSucc(false, "订单编号为空！", response);
				return null;
			}
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				writeMsgSucc(false, "登录用户信息不存在！", response);
				return null;
			}
			//是否占用库存
			if("1".equals(hasOccupyStock)){
				status.setSwitchFlag(true);
			}else{
				status.setSwitchFlag(false);
			}
			//操作人
			status.setAdminUser(adminUser.getUserName());
			ReturnInfo ri = orderNormalService.normalOrderByOrderSn(status.getOrderSn(), status);
			if (null != ri && ri.getIsOk() == Constant.OS_YES) {
				success = true;
				msg = ri.getMessage();
			} else if (null != ri) {
				msg = ri.getMessage();
			}
		} catch(OrderException oe) {
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
		return null;
	}
	

	
	/**
	 * 主付款单已支付
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param masterPaySn
	 * @return
	 */
	@RequestMapping(value = "pay")
	@ResponseBody
	public Map pay(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String masterPaySn){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "主单已支付失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if("".equals(masterPaySn)||masterPaySn==null){
				msg = "主付款单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setPaySn(masterPaySn);
			orderStatus.setSource("OMS");
			orderStatus.setMessage("主付款单已付款");
			ReturnInfo ri = payService.payStCh(orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "主付款单已付款成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 主付款单未支付
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param masterPaySn
	 * @return
	 */
	@RequestMapping(value = "unPay")
	@ResponseBody
	public Map unPay(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String masterPaySn){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "主单未支付失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if("".equals(masterPaySn)||masterPaySn==null){
				msg = "主付款单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setPaySn(masterPaySn);
			orderStatus.setSource("OMS");
			orderStatus.setMessage("主付款单未付款");
			ReturnInfo ri = payService.unPayStCh(orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "主付款单未付款成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 主单分配
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param masterPaySn
	 * @return
	 */
	@RequestMapping(value = "allocation")
	@ResponseBody
	public Map allocation(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "主单分配失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage("主单分配");
			ReturnInfo ri = orderDistributeOpService.allocationByMaster(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "主单分配成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	@RequestMapping(value = "sonAllocation")
	@ResponseBody
	public Map sonAllocation(HttpServletRequest request,
			HttpServletResponse response,String orderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "交货单分配失败！";
		try{
			if("".equals(orderSn)||orderSn==null){
				msg = "交货单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage("交货单分配");
			ReturnInfo ri = orderDistributeOpService.allocation(orderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "交货单分配成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 主单结算
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "settle")
	@ResponseBody
	public Map settle(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "主单结算失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setMessage("主单结算");
			ReturnInfo ri = orderDistributeOpService.settleOrder(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "主单结算成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 订单复活
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "relive")
	@ResponseBody
	public Map relive(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "订单复活失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.valueOf(adminUser.getUserId()));
			orderStatus.setMessage("订单复活");
			ReturnInfo ri = orderDistributeOpService.reviveOrder(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "订单复活成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 仅退货
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "onlyReturn")
	@ResponseBody
	public Map onlyReturn(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String message){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "仅退货失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			ReturnInfo ri = orderExchangeStService.onlyReturnGoods(masterOrderSn, adminUser.getUserName(), "仅退货", Integer.valueOf(adminUser.getUserId()));
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "仅退货成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param isHistory是否历史单 0：否 1：是
	 * @return
	 */
	@RequestMapping(value = "getLogs")
	@ResponseBody
	public Map getLogs(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,Integer isHistory){
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "日志补偿失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "主单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setIsHistory(isHistory);
			//拼装接口入参
			ReturnInfo ri = orderDistributeOpService.moveOrderAction(masterOrderSn, orderStatus);
			if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "日志补偿成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 一键设置问题单
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "oneKeyQuest")
	public Map oneKeyQuest(HttpServletRequest request, HttpServletResponse response,OrderStatus orderStatus) throws Exception {
		
		Map map = new HashMap();
		String code = "0";//0:失败  1:成功
		String msg = "一键设置问题单失败！";
		try{
			if (null == orderStatus || StringUtil.isBlank(orderStatus.getMasterOrderSn())) {
				msg = "订单编号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				msg = "登录用户信息不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//拼装接口入参
			orderStatus.setSwitchFlag(false);//默认不释放库存
			orderStatus.setMessage("一键设置问题单");
			orderStatus.setCode("19");//默认操作原因：客户退款转问题单
			orderStatus.setAdminUser(adminUser.getUserName());//操作人
			//调用接口
			ReturnInfo ri = orderQuestionService.questionOrderByMasterSn(orderStatus.getMasterOrderSn(), orderStatus);
			if (null != ri && ri.getIsOk() == Constant.OS_YES) {
				code = "1";
				msg = "一键设置问题单成功！";
			} else if (null != ri) {
				msg = ri.getMessage();
			}
		}catch(Exception e){
			e.printStackTrace();
			msg = msg + e ;
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/***
	 *强制下发 
	 ***/
	@RequestMapping(value = "mandatoryIssued")
	public void mandatoryIssued(HttpServletRequest request, HttpServletResponse response, String orderSns) throws Exception {
		
		String msg = "强制下发失败！";
		boolean success = false;
		if (null == orderSns ||  0 == orderSns.length()) {
			writeMsgSucc(false, "订单编号为空！", response);
			return;
		}
		
		String [] orderSn = orderSns.split(",");
		
		try{
			for(String orderSnStr: orderSn){
				distributeSupplierService.executeMaster(orderSnStr, true);
			}
			success = true;
		}catch (Exception e){
			logger.error(msg + e.getMessage());
			msg = msg + e.getMessage();
		}finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
		
	}
	
	/**
	 * 订单批量确认
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "batchConfirm")
	public void batchConfirm(HttpServletRequest request, HttpServletResponse response, String[] masterOrderSns, String message) throws Exception {
		boolean success = false;
		String msg = "batchConfirm : 订单批量确认失败";
		if (null == masterOrderSns || masterOrderSns.length == 0) {
			writeMsgSucc(false, "订单编号为空！", response);
			return;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		try {
			for (String orderSn : masterOrderSns) {
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setAdminUser(adminUser.getUserName());
				orderStatus.setType("0");
				orderStatus.setMasterOrderSn(orderSn);
				orderStatus.setMessage("订单批量确认：");
				final String param = JSON.toJSONString(orderStatus);
				batchConfirmJmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						TextMessage message = session.createTextMessage();
						message.setText(param);
						return message;
					}
				});
			}
			success = true;
			msg = "订单批量确认成功";
		} catch(OrderException oe) {
			logger.error(msg, oe);
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg, e);
			msg = msg + e.getMessage();
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	
	/**
	 * 订单批量未确认
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "batchUnConfirm")
	public void batchUnConfirm(HttpServletRequest request, HttpServletResponse response, String[] masterOrderSns, String message) throws Exception {
		boolean success = false;
		String msg = "batchUnConfirm : 订单批量未确认失败";
		if (null == masterOrderSns || masterOrderSns.length == 0) {
			writeMsgSucc(false, "订单编号为空！", response);
			return;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		try {
			for (String orderSn : masterOrderSns) {
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setAdminUser(adminUser.getUserName());
				orderStatus.setType("0");
				orderStatus.setMasterOrderSn(orderSn);
				orderStatus.setMessage("订单批量未确认：");
				final String param = JSON.toJSONString(orderStatus);
				batchUnConfirmJmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						TextMessage message = session.createTextMessage();
						message.setText(param);
						return message;
					}
				});
			}
			success = true;
			msg = "订单批量未确认成功";
		} catch(OrderException oe) {
			logger.error(msg, oe);
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg, e);
			msg = msg + e.getMessage();
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	
	
	@Resource(name = "orderUnConfirmProviderJmsTemplate")
	private JmsTemplate batchUnConfirmJmsTemplate;
	
	@Resource(name = "orderConfirmProviderJmsTemplate")
	private JmsTemplate batchConfirmJmsTemplate;
	
	
	/**
	 * 订单批量下发
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "batchToErp")
	public void batchToErp(HttpServletRequest request, HttpServletResponse response, String[] orderSns, String type) throws Exception {
		boolean success = false;
		String msg = "订单批量下发失败";
		try {
			if (null == orderSns || orderSns.length == 0) {
				writeMsgSucc(false, "订单编号为空！", response);
				return;
			}
			AdminUser adminUser = getLoginUser(request);

			if (null == adminUser) {
				writeMsgSucc(false, "登录用户信息不存在！", response);
				return;
			}
			for (String orderSn : orderSns) {
			
				if(type.equals("0")){
					OrderDepotResult OrderDepotResult1 = distributeSupplierService.executeMasterByMq(orderSn); // 主单
					success = true;
					msg = "订单批量下发成功";
				} else if(type.equals("1")){
					OrderDepotResult OrderDepotResult = distributeSupplierService.executeDistributeByMq(orderSn); //子单
					success = true;
					msg = "订单批量下发成功";
				} else{
					success = false;
					msg = "订单批量下发失败";
				}
	
			}
			
		} catch(OrderException oe) {
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	
/*	@Resource
	private OrderPushService orderPushService;*/
	
	/**
	 * 订单分仓发货
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "swdiInList")
	public void swdiInList(HttpServletRequest request, HttpServletResponse response, String[] orderSn, String type) throws Exception {
		boolean success = false;
		String msg = "订单分仓发货失败";
		if (null == orderSn || orderSn.length == 0) {
			writeMsgSucc(false, "订单编号为空！", response);
			return;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		try {
		//	if (orderSn.length == 1) {
				// 调用获取分仓发货信息接口
			//	ReturnInfo ri = orderStChService.getCrossWarehShipped(orderSn[0]);
			/*	if (null != ri && ri.getIsOk() == Constant.OS_YES) {
					success = true;
					msg = ri.getMessage();
				} else if (null != ri) {
					msg = ri.getMessage();
				}*/
		//	} else {
	/*			for (String subOrderSn : orderSn) {
					final String param = subOrderSn;
					crossWarehShippedJmsTemplate.send(new MessageCreator() {
						public Message createMessage(Session session) throws JMSException {
							TextMessage message = session.createTextMessage();
							message.setText(param);
							return message;
						}
					});
		//		}
		 * 
*/				
		/*	OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			orderStatus.setUserId(Integer.parseInt(adminUser.getUserId()));
			orderStatus.setMessage("订单分仓发货");*/
	//		ReturnInfo ri = orderDistributeOpService.sWDI(masterOrderSn, "", orderStatus);
		/*	if(ri!=null){
				if(ri.getIsOk()==1){
					code = "1";
					msg = "swdi:订单分仓发货成功！";
				}else{
					if(!"".equals(ri.getMessage())&&ri.getMessage()!=null){
						msg = ri.getMessage();
					}
				}
			}*/

			orderDistributeOpService.swdiPushMQ(orderSn,  type);
			success = true;
				msg = "订单分仓发货成功";
			//}
		} catch(OrderException oe) {
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
	}
	

//	@Resource
//	private OrderStChService orderStChService;
	
	@Resource(name = "distributeSwdiJmsTemplate")
	private JmsTemplate crossWarehShippedJmsTemplate;
	
	
	/**
	 * 订单批量取消
	 * @param request
	 * @param response
	 * @param orderStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "batchCancel")
	public void batchCancel(HttpServletRequest request, HttpServletResponse response, String[] orderSns, OrderStatus status) throws Exception {
		boolean success = false;
		String msg = "订单批量取消失败";
		try {
			if (null == orderSns || orderSns.length == 0) {
				writeMsgSucc(false, "订单编号为空！", response);
				return;
			}
			AdminUser adminUser = getLoginUser(request);

			if (null == adminUser) {
				writeMsgSucc(false, "登录用户信息不存在！", response);
				return;
			}

			BatchReturnInfo ri = new BatchReturnInfo();
			ri.setIsOk(Constant.OS_NO);
			StringBuffer resultNote = new StringBuffer("");

			/* 检查参数 */
			if (orderSns == null || orderSns.length == 0) {
				resultNote.append("传入订单编号数组参数为空！");
				ri.setMessage(resultNote.toString());
				writeMsgSucc(false, msg, response);
				return;
				
			}
	
				/* 循环确定操作 */
				for (int j = 0; j < orderSns.length; ++j) {
					String orderSn = orderSns[j];
					
					OrderStatus param = new OrderStatus();
					param.setAdminUser(adminUser.getUserName());
			
					param.setMasterOrderSn(orderSn);
				//	param.setActionNote(status.getMessage());
					param.setMessage(status.getMessage());
					param.setCode(status.getCode()); // type = 8 . 编码长度为4位。erp使用后两位
			//		param.setRealeseStock(false);
			//		param.setOccupyStock(true);
					param.setUserId(Integer.valueOf(adminUser.getUserId()));
			//		param.setCancelSourceType("OS");
				//	param.setCreateReturn("1");
					param.setType(status.getType());
					String message = JSON.toJSONString(param);
					final String msg1 = message;
					cancelOrderinfoJmsTemplate.send(new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							TextMessage message = session.createTextMessage();
							message.setText(msg1);
							logger.debug("CANCELORDERINFO message to  Queue:"+msg1);
							return session.createTextMessage(msg1);
						}
					});
		
				}
	
				success = true;
				msg = "订单已放入队列";
		} catch(OrderException oe) {
			logger.error(msg + oe.getMessage());
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg + e);
			msg = msg + e;
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
		return;
	}

	@Resource(name = "orderCancelProviderJmsTemplate")
	private JmsTemplate cancelOrderinfoJmsTemplate;
	
	/**
	 * 批量返回正常单
	 * @param request
	 * @param response
	 * @param status
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "batchNormal")
	public ModelAndView batchReturnNormal(HttpServletRequest request, HttpServletResponse response, OrderStatus status,
			String[] orderSns, String hasOccupyStock, String mainChild, String type) throws Exception {
		boolean success = false;
		String msg = "normal : 批量返回正常单失败";
//		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == status) {
			msg = "参数不能为空！";
			logger.error("批量返回正常单:参数不能为空！");
			return null;
		}
		if (null == orderSns || orderSns.length == 0) {
			msg = "订单编号为空！";
			logger.error("批量返回正常单:订单编号为空！");
			return null;
		}
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			msg = "登录用户信息不存在或者没有登陆！";
			logger.error("批量返回正常单:登录用户信息不存在或者没有登陆！");
			return null;
		}
		try {
			for (String orderSn : orderSns) {
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setAdminUser(adminUser.getUserName());

				orderStatus.setMessage("批量返回正常单：" + status.getMessage());
			
   		        if("main".equals(mainChild)){//主订单号
					orderStatus.setMasterOrderSn(orderSn);
				}else{//交货单号
					orderStatus.setOrderSn(orderSn);
				}
				
				if("true".equals(type)){ //一般问题单
					orderStatus.setType("0");
				}else {//缺货问题单
					orderStatus.setType("1");
				}
			
				if (null !=  hasOccupyStock  && "1".equals(hasOccupyStock)){
					orderStatus.setSwitchFlag(true);
				}else {
					orderStatus.setSwitchFlag(false);
				}
				 
				final String param = JSON.toJSONString(orderStatus);
				batchNormalJmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						TextMessage message = session.createTextMessage();
						message.setText(param);
						return message;
					}
				});
			}
			success = true;
			msg = "订单批量返回正常单放入MQ成功";

		} catch(OrderException oe) {
			logger.error(msg, oe);
			msg = msg + oe.getMessage();
		} catch (Exception e) {
			logger.error(msg, e);
			msg = msg + e.getMessage();
		} finally {
			if (success) {
				writeMsgSucc(true, msg, response);
			} else {
				writeMsgSucc(false, msg, response);
			}
		}
		return null;
	}
	
	@Resource(name = "orderNormalProviderJmsTemplate")
	private JmsTemplate batchNormalJmsTemplate;

}
