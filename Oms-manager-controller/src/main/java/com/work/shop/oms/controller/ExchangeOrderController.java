package com.work.shop.oms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.oms.api.param.bean.ExchangeOrderBean;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoHistory;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeHistory;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.MasterOrderInfoVO;
import com.work.shop.oms.dao.MasterOrderInfoHistoryMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDistributeHistoryMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderReturn.service.OrderExchangeStService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.Constants;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.ReturnCommonVO;

@Controller
@RequestMapping(value = "/exchangeorder")
public class ExchangeOrderController extends BaseController{

	private final static Logger logger = Logger.getLogger(ExchangeOrderController.class);

	@Resource
	private OrderExchangeStService orderExchangeStService;
	
	@Resource
	private OrderConfirmService orderConfirmService;
	
//	@Autowired
//	private OrderActionService orderActionService;
	
	@Resource
	private MasterOrderActionService masterOrderActionService;
	
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private MasterOrderInfoHistoryMapper masterOrderInfoHistoryMapper;
	@Resource
	private OrderDistributeHistoryMapper orderDistributeHistoryMapper;
	
	@RequestMapping(value="exchangeDetailPage")
	public ModelAndView exchangeDetailPage(HttpServletRequest request,HttpServletResponse response,String relatingOrderSn,String exchangeOrderSn,Integer isHistory){
		ModelAndView mav = new ModelAndView();
		if (isHistory == null) {
			isHistory = 0;
		}
		if(StringUtil.isNotBlank(exchangeOrderSn)){//换货单详情
			if(isHistory == 0){
				//exchangeOrderSn是换单的主单号
				MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(exchangeOrderSn);
				mav.addObject("orderSn", orderInfo.getRelatingOriginalSn());
				mav.addObject("returnSn",orderInfo.getRelatingReturnSn());
				mav.addObject("exchangeOrderSn",exchangeOrderSn);
				String relatingOriginalSn = orderInfo.getRelatingOriginalSn();//关联的交货单号
//				OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(relatingOriginalSn);
				mav.addObject("masterOrderSn",relatingOriginalSn);
			}else{
				MasterOrderInfoHistory orderInfoHistory = masterOrderInfoHistoryMapper.selectByPrimaryKey(relatingOrderSn);
				//mav.addObject("orderSn", orderInfoHistory.getRelatingOriginalSn());
				mav.addObject("returnSn",orderInfoHistory.getRelatingReturnSn());
				mav.addObject("exchangeOrderSn",exchangeOrderSn);
				String relatingOriginalSn = orderInfoHistory.getRelatingOriginalSn();//关联的交货单号
				OrderDistributeHistory orderDistribute = orderDistributeHistoryMapper.selectByPrimaryKey(relatingOriginalSn);
				mav.addObject("masterOrderSn",orderDistribute.getMasterOrderSn());
			}
		}else if(StringUtil.isNotBlank(relatingOrderSn)){//申请换货单
			mav.addObject("orderSn",relatingOrderSn);
			OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(relatingOrderSn);
			if (orderDistribute != null) {
				mav.addObject("masterOrderSn",orderDistribute.getMasterOrderSn());
				MasterOrderInfo masterOrderInfo = this.masterOrderInfoMapper.selectByPrimaryKey(orderDistribute.getMasterOrderSn());
				mav.addObject("userId",masterOrderInfo.getUserId());
			}
		}

		mav.addObject("isHistory", isHistory);
		mav.setViewName("exchangeOrder/redirectExchange");
		return mav;
	}
	
	@RequestMapping(value = "getExchangeOrderDetail")
	public ModelAndView getExchangeOrderDetail(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn, Integer isHistory) {
		logger.info("exchangeOrderSn=" + exchangeOrderSn + "  isHistory="+ isHistory);
		Map<String,Object>data=new HashMap<String, Object>();
		try {
			AdminUser adminUser = getLoginUser(request);
			if (StringUtil.isNotEmpty(exchangeOrderSn)) {
				MasterOrderInfoVO infoVO = this.orderInfoService.getOrderDetail(adminUser,exchangeOrderSn, isHistory);
				if (null == infoVO) {
					data.put("success", false);
					data.put("errorMessage", "该订单["+exchangeOrderSn+"]不存在！");
				}else{
					/*	
					map.put("orderStatusUtils", orderStatusUtils);
					map.put("masterOrderInfo", masterOrderInfo);
					map.put("masterOrderInfoExtend", masterOrderInfoExtend);
					map.put("masterOrderAddressInfo", masterOrderAddressInfo);
					map.put("mergedMasOrdGoodsDetailList", mergedMasOrdGoodsDetailList);
					map.put("sonOrderList", sonOrderList);
					map.put("masterOrderPayTypeDetailList", masterOrderPayTypeDetailList);*/
					Map<String, Integer> buttonStatusMap = controlOrderExchangeButton(infoVO);
					data.put("success", true);
					data.put("buttonStatus", buttonStatusMap);
					data.put("result", infoVO.getMasterOrderInfo());
					data.put("goodDetail",infoVO.getMergedMasOrdGoodsDetailList());
					data.put("delivery", infoVO.getOrderDepotShipDetailList());
					data.put("payDetail", infoVO.getMasterOrderPayTypeDetailList());
				}
				}
		} catch (Exception e) {
			logger.error("获取换单详情数据,orderSn=" +exchangeOrderSn +"异常：", e);
		}
		outPrintJson(response, data);
		return null;
	}
	
