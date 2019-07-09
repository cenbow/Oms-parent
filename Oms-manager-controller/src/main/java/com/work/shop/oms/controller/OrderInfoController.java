package com.work.shop.oms.controller;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.MasterOrderPayTypeDetail;
import com.work.shop.oms.bean.OrderDepotShipDetail;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.common.bean.Common;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.utils.DistributeOrderInfoVO;
import com.work.shop.oms.common.utils.DistributeOrderStatusUtils;
import com.work.shop.oms.common.utils.MasterOrderInfoVO;
import com.work.shop.oms.common.utils.OrderStatusUtils;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.service.SystemRegionAreaService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.FtpUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.Pagination;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.DeliveryInfoParam;

@Controller
@RequestMapping(value = "orderInfo")
public class OrderInfoController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OrderInfoService orderInfoService;

	/**
	 * 订单详情页页面跳转
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param orderSn
	 * @param isHistory
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderDetail")
	public ModelAndView orderDetail(HttpServletRequest request,
			HttpServletResponse response, String masterOrderSn,String orderSn, Integer isHistory) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("masterOrderSn", masterOrderSn);
		mav.addObject("initOrderSn", orderSn);
		if (isHistory == null) {
			isHistory = 0;
		}
		mav.addObject("isHistory", isHistory);
		mav.setViewName("orderInfo/orderInfoPage");
		return mav;
	}
	
	/**
	 * 获取订单详情数据
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getOrderDetail")
	public Object getOrderDetail(HttpServletRequest request,
			HttpServletResponse response, @Param("masterOrderSn") String masterOrderSn, Integer isHistory){
		Map<String,Object> data = new HashMap<String,Object>();
		String msg = "获取订单数据异常！";
		boolean success = false;
		try{
			//判断登录
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
				data.put("success", success);
				data.put("msg", msg);
				outPrintJson(response,data);
				return null;
			}
			//判断订单号
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效的主单号！";
				data.put("success", success);
				data.put("msg", msg);
				outPrintJson(response,data);
				return null;
			}
			//请求数据
			MasterOrderInfoVO masterOrderInfoVO = orderInfoService.getOrderDetail(adminUser, masterOrderSn, isHistory);
			MasterOrderDetail masterOrderInfo = masterOrderInfoVO.getMasterOrderInfo();
			if(masterOrderInfo==null){
				msg = "该订单["+masterOrderSn+"]不存在！";
				data.put("success", success);
				data.put("msg", msg);
				outPrintJson(response,data);
				return null;
			}
			//获取数据
			OrderStatusUtils orderStatusUtils = masterOrderInfoVO.getOrderStatusUtils();//主单按钮状态信息
			List<MasterOrderGoodsDetail> mergedMasOrdGoodsDetailList = masterOrderInfoVO.getMergedMasOrdGoodsDetailList();//主单商品信息（含发货仓、配送信息，且合并）
			List<DistributeOrderInfoVO> sonOrderList = masterOrderInfoVO.getSonOrderList();//交货单配套信息
			List<MasterOrderPayTypeDetail> masterOrderPayTypeDetailList = masterOrderInfoVO.getMasterOrderPayTypeDetailList();//付款信息
			double returnSettleMoney = masterOrderInfoVO.getReturnSettleMoney();//主单下的退单总金额
			//获取系统操作权限
			HttpSession Session = request.getSession();
			List<SystemOmsResource> resources = (List<SystemOmsResource>)Session.getAttribute(orderListPageNo);
			Map<String, Integer> resourceMap = new HashMap<String, Integer>();
			if(resources!=null&&resources.size()>0){
				for (SystemOmsResource resource : resources) {
					resourceMap.put(resource.getResourceCode(), 1);
				}
			}
			//主单按钮跟系统操作权限比对
			Field[] fields= orderStatusUtils.getClass().getDeclaredFields();
			boolean authPay = false;
			boolean authUnPay = false;
			int authShipping = 0;
			for(int i=0;i<fields.length;i++){
				String fieldName = fields[i].getName();
				Object fieldValue = getFieldValueByName(fieldName, orderStatusUtils);
				Class fieldType = fields[i].getType();
				if (fieldType != null && "java.lang.Integer".equals(fieldType.getName())) {
					Integer code = resourceMap.get(orderInfoPageNo + fieldName);
					if (code != null && code.intValue() == 1
							&& fieldValue != null && ((Integer)fieldValue).intValue() == 1) {
						setFieldValueByName(fieldName, orderStatusUtils, 1);
						if ("pay".equals(fieldName)) {
							authPay = true;
						} else if ("unpay".equals(fieldName)) {
							authUnPay = true;
						} else if ("shipping".equals(fieldName)) {
							authShipping = 1;
						}
					} else {
						if(fieldName.equals("getLogs")&& fieldValue != null && ((Integer)fieldValue).intValue() == 1){
							setFieldValueByName(fieldName, orderStatusUtils, 1);
						}else if (fieldName.equals("occupy") && fieldValue != null && ((Integer)fieldValue).intValue() == 1) {
							setFieldValueByName(fieldName, orderStatusUtils, 1);
						} else {
							setFieldValueByName(fieldName, orderStatusUtils, 0);
						}
					}
				}
			}
			if(masterOrderPayTypeDetailList!=null&&masterOrderPayTypeDetailList.size()>0){
				for(MasterOrderPayTypeDetail bean : masterOrderPayTypeDetailList){
					// 判断支付状态和支付权限 加载支付未支付按钮
					int pay = 0;
					if (authPay && orderStatusUtils.getPay() == 1) {
						pay = 1;
					}
					int unpay = 0;
					if (authUnPay && orderStatusUtils.getUnpay() == 1) {
						unpay = 1;
					}
					bean.setPay(pay);
					bean.setUnpay(unpay);
				}
			}
			//交货单按钮跟系统操作权限比对
			if(sonOrderList!=null&&sonOrderList.size()>0){
				for (DistributeOrderInfoVO distributeOrderInfoVO : sonOrderList) {
					DistributeOrderStatusUtils sonOrderButtonStatus = distributeOrderInfoVO.getDistributeOrderStatusUtils();
					if(sonOrderButtonStatus!=null){
						Field[] sonFields= sonOrderButtonStatus.getClass().getDeclaredFields();
						for(int i=0;i<sonFields.length;i++){
							String fieldName = sonFields[i].getName();
							Object fieldValue = getFieldValueByName(fieldName, sonOrderButtonStatus);
							Class fieldType = sonFields[i].getType();
							if (fieldType != null && "java.lang.Integer".equals(fieldType.getName())) {
								Integer code = resourceMap.get(sonOrderResourceHead + fieldName);
								if (code != null && code.intValue() == 1
										&& fieldValue != null && ((Integer)fieldValue).intValue() == 1) {
									setFieldValueByName(fieldName, sonOrderButtonStatus, 1);
								} else {//设置不可用
									setFieldValueByName(fieldName, sonOrderButtonStatus, 0);
								}
							}
						}
					}
					//交货单修改承运商权限
					List<OrderDepotShipDetail> sonOrderDepotShipList = distributeOrderInfoVO.getSonOrderDepotShipList();
					if(sonOrderDepotShipList!=null&&sonOrderDepotShipList.size()>0){
						for(OrderDepotShipDetail depotShip : sonOrderDepotShipList){
							depotShip.setDelivery(authShipping);
						}	
					}
				}
			}
			//返回数据
			msg = "获取订单数据成功！";
			data.put("orderStatusUtils", orderStatusUtils);
			data.put("masterOrderInfo", masterOrderInfo);
			data.put("mergedMasOrdGoodsDetailList", mergedMasOrdGoodsDetailList);
			data.put("sonOrderList", sonOrderList);
			data.put("masterOrderPayTypeDetailList", masterOrderPayTypeDetailList);
			data.put("returnSettleMoney", returnSettleMoney);
			data.put("success", true);
			data.put("msg", msg);
		}catch(Exception e){
			e.printStackTrace();
		}
		outPrintJson(response,data);
		return null;
	}
	
	/**
	 * 获取主订单收货人地址信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param isHistory
	 * @return
	 */
	@RequestMapping(value = "getMasterOrderAddressInfo")
	@ResponseBody
	public Map getMasterOrderAddressInfo(HttpServletRequest request,
			HttpServletResponse response, String masterOrderSn, Integer isHistory){
		return orderInfoService.getMasterOrderAddressInfo(getLoginUser(request), masterOrderSn, isHistory);
	}
	
	/**
	 * 保存收货地址信息
	 * @param request
	 * @param response
	 * @param formParam
	 * @param oldTel
	 * @param oldMobile
	 * @return
	 */
	@RequestMapping(value = "doSaveAddrEdit")
	@ResponseBody
	public Map doSaveAddrEdit(HttpServletRequest request,
			HttpServletResponse response,MasterOrderAddressInfo formParam,String oldTel,String oldMobile){
		return orderInfoService.doSaveAddrEdit(getLoginUser(request), formParam, oldTel, oldMobile);
	}
	
	/**
	 * 获取发票信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param isHistory
	 * @return
	 */
	@RequestMapping(value = "getMasterOrderInfoExtend")
	@ResponseBody
	public Map getMasterOrderInfoExtend(HttpServletRequest request,
			HttpServletResponse response, String masterOrderSn, Integer isHistory){
		return orderInfoService.getMasterOrderInfoExtend(getLoginUser(request), masterOrderSn, isHistory);
	}
	
	/**
	 * 保存发票编辑信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param invType
	 * @param invPayee
	 * @param invContent
	 * @param howOos
	 * @param postscript
	 * @return
	 */
	@RequestMapping(value = "doSaveOtherEdit")
	@ResponseBody
	public Map doSaveOtherEdit(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String invType,String invPayee,String invContent,String howOos,String postscript,String toBuyer){
		return orderInfoService.doSaveOtherEdit(getLoginUser(request), masterOrderSn, invType, invPayee, invContent, howOos, postscript,toBuyer);
	}
	
	/**
	 * 查询物流信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param orderSn
	 * @param depotCode
	 * @param invoiceNo
	 * @return
	 */
	@RequestMapping(value = "queryExpress")
	@ResponseBody
	public Map queryExpress(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String orderSn,String depotCode,String invoiceNo){
		return orderInfoService.queryExpress(masterOrderSn, depotCode, invoiceNo);
	}
	
	/**
	 * 获取可用的承运商列表
	 * @param request
	 * @param response
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "getAvaliableDelivery")
	@ResponseBody
	public Map getAvaliableDelivery(HttpServletRequest request,
			HttpServletResponse response,DeliveryInfoParam param){
		return orderInfoService.getAvaliableDelivery(param);
	}
	
	/**
	 * 保存承运商变更
	 * @param request
	 * @param response
	 * @param shippingCode
	 * @param orderSn
	 * @param depotCode
	 * @return
	 */
	@RequestMapping(value = "doSaveDeliveryChange")
	@ResponseBody
	public Map doSaveDeliveryChange(HttpServletRequest request,
			HttpServletResponse response,String shippingCode,String orderSn,String depotCode){
		return orderInfoService.doSaveDeliveryChange(getLoginUser(request), shippingCode,orderSn, depotCode);
	}
	
	/**
	 * 获取主单付款单信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @return
	 */
	@RequestMapping(value = "getMasterOrderPayInfo")
	@ResponseBody
	public Map getMasterOrderPayInfo(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn){
		return orderInfoService.getMasterOrderPayInfo(masterOrderSn);
	}
	
	@RequestMapping(value = "doSaveShippingFee")
	@ResponseBody
	public Map doSaveShippingFee(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String shippingTotalFee){
		return orderInfoService.doSaveShippingFee(getLoginUser(request), masterOrderSn, shippingTotalFee);
	}
	
	/**
	 * 获取卡券/红包详细信息
	 * @param request
	 * @param response
	 * @param cardNo
	 * @param couponType  20表示红包  30表示打折券
	 * @return
	 */
	@RequestMapping(value = "getCouponInfo")
	@ResponseBody
	public Map getCouponInfo(HttpServletRequest request,
			HttpServletResponse response,String cardNo,String couponType){
		return orderInfoService.getCouponInfo(cardNo, couponType);
	}
	
	/**
	 * 查询库存
	 * @param request
	 * @param response
	 * @param customCode
	 * @param channelCode
	 * @return
	 */
	@RequestMapping(value = "getStock")
	@ResponseBody
	public Map getStock(HttpServletRequest request,
			HttpServletResponse response,String customCode,String channelCode){
		return orderInfoService.getStock(customCode,channelCode);
	}
	
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	private void setFieldValueByName(String fieldName, Object o, Integer value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setter = "set" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(setter, new Class[] {Integer.class});
			method.invoke(o, value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	

	/**
	 * 订单列表查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderInfoList.spmvc")
	public ModelAndView OrderInfoInfoPage(HttpServletRequest request,
			HttpServletResponse response, Common orderInfoSerach, PageHelper helper, String method, String type) throws Exception {
		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
				mav.addObject("display", "1");
				mav.addObject("batchLock", "0");
			if(StringUtil.isNotBlank(type) && "cashOnDelivery".equals(type)) {
				mav.addObject("settleType", "1");
			}
			if(StringUtil.isNotBlank(type) && "orderInfoSettle".equals(type)) {
				mav.addObject("settleType", "orderInfoSettle");
			}
			mav.addObject("batchUnlock", "0");
			mav.addObject("batchConfirm", "0");
			int batchConfirm = 1;
			mav.addObject("batchConfirm", batchConfirm);
			mav.setViewName("orderInfoList/orderInfoListPage");
			return mav;
		}
		try {
			HttpSession session = request.getSession();
			List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
			if (!StringUtil.isListNotNull(strings)) {
				return null;
			}
			orderInfoSerach.setSites(strings);
			Paging paging = null;
			if(StringUtil.isNotBlank(type) && "orderInfoSettle".equals(type)) {
				orderInfoSerach.setIsNotQuanQiuTong(true);
				paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			} else{
				paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			}
			writeJson(paging, response);
		} catch (Exception e) {
			logger.error("查询订单列表异常" + e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 订单csv导出
	 * @param request
	 * @param response
	 * @param orderInfoSerach
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	@RequestMapping(value = "orderInfoExportCsvFile.spmvc")
	public void orderInfoExportCsvFile(HttpServletRequest request,
			HttpServletResponse response,  Common orderInfoSerach, PageHelper helper)throws Exception{
		InputStream is = null;
		BufferedWriter writer = null;
		logger.info("OrderInfoController.orderInfoExportCsvFile  begin..:Common : " + JSON.toJSONString( orderInfoSerach) );
		HttpSession session = request.getSession();
		List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
		if (!StringUtil.isListNotNull(strings)) {
			return;
		}
		orderInfoSerach.setSites(strings);
		orderInfoSerach.setExportTemplateType("3");
		int pageSize = 5000;
		StringBuffer sb = new StringBuffer();
		Pagination pagination = new Pagination(1, pageSize);
		helper.setLimit(pageSize);
		try{
			// 创建本地文件
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "oiTempFile";
			String sfileName ="orderInfoList" + dateStr+".csv";
			String fileName = folderName + "/"+sfileName;
			logger.info("OrderInfoController.orderInfoExportCsvFile fileName = "+ fileName);
			File fileRoot=new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			logger.info("OrderInfoController.orderInfoExportCsvFile basepath="+ request.getSession().getServletContext().getRealPath("/") );
			if(!fileRoot.exists()){
				fileRoot.mkdirs();
			}
			String path = request.getSession().getServletContext().getRealPath("/") + fileName;
			logger.info("OrderInfoController.orderInfoExportCsvFile   path =     "+ path);
			if(StringUtil.isNull(orderInfoSerach.getExportTemplateType()) || "3".equals(orderInfoSerach.getExportTemplateType())){
				logger.info("OrderInfoController.orderInfoExportCsvFile    ExportTemplateType=  " +  orderInfoSerach.getExportTemplateType() +"      默认模版   start............    " );
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
				Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
				List<Common> orderInfoList = (List<Common>) paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				sb.append(
					"订单号," + 
					"外部交易号," +
					"订单店铺," + 
//					"店铺地址," +
					"收货人姓名," +
					"手机号码," +
					"商品编码," +
					"商品数量" +
					"\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportInfoGoods(orderInfoList, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
						getExportInfoGoods((List<Common>) paging.getRoot(), writer);
					}
				}
				logger.info("OrderInfoController.orderInfoExportCsvFile    ExportTemplateType=0      默认模版   end............    " );
			}
			/*if(StringUtil.isNull(orderInfoSerach.getExportTemplateType()) || "3".equals(orderInfoSerach.getExportTemplateType())){
				logger.info("OrderInfoController.orderInfoExportCsvFile    ExportTemplateType=  " +  orderInfoSerach.getExportTemplateType() +"      默认模版   start............    " );
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
				Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
				List<Common> orderInfoList = (List<Common>) paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				sb.append(
					"订单号," + 
					"外部交易号," +
					"订单类型," + 
					"订单状态," +
					"付款状态," + 
					"发货状态," + 
					"下单人,"+
					"下单时间," + 
					"发货时间," +
					"订单店铺," + 
					"订单来源," +
					"商品数量," +
					"应付款金额," +
					"实收金额," +
					"总金额," +
					"优惠金额," +
					"邮费金额" + 
					"\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportInfo(orderInfoList, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
						getExportInfo((List<Common>) paging.getRoot(), writer);
					}
				}
				logger.info("OrderInfoController.orderInfoExportCsvFile    ExportTemplateType=0      默认模版   end............    " );
			}*/
			JsonResult jsonResult = new JsonResult();
			try {
				is = new FileInputStream(path);
				String ftpFileName = StringUtil.fileNameSpliceCsv("OrderInfo", "system");
				logger.info("OrderInfoController.orderInfoExportCsvFile   ftpFileName = " +ftpFileName );
				String tFtpPath = ftpRootPath + "/"+ TimeUtil.format2Date(new Date());
				logger.info("OrderInfoController.orderInfoExportCsvFile   tFtpPath = " +tFtpPath );
				HashMap<String, Object> soMap = FtpUtil.uploadFile(ftpFileName, is, tFtpPath +"/");
				logger.info("OrderInfoController.orderInfoExportCsvFile................   FtpUtil.uploadFile= " +  JSON.toJSONString(soMap) );
				if((Boolean)soMap.get("isok")) {
					Map<String,Object> map = new HashMap<String, Object>();
					String ftpPath = (String)soMap.get("path");
					map.put("path", ftpPath);
					map.put("fileName", ftpFileName);
					logger.info("OrderInfoController.orderInfoExportCsvFile................   导出成功 !! ............   path   = " + ftpPath  +"  fileName=  " +ftpFileName);
					jsonResult.setIsok(true);
					jsonResult.setData(map);
				} else {
					jsonResult.setIsok(false);
					jsonResult.setData("上传ftp失败");
					logger.info("OrderInfoController.orderInfoExportCsvFile................   导出失败 !! ............ ");
				}
				if (StringUtil.isArrayNotNull(fileRoot.listFiles())) {
					for (File temp : fileRoot.listFiles()) {
						if (temp.getName().equals(sfileName)) {
							logger.info("删除本地临时生成文件,路径：" + path);
							temp.delete();
							break;
						}
					}
				}
				writeObject(jsonResult, response);
			} catch (Exception e) {
				logger.error("订单信息导出上传至FTP异常", e);
				jsonResult.setIsok(false);
			} finally{
				if(is != null) {
					try {
						is.close();
					} catch(Exception e){
						logger.error("关闭流文件【InputStream】异常", e);
					}
				}
				if(writer != null){
					writer.close();
					writer = null;
				}
			}
		}catch (Exception e) {
			logger.error("订单信息导出异常", e);
		}
		logger.info("订单信息导出userId：" + "system" + " end");
	}
	
	
	/***
	 *默认订单导出模版 
	 ****/
	private void getExportInfo(List<Common> list, BufferedWriter writer) {
		if (!StringUtil.isListNotNull(list)) {
			return ;
		}
		try {
			StringBuffer buffer = new StringBuffer();
			for (Common orderInfo : list) {
				String masterOrderSn = orderInfo.getMasterOrderSn() == null ?"": "\'" +orderInfo.getMasterOrderSn();//主订单号
				String orderOutSn = orderInfo.getOrderOutSn();//外部交易号
//				String QuestionStatus = orderInfo.getQuestionStatus()== 0 ? "正常单" : "问题单"; //问题单状态;
			//	String timeoutStatus = orderInfo.getTimeoutStatus()== 0 ?"正常单" : "超时单";//超时单状态;
		//		String relatingExchangeSn = StringUtil.isNull(orderInfo.getRelatingExchangeSn()) ? "" :"\'" + orderInfo.getRelatingExchangeSn();  // '关联换货单原订单号',
				
//				String relatingOriginalSn = StringUtil.isNull(orderInfo.getRelatingOriginalSn()) ? "" : "\'" +orderInfo.getRelatingOriginalSn(); // '关联换货单原订单号',
				String orderTypeStr = StringUtil.isNull(orderInfo.getOrderTypeStr()) ? "" : orderInfo.getOrderTypeStr();
				String addTime = orderInfo.getAddTime() == null ? "":TimeUtil.formatDate(orderInfo.getAddTime()); //'订单生成时间',
				String deliveryTime = orderInfo.getDeliveryTime() == null ?"":TimeUtil.formatDate(orderInfo.getDeliveryTime()); //发货时间	

				String channelName = orderInfo.getChannelName() == null ? "":orderInfo.getChannelName(); //// 店铺名称
				String referer = orderInfo.getReferer() == null ? "":orderInfo.getReferer(); //'订单的来源
//				String userName = orderInfo.getUserName() == null ? "":orderInfo.getUserName(); // 下单人
				String userId = orderInfo.getUserId() == null ? "":orderInfo.getUserId(); // 下单人
				String goodsCount = (String) (orderInfo.getGoodsCount() == null ? "":orderInfo.getGoodsCount().toString()); //'订单商品总数',
				BigDecimal  totalPayable = orderInfo.getTotalPayable();//'应付款总金额 ',
				BigDecimal  moneyPaid = orderInfo.getMoneyPaid(); //'已付款金额',
				totalPayable =totalPayable==null ? new BigDecimal(0.00) :totalPayable;
				moneyPaid = moneyPaid==null ? new BigDecimal(0.00):moneyPaid ;
				BigDecimal  Surplus = orderInfo.getSurplus(); //'该订单使用余额的数量，取用户设定余额、用户可用余额、订单金额中最小者',
				Surplus =Surplus == null ? new BigDecimal(0.00) : Surplus ;
				BigDecimal totalFee = orderInfo.getTotalFee(); //订单总金额
				totalFee =totalFee == null ? new BigDecimal(0.00) : totalFee ;
				BigDecimal discount = orderInfo.getDiscount();
				BigDecimal  shippingTotalFee = orderInfo.getShippingTotalFee(); //'配送总费用',
				shippingTotalFee = shippingTotalFee == null ? new BigDecimal(0.00)  : shippingTotalFee;
//				String discountRate = calculateDiscount(orderInfo);
				BigDecimal dBonus  = orderInfo.getBonus();
//				BigDecimal bonus = dBonus == null ?  new BigDecimal(0.00) : dBonus;
				buffer.append(
						masterOrderSn+","+   //订单号
						orderOutSn+","+   //外部交易号
//						relatingOriginalSn + "," +   //换货单原订单号
						orderTypeStr + "," +  //订单类型
						orderInfo.getOrderStatusStr()+","+  //订单状态
						orderInfo.getPayStatusStr()+","+  //付款状态
						orderInfo.getShipStatusStr()+","+  //发货状态
						userId+","+   //下单人
						addTime+","+   //下单时间
						deliveryTime+","+   //发货时间
						channelName+","+  //订单来源
						referer+","+
						goodsCount+","+  //商品数量
						totalPayable+","+//应付款金额
						moneyPaid+","+ // 实收金额
						totalFee +","+ // 总金额
						discount +","+ // 优惠金额
//						bonus +","+
						shippingTotalFee + // 邮费金额,
//						postscript + // 客户留言
						"\r\n");
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
	}
	
	
	/***
	 *默认订单商品导出模版 
	 ****/
	private void getExportInfoGoods(List<Common> list, BufferedWriter writer) {
		if (!StringUtil.isListNotNull(list)) {
			return ;
		}
		try {
			StringBuffer buffer = new StringBuffer();
			for (Common orderInfo : list) {
				String masterOrderSn = orderInfo.getMasterOrderSn() == null ?"": "\'" +orderInfo.getMasterOrderSn();//主订单号
				String orderOutSn = orderInfo.getOrderOutSn();//外部交易号
				String orderTypeStr = StringUtil.isNull(orderInfo.getOrderTypeStr()) ? "" : orderInfo.getOrderTypeStr();
				String addTime = orderInfo.getAddTime() == null ? "":TimeUtil.formatDate(orderInfo.getAddTime()); //'订单生成时间',
				String deliveryTime = orderInfo.getDeliveryTime() == null ?"":TimeUtil.formatDate(orderInfo.getDeliveryTime()); //发货时间	

				String channelName = orderInfo.getChannelName() == null ? "":orderInfo.getChannelName(); //// 店铺名称
				String referer = orderInfo.getReferer() == null ? "":orderInfo.getReferer(); //'订单的来源
				String userId = orderInfo.getUserId() == null ? "":orderInfo.getUserId(); // 下单人
				String goodsCount = (String) (orderInfo.getGoodsCount() == null ? "":orderInfo.getGoodsCount().toString()); //'订单商品总数',
				BigDecimal  totalPayable = orderInfo.getTotalPayable();//'应付款总金额 ',
				BigDecimal  moneyPaid = orderInfo.getMoneyPaid(); //'已付款金额',
				totalPayable =totalPayable==null ? new BigDecimal(0.00) :totalPayable;
				moneyPaid = moneyPaid==null ? new BigDecimal(0.00):moneyPaid ;
				BigDecimal  Surplus = orderInfo.getSurplus(); //'该订单使用余额的数量，取用户设定余额、用户可用余额、订单金额中最小者',
				Surplus =Surplus == null ? new BigDecimal(0.00) : Surplus ;
				BigDecimal totalFee = orderInfo.getTotalFee(); //订单总金额
				totalFee =totalFee == null ? new BigDecimal(0.00) : totalFee ;
				BigDecimal discount = orderInfo.getDiscount();
				BigDecimal  shippingTotalFee = orderInfo.getShippingTotalFee(); //'配送总费用',
				shippingTotalFee = shippingTotalFee == null ? new BigDecimal(0.00)  : shippingTotalFee;
				String mobile = orderInfo.getMobile() == null ? "": orderInfo.getMobile(); //手机号码
				String consignee = orderInfo.getConsignee() == null ? "": orderInfo.getConsignee();
				String skuSn = orderInfo.getSkuSn() == null ? "": orderInfo.getSkuSn();
				Integer goodsNum = orderInfo.getGoodsNum();
				buffer.append(
						masterOrderSn+","+ //订单号
						orderOutSn+","+ //外部交易号
						channelName+","+ //订单来源
//						referer+","+
						consignee+","+ //收货人姓名
						mobile+","+ //手机号码,
						skuSn+","+ // skuSn
						goodsNum+ // 商品数量
						"\r\n");
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
	}
	
	/**
	 * 按财务模版导出 
	 * @param List<Common> list;
	 ***/
	private void getExportInfoByFinance(List<Common> list, BufferedWriter writer) {
			
		logger.info("OrderInfoController.getExportInfoByFinance.......   start ...........    " + JSON.toJSONString(list) );
		
		if (!StringUtil.isListNotNull(list)) {
			return ;
		}
		try {
			StringBuffer buffer = new StringBuffer();
			for (Common orderInfo : list) {
				
			//	logger.info("OrderInfoController.getExportInfoByFinance.......  common orderInfo : list   " + JSON.toJSONString(orderInfo));
				
		//		String orderSn = orderInfo.getOrderSn() == null ?"": "\'" +orderInfo.getOrderSn();//订单号
				String orderOutSn = StringUtil.isNull(orderInfo.getOrderOutSn()) ? "" : "\'" + orderInfo.getOrderOutSn(); //外部交易号,
				
				String masterOrderSn = orderInfo.getMasterOrderSn() == null ?"": "\'" +orderInfo.getMasterOrderSn();//主订单号
		
			//	String relatingExchangeSn = StringUtil.isNull(orderInfo.getRelatingExchangeSn()) ? "" : "\'" +orderInfo.getRelatingExchangeSn();  // 关联换货单原订单号,
				String relatingOriginalSn = StringUtil.isNull(orderInfo.getRelatingOriginalSn()) ? "" : orderInfo.getRelatingOriginalSn(); // 关联换货单原订单号,
				String orderTypeStr = StringUtil.isNull(orderInfo.getOrderTypeStr()) ? "" : orderInfo.getOrderTypeStr(); // 订单类型
				String deliveryTime =  orderInfo.getDeliveryTime() == null ?"":TimeUtil.formatDate(orderInfo.getDeliveryTime()); //发货时间
				String channelName = orderInfo.getChannelName() == null ? "":orderInfo.getChannelName(); // 店铺名称
				String goodsCount = (String) (orderInfo.getGoodsCount() == null ? "":orderInfo.getGoodsCount().toString()); //订单商品总数,

				BigDecimal  Surplus = orderInfo.getSurplus(); //该订单使用余额的数量，取用户设定余额、用户可用余额、订单金额中最小者,
				Surplus = Surplus == null ?  new BigDecimal(0.00) : Surplus ;

				BigDecimal  shippingTotalFee = orderInfo.getShippingTotalFee(); //'配送总费用',
				shippingTotalFee = shippingTotalFee == null ? new BigDecimal(0.00): shippingTotalFee ;
				String returnSn = orderInfo.getReturnSn() == null ? "": "\"" + orderInfo.getReturnSn() + "\"";  //// 订单关联所有退单列表
				String payName = orderInfo.getPayName() == null ? "": "\"" + orderInfo.getPayName()+ "\""; //支付方式
				String payNote = orderInfo.getPayNote() == null ? "" : "\"\'" +orderInfo.getPayNote()+"\""; //付款备注
				
		//		BigDecimal payTotalFee = orderInfo.getPayTotalFee() == null ? new BigDecimal(0.00) : orderInfo.getPayTotalFee();
				
				//total_payable
				
			//	BigDecimal totalPayable = orderInfo.getTotalPayable() == null ? new BigDecimal(0.00):orderInfo.getTotalPayable();
				 
				BigDecimal totalFee = orderInfo.getTotalFee() == null ? new BigDecimal(0.00) : orderInfo.getTotalFee();//订单总金额
				

				BigDecimal moneyPaid = orderInfo.getMoneyPaid()== null ? new BigDecimal(0.00) : orderInfo.getMoneyPaid();
				
				String payTime = orderInfo.getPayTime() == null ? "":TimeUtil.formatDate(orderInfo.getPayTime()); //支付时间,
				String clearTime = orderInfo.getClearTime() == null ? "":TimeUtil.formatDate(orderInfo.getClearTime()); //订单结算时间,

				String mergePaySn = orderInfo.getMergePaySn()== null ?"": "\'" +orderInfo.getMergePaySn();//合并付款单编号
				
				BigDecimal mergePayFee = orderInfo.getMergePayFee()== null ? new BigDecimal(0.00) : orderInfo.getMergePayFee();
				
				BigDecimal exchangeReturnMoney = new BigDecimal(0.00);
		
				if(null !=orderInfo.getPayId() &&  26==orderInfo.getPayId()){
					exchangeReturnMoney = orderInfo.getPayTotalfee();
					exchangeReturnMoney =exchangeReturnMoney == null ? new BigDecimal(0.00) : exchangeReturnMoney;
				}
	
				BigDecimal integralMoney = new BigDecimal(0.00);  //使用积分金额
				integralMoney =  orderInfo.getIntegralMoney();
				integralMoney =integralMoney == null ? new BigDecimal(0.00) : integralMoney;
				
				// 红包
				BigDecimal dBonus = orderInfo.getBonus();
				BigDecimal bonus = dBonus == null ? new BigDecimal(0.00) : dBonus;
				// 财务价格
				BigDecimal settlementPrice = orderInfo.getSettlementPrice();

				String sourceCode = orderInfo.getSourceCode() == null ?"":"\"" +  orderInfo.getSourceCode()+ "\"";//GPS来源
				
				String masterPaySn = orderInfo.getMasterPaySn() == null ?"":"\"" +  orderInfo.getMasterPaySn()+ "\"";//GPS来源
				
				String createTime = orderInfo.getCreateTime() == null ? "" : TimeUtil.formatDate(orderInfo.getCreateTime()); //付款单生成时间,
				
				buffer.append(
						masterOrderSn + ","+  //订单号1
						orderOutSn+","+   //外部交易号2
						relatingOriginalSn + "," +  //换货单原订单号, 3
						channelName + "," +  //订单来源,4
						orderTypeStr + "," +  //订单类型4
						orderInfo.getOrderStatusStr()+","+ //订单状态6
						orderInfo.getPayStatusStr()+","+  //付款状态7
						orderInfo.getShipStatusStr()+","+ //发货状态8
						deliveryTime+","+ //发货时间9
						goodsCount+","+  //订单商品数量10
						payName+","+  //支付方式11   order_pay.pay_name
						payNote+","+ //付款备注12    order_pay.pay_note`,
						moneyPaid +","+ //付款总金额13   order_pay.money_paid,
						payTime+","+//"支付时间"14    order_pay.pay_time,
						settlementPrice +","+  //财务价格15
						bonus +","+   //红包金额16
						shippingTotalFee +","+ //邮费金额17
						returnSn +","+ //关联退单号"18
						totalFee+","+  //订单总金额19   order_info.total_fee
						clearTime+","+ //订单结算时间20   order_info.clear_time
						Surplus+","+ //"余额支付21   order_info. surplus
						exchangeReturnMoney+","+ //换货时退货转入款22
						integralMoney+","+
						mergePaySn+","+
						mergePayFee+","+
						sourceCode+","+
						masterPaySn+","+
						createTime+
						"\r\n");
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
		
		logger.info("OrderInfoController.getExportInfoByFinance.......   end ...........    "  );
	}
	
	/**
	 *导出物流模板 
	 ***/
	private void getExportInfoByLogistics(List<Common> list, BufferedWriter writer) {
		if (!StringUtil.isListNotNull(list)) {
			return ;
		}
		try {
			StringBuffer buffer = new StringBuffer();
			for (Common orderInfo : list) {
	
	//			String orderSn = orderInfo.getOrderSn() == null ?"":   "\'" +orderInfo.getOrderSn();//订单号

				String masterOrderSn = orderInfo.getMasterOrderSn() == null ?"": "\'" +orderInfo.getMasterOrderSn();//主订单号
				
				String QuestionStatus = orderInfo.getQuestionStatus()== 0 ? "正常单" : "问题单"; //问题单状态;
			//	String timeoutStatus = orderInfo.getTimeoutStatus()== 0 ?"正常单" : "超时单";//超时单状态;
				String lockUserName = orderInfo.getLockUserName();
		//		String LockStatus = orderInfo.getLockStatus()== 0 ?"未锁定" : lockUserName;//超时单状态;//锁定状态;
				String orderOutSn = StringUtil.isNull(orderInfo.getOrderOutSn()) ? "" : "\'" + orderInfo.getOrderOutSn(); //'外部交易号',
		//		String relatingExchangeSn = StringUtil.isNull(orderInfo.getRelatingOriginalSn()) ? "" : orderInfo.getRelatingOriginalSn(); // '关联换货单原订单号',
		//		String orderTypeStr = StringUtil.isNull(orderInfo.getOrderTypeStr()) ? "" : orderInfo.getOrderTypeStr();
				String addTime = orderInfo.getAddTime() == null ? "":TimeUtil.formatDate(orderInfo.getAddTime()); //'订单生成时间',
		//		String shippingTime =  orderInfo.getShippingTime() == null ?"":TimeUtil.formatDate(orderInfo.getShippingTime()); //发货时间	
			//	String orderCategoryStr = orderInfo.getOrderCategoryStr() == null ? "":orderInfo.getOrderCategoryStr();
				String transTypeStr = orderInfo.getTransTypeStr() == null ? "":orderInfo.getTransTypeStr();
		//		String channelName = orderInfo.getChannelName() == null ? "":orderInfo.getChannelName(); //// 店铺名称
				String referer = orderInfo.getReferer() == null ? "":orderInfo.getReferer(); //'订单的来源
//				String userName = orderInfo.getUserName() == null ? "":orderInfo.getUserName(); // 下单人
		//		String depotStatus = getDepotStatus(orderInfo.getDepotStatus());// 分仓发货状态
		//		String goodsCount = (String) (orderInfo.getGoodsCount() == null ? "":orderInfo.getGoodsCount().toString()); //'订单商品总数',
				BigDecimal  totalPayable = orderInfo.getTotalPayable();//'应付款总金额 ',
				BigDecimal  moneyPaid = orderInfo.getMoneyPaid(); //'已付款金额',
				totalPayable =  totalPayable ==null ?  new BigDecimal(0.00)  :totalPayable;
				
				moneyPaid = moneyPaid==null ? new BigDecimal(0.00):moneyPaid ;
				
				BigDecimal  Surplus = orderInfo.getSurplus(); //'该订单使用余额的数量，取用户设定余额、用户可用余额、订单金额中最小者',
				Surplus = Surplus == null ? new BigDecimal(0.00) : Surplus ;
				BigDecimal totalFee = orderInfo.getTotalFee(); //订单总金额
				totalFee = totalFee == null ?  new BigDecimal(0.00) : totalFee ;

				BigDecimal  shippingTotalFee = orderInfo.getShippingTotalFee(); //'配送总费用',
				shippingTotalFee =shippingTotalFee == null ? new BigDecimal(0.00) : shippingTotalFee ;
		//		String postscript = orderInfo.getPostscript() == null ?"": "\"" +orderInfo.getPostscript()+ "\"";

		//		String  Discount  = calculateDiscount(orderInfo);
	//			Integer sIsAdvance = orderInfo.getIsAdvance();  //// 预售商品
				
				BigDecimal dBonus  = orderInfo.getBonus();
	//			BigDecimal bonus =  (BigDecimal) (dBonus == null ?  new BigDecimal(0.00) : dBonus);

		//		BigDecimal settlementPrice = orderInfo.getSettlementPrice();
				
		/*		String isAdvance ="";
				if(null == sIsAdvance ||  0 == sIsAdvance ){
					isAdvance = "否";
				} else{
					isAdvance = "是";
				}*/
		
		//		String returnSn = orderInfo.getReturnSn() == null ? "": "\"" + orderInfo.getReturnSn() + "\"";  //// 订单关联所有退单列表
		
				/***物流***/
				String shipCode = orderInfo.getShipCode() == null ?"":orderInfo.getShipCode(); // '承运商',
				
		//		String shippingName = orderInfo.getShippingName() == null ?"":orderInfo.getShippingName(); // '承运商',
				
				String distWarehCode = orderInfo.getDistWarehCode() == null ?"":orderInfo.getDistWarehCode();  // 仓库code',
				
				
				String invoiceNo = orderInfo.getInvoiceNo() == null ? "" :  "\'" +orderInfo.getInvoiceNo();//快递号
				
				String orderFrom = orderInfo.getOrderFrom() == null ? "" :  orderInfo.getOrderFrom();//店铺编码
			
				String confirmTime =  orderInfo.getConfirmTime() == null ?"":TimeUtil.formatDate(orderInfo.getConfirmTime()); //发货确定时间	
		
				//问题单原因
		//		String questionReason = orderInfo.getQuestionReason()== null ? "" : orderInfo.getQuestionReason();
				
				String province = orderInfo.getProvince()== null ?"":orderInfo.getProvince();//收货省
				
				String   provinceName  =  getSystemRegionAreaName(province);
		
				String city = orderInfo.getCity()== null ? "" : orderInfo.getCity();//收货城市
		
				String   cityName  =  getSystemRegionAreaName(city);

				String district = orderInfo.getDistrict()== null ? "" : orderInfo.getDistrict();//收货区
				String   districtName  =  getSystemRegionAreaName(district);

				buffer.append(
						masterOrderSn+","+   //订单号
						distWarehCode+","+
						shipCode+","+
						invoiceNo+","+//快递号
						
						orderFrom+","+  //店铺编码
						
						referer+","+ //referer
						orderInfo.getShipStatusStr()+","+  //发货状态	
						orderInfo.getShippingStatusStr()+","+  //发货状态	
						addTime+","+   //下单时间
						confirmTime+","+   //确定时间
						orderOutSn+","+   //交易号,
						transTypeStr+","+   //交易类型
						QuestionStatus+","+  //是否问题单
		
					//	questionReason+","+
						provinceName+","+
						cityName+","+
						districtName+","+
				
						"\r\n");
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
	}
	
	/***
	 * 调用省,市,区
	 ****/
	private String getSystemRegionAreaName(String regionId){
		
		if(StringUtil.isBlank(regionId)){
			return "";
		}
		SystemRegionArea  systemRegionArea = null;
		try{
			systemRegionArea = systemRegionAreaMapper.selectByPrimaryKey(regionId);
			
			if(StringUtil.isNotBlank(systemRegionArea.getRegionName())){
				return systemRegionArea.getRegionName();
			}
		} catch(Exception e){
			e.printStackTrace();
			return "";
			
		}

		return "";
	}
	
	/**
	 * 计算折扣 
	 **/
	private String calculateDiscount(Common orderInfo){
	
		BigDecimal shippingTotalFee  =  (BigDecimal) (orderInfo.getShippingFee()==null? new BigDecimal(0.00)   : orderInfo.getShippingFee());	
		BigDecimal totalFee  =  (BigDecimal) (orderInfo.getTotalFee()==null? new BigDecimal(0.00)   : orderInfo.getTotalFee());	
		BigDecimal surplus  =  (BigDecimal) (orderInfo.getSurplus()==null? new BigDecimal(0.00)   : orderInfo.getSurplus());	
		BigDecimal moneyPaid  =  (BigDecimal) (orderInfo.getMoneyPaid()==null? new BigDecimal(0.00)   : orderInfo.getMoneyPaid());
		if(totalFee.doubleValue() - shippingTotalFee.doubleValue() <= 0) {
			return "";
		} else {
			double a = moneyPaid.doubleValue() + surplus.doubleValue()- shippingTotalFee.doubleValue();
			double b = totalFee.doubleValue() - shippingTotalFee.doubleValue();
			double re = (a/b)*100;
			re =re*100/100;
			String sRe = String.valueOf(re);
			if(sRe.length()>=5){
				sRe = sRe.substring(0, 5);
			}
			return sRe + "%";
		}
	}
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	/**
	 *根据状态取分仓名称 
	 ****/
	private String getDepotStatus(Byte depotStatus) {
		if (null == depotStatus) {
			return "";
		}
		String depotStatusName = "";
		switch (depotStatus) {
		case 0:
			depotStatusName = "未分仓";
			break;
		case 1:
			depotStatusName = "已分仓未通知";
			break;
		case 2:
			depotStatusName = "已分仓已通知";
			break;
		default:
			break;
		}
		
		return depotStatusName;
		
	}
	
	/**
	 * 导入订单号和问题单号
	 * 
	 **/
	@RequestMapping(value = "inputIsDerivedByOrderSnOrOrderOutSn.spmvc", headers = "content-type=multipart/*")
	public ModelAndView inputIsDerivedByOrderSnOrOrderOutSn(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam MultipartFile myfile, int inputType)
			throws Exception {

		InputStream inputis = myfile.getInputStream();
		StringBuffer sb = new StringBuffer("");
		
	//	AdminUser adminUser = getLoginUser(request);

		Common common = null;
		
		try {
			//将流转化为list<OrderQuestion>对象
			 common = orderInfoService.InputIsDerivedOrderListByOrderSnOrOrderOutSn(inputis,  sb,  inputType);
	
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导入异常", e);
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
		} finally {
			if (inputis != null) {
				try {
					inputis.close();
					inputis = null;
				} catch (IOException e) {
					logger.error("导入流关闭异常", e);
					e.printStackTrace();
				}
			}
		}
		Map<String,Object> data = new HashMap<String, Object>();

		Paging paging = null;
		try{
		    PageHelper helper = new PageHelper();
			helper.setStart(0);
			helper.setLimit(1000);
			
			paging = this.orderInfoService.getOrderInfoPage(common, helper);
	
			String jsonObject = JSONObject.toJSONString(paging);
			
			writeMsgSucc(true, URLEncoder.encode(jsonObject, "utf-8"), response);
		
		}catch(Exception e){
			logger.error("输入交易号查询异常！", e);
	
			writeMsgSucc(false, "导入失败",  response);
			return null;
			
		}

		return null;
	}
	
	/**
	 * 获取地区树数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getArea")
	public ModelAndView getArea(HttpServletRequest request,
			HttpServletResponse response,Integer type, String regionId) throws Exception {
		logger.debug("查询地区列表 type:" + type + ";regionId:" + regionId);
		try {
			if(0==type){
				List<SystemRegionArea> SystemRegionCountryList = systemRegionAreaService.getSystemRegionListByType(0);
				writeObject(SystemRegionCountryList, response);
				return null;
			}
			List<SystemRegionArea> SystemRegionCountryList = systemRegionAreaService.getSystemRegionListByTypeAndPid(type, regionId);
			SystemRegionArea systemRegion = new SystemRegionArea();
			systemRegion.setRegionId(null);
			systemRegion.setRegionName("请选择...");
			SystemRegionCountryList.add(0, systemRegion);
			writeObject(SystemRegionCountryList, response);
		} catch (Exception e) {
			logger.error("查询地区列表异常 type:" + type + ";regionId:" + regionId, e);
		}
		return null;
	}
	
	@Resource
	private SystemRegionAreaService systemRegionAreaService;
	
	
	/**
	 * 待结算订单;
	 * ***/
	@RequestMapping(value = "orderInfoOrdersettleExportCsvFile.spmvc")
	public void orderInfoOrdersettleExportCsvFile(HttpServletRequest request,
			HttpServletResponse response,  Common orderInfoSerach, PageHelper helper)throws Exception{
		
		InputStream is = null;
		BufferedWriter writer = null;
		AdminUser adminUser = getLoginUser(request);
		logger.info("订单信息导出userId：" + adminUser.getUserName() + " start");
		int pageSize = 5000;
		StringBuffer sb = new StringBuffer();
		Pagination pagination = new Pagination(1, pageSize);
		helper.setLimit(pageSize);
		
		orderInfoSerach.setIsNotQuanQiuTong(true);
		
		try{
			// 创建本地文件
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "oiTempFile";
			String sfileName ="orderInfoList" + dateStr+".csv";
			String fileName = folderName + "/"+sfileName;
			File fileRoot=new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			if(!fileRoot.exists()){
				fileRoot.mkdirs();
			}
			String path = request.getSession().getServletContext().getRealPath("/") + fileName;

			 //财务
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
			Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			List<Common> orderInfoList = (List<Common>) paging.getRoot();
			pagination.setTotalSize(paging.getTotalProperty());
			sb.append(
					
					"主订单号," +
					 "交易号," +
					  "换货单原订单号," + 
					  "订单来源," +
					"订单类型," +
					 "订单状态," +
					  "付款状态," + 
					  "发货状态," +
					"发货时间," +
					 "订单商品数量," +
					  "支付方式,"+  
					  "付款备注," +
					"付款总金额, "+ 
					"支付时间,"+
					 "财务价格," + 
					 "红包金额," +
					"邮费金额," + 
					"关联退单号," + 
					"订单总金额," + 
					"订单结算时间," +
					"余额支付,"+ 
					"换货时退货转入款,"+
					"使用积分金额,"+
					"合并付款单编号,"+
					"合成付款总金额,"+
					"GPS来源,"+
					"付款单编号"+
					 "\r\n"
			
			);
			writer.write(sb.toString());
			writer.flush();
			if (paging.getTotalProperty() > 0) {
				//第一页
				getExportInfoByFinance(orderInfoList, writer);
				//第二页及后续页
				for (int j = 2; j <= pagination.getTotalPages(); j++) {
					pagination.setCurrentPage(j);
					helper.setStart(pagination.getStartRow());
					paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
					getExportInfoByFinance((List<Common>) paging.getRoot(), writer);
				}
			}
		
				
		//	} 
			JsonResult jsonResult = new JsonResult();
			try {
				is = new FileInputStream(path);
				String ftpFileName = StringUtil.fileNameSpliceCsv("OrderInfo", adminUser.getUserName());
				String tFtpPath = ftpRootPath + "/"+ TimeUtil.format2Date(new Date());
				HashMap<String, Object> soMap = FtpUtil.uploadFile(ftpFileName, is, tFtpPath +"/");
				if((Boolean)soMap.get("isok")) {
					Map<String,Object> map = new HashMap<String, Object>();
					String ftpPath = (String)soMap.get("path");
					map.put("path", ftpPath);
					map.put("fileName", ftpFileName);
					jsonResult.setIsok(true);
					jsonResult.setData(map);
				} else {
					jsonResult.setIsok(false);
					jsonResult.setData("上传ftp失败");
				}
				if (StringUtil.isArrayNotNull(fileRoot.listFiles())) {
					for (File temp : fileRoot.listFiles()) {
						if (temp.getName().equals(sfileName)) {
							logger.info("删除本地临时生成文件,路径：" + path);
							temp.delete();
							break;
						}
					}
				}
				writeObject(jsonResult, response);
			} catch (Exception e) {
				logger.error("订单信息导出上传至FTP异常", e);
				jsonResult.setIsok(false);
			} finally{
				if(is != null) {
					try {
						is.close();
					} catch(Exception e){
						logger.error("关闭流文件【InputStream】异常", e);
					}
				}
				if(writer != null){
					writer.close();
					writer = null;
				}
			}
		}catch (Exception e) {
			logger.error("订单信息导出异常", e);
		}
		
	}
	
	/**
	 * 初始化订单详情页发送短信的页面数据
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param channelCode
	 * @return
	 */

	@RequestMapping(value = "initSendMessage")
	@ResponseBody
	public Map initSendMessage(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn,String channelCode){
		return orderInfoService.initSendMessage(masterOrderSn, channelCode);
	}
	
	/**
	 * 订单详情页发送短信
	 * @param request
	 * @param response
	 * @param encodedMobile
	 * @param shopCode
	 * @param masterOrderSn
	 * @param channelCode
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "doSendMessage")
	@ResponseBody
	public Map doSendMessage(HttpServletRequest request,
			HttpServletResponse response,String encodedMobile,String shopCode,String masterOrderSn,String channelCode,String message){
		return orderInfoService.doSendMessage(getLoginUser(request), encodedMobile, shopCode, masterOrderSn, channelCode, message);
	}
	
	/**
	 * 工具页发送短信
	 * @param request
	 * @param response
	 * @param mobile
	 * @param sendType
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "toolsDoSendMessage")
	@ResponseBody
	public Map toolsDoSendMessage(HttpServletRequest request,
			HttpServletResponse response,String mobile,String sendType,String message){
		return orderInfoService.toolsDoSendMessage(getLoginUser(request),mobile, sendType, message);
	}
		
}
