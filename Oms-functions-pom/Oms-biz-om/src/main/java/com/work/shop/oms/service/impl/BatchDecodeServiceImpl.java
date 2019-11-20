package com.work.shop.oms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ibm.icu.text.SimpleDateFormat;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.BatchDecodeList;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderBillListExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.BatchDecodeListMapper;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.service.BatchDecodeService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.AdminUser;

@Service
public class BatchDecodeServiceImpl implements BatchDecodeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private OrderBillListMapper orderBillListMapper;
	@Resource
	private BatchDecodeListMapper batchDecodeListMapper;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private JmsTemplate batchDecodeQeueJmsTemplate;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;

	@Override
	public Map doImport(AdminUser adminUser, MultipartFile myfile,String ip) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "1";//0:成功 1：失败
		String msg = "批量导入订单号失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//读取文件  获取订单列表
			InputStream is = myfile.getInputStream();
			Map<String,Object> dataMap = fileToList(is);
			String errorMsg = (String) dataMap.get("errorMsg");
			if(StringUtils.isNotEmpty(errorMsg)){
				msg = errorMsg;
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			List<String> dataList = (List<String>) dataMap.get("list");
			if(dataList==null||!(dataList.size()>0)){
				msg = "没有待解密数据！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			String userName = adminUser.getUserName();//用户名
			String billNo = ConstantValues.getSettleBillCode();//批次号
			if("".equals(billNo)||billNo==null){
				msg = "获取批次号失败！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//向order_bill_list表插数据
			OrderBillList record = new OrderBillList();
			record.setBillNo(billNo);
			record.setChannelCode("");
			record.setBillType(9);//批量解密联系方式业务
			record.setActionUser(userName);//操作人
			record.setIsSync(new Byte("0"));//未同步
			record.setAddTime(new Date());
			orderBillListMapper.insert(record);
			//向batch_decode_list、master_order_action表插数据
			for(String masterOrderSn : dataList){
				MasterOrderAddressInfo info = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
				BatchDecodeList bean = new BatchDecodeList();
				bean.setBillNo(billNo);
				bean.setMasterOrderSn(masterOrderSn);
				//处理联系方式字段
				if(info!=null){
					String mobile = info.getMobile();
					String tel = info.getTel();
					if(!"".equals(mobile)&&mobile!=null){
						bean.setDecodedNumber(mobile);//先存放加密的
					}else if(!"".equals(tel)&&tel!=null){
						bean.setDecodedNumber(tel);//先存放加密的
					}else{
						bean.setDecodedNumber("");	
					}
					masterOrderActionService.insertOrderActionBySn(masterOrderSn,"批量解密联系方式", userName);
				}else{
					bean.setDecodedNumber("");
				}
				batchDecodeListMapper.insert(bean);
			}
			//放入执行队列
			final String executeData = JSON.toJSONString(billNo);
			batchDecodeQeueJmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage();
					message.setText(executeData);
					logger.info("批量解密联系方式数据:"+executeData);
					return session.createTextMessage(executeData);
				}
			});
			code = "0";
			msg = "批量导入订单号成功！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 从文件读取数据放入list
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	private Map fileToList(InputStream is){
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> list = new ArrayList<String>();
		String errorMsg = "";
		try{
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
					String masterOrderSn = (StringUtil.Null2TrimStr(cell0.getStringCellValue())).replace(" ", "");//订单号 
					if(StringUtils.isNotEmpty(masterOrderSn)){
						list.add(masterOrderSn);
					}
				}
			}
		}catch(Exception e){
			errorMsg = "请检查所有数据是否文本格式！";
			e.printStackTrace();
		}
		map.put("errorMsg", errorMsg);
		map.put("list", list);
		return map;
	}

	@Override
	public Paging getBatchDecodeList(PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			OrderBillListExample example = new OrderBillListExample();
			OrderBillListExample.Criteria criteria = example.or();
			criteria.limit(helper.getStart(), helper.getLimit());
			criteria.andBillTypeEqualTo(9);
			example.setOrderByClause("add_time desc");
			//查询
			int listCount = orderBillListMapper.countByExample(example);
			List<OrderBillList> list = orderBillListMapper.selectByExample(example);
			//装载返回参数
			paging.setTotalProperty(listCount);
			paging.setRoot(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map checkDownloadDecodeList(AdminUser adminUser, String billNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "1";//0:成功 1：失败
		String msg = "不符合下载条件！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if("".equals(billNo)||billNo==null){
				msg = "无效的批次号！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			OrderBillList bean = orderBillListMapper.selectByPrimaryKey(billNo);
			if(bean==null){
				msg = "该批次号不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			String uploadPerson = bean.getActionUser();
			String loginPerson = adminUser.getUserName();
			if(!uploadPerson.endsWith(loginPerson)){
				msg = "非本人上传不可以下载！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			Long addData = bean.getAddTime().getTime();
			Long now = new Date().getTime();
			long hours = (now-addData)/(60*60*1000);
			if(hours>=2){
				msg = "超过两小时不允许下载！请重新导入订单号！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			code = "0";
			msg = "符合下载条件！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	

}
