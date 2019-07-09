package com.work.shop.oms.service;

import java.io.InputStream;
import java.util.List;


import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderBillListVo;
import com.work.shop.oms.bean.OrderSettleBill;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.OrderMoney;
import com.work.shop.oms.utils.PageHelper;


public interface OrderSettleBillService {
	
	/**
	 * 订单结算列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderBillList(OrderBillListVo model, PageHelper helper) throws Exception;
	
	/**
	 * 导入订单结算exl 
	 ****/
	
	List<OrderSettleBill> importOrderSettle(InputStream is, StringBuffer sb, int logType, String settleBillCode,Byte shippingId) throws Exception ;
	
	
	List<OrderSettleBill> selectOrderSettleBill(OrderSettleBill model, PageHelper helper);
	
	List<SystemShipping> getSystemShipping();

	
	Paging getOrderSettleBill(OrderSettleBill model, PageHelper helper) throws Exception;
	
	/**
	 * 根据调整单号数据集查询
	 **/
	List<OrderBillList> selectOrderBillListByBillNoList(List<String> list,List<Byte> isSncList);

	/**
	 * 验证相关调整单号的结算 单
	 ***/
	List<OrderBillList>  checkDeleteOrderBillListByBillNos(List<String> billNoList);
	
	/**
	 *废除调整单 
	 ****/
	int  deleteOrderBillListByBillNos(List<String> billNoList);
	
	int updateOrderBillList(OrderBillList record);
	
	
	
	/**
	 * 保证金列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderDepositList(OrderBillListVo model, PageHelper helper) throws Exception;
	
	
	/**
	 * 导入订单结算exl 
	 ****/
	
	List<OrderSettleBill> importOrderDeposit(InputStream is, StringBuffer sb, String settleBillCode, Byte returnSettlementType) throws Exception ;
	
	/**
	 * 订单退单调整日志列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderInfoOrOrderReturnLogListList(OrderBillListVo model, PageHelper helper) throws Exception;
	
	
	
	/**
	 * 导入订单退单调整单日志exl 
	 ****/
	
	List<OrderSettleBill> importOrderLog(InputStream is, StringBuffer sb, String settleBillCode, Byte orderType) throws Exception ;
	
	public List<OrderMoney> importOrderMoney(InputStream is,StringBuffer sb)throws Exception;
	
}
