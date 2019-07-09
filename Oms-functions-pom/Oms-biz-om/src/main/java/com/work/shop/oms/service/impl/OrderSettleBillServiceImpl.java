package com.work.shop.oms.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderBillListExample;
import com.work.shop.oms.bean.OrderBillListVo;
import com.work.shop.oms.bean.OrderSettleBill;
import com.work.shop.oms.bean.OrderSettleBillExample;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.bean.SystemShippingExample;
import com.work.shop.oms.common.bean.OrderMoney;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.dao.OrderSettleBillMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.dao.define.OrderInfoSearchMapper;
import com.work.shop.oms.service.OrderSettleBillService;
import com.work.shop.oms.utils.PageHelper;


@Service("orderSettleBillService")
public class OrderSettleBillServiceImpl implements OrderSettleBillService {

	@Resource
	private OrderInfoSearchMapper orderInfoSearchMapper;
	
	@Resource
	private OrderSettleBillMapper orderSettleBillMapper;
	
	@Resource
	private OrderBillListMapper orderBillListMapper;

	@Resource
	private SystemShippingMapper systemShippingMapper;
	
	@Override
	public Paging getOrderBillList(OrderBillListVo orderSettleBill,PageHelper helper) throws Exception {
		
		if(StringUtil.isNotBlank(orderSettleBill.getChannelCode())){
			orderSettleBill.setChannelCode(orderSettleBill.getChannelCode());
		}
		
		if(null != orderSettleBill.getIsSync()){
			orderSettleBill.setIsSync(orderSettleBill.getIsSync());
		}
		
		if(null != orderSettleBill.getBillType()){
			orderSettleBill.setBillType(orderSettleBill.getBillType());
		} 
		
		if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
			orderSettleBill.setBillNo(orderSettleBill.getBillNo());
		}
		
