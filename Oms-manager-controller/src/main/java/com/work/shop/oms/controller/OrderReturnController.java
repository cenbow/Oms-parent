package com.work.shop.oms.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.api.param.bean.CreateOrderRefund;
import com.work.shop.oms.api.param.bean.CreateOrderReturn;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.api.param.bean.CreateOrderReturnGoods;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderAddressInfoExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderAttachment;
import com.work.shop.oms.bean.OrderAttachmentExample;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.OrderReturnShipExample;
import com.work.shop.oms.bean.ReturnForward;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.common.bean.CommonUtil;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.PayType;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderAttachmentMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderReturnGoodsDetailMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.exception.BGSystemException;
import com.work.shop.oms.orderReturn.service.OrderReturnSearchService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.service.ErpWarehouseListService;
import com.work.shop.oms.service.OrderReturnInfoService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.Constants;
import com.work.shop.oms.utils.FtpUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.Pagination;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.OrderRefundListVO;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.ReturnAccountVO;
import com.work.shop.oms.vo.ReturnCommonVO;
import com.work.shop.oms.vo.ReturnGoodsVO;
import com.work.shop.oms.vo.ReturnOrderParam;
import com.work.shop.oms.vo.ReturnOrderVO;
import com.work.shop.oms.vo.SettleParamObj;
import com.work.shop.oms.vo.StorageGoods;


@Controller
@RequestMapping(value = "orderReturn")
public class OrderReturnController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String SERVER_DOMAIN = ConfigCenter.getProperty("serverDomain");
	
	@Resource
	private OrderReturnSearchService orderReturnSearchService;
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	
	@Resource
	private OrderAttachmentMapper orderAttachmentMapper;
	
	@Resource
	private OrderReturnService orderReturnService;
	@Resource
	private OrderReturnGoodsMapper orderReturnGoodsMapper;

	@Resource
	private OrderReturnStService orderReturnStService;

	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
   	private OrderReturnInfoService orderReturnInfoService;
	@Resource
	private OrderSettleService orderSettleService;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private OrderReturnGoodsDetailMapper orderReturnGoodsDetailMapper;
	@Resource
	private OrderReturnMapper orderReturnMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	
	
	/**
	 * 调转至查看订单详情页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderReturnPage")
	public ModelAndView orderDetail(HttpServletRequest request,
			HttpServletResponse response, String relatingOrderSn, String returnSn,Integer returnType) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("relatingOrderSn", relatingOrderSn);
		mav.addObject("returnSn", returnSn);
		MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(relatingOrderSn);
		if(masterOrderInfo !=null){
			mav.addObject("masterOrderSn", masterOrderInfo.getMasterOrderSn());
			mav.addObject("siteCode", masterOrderInfo.getChannelCode());
		}
		OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(relatingOrderSn);
		if(orderDistribute != null){
			mav.addObject("masterOrderSn", orderDistribute.getMasterOrderSn());
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(orderDistribute.getMasterOrderSn());
			if(master !=null){
				mav.addObject("siteCode", master.getChannelCode());
			}
		}
		if(StringUtils.isNotBlank(returnSn)){
			OrderReturn orderReturn = orderReturnSearchService.getOrderReturnByReturnSn(returnSn);
			if(orderReturn == null){
				throw new RuntimeException("退单不存在"+returnSn);
			}
			mav.addObject("masterOrderSn", orderReturn.getMasterOrderSn());
			mav.addObject("siteCode", orderReturn.getSiteCode());
			returnType = orderReturn.getReturnType().intValue();
		}
		mav.addObject("returnType", returnType);
		mav.setViewName("orderReturn/redirectReturnShow");
		return mav;
	}
	
	/**
	 * 退单详情
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getOrderReturnDetail")
	public ModelAndView getOrderReturnDetail(HttpServletRequest request,
			HttpServletResponse response, String returnSn, String relOrderSn,String returnType) throws Exception {
		logger.info("getOrderReturnDetail.returnSn=" + returnSn + "  relOrderSn="+ relOrderSn +",returnType:"+returnType);
		Map<String,Object>data = new HashMap<String, Object>();
		try {
				ReturnOrderVO infoVO = this.orderReturnSearchService.getOrderReturnDetailVO(returnSn, relOrderSn, returnType);
				MasterOrderAddressInfo addressInfo = null;
				List<MasterOrderPay> orderPayList = null ;
				//添加原订单收货人信息和支付信息
				String masterOrderSn = infoVO.getReturnCommon().getMasterOrderSn();
				MasterOrderAddressInfoExample masterOrderAddressInfoExample=new MasterOrderAddressInfoExample();
				masterOrderAddressInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn);
				List<MasterOrderAddressInfo>list=masterOrderAddressInfoMapper.selectByExample(masterOrderAddressInfoExample);
				if(list.size()>0){
					addressInfo = list.get(0);
				}
//				addressInfo = orderAddressInfoService.findOrderShipByOrderSn(relOrderSn);
				//转换成需要显示的地区格式
				processAddress(addressInfo);
				
				MasterOrderPayExample example =new MasterOrderPayExample();
				example.or().andMasterOrderSnEqualTo(masterOrderSn);
				orderPayList = masterOrderPayMapper.selectByExample(example);
				
				//获取退单商品入库时间戳列表
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("relatingReturnSn", returnSn);
				List<OrderReturnGoods> storageTimesList = orderReturnGoodsDetailMapper.getStorageTimesList(paramMap);
					
				Map<String, Integer> buttonStatusMap = controlOrderReturnButton(infoVO);
				data.put("success", true);
				data.put("buttonStatus", buttonStatusMap);
				data.put("returnCommon", infoVO.getReturnCommon());
				data.put("returnGoods", infoVO.getReturnGoodsList());
				data.put("returnRefunds", infoVO.getReturnRefundList());
				data.put("returnAccount", infoVO.getReturnAccount());
				data.put("orderPays", infoVO.getOrderPayList());
				data.put("orderShipType", infoVO.getOrderShipType());
				data.put("addressInfo", addressInfo);
				data.put("orderPayList", orderPayList);
				data.put("storageTimesList", storageTimesList);
			} catch (Exception e) {
				logger.debug(e.getMessage(),e);
				data.put("success", false);
				data.put("errorMessage", "该退单数据加载错误，"+e.getMessage());
				logger.error("获取退单详情数据,returnSn=" +returnSn +"异常：" + e.getMessage());
			}
			outPrintJson(response, data);
			return null;
	}
	
	
	private void processAddress(MasterOrderAddressInfo addressInfo) {
		// 地区ID转成名称
		List<String> values = new ArrayList<String>();
		if (StringUtil.isNotEmpty(addressInfo.getCountry())) {
			values.add(addressInfo.getCountry());
		}
		if (StringUtil.isNotEmpty(addressInfo.getProvince())) {
			values.add(addressInfo.getProvince());
		}
		if (StringUtil.isNotEmpty(addressInfo.getCity())) {
			values.add(addressInfo.getCity());
		}
		if (StringUtil.isNotEmpty(addressInfo.getDistrict())) {
			values.add(addressInfo.getDistrict());
		}
		if (StringUtil.isNotEmpty(addressInfo.getStreet())) {
			values.add(addressInfo.getStreet());
		}
		if (StringUtil.isListNotNull(values)) {
			SystemRegionAreaExample example = new SystemRegionAreaExample();
			example.or().andRegionIdIn(values);
			List<SystemRegionArea> regionAreas = systemRegionAreaMapper.selectByExample(example);
			if (StringUtil.isListNotNull(regionAreas)) {
				for (SystemRegionArea area : regionAreas) {
					if (area.getRegionId().equals(addressInfo.getCountry())) {
						addressInfo.setCountry(area.getRegionName());
					}
					if (area.getRegionId().equals(addressInfo.getProvince())) {
						addressInfo.setProvince(area.getRegionName());
					}
					if (area.getRegionId().equals(addressInfo.getCity())) {
						addressInfo.setCity(area.getRegionName());
					}
					if (area.getRegionId().equals(addressInfo.getDistrict())) {
						addressInfo.setDistrict(area.getRegionName());
					}
					if (area.getRegionId().equals(addressInfo.getStreet())) {
						addressInfo.setStreet(area.getRegionName());
					}
				}
			}
		}
		
		String address = "";
		if (StringUtil.isNotEmpty(addressInfo.getCountry())) {
			address += addressInfo.getCountry() + " ";
		}
		if (StringUtil.isNotEmpty(addressInfo.getProvince())) {
			address += addressInfo.getProvince() + " ";
		}
		if (StringUtil.isNotEmpty(addressInfo.getCity())) {
			address += addressInfo.getCity() + " ";
		}
		if (StringUtil.isNotEmpty(addressInfo.getDistrict())) {
			address += addressInfo.getDistrict() + " ";
		}
		if (StringUtil.isNotEmpty(addressInfo.getStreet())) {
			address += addressInfo.getStreet() + " ";
		}
		addressInfo.setAddress("[" + address + "] " + addressInfo.getAddress());
	}
	
	
	/**
	 * 退单详情按钮事件
	 */
	@RequestMapping(value = "lockReturnButtonClick")
	public ModelAndView lockReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "unLockReturnButtonClick")
	public ModelAndView unLockReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "saveReturnButtonClick")
	public ModelAndView saveReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "confirmReturnButtonClick")
	public ModelAndView confirmReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "unConfirmReturnButtonClick")
	public ModelAndView unConfirmReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
 
	@RequestMapping(value = "receiveReturnButtonClick")
	public ModelAndView receiveReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "unReceiveReturnButtonClick")
	public ModelAndView unReceiveReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "passReturnButtonClick")
	public ModelAndView passReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "unPassReturnButtonClick")
	public ModelAndView unPassReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "storageReturnButtonClick")
	public ModelAndView storageReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	
	@RequestMapping(value = "virtualStorageReturnButtonClick")
	public ModelAndView virtualStorageReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,returnOrder));
		return null;
	}
	@RequestMapping(value = "settleReturnButtonClick")
	public ModelAndView settleReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "processReturnButtonClick")
	public ModelAndView processReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "invalidReturnButtonClick")
	public ModelAndView invalidReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "communicateReturnButtonClick")
	public ModelAndView communicateReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "backToCsReturnButtonClick")
	public ModelAndView backToCsReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,String actionNote) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, actionNote,null));
		return null;
	}
	@RequestMapping(value = "editReturnButtonClick")
	public ModelAndView backToCsReturnButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,CreateOrderReturnBean returnOrder) throws Exception {
		outPrintJson(response, processButtonClick(request,returnSn, btnType, StringUtils.EMPTY,returnOrder));
		return null;
	}
	
	@RequestMapping(value = "returnPayEditButtonClick")
	public ModelAndView returnPayEditButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,String btnType,CreateOrderReturnBean returnOrder) throws Exception {
	outPrintJson(response, processButtonClick(request,returnSn, btnType, StringUtils.EMPTY,returnOrder));
		return null;
	}
	
	/**
	 * 退单页面按钮状态控制
	 * @param returnObj
	 * @return
	 */
	private Map<String,Integer> controlOrderReturnButton(ReturnOrderVO infoVO){
		Map<String,Integer> buttonStatusMap = new HashMap<String,Integer>();
		ReturnCommonVO common = infoVO.getReturnCommon();
		
		
		if(StringUtils.isEmpty(common.getReturnSn())){
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SAVERETURN, ConstantValues.YESORNO_YES);
		}else{
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_YES);
		}
			
		if(common.getLockStatus() == 0){
			//未锁定
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_NO);
		}else{
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_YES);
			//回退客服和取消回退按钮权限放开
			/*//退货单 、拒收入库单 - 操作 - 启用回退客服
			if(common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_YES);
			}   */
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RETURNFORWARD, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_MESSAGEEDIT, ConstantValues.YESORNO_YES);
			
			//已入库、部分入库的作废按钮不可用
			if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED ||
					common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
			}
			
			//确认必须已锁定
			if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM){
				//未确定 -> 确定可用，不确定不可用
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_YES);
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
			}else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
