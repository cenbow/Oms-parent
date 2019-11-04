package com.work.shop.oms.service.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.common.bean.OrderSettleBillVO;
import com.work.shop.oms.common.paraBean.OrderMoney;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.OrderAmountAdjustmentMapper;
import com.work.shop.oms.service.OrderAmountAdjustmentService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.united.client.dataobject.User;


@Service("orderAmountAdjustmentService")
public class OrderAmountAdjustmentServiceImpl implements OrderAmountAdjustmentService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private OrderAmountAdjustmentMapper orderAmountAdjustmentMapper;
	@Resource(name = "processOrderMoneyJmsContainer")
	private JmsTemplate orderMoneyjmsTemplate;
	
	public Paging getOrderAmountAdjustmentList(OrderBillVO vo,PageHelper helper){
		Paging paging = new Paging();
		try{
			//拼装查询入参
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("billNo", vo.getBillNo());
			paramMap.put("isSync", vo.getIsSync());
			paramMap.put("billType", "7");//业务类型:0未定义 1订单结算2订单货到付款结算3退单退款方式结算4保证金结算5日志6邦付宝退款结算7订单金额调整 ，这里只查询7
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int orderAmountAdjustmentListCount = orderAmountAdjustmentMapper.getOrderAmountAdjustmentListCount(paramMap);
			List<OrderBillVO> orderAmountAdjustmentList = orderAmountAdjustmentMapper.getOrderAmountAdjustmentList(paramMap);
			//装载返回参数
			paging.setTotalProperty(orderAmountAdjustmentListCount);
			paging.setRoot(orderAmountAdjustmentList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map delOrderAmountAdjustment(HttpServletRequest request, String ids) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：成功  1：失败
		String msg = "失败！";
		try{
			//获取登录用户
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
			String userName = user.getUserName();
			if("".equals(ids)||ids==null){
				msg = "失败！参数获取失败！";
			}else if("".equals(userName)||userName==null){
				msg = "失败！用户信息获取失败！";
			}else{
				//拼装入参
				Map<String,Object> paramMap = new HashMap<String,Object>();
				
				String[] idArray = ids.split(",");
				List<String> paramList =  Arrays.asList(idArray);
				//查询未同步状态以外的调整单数量
				int unFitCount = orderAmountAdjustmentMapper.checkSynCount(paramList);
				if(unFitCount>0){
					msg = "失败！只能删除未同步的调整单！";
				}else{
					paramMap.put("userName", userName);
					paramMap.put("paramList", paramList);
					orderAmountAdjustmentMapper.delOrderAmountAdjustment(paramMap);
					code = "0";
					msg = "成功！";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public void downloadRecord(HttpServletRequest request,
			HttpServletResponse response, String billNo) {
		// TODO Auto-generated method stub
		try{
			//定义文件名
			String fileName = "订单金额调整清单_";
			//拼用户名
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
			String userName = user.getUserName();
			if(!"".equals(userName)||userName!=null){
				fileName+=userName+"_";
			}
			//拼上时间
			String now = DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
			fileName+=now;
			
			//定义导出文件头部
			LinkedHashMap headNameMap = new LinkedHashMap();
			headNameMap.put("1", "单据批次号");
			headNameMap.put("2", "外部交易号");
			headNameMap.put("3", "调整金额");
			headNameMap.put("4", "处理结果");
			
			//获取要导出的数据
			List<OrderSettleBillVO> dataList = orderAmountAdjustmentMapper.getOrderAmountAjustDownloadList(billNo);
			List<Map> exportData = new ArrayList<Map>();
			for(OrderSettleBillVO vo : dataList){
				Map<String,String> row = new LinkedHashMap<String,String>();
				row.put("1", vo.getBillNo()+"\t");
				row.put("2", vo.getOrderCode()+"\t");
				row.put("3", vo.getMoney()+"\t");
				row.put("4", vo.getResultMsg()+"\t");
				exportData.add(row);
			}
			
			//写入response的输出流
			response.reset();
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition","attachment; filename=" + fileName +".csv");
            response.setContentType("application/octet-stream;charset=gbk");
            
			OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(),"GBK");
        	BufferedWriter bw = new BufferedWriter(osw);
        	// 写入文件头部  
            for (Iterator propertyIterator = headNameMap.entrySet().iterator(); propertyIterator.hasNext();) {  
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
                bw.write("\""+ propertyEntry.getValue().toString() + "\"");  
                if (propertyIterator.hasNext()) {  
                	bw.write(",");  
                }  
            }
            //换行
            bw.newLine();
            //写数据
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {   
                LinkedHashMap row = (LinkedHashMap) iterator.next();
                for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {    
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next(); 
                    bw.write("\""+propertyEntry.getValue().toString() + "\"");    
                   if (propertyIterator.hasNext()) {    
                	   bw.write(",");    
                    }    
                }    
                if (iterator.hasNext()) {    
                	bw.newLine();    
                }    
            }
            bw.close();			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Paging getOrdAmoAjuDetailList(String billNo, PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("billNo", billNo);
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int ordAmoAjuDetailListCount = orderAmountAdjustmentMapper.getOrdAmoAjuDetailListCount(billNo);
			List<OrderSettleBillVO> ordAmoAjuDetailList = orderAmountAdjustmentMapper.getOrdAmoAjuDetailList(paramMap);
			//装载返回参数
			paging.setTotalProperty(ordAmoAjuDetailListCount);
			paging.setRoot(ordAmoAjuDetailList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Transactional
	@Override
	public Map doImport(HttpServletRequest request, MultipartFile myfile,
			String note) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		boolean success = false;
		String code = "1";//0:成功 1：失败
		String msg = "失败！";
		String billNo = "";//调整单号
		StringBuffer errorMsg = new StringBuffer("");
		try{
			//获取登录者信息
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
			String userName = user.getUserName();
			if("".equals(userName)||userName==null){
				msg = "失败！登录失效！请重新登录！";
				throw new RuntimeException("用户登录信息不存在！");
			}
			//读取文件
			InputStream is = myfile.getInputStream();
			List<Map<String,String>> dataList = fileToList(is,errorMsg);
			
			if(errorMsg.toString().length()>0){//存在异常数据
				msg = "失败！"+errorMsg.toString();
			}else if(dataList!=null&&checkSame(dataList)){
				msg = "失败！存在相同的外部交易号！";
			}else if(dataList.size()>0){//数据正常  执行插入
				//获取新调整单号
				billNo = ConstantValues.getSettleBillCode();
				if("".equals(billNo)||billNo==null){
					msg = "失败！获取调整单失败！";
					throw new RuntimeException("获取调整单失败！");
				}
				//向order_bill_list表插数据
				//拼装入参
				OrderBillVO obVO = new OrderBillVO();
				obVO.setBillNo(billNo);//调整单号
				obVO.setChannelCode("");//渠道号
				obVO.setBillType("7");//订单金额调整业务
				obVO.setActionUser(userName);//操作人
				obVO.setNote(note);//备注
				obVO.setIsTiming("0");//不是定时任务
				obVO.setIsSync("0");//未同步
				//插入日期放到sql中
				orderAmountAdjustmentMapper.insertOrderBillVO(obVO);
				//向order_settle_bill表插数据
				for(int i=0;i<dataList.size();i++){
					Map<String,String> tempMap = new HashMap<String,String>();
					tempMap = dataList.get(i);
					String orderCode = tempMap.get("orderCode");
					String money = tempMap.get("money");
					OrderSettleBillVO osbVO = new OrderSettleBillVO();
					osbVO.setBillNo(billNo);//调整单号
					osbVO.setOrderCode(orderCode);//外部交易号
					osbVO.setOrderCodeType("1");//参数类型：外部交易号
					osbVO.setShippingId("0");//配送方式
					osbVO.setReturnPay("0");//结算方式（只调整  不结算 这里随便设个值  没有实际用处）
					osbVO.setMoney(money);//结算金额
					osbVO.setResultMsg("");//处理结果
					//添加日期放到sql中
					orderAmountAdjustmentMapper.insertOrderSettleBillVO(osbVO);
				}
				success = true;
				code="0";
				msg="成功！";
			}else{
				msg = "失败！未知异常！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("success", success);
		map.put("code", code);
		map.put("msg", msg);
		map.put("billNo", billNo);
		return map;
	}
	
	/**
	 * 从文件读取数据放入list
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	private List<Map<String,String>> fileToList(InputStream is,StringBuffer errorMsg) throws IOException{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//流转文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		//遍历文件
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
	
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				String orderCode = getValue(cell0);// 订单号/退单号
				
				HSSFCell cell1 = hssfRow.getCell((short) 1);
				String money = getValue(cell1);// 金额

				if (StringUtil.isNotBlank(orderCode) && StringUtil.isNotEmpty(money) ){
					Map<String,String> map = new HashMap<String,String>();
					map.put("orderCode", orderCode.trim());
					map.put("money", money.trim());
					list.add(map);
				} else {
					//订单号/外部交易号，金额必填项为空	
					if (StringUtil.isNotBlank(orderCode) || StringUtil.isNotEmpty(money) ) {
						errorMsg.append("第"+rowNum+"行"+ "[" + "外部交易号：" + orderCode + ";金额：" + money+";");
						errorMsg.append( "] 导入数据不符合模板要求");
						break;
					} else {
						continue;
					}
				}
			}
		}
		return list;
	}
	
	private String getValue(HSSFCell hssfCell) {
		if (null == hssfCell) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			Double temp = hssfCell.getNumericCellValue();
			BigDecimal bd = new BigDecimal(temp);
			return String.valueOf(bd.toString());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	/**
	 * 查询list中是否存在相同退单号,存在则返回true
	 * @param dataList
	 * @return
	 */
	private boolean checkSame(List<Map<String,String>> dataList){
		boolean result = false;
		String orderCodes = "";
		for(Map<String,String> map : dataList){
			String orderCode = map.get("orderCode");
			if(orderCodes.contains(orderCode)){
				result = true;
				break;
			}
			orderCodes+=","+orderCode+",";
		}
		return result;
	}

	@Transactional
	@Override
	public Map doExecute(HttpServletRequest request, String ids) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0:成功 1：失败
		String msg = "失败！";
		try{
			//获取登录者信息
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute(Constant.SESSION_USER_KEY);
			String userName = user.getUserName();
			if("".equals(userName)||userName==null){
				msg = "失败！登录失效！请重新登录！";
				throw new RuntimeException("用户登录信息不存在！");
			}
			//处理调整单号
			String[] idArray = ids.split(",");
			List<String> paramList =  Arrays.asList(idArray);
			//判断是否存在 已同步或已废除的调整单
			int unfitCount = orderAmountAdjustmentMapper.checkUnfitCount(paramList);
			//判断是否存在未同步状态以外状态的调整单
			int synCount = orderAmountAdjustmentMapper.checkSynCount(paramList);
			if(unfitCount>0){
				msg = "失败！存在已同步或已废除的调整单！";
			}else if(synCount>0){
				msg = "失败！只能执行未同步的调整单！";
			}else{
				//更新状态为同步中,更新执行时间和更新时间
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userName", userName);
				paramMap.put("paramList", paramList);
				orderAmountAdjustmentMapper.updateSynStatus(paramMap);
				//遍历待执行调整单列表，把待执行清单放入执行队列
				for(String billNo:idArray){
					List<OrderSettleBillVO> list = orderAmountAdjustmentMapper.getOrderAmountAjustDownloadList(billNo);
					if(StringUtil.isNotNullForList(list)){
						for(OrderSettleBillVO vo:list){
							//调用dubbo服务的导入 物流问题单
							OrderMoney om = new OrderMoney();
							om.setBillNo(vo.getBillNo());
							om.setOrderOutSn(vo.getOrderCode());
							om.setOrderMoney(Double.parseDouble(vo.getMoney()));
							om.setActionUser(userName);
							
							String data = JSON.toJSONString(om);
							final String executeData = data;
							
							orderMoneyjmsTemplate.send(new MessageCreator() {
								public Message createMessage(Session session) throws JMSException {
									TextMessage message = session.createTextMessage();
									logger.info("订单金额调整   待调整数据:"+executeData);
									message.setText(executeData);
									return message;
								}
							});
							logger.info("订单金额调整" +billNo+ "添加至MQ队列任务中");
						}
					}
				}				
				code = "0";
				msg = "成功放入执行队列！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

}
