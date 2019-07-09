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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.AdminUser;
import com.work.shop.oms.bean.ShortageQuestion;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.OrderQuestionSearchResultVO;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.exception.BGSystemException;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.service.ShortageQuestionService;
import com.work.shop.oms.utils.FtpUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.Role;
import com.work.shop.oms.vo.RoleInfo;

@Controller
@RequestMapping(value = "orderQuestion")
public class OrderQuestionController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OrderInfoService orderInfoService;
	
//	@Resource
//	private ShortageQuestionService shortageQuestionService;


	/**
	 * 问题单订单列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderQuestionList.spmvc")
	public ModelAndView orderQuestionQuery(HttpServletRequest request,
			HttpServletResponse response, OrderQuestionSearchVO searchVO, PageHelper helper) throws Exception {
		
		logger.debug("问题单订单列表查询:searchVO=" + searchVO.toString() + ";helper=" + helper.toString());
		
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (StringUtil.isNotNull(method) && method.equals("init")) {
			//返回视图的
			ModelAndView mav = new ModelAndView();

//			// 批量返回正常单

	//			mav.addObject("batchReturnNormal", "1");

//			//手工导入物流问题单

		//		mav.addObject("importLogisticsQuestion", "1");

			// 老版权限控制
	//		com.work.shop.oms.vo.AdminUser adminUser = getLoginUser(request);

	/*		HttpServletRequest hreq = (HttpServletRequest) request;
			HttpSession session = hreq.getSession();
			
			List<SystemOmsResource> list  = (List<SystemOmsResource>) session.getAttribute("order_question_list");
			*/
			int toNormal = 0;
			int addQuestion = 0;
			int batchTaskOutStock = 0;
			int batchTasklogstcQust = 0;
			int batchNormalQuestion = 0;
			List<RoleInfo> ris = new ArrayList<RoleInfo>(0);
	/*
			
			if("admin".equalsIgnoreCase(adminUser.getUserName())){
				toNormal = 1;
				batchTaskOutStock = 1;
				batchTasklogstcQust = 1;
				batchNormalQuestion = 1;

			} else {*/
				
			//	for(SystemOmsResource systemOmsResource:list ){
					// String resourceCode = systemOmsResource.getResourceCode();
					 
				//	 if("order_question_batchReturnNormal".equals(resourceCode)){//问题单列表-订单批量返回正常单
						 toNormal=1;
				//	 }
					 
				//	 if("order_question_batchSetQuestion".equals(resourceCode)){//问题单列表-批量设问题单
						 batchNormalQuestion=1;
				//	 }
					 
				//	 if("order_question_batchSetPick".equals(resourceCode)){//问题单列表-批量导入短拣问题单
						 batchTasklogstcQust=1;
				//	 }
					 
				//	 if("order_question_batchSetMatch".equals(resourceCode)){//问题单列表-批量导入短配问题单
						 batchTaskOutStock=1;
				//	 }
					 
			//	}
				

			//}
		//	if (batchTaskOutStock == 1 || batchTasklogstcQust == 1 || batchNormalQuestion == 1) {
				addQuestion = 1;
		//	}
		//	mav.addObject("ris", JSON.toJSONString(ris));
		/*	mav.addObject("toNormal", toNormal);
			mav.addObject("addQuestion", addQuestion);
			mav.addObject("adminUser", adminUser);*/
			mav.setViewName("orderQuestionList/orderQuestionListPage");

			return mav;
		}
		try {
			searchVO.setStart(helper.getStart());
			searchVO.setLimits(helper.getLimit());
			// 订单来源信息
			Paging paging = this.orderInfoService.getorderQuestionSearchResultVOsPage(searchVO);
			logger.debug("问题单订单列表查询完成！");
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("问题单订单列表查询异常:" + e.getMessage());
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
	@RequestMapping(value = "orderquestionExportCsvFile.spmvc")
	public void orderInfoExportCsvFile(HttpServletRequest request,
			HttpServletResponse response, OrderQuestionSearchVO searchVO, PageHelper helper)throws Exception{
		// 订单来源信息
		com.work.shop.oms.vo.AdminUser adminUser = getLoginUser(request);
		InputStream is = null;
		try{
			searchVO.setLimits(5000);
			Paging paging = this.orderInfoService.getorderQuestionSearchResultVOsPage(searchVO);
			StringBuffer sb = new StringBuffer();
			if (paging.getTotalProperty() > 0) {
				sb.append(//"序号号"+","+
					"订单号"+","+
					"外部交易号"+","+  
					"锁定状态"+","+
					"问题单类型"+","+   
					"问题单原因"+","+
					"订单状态"+","+
					"下单时间"+","+
					"问题单时间"+","+
					"订单类型"+","+
					"订单来源"+","+
					"下单人"+","+
					"总金额"+","+
					"应付金额"+","+
					"客户备注"+","+
					"商家备注"+","+
					"渠道会员等级类型"+","+
					"缺货商品号"+","+	
					"发货仓编码,"+
					"缺货量"+
					
				//	"商品编码|发货仓库编码|交货单编码|缺货原因"+","+  //物流物流单 
					
					"\r\n");
				for (OrderQuestionSearchResultVO orderInfo : (List<OrderQuestionSearchResultVO>)paging.getRoot()) {
			//		String no = orderInfo.getNo() == null ? "" : orderInfo.getNo();
			//		sb.append(no +",");
					
					String masterOrderSn = orderInfo.getMasterOrderSn()== null ? "": "\'" + orderInfo.getMasterOrderSn();	
					sb.append(masterOrderSn +",");	
					
					String orderOutSn = orderInfo.getOrderOutSn() == null ? "": "\'" +orderInfo.getOrderOutSn();
					sb.append(orderOutSn  +",");	
		
					int iLockStatus = orderInfo.getLockStatus();
					String LockUserName = orderInfo.getLockUserName();
					String lockStatus ="";
					if(0==iLockStatus){
						lockStatus="未锁定";
					}else{
						if( 10000 == iLockStatus && (null == LockUserName || ""==LockUserName) ) {
							lockStatus="其他管理员锁定";
						} else {
							lockStatus=LockUserName==null ?"":LockUserName;
						}
					}
					sb.append(lockStatus  +",");
					String questionTypeStr =orderInfo.getQuestionTypeStr()== null ? "":orderInfo.getQuestionTypeStr();
					sb.append(questionTypeStr  +",");
					
					String questionReason =  orderInfo.getQuestionDesc()== null ? "": "\"" +orderInfo.getQuestionDesc()+ "\"";
				
					sb.append(questionReason  +",");
				
					String processStatusStr =  orderInfo.getProcessStatusStr() ==null ? "":   "\"" +orderInfo.getProcessStatusStr() + "\""; 
					sb.append(processStatusStr +",");
					//下单时间
					
					String addTime = orderInfo.getAddTime() == null ? "": TimeUtil.formatDate(orderInfo.getAddTime()) ;
					sb.append(addTime+",");
					//问题单时间
					String questionAddTime = orderInfo.getQuestionAddTime() == null ? "":  TimeUtil.formatDate(orderInfo.getQuestionAddTime());
					sb.append(questionAddTime+",");
					
					String transTypeStr  = orderInfo.getTransTypeStr() == null ? "" :  orderInfo.getTransTypeStr();
					sb.append(transTypeStr  +",");
					
					String channelName =  orderInfo.getChannelName() == null ? "" : orderInfo.getChannelName();
					sb.append(channelName+",");
					
					String userName  = orderInfo.getUserName() == null ? "" :   orderInfo.getUserName();
					sb.append(userName  +",");
					
					BigDecimal totalFee =  orderInfo.getTotalFee()== null ?  new BigDecimal(0.00) : orderInfo.getTotalFee();
					sb.append(totalFee +",");
					
					BigDecimal totalPayable =  orderInfo.getTotalPayable() == null ?  new BigDecimal(0.00) : orderInfo.getTotalPayable();
					sb.append(totalPayable+",");
					
					String CustomerNote =  orderInfo.getCustomerNote()== null ? "" : "\"" + orderInfo.getCustomerNote()+ "\"";
					sb.append(CustomerNote+",");
					String businessNote =  orderInfo.getBusinessNote()== null ? "" :   "\"" + orderInfo.getBusinessNote() + "\"";
					sb.append(businessNote +",");
					
					String useLevelStr = orderInfo.getUseLevelStr()  == null ? "" : orderInfo.getUseLevelStr();
					sb.append(useLevelStr +",");
					
					String customCode = orderInfo.getCustomCode() == null ? "" : orderInfo.getCustomCode();
					sb.append(customCode +",");
					
					String depotCode = orderInfo.getDepotCode()  == null ? "" : orderInfo.getDepotCode();
					sb.append(depotCode +",");
					
					
					Short lackNum = orderInfo.getLackNum()  == null ? (short)0 : orderInfo.getLackNum();
					sb.append(lackNum +",");
					
					// "商品编码|发货仓库编码|交货单编码|缺货原因"+","+  //物流物流单 
				/*	if(1 == orderInfo.getQuestionType()){
						List<ShortageQuestion> list  = orderInfoService.getShortageQuestionList(orderInfo.getOrderSn(),Integer.valueOf(orderInfo.getNo()));		 
						for(ShortageQuestion shortageQuestion :list){
							sb.append(shortageQuestion.getOutStockCode() == null? "" : shortageQuestion.getOutStockCode().trim() + "|");
							sb.append(shortageQuestion.getDepotCode() == null? "" : shortageQuestion.getDepotCode() + "|");
							sb.append(shortageQuestion.getDeliverySn() == null? "" : shortageQuestion.getDeliverySn() + "|");
							sb.append(shortageQuestion.getOutStockReason() == null? "" : shortageQuestion.getOutStockReason());
							sb.append(";");
						}
						sb.append(",");
					}*/
					sb.append("\r\n");
				}
				int total = paging.getTotalProperty();
				int pageNum = total / searchVO.getLimits();
				if (total % searchVO.getLimits() > 0) {
					++pageNum;
				}
				for (int j = 1; j <= pageNum; j++) {
					searchVO.setStart(j *searchVO.getLimits());
					paging = this.orderInfoService.getorderQuestionSearchResultVOsPage(searchVO);
					for (OrderQuestionSearchResultVO orderInfo : (List<OrderQuestionSearchResultVO>)paging.getRoot()) {
			//			String no = orderInfo.getNo() == null ? "" : orderInfo.getNo();
			//			sb.append(no +",");
						
						String masterOrderSn = orderInfo.getMasterOrderSn()== null ? "": "\'" + orderInfo.getMasterOrderSn();	
						sb.append(masterOrderSn +",");	
						
						String orderOutSn = orderInfo.getOrderOutSn() == null ? "":  "\'" +orderInfo.getOrderOutSn();
						sb.append(orderOutSn  +",");	

						int iLockStatus = orderInfo.getLockStatus();
						String LockUserName = orderInfo.getLockUserName();
						String lockStatus ="";	
						if(0==iLockStatus){
							lockStatus="未锁定";
						}else{
							if( 10000 == iLockStatus && (null == LockUserName || ""==LockUserName) ) {
								lockStatus="其他管理员锁定";
							} else {
								lockStatus=LockUserName==null ?"":LockUserName;
							}
				
						}
						sb.append(lockStatus  +",");	
	
						
						String questionTypeStr =orderInfo.getQuestionTypeStr()== null ? "":orderInfo.getQuestionTypeStr();
						sb.append(questionTypeStr  +",");	
						
						String questionReason =  orderInfo.getQuestionDesc()== null ? "": "\"" + orderInfo.getQuestionDesc()+ "\"";
						sb.append(questionReason  +",");		
					
						String processStatusStr =  orderInfo.getProcessStatusStr() ==null ? "":   "\"" +orderInfo.getProcessStatusStr() + "\""; 
						sb.append(processStatusStr +",");		
				
						//下单时间
						
						String addTime = orderInfo.getAddTime() == null ? "": TimeUtil.formatDate(orderInfo.getAddTime()) ;
						sb.append(addTime+",");
						//问题单时间
						String questionAddTime = orderInfo.getQuestionAddTime() == null ? "":  TimeUtil.formatDate(orderInfo.getQuestionAddTime());
						sb.append(questionAddTime+",");
						
						String transTypeStr  = orderInfo.getTransTypeStr() == null ? "" :  orderInfo.getTransTypeStr();
						sb.append(transTypeStr  +",");
						
						String channelName =  orderInfo.getChannelName() == null ? "" : orderInfo.getChannelName();
						sb.append(channelName+",");
						
						String userName  = orderInfo.getUserName() == null ? "" :   orderInfo.getUserName();
						sb.append(userName  +",");
						
						BigDecimal totalFee =  orderInfo.getTotalFee()== null ? new BigDecimal(0.00) : orderInfo.getTotalFee();
						sb.append(totalFee +",");
						
						BigDecimal totalPayable =  orderInfo.getTotalPayable() == null ? new BigDecimal(0.00) : orderInfo.getTotalPayable();
						sb.append(totalPayable+",");
						
						String CustomerNote =  orderInfo.getCustomerNote()== null ? "" :"\"" + orderInfo.getCustomerNote()+ "\"";
						sb.append(CustomerNote+",");
						
						String businessNote =  orderInfo.getBusinessNote()== null ? "" :  "\"" +orderInfo.getBusinessNote()+ "\"";
						sb.append(businessNote +",");
						
						String useLevelStr = orderInfo.getUseLevelStr()  == null ? "" : orderInfo.getUseLevelStr();
						sb.append(useLevelStr +",");
						
						Short lackNum = orderInfo.getLackNum()  == null ? (short)0 : orderInfo.getLackNum();
						sb.append(lackNum +",");
						
				/*		if(1== orderInfo.getQuestionType()){
							 if("1".equals(orderInfo.getLogisticsType())){ //1:缺货问题单,
								 List<ShortageQuestion> list  = orderInfoService.getShortageQuestionList(orderInfo.getOrderSn(),Integer.valueOf(orderInfo.getNo()));
								 sb.append("" +",");
								 for(ShortageQuestion shortageQuestion :list){
									 sb.append("[");
									 sb.append(shortageQuestion.getOutStockCode() == null? "" : shortageQuestion.getOutStockCode());
									 sb.append("|");
									 
									 sb.append(shortageQuestion.getDepotCode() == null? "" : shortageQuestion.getDepotCode());
									 sb.append("|");
									 sb.append(shortageQuestion.getOutStockReason() == null? "" : shortageQuestion.getOutStockReason());
									 
									 sb.append("]");
								 }
								 sb.append(",");
							 }else if("0".equals(orderInfo.getLogisticsType())){//0:一般物流问题单,
								 List<ShortageQuestion> list  = orderInfoService.getShortageQuestionList(orderInfo.getOrderSn(),Integer.valueOf(orderInfo.getNo()));
								 for(ShortageQuestion shortageQuestion :list){
									 sb.append("[");
									 sb.append(shortageQuestion.getOutStockCode() == null? "" : shortageQuestion.getOutStockCode());
									 sb.append("|");
									 sb.append(shortageQuestion.getDeliverySn() == null? "" : shortageQuestion.getDeliverySn() );
									 sb.append("]");
								 }
								 sb.append(",");
								 sb.append("" +",");
							 }
						}*/
						sb.append("\r\n");
					}
				}
			}
			JsonResult jsonResult = new JsonResult();
			try {
				String dateStr = TimeUtil.format3Date(new Date());
				String sfileName ="orderQeustionList"+dateStr+".csv";
				String fileName = tempFileFolder + "/" +  sfileName;
				File fileRoot = new File(request.getSession().getServletContext().getRealPath("/") + tempFileFolder);
				if(!fileRoot.exists()){
					fileRoot.mkdirs();
				}
				String path = request.getSession().getServletContext().getRealPath("/") + fileName;
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
				writer.write(sb.toString());
				writer.close();
				is = new FileInputStream(path);
				String ftpFileName = StringUtil.fileNameSpliceCsv("OrderQuestion", adminUser.getUserName());
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
				//删除临时文件
				File delFile = new File(path);
				if (delFile.isFile()) {
					delFile.delete();
				}
			} catch (Exception e) {
				logger.info("导出查询问题单数据上传FTP异常：", e);
			}
			finally{
				if(is != null) {
					try {
						is.close();
					} catch(Exception e){
						logger.info("导出查询问题单关闭数据流异常：", e);
					}
				}
			}
		}catch (Exception e) {
			logger.info("导出查询问题单数据异常：", e);
		}
		logger.debug("导出查询问题单数据结束！");	
	}
	

	
	private Short Short(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 跳转至导入问题单页面
	 * 
	 **/
	@RequestMapping(value = "gotoImportQuestionPage")
	public ModelAndView gotoImportQuestionPage(HttpServletRequest request, HttpServletResponse response,
			String orderSn) throws Exception {
		ModelAndView mav = new ModelAndView();
		com.work.shop.oms.vo.AdminUser adminUser = getLoginUser(request);
		
/*		Role role = new Role(adminUser.getRoles());
		String rolesString = "";
		if(Role.ORDER_ROLE_STRING.length() < 20){
			Role.ORDER_ROLE_STRING = orderUserRoleService.initRoleString();
		}
		rolesString = Role.ORDER_ROLE_STRING;
		String roleString = role.getOrderRole(rolesString);*/
		
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpSession session = hreq.getSession();
		
		List<SystemOmsResource> list  = (List<SystemOmsResource>) session.getAttribute("order_question_list");
		
		int batchTaskOutStock = 0;
		int batchTasklogstcQust = 0;
		int batchNormalQuestion = 0;
		
		if("admin".equalsIgnoreCase(adminUser.getUserName())){
			batchTaskOutStock = 1;
			batchTasklogstcQust = 1;
			batchNormalQuestion = 1;
		} else {
			
			for(SystemOmsResource systemOmsResource:list ){
				 String resourceCode = systemOmsResource.getResourceCode();
				 
			/*	 if("order_question_batchReturnNormal".equals(resourceCode)){//问题单列表-订单批量返回正常单
					 toNormal=1;
				 }*/
				 
				 if("order_question_batchSetQuestion".equals(resourceCode)){//问题单列表-批量设问题单
					 batchNormalQuestion=1;
				 }
				 
				 if("order_question_batchSetPick".equals(resourceCode)){//问题单列表-批量导入短拣问题单
					 batchTasklogstcQust=1;
				 }
				 
				 if("order_question_batchSetMatch".equals(resourceCode)){//问题单列表-批量导入短配问题单
					 batchTaskOutStock=1;
				 }
				 
			}
			
		/*	if (StringUtil.isNotNull(roleString)) {
				try {
					if (roleString.length() >= 38) {
						batchTaskOutStock = Integer.valueOf(roleString.substring(38, 39));
					}
					if (roleString.length() >= 39) {
						batchTasklogstcQust = Integer.valueOf(roleString.substring(39, 40));
					}
					if (roleString.length() >= 41) {
						batchNormalQuestion = Integer.valueOf(roleString.substring(41, 42));
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("获取权限异常：", e);
				}
			}*/
			
		}
		if (batchTaskOutStock == 0 && batchTasklogstcQust == 0 && batchNormalQuestion == 0) {
			throw new BGSystemException("你没有批量设问题单的权限，请联系管理员！");
		}
		mav.addObject("batchNormalQuestion", batchNormalQuestion);
		mav.addObject("batchTaskOutStock", batchTaskOutStock);
		mav.addObject("batchTasklogstcQust", batchTasklogstcQust);
		mav.setViewName("orderQuestion/importOrderLogisticsQuestion");
		return mav;
	}
	
	
	/**
	 * 跳转至导入问题单页面
	 * 
	 **/
	@RequestMapping(value = "gotoImportQuestionPage1")
	public ModelAndView gotoImportQuestionPage1(HttpServletRequest request, HttpServletResponse response,
			String orderSn) throws Exception {
	
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		com.work.shop.oms.vo.AdminUser adminUser = getLoginUser(request);
		
	/*	HttpServletRequest hreq = (HttpServletRequest) request;
		HttpSession session = hreq.getSession();
	
		List<SystemOmsResource> list  = (List<SystemOmsResource>) session.getAttribute("order_question_list");*/
		
		int batchTaskOutStock = 0;
		int batchTasklogstcQust = 0;
		int batchNormalQuestion = 0;
		
	//	if("admin".equalsIgnoreCase(adminUser.getUserName())){
			batchTaskOutStock = 1;
			batchTasklogstcQust = 1;
			batchNormalQuestion = 1;
//		} else {
			
		/*	for(SystemOmsResource systemOmsResource:list ){
				 String resourceCode = systemOmsResource.getResourceCode();
				 
				 if("order_question_batchSetQuestion".equals(resourceCode)){//问题单列表-批量设问题单
*/					 batchNormalQuestion=1;
			/*	 }
				 
				 if("order_question_batchSetPick".equals(resourceCode)){//问题单列表-批量导入短拣问题单
*/					 batchTasklogstcQust=1;
			/*	 }
				 
				 if("order_question_batchSetMatch".equals(resourceCode)){//问题单列表-批量导入短配问题单
*/					 batchTaskOutStock=1;
		/*		 }
				 
			}*/
		
	//	}
		
/*		if (batchTaskOutStock == 0 && batchTasklogstcQust == 0 && batchNormalQuestion == 0) {
			throw new BGSystemException("你没有批量设问题单的权限，请联系管理员！");
		}*/
	
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("batchNormalQuestion", batchNormalQuestion);
		map.put("batchTasklogstcQust", batchTasklogstcQust);
		map.put("batchTaskOutStock", batchTaskOutStock);

		jsonResult.setData(map);
		jsonResult.setIsok(true);
		writeObject(jsonResult, response);
		return null;
	}
	
	
	@Resource
	private ShortageQuestionService shortageQuestionService;
	
	/**
	 *根据订单号查询缺货商品;
	 *@author chenrui;
	 *@param String orderSn
	 ***/
	@RequestMapping(value = "getShortageQuestionList.spmvc")
	public ModelAndView getShortageQuestionList(HttpServletRequest request,
			HttpServletResponse response,OrderQuestionSearchVO searchVO) throws Exception {
		if(!StringUtil.isNotNull(searchVO.getOrderSn())){
			Paging paging = new Paging(0, null);
			writeJson(paging, response);
			return null;
		}
		Paging paging = shortageQuestionService.getShortageQuestionList(searchVO);
		writeJson(paging, response);
		return null;
	}
	

	
}
