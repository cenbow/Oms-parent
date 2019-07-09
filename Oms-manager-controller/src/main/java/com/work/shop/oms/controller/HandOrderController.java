package com.work.shop.oms.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.HandOrderInfo;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.HandOrderBatchVo;
import com.work.shop.oms.common.bean.HandOrderInfoVo;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.orderop.service.HandOrderService;
import com.work.shop.oms.orderop.service.ShopUserService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.FtpUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.Pagination;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.united.client.dataobject.User;

@Controller
@RequestMapping(value = "handOrder")
public class HandOrderController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="handOrderServiceImpl")
	private HandOrderService handOrderService;
	@Resource
	private ChannelInfoService channelInfoService;
	@Resource
	private ShopUserService shopUserService;
	
	/**
	 * 订单列表查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "handOrderBatchList")
	public ModelAndView handOrderBatchList(HttpServletRequest request,
			HttpServletResponse response, HandOrderBatchVo model, PageHelper helper, String method, String type) throws Exception {
		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("orderInfoList/orderInfoListPage");
			return mav;
		}
		try {
			Paging paging = this.handOrderService.getHardOrderPage(model, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			logger.error("查询订单列表异常" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取线上渠道店铺列表
	 * @param isActive
	 * @param shopType
	 * @return
	 */
	@RequestMapping(value="getOnlineChannel")
	@ResponseBody
	public List<ChannelShop> getOnlineChannel(){
		try {
			ReturnInfo<List<ChannelShop>> info = channelInfoService.findChannelShopByChannelCode(Constant.Chlitina);
			if (null != info && info.getIsOk() == Constant.OS_YES) {
				return info.getData();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("查询[" + Constant.Chlitina + "] 店铺列表异常：" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取第三方导入清单指定批次清单列表
	 * @param billNo
	 * @return
	 */
	@RequestMapping(value="getThirdPartyOrderDetailList")
	@ResponseBody
	public Paging getThirdPartyOrderDetailList(String batchNo, PageHelper helper){
		return handOrderService.getThirdPartyOrderDetailList(batchNo,helper);
	}
	
	/**
	 * 从excel读取批量导入数据
	 * @param request
	 * @param myfile
	 * @param note
	 * @param channelCode
	 * @param channelName
	 * @return
	 */
	@RequestMapping(value="doImport.spmvc", headers = "content-type=multipart/*")
	@ResponseBody
	public Map doImport(HttpServletRequest request,@RequestParam MultipartFile myfile,
			String note,String channelCode,String channelName, Integer sourceType){
		//获取登录者信息
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
		String userName = user.getUserName();
		String code = "1";//0:成功 1：失败
		String msg = "提交导入数据失败！";
		boolean success = false;
		if("".equals(userName)||userName==null){
			msg = "失败！登录失效！请重新登录！";
			throw new RuntimeException("用户登录信息不存在！");
		}
		ReturnInfo<List<HandOrderInfoVo>> info = handOrderService.doImport(myfile,note,channelCode,channelName, sourceType);
		if (info != null && info.getIsOk() == Constant.OS_YES) {
			msg = "提交成功！";
			/*if(!"".equals(info.getData())){
				msg+="调整单号为：" + info.getData();
			}*/
			code = "0";
			success = true;
		} else {
			msg += info.getMessage();
		}
		map.put("channelCode", channelCode);
		map.put("channelName", channelName);
		map.put("success", success);
		map.put("code", code);
		map.put("msg", msg);
		map.put("batchList", info.getData());
		return map;
	}
	/**
	 * 提交导入第三方导入订单数据
	 * @param list
	 * @return
	 */
	@RequestMapping(value="submitImport")
	@ResponseBody
	public Map submitImport(HttpServletRequest request,HttpServletResponse response,
			String paramsJson){
		//获取登录者信息
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
		String userName = user.getUserName();
		String code = "1";//0:成功 1：失败
		String msg = "提交导入数据失败！";
		boolean success = false;
		if("".equals(userName)||userName==null){
			msg = "失败！登录失效！请重新登录！";
			throw new RuntimeException("用户登录信息不存在！");
		}
		if (StringUtil.isTrimEmpty(paramsJson)) {
			map.put("success", success);
			map.put("code", code);
			map.put("msg", "提交数据为空");
			return map;
		}
		List<HandOrderInfoVo> infoVos = JSON.parseArray(paramsJson, HandOrderInfoVo.class);
		ReturnInfo<String> info = handOrderService.submitImport(infoVos, userName);
		if (info != null && info.getIsOk() == Constant.OS_YES) {
			msg = "提交成功！";
			if(!"".equals(info.getData())){
				msg+="调整单号为：" + info.getData();
			}
			code = "0";
			success = true;
		} else {
			msg += info.getMessage();
		}
		map.put("success", success);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 提交导入第三方导入订单数据
	 * @param list
	 * @return
	 */
	@RequestMapping(value="shopHandOrderCheck")
	@ResponseBody
	public void shopHandOrderCheck(HttpServletRequest request,HttpServletResponse response,
			String userId){
		//获取登录者信息
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
		String userName = user.getUserName();
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if("".equals(userName)||userName==null){
			throw new RuntimeException("用户登录信息不存在！");
		}
		info.setData(userName);
		if (StringUtil.isTrimEmpty(userId)) {
			info.setMessage("用户ID不能为空");
			write(JSON.toJSONString(info), response);
			return;
		}
		try {
			ReturnInfo<ShopUserInfo> tInfo = shopUserService.getUserCreateOrderInfo(userId);
			if (tInfo==null || tInfo.getIsOk() == Constant.OS_NO) {
				info.setMessage("用户信息不存在");
			} else {
				info.setIsOk(Constant.OS_YES);
				info.setMessage("检查结束");
			}
		} catch (Exception e) {
			logger.error("校验用户[" + userId + "]异常:" + e.getMessage(), e);
			info.setMessage("校验用户[" + userId + "]异常:" + e.getMessage());
		}
		write(JSON.toJSONString(info), response);
	}
	
	/**
	 * 提交导入第三方导入订单数据
	 * @param list
	 * @return
	 */
	@RequestMapping(value="downloadRecord")
	public void downloadRecord(HttpServletRequest request,HttpServletResponse response,
			String batchNo){
		//获取登录者信息
		Map<String,Object> map = new HashMap<String, Object>();
		PageHelper helper = new PageHelper();
		BufferedWriter writer = null;
		AdminUser adminUser = getLoginUser(request);
		logger.info("订单信息导出userId：" + adminUser.getUserName() + " start");
		int pageSize = 5000;
		StringBuffer sb = new StringBuffer();
		Pagination pagination = new Pagination(1, pageSize);
		helper.setLimit(pageSize);
		String path = null;
		File fileRoot= null;
		String sfileName = null;
		try{
			// 创建本地文件
			String dateStr = TimeUtil.format3Date(new Date());
			String folderName = "hoTempFile";
			sfileName ="handOrderList" + dateStr+".csv";
			String fileName = folderName + "/"+sfileName;
			fileRoot=new File(request.getSession().getServletContext().getRealPath("/") + folderName);
			if(!fileRoot.exists()) {
				fileRoot.mkdirs();
			}
			path = request.getSession().getServletContext().getRealPath("/") + fileName;
			//财务
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
			Paging paging = this.handOrderService.getThirdPartyOrderDetailList(batchNo,helper);

			List<HandOrderInfo> list = (List<HandOrderInfo>) paging.getRoot();
			pagination.setTotalSize(paging.getTotalProperty());
			sb.append(
				"批次号," +
				"订单号," +
				"用户名," + 
				"打单类型," +
				"是否打单," +
				"处理结果" +
				"\r\n"
			);
			writer.write(sb.toString());
			writer.flush();
			if (paging.getTotalProperty() > 0) {
				//第一页
				exportHandOrder(list, writer);
				//第二页及后续页
				for (int j = 2; j <= pagination.getTotalPages(); j++) {
					pagination.setCurrentPage(j);
					helper.setStart(pagination.getStartRow());
					paging = this.handOrderService.getThirdPartyOrderDetailList(batchNo,helper);;
					exportHandOrder((List<HandOrderInfo>) paging.getRoot(), writer);
				}
			}
			
		}catch (Exception e) {
			logger.error("订单信息导出异常", e);
		} finally{
			if(writer != null){
				try {
					writer.close();
				} catch (Exception e2) {
					logger.error("关闭流文件【BufferedWriter】异常", e2);
				}
				writer = null;
			}
		}
		JsonResult jsonResult = new JsonResult();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			String ftpFileName = StringUtil.fileNameSpliceCsv("handorder", adminUser.getUserName());
			String tFtpPath = ftpRootPath + "/"+ TimeUtil.format2Date(new Date());
			HashMap<String, Object> soMap = FtpUtil.uploadFile(ftpFileName, is, tFtpPath +"/");
			if((Boolean)soMap.get("isok")) {
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
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch(Exception e){
					logger.error("关闭流文件【InputStream】异常", e);
				}
			}
		}
	}
	
	/**
	 * 手工打单导出 
	 * @param List<HandOrderInfo> list
	 ***/
	private void exportHandOrder(List<HandOrderInfo> list, BufferedWriter writer) {
		logger.info("exportHandOrder start ..........." + JSON.toJSONString(list) );
		if (!StringUtil.isListNotNull(list)) {
			return ;
		}
		try {
			StringBuffer buffer = new StringBuffer();
			for (HandOrderInfo orderInfo : list) {
				String batchNo = StringUtil.isNull(orderInfo.getBatchNo()) ? "" : "\'" + orderInfo.getBatchNo(); //批次号,
				String masterOrderSn = orderInfo.getMasterOrderSn() == null ?"": "\'" +orderInfo.getMasterOrderSn();//主订单号
				String userId = orderInfo.getUserId() == null ?"": "\'" +orderInfo.getUserId();//用户名
				String isOk = "";
				if (orderInfo.getIsOk() == null || orderInfo.getIsOk().intValue() == 0) {
					isOk = "未打单";
				} else if (orderInfo.getIsOk().intValue() == 1) {
					isOk = "打单成功";
				} else if (orderInfo.getIsOk().intValue() == 2) {
					isOk = "打单失败";
				}
				
				String sourceType = "一般赠品订单";
				if (orderInfo.getSourceType().intValue() == 4) {
					sourceType = "首购赠品订单";
				}
				String processMessage = StringUtil.isTrimEmpty(orderInfo.getProcessMessage()) ? "" : "\"" +  orderInfo.getProcessMessage()+ "\"";
				buffer.append(
						batchNo + ","+
						masterOrderSn+","+
						userId + "," +
						sourceType + "," +
						isOk + "," +
						processMessage +
						"\r\n");
				writer.write(buffer.toString());
				writer.flush();
				buffer = new StringBuffer();
			}
		} catch (Exception e) {
			logger.error("查询内容写入CSV文件异常", e);
		}
		logger.info("exportHandOrder end ...........");
	}
}
