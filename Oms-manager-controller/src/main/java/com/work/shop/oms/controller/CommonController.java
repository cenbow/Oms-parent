package com.work.shop.oms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.CsChannelInfo;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.OrderShopParam;
import com.work.shop.oms.common.bean.ReturnDepot;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopDepotInfo;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.orderget.service.ChannelManagerService;
import com.work.shop.oms.service.CommonService;
import com.work.shop.oms.service.RegionManagementService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.united.client.facade.UserStore;
import com.work.shop.united.client.filter.config.Config;

@Controller
@RequestMapping(value = "/common")
public class CommonController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String SHOP_USER_SERVICE_URL = ConfigCenter.getProperty("shopuserservice.address");

	@Resource
	private CommonService commonService;
	@Resource
	private SystemPaymentService systemPaymentService;
	@Resource
	private RegionManagementService regionManagementService;
	@Resource
	private ChannelManagerService channelManagerService;
	@Resource
	private OrderReturnMapper orderReturnMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private ChannelInfoService channelInfoService;
	
	/**
	 * 查询商家自定义信息列表
	 * @param request
	 * @param response
	 * @param brandCode
	 * @throws Exception
	 */
	@RequestMapping(value = "getOrderCustomDefine")
	public void getOrderCustomDefine(HttpServletRequest request, HttpServletResponse response,
			OrderCustomDefine define) throws Exception{
		List<OrderCustomDefine> defines = null;
		try {
			defines = commonService.selectOrderCustomDefine(define);
		} catch (Exception e) {
			logger.error("查询商家自定义信息列表异常", e);
		}
		outPrintJson(response, defines);
	}
	
	/**
	 * 获取付款方式
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="selectSystemPaymentList")
	public void selectSystemPaymentList(HttpServletRequest request,HttpServletResponse response){
		List<SystemPayment>list=null;
		try {
			list=systemPaymentService.getSystemPaymentList();
		} catch (Exception e) {
			logger.error("获取付款方式信息出错"+e);
		}
		outPrintJson(response, list);
	}
	
	/**
	 * 获取配送方式(快递商家下拉框)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="selectSystemShippingList")
	public void selectSystemShippingList(HttpServletRequest request,HttpServletResponse response,String returnSn){
		List<SystemShipping>list=null;
		SystemShipping systemShipping=new SystemShipping();
		try {
			list=commonService.selectSystemShippingList(systemShipping);
			
		   Map<String,Object> map = new HashMap<String, Object>();
			if(StringUtil.isNotBlank(returnSn)){
				
				OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
				OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
				orderDepotShipExample.or().andOrderSnEqualTo(orderReturn.getRelatingOrderSn());
				List<OrderDepotShip> orderDepotShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);
				
				for (OrderDepotShip orderDepotShip : orderDepotShipList) {
					map.put(orderDepotShip.getShippingId().toString(),orderDepotShip);
				}
				
				List<SystemShipping> newList = new ArrayList<SystemShipping>();
				for(SystemShipping shipping : list){
					if(map.containsKey(shipping.getShippingId().toString())){
						newList.add(shipping);
					}
				}
				
				if(CollectionUtils.isNotEmpty(newList)){
					list = newList ;
				}
			}
		} catch (Exception e) {
			logger.error("获取配送方式信息出错"+e);
		}
		outPrintJson(response, list);
	}
	
	/**
	 * 查询退单页面退款仓库信息
	 * @param request
	 * @param response
	 * @param userAddress
	 * @throws Exception
	 */
	@RequestMapping(value = "getReturnGoodsDepotList")
	public void getReturnGoodsDepotList(HttpServletRequest request, HttpServletResponse response, String siteCode) throws Exception{
		logger.info("查询商城退货仓库信息siteCode：" + siteCode);
		List<ErpWarehouseList> list = new ArrayList<ErpWarehouseList>();
		try {
			ErpWarehouseList warehouse = new ErpWarehouseList();
			warehouse.setWarehouseCode(Constant.DETAILS_DEPOT_CODE);
			warehouse.setWarehouseName("原仓退回");
			list.add(warehouse);
			if (Constant.Chlitina.equals(siteCode)) {
				ReturnInfo<ShopDepotInfo> info = getShopReturnDepotInfo();
				if (info.getIsOk() == Constant.OS_YES) {
					ShopDepotInfo depotInfo = info.getData();
					if(CollectionUtils.isNotEmpty(depotInfo.getList())){
						for(ReturnDepot returnDepot: depotInfo.getList()){
							ErpWarehouseList warehouseList = new ErpWarehouseList();
							warehouseList.setWarehouseCode(returnDepot.getCodeCd());
							warehouseList.setWarehouseName(returnDepot.getCodeDisplay());
							list.add(warehouseList);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询退货仓库信息异常" + e.getMessage(), e);
		}
		outPrintJson(response, list);
	}
	
	private ReturnInfo<ShopDepotInfo> getShopReturnDepotInfo() {
		logger.info("[获取创建退货仓库列表]start.");
		ReturnInfo<ShopDepotInfo> response = new ReturnInfo<ShopDepotInfo>(Constant.OS_NO);
		try {
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String url = SHOP_USER_SERVICE_URL +"/custom/api/getUserDepotListByOrder.do";
			String back = HttpClientUtil.post(url,paramsList);
			logger.info("[获取创建退货仓库列表接口调用]reponse:" + back);
			if (StringUtil.isEmpty(back)) {
				logger.error("[获取创建退货仓库列表接口调用]FAILURE,reponse:返回结果为空！");
				response.setMessage("[获取创建退货仓库列表接口调用]返回结果为空！");
				return response;
			}
			ShopDepotInfo info = JSON.parseObject(back, ShopDepotInfo.class);
			if (info.getIsOk() && StringUtil.isListNotNull(info.getList())) {
				response.setData(info);
				response.setIsOk(Constant.OS_YES);
				response.setMessage("success");
			} else {
				response.setMessage("[获取创建退货仓库列表接口调用]FAILURE,reponse:用户信息为空！");
				logger.error("[获取创建退货仓库列表接口调用]FAILURE,reponse:用户信息为空！");
			}
		} catch (Exception e) {
			response.setMessage("获取创建退货仓库列表接口调用失败！错误信息:"+e.getMessage());
			logger.error("[获取创建退货仓库列表接口调用]Failed,msg:"+e.getMessage(),e);
		}
		return response;
	}

	/**
	 * 区域管理：区域下拉菜单数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRegionQueryCondition")
	@ResponseBody
	public List<Map<String,String>> getRegionQueryCondition(HttpServletRequest request,
			HttpServletResponse response,String parentId) throws Exception {
		//区域列表
		List<Map<String,String>> regionList = new ArrayList<Map<String,String>>();
		try{
			if(!"".equals(parentId)&&parentId!=null){
				regionList = regionManagementService.getRegionQueryCondition(parentId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return regionList;
		}
	}

	/**
	 * 根据渠道信息查询渠道店铺信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getChannelInfos")
	public ModelAndView getChannelInfos(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		List<String> list = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
		if (!StringUtil.isListNotNull(list)) {
			write("[]", response);
			return null;
		}
		List<CsChannelInfo> siteInfos = new ArrayList<CsChannelInfo>();
		CsChannelInfo channelInfo = new CsChannelInfo();
		channelInfo.setChannelTitle("请选择...");
		siteInfos.add(channelInfo);
		// 查询问题表
		for (String str : list) {
			ReturnInfo<CsChannelInfo> jsonResult = channelInfoService.findSiteInfoBySiteCode(str);
			if (jsonResult == null) {
				write("[]", response);
				return null;
			}
			if (null != jsonResult && jsonResult.getIsOk() == Constant.OS_YES) {
				if (jsonResult.getData() != null) {
					siteInfos.add(jsonResult.getData());
				}
			}
		}
		writeObject(siteInfos, response);
		return null;
	}
	
	/**
	 * 根据渠道信息查询渠道店铺信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getChannelShops")
	public ModelAndView getChannelShops(HttpServletRequest request,
			HttpServletResponse response, String channelCode) throws Exception {
		if (StringUtil.isNull(channelCode)) {
			write("[]", response);
			return null;
		}
		ReturnInfo<List<ChannelShop>> jsonResult  = channelInfoService.findChannelShopByChannelCode(channelCode);
		if (jsonResult == null) {
			write("[]", response);
			return null;
		}
		List<ChannelShop> infos = jsonResult.getData();
		if (StringUtil.isListNotNull(infos)) {
			for (ChannelShop channelShop : infos) {
				channelShop.setShopTitle(channelShop.getShopTitle() + "[" + channelShop.getShopCode() + "]");
			}
		}
		writeObject(infos,response);
		return null;
	}
	
	/**
	 * 公共方法：渠道类型获取
	 * @param dataType 0拉单、1转单、2发货、3退单
	 * @return
	 */
	@RequestMapping(value = "loadOrderShopData")
	@ResponseBody
	public List<OrderShopParam> loadOrderShopData(Integer dataType){
		List<OrderShopParam> list = new ArrayList<OrderShopParam>();
		try{
			list = channelManagerService.loadOrderShopData(dataType);
			for(int i=0;i<list.size();i++){
				logger.info("NO."+i+":shortName:"+list.get(i).getShortName()+":shortText:"+list.get(i).getShortText()+":channelCode:"+list.get(i).getChannelCode());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="logout")
	public void logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("用户退出登录 start");
		ModelAndView mav = new ModelAndView();
		String logout = Config.getAuthCenterUrl() + "/logout";
		HttpSession session = request.getSession();
		if (session != null) {
			session.removeAttribute(Constant.SESSION_USER_KEY);
			session.removeAttribute(Constant.SESSION_ROLE_KEY);
			session.removeAttribute(Constant.SESSION_RES_KEY);
			session.invalidate();
		}
		String userName =UserStore.get(request).getUserName();
		try {
			int userId = UserStore.get(request).getId();
			if (userId != 0) {
				// 订单解锁
				MasterOrderInfo masterOrderInfo = new MasterOrderInfo();
				masterOrderInfo.setLockStatus(0);
				MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
				masterOrderInfoExample.or().andLockStatusEqualTo(userId);
				masterOrderInfoMapper.updateByExampleSelective(masterOrderInfo, masterOrderInfoExample);
				// 退单解锁
				OrderReturn orderReturn = new OrderReturn();
				orderReturn.setLockStatus(0);
				OrderReturnExample orderReturnExample = new OrderReturnExample();
				OrderReturnExample.Criteria orderReturnCriteria = orderReturnExample.or();
				orderReturnCriteria.andLockStatusEqualTo(userId);
				orderReturnMapper.updateByExampleSelective(orderReturn, orderReturnExample);
			}
		} catch (Exception e) {
			logger.error(userName + "IndexController.afterWorkUnlock [ 下班 解锁失败！]");
		}
		response.sendRedirect(logout);
		logger.debug("用户退出登录 end");
	}
	
}