		List<OrderBillListVo> OrderSettleBillList = orderInfoSearchMapper.getOrderBillList(orderSettleBill);
		int count = orderInfoSearchMapper.countOrderBillList(orderSettleBill);
		Paging paging = new Paging(count, OrderSettleBillList);
		return paging;
	}

	@Override
	public List<OrderSettleBill> importOrderSettle(InputStream is, StringBuffer sb, int orderCodeType,String settleBillCode,Byte shippingId)
			throws Exception {

		List<OrderSettleBill> orderSettleBillList = new ArrayList<OrderSettleBill>();

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
		
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
	
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				String orderSn = getValue(cell0);// 订单号
				
				HSSFCell cell1 = hssfRow.getCell((short) 1);
				String money = getValue(cell1);// 金额

				HSSFCell cell2 = hssfRow.getCell((short) 2);
				String returnPay = getValue(cell2);// 支付方式
				
				if (StringUtil.isNotBlank(orderSn) && StringUtil.isNotEmpty(money) ){	
					OrderSettleBill osb = new OrderSettleBill();
					osb.setOrderCode(orderSn);
					osb.setMoney(new BigDecimal(money));	
					if(StringUtil.isNotBlank(returnPay)){
						osb.setReturnPay(Short.valueOf(returnPay));
					}
					osb.setBillNo(settleBillCode); //调整单号
					osb.setOrderCodeType((byte)orderCodeType);
					osb.setAddTime(new Date());
					osb.setShippingId(shippingId);
					orderSettleBillList.add(osb);
				
				} else {//订单号/外部交易号，金额必填项为空	
					if (StringUtil.isNotBlank(orderSn) || StringUtil.isNotEmpty(money) ) {
						sb.append(errorLine + "[" + "OS订单号：" + orderSn + ";金额：" + money+";");
						if(StringUtil.isNotBlank(returnPay)){
							sb.append("支付方式:"+returnPay);
						}
						sb.append( "] 导入数据不符合模板要求");
						break;
					} else {
						continue;
					}
				}

			}
	
		}

		return orderSettleBillList;

	}

	@SuppressWarnings("static-access")
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
	 * 
	 *查询批次结算单据表-详细数据
	 ****/
	public List<OrderSettleBill> selectOrderSettleBill(OrderSettleBill orderSettleBill, PageHelper helper) {
	
		OrderSettleBillExample example = new OrderSettleBillExample();	
		OrderSettleBillExample.Criteria criteria = example.or();
		
		if(null != helper){
			criteria.limit(helper.getStart(), helper.getLimit());
		}

		if(null != orderSettleBill.getId()){
			criteria.andIdEqualTo(orderSettleBill.getId());
		}
		
		if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
			criteria.andBillNoEqualTo(orderSettleBill.getBillNo());
		}
		
		return orderSettleBillMapper.selectByExample(example);
	}
	
	/**
	 * 查询批次结算单据表-详细数据 数量
	 ***/
	public  int countOrderSettleBill(OrderSettleBill orderSettleBill, PageHelper helper) {
		
		OrderSettleBillExample example = new OrderSettleBillExample();	
		OrderSettleBillExample.Criteria criteria = example.or();
		if(null != helper){
			criteria.limit(helper.getStart(), helper.getLimit());
		}
		
		if(null != orderSettleBill.getId()){
			criteria.andIdEqualTo(orderSettleBill.getId());
		}
		
		if(StringUtil.isNotBlank(orderSettleBill.getBillNo())){
			criteria.andBillNoEqualTo(orderSettleBill.getBillNo());
		}
		
		return orderSettleBillMapper.countByExample(example);
	}

	/**
	 *查询配送方式 
	 ****/
	public List<SystemShipping> getSystemShipping() {
		SystemShippingExample example = new SystemShippingExample();	
		SystemShippingExample.Criteria criteria = example.or();
		return systemShippingMapper.selectByExample(example);
	}

	@Override
	public Paging getOrderSettleBill(OrderSettleBill model, PageHelper helper)
			throws Exception {
		
		List<OrderSettleBill> list =  selectOrderSettleBill(model,helper);
		int count =  countOrderSettleBill(model,helper);

		Paging paging = new Paging(count, list);
		return paging;
	}

	/***
	 *根据调整单号数据集和同步状态：已同步，废除查询
	 ***/
	public List<OrderBillList> selectOrderBillListByBillNoList(List<String> billNoList ,List<Byte> isSncList) {
		
		OrderBillListExample example = new OrderBillListExample();	
		OrderBillListExample.Criteria criteria = example.or();

		if(StringUtil.isNotNullForList(billNoList)){
			criteria.andBillNoIn(billNoList);
		}
	
		if(StringUtil.isNotNullForList(isSncList)){
			criteria.andIsSyncIn(isSncList);
		}
		
		return orderBillListMapper.selectByExample(example);
	}

	/***
	 *验证删除的调整单中有已同步和废除 
	 ***/
	public List<OrderBillList>  checkDeleteOrderBillListByBillNos(List<String> billNoList) {

		List<Byte> isSncList = new ArrayList<Byte>();
		isSncList.add((byte)1);
		isSncList.add((byte)2);
	
		List<OrderBillList> list = selectOrderBillListByBillNoList(billNoList,isSncList);
	
		return list;
	}

	/**
	 *根据 
	 ***/
	public int deleteOrderBillListByBillNos(List<String> billNoList) {
		
		int count = 0;

		for(String billNo: billNoList){
			OrderBillList record = new OrderBillList();
			record.setIsSync((byte)2);
			record.setBillNo(billNo);
			int num = deleteOrderBillList(record);
			
			if(0 < num){
				count = count+num;
			}
			
		}

		return count;
	}

	public int deleteOrderBillList(OrderBillList record) {
		
		OrderBillListExample example = new OrderBillListExample();	
		OrderBillListExample.Criteria criteria = example.or();
		
		if(StringUtil.isNotBlank(record.getBillNo())){
			criteria.andBillNoEqualTo(record.getBillNo());
		}
		
		return orderBillListMapper.updateByExampleSelective(record, example);
	}
	
	/**
	 *修改订单结算单 
	 ***/
	public int updateOrderBillList(OrderBillList record) {
		
		OrderBillListExample example = new OrderBillListExample();	
		OrderBillListExample.Criteria criteria = example.or();
		
		if(StringUtil.isNotBlank(record.getBillNo())){
			criteria.andBillNoEqualTo(record.getBillNo());
		}
		
		return orderBillListMapper.updateByExampleSelective(record, example);
	}

	@Override
	public Paging getOrderDepositList(OrderBillListVo model, PageHelper helper)
			throws Exception {
		
	/*	if(StringUtil.isNotBlank(model.getChannelCode())){
			model.setChannelCode(model.getChannelCode());
		}
		*/
		if(null != model.getIsSync()){
			model.setIsSync(model.getIsSync());
		}
		
	/*	if(null != model.getBillType()){
			model.setBillType(model.getBillType());
		} */
		
		if(StringUtil.isNotBlank(model.getBillNo())){
			model.setBillNo(model.getBillNo());
		}
		
		List<OrderBillListVo> OrderSettleBillList = orderInfoSearchMapper.getOrderDepositList(model);
		int count = orderInfoSearchMapper.countOrderDepositList(model);
		Paging paging = new Paging(count, OrderSettleBillList);
		return paging;
	}

    /**
     * 
     **/
	public List<OrderSettleBill> importOrderDeposit(InputStream is,
			StringBuffer sb, String settleBillCode, Byte returnSettlementType) throws Exception {

		List<OrderSettleBill> orderSettleBillList = new ArrayList<OrderSettleBill>();

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
		
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
	
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				String orderSn = getValue(cell0);// 订单号
				
			/*	HSSFCell cell1 = hssfRow.getCell((short) 1);
				String money = getValue(cell1);// 金额

				HSSFCell cell2 = hssfRow.getCell((short) 2);
				String returnPay = getValue(cell2);// 支付方式
*/				
				if (StringUtil.isNotBlank(orderSn)){	
					OrderSettleBill osb = new OrderSettleBill();
					osb.setOrderCode(orderSn); //退单号
				//	osb.setMoney(new BigDecimal(money));	
				/*	if(StringUtil.isNotBlank(returnPay)){
						osb.setReturnPay(Short.valueOf(returnPay));
					}*/
					osb.setBillNo(settleBillCode); //调整单号
					osb.setOrderCodeType((byte)0); //'0订单号/退单号  ,1外部交易号',
					osb.setOrderType(Integer.valueOf(returnSettlementType)); //1,预付款，2保证金',
				//	osb.setOrderType(4);//
					osb.setAddTime(new Date());
				//	osb.setShippingId(shippingId);
					orderSettleBillList.add(osb);
				
				} else {//订单号/外部交易号，金额必填项为空	
					 if (StringUtil.isBlank(orderSn)) {
						sb.append(errorLine + "[" + "退单号： return_sn is null   ;");
					/*	if(StringUtil.isNotBlank(returnPay)){
							sb.append("支付方式:"+returnPay);
						}*/
						sb.append( "] 导入数据不符合模板要求");
						continue;
					//	break;
					} /*else {
						continue;
					}*/
				}

			}
	
		}

		return orderSettleBillList;
	}

	/**
	 * 订单退单调整单日志列表
	 * **/
	public Paging getOrderInfoOrOrderReturnLogListList(OrderBillListVo model,
			PageHelper helper) throws Exception {
	
		if(null != model.getIsSync()){//同步状态
			model.setIsSync(model.getIsSync());
		}

		if(StringUtil.isNotBlank(model.getBillNo())){//调整号
			model.setBillNo(model.getBillNo());
		}
		
		List<OrderBillListVo> OrderSettleBillList = orderInfoSearchMapper.getOrderInfoOrOrderReturnLogListList(model);
		int count = orderInfoSearchMapper.countOrderInfoOrOrderReturnLogListList(model);
		Paging paging = new Paging(count, OrderSettleBillList);
		return paging;
		
	}

	@Override
	public List<OrderSettleBill> importOrderLog(InputStream is,
			StringBuffer sb, String settleBillCode, Byte orderType)
			throws Exception {
		
		List<OrderSettleBill> orderSettleBillList = new ArrayList<OrderSettleBill>();

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
		
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
	
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				String orderSn = getValue(cell0);// 订单号
	
				HSSFCell cell1 = hssfRow.getCell((short) 1);
				String msg = getValue(cell1);//日志
				
				if (StringUtil.isNotBlank(orderSn)){	
					OrderSettleBill osb = new OrderSettleBill();
					osb.setOrderCode(orderSn); //退单号,订单号
			
					osb.setBillNo(settleBillCode); //调整单号
					osb.setOrderCodeType((byte)0); //'0订单号/退单号  ,1外部交易号',
					osb.setOrderType(Integer.valueOf(orderType)); //1,订单号，2.退单号',
					osb.setMessage(msg); //日志
					osb.setAddTime(new Date());
				//	osb.setShippingId(shippingId);
					orderSettleBillList.add(osb);
				
				} else {//订单号/外部交易号，金额必填项为空	
					 if (StringUtil.isBlank(orderSn)) {
						sb.append(errorLine + "[" + "退单号： return_sn is null   ;");
				
						sb.append( "] 导入数据不符合模板要求");
						continue;
				
					} 
				}

			}
	
		}

		return orderSettleBillList;
	}
	
	
	@Override
	public List<OrderMoney> importOrderMoney(InputStream is,
			StringBuffer sb)
			throws Exception {
		
		List<OrderMoney> orderMoneyList = new ArrayList<OrderMoney>();

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
		
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
	
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				String orderSn = getValue(cell0);// 订单号
	
				HSSFCell cell1 = hssfRow.getCell((short) 1);
				String orderOutSn = getValue(cell1);//外部订单号
				
				HSSFCell cell2 = hssfRow.getCell((short) 2);
				double orderMoney =Double.parseDouble(getValue(cell2));//订单金额
				
				if ((StringUtil.isNotBlank(orderSn)||StringUtil.isNotBlank(orderOutSn))&&orderMoney>0){	
					OrderMoney osb = new OrderMoney();
					osb.setOrderSn(orderSn);
					osb.setOrderOutSn(orderOutSn);
					osb.setOrderMoney(orderMoney);
					orderMoneyList.add(osb);
				
				} else {//订单号/外部交易号，金额必填项为空	
					 if (StringUtil.isBlank(orderSn)) {
						sb.append(errorLine + "[" + "退单号： return_sn is null   ;");
				
						sb.append( "] 导入数据不符合模板要求");
						continue;
				
					} 
				}

			}
	
		}

		return orderMoneyList;
	}

}
