package com.work.shop.oms.controller;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.bean.GoodsReturnChangeExample;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.MasterOrderInfoVO;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.order.service.GoodsReturnChangeService;
import com.work.shop.oms.service.GoodsReturnChangePageService;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.AdminUser;

@Controller
@RequestMapping(value = "goodsReturnChange")
public class GoodsReturnChangeController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private GoodsReturnChangePageService goodsReturnChangePageService;

	@Resource
	private OrderInfoService orderInfoService;
	
	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;
	
	@Resource(name = "goodsReturnChangeService")
	private GoodsReturnChangeService goodsReturnChangeService;

	/**
	 * 申请单列表查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "goodsReturnChangeList.spmvc")
	public ModelAndView goodsReturnChangePage(HttpServletRequest request,
			HttpServletResponse response, GoodsReturnChangeVO goodsReturnChangeVO, PageHelper helper) throws Exception {
		logger.debug("退换货申请单列表查询 start");
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("goodsReturnChange/goodsReturnChangeList");
			return mav;
		}
		try {
			HttpSession session = request.getSession();
			List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
			if (!StringUtil.isListNotNull(strings)) {
				return null;
			}
			goodsReturnChangeVO.setSites(strings);
			Paging paging = goodsReturnChangePageService.getPageList(goodsReturnChangeVO, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			logger.error("退换货申请单列表查询异常", e);
		}
		logger.debug("退换货申请单列表查询  end");
		return null;
	}
	
	/**
	 * 申请单csv导出
	 * @param request
	 * @param response
	 * @param orderInfoSerach
	 * @throws Exception
	 */
	@RequestMapping(value = "goodsReturnChangeExportCsvFile.spmvc")
	public void orderInfoExportCsvFile(HttpServletRequest request,
			HttpServletResponse response, GoodsReturnChangeVO goodsReturnChangeVO)throws Exception{
		JsonResult jsonResult = new JsonResult();
		HttpSession session = request.getSession();
		List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
		if (!StringUtil.isListNotNull(strings)) {
			writeObject(jsonResult, response);
		}
		goodsReturnChangeVO.setSites(strings);
		List<GoodsReturnChangeBean> list= goodsReturnChangePageService.getList(goodsReturnChangeVO);
		StringBuffer sb = new StringBuffer();
		sb.append("关联订单号"+","+"申请单id"+","+"处理状态"+","+"关联退单号"+","+"关联退款单号"+","+"交易类型"+","+"订单来源"+","+"申请单号"+","+"退换类型"+","+"下单人"+","+"收货人"+","
		+"订单金额"+","+"退换货商品码"+","+"货物数量"+","+"退换原因"+","+"退换说明"+","+"换购选择类型"+","+"吊牌情况"+","+"外观情况"+","+"赠品情况"+","+"创建时间"+"\r\n");
		for (GoodsReturnChangeBean changeVO :list){
			sb.append("\t"+changeVO.getOrderSn()+","+changeVO.getId()+","+changeVO.getStatusStr()+","+changeVO.getReturnSn()+","
					+changeVO.getReturnPaySn()+","+changeVO.getTransTypeStr()+","+changeVO.getShopName()
					+","+changeVO.getReturnchangeSn()+","+changeVO.getReturnTypeStr()+","+changeVO.getUserId()+","+changeVO.getContactName()+
					","+changeVO.getTotalFee()+","+"\t"+changeVO.getSkuSn()+","+changeVO.getReturnSum()+","+changeVO.getReasonStr()+","
					+changeVO.getExplain()+","+changeVO.getRedemptionStr()+","+changeVO.getTagTypeStr()+","+changeVO.getExteriorTypeStr()+","
					+changeVO.getGiftTypeStr()+","+"\t"+TimeUtil.formatDate(changeVO.getCreate())+"\r\n");
		}
		try {
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "grcTempFile"; 
			String fileName = folderName + "/goodsReturnChangeList"+dateStr+".csv";
			File fileRoot=new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			if(!fileRoot.exists()){
				fileRoot.mkdirs();
			}
			String path=request.getSession().getServletContext().getRealPath("/") + fileName;
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path),"GBK");
			BufferedWriter writer=new BufferedWriter(write);
			writer.write(sb.toString());
			writer.close();
			jsonResult.setIsok(true);
			jsonResult.setData(fileName);
			writeObject(jsonResult, response);
		} catch (Exception e) {
			logger.error("退换货申请单查询导出异常", e);
		}
	}
	
	
	/**
	 * 跳转到申请退单详情页面
	 * @param request
	 * @param response
	 * @param orderSn
	 * @param isHistory
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "returnChangePage")
	public ModelAndView returnChangePage(HttpServletRequest request,HttpServletResponse response, String orderSn, Integer isHistory, Integer id) {
		logger.info("申请退单id："+id+"orderSn=" + orderSn + "  isHistory="+ isHistory);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("goodsReturnChange/goodsReturnChange");
		mav.addObject("id", id);
		mav.addObject("orderSn", orderSn);
		mav.addObject("isHistory", isHistory);
		return mav;
	}

	/**
	 * 退换货申请单信息(新)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "newReturnChangeInfo")
	public ModelAndView newReturnChangeInfo(HttpServletRequest request,
			HttpServletResponse response, String orderSn, Integer isHistory, Integer id) throws Exception {
		logger.info("orderSn=" + orderSn + "  isHistory="+ isHistory);
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("success", false);
		try {
			// 获取用户信息
			AdminUser adminUser = getLoginUser(request);
			if (StringUtil.isEmpty(orderSn)) {
				map.put("message", "订单编号不能为空！");
				outPrintJson(response, map);
				return null;
			}
		/*	OrderInfoVO infoVO = this.orderInfoService.getOrderDetail(orderSn.trim(),
					isHistory, adminUser);*/
					
			MasterOrderInfoVO infoVO = this.orderInfoService.getOrderDetail(adminUser,orderSn.trim(),isHistory);
			
			if (null == infoVO) {
				logger.error("该订单["+orderSn+"]不存在！");
				map.put("success", false);
				map.put("message", "该订单["+orderSn+"]不存在！");
			}else{
				List<GoodsReturnChangeAction> logList=null;
				List<GoodsReturnChangeInfoVO> goodsReturnChangeList = goodsReturnChangePageService.findGoodsReturnChangeBySn(orderSn);
				GoodsReturnChange goodsReturnChange=goodsReturnChangePageService.findGoodsReturnChangeById(id);
				if(null!=goodsReturnChange){
					if(StringUtil.isNull(goodsReturnChange.getContactMobile())){
						goodsReturnChange.setContactMobile("");
					}else{
						goodsReturnChange.setContactMobile(goodsReturnChange.getContactMobile());
					}
					if(StringUtil.isNotBlank(goodsReturnChange.getReturnchangeSn())){
						logList=goodsReturnChangePageService.findActionByReturnChangeSn(goodsReturnChange.getReturnchangeSn()); 
					}
				}
				map.put("success", true);
				map.put("logList", logList);
				map.put("goodsReturnChangeList", goodsReturnChangeList);
				map.put("goodsReturnChange",goodsReturnChange);
				map.put("message", "查询申请单详情成功");
				
				/*
				map.put("result", infoVO.getCommon());
				map.put("goodDetail", infoVO.getOrderGoods());
				map.put("payDetail", infoVO.getOrderPaysVo());*/
				
				map.put("result", infoVO.getMasterOrderInfo());
				map.put("goodDetail", infoVO.getMergedMasOrdGoodsDetailList());
				map.put("payDetail", infoVO.getMasterOrderPayTypeDetailList());
				
			}
			
		} catch (Exception e) {
			logger.error("查询订单详情,orderSn=" +orderSn +"异常：", e);
			map.put("message","查询订单详情,orderSn=" +orderSn +"异常");
		}
		outPrintJson(response, map);
		return null;
	}
	
	/**
	 * 批量修改状态 
	 * @param type
	 * @param request
	 * @param response
	 * @param ids
	 * @throws Exception
	 */
	@RequestMapping(value = "updateStatusBatch.spmvc")
	public void updateStatusBatch(HttpServletRequest request,HttpServletResponse response,Integer type,Integer[] ids) throws Exception {
		try {
//			SystemAdminUser adminUser = getLoginUser(request);
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				writeMsgSucc(false, "登录用户信息不存在！", response);
				return;
			}
			ReturnInfo returnInfo=goodsReturnChangePageService.updateStatusBatch(type, ids,adminUser.getUserName());
			if(returnInfo.getIsOk()==0){
				writeMsgSucc(false,returnInfo.getMessage(), response);
			}else{
			writeMsgSucc(true, "", response);
			}
		} catch (Exception e) {
			writeMsgSucc(false, "修改失败！"+e.toString(), response);
			logger.error("退换货申请单批量修改状态  异常", e);
		}
	}
	
	/**
	 * 获取退换货上传图片列表
	 * @param request
	 * @param response
	 * @param goodsReturnChangeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getReturnChangeImageList")
	public ModelAndView getReturnChangeImageList(HttpServletRequest request,HttpServletResponse response, String orderSn) throws Exception {
		logger.info("getReturnChangeImageList.orderSn="+ orderSn);
		Map<String,Object>data = new HashMap<String, Object>();
		try {
			List<String> returnChangeImageList = new ArrayList<String>();
			if(StringUtil.isTrimEmpty(orderSn)){
				data.put("success", false);
				data.put("errorMessage", "订单号不可为空！");
			}else{
				GoodsReturnChangeExample example = new GoodsReturnChangeExample();
				example.or().andOrderSnEqualTo(orderSn);
				List<GoodsReturnChange> list = goodsReturnChangeMapper.selectByExample(example);
				if(list==null){
					data.put("success", false);
					data.put("errorMessage", "查询不到订单信息！");
				}else{
					for(GoodsReturnChange bean : list){
						String urls = bean.getReturnGoodsImg();
						if(StringUtil.isNotEmpty(urls)){
							String[] array = urls.split(";");
							for(int j=0;j<array.length;j++){
								if(StringUtil.isNotEmpty(array[j])){
									returnChangeImageList.add(array[j]);
								}
							}
						}
					}
					data.put("returnChangeImageList", returnChangeImageList);
					data.put("success", true);
					data.put("errorMessage", "获取退单图片成功！");
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			data.put("success", false);
			data.put("errorMessage", "获取获取退换货图片信息加载错误，"+e.getMessage());
			logger.error("获取获换取退货图片信息,orderSn=" +orderSn +"异常：" + e.getMessage());
		}
		outPrintJson(response, data);
		return null;
	}
	
	/**
	 * 申请单详情页面中跟新和全部跟新申请单状态
	 * @param request
	 * @param response
	 * @param id
	 * @param type 0：全部更新 1：更新
	 * @throws Exception 
	 */
	@RequestMapping(value = "updateProcessStatus.spmvc")
	public void updateProcessStatus(HttpServletRequest request,HttpServletResponse response,Integer id,String orderSn,Integer status,String type,String actionNote) throws Exception{
		// 获取用户信息
		AdminUser adminUser = getLoginUser(request);
		if (null == adminUser) {
			writeMsgSucc(false, "登录用户信息不存在！", response);
			return;
		}
		ReturnInfo returnInfo=new ReturnInfo();
		try {
			if(type.equals("0")){
				List<GoodsReturnChange> list= goodsReturnChangePageService.findGoodsReturnChangeByOrderSn(orderSn);
				for(GoodsReturnChange goodsReturnChange:list){
					returnInfo=goodsReturnChangeService.updateStatus(status,goodsReturnChange.getId(),orderSn,adminUser.getUserName(),"全部更新");
				}
			}else if(type.equals("1")){
				returnInfo=goodsReturnChangeService.updateStatus(status,id,orderSn,adminUser.getUserName(),"更新");
			}else if(type.equals("2")){
				returnInfo=goodsReturnChangeService.updateStatus(null,id,orderSn,adminUser.getUserName(),actionNote);
			}
			if(returnInfo!=null){
				if(returnInfo.getIsOk()==1){
					writeMsgSucc(true, "", response);
				} else {
					writeMsgSucc(false, "修改失败！"+returnInfo.getMessage(), response);
				}
			}else{
				writeMsgSucc(false, "修改失败！没有返回信息！", response);
			}
		} catch (Exception e) {
			writeMsgSucc(false, "修改失败！"+e.toString(), response);
			logger.error("申请单详情页面中跟新和全部跟新申请单状态异常", e);
		}
	}
}
