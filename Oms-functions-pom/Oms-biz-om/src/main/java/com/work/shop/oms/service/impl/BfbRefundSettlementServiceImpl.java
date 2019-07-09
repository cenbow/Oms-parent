package com.work.shop.oms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.BfbRefundSettlementMapper;
import com.work.shop.oms.service.BfbRefundSettlementService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.SettleBillQueue;
import com.work.shop.united.client.dataobject.User;


@Service("bfbRefundSettlementService")
public class BfbRefundSettlementServiceImpl implements BfbRefundSettlementService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private BfbRefundSettlementMapper bfbRefundSettlementMapper;
	
	@Override
	public Paging getBfbRefundSettlementList(OrderBillVO vo, PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			//拼装查询入参
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("channelCode", vo.getChannelCode());
			paramMap.put("billNo", vo.getBillNo());
			paramMap.put("isSync", vo.getIsSync());
			paramMap.put("billType", "6");//业务类型:0未定义 1订单结算2订单货到付款结算3退单退款方式结算4保证金结算5日志6邦付宝退款结算 ，这里只查询邦付宝退款结算
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int bfbRefundSettlementListCount = bfbRefundSettlementMapper.getBfbRefundSettlementListCount(paramMap);
			List<OrderBillVO> bfbRefundSettlementList = bfbRefundSettlementMapper.getBfbRefundSettlementList(paramMap);
			//装载返回参数
			paging.setTotalProperty(bfbRefundSettlementListCount);
			paging.setRoot(bfbRefundSettlementList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map delSettlement(String ids,HttpServletRequest request) {
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
				int unFitCount = bfbRefundSettlementMapper.checkSynCount(paramList);
				if(unFitCount>0){
					msg = "失败！只能删除未同步的调整单！";
				}else{
					paramMap.put("userName", userName);
					paramMap.put("paramList", paramList);
					bfbRefundSettlementMapper.delSettlement(paramMap);
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
	public Map downloadRecord(HttpServletRequest request,HttpServletResponse response,String billNo) {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			//定义文件名
			String fileName = "邦付宝退款结算_";
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
			headNameMap.put("2", "退单号");
			headNameMap.put("3", "退款方式");
			headNameMap.put("4", "退款金额");
			headNameMap.put("5", "处理结果");
			
			//获取要导出的数据
			List<OrderSettleBillVO> dataList = bfbRefundSettlementMapper.getBfbRefundDownloadList(billNo);
			List<Map> exportData = new ArrayList<Map>();
			for(OrderSettleBillVO vo : dataList){
				Map<String,String> row = new LinkedHashMap<String,String>();
				row.put("1", vo.getBillNo()+"\t");
				row.put("2", vo.getOrderCode()+"\t");
				row.put("3", vo.getReturnPay()+"\t");
				row.put("4", vo.getMoney()+"\t");
				row.put("5", vo.getResultMsg()+"\t");
				exportData.add(row);
			}
			
			map.put("fileName", fileName);
			map.put("headNameMap", headNameMap);
			map.put("exportData", exportData);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Paging getBfbRefSetDetailList(String billNo,PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("billNo", billNo);
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int bfbRefSetDetailListCount = bfbRefundSettlementMapper.getBfbRefSetDetailListCount(billNo);
			List<OrderSettleBillVO> bfbRefSetDetailList = bfbRefundSettlementMapper.getBfbRefSetDetailList(paramMap);
			//装载返回参数
			paging.setTotalProperty(bfbRefSetDetailListCount);
			paging.setRoot(bfbRefSetDetailList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Transactional
	@Override
	public Map doImport(HttpServletRequest request, MultipartFile myfile, String note) {
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
			}else if(checkSame(dataList)){
				msg = "失败！存在重复的退单号！";
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
				obVO.setChannelCode(ConstantValues.HQ01S116);//平台渠道号
				obVO.setBillType("6");//邦付宝退款结算业务
				obVO.setActionUser(userName);//操作人
				obVO.setNote(note);//备注
				obVO.setIsTiming("0");//不是定时任务
				obVO.setIsSync("0");//未同步
				//插入日期放到sql中
				bfbRefundSettlementMapper.insertOrderBillVO(obVO);
				//向order_settle_bill表插数据
				for(int i=0;i<dataList.size();i++){
					Map<String,String> tempMap = new HashMap<String,String>();
					tempMap = dataList.get(i);
					String orderCode = tempMap.get("orderCode");
					String money = tempMap.get("money");
					OrderSettleBillVO osbVO = new OrderSettleBillVO();
					osbVO.setBillNo(billNo);//调整单号
					osbVO.setOrderCode(orderCode);//订单号/退单号
					osbVO.setOrderCodeType("0");//参数类型：订单号/退单号
					osbVO.setOrderType("3");//退单
					osbVO.setShippingId("0");
					osbVO.setReturnPay("3");//结算方式：余额支付
					osbVO.setMoney(money);//结算金额
					osbVO.setResultMsg("");//处理结果
					//添加日期放到sql中
					bfbRefundSettlementMapper.insertOrderSettleBillVO(osbVO);
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
					map.put("money", money);
					list.add(map);
				} else {
					//订单号/外部交易号，金额必填项为空	
					if (StringUtil.isNotBlank(orderCode) || StringUtil.isNotEmpty(money) ) {
						errorMsg.append("第"+rowNum+"行"+ "[" + "订单号/退单号：" + orderCode + ";金额：" + money+";");
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
			int unfitCount = bfbRefundSettlementMapper.checkUnfitCount(paramList);
			//判断是否存在未同步状态以外状态的调整单
			int synCount = bfbRefundSettlementMapper.checkSynCount(paramList);
			if(unfitCount>0){
				msg = "失败！存在已同步或已废除的调整单！";
			}else if(synCount>0){
				msg = "失败！只能执行未同步的调整单！";
			}else{
				//更新状态为同步中,更新执行时间和更新时间
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userName", userName);
				paramMap.put("paramList", paramList);
				bfbRefundSettlementMapper.updateSynStatus(paramMap);
				//遍历待执行调整单列表，把待执行清单放入执行队列
				for(String billNo:idArray){
					List<OrderSettleBillVO> list = bfbRefundSettlementMapper.getBfbRefundDownloadList(billNo);
					if(StringUtil.isNotNullForList(list)){
						for(OrderSettleBillVO vo:list){
							SettleBillQueue sbq = new SettleBillQueue();
							sbq.setId(vo.getId());
							sbq.setBillNo(vo.getBillNo());
							sbq.setOrderCode(vo.getOrderCode());
							sbq.setOrderCodeType(Integer.valueOf(vo.getOrderCodeType()));
							sbq.setMoney(Double.parseDouble(vo.getMoney()));
							sbq.setShippingId(vo.getShippingId().getBytes()[0]);
							sbq.setReturnPay(Integer.valueOf(vo.getReturnPay()));
							String data = JSON.toJSONString(sbq);
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
