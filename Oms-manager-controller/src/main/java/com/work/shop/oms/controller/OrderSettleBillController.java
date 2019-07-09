package com.work.shop.oms.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderBillListVo;
import com.work.shop.oms.bean.OrderSettleBill;
import com.work.shop.oms.bean.OrderSettleBillExample;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.ChannelShop;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.OrderMoney;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.dao.OrderSettleBillMapper;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.service.OrderSettleBillService;
import com.work.shop.oms.utils.ConstantsUtil;
import com.work.shop.oms.utils.FtpUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.SettleBillQueue;

@Controller
@RequestMapping(value = "orderSettleBill")
public class OrderSettleBillController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "processOrderSettleBillJmsTemplate")
	private JmsTemplate jmsTemplate;

/*	@Resource(name = "processOrderMoneyJmsContainer")
	private JmsTemplate orderMoneyjmsTemplate;*/
		
	@Resource
	private OrderInfoService orderInfoService;
	
	@Resource
	private OrderSettleBillService orderSettleBillService;
	
	@Resource
	private OrderSettleBillMapper orderSettleBillMapper;
	
	@Resource
	private OrderBillListMapper orderBillListMapper;
	
	/**
	 * 订单结算列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderBillList.spmvc")
	public ModelAndView orderSettleBillList(HttpServletRequest request,
			HttpServletResponse response, OrderBillListVo orderSettleBill, PageHelper helper, String method) throws Exception {

		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			//会话中权限
//			Map<String, String>  map = (Map<String, String>)request.getSession().getAttribute(Constant.SYSTEM_RESOURCE);
			//详情
			mav.setViewName("orderTicket/orderTicket");
			return mav;
		}
		try {
			//Paging paging = null;
			
			Paging paging =  orderSettleBillService.getOrderBillList(orderSettleBill, helper);
		//	Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}

	/**
	 * 进入调整页面 
	 ***/
	@RequestMapping(value = "createOrderSettleBill.spmvc")
	public ModelAndView createOrderSettleBill(HttpServletRequest request,
				HttpServletResponse response, OrderSettleBill orderSettleBill,String channelCode, PageHelper helper, String method,String orderReturnFlag) throws Exception {
			ModelAndView mav = new ModelAndView();
			if (StringUtil.isNotNull(method) && method.equals("init")) {
				
				if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
					mav.addObject("billNo", orderSettleBill.getBillNo());
				}else{
					String billNo = ConstantsUtil.getSettleBillCode();
					//System.out.println("settleBillCode=    "+settleBillCode);
					mav.addObject("billNo", billNo);
				}
				mav.addObject("isDisabled", "1");//显示
				if(StringUtil.isNotBlank(orderReturnFlag)){
					mav.setViewName("orderReturn/addOrderReturnTicket");
				}else{
					mav.setViewName("orderTicket/addOrderTicket");
				}
				return mav;
			}else if( StringUtil.isNotNull(method) && method.equals("update")){
				if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
	
				//	List<OrderSettleBill> list = orderSettleBillService.selectOrderSettleBill(orderSettleBill, null);
					
				//	if(StringUtil.isNotNullForList(list)){
					mav.addObject("isDisabled", "0");//置灰
				//	}
					mav.addObject("billNo", orderSettleBill.getBillNo());
					
				    
//					String result =	channelShopApiService.findShopInfoByShopCode(channelCode, 1);
					/*JsonResult jsonResult = channelShopApiService.findShopInfoByShopCode(channelCode, 1);
					if (jsonResult != null) {
//					jsonResult = JSONObject.parseObject(result, JsonResult.class);
						if(jsonResult.isIsok()){
							ChannelShop channelShop = (ChannelShop)jsonResult.getData();
							
							mav.addObject("channelCode", channelShop.getChannelCode());//二级	
							mav.addObject("shopCode", channelCode);//三级
							
							String orderFromFirstStr  = channgeOrderFromFirst(channelShop);	
							mav.addObject("orderFromFirst", orderFromFirstStr);//一级
						}
					}	*/
					
				}
	
				if(StringUtil.isNotBlank(orderReturnFlag)){
					mav.setViewName("orderReturn/addOrderReturnTicket");
				}else{
					mav.setViewName("orderTicket/addOrderTicket");
				}
				return mav;
			}
			
			try {
				Paging paging =  orderSettleBillService.getOrderSettleBill(orderSettleBill, helper);
				writeJson(paging, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
	 }
	
	/***
	 * 导入 调整页面 
	 **/
	@RequestMapping(value = "exportTicket.spmvc", headers = "content-type=multipart/*")
	public ModelAndView exportTicket(HttpServletRequest request,
				HttpServletResponse response,  @RequestParam MultipartFile myfile, 
				int orderCodeType, Byte shippingId, Integer billType, String billNo, String channelCode ,String note) throws Exception {
			InputStream inputis = null;
			StringBuffer sb = new StringBuffer("");
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			List<OrderSettleBill> list = new ArrayList<OrderSettleBill>();
			try {
				inputis = myfile.getInputStream();
				list = orderSettleBillService.importOrderSettle(inputis, sb, orderCodeType, billNo,shippingId);
				if(StringUtil.isNotNullForList(list)){
					for(OrderSettleBill orderSettleBill : list){
						int count = orderSettleBillMapper.insertSelective(orderSettleBill);
					}
					OrderBillList orderBillList = new OrderBillList();
					orderBillList.setActionUser(adminUser.getUserName());
					orderBillList.setAddTime(new Date());
					orderBillList.setBillNo(billNo);
					orderBillList.setBillType(orderCodeType);
					orderBillList.setBillType(billType);
					orderBillList.setIsSync((byte)0);
					if(StringUtil.isNotBlank(channelCode)){
						orderBillList.setChannelCode(channelCode);
					}
					if(StringUtil.isNotBlank(note)){
						orderBillList.setNote(note);
					}
					orderBillListMapper.insertSelective(orderBillList);
					writeMsgSucc(true,"导入成功！",  response);
					return null;
				}
			}catch (Exception e){
				e.printStackTrace();
				writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			} finally {
				if (inputis != null) {
					try {
						inputis.close();
						inputis = null;
					} catch (IOException e) {
						logger.error("exportTicket 关闭写入文件异常", e);
					}
				}
			}
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
	 }
	
	/***
	 *跳转导入订单结算 
	 ****/
	@RequestMapping(value = "enterExportTicketPage.spmvc")
	public ModelAndView enterExportTicketPage(HttpServletRequest request,
				HttpServletResponse response, String billNo,String orderReturnFlag,PageHelper helper, String method) throws Exception {
			ModelAndView mav = new ModelAndView();
			mav.addObject("billNo", billNo);
			if(StringUtil.isNotBlank(orderReturnFlag)){
				mav.setViewName("orderReturn/exportTicket");
			}else{
				mav.setViewName("orderTicket/exportTicket");
			}
			return mav;
	
			//return null;
	 }
	
	/***
	 *根据调整单号查询id号 
	 ***/
	private Long selectOrderSettleBillOfIdByBillNo(String billNo) {
		
		Long id = null;
		
		OrderSettleBillExample example = new OrderSettleBillExample();	
		OrderSettleBillExample.Criteria criteria = example.or();

		if(StringUtil.isNotBlank(billNo)){
			criteria.andBillNoEqualTo(billNo);
		}
		
		List<OrderSettleBill> list = orderSettleBillMapper.selectByExample(example);
		
		if(StringUtil.isNotNullForList(list)){
			id = list.get(0).getId();
		}
		
		return id;
	}
	
	/**
	 *取所有配送方式 
	 ***/
	@RequestMapping(value = "getSystemShipping.spmvc")
	public ModelAndView getSystemShipping(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ModelAndView mav = new ModelAndView();
			
			try {
				List<SystemShipping> list  = orderSettleBillService.getSystemShipping();
				writeObject(list, response);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询配送方式列表异常 ", e);
			}

			return null;
	 }
	
	/***
	 * 上传到mq 
	 ***/
	@RequestMapping(value = "performActiveMQ.spmvc")
	public ModelAndView performActiveMQ(HttpServletRequest request,
				HttpServletResponse response, String ids) throws Exception {

		   JsonResult jsonResult = new JsonResult();
		
			String [] id =  ids.split(",");
			
			List<String> billNoList = new ArrayList<String>();
				
			for(String billNo: id){
			   if(StringUtil.isNotBlank(billNo)){
				  billNoList.add(billNo);
				  
				  //开始结算的单子都变更状态为同步中
				  OrderBillList record = new OrderBillList();
				  record.setIsSync((byte)9);//同步中
				  record.setBillNo(billNo);
				  orderSettleBillService.updateOrderBillList(record);
			   }
			}
			
			List<OrderBillList> checkList =  orderSettleBillService.checkDeleteOrderBillListByBillNos(billNoList);
			   
			if(StringUtil.isNotNullForList(checkList)){
			    jsonResult.setIsok(false);
				jsonResult.setMessage("执行失败,执行的队列中有已同步和废除！");
				writeObject(jsonResult, response);
				return  null;
			}
	
			try {
 
				for(String billNo : id){
					
					OrderBillList qbl = new OrderBillList();
					qbl.setExecTime(new Date());
					qbl.setBillNo(billNo);	
					orderSettleBillService.updateOrderBillList(qbl);//修改执行时间
					
					OrderSettleBill qsbObj = new OrderSettleBill();
					qsbObj.setBillNo(billNo);
				 	List<OrderSettleBill>  list = orderSettleBillService.selectOrderSettleBill(qsbObj, null);
					
	                if(StringUtil.isNotNullForList(list)){
	            	   for(OrderSettleBill qsb :list ){
				 			SettleBillQueue sbq = new SettleBillQueue();
							sbq.setId(String.valueOf(qsb.getId()));
							sbq.setBillNo(qsb.getBillNo());
							sbq.setOrderCode(qsb.getOrderCode());
							sbq.setOrderCodeType(Integer.valueOf(qsb.getOrderCodeType()));
							sbq.setMoney(qsb.getMoney().doubleValue());
							sbq.setShippingId(qsb.getShippingId());
							sbq.setReturnPay(qsb.getReturnPay().intValue());
	
							String msg = JSON.toJSONString(sbq);
							final String msg1 = msg;
							
							jmsTemplate.send(new MessageCreator() {
								@Override
								public Message createMessage(Session session) throws JMSException {
									TextMessage message = session.createTextMessage();
									message.setText(msg1);
									logger.debug("QUEUE_SETTLE_BILL_BATCH-put message to settlement Queue:"+msg1);
									return session.createTextMessage(msg1);
								}
							});
							logger.debug("订单结算" +billNo+ "添加至MQ队列任务QUEUE_SETTLE_BILL_BATCH中");
							
				 	  }
	               }
						 	  
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
		    jsonResult.setIsok(true);
			jsonResult.setMessage("执行成功！");
			writeObject(jsonResult, response);
			return null;
	 }
	
	/**
	 *  删除订单结算单 
	 ***/
	@RequestMapping(value = "deleteOrderSettleBill.spmvc")
	public ModelAndView deleteOrderSettleBill(HttpServletRequest request,
				HttpServletResponse response, OrderBillList orderBillList,String billNos) throws Exception {
		
		   JsonResult jsonResult = new JsonResult();
	
		  String [] billNoArray  = billNos.split(",");

		  List<String> billNoList = new ArrayList<String>();
		
		  for(String billNo: billNoArray){
			  if(StringUtil.isNotBlank(billNo)){
				  billNoList.add(billNo);
		      }
		  }
		
		  try{
			  List<OrderBillList> list =  orderSettleBillService.checkDeleteOrderBillListByBillNos(billNoList);
			   
			  if(StringUtil.isNotNullForList(list)){
				  jsonResult.setIsok(false);
				  jsonResult.setMessage("删除失败,删除的队列中有已同步和废除！");
				  writeObject(jsonResult, response);
				  return  null;
			  }
	
			  int num = orderSettleBillService.deleteOrderBillListByBillNos(billNoList);
			  
			  if(0 < num){
				  jsonResult.setIsok(true);
				  jsonResult.setMessage("删除成功！");
				  writeObject(jsonResult, response);
				  return null; 
			  }
			
		  }catch(Exception e){
			  jsonResult.setIsok(false);
			  jsonResult.setMessage("删除失败！");
			  writeObject(jsonResult, response);
			  return null;
		  }

		  jsonResult.setIsok(false);
		  jsonResult.setMessage("删除失败！");
		  writeObject(jsonResult, response);
		
		return null;
	 }
	
	/**
	 * 保存店铺号 
	 ***/
	@RequestMapping(value = "insertOrderBillList.spmvc")
	public ModelAndView insertOrderBillList(HttpServletRequest request,
				HttpServletResponse response, OrderBillList orderBillList) throws Exception {
	
		JsonResult jsonResult = new JsonResult();
		try{

		 //  int num = orderBillListMapper.insertSelective(orderBillList);
		   
			int num = orderSettleBillService.updateOrderBillList(orderBillList);
		   
		   if(0 < num){
			   jsonResult.setIsok(true);
			   jsonResult.setMessage("添加成功！");
			   writeObject(jsonResult, response);
		   }else{
			   jsonResult.setIsok(false);
			   jsonResult.setMessage("添加失败！");
			   writeObject(jsonResult, response);
		   }
		} catch(Exception e){
			  jsonResult.setIsok(false);
			  jsonResult.setMessage("添加失败！");
			  writeObject(jsonResult, response);
			  return null;
		}
		
		return null;
	 }
	
	
	private  String channgeOrderFromFirst(ChannelShop channelShop){
		
	//	  `shop_channel` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0集团店铺，1外部渠道店铺',
		  
	//	  `shop_type` varchar(50) DEFAULT NULL COMMENT '店铺类型 0:自营 1：加盟',

/*		
		{v : '1', n : '线上直营渠道', c : '1'},
		{v : '2', n : '线上加盟渠道', c : '1'},
		{v : '3', n : '线下直营渠道', c : '1'},
		{v : '4', n : '线下加盟渠道', c : '1'}*/
		
		if(0 ==channelShop.getShopChannel() && "0".equals(channelShop.getShopType())){
			return "3";//线下直营渠道
		} else if(0 ==channelShop.getShopChannel() && "1".equals(channelShop.getShopType())){
			return "4";//线下加盟渠道
		} else if(1 ==channelShop.getShopChannel() && "0".equals(channelShop.getShopType())){
			return "1"; //线上直营渠道
		} else if(1 ==channelShop.getShopChannel() && "1".equals(channelShop.getShopType())){
			return "2";  //线上加盟渠道
		} 
	
		return "";
		
	}
	
	
	@RequestMapping(value="orderReturnSettleExportCsv")
	public String orderReturnSettleExportCsv(HttpServletRequest request,HttpServletResponse response,OrderSettleBill orderSettleBill,String billType){
		InputStream is = null;
		BufferedWriter writer = null;
		
		JsonResult jsonResult = new JsonResult();
		logger.debug("创建导出退单结算CSV文件开始！");
		try{
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
				List<OrderSettleBill> orderSettleBillList=orderSettleBillService.selectOrderSettleBill(orderSettleBill, null);
				if(CollectionUtils.isEmpty(orderSettleBillList)){
					logger.debug("创建导出退单结算CSV文件:单据批次号对应批次结算单据表-详细数据不存在！");
				}
				//创建本地文件
				String dateStr = TimeUtil.format3Date(new Date());
				String folderName = "orTempFile"; 
				String sfileName ="orderSettlt" +dateStr+".csv";
				String fileName = folderName + "/"+sfileName;
				File fileRoot = new File(request.getSession().getServletContext().getRealPath("/") + folderName);
				if(!fileRoot.exists()){
					fileRoot.mkdirs();
				}
				String path=request.getSession().getServletContext().getRealPath("/") + fileName;
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path),"GBK");
				writer=new BufferedWriter(write);
				//导出CVS目录
				StringBuffer sb = new StringBuffer();
				if(StringUtil.isNotBlank(billType)&&Integer.valueOf(billType)==3){
					sb.append(
							"单据批次号," + 
							"退单号," + 
							"退款方式," +  
						//	"结算金额,"+
							"处理结果," + "\r\n"
							);
				}else{
					sb.append(
							"单据批次号," + 
							"订单号," + 
						//	"结算金额,"+
							"处理结果," + "\r\n"
							);
				}
				writer.write(sb.toString());
				writer.flush();
				for (OrderSettleBill bill : orderSettleBillList) {
					   sb=new StringBuffer();
					   if(StringUtil.isNotBlank(billType)&&Integer.valueOf(billType)==3){
						   sb.append("\t"+bill.getBillNo()+"," +
						   		""+bill.getOrderCode()+"," +
						   				""+bill.getReturnPay()+"," +
						   				//		""+bill.getMoney()+"," +
						   								""+bill.getResultMsg()+"\r\n");
					   }else{
									   sb.append("\t"+bill.getBillNo()+",\t"
								   +bill.getOrderCode()+","
									//		   +bill.getMoney()+","
								   +bill.getResultMsg()+"\r\n");
					   }
					   writer.write(sb.toString());
					   writer.flush();
				}
				try{
					is = new FileInputStream(path);
					String ftpFileName = StringUtil.fileNameSpliceCsv("orderSettlt", adminUser.getUserName());
					String tFtpPath = ftpRootPath + "/"+ TimeUtil.format2Date(new Date());
					HashMap<String, Object> soMap = FtpUtil.uploadFile(ftpFileName, is, tFtpPath +"/");
					if((Boolean)soMap.get("isok")) {
						Map<String,Object> map = new HashMap<String, Object>();
						String ftpPath = (String)soMap.get("path");
						map.put("path", ftpPath);
						map.put("fileName", ftpFileName);
						jsonResult.setIsok(true);
						jsonResult.setMessage("创建导出退单结算CSV文件成功！");
						jsonResult.setData(map);
					} else {
						jsonResult.setIsok(false);
						jsonResult.setData("");
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
							logger.error("关闭流文件【InputStream】异常", e);
						}
					}
					if(writer != null){
						writer.close();
						writer = null;
					}
				}
				
			}else{
				logger.debug("创建导出退单结算CSV文件:单据批次号不能为空！");
				jsonResult.setIsok(false);
				jsonResult.setMessage("创建导出退单结算CSV文件:单据批次号不能为空！");
			}
		}catch(Exception e){
			logger.error("创建导出退单结算CSV文件出错！");
			jsonResult.setIsok(false);
			jsonResult.setMessage("创建导出退单结算CSV文件出错！");
		}
		writeObject(jsonResult, response);
		return null;
	}
	
	@RequestMapping(value = "checkSettleList")
	public String checkSettleList(HttpServletRequest request,HttpServletResponse response,OrderSettleBill orderSettleBill,String ids){
			
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(true);
		jsonResult.setMessage("结算单校验成功！");
		if(StringUtil.isEmpty(ids)){
			jsonResult.setIsok(false);
			jsonResult.setMessage("结算单号为空！");
		}
		String [] id =  ids.split(",");
		
		for(String billNo: id){
			   if(StringUtil.isNotBlank(billNo)){
				   OrderBillList orderBillList= orderBillListMapper.selectByPrimaryKey(billNo);
				   if(orderBillList.getIsSync() != 0){
					   jsonResult.setIsok(false);
					   jsonResult.setMessage("结算单校验失败！有已执行过的结算单：如"+billNo);
					   break ;
				   }
			   }
			}
		writeObject(jsonResult, response);
		return null;
	}
	
	/**
	 * 保证金列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderDepositList.spmvc")
	public ModelAndView orderDepositList(HttpServletRequest request,
			HttpServletResponse response, OrderBillListVo orderSettleBill, PageHelper helper, String method) throws Exception {

		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			//会话中权限
//			Map<String, String>  map = (Map<String, String>)request.getSession().getAttribute(Constant.SYSTEM_RESOURCE);
			//详情
			mav.setViewName("deposit/orderDeposit");
			return mav;
		}
		try {
			//Paging paging = null;
			
			Paging paging =  orderSettleBillService.getOrderDepositList(orderSettleBill, helper);
		//	Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	/**
	 * 进入保证金调整页面 
	 ***/
	@RequestMapping(value = "createOrderDeposit.spmvc")
	public ModelAndView createOrderDeposit(HttpServletRequest request,
				HttpServletResponse response, OrderSettleBill orderSettleBill,String channelCode, PageHelper helper, String method,String orderReturnFlag) throws Exception {
			ModelAndView mav = new ModelAndView();
			if (StringUtil.isNotNull(method) && method.equals("init")) {
				
				if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
					mav.addObject("billNo", orderSettleBill.getBillNo());
				}else{
					String billNo = ConstantsUtil.getSettleBillCode();
					//System.out.println("settleBillCode=    "+settleBillCode);
					mav.addObject("billNo", billNo);
				}
				mav.addObject("isDisabled", "1");//显示
		
				mav.setViewName("deposit/addOrderDeposit");
				return mav;
			}else if( StringUtil.isNotNull(method) && method.equals("update")){
				
				
				mav.addObject("isDisabled", "0");//置灰
				
				mav.addObject("billNo", orderSettleBill.getBillNo());
				
				
				OrderSettleBill orderSettleBillPram = new OrderSettleBill();
				orderSettleBillPram.setBillNo(orderSettleBill.getBillNo());
				List<OrderSettleBill> list = orderSettleBillService.selectOrderSettleBill(orderSettleBillPram,null);
				
				Integer orderType = null;
				if(StringUtil.isNotNullForList(list)){
					orderType = list.get(0).getOrderType();
					mav.addObject("returnSettlementType", orderType);
				}
	
				mav.setViewName("deposit/addOrderDeposit");
		
				return mav;
	
			}

			return null;
	 }
	
	
	/***
	 *跳转导入订单结算 
	 ****/
	@RequestMapping(value = "enterExportDepositPage.spmvc")
	public ModelAndView enterExportDepositPage(HttpServletRequest request,
				HttpServletResponse response, String billNo,String orderReturnFlag,PageHelper helper, String method) throws Exception {
			ModelAndView mav = new ModelAndView();
			mav.addObject("billNo", billNo);
		/*	if(StringUtil.isNotBlank(orderReturnFlag)){
				mav.setViewName("orderReturn/exportTicket");
			}else{*/
			mav.setViewName("deposit/exportDeposit");
		/*	}*/
			return mav;
	
			//return null;
	 }
	
	
	/***
	 * 导入 调整页面 
	 **/
	@RequestMapping(value = "exportDeposit.spmvc", headers = "content-type=multipart/*")
	public ModelAndView exportDeposit(HttpServletRequest request,
				HttpServletResponse response,  @RequestParam MultipartFile myfile, 
			//	int orderType,
				Byte returnSettlementType,
				String billNo
			//	String note
				) throws Exception {
			InputStream inputis = null;
			StringBuffer sb = new StringBuffer("");
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			List<OrderSettleBill> list = new ArrayList<OrderSettleBill>();
			try {
				inputis = myfile.getInputStream();
				list = orderSettleBillService.importOrderDeposit(inputis, sb, billNo,returnSettlementType);
				if(StringUtil.isNotNullForList(list)){
					for(OrderSettleBill orderSettleBill : list){
						int count = orderSettleBillMapper.insertSelective(orderSettleBill);
					}
					OrderBillList orderBillList = new OrderBillList();
					orderBillList.setActionUser(adminUser.getUserName());
					orderBillList.setAddTime(new Date());
					orderBillList.setBillNo(billNo);
					orderBillList.setBillType(4);
					/*orderBillList.setBillType(orderCodeType);
					orderBillList.setBillType(billType);*/
					orderBillList.setIsSync((byte)0);
					/*if(StringUtil.isNotBlank(channelCode)){
						orderBillList.setChannelCode(channelCode);
					}*/
				/*	if(StringUtil.isNotBlank(note)){
						orderBillList.setNote(note);
					}*/
					orderBillListMapper.insertSelective(orderBillList);
					writeMsgSucc(true,"导入成功！",  response);
					return null;
				}
			}catch (Exception e){
				e.printStackTrace();
				writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			} finally {
				if (inputis != null) {
					try {
						inputis.close();
						inputis = null;
					} catch (IOException e) {
						logger.error("exportTicket 关闭写入文件异常", e);
					}
				}
			}
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
	 }
	
	
	/***
	 * 上传到mq 
	 ***/
	@RequestMapping(value = "depositActiveMQ.spmvc")
	public ModelAndView depositActiveMQ(HttpServletRequest request,
				HttpServletResponse response, String ids) throws Exception {

		   JsonResult jsonResult = new JsonResult();
		
			String [] id =  ids.split(",");
			
			List<String> billNoList = new ArrayList<String>();
				
			for(String billNo: id){
			   if(StringUtil.isNotBlank(billNo)){
				  billNoList.add(billNo);
				  
				  //开始结算的单子都变更状态为同步中
				  OrderBillList record = new OrderBillList();
				  record.setIsSync((byte)9);//同步中
				  record.setBillNo(billNo);
				  orderSettleBillService.updateOrderBillList(record);
			   }
			}
			
			List<OrderBillList> checkList =  orderSettleBillService.checkDeleteOrderBillListByBillNos(billNoList);
			   
			if(StringUtil.isNotNullForList(checkList)){
			    jsonResult.setIsok(false);
				jsonResult.setMessage("执行失败,执行的队列中有已同步和废除！");
				writeObject(jsonResult, response);
				return  null;
			}
	
			try {
 
				for(String billNo : id){
					
					OrderBillList qbl = new OrderBillList();
					qbl.setExecTime(new Date());  //执行时间
					qbl.setBillNo(billNo);	 //调整单
					orderSettleBillService.updateOrderBillList(qbl);//修改执行时间
					
					OrderSettleBill qsbObj = new OrderSettleBill();
					qsbObj.setBillNo(billNo);
				 	List<OrderSettleBill>  list = orderSettleBillService.selectOrderSettleBill(qsbObj, null);  //查询批次结算单据表-详细数据
					
	                if(StringUtil.isNotNullForList(list)){
	            	   for(OrderSettleBill qsb :list ){
				 			SettleBillQueue sbq = new SettleBillQueue(); //mq上传类
							sbq.setId(String.valueOf(qsb.getId())); //id
							sbq.setBillNo(qsb.getBillNo());   // 调整单号
							sbq.setOrderCode(qsb.getOrderCode());  //退单或订单
							sbq.setOrderCodeType(Integer.valueOf(qsb.getOrderCodeType())); //单号类型：0订单号/退单号   1外部交易号
					///		sbq.setMoney(qsb.getMoney().doubleValue()); //钱
					//		sbq.setShippingId(qsb.getShippingId());  //配送id
							sbq.setReturnPay(qsb.getReturnPay().intValue());  // 退款方式
							sbq.setReturnSettlementType((byte)qsb.getOrderType().intValue());
	
							String msg = JSON.toJSONString(sbq);
							final String msg1 = msg;
							
							jmsTemplate.send(new MessageCreator() {
								@Override
								public Message createMessage(Session session) throws JMSException {
									TextMessage message = session.createTextMessage();
									message.setText(msg1);
									logger.debug("QUEUE_SETTLE_BILL_BATCH-put message to settlement Queue:"+msg1);
									return session.createTextMessage(msg1);
								}
							});
							logger.debug("订单结算" +billNo+ "添加至MQ队列任务QUEUE_SETTLE_BILL_BATCH中");
							
				 	  }
	               }
						 	  
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
		    jsonResult.setIsok(true);
			jsonResult.setMessage("执行成功！");
			writeObject(jsonResult, response);
			return null;
	 }
	
	
	/**
	 * 保证金列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orderInfoOrOrderReturnLogList.spmvc")
	public ModelAndView orderInfoOrOrderReturnLogList(HttpServletRequest request,
			HttpServletResponse response, OrderBillListVo orderSettleBill, PageHelper helper, String method) throws Exception {

		if (StringUtil.isNotNull(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			//会话中权限
//			Map<String, String>  map = (Map<String, String>)request.getSession().getAttribute(Constant.SYSTEM_RESOURCE);
			//详情
			mav.setViewName("orderInfoOrOrderReturnLog/orderLog");
			return mav;
		}
		try {
			//Paging paging = null;
			
			Paging paging =  orderSettleBillService.getOrderInfoOrOrderReturnLogListList(orderSettleBill, helper);
		//	Paging paging = this.orderInfoService.getOrderInfoPage(orderInfoSerach, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	/**
	 * 进入保证金调整页面 
	 ***/
	@RequestMapping(value = "createOrderInfoOrOrderReturnLog.spmvc")
	public ModelAndView createOrderInfoOrOrderReturnLog(HttpServletRequest request,
				HttpServletResponse response, OrderSettleBill orderSettleBill,String channelCode, PageHelper helper, String method,String orderReturnFlag) throws Exception {
			ModelAndView mav = new ModelAndView();
			if (StringUtil.isNotNull(method) && method.equals("init")) {
				
				if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
					mav.addObject("billNo", orderSettleBill.getBillNo());
				}else{
					String billNo = ConstantsUtil.getSettleBillCode();
					//System.out.println("settleBillCode=    "+settleBillCode);
					mav.addObject("billNo", billNo);
				}
				mav.addObject("isDisabled", "1");//显示
		
				mav.setViewName("orderInfoOrOrderReturnLog/addOrderLog");
				return mav;
			}else if( StringUtil.isNotNull(method) && method.equals("update")){
				
				
				mav.addObject("isDisabled", "0");//置灰
				
				mav.addObject("billNo", orderSettleBill.getBillNo());
				
				
				OrderSettleBill orderSettleBillPram = new OrderSettleBill();
				orderSettleBillPram.setBillNo(orderSettleBill.getBillNo());
				List<OrderSettleBill> list = orderSettleBillService.selectOrderSettleBill(orderSettleBillPram,null);
			
				Integer orderType = null;
				if(StringUtil.isNotNullForList(list)){
					orderType = list.get(0).getOrderType();
					mav.addObject("orderType", orderType);
				}
	
				mav.setViewName("orderInfoOrOrderReturnLog/addOrderLog");
		
				return mav;
	
			}

			return null;
	 }
	
	/***
	 *跳转导入订单退单调整日志 
	 ****/
	@RequestMapping(value = "enterExportOrderInfoOrOrderReturnLogPage.spmvc")
	public ModelAndView enterExportOrderInfoOrOrderReturnLogPage(HttpServletRequest request,
				HttpServletResponse response, String billNo,String orderReturnFlag,PageHelper helper, String method) throws Exception {
			ModelAndView mav = new ModelAndView();
			mav.addObject("billNo", billNo);
			mav.setViewName("orderInfoOrOrderReturnLog/exportLog");
			return mav;
	
	}
	
	/***
	 * 导入订单退单调整日志
	 **/
	@RequestMapping(value = "exportOrderInfoOrOrderReturnLog.spmvc", headers = "content-type=multipart/*")
	public ModelAndView exportOrderInfoOrOrderReturnLog(HttpServletRequest request,
				HttpServletResponse response,  @RequestParam MultipartFile myfile, 
			//	int orderType,
				Byte orderType,
				String billNo
			//	String note
				) throws Exception {
			InputStream inputis = null;
			StringBuffer sb = new StringBuffer("");
			AdminUser adminUser = getLoginUser(request);
			if (null == adminUser) {
				throw new RuntimeException("用户登录信息不存在！");
			}
			List<OrderSettleBill> list = new ArrayList<OrderSettleBill>();
			try {
				inputis = myfile.getInputStream();
				list = orderSettleBillService.importOrderLog(inputis, sb, billNo,orderType);
				if(StringUtil.isNotNullForList(list)){
					for(OrderSettleBill orderSettleBill : list){
						int count = orderSettleBillMapper.insertSelective(orderSettleBill);
					}
					OrderBillList orderBillList = new OrderBillList();
					orderBillList.setActionUser(adminUser.getUserName());
					orderBillList.setAddTime(new Date());
					orderBillList.setBillNo(billNo); //调整单号
					orderBillList.setBillType(5);//订单退单调整单日志
					/*orderBillList.setBillType(orderCodeType);
					orderBillList.setBillType(billType);*/
					orderBillList.setIsSync((byte)0);
					/*if(StringUtil.isNotBlank(channelCode)){
						orderBillList.setChannelCode(channelCode);
					}*/
				/*	if(StringUtil.isNotBlank(note)){
						orderBillList.setNote(note);
					}*/
					orderBillListMapper.insertSelective(orderBillList);
					writeMsgSucc(true,"导入成功！",  response);
					return null;
				}
			}catch (Exception e){
				e.printStackTrace();
				writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			} finally {
				if (inputis != null) {
					try {
						inputis.close();
						inputis = null;
					} catch (IOException e) {
						logger.error("exportTicket 关闭写入文件异常", e);
					}
				}
			}
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
	 }
	
	
	/***
	 *订单退单调整单日志上传到mq 
	 ***/
	@RequestMapping(value = "orderLogActiveMQ.spmvc")
	public ModelAndView orderLogActiveMQ(HttpServletRequest request,
				HttpServletResponse response, String ids) throws Exception {

		   JsonResult jsonResult = new JsonResult();
		
			String [] id =  ids.split(",");
			
			List<String> billNoList = new ArrayList<String>();
				
			for(String billNo: id){
			   if(StringUtil.isNotBlank(billNo)){
				  billNoList.add(billNo);
				  
				  //开始结算的单子都变更状态为同步中
				  OrderBillList record = new OrderBillList();
				  record.setIsSync((byte)9);//同步中
				  record.setBillNo(billNo);
				  orderSettleBillService.updateOrderBillList(record);
			   }
			}
			
			List<OrderBillList> checkList =  orderSettleBillService.checkDeleteOrderBillListByBillNos(billNoList);
			   
			if(StringUtil.isNotNullForList(checkList)){
			    jsonResult.setIsok(false);
				jsonResult.setMessage("执行失败,执行的队列中有已同步和废除！");
				writeObject(jsonResult, response);
				return  null;
			}
			
			AdminUser adminUser = getLoginUser(request);
	
			try {
 
				for(String billNo : id){
					
					OrderBillList qbl = new OrderBillList();
					qbl.setExecTime(new Date());  //执行时间
					qbl.setBillNo(billNo);	 //调整单
					orderSettleBillService.updateOrderBillList(qbl);//修改执行时间
					
					OrderSettleBill qsbObj = new OrderSettleBill();
					qsbObj.setBillNo(billNo);
				 	List<OrderSettleBill>  list = orderSettleBillService.selectOrderSettleBill(qsbObj, null);  //查询批次结算单据表-详细数据
					
	                if(StringUtil.isNotNullForList(list)){
	            	   for(OrderSettleBill qsb :list ){
				 			SettleBillQueue sbq = new SettleBillQueue(); //mq上传类
							sbq.setId(String.valueOf(qsb.getId())); //id
							sbq.setBillNo(qsb.getBillNo());   // 调整单号
							sbq.setOrderCode(qsb.getOrderCode());  //退单或订单
					//		sbq.setOrderCodeType(Integer.valueOf(qsb.getOrderCodeType())); //单号类型：0订单号/退单号   1外部交易号
					///		sbq.setMoney(qsb.getMoney().doubleValue()); //钱
					//		sbq.setShippingId(qsb.getShippingId());  //配送id
						//	sbq.setReturnPay(qsb.getReturnPay().intValue());  // 退款方式
						//	sbq.setReturnSettlementType((byte)qsb.getOrderType().intValue());
							sbq.setOrderCodeType(qsb.getOrderType()); //1.订单2.退单
							sbq.setMessage(qsb.getMessage()); //日志
							sbq.setActionUser(adminUser.getUserName());
							
							String msg = JSON.toJSONString(sbq);
							final String msg1 = msg;
							
							jmsTemplate.send(new MessageCreator() {
								@Override
								public Message createMessage(Session session) throws JMSException {
									TextMessage message = session.createTextMessage();
									message.setText(msg1);
									logger.debug(" OrderSettleBillController.orderLogActiveMQ  QUEUE_SETTLE_BILL_BATCH-put message to settlement Queue:"+msg1);
									return session.createTextMessage(msg1);
								}
							});
							logger.debug("订单退单调整单日志" +billNo+ "添加至MQ队列任务QUEUE_SETTLE_BILL_BATCH中");
							
				 	  }
	               }
						 	  
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
		    jsonResult.setIsok(true);
			jsonResult.setMessage("执行成功！");
			writeObject(jsonResult, response);
			return null;
	 }
	
	/**
	 * 跳转至导入订单价格页面
	 * 
	 **/
	@RequestMapping(value = "gotoOrderMoneyImportPage.spmvc")
	public ModelAndView gotoOrderMoneyImportPage(HttpServletRequest request, HttpServletResponse response,
			String orderSn) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("orderTicket/importOrderMoney");
		return mav;
	}
	
	/**
	 * 导入订单成交价格
	 * 
	 **/
	@RequestMapping(value = "importOrderMoney.spmvc", headers = "content-type=multipart/*")
	public ModelAndView importOrderMoney(
			HttpServletRequest request, HttpServletResponse response,
			 @RequestParam MultipartFile myfile)
			throws Exception {
		InputStream is = myfile.getInputStream();
		StringBuffer sb = new StringBuffer("");
		AdminUser adminUser = getLoginUser(request);
		try {
			List<OrderMoney> list = orderSettleBillService.importOrderMoney(is, sb);
			if ("".equals(sb.toString())) {
				if(null != list && list.size()>0) {
					for(int i=0;i<list.size();i++) {
						list.get(i).setActionUser(adminUser.getUserName());
						//调用dubbo服务的导入 物流问题单
						final OrderMoney param = list.get(i);
						
					/*	orderMoneyjmsTemplate.send(new MessageCreator() {
							public Message createMessage(Session session) throws JMSException {
								TextMessage message = session.createTextMessage();
								message.setText(JSONObject.toJSONString(param));
								return message;
							}
						});*/
					}
				}
			} else {
				writeMsgSucc(false,sb.toString() + ",请检查模版中记录",  response);
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导入订单成交价格异常", e);
			writeMsgSucc(false,sb.toString() + "请检查模版中记录", response);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					logger.error("导入订单成交价格 流关闭异常", e);
					e.printStackTrace();
				}
			}
		}
		writeMsgSucc(true,"导入成功！",  response);
		return null;
	}
	
	
}