//			  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
			}else{
				
				//确认-已入库
				if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
//					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGECANCLE, ConstantValues.YESORNO_YES);
					
				}else{
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_YES);
				}
				
				if(common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY
						|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY){
					//退款单、额外退款单 - 操作
					//结算必须已确认
					if(common.getPayStatus().intValue() != 1){
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
					}else{
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
					}
				}else{
					//退货单 、拒收入库单 - 操作
					//收货必须已确认
					if(common.getIsGoodReceived().intValue() == 0){
						//未收货 -> 收货可用，未收货不可用
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_YES);
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
					}else{
						
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_NO);
						//未收货-入库
						if(common.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_YES);						 
						}
						
						//未确认 不可用
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
						
						//质检必须已收货
						if(common.getReturnOrderIspass().intValue() == 0){
							//未质检通过 -> 质检通过可用，未质检通过不可用
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_YES);
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_NO);
						}else{
							//质检通过未收货不能点击
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
							
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_NO);							   
							//质检不通过-入库
							if(common.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_YES);
							}
							
							//入库必须质检通过
							if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.WAITINPUT){
								//待入库 -> 入库可用，结算不可用
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_YES);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_VIRTUALSTORAGERETURN, ConstantValues.YESORNO_YES);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
							}else{
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_NO);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
								
								if(common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE){
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN,ConstantValues.YESORNO_NO);
								}
								//入库后不可作废
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
								
								//入库后不可回退客服