	@RequestMapping(value = "getOrderDetail")
	public ModelAndView getOrderDetail(HttpServletRequest request,
			HttpServletResponse response, String orderSn, Integer isHistory) {
		logger.info("orderSn=" + orderSn + "  isHistory="+ isHistory);
		Map<String,Object>data=new HashMap<String, Object>();
		AdminUser adminUser = getLoginUser(request);
		data = this.orderInfoService.getExchangeOrderDetail(adminUser,orderSn, isHistory);
		data.put("errorMessage", data.get("message"));
		outPrintJson(response, data);
		return null;
	}
	
	//保存换单
	@RequestMapping(value = "saveExchangeButtonClick")
	public ModelAndView saveExchangeButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionNote,ExchangeOrderBean exchangeBean) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "保存换单失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionNote,exchangeBean, adminUser));
		return null;
	}
	
	//未确认换单
	@RequestMapping(value = "unConfirmExchangeButtonClick")
	public ModelAndView unComfirmExchangeButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionNote) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "未确认换单失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionNote,null,adminUser));
		return null;
	}
	//确认换单
	@RequestMapping(value = "confirmExchangeButtonClick")
	public ModelAndView comfirmExchangeButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionNote) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "确认换单失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionNote,null,adminUser));
		return null;
	}
	//确认换单
	@RequestMapping(value = "actionClickButtonClick")
	public ModelAndView actionClickButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionMsg) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "确认换单失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionMsg,null, adminUser));
		return null;
	}
	//取消换单
	@RequestMapping(value = "cancelExchangeButtonClick")
	public ModelAndView cancelExchangeButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionNote) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "取消换单失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionNote,null, adminUser));
		return null;
	}
	//仅退货
	@RequestMapping(value = "onlyReturnExchangeButtonClick")
	public ModelAndView onlyReturnExchangeButtonClick(HttpServletRequest request,
			HttpServletResponse response, String exchangeOrderSn,String btnType,String actionNote) throws Exception {
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("errorMessage", "仅退货失败：登陆用户信息为空！");
			outPrintJson(response, data);
			return null;
		}
		outPrintJson(response, processButtonClick(exchangeOrderSn, btnType, actionNote,null, adminUser));
		return null;
	}

	/**
	 * 查询用户预付卡余额
	 * @param request
	 * @param response
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value = "getUserRealMoney")
	public void getUserRealMoney(HttpServletRequest request,
			HttpServletResponse response, String userId) throws Exception {
		logger.info("查询用户预付卡余额 userId:" + userId);
		AdminUser adminUser = getLoginUser(request);
		ReturnInfo<Double> info = new ReturnInfo<Double>(Constant.OS_NO);
		if (null == adminUser) {
			logger.error(userId + "查询用户预付卡余额：登陆用户信息为空！");
			info.setMessage(userId + "查询用户预付卡余额：登陆用户信息为空！");
			outPrintJson(response, info);
			return;
		}
		try {
			logger.info("查询用户预付卡余额 userId:" + userId);
//			double data = platfromBvOrderApi.getUserRealMoney(userId);
			double data = 0D;
			logger.info("查询用户预付卡余额 :" + data);
			info.setData(data);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		} catch (Exception e) {
			logger.error("查询用户预付卡余额异常：" + e.getMessage(), e);
			info.setMessage("查询用户预付卡余额异常：" + e.getMessage());
		}
		outPrintJson(response, info);
		return ;
	}

	/**
	 * 换单详情-按钮处理公共逻辑
	 * @param returnSn
	 * @param btnType
	 * @param exchangeOrderBean
	 * @return
	 */
	private Map<String,Object> processButtonClick(String exchangeOrderSn,String btnType,
			String actionNote,ExchangeOrderBean exchangeOrderBean, AdminUser adminUser){
		Map<String,Object> data = new HashMap<String, Object>();
		ReturnInfo result = null;
		try{
			String actionUser = adminUser.getUserName();
			Integer userId = Integer.valueOf(adminUser.getUserId());
			actionNote = StringUtils.trimToEmpty(actionNote);
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(exchangeOrderSn);
			orderStatus.setAdminUser(adminUser.getUserName());
			if(Constants.EXCHANGE_ORDER_CLICK_TYPE_SAVEEXCHANGE.equalsIgnoreCase(btnType)){
				//保存换单
				exchangeOrderBean.setActionUser(actionUser);
				result = orderExchangeStService.createNewExchangeOrder(exchangeOrderBean);
			}else if(Constants.EXCHANGE_ORDER_CLICK_TYPE_CONFIRMEXCHANGE.equalsIgnoreCase(btnType)){
				//确认换单
				orderStatus.setMessage("确认订单");
				result = orderConfirmService.confirmOrderByMasterSn(exchangeOrderSn, orderStatus);
			}else if(Constants.EXCHANGE_ORDER_CLICK_TYPE_UNCONFIRMEXCHANGE.equalsIgnoreCase(btnType)){
				//未确认换单
				orderStatus.setMessage("未确认订单");
				result = orderConfirmService.unConfirmOrderByMasterSn(exchangeOrderSn, orderStatus);
			}else if(Constants.EXCHANGE_ORDER_CLICK_TYPE_ACTIONCLICK.equalsIgnoreCase(btnType)){
				//沟通
				masterOrderActionService.insertOrderActionBySn(exchangeOrderSn, "沟通："+actionNote, adminUser.getUserName());
//				result = orderActionService.communicate(exchangeOrderSn, actionUser, "沟通："+actionNote);
				result.setMessage("沟通："+actionNote);
			}else if(Constants.EXCHANGE_ORDER_CLICK_TYPE_CANCELEXCHANGE.equalsIgnoreCase(btnType)){
				//取消换单
				result = orderExchangeStService.cancelExchangeOrder(exchangeOrderSn, actionNote, actionUser,userId);
			}else if(Constants.EXCHANGE_ORDER_CLICK_TYPE_ONLYRETURN.equalsIgnoreCase(btnType)){
				//仅退货
				result = orderExchangeStService.onlyReturnGoods(exchangeOrderSn, actionUser, actionNote,userId);
			}else{
				throw new RuntimeException("无法解析前台操作");
			}
			data.put("ReturnInfo", result);				 
			if(result.getIsOk() > 0){
				data.put("success", true);
			}else{
				data.put("success", false);
				data.put("errorMessage", "换单操作失败："+result.getMessage());
			}
			data.put("returnCommon", new ReturnCommonVO());
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "换单操作异常："+e.getMessage());
			logger.error("换单按钮操作,returnSn=" +exchangeOrderSn +"异常：" + e.getMessage());
		}
		return data;
	}
	
	/**
	 * 换单页面按钮状态控制
	 * @param returnObj
	 * @return
	 */
	private Map<String,Integer> controlOrderExchangeButton(MasterOrderInfoVO infoVO){
		Map<String,Integer> buttonStatusMap = new HashMap<String,Integer>();
		MasterOrderDetail masterOrderInfo = infoVO.getMasterOrderInfo();
		String exchangeOrderSn = masterOrderInfo.getMasterOrderSn();
		if(StringUtils.isBlank(exchangeOrderSn)){
			buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_SAVEEXCHANGE, ConstantValues.YESORNO_YES);
		}else{
			buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_ACTIONCLICK, ConstantValues.YESORNO_YES);		   
		}
		if(masterOrderInfo.getOrderStatus().intValue() == ConstantValues.ORDER_STATUS.CANCELED.intValue()){
			//全部禁用
		}else{
			buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_CANCELEXCHANGE, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_ONLYRETURN, ConstantValues.YESORNO_YES);
			//确认
			if(masterOrderInfo.getOrderStatus().intValue() == ConstantValues.ORDER_STATUS.CONFIRMED.intValue()){
				buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_UNCONFIRMEXCHANGE, ConstantValues.YESORNO_YES);
			}else {
				buttonStatusMap.put(Constants.EXCHANGE_ORDER_CLICK_TYPE_CONFIRMEXCHANGE, ConstantValues.YESORNO_YES);
			}
		}
		return buttonStatusMap;
	}
}
