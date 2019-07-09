package com.work.shop.oms.express.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressCompanyService;
import com.work.shop.oms.api.express.service.OrderExpressPullService;
import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.ErpStatusInfo;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderExpressCompany;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.CommonUtil;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.express.bean.ErpStatusInfoRO;
import com.work.shop.oms.express.bean.ExpressContent;
import com.work.shop.oms.express.bean.ExpressInfo;
import com.work.shop.oms.express.bean.ExpressInfoBean;
import com.work.shop.oms.express.bean.ErpStatusInfoRO.ErpStatus;
import com.work.shop.oms.express.utils.HttpClientUtil;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

@Controller
public class QueryExpressAction {

	private static Logger logger = Logger.getLogger(QueryExpressAction.class);

	@Autowired
	private OrderExpressTracingService orderExpressTracingService;
	
	@Resource 
	private OrderExpressCompanyService orderExpressCompanyService;
	
	@Resource(name = "orderExpressPullServiceImpl")
	private OrderExpressPullService orderExpressPullService;
	@Resource
	private SystemShippingMapper systemShippingMapper;
	
	@RequestMapping("/host")
	public @ResponseBody
	String queryHost(ModelMap model) {
		return CommonUtil.getLocalIP();
	}
	
	/**
	 * 退单物流查询
	 * @param orderReturnSn
	 * 			退单SN
	 * @param expressCode
	 * 			物流公司号
	 * @param trackno
	 * 			物流号
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/orderRturnExpressByReturnSn")
	@ResponseBody
	public String orderRturnQuery(
			@RequestParam(value = "orderReturnSn", required = false) String orderReturnSn,
			@RequestParam(value = "expressCode", required = false) String expressCode,// 物流公司号
			@RequestParam(value = "trackno", required = false) String trackno,// 物流号
			ModelMap model) throws Exception {
		String json = "";
		// 直接查快递信息，不查询舱内信息
		List<OrderExpressTracing> list = new ArrayList<OrderExpressTracing>();
		if (StringUtils.isEmpty(expressCode) && StringUtils.isEmpty(trackno)) {
			list = orderExpressTracingService.getTracingListBySn(orderReturnSn,null);
		} else {
			OrderExpressCompany company = orderExpressCompanyService.selectCompanyByCode(expressCode);
			if (company == null) {
				throw new RuntimeException("无法获取有效的承运商信息！code:" + expressCode);
			}
			list = orderExpressTracingService.getTracingListByExpress(expressCode, trackno,null);
		}
		if (CollectionUtils.isNotEmpty(list)) {
			// ro = erpStatusInfo(list.get(0).getOrderSn());
			logger.debug("[QueryExpressAction.orderQuery.list].orderReturnSn:"
					+ orderReturnSn + ",list:" + JSON.toJSONString(list));
			json = JSON.toJSONString(list.get(0).getNote());
		}else{
			ExpressInfo info=new ExpressInfo();
			info.setMessage("no");
			info.setStatus(0);
			json=JSON.toJSONString(info);
			
		}
		return json;
	}

	/**
	 * @param expressCode
	 * 			物流公司号
	 * @param trackno
	 * 			物流号
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/orderRturnExpress")
	@ResponseBody
	public String orderRturnQueryByTrackNo(
			@RequestParam(value = "expressCode", required = false) String expressCode,// 物流公司号
			@RequestParam(value = "trackno", required = false) String trackno,// 物流号
			ModelMap model) throws Exception {
		String json = "";
		List<OrderExpressTracing> list = new ArrayList<OrderExpressTracing>();
		OrderExpressCompany company = orderExpressCompanyService.selectCompanyByCode(expressCode);
		if (company == null) {
			throw new RuntimeException("无法获取有效的承运商信息！code:" + expressCode);
		}
		list = orderExpressTracingService.getTracingListByExpress(expressCode, trackno,null);
		if (CollectionUtils.isNotEmpty(list)) {
			// ro = erpStatusInfo(list.get(0).getOrderSn());
			logger.debug("[QueryExpressAction.orderQuery.list].trackno:"
					+ trackno + ",list:" + JSON.toJSONString(list));
//			ExpressInfoBean bean = new ExpressInfoBean();
//			List<ExpressInfo> infoList = getExpressInfo(list, bean);
//			bean.setExpress(infoList);
			if(null!=list.get(0).getNote()){
				json = JSON.toJSONString(list.get(0).getNote());
			}else{
				ExpressInfo info=new ExpressInfo();
				info.setMessage("no");
				info.setStatus(0);
				json=JSON.toJSONString(info);
				
			}
		}
		return json;
	}

//	@RequestMapping("/expressOMS")
	@RequestMapping(value="/expressOMS", produces="text/plain;charset=utf-8")
	public @ResponseBody
	String orderQueryOMS(
			@RequestParam(value = "force", required = false) Integer force,
			@RequestParam(value = "orderSn", required = true) String orderSn,
			@RequestParam(value = "erpOnly", required = false) Integer erp,
			@RequestParam(value = "expressCode", required = false) String expressCode,//物流公司号
			@RequestParam(value = "depotCode", required = true) String depotCode,//仓库号
			@RequestParam(value = "trackno", required = false) String trackno,//物流号
			ModelMap model) throws ParseException {
		String json = "";
		try {
			logger.debug("[QueryExpressAction.orderQuery].express.begin----orderSn:"+orderSn+",force:"+force+",erpOnly:"+erp+",expressCode:"+expressCode+",trackno:"+trackno);
			
			// 通过订单号查询，获取快递信息，仓内信息
			ExpressInfoBean bean = new ExpressInfoBean();
			// 直接查快递信息，不查询舱内信息
			List<OrderExpressTracing> list = new ArrayList<OrderExpressTracing>();
			
			List<ExpressInfo> infoList = new ArrayList<ExpressInfo>();
			if (orderSn.trim().length() > 5 || (StringUtils.isNotEmpty(expressCode) && StringUtils.isNotEmpty(trackno))) {
				// 1仓库内作业信息
				ErpStatusInfoRO ro = new ErpStatusInfoRO();
				if(StringUtils.isEmpty(expressCode) && StringUtils.isEmpty(trackno) && StringUtils.isNotEmpty(depotCode)){
					ro = erpStatusInfoOMS(orderSn,depotCode);
					logger.debug("[QueryExpressAction.orderQuery].orderSn:"+orderSn+",ErpStatusInfoRO:"+JSON.toJSONString(ro));
					if (erp != null && erp > 0) {
						return JSON.toJSONString(ro);
					}
				}
				// 2仓库内作业信息,优先从tracing表中查询,如订单查询不到，在查询order_ship表，再无则返回快递信息为空
				if(StringUtils.isEmpty(expressCode) && StringUtils.isEmpty(trackno)){
					list = orderExpressTracingService.getTracingListBySn(orderSn,depotCode);					
				}else{
					OrderExpressCompany company = orderExpressCompanyService.selectCompanyByCode(expressCode);
					if(company == null){
						throw new RuntimeException("无法获取有效的承运商信息！code:" + expressCode);
					}
					list = orderExpressTracingService.getTracingListByExpress(expressCode, trackno,depotCode);
					if(CollectionUtils.isNotEmpty(list)){
						ro = erpStatusInfoOMS(list.get(0).getOrderSn(),list.get(0).getDepotCode());
					}
				}
				logger.debug("[QueryExpressAction.orderQuery.list].orderSn:"+orderSn+",list:"+JSON.toJSONString(list));
				if (list != null && list.size() > 0) {
					for (OrderExpressTracing oet : list) {
						try {
							if (force != null) {
								if (oet.getCompanyCode() != null
										&& oet.getTrackno() != null) {
									infoList.add(getMessage(bean, orderSn,
											oet.getCompanyCode(),
											oet.getTrackno()));
								}
							} else {
								if (oet.getNote() != null
										&& oet.getNote().trim().length() > 10) {
									if (oet.getNote().trim().charAt(0) != '{') {
										infoList.add(getMessage(bean, orderSn,
												oet.getCompanyCode(),
												oet.getTrackno()));
									} else {
										
										bean.setFrom("DB");
										ExpressInfo in = JSON.parseObject(
												oet.getNote(),
												ExpressInfo.class);
											List<ExpressContent> data = in.getData();
											if(data.get(0).getTime().compareTo(data.get(data.size()-1).getTime()) < 0){
												List<ExpressContent> temp = new ArrayList<ExpressContent>();
												for(int i=data.size();i>0;i--){
													temp.add(data.get(i-1));
												}
												in.setData(temp);
											}
											infoList.add(in);
									}
								} else {
									if (oet.getCompanyCode() != null
											&& oet.getTrackno() != null) {
										infoList.add(getMessage(bean, orderSn, 
												oet.getCompanyCode(),
												oet.getTrackno()));
									}
								}
							}
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
					}
				} else {
					ReturnInfo<List<OrderDepotShip>> info = orderExpressPullService.selectEffectiveShip(orderSn);
					if (info.getIsOk() == Constant.OS_YES && StringUtil.isListNotNull(info.getData())) {
						List<OrderDepotShip> orderShipList = info.getData();
						for (OrderDepotShip depotShip : orderShipList) {
							if (depotShip.getInvoiceNo() != null && depotShip.getInvoiceNo().length() > 5) {
								SystemShipping shipping = systemShippingMapper.selectByPrimaryKey(depotShip.getShippingId());
								infoList.add(getMessage(bean, orderSn, shipping.getShippingCode(), depotShip.getInvoiceNo()));
							}
						}
					}
				}
				bean.setErp(ro);
				bean.setExpress(infoList);
				json = JSON.toJSONString(bean);
			}
			json = StringUtils.isEmpty(json) ? "[]" : json;
			logger.info(json);
		} catch (Exception e) {
			logger.error(e.getCause() + "---" + e.getLocalizedMessage() + " orderSn = " + orderSn);
		}
		logger.debug("[QueryExpressAction.orderQuery].express.end----orderSn:"+orderSn+",force:"+force+",erpOnly:"+erp+",expressCode:"+expressCode+",trackno:"+trackno);
		return json;
	}

//	@RequestMapping("/express")
	@RequestMapping(value="/express", produces="text/plain;charset=utf-8")
	public @ResponseBody
	String orderQuery(
			@RequestParam(value = "force", required = false) Integer force,
			@RequestParam(value = "orderSn", required = true) String orderSn,
			@RequestParam(value = "erpOnly", required = false) Integer erp,
			@RequestParam(value = "expressCode", required = false) String expressCode,//物流公司号
			@RequestParam(value = "trackno", required = false) String trackno,//物流号
			ModelMap model) throws ParseException {
		String json = "";
		try {
			logger.debug("[QueryExpressAction.orderQuery].express.begin----orderSn:"+orderSn+",force:"+force+",erpOnly:"+erp+",expressCode:"+expressCode+",trackno:"+trackno);
			
			// 通过订单号查询，获取快递信息，仓内信息
			ExpressInfoBean bean = new ExpressInfoBean();
			// 直接查快递信息，不查询舱内信息
			List<OrderExpressTracing> list = new ArrayList<OrderExpressTracing>();
			
			List<ExpressInfo> infoList = new ArrayList<ExpressInfo>();
			if (orderSn.trim().length() > 5 || (StringUtils.isNotEmpty(expressCode) && StringUtils.isNotEmpty(trackno))) {
				// 1仓库内作业信息
				ErpStatusInfoRO ro = new ErpStatusInfoRO();
				if(StringUtils.isEmpty(expressCode) && StringUtils.isEmpty(trackno) ){
					ro = erpStatusInfo(orderSn);
					logger.debug("[QueryExpressAction.orderQuery].orderSn:"+orderSn+",ErpStatusInfoRO:"+JSON.toJSONString(ro));
					if (erp != null && erp > 0) {
						return JSON.toJSONString(ro);
					}
				}
				// 2仓库内作业信息,优先从tracing表中查询,如订单查询不到，在查询order_ship表，再无则返回快递信息为空
				if(StringUtils.isEmpty(expressCode) && StringUtils.isEmpty(trackno)){
					list = orderExpressTracingService.getTracingListBySn(orderSn,null);
				}else{
					OrderExpressCompany company = orderExpressCompanyService.selectCompanyByCode(expressCode);
					if(company == null){
						throw new RuntimeException("无法获取有效的承运商信息！code:"+expressCode);
					}
					list = orderExpressTracingService.getTracingListByExpress(expressCode, trackno,null);
					if(CollectionUtils.isNotEmpty(list)){
						ro = erpStatusInfo(list.get(0).getOrderSn());
					}
				}
				logger.debug("[QueryExpressAction.orderQuery.list].orderSn:"+orderSn+",list:"+JSON.toJSONString(list));
				if (list != null && list.size() > 0) {
					for (OrderExpressTracing oet : list) {
						try {
							if (force != null) {
								if (oet.getCompanyCode() != null && oet.getTrackno() != null) {
									infoList.add(getMessage(bean, orderSn, oet.getCompanyCode(), oet.getTrackno()));
								}
							} else {
								if (oet.getNote() != null && oet.getNote().trim().length() > 10) {
									if (oet.getNote().trim().charAt(0) != '{') {
										infoList.add(getMessage(bean, orderSn, oet.getCompanyCode(), oet.getTrackno()));
									} else {
										bean.setFrom("DB");
										String note="";
										ExpressInfo in = JSON.parseObject(oet.getNote(),ExpressInfo.class);
										List<ExpressContent> data = in.getData();
										if(data.get(0).getTime().compareTo(data.get(data.size()-1).getTime()) < 0){
											List<ExpressContent> temp = new ArrayList<ExpressContent>();
											for(int i=data.size();i>0;i--){
												temp.add(data.get(i-1));
											}
											in.setData(temp);
										}
										infoList.add(in);
									}
								} else {
									if (oet.getCompanyCode() != null && oet.getTrackno() != null) {
										infoList.add(getMessage(bean, orderSn, oet.getCompanyCode(), oet.getTrackno()));
									}
								}
							}
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
					}
				} else {
					ReturnInfo<List<OrderDepotShip>> info = orderExpressPullService.selectEffectiveShip(orderSn);
					if (info.getIsOk() == Constant.OS_YES && StringUtil.isListNotNull(info.getData())) {
						List<OrderDepotShip> orderShipList = info.getData();
						for (OrderDepotShip depotShip : orderShipList) {
							if (depotShip.getInvoiceNo() != null && depotShip.getInvoiceNo().length() > 5) {
								SystemShipping shipping = systemShippingMapper.selectByPrimaryKey(depotShip.getShippingId());
								infoList.add(getMessage(bean, orderSn, shipping.getShippingCode(), depotShip.getInvoiceNo()));
							}
						}
					}
				}
				bean.setErp(ro);
				bean.setExpress(infoList);
				json = JSON.toJSONString(bean);
			}
		} catch (Exception e) {
			logger.error(e.getCause() + "---" + e.getLocalizedMessage() + " orderSn = " + orderSn);
		}
		logger.debug("[QueryExpressAction.orderQuery].express.end----orderSn:"+orderSn+",force:"+force+",erpOnly:"+erp+",expressCode:"+expressCode+",trackno:"+trackno);
		return StringUtils.isEmpty(json) ? "[]" : json;
	}
	
	public ExpressInfo getMessage(ExpressInfoBean bean, String orderSn, String companyCode, String trackNo) {
		ExpressInfo express = null;
		bean.setFrom("100_API");
		express = HttpClientUtil.getKuaidi100NEW(trackNo, companyCode, null, null);
		if(express==null){
			express = new ExpressInfo();
		}
		logger.info("trackNo =" + trackNo + " :" + JSON.toJSONString(express));
		if (express == null || StringUtil.isListNull(express.getData())) {
			return express;
		}
		List<OrderExpressTracing> expressTracings = orderExpressTracingService.selectExpressByOrderSnAndtrackno(orderSn, trackNo);
		if (StringUtil.isListNull(expressTracings)) {
			return express;
		}
		OrderExpressTracing oet = expressTracings.get(0);
		try {
			OrderExpressTracing newTracing = newOrderExpressTracing(oet, express, companyCode);
			int count=orderExpressTracingService.updateTracing(newTracing);
			logger.info("快递100 Udate Count"+"*******"+count+"=====");
		} catch (Exception e) {
			logger.error("订单[" + orderSn + "] trackNo" + trackNo + "更新快点信息异常：" + e.getMessage(), e);
		}
		return express;
	}
	
	public OrderExpressTracing newOrderExpressTracing(OrderExpressTracing old,
			ExpressInfo info, String expressName) throws ParseException {
		if (info == null || info.getData() == null || info.getData().size() < 1) {
			return old;
		}
		old.setNote(JSON.toJSONString(info));
/*		String city_s = "null";
		if (city != null && city.length() > 0) {
			city_s = city.replace("市", "");
		}
		String dist_s = "null";
		if (dist != null && dist.length() > 0) {
			dist_s = dist.replaceFirst("区|县", "");
		}*/
		List<com.work.shop.oms.express.bean.ExpressContent> list = info.getData();
//		int site = 0;
		// 10，快递取件；11，运输中；11.1，运输中(上干线)；11.2，运输中(到达目的地)；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
		float expressStatus = 0.0f;
		try {
			expressStatus = info.getState();
			if (expressStatus == Constant.express_status_sign
					|| expressStatus == Constant.express_status_returnSign) {
				old.setExpressTimeSign(TimeUtil.parseString2Date(list.get(0).getTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		old.setExpressStatus(expressStatus);
		old.setUpdTime(new Date());
//		old.setFetchFlag(new Byte("1"));
		return old;
	}
	
	
	public static Date setDateStr(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = date.replaceAll("/", "-");
		return sdf.parse(repalceSpace(date.getBytes()));
	}
	
	public static String repalceSpace(byte[] b) {
		byte[] n = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			if (b[i] > 0) {
				n[i] = b[i];
			} else {
				n[i] = 32;
			}
		}
		String newstr = new String(n);
		Pattern p = Pattern.compile(" {2,} ");
		Matcher m = p.matcher(newstr);

		return m.replaceAll(" ");
	}

	/**
	 * 仓库内作业信息
	 * @param orderSn
	 * @return
	 */
	public ErpStatusInfoRO erpStatusInfo(String orderSn) {
		ErpStatusInfoRO ro = null;

		if (StringUtils.isBlank(orderSn)) {
			ro = new ErpStatusInfoRO();
			ro.setCode(ErpStatusInfoRO.CODE_ERR_PARAMS);
		} else {
			orderSn = orderSn.trim();
			try {
				ro = new ErpStatusInfoRO();
				ro.setOrderSn(orderSn);
				List<ErpStatusInfo> erpStatusInfoList = orderExpressTracingService
						.queryErpStatusInfo(orderSn);//////=============================
				if (erpStatusInfoList != null && erpStatusInfoList.size() > 0) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					int i = 0;
					List<ErpStatus> erpStatusList = new ArrayList<ErpStatus>();
					for (ErpStatusInfo erpStatusInfo : erpStatusInfoList) {
						ErpStatus status = ro.new ErpStatus();
						status.setIndex(i);
						if (erpStatusInfo.getAddTime() != null) {
							status.setTime(format.format(erpStatusInfo
									.getAddTime()));
						}

//						status.setStatusInfo(erpStatusInfo.convertStatus());
//						status.setDocType(erpStatusInfo.convertDocTypeStatus());
						status.setDepotCode(erpStatusInfo.getDepotCode());
						erpStatusList.add(status);
						i++;
					}
					ro.setErpStatusList(erpStatusList);
					ro.setCode(ErpStatusInfoRO.CODE_SUCCESS);
				} else {
					ro.setCode(ErpStatusInfoRO.CODE_EMPTY_RESULT);
				}
			} catch (Throwable t) {
				logger.error(t);
				ro.setCode(ErpStatusInfoRO.CODE_ERR_SERVER);
			}
		}

		return ro;
	}
	/**
	 * 仓库内作业信息
	 * @param orderSn
	 * @param depotCode
	 * @return
	 */
	public ErpStatusInfoRO erpStatusInfoOMS(String orderSn,String depotCode) {
		ErpStatusInfoRO ro = null;

		if (StringUtils.isBlank(orderSn)) {
			ro = new ErpStatusInfoRO();
			ro.setCode(ErpStatusInfoRO.CODE_ERR_PARAMS);
		} else {
			orderSn = orderSn.trim();
			try {
				ro = new ErpStatusInfoRO();
				ro.setOrderSn(orderSn);
				List<ErpStatusInfo> erpStatusInfoList = orderExpressTracingService
						.queryErpStatusInfoOMS(orderSn,depotCode);//////=============================
				if (erpStatusInfoList != null && erpStatusInfoList.size() > 0) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					int i = 0;
					List<ErpStatus> erpStatusList = new ArrayList<ErpStatus>();
					for (ErpStatusInfo erpStatusInfo : erpStatusInfoList) {
						ErpStatus status = ro.new ErpStatus();
						status.setIndex(i);
						if (erpStatusInfo.getAddTime() != null) {
							status.setTime(format.format(erpStatusInfo
									.getAddTime()));
						}

//						status.setStatusInfo(erpStatusInfo.convertStatus());
//						status.setDocType(erpStatusInfo.convertDocTypeStatus());
						status.setDepotCode(erpStatusInfo.getDepotCode());
						erpStatusList.add(status);
						i++;
					}
					ro.setErpStatusList(erpStatusList);
					ro.setCode(ErpStatusInfoRO.CODE_SUCCESS);
				} else {
					ro.setCode(ErpStatusInfoRO.CODE_EMPTY_RESULT);
				}
			} catch (Throwable t) {
				logger.error(t);
				ro.setCode(ErpStatusInfoRO.CODE_ERR_SERVER);
			}
		}
		return ro;
	}
}