//							  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
								
								//待结算 -> 结算可用
								if(common.getPayStatus().intValue() != 1 && common.getCheckinStatus().intValue() != ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE){
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
								}else{
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
//								  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
								}
							}
						}
					}
				}
			}
		}
		if (common.getToErp() != null && common.getToErp().intValue() == 2) {
			banButtons(buttonStatusMap);
		}
		//删除商品产生的退款单在进入退单详情的时候不允许操作按钮
		if(StringUtil.isNotBlank(common.getReturnSn()) && common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY && common.getRefundType().intValue() == ConstantValues.ORDERRETURN_REFUND_TYPE.DELETE_GOODS){
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_NO);
			banButtons(buttonStatusMap);
		}
		
		//当退单无效时，禁止操作其他按钮
		if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
			banButtons(buttonStatusMap);
		}
		
		return buttonStatusMap;
	}
	private void banButtons(Map<String,Integer> buttonStatusMap) {
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SAVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_YES);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_EDITRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RETURNPAYEDIT, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_MESSAGEEDIT, ConstantValues.YESORNO_NO);
		
	}
	
	/**
	 * 获取退货仓库信息数据
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getOrderReturnGoodsDepotList")
	public ModelAndView getOrderReturnGoodsDepotList(HttpServletRequest request,HttpServletResponse response, String relOrderSn) throws Exception {
		logger.info("getOrderReturnGoodsDepotList.relOrderSn="+ relOrderSn);
		Map<String,Object>data = new HashMap<String, Object>();
		try {
			List<ChannelShop> channelShopList = this.orderReturnSearchService.getOrderReturnGoodsDepotList(relOrderSn);
			data.put("success", true);
			data.put("channelShopList", channelShopList);
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "该退货仓库信息数据加载错误，"+e.getMessage());
			logger.error("获取退货仓库信息数据,relOrderSn=" +relOrderSn +"异常：" + e.getMessage());
		}
		outPrintJson(response, data);
		return null;
	}
	/**
	 * 获取获取退货图片信息
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getOrderReturnImageList")
	public ModelAndView getOrderReturnImageList(HttpServletRequest request,HttpServletResponse response, String returnSn) throws Exception {
		logger.info("getOrderReturnImageList.returnSn="+ returnSn);
		Map<String,Object>data = new HashMap<String, Object>();
		try {
			OrderAttachmentExample orderAttachmentExample = new OrderAttachmentExample();
			orderAttachmentExample.or().andSnEqualTo(returnSn);
			List<OrderAttachment> returnImageList = orderAttachmentMapper.selectByExample(orderAttachmentExample);
			data.put("success", true);
			data.put("returnImageList", returnImageList);
			data.put("serverUrl", SERVER_DOMAIN);
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "获取获取退货图片信息加载错误，"+e.getMessage());
			logger.error("获取获取退货图片信息,returnSn=" +returnSn +"异常：" + e.getMessage());
		}
		outPrintJson(response, data);
		return null;
	}
	
	@RequestMapping(value="orderRefundList")
	public ModelAndView orderRefundList(HttpServletRequest request, HttpServletResponse response, OrderRefundListVO orderReturnSerach, PageHelper helper) throws Exception {
		try{
			 Paging paging = orderReturnSearchService.getOrderRefundPage(orderReturnSerach, helper);
			 writeJson(paging, response);
		} catch (Exception e){
			logger.error("查询退单结算数据出错！",e);
		}
		return null;
	}
	
	/**
	 * 短信发送
	 * @param mobile
	 * @param message
	 * @param actionUser
	 * @param returnSn
	 */
	@RequestMapping(value="sendMessage")
	public void sendMessage(HttpServletRequest request,HttpServletResponse response ) {
		Map<String,Object> data = new HashMap<String, Object>();
		String mobile = StringUtils.trimToEmpty(request.getParameter("mobileNum"));
		String message = StringUtils.trimToEmpty(request.getParameter("message"));
		String returnSn = StringUtils.trimToEmpty(request.getParameter("returnSn"));
		try {
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			String actionUser = adminUser.getUserName();
			orderReturnService.sendReturnMessage(mobile, message, actionUser, returnSn);
			data.put("success", true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			data.put("success", false);
			data.put("errorMessage", e.getMessage());
		}
		outPrintJson(response, data);
	}
	
	/**
	 * 退单详情-按钮处理公共逻辑
	 * @param returnSn
	 * @param btnType
	 * @param returnOrder
	 * @return
	 */
	private Map<String,Object> processButtonClick(HttpServletRequest request,String returnSn,String btnType,String actionNote,CreateOrderReturnBean returnOrder){
		Map<String,Object> data = new HashMap<String, Object>();
		ReturnInfo<String> result = new ReturnInfo<String>();
		long time = System.currentTimeMillis();
		try{
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			String actionUser = adminUser.getUserName();
			Integer userId = Integer.valueOf(adminUser.getUserId());
			actionNote = StringUtils.trimToEmpty(actionNote);
			ReturnOrderParam returnOrderParam = new ReturnOrderParam();
			returnOrderParam.setReturnSn(returnSn);
			returnOrderParam.setActionNote(actionNote);
			returnOrderParam.setUserId(userId);
			returnOrderParam.setPullInAll(false);
			if(null == returnOrder){
				returnOrderParam.setPullInAll(true);
			}else{
				if(StringUtils.isNotBlank(returnOrder.getGoodsId())){
					StorageGoods storageGoods = new StorageGoods();
					List<StorageGoods> storageGoodsList = new ArrayList<StorageGoods>();
					OrderReturnGoods orderReturnGoods = orderReturnGoodsMapper.selectByPrimaryKey(Long.valueOf(returnOrder.getGoodsId()));
					returnOrderParam.setActionNote(actionNote+"单件商品"+orderReturnGoods.getCustomCode());
					storageGoods.setId(orderReturnGoods.getId());
					storageGoods.setProdScanNum(orderReturnGoods.getGoodsReturnNumber());
					storageGoods.setCustomCode(orderReturnGoods.getCustomCode());
					storageGoods.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
					storageGoodsList.add(storageGoods);
					returnOrderParam.setStorageGoods(storageGoodsList);
				}else{
					returnOrderParam.setPullInAll(true);
				}
			}
			
			returnOrderParam.setUserName(actionUser);
			returnOrderParam.setToERP(true);//推送erp
			
			if(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN.equalsIgnoreCase(btnType)){
				//锁定退单
				result = orderReturnStService.returnOrderLock(returnSn, actionNote, actionUser, userId);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN.equalsIgnoreCase(btnType)){
				//解锁退单
				result = orderReturnStService.returnOrderUnLock(returnSn, StringUtils.EMPTY, actionUser, userId);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_SAVERETURN.equalsIgnoreCase(btnType)){
				//保存退单
				returnOrder.setActionUser(actionUser);
				returnOrder.setActionNote(actionNote);
				result = orderReturnService.createOrderReturn(returnOrder);
				
				if(result.getIsOk() > 0){
					returnSn = result.getReturnSn();
					//锁定退单
					ReturnInfo<String> lockResult = orderReturnStService.returnOrderLock(returnSn, actionNote, actionUser, userId);				 
					logger.info("退单锁定："+JSON.toJSON(lockResult));
					//确认退单
					/*ReturnInfo<String> confirmResult = orderReturnStService.returnOrderConfirm(returnSn, actionNote, actionUser);
					logger.info("退单确认："+JSON.toJSON(confirmResult));*/
					
					//失货退货单默认收货质检
					if(returnOrder.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS){
					   returnOrderParam.setReturnSn(returnSn);
					  //已收货
					  orderReturnStService.returnOrderReceive(returnOrderParam);
					  //质检通过
					  orderReturnStService.returnOrderPass(returnOrderParam);
					}
				}
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN.equalsIgnoreCase(btnType)){
				//确认退单
				result = orderReturnStService.returnOrderConfirm(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN.equalsIgnoreCase(btnType)){
				//未确认
				result = orderReturnStService.returnOrderUnConfirm(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN.equalsIgnoreCase(btnType)){
				//已收货
				result = orderReturnStService.returnOrderReceive(returnOrderParam);
				if(result.getIsOk() > 0){
					//质检通过
					result = orderReturnStService.returnOrderPass(returnOrderParam);
				}
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN.equalsIgnoreCase(btnType)){
				//未收货
				result = orderReturnStService.returnOrderUnReceive(returnOrderParam);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN.equalsIgnoreCase(btnType)){
				//质检通过
				result = orderReturnStService.returnOrderPass(returnOrderParam);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN.equalsIgnoreCase(btnType)){
				//质检不通过
				result = orderReturnStService.returnOrderUnPass(returnOrderParam);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN.equalsIgnoreCase(btnType) || Constants.RETURN_ORDER_CLICK_TYPE_VIRTUALSTORAGERETURN.equalsIgnoreCase(btnType)){
				//入库
				result = orderReturnStService.returnOrderStorage(returnOrderParam);
			}/*else if(Constants.RETURN_ORDER_CLICK_TYPE_STORAGECANCLE.equalsIgnoreCase(btnType) || Constants.RETURN_ORDER_CLICK_TYPE_VIRTUALSTORAGERETURN.equalsIgnoreCase(btnType)){
				//入库撤销
				result = orderReturnStService.returnStorageCancle(returnSn,actionUser,storageTimeStamp);
			}*/else if(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN.equalsIgnoreCase(btnType)){
				//结算
				result = orderReturnStService.returnOrderSettle(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN.equalsIgnoreCase(btnType)){
				//作废
				result = orderReturnStService.returnOrderInvalid(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN.equalsIgnoreCase(btnType)){
				//沟通
				result = orderReturnStService.returnOrderCommunicate(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN.equalsIgnoreCase(btnType)){
				//回退客服
				result = orderReturnStService.returnOrderBackToCs(returnSn, actionNote, actionUser);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_EDITRETURN.equalsIgnoreCase(btnType)){
				//更新退单信息
				returnOrder.setActionUser(actionUser);
				result = orderReturnService.updateOrderReturn(returnOrder);
			}else if(Constants.RETURN_ORDER_CLICK_TYPE_RETURNPAYEDIT.equalsIgnoreCase(btnType)){
				//更新退单信息
				returnOrder.setActionUser(actionUser);
				result = orderReturnService.updateOrderReturnFee(returnOrder);
			}else{
				throw new RuntimeException("无法解析前台操作");
			}
			data.put("ReturnInfo", result);
			if(result.getIsOk() > 0){
				data.put("success", true);
			}else{
				data.put("success", false);
				data.put("errorMessage", "退单操作失败："+result.getMessage());
			}
			data.put("returnCommon", new ReturnCommonVO());
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "退单操作异常："+e.getMessage());
			logger.error("退单按钮操作,returnSn=" +returnSn +"异常：" + e.getMessage());
		}finally{
			logger.debug("processButtonClick.end....returnSn:"+returnSn+",time:"+(System.currentTimeMillis() - time));
		}
		return data;
	}
	
	@RequestMapping(value = "checkDepotCode")
	public String checkDepotCode(String returnSn,HttpServletResponse response,String type){
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtil.isBlank(returnSn)){
			map.put("message", "退单号为空！");
			
		}else {
			if(type.equalsIgnoreCase("haveDepotFlag")){//校验是否有仓库
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
				if(StringUtil.isNotBlank(orderReturnShip.getDepotCode())){
					map.put("success", true);
				}
				if(StringUtils.isNotBlank(orderReturnShip.getReturnExpress())){
					map.put("returnExpressFlag", true);
				}
			}else{//校验是否入库
				OrderReturn orderReturn = orderReturnSearchService.getOrderReturnByReturnSn(returnSn);
				if(orderReturn.getShipStatus() != null && orderReturn.getShipStatus().intValue() == ConstantValues.ORDERRETURN_SHIP_STATUSS.STORAG){
					map.put("success", true);
					map.put("message", "退单已入库，无法再更新仓库！");
				}
			}
			
		}
		outPrintJson(response, map);
		return null;
	}
	
	@RequestMapping(value = "checkReturnData")
	public String checkReturnData(String returnSn,HttpServletResponse response,String type){
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("success", false);
		if(StringUtil.isBlank(returnSn)){
			map.put("message", "退单号为空！");
		}else {
			if(type.equalsIgnoreCase("checkReturnExpress")){//校验是否有承运商
				OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
				if(StringUtil.isNotBlank(orderReturnShip.getReturnExpress())){
					map.put("success", true);
				}
			}
		}
		outPrintJson(response, map);
		return null;
	}
	
	/**
	 * 退单转发
	 */
	@RequestMapping(value = "returnForward")
	public void returnForward(ReturnForward returnForward,HttpServletResponse response,String type,HttpServletRequest request){
		outPrintJson(response, orderReturnStService.returnForward(returnForward, type,getLoginUser(request).getUserName()));
	}

	/**
	 * 更新退单商品数据
	 * @param request
	 * @param response
	 * @param createOrderReturnBean
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="updateReturnGoods")
	public String updateReturnGoods(HttpServletRequest request,HttpServletResponse response,CreateOrderReturnBean createOrderReturnBean) throws Exception{
		Map<String,Object>map=new HashMap<String, Object>();
		map.put("success", false);
		// 获取用户信息
		AdminUser adminUser = getLoginUser(request);
		createOrderReturnBean.setActionUser(adminUser.getUserName());
		ReturnInfo<String> orm=orderReturnService.updateReturnGoods(createOrderReturnBean);
		if(orm.getIsOk()==Constant.YESORNO_YES){
			map.put("success", true);
			map.put("message", "更新退单商品数据成功！");
		}else{
			map.put("message", orm.getMessage());
		}
		outPrintJson(response, map);
		return null;
	}
	
	/**
	 * 退货商品图片上传
	 */
	@RequestMapping(value = "uploadImageButtonClick")
	public ModelAndView uploadImageButtonClick(HttpServletRequest request,
			HttpServletResponse response, String returnSn,@RequestParam(value = "returnImage", required = false) MultipartFile returnImage) throws Exception {
		Map<String,Object> data = new HashMap<String, Object>();
		try{
			String path = request.getSession().getServletContext().getRealPath("upload");
			
			//文件后缀
			String[] imgType = returnImage.getContentType().split("/");
			String fileType = StringUtils.EMPTY;
			if (StringUtil.isArrayNotNull(imgType) && "image".equals(imgType[0])) {
				fileType = imgType[1];
			}
			
			// 文件名重新定义
			String dateStr = TimeUtil.format3Date(new Date());
			int random = new Random().nextInt(10000);
			String newFileName = returnSn + "_" + dateStr + "_" + random +"." +fileType;
			File targetFile = new File(path, newFileName);
			if(!targetFile.exists()){
				targetFile.mkdirs();
			}
			HashMap<String, Object> uploadResult = FtpUtil.uploadFile(newFileName, returnImage.getInputStream(), "");
			
			if((Boolean)uploadResult.get("isok")) {
				String ftpPath = (String)uploadResult.get("path");
				
				//附件路径-数据库
				OrderAttachment orderAttachment = new OrderAttachment();
				orderAttachment.setSn(returnSn);
				orderAttachment.setOrderType((short)2);
				orderAttachment.setFilepath(ftpPath + "/" + newFileName);
				orderAttachment.setAddTime(new Date());
				orderAttachmentMapper.insertSelective(orderAttachment);
				data.put("success", true);
			} else {
				data.put("success", false);
				data.put("errorMessage", "退货图片上传失败");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "退货图片上传异常："+e.getMessage());
			logger.error("退货图片上传操作,returnSn=" +returnSn +"异常：" + e.getMessage());
		}
		outPrintJson(response, data);
		return null;
	}
	
	/**
	 * 根据退单号获取退单日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getOrderReturnActions")
	public void getOrderReturnActions(HttpServletRequest request,
			HttpServletResponse response,String returnSn) throws Exception {
		if(StringUtil.isTrimEmpty(returnSn)){
			Paging paging = new Paging(0, null);
			paging.setMessage("根据退单号获取退单日志：退单号为空！");
			paging.setTotalProperty(0);
			outPrintJson(response, paging);
			return ;
		}
		OrderReturnAction model = new OrderReturnAction();
		model.setReturnSn(returnSn);
		List<OrderReturnAction> infos = orderReturnStService.getOrderReturnActionList(model);
		Paging paging = new Paging(infos.size(), infos);
		outPrintJson(response, paging);
	}
	
	

	
	/* 退单列表查询
   	 * 
   	 * @param request
   	 * @param response
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "orderReturnList")
   	public ModelAndView orderReturnList(HttpServletRequest request, HttpServletResponse response, 
   			OrderReturnListVO orderReturnSerach, PageHelper helper,String type) throws Exception {
   		
   		String method = request.getParameter("method") == null ? "" : request.getParameter("method");
   		if (StringUtil.isNotNull(method) && method.equals("init")) {
   			ModelAndView mav = new ModelAndView();
   			
   		//	Map<String, String>  map = (Map<String, String>)request.getSession().getAttribute(Constant.SYSTEM_RESOURCE);
   			
   		//	if(StringUtil.isNotBlank(map.get(Constant.RETURN_LIST  +"_"+Constant.DETAILS_DISPLAY))){
   				mav.addObject("display", "1");
   		//	}
   			
   			if(StringUtil.isNotBlank(type) && "settle".equals(type)) {
   				mav.addObject("settleType", "orderReturnSettleList");//待退单结算单
   			}	
   			
   			if(StringUtil.isNotBlank(type) && "bePutInStorage".equals(type)) {
   				mav.addObject("settleType", "bePutInStorage");//待入库结算单
   			}
   				
   		//	if(StringUtil.isNotBlank(map.get(Constant.RETURN_LIST  +"_"+Constant.ORDER_RETURN_DISPLAY))){
   				mav.addObject("orderReturnDisplay", "1");
   		//	}
   			
   			mav.setViewName("orderReturnList/orderReturnListPage");
   		
   			return mav;
   		}
   		try{
   			HttpSession session = request.getSession();
			List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
			if (!StringUtil.isListNotNull(strings)) {
				return null;
			}
			orderReturnSerach.setSites(strings);
   			Paging paging = null;
   			 if(StringUtil.isNotBlank(type) && "settle".equals(type)) {
   				 orderReturnSerach.setIsNotQuanQiuTong(true);
   				 paging = orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
   			 }else{
   				 paging = orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
   			 }
   			 
   			 writeJson(paging, response);
   		} catch (Exception e){
   			e.printStackTrace();
   		}
   		
   		return null;
   	}

   	
   	
   	/**
	 * 退单csv导出
	 * 
	 * @param request
	 * @param response
	 * @param orderReturnSerach
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	@RequestMapping(value = "orderReturnExportCsvFile.spmvc")
	public void orderReturnExportCsvFile(HttpServletRequest request, HttpServletResponse response, OrderReturnListVO orderReturnSerach, PageHelper helper) throws Exception {
		String noOrderGoods = orderReturnSerach.getNoOrderGoods();
		InputStream is = null;
		BufferedWriter writer = null;
		AdminUser adminUser = getLoginUser(request);
		
		HttpSession session = request.getSession();
		List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
		if (!StringUtil.isListNotNull(strings)) {
			return;
		}
		orderReturnSerach.setSites(strings);
		int pageSize = 5000;
		Pagination pagination = new Pagination(1, pageSize);
		helper.setLimit(pageSize);
		try{
			logger.debug("创建导出退单CSV文件开始！");
			//创建本地文件
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "orTempFile";
			String sfileName ="orderReturnList" +dateStr+".csv";
			String fileName = folderName + "/"+sfileName;
			File fileRoot = new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			if(!fileRoot.exists()){
				fileRoot.mkdirs();
			}
			String path=request.getSession().getServletContext().getRealPath("/") + fileName;
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path),"GBK");
			writer=new BufferedWriter(write);
			if(StringUtil.isNull(orderReturnSerach.getExportTemplateType()) || "0".equals(orderReturnSerach.getExportTemplateType())){//默认
				Paging paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
				List<OrderReturnListVO> orderReturnList = (List<OrderReturnListVO>)paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				StringBuffer sb = new StringBuffer();
				sb.append(
					"退单号," + 
					"主订单号," + 
				   "关联订单号," + 
					"交易类型," + 
				   "订单来源," +
					"referer," +
				   "下单人,"+
				   "关联外部交易号," +  
					"退单类型," + 
				   "订单状态," +
					"退单状态," +
				   "财务状态," + 
					"物流状态," + 
				   "入库时间," +
	 				"邮费," + 
				   "退商品金额," + 
	 				"退货快递单号," + 
				   "结算时间," +
					"退款方式," + 
				   "产生退单时间," + 
				   "扫描数量,"+
					"退货数量," + 
				   "退款金额," +
					"退其他费用," +
				   "处理方式," + 
					"退单原因," + 
				   "商品代码," +
					"商品名称," + 
				   "企业SKU码," + 
					"商品SKU码," + 
				 
				   "退货数量," +
					"购买数量," + 
				   "实际价格,"+
					"成交价格," + 
				   "退款银行," + 
					"退款支行," + 
				   "退款帐号," + 
					"退款账户," + 
				   "退货入库仓," + 
					"操作日志" + 
				   "\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportOrderReturn(orderReturnList, noOrderGoods, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
						getExportOrderReturn((List<OrderReturnListVO>)paging.getRoot(), noOrderGoods, writer);
					}
				}
			} else if("1".equals(orderReturnSerach.getExportTemplateType())){ // 财务导出模版
				Paging paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
				List<OrderReturnListVO> orderReturnList = (List<OrderReturnListVO>)paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				StringBuffer sb = new StringBuffer();
				sb.append(
					"退单号," + 
					"退单类型," + 
					"关联订单号," + 
					"订单来源," +
					"关联外部交易号," +
					"付款方式," + 
					"付款备注," + 
					 "扫描数量,"+
					"退货数量," +
					"退款总金额," + 
					"退商品金额," + 
					"财务价格," + 
					"退红包金额," +
					"退邮费," + 
					"退其他费用," +
					"退款方式," + 
					"退单状态," +
					"财务状态," +
					"是否退款," +
					"入库时间,"+
					"承运商,"+
					"快递单号,"+
					"使用积分金额"+
					"\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportOrderReturnByFinance(orderReturnList, noOrderGoods, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
						getExportOrderReturnByFinance((List<OrderReturnListVO>)paging.getRoot(), noOrderGoods,writer);
					}
				}
			}else if("2".equals(orderReturnSerach.getExportTemplateType())){//物流模版
				Paging paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
				List<OrderReturnListVO> orderReturnList = (List<OrderReturnListVO>)paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				StringBuffer sb = new StringBuffer();
				sb.append(
					
						"退单号," + 
						"关联订单号," + 
						"关联订单来源," + 
						"referer," + 
						"关联外部交易号," + 
						"关联支付方式," + 
						"退单类型," + 	
					//	"退款方式," + 	
						"产生退单时间," + 	
						"入库时间," + 
				//		"下单人," + 
				//		"退单人," + 	
						"扫描数量,"+
						"退货数量(退货单)," + 
						"退款金额," + 
						"邮费," + 
						"退商品金额," + 	
						"处理方式," + 
						"退货快递单号," + 	
						"结算时间," + 	
						"退单状态," + 	
						"财务状态," + 
						"物流状态," + 
					//	"操作者工号," + 
						"退单原因," + 
						"商品代码," + 	
						"商品名称," + 	
						"企业SKU码," + 
			//			"商品SKU码," + 	
				//		"退货数量," + 	
						"购买数量," + 
						"退货数量," + 
						"实际价格," + 
						"成交价格," + 	
						"入库人,"+
						"退款银行," + 	
						"退款支行," + 	
						"退款帐号," + 
						"退款账户," + 	
						"退货入库仓," + 
						"操作日志" + 
						
						"\r\n"
					);
					writer.write(sb.toString());
					writer.flush();
					if (paging.getTotalProperty() > 0) {
						//第一页
						getExportOrderReturnByLogistics(orderReturnList, noOrderGoods, writer);
						//第二页及后续页
						for (int j = 2; j <= pagination.getTotalPages(); j++) {
							pagination.setCurrentPage(j);
							helper.setStart(pagination.getStartRow());
							paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
							getExportOrderReturnByLogistics((List<OrderReturnListVO>)paging.getRoot(), noOrderGoods,writer);
						}
					}
			}
			JsonResult jsonResult = new JsonResult();
			try {
				is = new FileInputStream(path);
				String ftpFileName = StringUtil.fileNameSpliceCsv("OrderReturn", adminUser.getUserName());
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
					jsonResult.setData("");
				}
				writeObject(jsonResult, response);
				if (StringUtil.isArrayNotNull(fileRoot.listFiles())) {
					for (File temp : fileRoot.listFiles()) {
						if (temp.getName().equals(sfileName)) {
							logger.info("删除本地临时生成文件,路径：" + path);
							temp.delete();
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("退单信息导出上传至FTP异常", e);
				jsonResult.setIsok(false);
			} finally {
				if(is != null) {
					try {
						is.close();
						is = null;
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
			logger.error("导出失败!", e);
		}
		logger.debug("创建导出退单CSV文件完成！");	
		
	}
	
	
	/**
	 * 导出默认模版
	 * @param  List<OrderReturnListVO> list;
	 * @param  noOrderGoods  无商品标示;
	 ***/
	private void getExportOrderReturn(List<OrderReturnListVO> list, String noOrderGoods, BufferedWriter writer) {
		if (!StringUtil.isListNotNull(list)) {
			return;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			for(OrderReturnListVO orderReturn : list){
				String returnSn = orderReturn.getReturnSn() == null ? "" :orderReturn.getReturnSn(); 	
				String masterOrderSn = orderReturn.getMasterOrderSn() == null ? "" :"\'" +orderReturn.getMasterOrderSn();
				String relatingOrderSn = orderReturn.getRelatingOrderSn() == null ? "" :"\'" + orderReturn.getRelatingOrderSn();
			
				String transTypeStr = orderReturn.getTransTypeStr() == null ? "":orderReturn.getTransTypeStr();
				String channelName = orderReturn.getChannelName() == null ? "" : orderReturn.getChannelName();
				
				String referer = orderReturn.getReferer() == null ? "" :  "\"" +orderReturn.getReferer() + "\"";
				String orderOutSn = orderReturn.getOrderOutSn() == null ? "" :"\"'"+ orderReturn.getOrderOutSn()+ "\"";
				String returnTypeStr = orderReturn.getReturnTypeStr() == null ? "" : orderReturn.getReturnTypeStr();
				
				String userName = orderReturn.getUserName() == null ? "" : orderReturn.getUserName();
		
				String returnOrderStatusStr = orderReturn.getOrderStatusStr() == null ? "" :  "\"" +orderReturn.getOrderStatusStr()+ "\"";
		
				String getReturnOrderStatusStr = orderReturn.getReturnOrderStatusStr() == null ? "" :  "\"" +orderReturn.getReturnOrderStatusStr()+ "\"";
				String getPayStatus = orderReturn.getPayStatus() == null ? "" :  "\"" +CommonUtil.payStatus(orderReturn.getPayStatus())+ "\"";
				
				
				String checkinStatus = orderReturn.getCheckinStatus() == null ? "" :  CommonUtil.checkinStatus(orderReturn.getCheckinStatus());
				
				
				String logStatus = "\"" +checkinStatus+ "\"";

				//入库时间
				String checkinTime = orderReturn.getCheckinTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getCheckinTime());
				
				//邮费
				BigDecimal dReturnShipping  = orderReturn.getReturnShipping();
				String returnShipping =  (String) (dReturnShipping == null ? "" : dReturnShipping.toString());
				
				//退商品金额
				BigDecimal dReturnGoodsMoney  = orderReturn.getReturnGoodsMoney();
				String returnGoodsMoney =  (String) (dReturnGoodsMoney == null ? "" : dReturnGoodsMoney.toString());
				
				//退货快递单号
				String returnInvoiceNo = orderReturn.getReturnInvoiceNo() == null ? "" :  "\"" +orderReturn.getReturnInvoiceNo()+ "\"";
			
				//结算时间
				String clearTime = orderReturn.getClearTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getClearTime());
				String returnPayStr = orderReturn.getReturnPayStr() == null ? "" :  "\"" +orderReturn.getReturnPayStr()+ "\"";
				String addTime = orderReturn.getAddTime() == null ? "" : TimeUtil.formatDate(orderReturn.getAddTime());
				Integer iReturnGoodsCount =  orderReturn.getReturnGoodsCount();
				String returnGoodsCount =  (String)(iReturnGoodsCount == null ? "" : iReturnGoodsCount.toString());
				BigDecimal dReturnTotalFee  = orderReturn.getReturnTotalFee();
				String returnTotalFee =  (String) (dReturnTotalFee == null ? "" : dReturnTotalFee.toString());
				String returnOtherMoney = orderReturn.getReturnOtherMoney() == null ? "0" : orderReturn.getReturnOtherMoney().toString();
				String processTypeStr = orderReturn.getProcessTypeStr() == null ? "" : orderReturn.getProcessTypeStr();
				String returnReasonName = orderReturn.getReturnReasonName() == null ? "" : orderReturn.getReturnReasonName(); 
				
				String returnReasonStr = orderReturn.getReturnReasonStr() == null ? "" : orderReturn.getReturnReasonStr(); //退单原因
			
				String prodscannum =orderReturn.getProdscannum() == null ? "0" : orderReturn.getProdscannum().toString(); //扫描数量
		
				String skuSn = "";
				String goodsSn  ="";
				String goodsName="";
				String customCode = "";
				String goodsReturnNumber="";
				String goodsBuyNumber="";
				String marketPrice = "";
				String goodsPrice = "";
				//导出不包含商品信息
				if(!Constant.EXPORT_THE_RESULTS_DO_NOT_INCLUDE_THE_COMMODITY_INFO.equals(noOrderGoods)){
					goodsSn = orderReturn.getGoodsSns() == null ? "" : "\"'" +orderReturn.getGoodsSns()+ "\"";//商品代码
					goodsName = orderReturn.getGoodsNames() == null ? "" :"\"" + orderReturn.getGoodsNames()+ "\"";//商品名称
					skuSn = orderReturn.getSkuSns() == null ? "" :"\"'" + orderReturn.getSkuSns()+ "\"";//13位码
					customCode = orderReturn.getCustumCodes() == null ? "" : "\"'" +orderReturn.getCustumCodes()+ "\"";//11位码
					goodsReturnNumber = orderReturn.getGoodsReturnNumber() == null ? "0" : orderReturn.getGoodsReturnNumber().toString();//退货数量	
					goodsBuyNumber = orderReturn.getGoodsBuyNumber() == null ? "0" : orderReturn.getGoodsBuyNumber().toString();//购买数量	
					marketPrice = orderReturn.getMarketPrice() == null ? "0" : orderReturn.getMarketPrice().toString();//实际价格
					goodsPrice = orderReturn.getGoodsPrice() == null ? "0" : orderReturn.getGoodsPrice().toString();//成交价格
				}
				String bank = orderReturn.getBank() == null ? "" : orderReturn.getBank();	
				String subsidiaryBank = orderReturn.getSubsidiaryBank() == null ? "" : orderReturn.getSubsidiaryBank();
				String accountNumber = orderReturn.getAccountNumber() == null ? "" : orderReturn.getAccountNumber();
				String account = orderReturn.getAccount() == null ? "" : orderReturn.getAccount();
				String warehouseName = orderReturn.getWarehouseName() == null ? "" : orderReturn.getWarehouseName();
				String str ="";
				if(StringUtil.isNotBlank(returnSn)) {
					List<OrderReturnAction> OrderReturnActionList = orderReturnInfoService.getOrderReturnActionList(returnSn);
					for(OrderReturnAction shortageQuestion :OrderReturnActionList){
						String actionNote = shortageQuestion.getActionNote();
						str +=actionNote;
						str +="|";
					}
				}
				str = str.replace("\n", "");
				buffer.append(returnSn+","+	//退单号
						masterOrderSn+","+ //主订单
						relatingOrderSn+","+   //关联订单号
						transTypeStr+","+	//交易类型
						channelName+","+   //订单来源
						referer+","+   //referer
						userName+","+
						orderOutSn+","+   //关联外部交易号
						returnTypeStr+","+  //退单类型
						returnOrderStatusStr+","+   //订单状态
						getReturnOrderStatusStr +","+   //退单状态
						getPayStatus +","+  //财务状态
						logStatus +","+  //物流状态
						checkinTime +","+   //入库时间
						returnShipping +","+   // 邮费
						returnGoodsMoney +","+  //  退商品金额
						returnInvoiceNo +","+  //  退货快递单号
						clearTime +","+   //   结算时间
						returnPayStr+","+   //  退款方式
						addTime+","+  //  产生退单时间
						
						prodscannum+","+ //扫描数量
						returnGoodsCount+","+  //  退货数量
						returnTotalFee+","+   //  退款金额
						returnOtherMoney+","+  //  退其他费用
						processTypeStr+","+   //  处理方式
						returnReasonStr+","+  //  退单原因
						goodsSn+","+   //  商品代码
						goodsName+","+  //  商品名称
						customCode+","+   //  企业SKU码
						skuSn+","+   //  商品SKU码
						goodsReturnNumber+","+  //  退货数量
						goodsBuyNumber+","+  //  购买数量
						marketPrice+","+   //  实际价格
						goodsPrice+","+   //  成交价格
						bank +","+  //   退款银行
						subsidiaryBank +","+  //  退款支行
						accountNumber +","+  //  退款帐号
						account  +","+  //  退款账户
						warehouseName+","+   //  退货入库仓
						"\"" +str+ "\""   //  操作日志
						+"\r\n"
				);
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
	}
	
	/**
	 * 导出财务模版退单信息 
	 **/
	private void getExportOrderReturnByFinance(List<OrderReturnListVO> list, String noOrderGoods, BufferedWriter writer) {
		if (!StringUtil.isListNotNull(list)) {
			return;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			for(OrderReturnListVO orderReturn : list){
				String returnSn = orderReturn.getReturnSn() == null ? "" :orderReturn.getReturnSn(); 	
				
				String masterOrderSn = orderReturn.getMasterOrderSn() == null ? "" :orderReturn.getMasterOrderSn();
				
				String relatingOrderSn = orderReturn.getRelatingOrderSn() == null ? "" : "\'" +orderReturn.getRelatingOrderSn();
				String channelName = orderReturn.getChannelName() == null ? "" : orderReturn.getChannelName();
				String payNote = orderReturn.getPayNote() == null ? "" : "\"'" +"\'" +orderReturn.getPayNote()+ "\"";
				String orderOutSn = StringUtil.isEmpty(orderReturn.getOrderOutSn()) ? "" :"\"'"+ orderReturn.getOrderOutSn()+ "\"";
				String returnTypeStr = orderReturn.getReturnTypeStr() == null ? "" : orderReturn.getReturnTypeStr();
				String getReturnOrderStatusStr = orderReturn.getReturnOrderStatusStr() == null ? "" :  "\"" +orderReturn.getReturnOrderStatusStr()+ "\"";
				String getPayStatus = orderReturn.getPayStatus() == null ? "" :  "\"" +CommonUtil.payStatus(orderReturn.getPayStatus())+ "\"";
				//邮费
				BigDecimal dReturnShipping = orderReturn.getReturnShipping();
				String returnShipping = (String) (dReturnShipping == null ? "" : dReturnShipping.toString());
				//退商品金额
				BigDecimal dReturnGoodsMoney  = orderReturn.getReturnGoodsMoney();
				String returnGoodsMoney = (String) (dReturnGoodsMoney == null ? "" : dReturnGoodsMoney.toString());
				// 退款方式
				String returnPayName = orderReturn.getReturnPayStr() == null ? "" :  orderReturn.getReturnPayStr();
				// 退款方式
				String returnPaysStr = orderReturn.getReturnPaysStr() == null ? "" :  "\"" +orderReturn.getReturnPaysStr()+ "\"";

				// 支付方式
				String payName = orderReturn.getPayName() == null ? "" : "\"" + orderReturn.getPayName()+ "\"";
				
				Integer iReturnGoodsCount =  orderReturn.getReturnGoodsCount();
				
				String returnGoodsCount =  (String)(iReturnGoodsCount == null ? "" : iReturnGoodsCount.toString());
				//退款总金额
				BigDecimal dReturnTotalFee  = orderReturn.getReturnTotalFee();
				String returnTotalFee =  (String) (dReturnTotalFee == null ? "" : dReturnTotalFee.toString());
				String returnOtherMoney = orderReturn.getReturnOtherMoney() == null ? "0" : orderReturn.getReturnOtherMoney().toString();
				
		/*		BigDecimal dBonus  = orderReturn.getReturnBonusMoney();
				BigDecimal bonus =  (BigDecimal) (dBonus == null ?  new BigDecimal(0.00) : dBonus);*/
								
				BigDecimal returnBonusMoney = (BigDecimal) (orderReturn.getReturnBonusMoney() == null ? new BigDecimal(0.00) : orderReturn.getReturnBonusMoney());
				
		//		String financialPrices = orderReturn.getFinancialPrices() == null ? "" : orderReturn.getFinancialPrices().toString();
				
				BigDecimal dFinancialPrices  = orderReturn.getFinancialPrices();
				BigDecimal financialPrices =  (BigDecimal) (dFinancialPrices == null ?  new BigDecimal(0.00) : dFinancialPrices);
				
				
				String haveRefund = orderReturn.getHaveRefund() == null ? "" :  "\"" +CommonUtil.haveRefund(orderReturn.getHaveRefund())+ "\"";
				
				
				String returnInvoiceNo = orderReturn.getReturnInvoiceNo() == null ? "" :  "\'" +orderReturn.getReturnInvoiceNo(); 	
				
				String returnExpressStr = orderReturn.getReturnExpressStr() == null ? "" :orderReturn.getReturnExpressStr();
				
				String checkinTime = orderReturn.getCheckinTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getCheckinTime());
				

			//	String prodscannum =(String)(orderReturn.getProdscannum() == null ? "0" : orderReturn.getProdscannum()); //扫描数量
					
				String prodscannum =orderReturn.getProdscannum() == null ? "0" : orderReturn.getProdscannum().toString(); //扫描数量
				
				BigDecimal integralMoney  = orderReturn.getIntegralMoney();
				integralMoney =  (BigDecimal) (integralMoney == null ?  new BigDecimal(0.00) : integralMoney);
		
				buffer.append(
						returnSn+","+ //退单号
						returnTypeStr+","+  //退单类型
						relatingOrderSn+","+   //关联订单号
						channelName+","+   //订单来源
						orderOutSn+","+   //关联外部交易号
						payName +","+  //付款方式
						payNote +","+   // 付款备注
						prodscannum+","+ //扫描数量
						returnGoodsCount+","+  //  退货数量
						returnTotalFee +","+ //退款总金额   
						returnGoodsMoney +","+  //  退商品金额
						financialPrices+"," +//财务价格
						returnBonusMoney+","+  //退红包金额	orderinfo.Bonus
						returnShipping +","+ //退邮费
						returnOtherMoney+","+  //  退其他费用
						returnPaysStr+","+   //  退款方式
						getReturnOrderStatusStr +","+   //退单状态
						getPayStatus +","+  //财务状态
						haveRefund+","+ 
						checkinTime+","+
						
						returnExpressStr+","+
						returnInvoiceNo+","+
						integralMoney+
						"\r\n"
				);
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
	}
	
	
	/**
	 *csv导出, 物流模版 
	 **/
	private void getExportOrderReturnByLogistics(List<OrderReturnListVO> list, String noOrderGoods, BufferedWriter writer) {
		
		if (!StringUtil.isListNotNull(list)) {
			return;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			for(OrderReturnListVO orderReturn : list){
				String returnSn = orderReturn.getReturnSn() == null ? "" :orderReturn.getReturnSn(); 	
				
				String masterOrderSn = orderReturn.getMasterOrderSn() == null ? "" :orderReturn.getMasterOrderSn();
				
				String relatingOrderSn = orderReturn.getRelatingOrderSn() == null ? "" :"\'" + orderReturn.getRelatingOrderSn();
			
				String transTypeStr = orderReturn.getTransTypeStr() == null ? "":orderReturn.getTransTypeStr();
				String channelName = orderReturn.getChannelName() == null ? "" : orderReturn.getChannelName();
				
				String referer = orderReturn.getReferer() == null ? "" :  "\"" +orderReturn.getReferer() + "\"";
				String orderOutSn = orderReturn.getOrderOutSn() == null ? "" :"\"'"+ orderReturn.getOrderOutSn()+ "\"";
				String returnTypeStr = orderReturn.getReturnTypeStr() == null ? "" : orderReturn.getReturnTypeStr();
		
				String returnOrderStatusStr = orderReturn.getOrderStatusStr() == null ? "" :  "\"" +orderReturn.getOrderStatusStr()+ "\"";
		
				String getReturnOrderStatusStr = orderReturn.getReturnOrderStatusStr() == null ? "" :  "\"" +orderReturn.getReturnOrderStatusStr()+ "\"";
				String getPayStatus = orderReturn.getPayStatus() == null ? "" :  "\"" +CommonUtil.payStatus(orderReturn.getPayStatus())+ "\"";
				
				String isGoodReceived = orderReturn.getIsGoodReceived() == null ? "" :  CommonUtil.isGoodReceived(orderReturn.getIsGoodReceived());
				
				String checkinStatus = orderReturn.getCheckinStatus() == null ? "" :  CommonUtil.checkinStatus(orderReturn.getCheckinStatus());
				
				String qualityStatus = orderReturn.getQualityStatus() == null ? "" : CommonUtil.qualityStatus(orderReturn.getQualityStatus());
				
				String logStatus = "\"" +isGoodReceived+","+checkinStatus+","+qualityStatus+ "\"";

				//入库时间
				String checkinTime = orderReturn.getCheckinTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getCheckinTime());
				
				//入库时间
				String orgCheckinTime = orderReturn.getOrgCheckinTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getOrgCheckinTime());
				
				//邮费
				BigDecimal dReturnShipping  = orderReturn.getReturnShipping();
				String returnShipping =  (String) (dReturnShipping == null ? "" : dReturnShipping.toString());
				
				//退商品金额
				BigDecimal dReturnGoodsMoney  = orderReturn.getReturnGoodsMoney();
				String returnGoodsMoney =  (String) (dReturnGoodsMoney == null ? "" : dReturnGoodsMoney.toString());
				
				//退货快递单号
				String returnInvoiceNo = orderReturn.getReturnInvoiceNo() == null ? "" :  "\"'" +orderReturn.getReturnInvoiceNo()+ "\"";
			
				// 支付方式
				String payName = orderReturn.getPayName() == null ? "" : "\"" + orderReturn.getPayName()+ "\"";
				
				// String prodscannum =(String)(orderReturn.getProdscannum() == null ? "0" : orderReturn.getProdscannum()); //扫描数量
				 
					String prodscannum =orderReturn.getProdscannum() == null ? "0" : orderReturn.getProdscannum().toString(); //扫描数量
				
				//结算时间
				String clearTime = orderReturn.getClearTime() == null ? "" :  TimeUtil.formatDate(orderReturn.getClearTime());
				String returnPayStr = orderReturn.getReturnPayStr() == null ? "" :  "\"" +orderReturn.getReturnPayStr()+ "\"";
				String addTime = orderReturn.getAddTime() == null ? "" : TimeUtil.formatDate(orderReturn.getAddTime());
				Integer iReturnGoodsCount =  orderReturn.getReturnGoodsCount();
				String returnGoodsCount =  (String)(iReturnGoodsCount == null ? "" : iReturnGoodsCount.toString());
				BigDecimal dReturnTotalFee  = orderReturn.getReturnTotalFee();
				String returnTotalFee =  (String) (dReturnTotalFee == null ? "" : dReturnTotalFee.toString());
				String returnOtherMoney = orderReturn.getReturnOtherMoney() == null ? "0" : orderReturn.getReturnOtherMoney().toString();
				String processTypeStr = orderReturn.getProcessTypeStr() == null ? "" : orderReturn.getProcessTypeStr();
				String returnReasonName = orderReturn.getReturnReasonName() == null ? "" : orderReturn.getReturnReasonName(); 
				
				String returnReasonStr = orderReturn.getReturnReasonStr() == null ? "" : orderReturn.getReturnReasonStr(); //退单原因
				
				String actionUser = orderReturn.getActionUser() == null ? "" : orderReturn.getActionUser(); //入库人
				
				String skuSn = "";
				String goodsSn  ="";
				String goodsName="";
				String customCode = "";
				String goodsReturnNumber="";
				String goodsBuyNumber="";
				//String marketPrice = "";
			//	String goodsPrice = "";
				//导出不包含商品信息
			//	if(!Constant.EXPORT_THE_RESULTS_DO_NOT_INCLUDE_THE_COMMODITY_INFO.equals(noOrderGoods)){
				
				goodsSn = orderReturn.getGoodsSn() == null ? "" : "\"'" +orderReturn.getGoodsSn()+ "\"";//商品代码
				goodsName = orderReturn.getGoodsName() == null ? "" :"\"" + orderReturn.getGoodsName()+ "\"";//商品名称
				skuSn = orderReturn.getSkuSn() == null ? "" :"\"'" + orderReturn.getSkuSn()+ "\"";//13位码
				customCode = orderReturn.getCustomCode() == null ? "" : "\"'" +orderReturn.getCustomCode()+ "\"";//11位码
				
				goodsReturnNumber = orderReturn.getGoodsReturnNumber() == null ? "0" : orderReturn.getGoodsReturnNumber().toString();//退货数量	
				goodsBuyNumber = orderReturn.getGoodsBuyNumber() == null ? "0" : orderReturn.getGoodsBuyNumber().toString();//购买数量	
				
				//	marketPrice = orderReturn.getMarketPrice() == null ? "0" : orderReturn.getMarketPrice().toString();//实际价格
				//	goodsPrice = orderReturn.getGoodsPrice() == null ? "0" : orderReturn.getGoodsPrice().toString();//成交价格
						
				BigDecimal marketPrice = (BigDecimal) (orderReturn.getMarketPrice() == null ? new BigDecimal(0.00):orderReturn.getMarketPrice());//实际价格
				BigDecimal goodsPrice = (BigDecimal) (orderReturn.getGoodsPrice() == null ? new BigDecimal(0.00):orderReturn.getGoodsPrice());//成交价格
					
		//		}
				String bank = orderReturn.getBank() == null ? "" : orderReturn.getBank();	 //   退款银行 34
				String subsidiaryBank = orderReturn.getSubsidiaryBank() == null ? "" : orderReturn.getSubsidiaryBank(); //  退款支行 35
				String accountNumber = orderReturn.getAccountNumber() == null ? "" : orderReturn.getAccountNumber(); //  退款帐号 36
				String account = orderReturn.getAccount() == null ? "" : orderReturn.getAccount();  //退款账户 37
				String warehouseName = orderReturn.getWarehouseName() == null ? "" : orderReturn.getWarehouseName();//  退货入库仓38
				
				String str ="";
				if(StringUtil.isNotBlank(returnSn)) {
					List<OrderReturnAction> OrderReturnActionList = orderReturnInfoService.getOrderReturnActionList(returnSn);
					for(OrderReturnAction shortageQuestion :OrderReturnActionList){
						String actionNote = shortageQuestion.getActionNote();
						str +=actionNote;
						str +="|";
					}
				}
				str = str.replace("\n", "");
				
				buffer.append(
						
					/*	
								"退单号," +	 //1
								"关联订单号," +	 //2 
								"关联订单来源," +	//3
								"referer," +	 // 4
								"关联外部交易号," +   //5
								"关联支付方式," +	//6
								"退单类型," + 	 //7
								"退款方式," + 	//8
								"产生退单时间," + 	//9
								"入库时间," +	 //10
								"下单人," +	 //11
								"退单人," + 	   //12
								"退货数量," +	//13
								"退款金额," +   //14
								"邮费," +	  //15
								"退商品金额," + 	   //16
								"处理方式," +	 //17
								"退货快递单号," + 	 //18
								"结算时间," + 	//19
								"退单状态," + 	//20
								"财务状态," + //21
								"物流状态," + //22
								"操作者工号," + //23
								"退单原因," + //24
								"商品代码," + 	//25
								"商品名称," + 	//26
								"企业SKU码," + //27
								"商品SKU码," + 	//28
								"退货数量," + 	//29
								"购买数量," + //30
								"实际价格," + //31
								"成交价格," + 	 //32
								"操作日志," + 	//33
								"退款银行," + 	//34
								"退款支行," + 	//35
								"退款帐号," + //36
								"退款账户," + 	//37
								"退货入库仓," //38
								+ */
		
						
						returnSn+","+	//退单号   1
						relatingOrderSn+","+   //关联订单号  2
						channelName	+","+					//3	"关联订单来源," 
						referer+","+ //referer   4
						orderOutSn+","+   //关联外部交易号  5
						
						payName	 +","+  		//6	"关联支付方式,"   
						returnTypeStr+","+  //退单类型   7
					//	returnPayStr+","+   //  退款方式8
						addTime+","+  //  产生退单时间9
						orgCheckinTime +","+   //入库时间10
					//		 //  下单人,11
					//	   // 	"退单人," + 12
						prodscannum+","+ //扫描数量
						returnGoodsCount+","+  //  退货数量13
						returnTotalFee+","+   //  退款金额14
						returnShipping +","+   // 邮费15
						returnGoodsMoney +","+  //  退商品金额16
						processTypeStr+","+   //  处理方式17
						returnInvoiceNo +","+  //  退货快递单号18
						clearTime +","+   //   结算时间 19
						getReturnOrderStatusStr +","+   //退单状态20
						getPayStatus +","+  //财务状态21
						logStatus +","+  //物流状态22
						
						returnReasonStr+","+  //  退单原因24				
						goodsSn+","+   //  商品代码25
						goodsName+","+  //  商品名称26
						customCode+","+   //  企业SKU码 27
					//	skuSn+","+   //  商品SKU码 28
						goodsBuyNumber+","+  //  购买数量30
						goodsReturnNumber+","+//退货数量	 order_return_goods
						
						marketPrice+","+   //  实际价格 31
						goodsPrice+","+   //  成交价格 32
						actionUser+","+
						bank +","+  //   退款银行 34
						subsidiaryBank +","+  //  退款支行 35
						accountNumber +","+  //  退款帐号 36
						account  +","+  //  退款账户 37
						warehouseName+","+	//  退货入库仓38
						"\"" +str+ "\"" +  //  操作日志 33
						"\r\n"
						
					/*	transTypeStr+","+	//交易类型
						channelName+","+   //订单来源
						referer+","+   //referer
						orderOutSn+","+   //关联外部交易号
						returnTypeStr+","+  //退单类型
						returnOrderStatusStr+","+   //订单状态
						getReturnOrderStatusStr +","+   //退单状态
						getPayStatus +","+  //财务状态
						logStatus +","+  //物流状态
						checkinTime +","+   //入库时间
						returnShipping +","+   // 邮费
						returnGoodsMoney +","+  //  退商品金额
						returnInvoiceNo +","+  //  退货快递单号
						clearTime +","+   //   结算时间
						returnPayStr+","+   //  退款方式
						addTime+","+  //  产生退单时间
						returnGoodsCount+","+  //  退货数量
						returnTotalFee+","+   //  退款金额
						returnOtherMoney+","+  //  退其他费用
						processTypeStr+","+   //  处理方式
						returnReasonStr+","+  //  退单原因
						goodsSn+","+   //  商品代码
						goodsName+","+  //  商品名称
						customCode+","+   //  企业SKU码
						skuSn+","+   //  商品SKU码
						goodsReturnNumber+","+  //  退货数量
						goodsBuyNumber+","+  //  购买数量
						marketPrice+","+   //  实际价格
						goodsPrice+","+   //  成交价格
						bank +","+  //   退款银行
						subsidiaryBank +","+  //  退款支行
						accountNumber +","+  //  退款帐号
						account  +","+  //  退款账户
						warehouseName+","+   //  退货入库仓
						"\"" +str+ "\""   //  操作日志
*/						
				);
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
		
		
	}
	
	@RequestMapping(value="orderRefundExportCsv")
	public void orderRefundExportCsv(HttpServletRequest request, HttpServletResponse response,OrderRefundListVO orderReturnSerach, PageHelper helper) throws Exception{
		InputStream is = null;
		BufferedWriter writer = null;
		AdminUser adminUser  = getLoginUser(request);
		
		int pageSize = 5000;
		Pagination pagination = new Pagination(1, pageSize);
		helper.setLimit(pageSize);
		try{
			logger.debug("创建导出退单结算CSV文件开始！");
			//创建本地文件
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "orTempFile"; 
			String sfileName ="orderRefundList" +dateStr+".csv";
			String fileName = folderName + "/"+sfileName;
			File fileRoot = new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			if(!fileRoot.exists()){
				fileRoot.mkdirs();
			}
			String path=request.getSession().getServletContext().getRealPath("/") + fileName;
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path),"GBK");
			writer=new BufferedWriter(write);
			Paging paging = orderReturnSearchService.getOrderRefundPage(orderReturnSerach, helper);
				List<OrderRefundListVO> orderRefundList = (List<OrderRefundListVO>)paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				StringBuffer sb = new StringBuffer();
				sb.append(
					"退单号," + 
				   "关联订单号," + 
				   "关联外部交易号," +  
				   "退款金额,"+
				   "订单来源," +
				   "referer," +
				   "退单类型," + 
				   "退单状态," +
				   "财务状态," + 
				   "退款方式," + 
				   "退款方式code," + 
				   "退款方式备注,"+
				   "备注"+
					"\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportOrderRefund(orderRefundList, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = orderReturnInfoService.getOrderRefundPage(orderReturnSerach, helper);
						getExportOrderRefund((List<OrderRefundListVO>)paging.getRoot(), writer);
					}
				}
			JsonResult jsonResult = new JsonResult();
			try {
				is = new FileInputStream(path);
				String ftpFileName = StringUtil.fileNameSpliceCsv("OrderRefund", adminUser.getUserName());
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
					jsonResult.setData("");
				}
				writeObject(jsonResult, response);
				if (StringUtil.isArrayNotNull(fileRoot.listFiles())) {
					for (File temp : fileRoot.listFiles()) {
						if (temp.getName().equals(sfileName)) {
							logger.info("删除本地临时生成文件,路径：" + path);
							temp.delete();
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("退单结算信息导出上传至FTP异常", e);
				jsonResult.setIsok(false);
			} finally {
				if(is != null) {
					try {
						is.close();
						is = null;
					} catch(Exception e){
						logger.error("orderRefundExportCsv 关闭流文件【InputStream】异常", e);
					}
				}
				if(writer != null){
					writer.close();
					writer = null;
				}
			}
		}catch (Exception e) {
			logger.error("导出失败!", e);
		}
		logger.debug("创建导出退单结算CSV文件完成！");	
	}
	
	//导出结算退单
		private void getExportOrderRefund(List<OrderRefundListVO> orderRefundList,BufferedWriter writer) {
			if(CollectionUtils.isEmpty(orderRefundList)){
				return;
			}
			StringBuffer buffer = new StringBuffer();
			try {
				for(OrderRefundListVO orderRefundListVO : orderRefundList){
					String relatingReturnSn = orderRefundListVO.getRelatingReturnSn() == null ? "" :"\t"+orderRefundListVO.getRelatingReturnSn(); 		
					String relatingOrderSn = orderRefundListVO.getRelatingOrderSn() == null ? "" : "\t"+orderRefundListVO.getRelatingOrderSn();
					String orderOutSn = orderRefundListVO.getOrderOutSn() == null ? "" :"\t"+orderRefundListVO.getOrderOutSn();
					String returnFee = orderRefundListVO.getReturnFee() == null ?"" : orderRefundListVO.getReturnFee().toString();
					String channelName= orderRefundListVO.getChannelName()==null ?"" : orderRefundListVO.getChannelName();
					String referer = orderRefundListVO.getReferer() == null ? "" :  orderRefundListVO.getReferer();
					String returnTypeName = orderRefundListVO.getReturnTypeName()==null ? "" : orderRefundListVO.getReturnTypeName();
					String returnTotalStatus = orderRefundListVO.getReturnTotalStatus() == null ? "" :orderRefundListVO.getReturnOrderStatusName()+"-"+orderRefundListVO.getPayStatusName()+"-"+orderRefundListVO.getCheckinStatusName()+"-"+orderRefundListVO.getIsGoodReceivedName()+"-"+orderRefundListVO.getQualityStatusName();
					String returnPayStatusName = orderRefundListVO.getReturnPayStatusName() == null ? "" :orderRefundListVO.getReturnPayStatusName();
					String payName = orderRefundListVO.getPayName() == null ? "" :orderRefundListVO.getPayName();
					String returnPay = orderRefundListVO.getReturnPay()== null ? "" :orderRefundListVO.getReturnPay().toString();
					String payNote = orderRefundListVO.getPayNote() == null ? "" :"\t"+orderRefundListVO.getPayNote();
					
					if(StringUtils.isNotBlank(payNote)){
						payNote = payNote.replaceAll(",", "-");
					}
					buffer.append(relatingReturnSn+","+	//退单号
							relatingOrderSn+","+   //关联订单号
							orderOutSn+","+	//关联外部交易号
							returnFee+","+	//退款金额
							channelName+","+   //订单来源
							referer+"," +  //referer
							returnTypeName+"," +  //退单类型
							returnTotalStatus+"," +  //退单状态
							returnPayStatusName+"," +  //财务状态
							payName+"," +  //退款方式
							returnPay+"," +  //退款方式code
							payNote+","+   //退款方式备注	
				
							"\"" +relatingOrderSn+"关联退单号"+relatingReturnSn+"做退款,退"+payName+returnFee+ "\""
							+"\r\n"
					);
					writer.write(buffer.toString());
					writer.flush();
					buffer = new StringBuffer();
				}
			} catch (Exception e) {
				logger.error("查询内容写入CSV文件异常", e);
			}
		}
		
		
		/**
		 * 获取仓库
		 * @return
		 * @throws BGSystemException
		 */
		@RequestMapping(value = "getWarehouseList")
		public ModelAndView returnOrderProcess(HttpServletRequest request,
				HttpServletResponse response) {

			List<ErpWarehouseList>  infos = erpWarehouseListService.getErpWarehouseList();
			ErpWarehouseList erpWarehouseList = new ErpWarehouseList();
			erpWarehouseList.setWarehouseCode("-1");
			erpWarehouseList.setWarehouseName("请选择...");
			if(StringUtil.isNotNullForList(infos)){
				infos.add(0, erpWarehouseList);
				outPrintJson(response, infos);
			}
			return null;

		}
		

		@Resource
		private ErpWarehouseListService erpWarehouseListService;
		
		
		/**
		 *待结算退单 
		 ***/
		@RequestMapping(value = "orderReturnOrdersettleExportCsvFile.spmvc")
		public void orderReturnOrdersettleExportCsvFile(HttpServletRequest request,
				HttpServletResponse response, OrderReturnListVO orderReturnSerach,
				PageHelper helper) throws Exception {
			String noOrderGoods = orderReturnSerach.getNoOrderGoods();
			InputStream is = null;
			BufferedWriter writer = null;
			AdminUser adminUser = getLoginUser(request);
			int pageSize = 5000;
			Pagination pagination = new Pagination(1, pageSize);
			helper.setLimit(pageSize);
			
		//	 Set<String> strSet  = orderReturnInfoService.selectOrderInfoOfSettlementByPayStatus((byte)2,(byte)3);
		//	 List<String> addList = new ArrayList<String>(strSet);
		//	 orderReturnSerach.setAddList(addList);
			 orderReturnSerach.setIsNotQuanQiuTong(true);
			
			try{
					
				logger.debug("创建导出退单CSV文件开始！");
				//创建本地文件
				String dateStr = TimeUtil.format3Date(new Date());
				String folderName = "orTempFile"; 
				String sfileName ="orderReturnList" +dateStr+".csv";
				String fileName = folderName + "/"+sfileName;
				File fileRoot = new File(request.getSession().getServletContext().getRealPath("/") + folderName);
				if(!fileRoot.exists()){
					fileRoot.mkdirs();
				}
				String path=request.getSession().getServletContext().getRealPath("/") + fileName;
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path),"GBK");
				writer=new BufferedWriter(write);
			
				
		//		if(StringUtil.isNull(orderReturnSerach.getExportTemplateType()) || "0".equals(orderReturnSerach.getExportTemplateType())){
				
				 // 财务导出模版
				Paging paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
				List<OrderReturnListVO> orderReturnList = (List<OrderReturnListVO>)paging.getRoot();
				pagination.setTotalSize(paging.getTotalProperty());
				StringBuffer sb = new StringBuffer();
				sb.append(
				"退单号," + 
				"退单类型," + 
				"关联订单号," + 
				"订单来源," +
				"关联外部交易号," +
				"付款方式," + 
				"付款备注," + 
				"扫描数量,"+
				"退货数量," +
				"退款总金额," + 
				"退商品金额," + 
				"财务价格," + 
				"退红包金额," +
				"退邮费," + 
				"退其他费用," +
				"退款方式," + 
				"退单状态," +
				"财务状态," +
				"是否退款," +
				"入库时间,"+
				"承运商,"+
				"快递单号,"+
				"使用积分金额 "+
				"\r\n"
				);
				writer.write(sb.toString());
				writer.flush();
				if (paging.getTotalProperty() > 0) {
					//第一页
					getExportOrderReturnByFinance(orderReturnList, noOrderGoods, writer);
					//第二页及后续页
					for (int j = 2; j <= pagination.getTotalPages(); j++) {
						pagination.setCurrentPage(j);
						helper.setStart(pagination.getStartRow());
						paging = this.orderReturnInfoService.getOrderReturnPage(orderReturnSerach, helper);
						getExportOrderReturnByFinance((List<OrderReturnListVO>)paging.getRoot(), noOrderGoods,writer);
					}
				}
			
			
				JsonResult jsonResult = new JsonResult();
				try {
					is = new FileInputStream(path);
					String ftpFileName = StringUtil.fileNameSpliceCsv("OrderReturn", adminUser.getUserName());
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
						jsonResult.setData("");
					}
					writeObject(jsonResult, response);
					if (StringUtil.isArrayNotNull(fileRoot.listFiles())) {
						for (File temp : fileRoot.listFiles()) {
							if (temp.getName().equals(sfileName)) {
								logger.info("删除本地临时生成文件,路径：" + path);
								temp.delete();
								break;
							}
						}
					}
				} catch (Exception e) {
					logger.error("退单信息导出上传至FTP异常", e);
					jsonResult.setIsok(false);
				} finally {
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
				
			}catch(Exception e){ 
				logger.error("导出失败!", e);
			}
			logger.debug("创建导出退单CSV文件完成！");
		}
		
	
		@RequestMapping(value = "pushOrderReturnToERP")
		public@ResponseBody String pushOrderReturnToERP(HttpServletResponse response,String returnSns,Integer bussType,Integer orderType){
			long time = System.currentTimeMillis();
			ReturnInfo returnInfo = new ReturnInfo();
			try{
			logger.info("pushOrderReturnToERP:returnSns:"+returnSns+",bussType:"+bussType);
			if(StringUtil.isEmpty(returnSns) || null == bussType || null == orderType){
				logger.debug("推送单号为空、bussType类别不存在、单子类别（订单0、退单1）为空！");
				return null;
			}
			
			StringBuffer message =new StringBuffer();
			message.append("失败单子：");
			for( String returnSn : returnSns.split(",")){
				SettleParamObj paramObj = new SettleParamObj();
				paramObj.setDealCode(returnSn);
				paramObj.setBussType(bussType);
				paramObj.setUserId("system");
				paramObj.setTools(true);
				if(bussType == ConstantValues.ORDER_SELLTE_BUSSMODE.INPUT){//入库
					//推送erp拼接数据
					List<StorageGoods> storageList = new ArrayList<StorageGoods>();
					OrderReturnGoodsExample example = new OrderReturnGoodsExample();
					example.or().andRelatingReturnSnEqualTo(returnSn);
					List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(example);
					for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
						StorageGoods storageGoods = new StorageGoods();
						storageGoods.setId(orderReturnGoods.getId());
						storageGoods.setProdScanNum(orderReturnGoods.getGoodsReturnNumber());
						storageGoods.setCustomCode(orderReturnGoods.getCustomCode());
						storageGoods.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
						storageList.add(storageGoods);
					}
					paramObj.setStorageGoods(storageList);
					returnInfo = orderSettleService.inputReturnOrder(paramObj);
					logger.info("退单returnSn:"+returnSn+"入库推送ERP结果：isOk:"+returnInfo.getIsOk()+"message:"+returnInfo.getMessage()+"用时："+(System.currentTimeMillis()-time)+"毫秒,paramObj:"+JSON.toJSONString(paramObj));
				}else if(bussType == ConstantValues.ORDER_SELLTE_BUSSMODE.SELLTE){//结算
					if(orderType == ConstantValues.YESORNO_NO){//订单结算
						returnInfo = orderSettleService.MasterOrderSettle(paramObj);
						logger.info("订单OrderSn:"+returnSn+"结算推送ERP结果：isOk:"+returnInfo.getIsOk()+"message:"+returnInfo.getMessage()+"用时："+(System.currentTimeMillis()-time)+"毫秒,paramObj:"+JSON.toJSONString(paramObj));
					}else if (orderType == ConstantValues.YESORNO_YES){//退单结算
						returnInfo = orderSettleService.settleReturnOrder(paramObj);
						logger.info("退单returnSn:"+returnSn+"结算推送ERP结果：isOk:"+returnInfo.getIsOk()+"message:"+returnInfo.getMessage()+"用时："+(System.currentTimeMillis()-time)+"毫秒,paramObj:"+JSON.toJSONString(paramObj));
					}
				}else if(bussType == ConstantValues.ORDER_SELLTE_BUSSMODE.POINTS){
					/*double totalUserPayAmt = 0;
					String actionNote = "";
					String userId = "";
					logger.info("积分增减开始:"+returnSn);
					try {
						if(orderType == ConstantValues.YESORNO_NO){
							MasterOrderInfo order = masterOrderInfoMapper.selectByPrimaryKey(returnSn);
							
							MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
							goodsExample.or().andMasterOrderSnEqualTo(returnSn);
							List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
							if(CollectionUtils.isEmpty(goodsList)){
								throw new RuntimeException("无法获取有效的商品列表！");
							}
							SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
							List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
							double totalSettlement = 0d;
							for (MasterOrderGoods orderGoods : goodsList) {
								String customCode = orderGoods.getCustomCode();
								if(StringUtils.isBlank(customCode)){
									throw new RuntimeException("商品编码为空");
								}
								
								SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
								settleGoodsInfo.setGoodssku(customCode);
								settleGoodsInfo.setCost(orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue());
								settleGoodsInfo.setCount(orderGoods.getGoodsNumber().intValue());
								settleGoodsInfo.setGoodsname(orderGoods.getGoodsName());
								settleGoodsInfo.setUnitprice(orderGoods.getMarketPrice().doubleValue());
								if(orderGoods.getMarketPrice().doubleValue()>0){
									settleGoodsInfo.setRate(CommonUtil.roundingOff(2,orderGoods.getGoodsPrice().doubleValue()/orderGoods.getMarketPrice().doubleValue()));
								}else{
									settleGoodsInfo.setRate(1);
								}
								settleGoodsInfoList.add(settleGoodsInfo);
								totalSettlement += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().doubleValue();
							}
							settleOrderInfo.setUvid(order.getUserId());
							settleOrderInfo.setOrdersn(returnSn);
							settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
							settleOrderInfo.setGuideid(ConstantValues.HQ01S116);
							settleOrderInfo.setShopid(ConstantValues.HQ01S116);
							settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
							settleOrderInfo.setTime(new Date().getTime());
							
							
							totalUserPayAmt = order.getTotalFee().doubleValue() - order.getBonus().doubleValue();
							userId = order.getUserId();
							int point = (int)Math.floor(totalSettlement);
							ReturnInfo pointResponse = orderReturnService.plusAndMinusPoints(settleOrderInfo);
							if(pointResponse.getIsOk() > 0){
								actionNote = "订单结算-结算数据时CAS积分("+point+")赠送成功！orderSn:"+returnSn+",userId:"+userId+",totalUserPayAmt:"+totalUserPayAmt+",pointResponse:"+JSON.toJSONString(pointResponse);
								logger.info(actionNote);
							}else{
								actionNote = "订单结算-结算数据时CAS积分("+point+")赠送失败！orderSn:"+returnSn+",userId:"+userId+",totalUserPayAmt:"+totalUserPayAmt+",pointResponse:"+JSON.toJSONString(pointResponse);
								logger.info(actionNote);
							}
							OrderAction orderAction = new OrderAction();
							orderAction.setOrderSn(returnSn);
							orderAction.setActionUser("system");
							orderAction.setOrderStatus(order.getOrderStatus());
							orderAction.setShippingStatus(order.getShipStatus());
							orderAction.setPayStatus(order.getPayStatus());
							orderAction.setRangeStatus((byte) -1);
							orderAction.setQuestionStatus(order.getQuestionStatus().byteValue());
							orderAction.setActionNote(actionNote);
							orderAction.setLogTime(new Date());
							// 保存操作日志
							orderActionMapper.insertSelective(orderAction);*/
						}/*else if (orderType == ConstantValues.YESORNO_YES){
							
							OrderReturn orderReturn = orderReturnInfoService.getOrderReturnByReturnSn(returnSn);
							if(orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() &&
									orderReturn.getReturnType().intValue() != ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()){
								message.append(returnSn+",");
								continue;
							}
							userId = orderReturn.getUserId();
							OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
							goodsExample.or().andRelatingReturnSnEqualTo(returnSn);
							List<OrderReturnGoods> goodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
							
							SettleOrderInfo settleOrderInfo = new SettleOrderInfo();//积分参数
							List<SettleGoodsInfo> settleGoodsInfoList = new ArrayList<SettleGoodsInfo>();//积分商品
							double totalSettlement = 0d;
							if(CollectionUtils.isNotEmpty(goodsList)){
								for (OrderReturnGoods orderReturnGoods : goodsList) {
									totalUserPayAmt += orderReturnGoods.getSettlementPrice().doubleValue()*orderReturnGoods.getGoodsReturnNumber().doubleValue();
									
									SettleGoodsInfo settleGoodsInfo =new SettleGoodsInfo();
									settleGoodsInfo.setGoodssku(orderReturnGoods.getCustomCode());
									settleGoodsInfo.setCost(0-orderReturnGoods.getSettlementPrice().doubleValue()*orderReturnGoods.getGoodsReturnNumber().doubleValue());
									settleGoodsInfo.setCount(orderReturnGoods.getGoodsReturnNumber().intValue());
									settleGoodsInfo.setGoodsname(orderReturnGoods.getGoodsName());
									settleGoodsInfo.setUnitprice(orderReturnGoods.getMarketPrice().doubleValue());
									if(orderReturnGoods.getMarketPrice().doubleValue()>0){
										settleGoodsInfo.setRate(CommonUtil.roundingOff(2,orderReturnGoods.getGoodsPrice().doubleValue()/orderReturnGoods.getMarketPrice().doubleValue()));
									}else{
										settleGoodsInfo.setRate(1);
									}
									settleGoodsInfoList.add(settleGoodsInfo);
									totalSettlement += orderReturnGoods.getSettlementPrice().doubleValue()*orderReturnGoods.getGoodsReturnNumber().doubleValue();
									
								
								}
							}
							settleOrderInfo.setUvid(orderReturn.getUserId());
							settleOrderInfo.setOrdersn(returnSn);
							settleOrderInfo.setGoodsinfos(settleGoodsInfoList);
							settleOrderInfo.setGuideid(ConstantValues.HQ01S116);
							settleOrderInfo.setShopid(ConstantValues.HQ01S116);
							settleOrderInfo.setSettlement((int)Math.floor(totalSettlement));
							settleOrderInfo.setTime(new Date().getTime());
							
							
							int point = (int)Math.floor(totalSettlement);
							ReturnInfo pointResponse = orderReturnService.plusAndMinusPoints(settleOrderInfo);
							if(pointResponse.getIsOk() > 0){
								actionNote = "退单结算-结算数据时CAS积分("+point+")扣除成功！orderSn:"+returnSn+",userId:"+userId+",totalUserPayAmt:"+totalUserPayAmt+",pointResponse:"+JSON.toJSONString(pointResponse);
								logger.info(actionNote);
							}else{
								actionNote = "退单结算-结算数据时CAS积分("+point+")扣除失败！orderSn:"+returnSn+",userId:"+userId+",totalUserPayAmt:"+totalUserPayAmt+",pointResponse:"+JSON.toJSONString(pointResponse);
								logger.info(actionNote);
							}
							OrderReturnShip orderReturnShip =orderReturnShipMapper.selectByPrimaryKey(returnSn);
							// 设定要保存的退单操作日志信息
							OrderReturnAction orderReturnAction = new OrderReturnAction();
							orderReturnAction.setReturnSn(returnSn);
							orderReturnAction.setActionUser("system");
							orderReturnAction.setReturnOrderStatus(orderReturn.getReturnOrderStatus().intValue());
							orderReturnAction.setReturnShippingStatus(orderReturn.getReturnShipping().intValue());
							orderReturnAction.setReturnPayStatus(orderReturn.getPayStatus().intValue());
							orderReturnAction.setIsGoodReceived(orderReturnShip.getIsGoodReceived());
							orderReturnAction.setCheckinStatus(orderReturnShip.getCheckinStatus());
							orderReturnAction.setQualityStatus(orderReturnShip.getQualityStatus());
							orderReturnAction.setActionNote(actionNote);
							orderReturnAction.setLogTime(new Date());
							// 保存操作日志
							orderReturnActionMapper.insertSelective(orderReturnAction);
						}
					} catch (Exception e) {
						logger.error("积分赠送出错！"+e+";returnSn:"+returnSn);
						message.append(returnSn+",");
					}
					
				}*/
				else if(bussType == ConstantValues.ORDER_SELLTE_BUSSMODE.BATCH_RETURN_CREATE){
				
					//批量创建退单
					ReturnOrderVO returnOrderVO = orderReturnSearchService.getOrderReturnDetailVO(null, returnSn, "1");
					ReturnCommonVO returnCommon = returnOrderVO.getReturnCommon();
					ReturnAccountVO returnAccount = returnOrderVO.getReturnAccount();
					CreateOrderReturnBean param = new CreateOrderReturnBean();
					List<ReturnGoodsVO> returnGoodsVOList = returnOrderVO.getReturnGoodsList();
					List<PayType> createOrderPayList = returnOrderVO.getOrderPayList();
					param.setRelatingOrderSn(returnSn);
					param.setReturnType(1);
					param.setAddTime(new Date());
					
					//退单信息
					/*params['createOrderReturn.relatingOrderSn']=returnCommon.relatingOrderSn;
					params['createOrderReturn.newOrderSn']=params.newOrderSn;
					params['createOrderReturn.returnSettlementType']=params.returnSettlementType;
					params['createOrderReturn.returnSn']=returnCommon.returnSn;
					params['createOrderReturn.processType']=params.processType;
					params['createOrderReturn.returnTotalFee']=returnTotalFee;
					params['createOrderReturn.returnGoodsMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnGoodsMoney").getValue();
					params['createOrderReturn.totalPriceDifference']=Ext.getCmp("returnPaySetModule").getForm().findField("totalPriceDifference").getValue();
					params['createOrderReturn.returnType']=returnCommon.returnType;
					params['createOrderReturn.returnShipping']=Ext.getCmp("returnPaySetModule").getForm().findField("returnShipping").getValue();
					params['createOrderReturn.returnOtherMoney']=params.returnOtherMoney;
					params['createOrderReturn.totalIntegralMoney']=params.totalIntegralMoney;//积分使用金额
					params['createOrderReturn.returnBonusMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnBonusMoney").getRawValue();
					params['createOrderReturn.returnReason']=params.returnReason;
					params['createOrderReturn.haveRefund']=params.haveRefund;
					params['createOrderReturn.returnDesc']=params.returnDesc;*/
					CreateOrderReturn createOrderReturn = new CreateOrderReturn();
					createOrderReturn.setRelatingOrderSn(returnCommon.getRelatingOrderSn());
					createOrderReturn.setReturnSettlementType(returnCommon.getReturnSettlementType() == null ? 1:returnCommon.getReturnSettlementType().byteValue());
					createOrderReturn.setProcessType(returnCommon.getProcessType() == null ? 1:returnCommon.getReturnSettlementType().byteValue());
					createOrderReturn.setReturnTotalFee(returnAccount.getReturnTotalFee());
					createOrderReturn.setReturnGoodsMoney(returnAccount.getReturnGoodsMoney());
					createOrderReturn.setTotalPriceDifference(returnAccount.getTotalPriceDifference());
					createOrderReturn.setReturnType(returnCommon.getReturnType().byteValue());
					createOrderReturn.setReturnShipping(returnAccount.getReturnShipping());
					createOrderReturn.setReturnOtherMoney(returnAccount.getReturnOtherMoney());
					createOrderReturn.setTotalIntegralMoney(returnAccount.getTotalIntegralMoney());
					createOrderReturn.setReturnBonusMoney(returnAccount.getReturnBonusMoney());
					createOrderReturn.setReturnReason("33");
					createOrderReturn.setHaveRefund(returnCommon.getHaveRefund()==null ? 1:returnCommon.getHaveRefund());
					createOrderReturn.setReturnDesc(returnCommon.getReturnDesc());
					
					param.setCreateOrderReturn(createOrderReturn);
					
					//退单退款单
					List<CreateOrderRefund> createOrderRefundList = new ArrayList<CreateOrderRefund>();
					for(PayType payType : createOrderPayList){
						CreateOrderRefund createOrderRefund = new CreateOrderRefund();
						createOrderRefund.setReturnPay((short)payType.getpId());
						createOrderRefund.setReturnFee(payType.getPayFee());
						createOrderRefundList.add(createOrderRefund);
					}
					param.setCreateOrderRefundList(createOrderRefundList);
					
					
					//退单商品
					/*
					params['createOrderReturnGoodsList['+index+'].haveReturnCount']=goodDataChecked[i].data.havedReturnCount;
					params['createOrderReturnGoodsList['+index+'].returnReason']=goodDataChecked[i].data.returnReason;
					params['createOrderReturnGoodsList['+index+'].goodsThumb']=goodDataChecked[i].data.goodsThumb;
					params['createOrderReturnGoodsList['+index+'].payPoints']=goodDataChecked[i].data.settlementPrice;
					params['createOrderReturnGoodsList['+index+'].seller']=goodDataChecked[i].data.seller;
					params['createOrderReturnGoodsList['+index+'].integralMoney']=goodDataChecked[i].data.integralMoney;//积分使用金额
					params['createOrderReturnGoodsList['+index+'].salesMode']=goodDataChecked[i].data.salesMode;//商品销售模式：1为自营，2为买断，3为寄售，4为直发
					
					//退差价
					params['createOrderReturnGoodsList['+index+'].priceDifferNum']=goodDataChecked[i].data.priceDifferNum;
					params['createOrderReturnGoodsList['+index+'].priceDifference']=goodDataChecked[i].data.priceDifference;
					
					//价格
					params['createOrderReturnGoodsList['+index+'].goodsPrice']=goodDataChecked[i].data.goodsPrice;
					params['createOrderReturnGoodsList['+index+'].marketPrice']=goodDataChecked[i].data.marketPrice;
					params['createOrderReturnGoodsList['+index+'].settlementPrice']=goodDataChecked[i].data.settlementPrice;
					params['createOrderReturnGoodsList['+index+'].shareBonus']=goodDataChecked[i].data.shareBonus;
							*/
					List<CreateOrderReturnGoods> createOrderReturnGoodsList = new ArrayList<CreateOrderReturnGoods>();
					for(ReturnGoodsVO returnGoodsVO : returnGoodsVOList){
						CreateOrderReturnGoods createOrderReturnGoods = new CreateOrderReturnGoods();
						createOrderReturnGoods.setCustomCode(returnGoodsVO.getCustomCode());
						createOrderReturnGoods.setExtensionCode(returnGoodsVO.getExtensionCode());
						createOrderReturnGoods.setExtensionId(returnGoodsVO.getExtensionId());
						createOrderReturnGoods.setOsDepotCode(returnGoodsVO.getOsDepotCode());
						createOrderReturnGoods.setGoodsBuyNumber(returnGoodsVO.getGoodsBuyNumber().shortValue());
						createOrderReturnGoods.setChargeBackCount(returnGoodsVO.getShopReturnCount());
						createOrderReturnGoods.setGoodsReturnNumber(returnGoodsVO.getCanReturnCount().shortValue());
						createOrderReturnGoods.setHaveReturnCount(returnGoodsVO.getHavedReturnCount());
						createOrderReturnGoods.setReturnReason(returnGoodsVO.getReturnReason());
						createOrderReturnGoods.setGoodsThumb(returnGoodsVO.getGoodsThumb());
						createOrderReturnGoods.setPayPoints(returnGoodsVO.getSettlementPrice());
						createOrderReturnGoods.setSeller(returnGoodsVO.getSeller());
						createOrderReturnGoods.setIntegralMoney(returnGoodsVO.getIntegralMoney());
						createOrderReturnGoods.setSalesMode(returnGoodsVO.getSalesMode());
						createOrderReturnGoods.setPriceDifferNum(returnGoodsVO.getPriceDifferNum().shortValue());
						createOrderReturnGoods.setPriceDifference(returnGoodsVO.getPriceDifference());
						createOrderReturnGoods.setGoodsPrice(returnGoodsVO.getGoodsPrice());
						createOrderReturnGoods.setMarketPrice(returnGoodsVO.getMarketPrice());
						createOrderReturnGoods.setSettlementPrice(returnGoodsVO.getSettlementPrice());
						createOrderReturnGoods.setShareBonus(returnGoodsVO.getShareBonus());
						createOrderReturnGoodsList.add(createOrderReturnGoods);
					}
					param.setCreateOrderReturnGoodsList(createOrderReturnGoodsList);
					param.setActionUser("system");
					orderReturnService.createOrderReturn(param);
				}
			}
			message.append("其他单子都执行成功！");
			logger.info(message.toString());
			returnInfo.setMessage(message.toString());
			}catch(Exception e){
				logger.error("pushOrderReturnToERP报错:"+e.getMessage());
				returnInfo.setMessage("pushOrderReturnToERP报错"+e.getMessage());
			}
			outPrintJson(response, returnInfo);
			return null;
		}
		
		@RequestMapping(value = "storageCancleButtonClick")
		public ModelAndView storageCancleButtonClick(HttpServletRequest request,
				HttpServletResponse response, String returnSn,String storageTimeStamp){
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			String actionUser = adminUser.getUserName();
			outPrintJson(response,orderReturnStService.returnStorageCancle(returnSn,actionUser,storageTimeStamp));
			return null;
		}
		
		@RequestMapping(value = "orderExpressPull")
		public ModelAndView orderExpressPull(HttpServletRequest request,
				HttpServletResponse response, String beginTime,String endTime,String orderFlag,String sns){
			logger.info("物流推送数据,beginTime:"+beginTime+",endTime"+endTime+",orderFlag"+orderFlag+",sns"+sns);
			ReturnInfo returnInfo = new ReturnInfo();
			ReturnInfo expressInfo = new ReturnInfo();
			StringBuffer actionNote = new StringBuffer();
			actionNote.append("物流数据推送：单号");
			try {
				if(StringUtil.isNotEmpty(sns)){
					for(String orderSn :sns.split(",")){
						if(orderFlag.equalsIgnoreCase("orderSn")){
							expressInfo = orderReturnService.orderExpress(orderSn);
						}else if(orderFlag.equalsIgnoreCase("returnSn")){
							OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderSn);
							OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(orderSn);
							expressInfo = orderReturnService.orderReturnExpress(orderReturn, orderReturnShip);
						}
						actionNote.append(orderSn+"、");
					}
				}else{
					if(orderFlag.equalsIgnoreCase("orderSn")){
						OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
						orderDepotShipExample.or().andCreatTimeBetween(TimeUtil.parseString2Date(beginTime), TimeUtil.parseString2Date(endTime));
						List<OrderDepotShip> orderDepotShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);
						for (OrderDepotShip orderDepotShip : orderDepotShipList) {
							OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(orderDepotShip.getOrderSn());
							expressInfo = orderReturnService.orderExpress(orderDistribute.getMasterOrderSn());
							actionNote.append(orderDistribute.getMasterOrderSn()+"、");
						}
					}else if(orderFlag.equalsIgnoreCase("returnSn")){
						OrderReturnShipExample orderReturnShipExample = new OrderReturnShipExample();
						orderReturnShipExample.or().andAddTimeBetween(TimeUtil.parseString2Date(beginTime), TimeUtil.parseString2Date(endTime)).andReturnInvoiceNoNotEqualTo("");
						List<OrderReturnShip> orderReturnShipList = orderReturnShipMapper.selectByExample(orderReturnShipExample);
						for (OrderReturnShip orderReturnShip : orderReturnShipList) {
							OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(orderReturnShip.getRelatingReturnSn());
							expressInfo= orderReturnService.orderReturnExpress(orderReturn, orderReturnShip);
							actionNote.append(orderReturnShip.getRelatingReturnSn()+"、");
						}
					}
					
				}
				logger.info(JSON.toJSONString(expressInfo));
				actionNote.append("物流数据推送成功！");
			} catch (Exception e) {
				logger.error("物流数据推送失败！",e);
				actionNote.append("物流数据推送失败！"+e);
			}
			returnInfo.setMessage(actionNote.toString());
			outPrintJson(response, returnInfo);
			return null;
		}
		
		/**
		 * 删除退单详情页图片信息中的指定图片
		 * @param request
		 * @param response
		 * @param returnSn
		 * @param url
		 * @return
		 */
		@RequestMapping(value = "delReturnImg")
		@ResponseBody
		public Map delReturnImg(HttpServletRequest request,
				HttpServletResponse response,String returnSn,String url){
			Map map = new HashMap();
			try{
				AdminUser user = getLoginUser(request);
				if(user==null){
					map.put("code", "0");
					map.put("msg", "登录失效！请重新登录！");
					return map;
				}
				map = orderReturnSearchService.delReturnImg(user.getUserName(),returnSn,url);
			}catch(Exception e){
				e.printStackTrace();
				map.put("code", "0");
				map.put("msg", "删除图片出错！");
			}
			return map;
		}
		
}
